package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}

	@Override
	public void cycleStep() {
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setFoundationDamage(tar.getFoundationDamage()+10);	
	}
	public void strike() {
		super.strike();
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setFoundationDamage(tar.getFoundationDamage()+10);
	}


}
