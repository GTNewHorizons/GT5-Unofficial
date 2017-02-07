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
	
	
	public final Material NEON = new Material("Neon", 25600, new short[]{220, 75, 75}, -248, -246, 10, 10, false, "Ne", 0);//Not a GT Inherited Material
	
	
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
	
	
	public final Material GERMANIUM = new Material("Germanium", 51200, new short[]{255, 250, 205}, 937, 2830, 32, 41, false, "Ge", 0);//Not a GT Inherited Material
	
	
	public final Material ARSENIC = MaterialUtils.generateMaterialFromGtENUM(Materials.Arsenic);
	
	
	public final Material SELENIUM = new Material("Selenium", 51200, new short[]{255, 250, 205}, 217, 685, 34, 45, false, "Se", 0);//Not a GT Inherited Material
	public final Material BROMINE = new Material("Bromine", 51200, new short[]{255, 250, 205}, -7, 58, 35, 45, false, "Br", 0);//Not a GT Inherited Material
	public final Material KRYPTON = new Material("Krypton", 51200, new short[]{255, 250, 205}, -157, -153, 36, 48, false, "Kr", 0);//Not a GT Inherited Material
	
	
	public final Material RUBIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Rubidium);
	public final Material STRONTIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Strontium);
	public final Material YTTRIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Yttrium);
	public final Material ZIRCONIUM = new Material("Zirconium", 25600, new short[]{255, 250, 205}, 1855, 4377, 40, 51, false, "Zr", 0);//Not a GT Inherited Material
	public final Material NIOBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Niobium);
	public final Material MOLYBDENUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Molybdenum);
	
	
	public final Material TECHNETIUM = new Material("Technetium", 25600, new short[]{255, 250, 205}, 2200, 4877, 43, 55, false, "Tc", 0);//Not a GT Inherited Material
	public final Material RUTHENIUM  = new Material("Ruthenium", 25600, new short[]{255, 250, 205}, 2250, 3900, 44, 57, false, "Ru", 0);//Not a GT Inherited Material
	public final Material RHODIUM = new Material("Rhodium", 25600, new short[]{255, 250, 205}, 1966, 3727, 45, 58, false, "Rh", 0);//Not a GT Inherited Material
	
	
	public final Material PALLADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Palladium);
	public final Material SILVER = MaterialUtils.generateMaterialFromGtENUM(Materials.Silver);
	public final Material CADMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cadmium);
	public final Material INDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Indium);
	public final Material TIN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tin);


//Antimony - to the end

	public final Material ANTIMONY = MaterialUtils.generateMaterialFromGtENUM(Materials.Antimony);
	
	
	public final Material TELLURIUM = new Material("Tellurium", 25600, new short[]{255, 250, 205}, 449, 989, 52, 76, false, "Te", 0);//Not a GT Inherited Material
	public final Material IODINE = new Material("Iodine", 25600, new short[]{255, 250, 205}, 114, 184, 53, 74, false, "I", 0);//Not a GT Inherited Material
	public final Material XENON = new Material("Xenon", 25600, new short[]{255, 250, 205}, -111, -108, 54, 77, false, "Xe", 0);//Not a GT Inherited Material
	
	
	public final Material CESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Caesium);	
	public final Material BARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Barium);
	public final Material LANTHANUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lanthanum);
	public final Material CERIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cerium);
	public final Material PRASEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Praseodymium);
	public final Material NEODYMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Neodymium);
	public final Material PROMETHIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Promethium);
	public final Material SAMARIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Samarium);
	public final Material EUROPIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Europium);
	public final Material GADOLINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Gadolinium);
	public final Material TERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Terbium);
	
	public final Material DYSPROSIUM = new Material("Dysprosium", 25600, new short[]{255, 250, 205}, 1412, 2562, 66, 97, false, "Dy", 0);//Not a GT Inherited Material
	
	public final Material HOLMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Holmium);
	public final Material ERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Erbium);
	public final Material THULIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Thulium);
	public final Material YTTERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Ytterbium);
	public final Material LUTETIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lutetium);
	
	public final Material HAFNIUM = new Material("Hafnium", 25600, new short[]{255, 250, 205}, 2150, 5400, 72, 106, false, "Hf", 0);//Not a GT Inherited Material






	//Second 50 elements
	public final Material TANTALUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Tantalum);
	public final Material TUNGSTEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tungsten);
	
	
	public final Material RHENIUM = new Material("name", 25600, new short[]{255, 250, 205}, melt, boil, proton, neutron, false, "Zr", 0);//Not a GT Inherited Material
	
	
	public final Material OSMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmium);
	public final Material IRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Iridium);	
	public final Material PLATINUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);	
	public final Material GOLD = MaterialUtils.generateMaterialFromGtENUM(Materials.Gold);	
	public final Material MERCURY = MaterialUtils.generateMaterialFromGtENUM(Materials.Mercury); //Mercury
	
	
	public final Material THALLIUM = new Material("name", 25600, new short[]{255, 250, 205}, melt, boil, proton, neutron, false, "Zr", 0);//Not a GT Inherited Material
	
	
	public final Material LEAD = MaterialUtils.generateMaterialFromGtENUM(Materials.Lead);	
	public final Material BISMUTH = MaterialUtils.generateMaterialFromGtENUM(Materials.Bismuth);
	
	
	public final Material POLONIUM = new Material("name", 25600, new short[]{255, 250, 205}, melt, boil, proton, neutron, false, "Zr", 0);//Not a GT Inherited Material
	public final Material ASTATINE = new Material("name", 25600, new short[]{255, 250, 205}, melt, boil, proton, neutron, false, "Zr", 0);//Not a GT Inherited Material
	
	
	public final Material RADON = MaterialUtils.generateMaterialFromGtENUM(Materials.Radon);
	
	
	public final Material FRANCIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Francium); //Francium
	public final Material RADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Radium); //Radium
	public final Material ACTINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Actinium); //Actinium
	
	public final Material THORIUM = new Material("Thorium", 51200, Materials.Thorium.mRGBa, Materials.Thorium.mMeltingPoint, Materials.Thorium.mBlastFurnaceTemp, 90, 142, false, MaterialUtils.superscript("Th"), 1);
			
	public final Material PROTACTINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Protactinium); //Protactinium
	
	public final Material URANIUM238 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium);
	public final Material URANIUM235 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium235);

	public final Material NEPTUNIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Neptunium); //Neptunium
	
	public final Material PLUTONIUM244 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium);
	public final Material PLUTONIUM241 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium241);
	public final Material AMERICIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Americium); //Americium
	
	
	public final Material CURIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Curium); //Curium
	public final Material BERKELIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Berkelium); //Berkelium
	public final Material CALIFORNIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Californium); //Californium
	public final Material EINSTEINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Einsteinium); //Einsteinium
	public final Material FERMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Fermium); //100 Fermium
	
	
	


	//Misc
	public final Material AER = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedAir);
	public final Material IGNIS = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedFire);
	public final Material TERRA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedEarth);
	public final Material AQUA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedWater);
}
