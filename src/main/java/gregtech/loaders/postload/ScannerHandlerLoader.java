package gregtech.loaders.postload;

import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTScannerResult;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;

public class ScannerHandlerLoader {

    public static void registerScannerHandlers() {
        if (Mods.Forestry.isModLoaded()) {
            RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanForestryIndividual);
        }
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::copyDataOrb);
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanElement);
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::copyDataStick);
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanWrittenBook);
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanFilledMap);
        // consider moving this handler to core mod
        if (Mods.GalacticraftCore.isModLoaded()) {
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftCore.ID, "item.schematic", 1L, 1), (short) 2);
            // moon buggy
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftCore.ID, "item.schematic", 1L, 0), (short) 100);
        }
        if (Mods.GalacticraftMars.isModLoaded()) {
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.schematic", 1L, 0), (short) 3);
            // cargo rocket
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.schematic", 1L, 1), (short) 101);
            // astro miner
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.schematic", 1L, 2), (short) 102);
        }
        if (Mods.GalaxySpace.isModLoaded()) {
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.SchematicTier4", 1L, 0), (short) 4);
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.SchematicTier5", 1L, 0), (short) 5);
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.SchematicTier6", 1L, 0), (short) 6);
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.SchematicTier7", 1L, 0), (short) 7);
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftMars.ID, "item.SchematicTier8", 1L, 0), (short) 8);
        }
        if (Mods.GalacticraftAmunRa.isModLoaded()) {
            // shuttle, doesn't have a dream craft chip, consider removal or implementation of chip
            GALACTICRAFT_SCHEMATIC_LOOKUP
                .put(GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "item.schematic", 1L, 0), (short) 1);
        }
        if (!GALACTICRAFT_SCHEMATIC_LOOKUP.isEmpty()) {
            RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanGalacticraftSchematic);
        }
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::scanProspectorData);
        RecipeMaps.scannerHandlers.addLast(ScannerHandlerLoader::doAssemblyLineResearch);
    }

    public static final int FORESTRY_INDIVIDUAL_SCAN_EUT = 2;
    public static final int FORESTRY_INDIVIDUAL_SCAN_DURATION = 25 * SECONDS;
    public static final int FORESTRY_SCAN_HONEY_USAGE = 100;

    /** Scans bees, trees, butterflies and any other thing that uses the allele system from forestry. */
    public static @Nullable GTScannerResult scanForestryIndividual(@Nonnull MetaTileEntity aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        // must have at enough honey to start
        if (aFluid == null || !aFluid.containsFluid(Materials.Honey.getFluid(FORESTRY_SCAN_HONEY_USAGE))) return null;
        try {
            IIndividual tIndividual = AlleleManager.alleleRegistry.getIndividual(aInput);
            if (tIndividual == null) return null;
            ItemStack output = GTUtility.copyOrNull(aInput);
            int eut, duration, fluidConsume;
            if (tIndividual.analyze()) {
                NBTTagCompound tNBT = new NBTTagCompound();
                tIndividual.writeToNBT(tNBT);
                output.setTagCompound(tNBT);
                eut = FORESTRY_INDIVIDUAL_SCAN_EUT;
                duration = FORESTRY_INDIVIDUAL_SCAN_DURATION;
                fluidConsume = FORESTRY_SCAN_HONEY_USAGE;
            } else {
                eut = 1;
                duration = 1;
                fluidConsume = 0;
            }
            return new GTScannerResult(eut, duration, aInput.stackSize, 0, fluidConsume, output);
        } catch (Exception e) {
            if (D1) {
                e.printStackTrace(GTLog.err);
            }
        }
        return GTScannerResult.NOT_FOUND;
    }

    public static final int DATA_ORB_COPY_EUT = (int) TierEU.RECIPE_LV;
    public static final int DATA_ORB_COPY_DURATION = 25 * SECONDS + 12 * TICKS;

    public static @Nullable GTScannerResult copyDataOrb(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataOrb.isStackEqual(aSpecialSlot, false, true)) return null;
        if (!ItemList.Tool_DataOrb.isStackEqual(aInput, false, true)) return null;
        return new GTScannerResult(
            DATA_ORB_COPY_EUT,
            DATA_ORB_COPY_DURATION,
            1,
            0,
            0,
            GTUtility.copyAmount(1, aSpecialSlot));
    }

    public static final int ELEMENT_SCAN_EUT = (int) TierEU.RECIPE_LV;
    public static final long ELEMENT_SCAN_DURATION_MULTIPLIER = 8192L;

    public static @Nullable GTScannerResult scanElement(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataOrb.isStackEqual(aSpecialSlot, false, true)) return null;
        ItemData tData = GTOreDictUnificator.getAssociation(aInput);
        if (tData == null) return null;
        // must be a dust or a cell
        if (tData.mPrefix != OrePrefixes.dust && tData.mPrefix != OrePrefixes.cell) return null;
        // must be a scannable element
        return getElementScanResult(tData.mMaterial.mMaterial);
    }

    public static @Nullable GTScannerResult getElementScanResult(Materials aMaterial) {
        if (!isScannableMaterial(aMaterial)) return null;
        ItemStack output = ItemList.Tool_DataOrb.get(1L);
        BehaviourDataOrb.setDataTitle(output, "Elemental-Scan");
        BehaviourDataOrb.setDataName(output, aMaterial.mElement.name());
        return new GTScannerResult(
            ELEMENT_SCAN_EUT,
            GTUtility.safeInt(aMaterial.getMass() * ELEMENT_SCAN_DURATION_MULTIPLIER),
            1,
            1,
            0,
            output);
    }

    public static boolean isScannableMaterial(Materials aMaterial) {
        // must be a raw element
        if (aMaterial.mElement == null) return false;
        // must not be an isotope
        if (aMaterial.mElement.mIsIsotope) return false;
        // must not be magical
        if (aMaterial == Materials.Magic) return false;
        // must have mass > 1
        return aMaterial.getMass() > 0L;
    }

    public static final int DATA_STICK_COPY_EUT = (int) TierEU.ULV;
    public static final int DATA_STICK_COPY_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult copyDataStick(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aInput, false, true)) return null;
        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        return new GTScannerResult(DATA_STICK_COPY_EUT, DATA_STICK_COPY_DURATION, 1, 0, 0, output);
    }

    public static final int SCAN_WRITTEN_BOOK_EUT = (int) TierEU.ULV;
    public static final int SCAN_WRITTEN_BOOK_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult scanWrittenBook(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (aInput.getItem() != Items.written_book) return null;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(aInput.getTagCompound());
        return new GTScannerResult(SCAN_WRITTEN_BOOK_EUT, SCAN_WRITTEN_BOOK_DURATION, 1, 1, 0, output);
    }

    public static final int SCAN_FILLED_MAP_EUT = (int) TierEU.ULV;
    public static final int SCAN_FILLED_MAP_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult scanFilledMap(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (aInput.getItem() != Items.filled_map) return null;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(
            GTUtility.getNBTContainingShort(new NBTTagCompound(), "map_id", (short) aInput.getItemDamage()));
        return new GTScannerResult(SCAN_FILLED_MAP_EUT, SCAN_FILLED_MAP_DURATION, 1, 1, 0, output);
    }

    public static final int SCAN_GALACTICRAFT_SCHEMATIC_EUT = (int) TierEU.RECIPE_HV;
    public static final int SCAN_GALACTICRAFT_SCHEMATIC_DURATION = 30 * MINUTES;
    public static final ItemStackMap<Short> GALACTICRAFT_SCHEMATIC_LOOKUP = new ItemStackMap<>(false);

    public static @Nullable GTScannerResult scanGalacticraftSchematic(@Nonnull MetaTileEntity aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        short tier = GALACTICRAFT_SCHEMATIC_LOOKUP.getOrDefault(aInput, (short) -1);
        // abort if not found
        if (tier == -1) return null;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(GTUtility.getNBTContainingShort(new NBTTagCompound(), "rocket_tier", tier));

        return new GTScannerResult(
            SCAN_GALACTICRAFT_SCHEMATIC_EUT,
            SCAN_GALACTICRAFT_SCHEMATIC_DURATION,
            1,
            1,
            0,
            output);
    }

    public static final int SCAN_PROSPECTING_DATA_EUT = (int) TierEU.RECIPE_LV;
    public static final int SCAN_PROSPECTING_DATA_DURATION = 50 * SECONDS;

    public static @Nullable GTScannerResult scanProspectorData(@Nonnull MetaTileEntity aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!isValidProspectionBook(aInput, aSpecialSlot)) return null;

        ItemStack output = GTUtility.copyAmount(1, aInput);
        assert output != null;
        GTUtility.ItemNBT.setBookTitle(output, "Analyzed Prospection Data");
        GTUtility.ItemNBT.convertProspectionData(output);

        return new GTScannerResult(SCAN_PROSPECTING_DATA_EUT, SCAN_PROSPECTING_DATA_DURATION, 1, 0, 0, output);
    }

    public static boolean isValidProspectionBook(ItemStack aInput, ItemStack aSpecialSlot) {
        // no special slot should be present
        if (aSpecialSlot != null) return false;
        if (!ItemList.Tool_DataStick.isStackEqual(aInput, false, true)) return false;
        // this feels like a bad idea in terms of localization, maybe consider retooling this to a separate unique nbt
        // tag or item in the future.
        if (!GTUtility.ItemNBT.getBookTitle(aInput)
            .equals(GTUtility.ItemNBT.getRawProspectionDataName())) return false;
        return true;
    }

    public static @Nullable GTScannerResult doAssemblyLineResearch(@Nonnull MetaTileEntity aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        // check find a corresponding recipe
        GTRecipe.RecipeAssemblyLine tRecipe = GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes.parallelStream()
            .filter(
                recipe -> recipe.mOutput != null && GTUtility.areStacksEqual(recipe.mResearchItem, aInput, true)
                    && recipe.mResearchItem.stackSize <= aInput.stackSize)
            .findFirst()
            .orElse(null);
        // abort if not found
        if (tRecipe == null) return null;

        // Generate new recipe stick.
        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        // this shouldn't be able to fail, but if it did no other recipe will match anyway.
        if (!AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(output, tRecipe)) return null;
        // return
        return new GTScannerResult.ALScannerResult(
            tRecipe.mResearchVoltage,
            tRecipe.mResearchTime,
            tRecipe.mResearchItem.stackSize,
            1,
            0,
            output,
            tRecipe);
    }
}
