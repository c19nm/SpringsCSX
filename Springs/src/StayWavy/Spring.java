package StayWavy;

import org.opensourcephysics.display.Circle;

public class Spring extends Circle {
	// extends circle to make the balls circles
	// initializes the attributes for a ball
	double k = 0; // k constant
	double m = 0; // mass
	double position = 0; // deltax
	double deltax = 0; // distance
	double ax = 0; // acceleration
	double ay = 0; // acceleration
	double vx = 0; // velocity
	double vy = 0; // velocity
	double v0x = 0; // velocity
	double v0y = 0; // velocity
	double timeStep;
	double x;
	double y;

	public double getVx() {
		return vx;
	}
	
	public void oscY(double f, double y0) { 
		this.y = y0*Math.sin(2*Math.PI*f*this.timeStep); 
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

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
//		this.x = 
//		this.y = 
		return position;
	}

	public void setPosition() {
		this.x = (0.5 * this.getAx() * this.timeStep*this.timeStep)
				+ (this.getVoldX() * this.timeStep) + this.x;
		this.y = (0.5 * this.getAy() * this.timeStep*this.timeStep)
				+ (this.getVoldY() * this.timeStep) + this.y;
	}

	public double getDeltax() {
		return deltax;
	}

	public void setDeltax(double deltax) {
		this.deltax = deltax;
	}

	public double getVoldX() {
		return v0x;
	}

	public void setVoldX(double v0x) {
		this.v0x = v0x;
	}

	public double getVoldY() {
		return v0y;
	}

	public void setVoldY(double v0y) {
		this.v0y = v0y;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}

	public double getDistance(Spring og) {
		double distance;
		double xComp = this.getX() - og.getX();
		double yComp = this.getY() - og.getY();
		distance = Math.sqrt(xComp * xComp + yComp * yComp);
		return distance;
	}

	public double getXDIstance(Spring og) {
		return this.getX() - og.getX();
	}

	public double getYDistance(Spring og) {
		return this.getY() - og.getY();
	}

	/**
	 * Creates a ball with a unique set of attributes. Ball(x position, y position,
	 * x acceleration, y acceleration, x velocity, y velocity, initial velocity,
	 * angle).
	 *
	 */
	public Spring(double k, double m, double ex, double why, double deltax, double vx, double vy, double ax, double ay,
			double v0x, double v0y, double ts) {
		this.k = k;
		this.m = m;
		this.x = ex;
		this.y = why;
		this.deltax = deltax;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
		this.v0y = v0y;
		this.v0x = v0x;
		this.timeStep = ts;
	}

}
