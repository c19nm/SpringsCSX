package SpringsNMKSYJE;

import org.opensourcephysics.display.Circle;

public class Spring extends Circle {
	// extends circle to make the balls circles
	// initializes the attributes for a ball
	double k = 0; // k constant
	double m = 0; // mass
	double position = 0; // deltax
	double deltax = 0; // distance
	double a = 0; // acceleration
	double v = 0; // velocity
	double v0 = 0; // velocity

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getDeltax() {
		return deltax;
	}

	public void setDeltax(double deltax) {
		this.deltax = deltax;
	}
	
	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}
	
	public double getVold() {
		return v0;
	}

	public void setVold(double v0) {
		this.v0 = v0;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	/**
	 * Creates a ball with a unique set of attributes. Ball(x position, y position,
	 * x acceleration, y acceleration, x velocity, y velocity, initial velocity,
	 * angle).
	 *
	 */
	public Spring(double k, double m, double position, double deltax, double v, double a, double v0) {
		this.k = k;
		this.m = m;
		this.position = position;
		this.deltax = deltax;
		this.v = v;
		this.a = a;
		this.v0 = v0;
	}

}
