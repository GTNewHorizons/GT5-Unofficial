package GoodGenerator.util;

import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static GoodGenerator.Loader.Loaders.*;

public final class ItemRefer {

    public static ItemRefer Radiation_Protection_Plate = getItemStack(radiationProtectionPlate);
    public static ItemRefer Wrapped_Uranium_Ingot = getItemStack(wrappedUraniumIngot);
    public static ItemRefer High_Density_Uranium_Nugget = getItemStack(highDensityUraniumNugget);
    public static ItemRefer High_Density_Uranium = getItemStack(highDensityUranium);
    public static ItemRefer Wrapped_Thorium_Ingot = getItemStack(wrappedThoriumIngot);
    public static ItemRefer High_Density_Thorium_Nugget = getItemStack(highDensityThoriumNugget);
    public static ItemRefer High_Density_Thorium = getItemStack(highDensityThorium);
    public static ItemRefer Wrapped_Plutonium_Ingot = getItemStack(wrappedPlutoniumIngot);
    public static ItemRefer High_Density_Plutonium_Nugget = getItemStack(highDensityPlutoniumNugget);
    public static ItemRefer High_Density_Plutonium = getItemStack(highDensityPlutonium);
    public static ItemRefer Raw_Atomic_Separation_Catalyst = getItemStack(rawAtomicSeparationCatalyst);
    public static ItemRefer Advanced_Radiation_Protection_Plate = getItemStack(advancedRadiationProtectionPlate);
    public static ItemRefer Aluminum_Nitride_Dust = getItemStack(aluminumNitride);
    public static ItemRefer Special_Ceramics_Dust = getItemStack(specialCeramics);
    public static ItemRefer Special_Ceramics_Plate = getItemStack(specialCeramicsPlate);

    public static ItemRefer Field_Restriction_Casing = getItemStack(MAR_Casing);
    public static ItemRefer Naquadah_Fuel_Refinery_Casing = getItemStack(FRF_Casings);
    public static ItemRefer Field_Restriction_Coil_T1 = getItemStack(FRF_Coil_1);
    public static ItemRefer Field_Restriction_Coil_T2 = getItemStack(FRF_Coil_2);
    public static ItemRefer Field_Restriction_Coil_T3 = getItemStack(FRF_Coil_3);
    public static ItemRefer Radiation_Proof_Steel_Frame_Box = getItemStack(radiationProtectionSteelFrame);
    public static ItemRefer Field_Restriction_Glass = getItemStack(fieldRestrictingGlass);
    public static ItemRefer Raw_Cylinder = getItemStack(rawCylinder);
    public static ItemRefer Titanium_Plated_Cylinder = getItemStack(titaniumPlatedCylinder);
    public static ItemRefer Magic_Casing = getItemStack(magicCasing);
    public static ItemRefer Essentia_Cell_T1 = getItemStack(essentiaCell, 0);
    public static ItemRefer Essentia_Cell_T2 = getItemStack(essentiaCell, 1);
    public static ItemRefer Essentia_Cell_T3 = getItemStack(essentiaCell, 2);
    public static ItemRefer Essentia_Hatch = getItemStack(essentiaHatch);

    public static ItemRefer Large_Naquadah_Reactor = getItemStack(MAR);
    public static ItemRefer Naquadah_Fuel_Refinery = getItemStack(FRF);
    public static ItemRefer Universal_Chemical_Fuel_Engine = getItemStack(UCFE);
    public static ItemRefer Large_Essentia_Generator = getItemStack(LEG);

    private Item mItem = null;
    private Block mBlock = null;
    private ItemStack mItemStack = null;
    private int mMeta = 0;

    private static ItemRefer getItemStack(ItemStack itemStack) {
        return new ItemRefer(itemStack);
    }

    private static ItemRefer getItemStack(Item item) {
        return getItemStack(item, 0);
    }

    private static ItemRefer getItemStack(Item item, int meta) {
        if (item == null) return null;
        return new ItemRefer(item, meta);
    }

    private static ItemRefer getItemStack(Block block) {
        return getItemStack(block, 0);
    }

    private static ItemRefer getItemStack(Block block, int meta) {
        if (block == null) return null;
        return new ItemRefer(block, meta);
    }

    private ItemRefer(Item item, int meta) {
        mItem = item;
        mMeta = meta;
    }

    private ItemRefer(Block block, int meta) {
        mBlock = block;
        mMeta = meta;
    }

    private ItemRefer(ItemStack itemStack) {
        mItemStack = itemStack;
    }

    public ItemStack get(int amount){
        if (mItem != null) return new ItemStack(mItem, amount, mMeta);
        if (mBlock != null) return new ItemStack(mBlock, amount, mMeta);
        if (mItemStack != null) return GT_Utility.copyAmount(amount, mItemStack);
        return null;
    }
}
