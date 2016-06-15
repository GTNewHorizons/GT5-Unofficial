package miscutil.core.item.general.fuelrods;

public class FuelRod_Thorium extends FuelRod_Base{

	public FuelRod_Thorium(String unlocalizedName, String type, int fuelLeft, int maxFuel) {
		super(unlocalizedName, type, fuelLeft, maxFuel);
		this.setMaxDamage(maxFuel);
		this.maximumFuel = maxFuel;
		this.fuelRemaining = fuelLeft;
		this.fuelType = type;
	}

}
