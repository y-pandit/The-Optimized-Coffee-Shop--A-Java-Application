package p2;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * Simulation is the main class used to run the simulation.  You may
 * add any fields (static or instance) or any methods you wish.
 */
public class Simulation {
	// List to track simulation events during simulation
	

	public static List<SimulationEvent> events;  
	public static List<Customer> customerList; 
	public static Queue<Customer> orderQueue;
	public static HashMap<Customer,Boolean> orderStatusMap;
	public static int shopCapacity;
	public static List<Machine> machines;
    public static SwarmOptimizer swarm;
    public static int currentCustomerOrders = 0;
    public static boolean flag = false;


	/**
	 * Used by other classes in the simulation to log events
	 * @param event
	 */
	public static void logEvent(SimulationEvent event) {
		events.add(event);
		System.out.println(event);
	}

	/**
	 * 	Function responsible for performing the simulation. Returns a List of 
	 *  SimulationEvent objects, constructed any way you see fit. This List will
	 *  be validated by a call to Validate.validateSimulation. This method is
	 *  called from Simulation.main(). We should be able to test your code by 
	 *  only calling runSimulation.
	 *  
	 *  Parameters:
	 *	@param numCustomers the number of customers wanting to enter the coffee shop
	 *	@param numCooks the number of cooks in the simulation
	 *	@param numTables the number of tables in the coffe shop (i.e. coffee shop capacity)
	 *	@param machineCapacity the capacity of all machines in the coffee shop
	 *  @param randomOrders a flag say whether or not to give each customer a random order
	 *
	 */
	
