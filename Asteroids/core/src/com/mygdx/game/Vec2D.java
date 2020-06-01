package com.mygdx.game;

public class Vec2D {
	protected double i, j;
	
	public Vec2D(double i, double j) {
		this.i = i;
		this.j = j;
	}
	
	public double modulus() {
		return Math.sqrt(this.getI() * this.getI() + this.getJ() * this.getJ());
	}
	
	public Vec2D vecAdd(Vec2D otherVec) {
		return new Vec2D(this.getI() + otherVec.getI(), this.getJ() + otherVec.getJ());
	}
	
	public Vec2D vecSub(Vec2D otherVec) {
		return new Vec2D(this.getI() - otherVec.getI(), this.getJ() - otherVec.getJ());
	}
	
	public Vec2D scalarMultiply(double value) { //Used specifically for intersection right now
		return new Vec2D(this.getI() * value, this.getJ() * value);
	}
	
	public double dotProduct(Vec2D otherVec) {
		return this.getI() * otherVec.getI() + this.getJ() * otherVec.getJ();
	}
	
	public double getI() {
		return i;
	}
	
	public void setI(double value) {
		i = value;
	}
	
	public double getJ() {
		return j;
	}
	
	public void setJ(double value) {
		j = value;
	}
}
