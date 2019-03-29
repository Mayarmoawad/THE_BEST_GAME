package model.units;

import model.events.WorldListener;
import simulation.Address;

public abstract class MedicalUnit extends Unit {

	private int healingAmount;
	private int treatmentAmount;

	
	public MedicalUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener );

		healingAmount = 10;
		treatmentAmount = 10;

	}public int getTreatmentAmount() {
		return treatmentAmount;
	}


}
