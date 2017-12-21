package rl;

import controller.Controller;
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

public class TestRL<M extends Trainable> {

    private DeepQNetwork deepQNetwork;
	private int size = 4;

    private void initNet(){
		
		int inputLength = 16; // must be equal to the number of values that are
        // inserted using the toArray-function from Trainable
		int hiddenLayerCount = 150;

        MultiLayerConfiguration conf1 = new NeuralNetConfiguration.Builder()
       		 .seed(123)
	             .iterations(1)
	             .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
	             .learningRate(0.0025)
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
		initNet() ;

        /*
         * I guess this is for training
         */
        for(int m = 0; m < 1500; m++){
			System.out.println("Episode: " + m) ;

            for(int i = 0 ; i < 2*size ; i ++){
                Trainable model = Controller.getInstance().getModel();
				int a = deepQNetwork.getAction(model.toArray(), model.getActionMask()) ;
                Model newModel = (Model) model.doMove(a);
				float r = newModel.getReward();
                if(newModel.hasTerminated()){
					deepQNetwork.observeReward(r, null , newModel.getActionMask());
                    Controller.getInstance().setModel(GameLogic.newModel());
					break;
				}
				deepQNetwork.observeReward(r, newModel.toArray(), newModel.getActionMask());
				Controller.getInstance().setModel(newModel);
			}
		}


        /*
         * I guess this is for testing
         */
        deepQNetwork.setEpsilon(0); // this implies to not train anymore
		for(int m = 0 ; m < 100 ; m++){
            float tReward = 0;
			while(true){
				int a = deepQNetwork.getAction(Controller.getInstance().getModel().toArray(),
                        Controller.getInstance().getModel().getActionMask()) ;
				Model newModel = (Model) Controller.getInstance().getModel().doMove(a);
				float r = newModel.getReward();
				tReward += r;
                deepQNetwork.observeReward(r, newModel.toArray() , newModel.getActionMask());
				if(newModel.hasTerminated()) {
                    break;
                }
				Controller.getInstance().setModel(newModel);
			}
			System.out.println("Net Score: " + (tReward));
		}
	}
}
