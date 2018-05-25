package StayWavy;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class FlyFishing extends AbstractSimulation {
	// create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Cello String Simulation");

	// time
	double timeStep = .0000001;
	int counter = 0;

	// 1 meter
	// 100 heavy
	// 10 gram string
	// 100 newtons of tension
	// 200hz frequency
	// 1/1000 timestep
	// shake the end point with a sine function
	// 1cm of movement
	// rest length of each one

	// bungee variables
	double LineRestLength = 0;
	double lineMass = .001;
	double lineLength = 8;
	int Springs = 200;
	double kTotal = 1000;
	double k = kTotal * Springs;
	double springMass = (lineMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	int lastSpring = Springs - 1;
	double springLength = (lineLength / Springs);
	double frequency = 10; // 63
	double y0 = .01;
	double lineParticleRestLength = LineRestLength / Springs;
	double sideLength = .25;
	FishingRod rod;
	double vRestLength = sideLength;
	double rodRestLength = .22;
	double kRatio = (sideLength - rodRestLength) / ((2 * sideLength) - (Math.sqrt(2) * rodRestLength));
	double Kh = 8E9;
	double Kv = 5E8;
	double Kvt = 10;
	double Kdt = 1E7;
	double Kht = 1E6;
	double Kl = 300;
	double Kd = Kh * kRatio;
	double FirstTime = 0;
	double timeSpun = 1000;
	int NumofParticles = 6;
	int neg = 1;
	double b = .2;
	double drag = 0;
	double Ax = 0;
	double Ay = 0;
	double doubleLoopTimes = 0;

	// frequency

	@Override
	public void initialize() {
		// characteristics of the display frame
		d.setPreferredMinMax(-1, 2, -1, 9);
		d.setVisible(true);
		frequency = control.getDouble("frequency");
		// makes a new rod
		rod = new FishingRod(100, 3, NumofParticles, 1.5, sideLength, timeStep);
		for (int i = 0; i < rod.array.length; i++) {
			rod.array[i][0].setXY(rod.array[i][0].x, rod.array[i][0].y);
			rod.array[i][0].pixRadius = 3;
			d.addDrawable(rod.array[i][0]);
			rod.array[i][1].setXY(rod.array[i][1].x, rod.array[i][1].y);
			rod.array[i][1].pixRadius = 3;
			d.addDrawable(rod.array[i][1]);
		}
		// for (int i = 0; i < Springs; i++) {
		// Spring s = new Spring(k, springMass, -(i * springLength), sideLength *
		// (NumofParticles - 1), 0, 0, 0, 0,
		// timeStep);
		// s.pixRadius = 3;
		// s.color = Color.cyan;
		// s.setXY(s.x, s.y);
		// bungee.add(s);
		// d.addDrawable(s);
		// if (i == lastSpring) {
		// s.m = .0025;
		// }
		// }
	}

	/**
	 * Goes through code repeatedly
	 */
	protected void doStep() {

		for (int sean = 0; sean < 100; sean++) {
			// speeding it up
			this.setDelayTime(1);

			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; j < rod.array[i].length; j++) {
					Spring point = rod.array[i][j];
					point.ax = 0;
					point.ay = 0;
					// if (FirstTime < timeSpun) {
					if (i == 0 && j == 0) {
						// point.spin(sideLength, -1, 1, timeSpun, (Math.PI / 4));
						// if (frequency * FirstTime * timeStep < 1) {
						// if (Math.sin(2 * Math.PI * frequency * doubleLoopTimes * timeStep) > 0) {
						// point.oscX(frequency, y0, FirstTime * timeStep);
						// if (FirstTime < 10000) {
						// point.setAx(100 / point.m);
						// }
						// point.setX(point.x);
						// doubleLoopTimes++;
						// }
						// System.out.println(Math.sin(2 * Math.PI * frequency * doubleLoopTimes *
						// timeStep));

						// else if (bungee.get(lastSpring).vx < 0 && bungee.get(lastSpring).x >
						// bungee.get(lastSpring - 1).x) {
						// point.oscX(frequency, y0, doubleLoopTimes * timeStep);
						// point.setX(point.x);
						// doubleLoopTimes++;
						// }

						// }
					} else if (i == 0 && j == 1) {
						// point.spin(sideLength, -1, -1, timeSpun, (Math.PI / 4));
						point.x = rod.array[0][0].x + sideLength;
						point.setX(point.x);
					} else if (i == 1 && j == 0) {

						// point.spin(sideLength, 1, 1, timeSpun, (Math.PI / 4));
					} else if (i == 1 && j == 1) {
						// point.spin(sideLength, 1, -1, timeSpun, (Math.PI / 4));

					}

					else if (i <= (rod.array.length - 2) && i > 1 && j == 0) {
						// i-1, 0
						// i+1, 0
						// i, 1
						// i+1, 1
						// i-1, 1
						point.findA(point.getFs(rod.array[i - 1][0], sideLength, Kv));
						point.findA(point.getFs(rod.array[i + 1][0], sideLength, Kv));
						point.findA(point.getFs(rod.array[i][1], rodRestLength, Kh));
						// point.findA(point.getFs(rod.array[i + 1][1], rodRestLength, Kd));
						// point.findA(point.getFs(rod.array[i - 1][1], rodRestLength, Kd));
					} else if (i <= (rod.array.length - 2) && i > 1 && j == 1) {
						// i-1, 0
						// i+1, 0
						// i, 0
						// i+1, 1
						// i-1, 1
						// point.findA(point.getFs(rod.array[i - 1][0], rodRestLength, Kd));
						// point.findA(point.getFs(rod.array[i + 1][0], rodRestLength, Kd));
						point.findA(point.getFs(rod.array[i][0], rodRestLength, Kh));
						point.findA(point.getFs(rod.array[i + 1][1], sideLength, Kv));
						point.findA(point.getFs(rod.array[i - 1][1], sideLength, Kv));
					} else if (i == (rod.array.length - 1) && j == 0) {
						point.findA(point.getFs(rod.array[i][1], sideLength, Kht));
						point.findA(point.getFs(rod.array[i - 1][0], sideLength, Kvt));
						// point.findA(point.getFs(rod.array[i - 1][1], Math.sqrt(2) * sideLength,
						// Kdt));
						// point.findA(point.getFs(bungee.get(lastSpring), Math.sqrt(2) * sideLength,
						// Kl));
					} else if (i == (rod.array.length - 1) && j == 1) {
						rod.array[i - 1][0].color = Color.MAGENTA;
						rod.array[i - 1][1].color = Color.GREEN;
						point.findA(point.getFs(rod.array[i][0], sideLength, Kht));
						// point.findA(point.getFs(rod.array[i - 1][0], Math.sqrt(2) * sideLength,
						// Kdt));
						point.findA(point.getFs(rod.array[i - 1][1], sideLength, Kvt));
					}
					// velocity
					// System.out.println(point.getAx());
					point.setVx(point.getAx() * timeStep + point.getVoldX());
					point.setVy(point.getAy() * timeStep + point.getVoldY());
					// sets the old velocity
					point.setVoldX(point.getVx());
					point.setVoldY(point.getVy());
					doubleLoopTimes++;
				}
			}
			// goes through each spring
			// for (int i = 0; i < Springs; i++) {
			// // different forces for the end springs
			// // the first spring
			// Spring s = bungee.get(i);
			// if (i == 0) {
			// s.x = rod.array[NumofParticles - 1][0].x;
			// s.y = rod.array[NumofParticles - 1][0].y;
			// s.setX(rod.array[NumofParticles - 1][0].x);
			// s.setY(rod.array[NumofParticles - 1][0].y);
			// }
			// // for all of the other springs
			// else if (i <= lastSpring - 1) {
			// // System.out.println(i);
			// Spring sAfter = bungee.get(i + 1);
			// Spring sBefore = bungee.get(i - 1);
			// // a = f/m
			// // FX: a =( [-k*d(i-1) * (x(i)-x(i-1))/d(i-1)] + [k*d(i) *
			// (x(i+1)-x(i))/d(i)]
			// // )/m
			// // FY: a =( [-k*d(i-1) * (y(i)-y(i-1))/d(i-1)] + [k*d(i) *
			// (x(i+1)-x(i))/d(i)]
			// // )/m
			//
			// s.setAx(((-s.getK() * (s.getDistance(sBefore) - lineParticleRestLength)
			// * (s.getX() - sBefore.getX()) / s.getDistance(sBefore))
			// + s.getK() * (s.getDistance(sAfter) - lineParticleRestLength) *
			// (sAfter.getX() - s.getX())
			// / s.getDistance(sAfter))
			// / s.getM());
			//
			// s.setAy((((-s.getK() * (s.getDistance(sBefore) - lineParticleRestLength)
			// * (s.getY() - sBefore.getY()) / s.getDistance(sBefore))
			// + s.getK() * (s.getDistance(sAfter) - lineParticleRestLength) *
			// (sAfter.getY() - s.getY())
			// / s.getDistance(sAfter))
			// / s.getM()) - 9.81);
			// }
			// // the last one
			// else if (i == lastSpring) {
			// Spring sBefore = bungee.get(i - 1);
			// Ax = -s.getK() * (s.getDistance(sBefore) - lineParticleRestLength) *
			// (s.getX() - sBefore.getX())
			// / s.getDistance(sBefore);
			// Ay = -s.getK() * (s.getDistance(sBefore) - lineParticleRestLength) *
			// (s.getY() - sBefore.getY())
			// / s.getDistance(sBefore);
			// // if (Ax > 0) {
			// // neg = -1;
			// // } else {
			// // neg = 1;
			// // }
			// // drag = neg * b * s.getVx() * s.getVx();
			// s.setAx((Ax + drag) / s.getM());
			//
			// // if (Ay > 0) {
			// // neg = -1;
			// // } else {
			// // neg = 1;
			// // }
			// // // bv^2
			// // drag = neg * b * s.getVy() * s.getVy();
			// s.setAy(((Ay + drag) / s.getM()) - 9.81);
			//
			// // THE LINE IS DISAPPEARING BEACUSE OF THE WAY THE PARTICLE FALLS
			// // System.out.println(drag);
			// // System.out.println(s.getDistance(sBefore));
			// // System.out.println(" ");
			// s.color = Color.blue;
			// }
			// // resets the velocity: v(t) = at+v0
			// s.setVx(s.getAx() * timeStep + s.getVoldX());
			// s.setVy(s.getAy() * timeStep + s.getVoldY());
			// // sets the old velocity
			// s.setVoldX(s.getVx());
			// s.setVoldY(s.getVy());
			// }
			FirstTime++;
			// POSITION
			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; j < rod.array[i].length; j++) {
					Spring point = rod.array[i][j];
					point.setPosition();
					point.setXY(point.x, point.y);
				}
			}
			// for (int i = 0; i < Springs; i++) {
			// Spring s = bungee.get(i);
			// s.setPosition();
			// s.setXY(s.x, s.y);
			// }
		}
		counter++;
	}

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("frequency", frequency);
		d.clearData();
		d.clearDrawables();
	}

	/**
	 * Runs the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new FlyFishing());

	}
}
