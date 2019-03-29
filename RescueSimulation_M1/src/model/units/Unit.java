package model.units;

import model.events.SOSResponder;
import model.infrastructure.ResidentialBuilding;

import java.math.*;

import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private WorldListener worldListener;
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;

	public Unit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {

		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener=worldListener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void respond(Rescuable r) {
		int targetX = 0;
		int targetY = 0;
		this.target = r;
		this.state = UnitState.RESPONDING;
		if (target instanceof Citizen) {
			targetX = ((Citizen) target).getLocation().getX();
			targetY = ((Citizen) target).getLocation().getY();
		}
		if (target instanceof ResidentialBuilding) {
			targetX = ((ResidentialBuilding) target).getLocation().getX();
			targetY = ((ResidentialBuilding) target).getLocation().getY();
		}
		int deltaX = Math.abs(this.location.getX() - targetX);
		int deltaY = Math.abs(this.location.getY() - targetY);
		this.distanceToTarget = deltaX + deltaY;
	
	}

	public void jobsDone(){
		this.state = UnitState.IDLE;
		this.target = null;
	}
	
	public void cycleStep() {
		if (this.target instanceof ResidentialBuilding) {
			if (((ResidentialBuilding) target).getStructuralIntegrity() == 0) {
				this.jobsDone();
			}
		} else if (this.target instanceof Citizen) {
			if ((((Citizen) target).getState() == CitizenState.DECEASED)||((Citizen) target).getHp() == 100) {
				this.jobsDone();
			}

		}

		if (this.state == UnitState.RESPONDING) {
			if (distanceToTarget <= 0) {
				this.state = UnitState.TREATING;
				this.worldListener.assignAddress(this, target.getLocation()
						.getX(), target.getLocation().getY());
			} else {
				this.distanceToTarget = this.distanceToTarget - stepsPerCycle;
			}
		}

		if (this.state == UnitState.TREATING) {
			this.treat();
		}

	}

	private void treat() {
		this.target.getDisaster().setActive(false);
	}
}
