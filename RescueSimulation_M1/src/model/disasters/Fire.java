package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}
	public void cycleStep() {
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setFireDamage(tar.getFireDamage()+10);	
	}
	public void strike() {
		super.strike();
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setFireDamage(tar.getFireDamage()+10);
	}

}