	/*
	 *INVARIANTS:
	 * 1. Each event must be printed and validated b the validateSimulation() method.
	 * 2. Every simulated event should be logged in an events list
	 * 3. Every order should  be either random (unique order for each customer)  or same order ffor each customer
	 * 
	 *PRE-CONDITIONS:
	 * 1. numCustomers, numCooks, numTables, machineCapacity for each machine and randomOrders flag should all be defined
	 * for appropriate simulation of events
	 *
	 *POST-CONDITIONS:
	 * 1. The simulation must terminate only after all customers have been served.
	 * 2. Interrupt must called on each of the cooks to stop running as simulation has ended and 
	 * no further customers need to be served
	 * 
	 *EXCEPTIONS:
	 * 1. InterruptedException in case the Simulation is interrupted.
	 */
	
	
	public static List<SimulationEvent> runSimulation(
			int numCustomers, int numCooks,
			int numTables, 
			int machineCapacity,
			boolean randomOrders
			) {

		//This method's signature MUST NOT CHANGE.  


		//We are providing this events list object for you.  
		//  It is the ONLY PLACE where a concurrent collection object is 
		//  allowed to be used.
		events = Collections.synchronizedList(new ArrayList<SimulationEvent>());
		Comparator<Customer> comparator = new CustomerComparator();
		customerList = new ArrayList<Customer>();
		orderQueue = new LinkedList<Customer>();
		orderStatusMap = new HashMap<Customer, Boolean>();
		shopCapacity=numTables;




		// Start the simulation
		logEvent(SimulationEvent.startSimulation(numCustomers,
				numCooks,
				numTables,
				machineCapacity));



		// Set things up you might need


		// Start up machines
		machines = new ArrayList<Machine>();
		Machine machine1 = new Machine("MachineFrier",FoodType.fries,machineCapacity);
		Machine machine2 = new Machine("MachineGrill",FoodType.burger,machineCapacity);
		Machine machine3 = new Machine("CoffeeMaker2000",FoodType.coffee,machineCapacity);
		machines.add(machine1);
		machines.add(machine2);
		machines.add(machine3);
		

		// Let cooks in
		Thread[] cooks = new Thread[numCooks];
		for(int i=0; i<numCooks; i++){
			cooks[i] = new Thread(
					
					new Cook("Cook " + (i+1))); 
		}
		for(int i =0; i<cooks.length;i++){
			cooks[i].start();
		}


		// Build the customers.
		Thread[] customers = new Thread[numCustomers];
		LinkedList<Food> order;
		if (!randomOrders) {
			order = new LinkedList<Food>();
			order.add(FoodType.burger);
			order.add(FoodType.fries);
			order.add(FoodType.fries);
			order.add(FoodType.coffee);
			Random rnd = new Random();
			for(int i = 0; i < customers.length; i++) {
				int randomPriotity = rnd.nextInt(2)+1;
				int randomConsumptionTime = rnd.nextInt(150)+150;
				customers[i] = new Thread(
					
						new Customer("Customer " + (i+1), order,randomPriotity,randomConsumptionTime)
						);
			}
		}
		else {
			for(int i = 0; i < customers.length; i++) {
				Random rnd = new Random(27);
				int burgerCount = rnd.nextInt(3);
				int friesCount = rnd.nextInt(3);
				int coffeeCount = rnd.nextInt(3);
				order = new LinkedList<Food>();
				for (int b = 0; b < burgerCount; b++) {
					order.add(FoodType.burger);
				}
				for (int f = 0; f < friesCount; f++) {
					order.add(FoodType.fries);
				}
				for (int c = 0; c < coffeeCount; c++) {
					order.add(FoodType.coffee);
				}
				int randomPriotity = rnd.nextInt(2)+1;
				int randomConsumptionTime = rnd.nextInt(150)+150;
				customers[i] = new Thread(
						new Customer("Customer " + (i+1), order,randomPriotity,randomConsumptionTime)
						);
			}
		}


		// Now "let the customers know the shop is open" by
		//    starting them running in their own thread.
		for(int i = 0; i < customers.length; i++) {
			customers[i].start();
			//NOTE: Starting the customer does NOT mean they get to go
			//      right into the shop.  There has to be a table for
			//      them.  The Customer class' run method has many jobs
			//      to do - one of these is waiting for an available
			//      table...
		}


		try {
			// Wait for customers to finish
			//   -- you need to add some code here...
		
			
			
			
			for(int i = 0; i < customers.length; i++){
				customers[i].join();
			}
			
			
			

			// Then send cooks home...
			// The easiest way to do this might be the following, where
			// we interrupt their threads.  There are other approaches
			// though, so you can change this if you want to.
			for(int i = 0; i < cooks.length; i++)
				cooks[i].interrupt();
			for(int i = 0; i < cooks.length; i++)
				cooks[i].join();

		}
		catch(InterruptedException e) {
			System.out.println("Simulation thread interrupted.");
		}

		// Shut down machines





		// Done with simulation		
		logEvent(SimulationEvent.endSimulation());

		return events;
	}

