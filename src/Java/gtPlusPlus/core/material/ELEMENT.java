package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class ELEMENT {

	private static final ELEMENT thisClass = new ELEMENT();
	
	static {
		Logger.MATERIALS("Initialising Base Elements.");
	}

	public ELEMENT(){		
		//GTNH Trinium Handling		
		if (CORE.GTNH){
			TRINIUM  = MaterialUtils.generateMaterialFromGtENUM(MaterialUtils.getMaterialByName("Trinium"));
			TRINIUM_REFINED = new Material("Refined Trinium", MaterialState.SOLID, new short[]{210, 255, 170}, 4304, 14057, 181, 133, false, "Ke", 0, new MaterialStack[]{new MaterialStack(TRINIUM, 1)});//Not a GT Inherited Material
		}
		else {
			TRINIUM = new Material("Trinium", MaterialState.SOLID, new short[]{70, 110, 30}, 604, 4057, 181, 133, false, "Ke", 0, false);//Not a GT Inherited Material
			TRINIUM_REFINED = new Material("Refined Trinium", MaterialState.SOLID, new short[]{210, 255, 170}, 4304, 14057, 181, 133, false, "Ke", 0, new MaterialStack[]{new MaterialStack(TRINIUM, 1)});//Not a GT Inherited Material
		}		
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
	public final Material NEON = new Material("Neon", MaterialState.GAS, 12800, new short[]{255, 255, 255}, -248, -246, 10, 10, false, "Ne", 0);//Not a GT Inherited Material
	public final Material SODIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Sodium);
	public final Material MAGNESIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Magnesium);
	public final Material ALUMINIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Aluminium);
	public final Material SILICON = MaterialUtils.generateMaterialFromGtENUM(Materials.Silicon);
	public final Material PHOSPHORUS = MaterialUtils.generateMaterialFromGtENUM(Materials.Phosphor);
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
	public final Material GERMANIUM = new Material("Germanium", MaterialState.SOLID, 51200, new short[]{200, 200, 200}, 937, 2830, 32, 41, false, "Ge", 0);//Not a GT Inherited Material
	public final Material ARSENIC = MaterialUtils.generateMaterialFromGtENUM(Materials.Arsenic);
	public final Material SELENIUM = new Material("Selenium", MaterialState.SOLID, 51200, new short[]{190, 190, 190}, 217, 685, 34, 45, false, "Se", 0);//Not a GT Inherited Material
	public final Material BROMINE = new Material("Bromine", MaterialState.LIQUID, 51200, new short[]{200, 25, 25}, -7, 58, 35, 45, false, "Br", 0);//Not a GT Inherited Material
	public final Material KRYPTON = new Material("Krypton", MaterialState.GAS, 12800, new short[]{255, 255, 255}, -157, -153, 36, 48, false, "Kr", 0);//Not a GT Inherited Material
	public final Material RUBIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Rubidium);
	public final Material STRONTIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Strontium);
	public final Material YTTRIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Yttrium);
	public final Material ZIRCONIUM = new Material("Zirconium", MaterialState.SOLID, 51200, new short[]{255, 250, 205}, 1855, 4377, 40, 51, false, "Zr", 0);//Not a GT Inherited Material
	public final Material NIOBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Niobium);
	public final Material MOLYBDENUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Molybdenum);
	public final Material TECHNETIUM = new Material("Technetium", MaterialState.SOLID, 25600, new short[]{220, 220, 220}, 2200, 4877, 43, 55, false, "Tc", 2);//Not a GT Inherited Material
	public final Material RUTHENIUM  = new Material("Ruthenium", MaterialState.SOLID, 25600, new short[]{220, 220, 220}, 2250, 3900, 44, 57, false, "Ru", 0);//Not a GT Inherited Material
	public final Material RHODIUM = new Material("Rhodium", MaterialState.SOLID, 25600, new short[]{220, 220, 220}, 1966, 3727, 45, 58, false, "Rh", 0);//Not a GT Inherited Material
	public final Material PALLADIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Palladium);
	public final Material SILVER = MaterialUtils.generateMaterialFromGtENUM(Materials.Silver);
	public final Material CADMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Cadmium);
	public final Material INDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Indium);
	public final Material TIN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tin);
	public final Material ANTIMONY = MaterialUtils.generateMaterialFromGtENUM(Materials.Antimony);
	public final Material TELLURIUM = new Material("Tellurium", MaterialState.SOLID, 25600, new short[]{210, 210, 210}, 449, 989, 52, 76, false, "Te", 0);//Not a GT Inherited Material
	public final Material IODINE = new Material("Iodine", MaterialState.SOLID, 25600, new short[]{96, 96, 96}, 114, 184, 53, 74, false, "I", 0);//Not a GT Inherited Material
	public final Material XENON = new Material("Xenon", MaterialState.GAS, 12800, new short[]{255, 255, 255}, -111, -108, 54, 77, false, "Xe", 0);//Not a GT Inherited Material
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
	public final Material DYSPROSIUM = new Material("Dysprosium", MaterialState.SOLID, 25600, new short[]{180, 180, 180}, 1412, 2562, 66, 97, false, "Dy", 0);//Not a GT Inherited Material
	public final Material HOLMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Holmium);
	public final Material ERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Erbium);
	public final Material THULIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Thulium);
	public final Material YTTERBIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Ytterbium);
	public final Material LUTETIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Lutetium);
	public final Material HAFNIUM = new Material("Hafnium", MaterialState.SOLID, 25600, new short[]{128, 128, 128}, 2150, 5400, 72, 106, false, "Hf", 0);//Not a GT Inherited Material

	//Second 50 elements
	public final Material TANTALUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Tantalum);
	public final Material TUNGSTEN = MaterialUtils.generateMaterialFromGtENUM(Materials.Tungsten);
	public final Material RHENIUM = new Material("Rhenium", MaterialState.SOLID, 25600, new short[]{150, 150, 150}, 3180, 3627, 75, 111, false, "Re", 0);//Not a GT Inherited Material
	public final Material OSMIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmium);
	public final Material IRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Iridium);
	public final Material PLATINUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);
	public final Material GOLD = MaterialUtils.generateMaterialFromGtENUM(Materials.Gold);
	public final Material MERCURY = MaterialUtils.generateMaterialFromGtENUM(Materials.Mercury); //Mercury
	public final Material THALLIUM = new Material("Thallium", MaterialState.SOLID, 25600, new short[]{175, 175, 175}, 304, 1457, 81, 123, false, "Tl", 0);//Not a GT Inherited Material
	public final Material LEAD = MaterialUtils.generateMaterialFromGtENUM(Materials.Lead);
	public final Material BISMUTH = MaterialUtils.generateMaterialFromGtENUM(Materials.Bismuth);
	public final Material POLONIUM = new Material("Polonium", MaterialState.SOLID, 25600, new short[]{180, 170, 180}, 254, 962, 84, 125, false, "Po", 1);//Not a GT Inherited Material
	public final Material ASTATINE = new Material("Astatine", MaterialState.SOLID, 25600, new short[]{170, 180, 170}, 302, 337, 85, 125, false, "At", 1);//Not a GT Inherited Material
	public final Material RADON = MaterialUtils.generateMaterialFromGtENUM(Materials.Radon);
	public final Material FRANCIUM = new Material("Francium", MaterialState.SOLID, 25600, new short[]{170, 160, 170}, 27, 677, 87, 136, false, "Fr", 1);//Not a GT Inherited Material
	public final Material RADIUM = new Material("Radium", MaterialState.SOLID, 25600, new short[]{165, 165, 165}, 700, 1737, 88, 138, false, "Ra", 1);//Not a GT Inherited Material
	public final Material ACTINIUM = new Material("Actinium", MaterialState.SOLID, 25600, new short[]{150, 165, 165}, 1050, 3200, 89, 138, false, "Ac", 1);//Not a GT Inherited Material
	public final Material THORIUM = new Material("Thorium", MaterialState.SOLID, 51200, Materials.Thorium.mRGBa, Materials.Thorium.mMeltingPoint, Materials.Thorium.mBlastFurnaceTemp, 90, 142, false, StringUtils.superscript("Th"), 1);
	public final Material PROTACTINIUM = new Material("Protactinium", MaterialState.SOLID, 25600, new short[]{190, 150, 170}, 1568, 4027, 91, 140, false, "Pa", 1);//Not a GT Inherited Material
	public final Material URANIUM238 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium);
	public final Material URANIUM235 = MaterialUtils.generateMaterialFromGtENUM(Materials.Uranium235);
	public final Material NEPTUNIUM = new Material("Neptunium", MaterialState.SOLID, 25600, new short[]{200, 220, 205}, 640, 3902, 93, 144, false, "Np", 2);//Not a GT Inherited Material
	public final Material PLUTONIUM244 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium);
	public final Material PLUTONIUM241 = MaterialUtils.generateMaterialFromGtENUM(Materials.Plutonium241);
	public final Material AMERICIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Americium); //Americium
	public final Material CURIUM = new Material("Curium", MaterialState.SOLID, 25600, new short[]{175, 85, 110}, 1340, 3110, 96, 151, false, "Cm", 3);//Not a GT Inherited Material
	public final Material BERKELIUM = new Material("Berkelium", MaterialState.SOLID, 25600, new short[]{110, 250, 85}, 985, 710, 97, 150, false, "Bk", 4);//Not a GT Inherited Material
	public final Material CALIFORNIUM = new Material("Californium", MaterialState.SOLID, 25600, new short[]{85, 110, 205}, 899, 1472, 98, 153, false, "Cf", 4);//Not a GT Inherited Material
	public final Material EINSTEINIUM = new Material("Einsteinium", MaterialState.SOLID, 25600, new short[]{255, 85, 110}, 860, 3500, 99, 153, false, "Es", 5);//Not a GT Inherited Material //Boiling Point is made up
	public final Material FERMIUM = new Material("Fermium", MaterialState.LIQUID, 25600, new short[]{75, 90, 25}, 1527, 3850, 100, 157, false, "Fm", 5);//Not a GT Inherited Material //Boiling Point is made up

	//Misc
	public final Material AER = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedAir);
	public final Material IGNIS = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedFire);
	public final Material TERRA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedEarth);
	public final Material AQUA = MaterialUtils.generateMaterialFromGtENUM(Materials.InfusedWater);
	
	//Fictional	
	public final Material YELLORIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Yellorium);
	public final Material NAQUADAH = MaterialUtils.generateMaterialFromGtENUM(Materials.Naquadah);
	public final Material TRINIUM;
	public final Material TRINIUM_REFINED;
	//https://github.com/Blood-Asp/GT5-Unofficial/issues/609
}
