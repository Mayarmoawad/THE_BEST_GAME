package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener );

	}

	public void treat() {
		ResidentialBuilding R = (ResidentialBuilding)super.getTarget();
		R.setFireDamage(R.getFireDamage()-10);
	}
public void cycleStep(){
	if (((ResidentialBuilding) super.getTarget()).getFireDamage() == 0) {
		this.jobsDone();
	}
	else{
		super.cycleStep();
	}

}
}
