package StayWavy;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class FlyCast extends AbstractSimulation {
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
	double restLength = .4;
	double lineMass = .0127;
	double lineLength = .8;
	int Springs = 100;
	double kTotal = 100;
	double k = kTotal * Springs;
	double springMass = (lineMass / Springs);
	ArrayList<Spring> bungee = new ArrayList<Spring>();
	int lastSpring = Springs - 1;
	double springLength = (lineLength / Springs);
	double frequency = 40; // 63
	double y0 = .01;
	double individualRestLength = restLength / Springs;
	FishingRod rod;

	// frequency

	@Override
	public void initialize() {
		// characteristics of the display frame
		d.setPreferredMinMax(-.1, 1.1, -.15, .15);
		d.setVisible(true);
		frequency = control.getDouble("frequency");
		// initializes the springs
		// for (int i = 0; i < Springs; i++) {
		// Spring s = new Spring(k, springMass, i * springLength, 0, 0, 0, 0, 0,
		// timeStep);
		// s.pixRadius = 3;
		// s.setXY(s.x, s.y);
		// bungee.add(s);
		// d.addDrawable(s);
		// }
		rod = new FishingRod(100, 3, 3, 1.5, .0000001);
		for (int i = 0; i < rod.array.length; i++) {
			rod.array[i][0].setXY(rod.array[i][0].x, rod.array[i][0].y);
			rod.array[i][0].pixRadius = 3;
			d.addDrawable(rod.array[i][0]);
			rod.array[i][1].setXY(rod.array[i][1].x, rod.array[i][1].y);
			rod.array[i][1].pixRadius = 3;
			d.addDrawable(rod.array[i][1]);
		}
		horizontalRestLength = 1.1 * rod.array[0][0].getDistance(rod.array[0][1]);
		verticalRestLength = 1.1 * rod.array[0][0].getDistance(rod.array[1][0]);
		diagonalRestLength = 0.9 * Math.sqrt(horizontalRestLength * horizontalRestLength + verticalRestLength * verticalRestLength);
	}
double horizontalRestLength;
double verticalRestLength;
double diagonalRestLength;
	/**
	 * Goes through code repeatedly
	 */
	protected void doStep() {
		
		for (int sean = 0; sean < 1000; sean++) {
			// speeding it up
			this.setDelayTime(1);

			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; j < rod.array[i].length; j++) {
					Spring point = rod.array[i][j];
					point.ax = 0;
					point.ay = 0;
					if (i == 0 && j == 0) {
						// point.oscX(frequency, y0, timeStep * counter);
						// point.findA(point.getFs(rod.array[1][0], individualRestLength));
						// point.findA(point.getFs(rod.array[0][1], individualRestLength));
						// point.findA(point.getFs(rod.array[1][1], individualRestLength));
					} else if (i == 0 && j == 1) {
						// point.findA(point.getFs(rod.array[1][0], individualRestLength));
						// point.findA(point.getFs(rod.array[0][0], individualRestLength));
						// point.findA(point.getFs(rod.array[1][1], individualRestLength));
					} else if (i < (rod.array.length - 2) && j == 0) {
						// i-1, 0
						// i+1, 0
						// i, 1
						// i+1, 1
						// i-1, 1
						point.findA(point.getFs(rod.array[i - 1][0], verticalRestLength));
						point.findA(point.getFs(rod.array[i + 1][0], verticalRestLength));
						point.findA(point.getFs(rod.array[i][1], horizontalRestLength));
						point.findA(point.getFs(rod.array[i + 1][1], diagonalRestLength));
						point.findA(point.getFs(rod.array[i - 1][1], diagonalRestLength));
					} else if (i < (rod.array.length - 2) && j == 1) {
						// i-1, 0
						// i+1, 0
						// i, 0
						// i+1, 1
						// i-1, 1
						point.findA(point.getFs(rod.array[i - 1][0], diagonalRestLength));
						point.findA(point.getFs(rod.array[i + 1][0], diagonalRestLength));
						point.findA(point.getFs(rod.array[i][0], horizontalRestLength));
						point.findA(point.getFs(rod.array[i + 1][1], verticalRestLength));
						point.findA(point.getFs(rod.array[i - 1][1], verticalRestLength));
					} else if (i == (rod.array.length - 1) && j == 0) {
						point.findA(point.getFs(rod.array[i][1], horizontalRestLength));
						point.findA(point.getFs(rod.array[i - 1][0], verticalRestLength));
						point.findA(point.getFs(rod.array[i - 1][1], diagonalRestLength));
					} else if (i == (rod.array.length - 1) && j == 1) {
						point.findA(point.getFs(rod.array[i][0], horizontalRestLength));
						point.findA(point.getFs(rod.array[i - 1][0], diagonalRestLength));
						point.findA(point.getFs(rod.array[i - 1][1], verticalRestLength));
					}
					// velocity
					System.out.println(point.getAx());
					point.setVx(point.getAx() * timeStep + point.getVoldX());
					point.setVy(point.getAy() * timeStep + point.getVoldY());
					// sets the old velocity
					point.setVoldX(point.getVx());
					point.setVoldY(point.getVy());
				}
			}

			// POSITION
			for (int i = 0; i < rod.array.length; i++) {
				for (int j = 0; j < rod.array[i].length; j++) {
					Spring point = rod.array[i][j];
					point.setPosition();
					point.setXY(point.x, point.y);
				}
			}
		}
		counter++;
	} // FIG OUT ISSUE W UPDATING POSITION... TALK TO CONDIE
		// PUSH FLYCAST, FISHINGROD, SPRING

	/**
	 * Resetting the values
	 */
	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("frequency", 63);
		d.clearDrawables();
	}

	/**
	 * Runs the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new FlyCast());

	}
}
