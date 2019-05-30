package mycontroller;

public class Settings {
	// How many minimum units the wall is away from the player.
	private static int wallSensitivity = 1;
	// Car Speed to move at
	private static final int CAR_MAX_SPEED = 1;
	
	public static int getWallSensitivity() {
		return wallSensitivity;
	}
	
	public static void setWallSensitivity(int sensitivity) {
		wallSensitivity = sensitivity;
	}
	
	public static float getCAR_MAX_SPEED() {
		return CAR_MAX_SPEED;
	}
}