	/**
	 * Entry point for the simulation.
	 *
	 * @param args the command-line arguments for the simulation.  There
	 * should be exactly four arguments: the first is the number of customers,
	 * the second is the number of cooks, the third is the number of tables
	 * in the coffee shop, and the fourth is the number of items each cooking
	 * machine can make at the same time.  
	 */
	
	
	public static void swarmMethod(){
		
		List<Particle> pList = new ArrayList<>();
		List<Double> p1x = new ArrayList<Double>();
		p1x.add(1.0);
		p1x.add(2.0);
		p1x.add(3.0);
		//p1x.add(4.0);
		
		List<Double> p2x = new ArrayList<Double>();
		p2x.add(3.0);
		p2x.add(2.0);
		p2x.add(1.0);
		//p2x.add(1.0);
		
		List<Double> p3x = new ArrayList<Double>();
		p3x.add(2.0);
		p3x.add(3.0);
		p3x.add(1.0);
		//p3x.add(3.0);
		
		Particle p1 = new Particle(p1x);
		Particle p2 = new Particle(p2x);
		Particle p3 = new Particle(p3x);
		
		pList.add(p1);
		pList.add(p2);
		pList.add(p3);
		
		
		
	
		//if(Simulation.currentCustomerOrders%numCustomers==0){
			Object lock = new Object();
			swarm = new SwarmOptimizer(Simulation.customerList,pList);
			swarm.optimizationIterator();
			List<Double> gbestlist = swarm.gBest;
			List<Integer> actualGBestSequence = swarm.getSequence(gbestlist);
			synchronized(Simulation.orderQueue){
				//Simulation.orderQueue = new LinkedList<>();
				for(int i=0;i<Simulation.orderQueue.size();i++){
					Simulation.orderQueue.poll();
				}
				
				for(int i=0;i<gbestlist.size();i++){
					
					//Simulation.orderQueue.add(Simulation.customerList.get(list.get(i).intValue()-1));
					Simulation.orderQueue.add(Simulation.customerList.get((int) (actualGBestSequence.get(i)-1)));
					Customer currentCustomer = Simulation.customerList.get((int) (actualGBestSequence.get(i)-1));
					Simulation.logEvent(SimulationEvent.customerPlacedOrder(currentCustomer, currentCustomer.getOrder(), currentCustomer.getOrderNum()));
					Simulation.currentCustomerOrders--;
					
				}
				Simulation.flag = true;
				Simulation.orderQueue.notifyAll();
				
				
			}
			
			
		//}
		
		
		
		
	}
	
	
	
	
	public static List<Particle> initializeParticleSet(){
		List<Particle> particleSet = new ArrayList<>();
    	ArrayList<List<Double>> sampleParticleLists = new ArrayList<List<Double>>();
    	int numberOfCustomers = customerList.size();
    	List<Double> arrayOfCustomerIndices = new ArrayList<>();
    	for(int j = 0; j<customerList.size(); j++){
    		arrayOfCustomerIndices.add((double) (j+1));
    	}
    	
    	
    	
    	while(sampleParticleLists.size()<numberOfCustomers){
    		List<Double> newList = shuffleCustomerList(arrayOfCustomerIndices);
    		/*if(!sampleParticleLists.contains(newList)){
    			Particle p = new Particle(newList);
    			particleSet.add(p);
    			sampleParticleLists.add(newList);
    		}*/
    		if(particleSet.isEmpty()){
    			Particle p = new Particle(newList);
    			particleSet.add(p);
    		}else{
    			
    			boolean flag = false;
    			for(int i=0;i<particleSet.size();i++){
    				if(particleSet.get(i).x.equals(newList)){
    					flag= true;
    				}
    			}
    			
    			if(!flag){
    				Particle p = new Particle(newList);
        			particleSet.add(p);
    			}
    		}
    		
    	}
    	
    	return particleSet;
    }
	
	public static List<Double> shuffleCustomerList(List<Double> arrayOfCustomerIndices){
		for(int i = 0; i < arrayOfCustomerIndices.size(); i++){
			int r = (int) (Math.random() * (i+1));
			double swap = arrayOfCustomerIndices.get(r);
			arrayOfCustomerIndices.set(r, arrayOfCustomerIndices.get(i));
			arrayOfCustomerIndices.set(i, swap);
		}
		
		return arrayOfCustomerIndices;
	}
	public static void main(String args[]) throws InterruptedException {
		// Parameters to the simulation
		/*
		if (args.length != 4) {
			System.err.println("usage: java Simulation <#customers> <#cooks> <#tables> <capacity> <randomorders");
			System.exit(1);
		}
		int numCustomers = new Integer(args[0]).intValue();
		int numCooks = new Integer(args[1]).intValue();
		int numTables = new Integer(args[2]).intValue();
		int machineCapacity = new Integer(args[3]).intValue();
		boolean randomOrders = new Boolean(args[4]);
		 */
		int numCustomers = 5;
		int numCooks =2;
		int numTables = 3;
		int machineCapacity = 2;
		boolean randomOrders = true;


		// Run the simulation and then 
		//   feed the result into the method to validate simulation.
		System.out.println("Did it work? " + 
				Validate.validateSimulation(
						runSimulation(
								numCustomers, numCooks, 
								numTables, machineCapacity,
								randomOrders
								),numCustomers, numCooks, numTables, machineCapacity
						)
				);
	}

}



