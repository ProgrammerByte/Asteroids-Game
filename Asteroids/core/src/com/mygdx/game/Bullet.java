package com.mygdx.game;

public class Bullet extends Shape{
	private int lifeSpan;

	public Bullet(float[][] vertices, Vec2D playerDirection) {
		super(vertices);
		this.setVelocity(playerDirection.scalarMultiply(5));
		lifeSpan = 60;
	}
	
	public void update() {
		this.translate(this.getVelocity());
		this.setLifeSpan(this.getLifeSpan() - 1);
		if (this.getLifeSpan() == 0) {
			this.setHasExpired(true); //either no life left or has collided
		}
	}
	
	
	
	public int getLifeSpan() {
		return lifeSpan;
	}
	
	public void setLifeSpan(int value) {
		lifeSpan = value;
	}
}
