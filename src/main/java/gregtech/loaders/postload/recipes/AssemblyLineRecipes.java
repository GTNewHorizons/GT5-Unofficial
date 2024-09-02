package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import tectech.thing.CustomItemList;

public class AssemblyLineRecipes implements Runnable {

    private final Fluid solderIndalloy;
    private final Materials LuVMat;

    public AssemblyLineRecipes() {
        solderIndalloy = FluidRegistry.getFluid("molten.indalloy140");

        LuVMat = ExternalMaterials.getRuridit();
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
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 2),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .itemOutputs(ItemList.Electric_Motor_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // ZPM motor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 2),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .itemOutputs(ItemList.Electric_Motor_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV motor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 2),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 16),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2))
                .fluidInputs(
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000))
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
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NiobiumTitanium, 2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 4 },
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.HSSS, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .itemOutputs(ItemList.Electric_Pump_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(600)
                .addTo(AssemblyLine);

            // ZPM Pump
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 8 },
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .itemOutputs(ItemList.Electric_Pump_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV Pump
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 16 },
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2))
                .itemOutputs(ItemList.Electric_Pump_UV.get(1))
                .fluidInputs(
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Conveyors
        {
            // LuV Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 10 })
                .itemOutputs(ItemList.Conveyor_Module_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 20 })
                .itemOutputs(ItemList.Conveyor_Module_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Conveyor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40 })
                .itemOutputs(ItemList.Conveyor_Module_UV.get(1))
                .fluidInputs(
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Pistons
        {
            // LuV Piston
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.HSSS, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 4))
                .itemOutputs(ItemList.Electric_Piston_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Pistons
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 4))
                .itemOutputs(ItemList.Electric_Piston_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Piston
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 4))
                .itemOutputs(ItemList.Electric_Piston_UV.get(1))
                .fluidInputs(
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // RobotArms
        {
            // LuV Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 3),
                    ItemList.Electric_Motor_LuV.get(2),
                    ItemList.Electric_Piston_LuV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.EV), 8 },
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 6))
                .itemOutputs(ItemList.Robot_Arm_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 3),
                    ItemList.Electric_Motor_ZPM.get(2),
                    ItemList.Electric_Piston_ZPM.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 8 },
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 6))
                .itemOutputs(ItemList.Robot_Arm_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Robot Arm
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 3),
                    ItemList.Electric_Motor_UV.get(2),
                    ItemList.Electric_Piston_UV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 6))
                .itemOutputs(ItemList.Robot_Arm_UV.get(1))
                .fluidInputs(
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 2304),
                    Materials.Lubricant.getFluid(2000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Emitters
        {
            // LuV Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.stick, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7))
                .itemOutputs(ItemList.Emitter_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7))
                .itemOutputs(ItemList.Emitter_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Emitter
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    ItemList.Electric_Motor_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7))
                .itemOutputs(ItemList.Emitter_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Sensors
        {
            // LuV Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7))
                .itemOutputs(ItemList.Sensor_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7))
                .itemOutputs(ItemList.Sensor_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Sensor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    ItemList.Electric_Motor_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7))
                .itemOutputs(ItemList.Sensor_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Field Generators
        {
            // LuV Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_LuV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 8))
                .itemOutputs(ItemList.Field_Generator_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_ZPM.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8))
                .itemOutputs(ItemList.Field_Generator_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Field Generator
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                    ItemList.Gravistar.get(2),
                    ItemList.Emitter_UV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8))
                .itemOutputs(ItemList.Field_Generator_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Energy Hatches
        {
            // LuV Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_IV.get(1))
                .metadata(RESEARCH_TIME, 60 * MINUTES)
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    ItemList.LuV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Energy_LuV.get(1))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                    new FluidStack(solderIndalloy, 720))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // ZPM Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    ItemList.ZPM_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Energy_ZPM.get(1))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                    new FluidStack(solderIndalloy, 1440))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);

            // UV Energy Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2),
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
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000),
                    new FluidStack(solderIndalloy, 2880))
                .duration(40 * SECONDS)
                .eut((int) TierEU.RECIPE_UV)
                .addTo(AssemblyLine);
        }

        // Dynamo Hatches
        {
            // LuV Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_IV.get(1))
                .metadata(RESEARCH_TIME, 60 * MINUTES)
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get(
                        OrePrefixes.spring,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    ItemList.LuV_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1), ItemList.Reactor_Coolant_NaK_3.get(1),
                        ItemList.Reactor_Coolant_Sp_1.get(1) },
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_LuV.get(1))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                    new FluidStack(solderIndalloy, 720))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // ZPM Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    ItemList.ZPM_Coil.get(2),
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1), ItemList.Reactor_Coolant_NaK_6.get(1),
                        ItemList.Reactor_Coolant_Sp_2.get(1) },
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_ZPM.get(1))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                    new FluidStack(solderIndalloy, 1440))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);

            // UV Dynamo Hatch
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4),
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
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000),
                    new FluidStack(solderIndalloy, 2880))
                .duration(40 * SECONDS)
                .eut((int) TierEU.RECIPE_UV)
                .addTo(AssemblyLine);
        }

        // Fusion Controller
        {
            // mkI
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4),
                    ItemList.Field_Generator_LuV.get(2),
                    ItemList.Circuit_Wafer_UHPIC.get(32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32))
                .itemOutputs(ItemList.FusionComputer_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.VanadiumGallium.getMolten(1152))
                .duration(50 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkII
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 4),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32))
                .itemOutputs(ItemList.FusionComputer_ZPMV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.NiobiumTitanium.getMolten(1152))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkIII
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.block, Materials.Americium, 1))
                .metadata(RESEARCH_TIME, 6 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32))
                .itemOutputs(ItemList.FusionComputer_UV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.ElectrumFlux.getMolten(1152))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }
        // Energy Module
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_LapotronicOrb2.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                ItemList.Energy_LapotronicOrb2.get(8),
                ItemList.Field_Generator_LuV.get(2),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32))
            .itemOutputs(ItemList.Energy_Module.get(1))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Energy Cluster
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_Module.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                ItemList.Energy_Module.get(8),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32))
            .itemOutputs(ItemList.Energy_Cluster.get(1))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(200000)
            .addTo(AssemblyLine);

        // Integrated Ore Factory
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_OreWasher.get(1))
            .metadata(RESEARCH_TIME, 6 * HOURS)
            .itemInputs(
                ItemList.Hull_MAX.get(1),
                ItemList.Electric_Motor_UHV.get(32),
                ItemList.Electric_Piston_UHV.get(8),
                ItemList.Electric_Pump_UHV.get(16),
                ItemList.Conveyor_Module_UHV.get(8),
                ItemList.Robot_Arm_UHV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Duranium, 32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 32) },
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 64),
                new ItemStack[] { ItemList.Component_Grinder_Tungsten.get(64),
                    ItemList.Component_Grinder_Diamond.get(64) },
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 32),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Chrome, 16))
            .itemOutputs(ItemList.Ore_Processor.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.Naquadria.getMolten(1440))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone0.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                NewHorizonsCoreMod.isModLoaded()
                    ? GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyRocketEngineTier3", 4)
                    : ItemList.Casing_Firebox_TungstenSteel.get(16),
                ItemList.Large_Fluid_Cell_Osmium.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.MysteriousCrystal, 1),
                ItemList.Emitter_ZPM.get(4),
                ItemList.Energy_Module.get(1),
                ItemList.Cover_WirelessNeedsMaintainance.get(1))
            .itemOutputs(ItemList.TierdDrone1.get(4))
            .fluidInputs(new FluidStack(solderIndalloy, 576), FluidRegistry.getFluidStack("fluid.rocketfuelmixc", 4000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone1.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.Field_Generator_UV.get(16),
                ItemList.Gravistar.get(8),
                ItemList.Emitter_UV.get(4),
                CustomItemList.hatch_CreativeMaintenance.get(16),
                ItemList.Energy_Cluster.get(8),
                ItemList.Cover_WirelessNeedsMaintainance.get(1))
            .itemOutputs(ItemList.TierdDrone2.get(1))
            .fluidInputs(
                new FluidStack(solderIndalloy, 144000),
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 2000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

    }
}
