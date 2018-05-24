package StayWavy;

import java.util.ArrayList;

/**
 * student
 */

public class FishingRod {

	double length;
	int particleNum;
	Spring[][] array;

	public FishingRod(double k, double rodlength, int numofParticles, double springmass, double sideLength,
			double timestep) {
		length = rodlength;
		particleNum = numofParticles;
		// double springLength = rodlength / numofParticles;

		array = new Spring[particleNum][2];

		for (int i = 0; i < particleNum; i++) {
			array[i][0] = new Spring(k - 10 * i, springmass, 0, i * sideLength, 0, 0, 0, 0, timestep);
			array[i][1] = new Spring(k - 10 * i, springmass, 1, i * sideLength, 0, 0, 0, 0, timestep);
		}
	}

}
