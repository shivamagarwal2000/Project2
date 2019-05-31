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
	Coordinate exit = null; // This is to store the location of exit if pass by
	Boolean recorded = false;
	
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
		if(getSpeed() < Settings.getCAR_MAX_SPEED()){       // Need for speed to turn and progress toward the exit
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
	
	public boolean isReachable(ArrayList <Coordinate> path, HashMap<Coordinate, MapTile> currentView) {
		boolean isReachable = true;
		for(Coordinate var : path) {
			if(currentView.get(var).isType(MapTile.Type.WALL)) {
				isReachable = false;
				return isReachable;
			}
		}
		return isReachable;
	}

	public void recordExit(HashMap<Coordinate, MapTile> currentView, Coordinate currentPosition) {
		if (currentView.get(currentPosition).isType(MapTile.Type.FINISH)){
			exit = currentPosition;
			recorded = true;
		}
	}
	
	public void followingWall(HashMap<Coordinate, MapTile> currentView){
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
			// Unless there are also a wall on the left
			if(getDetector().checkWallAhead(this, getOrientation(),currentView, Settings.getWallSensitivity())) {
				if(getDetector().checkLeftWall(this, getOrientation(), currentView, Settings.getWallSensitivity())){
					turnRight();
				} else {
					turnLeft();
					isFollowingWall = true;
				}
			}
		}
	}
	
	public ArrayList<Coordinate> getFinalPath(Coordinate start, Coordinate end, HashMap<Coordinate, MapTile> currentView) {
		
		ArrayList<Coordinate> finalPath = new ArrayList<Coordinate>();
		Coordinate temp;
		if(start.y > end.y) {
			temp = start;
			temp.y = start.y - 1;
			finalPath.add(temp);
			if(isReachable(finalPath, currentView)) {
				return finalPath;
			}
		}
		
		else if(start.y == end.y && start.x < end.x) {
			temp = start;
			temp.x = start.x + 1;
			finalPath.add(temp);
			if(isReachable(finalPath, currentView)) {
				return finalPath;
			}
		}
		else {
			return null;
		}
		return null;
	}
	
	public void followingWallToExit(HashMap<Coordinate, MapTile> currentView){
		// checkStateChange();
		if(getSpeed() < Settings.getCAR_MAX_SPEED()){       // Need speed to turn and progress toward the exit
			applyForwardAcceleration();   // Tough luck if there's a wall in the way
		}
		if (isFollowingWall) {
			// If wall no longer on right, turn right
			if(!getDetector().checkFollowingWall(this, getOrientation(), currentView, Settings.getWallSensitivity())
					&&!getDetector().checkFourLavaRight(this, getOrientation(), currentView, Settings.getLavaSensitivity(), 1)) {
				turnRight();
			} else {
				// If a wall or three lava traps on right and straight ahead, turn left
				if(getDetector().checkWallAhead(this, getOrientation(), currentView, Settings.getWallSensitivity())
						||getDetector().checkThreeLavaAhead(this, getOrientation(), currentView, Settings.getLavaSensitivity(), 1)) {
					turnLeft();
				}
			}
		} else {
			// Start wall-following (with wall on right) as soon as we see a wall straight ahead
			// Unless there are also a wall on the left
			if(getDetector().checkWallAhead(this, getOrientation(),currentView, Settings.getWallSensitivity())) {
				if(getDetector().checkLeftWall(this, getOrientation(), currentView, Settings.getWallSensitivity())){
					turnRight();
				} else {
					turnLeft();
					isFollowingWall = true;
				}
			}
		}
	}
	
	@Override
	public void update() {
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		Coordinate currentPosition = new Coordinate(this.getPosition());
		// Try to record the exit location if pass by
		recordExit(currentView,currentPosition);
		//if car does not have enough parcels, it will search for it
		if(this.numParcelsFound()< numParcels()){
			Coordinate targetPosition;
			if((targetPosition = getDetector().getParcel(currentView, this))!= null) {
				if(Simulation.toConserve() == Simulation.StrategyMode.HEALTH) {
					HealthConsStratergy obj = new HealthConsStratergy(this, getOrientation(), currentView, targetPosition);
					obj.generateWeightedMap();
					obj.generateSourceAndDestination(currentPosition, targetPosition);
					obj.generateDArray();
					ArrayList <Coordinate> way= obj.dijkstra();
					if(isReachable(way, currentView)) {
						move(this, way);
						isFollowingWall = false;
					} else {
						followingWall(currentView);
					}
				}
				else if (Simulation.toConserve() == Simulation.StrategyMode.FUEL) {
					FuelConsStratergy obj = new FuelConsStratergy(this, getOrientation(), currentView, targetPosition);
					obj.generateWeightedMap();
					obj.generateSourceAndDestination(currentPosition, targetPosition);
					obj.generateDArray();
					ArrayList <Coordinate> way= obj.dijkstra();
					if(isReachable(way, currentView)) {
						move(this, way);
						isFollowingWall = false;
					}
				}
			} else {
				followingWall(currentView);
			}
		} else {
			//if car already has enough parcels, it will just follow the wall and find the exit
			//and if it has already passed the exit yet, the car would go to exit directly
			if(!recorded){
				followingWallToExit(currentView);
			} else {
				ArrayList <Coordinate> exitWay = getFinalPath(currentPosition, exit, currentView);
				if(exitWay != null) {
					move(this, exitWay);
					System.out.println(exitWay);
					System.out.println("I am using the hard code");
				}
				else {
					followingWall(currentView);
				}
			}
		}
	}	
}
