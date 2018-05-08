package SpringsNMKSYJE;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.frames.DisplayFrame;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class FindingK10P50 extends AbstractSimulation {
	// simulation

	// create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Bungee Jumping Simulation");

	// time
	double timeStep = .001;

	// acceleration
	double g = 10;
	double a = -g;

	// bungee variables
	double bungeeMass = 10;
	double bungeeLength = 40;
	double springNumber = 100;
	double kTotal = 30; //
	double k = kTotal * springNumber;
	double springMass = (bungeeMass / springNumber);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	double lastSpring = springNumber - 1;
	double springLength = (bungeeLength / springNumber);
	boolean FreeFall = true;
	double massPerson = 50;

	@Override
	public void initialize() {
		d.setPreferredMinMax(-10, 10, -120, 2);
		d.setVisible(true);
		//adding new springs
		for (int i = 0; i < springNumber; i++) {
			Spring s = new Spring(k, springMass, 0, 0, 0, a, 0);
			d.addDrawable(s);
			s.pixRadius = 3;
			s.setXY(control.getDouble("x"), s.getPosition());
			bungee.add(s);
			if (i == lastSpring) {
				s.setPosition(0);
				s.color = Color.red;
			} else {
				s.setPosition(i * springLength);
				s.color = Color.white;
			}
		}
	}

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
				if (i != 0) {
					if (bungee.get(i).getPosition() <= -0) {
						bungee.get(i).color = Color.red;
					}
				}
				bungee.get(0).color = Color.blue;
				bungee.get(i).setXY(control.getDouble("x"), bungee.get(i).getPosition());
				if (bungee.get(0).getPosition() <= -40) {
					FreeFall = false;
				}

				// old velocity
				bungee.get(i).setVold(bungee.get(i).getV());
			}
		} else {
			for (int i = 0; i < lastSpring; i++) {
				if (i == 0) {
					// F = ma
					// a = (k*(y (i+1) - y(i) - Length) - Length) - mg)/m
					bungee.get(i)
							.setA(((bungee.get(i).getK()
									* (bungee.get(i + 1).getPosition() - bungee.get(i).getPosition() - springLength))
									- (massPerson * g)) / massPerson);
				} else {
					// F = ma
					// a = (k*(y (i+1) - y(i) - Length) - k*(y(i) - y(i-1) - Length) - mg)/m
					bungee.get(i).setA(((bungee.get(i).getK()
							* (bungee.get(i + 1).getPosition() - bungee.get(i).getPosition() - springLength))
							- (bungee.get(i).getK()
									* (bungee.get(i).getPosition() - bungee.get(i - 1).getPosition() - springLength))
							- (bungee.get(i).getM() * g)) / bungee.get(i).getM());

				}
				// velocity: v(t) = at+v0
				bungee.get(i).setV(bungee.get(i).getA() * timeStep + bungee.get(i).getVold());
				// old velocity
				bungee.get(i).setVold(bungee.get(i).getV());
			}
			for (int i = 0; i < lastSpring; i++) {
				// 1/2at^2 + v0t +x0
				bungee.get(i).setPosition((0.5 * bungee.get(i).getA() * Math.pow(timeStep, 2))
						+ (bungee.get(i).getVold() * timeStep) + bungee.get(i).getPosition());
				bungee.get(i).setXY(control.getDouble("x"), bungee.get(i).getPosition());
				if (bungee.get(0).getPosition() <= -100) {
					System.out.println("STOP!!!");
					System.out.println(bungee.get(0).getPosition());
				}
			}
		}

	}

	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new FindingK10P50());

	}
}
