package controller;

import model.Model;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.mdp.toy.SimpleToyState;
import org.deeplearning4j.rl4j.space.ActionSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

/**
 * Created by Willem on 11-12-2017.
 */
public class MDP2048 implements MDP<Model, Integer, DiscreteSpace>{

    private Model model;


    public MDP2048(){
    }

    @Override
    public ObservationSpace<Model> getObservationSpace() {
        return null;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return null;
    }

    @Override
    public Model reset() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply<Model> step(Integer integer) {
        // Generate StepReplay
        // return new StepReply(this.model, this.model.score, this.isDone(), new JSONObject("{}"));
        return null;
    }

    @Override
    public boolean isDone() {
        return this.model.win || this.model.lose;
    }

    @Override
    public MDP<Model, Integer, DiscreteSpace> newInstance() {
        return null;
    }
}
