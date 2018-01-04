import Util.Plot;
import controller.GameLogic;
import model.Model;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import rl4j.MDP2048;

public class Main {

    public static void main(String[] args) throws Exception{
        DataManager manager = new DataManager(true);

        MDP2048 mdp = new MDP2048(GameLogic.newModel());

        Learning<Model, Integer, DiscreteSpace, IDQN> dql =
                new QLearningDiscreteDense<>(mdp, TOY_NET, TOY_QL, manager);
        dql.train();

        //Plot.createPlot(manager);
    }

    private static DQNFactoryStdDense.Configuration TOY_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.000).learningRate(0.025).numHiddenNodes(256).numLayer(2).build();

    private static QLearning.QLConfiguration TOY_QL =
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