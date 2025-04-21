package gtPlusPlus.core.fluids;

import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.TinkerConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class GTPPFluids {

    // Agricultural Chem
    public static Fluid PoopJuice;
    public static Fluid ManureSlurry;
    public static Fluid FertileManureSlurry;
    public static Fluid RedMud;
    public static Fluid PropionicAcid;
    public static Fluid FermentationBase;
    public static Fluid Urea;
    public static Fluid LiquidResin;
    public static Fluid Butanol;

    // Milling
    public static Fluid SphaleriteFlotationFroth;
    public static Fluid ChalcopyriteFlotationFroth;
    public static Fluid NickelFlotationFroth;
    public static Fluid PlatinumFlotationFroth;
    public static Fluid PentlanditeFlotationFroth;
    public static Fluid RedstoneFlotationFroth;
    public static Fluid SpessartineFlotationFroth;
    public static Fluid GrossularFlotationFroth;
    public static Fluid AlmandineFlotationFroth;
    public static Fluid PyropeFlotationFroth;
    public static Fluid MonaziteFlotationFroth;

    public static Fluid PineOil;

    // Coal Tar
    public static Fluid CoalGas;
    public static Fluid Ethylbenzene;
    public static Fluid Anthracene;
    public static Fluid CoalTar;
    public static Fluid CoalTarOil;
    public static Fluid SulfuricCoalTarOil;
    public static Fluid Naphthalene;

    // General Chem
    public static Fluid Nitrobenzene;
    public static Fluid Aniline;
    public static Fluid BoricAcid;
    public static Fluid Polyurethane;
    public static Fluid Cyclohexane;
    public static Fluid Cyclohexanone;
    public static Fluid Cadaverine;
    public static Fluid Putrescine;
    public static Fluid Ethylanthraquinone;
    public static Fluid Ethylanthrahydroquinone;
    public static Fluid HydrogenPeroxide;
    public static Fluid LithiumPeroxide;
    public static Fluid CarbonDisulfide;

    // Rocket Fuel
    public static Fluid Kerosene;
    public static Fluid RP1;
    public static Fluid NitrogenTetroxide;
    public static Fluid Hydrazine;
    public static Fluid Monomethylhydrazine;
    public static Fluid HydratedAmmoniumNitrateSlurry;
    public static Fluid LiquidHydrogen;
    public static Fluid Formaldehyde;
    public static Fluid H8N4C2O4RocketFuel;
    public static Fluid RP1RocketFuel;
    public static Fluid CN3H7O3RocketFuel;
    public static Fluid DenseHydrazineFuelMixture;

    // Nuclear
    public static Fluid BurntLiFBeF2ThF4UF4Salt;
    public static Fluid BurntLiFBeF2ZrF4UF4Salt;
    public static Fluid BurntLiFBeF2ZrF4U235Salt;
    public static Fluid ImpureMoltenSaltBase;

    // Misc
    public static Fluid GeneticMutagen;

    // Custom Blood Support
    private static final List<FluidStack> bloodFluids = new ArrayList<>();

    public static void init() {
        PoopJuice = FluidUtils.generateFluidNonMolten(
            "raw.waste",
            "Raw Animal Waste",
            32 + 175,
            new short[] { 100, 70, 30, 100 },
            null,
            null,
            0,
            true);

        ManureSlurry = FluidUtils.generateFluidNonMolten(
            "manure.slurry",
            "Manure Slurry",
            39 + 175,
            new short[] { 75, 45, 15, 100 },
            null,
            null,
            0,
            true);

        FertileManureSlurry = FluidUtils.generateFluidNonMolten(
            "fertile.manure.slurry",
            "Fertile Manure Slurry",
            45 + 175,
            new short[] { 65, 50, 15, 100 },
            null,
            null,
            0,
            true);

        RedMud = FluidUtils.generateFluidNoPrefix(
            "mud.red.slurry",
            "Red Mud Slurry",
            32 + 175,
            new short[] { 180, 35, 25, 100 },
            true);

        PropionicAcid = FluidUtils
            .generateFluidNoPrefix("propionicacid", "Propionic Acid", 200, new short[] { 198, 209, 148, 100 }, true);

        FermentationBase = FluidUtils.generateFluidNoPrefix(
            "fermentation.base",
            "Fermentation Base",
            200,
            new short[] { 107, 100, 63, 100 },
            true);

        Urea = FluidUtils.generateFluidNoPrefix("ureamix", "Urea Mix", 200, new short[] { 71, 55, 12, 100 }, true);

        LiquidResin = FluidUtils
            .generateFluidNoPrefix("liquidresin", "Liquid Resin", 200, new short[] { 59, 58, 56, 100 }, true);

        Butanol = FluidUtils.generateFluidNoPrefix("butanol", "Butanol", 200, new short[] { 159, 58, 56, 100 }, true);

        short[] aZincFrothRGB = Materials.Sphalerite.mRGBa;
        SphaleriteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.zincflotation",
            "Sphalerite Froth",
            32 + 175,
            new short[] { aZincFrothRGB[0], aZincFrothRGB[1], aZincFrothRGB[2], 100 },
            true);

        short[] aCopperFrothRGB = Materials.Chalcopyrite.mRGBa;
        ChalcopyriteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.copperflotation",
            "Chalcopyrite Froth",
            32 + 175,
            new short[] { aCopperFrothRGB[0], aCopperFrothRGB[1], aCopperFrothRGB[2], 100 },
            true);

        short[] aNickelFrothRGB = Materials.Nickel.mRGBa;
        NickelFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.nickelflotation",
            "Nickel Froth",
            32 + 175,
            new short[] { aNickelFrothRGB[0], aNickelFrothRGB[1], aNickelFrothRGB[2], 100 },
            true);

        short[] aPlatinumFrothRGB = Materials.Platinum.mRGBa;
        PlatinumFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.platinumflotation",
            "Platinum Froth",
            32 + 175,
            new short[] { aPlatinumFrothRGB[0], aPlatinumFrothRGB[1], aPlatinumFrothRGB[2], 100 },
            true);

        short[] aPentlanditeFrothRGB = Materials.Pentlandite.mRGBa;
        PentlanditeFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.pentlanditeflotation",
            "Pentlandite Froth",
            32 + 175,
            new short[] { aPentlanditeFrothRGB[0], aPentlanditeFrothRGB[1], aPentlanditeFrothRGB[2], 100 },
            true);

        short[] aRedstoneFrothRGB = Materials.Redstone.mRGBa;
        RedstoneFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.redstoneflotation",
            "Redstone Froth",
            32 + 175,
            new short[] { aRedstoneFrothRGB[0], aRedstoneFrothRGB[1], aRedstoneFrothRGB[2], 100 },
            true);

        short[] aSpessartineFrothRGB = Materials.Spessartine.mRGBa;
        SpessartineFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.spessartineflotation",
            "Spessartine Froth",
            32 + 175,
            new short[] { aSpessartineFrothRGB[0], aSpessartineFrothRGB[1], aSpessartineFrothRGB[2], 100 },
            true);

        short[] aGrossularFrothRGB = Materials.Grossular.mRGBa;
        GrossularFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.grossularflotation",
            "Grossular Froth",
            32 + 175,
            new short[] { aGrossularFrothRGB[0], aGrossularFrothRGB[1], aGrossularFrothRGB[2], 100 },
            true);

        short[] aAlmandineFrothRGB = Materials.Almandine.mRGBa;
        AlmandineFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.almandineflotation",
            "Almandine Froth",
            32 + 175,
            new short[] { aAlmandineFrothRGB[0], aAlmandineFrothRGB[1], aAlmandineFrothRGB[2], 100 },
            true);

        short[] aPyropeFrothRGB = Materials.Pyrope.mRGBa;
        PyropeFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.pyropeflotation",
            "Pyrope Froth",
            32 + 175,
            new short[] { aPyropeFrothRGB[0], aPyropeFrothRGB[1], aPyropeFrothRGB[2], 100 },
            true);

        short[] aMonaziteFrothRGB = Materials.Monazite.mRGBa;
        MonaziteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.Monaziteflotation",
            "Monazite Froth",
            32 + 175,
            new short[] { aMonaziteFrothRGB[0], aMonaziteFrothRGB[1], aMonaziteFrothRGB[2], 100 },
            true);

        PineOil = FluidUtils
            .generateFluidNoPrefix("pineoil", "Pine Oil", 32 + 175, new short[] { 250, 200, 60, 100 }, true);

        CoalGas = FluidUtils
            .generateFluidNonMolten("CoalGas", "Coal Gas", 500, new short[] { 48, 48, 48, 100 }, null, null);

        Ethylbenzene = FluidUtils.generateFluidNonMolten(
            "Ethylbenzene",
            "Ethylbenzene",
            136,
            new short[] { 255, 255, 255, 100 },
            null,
            null);

        Anthracene = FluidUtils
            .generateFluidNonMolten("Anthracene", "Anthracene", 340, new short[] { 255, 255, 255, 100 }, null, null);

        CoalTar = FluidUtils
            .generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[] { 32, 32, 32, 100 }, null, null);

        CoalTarOil = FluidUtils
            .generateFluidNonMolten("CoalTarOil", "Coal Tar Oil", 240, new short[] { 240, 240, 150, 100 }, null, null);

        SulfuricCoalTarOil = FluidUtils.generateFluidNonMolten(
            "SulfuricCoalTarOil",
            "Sulfuric Coal Tar Oil",
            240,
            new short[] { 250, 170, 12, 100 },
            null,
            null);

        Naphthalene = FluidUtils
            .generateFluidNonMolten("Naphthalene", "Naphthalene", 115, new short[] { 210, 185, 135, 100 }, null, null);

        Nitrobenzene = FluidUtils
            .generateFluidNoPrefix("nitrobenzene", "Nitrobenzene", 278, new short[] { 70, 50, 40, 100 }, true);

        Aniline = FluidUtils.generateFluidNoPrefix("aniline", "Aniline", 266, new short[] { 100, 100, 30, 100 }, true);

        BoricAcid = FluidUtils
            .generateFluidNoPrefix("boricacid", "Boric Acid", 278, new short[] { 90, 30, 120, 100 }, true);

        Polyurethane = FluidUtils
            .generateFluidNoPrefix("polyurethane", "Polyurethane", 350, new short[] { 100, 70, 100, 100 }, true);

        Cyclohexane = FluidUtils
            .generateFluidNoPrefix("cyclohexane", "Cyclohexane", 32 + 175, new short[] { 100, 70, 30, 100 }, true);

        Cyclohexanone = FluidUtils
            .generateFluidNoPrefix("cyclohexanone", "Cyclohexanone", 32 + 175, new short[] { 100, 70, 30, 100 }, true);

        Cadaverine = FluidUtils
            .generateFluidNoPrefix("cadaverine", "Cadaverine", 32 + 175, new short[] { 100, 70, 30, 100 }, true);

        Putrescine = FluidUtils
            .generateFluidNoPrefix("putrescine", "Putrescine", 32 + 175, new short[] { 100, 70, 30, 100 }, true);

        Ethylanthraquinone = FluidUtils.generateFluidNonMolten(
            "2Ethylanthraquinone",
            "2-Ethylanthraquinone",
            415,
            new short[] { 227, 255, 159, 100 },
            null,
            null);

        Ethylanthrahydroquinone = FluidUtils.generateFluidNonMolten(
            "2Ethylanthrahydroquinone",
            "2-Ethylanthrahydroquinone",
            415,
            new short[] { 207, 225, 129, 100 },
            null,
            null);

        HydrogenPeroxide = FluidUtils.generateFluidNonMolten(
            "HydrogenPeroxide",
            "Hydrogen Peroxide",
            150,
            new short[] { 210, 255, 255, 100 },
            null,
            null);

        LithiumPeroxide = FluidUtils.generateFluidNonMolten(
            "LithiumPeroxide",
            "Lithium Peroxide",
            446,
            new short[] { 135, 135, 135, 100 },
            null,
            null);

        CarbonDisulfide = FluidUtils
            .generateFluidNoPrefix("CarbonDisulfide", "Carbon Disulfide", 350, new short[] { 175, 175, 175, 100 });

        Kerosene = FluidUtils
            .generateFluidNonMolten("Kerosene", "Kerosene", 233, new short[] { 150, 40, 150, 100 }, null, null);

        RP1 = FluidUtils.generateFluidNonMolten("RP1Fuel", "RP-1", 500, new short[] { 210, 50, 50, 100 }, null, null);

        NitrogenTetroxide = FluidUtils.generateFluidNonMolten(
            "NitrogenTetroxide",
            "Nitrogen Tetroxide",
            261,
            new short[] { 170, 170, 0, 100 },
            null,
            null);

        Hydrazine = FluidUtils
            .generateFluidNonMolten("Hydrazine", "Hydrazine", 275, new short[] { 250, 250, 250, 100 }, null, null);

        Monomethylhydrazine = FluidUtils.generateFluidNonMolten(
            "Monomethylhydrazine",
            "Monomethylhydrazine",
            221,
            new short[] { 125, 125, 125, 100 },
            null,
            null);

        HydratedAmmoniumNitrateSlurry = FluidUtils.generateFluidNonMolten(
            "AmmoniumNitrateSlurry",
            "Hydrated Ammonium Nitrate Slurry",
            450,
            new short[] { 150, 75, 150, 100 },
            null,
            null);

        LiquidHydrogen = FluidUtils.generateFluidNonMolten(
            "LiquidHydrogen",
            "Liquid Hydrogen",
            14,
            new short[] { 75, 75, 220, 100 },
            null,
            null);

        Formaldehyde = FluidUtils
            .generateFluidNonMolten("Formaldehyde", "Formaldehyde", 185, new short[] { 150, 75, 150, 100 }, null, null);

        H8N4C2O4RocketFuel = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixA",
            "H8N4C2O4 Rocket Fuel",
            216,
            new short[] { 50, 220, 50, 100 },
            null,
            null);

        RP1RocketFuel = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixB",
            "Rp-1 Rocket Fuel",
            250,
            new short[] { 250, 50, 50, 100 },
            null,
            null);

        CN3H7O3RocketFuel = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixC",
            "CN3H7O3 Rocket Fuel",
            221,
            new short[] { 125, 75, 180, 100 },
            null,
            null);

        DenseHydrazineFuelMixture = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixD",
            "Dense Hydrazine Fuel Mixture",
            275,
            new short[] { 175, 80, 120, 100 },
            null,
            null);

        BurntLiFBeF2ThF4UF4Salt = FluidUtils.generateFluidNonMolten(
            "BurntLiFBeF2ThF4UF4",
            "Burnt LiFBeF2ThF4UF4 Salt",
            545,
            new short[] { 48, 175, 48, 100 },
            null,
            null);

        BurntLiFBeF2ZrF4UF4Salt = FluidUtils.generateFluidNonMolten(
            "BurntLiFBeF2ZrF4UF4",
            "Burnt LiFBeF2ZrF4UF4 Salt",
            520,
            new short[] { 48, 168, 68, 100 },
            null,
            null);

        BurntLiFBeF2ZrF4U235Salt = FluidUtils.generateFluidNonMolten(
            "BurntLiFBeF2ZrF4U235",
            "Burnt LiFBeF2ZrF4U235 Salt",
            533,
            new short[] { 68, 185, 48, 100 },
            null,
            null);

        ImpureMoltenSaltBase = FluidUtils.generateFluidNonMolten(
            "ImpureLiFBeF2",
            "Impure Molten Salt Base",
            533,
            new short[] { 110, 75, 186, 100 },
            null,
            null);

        GeneticMutagen = FluidUtils.generateFluidNonMolten(
            "GeneticMutagen",
            "Genetic Mutagen",
            12,
            new short[] { 22, 148, 185, 100 },
            null,
            null);

        handleBlood();
    }

    private static void handleBlood() {
        if (BiomesOPlenty.isModLoaded()) {
            FluidStack blood = FluidRegistry.getFluidStack("hell_blood", 100);
            if (blood != null) {
                Logger.INFO("Found Biome's o Plenty, enabled Blood support.");
                bloodFluids.add(blood);
            }
        }

        if (TinkerConstruct.isModLoaded()) {
            FluidStack blood = FluidRegistry.getFluidStack("blood", 100);
            if (blood != null) {
                Logger.INFO("Found Tinker's Construct, enabled Blood support.");
                bloodFluids.add(blood);
            }
        }

        // Handle Blood Internally, Create if required.
        if (bloodFluids.isEmpty()) {
            Logger.INFO("Did not find any existing Blood fluids, Generating our own");
            Fluid blood = FluidUtils
                .generateFluidNoPrefix("blood", "Blood", 32 + 175, new short[] { 175, 25, 25, 100 }, true);
            bloodFluids.add(new FluidStack(blood, 100));
        }
    }

    public static List<FluidStack> getBloodFluids() {
        return Collections.unmodifiableList(bloodFluids);
    }
}
