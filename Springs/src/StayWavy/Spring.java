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
	double cos;
	double sin;
	// double Fx;
	// double Fy;
	double timeStep;
	double x;
	double y;

	public void oscX(double f, double x0, double time) {
		this.x = 100*x0 * Math.sin(2 * Math.PI * f * time);
	}

	public void oscY(double f, double y0, double time) {
		this.y = y0 * Math.sin(2 * Math.PI * f * time);
	}

	public void spin(double sideLength, double negX, double negY, double spinning, double theta) {
		double r = (sideLength / 2) * Math.sqrt(2);
		this.y = this.y + (negY * r * (Math.sin(theta))) / spinning;
		this.x = this.x + (negX * (r - r * (Math.cos(theta)))) / spinning;
	}

	public double getVx() {
		return vx;
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

	public void setPosition() {
		this.x = (0.5 * this.getAx() * this.timeStep * this.timeStep) + (this.getVoldX() * this.timeStep) + this.x;
		this.y = (0.5 * this.getAy() * this.timeStep * this.timeStep) + (this.getVoldY() * this.timeStep) + this.y;
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

	public double getFs(Spring og, double individualRestLength, double K) {
		double Fs = K * (this.getDistance(og) - individualRestLength);
		return Fs;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public void findA(double Fs) {
		this.ax += (Fs * cos) / this.m;
		this.ay += (Fs * sin) / this.m;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}

	public double getDistance(Spring og) {
		double distance;
		double xComp = og.getX() - this.getX();
		double yComp = og.getY() - this.getY();
		distance = Math.sqrt(xComp * xComp + yComp * yComp);
		cos = xComp / distance;
		sin = yComp / distance;
		return distance;
	}

	public double getXDistance(Spring og) {
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
	public Spring(double k, double m, double ex, double why, double v0x, double v0y, double ax, double ay, double ts) {
		this.k = k;
		this.m = m;
		this.x = ex;
		this.y = why;
		this.ax = ax;
		this.ay = ay;
		this.v0y = v0y;
		this.v0x = v0x;
		this.timeStep = ts;
	}

}
