package model.units;

import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;

public abstract class FireUnit extends Unit {

	public FireUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener );

	}
	public void respond (Rescuable r){
		if(super.getTarget() != null){
				super.getTarget().getDisaster().setActive(true);
				}
			super.respond(r);
		}
		
}

