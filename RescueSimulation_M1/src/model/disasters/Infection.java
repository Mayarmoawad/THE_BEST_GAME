package model.disasters;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;

public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {

		super(startCycle, target);

	}

	public void cycleStep() {
		Citizen tar = (Citizen) super.getTarget();
		tar.setToxicity(tar.getToxicity() + 15);
	}

	public void strike() {
		super.strike();
		Citizen tar = (Citizen) super.getTarget();
		tar.setToxicity(tar.getToxicity() + 25);
	}

}
