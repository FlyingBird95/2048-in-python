package rl;

import org.nd4j.linalg.api.ndarray.INDArray;

class Replay {

	INDArray input;
	int action;
	float reward;
	INDArray nextInput;
	int nextActionMask[];
	
	Replay(INDArray input , int action , float reward , INDArray nextInput , int nextActionMask[]){
		this.input = input;
		this.action = action;
		this.reward = reward;
		this.nextInput = nextInput;
		this.nextActionMask = nextActionMask ;
	}
	
}
