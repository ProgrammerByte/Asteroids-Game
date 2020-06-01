package com.mygdx.game;

public class Matrix {
double[][] contents;
	
	public Matrix(double[][] values) {
		contents = values.clone(); //REPLACE THIS WITH A HARD CLONE IF NEEDED;
	}
	
	public double[][] matrixMultiply(Matrix otherMatrix) { //RETURNS THE CONTENTS OF THE RESULTING MATRIX INSTEAD OF A MATRIX OBJECT
		double[][] thisContents = this.getContents();
		double[][] otherContents = otherMatrix.getContents();
		
		double[][] resultContents = new double[thisContents.length][otherContents[0].length]; //DEFINES THE SIZE OF THE RESULTING MATRIX
		
		for (int a = 0; a < resultContents.length; a++) {
			for (int b = 0; b < resultContents[0].length; b++) {
				double result = 0;
				for (int c = 0; c < thisContents[0].length; c++) {
					result += thisContents[a][c] * otherContents[c][b];
				}
				resultContents[a][b] = result;
			}
		}
		
		return resultContents;
	}
	
	
	
	
	public double[][] getContents() {
		return contents;
	}
	
	public void setContents(double[][] values) {
		contents = values;
	}
}
