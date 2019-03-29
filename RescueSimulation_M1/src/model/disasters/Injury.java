package model.disasters;

import model.people.Citizen;

public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		Citizen tar = (Citizen) super.getTarget();
		tar.setBloodLoss(tar.getBloodLoss() + 10);
	}

	public void strike() {
		super.strike();
		Citizen tar = (Citizen) super.getTarget();
		tar.setBloodLoss(tar.getBloodLoss() + 30);
	}

}
