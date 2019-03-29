package model.units;

import java.util.ArrayList;

import simulation.Address;
import simulation.Rescuable;
import model.events.WorldListener;
import model.people.Citizen;

public abstract class PoliceUnit extends Unit {

	private ArrayList<Citizen> passengers;
	private int maxCapacity;
	private int distanceToBase;

	public PoliceUnit(String unitID, Address location, int stepsPerCycle, int maxCapacity,WorldListener worldListener) {

		super(unitID, location, stepsPerCycle,worldListener);
		passengers = new ArrayList<Citizen>();
		this.maxCapacity = maxCapacity;

	}

	public ArrayList<Citizen> getPassengers() {
		return passengers;
	}

	public int getDistanceToBase() {
		return distanceToBase;
	}

	public void setDistanceToBase(int distanceToBase) {
		this.distanceToBase = distanceToBase;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}
	public void respond (Rescuable r){
		if(super.getTarget() != null){
				super.getTarget().getDisaster().setActive(true);
				}
			super.respond(r);
		}
		
}
