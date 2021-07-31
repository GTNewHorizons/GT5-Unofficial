package GoodGenerator.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static GoodGenerator.Loader.Loaders.*;

public final class ItemRefer {

    public static ItemStack Radiation_Protection_Plate = getItemStack(radiationProtectionPlate);
    public static ItemStack Wrapped_Uranium_Ingot = getItemStack(wrappedUraniumIngot);
    public static ItemStack High_Density_Uranium_Nugget = getItemStack(highDensityUraniumNugget);
    public static ItemStack High_Density_Uranium = getItemStack(highDensityUranium);
    public static ItemStack Wrapped_Thorium_Ingot = getItemStack(wrappedThoriumIngot);
    public static ItemStack High_Density_Thorium_Nugget = getItemStack(highDensityThoriumNugget);
    public static ItemStack High_Density_Thorium = getItemStack(highDensityThorium);
    public static ItemStack Wrapped_Plutonium_Ingot = getItemStack(wrappedPlutoniumIngot);
    public static ItemStack High_Density_Plutonium_Nugget = getItemStack(highDensityPlutoniumNugget);
    public static ItemStack High_Density_Plutonium = getItemStack(highDensityPlutonium);
    public static ItemStack Raw_Atomic_Separation_Catalyst = getItemStack(rawAtomicSeparationCatalyst);
    public static ItemStack Advanced_Radiation_Protection_Plate = getItemStack(advancedRadiationProtectionPlate);
    public static ItemStack Aluminum_Nitride_Dust = getItemStack(aluminumNitride);
    public static ItemStack Special_Ceramics_Dust = getItemStack(specialCeramics);
    public static ItemStack Special_Ceramics_Plate = getItemStack(specialCeramicsPlate);

    public static ItemStack Field_Restriction_Casing = getItemStack(MAR_Casing);
    public static ItemStack Naquadah_Fuel_Refinery_Casing = getItemStack(FRF_Casings);
    public static ItemStack Field_Restriction_Coil_T1 = getItemStack(FRF_Coil_1);
    public static ItemStack Field_Restriction_Coil_T2 = getItemStack(FRF_Coil_2);
    public static ItemStack Field_Restriction_Coil_T3 = getItemStack(FRF_Coil_3);
    public static ItemStack Radiation_Proof_Steel_Frame_Box = getItemStack(radiationProtectionSteelFrame);
    public static ItemStack Field_Restriction_Glass = getItemStack(fieldRestrictingGlass);
    public static ItemStack Raw_Cylinder = getItemStack(rawCylinder);
    public static ItemStack Titanium_Plated_Cylinder = getItemStack(titaniumPlatedCylinder);
    public static ItemStack Magic_Casing = getItemStack(magicCasing);
    public static ItemStack Essentia_Cell_T1 = getItemStack(essentiaCell, 0);
    public static ItemStack Essentia_Cell_T2 = getItemStack(essentiaCell, 1);
    public static ItemStack Essentia_Cell_T3 = getItemStack(essentiaCell, 2);
    public static ItemStack Essentia_Hatch = getItemStack(essentiaHatch);

    public static ItemStack Large_Naquadah_Reactor = MAR;
    public static ItemStack Naquadah_Fuel_Refinery = FRF;
    public static ItemStack Universal_Chemical_Fuel_Engine = UCFE;
    public static ItemStack Large_Essentia_Generator = LEG;

    private static ItemStack getItemStack(Item item) {
        return getItemStack(item, 0);
    }

    private static ItemStack getItemStack(Item item, int meta) {
        if (item == null) return null;
        return new ItemStack(item, 1, meta);
    }

    private static ItemStack getItemStack(Block block) {
        return getItemStack(block, 0);
    }

    private static ItemStack getItemStack(Block block, int meta) {
        if (block == null) return null;
        return new ItemStack(block, 1, meta);
    }
}
