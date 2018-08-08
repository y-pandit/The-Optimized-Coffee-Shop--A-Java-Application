# The Optimized Coffee Shop

## The Problem Statement:  
A coffee shop is to be simulated with 3 entities at work:  
a.	Customer  
b.	Cook  
c.	Machine  
The customers come into a restaurant while maintaining the capacity of the Coffee Shop and order any combination 3 menu items:  
1.	Coffee  
2.	Burger  
3.	Fries  
Each order placed goes into a list and is resolved by any of the available cooks. Each cook picks up an order from the top of the queue and assigns the responsible machine to execute the order. Each order may require multiple machines to be executed and each machine has an individual capacity.   
The problem at hand is to fill up the order queue referred by the cooks such that two criteria are fulfilled in a period of time:  
a.	Maximum number of customers are catered to in the period of time.  
b.	Each cook is kept engaged for the minimum possible time period.  
c.	Each machine is utilized in the most optimum way (depending upon its individual capacity)  
d.	The time required for each order is optimal  

## The Problem Background:
	The problem find itself in every restaurant. In the restaurant business, it is of utmost importance that the maximum number of customers are catered to in the total hours of the day.   
	Another objective of the restaurant business is to maximize its profits while ensuring that their workforce is utilized in the most optimal way, thus minimizing loss of time and customers.  
	The bigger picture here is to ensure that all the involved entities (customer and cooks) are attended to and do not have cause to complain.  
	While this is a year round issue, I have here proposed a solution for a unit of time which can fall from anywhere between 1 hour to 12 hours of operation.  

## How Particle Swarm Optimization Algorithm can solve this problem:
	The Particle: There can be multiple ways in which a orders can be executed.   
a.	Each particle is a sequence in which all orders up to the shop capacity can be executed  
b.	Each particle will have a particular time of execution associated with it.  

	The Function To Be Optimized: Out of each possible way, our optimization algorithm will proceed by adding the order sequence with the minimum execution time (its own personal best) to the priority queue.  
a.	Only those particles will be proceeded with (added to the queue) that enable catering to the maximum number of customer in a given unit time in a way that generates maximum profit.  
b.	The cooks and machine must also be utilized to the Coffee Shop best interest.  

## The Pseudo Code For The Solution Is As Follows:
```
For each particle   
    Initialize particle by finding a possible order-execution solution  
END  
Do  
    For each particle   
        Calculate the minimum execution time (fitness value)  
        If the fitness value is less than the best fitness value (pBest) in history  
            set current value as the new pBest  
    End  

    Choose the particle with the best fitness value of all the particles as the gBest  
    For each particle   
        Calculate particle velocity   
        Update particle position  
        If the time required by particle is less than pBest, update pbest  
        If cost of pbest is less that gbest, update gbest  
    End   
While maximum iterations or minimum error criteria is not attained  
Return gbest  
```

## The output of the project is as follows:



