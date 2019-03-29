package simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;

public class Simulator implements WorldListener {
	private SOSListener emergencyService;
	private int currentCycle = 0;
	private ArrayList<ResidentialBuilding> buildings;
	private ArrayList<Citizen> citizens;
	private ArrayList<Unit> emergencyUnits;
	private ArrayList<Disaster> plannedDisasters;
	private ArrayList<Disaster> executedDisasters;
	private Address[][] world;

	public Simulator(SOSListener emergencyService) throws Exception {
		this.emergencyService = emergencyService;
		buildings = new ArrayList<ResidentialBuilding>();
		citizens = new ArrayList<Citizen>();
		emergencyUnits = new ArrayList<Unit>();
		plannedDisasters = new ArrayList<Disaster>();
		executedDisasters = new ArrayList<Disaster>();

		world = new Address[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				world[i][j] = new Address(i, j);
			}
		}

		loadUnits("units.csv");
		loadBuildings("buildings.csv");
		loadCitizens("citizens.csv");
		loadDisasters("disasters.csv");

		for (int i = 0; i < buildings.size(); i++) {

			ResidentialBuilding building = buildings.get(i);
			for (int j = 0; j < citizens.size(); j++) {

				Citizen citizen = citizens.get(j);
				if (citizen.getLocation() == building.getLocation())
					building.getOccupants().add(citizen);

			}
		}
	}

	private void loadUnits(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			String id = info[1];
			int steps = Integer.parseInt(info[2]);

			switch (info[0]) {

			case "AMB":
				emergencyUnits.add(new Ambulance(id, world[0][0], steps, this));
				break;

			case "DCU":
				emergencyUnits.add(new DiseaseControlUnit(id, world[0][0],
						steps, this));
				break;

			case "EVC":
				emergencyUnits.add(new Evacuator(id, world[0][0], steps,
						this,Integer.parseInt(info[3])));
				break;

			case "FTK":
				emergencyUnits.add(new FireTruck(id, world[0][0], steps, this));
				break;

			case "GCU":
				emergencyUnits.add(new GasControlUnit(id, world[0][0], steps,
						this));
				break;

			}

			line = br.readLine();
		}

		br.close();
	}

	private void loadBuildings(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			ResidentialBuilding B = new ResidentialBuilding(world[x][y]);
			B.setEmergencyService(emergencyService);
			buildings.add(B);

			line = br.readLine();

		}
		br.close();
	}

	private void loadCitizens(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			String id = info[2];
			String name = info[3];
			int age = Integer.parseInt(info[4]);
			Citizen C = new Citizen(world[x][y], id, name, age, this);
			C.setEmergencyService(this.emergencyService);
			citizens.add(C);
			line = br.readLine();

		}
		br.close();
	}

	private void loadDisasters(String path) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();

		while (line != null) {

			String[] info = line.split(",");
			int startCycle = Integer.parseInt(info[0]);
			ResidentialBuilding building = null;
			Citizen citizen = null;

			if (info.length == 3)
				citizen = getCitizenByID(info[2]);
			else {

				int x = Integer.parseInt(info[2]);
				int y = Integer.parseInt(info[3]);
				building = getBuildingByLocation(world[x][y]);

			}

			switch (info[1]) {

			case "INJ":
				plannedDisasters.add(new Injury(startCycle, citizen));
				break;

			case "INF":
				plannedDisasters.add(new Infection(startCycle, citizen));
				break;

			case "FIR":
				plannedDisasters.add(new Fire(startCycle, building));
				break;

			case "GLK":
				plannedDisasters.add(new GasLeak(startCycle, building));
				break;
			}

			line = br.readLine();
		}
		br.close();
	}

	private Citizen getCitizenByID(String id) {

		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getNationalID().equals(id))
				return citizens.get(i);
		}

		return null;
	}

	private ResidentialBuilding getBuildingByLocation(Address location) {

		for (int i = 0; i < buildings.size(); i++) {
			if (buildings.get(i).getLocation() == location)
				return buildings.get(i);
		}

		return null;
	}

	@Override
	public void assignAddress(Simulatable sim, int x, int y) {

		if (sim instanceof Citizen) {
			((Citizen) sim).setLocation(world[x][y]);
		}
		if (sim instanceof Unit) {
			((Unit) sim).setLocation(world[x][y]);
		}

	}

	public ArrayList<Unit> getEmergencyUnits() {
		return emergencyUnits;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}

	// if rule 1
	// {
	// if rule 2
	// {
	// if rule 3
	// {
	// return true;
	// }
	// }
	// }
	// return false;

	// if rule 1
	// if rule 2

	public boolean checkGameOver() {

		if (plannedDisasters.size() > 0) {
			return false;
		} else {
			for (int i = 0; i < executedDisasters.size(); i++) {
				if (executedDisasters.get(i).isActive()) {
					if (executedDisasters.get(i).getTarget() instanceof Citizen) {
						if (((Citizen) executedDisasters.get(i).getTarget())
								.getState() != CitizenState.DECEASED) {
							return false;

						}

					}
					if (executedDisasters.get(i).getTarget() instanceof ResidentialBuilding) {
						if (((ResidentialBuilding) executedDisasters.get(i)
								.getTarget()).getStructuralIntegrity() != 0) {
							return false;

						}

					}
				}
			}

			for (int i = 0; i < emergencyUnits.size(); i++) {
				if (emergencyUnits.get(i).getState() != UnitState.IDLE) {
					return false;
				}
			}
		}
		return true;
	}

	public int calculateCasualties() {
		int counter = 0;
		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getState() == CitizenState.DECEASED) {
				counter++;
			}
		}
		return counter;
	}

	public void nextCycle() {
		currentCycle++;
		for (int i = plannedDisasters.size()-1; i >= 0; i--) {
			if (plannedDisasters.get(i).getStartCycle() == currentCycle) {
				if (plannedDisasters.get(i).getTarget() instanceof Citizen) {
					plannedDisasters.get(i).strike();
					executedDisasters.add(plannedDisasters.remove(i));
				}
				if(plannedDisasters.get(i).getTarget() instanceof ResidentialBuilding){
					if(plannedDisasters.get(i) instanceof Fire){
						if(((ResidentialBuilding)plannedDisasters.get(i).getTarget()).getGasLevel()==0){
							plannedDisasters.get(i).strike();
							executedDisasters.add(plannedDisasters.remove(i));
						}else if(((ResidentialBuilding)plannedDisasters.get(i).getTarget()).getGasLevel()>0 &&((ResidentialBuilding)plannedDisasters.get(i).getTarget()).getGasLevel()<70){
						Collapse Col = new Collapse(currentCycle,(ResidentialBuilding)plannedDisasters.get(i).getTarget());
						Col.strike();
						plannedDisasters.remove(i);
							executedDisasters.add(Col);
						}else if(((ResidentialBuilding)plannedDisasters.get(i).getTarget()).getGasLevel()>=70){
							((ResidentialBuilding)plannedDisasters.get(i).getTarget()).setStructuralIntegrity(0);
						}
					
					} else if(plannedDisasters.get(i) instanceof GasLeak){
						if(plannedDisasters.get(i).getTarget().getDisaster() instanceof Fire){
							Collapse Col = new Collapse(currentCycle,(ResidentialBuilding)plannedDisasters.get(i).getTarget());
							Col.strike();
							plannedDisasters.remove(i);
							executedDisasters.add(Col);}
						else{
							plannedDisasters.get(i).strike();
							executedDisasters.add(plannedDisasters.remove(i));
						}
							
					}
					
				}
			}
			}
		for(int i = 0; i<buildings.size();i++){
			if(buildings.get(i).getFireDamage() == 100){
				buildings.get(i).getDisaster().setActive(false);
				buildings.get(i).setFireDamage(0);
				Collapse Col = new Collapse(currentCycle,buildings.get(i));
				Col.strike();
				executedDisasters.add(Col);
				}
				
			}
			for(int i = 0 ; i <emergencyUnits.size();i++){
				if(emergencyUnits.get(i).getState()!=UnitState.IDLE)
					emergencyUnits.get(i).cycleStep();
			}
			
			for(int i = 0; i<executedDisasters.size();i++){
				if(executedDisasters.get(i).isActive()){
					if(executedDisasters.get(i).getStartCycle() != currentCycle){
						executedDisasters.get(i).cycleStep();
					}
				}
			}
			
			for(int i =0 ; i<buildings.size();i++){
				buildings.get(i).cycleStep();
			}
			for(int i =0 ; i<citizens.size();i++){
				citizens.get(i).cycleStep();
			}
	
	}
	
	}
