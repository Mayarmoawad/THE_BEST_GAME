package model.people;

import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.units.WorldListener;

public class Citizen implements Rescuable, Simulatable {
	private SOSListener emergencyService;
	private WorldListener worldListener;
	private CitizenState state;
	private Disaster disaster;

	private String name;
	private String nationalID;
	private int age;
	private int hp;
	private int bloodLoss;
	private int toxicity;
	private Address location;

	public Citizen(Address location, String nationalID, String name, int age,
			WorldListener worldListener) {

		this.name = name;
		this.nationalID = nationalID;
		this.age = age;
		this.location = location;
		this.state = CitizenState.SAFE;
		this.hp = 100;
		this.worldListener = worldListener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	public CitizenState getState() {
		return state;
	}

	public void setState(CitizenState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if (hp > 0 || hp <= 100) {
			this.hp = hp;
		} else if (hp == 0) {
			this.state = CitizenState.DECEASED;
		} else if (hp < 0) {
			setHp(0);
		} else if (hp > 100) {
			setHp(100);
		}
	}

	public int getBloodLoss() {
		return bloodLoss;
	}

	public void setBloodLoss(int bloodLoss) {

		if (bloodLoss == 100) {
			setHp(0);
		}
		if (bloodLoss < 0) {

			setBloodLoss(0);
		}

		else if (bloodLoss > 100) {
			setBloodLoss(100);
		} else {
			this.bloodLoss = bloodLoss;
		}
			}

	public int getToxicity() {
		return toxicity;
	}

	public void setToxicity(int toxicity) {
		if (toxicity > 100 )
			setToxicity(100);
				
				if( toxicity < 0) {
			setToxicity(0);
		}
		if (toxicity == 100) {
			setHp(0);
		} else {
			this.toxicity = toxicity;
		}
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public String getNationalID() {
		return nationalID;
	}

	@Override
	public void cycleStep() {
		if ((bloodLoss > 0 && bloodLoss < 30)
				|| (toxicity > 0 && toxicity < 30)) {
			setHp(hp - 5);
		}
		if ((bloodLoss >= 30 && bloodLoss < 70)
				|| (toxicity >= 30 && toxicity < 70)) {
			setHp(hp - 10);
		}
		if (bloodLoss >= 70 || toxicity >= 70) {
			setHp(hp - 15);
		}

	}

	@Override
	public void struckBy(Disaster d) {
		this.disaster = d;
		this.state = CitizenState.IN_TROUBLE;
		if (emergencyService != null) {
			emergencyService.receiveSOSCall(this);
		}

	}

}
