# Project2



The University of Melbourne

SWEN30006 Software Modelling and Design
 Semester 1, 2019

Project 2 Report
Parcel Pickup

Team: W6-5

Team Member (Name, Student ID, email):
Shivam Agarwal, 951424, agarwals1@student.unimelb.edu.au
Zac Pullman, 695145, zpullman@student.unimelb.edu.au
Zijian Zhao, 888116, zijianz3@student.unimelb.edu.au

Introduction
The task is to design, implement, integrate and test a car auto-controller that is able to successfully traverse the map and its traps. The auto-controller has to explore the map, locate parcels, collect parcels and finally make its way to exit. There are two strategies required; Health conservation and fuel conservation. The design also needs to be extendable and preferably modular.
Path Finding Algorithm Description
In our path finding strategy we implemented some components of the Dijkstra algorithm. To use that we converted our hashmap to a 2d representation of coordinates and generated a weighted matrix that applied logic of the traps based on fuel and health strategies. Then we applied the Dijkstra condition to our matrix and stored a path leading to the destination from a source node. This path was then converted to a hashmap of coordinates that led to the destination
Design Logic
The system required two strategies for solving the maze, which depended on either health or fuel. We saw that solving the maze required conservation of both health and fuel where possible. As such we chose for both our strategies to take optimal path to a parcel as soon as it is seen. We do this by the use of dijkstra, a weighted map and a heuristic that depends on what resource needs to be conserved. That is we dodge lava when we need to conserve health or if we need to conserve fuel we sacrifice some health and traverse the lava.
The MyAutoController class is used not only as a means to communicate with the larger system, but as the core of the subsystem. The logic is easier to see as a software “use case”;
CarController reads in driving properties.
The MyAutoController is instantiated
MyAutoController calls for the strategy required by the system to be implemented for the given maze.
Then it runs through the maze using a wall follower.
When package is spotted apply chosen strategy to collect package.
If there are more packages to find, go back to 4) and loop until all packages are found.
 Once all packages are collected move to exit:
We have passed exit before so use greedy algorithm to get back to exit.
We haven't passed exit so we keep following wall.
Passed the exit, but greedy path back is blocked so keep wall following.
Results in:
Successfully exit the maze.
Run out of fuel.
run out of health.
These steps are achieved by the use of the IStrategy abstract class and Detector class. The detector class is required for the implementation of both the wall follower and the strategies as a guard for not crashing into walls. The detector is also used to find parcels, once a parcel is seen the strategy takes over and takes best path to preserve health or fuel.

Design Diagram
The design diagram splits from the overarching diagram given to give a clearer diagram of the MyAutoController class implemented. Noting that the detector class uses WorldSpatial enumerations for giving directions.
The key GoF pattern used in our design are the facade and the strategy pattern, which is complemented by the factory pattern. The Istrategy class allows the controller class MyAutoController to choose which Strategy is required. The two strategies are implemented by inputting different weighted maps for fuel and health conservation.
Grasp patterns involved in the MyAutoController subsystem are polymorphism, pure fabrication, protected variations and an information expert (not in diagram, couldn’t implement in time). These allow for high cohesion and low coupling. Facade being MyAutoController, purely fabricated class of Detector, the polymorphism used to achieve a strategy factory, protected variations of strategies and information expert in MapInfoCollector  (not in diagram).
Communication Diagram
The subclasses of mycontroller used in this project are MyAutoController, Settings, Detector, IStrategy, HealthConsStrategy and FuelConsStrategy. The black dot shows the start of the process. At the start, MyAutoController creates a Detector class for viewing the exact types of map tiles in the car’s view. It uses the followingWall method to guide the way of exploring the maze with the detector, and it also uses the number of wall sensitivity and car speed from Settings class. During the driving, the MyAutoController would set a record for the location of exit if the car pass it. If the car views a parcel tile in its view, MyAutoController would create a IStrategy based on the condition of strategy type: If the car is using HEALTH type, it would create a HealthConsStrategy class, otherwise it would create a FuelConsStrategy one. And the IStrategy could calculating the path to collect the parcel based on different weights of tiles. After it gets the path, the MyAutoController class use move method to collect that parcel. The same process would happen again until the car gets enough parcels to escape. After that, if the car already has enough parcels, it will just use follow the wall method and find the exit. However, if it already has passed the exit yet, the car would go to exit directly based on the location of exit we stored before, using getFinalPath and move methods.
Polymorphism and strategy factory:
In the Istrategy abstract class, we chose to implement a strategy factory. We chose a strategy factory because the system required multiple strategies, and for future extendability a factory works well. That is we could have a controller require multiple different strategies, at different times throughout the maze. The style of the factory is to allow the subclass to instantiate itself. In this case the subclass is a strategy, so this is a great fit. The strategy factory promotes high cohesion and low coupling by being a single point of contact of the MyAutoController class.
The strategies themselves use a weighted map to decide on the optimal path to take given the constraint on health or fuel. This simple yet effective design means at future stages different strategies can be implemented with changes just to the weights of the map. The choice of weighted maps also allows for composite strategies to be made, where we choose a balanced path between constraints.
The best way to implement the strategy factory is through polymorphism, which is exactly why we did it. By using polymorphism we see that high cohesion follows, allowing each strategy to do exactly what it needs to, as well as offering protected variations. 

Pure Fabrication
The detector class is an example of pure fabrication. This class is used to complement the auto-controller by handling the logic of vision. Detector checks for walls, parcels and lava. Detector could be added to, to extend the ability of the auto-controller. lava mazes today, moon mazes tomorrow.  We choose pure fabrication here instead of expert because we only care about our current view since we have no sense of the maze. The detector is primarily required for finding parcels, and traversing an unknown map.
Pure fabrication is also used in the case of the strategy factory. However this is a consequence of the GoF being predominantly software ideas.
Information Expert
The MapInfoCollector whilst never used was intended to be an information expert. MapInfoCollector takes in the current view of the car and adds it to a map. this map is updated with each step and would allow for optimal path taken to get to the exit if the car passed the exit before collecting all parcels. This could also be used if we haven't seen the exit we know that it’s not where we have been before. The use of information expert would improve our design by allowing for dijkstra or A* search to get a path to the exit, rather than a greedy search back with a wall follower as a backup.
High cohesion and low coupling
High cohesion and low coupling was achieved by the use of the above GoF and GRASP patterns. When looking at the design diagram it is clear that each class is highly cohesive. That is the strategies are only connected to Istrategy, and aren’t connected to each other. Detector is a stand alone class of methods for looking at the maze separating some long logic from the auto-controller. Settings are also called upon elsewhere to ensure that the auto-controller is robust to change. Lastly the MyAutoController class is the core that uses these classes as required to achieve the goal of collecting the packages and exiting the maze.
Conclusion and reflection
The rationale of the design is sound. However given more time there are possible improvements to be made to functionality. Implementation of an information expert to map the maze as the car travels would improve the efficiency of exiting the maze. Attached to this information expert would be the creation of an ExitStrategy class that would fall under IStrategy and be called once all parcels were collected. Improvements to the wall follower so that it would follow a wall at maximum distance from it so it could gather as much information as possible. 
