package StayWavy;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class Hair extends AbstractSimulation {
	// create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Bungee Jumping Simulation");

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
	double restLength = 0;
	double bungeeMass = .01;
	double bungeeLength = 1;
	double Springs = 100;
	double kTotal = 50;
	double k = kTotal * Springs;
	double springMass = (bungeeMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	double lastSpring = Springs - 1;
	double frequency;
	double springLength = (bungeeLength / Springs);
	double y0 = .01;

	// frequency

	@Override
	public void initialize() {
		// characteristics of the display frame
		d.setPreferredMinMax(-.1, 1.1, -.15, .15);
		d.setVisible(true);
		frequency = control.getDouble("frequency");
		// initializes the springs
		for (int i = 0; i < Springs; i++) {
			Spring s = new Spring(k, springMass, i * springLength, 0, 0, 0, 0, 0, 0, 0, 0, timeStep);
			s.pixRadius = 3;
			s.setXY(s.x, s.y);
			bungee.add(s);
			d.addDrawable(s);
		}
	}

	/**
	 * Goes through code repeatedly
	 */
	protected void doStep() {
		for (int sean = 0; sean < 1000; sean++) {
			// speeding it up
			this.setDelayTime(1);

			// goes through each spring
			for (int i = 0; i < lastSpring; i++) {
				// different forces for the end springs
				// the first spring
				Spring s = bungee.get(i);
				if (i == 0) {
					s.oscY(frequency, y0, timeStep * counter);
					s.setY(s.y);
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

					s.setAx(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getX() - sBefore.getX())
							/ s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - restLength) * (sAfter.getX() - s.getX())
									/ s.getDistance(sAfter))
							/ s.getM());

					s.setAy(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getY() - sBefore.getY())
							/ s.getDistance(sBefore))
							+ s.getK() * (s.getDistance(sAfter) - restLength) * (sAfter.getY() - s.getY())
									/ s.getDistance(sAfter))
							/ s.getM());
				}
				// the last one
				else if (i == lastSpring) {
					Spring sBefore = bungee.get(i - 1);
					s.setAx(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getX() - sBefore.getX())
							/ s.getDistance(sBefore))) / s.getM());

					s.setAy(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getY() - sBefore.getY())
							/ s.getDistance(sBefore))) / s.getM());
					s.color = Color.blue;

				}
				// resets the velocity: v(t) = at+v0
				s.setVx(s.getAx() * timeStep + s.getVoldX());
				s.setVy(s.getAy() * timeStep + s.getVoldY());
				// sets the old velocity
				s.setVoldX(s.getVx());
				s.setVoldY(s.getVy());
			}
			// resets the position of each spring in the bungee
			for (int i = 0; i < lastSpring; i++) {
				Spring s = bungee.get(i);
				s.setPosition();
				s.setXY(s.x, s.y);
			}
			counter++;
		}
	}

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("frequency", 36.6666);
	}

	/**
	 * Runs the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new Hair());

	}
}
