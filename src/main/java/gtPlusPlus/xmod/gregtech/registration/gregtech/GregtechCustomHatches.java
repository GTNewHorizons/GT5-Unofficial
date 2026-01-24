package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static gregtech.api.enums.MetaTileEntityIDs.ChiselBus_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ChiselBus_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ChiselBus_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Air_Intake;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Air_Intake_Atmospheric;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Air_Intake_Extreme;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Extrusion_I;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Extrusion_II;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Extrusion_III;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Extrusion_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Cryotheum;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Debug_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Pyrotheum;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_TurbineHousing;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Muffler_Adv_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Reservoir;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Solidifier_I;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Solidifier_II;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Solidifier_III;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Solidifier_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Input_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_SuperBus_Output_ZPM;

import net.minecraft.util.EnumChatFormatting;

import gregtech.GTMod;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchAirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchAirIntakeAtmosphere;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchAirIntakeExtreme;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchMufflerAdvanced;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchReservoir;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSuperBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbineProvider;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTESuperBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBaseDebug;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class GregtechCustomHatches {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Custom Fluid Hatches.");
        run1();
        if (GTMod.proxy.mPollution) {
            run2();
        }
        run3();
        run4(); // Chisel buses
        run6(); // Solidifier hatches
        run7(); // Extrusion hatches
    }

    private static void run1() {

        GregtechItemList.Hatch_Input_Cryotheum.set(
            new MTEHatchCustomFluidBase(
                TFFluids.fluidCryotheum, // Fluid to restrict hatch to
                128000, // Capacity
                Hatch_Input_Cryotheum.ID, // ID
                "hatch.cryotheum.input.tier.00", // unlocal name
                "Cryotheum Cooling Hatch", // Local name
                5 // Casing Texture
            ).getStackForm(1L));

        GregtechItemList.Hatch_Input_Pyrotheum.set(
            new MTEHatchCustomFluidBase(
                TFFluids.fluidPyrotheum, // Fluid to restrict hatch to
                128000, // Capacity
                Hatch_Input_Pyrotheum.ID, // ID
                "hatch.pyrotheum.input.tier.00", // unlocal name
                "Pyrotheum Heating Vent", // Local name
                5 // Casing texture
            ).getStackForm(1L));

        GregtechItemList.Hatch_Input_TurbineHousing.set(
            new MTEHatchTurbineProvider(
                Hatch_Input_TurbineHousing.ID, // ID
                "hatch.turbine.input.tier.00", // unlocal name
                "Turbine Housing", // Local name
                8).getStackForm(1L));

        // Multiblock Air Intake Hatch
        GregtechItemList.Hatch_Air_Intake.set(
            new MTEHatchAirIntake(Hatch_Air_Intake.ID, "hatch.air.intake.tier.00", "Air Intake Hatch", 5)
                .getStackForm(1L));
        GregtechItemList.Hatch_Air_Intake_Extreme.set(
            new MTEHatchAirIntakeExtreme(
                Hatch_Air_Intake_Extreme.ID,
                "hatch.air.intake.tier.01",
                "Extreme Air Intake Hatch",
                7).getStackForm(1L));
        GregtechItemList.Hatch_Air_Intake_Atmospheric.set(
            new MTEHatchAirIntakeAtmosphere(
                Hatch_Air_Intake_Atmospheric.ID,
                "hatch.air.intake.tier.02",
                "Atmospheric Intake Hatch",
                9).getStackForm(1L));
        addItemTooltip(
            GregtechItemList.Hatch_Air_Intake_Atmospheric.get(1),
            () -> "Author: " + GTAuthors.AuthorNoc.get());

        // Multiblock Reservoir Hatch
        GregtechItemList.Hatch_Reservoir.set(
            new MTEHatchReservoir(Hatch_Reservoir.ID, "hatch.water.intake.tier.00", "Reservoir Hatch", 4)
                .getStackForm(1L));

        // Steam Hatch
        GregtechItemList.Hatch_Input_Steam.set(
            new MTEHatchCustomFluidBase(
                Materials.Steam.getGas(1)
                    .getFluid(), // Fluid to restrict hatch to
                64000, // Capacity
                Hatch_Input_Steam.ID, // ID
                "hatch.steam.input.tier.00", // unlocal name
                "Steam Hatch", // Local name
                0 // Casing texture
            ).getStackForm(1L));
        // Debug Steam Hatch
        GregtechItemList.Hatch_Input_Debug_Steam.set(
            new MTEHatchCustomFluidBaseDebug(
                Materials.Steam.getGas(1)
                    .getFluid(), // Fluid to restrict hatch
                Hatch_Input_Debug_Steam.ID, // ID
                "hatch.steam.input.debug", // unlocal name
                "Debug Steam Hatch", // local name
                0) // casing texture
                    .getStackForm(1));
    }

    private static void run2() {
        GregtechItemList.Hatch_Muffler_Adv_LV.set(
            (new MTEHatchMufflerAdvanced(
                Hatch_Muffler_Adv_LV.ID,
                "hatch.muffler.adv.tier.01",
                "Advanced Muffler Hatch (LV)",
                1)).getStackForm(1L));
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
        String DEPRECATED = EnumChatFormatting.RED + "DEPRECATED! " + EnumChatFormatting.RESET;
        GregtechItemList.Hatch_SuperBus_Input_LV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_LV.ID,
                "hatch.superbus.input.tier.01",
                DEPRECATED + "Super Bus (I) (LV)",
                1)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_MV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_MV.ID,
                "hatch.superbus.input.tier.02",
                DEPRECATED + "Super Bus (I) (MV)",
                2)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_HV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_HV.ID,
                "hatch.superbus.input.tier.03",
                DEPRECATED + "Super Bus (I) (HV)",
                3)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_EV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_EV.ID,
                "hatch.superbus.input.tier.04",
                DEPRECATED + "Super Bus (I) (EV)",
                4)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_IV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_IV.ID,
                "hatch.superbus.input.tier.05",
                DEPRECATED + "Super Bus (I) (IV)",
                5)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_LuV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_LuV.ID,
                "hatch.superbus.input.tier.06",
                DEPRECATED + "Super Bus (I) (LuV)",
                6)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_ZPM.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_ZPM.ID,
                "hatch.superbus.input.tier.07",
                DEPRECATED + "Super Bus (I) (ZPM)",
                7)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_UV.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_UV.ID,
                "hatch.superbus.input.tier.08",
                DEPRECATED + "Super Bus (I) (UV)",
                8)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Input_MAX.set(
            ((IMetaTileEntity) makeInputBus(
                Hatch_SuperBus_Input_UHV.ID,
                "hatch.superbus.input.tier.09",
                DEPRECATED + "Super Bus (I) (UHV)",
                9)).getStackForm(1L));

        GregtechItemList.Hatch_SuperBus_Output_LV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_LV.ID,
                "hatch.superbus.output.tier.01",
                DEPRECATED + "Super Bus (O) (LV)",
                1)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_MV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_MV.ID,
                "hatch.superbus.output.tier.02",
                DEPRECATED + "Super Bus (O) (MV)",
                2)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_HV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_HV.ID,
                "hatch.superbus.output.tier.03",
                DEPRECATED + "Super Bus (O) (HV)",
                3)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_EV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_EV.ID,
                "hatch.superbus.output.tier.04",
                DEPRECATED + "Super Bus (O) (EV)",
                4)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_IV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_IV.ID,
                "hatch.superbus.output.tier.05",
                DEPRECATED + "Super Bus (O) (IV)",
                5)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_LuV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_LuV.ID,
                "hatch.superbus.output.tier.06",
                DEPRECATED + "Super Bus (O) (LuV)",
                6)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_ZPM.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_ZPM.ID,
                "hatch.superbus.output.tier.07",
                DEPRECATED + "Super Bus (O) (ZPM)",
                7)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_UV.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_UV.ID,
                "hatch.superbus.output.tier.08",
                DEPRECATED + "Super Bus (O) (UV)",
                8)).getStackForm(1L));
        GregtechItemList.Hatch_SuperBus_Output_MAX.set(
            ((IMetaTileEntity) makeOutputBus(
                Hatch_SuperBus_Output_UHV.ID,
                "hatch.superbus.output.tier.09",
                DEPRECATED + "Super Bus (O) (UHV)",
                9)).getStackForm(1L));
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
        GregtechItemList.ChiselBus_LV
            .set((makeChiselBus(ChiselBus_LV.ID, "hatch.chisel.tier.01", "Chisel Bus I", 1)).getStackForm(1L));
        GregtechItemList.ChiselBus_MV
            .set((makeChiselBus(ChiselBus_MV.ID, "hatch.chisel.tier.02", "Chisel Bus II", 2)).getStackForm(1L));
        GregtechItemList.ChiselBus_HV
            .set((makeChiselBus(ChiselBus_HV.ID, "hatch.chisel.tier.03", "Chisel Bus III", 3)).getStackForm(1L));

    }

    private static void run6() {
        GregtechItemList.Hatch_Solidifier_I.set(
            new MTEHatchSolidifier(Hatch_Solidifier_I.ID, "hatch.solidifier.tier.05", "Solidifier Hatch I", 5)
                .getStackForm(1L));
        GregtechItemList.Hatch_Solidifier_II.set(
            new MTEHatchSolidifier(Hatch_Solidifier_II.ID, "hatch.solidifier.tier.06", "Solidifier Hatch II", 6)
                .getStackForm(1L));
        GregtechItemList.Hatch_Solidifier_III.set(
            new MTEHatchSolidifier(Hatch_Solidifier_III.ID, "hatch.solidifier.tier.07", "Solidifier Hatch III", 7)
                .getStackForm(1L));
        GregtechItemList.Hatch_Solidifier_IV.set(
            new MTEHatchSolidifier(Hatch_Solidifier_IV.ID, "hatch.solidifier.tier.08", "Solidifier Hatch IV", 8)
                .getStackForm(1L));
    }

    private static void run7() {
        GregtechItemList.Hatch_Extrusion_I.set(
            new MTEHatchExtrusion(Hatch_Extrusion_I.ID, "hatch.extrusion.tier.05", "Extrusion Bus I", 5)
                .getStackForm(1L));
        GregtechItemList.Hatch_Extrusion_II.set(
            new MTEHatchExtrusion(Hatch_Extrusion_II.ID, "hatch.extrusion.tier.06", "Extrusion Bus II", 6)
                .getStackForm(1L));
        GregtechItemList.Hatch_Extrusion_III.set(
            new MTEHatchExtrusion(Hatch_Extrusion_III.ID, "hatch.extrusion.tier.07", "Extrusion Bus III", 7)
                .getStackForm(1L));
        GregtechItemList.Hatch_Extrusion_IV.set(
            new MTEHatchExtrusion(Hatch_Extrusion_IV.ID, "hatch.extrusion.tier.08", "Extrusion Bus IV", 8)
                .getStackForm(1L));
    }
}
