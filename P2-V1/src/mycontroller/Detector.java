package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class Detector {
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 1;
	
	//Check if you have a wall in front of you
	public boolean checkWallAhead(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView) {
		switch (orientation) {
		case EAST:
			return checkEastWall(controller, currentView, wallSensitivity);
		case NORTH:
			return checkNorthWall(controller, currentView, wallSensitivity);
		case SOUTH:
			return checkSouthWall(controller, currentView, wallSensitivity);
		case WEST:
			return checkWestWall(controller, currentView, wallSensitivity);
		default:
			return false;
		}
	}
	
	//Check if the wall is on your left hand side given your orientation
	public boolean checkFollowingWall(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView) {
		switch (orientation) {
		case EAST:
			return checkNorthWall(controller, currentView, wallSensitivity);
		case NORTH:
			return checkWestWall(controller, currentView, wallSensitivity);
		case SOUTH:
			return checkEastWall(controller, currentView, wallSensitivity);
		case WEST:
			return checkSouthWall(controller, currentView, wallSensitivity);
		default:
			return false;
		}
	}
	
	//Method below just iterates through the list and check in the correct coordinates.
	//checkEast will check up to wallSensitivity amount of tiles to the right.
	public boolean checkEastWall(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
		// Check east tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(
					new Coordinate(currentPosition.x + i, currentPosition.y));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}
	
	//checkWest will check up to wallSensitivity amount of tiles to the left.
	public boolean checkWestWall(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
		// Check west tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(
					new Coordinate(currentPosition.x - i, currentPosition.y));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}
	
	//checkNorth will check up to wallSensitivity amount of tiles to the top.
	public boolean checkNorthWall(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
		// Check north tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(
					new Coordinate(currentPosition.x, currentPosition.y + i));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}
	
	//checkSouth will check up to wallSensitivity amount of tiles below.
	public boolean checkSouthWall(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
		// Check south tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(
					new Coordinate(currentPosition.x, currentPosition.y - i));
			if (tile.isType(MapTile.Type.WALL) || currentPosition.y == 1) {
				return true;
			}
		}
		return false;
	}
}
