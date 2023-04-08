package gtPlusPlus.xmod.ic2.recipe;

import static gregtech.api.enums.Mods.EnderIO;
import static gtPlusPlus.core.recipe.RECIPES_Tools.craftingToolHardHammer;
import static gtPlusPlus.core.recipe.RECIPES_Tools.craftingToolWrench;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.ic2.item.IC2_Items;
import ic2.core.Ic2Items;

public class RECIPE_IC2 {

    public static String plate_T1 = "plateEnergeticAlloy";
    public static String plate_T2 = "plateTungstenSteel";
    public static String plate_T3 = "plateVibrantAlloy";
    public static String plate_T4 = "plateAlloyIridium";

    public static ItemStack block_T1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L);
    public static ItemStack block_T2 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L);
    public static ItemStack block_T3 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.VibrantAlloy, 1L);
    public static ItemStack block_T4 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L);

    public static ItemStack shaft_block_T1 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnergeticAlloy, 1L);
    public static ItemStack shaft_block_T2 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.TungstenSteel, 1L);
    public static ItemStack shaft_block_T3 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.VibrantAlloy, 1L);
    public static ItemStack shaft_block_T4 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iridium, 1L);

    public static String ingot_T1 = "ingotEnergeticAlloy";
    public static String ingot_T2 = "ingotTungstenSteel";
    public static String ingot_T3 = "ingotVibrantAlloy";
    public static String ingot_T4 = "ingotIridium";

    public static String ring_T1 = "ringStainlessSteel";
    public static String ring_T2 = "ringTungstenSteel";
    public static String ring_T3 = "ringChrome";
    public static String ring_T4 = "ringOsmiridium";

    private static ItemStack rotor_blade_T1 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_1.getItem());
    private static ItemStack rotor_blade_T2 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_2.getItem());
    private static ItemStack rotor_blade_T3 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_3.getItem());
    private static ItemStack rotor_blade_T4 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_4.getItem());

    private static ItemStack shaft_T1 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_1.getItem());
    private static ItemStack shaft_T2 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_2.getItem());
    private static ItemStack shaft_T3 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_3.getItem());
    private static ItemStack shaft_T4 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_4.getItem());

    private static ItemStack rotor_T1 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_1.getItem());
    private static ItemStack rotor_T2 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_2.getItem());
    private static ItemStack rotor_T3 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_3.getItem());
    private static ItemStack rotor_T4 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_4.getItem());

    private static boolean checkForEnderIO() {
        if (!EnderIO.isModLoaded()) {
            plate_T1 = "plateMagnalium";
            plate_T2 = "plateTungstenSteel";
            plate_T3 = "plateUltimet";
            plate_T4 = "plateAlloyIridium";

            block_T1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Magnalium, 1L);
            block_T2 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L);
            block_T3 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Ultimet, 1L);
            block_T4 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L);

            ingot_T1 = "ingotMagnalium";
            ingot_T2 = "ingotTungstenSteel";
            ingot_T3 = "ingotUltimet";
            ingot_T4 = "ingotIridium";
            return true;
        }
        return false;
    }

    public static void initRecipes() {

        checkForEnderIO();
        addAdvancedHazmat();

        RecipeUtils.addShapedRecipe(
                plate_T1,
                plate_T1,
                plate_T1,
                plate_T1,
                ring_T1,
                plate_T1,
                plate_T1,
                plate_T1,
                plate_T1,
                rotor_blade_T1);

        RecipeUtils.addShapedRecipe(
                plate_T2,
                plate_T2,
                plate_T2,
                plate_T2,
                ring_T2,
                plate_T2,
                plate_T2,
                plate_T2,
                plate_T2,
                rotor_blade_T2);

        RecipeUtils.addShapedRecipe(
                plate_T3,
                plate_T3,
                plate_T3,
                plate_T3,
                ring_T3,
                plate_T3,
                plate_T3,
                plate_T3,
                plate_T3,
                rotor_blade_T3);

        RecipeUtils.addShapedRecipe(
                plate_T4,
                plate_T4,
                plate_T4,
                plate_T4,
                ring_T4,
                plate_T4,
                plate_T4,
                plate_T4,
                plate_T4,
                rotor_blade_T4);

        // Shaft Extruder Recipe
        GT_ModHandler.addCraftingRecipe(
                GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                        | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod,
                        Character.valueOf('X'), OrePrefixes.plate.get(Materials.DarkSteel), Character.valueOf('S'),
                        OrePrefixes.screw.get(Materials.DarkSteel) });
        GT_ModHandler.addCraftingRecipe(
                GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                        | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod,
                        Character.valueOf('X'), OrePrefixes.plate.get(Materials.TungstenSteel), Character.valueOf('S'),
                        OrePrefixes.screw.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                        | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod,
                        Character.valueOf('X'), OrePrefixes.plate.get(Materials.Molybdenum), Character.valueOf('S'),
                        OrePrefixes.screw.get(Materials.Molybdenum) });
        Logger.INFO("Added recipe item for GT5 Extruder: Shaft Shape");

        // Custm Recipes for Iron and Steel
        ItemStack mShaftIron = ItemList.IC2_ShaftIron.get(1);
        ItemStack mShaftSteel = ItemList.IC2_ShaftSteel.get(1);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftIron,
                32 * 20,
                120);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftIron,
                32 * 20,
                120);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftIron,
                32 * 20,
                120);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftIron,
                32 * 20,
                120);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftSteel,
                64 * 20,
                120);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Steel, 1L)),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                mShaftSteel,
                64 * 20,
                120);

        // Shaft Recipes
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, block_T1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T1,
                2560,
                250);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, shaft_block_T1),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T1,
                2560,
                250);
        Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Energetic]");
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, block_T2),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T2,
                5120,
                500);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, shaft_block_T2),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T2,
                5120,
                500);

        Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [TungstenSteel]");
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, block_T3),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T3,
                10240,
                2000);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, shaft_block_T3),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T3,
                10240,
                2000);
        Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Vibrant]");
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(9L, block_T4),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T4,
                20480,
                4000);
        GT_Values.RA.addExtruderRecipe(
                GT_Utility.copyAmount(1L, shaft_block_T4),
                GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]),
                shaft_T4,
                20480,
                4000);
        Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Iridium]");

        RecipeUtils.addShapedRecipe(
                shaft_T1,
                rotor_blade_T1,
                craftingToolHardHammer,
                rotor_blade_T1,
                ring_T1,
                rotor_blade_T1,
                craftingToolWrench,
                rotor_blade_T1,
                shaft_T1,
                rotor_T1);

        RecipeUtils.addShapedRecipe(
                shaft_T2,
                rotor_blade_T2,
                craftingToolHardHammer,
                rotor_blade_T2,
                ring_T2,
                rotor_blade_T2,
                craftingToolWrench,
                rotor_blade_T2,
                shaft_T2,
                rotor_T2);

        RecipeUtils.addShapedRecipe(
                shaft_T3,
                rotor_blade_T3,
                craftingToolHardHammer,
                rotor_blade_T3,
                ring_T3,
                rotor_blade_T3,
                craftingToolWrench,
                rotor_blade_T3,
                shaft_T3,
                rotor_T3);

        RecipeUtils.addShapedRecipe(
                shaft_T4,
                rotor_blade_T4,
                craftingToolHardHammer,
                rotor_blade_T4,
                ring_T4,
                rotor_blade_T4,
                craftingToolWrench,
                rotor_blade_T4,
                shaft_T4,
                rotor_T4);
    }

    private static void addAdvancedHazmat() {

        ItemStack[] aBasicHazmatPieces = new ItemStack[] { Ic2Items.hazmatHelmet.copy(),
                Ic2Items.hazmatChestplate.copy(), Ic2Items.hazmatLeggings.copy(), Ic2Items.hazmatBoots.copy() };

        Material aRubber = MaterialUtils.generateMaterialFromGtENUM(Materials.Rubber);
        ItemStack aYellowWool = ItemUtils.getSimpleStack(Blocks.wool, 4, 1);
        ItemStack aBlackWool = ItemUtils.getSimpleStack(Blocks.wool, 15, 1);
        ItemStack aCoilIC2 = Ic2Items.coil;
        ItemStack aPlateCobalt = CI.getTieredComponentOfMaterial(Materials.Cobalt, OrePrefixes.plate, 1);
        ItemStack aGearSmallSteel = CI.getTieredComponentOfMaterial(Materials.Steel, OrePrefixes.gearGtSmall, 1);
        ItemStack aGearSmallAluminium = CI
                .getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.gearGtSmall, 1);
        ItemStack aGearPotin = ALLOY.TUMBAGA.getGear(1);
        ItemStack aGearSiliconCarbide = ALLOY.SILICON_CARBIDE.getGear(1);

        CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(2), aBasicHazmatPieces[0],
                        ItemUtils.getSimpleStack(aYellowWool, 16), ItemUtils.getSimpleStack(aPlateCobalt, 4),
                        ItemUtils.getSimpleStack(aCoilIC2, 8), ItemUtils.getSimpleStack(aGearSmallAluminium, 4), },
                aRubber.getFluidStack(144 * 4),
                GregtechItemList.Armour_Hazmat_Advanced_Helmet.get(1),
                30 * 20,
                MaterialUtils.getVoltageForTier(2));

        CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(2), aBasicHazmatPieces[1],
                        ItemUtils.getSimpleStack(aYellowWool, 64), ItemUtils.getSimpleStack(aCoilIC2, 32),
                        ItemUtils.getSimpleStack(aPlateCobalt, 16), ItemUtils.getSimpleStack(aGearSiliconCarbide, 8), },
                aRubber.getFluidStack(144 * 10),
                GregtechItemList.Armour_Hazmat_Advanced_Chest.get(1),
                90 * 20,
                MaterialUtils.getVoltageForTier(2));

        CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(2), aBasicHazmatPieces[2],
                        ItemUtils.getSimpleStack(aYellowWool, 32), ItemUtils.getSimpleStack(aCoilIC2, 16),
                        ItemUtils.getSimpleStack(aPlateCobalt, 8), ItemUtils.getSimpleStack(aGearSiliconCarbide, 4), },
                aRubber.getFluidStack(144 * 8),
                GregtechItemList.Armour_Hazmat_Advanced_Legs.get(1),
                75 * 20,
                MaterialUtils.getVoltageForTier(2));

        CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(2), aBasicHazmatPieces[3],
                        ItemUtils.getSimpleStack(aBlackWool, 16), ItemUtils.getSimpleStack(aCoilIC2, 6),
                        ItemUtils.getSimpleStack(aGearSmallSteel, 8), ItemUtils.getSimpleStack(aGearPotin, 4), },
                aRubber.getFluidStack(144 * 6),
                GregtechItemList.Armour_Hazmat_Advanced_Boots.get(1),
                45 * 20,
                MaterialUtils.getVoltageForTier(2));
    }
}
