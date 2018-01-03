import Util.Plot;
import controller.Controller;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import rl4j.MDP2048;
import model.Model;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import view.View;

public class Main {

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) throws Exception{

        Model model = new Model();
        Controller controller = new Controller(model);
        controller.resetModel();

//        View view = View.createView();
//        controller.addObserver(view);
        controller.modelChanged();

//        view.setController(controller);
//        view.addKeyListener(view.getKeyListener());


        DataManager manager = new DataManager(false);
        MDP2048 mdp = new MDP2048(controller);

        final int numRows = 28;
        final int numColumns = 28;
        int outputNum = 16; // number of output classes
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)
                .activation(Activation.RELU)
                .learningRate(0.001)
                .regularization(true).l2(0.001 * 0.005) // regularize learning model
                .list()
                .layer(0, new DenseLayer.Builder() //create the first input layer.
                        .nIn(numRows * numColumns)
                        .nOut(500)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .activation(Activation.SOFTMAX)
                        .nIn(100)
                        .nOut(outputNum)
                        .build())
                .build();
        MultiLayerNetwork mln = new MultiLayerNetwork(conf);
        DQN dqn = new DQN(mln);

        // Learning methods
        Learning<Model, Integer, DiscreteSpace, IDQN> dql =
                new QLearningDiscreteDense<>(mdp, dqn, TOY_QL, manager);
        dql.train();

        //Plot.createPlot(manager);
    }

    public static DQNFactoryStdDense.Configuration TOY_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.001).learningRate(0.01).numHiddenNodes(128).numLayer(2).build();

    public static QLearning.QLConfiguration TOY_QL =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    20000,    //Max step By epoch
                    50000, //Max step
                    150000, //Max size of experience replay
                    100,     //size of batches
                    500,    //target update (hard)
                    0,      //num step noop warmup   - The game only updates after doing a move.
                    1,   //reward scaling
                    0,   //gamma
                    1.0,    //td-error clipping
                    0.1f,   //min epsilon
                    1000,   //num step for eps greedy anneal
                    true    //double DQN
            );
}