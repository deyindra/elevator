Basic Requirement
============================
### 1. Design and implement a basic elevator control system for a N-story (N>1) building with only 1 elevator, and a simulator that can accept requests from passengers and change states (idle, up, down, open, close) based on decisions made by the control system.
### 2. There could be multiple users trying to use the elevator at any given time, e.g. Elevator is in transit to floor X with passenger A when passenger B pushes the button on floor Y.
### 3. Adding multiple elevators to the system (Enhance requirement)


 Class Diagram
============================
![class diagram](elevator.jpg)


Assumption
=====================
##### 1. Currently all elevator should start from floor 1
##### 2. All passenger will enter to building from floor 1



Build
=====================
### Requirement
##### 1. JDK 1.7
##### 2. Maven 3.0.x



Building and Execution
=======================
##### 1. Check out the Git repository
    git clone https://github.com/deyindra/elevator.git

##### 2. Run following maven command to build the project
    mvn clean install

##### 3. Go to the target folder after build is finshed and un-tar "Sudoku-bin.tar"
        cd target
        mkdir releases
        cd releases
        tar -xvf ../elevator-bin.tar

##### 4. Execute the following command to run the elevator simulation
        java -jar elevator-1.0-SNAPSHOT.jar (This will take default property. Hardcoded in the code)

        if user want to run with custom property value he/she needs to execute the following command
        java -Dproperty.path=./config/config.properties -jar elevator-1.0-SNAPSHOT.jar

Configuration
=======================

##### 1 total.floor - Total number of floor
##### 2 total.elevator - Total elevator
##### 3  simulation.max.people - Total people for the simulation
##### 4  simulation.max.time - Maximum simulation counter
##### 5  floor.wait.time - Wait time of the elevator in the floor
##### 6  floor.travel.time - Floor travel time
##### 7  elevator.inactive.time - Elevator inactive time
##### 8  elevator.max.occupancy - Elevator max occupancy
##### 9  person.waiting.time - Person's wait time
##### 10  person.working.time - Person's working time
##### 11  elevator.door.time - Elevator's door toggle time

Limitation
=======================

#### Currently system designed for FCFS (First come First Serve) Algorithm to get the elevator. This can be enhanced to Cost based Shortest path algorithm.
#### Currently system designed for All the person enter from the first floor. Later this can be enhanced to select random initial floor location

Enhancement
=======================
#### Make this as Web API based
#### Added More test cased to check the concurrency.




