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
	double timeStep = .01;

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
	double bungeeMass = 10;
	double bungeeLength = 40;
	double Springs = 20;
	double kTotal = 28;
	double k = kTotal * Springs;
	double springMass = (bungeeMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	double lastSpring = Springs - 1;
	double springLength = (bungeeLength / Springs);
	boolean FreeFall = true;
	double massPerson = 50;

	@Override
	public void initialize() {
		// the bridge
		DrawableShape bridge = DrawableShape.createRectangle(0, 0.1, 100, 0.2);
		d.addDrawable(bridge);
		bridge.setMarkerColor(Color.BLACK, Color.blue);
		// characteristics of the display frame
		d.setPreferredMinMax(-10, 10, -120, 2);
		d.setVisible(true);
		// initializes the springs
		for (int i = 0; i < Springs; i++) {
			Spring s = new Spring(k, springMass, 0, 0, 0, 0, 0, 0, 0, 0, timeStep);
			d.addDrawable(s);
			s.pixRadius = 3;
			s.setXY(control.getDouble("x"), s.getPosition());
			bungee.add(s);
			// makes the last spring red
			if (i == lastSpring) {
				s.setPosition(0);
				s.color = Color.red;
			}
			// makes all of the springs white and makes them line up vertically
			else {
				s.setPosition(i * springLength);
				s.color = Color.white;
			}
		}
	}

	/**
	 * Goes through code repeatedly
	 */
	protected void doStep() {
		// speeding it up
		this.setDelayTime(1);

		// goes through each spring
		for (int i = 0; i < lastSpring; i++) {
			// different forces for the end springs
			// the first spring
			Spring s = bungee.get(i);
			Spring sBefore = bungee.get(i - 1);
			Spring sAfter = bungee.get(i + 1);
			if (i == 0) {
				s.setAx((s.getK() * (s.getDistance(sAfter) - restLength) * (sAfter.getX() - s.getX())
						/ s.getDistance(sAfter)) / s.getM());
				s.setAy((s.getK() * (s.getDistance(sAfter) - restLength) * (sAfter.getY() - s.getY())
						/ s.getDistance(sAfter)) / s.getM());
			}
			// for all of the other springs
			else if (i < lastSpring - 2) {

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
			else if (i == lastSpring - 1) {
				s.setAx(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getX() - sBefore.getX())
						/ s.getDistance(sBefore))) / s.getM());

				s.setAy(((-s.getK() * (s.getDistance(sBefore) - restLength) * (s.getY() - sBefore.getY())
						/ s.getDistance(sBefore))) / s.getM());

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
			// 1/2at^2 + v0t +x0
			// bungee.get(i).setPosition((0.5 * bungee.get(i).getA() * Math.pow(timeStep,
			// 2))
			// + (bungee.get(i).getVold() * timeStep) + bungee.get(i).getPosition());
			// sets the animation
			bungee.get(i).setXY(control.getDouble("x"), bungee.get(i).getPosition());
		}
	}

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
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
