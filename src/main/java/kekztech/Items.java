package kekztech;

import common.items.MetaItem_CraftingComponent;
import common.items.MetaItem_ReactorComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import util.Util;

import java.util.Arrays;

public enum Items {
	/*
	// Heat Vents
	T1HeatVent(0,0), T2HeatVent(1,0), T3HeatVent(2,0), T4HeatVent(3,0),
	T1ComponentHeatVent(4,0), T2ComponentHeatVent(5,0), T3ComponentHeatVent(6,0), T4ComponentHeatVent(7,0), 
	T1OverclockedHeatVent(8,0), T2OverclockedHeatVent(9,0), T3OverclockedHeatVent(10,0), T4OverclockedHeatVent(11,0), 
	// Heat Exchanger
	T1HeatExchanger(12,0), T2HeatExchanger(13,0), T3HeatExchanger(14,0), T4HeatExchanger(15,0),
	// Fuel Rods
	UraniumFuelRod(16,0), UraniumDualFuelRod(17,0), UraniumQuadFuelRod(18,0),
	ThoriumFuelRod(19,0), ThoriumDualFuelRod(20,0), ThoriumQuadFuelRod(21,0),
	MOXFuelRod(22,0), MOXDualFuelRod(23,0), MOXQuadFuelRod(24,0),
	NaquadahFuelRod(25,0), NaquadahDualFuelRod(26,0), NaquadahQuadFuelRod(27,0),
	Th_MOXFuelRod(28,0), Th_MOXDualFuelRod(29,0), Th_MOXQuadFuelRod(30,0),
	// Depleted Fuel Rods
	DepletedUraniumFuelRod(31,0), DepletedUraniumDualFuelRod(32,0), DepletedUraniumQuadFuelRod(33,0),
	DepletedThoriumFuelRod(34,0), DepletedThoriumDualFuelRod(35,0), DepletedThoriumQuadFuelRod(36,0),
	DepletedMOXFuelRod(37,0), DepletedMOXDualFuelRod(38,0), DepletedMOXQuadFuelRod(39,0),
	DepletedNaquadahFuelRod(40,0), DepletedNaquadahDualFuelRod(41,0), DepletedNaquadahQuadFuelRod(42,0),
	Th_DepletedMOXFuelRod(43,0), Th_DepletedMOXDualFuelRod(44,0), Th_DepletedMOXQuadFuelRod(45,0),
	// Neutron Reflectors
	T1NeutronReflector(46,0), T2NeutronReflector(47,0),
	// Coolant Cells
	HeliumCoolantCell360k(48,0), NaKCoolantCell360k(49,0),
	
	// Heat Pipes
	CopperHeatPipe(0,1), SilverHeatPipe(1,1), BoronArsenideHeatPipe(2,1), DiamondHeatPipe(3,1),
	BoronArsenideDust(4,1), IsotopicallyPureDiamondDust(5,1), AmineCarbamiteDust(6,1), 
	BoronArsenideCrystal(7,1), IsotopicallyPureDiamondCrystal(8,1),
	*/
	// Ceramics
	YSZCeramicDust(9,1), GDCCeramicDust(10,1),
	YttriaDust(11,1), ZirconiaDust(12,1), CeriaDust(13,1),
	YSZCeramicPlate(14,1), GDCCeramicPlate(15,1),
	// Error Item
	Error(0,1),
	// Configurator
	Configurator(0, 1);
	
	static {
			YttriaDust.setOreDictName("dustYttriumOxide");
			ZirconiaDust.setOreDictName("dustCubicZirconia");
	}
	
	private final int metaID;
	private final int identifier;
	
	Items(int metaID, int identifier) {
		this.metaID = metaID;
		this.identifier = identifier;
	}
	
	public int getMetaID() {
		return metaID;
	}
	
	String OreDictName;

	private void registerOreDict(){
		OreDictionary.registerOre(getOreDictName(),getNonOreDictedItemStack(1));
	}

	public static void registerOreDictNames(){
		Arrays.stream(Items.values()).filter(e -> e.getOreDictName() != null).forEach(Items::registerOreDict);
	}

	public ItemStack getNonOreDictedItemStack(int amount){
		return 	identifier == 0 ? 	new ItemStack(MetaItem_ReactorComponent.getInstance(),amount,this.getMetaID()) :
									new ItemStack(MetaItem_CraftingComponent.getInstance(),amount,this.getMetaID());
	}

	public ItemStack getOreDictedItemStack(int amount){
		return 	this.getOreDictName() != null ? 		Util.getStackofAmountFromOreDict(this.getOreDictName(),amount) :
				identifier == 0 ? 	new ItemStack(MetaItem_ReactorComponent.getInstance(),amount,this.getMetaID()) :
									new ItemStack(MetaItem_CraftingComponent.getInstance(),amount,this.getMetaID());
	}
	
	public String getOreDictName() {
		return OreDictName;
	}

	public void setOreDictName(String oreDictName) {
		OreDictName = oreDictName;
	}
}
