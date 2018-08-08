package p2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SwarmOptimizer {

	List<Double> gBest;
	List<Customer> customerList;
	List<Particle> particleSet;
	int numberOfIterations, numberOfParticles;
	double omega, theta1, theta2, beta1,beta2,gBestFitnessValue;

	
	
	public SwarmOptimizer(List<Customer> customerList,List<Particle> particleSet){
		this.customerList = customerList;
		this.particleSet = particleSet;
		gBest = new ArrayList<Double>();
		numberOfIterations = 7;
		numberOfParticles = 3;
		omega = 0.6;
		theta1 = theta2 =0.2;
		beta1 = 0.3;
		beta2 = 0.5;
		gBestFitnessValue = Double.MAX_VALUE;
		//initializeParticleSet();
	}
	
   /* public void initializeParticleSet(){
    	ArrayList<List<Double>> sampleParticleLists = new ArrayList<List<Double>>();
    	int numberOfCustomers = customerList.size();
    	List<Double> arrayOfCustomerIndices = new ArrayList<>();
    	for(int j = 0; j<customerList.size(); j++){
    		arrayOfCustomerIndices.add((double) (j+1));
    	}
    	
    	
    	
    	while(sampleParticleLists.size()<numberOfCustomers){
    		List<Double> newList = shuffleCustomerList(arrayOfCustomerIndices);
    		if(!sampleParticleLists.contains(newList)){
    			Particle p = new Particle(newList);
    			particleSet.add(p);
    			sampleParticleLists.add(newList);
    		}
    	}
    }
	
	public List<Double> shuffleCustomerList(List<Double> arrayOfCustomerIndices){
		for(int i = 0; i < arrayOfCustomerIndices.size(); i++){
			int r = (int) (Math.random() * (i+1));
			double swap = arrayOfCustomerIndices.get(r);
			arrayOfCustomerIndices.set(r, arrayOfCustomerIndices.get(i));
			arrayOfCustomerIndices.set(i, swap);
		}
		
		return arrayOfCustomerIndices;
	}*/
	
	public List<Integer> getSequence(List<Double> x){
		
		Comparator<Map.Entry<Integer,Double>> comparator = new Comparator<Map.Entry<Integer,Double>>(){

			@Override
			public int compare(Entry<Integer, Double> arg0, Entry<Integer, Double> arg1) {
				// TODO Auto-generated method stub
				Double d1 = arg0.getValue();
				Double d2 = arg1.getValue();
				int val = d1.compareTo(d2);
				return val;
			}
			
			
		};
		
		Map<Integer,Double> map = new HashMap<>();
		for(int i=1;i<=x.size();i++){
			map.put(i, x.get(i-1));
		}
		
		List<Map.Entry<Integer, Double>> custMapList = new ArrayList<>(map.entrySet());
		Collections.sort(custMapList,comparator);
		
		List<Integer> finalList = new ArrayList<>();
		for(Map.Entry<Integer, Double> entry: custMapList){
			finalList.add(entry.getKey());
		}
		return finalList;
	}
	
	
	public void fitnessValueCalculator(){
		for(int i =0; i<particleSet.size(); i++){
			List<Integer> finalSequence = getSequence(particleSet.get(i).x);
			double fitnessTime = getFitnessValue(finalSequence);
			if(fitnessTime<particleSet.get(i).fitnessValue){
				particleSet.get(i).fitnessValue = fitnessTime;
				particleSet.get(i).pBest = particleSet.get(i).x;
			}
			
		}
	}
	
	public double getFitnessValue(List<Integer> finalSequence){
		
		Double time =0.0;
		for(int i=0;i<finalSequence.size();i++){
			System.out.println(customerList);
			Customer c =  customerList.get((finalSequence.get(i))-1);
			System.out.println("customer index: "+(finalSequence.get(i)-1));
			for(Food item: c.getOrder()){
				time += item.cookTimeMS;
			}
		}
		
		double cookPlatingTime = getRandomVelInRange(200, 500);
		return time+cookPlatingTime;
	}
	
	public double getRandomVelInRange(double minVel, double maxVel){
		Random r = new Random();
		double range = maxVel - minVel;
		double scaled = r.nextDouble()*range;
		double shifted = scaled + minVel;
		return shifted;
		
	}
	
	public void updateSwarmGBest(){
		
		for(int i = 0; i < particleSet.size(); i++){
			if(particleSet.get(i).fitnessValue<gBestFitnessValue){
				gBestFitnessValue = particleSet.get(i).fitnessValue;
				gBest = particleSet.get(i).x;
			}
		}
		
	}
	
	
	public void updateParticleVelocity(){
		for(int i=0; i<particleSet.size(); i++){
			List<Double> currentVelocity = particleSet.get(i).velocity;
			List<Double> x = particleSet.get(i).velocity;
			List<Double> newVelocity = new ArrayList<Double>();
			for(int j = 0; j<currentVelocity.size(); j++){
				double memberVelocity = omega * (currentVelocity.get(j)) + theta1 * beta1 * (particleSet.get(i).pBest.get(j)-x.get(j))+ theta2 * beta2 * (gBest.get(j)-x.get(j));
				newVelocity.add(memberVelocity);
			}
			particleSet.get(i).velocity= newVelocity;
		}
	}
	
	
	public void updateParticleX(){
		for(int i = 0; i<particleSet.size(); i++){
			List<Double> x = particleSet.get(i).x;
			List<Double> newX = new ArrayList<Double>();
			List<Double> velocity = particleSet.get(i).velocity;
			for(int j = 0; j< x.size(); j++){
				Double memberX = x.get(j)+velocity.get(j);
				if(memberX > x.size()){
					memberX = (double) x.size();
				}
				newX.add(memberX);
			}
			particleSet.get(i).x = newX;
		}
	
	}
	
	public void optimizationIterator(){
		for(int i = 0; i< numberOfIterations ; i++){
			
			fitnessValueCalculator();
			
			updateSwarmGBest();
			
			updateParticleVelocity();
			
			updateParticleX();
		}
		
	}
	
	
	
	
}
