package gregtech.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.MaterialLib;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.util.GTUtility;

public class OreDictEventContainer {

    public final OreDictionary.OreRegisterEvent mEvent;
    public final OrePrefixes mPrefix;
    public final Material mMaterial;
    /// The recognition marker this registration's census resolved through (see
    /// `GTProxy#resolveCensusMaterial`), or null for a plain material-named registration. When set,
    /// [#registerRecipes] dispatches ore processing through the marker rather than through [#mMaterial].
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
                ore.mMaterial == null ? Materials.NULL : ore.mMaterial,
                ore.mEvent.Name,
                ore.mModID,
                tStack);
        }
    }

    /// Whether `stack`'s item belongs to MaterialLib. Such stacks are processed by
    /// `gregtech.loaders.shapeconsumers` instead, once per (shape, material). The test is the item's own
    /// registered domain, not `mModID`, which names whichever mod's code was active when the underlying
    /// `OreDictionary.OreRegisterEvent` fired.
    private static boolean isMaterialLibItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id != null && MaterialLib.MODID.equals(id.modId);
    }
}
