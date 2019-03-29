package model.infrastructure;

import java.util.ArrayList;
import java.util.Random;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class ResidentialBuilding implements Rescuable, Simulatable {
	private SOSListener emergencyService;
	private Address location;
	private int structuralIntegrity;
	private int fireDamage;
	private int gasLevel;
	private int foundationDamage;
	private ArrayList<Citizen> occupants;
	private Disaster disaster;

	public ResidentialBuilding(Address location) {

		this.location = location;
		this.structuralIntegrity = 100;
		occupants = new ArrayList<Citizen>();

	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public int getStructuralIntegrity() {
		return structuralIntegrity;
	}

	public void setStructuralIntegrity(int structuralIntegrity) {
		if (structuralIntegrity < 0) {
			setStructuralIntegrity(0);
		} else if (structuralIntegrity == 0) {
			for (int i = 0; i < occupants.size(); i++) {
				occupants.get(i).setHp(0);
			}
		} else {
			this.structuralIntegrity = structuralIntegrity;
		}

	}

	public int getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(int fireDamage) {
		if (fireDamage < 0)
			setFireDamage(0);

		if (fireDamage > 100)
			setFireDamage(100);
		else {
			this.fireDamage = fireDamage;
		}

	}

	public int getGasLevel() {
		return gasLevel;
	}

	public void setGasLevel(int gasLevel) {
		if (gasLevel > 100)
			setGasLevel(100);
		if (gasLevel < 0)
			setGasLevel(0);

		else if (gasLevel == 100) {
			for (int i = 0; i < occupants.size(); i++) {
				occupants.get(i).setHp(0);
			}
		} else {
			this.gasLevel = gasLevel;
		}
	}

	public int getFoundationDamage() {
		return foundationDamage;
	}

	public void setFoundationDamage(int foundationDamage) {
		if (foundationDamage >= 100) {
			setStructuralIntegrity(0);
		} else {
			this.foundationDamage = foundationDamage;
		}
	}

	public Address getLocation() {
		return location;
	}

	public ArrayList<Citizen> getOccupants() {
		return occupants;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	@Override
	public void cycleStep() {
		if (foundationDamage > 0) {
			Random r = new Random();
			int Random = r.nextInt(10 - 5 + 1) + 5;
			structuralIntegrity = structuralIntegrity - Random;
		}

		if (fireDamage > 0 && fireDamage < 30) {
			structuralIntegrity = structuralIntegrity - 3;
		} else if (fireDamage >= 30 && fireDamage < 70) {
			structuralIntegrity = structuralIntegrity - 5;
		} else if (fireDamage >= 70) {
			structuralIntegrity = structuralIntegrity - 7;
		}

	}

	@Override
	public void struckBy(Disaster d) {
		this.disaster = d;
		if (emergencyService != null) {
			emergencyService.receiveSOSCall(this);
		}

	}

}
