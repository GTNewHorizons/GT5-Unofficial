package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import tectech.thing.CustomItemList;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class AssemblyLineRecipes implements Runnable {

    private final Materials LuVMat;

    public AssemblyLineRecipes() {
        LuVMat = WerkstoffLoader.Ruridit.getGTMaterial();
    }

    @Override
    public void run() {
        // recipe len:
        // LUV 6 72000 600 32k
        // ZPM 9 144000 1200 125k
        // UV- 12 288000 1800 500k
        // UV+/UHV- 14 360000 2100 2000k
        // UHV+ 16 576000 2400 4000k

        // addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[]
        // aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

        // indalloy and ruridit are from gt++ and bartworks which are not dependencies

        // Motors
        {
            // LuV motor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.stick, (int) (1)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.stickLong, (int) (2)),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 2))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
                .itemOutputs(ItemList.Electric_Motor_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // ZPM motor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.stick, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.stickLong, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.round, (int) (16)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 2))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .itemOutputs(ItemList.Electric_Motor_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV motor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    MaterialLibAPI
                        .getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.stickLong, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.stickLong, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.round, (int) (16)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 2))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
                .itemOutputs(ItemList.Electric_Motor_UV.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);
        }

        // Pumps
        {
            // LuV Pump
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get("pipeSmallNiobiumTitanium", 2),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.screw, (int) (8)),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.rotor, (int) (2)),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 2))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
                .itemOutputs(ItemList.Electric_Pump_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(600)
                .addTo(AssemblyLine);

            // ZPM Pump
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    GTOreDictUnificator.get("pipeMediumEnderium", 2),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.screw, (int) (8)),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 8 },
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.rotor, (int) (2)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 2))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .itemOutputs(ItemList.Electric_Pump_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV Pump
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    GTOreDictUnificator.get("pipeLargeNaquadah", 2),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.screw, (int) (8)),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 16 },
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.rotor, (int) (2)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 2))
                .itemOutputs(ItemList.Electric_Pump_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Conveyors
        {
            // LuV Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(2),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.round, (int) (32)),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 10 })
                .itemOutputs(ItemList.Conveyor_Module_LuV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(2),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.round, (int) (32)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 20 })
                .itemOutputs(ItemList.Conveyor_Module_ZPM.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(2),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plate, (int) (2)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.round, (int) (32)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40 })
                .itemOutputs(ItemList.Conveyor_Module_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Pistons
        {
            // LuV Piston
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plate, (int) (6)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.round, (int) (32)),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.stick, (int) (4)),
                    GTOreDictUnificator.get("gearHSSS", 1),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.gearGtSmall, (int) (2)),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 4))
                .itemOutputs(ItemList.Electric_Piston_LuV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Pistons
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plate, (int) (6)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.round, (int) (32)),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.stick, (int) (4)),
                    GTOreDictUnificator.get("gearNaquadahAlloy", 1),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.gearGtSmall, (int) (2)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 4))
                .itemOutputs(ItemList.Electric_Piston_ZPM.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Piston
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plate, (int) (6)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ring, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.round, (int) (32)),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.stick, (int) (4)),
                    GTOreDictUnificator.get("gearNeutronium", 1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.gearGtSmall, (int) (2)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 4))
                .itemOutputs(ItemList.Electric_Piston_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // RobotArms
        {
            // LuV Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.stickLong, (int) (4)),
                    GTOreDictUnificator.get("gearHSSS", 1),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.gearGtSmall, (int) (3)),
                    ItemList.Electric_Motor_LuV.get(2),
                    ItemList.Electric_Piston_LuV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.EV), 8 },
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 6))
                .itemOutputs(ItemList.Robot_Arm_LuV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (250)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.stickLong, (int) (4)),
                    GTOreDictUnificator.get("gearNaquadahAlloy", 1),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.gearGtSmall, (int) (3)),
                    ItemList.Electric_Motor_ZPM.get(2),
                    ItemList.Electric_Piston_ZPM.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 8 },
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 6))
                .itemOutputs(ItemList.Robot_Arm_ZPM.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.stickLong, (int) (4)),
                    GTOreDictUnificator.get("gearNeutronium", 1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.gearGtSmall, (int) (3)),
                    ItemList.Electric_Motor_UV.get(2),
                    ItemList.Electric_Piston_UV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 6))
                .itemOutputs(ItemList.Robot_Arm_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Emitters
        {
            // LuV Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtHSSS", 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.stick, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 7))
                .itemOutputs(ItemList.Emitter_LuV.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNaquadahAlloy", 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.stick, (int) (8)),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 7))
                .itemOutputs(ItemList.Emitter_ZPM.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNeutronium", 1),
                    ItemList.Electric_Motor_UV.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.stick, (int) (8)),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 7))
                .itemOutputs(ItemList.Emitter_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Sensors
        {
            // LuV Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtHSSS", 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 7))
                .itemOutputs(ItemList.Sensor_LuV.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNaquadahAlloy", 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.plate, (int) (8)),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 7))
                .itemOutputs(ItemList.Sensor_ZPM.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNeutronium", 1),
                    ItemList.Electric_Motor_UV.get(1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plate, (int) (8)),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.foil, (int) (64)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 7))
                .itemOutputs(ItemList.Sensor_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Field Generators
        {
            // LuV Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_EV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtHSSS", 1),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plate, (int) (6)),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_LuV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get("cableGt01YttriumBariumCuprate", 8))
                .itemOutputs(ItemList.Field_Generator_LuV.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNaquadahAlloy", 1),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plate, (int) (6)),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_ZPM.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.wireFine, (int) (64)),
                    GTOreDictUnificator.get("cableGt04VanadiumGallium", 8))
                .itemOutputs(ItemList.Field_Generator_ZPM.get(1))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    GTOreDictUnificator.get("frameGtNeutronium", 1),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plate, (int) (6)),
                    ItemList.Gravistar.get(2),
                    ItemList.Emitter_UV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.wireFine, (int) (64)),
                    GTOreDictUnificator.get("cableGt04NaquadahAlloy", 8))
                .itemOutputs(ItemList.Field_Generator_UV.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (9 * INGOTS)),
                    MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Energy Hatches
        {
            // LuV Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get("wireGt01SuperconductorLuV", 2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    ItemList.LuV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Energy_LuV.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(2_000), MaterialsAlloy.INDALLOY_140.getFluidStack(5 * INGOTS))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // ZPM Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get("wireGt02SuperconductorZPM", 2),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    ItemList.ZPM_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Energy_ZPM.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(4_000), MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);

            // UV Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get("wireGt02SuperconductorUV", 2),
                    ItemList.Circuit_Chip_PPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                    ItemList.UV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_UV.get(1))
                .itemOutputs(ItemList.Hatch_Energy_UV.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(8_000), MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS))
                .duration(40 * SECONDS)
                .eut((int) TierEU.RECIPE_UV)
                .addTo(AssemblyLine);
        }

        // Dynamo Hatches
        {
            // LuV Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_IV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get("springTetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid", 2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    ItemList.LuV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_LuV.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(2_000), MaterialsAlloy.INDALLOY_140.getFluidStack(5 * INGOTS))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // ZPM Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_LuV.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get("springTetranaquadahdiindiumhexaplatiumosminid", 4),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    ItemList.ZPM_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_ZPM.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(4_000), MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);

            // UV Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_ZPM.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get("springLongasssuperconductornameforuvwire", 4),
                    ItemList.Circuit_Chip_PPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                    ItemList.UV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_UV.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_UV.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(8_000), MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS))
                .duration(40 * SECONDS)
                .eut((int) TierEU.RECIPE_UV)
                .addTo(AssemblyLine);
        }

        // Fusion Controller
        {
            // mkI
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get("wireGt01SuperconductorLuV", 1))
                .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_IV))
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, (int) (4)),
                    MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.plateDense, (int) (1)),
                    ItemList.Field_Generator_LuV.get(2),
                    ItemList.Circuit_Wafer_UHPIC.get(32),
                    GTOreDictUnificator.get("wireGt01SuperconductorLuV", 32))
                .itemOutputs(ItemList.FusionComputer_LuV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.VanadiumGallium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (8 * INGOTS)))
                .duration(50 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkII
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get("blockEuropium", 1))
                .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.plateSuperdense, (int) (1)),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    GTOreDictUnificator.get("wireGt02SuperconductorZPM", 32))
                .itemOutputs(ItemList.FusionComputer_ZPMV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.NiobiumTitanium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (8 * INGOTS)))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkIII
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get("blockAmericium", 1))
                .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_ZPM))
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.plateSuperdense, (int) (1)),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GTOreDictUnificator.get("wireGt04SuperconductorUV", 32))
                .itemOutputs(ItemList.FusionComputer_UV.get(1))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.ElectrumFlux,
                        Materials2FluidShapes.fluidMolten,
                        (int) (8 * INGOTS)))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }
        // Energy Module
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_LapotronicOrb2.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.plate, (int) (16)),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                ItemList.Energy_LapotronicOrb2.get(8),
                ItemList.Field_Generator_LuV.get(2),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                GTOreDictUnificator.get("cableGt01Naquadah", 32))
            .itemOutputs(ItemList.Energy_Module.get(1))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS), GTModHandler.getIC2Coolant(16_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Energy Cluster
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_Module.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.plate, (int) (32)),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                ItemList.Energy_Module.get(8),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(16),
                GTOreDictUnificator.get("cableGt01NaquadahAlloy", 32))
            .itemOutputs(ItemList.Energy_Cluster.get(1))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS), GTModHandler.getIC2Coolant(16_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(200000)
            .addTo(AssemblyLine);

        // Integrated Ore Factory
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_OreWasher.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Hull_MAX.get(1),
                ItemList.Electric_Motor_UHV.get(32),
                ItemList.Electric_Piston_UHV.get(8),
                ItemList.Electric_Pump_UHV.get(16),
                ItemList.Conveyor_Module_UHV.get(8),
                ItemList.Robot_Arm_UHV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                new ItemStack[] { GTOreDictUnificator.get("wireGt04Duranium", 32),
                    GTOreDictUnificator.get("wireGt04NaquadahAlloy", 32) },
                GTOreDictUnificator.get("pipeMediumPolybenzimidazole", 64),
                new ItemStack[] { ItemList.Component_Grinder_Tungsten.get(64),
                    ItemList.Component_Grinder_Diamond.get(64) },
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plateDouble, (int) (32)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.rotor, (int) (16)))
            .itemOutputs(ItemList.IntegratedOreFactory.get(1))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Naquadria,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone0.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, (int) (16)),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                NewHorizonsCoreMod.isModLoaded()
                    ? GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier3", 4)
                    : ItemList.Casing_Firebox_TungstenSteel.get(16),
                ItemList.Large_Fluid_Cell_Osmium.get(1),
                GTOreDictUnificator.get("pipeQuadrupleMysteriousCrystal", 1),
                ItemList.Emitter_ZPM.get(4),
                ItemList.Energy_Module.get(1),
                ItemList.Cover_WirelessNeedsMaintainance.get(1))
            .itemOutputs(ItemList.TierdDrone1.get(4))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS),
                FluidRegistry.getFluidStack("fluid.rocketfuelmixc", 4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone1.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, (int) (16)),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                ItemList.Field_Generator_UV.get(16),
                ItemList.Gravistar.get(8),
                ItemList.Emitter_UV.get(4),
                CustomItemList.hatch_CreativeMaintenance.get(16),
                ItemList.Energy_Cluster.get(8),
                ItemList.Cover_WirelessNeedsMaintainance.get(1))
            .itemOutputs(ItemList.TierdDrone2.get(1))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(15 * STACKS + 40 * INGOTS),
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // Drone remote interface
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone2.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (16)),
                ItemList.Cover_Screen.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                ItemList.Field_Generator_UHV.get(4),
                ItemList.Sensor_UHV.get(8),
                ItemList.Emitter_UHV.get(8),
                ItemList.TierdDrone2.get(1),
                ItemList.Tool_DataOrb.get(4))
            .itemOutputs(ItemList.DroneRemoteInterface.get(1))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(15 * STACKS + 40 * INGOTS),
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(64_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // Drone T4
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.DroneRemoteInterface.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UEV))
            .itemInputs(
                MaterialsElements.STANDALONE.HYPOGEN.getIngot(1),
                getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.Field_Generator_UHV.get(16),
                ItemList.NuclearStar.get(8),
                ItemList.Emitter_UHV.get(4),
                ItemList.ZPM3.get(1),
                ItemList.SpaceElevatorMotorT3.get(64))
            .itemOutputs(ItemList.TierdDrone3.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (8_000)),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(8_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(AssemblyLine);

        // Endothermic Fridge
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_VacuumFreezer.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 20 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Machine_Multi_VacuumFreezer.get(64),
                ItemList.CryogenicFreezer.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                ItemList.Coolant_Duct_Casing.get(4),
                MaterialLibAPI.getStack(Materials2Materials.CallistoIce, Materials2Shapes.stick, (int) (32)),
                ItemList.Electric_Pump_ZPM.get(8),
                ItemList.FluidRegulator_ZPM.get(8),
                GTOreDictUnificator.get("wireGt02SuperconductorZPM", 16),
                ItemList.Naquarite_Universal_Insulator_Foil.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Ledox, Materials2Shapes.plateSuperdense, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plateSuperdense, (int) (8)))
            .fluidInputs(
                new FluidStack(TFFluids.fluidCryotheum, 256_000),
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (16_000)))
            .itemOutputs(ItemList.EndothermicFridge.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Exothermic Hearth
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_BlastFurnace.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 20 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Machine_Multi_BlastFurnace.get(64),
                GregtechItemList.Machine_Adv_BlastFurnace.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                ItemList.Heating_Duct_Casing.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Firestone, Materials2Shapes.stick, (int) (32)),
                ItemList.Electric_Pump_ZPM.get(8),
                ItemList.FluidRegulator_ZPM.get(8),
                GTOreDictUnificator.get("wireGt02SuperconductorZPM", 16),
                ItemList.Naquarite_Universal_Insulator_Foil.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.plateSuperdense, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.plateSuperdense, (int) (8)))
            .fluidInputs(
                new FluidStack(GTPPFluids.Pyrotheum, 256_000),
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (16_000)))
            .itemOutputs(ItemList.ExothermicHearth.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Mega Distillation Tower
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Machine_Adv_DistillationTower.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Distillation_Tower.get(64),
                GregtechItemList.Machine_Adv_DistillationTower.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                ItemList.CasingNaquadahReinforcedDistillation.get(8),
                ItemList.Electric_Pump_LuV.get(4),
                ItemList.FluidRegulator_LuV.get(4),
                ItemList.Machine_IV_Distillery.get(2),
                GTOreDictUnificator.get("wireGt02SuperconductorLuV", 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .itemOutputs(ItemList.MegaDistillationTower.get(1))
            .eut(TierEU.RECIPE_LuV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Mega Chemical Reactor
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_LargeChemicalReactor.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Machine_Multi_LargeChemicalReactor.get(64),
                GregtechItemList.ChemicalPlant_Controller.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                ItemList.Casing_Chemically_Inert.get(8),
                ItemList.Electric_Pump_LuV.get(4),
                ItemList.FluidRegulator_LuV.get(4),
                ItemList.Machine_IV_ChemicalReactor.get(2),
                GTOreDictUnificator.get("wireGt02SuperconductorLuV", 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .itemOutputs(ItemList.MegaChemicalReactor.get(1))
            .eut(TierEU.RECIPE_LuV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Mega Oil Cracker
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.OilCracker.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.OilCracker.get(64),
                ItemList.Machine_Multi_LargeChemicalReactor.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                ItemList.CasingNaquadahReinforcedDistillation.get(8),
                ItemList.Steam_Valve_IV.get(8),
                ItemList.Steam_Regulator_IV.get(8),
                ItemList.Casing_Coil_Naquadah.get(8),
                GTOreDictUnificator.get("wireGt02SuperconductorLuV", 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .itemOutputs(ItemList.MegaOilCracker.get(1))
            .eut(TierEU.RECIPE_LuV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

    }
}
