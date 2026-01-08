package gregtech.loaders.postload;

import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.objects.ItemData;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTScannerResult;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import gregtech.common.tileentities.machines.basic.MTEScanner;

public class ScannerHandlerLoader {

    public static void registerScannerHandlers() {
        if (Mods.Forestry.isModLoaded()) {
            MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanForestryIndividual);
        }
        if (Mods.IndustrialCraft2.isModLoaded()) {
            MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanIC2Seed);
        }
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::copyDataOrb);
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanElementIntoDataOrb);
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::copyDataStick);
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanWrittenBook);
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::cropFilledMapToDataStick);
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
            MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanGalacticraftSchematics);
        }
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::scanProspectorData);
        MTEScanner.HANDLERS.addLast(ScannerHandlerLoader::doAssemblyLineResearch);
    }

    public static final int FORESTRY_INDIVIDUAL_SCAN_EUT = 2;
    public static final int FORESTRY_INDIVIDUAL_SCAN_DURATION = 25 * SECONDS;
    public static final int FORESTRY_SCAN_HONEY_USAGE = 100;

    /** Scans bees, trees, butterflies and any other thing that uses the allele system from forestry. */
    public static @Nullable GTScannerResult scanForestryIndividual(@Nonnull MTEScanner aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        // must have at enough honey to start
        FluidStack fillableStack = aScanner.getFillableStack();
        if (fillableStack == null || !fillableStack.containsFluid(Materials.Honey.getFluid(FORESTRY_SCAN_HONEY_USAGE)))
            return null;
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
            return new GTScannerResult(eut, duration, aInput.stackSize, 0, fluidConsume, output, true);
        } catch (Exception e) {
            if (D1) {
                e.printStackTrace(GTLog.err);
            }
        }
        return GTScannerResult.NOT_FOUND;
    }

    public static final int IC2_SEED_SCAN_EUT = (int) TierEU.ULV;
    public static final int IC2_SEED_SCAN_DURATION = 8 * SECONDS;

    public static @Nullable GTScannerResult scanIC2Seed(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.IC2_Crop_Seeds.isStackEqual(aInput, true, true)) return null;
        if (!aInput.hasTagCompound()) {
            // should not be possible, abort recipe check entirely.
            return GTScannerResult.NOT_MET;
        }
        ItemStack output = GTUtility.copyAmount(1, aInput);
        NBTTagCompound tNBT = output.getTagCompound();
        int duration, eut;
        if (tNBT.getByte("scan") < 4) {
            output.setTagCompound(tNBT);
            duration = IC2_SEED_SCAN_DURATION;
            eut = IC2_SEED_SCAN_EUT;
        } else {
            duration = 1;
            eut = 1;
        }
        return new GTScannerResult(eut, duration, 1, 0, 0, output, false);
    }

    public static final int DATA_ORB_COPY_EUT = (int) TierEU.RECIPE_LV;
    public static final int DATA_ORB_COPY_DURATION = 25 * SECONDS + 12 * TICKS;

    public static @Nullable GTScannerResult copyDataOrb(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataOrb.isStackEqual(aSpecialSlot, false, true)) return null;
        if (!ItemList.Tool_DataOrb.isStackEqual(aInput, false, true)) return null;
        return new GTScannerResult(
            DATA_ORB_COPY_EUT,
            DATA_ORB_COPY_DURATION,
            1,
            0,
            0,
            GTUtility.copyAmount(1, aSpecialSlot),
            true);
    }

    public static final int ELEMENT_SCAN_EUT = (int) TierEU.ULV;
    public static final long ELEMENT_SCAN_DURATION_MULTIPLIER = 8192L;

    public static @Nullable GTScannerResult scanElementIntoDataOrb(@Nonnull MTEScanner aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataOrb.isStackEqual(aSpecialSlot, false, true)) return null;
        ItemData tData = GTOreDictUnificator.getAssociation(aInput);
        if (tData == null) return null;
        // must be a dust or a cell
        if (tData.mPrefix != OrePrefixes.dust && tData.mPrefix != OrePrefixes.cell) return null;
        // must be a raw element
        if (tData.mMaterial.mMaterial.mElement == null) return null;
        // must not be an isotope
        if (tData.mMaterial.mMaterial.mElement.mIsIsotope) return null;
        // must not be magical
        if (tData.mMaterial.mMaterial == Materials.Magic) return null;
        // must have mass > 1
        if (tData.mMaterial.mMaterial.getMass() <= 0L) return null;
        ItemStack output = ItemList.Tool_DataOrb.get(1L);
        BehaviourDataOrb.setDataTitle(output, "Elemental-Scan");
        BehaviourDataOrb.setDataName(output, tData.mMaterial.mMaterial.mElement.name());
        return new GTScannerResult(
            ELEMENT_SCAN_EUT,
            GTUtility.safeInt(tData.mMaterial.mMaterial.getMass() * ELEMENT_SCAN_DURATION_MULTIPLIER),
            1,
            1,
            0,
            output,
            true);
    }

    public static final int DATA_STICK_COPY_EUT = (int) TierEU.ULV;
    public static final int DATA_STICK_COPY_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult copyDataStick(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aInput, false, true)) return null;
        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        return new GTScannerResult(DATA_STICK_COPY_EUT, DATA_STICK_COPY_DURATION, 1, 0, 0, output, true);
    }

    public static final int SCAN_WRITTEN_BOOK_EUT = (int) TierEU.ULV;
    public static final int SCAN_WRITTEN_BOOK_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult scanWrittenBook(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (aInput.getItem() != Items.written_book) return null;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(aInput.getTagCompound());
        return new GTScannerResult(SCAN_WRITTEN_BOOK_EUT, SCAN_WRITTEN_BOOK_DURATION, 1, 1, 0, output, true);
    }

    public static final int SCAN_FILLED_MAP_EUT = (int) TierEU.ULV;
    public static final int SCAN_FILLED_MAP_DURATION = 6 * SECONDS + 8 * TICKS;

    public static @Nullable GTScannerResult cropFilledMapToDataStick(@Nonnull MTEScanner aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        if (aInput.getItem() != Items.filled_map) return null;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(
            GTUtility.getNBTContainingShort(new NBTTagCompound(), "map_id", (short) aInput.getItemDamage()));
        return new GTScannerResult(SCAN_FILLED_MAP_EUT, SCAN_FILLED_MAP_DURATION, 1, 1, 0, output, true);
    }

    public static final int SCAN_GALACTICRAFT_SCHEMATIC_MIN_TIER = VoltageIndex.HV;
    public static final int SCAN_GALACTICRAFT_SCHEMATIC_EUT = (int) TierEU.RECIPE_HV;
    public static final int SCAN_GALACTICRAFT_SCHEMATIC_DURATION = 30 * MINUTES;
    public static final ItemStackMap<Short> GALACTICRAFT_SCHEMATIC_LOOKUP = new ItemStackMap<>(false);

    public static @Nullable GTScannerResult scanGalacticraftSchematics(@Nonnull MTEScanner aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        short tier = GALACTICRAFT_SCHEMATIC_LOOKUP.getOrDefault(aInput, (short) -1);
        // abort if not found
        if (tier == -1) return null;
        // fail if not high enough tier
        if (aScanner.mTier < SCAN_GALACTICRAFT_SCHEMATIC_MIN_TIER) return GTScannerResult.NOT_MET;

        ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);
        assert output != null;
        output.setTagCompound(GTUtility.getNBTContainingShort(new NBTTagCompound(), "rocket_tier", tier));

        return new GTScannerResult(
            SCAN_GALACTICRAFT_SCHEMATIC_EUT,
            SCAN_GALACTICRAFT_SCHEMATIC_DURATION,
            1,
            1,
            0,
            output,
            true);
    }

    public static final int SCAN_PROSPECTING_DATA_EUT = (int) TierEU.RECIPE_LV;
    public static final int SCAN_PROSPECTING_DATA_DURATION = 50 * SECONDS;

    public static @Nullable GTScannerResult scanProspectorData(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        // no special slot should be present
        if (aSpecialSlot != null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aInput, false, true)) return null;
        // this feels like a bad idea in terms of localization, maybe consider retooling this to a separate unique nbt
        // tag or item in the future.
        if (!GTUtility.ItemNBT.getBookTitle(aInput)
            .equals("Raw Prospection Data")) return null;

        ItemStack output = GTUtility.copyAmount(1, aInput);
        assert output != null;
        GTUtility.ItemNBT.setBookTitle(output, "Analyzed Prospection Data");
        GTUtility.ItemNBT.convertProspectionData(output);

        return new GTScannerResult(SCAN_PROSPECTING_DATA_EUT, SCAN_PROSPECTING_DATA_DURATION, 1, 0, 0, output, true);
    }

    public static @Nullable GTScannerResult doAssemblyLineResearch(@Nonnull MTEScanner aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aSpecialSlot == null) return null;
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return null;
        for (GTRecipe.RecipeAssemblyLine tRecipe : GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes) {
            if (GTUtility.areStacksEqual(tRecipe.mResearchItem, aInput, true)) {
                GTRecipe matchingRecipe = null;

                for (GTRecipe scannerRecipe : scannerFakeRecipes.getAllRecipes()) {
                    if (GTUtility.areStacksEqual(scannerRecipe.mInputs[0], aInput, true)) {
                        matchingRecipe = scannerRecipe;
                        break;
                    }
                }

                if (matchingRecipe == null || aInput.stackSize < matchingRecipe.mInputs[0].stackSize) {
                    return GTScannerResult.NOT_MET;
                }

                ItemStack output = GTUtility.copyAmount(1, aSpecialSlot);

                // Use Assline Utils
                if (AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(output, tRecipe)) {
                    // In case recipe is too OP for that machine
                    if (tRecipe.mResearchVoltage > V[aScanner.mTier]) {
                        return GTScannerResult.NOT_MET;
                    }
                    return new GTScannerResult(
                        tRecipe.mResearchVoltage,
                        tRecipe.mResearchTime,
                        matchingRecipe.mInputs[0].stackSize,
                        1,
                        0,
                        output,
                        true);
                }
            }
        }
        return null;
    }
}
