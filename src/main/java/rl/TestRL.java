package rl;

import controller.GameLogic;
import model.Model;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import view.View;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TestRL<M extends Trainable> {

    private DeepQNetwork deepQNetwork;

    private void initNet(){
		
		int inputLength = 16; // must be equal to the number of values that are
        // inserted using the encode-function from Trainable
		int hiddenLayerCount = 150;

        MultiLayerConfiguration conf1 = new NeuralNetConfiguration.Builder()
       		 .seed(123)
	             .iterations(1)
	             .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
	             .learningRate(0.025)
	             .updater(Updater.NESTEROVS)
	             .list()
	             .layer(0, new DenseLayer.Builder().nIn(inputLength).nOut(hiddenLayerCount)
	            		 	.weightInit(WeightInit.XAVIER)
		                        .activation("relu")
		                        .build())
	             .layer(1, new OutputLayer.Builder(LossFunction.MSE)
	                        .weightInit(WeightInit.XAVIER)
	                        .activation("identity").weightInit(WeightInit.XAVIER)
	                        .nIn(hiddenLayerCount).nOut(4).build())
	             .pretrain(false).backprop(true).build();

		deepQNetwork = new DeepQNetwork(conf1 ,  100000 , .99f , 1d , 1024 , 500 , 1024 , inputLength);
	}


	public void train() {
		initNet();

        System.out.println("Average training score: " + run(100));
        deepQNetwork.setEpsilon(0);
        System.out.println("Average testing score: " + run(100));
	}

    private float run(int n){
        float totalScore = 0;
        for(int i = 0; i<n; i++){
            Trainable model = GameLogic.newModel();
            while(!model.hasTerminated()){
                int a = deepQNetwork.getAction(model.encode(), model.getActionMask());
                Model newModel = (Model) model.doMove(a);
                float r = newModel.getReward();
                try {
                    deepQNetwork.observeReward(r, model.encode(), model.getActionMask());
                } catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("for some reason this error: " + e.getMessage());
                }
                model = newModel;
                View.getInstance().update((Model) model);
            }
            totalScore = model.getScore();
        }
        return totalScore / n;
    }
}
