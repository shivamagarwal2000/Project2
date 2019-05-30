package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

public class MapInfoCollector {

	private static HashMap<Coordinate, MapTile> allTiles = null;
	private static HashMap<Coordinate, MapTile> visitedTiles = null;
	
	private static MapInfoCollector collector = null;
	
	public static synchronized MapInfoCollector getInstance() {
		if (collector == null) {
			collector = new MapInfoCollector();
		}
		return collector;
	}
	
	public synchronized void initialize(CarController controller) {
		allTiles = controller.getMap();
		visitedTiles = new HashMap<Coordinate, MapTile>();
	}
	
	public synchronized void addVisitedTiles(Coordinate coordinate,
			MapTile mapTile) {
		if (!visitedTiles.containsKey(coordinate)) {
			visitedTiles.put(coordinate, mapTile);
		}
	}
	
	public synchronized void updataAllTiles(Coordinate coordinate,
			MapTile mapTile) {
		if (!allTiles.containsKey(coordinate)) {
			System.err.println("something wrong, system exit");
			System.exit(-1);
		}
		if (!allTiles.get(coordinate).isType(MapTile.Type.TRAP)) {
			allTiles.put(coordinate, mapTile);
		}
	}
}
