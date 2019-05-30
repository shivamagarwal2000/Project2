package mycontroller;

import controller.CarController;
import swen30006.driving.Simulation;
import world.Car;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class MyAutoController extends CarController{		
		
	private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
	
	public MyAutoController(Car car) {
		super(car);
		detector = new Detector();
	}
		
	//split the detector out
	private Detector detector;

	public Detector getDetector() {
		return detector;
	}
	
	public void move(CarController controller, ArrayList <Coordinate> path){
		boolean turnAlready = false;
		if(getSpeed() < Settings.getCAR_MAX_SPEED()){       // Need speed to turn and progress toward the exit
			applyForwardAcceleration();   // Tough luck if there's a wall in the way
		}
		Collections.reverse(path);
		for (Coordinate target: path){
			
			Coordinate currentPosition = new Coordinate(controller.getPosition());
			WorldSpatial.Direction currentDirection = controller.getOrientation();
			switch (currentDirection) {
			case EAST:
				if(target.x==currentPosition.x&&(target.y-1)==currentPosition.y&&!turnAlready){
					controller.turnLeft();
					turnAlready=true;
				} else if(target.x==currentPosition.x&&(target.y+1)==currentPosition.y&&!turnAlready){
					controller.turnRight();
					turnAlready=true;
				}
				return;
			case NORTH:
				if((target.x+1)==currentPosition.x&&target.y==currentPosition.y&&!turnAlready){
					controller.turnLeft();
					turnAlready=true;
				} else if((target.x-1)==currentPosition.x&&target.y==currentPosition.y&&!turnAlready){
					controller.turnRight();
					turnAlready=true;
				}
				return;
			case SOUTH:
				if((target.x-1)==currentPosition.x&&target.y==currentPosition.y&&!turnAlready){
					controller.turnLeft();
					turnAlready=true;
				} else if((target.x+1)==currentPosition.x&&target.y==currentPosition.y&&!turnAlready){
					controller.turnRight();
					turnAlready=true;
				}
				return;
			case WEST:
				if(target.x==currentPosition.x&&(target.y+1)==currentPosition.y&&!turnAlready){
					controller.turnLeft();
					turnAlready=true;
				} else if(target.x==currentPosition.x&&(target.y-1)==currentPosition.y&&!turnAlready){
					controller.turnRight();
					turnAlready=true;
				}
				return;
			}
		}
	}
	
	@Override
	public void update() {
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		Coordinate currentPosition = new Coordinate(this.getPosition());
		Coordinate targetPosition;
		if((targetPosition = getDetector().getParcel(currentView, this))!= null) {
		
			if(Simulation.toConserve() == Simulation.StrategyMode.HEALTH) {
				HealthConsStratergy obj = new HealthConsStratergy(this, getOrientation(), currentView, targetPosition);
				obj.generateWeightedMap();
				obj.generateSourceAndDestination(currentPosition, targetPosition);
				obj.generateDArray();
				ArrayList <Coordinate> test= obj.dijkstra();
				move(this, test);
			} else if (Simulation.toConserve() == Simulation.StrategyMode.FUEL) {
				
			}
		}
		
		else{
			
		// checkStateChange();
		if(getSpeed() < Settings.getCAR_MAX_SPEED()){       // Need speed to turn and progress toward the exit
			applyForwardAcceleration();   // Tough luck if there's a wall in the way
		}

		if (isFollowingWall) {
			// If wall no longer on right, turn right
			if(!getDetector().checkFollowingWall(this, getOrientation(), currentView, Settings.getWallSensitivity())) {
				turnRight();
			} else {
				// If wall on right and wall straight ahead, turn left
				if(getDetector().checkWallAhead(this, getOrientation(), currentView, Settings.getWallSensitivity())) {
					turnLeft();
				}
			}
		} else {
			// Start wall-following (with wall on right) as soon as we see a wall straight ahead
			if(getDetector().checkWallAhead(this, getOrientation(),currentView, Settings.getWallSensitivity())) {
				turnLeft();
				isFollowingWall = true;
			}
		}
		}
			
	}
		
}
