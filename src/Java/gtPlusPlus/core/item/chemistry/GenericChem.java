package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GenericChem extends ItemPackage {

	/**
	 * Materials
	 */	
	
	public static final Material BAKELITE = new Material("Bakelite", MaterialState.SOLID, TextureSet.SET_DULL, new short[]{90, 140, 140}, 120, 240, 23, 24, true, null, 0);//Not a GT Inherited Material
	public static final Material NYLON = new Material("Nylon", MaterialState.SOLID, TextureSet.SET_SHINY, new short[]{45, 45, 45}, 300, 600, 44, 48, true, null, 0);//Not a GT Inherited Material
	public static final Material CARBYNE = new Material("Carbyne", MaterialState.SOLID, TextureSet.SET_DULL, new short[]{25, 25, 25}, 2500, 5000, 63, 52, true, null, 0);//Not a GT Inherited Material
	
	
	//Refined PTFE
	public static final Material TEFLON = new Material(
			"Teflon",
			MaterialState.SOLID,
			TextureSet.SET_SHINY,
			new short[] { 75, 45, 75 },
			330, 640,
			-1, -1,
			false,
			null,
			0,
			new MaterialStack[] {
					new MaterialStack(NONMATERIAL.PTFE, 75),
					new MaterialStack(NONMATERIAL.PLASTIC, 15),
					new MaterialStack(ELEMENT.getInstance().CARBON, 5),
					new MaterialStack(ELEMENT.getInstance().SODIUM, 5)
					});// Not a GT
																										// Inherited
																										// Material

	//public static final Material HYPOGEN = new Material("Hypogen", MaterialState.SOLID, TextureSets.NUCLEAR.get(), new short[]{220, 120, 75}, 12255, 19377, 240, 251, true, "Hy⚶", 0);//Not a GT Inherited Material
	//public static final Material HYPOGEN = new Material("Hypogen", MaterialState.SOLID, TextureSets.NUCLEAR.get(), new short[]{220, 120, 75}, 12255, 19377, 240, 251, true, "Hy⚶", 0);//Not a GT Inherited Material
	//public static final Material Nylon = new Material();
	
	/**
	 * Fluids
	 */

	public Fluid Benzene;
	public Fluid NitroBenzene;
	public Fluid Aniline;
	public Fluid Polyurethane;
	public Fluid Phenol; //https://en.wikipedia.org/wiki/Phenol#Uses
	public Fluid Cyclohexane; //https://en.wikipedia.org/wiki/Cyclohexane	
	public Fluid Cyclohexanone; //https://en.wikipedia.org/wiki/Cyclohexanone
	
	public Fluid Cadaverine; //https://en.wikipedia.org/wiki/Cadaverine
	public Fluid Putrescine; //https://en.wikipedia.org/wiki/Putrescine
	

	public static Fluid Ethylanthraquinone2;
	public static Fluid Ethylanthrahydroquinone2;
	public static Fluid Hydrogen_Peroxide;
	public static Fluid Lithium_Peroxide;

	/**
	 * Items
	 */

	// Phenol Byproducts
	public Item PhenolicResins; //https://en.wikipedia.org/wiki/Phenol_formaldehyde_resin
	public ItemGenericChemBase mGenericChemItem1;



	@Override
	public void items() {		
		PhenolicResins = ItemUtils.generateSpecialUseDusts("phenolicresins", "Phenolic Resin", "HOC6H4CH2OH", Utils.rgbtoHexValue(80, 40, 40))[0];		
		MaterialGenerator.generate(BAKELITE, false);	
		MaterialGenerator.generate(NYLON, false);
		MaterialGenerator.generate(TEFLON, false);		

		mGenericChemItem1 = new ItemGenericChemBase();		
		
		registerItemStacks();
		registerOreDict();
	}

	private ItemStack mCatalystCarrier;
	
	public ItemStack mRedCatalyst;
	public ItemStack mYellowCatalyst;
	public ItemStack mBlueCatalyst;
	public ItemStack mOrangeCatalyst;
	public ItemStack mPurpleCatalyst;
	public ItemStack mBrownCatalyst;
	
	
	public void registerItemStacks() {

		mCatalystCarrier = ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 13, 1);
		
		mRedCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 0, 1);
		mYellowCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 1, 1);
		mBlueCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 2, 1);
		mOrangeCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 3, 1);
		mPurpleCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 4, 1);
		mBrownCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 5, 1);
		
	}
	
	public void registerOreDict() {

		ItemUtils.addItemToOreDictionary(mRedCatalyst, "catalystIronCopper");
		ItemUtils.addItemToOreDictionary(mYellowCatalyst, "catalystTungstenNickel");
		ItemUtils.addItemToOreDictionary(mBlueCatalyst, "catalystCobaltTitanium");
		ItemUtils.addItemToOreDictionary(mOrangeCatalyst, "catalystVanadiumPalladium");
		ItemUtils.addItemToOreDictionary(mPurpleCatalyst, "catalystIridiumRuthenium");
		ItemUtils.addItemToOreDictionary(mBrownCatalyst, "catalystNickelAluminium");
		
	}

	@Override
	public void blocks() {}

	@Override
	public void fluids() {
		
		if (!FluidRegistry.isFluidRegistered("benzene")) {
			Benzene = FluidUtils.generateFluidNoPrefix("benzene", "Benzene", 278,	new short[] { 100, 70, 30, 100 }, true);			
		}
		else {
			Benzene = FluidRegistry.getFluid("benzene");
		}
		
		NitroBenzene = FluidUtils.generateFluidNoPrefix("nitrobenzene", "NitroBenzene", 278,	new short[] { 70, 50, 40, 100 }, true);
		
		Aniline = FluidUtils.generateFluidNoPrefix("aniline", "Aniline", 266,	new short[] { 100, 100, 30, 100 }, true);
		
		Polyurethane = FluidUtils.generateFluidNoPrefix("polyurethane", "Polyurethane", 350,	new short[] { 100, 70, 100, 100 }, true);
		
		if (!FluidRegistry.isFluidRegistered("phenol")) {
			Phenol = FluidUtils.generateFluidNoPrefix("phenol", "Phenol", 313,	new short[] { 100, 70, 30, 100 }, true);			
		}
		else {
			Phenol = FluidRegistry.getFluid("phenol");
		}		
		
		Cyclohexane = FluidUtils.generateFluidNoPrefix("cyclohexane", "Cyclohexane", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);		
		Cyclohexanone = FluidUtils.generateFluidNoPrefix("cyclohexanone", "Cyclohexanone", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		
		Cadaverine = FluidUtils.generateFluidNoPrefix("cadaverine", "Cadaverine", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		Putrescine = FluidUtils.generateFluidNoPrefix("putrescine", "Putrescine", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		
		//Create 2-Ethylanthraquinone
		//2-Ethylanthraquinone is prepared from the reaction of phthalic anhydride and ethylbenzene
		Ethylanthraquinone2 = FluidUtils.generateFluidNonMolten("2Ethylanthraquinone", "2-Ethylanthraquinone", 415, new short[]{227, 255, 159, 100}, null, null);
		//Create 2-Ethylanthrahydroquinone
		//Palladium plate + Hydrogen(250) + 2-Ethylanthraquinone(500) = 600 Ethylanthrahydroquinone
		Ethylanthrahydroquinone2 = FluidUtils.generateFluidNonMolten("2Ethylanthrahydroquinone", "2-Ethylanthrahydroquinone", 415, new short[]{207, 225, 129, 100}, null, null);
		//Create Hydrogen Peroxide
		//Compressed Air(1500) + Ethylanthrahydroquinone(500) + Anthracene(5) = 450 Ethylanthraquinone && 200 Peroxide
		Hydrogen_Peroxide = FluidUtils.generateFluidNonMolten("HydrogenPeroxide", "Hydrogen Peroxide", 150, new short[]{210, 255, 255, 100}, null, null);



		//Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
		//ItemUtils.generateSpecialUseDusts("LithiumHydroperoxide", "Lithium Hydroperoxide", "HLiO2", Utils.rgbtoHexValue(125, 125, 125));
		// v - Dehydrate
		//Lithium Peroxide - 2 LiOOH → Li2O2 + H2O2 + 2 H2O
		Lithium_Peroxide = FluidUtils.generateFluidNonMolten("LithiumPeroxide", "Lithium Peroxide", 446, new short[]{135, 135, 135, 100}, null, null);

		
		
	}	

	@Override
	public String errorMessage() {
		return "Failed to generate recipes for GenericChem.";
	}

	@Override
	public boolean generateRecipes() {
		
		recipeCatalystRed();
		recipeCatalystYellow();
		recipeCatalystBlue();
		recipeCatalystOrange();
		recipeCatalystPurple();
		recipeCatalystBrown();
		
		recipeNitroBenzene();
		recipeAniline();
		recipeCadaverineAndPutrescine();
		recipeCyclohexane();
		recipeCyclohexanone();
		
		recipe2Ethylanthraquinone();
		recipe2Ethylanthrahydroquinone();
		recipeHydrogenPeroxide();
		recipeLithiumHydroperoxide();
		recipeLithiumPeroxide();		
		
		return true;
	}

	private void recipeCyclohexane() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierTwoChip(),		
						ItemUtils.getSimpleStack(mBrownCatalyst, 1)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Benzene, 2000),
						FluidUtils.getFluidStack("hydrogen", 10000)
				}, 
				new ItemStack[] {
						ItemUtils.getSimpleStack(mCatalystCarrier, 1)
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cyclohexane, 1000),
				}, 
				20 * 120, 
				120, 
				2);
		
	}

	private void recipeCyclohexanone() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierTwoChip(),		
						ItemUtils.getSimpleStack(mBlueCatalyst, 1)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cyclohexane, 2000),
						FluidUtils.getFluidStack("air", 10000)
				}, 
				new ItemStack[] {
						ItemUtils.getSimpleStack(mCatalystCarrier, 1)
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cyclohexanone, 2000),
				},  
				20 * 120, 
				120, 
				2);
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierTwoChip(),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Phenol, 2000),
						FluidUtils.getFluidStack("oxygen", 5000)
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cyclohexanone, 2000),
				},  
				20 * 120, 
				120, 
				2);
		
		
		
		
	}

	private void recipeCatalystRed() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierOneChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().IRON.getDust(2),
				ELEMENT.getInstance().COPPER.getDust(2),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mRedCatalyst, 10),
				20 * 20, 
				30);
		
	}

	private void recipeCatalystYellow() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierThreeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().TUNGSTEN.getDust(4),
				ELEMENT.getInstance().NICKEL.getDust(4),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mYellowCatalyst, 10),
				60 * 20, 
				2000);
		
	}

	private void recipeCatalystBlue() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierTwoChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().COBALT.getDust(3),
				ELEMENT.getInstance().TITANIUM.getDust(3),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mBlueCatalyst, 10),
				40 * 20, 
				500);
		
	}

	private void recipeCatalystOrange() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierTwoChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().VANADIUM.getDust(5),
				ELEMENT.getInstance().PALLADIUM.getDust(5),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mOrangeCatalyst, 10),
				40 * 20, 
				500);
		
	}

	private void recipeCatalystPurple() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierFourChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().IRIDIUM.getDust(6),
				ELEMENT.getInstance().RUTHENIUM.getDust(6),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mPurpleCatalyst, 10),
				120 * 20, 
				8000);
		
	}

	private void recipeCatalystBrown() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getTierOneChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mCatalystCarrier, 10),
				ELEMENT.getInstance().NICKEL.getDust(4),
				ELEMENT.getInstance().ALUMINIUM.getDust(4),
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(mBrownCatalyst, 10),
				15 * 20, 
				30);
		
	}

	private void recipeCadaverineAndPutrescine() {
		
		// Basic Recipe
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierOneChip(),		
						ItemUtils.getSimpleStack(Items.rotten_flesh, 64)
				}, 
				new FluidStack[] {
						FluidUtils.getHotWater(2000)
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cadaverine, 250),
						FluidUtils.getFluidStack(Putrescine, 250),
				}, 
				20 * 120, 
				120, 
				1);

		// Advanced Recipe
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierTwoChip(),		
						ItemUtils.getSimpleStack(Items.rotten_flesh, 128),		
						ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 8, 32)
				}, 
				new FluidStack[] {
						FluidUtils.getHotWater(3000)
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Cadaverine, 750),
						FluidUtils.getFluidStack(Putrescine, 750),
				}, 
				20 * 120, 
				240, 
				2);
		
	}

	private void recipeAniline() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierThreeChip(),		
						ItemUtils.getSimpleStack(mBlueCatalyst, 1)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(NitroBenzene, 2000),
						FluidUtils.getFluidStack("hydrogen", 10000)
				}, 
				new ItemStack[] {
						ItemUtils.getSimpleStack(mCatalystCarrier, 1)
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Aniline, 2000),
				}, 
				20 * 30, 
				500, 
				3);
		
	}

	private void recipeNitroBenzene() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						getTierThreeChip(),						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Benzene, 5000),
						FluidUtils.getFluidStack("sulfuricacid", 3000),
						FluidUtils.getFluidStack("nitricacid", 3000),
						FluidUtils.getDistilledWater(10000)
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("dilutedsulfuricacid", 3000),
						FluidUtils.getFluidStack(NitroBenzene, 5000),
				}, 
				20 * 30, 
				500, 
				3);
		
	}

	private void recipe2Ethylanthraquinone() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(4),		
						ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 4),				
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(CoalTar.Ethylbenzene, 2000),
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Ethylanthraquinone2, 2000+(144*4)),
				}, 
				20 * 15, 
				120, 
				1);		
		
		/*GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2),
				null,
				FluidUtils.getFluidStack("fluid.2ethylanthraquinone", 2000+(144*4)),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				20*16);*/
	
	}

	private void recipe2Ethylanthrahydroquinone() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(4),		
						ItemUtils.getSimpleStack(mOrangeCatalyst, 1),			
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Ethylanthraquinone2, 4000),
						FluidUtils.getFluidStack("hydrogen", 2000),
				}, 
				new ItemStack[] {
						
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Ethylanthrahydroquinone2, 5000),
				}, 
				20 * 40, 
				120, 
				1);	
		
		/*GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("platePalladium", 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cell2Ethylanthraquinone", 1),
				FluidUtils.getFluidStack("hydrogen", 500),
				FluidUtils.getFluidStack("fluid.2ethylanthrahydroquinone", 1200),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1),
				20*40);*/
	
	}

	private void recipeLithiumPeroxide() {
		CORE.RA.addDehydratorRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 2),
						ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 3)
				}, 
				null, 
				null, 
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumPeroxide", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2)
				},
				new int[]{10000, 10000, 10000}, 
				20*100, 
				120);
	}

	private void recipeLithiumHydroperoxide() {
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(4),		
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 7),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("fluid.hydrogenperoxide", 2000),
				}, 
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 14),						
				}, 
				new FluidStack[] {
						
				}, 
				20 * 30, 
				240, 
				1);
		
		
		
		/*CORE.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 7),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 1),
				20,
				FluidUtils.getFluidStack("fluid.cellhydrogenperoxide", 50),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 14),
				CI.emptyCells(1),
				20*30,
				240);	*/
	}

	private void recipeHydrogenPeroxide() {	
		
		CORE.RA.addFluidReactorRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(4),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("air", 15000),
						FluidUtils.getFluidStack(Ethylanthrahydroquinone2, 5000),
						FluidUtils.getFluidStack("fluid.anthracene", 50),
				}, 
				new ItemStack[] {					
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Ethylanthraquinone2, 4000),
						FluidUtils.getFluidStack("fluid.hydrogenperoxide", 2000),						
				}, 
				20 * 30, 
				240, 
				1);
		
		/*		CORE.RA.addChemicalRecipe(
						GT_ModHandler.getAirCell(15),
						ItemUtils.getItemStackOfAmountFromOreDict("cell2Ethylanthrahydroquinone", 5),
						20,
						FluidUtils.getFluidStack("fluid.anthracene", 50),
						FluidUtils.getFluidStack("fluid.2ethylanthrahydroquinone", 4450),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 2),
						CI.emptyCells(18),
						20*30,
						240);*/
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static final ItemStack getTierOneChip() {
		return CI.getNumberedBioCircuit(4);
	}
	private static final ItemStack getTierTwoChip() {
		return CI.getNumberedBioCircuit(8);
	}
	private static final ItemStack getTierThreeChip() {
		return CI.getNumberedBioCircuit(12);
	}
	private static final ItemStack getTierFourChip() {
		return CI.getNumberedBioCircuit(16);
	}
	
	
	
}
