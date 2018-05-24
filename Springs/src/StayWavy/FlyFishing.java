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
	double timeStep = .00001;
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
	double LineRestLength = .05;
	double lineMass = .0127;
	double lineLength = 1;
	int Springs = 30;
	double kTotal = 1000;
	double k = kTotal * Springs;
	double springMass = (lineMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	int lastSpring = Springs - 1;
	double springLength = (lineLength / Springs);
	double frequency = 40; // 63
	double y0 = .01;
	double lineParticleRestLength = LineRestLength / Springs;
	double sideLength = .25;
	FishingRod rod;
	double rodRestLength = sideLength;
	double kRatio = (sideLength - rodRestLength) / ((2 * sideLength) - (Math.sqrt(2) * rodRestLength));
	double Kh = 8000000;
	double Kv = 800000000;
	double Kd = Kh * kRatio;
	double FirstTime = 0;
	double timeSpun = 1000;
	int NumofParticles = 50;
	int neg = 1;
	double b = .2;
	double drag = 0;
	double Ax = 0;
	double Ay = 0;

	// frequency

	@Override
	public void initialize() {
		// characteristics of the display frame
		d.setPreferredMinMax(-1, 2, -1, 6.5);
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
		for (int i = 0; i < Springs; i++) {
			Spring s = new Spring(k, springMass, -(i * springLength), sideLength * (NumofParticles - 1), 0, 0, 0, 0,
					timeStep);
			s.pixRadius = 3;
			s.color = Color.cyan;
			s.setXY(s.x, s.y);
			bungee.add(s);
			d.addDrawable(s);
			if (i == lastSpring) {
				s.m = 10;
				s.v0x = 40;
			}
		}
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
						point.oscX(frequency, y0, FirstTime * timeStep);
						point.setX(point.x);
					} else if (i == 0 && j == 1) {
						// point.spin(sideLength, -1, -1, timeSpun, (Math.PI / 4));
						point.x = rod.array[0][0].x + sideLength;
						point.setX(point.x);
					}
					// else if (i == 1 && j == 0) {
					// // point.spin(sideLength, 1, 1, timeSpun, (Math.PI / 4));
					// } else if (i == 1 && j == 1) {
					// // point.spin(sideLength, 1, -1, timeSpun, (Math.PI / 4));
					// }
					// }

					else if (i <= (rod.array.length - 2) && i > 0 && j == 0) {
						// i-1, 0
						// i+1, 0
						// i, 1
						// i+1, 1
						// i-1, 1
						point.findA(point.getFs(rod.array[i - 1][0], rodRestLength, Kv));
						point.findA(point.getFs(rod.array[i + 1][0], rodRestLength, Kv));
						point.findA(point.getFs(rod.array[i][1], rodRestLength, Kh));
						point.findA(point.getFs(rod.array[i + 1][1], rodRestLength, Kd));
						point.findA(point.getFs(rod.array[i - 1][1], rodRestLength, Kd));
					} else if (i <= (rod.array.length - 2) && i > 0 && j == 1) {
						// i-1, 0
						// i+1, 0
						// i, 0
						// i+1, 1
						// i-1, 1
						point.findA(point.getFs(rod.array[i - 1][0], rodRestLength, Kd));
						point.findA(point.getFs(rod.array[i + 1][0], rodRestLength, Kd));
						point.findA(point.getFs(rod.array[i][0], rodRestLength, Kh));
						point.findA(point.getFs(rod.array[i + 1][1], rodRestLength, Kv));
						point.findA(point.getFs(rod.array[i - 1][1], rodRestLength, Kv));
					} else if (i == (rod.array.length - 1) && j == 0) {
						point.findA(point.getFs(rod.array[i][1], rodRestLength, Kh));
						// point.findA(point.getFs(rod.array[i - 1][0], rodRestLength, Kv));
						point.findA(point.getFs(rod.array[i - 1][1], rodRestLength, Kd));
					} else if (i == (rod.array.length - 1) && j == 1) {
						point.findA(point.getFs(rod.array[i][0], rodRestLength, Kh));
						point.findA(point.getFs(rod.array[i - 1][0], rodRestLength, Kd));
						// point.findA(point.getFs(rod.array[i - 1][1], rodRestLength, Kv));
					}
					// velocity
					// System.out.println(point.getAx());
					point.setVx(point.getAx() * timeStep + point.getVoldX());
					point.setVy(point.getAy() * timeStep + point.getVoldY());
					// sets the old velocity
					point.setVoldX(point.getVx());
					point.setVoldY(point.getVy());

				}
			}
			// goes through each spring
			for (int i = 0; i < Springs; i++) {
				// different forces for the end springs
				// the first spring
				Spring s = bungee.get(i);
				if (i == 0) {
					s.x = rod.array[NumofParticles - 2][0].x;
					s.y = rod.array[NumofParticles - 2][0].y;
					s.setX(rod.array[NumofParticles - 2][0].x);
					s.setY(rod.array[NumofParticles - 2][0].y);
				}
				// for all of the other springs
				else if (i <= lastSpring - 1) {
					// System.out.println(i);
					Spring sAfter = bungee.get(i + 1);
					Spring sBefore = bungee.get(i - 1);
					// a = f/m
					// FX: a =( [-k*d(i-1) * (x(i)-x(i-1))/d(i-1)] + [k*d(i) * (x(i+1)-x(i))/d(i)]
					// )/m
					// FY: a =( [-k*d(i-1) * (y(i)-y(i-1))/d(i-1)] + [k*d(i) * (x(i+1)-x(i))/d(i)]
					// )/m

					s.setAx(((-s.getK() * (s.getDistance(sBefore) - lineParticleRestLength)
							* (s.getX() - sBefore.getX()) / s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - lineParticleRestLength) * (sAfter.getX() - s.getX())
									/ s.getDistance(sAfter))
							/ s.getM());

					s.setAy((((-s.getK() * (s.getDistance(sBefore) - lineParticleRestLength)
							* (s.getY() - sBefore.getY()) / s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - lineParticleRestLength) * (sAfter.getY() - s.getY())
									/ s.getDistance(sAfter))
							/ s.getM()) - 9.81);
				}
				// the last one
				else if (i == lastSpring) {
					Spring sBefore = bungee.get(i - 1);
					Ax = -s.getK() * (s.getDistance(sBefore) - lineParticleRestLength) * (s.getX() - sBefore.getX())
							/ s.getDistance(sBefore);
					Ay = -s.getK() * (s.getDistance(sBefore) - lineParticleRestLength) * (s.getY() - sBefore.getY())
							/ s.getDistance(sBefore);
					if (Ax > 0) {
						neg = -1;
					} else {
						neg = 1;
					}
					drag = neg * b * s.getVx() * s.getVx();
					s.setAx((Ax + drag) / s.getM());

					if (Ay > 0) {
						neg = -1;
					} else {
						neg = 1;
					}
					// bv^2
					drag = neg * b * s.getVy() * s.getVy();
					s.setAy(((Ay + drag) / s.getM()) - 9.81);

					// THE LINE IS DISAPPEARING BEACUSE OF THE WAY THE PARTICLE FALLS
					// System.out.println(drag);
					// System.out.println(s.getDistance(sBefore));
					// System.out.println(" ");
					s.color = Color.blue;
				}
				// resets the velocity: v(t) = at+v0
				s.setVx(s.getAx() * timeStep + s.getVoldX());
				s.setVy(s.getAy() * timeStep + s.getVoldY());
				// sets the old velocity
				s.setVoldX(s.getVx());
				s.setVoldY(s.getVy());
			}
			FirstTime++;
			// POSITION
			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; j < rod.array[i].length; j++) {
					Spring point = rod.array[i][j];
					point.setPosition();
					point.setXY(point.x, point.y);
				}
			}
			for (int i = 0; i < Springs; i++) {
				Spring s = bungee.get(i);
				s.setPosition();
				s.setXY(s.x, s.y);
			}
		}
		counter++;
	}

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("frequency", 63);
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
