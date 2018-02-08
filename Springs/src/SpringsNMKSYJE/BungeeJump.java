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
	double timeStep = .01;

	// acceleration
	double g = 9.81;
	double a = -g;

	// bungeee
	double Kb = 3;
	double mSpring = 3;
	double deltax = 6;
	double lengthBungee = 40;

	// balls
	ArrayList<Circle> balls = new ArrayList<Circle>();

	// number of springs in the length of 40m
	double springNumber = 200;
	double drop = 1;

	// creates array list of balls
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	boolean FreeFall = true;

	@Override
	public void initialize() {
		// simulation
		d.setPreferredMinMax(-10, 10, -50, 5);
		d.setVisible(true);

		for (int i = 0; i < springNumber; i++) {
			Spring s = new Spring(Kb, mSpring, 0, deltax, 0, 0, 0);
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
		// going through each spring
		for (int i = 0; i < drop; i++) {
			// free fall for the first 40 meters
			if (FreeFall == true) {
				// delays the animation
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// new velocity
				bungee.get(i).setV(bungee.get(i).getVold() + bungee.get(i).getA() * timeStep);

				// using a Riemann sum of the right hand rule to get the position
				bungee.get(i).setPosition(bungee.get(i).getPosition() + (timeStep * bungee.get(i).getV()));

				// sets the animation position
				bungee.get(i).setXY(bungee.get(i).getX(), bungee.get(i).getPosition());

				// update velocity
				bungee.get(i).setVold(bungee.get(i).getV());

				// new acceleration taking into account air resistance
				bungee.get(i).setA(-(g - (b * bungee.get(i).getV() * bungee.get(i).getV())));

				// stopping the freefall
				if (bungee.get(i).getPosition() < -40) {
					System.out.println("made it");
					FreeFall = false;
					bungee.get(i).color = Color.blue;
				}

				this.setDelayTime(1);

				// adds it so each segment is going one after the other
				if (drop < springNumber) {
					if (i == drop - 1) {
						System.out.println("what");
						drop++;
						i = 0;
					}
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
