package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class FuelConsStratergy extends IStratergy {
	
	// variable to store the the map with the weight of each tile
	HashMap<Coordinate, Integer> weightedMap;
	int[][] dArray = new int[81][81];
	int src, dest;
	
	FuelConsStratergy(CarController controller, WorldSpatial.Direction orientation,
			HashMap<Coordinate, MapTile> currentView, Coordinate target) {
		super(controller, orientation, currentView, target);
		weightedMap = new HashMap<Coordinate, Integer>();
	}
	
	// generate the weighted map based on the type of the tile
	public void generateWeightedMap() {
		// run through every tile in the current view
		for (Coordinate var : currentView.keySet()) {
				
			MapTile tile = currentView.get(var);
				
			// Assign specific weights to the tiles in the map
			if (tile.isType(MapTile.Type.TRAP)) {
				TrapTile trapT = (TrapTile) tile;
				if(trapT.getTrap().equals("parcel")) {
					weightedMap.put(var, 5);
				}
				else if(trapT.getTrap().equals("water") || trapT.getTrap().equals("health")) {
					weightedMap.put(var, 1);
				}
				// Do not consider lava as a danger in this case so we put lesser weight to it
				else if (trapT.getTrap().equals("lava")) {
					weightedMap.put(var, 10);
				}
			}
			else if(tile.isType(MapTile.Type.ROAD)) {
				weightedMap.put(var,10);
			}
			else if(tile.isType(MapTile.Type.WALL)) {
				weightedMap.put(var, 100000);
			}
			else {
				weightedMap.put(var, 100);
			}
					
		}
	}
	
	public void generateSourceAndDestination(Coordinate cur, Coordinate tar) {
		int count = 0;
		
		for (Coordinate var : weightedMap.keySet()) {
			
			if(var.equals(cur)) {
				src = count;
			}
			else if(var.equals(tar)) {
				dest = count;
			}
			count++;
		}
		
	}
	
	// generate a 81*81 matrix of the vertices that contains the possible paths to apply dijkstra on it
	public void generateDArray() {
		int row = 0, col = 0;
		for(Coordinate var1 : weightedMap.keySet()) {
			col = 0;
			for(Coordinate var2 : weightedMap.keySet()) {
				if((var2.x == var1.x + 1 && var2.y == var1.y) || (var2.x == var1.x - 1 && var2.y == var1.y) 
						|| (var2.x == var1.x && var2.y == var1.y + 1) || (var2.x == var1.x && var2.y == var1.y - 1)) {
					dArray[row][col] = Math.abs(weightedMap.get(var1) - weightedMap.get(var2));
					if (Math.abs(weightedMap.get(var1) - weightedMap.get(var2)) == 0) {
						dArray[row][col] = 1;
					}
				}
				else {
					dArray[row][col] = 0;
				}
				col++;
			}
			row++;
		}
		
	}
	
	// Computes the minimum distance
	public int minDistance(int dist[], Boolean b[]) {
		int min = Integer.MAX_VALUE, index = -1;
		for(int x = 0; x < 81; x++) {
			if (b[x] == false && dist[x] <= min) {
				min = dist[x];
				index = x;
			}
		}
		return index;
	}
	
	public void printGraph(int dist[], int x) {
//		System.out.println("Distance from source  "+ src +"to Destination is  "+dest);
//		
//		System.out.println(dest + " tt "+ dist[dest]);
		
	}
	
	//returns a int array of indexes from src to dest.
	public ArrayList<Integer> backtrackPath(int[] previousIndex){
		
		ArrayList<Integer> reversePath = new ArrayList<>();
		ArrayList<Integer> path = new ArrayList<>();

		int index = dest;

		while(index != src){
			reversePath.add(index);
			index = previousIndex[index];
		}
		for(Integer i: reversePath){
			path.add(i);
		}
		return path;
	}

	
		//Converts the indexes to coordinates.
	public ArrayList<Coordinate> convertIndexToCoordinate(ArrayList<Integer>indexes){

		ArrayList<Coordinate> followCoordinates = new ArrayList<>();
		int count = -1;

		for(Integer i: indexes){
			count = 0;
			for (Coordinate keys : currentView.keySet()) {
				if( count == i){
					followCoordinates.add(keys);
					count += 1;
					break;
				}
					count += 1;
			}
		}
		return followCoordinates;
	}

	
	// apply dijkstra's algorithm to the dArray that has the 
	public ArrayList<Coordinate> dijkstra() {
		
		int dist[] = new int[81];
		
		ArrayList<Coordinate> followCoordinates = new ArrayList<>();
		ArrayList<Integer> pathList = new ArrayList<>();
		
		// Initialize every node as unvisited and make it unreachable at first
		Boolean b[] = new Boolean[81];
		
		// Array of the previous indexes that can be used to generate the coordinates of paths
		int path[] = new int[81];
		
		// Initialize the distance array to vary large value and set every node as unvisited
		for (int i = 0; i < 81; i++) {
			dist[i] = Integer.MAX_VALUE;
			b[i] = false;
		}
		// Initialize the src as local variable and put the distance of it to 0
		int src = this.src;
		dist[src] = 0;
		
		// Find the shortest distance and shortest paths
		for (int i = 0; i < 81; i++) {
			
			// Generate minimum distance for the given and update the node as visited
			int u = minDistance(dist, b);
			b[u] = true;
			
			// Check against every element and only update distance if its unvisited and the new distance
			// is less than the computed distance
			for (int x = 0; x < 81; x++) {
				if(!b[x] && dArray[u][x] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + dArray[u][x] < dist[x]) {
					
					// update the distance
					dist[x] = dist[u] + dArray[u][x];
					
					// update the path
					path[x] = u;
				}
			}
			printGraph(dist, 81);
			
		}
		pathList = backtrackPath(path);
		followCoordinates = convertIndexToCoordinate(pathList);
		return followCoordinates;
	}
}
