package rl4j;

import Util.MoveUtil;
import controller.GameLogic;
import model.Model;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import view.View;

public class MDP2048 implements MDP<Model, Integer, DiscreteSpace> {

    private Model model;

    public MDP2048(Model model){
        this.model = model;
    }

    @Override
    public ObservationSpace<Model> getObservationSpace() {
        return new ObservationSpace2048();
    }

    @Override
    public MoveSpace getActionSpace() {
        return new MoveSpace();
    }

    @Override
    public Model reset() {
        this.model = GameLogic.newModel();
        return this.model;
    }

    @Override
    public void close() {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public StepReply<Model> step(Integer action) {
        Model newModel = GameLogic.doMove(model, MoveUtil.getMove(action));
        GameLogic.addTile(newModel);
        View.getInstance().update(newModel);
        model = newModel;
        return new StepReply<>(newModel,
                GameLogic.getReward(newModel),
                GameLogic.isDone(newModel),
                new JSONObject("{}"));
    }

    @Override
    public boolean isDone() {
        return GameLogic.isDone(model);
    }

    @Override
    public MDP<Model, Integer, DiscreteSpace> newInstance() {
        return new MDP2048(GameLogic.newModel());
    }
}
