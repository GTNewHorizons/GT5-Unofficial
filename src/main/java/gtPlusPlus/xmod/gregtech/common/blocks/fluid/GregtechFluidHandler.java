package gtPlusPlus.xmod.gregtech.common.blocks.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechFluidHandler {

    protected static int cellID = 0;

    public static void run() {
        start();
    }

    private static void start() {
        Logger.INFO("Adding in our own GT versions of Thermal Foundation Fluids if they do not already exist.");
        if (!FluidRegistry.isFluidRegistered("cryotheum")) {
            FluidUtils.addGtFluid(
                "cryotheum",
                "Gelid Cryotheum",
                GT_Materials.Cryotheum,
                4,
                5,
                GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.Cryotheum, 1L),
                ItemUtils.getEmptyCell(),
                1000);
        }
        if (!FluidRegistry.isFluidRegistered("pyrotheum")) {
            FluidUtils.addGtFluid(
                "pyrotheum",
                "Blazing Pyrotheum",
                GT_Materials.Pyrotheum,
                4,
                4000,
                GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.Pyrotheum, 1L),
                ItemUtils.getEmptyCell(),
                1000);
        }
        if (!FluidRegistry.isFluidRegistered("ender")) {
            FluidUtils.addGtFluid(
                "ender",
                "Resonant Ender",
                GT_Materials.Ender,
                4,
                4000,
                GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.Ender, 1L),
                ItemUtils.getEmptyCell(),
                1000);
        }
        Logger.INFO("Adding in GT Fluids for various nuclear related content.");

        FluidUtils.addGtFluid(
            "hydrofluoricAcid",
            "Industrial Strength Hydrofluoric Acid",
            GT_Materials.HydrofluoricAcid,
            1,
            120,
            GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrofluoricAcid, 1L),
            ItemUtils.getEmptyCell(),
            1000,
            false);
        generateIC2FluidCell("HydrofluoricAcid");

        FluidUtils.generateFluidNoPrefix(
            "SulfurDioxide",
            "High Quality Sulfur Dioxide",
            263,
            GT_Materials.SulfurDioxide.mRGBa);

        FluidUtils.addGtFluid(
            "sulfurousAcid",
            "Sulfurous Acid",
            GT_Materials.SulfurousAcid,
            4,
            75,
            GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfurousAcid, 1L),
            ItemUtils.getEmptyCell(),
            1000,
            false);
        generateIC2FluidCell("SulfurousAcid");

        FluidUtils.addGtFluid(
            "sulfuricApatite",
            "Sulfuric Apatite Mix",
            GT_Materials.SulfuricApatite,
            4,
            500,
            GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricApatite, 1L),
            ItemUtils.getEmptyCell(),
            1000,
            false);
        generateIC2FluidCell("SulfuricApatite");

        // Check for Hydrogen Chloride
        if (FluidUtils.getFluidStack("hydrogenchloride", 1) == null) {
            FluidUtils.addGtFluid(
                "hydrogenChloride",
                "Industrial Strength Hydrogen Chloride",
                GT_Materials.HydrogenChloride,
                4,
                75,
                GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.HydrogenChloride, 1L),
                ItemUtils.getEmptyCell(),
                1000,
                false);
            generateIC2FluidCell("HydrogenChloride");
        }

        FluidUtils.addGtFluid(
            "sulfuricLithium",
            "Sulfuric Lithium Mix",
            GT_Materials.SulfuricLithium,
            4,
            280,
            GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.SulfuricLithium, 1L),
            ItemUtils.getEmptyCell(),
            1000,
            false);
        generateIC2FluidCell("SulfuricLithium");

        FluidUtils.addGtFluid(
            "lithiumHydroxide",
            "Lithium Hydroxide",
            GT_Materials.LithiumHydroxide,
            4,
            500,
            GTOreDictUnificator.get(OrePrefixes.cell, GT_Materials.LithiumHydroxide, 1L),
            ItemUtils.getEmptyCell(),
            1000,
            false);
        generateIC2FluidCell("LithiumHydroxide");
    }

    private static ItemStack generateIC2FluidCell(final String fluidNameWithCaps) {
        Logger.INFO("Adding a Cell for " + fluidNameWithCaps);
        return Utils.createInternalNameAndFluidCell(fluidNameWithCaps);
    }

}
