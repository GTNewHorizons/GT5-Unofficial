package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.util.materials.MaterialUtils;

public final class ELEMENT {
	
	private static final ELEMENT thisClass = new ELEMENT();
	
	public ELEMENT(){
		
	}	
	
	public static ELEMENT getInstance(){
	return thisClass;	
	}

	//First 50 Elements
	public final Material HYDROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Hydrogen);
	public final Material HELIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Helium);
	public final Material LITHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lithium);
	public final Material BERYLLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Beryllium);
	public final Material BORON = MaterialUtils.generateMaterialFromGtENUM(Materials.Boron);
	public final Material CARBON = MaterialUtils.generateMaterialFromGtENUM(Materials.Carbon);
	public final Material NITROGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Nitrogen);
	public final Material OXYGEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Oxygen);
	public final Material FLUORINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Fluorine);
	//public final Material NEON = MaterialUtils.generateMaterialFromGtENUM(Materials.Ne);
	public final Material SODIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Sodium);
	public final Material MAGNESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Magnesium);
	public final Material ALUMINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Aluminium);
	public final Material SILICON = MaterialUtils.generateMaterialFromGtENUM(Materials.Silicon);
	public final Material PHOSPHORUS = MaterialUtils.generateMaterialFromGtENUM(Materials.Phosphorus);
	public final Material SULFUR = MaterialUtils.generateMaterialFromGtENUM(Materials.Sulfur);
	public final Material CHLORINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Chlorine);
	public final Material ARGON = MaterialUtils.generateMaterialFromGtENUM(Materials.Argon);
	public final Material POTASSIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Potassium);
	public final Material CALCIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Calcium);
	public final Material SCANDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Scandium);
	public final Material TITANIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Titanium);
	public final Material VANADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Vanadium);
	public final Material CHROMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Chrome);
	public final Material MANGANESE = MaterialUtils.generateMaterialFromGtENUM(Materials.Manganese);
	public final Material IRON = MaterialUtils.generateMaterialFromGtENUM(Materials.Iron);
	public final Material COBALT = MaterialUtils.generateMaterialFromGtENUM(Materials.Cobalt);
	public final Material NICKEL = MaterialUtils.generateMaterialFromGtENUM(Materials.Nickel);
	public final Material COPPER = MaterialUtils.generateMaterialFromGtENUM(Materials.Copper);
	public final Material ZINC = MaterialUtils.generateMaterialFromGtENUM(Materials.Zinc);
	public final Material GALLIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Gallium);
	//public final Material GERMANIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Germanium);
	public final Material ARSENIC = MaterialUtils.generateMaterialFromGtENUM(Materials.Arsenic);
	//public final Material SELENIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Selenium);
	//public final Material BROMINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Bromine);
	//public final Material KRYPTON = MaterialUtils.generateMaterialFromGtENUM(Materials.Krypton);
	public final Material RUBIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Rubidium);
	public final Material STRONTIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Strontium);
	public final Material YTTRIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Yttrium);
	public final Material ZIRCONIUM = new Material("Zirconium", new short[]{255, 250, 205}, 1855, 4377, 40, 51, false, "Zr", 0);//Not a GT Inherited Material
	public final Material NIOBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Niobium);
	public final Material MOLYBDENUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Molybdenum);
	//public final Material TECHNETIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Technetium);
	//public final Material RUTHENIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Ruthenium);
	//public final Material RHODIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Rhodium);
	public final Material PALLADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Palladium);
	public final Material SILVER = MaterialUtils.generateMaterialFromGtENUM(Materials.Silver);
	public final Material CADMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cadmium);
	public final Material INDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Indium);
	public final Material TIN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tin);









	//Second 50 elements
	public final Material TANTALUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Tantalum);
	public final Material TUNGSTEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tungsten);
	public final Material OSMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmium);
	public final Material IRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Iridium);	
	public final Material PLATINUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);	
	public final Material GOLD = MaterialUtils.generateMaterialFromGtENUM(Materials.Gold);	
	public final Material LEAD = MaterialUtils.generateMaterialFromGtENUM(Materials.Lead);	
	public final Material BISMUTH = MaterialUtils.generateMaterialFromGtENUM(Materials.Bismuth);	
	public final Material RADON = MaterialUtils.generateMaterialFromGtENUM(Materials.Radon);	
	public final Material THORIUM = new Material("Thorium", Materials.Thorium.mRGBa, Materials.Thorium.mMeltingPoint, Materials.Thorium.mBlastFurnaceTemp, 90, 142, false, MaterialUtils.superscript("Th"), 1);
			/*MaterialUtils.generateMaterialFromGtENUM(Materials.Thorium);*/	
	public final Material URANIUM238 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium);
	public final Material URANIUM235 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium235);	
	public final Material PLUTONIUM244 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium);
	public final Material PLUTONIUM241 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium241);
	
	
	


	//Misc

	public final Material AER = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedAir);
	public final Material IGNIS = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedFire);
	public final Material TERRA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedEarth);
	public final Material AQUA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedWater);
}
