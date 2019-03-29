package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {

		super(startCycle, target);

	}
	public void cycleStep() {
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setGasLevel(tar.getGasLevel()+15);	
	}
	public void strike() {
		super.strike();
		ResidentialBuilding tar = (ResidentialBuilding)super.getTarget();
		tar.setGasLevel(tar.getGasLevel()+10);
		}

}
