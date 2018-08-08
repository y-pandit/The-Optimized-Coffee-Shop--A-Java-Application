package p2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A Machine is used to make a particular Food.  Each Machine makes
 * just one kind of Food.  Each machine has a capacity: it can make
 * that many food items in parallel; if the machine is asked to
 * produce a food item beyond its capacity, the requester blocks.
 * Each food item takes at least item.cookTimeMS milliseconds to
 * produce.
 */
public class Machine {
	public final String machineName;
	public final Food machineFoodType;
	private boolean isBusy;
	public int machineCapacity;
	public Queue<Integer> orderNumberQueue;

	//YOUR CODE GOES HERE...


	/**
	 * The constructor takes at least the name of the machine,
	 * the Food item it makes, and its capacity.  You may extend
	 * it with other arguments, if you wish.  Notice that the
	 * constructor currently does nothing with the capacity; you
	 * must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	
	/*INVARIANTS:
	 * 1. The constructor must assign capacity to each machine
	 * 2. An object of this class must be initialized with a defined name, type of food it can cook and 
	 * its food item at a time capacity at the very least.
	 * 3. The Machine can only cook items up to its capacity
	 * 4. The Machine can cook multiple items from the same order itself.
	 * 5. Cook food items in parallel but ensure that they take the required time
	 */
	
	
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
		this.machineCapacity = capacityIn;
		this.orderNumberQueue = new LinkedList<Integer>();
		
		//YOUR CODE GOES HERE...

	}
	

	

	/**
	 * This method is called by a Cook in order to make the Machine's
	 * food item.  You can extend this method however you like, e.g.,
	 * you can have it take extra parameters or return something other
	 * than Object.  It should block if the machine is currently at full
	 * capacity.  If not, the method should return, so the Cook making
	 * the call can proceed.  You will need to implement some means to
	 * notify the calling Cook when the food item is finished.
	 */
	
	/*
	 * 
	 *PRE-CONDITIONS:
	 * 1. the Machine can accept order to cook while still in process when it has NOT reached its full capacity.
	 * 
	 *POST-CONDITIONS:
	 * 1. The machine has completed cooking the item requested by the cook in the quantity specified 
	 * 2. The machine returns the Food object that has been cooked and notifies the cook
	 * 
	 *EXCEPTIONS:
	 * InterruptedException: Will block if the machine is currently at full capacity
	 * 
	 * 
	 */
	
	public void makeFood(Cook cook, int orderNumber) throws InterruptedException {
		//YOUR CODE GOES HERE...
		orderNumberQueue.add(orderNumber);
		Thread currentThread = new Thread(new CookAnItem(cook, orderNumber));
		currentThread.start();
	
	}
	
	/*
	 *  
	 *PRE-CONDITIONS:
	 * 1. Only one order is being processed at a time by the machine
	 * 2. Machine accepts cooking orders from the cook untill it reaches its full capacity.
	 *  
	 *POST-CONDITIONS:
	 * 1. After the food item has finished cooking, the Cook must be notified so he can proceed.
	 * 2. All the items in an order must be completed in terms of cooking in their required time.
	 *   
	 *EXCEPTIONS:
	 * 1. InterruptedException is thrown if there is violation in terms of machine capacity.
	 */

	//THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		Cook currentCook;
		int orderNumber;
		public CookAnItem(Cook currentCook, int orderNumber){
			this.currentCook = currentCook;
			this.orderNumber = orderNumber;
		}
	
		public void run() {
			try {
				//YOUR CODE GOES HERE...	
					Simulation.logEvent(SimulationEvent.machineCookingFood(Machine.this, machineFoodType));
					Thread.sleep(machineFoodType.cookTimeMS);
					Simulation.logEvent(SimulationEvent.machineDoneFood(Machine.this, machineFoodType));
					Simulation.logEvent(SimulationEvent.cookFinishedFood(currentCook, machineFoodType,orderNumber));
					synchronized(orderNumberQueue){
						orderNumberQueue.remove();
						orderNumberQueue.notifyAll();	
					}
					synchronized(currentCook.orderItemsList){
						currentCook.orderItemsList.add(machineFoodType);
						currentCook.orderItemsList.notifyAll();	
					}
				
				
			} catch(InterruptedException e) { }
		 
		}
	}
 

	public String toString() {
		return machineName;
	}
}