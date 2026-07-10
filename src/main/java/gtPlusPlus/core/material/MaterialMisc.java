package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MaterialMisc {

    /*
     * Some of these materials purely exist as data objects, items will most likely be assigned separately. Most are
     * just compositions which will have dusts assigned to them.
     */

    public static void run() {
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(POTASSIUM_NITRATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_NITRATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_OXIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_HYDROXIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(CYANOACETIC_ACID, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_CYANIDE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(COPPER_SULFATE, false);
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(COPPER_SULFATE_HYDRATED, false);
        WATER.registerComponentForMaterial(Materials.Water.getFluid(1_000));
    }

    public static final Material POTASSIUM_NITRATE = MaterialReconstruction.byName("PotassiumNitrate");
    public static final Material SODIUM_NITRATE = MaterialReconstruction.byName("SodiumNitrate");

    public static final Material SOLAR_SALT_COLD = MaterialReconstruction.byName("SolarSaltCold");

    public static final Material SOLAR_SALT_HOT = MaterialReconstruction.byName("SolarSaltHot");

    public static final Material STRONTIUM_OXIDE = MaterialReconstruction.byName("StrontiumOxide");

    public static final Material SELENIUM_DIOXIDE = MaterialReconstruction.byName("SeleniumDioxide");

    public static final Material SELENIOUS_ACID = MaterialReconstruction.byName("SeleniousAcid");

    public static final Material HYDROGEN_CYANIDE = MaterialReconstruction.byName("HydrogenCyanide");

    public static final Material WOODS_GLASS = MaterialReconstruction.byName("Wood'sGlass");

    /*
     * Rare Earth Materials
     */

    public static final Material RARE_EARTH_LOW = MaterialReconstruction.byName("RareEarthI");

    public static final Material RARE_EARTH_MID = MaterialReconstruction.byName("RareEarthII");

    public static final Material RARE_EARTH_HIGH = MaterialReconstruction.byName("RareEarthIII");

    public static final Material WATER = MaterialReconstruction.byName("Water");

    // NH3
    public static final Material AMMONIA = MaterialReconstruction.byName("Ammonia");

    // NH4
    public static final Material AMMONIUM = MaterialReconstruction.byName("Ammonium");

    public static final Material HYDROGEN_CHLORIDE_MIX = MaterialReconstruction.byName("HydrogenChlorideMix");

    public static final Material SODIUM_CHLORIDE = MaterialReconstruction.byName("SodiumChloride");

    public static final Material SALT_WATER = MaterialReconstruction.byName("SaltWater");

    public static final Material BRINE = MaterialReconstruction.byName("Brine");

    public static final Material STRONTIUM_HYDROXIDE = MaterialReconstruction.byName("StrontiumHydroxide");

    // Glue Chemicals

    public static final Material ACETIC_ANHYDRIDE = MaterialReconstruction.byName("AceticAnhydride");

    public static final Material CHLOROACETIC_ACID = MaterialReconstruction.byName("ChloroaceticAcid");

    public static final Material DICHLOROACETIC_ACID = MaterialReconstruction.byName("DichloroaceticAcid");

    public static final Material TRICHLOROACETIC_ACID = MaterialReconstruction.byName("TrichloroaceticAcid");

    public static final Material CHLOROACETIC_MIXTURE = MaterialReconstruction.byName("ChloroaceticMixture");

    public static final Material SODIUM_CYANIDE = MaterialReconstruction.byName("SodiumCyanide");

    public static final Material CYANOACETIC_ACID = MaterialReconstruction.byName("CyanoaceticAcid");

    public static final Material SOLID_ACID_MIXTURE = MaterialReconstruction.byName("SolidAcidCatalystMixture");

    public static final Material COPPER_SULFATE = MaterialReconstruction.byName("CopperIISulfate");

    public static final Material COPPER_SULFATE_HYDRATED = MaterialReconstruction.byName("CopperIISulfatePentahydrate");

    public static final Material ETHYL_CYANOACETATE = MaterialReconstruction.byName("EthylCyanoacetate");

    public static final Material CYANOACRYLATE_POLYMER = MaterialReconstruction.byName("CyanoacrylatePolymer");

    public static final Material ETHYL_CYANOACRYLATE = MaterialReconstruction.byName("EthylCyanoacrylateSuperGlue");

    public static final Material MUTATED_LIVING_SOLDER = MaterialReconstruction.byName("MutatedLivingSolder");
}
