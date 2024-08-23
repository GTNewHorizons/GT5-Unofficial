package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 2),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .itemOutputs(ItemList.Electric_Motor_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // ZPM motor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 2),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 16),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .itemOutputs(ItemList.Electric_Motor_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV motor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Motor_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 2),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2))
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NiobiumTitanium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.HSSS, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .itemOutputs(ItemList.Electric_Pump_LuV.get(1))
                .eut(TierEU.RECIPE_IV)
                .duration(600)
                .addTo(AssemblyLine);

            // ZPM Pump
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 8 },
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .itemOutputs(ItemList.Electric_Pump_ZPM.get(1))
                .eut(TierEU.RECIPE_LuV)
                .duration(30 * SECONDS)
                .addTo(AssemblyLine);

            // UV Pump
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Pump_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 8),
                    new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 16 },
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2))
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 10 })
                .itemOutputs(ItemList.Conveyor_Module_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Conveyor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2),
                    new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 20 })
                .itemOutputs(ItemList.Conveyor_Module_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Conveyor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Conveyor_Module_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.HSSS, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 4))
                .itemOutputs(ItemList.Electric_Piston_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Pistons
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 4))
                .itemOutputs(ItemList.Electric_Piston_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Piston
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Electric_Piston_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Electric_Motor_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 4))
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 3),
                    ItemList.Electric_Motor_LuV.get(2),
                    ItemList.Electric_Piston_LuV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.EV), 8 },
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 6))
                .itemOutputs(ItemList.Robot_Arm_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576), Materials.Lubricant.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Robot Arm
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 3),
                    ItemList.Electric_Motor_ZPM.get(2),
                    ItemList.Electric_Piston_ZPM.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 8 },
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 6))
                .itemOutputs(ItemList.Robot_Arm_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152), Materials.Lubricant.getFluid(750))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Robot Arm
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Robot_Arm_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 3),
                    ItemList.Electric_Motor_UV.get(2),
                    ItemList.Electric_Piston_UV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 6))
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.stick, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7))
                .itemOutputs(ItemList.Emitter_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Emitter
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7))
                .itemOutputs(ItemList.Emitter_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Emitter
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Emitter_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    ItemList.Electric_Motor_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7))
                .itemOutputs(ItemList.Emitter_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Sensors
        {
            // LuV Sensor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    ItemList.Electric_Motor_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, LuVMat, 8),
                    ItemList.QuantumStar.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7))
                .itemOutputs(ItemList.Sensor_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Sensor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    ItemList.Electric_Motor_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8),
                    ItemList.QuantumStar.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7))
                .itemOutputs(ItemList.Sensor_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Sensor
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Sensor_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    ItemList.Electric_Motor_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                    ItemList.Gravistar.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7))
                .itemOutputs(ItemList.Sensor_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Field Generators
        {
            // LuV Field Generator
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_IV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_LuV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 8))
                .itemOutputs(ItemList.Field_Generator_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 576))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_IV)
                .addTo(AssemblyLine);

            // ZPM Field Generator
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6),
                    ItemList.QuantumStar.get(2),
                    ItemList.Emitter_ZPM.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8))
                .itemOutputs(ItemList.Field_Generator_ZPM.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // UV Field Generator
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Field_Generator_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                    ItemList.Gravistar.get(2),
                    ItemList.Emitter_UV.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8))
                .itemOutputs(ItemList.Field_Generator_UV.get(1))
                .fluidInputs(Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }

        // Energy Hatches
        {
            // LuV Energy Hatch
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_IV.get(1))
                .metadata(RESEARCH_TIME, 60 * MINUTES)
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Energy_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_IV.get(1))
                .metadata(RESEARCH_TIME, 60 * MINUTES)
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GT_OreDictUnificator.get(
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_LuV.get(1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_Dynamo_ZPM.get(1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4),
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
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1))
                .metadata(RESEARCH_TIME, 2 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4),
                    ItemList.Field_Generator_LuV.get(2),
                    ItemList.Circuit_Wafer_UHPIC.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32))
                .itemOutputs(ItemList.FusionComputer_LuV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.VanadiumGallium.getMolten(1152))
                .duration(50 * SECONDS)
                .eut((int) TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkII
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1))
                .metadata(RESEARCH_TIME, 4 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 4),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32))
                .itemOutputs(ItemList.FusionComputer_ZPMV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.NiobiumTitanium.getMolten(1152))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(AssemblyLine);

            // mkIII
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GT_OreDictUnificator.get(OrePrefixes.block, Materials.Americium, 1))
                .metadata(RESEARCH_TIME, 6 * HOURS)
                .itemInputs(
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32))
                .itemOutputs(ItemList.FusionComputer_UV.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.ElectrumFlux.getMolten(1152))
                .duration(50 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(AssemblyLine);
        }
        // Energy Module
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_LapotronicOrb2.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                ItemList.Energy_LapotronicOrb2.get(8),
                ItemList.Field_Generator_LuV.get(2),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(8),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32))
            .itemOutputs(ItemList.Energy_Module.get(1))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Energy Cluster
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Energy_Module.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                ItemList.Energy_Module.get(8),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Wafer_HPIC.get(64),
                ItemList.Circuit_Parts_DiodeASMD.get(16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32))
            .itemOutputs(ItemList.Energy_Cluster.get(1))
            .fluidInputs(
                new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(200000)
            .addTo(AssemblyLine);

        // Integrated Ore Factory
        GT_Values.RA.stdBuilder()
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
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Duranium, 32),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 32) },
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 64),
                new ItemStack[] { ItemList.Component_Grinder_Tungsten.get(64),
                    ItemList.Component_Grinder_Diamond.get(64) },
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 32),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Chrome, 16))
            .itemOutputs(ItemList.Ore_Processor.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, 2880), Materials.Naquadria.getMolten(1440))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T2
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone0.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                NewHorizonsCoreMod.isModLoaded()
                    ? GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyRocketEngineTier3", 4)
                    : ItemList.Casing_Firebox_TungstenSteel.get(16),
                ItemList.Large_Fluid_Cell_Osmium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.MysteriousCrystal, 1),
                ItemList.Emitter_ZPM.get(4),
                ItemList.Energy_Module.get(1),
                ItemList.Cover_WirelessNeedsMaintainance.get(1))
            .itemOutputs(ItemList.TierdDrone1.get(4))
            .fluidInputs(new FluidStack(solderIndalloy, 576), FluidRegistry.getFluidStack("fluid.rocketfuelmixc", 4000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Drone T3
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.TierdDrone1.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 16),
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
