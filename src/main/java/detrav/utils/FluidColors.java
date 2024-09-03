package detrav.utils;

import static detrav.net.ProspectingPacket.fluidColors;

import java.util.Arrays;
import java.util.Objects;

import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.Materials;

public class FluidColors {

    public static void makeColors() {

        reFillFluidColors();

        // ProgressManager.ProgressBar progressBar = ProgressManager.push("Making Fluid Colors", (int)
        // (Materials.values().length +
        // FluidRegistry.getRegisteredFluids().values().stream().filter(Objects::nonNull).count()));
        Arrays.stream(Materials.values())
            .forEach(mat -> {
                // progressBar.step(mat.mDefaultLocalName);
                if (mat.getSolid(0) != null) fluidColors.putIfAbsent(
                    mat.getSolid(0)
                        .getFluidID(),
                    mat.mRGBa);
                if (mat.getGas(0) != null) fluidColors.putIfAbsent(
                    mat.getGas(0)
                        .getFluidID(),
                    mat.mRGBa);
                if (mat.getFluid(0) != null) fluidColors.putIfAbsent(
                    mat.getFluid(0)
                        .getFluidID(),
                    mat.mRGBa);
                if (mat.getMolten(0) != null) fluidColors.putIfAbsent(
                    mat.getMolten(0)
                        .getFluidID(),
                    mat.mRGBa);
            });
        FluidRegistry.getRegisteredFluids()
            .values()
            .stream()
            .filter(Objects::nonNull)
            .forEach(fluid -> {
                // progressBar.step(fluid.getName());
                fluidColors.putIfAbsent(fluid.getID(), convertColorInt(fluid.getColor()));
            });
        // ProgressManager.pop(progressBar);
    }

    private static void reFillFluidColors() {
        try {
            // Should probably be put somewhere else, but I suck at Java
            fluidColors.put(Materials.NatruralGas.mGas.getID(), new short[] { 0x00, 0xff, 0xff });
            fluidColors.put(Materials.OilLight.mFluid.getID(), new short[] { 0xff, 0xff, 0x00 });
            fluidColors.put(Materials.OilMedium.mFluid.getID(), new short[] { 0x00, 0xFF, 0x00 });
            fluidColors.put(Materials.OilHeavy.mFluid.getID(), new short[] { 0xFF, 0x00, 0xFF });
            fluidColors.put(Materials.Oil.mFluid.getID(), new short[] { 0x00, 0x00, 0x00 });
            fluidColors.put(Materials.Helium_3.mGas.getID(), new short[] { 0x80, 0x20, 0xe0 });
            fluidColors.put(Materials.SaltWater.mFluid.getID(), new short[] { 0x80, 0xff, 0x80 });
            // fluidColors.put(Materials.Naquadah.getMolten(0).getFluid().getID(), new short[]{0x20, 0x20, 0x20});
            // fluidColors.put(Materials.NaquadahEnriched.getMolten(0).getFluid().getID(), new short[]{0x60, 0x60,
            // 0x60});
            fluidColors.put(
                Materials.Lead.getMolten(0)
                    .getFluid()
                    .getID(),
                new short[] { 0xd0, 0xd0, 0xd0 });
            fluidColors.put(Materials.Chlorobenzene.mFluid.getID(), new short[] { 0x40, 0x80, 0x40 });
            fluidColors.put(
                FluidRegistry.getFluid("liquid_extra_heavy_oil")
                    .getID(),
                new short[] { 0x00, 0x00, 0x50 });
            fluidColors.put(Materials.Oxygen.mGas.getID(), new short[] { 0x40, 0x40, 0xA0 });
            fluidColors.put(Materials.Nitrogen.mGas.getID(), new short[] { 0x00, 0x80, 0xd0 });
            fluidColors.put(Materials.Methane.mGas.getID(), new short[] { 0x80, 0x20, 0x20 });
            fluidColors.put(Materials.Ethane.mGas.getID(), new short[] { 0x40, 0x80, 0x20 });
            fluidColors.put(Materials.Ethylene.mGas.getID(), new short[] { 0xd0, 0xd0, 0xd0 });
            fluidColors.put(FluidRegistry.LAVA.getID(), new short[] { 0xFF, 0x00, 0x00 });
            fluidColors.put(
                FluidRegistry.getFluid("unknowwater")
                    .getID(),
                new short[] { 0x8A, 0x2B, 0xE2 });
            fluidColors.put(Materials.Hydrogen.mGas.getID(), new short[] { 0x32, 0x32, 0xD6 });
            fluidColors.put(Materials.SulfuricAcid.mFluid.getID(), new short[] { 0xFF, 0xB9, 0x0F });
            fluidColors.put(Materials.HydricSulfide.mFluid.getID(), new short[] { 0xFF, 0x8F, 0x43 });
            fluidColors.put(Materials.CarbonMonoxide.mGas.getID(), new short[] { 0x10, 0x4E, 0x8B });
            fluidColors.put(Materials.CarbonDioxide.mGas.getID(), new short[] { 0x69, 0x69, 0x69 });
            fluidColors.put(
                FluidRegistry.getFluid("ic2distilledwater")
                    .getID(),
                new short[] { 0x1E, 0x90, 0xFF });
            fluidColors.put(Materials.Deuterium.mGas.getID(), new short[] { 0xFF, 0xE3, 0x9F });
            fluidColors.put(
                Materials.Iron.getMolten(0)
                    .getFluid()
                    .getID(),
                new short[] { 0x8B, 0x88, 0x78 });
            fluidColors.put(
                Materials.Tin.getMolten(0)
                    .getFluid()
                    .getID(),
                new short[] { 0xE7, 0xE7, 0xE4 });
            fluidColors.put(
                Materials.Copper.getMolten(0)
                    .getFluid()
                    .getID(),
                new short[] { 0xFF, 0x7F, 0x24 });
            fluidColors.put(
                FluidRegistry.getFluid("fluorine")
                    .getID(),
                new short[] { 0x99, 0xC1, 0xAD });
            fluidColors.put(
                FluidRegistry.getFluid("hydrofluoricacid")
                    .getID(),
                new short[] { 0x00, 0xCE, 0xD1 });
            fluidColors.put(Materials.PhosphoricAcid.mFluid.getID(), new short[] { 0xEE, 0x76, 0x00 });

            // possible nulls
            fluidColors.put(Materials.LiquidAir.mFluid.getID(), new short[] { 0x99, 0x99, 0xEA });
        } catch (Exception ignored) {}
        /*
         * Set set = fluidColors.entrySet(); Iterator iterator = set.iterator(); System.out.println(
         * "DETRAV SCANNER DEBUG" ); while(iterator.hasNext()) { Map.Entry mentry = (Map.Entry) iterator.next();
         * System.out.println( "key is: "+ (Integer)mentry.getKey() + " & Value is: " + ((short[])mentry.getValue())[0]
         * + " " + ((short[])mentry.getValue())[1] + " " + ((short[])mentry.getValue())[2] ); }
         */
    }

    private static short[] convertColorInt(int color) {
        return new short[] { (short) (color << 16 & 0xff), (short) (color << 8 & 0xff), (short) (color & 0xff) };
    }

}