```
Starting simulation: 30 customers; 5 cooks; 10 tables; machine capacity 4.
Cook 2 reporting for work.
Cook 1 reporting for work.
Cook 3 reporting for work.
Cook 4 reporting for work.
Cook 5 reporting for work.
Customer 1 going to coffee shop.
Customer 1 entered coffee shop.
Current Customer Counter: 1
Customer 2 going to coffee shop.
Customer 4 going to coffee shop.
Customer 2 entered coffee shop.
Current Customer Counter: 2
Customer 4 entered coffee shop.
Current Customer Counter: 3
Customer 5 going to coffee shop.
Customer 5 entered coffee shop.
Current Customer Counter: 4
Customer 6 going to coffee shop.
Customer 6 entered coffee shop.
Current Customer Counter: 5
Customer 8 going to coffee shop.
Customer 8 entered coffee shop.
Current Customer Counter: 6
Customer 9 going to coffee shop.
Customer 11 going to coffee shop.
Customer 12 going to coffee shop.
Customer 10 going to coffee shop.
Customer 9 entered coffee shop.
Current Customer Counter: 7
Customer 10 entered coffee shop.
Customer 13 going to coffee shop.
Current Customer Counter: 8
……
…
…
Customer 27 going to coffee shop.
Customer 7 going to coffee shop.
Customer 12 placing order 12 [burger, burger, fries, fries, coffee, coffee]
Customer 1 placing order 1 [burger, burger, fries]
Customer 2 placing order 2 [burger, burger, fries]
Customer 4 placing order 4 [burger, fries, fries, coffee]
Customer 5 placing order 5 [burger]
Customer 6 placing order 6 [coffee]
Customer 8 placing order 8 [burger, burger, fries, coffee, coffee]
Customer 9 placing order 9 [fries, coffee, coffee]
Customer 10 placing order 10 [fries, coffee]
Customer 11 placing order 11 [burger, burger, fries, fries, coffee]
Customer 7 entered coffee shop.
Current Customer Counter: 1
Cook 5 starting order 12 [burger, burger, fries, fries, coffee, coffee]
Cook 5 cooking burger for order 12
Cook 4 starting order 1 [burger, burger, fries]
Cook 3 starting order 2 [burger, burger, fries]
Cook 1 starting order 4 [burger, fries, fries, coffee]
Cook 2 starting order 5 [burger]
Customer 27 entered coffee shop.
Current Customer Counter: 2
Customer 19 entered coffee shop.
Current Customer Counter: 3
Customer 20 entered coffee shop.
Current Customer Counter: 4
Customer 26 entered coffee shop.
Current Customer Counter: 5
Customer 22 entered coffee shop.
Current Customer Counter: 6
Customer 25 entered coffee shop.
Current Customer Counter: 7
…
…
…
Customer 13 entered coffee shop.
Current Customer Counter: 20
Cook 5 cooking burger for order 12
Cook 5 cooking fries for order 12
Cook 5 cooking fries for order 12
Cook 5 cooking coffee for order 12
Cook 2 cooking burger for order 5
Cook 5 cooking coffee for order 12
Cook 1 cooking burger for order 4
Cook 1 cooking fries for order 4
Cook 1 cooking fries for order 4
Cook 1 cooking coffee for order 4
MachineGrill cooking burger.
MachineGrill cooking burger.
MachineFrier cooking fries.
MachineFrier cooking fries.
CoffeeMaker2000 cooking coffee.
MachineGrill cooking burger.
CoffeeMaker2000 cooking coffee.
MachineGrill cooking burger.
MachineFrier cooking fries.
CoffeeMaker2000 cooking coffee.
MachineFrier cooking fries.
CoffeeMaker2000 completed coffee.
CoffeeMaker2000 completed coffee.
Cook 5 finished coffee for order 12
Cook 5 finished coffee for order 12
CoffeeMaker2000 completed coffee.
Cook 1 finished coffee for order 4
MachineFrier completed fries.

…..
…..
….
Cook 1 took for order: 4---->501 Milliseconds
Cook 3 cooking burger for order 2
Cook 1 starting order 6 [coffee]
Cook 1 cooking coffee for order 6
Cook 4 cooking fries for order 1
Cook 2 took for order: 5---->503 Milliseconds
Cook 2 starting order 8 [burger, burger, fries, coffee, coffee]
Cook 5 completed order 12
Cook 5 took for order: 12---->502 Milliseconds
Cook 5 starting order 9 [fries, coffee, coffee]
Cook 5 cooking fries for order 9
Cook 3 cooking burger for order 2
Cook 5 cooking coffee for order 9
Cook 3 cooking fries for order 2
Cook 5 cooking coffee for order 9
Customer 12 received order 12 [burger, burger, fries, fries, coffee, coffee]
MachineGrill cooking burger.
MachineGrill cooking burger.
CoffeeMaker2000 cooking coffee.
MachineFrier cooking fries.
CoffeeMaker2000 cooking coffee.
MachineGrill cooking burger.
MachineFrier cooking fries.
MachineFrier cooking fries.
CoffeeMaker2000 cooking coffee.
CoffeeMaker2000 completed coffee.
Cook 1 finished coffee for order 6
Cook 1 completed order 6
Cook 1 took for order: 6---->102 Milliseconds
CoffeeMaker2000 completed coffee.
CoffeeMaker2000 completed coffee.


Customer 21 leaving coffee shop.
Customer 18 received order 18 [burger, burger, fries, fries]
Customer 30 leaving coffee shop.
Customer 17 received order 17 [burger, burger, coffee]
Customer 18 leaving coffee shop.
Customer 13 received order 13 [burger, fries, fries, coffee]
Customer 17 leaving coffee shop.
Customer 15 received order 15 [burger, fries]
Customer 13 leaving coffee shop.
Customer 15 leaving coffee shop.
Cook 1 going home for the night.
Cook 5 going home for the night.
Cook 3 going home for the night.
Cook 4 going home for the night.
Cook 2 going home for the night.
Simulation ended.
Did it work? true
The fitness value of the best Solution is : 11.056247097713163 Seconds
The Total Service Time for all customers was:  38 Seconds
For every order, cooks took collectively: 11.757 Seconds
```



