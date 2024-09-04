package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Air_Intake;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Air_Intake_Extreme;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Cryotheum;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Naquadah;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Pyrotheum;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_TurbineHousing;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Reservoir;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.METHatchAirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchAirIntakeExtreme;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchMufflerAdvanced;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchNaquadah;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchReservoir;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSuperBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbineProvider;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTESuperBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;

public class GregtechCustomHatches {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Custom Fluid Hatches.");
        run1();
        if (PollutionUtils.isPollutionEnabled()) {
            run2();
        }
        run3();
        run4(); // Chisel buses
        run6(); // Solidifier hatches
    }

    private static void run1() {

        GregtechItemList.Hatch_Input_Cryotheum.set(
            new MTEHatchCustomFluidBase(
                FluidUtils.getFluidStack("cryotheum", 1)
                    .getFluid(), // Fluid to restrict hatch to
                128000, // Capacity
                Hatch_Input_Cryotheum.ID, // ID
                "hatch.cryotheum.input.tier.00", // unlocal name
                "Cryotheum Cooling Hatch", // Local name
                5 // Casing Texture
            ).getStackForm(1L));

        GregtechItemList.Hatch_Input_Pyrotheum.set(
            new MTEHatchCustomFluidBase(
                FluidUtils.getFluidStack("pyrotheum", 1)
                    .getFluid(), // Fluid to restrict hatch to
                128000, // Capacity
                Hatch_Input_Pyrotheum.ID, // ID
                "hatch.pyrotheum.input.tier.00", // unlocal name
                "Pyrotheum Heating Vent", // Local name
                5 // Casing texture
            ).getStackForm(1L));

        GregtechItemList.Hatch_Input_Naquadah.set(
            new MTEHatchNaquadah(
                Hatch_Input_Naquadah.ID, // ID
                "hatch.naquadah.input.tier.00", // unlocal name
                "Naquadah Reactor Input hatch" // Local name
            ).getStackForm(1L));

        GregtechItemList.Hatch_Input_TurbineHousing.set(
            new MTEHatchTurbineProvider(
                Hatch_Input_TurbineHousing.ID, // ID
                "hatch.turbine.input.tier.00", // unlocal name
                "Turbine Housing", // Local name
                8).getStackForm(1L));

        // Multiblock Air Intake Hatch
        GregtechItemList.Hatch_Air_Intake.set(
            new METHatchAirIntake(Hatch_Air_Intake.ID, "hatch.air.intake.tier.00", "Air Intake Hatch", 5)
                .getStackForm(1L));
        GregtechItemList.Hatch_Air_Intake_Extreme.set(
            new MTEHatchAirIntakeExtreme(
                Hatch_Air_Intake_Extreme.ID,
                "hatch.air.intake.tier.01",
                "Extreme Air Intake Hatch",
                6).getStackForm(1L));

        // Multiblock Reservoir Hatch
        GregtechItemList.Hatch_Reservoir.set(
            new MTEHatchReservoir(Hatch_Reservoir.ID, "hatch.water.intake.tier.00", "Reservoir Hatch", 4)
                .getStackForm(1L));

        // Steam Hatch
        GregtechItemList.Hatch_Input_Steam.set(
            new MTEHatchCustomFluidBase(
                FluidUtils.getSteam(1)
                    .getFluid(), // Fluid to restrict hatch to
                64000, // Capacity
                Hatch_Input_Steam.ID, // ID
                "hatch.steam.input.tier.00", // unlocal name
                "Steam Hatch", // Local name
                0 // Casing texture
            ).getStackForm(1L));
    }

    private static void run2() {
        GregtechItemList.Hatch_Muffler_Adv_LV.set(
            (new MTEHatchMufflerAdvanced(30001, "hatch.muffler.adv.tier.01", "Advanced Muffler Hatch (LV)", 1))
                .getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_MV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_MV.ID,
                "hatch.muffler.adv.tier.02",
                "Advanced Muffler Hatch (MV)",
                2)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_HV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_HV.ID,
                "hatch.muffler.adv.tier.03",
                "Advanced Muffler Hatch (HV)",
                3)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_EV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_EV.ID,
                "hatch.muffler.adv.tier.04",
                "Advanced Muffler Hatch (EV)",
                4)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_IV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_IV.ID,
                "hatch.muffler.adv.tier.05",
                "Advanced Muffler Hatch (IV)",
                5)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_LuV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_LuV.ID,
                "hatch.muffler.adv.tier.06",
                "Advanced Muffler Hatch (LuV)",
                6)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_ZPM.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_ZPM.ID,
                "hatch.muffler.adv.tier.07",
                "Advanced Muffler Hatch (ZPM)",
                7)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_UV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_UV.ID,
                "hatch.muffler.adv.tier.08",
                "Advanced Muffler Hatch (UV)",
                8)).getStackForm(1L));
        GregtechItemList.Hatch_Muffler_Adv_MAX.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_MAX.ID,
                "hatch.muffler.adv.tier.09",
                "Advanced Muffler Hatch (UHV)",
                9)).getStackForm(1L));
    }

    private static void run3() {
        /*
         * Super Input Busses
         */

        int aStartID = 30022;

        GregtechItemList.Hatch_SuperBus_Input_LV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.01", "Super Bus (I) (LV)", 1))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_MV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.02", "Super Bus (I) (MV)", 2))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_HV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.03", "Super Bus (I) (HV)", 3))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_EV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.04", "Super Bus (I) (EV)", 4))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_IV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.05", "Super Bus (I) (IV)", 5))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_LuV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.06", "Super Bus (I) (LuV)", 6))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_ZPM.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.07", "Super Bus (I) (ZPM)", 7))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_UV.set(
            ((IMetaTileEntity) makeInputBus(aStartID++, "hatch.superbus.input.tier.08", "Super Bus (I) (UV)", 8))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_MAX.set(
            ((IMetaTileEntity) makeInputBus(aStartID, "hatch.superbus.input.tier.09", "Super Bus (I) (UHV)", 9))
                .getStackForm(1L));

        /*
         * Super Output Busses
         */
        aStartID = 30032;
        GregtechItemList.Hatch_SuperBus_Output_LV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.01", "Super Bus (O) (LV)", 1))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_MV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.02", "Super Bus (O) (MV)", 2))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_HV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.03", "Super Bus (O) (HV)", 3))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_EV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.04", "Super Bus (O) (EV)", 4))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_IV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.05", "Super Bus (O) (IV)", 5))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_LuV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.06", "Super Bus (O) (LuV)", 6))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_ZPM.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.07", "Super Bus (O) (ZPM)", 7))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_UV.set(
            ((IMetaTileEntity) makeOutputBus(aStartID++, "hatch.superbus.output.tier.08", "Super Bus (O) (UV)", 8))
                .getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_MAX.set(
            ((IMetaTileEntity) makeOutputBus(aStartID, "hatch.superbus.output.tier.09", "Super Bus (O) (UHV)", 9))
                .getStackForm(1L));
    }

    private static MTEHatchSuperBusInput makeInputBus(int id, String unlocalizedName, String localizedName, int tier) {
        return new MTEHatchSuperBusInput(id, unlocalizedName, localizedName, tier);
    }

    private static MTESuperBusOutput makeOutputBus(int id, String unlocalizedName, String localizedName, int tier) {
        return new MTESuperBusOutput(id, unlocalizedName, localizedName, tier);
    }

    private static MTEHatchChiselBus makeChiselBus(int id, String unlocalizedName, String localizedName, int tier) {
        return new MTEHatchChiselBus(id, unlocalizedName, localizedName, tier);
    }

    private static void run4() {
        int aID = 31778; // 31778 - 31780

        GregtechItemList.GT_MetaTileEntity_ChiselBus_LV
            .set((makeChiselBus(aID++, "hatch.chisel.tier.01", "Chisel Bus I", 1)).getStackForm(1L));
        GregtechItemList.GT_MetaTileEntity_ChiselBus_MV
            .set((makeChiselBus(aID++, "hatch.chisel.tier.02", "Chisel Bus II", 2)).getStackForm(1L));
        GregtechItemList.GT_MetaTileEntity_ChiselBus_HV
            .set((makeChiselBus(aID++, "hatch.chisel.tier.03", "Chisel Bus III", 3)).getStackForm(1L));

    }

    private static void run6() {
        int aID = 31781; // 31781-31784

        GregtechItemList.GT_MetaTileEntity_Solidifier_I
            .set(new MTEHatchSolidifier(aID++, "hatch.solidifier.tier.05", "Solidifier Hatch I", 5).getStackForm(1L));
        GregtechItemList.GT_MetaTileEntity_Solidifier_II
            .set(new MTEHatchSolidifier(aID++, "hatch.solidifier.tier.06", "Solidifier Hatch II", 6).getStackForm(1L));
        GregtechItemList.GT_MetaTileEntity_Solidifier_III
            .set(new MTEHatchSolidifier(aID++, "hatch.solidifier.tier.07", "Solidifier Hatch III", 7).getStackForm(1L));
        GregtechItemList.GT_MetaTileEntity_Solidifier_IV
            .set(new MTEHatchSolidifier(aID, "hatch.solidifier.tier.08", "Solidifier Hatch IV", 8).getStackForm(1L));
    }

}
