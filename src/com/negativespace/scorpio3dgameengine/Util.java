package com.negativespace.scorpio3dgameengine;

public class Util {
	
	private final static double PI = 3.14159265358979323846;
	
	public static float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }
     
    public static float degreesToRadians(float degrees) {
        return degrees * (float)(PI / 180d);
    }

}
