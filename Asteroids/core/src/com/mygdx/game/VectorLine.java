package com.mygdx.game;

public class VectorLine {
	protected Vec2D position, direction, intersection;
	protected double lambda1, lambda2;
	
	public VectorLine(Vec2D position, Vec2D direction) {
		this.position = position;
		this.direction = direction;
	}
	
	public boolean findIntersection(VectorLine otherLine) {
		Matrix2By2 equationMatrix = new Matrix2By2(new double[][] {{this.getDirection().getI(), -otherLine.getDirection().getI()},
																   {this.getDirection().getJ(), -otherLine.getDirection().getJ()}});
		
		Matrix solutionMatrix = new Matrix(new double[][] {{otherLine.getPosition().getI() - this.getPosition().getI()},
														   {otherLine.getPosition().getJ() - this.getPosition().getJ()}});
		
		if (equationMatrix.findInverse() == true) {
			double[][] solutions = new Matrix(equationMatrix.getInverse()).matrixMultiply(solutionMatrix);
			double lambda1 = solutions[0][0];
			double lambda2 = solutions[1][0];
			this.setLambda1(lambda1);
			this.setLambda2(lambda2);
			this.setIntersection(this.getPosition().vecAdd(this.getDirection().scalarMultiply(lambda1)));
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public Vec2D getPosition() {
		return position;
	}
	
	public Vec2D getDirection() {
		return direction;
	}
	
	public Vec2D getIntersection() {
		return intersection;
	}
	
	public void setIntersection(Vec2D value) {
		intersection = value;
	}
	
	public double getLambda1() {
		return lambda1;
	}
	
	public void setLambda1(double value) {
		lambda1 = value;
	}
	
	public double getLambda2() {
		return lambda2;
	}
	
	public void setLambda2(double value) {
		lambda2 = value;
	}
}
