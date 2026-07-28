package gregtech.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.MaterialLib;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTUtility;

public class OreDictEventContainer {

    public final OreDictionary.OreRegisterEvent mEvent;
    public final OrePrefixes mPrefix;
    public final Material mMaterial;
    /// The recognition marker this registration's census resolved through (see
    /// `GTProxy#resolveCensusMaterial`), or null for a plain [Materials]-named registration. A marker carries no
    /// legacy [Materials] counterpart, so [#registerRecipes] dispatches ore processing through it directly
    /// instead of through [#mMaterial]'s backing -- via the recognition-marker registrator entries
    /// (e.g. `ProcessingDust`, `ProcessingCrystallized`).
    public final Material mRecognitionMarker;
    public final String mModID;

    public OreDictEventContainer(OreDictionary.OreRegisterEvent aEvent, OrePrefixes aPrefix, Material aMaterial,
        Material aRecognitionMarker, String aModID) {
        this.mEvent = aEvent;
        this.mPrefix = aPrefix;
        this.mMaterial = aMaterial;
        this.mRecognitionMarker = aRecognitionMarker;
        this.mModID = ((aModID == null) || (aModID.equals("UNKNOWN")) ? null : aModID);
    }

    public static void registerRecipes(OreDictEventContainer ore) {
        if ((ore.mEvent.Ore == null) || (ore.mEvent.Ore.getItem() == null)
            || (ore.mPrefix == null)
            || ore.mPrefix.isIgnored(ore.mRecognitionMarker != null ? ore.mRecognitionMarker : ore.mMaterial)
            || isMaterialLibItem(ore.mEvent.Ore)) {
            return;
        }
        if (ore.mEvent.Ore.stackSize != 1) {
            ore.mEvent.Ore.stackSize = 1;
        }

        ItemStack tStack = GTUtility.copyAmount(1, ore.mEvent.Ore);
        if (ore.mRecognitionMarker != null) {
            ore.mPrefix.processRecognitionOre(ore.mRecognitionMarker, ore.mEvent.Name, ore.mModID, tStack);
        } else {
            ore.mPrefix.processOre(
                ore.mMaterial == null ? Materials2Materials.NULL : ore.mMaterial,
                ore.mEvent.Name,
                ore.mModID,
                tStack);
        }
    }

    /// Whether `stack`'s item belongs to MaterialLib -- such stacks are already covered by
    /// `gregtech.loaders.shapeconsumers` (dispatched once per (shape, material) at MaterialLib's init) and
    /// would otherwise be double-processed here too, since MaterialLib's own oredict registrations replay
    /// through this same path (see `GTProxy#catchUpPreExistingOreDictEntries`). Checked by the item's own
    /// registered domain rather than `ore.mModID` (which identifies whichever mod's code happened to be active
    /// when the underlying `OreDictionary.OreRegisterEvent` fired -- for the catch-up replay path that is
    /// GregTech's own preInit, not MaterialLib).
    private static boolean isMaterialLibItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id != null && MaterialLib.MODID.equals(id.modId);
    }
}
