package com.mygdx.game;

public class Player extends Shape{ //TODO - MAYBE ADD A SENSE OF SCALE / TIME
	private Vec2D acceleration, direction;

	public Player(float[][] vertices) {
		super(vertices);
		//this.type = "Player";
		this.acceleration = new Vec2D(0, 0);
		this.findDirection();
	}
	
	public void findDirection() { //unit vector TODO - CALL THIS WHENEVER PLAYER IS ROTATED
		float[] centre = this.getCentre();
		float[][] vertices = this.getVertices()[0];
		Vec2D displacement = new Vec2D(vertices[2][0] - centre[0], vertices[2][1] - centre[1]);
		this.setDirection(displacement.scalarMultiply((double)1 / displacement.modulus()));
	}
	
	public void update() {
		//this.applyDrag();
		this.setVelocity(this.getVelocity().vecAdd(this.getAcceleration()).scalarMultiply(0.99));
		this.translate(this.getVelocity());
		this.setAcceleration(new Vec2D(0, 0));
	}
	
	//public void applyDrag() {
	//	double velMod = this.getVelocity().modulus();
	//	this.setAcceleration(this.getAcceleration().vecSub(this.getVelocity().scalarMultiply(velMod * 0.1))); //multiply velmod by something to alter drag
	//}
	
	public void applyThrust() { //only when w pressed
		this.setAcceleration(this.getDirection().scalarMultiply(0.1)); //10 is magnitude of acceleration exerted
	}
	
	
	
	public Vec2D getAcceleration() {
		return acceleration;
	}
	
	public void setAcceleration(Vec2D value) {
		acceleration = value;
	}
	
	public Vec2D getDirection() {
		return direction;
	}
	
	public void setDirection(Vec2D value) {
		direction = value;
	}
}
