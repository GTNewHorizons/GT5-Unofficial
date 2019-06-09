package kekztech;

public enum Items {
	// Heat Vents
	T1HeatVent(0), T2HeatVent(1), T3HeatVent(2), T4HeatVent(3),
	T1ComponentHeatVent(4), T2ComponentHeatVent(5), T3ComponentHeatVent(6), T4ComponentHeatVent(7), 
	T1OverclockedHeatVent(8), T2OverclockedHeatVent(9), T3OverclockedHeatVent(10), T4OverclockedHeatVent(11), 
	// Heat Exchanger
	T1HeatExchanger(12), T2HeatExchanger(13), T3HeatExchanger(14), T4HeatExchanger(15),
	// Fuel Rods
	UraniumFuelRod(16), UraniumDualFuelRod(17), UraniumQuadFuelRod(18),
	ThoriumFuelRod(19), ThoriumDualFuelRod(20), ThoriumQuadFuelRod(21),
	MOXFuelRod(22), MOXDualFuelRod(23), MOXQuadFuelRod(24),
	NaquadahFuelRod(25), NaquadahDualFuelRod(26), NaquadahQuadFuelRod(27),
	Th_MOXFuelRod(28), Th_MOXDualFuelRod(29), Th_MOXQuadFuelRod(30),
	// Depleted Fuel Rods
	DepletedUraniumFuelRod(31), DepletedUraniumDualFuelRod(32), DepletedUraniumQuadFuelRod(33),
	DepletedThoriumFuelRod(34), DepletedThoriumDualFuelRod(35), DepletedThoriumQuadFuelRod(36),
	DepletedMOXFuelRod(37), DepletedMOXDualFuelRod(38), DepletedMOXQuadFuelRod(39),
	DepletedNaquadahFuelRod(40), DepletedNaquadahDualFuelRod(41), DepletedNaquadahQuadFuelRod(42),
	Th_DepletedMOXFuelRod(43), Th_DepletedMOXDualFuelRod(44), Th_DepletedMOXQuadFuelRod(45),
	// Neutron Reflectors
	T1NeutronReflector(46), T2NeutronReflector(47),
	// Coolant Cells
	HeliumCoolantCell360k(48), NaKCoolantCell360k(49),
	
	// Heat Pipes
	CopperHeatPipe(0), SilverHeatPipe(1), BoronArsenideHeatPipe(2), DiamondHeatPipe(3),
	BoronArsenideDust(4), IsotopicallyPureDiamondDust(5), AmineCarbamiteDust(6), 
	BoronArsenideCrystal(7), IsotopicallyPureDiamondCrystal(8),
	// Ceramics
	YSZCeramicDust(9), GDCCeramicDust(10),
	YttriaDust(11), ZirconiaDust(12), CeriaDust(13),
	YSZCeramicPlate(14), GDCCeramicPlate(15),
	// Error Item
	Error(0);
	
	private final int metaID;
	
	private Items(int metaID) {
		this.metaID = metaID;
	}
	
	public int getMetaID() {
		return metaID;
	}
	
}
