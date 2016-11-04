package gtPlusPlus.core.common.compat;

import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.ShapedRecipeObject;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class COMPAT_CompactWindmills {

	// Change IC2 Upgrades
	public static ItemStack				kineticWind		= ItemUtils.simpleMetaStack("IC2:blockKineticGenerator", 0, 1);
	public static ItemStack				shaftIron		= ItemUtils.simpleMetaStack("IC2:itemRecipePart", 11, 1);
	public static ItemStack				cableCopper		= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 1367,
			1);
	public static String				plateRubber		= "ore:plateRubber";

	// Machine Casings
	public static ItemStack				elvCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 0, 1);
	public static ItemStack				lvCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 1, 1);
	public static ItemStack				mvCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 2, 1);
	public static ItemStack				hvCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 3, 1);
	public static ItemStack				evCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 4, 1);
	public static ItemStack				ivCasing		= ItemUtils.simpleMetaStack("gregtech:gt.blockcasings", 5, 1);

	// GT Transformers
	public static ItemStack				elvTransformer	= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 20, 1);
	public static ItemStack				lvTransformer	= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 21, 1);
	public static ItemStack				mvTransformer	= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 22, 1);
	public static ItemStack				hvTransformer	= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 23, 1);
	public static ItemStack				evTransformer	= ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 24, 1);

	// Compact Windmills
	public static ItemStack				elvWindmill		= ItemUtils
			.simpleMetaStack("CompactWindmills:blockCompactWindmill", 0, 1);
	public static ItemStack				lvWindmill		= ItemUtils
			.simpleMetaStack("CompactWindmills:blockCompactWindmill", 1, 1);
	public static ItemStack				mvWindmill		= ItemUtils
			.simpleMetaStack("CompactWindmills:blockCompactWindmill", 2, 1);
	public static ItemStack				hvWindmill		= ItemUtils
			.simpleMetaStack("CompactWindmills:blockCompactWindmill", 3, 1);
	public static ItemStack				evWindmill		= ItemUtils
			.simpleMetaStack("CompactWindmills:blockCompactWindmill", 4, 1);

	// Compact Rotors
	public static ItemStack				rotor2			= ItemUtils.getItemStack("CompactWindmills:WOOL", 1);
	public static ItemStack				rotor1			= ItemUtils.getItemStack("CompactWindmills:WOOD", 1);
	public static ItemStack				rotor3			= ItemUtils.getItemStack("CompactWindmills:ALLOY", 1);
	public static ItemStack				rotor4			= ItemUtils.getItemStack("CompactWindmills:CARBON", 1);
	public static ItemStack				rotor5			= ItemUtils.getItemStack("CompactWindmills:IRIDIUM", 1);

	// IC2 Rotors
	public static ItemStack				rotorIC1		= ItemUtils.getItemStack("IC2:itemwoodrotor", 1);
	public static ItemStack				rotorIC2		= ItemUtils.getItemStack("IC2:itemironrotor", 1);
	public static ItemStack				rotorIC3		= ItemUtils.getItemStack("IC2:itemsteelrotor", 1);
	public static ItemStack				rotorIC4		= ItemUtils.getItemStack("IC2:itemwcarbonrotor", 1);
	public static ItemStack				rotorBlade1		= ItemUtils.simpleMetaStack("IC2:itemRecipePart", 7, 1);
	public static ItemStack				rotorBlade2		= ItemUtils.simpleMetaStack("IC2:itemRecipePart", 8, 1);
	public static ItemStack				rotorBlade3		= ItemUtils.simpleMetaStack("IC2:itemRecipePart", 10, 1);
	public static ItemStack				rotorBlade4		= ItemUtils.simpleMetaStack("IC2:itemRecipePart", 9, 1);

	// Plates
	public static String				plateTier1		= "ore:plateMagnalium";
	public static String				plateTier2		= "ore:plateStainlessSteel";
	public static String				plateTier3		= "ore:plateTitanium";
	public static String				plateTier4		= "ore:plateTungstenSteel";
	public static String				plateTier5		= "ore:plateNichrome";
	public static String				plateCarbon		= "ore:plateAlloyCarbon";
	public static String				plateAlloy		= "ore:plateAlloyAdvanced";

	// Circuits
	public static String				circuitTier1	= "ore:circuitGood";
	public static String				circuitTier2	= "ore:circuitAdvanced";
	public static String				circuitTier3	= "ore:circuitData";
	public static String				circuitTier4	= "ore:circuitElite";
	public static String				circuitTier5	= "ore:circuitMaster";

	// Wooden Rotor
	public static ShapedRecipeObject	Wooden_Rotor	= new ShapedRecipeObject(COMPAT_CompactWindmills.rotorBlade1,
			"ore:plateAnyIron", "ore:plateAnyIron", "ore:screwAluminium", COMPAT_CompactWindmills.rotorIC1,
			"ore:screwAluminium", COMPAT_CompactWindmills.rotorBlade1, "plateAnyIron",
			COMPAT_CompactWindmills.rotorBlade1, COMPAT_CompactWindmills.rotor1);
	// Alloy Rotor
	public static ShapedRecipeObject	Alloy_Rotor		= new ShapedRecipeObject(COMPAT_CompactWindmills.plateAlloy,
			COMPAT_CompactWindmills.plateAlloy, COMPAT_CompactWindmills.plateAlloy, COMPAT_CompactWindmills.plateAlloy,
			COMPAT_CompactWindmills.rotorIC3, COMPAT_CompactWindmills.plateAlloy, COMPAT_CompactWindmills.plateAlloy,
			COMPAT_CompactWindmills.plateAlloy, COMPAT_CompactWindmills.plateAlloy, COMPAT_CompactWindmills.rotor3);
	// Carbon Rotor
	public static ShapedRecipeObject	Carbon_Rotor	= new ShapedRecipeObject(COMPAT_CompactWindmills.plateCarbon,
			COMPAT_CompactWindmills.rotorBlade4, COMPAT_CompactWindmills.plateCarbon,
			COMPAT_CompactWindmills.rotorBlade4, COMPAT_CompactWindmills.rotor3, COMPAT_CompactWindmills.rotorBlade4,
			COMPAT_CompactWindmills.plateCarbon, COMPAT_CompactWindmills.rotorBlade4,
			COMPAT_CompactWindmills.plateCarbon, COMPAT_CompactWindmills.rotor4);

	// Kinetic Wind Turbine
	public static ShapedRecipeObject	KWT				= new ShapedRecipeObject(COMPAT_CompactWindmills.plateCarbon,
			COMPAT_CompactWindmills.shaftIron, COMPAT_CompactWindmills.plateCarbon, COMPAT_CompactWindmills.cableCopper,
			COMPAT_CompactWindmills.mvCasing, COMPAT_CompactWindmills.cableCopper, COMPAT_CompactWindmills.plateRubber,
			COMPAT_CompactWindmills.plateCarbon, COMPAT_CompactWindmills.plateRubber,
			COMPAT_CompactWindmills.kineticWind);

	// ELV Windmill
	public static ShapedRecipeObject	WM_ELV			= new ShapedRecipeObject(

			COMPAT_CompactWindmills.circuitTier1, COMPAT_CompactWindmills.elvTransformer,
			COMPAT_CompactWindmills.circuitTier1, COMPAT_CompactWindmills.plateTier1, COMPAT_CompactWindmills.lvCasing,
			COMPAT_CompactWindmills.plateTier1, COMPAT_CompactWindmills.plateTier1, COMPAT_CompactWindmills.rotor1,
			COMPAT_CompactWindmills.plateTier1, COMPAT_CompactWindmills.elvWindmill);

	// LV Windmill
	public static ShapedRecipeObject	WM_LV			= new ShapedRecipeObject(COMPAT_CompactWindmills.circuitTier2,
			COMPAT_CompactWindmills.lvTransformer, COMPAT_CompactWindmills.circuitTier2,
			COMPAT_CompactWindmills.plateTier2, COMPAT_CompactWindmills.mvCasing, COMPAT_CompactWindmills.plateTier2,
			COMPAT_CompactWindmills.plateTier2, COMPAT_CompactWindmills.rotor2, COMPAT_CompactWindmills.plateTier2,
			COMPAT_CompactWindmills.lvWindmill);

	// MV Windmill
	public static ShapedRecipeObject	WM_MV			= new ShapedRecipeObject(COMPAT_CompactWindmills.circuitTier3,
			COMPAT_CompactWindmills.mvTransformer, COMPAT_CompactWindmills.circuitTier3,
			COMPAT_CompactWindmills.plateTier3, COMPAT_CompactWindmills.hvCasing, COMPAT_CompactWindmills.plateTier3,
			COMPAT_CompactWindmills.plateTier3, COMPAT_CompactWindmills.rotor3, COMPAT_CompactWindmills.plateTier3,
			COMPAT_CompactWindmills.mvWindmill);

	// HV Windmill
	public static ShapedRecipeObject	WM_HV			= new ShapedRecipeObject(COMPAT_CompactWindmills.circuitTier4,
			COMPAT_CompactWindmills.hvTransformer, COMPAT_CompactWindmills.circuitTier4,
			COMPAT_CompactWindmills.plateTier4, COMPAT_CompactWindmills.evCasing, COMPAT_CompactWindmills.plateTier4,
			COMPAT_CompactWindmills.plateTier4, COMPAT_CompactWindmills.rotor4, COMPAT_CompactWindmills.plateTier4,
			COMPAT_CompactWindmills.hvWindmill);

	// EV Windmill
	public static ShapedRecipeObject	WM_EV			= new ShapedRecipeObject(COMPAT_CompactWindmills.circuitTier5,
			COMPAT_CompactWindmills.evTransformer, COMPAT_CompactWindmills.circuitTier5,
			COMPAT_CompactWindmills.plateTier5, COMPAT_CompactWindmills.ivCasing, COMPAT_CompactWindmills.plateTier5,
			COMPAT_CompactWindmills.plateTier5, COMPAT_CompactWindmills.rotor5, COMPAT_CompactWindmills.plateTier5,
			COMPAT_CompactWindmills.evWindmill);

	public static void OreDict() {
		COMPAT_CompactWindmills.run();
	}

	private static final void run() {
		// RemoveRecipeQueue.add("CompactWindmills:WOOL");
		// RemoveRecipeQueue.add("CompactWindmills:WOOD");
		// RemoveRecipeQueue.add("CompactWindmills:ALLOY");
		// RemoveRecipeQueue.add("CompactWindmills:CARBON");
		// RemoveRecipeQueue.add("CompactWindmills:IRIDIUM");

		// Remove Recipes
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.kineticWind);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.elvWindmill);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.lvWindmill);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.mvWindmill);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.hvWindmill);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.evWindmill);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.rotor1);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.rotor3);
		COMPAT_HANDLER.RemoveRecipeQueue.add(COMPAT_CompactWindmills.rotor4);

		// Add Recipes
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.Wooden_Rotor);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.Alloy_Rotor);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.Carbon_Rotor);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.KWT);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.WM_ELV);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.WM_LV);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.WM_MV);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.WM_HV);
		COMPAT_HANDLER.AddRecipeQueue.add(COMPAT_CompactWindmills.WM_EV);

	}
}
