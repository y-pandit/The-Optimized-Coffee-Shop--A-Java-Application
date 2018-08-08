package p2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {
	
	List<Double> x;
	List<Double> pBest;
	List<Double> velocity;
	double fitnessValue;
	
	public Particle(List<Double> initList){
		this.x = initList;
		this.pBest = new ArrayList<Double>();
		this.velocity = new ArrayList<Double>();
		this.fitnessValue = Double.MAX_VALUE;
		for(int i = 0; i<initList.size(); i++){
			double randomVelocity = getRandomVelInRange(0, Double.valueOf(initList.size()));
			this.velocity.add(randomVelocity);	
		}
		
	}
	
	
	public double getRandomVelInRange(double minVel, double maxVel){
		Random r = new Random();
		double range = maxVel - minVel;
		double scaled = r.nextDouble()*range;
		double shifted = scaled + minVel;
		return shifted;
		
	}
	

}
