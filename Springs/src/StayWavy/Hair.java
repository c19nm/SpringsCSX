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

	// acceleration
	double g = 9.81;
	double a = -g;

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
			Spring s = new Spring(k, springMass, 0, 0, 0, a, 0);
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

		// free-fall first 40m
		if (FreeFall == true) {
			for (int i = 0; i < lastSpring; i++) {
				// acceleration is g, so -9.81 m/s/s

				// velocity: v(t) = at+v0
				bungee.get(i).setV(bungee.get(i).getA() * timeStep + bungee.get(i).getVold());

				// position: 1/2at^2 + v0t + x0
				bungee.get(i).setPosition(0.5 * bungee.get(i).getA() * Math.pow(timeStep, 2)
						+ (bungee.get(i).getVold() * timeStep) + bungee.get(i).getPosition());

				// when it crosses the origin, the color changes to red
				if (i != 0) {
					if (bungee.get(i).getPosition() <= -0) {
						bungee.get(i).color = Color.red;
					}
				}

				// setting the person as green
				bungee.get(0).color = Color.green;

				// resets the position
				bungee.get(i).setXY(control.getDouble("x"), bungee.get(i).getPosition());

				// stops the freefall after 40 meters
				if (bungee.get(0).getPosition() <= -40) {
					// System.out.println(bungee.get(0).getV());
					FreeFall = false;
				}

				// setting old velocity
				bungee.get(i).setVold(bungee.get(i).getV());
			}
		} else {
			// goes past 40 meters, the acceleration is not just -9.81m/s/s
			// goes through each spring
			for (int i = 0; i < lastSpring; i++) {
				// different forces for the last spring
				if (i == 0) {
					// F = ma
					// a = (k*(y (i+1) - y(i) - Length) - Length) - mg)/m
					bungee.get(i)
							.setA(((bungee.get(i).getK()
									* (bungee.get(i + 1).getPosition() - bungee.get(i).getPosition() - springLength))
									- (massPerson * g)) / massPerson);
				}
				// for all of the other springs
				else {
					// F = ma
					// a = (k*(y (i+1) - y(i) - Length) - k*(y(i) - y(i-1) - Length) - mg)/m
					bungee.get(i).setA(((bungee.get(i).getK()
							* (bungee.get(i + 1).getPosition() - bungee.get(i).getPosition() - springLength))
							- (bungee.get(i).getK()
									* (bungee.get(i).getPosition() - bungee.get(i - 1).getPosition() - springLength))
							- (bungee.get(i).getM() * g)) / bungee.get(i).getM());

				}
				// resets the velocity: v(t) = at+v0
				bungee.get(i).setV(bungee.get(i).getA() * timeStep + bungee.get(i).getVold());
				// sets the old velocity
				bungee.get(i).setVold(bungee.get(i).getV());
			}
			// resets the position of each spring in the bungee
			for (int i = 0; i < lastSpring; i++) {
				// 1/2at^2 + v0t +x0
				bungee.get(i).setPosition((0.5 * bungee.get(i).getA() * Math.pow(timeStep, 2))
						+ (bungee.get(i).getVold() * timeStep) + bungee.get(i).getPosition());
				// sets the animation
				bungee.get(i).setXY(control.getDouble("x"), bungee.get(i).getPosition());
			}
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
