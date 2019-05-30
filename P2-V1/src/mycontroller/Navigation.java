package mycontroller;

import java.util.ArrayList;

import controller.CarController;
import utilities.Coordinate;
import world.WorldSpatial;

public class Navigation {
	public void move(CarController controller, ArrayList <Coordinate> path){
		boolean turnAlready = false;
		if(controller.getSpeed() < Settings.getCAR_MAX_SPEED()){
			controller.applyForwardAcceleration();
		}
		for (Coordinate target: path){
			Coordinate currentPosition = new Coordinate(controller.getPosition());
			WorldSpatial.Direction currentDirection = controller.getOrientation();
			switch (currentDirection) {
			case EAST:
				if(target.x-1==currentPosition.x&&target.y-1==currentPosition.y&&!turnAlready){
					controller.turnLeft();
					turnAlready=true;
					System.out.println("               Left");
				} else if(target.x-1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnRight();
					System.out.println("               Right");
				}
			case NORTH:
				if(target.x+1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnLeft();
					System.out.println("               Left");
				} else if(target.x-1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnRight();
					System.out.println("               Right");
				}
			case SOUTH:
				if(target.x-1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnLeft();
					System.out.println("               Left");
				} else if(target.x+1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnRight();
					System.out.println("               Right");
				}
			case WEST:
				if(target.x+1==currentPosition.x&&target.y+1==currentPosition.y){
					controller.turnLeft();
					System.out.println("               Left");
				} else if(target.x+1==currentPosition.x&&target.y-1==currentPosition.y){
					controller.turnRight();
					System.out.println("               Right");
				}
			}
		}
	}
}
