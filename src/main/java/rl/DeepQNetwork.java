package rl;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeepQNetwork {

	private int replayMemoryCapacity;
	private List<Replay> replayMemory;

	private double epsilon;
	private float discount;
	private MultiLayerNetwork deepQ;
	private MultiLayerNetwork targetDeepQ;
	private int batchSize;
	private int updateFreq;
	private int updateCounter;
	private int replayStartSize;
	private Random random;
	
	private int inputLength;

	private INDArray lastInput;
	private int lastAction;
	
	DeepQNetwork(MultiLayerConfiguration conf, int replayMemoryCapacity, float discount,
				 double epsilon, int batchSize, int updateFreq, int replayStartSize, int inputLength){
		deepQ = new MultiLayerNetwork(conf);
		deepQ.init();
		targetDeepQ = new MultiLayerNetwork(conf);
		targetDeepQ.init();
		targetDeepQ.setParams(deepQ.params());

		this.replayMemoryCapacity = replayMemoryCapacity;
		this.epsilon = epsilon;
		this.discount = discount;
		random = new Random();
		this.batchSize = batchSize;
		this.updateFreq = updateFreq;
		updateCounter = 0;
		replayMemory = new ArrayList<>();
		this.replayStartSize = replayStartSize;
		this.inputLength = inputLength;
	}
	
	void setEpsilon(double e){
		epsilon = e;
	}
	
	private void addReplay(float reward , INDArray nextInput , int nextActionMask[]){
		if( replayMemory.size() >= replayMemoryCapacity)
			replayMemory.remove( random.nextInt(replayMemory.size()) );
		replayMemory.add(new Replay(lastInput, lastAction, reward , nextInput , nextActionMask));
	}
	
	private Replay[] getMiniBatch(int batchSize){
		int size = replayMemory.size() < batchSize ? replayMemory.size() : batchSize ;
		Replay[] retVal = new Replay[size];
		
		for(int i = 0 ; i < size ; i++){
			retVal[i] = replayMemory.get(random.nextInt(replayMemory.size()));
		}
		return retVal;
		
	}
	
	private float findMax(INDArray netOutputs , int actionMask[]){
		int i = 0;
		while(actionMask[i] == 0) i++;
		
		float maxVal = netOutputs.getFloat(i);
		for(; i < netOutputs.size(1) ; i++){
			if(netOutputs.getFloat(i) > maxVal && actionMask[i] == 1){
				maxVal = netOutputs.getFloat(i);
			}
		}
		return maxVal;
	}
	
	private int findActionMax(INDArray netOutputs , int actionMask[]){
		int i = 0;
		while(actionMask[i] == 0) i++;
		
		float maxVal = netOutputs.getFloat(i);
		int maxValI = i;
		for(; i < netOutputs.size(1) ; i++){
			if(netOutputs.getFloat(i) > maxVal && actionMask[i] == 1){
				maxVal = netOutputs.getFloat(i);
				maxValI = i;
			}
		}
		return maxValI;
	}
	
	
	int getAction(INDArray inputs , int actionMask[]){
		lastInput = inputs;
		INDArray outputs = deepQ.output(inputs);
		System.out.print(outputs + "ok ");
		if(epsilon > random.nextDouble()) {
			 lastAction = random.nextInt(outputs.size(1));
			 while(actionMask[lastAction] == 0)
				 lastAction = random.nextInt(outputs.size(1));
			 System.out.println(lastAction);
			 return lastAction;
		}
		
		lastAction = findActionMax(outputs , actionMask);
		System.out.println(lastAction);
		return lastAction;
	}
	
	void observeReward(float reward , INDArray nextInputs , int nextActionMask[]){
		addReplay(reward , nextInputs , nextActionMask);
		if(replayStartSize <  replayMemory.size())
			trainNetwork(batchSize);
		updateCounter++;
		if(updateCounter == updateFreq){
			updateCounter = 0;
			System.out.println("Reconciling Networks");
			reconcileNetworks();
		}
	}
	
	private INDArray combineInputs(Replay replays[]){
		INDArray retVal = Nd4j.create(replays.length , inputLength);
		for(int i = 0; i < replays.length ; i++){
			retVal.putRow(i, replays[i].input);
		}
		return retVal;
	}
	
	private INDArray combineNextInputs(Replay replays[]){
		INDArray retVal = Nd4j.create(replays.length , inputLength);
		for(int i = 0; i < replays.length ; i++){
			if(replays[i].nextInput != null)
				retVal.putRow(i, replays[i].nextInput);
		}
		return retVal;
	}

	private void trainNetwork(int batchSize){
		Replay replays[] = getMiniBatch(batchSize);
		INDArray CurrInputs = combineInputs(replays);
		INDArray TargetInputs = combineNextInputs(replays);

        INDArray CurrOutputs = deepQ.output(CurrInputs);
		INDArray TargetOutputs = targetDeepQ.output(TargetInputs);
		for(int i = 0 ; i < replays.length; i++){
			int ind[] = { i , replays[i].action};
			float FutureReward = 0 ;
			if(replays[i].nextInput != null)
				FutureReward = findMax(TargetOutputs.getRow(i) , replays[i].nextActionMask);
			float TargetReward = replays[i].reward + discount * FutureReward ;
            CurrOutputs.putScalar(ind , TargetReward ) ;
		}
		deepQ.fit(CurrInputs, CurrOutputs);
	}
	
	private void reconcileNetworks(){
		targetDeepQ.setParams(deepQ.params());
	}
	
	public boolean saveNetwork(String paramFileName , String jsonFileName){
	    //Write the network parameters:
	    try(DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(paramFileName)))){
	        Nd4j.write(deepQ.params(),dos);
	    } catch (IOException e) {
	    	System.out.println("Failed to write params");
			return false;
		}
	    
	    //Write the network configuration:
	    try {
			FileUtils.write(new File(jsonFileName), deepQ.getLayerWiseConfigurations().toJson());
		} catch (IOException e) {
			System.out.println("Failed to write json");
			return false;
		}
	    return true;
	}
	
	public boolean loadNetwork(String paramFileName , String jsonFileName){
		//Load network configuration from disk:
	    MultiLayerConfiguration confFromJson;
		try {
			confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File(jsonFileName)));
		} catch (IOException e1) {
			System.out.println("Failed to load json");
			return false;
		}

	    //Load parameters from disk:
	    INDArray newParams;
	    try(DataInputStream dis = new DataInputStream(new FileInputStream(paramFileName))){
	        newParams = Nd4j.read(dis);
	    } catch (IOException e) {
	    	System.out.println("Failed to load parems");
			return false;
		}

	    //Create a MultiLayerNetwork from the saved configuration and parameters 
	    deepQ = new MultiLayerNetwork(confFromJson);
	    deepQ.init();
	    deepQ.setParameters(newParams);
	    reconcileNetworks();
	    return true;
	    
	}
	
	
}
