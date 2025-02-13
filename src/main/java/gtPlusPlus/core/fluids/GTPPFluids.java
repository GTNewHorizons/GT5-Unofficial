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

        // Sewage
        ManureSlurry = FluidUtils.generateFluidNonMolten(
            "manure.slurry",
            "Manure Slurry",
            39 + 175,
            new short[] { 75, 45, 15, 100 },
            null,
            null,
            0,
            true);

        // Sewage
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
