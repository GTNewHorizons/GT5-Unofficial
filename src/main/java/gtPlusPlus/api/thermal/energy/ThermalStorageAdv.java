package gtPlusPlus.api.thermal.energy;

public class ThermalStorageAdv extends ThermalStorage {
	
	public ThermalStorageAdv(int arg0) {
		this(arg0, arg0, arg0);
	}

	public ThermalStorageAdv(int arg0, int arg1) {
		this(arg0, arg1, arg1);
	}

	public ThermalStorageAdv(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public int receiveEnergyNoLimit(int arg0, boolean arg1) {
		int arg2 = Math.min(super.capacity - super.thermal_energy, arg0);
		if (!arg1) {
			super.thermal_energy += arg2;
		}

		return arg2;
	}

	public int extractEnergyNoLimit(int arg0, boolean arg1) {
		int arg2 = Math.min(super.thermal_energy, arg0);
		if (!arg1) {
			super.thermal_energy -= arg2;
		}

		return arg2;
	}
}