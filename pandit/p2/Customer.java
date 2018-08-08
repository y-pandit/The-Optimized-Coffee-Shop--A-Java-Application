package p2;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the coffee shop (only successful if the
 * coffee shop has a free table), place its order, and then leave the 
 * coffee shop when the order is complete.
 */
public class Customer implements Runnable {
	//JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	int cust_id;
	private final String name;
	private final List<Food> order;
	private final int orderNum;    
	private final int priority; 
	private final int consumptionTime; 
	 
	public volatile boolean isServed;
	private static int runningCounter = 0;
	

	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order, int priority, int consumptionTime) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		this.priority = priority;
		this.consumptionTime = consumptionTime;
		this.isServed=false;
	}

	
	
	public int getPriority() {
		return priority;
	}

	public List<Food> getOrder() {
		return order;
	}

	public int getOrderNum() {
		return orderNum;
	}



	public String toString() {
		return name;
	}

	/** 
	 * This method defines what an Customer does: The customer attempts to
	 * enter the coffee shop (only successful when the coffee shop has a
	 * free table), place its order, and then leave the coffee shop
	 * when the order is complete.
	 */
	
	
	/*INVARIANTS:
	 * 1. Every Customer must be given a randomly generated priority (between 1 and 3)
	 * 2. Cook must always handle the order of the customer with highest priority
	 * 3. Another customer can be served only ifthe current custmer has been served
	 * 
	 *PRE-CONDITIONS:
	 * 1. A time-to-eat and a random customer ID should be generated for each customer.
	 * 2. Customer should be queued and successfully admitted to the coffee shop only if there is a free table.
	 * 
	 *POST-CONDITIONS:
	 * 1. Customer has successfully either entered the coffee shop, placed thier order or left the shop depending upon
	 *  with condition is invoked.   
	 * 2. Customer is served according to priority
	 * 
	 * EXCEPTIONS:
	 * 1. InterruptedException is thrown if any of the customer related actions are interrupted.
	 * 
	 */
	
	
	Object monitor = new Object();
	
	public void run() {
		//YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
	//	while(true){

			while(Simulation.customerList.size()>Simulation.shopCapacity){
				
				//synchronized(Simulation.orderQueue){
					
					/*
					 * IN THIS PART EACH CUSTOMER WILL PLACE THEIR ORDER 
					 * THEN ALL THE ORDERS WILL BE FED TO THE PSO FUNCTION AND THEN ADDED TO THE ORDERQUEUE 
					 * IN THE RESULTING ORDER..REST EVERYTHING REMAINS THE SAME.
					 * 				
					 */
						
//						List<Double> list = Simulation.swarm.gBest;				
//						for(int i=0;i<list.size();i++){
//							Simulation.orderQueue.add(Simulation.customerList.get(list.get(i).intValue()-1));
//							Customer currentCustomer = Simulation.customerList.get(list.get(i).intValue()-1);
//							Simulation.logEvent(SimulationEvent.customerPlacedOrder(currentCustomer, currentCustomer.order, currentCustomer.orderNum));
//							Simulation.orderQueue.notifyAll();
//						}
							    
			//	}
				
				
				
				
				//make the customer wait 
				synchronized(Simulation.customerList){
					try {
						Simulation.customerList.wait();
					    
						
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
			}
			
				synchronized(Simulation.customerList){
					Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
					
					
//AT THIS POINT, THE CUSTOMER WILL ENTER THE RESTAURANT WITHOUT ANY PRIORITY CALCULATION (FIRST COME FIRST SERVE)
//THE SHOP WILL FILL TO CAPACITY AND THEN THE QUEUE WILL WAIT FOR SPOTS TO BE EMPTY..SO THIS IS ESSENTIALLY JUST A QUEUE
					
					
					Simulation.customerList.add(this);
					Simulation.currentCustomerOrders++;
					System.out.println("Current Customer Counter: "+Simulation.currentCustomerOrders);
					if(Simulation.currentCustomerOrders>0 && Simulation.flag==true){
						//Simulation.orderQueue = new LinkedList<>();
						/*for(int i=0;i<Simulation.orderQueue.size();i++){
							Simulation.orderQueue.poll();
						}*/
						
						synchronized(Simulation.orderQueue){
							if(!Simulation.orderQueue.contains(this)){
								Simulation.orderQueue.add(this);
								Simulation.orderQueue.notifyAll();
							}
							
							
						}
						
					}
					else if(Simulation.customerList.size()==Simulation.shopCapacity){
						System.out.println(Simulation.customerList);
						Simulation.swarmMethod();
					}
					
//					if(Simulation.customerList.size() == Simulation.shopCapacity){
//						
//	                synchronized(Simulation.orderQueue){
//							
//							/*
//							 * IN THIS PART EACH CUSTOMER WILL PLACE THEIR ORDER 
//							 * THEN ALL THE ORDERS WILL BE FED TO THE PSO FUNCTION AND THEN ADDED TO THE ORDERQUEUE 
//							 * IN THE RESULTING ORDER..REST EVERYTHING REMAINS THE SAME.
//							 * 				
//							 */
//								SwarmOptimizer swarm = new SwarmOptimizer(Simulation.customerList);
//								swarm.optimizationIterator();
//								List<Double> list = swarm.gBest;
//												
//								for(int i=0;i<list.size();i++){
//									Simulation.orderQueue.add(Simulation.customerList.get(list.get(i).intValue()-1));
//									Customer currentCustomer = Simulation.customerList.get(list.get(i).intValue()-1);
//									Simulation.logEvent(SimulationEvent.customerPlacedOrder(currentCustomer, currentCustomer.order, currentCustomer.orderNum));
//									Simulation.orderQueue.notifyAll();
//								}
//									    
//						}
//						
//						
//						
//					}
					
					
					
					
					
					//Notify everyone watching the customerPriotityQueue
					Simulation.customerList.notifyAll();
				}//Synch monitor
				
				
					
				synchronized(Simulation.orderStatusMap){
					Simulation.orderStatusMap.put(this, false);
				}
					
					
				synchronized(Simulation.orderStatusMap){
					while(!Simulation.orderStatusMap.get(this)){
						try {
							//waiting here for the cook to notify that the order is ready
							Simulation.orderStatusMap.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
					try {
						Thread.sleep(this.consumptionTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
					
					synchronized(Simulation.customerList){
						Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
						Simulation.customerList.remove(this);
						Simulation.customerList.notifyAll();
						
		
						
						
						
					}
					
				
				
			}

}