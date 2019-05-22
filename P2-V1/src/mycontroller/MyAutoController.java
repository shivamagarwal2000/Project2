package mycontroller;

import controller.CarController;
import world.Car;
import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class MyAutoController extends CarController{		
		
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		
		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;
		
		public MyAutoController(Car car) {
			super(car);
			detector = new Detector();
		}
		
		//split the detector out
		private Detector detector;

	    public Detector getDetector() {
	    	return detector;
	    }
	    
		// Coordinate initialGuess;
		// boolean notSouth = true;
		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();
			
			// checkStateChange();
			if(getSpeed() < CAR_MAX_SPEED){       // Need speed to turn and progress toward the exit
				applyForwardAcceleration();   // Tough luck if there's a wall in the way
			}
			if (isFollowingWall) {
				// If wall no longer on left, turn left
				if(!getDetector().checkFollowingWall(this, getOrientation(), currentView)) {
					turnLeft();
				} else {
					// If wall on left and wall straight ahead, turn right
					if(getDetector().checkWallAhead(this, getOrientation(), currentView)) {
						turnRight();
					}
				}
			} else {
				// Start wall-following (with wall on left) as soon as we see a wall straight ahead
				if(getDetector().checkWallAhead(this, getOrientation(),currentView)) {
					turnRight();
					isFollowingWall = true;
				}
			}
		}
		
	}
