package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class Detector {
	
	public boolean checkAhead(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView, MapTile.Type type) {
		switch (orientation) {
		case EAST:
			return checkEast(controller, currentView, Settings.getWallSensitivity(), type);
		case NORTH:
			return checkNorth(controller, currentView, Settings.getWallSensitivity(), type);
		case SOUTH:
			return checkSouth(controller, currentView, Settings.getWallSensitivity(), type);
		case WEST:
			return checkWest(controller, currentView, Settings.getWallSensitivity(), type);
		default:
			return false;
		}
	}
	
	public boolean checkEast(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int sensitivity, MapTile.Type type) {
		// Check east tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= sensitivity; i++) {
			MapTile tile = currentView.get(
			new Coordinate(currentPosition.x + i, currentPosition.y));
			if (tile.isType(type)) {
				if(type == MapTile.Type.TRAP) {
					if(((TrapTile) tile).getTrap().equals("parcel"))
						return true;
				}
				return true;
			}
		}
		return false;
	}
	public boolean checkSouth(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int sensitivity, MapTile.Type type) {
		// Check South tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= sensitivity; i++) {
			MapTile tile = currentView.get(
			new Coordinate(currentPosition.x, currentPosition.y - 1));
			if (tile.isType(type)) {
				if(type == MapTile.Type.TRAP) {
					if(((TrapTile) tile).getTrap().equals("parcel"))
						return true;
				}
				return true;
			}
		}
		return false;
	}
	public boolean checkWest(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int sensitivity, MapTile.Type type) {
		// Check West tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= sensitivity; i++) {
			MapTile tile = currentView.get(
			new Coordinate(currentPosition.x - i, currentPosition.y));
			if (tile.isType(type)) {
				if(type == MapTile.Type.TRAP) {
					if(((TrapTile) tile).getTrap().equals("parcel"))
						return true;
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean checkNorth(CarController controller,
			HashMap<Coordinate, MapTile> currentView, int sensitivity, MapTile.Type type) {
		// Check North tile
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = 0; i <= sensitivity; i++) {
			MapTile tile = currentView.get(
			new Coordinate(currentPosition.x, currentPosition.y + i));
			if (tile.isType(type)) {
				if(type == MapTile.Type.TRAP) {
					if(((TrapTile) tile).getTrap().equals("parcel"))
						return true;
				}
				return true;
			}
		}
		return false;
	}
	
	//Check if you have a wall in front of you
	public boolean checkWallAhead(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
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
			HashMap<Coordinate, MapTile> currentView, int wallSensitivity) {
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
	
	// check if there is a parcel in the view and return its coordinates
	// This type of function can be generalized to fetch any required target tile and return to the controller
	public Coordinate getParcel(HashMap<Coordinate, MapTile> currentView, CarController controller) {
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		for (int i = -4; i <= 4; i++) {
			for (int j = -4; j <= 4; j++) {
				Coordinate temp = new Coordinate(currentPosition.x +i, currentPosition.y +j);
				MapTile tile = currentView.get(temp);
				if (tile.isType(MapTile.Type.TRAP)) {
					TrapTile trapT = (TrapTile) tile;
					if(trapT.getTrap().equals("parcel")) {
						return(temp);
					}
					
				}
				
			}	
		}
		
		return(null);
	}
}
