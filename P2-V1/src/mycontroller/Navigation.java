package mycontroller;

import java.util.ArrayList;

import controller.CarController;
import utilities.Coordinate;
import world.WorldSpatial;

public class Navigation {
	public void move(CarController controller, ArrayList <Coordinate> path){
		if(controller.getSpeed() < Settings.getCAR_MAX_SPEED()){
			controller.applyForwardAcceleration();
		}
		for (Coordinate target: path){
			Coordinate currentPosition = new Coordinate(controller.getPosition());
			WorldSpatial.Direction currentDirection = controller.getOrientation();
			switch (currentDirection) {
			case EAST:
				if(target.x-1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnLeft();
				} else if(target.x-1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnRight();
				}
			case NORTH:
				if(target.x+1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnLeft();
				} else if(target.x-1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnRight();
				}
			case SOUTH:
				if(target.x-1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnLeft();
				} else if(target.x+1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnRight();
				}
			case WEST:
				if(target.x+1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnLeft();
				} else if(target.x+1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnRight();
				}
			}
		}
	}
}
