package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.WorldSpatial;


// This class is the implementation of health conservation stratergy
public class HealthConsStratergy extends IStratergy {
	
	// variable to store the the map with the weight of each tile
	HashMap<Coordinate, Integer> weightedMap;
	
	HealthConsStratergy(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView, Coordinate target) {
		super(controller, orientation, currentView, target);
		HashMap<Coordinate, Integer> weightedMap = new HashMap<Coordinate, Integer>();
	}
	
	// generate the weighted map based on the type of the tile type
	public void generateWeightedMap() {
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		// run through every tile in the current view
		for (int i = -4; i <= 4; i++) {
			for (int j = -4; j <= 4; j++) {
				Coordinate temp = new Coordinate(currentPosition.x +i, currentPosition.y +j);
				MapTile tile = currentView.get(temp);
				// Assign specific weights to the tiles in the map
				if (tile.isType(MapTile.Type.TRAP)) {
					TrapTile trapT = (TrapTile) tile;
					if(trapT.getTrap().equals("parcel")) {
						weightedMap.put(temp, 0);
					}
					else if(trapT.getTrap().equals("water") || trapT.getTrap().equals("health")) {
						weightedMap.put(temp, 0);
					}
					else if (trapT.getTrap().equals("lava")) {
						weightedMap.put(temp, 25);
					}
				}
				else if(tile.isType(MapTile.Type.ROAD)) {
					weightedMap.put(temp,5);
				}
				else if(tile.isType(MapTile.Type.WALL)) {
					weightedMap.put(temp, 100000);
				}
				else {
					weightedMap.put(temp, 5);
				}
				
			}	
		}
	}
	
	// generate an optimal path in the map using the weighted map and source and target tile
	public void generatePath() {
		
	}
}
