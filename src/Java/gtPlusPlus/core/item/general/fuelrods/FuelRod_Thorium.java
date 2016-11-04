package gtPlusPlus.core.item.general.fuelrods;

public class FuelRod_Thorium extends FuelRod_Base {

	public FuelRod_Thorium(final String unlocalizedName, final String type, final int fuelLeft, final int maxFuel) {
		super(unlocalizedName, type, fuelLeft, maxFuel);
		this.setMaxDamage(maxFuel);
		this.maximumFuel = maxFuel;
		this.fuelRemaining = fuelLeft;
		this.fuelType = type;
	}

}
