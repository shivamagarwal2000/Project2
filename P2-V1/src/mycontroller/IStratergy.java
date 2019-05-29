package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public abstract class IStratergy {
	CarController controller;
	WorldSpatial.Direction orientation;
	HashMap<Coordinate, MapTile> currentView;
	Coordinate target;
	
	
	IStratergy(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView, Coordinate target) {
		
		this.controller = controller;
		this.orientation = orientation;
		this.currentView = currentView;
		this.target = target;
	}
	
}
