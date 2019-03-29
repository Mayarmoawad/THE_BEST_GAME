package model.units;

import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener );


	}

	public void treat() {
		ResidentialBuilding R = (ResidentialBuilding) super.getTarget();
		R.setGasLevel(R.getGasLevel() - 10);

	}

	public void cycleStep() {
		if (((ResidentialBuilding) super.getTarget()).getGasLevel() == 0) {
			this.jobsDone();
		} else {
			super.cycleStep();
		}
	}
}
