package com.mygdx.game;

public class Shape {
	protected float[][][] vertices; //i.e. {real, fake, fake, fake} (screenwrap)
	protected float[][] boundingBox; //xmin, ymin, xmax, ymax
	protected float[] centre; //MAYBE CHANGE TO DOUBLE IF PROBLEMS ARISE!!!
	//protected String type;
	protected boolean hasExpired;
	protected Vec2D velocity; //per frame for now
	
	public Shape(float[][] vertices) {
		this.vertices = new float[][][] {vertices};
		this.findCentre();
		this.findBoundingBox();
		//this.type = "Shape"; //default value - determines the type of shape i.e. player, asteroid, etc.
		this.velocity = new Vec2D(0, 0);
		this.hasExpired = false;
	}
	
	public void update() {
		this.translate(this.getVelocity());
	}
	
	public void findCentre() { //only of real shape
		float[][] vertices = this.getVertices()[0];
		float[] centre = {0, 0};
		int count = vertices.length;
		
		for (int i = 0; i < count; i++) {
			centre[0] += vertices[i][0];
			centre[1] += vertices[i][1];
		}
		centre[0] /= count;
		centre[1] /= count;
		this.setCentre(centre);
	}
	
	public void findBoundingBox() { //only real shape
		float[][] vertices = this.getVertices()[0];
		float xMin = vertices[0][0], xMax = vertices[0][0], yMin = vertices[0][1], yMax = vertices[0][1];
		
		for (int i = 1; i < vertices.length; i++) {
			if (vertices[i][0] < xMin) {
				xMin = vertices[i][0];
			}
			else if (vertices[i][0] > xMax) {
				xMax = vertices[i][0];
			}
			
			if (vertices[i][1] < yMin) {
				yMin = vertices[i][1];
			}
			else if (vertices[i][1] > yMax) {
				yMax = vertices[i][1];
			}
		}
		
		this.setBoundingBox(new float[][] {{xMin, yMin}, {xMax, yMax}});
	}
	
	public float[][] translateArray(float[][] array, Vec2D vector) { //of coordinates, only real shapes need to be translated / moved
		float[][] result = new float[array.length][2];
		for (int i = 0; i < array.length; i++) {
			result[i][0] = (float) (array[i][0] + vector.getI());
			result[i][1] = (float) (array[i][1] + vector.getJ());
		}
		return result;
	}
	public void translate(Vec2D vector) {
		this.setCentre(translateArray(new float[][] {this.getCentre()}, vector)[0]);
		this.setBoundingBox(translateArray(this.getBoundingBox(), vector));
		this.setVertices(new float[][][] {translateArray(this.getVertices()[0], vector)}); //produce duplicate shapes after this operation
	}
	
	public void rotate(double angle) {
		Matrix rotationMatrix = new Matrix(new double[][] {{Math.cos(angle), -Math.sin(angle)},
														  {Math.sin(angle), Math.cos(angle)}});
		float[] centre = this.getCentre();
		float[][] vertices = this.translateArray(this.getVertices()[0], new Vec2D(-centre[0], -centre[1]));
		double[][] multiplied;
		Matrix point;
		for (int i = 0; i < vertices.length; i++) {
			point = new Matrix(new double[][] {{vertices[i][0]}, {vertices[i][1]}});
			multiplied = rotationMatrix.matrixMultiply(point);
			vertices[i][0] = (float)multiplied[0][0];
			vertices[i][1] = (float)multiplied[1][0];
		}
		this.setVertices(new float[][][] {this.translateArray(vertices, new Vec2D(centre[0], centre[1]))});
		this.findBoundingBox();
	}
	
	public void screenWrap(float height, float width) { //of window TODO - MAYBE HALF THE NUMBER OF IF STATEMENTS, TRANSLATION AND ROTATION BEFORE THIS IS CALLED via update routine
		float[][] boundingBox = this.getBoundingBox();
		if (boundingBox[0][0] < 0 || boundingBox[0][1] < 0 || boundingBox[1][0] > width || boundingBox[1][1] > height) { //only check for screenwrap if bounding box does not lie perfectly within window
			float[] centre = this.getCentre(); //ensures centre of real shape remains in bounds
			if (centre[0] > width) {
				this.translate(new Vec2D(-width, 0));
			}
			else if (centre[0] < 0) {
				this.translate(new Vec2D(width, 0));
			}
			
			if (centre[1] > height) {
				this.translate(new Vec2D(0, -height));
			}
			else if (centre[1] < 0) {
				this.translate(new Vec2D(0, height));
			}
			
			//then add fake shapes here
			Vec2D farShape = new Vec2D(0, 0);
			if (boundingBox[0][0] < 0) {
				farShape.setI(width);
			}
			else if (boundingBox[1][0] > width) {
				farShape.setI(-width);
			}
			
			if (boundingBox[0][1] < 0) {
				farShape.setJ(height);
			}
			else if (boundingBox[1][1] > height) {
				farShape.setJ(-height);
			}
			
			
			float[][] vertices = this.getVertices()[0];
			if (farShape.getI() != 0 && farShape.getJ() != 0) {
				//use both
				this.setVertices(new float[][][] {vertices, translateArray(vertices, farShape), translateArray(vertices, new Vec2D(farShape.getI(), 0)), translateArray(vertices, new Vec2D(0, farShape.getJ()))});
			}
			else if (farShape.getI() != 0 || farShape.getJ() != 0) {
				//use one
				this.setVertices(new float[][][] {vertices, translateArray(vertices, farShape)});
			} //TODO - DO COLLISION DETECTION AND RENDERING AFTER THIS METHOD IS CALLED!!!
		}
		else {
			this.setVertices(new float[][][] {this.getVertices()[0]}); //ensures that there are no fake shapes when they are not needed TODO - MAYBE NOT NEEDED
		}
	}
	
	
	
	
	public float[][][] getVertices() {
		return vertices;
	}
	
	public void setVertices(float[][][] value) {
		vertices = value;
	}
	
	public float[][] getBoundingBox() {
		return boundingBox;
	}
	
	public void setBoundingBox(float[][] value) {
		boundingBox = value;
	}
	
	public float[] getCentre() {
		return centre;
	}
	
	public void setCentre(float[] value) {
		centre = value;
	}
	
	public Vec2D getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vec2D value) {
		velocity = value;
	}
	
	public boolean getHasExpired() {
		return hasExpired;
	}
	
	public void setHasExpired(boolean value) {
		hasExpired = value;
	}
	
	//public String getType() {
	//	return type;
	//}
}
