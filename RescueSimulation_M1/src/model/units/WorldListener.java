package model.units;

import simulation.Simulatable;

public interface WorldListener {
	public void assignAddress(Simulatable sim, int x , int y);
}
