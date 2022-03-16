package com.nm;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter left border: ");
		double leftBorder = scanner.nextDouble();
		System.out.println("Enter right border: ");
		double rightBorder = scanner.nextDouble();
		System.out.println("Enter accuracy: ");
		String epsilonString = scanner.next();
		
		double epsilon = Double.parseDouble(epsilonString);
		
		String format = epsilonString.replaceAll("^[1-9]", "0");
		
		DecimalFormat df = new DecimalFormat(format);
		
		NewtonsMethod nm = NewtonsMethod.createNewtonsMethod(leftBorder, rightBorder, epsilon);
		if(nm != null) {
			System.out.println("Result for Newton's method: " + df.format(nm.solve()));
			System.out.println("Iterations: " + nm.getIterations());
		}
		
		IterationsMethod im = IterationsMethod.createIterationsMethod(leftBorder, rightBorder, epsilon);
		if(im != null) {
			System.out.println("Result for iterations method: " + df.format(im.solve()));
			System.out.println("Iterations: " + im.getIterations());
		}
		
		scanner.close();
	}
	
}

class NewtonsMethod {
	private final double leftBorder;
	private final double rightBorder;
	
	private final double epsilon;
	
	private int iterations;
	
	public static NewtonsMethod createNewtonsMethod(double leftBorder, double rightBorder, double epsilon) {
		if(Helper.validateInput(leftBorder, rightBorder, epsilon)) {
			return new NewtonsMethod(leftBorder, rightBorder, epsilon);
		} else return null;
	}
	
	private NewtonsMethod(double leftBorder, double rightBorder, double epsilon) {
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.epsilon = epsilon;
		this.iterations = 0;
	}
	
	public double solve() {
		double x = 0;
		double prev = x;
		if(this.isStartPoint(this.leftBorder)) {
			x = this.leftBorder;
		} else if(this.isStartPoint(this.rightBorder)) {
			x = this.rightBorder;
		}
		
		while(Math.abs(x - prev) > this.epsilon) {
			if(this.isSolved(x)) {
				return x;
			}
			prev = x;
			x = this.getNextArg(x);
			this.iterations++;
		}
		return x;
	}
	
	private boolean isStartPoint(double point) {
		if(this.getResultOfEquation(point) * this.getResultOfSecondDerivative(point) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isSolved(double x) {
		return this.getResultOfEquation(x) == 0;
	}
	
	private double getNextArg(double x) {
		return x - (this.getResultOfEquation(x) / this.getResultOfDirivative(x));
	}
	
	private double getResultOfEquation(double x) {
		return Helper.getResultOfEquation(x);
	}
	
	private double getResultOfDirivative(double x) {
		return Helper.getResultOfDirivative(x);
	}
	
	private double getResultOfSecondDerivative(double x) {
		return Helper.getResultOfSecondDerivative(x);
	}

	public int getIterations() {
		return this.iterations;
	}

}

class IterationsMethod {
	private final double leftBorder;
	private final double rightBorder;
	
	private final double epsilon;
	
	private int iterations;
	
	public static IterationsMethod createIterationsMethod(double leftBorder, double rightBorder, double epsilon) {
		if(Helper.validateInput(leftBorder, rightBorder, epsilon)) {
			return new IterationsMethod(leftBorder, rightBorder, epsilon);
		} else return null;
	}
			
	private IterationsMethod(double leftBorder, double rightBorder, double epsilon) {
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.epsilon = epsilon;
		this.iterations = 0;
	}
	
	public double solve() {
		double x = 0;
		double prev = x;
		if(this.checkRightBorder()) {
			x = this.rightBorder;
		} else if(this.checkLeftBorder()) {
			x = this.leftBorder;
		} else {
			System.out.println("Error in iterations method!");
			return 0.0;
		}
		
		while(Math.abs(x - prev) > this.epsilon) {
			if(this.getResultOfEquation(x) == 0) {
				return x;
			}
			prev = x;
			x = this.getResultOfFi(x);
			this.iterations++;
		}
		
		return x;
	}
	
	private boolean checkRightBorder() {
		return this.getResultOfFiDirivative(this.rightBorder) < 1 &&
				this.getResultOfFiDirivative(this.rightBorder) < this.getResultOfFiDirivative(this.leftBorder);
	}
	
	private boolean checkLeftBorder() {
		return this.getResultOfFiDirivative(this.leftBorder) < 1 &&
				this.getResultOfFiDirivative(this.leftBorder) < this.getResultOfFiDirivative(this.rightBorder);
	}
	
	private double getResultOfEquation(double x) {
		return Helper.getResultOfEquation(x);
	}
	
	private double getResultOfDirivative(double x) {
		return Helper.getResultOfDirivative(x);
	}
	
	private double getResultOfSecondDerivative(double x) {
		return Helper.getResultOfSecondDerivative(x);
	}
	
	private double getResultOfFi(double x) {
		return (Math.pow(x, 3.0)-(3*(Math.pow(x, 2.0)))+3)/(-6);
	}

	private double getResultOfFiDirivative(double x) {
		return ((3*Math.pow(x, 2.0)) - 6*x)/(-6);
	}
	
	public int getIterations() {
		return this.iterations;
	}

}

class Helper {
	static boolean validateInput(double leftBorder, double rightBorder, double epsilon)	{
		if((getResultOfEquation(leftBorder)*getResultOfEquation(rightBorder) > 0) || (rightBorder <= leftBorder)){
			System.out.println("Invalid borders!");
			return false;
		} else if(epsilon <= 0)
		{
			System.out.println("Invalid accuracy!");
			return false;
		} else {
			return true;
		}
	}
	
	public static double getResultOfEquation(double x) {
		return (Math.pow(x, 3.0)-(3*(Math.pow(x, 2.0)))+6*x+3);
	}
	
	public static double getResultOfDirivative(double x) {
		return Math.abs((3*Math.pow(x, 2.0)) - 6*x + 6);
	}
	
	public static double getResultOfSecondDerivative(double x) {
		return 6*x-6;
	}
}