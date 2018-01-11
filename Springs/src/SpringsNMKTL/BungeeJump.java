package SpringsNMKTL;
//

//HELLO!!!!!!!

//import java.awt.Color;
//import java.util.ArrayList;
//import org.opensourcephysics.controls.AbstractSimulation;
//import org.opensourcephysics.controls.SimulationControl;
//import org.opensourcephysics.frames.PlotFrame;
//import org.opensourcephysics.display.*;
//import org.opensourcephysics.frames.*;
//
public class BungeeJump{
	public static void main(String[] args) {
		System.out.println("hi");
	}
}
//public class BungeeJump extends AbstractSimulation {
//	/**
//	 * Creates four things: a simulation of a ball dropping, a Velocity/Time Graph,
//	 * a Position/Time Graph, and an Acceleration/Time Graph.
//	 */
//
//	// simulation
//	// create a circle named c
//	Circle c = new Circle();
//
//	// create a display frame named d
//	DisplayFrame d = new DisplayFrame("X", "Y", "Dropping Ball Simulation");
//
//	// beta value
//	double b = .02;
//
//	// time
//	double timeStep = .1;
//	double tf = 0;
//
//	// velocity
//	double v0 = 0;
//	double v = v0;
//	double midv = 0;
//
//	// acceleration
//	int g = 10;
//	double a = -g;
//	
//	//bungeee
//	double Kb = 3;
//	double mSpring = 3;
//	double distance = 6;
//	double lengthBungee = 8;
//
//	// balls
//	ArrayList<Circle> balls = new ArrayList<Circle>();
//
//	// position
//	double midArea = 0;
//	double trapArea = 0;
//	double Area = (2 * midArea + trapArea) / 3;
//	double x0 = 0;
//
//	@Override
//	public void initialize() {
//		// simulation
//		c.setXY(control.getDouble("x"), control.getDouble("y"));
//		d.setVisible(false);
//		d.addDrawable(c);
//		balls.add(c);
//	}
//
//	protected void doStep() {
//
//		// new velocity
//		v = v0 + a * timeStep;
//		// middle velocity
//		midv = v0 + a * .05 * timeStep; // find velocity at midpoint of slice
//
//		// position using adapted simpson's rule
//		midArea = midv * timeStep;
//		trapArea = timeStep * (.05 * (v0 + v));
//		Area = Area + (2 * midArea + trapArea) / 3;
//
//		// ball moves according to position function
//		c.setXY(c.getX(), -Area);
//
//		// add time
//		tf = tf + timeStep;
//
//		// update velocity
//		v0 = v;
//
//		// new acceleration taking into account air resistance
//		a = g - (b * v * v);
//	}
//
//	public void reset() {
//		// starts at the origin
//		control.setValue("x", 0);
//		control.setValue("y", 0);
//
//	}
//
//	public static void main(String[] args) {
//		// allows us to see the simulation
//		SimulationControl.createApp(new BungeeJump());
//
//	}
//}
