package gregtech.loaders.postload.recipes;

import static gregtech.loaders.postload.GT_MachineRecipeLoader.isBartWorksLoaded;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;

public class AssemblyLineRecipes implements Runnable {

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

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        Materials LuVMat = isBartWorksLoaded ? Materials.Ruridit : Materials.Osmiridium;

        // Motors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_IV.get(1, new Object() {}),
                144000,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, LuVMat, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250) },
                ItemList.Electric_Motor_LuV.get(1),
                600,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                144000,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750) },
                ItemList.Electric_Motor_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                288000,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 1296),
                        Materials.Lubricant.getFluid(2000) },
                ItemList.Electric_Motor_UV.get(1),
                600,
                100000);

        // Pumps
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_IV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NiobiumTitanium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 8L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 4L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.HSSS, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250) },
                ItemList.Electric_Pump_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_LuV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 8L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750) },
                ItemList.Electric_Pump_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { ItemList.Electric_Motor_UV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 8L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 16L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 1296),
                        Materials.Lubricant.getFluid(2000) },
                ItemList.Electric_Pump_UV.get(1),
                600,
                100000);

        // Conveyors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_IV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Electric_Motor_LuV.get(2, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 10L }, },
                new FluidStack[] { new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250) },
                ItemList.Conveyor_Module_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_LuV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Electric_Motor_ZPM.get(2, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 20L }, },
                new FluidStack[] { new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750) },
                ItemList.Conveyor_Module_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { ItemList.Electric_Motor_UV.get(2, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L),
                        new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40L } },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 1296),
                        Materials.Lubricant.getFluid(2000) },
                ItemList.Conveyor_Module_UV.get(1),
                600,
                100000);

        // Pistons
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_IV.get(1, new Object() {}),
                144000,
                new ItemStack[] { ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.HSSS, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 4L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250) },
                ItemList.Electric_Piston_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_LuV.get(1, new Object() {}),
                144000,
                new ItemStack[] { ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 4L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750) },
                ItemList.Electric_Piston_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_ZPM.get(1, new Object() {}),
                288000,
                new ItemStack[] { ItemList.Electric_Motor_UV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 4L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 1296),
                        Materials.Lubricant.getFluid(2000) },
                ItemList.Electric_Piston_UV.get(1),
                600,
                100000);

        // RobotArms
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_IV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 3L),
                        ItemList.Electric_Motor_LuV.get(2, new Object() {}),
                        ItemList.Electric_Piston_LuV.get(1, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Elite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Data), 8 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 6L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 576), Materials.Lubricant.getFluid(250) },
                ItemList.Robot_Arm_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_LuV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 3L),
                        ItemList.Electric_Motor_ZPM.get(2, new Object() {}),
                        ItemList.Electric_Piston_ZPM.get(1, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 2 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Elite), 8 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 6L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 1152), Materials.Lubricant.getFluid(750) },
                ItemList.Robot_Arm_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 3L),
                        ItemList.Electric_Motor_UV.get(2, new Object() {}),
                        ItemList.Electric_Piston_UV.get(1, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 2 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 8 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 6L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304),
                        Materials.Lubricant.getFluid(2000) },
                ItemList.Robot_Arm_UV.get(1),
                600,
                100000);

        // Emitters
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_IV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                        ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8L),
                        ItemList.QuantumStar.get(1, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 576) },
                ItemList.Emitter_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_LuV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                        ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8L),
                        ItemList.QuantumStar.get(2, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 1152) },
                ItemList.Emitter_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                        ItemList.Electric_Motor_UV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8L),
                        ItemList.Gravistar.get(4, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304) },
                ItemList.Emitter_UV.get(1),
                600,
                100000);

        // Sensors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_IV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                        ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8L),
                        ItemList.QuantumStar.get(1, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 576) },
                ItemList.Sensor_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_LuV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                        ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8L),
                        ItemList.QuantumStar.get(2, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 1152) },
                ItemList.Sensor_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                        ItemList.Electric_Motor_UV.get(1, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L),
                        ItemList.Gravistar.get(4, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304) },
                ItemList.Sensor_UV.get(1),
                600,
                100000);

        // Field Generators
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_IV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6L),
                        ItemList.QuantumStar.get(2, new Object() {}), ItemList.Emitter_LuV.get(4, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 8L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 576) },
                ItemList.Field_Generator_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_LuV.get(1, new Object() {}),
                144000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6L),
                        ItemList.QuantumStar.get(2, new Object() {}), ItemList.Emitter_ZPM.get(4, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8L) },
                new FluidStack[] { new FluidStack(solderIndalloy, 1152) },
                ItemList.Field_Generator_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                        ItemList.Gravistar.get(2, new Object() {}), ItemList.Emitter_UV.get(4, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8L) },
                new FluidStack[] { Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304) },
                ItemList.Field_Generator_UV.get(1),
                600,
                100000);

        // Energy Hatches Luv-UV
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_IV.get(1, new Object() {}),
                72000,
                new Object[] { ItemList.Hull_LuV.get(1L, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2L),
                        ItemList.Circuit_Chip_UHPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                        ItemList.LuV_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {}) },
                        ItemList.Electric_Pump_LuV.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        new FluidStack(solderIndalloy, 720) },
                ItemList.Hatch_Energy_LuV.get(1),
                400,
                (int) TierEU.RECIPE_LuV);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_LuV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Hull_ZPM.get(1L, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2L),
                        ItemList.Circuit_Chip_NPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 2 },
                        ItemList.ZPM_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        ItemList.Electric_Pump_ZPM.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        new FluidStack(solderIndalloy, 1440) },
                ItemList.Hatch_Energy_ZPM.get(1),
                600,
                (int) TierEU.RECIPE_ZPM);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { ItemList.Hull_UV.get(1L, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2L),
                        ItemList.Circuit_Chip_PPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 2 },
                        ItemList.UV_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        ItemList.Electric_Pump_UV.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000),
                        new FluidStack(solderIndalloy, 2880) },
                ItemList.Hatch_Energy_UV.get(1),
                800,
                500000);

        // Dynamo Hatches Luv-UV
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_IV.get(1, new Object() {}),
                72000,
                new Object[] { ItemList.Hull_LuV.get(1L, new Object() {}),
                        GT_OreDictUnificator.get(
                                OrePrefixes.spring,
                                Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                                2L),
                        ItemList.Circuit_Chip_UHPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                        ItemList.LuV_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {}) },
                        ItemList.Electric_Pump_LuV.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        new FluidStack(solderIndalloy, 720) },
                ItemList.Hatch_Dynamo_LuV.get(1),
                400,
                (int) TierEU.RECIPE_LuV);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_LuV.get(1, new Object() {}),
                144000,
                new Object[] { ItemList.Hull_ZPM.get(1L, new Object() {}),
                        GT_OreDictUnificator
                                .get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4L),
                        ItemList.Circuit_Chip_NPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 2 },
                        ItemList.ZPM_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        ItemList.Electric_Pump_ZPM.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        new FluidStack(solderIndalloy, 1440) },
                ItemList.Hatch_Dynamo_ZPM.get(1),
                600,
                (int) TierEU.RECIPE_ZPM);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_ZPM.get(1, new Object() {}),
                288000,
                new Object[] { ItemList.Hull_UV.get(1L, new Object() {}),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4L),
                        ItemList.Circuit_Chip_PPIC.get(2L, new Object() {}),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 2 },
                        ItemList.UV_Coil.get(2L, new Object() {}),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                                ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {}) },
                        ItemList.Electric_Pump_UV.get(1L, new Object() {}) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000),
                        new FluidStack(solderIndalloy, 2880) },
                ItemList.Hatch_Dynamo_UV.get(1),
                800,
                500000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Energy_LapotronicOrb2.get(1),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        ItemList.Energy_LapotronicOrb2.get(8L), ItemList.Field_Generator_LuV.get(2),
                        ItemList.Circuit_Wafer_SoC2.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32) },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                ItemList.Energy_Module.get(1),
                2000,
                100000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Energy_Module.get(1),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        ItemList.Energy_Module.get(8L), ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Wafer_HPIC.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                ItemList.Energy_Cluster.get(1),
                2000,
                200000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),
                144000,
                new Object[] { ItemList.Casing_Fusion_Coil.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4L),
                        ItemList.Field_Generator_LuV.get(2), ItemList.Circuit_Wafer_UHPIC.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.VanadiumGallium.getMolten(1152L), },
                ItemList.FusionComputer_LuV.get(1),
                1000,
                30000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1),
                288000,
                new Object[] { ItemList.Casing_Fusion_Coil.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 4L),
                        ItemList.Field_Generator_ZPM.get(2), ItemList.Circuit_Wafer_PPIC.get(48),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.NiobiumTitanium.getMolten(1152L), },
                ItemList.FusionComputer_ZPMV.get(1),
                1000,
                60000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Americium, 1),
                432000,
                new Object[] { ItemList.Casing_Fusion_Coil.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4L),
                        ItemList.Field_Generator_UV.get(2), ItemList.Circuit_Wafer_QPIC.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.ElectrumFlux.getMolten(1152L), },
                ItemList.FusionComputer_UV.get(1),
                1000,
                90000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Machine_IV_OreWasher.get(1),
                432000,
                new Object[] { ItemList.Hull_MAX.get(1L), ItemList.Electric_Motor_UHV.get(32L),
                        ItemList.Electric_Piston_UHV.get(8L), ItemList.Electric_Pump_UHV.get(16L),
                        ItemList.Conveyor_Module_UHV.get(8L), ItemList.Robot_Arm_UHV.get(8L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Duranium, 32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 32) },
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 64),
                        new ItemStack[] { ItemList.Component_Grinder_Tungsten.get(64L),
                                ItemList.Component_Grinder_Diamond.get(64L) },
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 32),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Chrome, 16) },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.Naquadria.getMolten(1440) },
                ItemList.Ore_Processor.get(1),
                1200,
                900000);
    }
}
