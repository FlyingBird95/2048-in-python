package rl4j;

import Util.MoveUtil;
import controller.Controller;
import model.Model;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;

public class MDP2048 implements MDP<Model, Integer, DiscreteSpace>{

    private Controller controller;
    private MoveSpace moveSpace;
    private ObservationSpace<Model> observationSpace;

    public MDP2048(Controller controller){
        this.controller = controller;
        this.observationSpace = new ObservationSpace2048(this.controller.model);
        this.moveSpace = new MoveSpace(this.controller);
    }

    @Override
    public ObservationSpace<Model> getObservationSpace() {
        return this.observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return this.moveSpace;
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
        MoveUtil.Move move = MoveUtil.getMove(integer);
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
    public MDP2048 newInstance() {
        return new MDP2048(this.controller.clone());
    }
}
