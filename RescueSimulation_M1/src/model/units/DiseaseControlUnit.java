package model.units;

import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener );


	}

	public void treat() {
		Citizen C = (Citizen) super.getTarget();
		if (C.getBloodLoss() == 0) {
			if (C.getState() != CitizenState.RESCUED) {
				C.setState(CitizenState.RESCUED);
			}
			this.heal();
		} else if (C.getToxicity() <= this.getTreatmentAmount()) {
			C.setToxicity(0);
		} else {
			C.setToxicity(C.getToxicity() - super.getTreatmentAmount());
		}

	}

	public void heal() {
		Citizen C = (Citizen) super.getTarget();
		if (super.getTreatmentAmount() >= (100 - C.getHp())) {
			C.setHp(100);
		} else {
			C.setHp(C.getHp() + super.getTreatmentAmount());
		}
	}
	public void respond (Rescuable r){
		if(super.getTarget() != null){
			if(((Citizen)super.getTarget()).getToxicity()!=0){
				super.getTarget().getDisaster().setActive(true);
			}
			
		}
		super.respond(r);
	}
}
