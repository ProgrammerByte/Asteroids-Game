package com.mygdx.game;

public class Matrix2By2 extends Matrix {
	protected double[][] inverse;

	public Matrix2By2(double[][] values) {
		super(values);
	}
	
	public boolean findInverse() {
		double[][] contents = this.getContents().clone();
		double a = contents[0][0], b = contents[0][1], c = contents[1][0], d = contents[1][1];
		double determinant = a * d - b * c;
		
		if (determinant != 0) {
			double[][] results = {{d / determinant, -b / determinant},
								  {-c / determinant, a / determinant}};
			this.setInverse(results);
			return true;
		}
		else {
			return false;
		}
	}
	
	public double[][] getInverse() {
		return inverse;
	}
	
	public void setInverse(double[][] value) {
		inverse = value;
	}
}
