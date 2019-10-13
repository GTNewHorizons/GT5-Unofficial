package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.client.CustomTextureSet.TextureSets;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MISC_MATERIALS {	

	/*
	 * Some of these materials purely exist as data objects, items will most likely be assigned seperately.
	 * Most are just compositions which will have dusts assigned to them.
	 */

	public static void run() {
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_OXIDE, false);
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_HYDROXIDE, false);
		WATER.registerComponentForMaterial(FluidUtils.getWater(1000));
	}

	public static final Material STRONTIUM_OXIDE = new Material(
			"Strontium Oxide", 
			MaterialState.SOLID, 
			TextureSet.SET_METALLIC,
			null,
			-1,
			-1,
			-1,
			-1, 
			false, 
			"SrO",
			0, 
			false,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 1)
			});

	public static final Material STRONTIUM_HYDROXIDE = new Material(
			"Strontium Hydroxide", 
			MaterialState.SOLID, 
			TextureSet.SET_METALLIC,
			null,
			-1,
			-1,
			-1,
			-1, 
			false, 
			"Sr(OH)2",
			0, 
			false,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
					new MaterialStack(MISC_MATERIALS.HYDROXIDE, 2)
			});

	public static final Material SELENIUM_DIOXIDE = new Material(
			"Selenium Dioxide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SELENIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 2)
			});

	public static final Material SELENIOUS_ACID = new Material(
			"Selenious Acid",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(SELENIUM_DIOXIDE, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 8),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 4)
			});

	public static final Material HYDROGEN_CYANIDE = new Material(
			"Hydrogen Cyanide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			4, //Melting Point in C
			26, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().CARBON, 1),
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1)
			});

	public static final Material CARBON_DIOXIDE = new Material(
			"Carbon Dioxide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CARBON, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 2)
			});


	/*
	 * Rare Earth Materials
	 */

	public static final Material RARE_EARTH_LOW = new Material(
			"Rare Earth (I)", //Material Name
			MaterialState.ORE, //State
			TextureSets.GEM_A.get(), //Texture Set
			null, //Material Colour
			1200,
			2500,
			-1,
			-1,
			-1, //Radiation
			new MaterialStack[]{
					new MaterialStack(ORES.GREENOCKITE, 1),
					new MaterialStack(ORES.LANTHANITE_CE, 1),					
					new MaterialStack(ORES.AGARDITE_CD, 1),					
					new MaterialStack(ORES.XENOTIME, 1),
					new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.NetherQuartz), 1),
					new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Galena), 1),
					new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Chalcopyrite), 1),
					new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Cobaltite), 1),
					new MaterialStack(ELEMENT.STANDALONE.GRANITE, 1)
			});

	public static final Material RARE_EARTH_MID = new Material(
			"Rare Earth (II)", //Material Name
			MaterialState.ORE, //State
			TextureSets.ENRICHED.get(), //Texture Set
			null, //Material Colour
			3500,
			5000,
			-1,
			-1,
			-1, //Radiation
			new MaterialStack[]{
					new MaterialStack(ORES.LANTHANITE_ND, 1),
					new MaterialStack(ORES.AGARDITE_ND, 1),					
					new MaterialStack(ORES.YTTRIAITE, 1),					
					new MaterialStack(ORES.CROCROITE, 1),					
					new MaterialStack(ORES.NICHROMITE, 1),					
					new MaterialStack(ORES.ZIRCON, 1),
					new MaterialStack(ELEMENT.STANDALONE.GRANITE, 1),
					new MaterialStack(ELEMENT.STANDALONE.BLACK_METAL, 1),
					new MaterialStack(ELEMENT.STANDALONE.RUNITE, 1)			
			});

	public static final Material RARE_EARTH_HIGH = new Material(
			"Rare Earth (III)", //Material Name
			MaterialState.ORE, //State
			TextureSets.REFINED.get(), //Texture Set
			null, //Material Colour
			5200,
			7500,
			-1,
			-1,
			-1, //Radiation
			new MaterialStack[]{
					new MaterialStack(ORES.GADOLINITE_Y, 1),
					new MaterialStack(ORES.LEPERSONNITE, 1),
					new MaterialStack(ORES.FLORENCITE, 1),					
					new MaterialStack(ORES.FLUORCAPHITE, 1),
					new MaterialStack(ORES.LAUTARITE, 1),
					new MaterialStack(ORES.DEMICHELEITE_BR, 1),					
					new MaterialStack(ORES.ALBURNITE, 1),
					new MaterialStack(ORES.KASHINITE, 1),
					new MaterialStack(ORES.AGARDITE_LA, 1),
			});

	public static final Material WATER = new Material(
			"Water",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 2),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 1)
			});

	//OH
	public static final Material HYDROXIDE = new Material(
			"Hydroxide", //Material Name
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1)
			});

	//NH3
	public static final Material AMMONIA = new Material(
			"Ammonia", //Material Name
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-77, //Melting Point in C
			-33, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 3)
			});

	//NH4
	public static final Material AMMONIUM = new Material(
			"Ammonium", //Material Name
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 4)
			});


	public static final Material HYDROGEN_CHLORIDE = new Material(
			"Hydrogen Chloride",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().CHLORINE, 1),
			});


	public static final Material SODIUM_CHLORIDE = new Material(
			"Sodium Chloride",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SODIUM, 1),
					new MaterialStack(ELEMENT.getInstance().CHLORINE, 1),
			});


	public static final Material SODIUM_HYDROXIDE = new Material(
			"Sodium Hydroxide",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SODIUM, 1),
					new MaterialStack(HYDROXIDE, 1),
			});

	public static final Material SALT_WATER = new Material(
			"Salt Water",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(WATER, 3),
					new MaterialStack(SODIUM_CHLORIDE, 1),
			});

	public static final Material BRINE = new Material(
			"Brine",
			MaterialState.PURE_LIQUID,
			new MaterialStack[]{
					new MaterialStack(SALT_WATER, 1),
					new MaterialStack(SODIUM_CHLORIDE, 2),
			});



}
