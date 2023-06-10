package gregtech.loaders.load;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NotEnoughItems;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.postload.GT_PCBFactoryMaterialLoader;
import gregtech.loaders.postload.GT_ProcessingArrayRecipeLoader;
import ic2.core.Ic2Items;

public class GT_Loader_MetaTileEntities_Recipes implements Runnable {

    private static final String aTextPlate = "PPP";
    private static final String aTextPlateWrench = "PwP";
    private static final String aTextPlateMotor = "PMP";
    private static final String aTextCableHull = "CMC";
    private static final String aTextWireHull = "WMW";
    private static final String aTextWireChest = "WTW";
    private static final String aTextWireCoil = "WCW";
    private static final String aTextMotorWire = "EWE";
    private static final String aTextWirePump = "WPW";

    private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
        | GT_ModHandler.RecipeBits.BUFFERED;
    private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | bits;

    private static void run1() {
        Materials LuVMat2 = BartWorks.isModLoaded() ? Materials.get("Rhodium-PlatedPalladium") : Materials.Chrome;

        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polytetrafluoroethylene.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene), 'F',
                OrePrefixes.frameGt.get(Materials.Polytetrafluoroethylene), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polytetrafluoroethylene) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polybenzimidazole.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole), 'F',
                OrePrefixes.frameGt.get(Materials.Polybenzimidazole), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ULV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_LV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_HV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_EV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_IV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_LuV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(LuVMat2) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ZPM.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Iridium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_UV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Osmium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MAX.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_BronzePlatedBricks.get(1L),
            bits,
            new Object[] { "PhP", "PBP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_StableTitanium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_HeatProof.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Invar), 'F',
                OrePrefixes.frameGt.get(Materials.Invar) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FrostProof.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_CleanStainlessSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                OrePrefixes.frameGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_RobustTungstenSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningOsmiridium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Osmiridium), 'F',
                OrePrefixes.frameGt.get(Materials.Osmiridium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningNeutronium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Neutronium), 'F',
                OrePrefixes.frameGt.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningBlackPlutonium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.BlackPlutonium), 'F',
                OrePrefixes.frameGt.get(Materials.BlackPlutonium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Magnalium), 'F',
                OrePrefixes.frameGt.get(Materials.BlueSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine1.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine2.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine3.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_TurbineGasAdvanced.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.HSSS), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Bronze.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'I', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Steel.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'I', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Titanium.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'I', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_TungstenSteel.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'I',
                OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Bronze.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'G', OrePrefixes.gearGt.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Steel.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Titanium.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_TungstenSteel.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', ItemList.Robot_Arm_IV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Grate.get(1L),
            bits,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', new ItemStack(Blocks.iron_bars, 1), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V',
                OrePrefixes.rotor.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Assembler.get(1L),
            bits,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', OrePrefixes.circuit.get(Materials.Ultimate), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'V',
                OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Bronze.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'S', OrePrefixes.stick.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Steel.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'S', OrePrefixes.stick.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Titanium.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'S', OrePrefixes.stick.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_TungstenSteel.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_A.get(1L),
            bits,
            new Object[] { "Y  ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_B.get(1L),
            bits,
            new Object[] { "  Y", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_RadioactiveHazard.get(1L),
            bits,
            new Object[] { " YB", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_BioHazard.get(1L),
            bits,
            new Object[] { " Y ", " MB", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ExplosionHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FireHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", " B ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_AcidHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MagicHazard.get(1L),
            bits,
            new Object[] { " Y ", "BM ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FrostHazard.get(1L),
            bits,
            new Object[] { "BY ", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_NoiseHazard.get(1L),
            bits,
            new Object[] { "   ", " M ", "BY ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Advanced_Iridium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Iridium), 'F',
                OrePrefixes.frameGt.get(Materials.Iridium) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_Stripes_A });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_Stripes_B });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_RadioactiveHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_BioHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_ExplosionHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_FireHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_AcidHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_MagicHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_FrostHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_NoiseHazard });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bricked_BlastFurnace.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "BFB", "FwF", "BFB", 'B', ItemList.Casing_Firebricks, 'F',
                OreDictNames.craftingIronFurnace });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze_Bricks.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HP.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HP_Bricks.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'B',
                new ItemStack(Blocks.brick_block, 1) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_ULV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead),
                'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_LV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'H',
                OrePrefixes.plate.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_MV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_MV, 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper),
                'H', OrePrefixes.plate.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_HV, 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'H',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_EV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium),
                'H', OrePrefixes.plate.get(Materials.Titanium), 'P', OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_IV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_IV, 'C', OrePrefixes.cableGt01.get(Materials.Tungsten),
                'H', OrePrefixes.plate.get(Materials.TungstenSteel), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_LuV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H', OrePrefixes.plate.get(LuVMat2), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_ZPM.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_ZPM, 'C', OrePrefixes.cableGt02.get(Materials.Naquadah),
                'H', OrePrefixes.plate.get(Materials.Iridium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_UV.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_MAX.get(1L),
            GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H', OrePrefixes.plate.get(Materials.Neutronium),
                'P', OrePrefixes.plate.get(Materials.Polybenzimidazole) });

        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ULV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_HV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_EV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_IV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LuV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ZPM.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_UV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MAX.get(1L));

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ULV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P',
                    OrePrefixes.plate.get(Materials.Wood) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P',
                    OrePrefixes.plate.get(Materials.WroughtIron) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P',
                    OrePrefixes.plate.get(Materials.WroughtIron) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_HV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Gold), 'H', OrePrefixes.plate.get(Materials.StainlessSteel),
                    'P', OrePrefixes.plate.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_EV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium), 'P',
                    OrePrefixes.plate.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_IV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Tungsten), 'H', OrePrefixes.plate.get(Materials.TungstenSteel),
                    'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LuV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                    OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H', OrePrefixes.plate.get(LuVMat2), 'P',
                    OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ZPM.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                    OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium), 'P',
                    OrePrefixes.plate.get(Materials.Polybenzimidazole) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_UV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                    OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium),
                    'P', OrePrefixes.plate.get(Materials.Polybenzimidazole) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MAX.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                    OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H',
                    OrePrefixes.plate.get(Materials.Neutronium), 'P',
                    OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        } else {
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ULV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Lead) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Tin) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Copper) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_HV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Gold) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_EV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Aluminium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_IV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                    OrePrefixes.cableGt01.get(Materials.Tungsten) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LuV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                    OrePrefixes.cableGt01.get(Materials.VanadiumGallium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ZPM.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                    OrePrefixes.cableGt01.get(Materials.Naquadah) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_UV.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                    OrePrefixes.wireGt04.get(Materials.NaquadahAlloy) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MAX.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                    OrePrefixes.wireGt04.get(Materials.SuperconductorUV) });
        }
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_LV_ULV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'B', OrePrefixes.cableGt01.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_MV_LV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_HV_MV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Gold),
                'B', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'K',
                OrePrefixes.componentCircuit.get(Materials.Inductor) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_EV_HV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OrePrefixes.cableGt01.get(Materials.Gold), 'K',
                ItemList.Circuit_Chip_ULPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_IV_EV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OrePrefixes.cableGt01.get(Materials.Aluminium), 'K',
                ItemList.Circuit_Chip_LPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_LuV_IV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'K', ItemList.Circuit_Chip_PIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_ZPM_LuV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'K', ItemList.Circuit_Chip_HPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_UV_ZPM.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_ZPM, 'C',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OrePrefixes.cableGt01.get(Materials.Naquadah),
                'K', ItemList.Circuit_Chip_UHPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_MAX_UV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_UV, 'C',
                OrePrefixes.wireGt01.get(Materials.Bedrockium), 'B', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy),
                'K', ItemList.Circuit_Chip_NPIC });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_ULV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_ULV, 'S', OrePrefixes.spring.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.Primitive), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_LV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_LV, 'S', OrePrefixes.spring.get(Materials.Tin), 'X',
                OrePrefixes.circuit.get(Materials.Basic), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_MV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_MV, 'S', OrePrefixes.spring.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_ULV.get(1L),
            bitsd,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.Primitive), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_LV.get(1L),
            bitsd,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'X', OrePrefixes.circuit.get(Materials.Basic), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_MV.get(1L),
            bitsd,
            new Object[] { "XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Maintenance.get(1L),
            bitsd,
            new Object[] { "dwx", "hMc", "fsr", 'M', ItemList.Hull_LV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_EV.get(1L),
            bitsd,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_EV, 'O', ItemList.Tool_DataStick, 'C',
                OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_LuV.get(1L),
            bitsd,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_LuV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_UV.get(1L),
            bitsd,
            new Object[] { "CRC", "OMO", "CRC", 'M', ItemList.Hull_UV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.Infinite), 'R', ItemList.Robot_Arm_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_AutoMaintenance.get(1L),
            bitsd,
            new Object[] { "CHC", "AMA", "CHC", 'M', ItemList.Hull_LuV, 'H', ItemList.Hatch_Maintenance, 'A',
                ItemList.Robot_Arm_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_LV.get(1L),
            bitsd,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Bronze), 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'X', ItemList.Electric_Motor_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_MV.get(1L),
            bitsd,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_MV, 'P', OrePrefixes.pipeMedium.get(Materials.Steel), 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'X', ItemList.Electric_Motor_MV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler_Lava.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PTP", aTextPlateMotor, 'M', ItemList.Hull_HP, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'T',
                GT_ModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler_Solar.get(1L),
            bitsd,
            new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_Bronze_Bricks, 'P',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'S', OrePrefixes.plateDouble.get(Materials.Silver), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Solar.get(1L),
            bitsd,
            new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_HP_Bricks, 'P',
                OrePrefixes.pipeSmall.get(Materials.Steel), 'S', OrePrefixes.plateTriple.get(Materials.Silver), 'G',
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Furnace.get(1L),
            bitsd,
            new Object[] { "XXX", "XMX", "XFX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Furnace.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Furnace, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Macerator.get(1L),
            bitsd,
            new Object[] { "DXD", "XMX", "PXP", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'D',
                OrePrefixes.gem.get(Materials.Diamond) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Macerator.get(1L),
            bitsd,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Macerator, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Extractor.get(1L),
            bitsd,
            new Object[] { "XXX", "PMG", "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Extractor.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Extractor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Hammer.get(1L),
            bitsd,
            new Object[] { "XPX", "XMX", "XAX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'A',
                OreDictNames.craftingAnvil });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Hammer.get(1L),
            bitsd,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Hammer, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Compressor.get(1L),
            bitsd,
            new Object[] { "XXX", aTextPlateMotor, "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Compressor.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Compressor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_AlloySmelter.get(1L),
            bitsd,
            new Object[] { "XXX", "FMF", "XXX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_AlloySmelter.get(1L),
            bitsd,
            new Object[] { "PSP", "PMP", "PXP", 'M', ItemList.Machine_Bronze_AlloySmelter, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt01.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt01.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt01.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt01.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt01.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt01.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt01.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_MAX.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt04.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt04.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt04.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt04.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt04.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_MAX.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt08.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt08.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt08.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt08.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt08.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt08.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt08.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_MAX.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt08.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_MAX.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_ULV_Tantalum, 'C', OrePrefixes.circuit.get(Materials.Primitive) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_LV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_MV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_HV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.Master), 'C', OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Energy_LapotronicOrb, 'C', OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Energy_LapotronicOrb2, 'C', OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Energy_LapotronicOrb2, 'C', OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM2,
                'C', OrePrefixes.circuit.get(Materials.SuperconductorUHV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_MAX.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest, 'B',
                ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.Infinite) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_ULV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ULV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_LV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_MV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_HV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_HV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_EV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_EV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_IV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_IV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_LuV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LuV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_ZPM.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ZPM, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_UV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_MAX.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MAX, 'T', OreDictNames.craftingChest });
    }

    private static void run2() {
        ItemList.Machine_LV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                201,
                "basicmachine.alloysmelter.tier.01",
                "Basic Alloy Smelter",
                1,
                "HighTech combination Smelter",
                GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                2,
                1,
                false,
                0,
                1,
                "AlloySmelter.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ALLOY_SMELTER",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                202,
                "basicmachine.alloysmelter.tier.02",
                "Advanced Alloy Smelter",
                2,
                "HighTech combination Smelter",
                GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                2,
                1,
                false,
                0,
                1,
                "AlloySmelter.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ALLOY_SMELTER",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                203,
                "basicmachine.alloysmelter.tier.03",
                "Advanced Alloy Smelter II",
                3,
                "HighTech combination Smelter",
                GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                2,
                1,
                false,
                0,
                1,
                "AlloySmelter.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ALLOY_SMELTER",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                204,
                "basicmachine.alloysmelter.tier.04",
                "Advanced Alloy Smelter III",
                4,
                "HighTech combination Smelter",
                GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                2,
                1,
                false,
                0,
                1,
                "AlloySmelter.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ALLOY_SMELTER",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                205,
                "basicmachine.alloysmelter.tier.05",
                "Advanced Alloy Smelter IV",
                5,
                "HighTech combination Smelter",
                GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                2,
                1,
                false,
                0,
                1,
                "AlloySmelter.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ALLOY_SMELTER",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                211,
                "basicmachine.assembler.tier.01",
                "Basic Assembling Machine",
                1,
                "Avengers, Assemble!",
                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "Assembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ASSEMBLER",
                new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                212,
                "basicmachine.assembler.tier.02",
                "Advanced Assembling Machine",
                2,
                "Avengers, Assemble!",
                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                9,
                1,
                true,
                0,
                1,
                "Assembler2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ASSEMBLER",
                new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                213,
                "basicmachine.assembler.tier.03",
                "Advanced Assembling Machine II",
                3,
                "Avengers, Assemble!",
                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                9,
                1,
                true,
                0,
                1,
                "Assembler2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ASSEMBLER",
                new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                214,
                "basicmachine.assembler.tier.04",
                "Advanced Assembling Machine III",
                4,
                "Avengers, Assemble!",
                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                9,
                1,
                true,
                0,
                1,
                "Assembler2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ASSEMBLER",
                new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                215,
                "basicmachine.assembler.tier.05",
                "Advanced Assembling Machine IV",
                5,
                "Avengers, Assemble!",
                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                9,
                1,
                true,
                0,
                1,
                "Assembler2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ASSEMBLER",
                new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                221,
                "basicmachine.bender.tier.01",
                "Basic Bending Machine",
                1,
                "Boo, he's bad! We want BENDER!!!",
                GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Bender.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "BENDER",
                new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                222,
                "basicmachine.bender.tier.02",
                "Advanced Bending Machine",
                2,
                "Boo, he's bad! We want BENDER!!!",
                GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Bender.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "BENDER",
                new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                223,
                "basicmachine.bender.tier.03",
                "Advanced Bending Machine II",
                3,
                "Boo, he's bad! We want BENDER!!!",
                GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Bender.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "BENDER",
                new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                224,
                "basicmachine.bender.tier.04",
                "Advanced Bending Machine III",
                4,
                "Boo, he's bad! We want BENDER!!!",
                GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Bender.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "BENDER",
                new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                225,
                "basicmachine.bender.tier.05",
                "Advanced Bending Machine IV",
                5,
                "Boo, he's bad! We want BENDER!!!",
                GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Bender.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "BENDER",
                new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                231,
                "basicmachine.canner.tier.01",
                "Basic Canning Machine",
                1,
                "Unmobile Food Canning Machine GTA4",
                GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                2,
                2,
                false,
                0,
                1,
                "Canner.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CANNER",
                new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                232,
                "basicmachine.canner.tier.02",
                "Advanced Canning Machine",
                2,
                "Unmobile Food Canning Machine GTA4",
                GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                2,
                2,
                false,
                0,
                1,
                "Canner.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CANNER",
                new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                233,
                "basicmachine.canner.tier.03",
                "Advanced Canning Machine II",
                3,
                "Unmobile Food Canning Machine GTA4",
                GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                2,
                2,
                false,
                0,
                1,
                "Canner.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CANNER",
                new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                234,
                "basicmachine.canner.tier.04",
                "Advanced Canning Machine III",
                4,
                "Unmobile Food Canning Machine GTA4",
                GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                2,
                2,
                false,
                0,
                1,
                "Canner.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CANNER",
                new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                235,
                "basicmachine.canner.tier.05",
                "Advanced Canning Machine IV",
                5,
                "Unmobile Food Canning Machine GTA4",
                GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                2,
                2,
                false,
                0,
                1,
                "Canner.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CANNER",
                new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                241,
                "basicmachine.compressor.tier.01",
                "Basic Compressor",
                1,
                "Compress-O-Matic C77",
                GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Compressor.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "COMPRESSOR",
                new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                242,
                "basicmachine.compressor.tier.02",
                "Advanced Compressor",
                2,
                "Compress-O-Matic C77",
                GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Compressor.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "COMPRESSOR",
                new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                243,
                "basicmachine.compressor.tier.03",
                "Advanced Compressor II",
                3,
                "Compress-O-Matic C77",
                GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Compressor.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "COMPRESSOR",
                new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                244,
                "basicmachine.compressor.tier.04",
                "Advanced Compressor III",
                4,
                "Compress-O-Matic C77",
                GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Compressor.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "COMPRESSOR",
                new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                245,
                "basicmachine.compressor.tier.05",
                "Singularity Compressor",
                5,
                "Compress-O-Matic C77",
                GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Compressor.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "COMPRESSOR",
                new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                251,
                "basicmachine.cutter.tier.01",
                "Basic Cutting Machine",
                1,
                "Slice'N Dice",
                GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                1,
                2,
                true,
                0,
                1,
                "Cutter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CUTTER",
                new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade })
                        .getStackForm(1L));
        ItemList.Machine_MV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                252,
                "basicmachine.cutter.tier.02",
                "Advanced Cutting Machine",
                2,
                "Slice'N Dice",
                GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                2,
                2,
                true,
                0,
                1,
                "Cutter2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CUTTER",
                new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade })
                        .getStackForm(1L));
        ItemList.Machine_HV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                253,
                "basicmachine.cutter.tier.03",
                "Advanced Cutting Machine II",
                3,
                "Slice'N Dice",
                GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                2,
                4,
                true,
                0,
                1,
                "Cutter4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CUTTER",
                new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade })
                        .getStackForm(1L));
        ItemList.Machine_EV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                254,
                "basicmachine.cutter.tier.04",
                "Advanced Cutting Machine III",
                4,
                "Slice'N Dice",
                GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                2,
                4,
                true,
                0,
                1,
                "Cutter4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CUTTER",
                new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade })
                        .getStackForm(1L));
        ItemList.Machine_IV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                255,
                "basicmachine.cutter.tier.05",
                "Advanced Cutting Machine IV",
                5,
                "Slice'N Dice",
                GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                2,
                4,
                true,
                0,
                1,
                "Cutter4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CUTTER",
                new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade })
                        .getStackForm(1L));

        ItemList.Machine_LV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                261,
                "basicmachine.e_furnace.tier.01",
                "Basic Electric Furnace",
                1,
                "Not like using a Commodore 64",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Furnace")
                        .getStackForm(1L));
        ItemList.Machine_MV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                262,
                "basicmachine.e_furnace.tier.02",
                "Advanced Electric Furnace",
                2,
                "Not like using a Commodore 64",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Furnace")
                        .getStackForm(1L));
        ItemList.Machine_HV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                263,
                "basicmachine.e_furnace.tier.03",
                "Advanced Electric Furnace II",
                3,
                "Not like using a Commodore 64",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Furnace")
                        .getStackForm(1L));
        ItemList.Machine_EV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                264,
                "basicmachine.e_furnace.tier.04",
                "Advanced Electric Furnace III",
                4,
                "Not like using a Commodore 64",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Furnace")
                        .getStackForm(1L));
        ItemList.Machine_IV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                265,
                "basicmachine.e_furnace.tier.05",
                "Electron Exitement Processor",
                5,
                "Not like using a Commodore 64",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Furnace")
                        .getStackForm(1L));

        ItemList.Machine_LV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                271,
                "basicmachine.extractor.tier.01",
                "Basic Extractor",
                1,
                "Dejuicer-Device of Doom - D123",
                GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Extractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRACTOR",
                new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                272,
                "basicmachine.extractor.tier.02",
                "Advanced Extractor",
                2,
                "Dejuicer-Device of Doom - D123",
                GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Extractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRACTOR",
                new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                273,
                "basicmachine.extractor.tier.03",
                "Advanced Extractor II",
                3,
                "Dejuicer-Device of Doom - D123",
                GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Extractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRACTOR",
                new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                274,
                "basicmachine.extractor.tier.04",
                "Advanced Extractor III",
                4,
                "Dejuicer-Device of Doom - D123",
                GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Extractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRACTOR",
                new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                275,
                "basicmachine.extractor.tier.05",
                "Vacuum Extractor",
                5,
                "Dejuicer-Device of Doom - D123",
                GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Extractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRACTOR",
                new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                281,
                "basicmachine.extruder.tier.01",
                "Basic Extruder",
                1,
                "Universal Machine for Metal Working",
                GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Extruder.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRUDER",
                new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                282,
                "basicmachine.extruder.tier.02",
                "Advanced Extruder",
                2,
                "Universal Machine for Metal Working",
                GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Extruder.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRUDER",
                new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                283,
                "basicmachine.extruder.tier.03",
                "Advanced Extruder II",
                3,
                "Universal Machine for Metal Working",
                GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Extruder.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRUDER",
                new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                284,
                "basicmachine.extruder.tier.04",
                "Advanced Extruder III",
                4,
                "Universal Machine for Metal Working",
                GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Extruder.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRUDER",
                new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                285,
                "basicmachine.extruder.tier.05",
                "Advanced Extruder IV",
                5,
                "Universal Machine for Metal Working",
                GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                2,
                1,
                false,
                0,
                1,
                "Extruder.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "EXTRUDER",
                new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                291,
                "basicmachine.lathe.tier.01",
                "Basic Lathe",
                1,
                "Produces Rods more efficiently",
                GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                1,
                2,
                false,
                0,
                1,
                "Lathe.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "LATHE",
                new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OrePrefixes.gem.get(Materials.Diamond) })
                        .getStackForm(1L));
        ItemList.Machine_MV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                292,
                "basicmachine.lathe.tier.02",
                "Advanced Lathe",
                2,
                "Produces Rods more efficiently",
                GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                1,
                2,
                false,
                0,
                1,
                "Lathe.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "LATHE",
                new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                    OrePrefixes.gemFlawless.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_HV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                293,
                "basicmachine.lathe.tier.03",
                "Advanced Lathe II",
                3,
                "Produces Rods more efficiently",
                GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                1,
                2,
                false,
                0,
                1,
                "Lathe.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "LATHE",
                new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond })
                        .getStackForm(1L));
        ItemList.Machine_EV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                294,
                "basicmachine.lathe.tier.04",
                "Advanced Lathe III",
                4,
                "Produces Rods more efficiently",
                GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                1,
                2,
                false,
                0,
                1,
                "Lathe.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "LATHE",
                new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond })
                        .getStackForm(1L));
        ItemList.Machine_IV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                295,
                "basicmachine.lathe.tier.05",
                "Advanced Lathe IV",
                5,
                "Produces Rods more efficiently",
                GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                1,
                2,
                false,
                0,
                1,
                "Lathe.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "LATHE",
                new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond })
                        .getStackForm(1L));

        ItemList.Machine_LV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                301,
                "basicmachine.macerator.tier.01",
                "Basic Macerator",
                1,
                "Schreddering your Ores",
                GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Macerator1.png",
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "MACERATOR",
                new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.gem.get(Materials.Diamond) })
                        .getStackForm(1L));
        ItemList.Machine_MV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                302,
                "basicmachine.macerator.tier.02",
                "Advanced Macerator",
                2,
                "Schreddering your Ores",
                GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                1,
                1,
                false,
                0,
                1,
                "Macerator1.png",
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "MACERATOR",
                new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    OrePrefixes.gemFlawless.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_HV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                303,
                "basicmachine.macerator.tier.03",
                "Universal Macerator",
                3,
                "Schreddering your Ores with Byproducts",
                GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Macerator2.png",
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                        .getStackForm(1L));
        ItemList.Machine_EV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                304,
                "basicmachine.macerator.tier.04",
                "Universal Pulverizer",
                4,
                "Schreddering your Ores with Byproducts",
                GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                1,
                3,
                false,
                0,
                1,
                "Macerator3.png",
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                        .getStackForm(1L));
        ItemList.Machine_IV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                305,
                "basicmachine.macerator.tier.05",
                "Blend-O-Matic 9001",
                5,
                "Schreddering your Ores with Byproducts",
                GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                1,
                4,
                false,
                0,
                1,
                "Macerator4.png",
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                        .getStackForm(1L));

        ItemList.Machine_LV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                311,
                "basicmachine.microwave.tier.01",
                "Basic Microwave",
                1,
                "Did you really read the instruction Manual?",
                GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "MICROWAVE",
                new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) })
                        .getStackForm(1L));
        ItemList.Machine_MV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                312,
                "basicmachine.microwave.tier.02",
                "Advanced Microwave",
                2,
                "Did you really read the instruction Manual?",
                GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "MICROWAVE",
                new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) })
                        .getStackForm(1L));
        ItemList.Machine_HV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                313,
                "basicmachine.microwave.tier.03",
                "Advanced Microwave II",
                3,
                "Did you really read the instruction Manual?",
                GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "MICROWAVE",
                new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) })
                        .getStackForm(1L));
        ItemList.Machine_EV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                314,
                "basicmachine.microwave.tier.04",
                "Advanced Microwave III",
                4,
                "Did you really read the instruction Manual?",
                GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "MICROWAVE",
                new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) })
                        .getStackForm(1L));
        ItemList.Machine_IV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                315,
                "basicmachine.microwave.tier.05",
                "Advanced Microwave IV",
                5,
                "Did you really read the instruction Manual?",
                GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Furnace.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "MICROWAVE",
                new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) })
                        .getStackForm(1L));

        ItemList.Machine_LV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                321,
                "basicmachine.printer.tier.01",
                "Basic Printer",
                1,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                322,
                "basicmachine.printer.tier.02",
                "Advanced Printer",
                2,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                323,
                "basicmachine.printer.tier.03",
                "Advanced Printer II",
                3,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                324,
                "basicmachine.printer.tier.04",
                "Advanced Printer III",
                4,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                325,
                "basicmachine.printer.tier.05",
                "Advanced Printer IV",
                5,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_LuV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                326,
                "basicmachine.printer.tier.06",
                "Advanced Printer V",
                6,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_ZPM_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                327,
                "basicmachine.printer.tier.07",
                "Advanced Printer VI",
                7,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_UV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                328,
                "basicmachine.printer.tier.08",
                "Advanced Printer VII",
                8,
                "It can copy Books and paint Stuff",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                1,
                1,
                true,
                0,
                1,
                "Printer.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.TOP_SMOKE,
                "PRINTER",
                new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                331,
                "basicmachine.recycler.tier.01",
                "Basic Recycler",
                1,
                "Compress, burn, obliterate and filter EVERYTHING",
                GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Recycler.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "RECYCLER",
                new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) })
                        .getStackForm(1L));
        ItemList.Machine_MV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                332,
                "basicmachine.recycler.tier.02",
                "Advanced Recycler",
                2,
                "Compress, burn, obliterate and filter EVERYTHING",
                GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Recycler.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "RECYCLER",
                new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) })
                        .getStackForm(1L));
        ItemList.Machine_HV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                333,
                "basicmachine.recycler.tier.03",
                "Advanced Recycler II",
                3,
                "Compress, burn, obliterate and filter EVERYTHING",
                GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Recycler.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "RECYCLER",
                new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) })
                        .getStackForm(1L));
        ItemList.Machine_EV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                334,
                "basicmachine.recycler.tier.04",
                "Advanced Recycler III",
                4,
                "Compress, burn, obliterate and filter EVERYTHING",
                GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Recycler.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "RECYCLER",
                new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) })
                        .getStackForm(1L));
        ItemList.Machine_IV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                335,
                "basicmachine.recycler.tier.05",
                "The Oblitterator",
                5,
                "Compress, burn, obliterate and filter EVERYTHING",
                GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Recycler.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "RECYCLER",
                new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) })
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LV, 'T', ItemList.Emitter_LV, 'R',
                ItemList.Sensor_LV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MV, 'T', ItemList.Emitter_MV, 'R',
                ItemList.Sensor_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_HV, 'T', ItemList.Emitter_HV, 'R',
                ItemList.Sensor_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_EV, 'T', ItemList.Emitter_EV, 'R',
                ItemList.Sensor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_IV, 'T', ItemList.Emitter_IV, 'R',
                ItemList.Sensor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        ItemList.Machine_LV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                351,
                "basicmachine.wiremill.tier.01",
                "Basic Wiremill",
                1,
                "Produces Wires more efficiently",
                GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                2,
                1,
                false,
                0,
                1,
                "Wiremill.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "WIREMILL",
                new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                352,
                "basicmachine.wiremill.tier.02",
                "Advanced Wiremill",
                2,
                "Produces Wires more efficiently",
                GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                2,
                1,
                false,
                0,
                1,
                "Wiremill.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "WIREMILL",
                new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                353,
                "basicmachine.wiremill.tier.03",
                "Advanced Wiremill II",
                3,
                "Produces Wires more efficiently",
                GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                2,
                1,
                false,
                0,
                1,
                "Wiremill.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "WIREMILL",
                new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                354,
                "basicmachine.wiremill.tier.04",
                "Advanced Wiremill III",
                4,
                "Produces Wires more efficiently",
                GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                2,
                1,
                false,
                0,
                1,
                "Wiremill.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "WIREMILL",
                new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                355,
                "basicmachine.wiremill.tier.05",
                "Advanced Wiremill IV",
                5,
                "Produces Wires more efficiently",
                GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                2,
                1,
                false,
                0,
                1,
                "Wiremill.png",
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                false,
                false,
                SpecialEffects.NONE,
                "WIREMILL",
                new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                361,
                "basicmachine.centrifuge.tier.01",
                "Basic Centrifuge",
                1,
                "Separating Molecules",
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                2,
                6,
                true,
                0,
                1,
                "Centrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CENTRIFUGE",
                new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                362,
                "basicmachine.centrifuge.tier.02",
                "Advanced Centrifuge",
                2,
                "Separating Molecules",
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                2,
                6,
                true,
                0,
                1,
                "Centrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CENTRIFUGE",
                new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                363,
                "basicmachine.centrifuge.tier.03",
                "Turbo Centrifuge",
                3,
                "Separating Molecules",
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                2,
                6,
                true,
                0,
                1,
                "Centrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CENTRIFUGE",
                new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                364,
                "basicmachine.centrifuge.tier.04",
                "Molecular Separator",
                4,
                "Separating Molecules",
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                2,
                6,
                true,
                0,
                1,
                "Centrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CENTRIFUGE",
                new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                365,
                "basicmachine.centrifuge.tier.05",
                "Molecular Cyclone",
                5,
                "Separating Molecules",
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                2,
                6,
                true,
                0,
                1,
                "Centrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CENTRIFUGE",
                new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                371,
                "basicmachine.electrolyzer.tier.01",
                "Basic Electrolyzer",
                1,
                "Electrolyzing Molecules",
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                2,
                6,
                true,
                0,
                1,
                "Electrolyzer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROLYZER",
                new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Gold), 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                372,
                "basicmachine.electrolyzer.tier.02",
                "Advanced Electrolyzer",
                2,
                "Electrolyzing Molecules",
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                2,
                6,
                true,
                0,
                1,
                "Electrolyzer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROLYZER",
                new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Silver),
                    'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                373,
                "basicmachine.electrolyzer.tier.03",
                "Advanced Electrolyzer II",
                3,
                "Electrolyzing Molecules",
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                2,
                6,
                true,
                0,
                1,
                "Electrolyzer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROLYZER",
                new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Electrum),
                    'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                374,
                "basicmachine.electrolyzer.tier.04",
                "Advanced Electrolyzer III",
                4,
                "Electrolyzing Molecules",
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                2,
                6,
                true,
                0,
                1,
                "Electrolyzer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROLYZER",
                new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Platinum),
                    'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                375,
                "basicmachine.electrolyzer.tier.05",
                "Molecular Disintegrator E-4908",
                5,
                "Electrolyzing Molecules",
                GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                2,
                6,
                true,
                0,
                1,
                "Electrolyzer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROLYZER",
                new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.HSSG), 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                381,
                "basicmachine.thermalcentrifuge.tier.01",
                "Basic Thermal Centrifuge",
                1,
                "Separating Ores more precisely",
                GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                1,
                3,
                false,
                0,
                1,
                "ThermalCentrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                382,
                "basicmachine.thermalcentrifuge.tier.02",
                "Advanced Thermal Centrifuge",
                2,
                "Separating Ores more precisely",
                GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                1,
                3,
                false,
                0,
                1,
                "ThermalCentrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                383,
                "basicmachine.thermalcentrifuge.tier.03",
                "Advanced Thermal Centrifuge II",
                3,
                "Separating Ores more precisely",
                GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                1,
                3,
                false,
                0,
                1,
                "ThermalCentrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                384,
                "basicmachine.thermalcentrifuge.tier.04",
                "Advanced Thermal Centrifuge III",
                4,
                "Separating Ores more precisely",
                GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                1,
                3,
                false,
                0,
                1,
                "ThermalCentrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                385,
                "basicmachine.thermalcentrifuge.tier.05",
                "Blaze Sweatshop T-6350",
                5,
                "Separating Ores more precisely",
                GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                1,
                3,
                false,
                0,
                1,
                "ThermalCentrifuge.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                391,
                "basicmachine.orewasher.tier.01",
                "Basic Ore Washing Plant",
                1,
                "Getting more Byproducts from your Ores",
                GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                1,
                3,
                true,
                0,
                1,
                "OreWasher.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ORE_WASHER",
                new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_MV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                392,
                "basicmachine.orewasher.tier.02",
                "Advanced Ore Washing Plant",
                2,
                "Getting more Byproducts from your Ores",
                GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                1,
                3,
                true,
                0,
                1,
                "OreWasher.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ORE_WASHER",
                new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_HV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                393,
                "basicmachine.orewasher.tier.03",
                "Advanced Ore Washing Plant II",
                3,
                "Getting more Byproducts from your Ores",
                GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                1,
                3,
                true,
                0,
                1,
                "OreWasher.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ORE_WASHER",
                new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_EV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                394,
                "basicmachine.orewasher.tier.04",
                "Advanced Ore Washing Plant III",
                4,
                "Getting more Byproducts from your Ores",
                GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                1,
                3,
                true,
                0,
                1,
                "OreWasher.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ORE_WASHER",
                new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_IV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                395,
                "basicmachine.orewasher.tier.05",
                "Repurposed Laundry-Washer I-360",
                5,
                "Getting more Byproducts from your Ores",
                GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                1,
                3,
                true,
                0,
                1,
                "OreWasher.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "ORE_WASHER",
                new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LV, 'R', ItemList.Robot_Arm_LV, 'V',
                ItemList.Conveyor_Module_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_MV, 'R', ItemList.Robot_Arm_MV, 'V',
                ItemList.Conveyor_Module_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_HV, 'R', ItemList.Robot_Arm_HV, 'V',
                ItemList.Conveyor_Module_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_EV, 'R', ItemList.Robot_Arm_EV, 'V',
                ItemList.Conveyor_Module_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'V',
                ItemList.Conveyor_Module_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LuV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV, 'V',
                ItemList.Conveyor_Module_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_ZPM_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM, 'V',
                ItemList.Conveyor_Module_ZPM, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_UV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_UV, 'R', ItemList.Robot_Arm_UV, 'V',
                ItemList.Conveyor_Module_UV, 'C', OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'W',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OreDictNames.craftingChest });

        ItemList.Machine_LV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                411,
                "basicmachine.unboxinator.tier.01",
                "Basic Unpackager",
                1,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_MV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                412,
                "basicmachine.unboxinator.tier.02",
                "Advanced Unpackager",
                2,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_HV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                413,
                "basicmachine.unboxinator.tier.03",
                "Advanced Unpackager II",
                3,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_EV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                414,
                "basicmachine.unboxinator.tier.04",
                "Advanced Unpackager III",
                4,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_IV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                415,
                "basicmachine.unboxinator.tier.05",
                "Unboxinator",
                5,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_LuV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                416,
                "basicmachine.unboxinator.tier.06",
                "Unboxinator",
                6,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_ZPM_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                417,
                "basicmachine.unboxinator.tier.07",
                "Unboxinator",
                7,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_UV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                418,
                "basicmachine.unboxinator.tier.08",
                "Unboxinator",
                8,
                "Grabs things out of Boxes",
                GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                1,
                2,
                false,
                0,
                1,
                "Unpackager.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "UNBOXINATOR",
                new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));

        ItemList.Machine_LV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                421,
                "basicmachine.chemicalreactor.tier.01",
                "Basic Chemical Reactor",
                1,
                "Letting Chemicals react with each other",
                GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                2,
                2,
                true,
                0,
                1,
                "ChemicalReactor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                422,
                "basicmachine.chemicalreactor.tier.02",
                "Advanced Chemical Reactor",
                2,
                "Letting Chemicals react with each other",
                GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                2,
                2,
                true,
                0,
                1,
                "ChemicalReactor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                423,
                "basicmachine.chemicalreactor.tier.03",
                "Advanced Chemical Reactor II",
                3,
                "Letting Chemicals react with each other",
                GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                2,
                2,
                true,
                0,
                1,
                "ChemicalReactor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    OrePrefixes.pipeMedium.get(Materials.Plastic) }).getStackForm(1L));
        ItemList.Machine_EV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                424,
                "basicmachine.chemicalreactor.tier.04",
                "Advanced Chemical Reactor III",
                4,
                "Letting Chemicals react with each other",
                GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                2,
                2,
                true,
                0,
                1,
                "ChemicalReactor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    OrePrefixes.pipeLarge.get(Materials.Plastic) }).getStackForm(1L));
        ItemList.Machine_IV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                425,
                "basicmachine.chemicalreactor.tier.05",
                "Advanced Chemical Reactor IV",
                5,
                "Letting Chemicals react with each other",
                GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                2,
                2,
                true,
                0,
                1,
                "ChemicalReactor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic) })
                        .getStackForm(1L));

        ItemList.Machine_LV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                431,
                "basicmachine.fluidcanner.tier.01",
                "Basic Fluid Canner",
                1,
                "Puts Fluids into and out of Containers",
                GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidCanner.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_CANNER",
                new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                432,
                "basicmachine.fluidcanner.tier.02",
                "Advanced Fluid Canner",
                2,
                "Puts Fluids into and out of Containers",
                GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidCanner.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_CANNER",
                new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                433,
                "basicmachine.fluidcanner.tier.03",
                "Quick Fluid Canner",
                3,
                "Puts Fluids into and out of Containers",
                GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidCanner.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_CANNER",
                new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                434,
                "basicmachine.fluidcanner.tier.04",
                "Turbo Fluid Canner",
                4,
                "Puts Fluids into and out of Containers",
                GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidCanner.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_CANNER",
                new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                435,
                "basicmachine.fluidcanner.tier.05",
                "Instant Fluid Canner",
                5,
                "Puts Fluids into and out of Containers",
                GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidCanner.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_CANNER",
                new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'C',
                OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_MV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C',
                OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_HV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'C',
                OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_EV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'C',
                OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_IV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'C',
                OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'G',
                new ItemStack(Blocks.glass, 1) });

        if (Forestry.isModLoaded()) {

            /* Conversion recipes */
            // TODO: Move those recipes with the other recipes
            if (Gendustry.isModLoaded()) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.Machine_IndustrialApiary.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "IndustrialApiary", 1, 0) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_Frame.get(1),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "UpgradeFrame", 1) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_PRODUCTION.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 0) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_PLAINS.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 17) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_LIGHT.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 11) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_FLOWERING.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 2) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_WINTER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 20) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_DRYER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 5) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_AUTOMATION.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 14) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 4) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_HELL.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 13) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_POLLEN.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 22) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_DESERT.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 16) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_COOLER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 7) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_LIFESPAN.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 1) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_SEAL.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 10) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_STABILIZER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 19) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_JUNGLE.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 18) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_TERRITORY.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 3) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_OCEAN.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 21) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_SKY.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 12) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_HEATER.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 6) });
                GT_ModHandler.addShapelessCraftingRecipe(
                    ItemList.IndustrialApiary_Upgrade_SIEVE.get(1L),
                    new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 15) });
            }
        }

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV, 'C',
                OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV, 'C',
                OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten) });

        ItemList.Machine_LV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                471,
                "basicmachine.amplifab.tier.01",
                "Basic Amplifabricator",
                1,
                "Extracting UU Amplifier",
                GT_Recipe.GT_Recipe_Map.sAmplifiers,
                1,
                1,
                1000,
                0,
                1,
                "Amplifabricator.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "AMPLIFAB",
                new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_MV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                472,
                "basicmachine.amplifab.tier.02",
                "Advanced Amplifabricator",
                2,
                "Extracting UU Amplifier",
                GT_Recipe.GT_Recipe_Map.sAmplifiers,
                1,
                1,
                1000,
                0,
                1,
                "Amplifabricator.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "AMPLIFAB",
                new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_HV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                473,
                "basicmachine.amplifab.tier.03",
                "Advanced Amplifabricator II",
                3,
                "Extracting UU Amplifier",
                GT_Recipe.GT_Recipe_Map.sAmplifiers,
                1,
                1,
                1000,
                0,
                1,
                "Amplifabricator.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "AMPLIFAB",
                new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_EV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                474,
                "basicmachine.amplifab.tier.04",
                "Advanced Amplifabricator III",
                4,
                "Extracting UU Amplifier",
                GT_Recipe.GT_Recipe_Map.sAmplifiers,
                1,
                1,
                1000,
                0,
                1,
                "Amplifabricator.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "AMPLIFAB",
                new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_IV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                475,
                "basicmachine.amplifab.tier.05",
                "Advanced Amplifabricator IV",
                5,
                "Extracting UU Amplifier",
                GT_Recipe.GT_Recipe_Map.sAmplifiers,
                1,
                1,
                1000,
                0,
                1,
                "Amplifabricator.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "AMPLIFAB",
                new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'F',
                ItemList.Field_Generator_LV, 'E', ItemList.Emitter_LV, 'C', OrePrefixes.circuit.get(Materials.Good),
                'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MV, 'F',
                ItemList.Field_Generator_MV, 'E', ItemList.Emitter_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced),
                'W', OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_HV, 'F',
                ItemList.Field_Generator_HV, 'E', ItemList.Emitter_HV, 'C', OrePrefixes.circuit.get(Materials.Data),
                'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_EV, 'F',
                ItemList.Field_Generator_EV, 'E', ItemList.Emitter_EV, 'C', OrePrefixes.circuit.get(Materials.Elite),
                'W', OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_IV, 'F',
                ItemList.Field_Generator_IV, 'E', ItemList.Emitter_IV, 'C', OrePrefixes.circuit.get(Materials.Master),
                'W', OrePrefixes.cableGt04.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Pump_LV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Pump_MV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Pump_HV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Pump_EV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Pump_IV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1) });

        ItemList.Machine_LV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                501,
                "basicmachine.fermenter.tier.01",
                "Basic Fermenter",
                1,
                "Fermenting Fluids",
                GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                1,
                1,
                true,
                0,
                1,
                "Fermenter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FERMENTER",
                new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                502,
                "basicmachine.fermenter.tier.02",
                "Advanced Fermenter",
                2,
                "Fermenting Fluids",
                GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                1,
                1,
                true,
                0,
                1,
                "Fermenter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FERMENTER",
                new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                503,
                "basicmachine.fermenter.tier.03",
                "Advanced Fermenter II",
                3,
                "Fermenting Fluids",
                GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                1,
                1,
                true,
                0,
                1,
                "Fermenter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FERMENTER",
                new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                504,
                "basicmachine.fermenter.tier.04",
                "Advanced Fermenter III",
                4,
                "Fermenting Fluids",
                GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                1,
                1,
                true,
                0,
                1,
                "Fermenter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FERMENTER",
                new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                505,
                "basicmachine.fermenter.tier.05",
                "Advanced Fermenter IV",
                5,
                "Fermenting Fluids",
                GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                1,
                1,
                true,
                0,
                1,
                "Fermenter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FERMENTER",
                new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                511,
                "basicmachine.fluidextractor.tier.01",
                "Basic Fluid Extractor",
                1,
                "Extracting Fluids from Items",
                GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidExtractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                512,
                "basicmachine.fluidextractor.tier.02",
                "Advanced Fluid Extractor",
                2,
                "Extracting Fluids from Items",
                GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidExtractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                513,
                "basicmachine.fluidextractor.tier.03",
                "Advanced Fluid Extractor II",
                3,
                "Extracting Fluids from Items",
                GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidExtractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                514,
                "basicmachine.fluidextractor.tier.04",
                "Advanced Fluid Extractor III",
                4,
                "Extracting Fluids from Items",
                GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidExtractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                515,
                "basicmachine.fluidextractor.tier.05",
                "Advanced Fluid Extractor IV",
                5,
                "Extracting Fluids from Items",
                GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidExtractor.png",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                521,
                "basicmachine.fluidsolidifier.tier.01",
                "Basic Fluid Solidifier",
                1,
                "Cools Fluids down to form Solids",
                GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidSolidifier.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_MV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                522,
                "basicmachine.fluidsolidifier.tier.02",
                "Advanced Fluid Solidifier",
                2,
                "Cools Fluids down to form Solids",
                GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidSolidifier.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_HV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                523,
                "basicmachine.fluidsolidifier.tier.03",
                "Advanced Fluid Solidifier II",
                3,
                "Cools Fluids down to form Solids",
                GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidSolidifier.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_EV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                524,
                "basicmachine.fluidsolidifier.tier.04",
                "Advanced Fluid Solidifier III",
                4,
                "Cools Fluids down to form Solids",
                GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidSolidifier.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));
        ItemList.Machine_IV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                525,
                "basicmachine.fluidsolidifier.tier.05",
                "Advanced Fluid Solidifier IV",
                5,
                "Cools Fluids down to form Solids",
                GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                1,
                1,
                true,
                0,
                1,
                "FluidSolidifier.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                        .getStackForm(1L));

        ItemList.Machine_LV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                531,
                "basicmachine.distillery.tier.01",
                "Basic Distillery",
                1,
                "Extracting the most relevant Parts of Fluids",
                GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                1,
                1,
                true,
                0,
                1,
                "Distillery.png",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "DISTILLERY",
                new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                532,
                "basicmachine.distillery.tier.02",
                "Advanced Distillery",
                2,
                "Extracting the most relevant Parts of Fluids",
                GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                1,
                1,
                true,
                0,
                1,
                "Distillery.png",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "DISTILLERY",
                new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                533,
                "basicmachine.distillery.tier.03",
                "Advanced Distillery II",
                3,
                "Extracting the most relevant Parts of Fluids",
                GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                1,
                1,
                true,
                0,
                1,
                "Distillery.png",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "DISTILLERY",
                new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                534,
                "basicmachine.distillery.tier.04",
                "Advanced Distillery III",
                4,
                "Extracting the most relevant Parts of Fluids",
                GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                1,
                1,
                true,
                0,
                1,
                "Distillery.png",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "DISTILLERY",
                new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                535,
                "basicmachine.distillery.tier.05",
                "Advanced Distillery IV",
                5,
                "Extracting the most relevant Parts of Fluids",
                GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                1,
                1,
                true,
                0,
                1,
                "Distillery.png",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "DISTILLERY",
                new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                541,
                "basicmachine.chemicalbath.tier.01",
                "Basic Chemical Bath",
                1,
                "Bathing Ores in Chemicals to separate them",
                GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                1,
                3,
                true,
                0,
                1,
                "ChemicalBath.png",
                SoundResource.NONE,
                false,
                true,
                SpecialEffects.NONE,
                "CHEMICAL_BATH",
                new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                542,
                "basicmachine.chemicalbath.tier.02",
                "Advanced Chemical Bath",
                2,
                "Bathing Ores in Chemicals to separate them",
                GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                1,
                3,
                true,
                0,
                1,
                "ChemicalBath.png",
                SoundResource.NONE,
                false,
                true,
                SpecialEffects.NONE,
                "CHEMICAL_BATH",
                new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                543,
                "basicmachine.chemicalbath.tier.03",
                "Advanced Chemical Bath II",
                3,
                "Bathing Ores in Chemicals to separate them",
                GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                1,
                3,
                true,
                0,
                1,
                "ChemicalBath.png",
                SoundResource.NONE,
                false,
                true,
                SpecialEffects.NONE,
                "CHEMICAL_BATH",
                new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                544,
                "basicmachine.chemicalbath.tier.04",
                "Advanced Chemical Bath III",
                4,
                "Bathing Ores in Chemicals to separate them",
                GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                1,
                3,
                true,
                0,
                1,
                "ChemicalBath.png",
                SoundResource.NONE,
                false,
                true,
                SpecialEffects.NONE,
                "CHEMICAL_BATH",
                new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                545,
                "basicmachine.chemicalbath.tier.05",
                "Advanced Chemical Bath IV",
                5,
                "Bathing Ores in Chemicals to separate them",
                GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                1,
                3,
                true,
                0,
                1,
                "ChemicalBath.png",
                SoundResource.NONE,
                false,
                true,
                SpecialEffects.NONE,
                "CHEMICAL_BATH",
                new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                551,
                "basicmachine.polarizer.tier.01",
                "Basic Polarizer",
                1,
                "Bipolarising your Magnets",
                GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Polarizer.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "POLARIZER",
                new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                552,
                "basicmachine.polarizer.tier.02",
                "Advanced Polarizer",
                2,
                "Bipolarising your Magnets",
                GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Polarizer.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "POLARIZER",
                new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                553,
                "basicmachine.polarizer.tier.03",
                "Advanced Polarizer II",
                3,
                "Bipolarising your Magnets",
                GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Polarizer.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "POLARIZER",
                new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                554,
                "basicmachine.polarizer.tier.04",
                "Advanced Polarizer III",
                4,
                "Bipolarising your Magnets",
                GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Polarizer.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "POLARIZER",
                new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                555,
                "basicmachine.polarizer.tier.05",
                "Advanced Polarizer IV",
                5,
                "Bipolarising your Magnets",
                GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                1,
                1,
                false,
                0,
                1,
                "Polarizer.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "POLARIZER",
                new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                561,
                "basicmachine.electromagneticseparator.tier.01",
                "Basic Electromagnetic Separator",
                1,
                "Separating the magnetic Ores from the rest",
                GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                1,
                3,
                false,
                0,
                1,
                "ElectromagneticSeparator.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                562,
                "basicmachine.electromagneticseparator.tier.02",
                "Advanced Electromagnetic Separator",
                2,
                "Separating the magnetic Ores from the rest",
                GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                1,
                3,
                false,
                0,
                1,
                "ElectromagneticSeparator.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                563,
                "basicmachine.electromagneticseparator.tier.03",
                "Advanced Electromagnetic Separator II",
                3,
                "Separating the magnetic Ores from the rest",
                GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                1,
                3,
                false,
                0,
                1,
                "ElectromagneticSeparator.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                564,
                "basicmachine.electromagneticseparator.tier.04",
                "Advanced Electromagnetic Separator III",
                4,
                "Separating the magnetic Ores from the rest",
                GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                1,
                3,
                false,
                0,
                1,
                "ElectromagneticSeparator.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                565,
                "basicmachine.electromagneticseparator.tier.05",
                "Advanced Electromagnetic Separator IV",
                5,
                "Separating the magnetic Ores from the rest",
                GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                1,
                3,
                false,
                0,
                1,
                "ElectromagneticSeparator.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                571,
                "basicmachine.autoclave.tier.01",
                "Basic Autoclave",
                1,
                "Crystallizing your Dusts",
                GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                2,
                2,
                true,
                0,
                1,
                "Autoclave2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "AUTOCLAVE",
                new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                572,
                "basicmachine.autoclave.tier.02",
                "Advanced Autoclave",
                2,
                "Crystallizing your Dusts",
                GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                2,
                2,
                true,
                0,
                1,
                "Autoclave2.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "AUTOCLAVE",
                new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                573,
                "basicmachine.autoclave.tier.03",
                "Advanced Autoclave II",
                3,
                "Crystallizing your Dusts",
                GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                2,
                3,
                true,
                0,
                1,
                "Autoclave3.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "AUTOCLAVE",
                new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                574,
                "basicmachine.autoclave.tier.04",
                "Advanced Autoclave III",
                4,
                "Crystallizing your Dusts",
                GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                2,
                4,
                true,
                0,
                1,
                "Autoclave4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "AUTOCLAVE",
                new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                575,
                "basicmachine.autoclave.tier.05",
                "Advanced Autoclave IV",
                5,
                "Crystallizing your Dusts",
                GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                2,
                4,
                true,
                0,
                1,
                "Autoclave4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "AUTOCLAVE",
                new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                581,
                "basicmachine.mixer.tier.01",
                "Basic Mixer",
                1,
                "Will it Blend?",
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                6,
                1,
                true,
                0,
                1,
                "Mixer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "MIXER",
                new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                582,
                "basicmachine.mixer.tier.02",
                "Advanced Mixer",
                2,
                "Will it Blend?",
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                6,
                1,
                true,
                0,
                1,
                "Mixer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "MIXER",
                new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                583,
                "basicmachine.mixer.tier.03",
                "Advanced Mixer II",
                3,
                "Will it Blend?",
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                6,
                4,
                true,
                0,
                1,
                "Mixer4.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "MIXER",
                new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                584,
                "basicmachine.mixer.tier.04",
                "Advanced Mixer III",
                4,
                "Will it Blend?",
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                9,
                4,
                true,
                0,
                1,
                "Mixer6.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "MIXER",
                new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                585,
                "basicmachine.mixer.tier.05",
                "Advanced Mixer IV",
                5,
                "Will it Blend?",
                GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                9,
                4,
                true,
                0,
                1,
                "Mixer6.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "MIXER",
                new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                591,
                "basicmachine.laserengraver.tier.01",
                "Basic Precision Laser Engraver",
                1,
                "Don't look directly at the Laser",
                GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                2,
                1,
                true,
                0,
                1,
                "LaserEngraver.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "LASER_ENGRAVER",
                new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                592,
                "basicmachine.laserengraver.tier.02",
                "Advanced Precision Laser Engraver",
                2,
                "Don't look directly at the Laser",
                GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                2,
                1,
                true,
                0,
                1,
                "LaserEngraver.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "LASER_ENGRAVER",
                new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                593,
                "basicmachine.laserengraver.tier.03",
                "Advanced Precision Laser Engraver II",
                3,
                "Don't look directly at the Laser",
                GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                2,
                1,
                true,
                0,
                1,
                "LaserEngraver.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "LASER_ENGRAVER",
                new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                594,
                "basicmachine.laserengraver.tier.04",
                "Advanced Precision Laser Engraver III",
                4,
                "Don't look directly at the Laser",
                GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                4,
                1,
                true,
                0,
                1,
                "LaserEngraver2.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "LASER_ENGRAVER",
                new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                595,
                "basicmachine.laserengraver.tier.05",
                "Advanced Precision Laser Engraver IV",
                5,
                "Don't look directly at the Laser",
                GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                4,
                1,
                true,
                0,
                1,
                "LaserEngraver2.png",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "LASER_ENGRAVER",
                new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                601,
                "basicmachine.press.tier.01",
                "Basic Forming Press",
                1,
                "Imprinting Images into things",
                GT_Recipe.GT_Recipe_Map.sPressRecipes,
                2,
                1,
                false,
                0,
                1,
                "Press.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "PRESS",
                new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                602,
                "basicmachine.press.tier.02",
                "Advanced Forming Press",
                2,
                "Imprinting Images into things",
                GT_Recipe.GT_Recipe_Map.sPressRecipes,
                2,
                1,
                false,
                0,
                1,
                "Press.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "PRESS",
                new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                603,
                "basicmachine.press.tier.03",
                "Advanced Forming Press II",
                3,
                "Imprinting Images into things",
                GT_Recipe.GT_Recipe_Map.sPressRecipes,
                4,
                1,
                false,
                0,
                1,
                "Press2.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "PRESS",
                new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                604,
                "basicmachine.press.tier.04",
                "Advanced Forming Press III",
                4,
                "Imprinting Images into things",
                GT_Recipe.GT_Recipe_Map.sPressRecipes,
                4,
                1,
                false,
                0,
                1,
                "Press2.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "PRESS",
                new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                605,
                "basicmachine.press.tier.05",
                "Advanced Forming Press IV",
                5,
                "Imprinting Images into things",
                GT_Recipe.GT_Recipe_Map.sPressRecipes,
                6,
                1,
                false,
                0,
                1,
                "Press3.png",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                false,
                false,
                SpecialEffects.NONE,
                "PRESS",
                new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                611,
                "basicmachine.hammer.tier.01",
                "Basic Forge Hammer",
                1,
                "Stop, Hammertime!",
                GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                1,
                1,
                true,
                6,
                3,
                "Hammer.png",
                SoundResource.RANDOM_ANVIL_USE,
                false,
                false,
                SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil })
                        .getStackForm(1L));
        ItemList.Machine_MV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                612,
                "basicmachine.hammer.tier.02",
                "Advanced Forge Hammer",
                2,
                "Stop, Hammertime!",
                GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                1,
                1,
                true,
                6,
                3,
                "Hammer.png",
                SoundResource.RANDOM_ANVIL_USE,
                false,
                false,
                SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil })
                        .getStackForm(1L));
        ItemList.Machine_HV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                613,
                "basicmachine.hammer.tier.03",
                "Advanced Forge Hammer II",
                3,
                "Stop, Hammertime!",
                GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                1,
                1,
                true,
                6,
                3,
                "Hammer.png",
                SoundResource.RANDOM_ANVIL_USE,
                false,
                false,
                SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil })
                        .getStackForm(1L));
        ItemList.Machine_EV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                614,
                "basicmachine.hammer.tier.04",
                "Advanced Forge Hammer III",
                4,
                "Stop, Hammertime!",
                GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                1,
                1,
                true,
                6,
                3,
                "Hammer.png",
                SoundResource.RANDOM_ANVIL_USE,
                false,
                false,
                SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil })
                        .getStackForm(1L));
        ItemList.Machine_IV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                615,
                "basicmachine.hammer.tier.05",
                "Advanced Forge Hammer IV",
                5,
                "Stop, Hammertime!",
                GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                1,
                1,
                true,
                6,
                3,
                "Hammer.png",
                SoundResource.RANDOM_ANVIL_USE,
                false,
                false,
                SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil })
                        .getStackForm(1L));

        ItemList.Machine_LV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                621,
                "basicmachine.fluidheater.tier.01",
                "Basic Fluid Heater",
                1,
                "Heating up your Fluids",
                GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                1,
                0,
                true,
                0,
                1,
                "FluidHeater.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_HEATER",
                new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                622,
                "basicmachine.fluidheater.tier.02",
                "Advanced Fluid Heater",
                2,
                "Heating up your Fluids",
                GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                1,
                0,
                true,
                0,
                1,
                "FluidHeater.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_HEATER",
                new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                623,
                "basicmachine.fluidheater.tier.03",
                "Advanced Fluid Heater II",
                3,
                "Heating up your Fluids",
                GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                1,
                0,
                true,
                0,
                1,
                "FluidHeater.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_HEATER",
                new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                624,
                "basicmachine.fluidheater.tier.04",
                "Advanced Fluid Heater III",
                4,
                "Heating up your Fluids",
                GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                1,
                0,
                true,
                0,
                1,
                "FluidHeater.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_HEATER",
                new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                625,
                "basicmachine.fluidheater.tier.05",
                "Advanced Fluid Heater IV",
                5,
                "Heating up your Fluids",
                GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                1,
                0,
                true,
                0,
                1,
                "FluidHeater.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "FLUID_HEATER",
                new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                631,
                "basicmachine.slicer.tier.01",
                "Basic Slicing Machine",
                1,
                "Slice of Life",
                GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                2,
                1,
                false,
                0,
                1,
                "Slicer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SLICER",
                new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                632,
                "basicmachine.slicer.tier.02",
                "Advanced Slicing Machine",
                2,
                "Slice of Life",
                GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                2,
                1,
                false,
                0,
                1,
                "Slicer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SLICER",
                new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                633,
                "basicmachine.slicer.tier.03",
                "Advanced Slicing Machine II",
                3,
                "Slice of Life",
                GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                2,
                1,
                false,
                0,
                1,
                "Slicer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SLICER",
                new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                634,
                "basicmachine.slicer.tier.04",
                "Advanced Slicing Machine III",
                4,
                "Slice of Life",
                GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                2,
                1,
                false,
                0,
                1,
                "Slicer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SLICER",
                new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                635,
                "basicmachine.slicer.tier.05",
                "Advanced Slicing Machine IV",
                5,
                "Slice of Life",
                GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                2,
                1,
                false,
                0,
                1,
                "Slicer.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SLICER",
                new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                641,
                "basicmachine.sifter.tier.01",
                "Basic Sifting Machine",
                1,
                "Stay calm and keep sifting",
                GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                1,
                9,
                true,
                2,
                5,
                "Sifter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SIFTER",
                new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                642,
                "basicmachine.sifter.tier.02",
                "Advanced Sifting Machine",
                2,
                "Stay calm and keep sifting",
                GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                1,
                9,
                true,
                2,
                5,
                "Sifter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SIFTER",
                new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                643,
                "basicmachine.sifter.tier.03",
                "Advanced Sifting Machine II",
                3,
                "Stay calm and keep sifting",
                GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                1,
                9,
                true,
                2,
                5,
                "Sifter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SIFTER",
                new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                644,
                "basicmachine.sifter.tier.04",
                "Advanced Sifting Machine III",
                4,
                "Stay calm and keep sifting",
                GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                1,
                9,
                true,
                2,
                5,
                "Sifter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SIFTER",
                new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                645,
                "basicmachine.sifter.tier.05",
                "Advanced Sifting Machine IV",
                5,
                "Stay calm and keep sifting",
                GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                1,
                9,
                true,
                2,
                5,
                "Sifter.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "SIFTER",
                new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                651,
                "basicmachine.arcfurnace.tier.01",
                "Basic Arc Furnace",
                1,
                "",
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "ArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                    'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));

        ItemList.Machine_MV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                652,
                "basicmachine.arcfurnace.tier.02",
                "Advanced Arc Furnace",
                2,
                "",
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "ArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                    'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));
        ItemList.Machine_HV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                653,
                "basicmachine.arcfurnace.tier.03",
                "Advanced Arc Furnace II",
                3,
                "",
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "ArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                    'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));

        ItemList.Machine_EV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                654,
                "basicmachine.arcfurnace.tier.04",
                "Advanced Arc Furnace III",
                4,
                "",
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                1,
                9,
                true,
                0,
                1,
                "ArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                    'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));

        ItemList.Machine_IV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                655,
                "basicmachine.arcfurnace.tier.05",
                "Advanced Arc Furnace IV",
                5,
                "",
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                1,
                9,
                true,
                0,
                1,
                "ArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                    'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));

        ItemList.Machine_LV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                661,
                "basicmachine.plasmaarcfurnace.tier.01",
                "Basic Plasma Arc Furnace",
                1,
                "",
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "PlasmaArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));
        ItemList.Machine_MV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                662,
                "basicmachine.plasmaarcfurnace.tier.02",
                "Advanced Plasma Arc Furnace",
                2,
                "",
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "PlasmaArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));
        ItemList.Machine_HV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                663,
                "basicmachine.plasmaarcfurnace.tier.03",
                "Advanced Plasma Arc Furnace II",
                3,
                "",
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                1,
                4,
                true,
                0,
                1,
                "PlasmaArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));
        ItemList.Machine_EV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                664,
                "basicmachine.plasmaarcfurnace.tier.04",
                "Advanced Plasma Arc Furnace III",
                4,
                "",
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                1,
                9,
                true,
                0,
                1,
                "PlasmaArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));
        ItemList.Machine_IV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                665,
                "basicmachine.plasmaarcfurnace.tier.05",
                "Advanced Plasma Arc Furnace IV",
                5,
                "",
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                1,
                9,
                true,
                0,
                1,
                "PlasmaArcFurnace.png",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite) })
                        .getStackForm(1L));

        ItemList.Machine_LV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                671,
                "basicmachine.e_oven.tier.01",
                "Basic Electric Oven",
                1,
                "Just a Furnace with a different Design",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Oven.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Oven")
                        .getStackForm(1L));
        ItemList.Machine_MV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                672,
                "basicmachine.e_oven.tier.02",
                "Advanced Electric Oven",
                2,
                "Just a Furnace with a different Design",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Oven.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Oven")
                        .getStackForm(1L));
        ItemList.Machine_HV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                673,
                "basicmachine.e_oven.tier.03",
                "Advanced Electric Oven II",
                3,
                "Just a Furnace with a different Design",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Oven.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Oven")
                        .getStackForm(1L));
        ItemList.Machine_EV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                674,
                "basicmachine.e_oven.tier.04",
                "Advanced Electric Oven III",
                4,
                "Just a Furnace with a different Design",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Oven.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Oven")
                        .getStackForm(1L));
        ItemList.Machine_IV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                675,
                "basicmachine.e_oven.tier.05",
                "Advanced Electric Oven IV",
                5,
                "Just a Furnace with a different Design",
                GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                1,
                1,
                false,
                0,
                1,
                "E_Oven.png",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                false,
                false,
                SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING }).setProgressBarTextureName("E_Oven")
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Miner.get(1L),
            bitsd,
            new Object[] { "EEE", aTextWireHull, "CSC", 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'S',
                ItemList.Sensor_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Miner.get(1L),
            bitsd,
            new Object[] { "PEP", aTextWireHull, "CSC", 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'P',
                ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt02.get(Materials.Copper), 'S', ItemList.Sensor_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Miner.get(1L),
            bitsd,
            new Object[] { "RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'P',
                ItemList.Electric_Piston_HV, 'R', ItemList.Robot_Arm_HV, 'C',
                OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.Gold), 'S',
                ItemList.Sensor_HV });
    }

    private static void run3() {
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_BlastFurnace.get(1L),
            bitsd,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_VacuumFreezer.get(1L),
            bitsd,
            new Object[] { aTextPlate, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_FrostProof, 'P',
                ItemList.Electric_Pump_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
            bitsd,
            new Object[] { "OOO", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_SolidSteel, 'O',
                Ic2Items.reinforcedStone, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Furnace.get(1L),
            bitsd,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.AnnealedCopper) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Bronze.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Bronze, 'C',
                OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Steel.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Steel, 'C',
                OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Titanium.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Titanium, 'C',
                OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_TungstenSteel.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_TungstenSteel,
                'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_LV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_MV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', OrePrefixes.gearGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_HV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_LV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_MV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_HV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_EV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_EV, 'E', ItemList.Electric_Motor_EV, 'R',
                OrePrefixes.rotor.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_IV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_IV, 'E', ItemList.Electric_Motor_IV, 'R',
                OrePrefixes.rotor.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_LV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'P', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_MV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_HV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'P', OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });

        if (!Thaumcraft.isModLoaded()) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_LV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.Advanced), 'T', ItemList.Field_Generator_LV, 'F',
                    OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_MV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.Data), 'T', ItemList.Field_Generator_MV, 'F',
                    OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_HV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.Elite), 'T', ItemList.Field_Generator_HV, 'F',
                    OrePrefixes.plate.get(Materials.Neutronium) });

            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_LV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B',
                    ItemList.MagicEnergyConverter_LV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'T',
                    ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_MV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Data), 'T',
                    ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_HV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Elite), 'T',
                    ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Europium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_EV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_HV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Master), 'T',
                    ItemList.Field_Generator_EV, 'F', OrePrefixes.plate.get(Materials.Neutronium) });
        }
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Fusion_Coil.get(1L),
            bitsd,
            new Object[] { "CTC", "FMF", "CTC", 'M', ItemList.Casing_Coil_Superconductor, 'C',
                OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_MV, 'T',
                ItemList.Neutron_Reflector });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_IV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'U',
                OrePrefixes.stick.get(Materials.Plutonium241) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_LuV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.wireGt04.get(Materials.VanadiumGallium),
                'U', OrePrefixes.stick.get(Materials.Europium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_ZPMV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.wireGt04.get(Materials.Naquadah), 'U',
                OrePrefixes.stick.get(Materials.Americium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Processing_Array.get(1L),
            bitsd,
            new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'F',
                ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb });

        GT_ProcessingArrayRecipeLoader.registerDefaultGregtechMaps();
        GT_ModHandler.addCraftingRecipe(
            ItemList.Distillation_Tower.get(1L),
            bitsd,
            new Object[] { "CBC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Data), 'F',
                ItemList.Electric_Pump_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeSteamTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeGasTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Data), 'P',
                OrePrefixes.gearGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeAdvancedGasTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Master), 'P',
                OrePrefixes.gearGt.get(Materials.HSSG) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_LV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Basic), 'P',
                ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_MV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_MV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Good), 'P',
                ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_HV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Advanced),
                'P', ItemList.Electric_Pump_HV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_EV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Data), 'P',
                ItemList.Electric_Pump_EV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_IV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'P',
                ItemList.Electric_Pump_IV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_LV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LV, 'E', ItemList.Emitter_LV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_MV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_MV, 'E', ItemList.Emitter_MV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_HV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_HV, 'E', ItemList.Emitter_HV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_EV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_EV, 'E', ItemList.Emitter_EV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_IV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_IV, 'E', ItemList.Emitter_IV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_LuV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LuV, 'E', ItemList.Emitter_LuV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_ZPM.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_ZPM, 'E', ItemList.Emitter_ZPM.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_UV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_UV, 'E', ItemList.Emitter_UV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.SuperconductorUHV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_HeatExchanger.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Pipe_Titanium, 'C',
                OrePrefixes.pipeMedium.get(Materials.Titanium), 'W', ItemList.Electric_Pump_EV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Charcoal_Pile.get(1L),
            bitsd,
            new Object[] { "EXE", "EME", "hCw", 'M', ItemList.Hull_HP_Bricks, 'E',
                OrePrefixes.plate.get(Materials.AnyBronze), 'C', new ItemStack(Items.flint_and_steel, 1), 'X',
                OrePrefixes.rotor.get(Materials.Steel), });

        // Converter recipes in case you had old one lying around
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_LV.get(1L),
            bits,
            new Object[] { ItemList.Seismic_Prospector_LV });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_MV.get(1L),
            bits,
            new Object[] { ItemList.Seismic_Prospector_MV });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_HV.get(1L),
            bits,
            new Object[] { ItemList.Seismic_Prospector_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_LV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.plateDouble.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Basic), 'C',
                ItemList.Sensor_LV, 'X', OrePrefixes.cableGt02.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_MV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.plateDouble.get(Materials.BlackSteel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C',
                ItemList.Sensor_MV, 'X', OrePrefixes.cableGt02.get(Materials.Copper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_HV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.plateDouble.get(Materials.StainlessSteel), 'E', OrePrefixes.circuit.get(Materials.Advanced),
                'C', ItemList.Sensor_HV, 'X', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_EV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.plateDouble.get(Materials.VanadiumSteel), 'E', OrePrefixes.circuit.get(Materials.Data), 'C',
                ItemList.Sensor_EV, 'X', OrePrefixes.cableGt04.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller1.get(1L),
            bitsd,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.frameGt.get(Materials.Steel),
                'E', OrePrefixes.circuit.get(Materials.Good), 'C', ItemList.Electric_Motor_MV, 'P',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'Q', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller2.get(1L),
            bitsd,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.ConcreteBackfiller1, 'W',
                OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.Data), 'C',
                ItemList.Electric_Motor_EV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q',
                ItemList.Electric_Pump_EV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.PyrolyseOven.get(1L),
            bitsd,
            new Object[] { "WEP", "EME", "WCP", 'M', ItemList.Hull_MV, 'W', ItemList.Electric_Piston_MV, 'P',
                OrePrefixes.wireGt04.get(Materials.Cupronickel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C',
                ItemList.Electric_Pump_MV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.OilCracker.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_HV, 'W',
                ItemList.Casing_Coil_Cupronickel, 'E', OrePrefixes.circuit.get(Materials.Advanced), 'C',
                ItemList.Electric_Pump_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_HV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_HV, 'B', ItemList.Battery_RE_HV_Lithium,
                'C', ItemList.Emitter_HV, 'G', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                ItemList.Field_Generator_HV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_EV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_EV, 'B',
                GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W), 'C', ItemList.Emitter_EV, 'G',
                OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Field_Generator_EV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_IV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_IV, 'B', ItemList.Energy_LapotronicOrb, 'C',
                ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Field_Generator_IV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_LUV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B', ItemList.Energy_LapotronicOrb2,
                'C', ItemList.Emitter_LuV, 'G', OrePrefixes.circuit.get(Materials.Master), 'P',
                ItemList.Field_Generator_LuV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_ZPM.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_ZPM, 'B',
                GregTech_API.sOPStuff.get(ConfigCategories.Recipes.gregtechrecipes, "EnableZPMandUVBatteries", false)
                    ? ItemList.Energy_Module
                    : ItemList.ZPM2,
                'C', ItemList.Emitter_ZPM, 'G', OrePrefixes.circuit.get(Materials.Ultimate), 'P',
                ItemList.Field_Generator_ZPM });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_UV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_UV, 'B',
                GregTech_API.sOPStuff.get(ConfigCategories.Recipes.gregtechrecipes, "EnableZPMandUVBatteries", false)
                    ? ItemList.Energy_Module
                    : ItemList.ZPM3,
                'C', ItemList.Emitter_UV, 'G', OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'P',
                ItemList.Field_Generator_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Assemblyline.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_IV, 'W', ItemList.Casing_Assembler,
                'E', OrePrefixes.circuit.get(Materials.Elite), 'C', ItemList.Robot_Arm_IV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_DieselEngine.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.TungstenSteel), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_EngineIntake.get(4L),
            bitsd,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.Titanium), 'F',
                ItemList.Casing_StableTitanium, 'P', OrePrefixes.rotor.get(Materials.Titanium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ExtremeDieselEngine.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                OrePrefixes.cableGt01.get(Materials.HSSG), 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ExtremeEngineIntake.get(4L),
            bitsd,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                'F', ItemList.Casing_RobustTungstenSteel, 'P', OrePrefixes.rotor.get(Materials.TungstenSteel) });

        // If Cleanroom is enabled, add a recipe, else hide from NEI.
        if (GT_Mod.gregtechproxy.mEnableCleanroom) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_Cleanroom.get(1L),
                bitsd,
                new Object[] { "FFF", "RHR", "MCM", 'H', ItemList.Hull_HV, 'F', ItemList.Component_Filter, 'R',
                    OrePrefixes.rotor.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                    OrePrefixes.circuit.get(Materials.Advanced) });
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1L),
                    ItemList.Component_Filter.get(2L),
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1L),
                    ItemList.Electric_Motor_HV.get(1L),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Machine_Multi_Cleanroom.get(1L))
                .fluidInputs(Materials.StainlessSteel.getMolten(864L))
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);
        } else {
            if (NotEnoughItems.isModLoaded()) {
                API.hideItem(ItemList.Machine_Multi_Cleanroom.get(1L));
            }
        }

        ItemList.Machine_LV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1180,
                "basicmachine.circuitassembler.tier.01",
                "Basic Circuit Assembler",
                1,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_MV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1181,
                "basicmachine.circuitassembler.tier.02",
                "Advanced Circuit Assembler",
                2,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_HV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1182,
                "basicmachine.circuitassembler.tier.03",
                "Advanced Circuit Assembler II",
                3,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_EV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1183,
                "basicmachine.circuitassembler.tier.04",
                "Advanced Circuit Assembler III",
                4,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_IV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1184,
                "basicmachine.circuitassembler.tier.05",
                "Advanced Circuit Assembler IV",
                5,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_LuV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1185,
                "basicmachine.circuitassembler.tier.06",
                "Advanced Circuit Assembler V",
                6,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_ZPM_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1186,
                "basicmachine.circuitassembler.tier.07",
                "Advanced Circuit Assembler VI",
                7,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_UV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                1187,
                "basicmachine.circuitassembler.tier.08",
                "Advanced Circuit Assembler VII",
                8,
                "Pick-n-Place all over the place",
                GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                6,
                1,
                true,
                0,
                1,
                "CircuitAssembler.png",
                SoundResource.NONE,
                false,
                false,
                SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                    GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_LuV, 'L', ItemList.Energy_LapotronicOrb, 'T',
                ItemList.Transformer_ZPM_LuV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_ZPM, 'L', ItemList.Energy_LapotronicOrb2, 'T',
                ItemList.Transformer_UV_ZPM });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_UV, 'L', ItemList.ZPM2, 'T',
                ItemList.Transformer_MAX_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeChemicalReactor.get(1L),
            bitsd,
            new Object[] { "CRC", "PMP", "CBC", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'R',
                OrePrefixes.rotor.get(Materials.StainlessSteel), 'P',
                OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene), 'M', ItemList.Electric_Motor_HV, 'B',
                ItemList.Hull_HV });
        GT_PCBFactoryMaterialLoader.load();
    }

    private static void run4() {
        long bits = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;

        boolean bEC = !GT_Mod.gregtechproxy.mHardcoreCables;

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getIC2Item("copperCableItem", 2L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.AnyCopper) });
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getIC2Item("goldCableItem", 4L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.Gold) });
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getIC2Item("ironCableItem", 3L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.AnyIron) });
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getIC2Item("tinCableItem", 3L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.Tin) });
        }

        // high pressure fluid pipes
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_EV.get(1L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(1L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(4096)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(2L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sAssemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ULV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Primitive) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_MV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_HV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_EV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_IV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LuV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ZPM.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_UV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.SuperconductorUHV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_MAX.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Infinite) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ULV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_HV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_EV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_IV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LuV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_UV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MAX.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MAX.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ULV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_HV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_EV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_IV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LuV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ZPM.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_UV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MAX.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataOrb });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataStick });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ULV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_HV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_EV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_IV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LuV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ZPM.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_UV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MAX.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_MAX.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.Basic) });
    }

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Recipes for MetaTileEntities.");
        run1();
        run2();
        run3();
        run4();
    }
}
