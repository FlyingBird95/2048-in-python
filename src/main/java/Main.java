//import model.Model;
//import view.View;

import controller.Controller;
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

        int modelSize = 4;
        Model model = new Model(modelSize);
        Controller controller = new Controller(model, modelSize);
        controller.resetModel();

        View view = View.createView(modelSize);
        controller.addObserver(view);
        controller.modelChanged();

        view.setController(controller);

        view.addKeyListener(view.getKeyListener());


        DataManager manager = new DataManager();

        MDP2048 mdp = new MDP2048(controller);

        // Learning methods
        Learning<Model, Integer, DiscreteSpace, IDQN> dql =
                new QLearningDiscreteDense<Model>(mdp, TOY_NET, TOY_QL, manager);
        dql.train();
    }

    public static DQNFactoryStdDense.Configuration TOY_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.01).learningRate(1e-2).numLayer(3).numHiddenNodes(16).build();

    public static QLearning.QLConfiguration TOY_QL =
            new QLearning.QLConfiguration(
                    123,   //Random seed
                    100000,//Max step By epoch
                    80000, //Max step
                    10000, //Max size of experience replay
                    32,    //size of batches
                    100,   //target update (hard)
                    0,     //num step noop warmup
                    0.05,  //reward scaling
                    0.99,  //gamma
                    10.0,  //td-error clipping
                    0.1f,  //min epsilon
                    2000,  //num step for eps greedy anneal
                    true   //double DQN
            );
}