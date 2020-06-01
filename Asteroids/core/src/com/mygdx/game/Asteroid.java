package com.mygdx.game;

import java.util.Random;

public class Asteroid extends Shape{
	private float[][] originalVertices;
	private int size;
	private double angle; //trajectory

	public Asteroid(float[][] vertices, float[] centre, int size, Vec2D velocity, double angle) {
		super(vertices); //screw this xd (code is very inefficient like this)
		this.originalVertices = vertices;
		
		float[][] tempVertices = new float[vertices.length][2];
		for (int i = 0; i < vertices.length; i++) {
			tempVertices[i] = vertices[i].clone();
		}
		
		if (size > 1) {
			for (int i = 0; i < tempVertices.length; i++) {
				tempVertices[i][0] *= size;
				tempVertices[i][1] *= size;
			}
			this.setVertices(new float[][][] {tempVertices});
			this.findCentre();
			this.findBoundingBox();
		}
		float[] currentPos = this.getCentre();
		this.translate(new Vec2D(centre[0] - currentPos[1], centre[1] - currentPos[1]));
		
		this.setVelocity(velocity);
		this.size = size;
		this.angle = angle;
	}
	
	public Asteroid[] split(Random random) { //returns 2 daughter asteroids
		int currentSize = this.getSize();
		this.setHasExpired(true);
		
		if (size > 1) {
			double originalAngle = this.getAngle();
			double angle1 = random.nextDouble() * Math.PI / 4;
			double angle2 = -random.nextDouble() * Math.PI / 4;
			double velMod = this.getVelocity().modulus();
			Asteroid[] result = new Asteroid[2];
			
			//conservation of momentum
			Matrix2By2 equationMatrix = new Matrix2By2(new double[][] {{Math.sin(angle1), Math.sin(angle2)},
																	   {Math.cos(angle1), Math.cos(angle2)}});
			Matrix solutionMatrix = new Matrix(new double[][] {{0},{2 * velMod}});
			
			equationMatrix.findInverse();
			Matrix inverseMatrix = new Matrix(equationMatrix.getInverse());
			double[][] solutions = inverseMatrix.matrixMultiply(solutionMatrix);
			double velMod1 = solutions[0][0];
			double velMod2 = solutions[1][0];
			
			angle1 += originalAngle;
			angle2 += originalAngle;
			
			Vec2D vel1 = new Vec2D(velMod1 * Math.cos(angle1), velMod1 * Math.sin(angle1));
			Vec2D vel2 = new Vec2D(velMod2 * Math.cos(angle2), velMod2 * Math.sin(angle2));
			
			result[0] = new Asteroid(this.getOriginalVertices(), this.getCentre(), currentSize / 2, vel1, angle1);
			result[1] = new Asteroid(this.getOriginalVertices(), this.getCentre(), currentSize / 2, vel2, angle2);
			return result;
		}
		else {
			return new Asteroid[0];
		}
	}
	
	
	public float[][] getOriginalVertices() {
		return originalVertices;
	}
	
	public int getSize() {
		return size;
	}
	
	public double getAngle() {
		return angle;
	}
}
