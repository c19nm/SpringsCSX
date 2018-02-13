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

public class Original extends AbstractSimulation {
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
	double timeStep = .11;

	// acceleration
	double g = 9.81;
	double a = -g;

	// bungeee
	double Kb = 3;
	double mSpring = 3;
	double deltax = 6;
	double lengthBungee = 40;
	int springNumber = 100; // number of springs in the length of 40m
	double springLength = (lengthBungee / springNumber);
	int lastSpring = springNumber - 1;

	// creates array list of balls
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	boolean FreeFall = true;
	int i = 0;
	int droppedLast = lastSpring;
	int reverse = 2;
	int count = 1;

	@Override
	public void initialize() {
		// simulation
		d.setPreferredMinMax(-10, 10, -50, 5);
		d.setVisible(true);

		for (int i = 0; i < springNumber; i++) {
			Spring s = new Spring(Kb, mSpring, 0, deltax, 0, a, 0);
			// makes the balls start at the given coordinates
			s.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			s.pixRadius = 2;
			// adds the ball to the array list of balls called "balls"
			bungee.add(s);
			// adds the balls to the simulation
			d.addDrawable(s);
		}
	}

	protected void doStep() {

		// free fall for the first 40 meters
		if (FreeFall == true) {
			// delays the animation
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ONE");

			// new velocity
			bungee.get(lastSpring).setV(bungee.get(lastSpring).getVold() + bungee.get(lastSpring).getA() * timeStep);

			// using a Riemann sum of the right hand rule to get the position
			bungee.get(lastSpring)
					.setPosition(bungee.get(lastSpring).getPosition() + (timeStep * bungee.get(lastSpring).getV()));

			// sets the animation position
			bungee.get(lastSpring).setXY(bungee.get(lastSpring).getX(), bungee.get(lastSpring).getPosition());

			// update velocity
			bungee.get(lastSpring).setVold(bungee.get(lastSpring).getV());

			// new acceleration taking into account air resistance
			bungee.get(lastSpring).setA(-(g - (b * bungee.get(lastSpring).getV() * bungee.get(lastSpring).getV())));

			// stopping the freefall
			if (bungee.get(lastSpring - 1).getPosition() < -40) {
				// System.out.println("made it");
				FreeFall = false;
				bungee.get(lastSpring).color = Color.blue;
			}

			this.setDelayTime(1);

			// adds it so each segment is going one after the other

			// do we need the 1/k1 + 1/k2 = 1/k thing?
			if (bungee.get(droppedLast).getPosition() < -springLength) {
				System.out.println(reverse);
				if (reverse + 1 < springNumber) {
					System.out.println("hi");
					reverse++;
				}
			}
			if (reverse > 1) {
				for (int j = 1; j < reverse; j++) {
					if (j == 3) {
						System.out.println("TWO");
					}
					System.out.println("here");
					// new velocity
					bungee.get(lastSpring - j)
							.setV(bungee.get(lastSpring).getVold() + bungee.get(lastSpring).getA() * timeStep);

					// using a Riemann sum of the right hand rule to get the position
					bungee.get(lastSpring - j).setPosition(
							bungee.get(lastSpring - j).getPosition() + (timeStep * bungee.get(lastSpring - j).getV()));

					// sets the animation position
					bungee.get(lastSpring - j).setXY(bungee.get(lastSpring - j).getX(),
							bungee.get(lastSpring - j).getPosition());

					// update velocity
					bungee.get(lastSpring - j).setVold(bungee.get(lastSpring - j).getV());

					// new acceleration taking into account air resistance
					bungee.get(lastSpring - j)
							.setA(-(g - (b * bungee.get(lastSpring - j).getV() * bungee.get(lastSpring - j).getV())));

					// making this the last one
					droppedLast = lastSpring - j;
				}
			}
		}

		else {
			bungee.get(lastSpring).setV(bungee.get(lastSpring).getVold() + bungee.get(lastSpring).getA() * timeStep);

			// using a Riemann sum of the right hand rule to get the position
			bungee.get(lastSpring)
					.setPosition(bungee.get(lastSpring).getPosition() + (timeStep * bungee.get(lastSpring).getV()));

			// sets the animation position
			bungee.get(lastSpring).setXY(bungee.get(lastSpring).getX(), bungee.get(lastSpring).getPosition());

			// update velocity
			bungee.get(lastSpring).setVold(bungee.get(lastSpring).getV());

			// new acceleration taking into account air resistance
			bungee.get(lastSpring).setA(-(g - (b * bungee.get(lastSpring).getV() * bungee.get(lastSpring).getV())));
			// array of masses??
		}

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
