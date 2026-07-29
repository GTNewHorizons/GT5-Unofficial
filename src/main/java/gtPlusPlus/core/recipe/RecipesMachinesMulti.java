package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gregtech.common.tileentities.machines.multi.MTEIndustrialMacerator;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipesMachinesMulti {

    public static void loadRecipes() {
        advHeatExchanger();
        distillus();
        cryoFreezer();
        volcanus();
        steamMultis();

        multiArcFurnace();
        multiDehydrator();
        multiAlloySmelter();
        multiRockBreaker();
        multiFluidHeater();
        multiMassFabricator();
        multiForgeHammer();
        multiReplicator();
        multiChisel();
        multiCentrifuge();
        multiCokeOven();
        multiElectrolyzer();
        multiBender();
        multiMacerator();
        multiWiremill();
        multiSifter();
        multiThermalCentrifuge();
        multiWasher();
        multiCutter();
        multiExtruder();
        multiImplo();
        multiPackager();
        multiUseCasing();
        multiAssembler();
    }

    private static void multiFluidHeater() {
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_IndustrialFluidHeater.get(1),
            BITS,
            new Object[] { "PCP", "IHI", "PDP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.plate, 1), 'C', "circuitElite",
                'I', "pipeHugeTantalloy60", 'H', ItemList.Machine_IV_FluidHeater.get(1), 'D', "circuitData" });
    }

    private static void advHeatExchanger() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_Multi_HeatExchanger.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plateDouble, 8),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.screw, 16),
                Circuits.IV.get(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.XL_HeatExchanger.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_XL_HeatExchanger.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void multiForgeHammer() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(2),
                ItemList.Machine_IV_Hammer.get(1),
                MaterialLibAPI.getStack(Materials2Materials.IncoloyDS, Materials2Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials2Materials.EnergyCrystal, Materials2Shapes.bolt, 32),
                MaterialLibAPI.getStack(Materials2Materials.Zirconium, Materials2Shapes.wireFine, 32),
                Circuits.IV.get(4))
            .itemOutputs(ItemList.IndustrialForgeHammer.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.IncoloyDS, 12 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_IndustrialForgeHammer.get(1),
            BITS,
            new Object[] { "IBI", "HCH", "IHI", 'I',
                MaterialLibAPI.getStack(Materials2Materials.IncoloyDS, Materials2Shapes.plate, 1), 'B',
                MaterialLibAPI.getStack(Materials2Materials.BabbitAlloy, Materials2Shapes.plate, 1), 'C',
                ItemList.Casing_HeatProof.get(1), 'H',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyX, Materials2Shapes.stick, 1) });
    }

    private static void multiReplicator() {
        // Elemental Duplicator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_Replicator.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Hull_ZPM.get(4),
                ItemList.Field_Generator_IV.get(16),
                ItemList.Electric_Motor_ZPM.get(16),
                ItemList.Electric_Piston_ZPM.get(4),
                GregtechItemList.Energy_Core_LuV.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.plate, 16),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.screw, 32),
                MaterialLibAPI.getStack(Materials2Materials.TriniumNaquadahCarbonite, Materials2Shapes.bolt, 32),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.stick, 10),
                new Object[] { "circuitUltimate", 20 },
                ItemList.Tool_DataOrb.get(32),
                GregtechItemList.Laser_Lens_Special.get(1))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.Pikyonium64B, 32 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.LafiumCompound, 16 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.TriniumNaquadahCarbonite, 16 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BabbitAlloy, 128 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Data Orb Repository
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Modulator_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GregtechItemList.GTPP_Casing_ZPM.get(2),
                ItemList.Field_Generator_EV.get(4),
                GregtechItemList.Energy_Core_EV.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials2Materials.TriniumNaquadahCarbonite, Materials2Shapes.bolt, 16),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.stick, 16),
                new Object[] { "circuitMaster", 32 },
                ItemList.Tool_DataOrb.get(32))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.Zeron100, 16 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.Arcanite, 8 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.EnergyCrystal, 8 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BabbitAlloy, 64 * INGOTS))
            .itemOutputs(GregtechItemList.Hatch_Input_Elemental_Duplicator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Elemental Confinement Shell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.ResonanceChamber_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Hull_LuV.get(5),
                ItemList.Field_Generator_HV.get(16),
                GregtechItemList.Energy_Core_MV.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.screw, 4),
                MaterialLibAPI.getStack(Materials2Materials.TriniumNaquadahCarbonite, Materials2Shapes.bolt, 8),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.stick, 4),
                new Object[] { "circuitElite", 4 },
                ItemList.Tool_DataStick.get(4))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.Inconel625, 16 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.Inconel792, 8 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.HastelloyN, 8 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BabbitAlloy, 16 * INGOTS))
            .itemOutputs(GregtechItemList.Casing_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    private static void multiAlloySmelter() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                ItemList.Machine_IV_AlloySmelter.get(1),
                MaterialLibAPI.getStack(Materials2Materials.TantalumCarbide, Materials2Shapes.gearGt, 16),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.bolt, (int) (64)),
                MaterialLibAPI.getStack(Materials2Materials.IncoloyDS, Materials2Shapes.plate, 16))
            .circuit(6)
            .itemOutputs(GregtechItemList.Industrial_AlloySmelter.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.Inconel792, 8 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void distillus() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Distillation_Tower.get(2),
                GregtechItemList.GTPP_Casing_IV.get(16),
                Circuits.LuV.get(8))
            .itemOutputs(GregtechItemList.Machine_Adv_DistillationTower.get(1))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.WatertightSteel, 32 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.BabbitAlloy, 16 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bronze, Materials2FluidShapes.fluidMolten, (int) (64 * INGOTS)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Kanthal, Materials2FluidShapes.fluidMolten, (int) (16 * INGOTS)))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
    }

    private static void steamMultis() {
        // Steam Grinder
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamMaceratorMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CDC", "PFP", "CDC", 'C', ItemList.Casing_BronzePlatedBricks, 'D', "gemDiamond", 'P',
                OreDictNames.craftingPiston, 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        // Steam Purifier
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamWasherMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "RFR", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.ingredient(Materials2Materials.CastIron), 'R',
                OrePrefixes.rotor.ingredient(Materials2Materials.Tin), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1), });

        // Steam Blender
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamMixerMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CRC", "OFO", "CRC", 'C', ItemList.Casing_BronzePlatedBricks, 'R',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.ring, 1), 'O',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.rotor, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        // Water Pump
        GTModHandler.addCraftingRecipe(
            GregtechItemList.WaterPump.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "FFF", "FGF", "CCC", 'F', OrePrefixes.frameGt.ingredient(Materials2Materials.Bronze), 'G',
                OrePrefixes.gearGt.ingredient(Materials2Materials.Bronze), 'C', ItemList.WoodenCasing });

        // Steam Separator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamCentrifugeMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "GFG", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.ingredient(Materials2Materials.CastIron), 'G',
                OrePrefixes.gearGt.ingredient(Materials2Materials.Bronze), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        // Steam Presser
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamForgeHammerMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "PAP", "CFC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.ingredient(Materials2Materials.CastIron), 'A', OreDictNames.craftingAnvil, 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        // Steam Squasher
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamCompressorMulti.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "GFG", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OreDictNames.craftingPiston, 'G',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.gearGt, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        if (EtFuturumRequiem.isModLoaded()) {
            // Steam Fuser
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamAlloySmelterMulti.get(1),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "BTB", "FUF", "BLB", 'B', ItemList.Casing_BronzePlatedBricks.get(1L), 'T',
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials2Materials.Bronze, 1L), 'F',
                    getModItem(EtFuturumRequiem.ID, "blast_furnace", 1, 0), 'U',
                    MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1), 'L',
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials2Materials.Bronze, 1L) });

            // Steam Hearth
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamFurnaceMulti.get(1),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "RGR", "YBZ", "WFW", 'R', OrePrefixes.plateDouble.ingredient(Materials2Materials.Bronze),
                    'G', MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.gearGt, 1), 'Y',
                    getModItem(EtFuturumRequiem.ID, "blast_furnace", 1, 0), 'B', ItemList.Machine_HP_Furnace, 'Z',
                    getModItem(EtFuturumRequiem.ID, "smoker", 1, 0), 'W',
                    OrePrefixes.plateDouble.ingredient(Materials2Materials.CastIron), 'F',
                    MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });
        }

        // Steam Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Steam.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PBP", "PTP", "PBP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Bronze), 'B',
                OrePrefixes.pipeMedium.ingredient(Materials2Materials.Bronze), 'T',
                GregtechItemList.GTFluidTank_ULV.get(1) });

        // Steam Input Bus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Bus_Steam.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "BTB", "SHS", "BTB", 'B', OrePrefixes.plate.ingredient(Materials2Materials.Bronze), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.plate, 1), 'S',
                OrePrefixes.plate.ingredient(Materials2Materials.Tin), 'H', new ItemStack(Blocks.hopper) });

        // Steam Output Bus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Output_Bus_Steam.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "BSB", "THT", "BSB", 'B', OrePrefixes.plate.ingredient(Materials2Materials.Bronze), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.plate, 1), 'S',
                OrePrefixes.plate.ingredient(Materials2Materials.Tin), 'H', new ItemStack(Blocks.hopper) });
    }

    public static void multiCentrifuge() {

        // Centrifuge Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Centrifuge1.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "ABA", "CBC", "ABA", 'A',
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.plate, 1), 'B',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.stick, 1), 'C',
                MaterialLibAPI.getStack(Materials2Materials.Inconel792, Materials2Shapes.plate, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Inconel792, Materials2Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.stick, 3))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Centrifuge1.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiCokeOven() {
        // Industrial Coke Oven
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialCokeOven.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "HOH", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy61, Materials2Shapes.plate, 1), 'C', "circuitData",
                'H', ItemList.Casing_EV, 'O', ItemList.CokeOvenController });

        // Structural Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PRP", "RFR", "PRP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy61, Materials2Shapes.plate, 1), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy61, Materials2Shapes.stick, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tantalloy61, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy61, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy61, Materials2Shapes.stick, 4),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tantalloy61, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_CokeOven.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Heat Resistant Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven_Coil1.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "FCF", "PPP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Bronze), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.TPVAlloy), 'C', ItemList.Casing_Gearbox_Bronze });

        // Heat Proof Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven_Coil2.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "FCF", "PPP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Steel), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.HSSS), 'C', ItemList.Casing_Gearbox_Steel });
    }

    private static void multiElectrolyzer() {
        // Electrolyzer Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Electrolyzer.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "RFR", "PRP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Potin, Materials2Shapes.plate, 1), 'C',
                OrePrefixes.stickLong.ingredient(Materials2Materials.Chrome), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Potin, Materials2Shapes.stickLong, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Potin, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Potin, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Potin, Materials2Shapes.stickLong, 3),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.stickLong, (int) (1)),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Potin, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Electrolyzer.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiBender() {
        // Material Press Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MaterialPress.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PBP", "TFT", "PBP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Titanium), 'B',
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.stickLong, 1), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy60, Materials2Shapes.stick, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.plate, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy60, Materials2Shapes.stick, 2),
                MaterialLibAPI.getStack(Materials2Materials.Tumbaga, Materials2Shapes.stickLong, 2),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MaterialPress.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Bending Machine
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialBendingMachine.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PGP", "MFM", "PRP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Titanium), 'G',
                OrePrefixes.gearGt.ingredient(Materials2Materials.Titanium), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy60, Materials2Shapes.gearGt, 1), 'M',
                OrePrefixes.stick.ingredient(Materials2Materials.Titanium), 'F', ItemList.Machine_EV_Bender });

        // Industrial Forming Press
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialFormingPress.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PGP", "MFM", "PRP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Titanium), 'G',
                OrePrefixes.gearGt.ingredient(Materials2Materials.Titanium), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Tantalloy60, Materials2Shapes.gearGt, 1), 'M',
                OrePrefixes.stick.ingredient(Materials2Materials.Titanium), 'F', ItemList.Machine_EV_Press });
    }

    private static void multiMacerator() {
        // Maceration Stack Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MacerationStack.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "RFR", "PXP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Palladium), 'R',
                OrePrefixes.stick.ingredient(Materials2Materials.Platinum), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Inconel625, 1), 'X',
                OrePrefixes.stickLong.ingredient(Materials2Materials.Palladium) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.plate, (int) (5)),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.stick, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.stickLong, (int) (1)),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Inconel625, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MacerationStack.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Maceration Stack
        GTModHandler.addCraftingRecipe(
            ItemList.MacerationStack.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PMP", "MCM", "PMP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Titanium), 'M',
                ItemList.Machine_EV_Macerator, 'C', "circuitData" });

        // Maceration Upgrade Chip
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Maceration_Upgrade_Chip.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PMP", "MCM", "PMP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.TungstenCarbide),
                'M', ItemList.Machine_IV_Macerator, 'C', "circuitUltimate" });

        // Maceration Stack T2 Shapeless Craft
        ItemStack t2MacerationStack = ItemList.MacerationStack.get(1);
        NBTTagCompound upgradeTag = new NBTTagCompound();
        upgradeTag.setByte(MTEIndustrialMacerator.TIER, (byte) 2);
        t2MacerationStack.setTagCompound(upgradeTag);

        GTModHandler.addShapelessCraftingRecipe(
            t2MacerationStack,
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.OVERWRITE_NBT,
            grid -> {
                for (int i = 0; i < grid.getSizeInventory(); i++) {
                    ItemStack stack = grid.getStackInSlot(i);
                    if (!ItemList.MacerationStack.isStackEqual(stack, false, true)) continue;
                    if (stack.hasTagCompound() && stack.getTagCompound()
                        .getByte(MTEIndustrialMacerator.TIER) >= 2) return false;
                }
                return true;
            },
            new Object[] { ItemList.MacerationStack, GregtechItemList.Maceration_Upgrade_Chip });
    }

    private static void multiWiremill() {
        // Wire Factory Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_WireFactory.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PRP", "RFR", "PRP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.BlueSteel), 'R',
                OrePrefixes.stick.ingredient(Materials2Materials.BlueSteel), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.BlueSteel), });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.plate, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.stick, (int) (4)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.BlueSteel, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_WireFactory.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

    }

    private static void multiMassFabricator() {
        // Matter Fabricator CPU
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_MassFab.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "WHW", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.AdvancedNitinol, Materials2Shapes.plate, 1), 'C',
                "circuitSuperconductor", 'W', OrePrefixes.cableGt04.ingredient(Materials2Materials.NaquadahAlloy), 'H',
                ItemList.Casing_UV });

        // Matter Fabricator Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MatterFab.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PRP", "RFR", "PRP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.NiobiumCarbide, Materials2Shapes.plate, 1), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Inconel792, Materials2Shapes.stick, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Inconel690, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NiobiumCarbide, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Inconel792, Materials2Shapes.stick, 4),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Inconel690, 1))
            .itemOutputs(GregtechItemList.Casing_MatterFab.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Matter Generation Coil
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MatterGen.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PRP", "FHF", "PRP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plate, 1), 'R',
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.plate, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Stellite, 1), 'H',
                ItemList.Casing_UV });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_UV.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Pikyonium64B, Materials2Shapes.plate, 2),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Stellite, 2))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MatterGen.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Research on Containment Fields
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Field_Generator_LuV.get(1), ItemList.Emitter_ZPM.get(2))
            .itemOutputs(
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserEngraverRecipes);

        // Containment Casing
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Field_Generator_IV.get(32),
                ItemList.Electric_Motor_EV.get(64),
                ItemList.Energy_LapotronicOrb.get(32),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials2Materials.YttriumBariumCuprate, 32),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Materials.Platinum, 64),
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.plate, (int) (64)),
                MaterialLibAPI.getStack(Materials2Materials.Gadolinium, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials2Materials.Samarium, Materials2Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials2Materials.Arcanite, Materials2Shapes.gearGt, 8),
                new Object[] { "circuitElite", 64 },
                new Object[] { "circuitMaster", 32 },
                new Object[] { "circuitUltimate", 16 },
                GregtechItemList.Laser_Lens_Special.get(1),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials2Materials.Nitinol60, 36 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.EnergyCrystal, 1 * STACKS + 8 * INGOTS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.Tumbaga, 4 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Nichrome,
                    Materials2FluidShapes.fluidMolten,
                    (int) (16 * INGOTS)))
            .itemOutputs(new ItemStack(ModBlocks.blockCasings3Misc, 32, 15))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * MINUTES)
            .addTo(AssemblyLine);
    }

    private static void multiSifter() {
        // Large Sifter Control Block
        GTModHandler.addCraftingRecipe(
            ItemList.LargeSifter.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "WMW", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.EglinSteel, Materials2Shapes.plate, 1), 'C',
                "circuitAdvanced", 'W', OrePrefixes.cableGt04.ingredient(Materials2Materials.Gold), 'M',
                ItemList.Machine_HV_Sifter });

        // Industrial Sieve Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Sifter.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "PFP", "PPP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.EglinSteel, Materials2Shapes.plate, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.EglinSteel, Materials2Shapes.plate, 8),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Tumbaga, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Sifter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Sieve Grate
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_SifterGrate.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "FWF", "WWW", "FWF", 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.EglinSteel, 1), 'W',
                OrePrefixes.wireFine.ingredient(Materials2Materials.Steel) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.wireFine, (int) (5)),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.EglinSteel, 4))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_SifterGrate.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiThermalCentrifuge() {
        // Thermal Processing Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_ThermalCentrifuge.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.RedSteel), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.BlackSteel) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RedSteel, Materials2Shapes.plate, (int) (6)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.BlackSteel, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_ThermalCentrifuge.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Large Thermal Refinery
        GTModHandler.addCraftingRecipe(
            ItemList.LargeThermalRefinery.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "RMR", "PGP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.RedSteel), 'C',
                "circuitData", 'R', MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.stick, 1),
                'M', ItemList.Machine_EV_ThermalCentrifuge, 'G',
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.gearGt, 1) });
    }

    private static void multiWasher() {
        // Wash Plant Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_WashPlant.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PhP", "TFT", "PwP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plate, 1), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Grisium, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 2),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Grisium, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_WashPlant.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Ore Washing Plant
        GTModHandler.addCraftingRecipe(
            ItemList.OreWashingPlant.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "TCT", "PAP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plate, 1), 'A',
                ItemList.Machine_EV_OreWasher, 'T',
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 1), 'C', "circuitData" });

        // Industrial Chemical Plant
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialChemicalBath.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "TCT", "PBP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plate, 1), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 1), 'C', "circuitData",
                'B', ItemList.Machine_EV_ChemicalBath });
    }

    private static void multiCutter() {
        // Cutting Factory Frame
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CuttingFactoryFrame.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PhP", "SFS", "PwP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.TungstenTitaniumCarbide, Materials2Shapes.plate, 1), 'S',
                MaterialLibAPI.getStack(Materials2Materials.Stellite, Materials2Shapes.plate, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.TantalumCarbide, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenTitaniumCarbide, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Stellite, Materials2Shapes.plate, 2),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.TantalumCarbide, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_CuttingFactoryFrame.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Cutting Factory
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialCuttingMachine.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "WMW", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.TungstenTitaniumCarbide, Materials2Shapes.plate, 1), 'C',
                "circuitData", 'W', OrePrefixes.wireFine.ingredient(Materials2Materials.Platinum), 'M',
                ItemList.Machine_IV_Cutter });
    }

    private static void multiExtruder() {
        // Inconel Reinforced Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Extruder.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PhP", "TFT", "PwP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Inconel690, Materials2Shapes.plate, 1), 'T',
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Staballoy, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Inconel690, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Talonite, Materials2Shapes.plate, 2),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Staballoy, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Extruder.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Extrusion Machine
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialExtruder.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IMI", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Inconel690, Materials2Shapes.plate, 1), 'C', "circuitElite",
                'I', ItemList.Electric_Piston_IV, 'M', ItemList.Machine_IV_Extruder });
    }

    private static void cryoFreezer() {
        // Advanced Cryogenic Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_AdvancedVacuum.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PGP", "AFB", "PGP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plateDouble, 1), 'G',
                MaterialLibAPI.getStack(Materials2Materials.IncoloyMA956, Materials2Shapes.gearGt, 1), 'A',
                ItemList.Reactor_Coolant_He_6, 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.Nitinol60, 1), 'B',
                ItemList.Reactor_Coolant_NaK_6.get(1) });

        // Cryogenic Freezer
        GTModHandler.addCraftingRecipe(
            ItemList.CryogenicFreezer.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "GCG", "PXP", "DOD", 'G',
                MaterialLibAPI.getStack(Materials2Materials.IncoloyMA956, Materials2Shapes.gearGt, 1), 'C',
                "circuitMaster", 'P', ItemList.Electric_Piston_IV, 'X', GregtechItemList.Casing_AdvancedVacuum, 'D',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.plateDouble, 1), 'O',
                GregtechItemList.Gregtech_Computer_Cube });

        // Cryotheum Cooling Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Cryotheum.get(1L),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "MGM", "CBC", "PHP", 'M',
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.plate, 1), 'G',
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.gearGt, 1), 'C',
                "circuitData", 'B', GregtechItemList.Casing_AdvancedVacuum.get(1), 'P',
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plate, (int) (1)), 'H',
                ItemList.Hatch_Input_IV.get(1) });
    }

    private static void volcanus() {
        // Volcanus Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Adv_BlastFurnace.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PAP", "BFC", "PGP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyN, Materials2Shapes.plateDouble, 1), 'A',
                getModItem(Mods.IndustrialCraft2.ID, "reactorHeatSwitchDiamond", 1, 1), 'B',
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentGold", 1, 1), 'C',
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentDiamond", 1, 1), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.HastelloyX, 1), 'G',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyW, Materials2Shapes.gearGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.HastelloyX, 1),
                MaterialLibAPI.getStack(Materials2Materials.HastelloyN, Materials2Shapes.plateDouble, 4),
                MaterialLibAPI.getStack(Materials2Materials.HastelloyW, Materials2Shapes.gearGt, 1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorHeatSwitchDiamond", 1, 1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentGold", 1, 1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentDiamond", 1, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Adv_BlastFurnace.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Volcanus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Machine_Adv_BlastFurnace.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "GCG", "RXR", "PZP", 'G',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyW, Materials2Shapes.gearGt, 1), 'C',
                "circuitMaster", 'R', ItemList.Robot_Arm_IV, 'X', GregtechItemList.Casing_Adv_BlastFurnace, 'P',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyN, Materials2Shapes.plateDouble, 1), 'Z',
                GregtechItemList.Gregtech_Computer_Cube });

        // Pyrotheum Heating Vent
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Pyrotheum.get(1L),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "MGM", "CBC", "MHM", 'M',
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.plate, 1), 'G',
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel300, Materials2Shapes.gearGt, 1), 'C',
                "circuitElite", 'B', GregtechItemList.Casing_Adv_BlastFurnace.get(1), 'H',
                ItemList.Hatch_Input_IV.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_IV.get(1),
                GregtechItemList.Casing_Adv_BlastFurnace.get(1),
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel250, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.MaragingSteel300, Materials2Shapes.gearGt, 1),
                Circuits.IV.get(2))
            .circuit(1)
            .itemOutputs(GregtechItemList.Hatch_Input_Pyrotheum.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiImplo() {
        // Density^2
        GTModHandler.addCraftingRecipe(
            ItemList.AdvancedImplosionCompressor.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "GCG", "FHR", "IXI", 'G',
                MaterialLibAPI.getStack(Materials2Materials.Grisium, Materials2Shapes.gearGt, 1), 'C', "circuitMaster",
                'F', ItemList.Field_Generator_IV, 'H', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_IV, 'I',
                "plateAlloyIridium", 'X', GregtechItemList.Gregtech_Computer_Cube });
    }

    private static void multiPackager() {
        // Supply Depot Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_AmazonWarehouse.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PMP", "wFh", "PCP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.HastelloyC276, Materials2Shapes.plateDouble, 1), 'M',
                ItemList.Electric_Motor_HV, 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.TungstenCarbide, 1), 'C',
                ItemList.Conveyor_Module_HV, });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.TungstenCarbide, 1),
                MaterialLibAPI.getStack(Materials2Materials.HastelloyC276, Materials2Shapes.plateDouble, 4),
                ItemList.Electric_Motor_HV.get(1),
                ItemList.Conveyor_Module_HV.get(1))
            .itemOutputs(GregtechItemList.Casing_AmazonWarehouse.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

    }

    private static void multiUseCasing() {
        // Multi-Use Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Multi_Use.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PhP", "SFS", "PwP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Staballoy, Materials2Shapes.plate, 1), 'S',
                OrePrefixes.plate.ingredient(Materials2Materials.StainlessSteel), 'F',
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.ZirconiumCarbide, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Staballoy, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plate, (int) (2)),
                MaterialParts.stack(Materials2PipeShapes.frameGt, Materials2Materials.ZirconiumCarbide, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Multi_Use.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiArcFurnace() {
        // Tempered Arc Furnace Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                GregtechItemList.TransmissionComponent_MV.get(2),
                ItemList.Electric_Piston_EV.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials2Materials.TungstenSteel, 1))
            .itemOutputs(GregtechItemList.Casing_Industrial_Arc_Furnace.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.Arcanite, 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

    }

    private static void multiDehydrator() {
        // Vacuum Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Casing_Coil_Nichrome.get(1),
                ItemList.Electric_Piston_HV.get(2),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.gearGt, 2))
            .itemOutputs(GregtechItemList.Casing_Vacuum_Furnace.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.EnergyCrystal, 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Utupu-Tanuri
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vacuum_Furnace.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Materials.YttriumBariumCuprate, 4),
                ItemList.Robot_Arm_EV.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plate, 8),
                Circuits.LuV.get(8))
            .itemOutputs(GregtechItemList.Controller_Vacuum_Furnace.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.Zeron100, 20 * INGOTS))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void multiChisel() {
        // Industrial 3D Copying Machine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GT_Chisel_HV.get(1),
                MaterialLibAPI.getStack(Materials2Materials.IncoloyDS, Materials2Shapes.plate, 8),
                ItemList.Electric_Motor_EV.get(8),
                ItemList.Conveyor_Module_EV.get(8),
                ItemList.Robot_Arm_EV.get(4))
            .circuit(14)
            .itemOutputs(ItemList.IndustrialPrinter.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.IncoloyDS, 8 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Sturdy Printer Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_SolidSteel.get(2),
                MaterialLibAPI.getStack(Materials2Materials.IncoloyDS, Materials2Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials2Materials.TantalumCarbide, Materials2Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ring, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.EglinSteel, Materials2Shapes.stick, 4))
            .circuit(14)
            .itemOutputs(GregtechItemList.Casing_IndustrialAutoChisel.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.EglinSteel, 2 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void multiRockBreaker() {
        // Boldarnator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_EV_RockBreaker.get(1),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plate, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.ring, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plateDouble, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.EglinSteel, Materials2Shapes.screw, 8))
            .circuit(12)
            .itemOutputs(ItemList.Boldarnator.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Aluminium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void multiAssembler() {
        // Bulk Production Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Block_IridiumTungstensteel.get(1),
                Circuits.MV.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Inconel625, Materials2Shapes.screw, 32),
                MaterialLibAPI.getStack(Materials2Materials.EnergyCrystal, Materials2Shapes.bolt, 12),
                MaterialLibAPI.getStack(Materials2Materials.Zeron100, Materials2Shapes.plate, 8))
            .itemOutputs(GregtechItemList.Casing_Autocrafter.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.TriniumNaquadahCarbonite, 4 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Large Scale Auto-Assembler v1.01
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Refinery_Structural.get(4),
                GregtechItemList.LFTRControlCircuit.get(1),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials2Materials.Platinum, 16),
                GregtechItemList.TransmissionComponent_IV.get(2),
                GregtechItemList.Gregtech_Computer_Cube.get(1))
            .itemOutputs(GregtechItemList.GT4_Multi_Crafter.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials2Materials.Pikyonium64B, 8 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }
}
