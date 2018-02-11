package SpringsNMKSYJE;

import org.opensourcephysics.controls.AbstractSimulation;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import SpringsNMKSYJE.Spring;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class BungeeJump extends AbstractSimulation {
	/**
	 * Creates four things: a simulation of a ball dropping, a Velocity/Time Graph,
	 * a Position/Time Graph, and an Acceleration/Time Graph.
	 * 
	 * Free fall And then the springness will kick in at the end
	 */

	// simulation
	// create a circle named c
	Circle c = new Circle();

	// create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Dropping Ball Simulation");

	// air resistance beta value
	double b = .02;

	// time
	double timeStep = .1;

	// acceleration
	double g = 9.81;
	double a = -g;

	// bungeee
	double massPerson = 50;
	double Kb = 300;
	double mSpring = 10;
	double deltax = .5;
	double lengthBungee = 40;
	double springNumber = 100; // number of springs in the length of 40m
	double springLength = (lengthBungee / springNumber);
	int lastSpring = (int) (springNumber - 1);

	// creates array list of balls
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	boolean FreeFall = true;
	int iterations = 0;
	int droppedLast = lastSpring;
	int reverse = 2;
	int count = 1;
	double Fg = 0;
	double Fup = 0;
	double firstBounce = 0;

	@Override
	public void initialize() {
		// simulation
		d.setPreferredMinMax(-10, 10, -92, 2);
		d.setVisible(true);
		Circle origin = new Circle();
		origin.color = Color.black;
		origin.setXY(control.getDouble("x"), control.getDouble("y"));
		d.addDrawable(origin);
		origin.pixRadius = 2;

		for (int i = 0; i < springNumber; i++) {
			Spring s = new Spring(Kb, mSpring, 0, 0, 0, a, 0);
			// makes the balls start at the given coordinates
			s.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			s.pixRadius = 2;
			// adds the ball to the array list of balls called "balls"
			bungee.add(s);
			// adds the balls to the simulation
			d.addDrawable(s);
			s.color = Color.white;
			s.setPosition(i * springLength);
		}
	}

	protected void doStep() {
		// delays the animation
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// free fall for the first 40 meters
		if (FreeFall == true) {
			for (int j = 0; j < springNumber; j++) {
				bungee.get(j).setV(bungee.get(j).getVold() + bungee.get(j).getA() * timeStep);

				// using a Riemann sum of the right hand rule to get the position
				bungee.get(j).setPosition(bungee.get(j).getPosition() + (timeStep * bungee.get(j).getV()));

				// sets the animation position
				bungee.get(j).setXY(bungee.get(j).getX(), bungee.get(j).getPosition());

				// update velocity
				bungee.get(j).setVold(bungee.get(j).getV());

				// new acceleration taking into account air resistance
				bungee.get(j).setA(-(g - (b * bungee.get(j).getV() * bungee.get(j).getV())));

				if (bungee.get(j).getPosition() < 0) {
					bungee.get(j).color = Color.red;
				}
			}
			if (bungee.get(0).getPosition() < -40) {
				for (int i = 0; i < springNumber; i++) {
					bungee.get(i).setPosition(bungee.get(i).getPosition() + (0 - bungee.get(lastSpring).getPosition()));
					bungee.get(i).setXY(bungee.get(i).getX(), bungee.get(i).getPosition());
				}
				FreeFall = false;
			}

		} else {
			// System.out.println("here " + count);
			if (iterations < springNumber) {
				for (int i = 0; i < iterations + 1; i++) {
					bungee.get(i).setDeltax(deltax);
					bungee.get(i).setPosition(bungee.get(i).getPosition() - deltax);
					bungee.get(i).setXY(bungee.get(i).getX(), bungee.get(i).getPosition());
				}
				iterations++;
			} else {
				if (firstBounce < springNumber) {
					Fg = (massPerson * g) + (bungee.get(0).getM() * g * firstBounce);
					Fup = (bungee.get(0).getK() * Math.pow(bungee.get(0).getDeltax(), 2) * springNumber);

					for (int j = 0; j < firstBounce + 1; j++) {
						// last spring
						if (j == 0) {
							bungee.get(j)
									.setA((bungee.get(j + 1).getK() * Math.pow(bungee.get(j + 1).getDeltax(), 2) - Fg)
											/ (2 * bungee.get(j + 1).getM()));
						}
						// first spring
						else if (j == lastSpring) {
							bungee.get(j).setA(0);
						}
						// middle springs
						else {
							bungee.get(j).setA(
									((Fup) - Fg - bungee.get(j - 1).getK() * Math.pow(bungee.get(j + 1).getDeltax(), 2))
											/ (2 * bungee.get(j + 1).getM()));
						}

						bungee.get(j).setV(bungee.get(j).getVold() + bungee.get(j).getA() * timeStep);

						// using a Riemann sum of the right hand rule to get the position
						bungee.get(j).setPosition(bungee.get(j).getPosition() + (timeStep * bungee.get(j).getV()));

						// sets the animation position
						bungee.get(j).setXY(bungee.get(j).getX(), bungee.get(j).getPosition());

						// update velocity
						bungee.get(j).setVold(bungee.get(j).getV());

					}

					firstBounce++;
				}
			}

		}

		// array of masses??

	}

	public void reset() {
		// starts at the origin
		control.setValue("x", 0);
		control.setValue("y", 0);

	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new BungeeJump());

	}
}
