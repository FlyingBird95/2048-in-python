package controller;

import model.Model;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class MDP2048 implements MDP<Model, Integer, DiscreteSpace>{

    private Controller controller;
    private DiscreteSpace actionSpace;
    private ObservationSpace<Model> observationSpace;

    public MDP2048(Controller controller){
        this.controller = controller;
        this.actionSpace = new DiscreteSpace(this.controller.model.moveList.length);
        this.observationSpace = new ObservationSpace<Model>() {
            @Override
            public String getName() {
                return "Custom";
            }

            @Override
            public int[] getShape() {
                return new int[]{controller.model.toArray().length};
            }

            @Override
            public INDArray getLow() {
                return Nd4j.create(1);
            }

            @Override
            public INDArray getHigh() {
                return Nd4j.create(1);
            }
        };
    }

    @Override
    public ObservationSpace<Model> getObservationSpace() {
        return this.observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public Model reset() {
        this.controller.resetModel();
        return this.controller.model;
    }

    @Override
    public void close() {
    }

    @Override
    public StepReply<Model> step(Integer integer) {
        Controller.Move move = Controller.getMove(integer);
        controller.doMove(move);
        return new StepReply<>(
                this.controller.model,
                this.controller.model.previousReward,
                this.isDone(),
                new JSONObject("{}")
        );
    }

    @Override
    public boolean isDone() {
        return this.controller.model.win || this.controller.model.lose;
    }

    @Override
    public MDP<Model, Integer, DiscreteSpace> newInstance() {
        return new MDP2048(this.controller);
    }
}
