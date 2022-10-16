package javaML.supervised.structures.networkElements;

public abstract class ActivationFunctions {
	
	protected abstract double activate(double x);
	protected abstract double derivative(double x);
	
	protected static double[] activate(ActivationFunctions f, double[] x) {
		double[] ret = new double[x.length];
		for(int index = 0; index < x.length; index++) {
			ret[index] = f.activate(x[index]);
		}
		
		return ret;
	}
	
	protected static double[] derivative(ActivationFunctions f, double[] x) {
		double[] ret = new double[x.length];
		for(int index = 0; index < x.length; index++) {
			ret[index] = f.derivative(x[index]);
		}
		
		return ret;
	}
}

class Linear extends ActivationFunctions {
	
	@Override
	protected double activate(double x) {
		return x;
	}
	
	@Override
	protected double derivative(double x) {
		return 1;
	}
}

class ReLU extends ActivationFunctions {
	
	@Override
	protected double activate(double x) {
		if(x > 0) return x;
		return 0;
	}
	
	@Override
	protected double derivative(double x) {
		if(x > 0) return 1;
		return 0;
	}
}

class Tanh extends ActivationFunctions {
	
	@Override
	protected double activate(double x) {
		return Math.tanh(x);
	}
	
	@Override
	protected double derivative(double x) {
		return 1 / Math.pow(Math.cosh(x), 2);
	}
}

class Sigmoid extends ActivationFunctions {
	
	@Override
	protected double activate(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	@Override
	protected double derivative(double x) {
		return this.activate(x) * (1 - this.activate(x));
	}
}