package rl;

import Util.Plot;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	             .learningRate(0.1)
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

		deepQNetwork = new DeepQNetwork(conf1,
                100000,
                .9f,
                .01d,
                1024,
                500,
                1024,
                inputLength);
	}


	public void train() {
		initNet();

		List<Double> trainScores = run(100);
        System.out.println("Average training score: " + getAverage(trainScores));
        Plot.createPlot(trainScores, "train scores");
    }

	private static double getAverage(List<Double> list){
        double sum = 0;
        for(double d : list){
            sum += d;
        }
        return sum / list.size();
    }

    private List<Double> run(int n){
        List<Double> scores = new ArrayList<>();
        for(int i = 0; i<n; i++){
            Trainable model = GameLogic.newModel();
            while(!model.hasTerminated()){
                int a = deepQNetwork.getAction(model.encode(), model.getActionMask());
                Model newModel = (Model) model.doMove(a);
                float r = newModel.getReward();
                deepQNetwork.observeReward(r, model.encode(), model.getActionMask());
                model = newModel;
                View.getInstance().update((Model) model);
            }
            scores.add((double) model.getScore());
        }
        return scores;
    }
}
