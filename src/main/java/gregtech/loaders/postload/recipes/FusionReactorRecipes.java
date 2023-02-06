package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class FusionReactorRecipes implements Runnable {

    @Override
    public void run() {
        // Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        // Fusion with margin 32700 65450 131000
        // Startup max 160M EU 320M EU 640M EU
        // Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        // FT1, FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than
        // available on the tier)
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lithium.getMolten(16),
                Materials.Tungsten.getMolten(16),
                Materials.Iridium.getMolten(16),
                64,
                32700,
                300000000); // FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Deuterium.getGas(125),
                Materials.Tritium.getGas(125),
                Materials.Helium.getPlasma(125),
                16,
                4096,
                40000000); // FT1 Cheap - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Deuterium.getGas(125),
                Materials.Helium_3.getGas(125),
                Materials.Helium.getPlasma(125),
                16,
                2048,
                60000000); // FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Carbon.getMolten(125),
                Materials.Helium_3.getGas(125),
                Materials.Oxygen.getPlasma(125),
                32,
                4096,
                80000000); // FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Aluminium.getMolten(16),
                Materials.Lithium.getMolten(16),
                Materials.Sulfur.getPlasma(144),
                32,
                10240,
                240000000); // FT1+ Cheap
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Beryllium.getMolten(16),
                Materials.Deuterium.getGas(375),
                Materials.Nitrogen.getPlasma(125),
                16,
                16384,
                180000000); // FT1+ Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silicon.getMolten(16),
                Materials.Magnesium.getMolten(16),
                Materials.Iron.getPlasma(144),
                32,
                8192,
                360000000); // FT1++ Cheap //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Potassium.getMolten(16),
                Materials.Fluorine.getGas(144),
                Materials.Nickel.getPlasma(144),
                16,
                32700,
                480000000); // FT1++ Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Beryllium.getMolten(16),
                Materials.Tungsten.getMolten(16),
                Materials.Platinum.getMolten(16),
                32,
                32700,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Neodymium.getMolten(16),
                Materials.Hydrogen.getGas(48),
                Materials.Europium.getMolten(16),
                32,
                24576,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lutetium.getMolten(16),
                Materials.Chrome.getMolten(16),
                Materials.Americium.getMolten(16),
                96,
                49152,
                200000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Plutonium.getMolten(16),
                Materials.Thorium.getMolten(16),
                Materials.Naquadah.getMolten(16),
                64,
                32700,
                300000000); // FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Americium.getMolten(144),
                Materials.Naquadria.getMolten(144),
                Materials.Neutronium.getMolten(144),
                240,
                122880,
                640000000); // FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Glowstone.getMolten(16),
                Materials.Helium.getPlasma(4),
                Materials.Sunnarium.getMolten(16),
                32,
                7680,
                40000000); // Mark 1 Expensive //

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tungsten.getMolten(16),
                Materials.Helium.getGas(16),
                Materials.Osmium.getMolten(16),
                256,
                24578,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Manganese.getMolten(16),
                Materials.Hydrogen.getGas(16),
                Materials.Iron.getMolten(16),
                64,
                8192,
                120000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Magnesium.getMolten(128),
                Materials.Oxygen.getGas(128),
                Materials.Calcium.getPlasma(16),
                128,
                8192,
                120000000); //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Mercury.getFluid(16),
                Materials.Magnesium.getMolten(16),
                Materials.Uranium.getMolten(16),
                64,
                49152,
                240000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gold.getMolten(16),
                Materials.Aluminium.getMolten(16),
                Materials.Uranium.getMolten(16),
                64,
                49152,
                240000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Uranium.getMolten(16),
                Materials.Helium.getGas(16),
                Materials.Plutonium.getMolten(16),
                128,
                49152,
                480000000); // FT2+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Vanadium.getMolten(16),
                Materials.Hydrogen.getGas(125),
                Materials.Chrome.getMolten(16),
                64,
                24576,
                140000000); // FT1 - utility

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gallium.getMolten(16),
                Materials.Radon.getGas(125),
                Materials.Duranium.getMolten(16),
                64,
                16384,
                140000000);
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Titanium.getMolten(48),
                Materials.Duranium.getMolten(32),
                Materials.Tritanium.getMolten(16),
                64,
                32700,
                200000000);
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tantalum.getMolten(16),
                Materials.Tritium.getGas(16),
                Materials.Tungsten.getMolten(16),
                16,
                24576,
                200000000); //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silver.getMolten(16),
                Materials.Lithium.getMolten(16),
                Materials.Indium.getMolten(16),
                32,
                24576,
                380000000); //

        // NEW RECIPES FOR FUSION
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Magnesium.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Argon.getPlasma(125),
                32,
                24576,
                180000000); // FT1+ - utility

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Copper.getMolten(72),
                Materials.Tritium.getGas(250),
                Materials.Zinc.getPlasma(72),
                16,
                49152,
                180000000); // FT2 - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Cobalt.getMolten(144),
                Materials.Silicon.getMolten(144),
                Materials.Niobium.getPlasma(144),
                16,
                49152,
                200000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gold.getMolten(144),
                Materials.Arsenic.getMolten(144),
                Materials.Silver.getPlasma(144),
                16,
                49152,
                350000000); // FT2+
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silver.getMolten(144),
                Materials.Helium_3.getGas(375),
                Materials.Tin.getPlasma(144),
                16,
                49152,
                280000000); // FT2
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tungsten.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Mercury.getPlasma(144),
                16,
                49152,
                300000000); // FT2

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tantalum.getMolten(144),
                Materials.Zinc.getPlasma(72),
                Materials.Bismuth.getPlasma(144),
                16,
                98304,
                350000000); // FT3 - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Caesium.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Promethium.getMolten(144),
                64,
                49152,
                400000000); // FT3
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Iridium.getMolten(144),
                Materials.Fluorine.getGas(500),
                Materials.Radon.getPlasma(144),
                32,
                98304,
                450000000); // FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Plutonium241.getMolten(144),
                Materials.Hydrogen.getGas(2000),
                Materials.Americium.getPlasma(144),
                64,
                98304,
                500000000); // FT3
        // GT_Values.RA.addFusionReactorRecipe(Materials.Neutronium.getMolten(144), Materials.Neutronium.getMolten(144),
        // Materials.Neutronium.getPlasma(72), 64, 130000, 640000000);//FT3+ - yes it is a bit troll XD

    }
}
