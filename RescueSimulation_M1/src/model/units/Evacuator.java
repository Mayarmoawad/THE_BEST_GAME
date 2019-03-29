package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.CitizenState;
import simulation.Address;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			 WorldListener worldListener,int maxCapacity) {

		super(unitID, location, stepsPerCycle, maxCapacity, worldListener);
	}

	public void cycleStep() {
		if (super.getPassengers().size() < super.getMaxCapacity()
				|| (((ResidentialBuilding) super.getTarget()).getOccupants()
						.size() != 0)) {
			super.cycleStep();
		} else {
			if (super.getDistanceToBase() <= 0) {
				for (int i = super.getPassengers().size() - 1; i >= 0; i--) {
					super.getPassengers().get(i).getWorldListener()
							.assignAddress(super.getPassengers().get(i), 0, 0);
					super.getPassengers().get(i).setState(CitizenState.RESCUED);
					super.getPassengers().remove(i);
				}
				if (((ResidentialBuilding) super.getTarget()).getOccupants()
						.size() == 0) {
					super.jobsDone();
				} else {
					super.setState(UnitState.RESPONDING);
				}
			} else {
				super.setDistanceToBase(super.getDistanceToBase()
						- super.getStepsPerCycle());
			}
		}

	}

	public void treat() {
		while (super.getPassengers().size() < super.getMaxCapacity()
				&& ((ResidentialBuilding) super.getTarget()).getOccupants()
						.size() > 0) {
			super.getPassengers().add(
					((ResidentialBuilding) super.getTarget()).getOccupants()
							.remove(0));
		}
	}
}
