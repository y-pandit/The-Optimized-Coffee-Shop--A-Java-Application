package p2;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Cooks are simulation actors that have at least one field, a name.
 * When running, a cook attempts to retrieve outstanding orders placed
 * by Eaters and process them.
 */
public class Cook implements Runnable {
	private final String name; 
	int fries_plating_time;
	int burger_plating_time;
	int coffee_plating_time;
	public List<Food> orderItemsList;
	private Customer currentCustomer;
	private List<Food> currentOrder;

	
	
	
	
	public int getFries_plating_time() {
		return fries_plating_time;
	}


	public void setFries_plating_time(int fries_plating_time) {
		this.fries_plating_time = fries_plating_time;
	}


	public int getBurger_plating_time() {
		return burger_plating_time;
	}


	public void setBurger_plating_time(int burger_plating_time) {
		this.burger_plating_time = burger_plating_time;
	}


	public int getCoffee_plating_time() {
		return coffee_plating_time;
	}


	public void setCoffee_plating_time(int coffee_plating_time) {
		this.coffee_plating_time = coffee_plating_time;
	}


	/**
	 * You can feel free modify this constructor.  It must
	 * take at least the name, but may take other parameters
	 * if you would find adding them useful. 
	 *
	 * @param: the name of the cook
	 */
	public Cook(String name) {
		this.name = name;
		this.orderItemsList = new ArrayList<Food>();
	}
	

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows.  The cook tries to retrieve
	 * orders placed by Customers.  For each order, a List<Food>, the
	 * cook submits each Food item in the List to an appropriate
	 * Machine, by calling makeFood().  Once all machines have
	 * produced the desired Food, the order is complete, and the Customer
	 * is notified.  The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some
	 * other thread calls the interrupt() method on it, which could
	 * raise InterruptedException if the cook is blocking), then it
	 * terminates.
	 */
	
	/*INVARIANTS: 
	 * 1. Cook must always handle the order of the customer with highest priority
	 * 2. The Cook can move on to another order only if the current order in process is complete
	 * 
	 *PRE-CONDITIONS:
	 * 1. Customer of highest priority is served first.
	 * 2. All machines have completed processing the previous order
	 * 
	 *POST CONDITIONS:
	 * 1. Customer is notified when the order is complete
	 * 2. None of the machines are processing any of the items in the current order
	 * 
	 *EXCEPTIONS:
	 * 1. InterruptedException is thrown if during the execution of an order, the cook is interrupted.
	 * The cook terminates in this case. This happends when all the customers have been served and the simulation 
	 * has ended
	 */
	
	Object lock = new Object();
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {
			while(true) {
				//YOUR CODE GOES HERE...
				synchronized(Simulation.orderQueue){
					while(Simulation.orderQueue.isEmpty()){
						Simulation.orderQueue.wait();
					}
					
						currentCustomer = Simulation.orderQueue.remove();
					    currentOrder = currentCustomer.getOrder();
					    Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, currentOrder, currentCustomer.getOrderNum()));
					    Simulation.orderQueue.notifyAll();
				}
					    for(Food item: currentOrder){
					    	for(int i=0; i<Simulation.machines.size();i++){
					    		if(Simulation.machines.get(i).machineFoodType.equals(item)){
					    			synchronized(Simulation.machines.get(i).orderNumberQueue){
										while(!(Simulation.machines.get(i).orderNumberQueue.size() < Simulation.machines.get(i).machineCapacity)){
											Simulation.machines.get(i).orderNumberQueue.wait();
										}
										Simulation.logEvent(SimulationEvent.cookStartedFood(this,Simulation.machines.get(i).machineFoodType  , currentCustomer.getOrderNum()));
										Simulation.machines.get(i).makeFood(this, currentCustomer.getOrderNum());
										Simulation.machines.get(i).orderNumberQueue.notifyAll();

									}
					    			break;
					    		}
					    	} 
					    }
					
					    synchronized(orderItemsList){
							while(!(orderItemsList.size() == currentCustomer.getOrder().size())){
								orderItemsList.wait();
								orderItemsList.notifyAll();
							}
						}
						Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, currentCustomer.getOrderNum()));
				
				
						synchronized(Simulation.orderStatusMap){
							Simulation.orderStatusMap.put(currentCustomer, true);
							Simulation.orderStatusMap.notifyAll();
						}
						orderItemsList = new ArrayList<Food>();
			}
		}
		catch(InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
}