package gregtech.api.covers;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;

public class Covers {

    /**
     * The Icon List for Covers
     */
    private static final Map<GTItemStack, ITexture> sCovers = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    private static final Map<GTItemStack, CoverBehaviorBase<?>> sCoverBehaviors = new ConcurrentHashMap<>();

    static {
        GregTechAPI.sItemStackMappings.add(sCovers);
        GregTechAPI.sItemStackMappings.add(sCoverBehaviors);
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, CoverBehavior aBehavior) {
        registerCover(aStack, aCover, (CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCover(ItemStack aStack, ITexture aCover, CoverBehaviorBase<?> aBehavior) {
        if (!sCovers.containsKey(new GTItemStack(aStack))) sCovers.put(
            new GTItemStack(aStack),
            aCover == null || !aCover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : aCover);
        if (aBehavior != null) sCoverBehaviors.put(new GTItemStack(aStack), aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, CoverBehavior aBehavior) {
        registerCoverBehavior(aStack, (CoverBehaviorBase<?>) aBehavior);
    }

    public static void registerCoverBehavior(ItemStack aStack, CoverBehaviorBase<?> aBehavior) {
        sCoverBehaviors.put(new GTItemStack(aStack), aBehavior == null ? GregTechAPI.sDefaultBehavior : aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover, CoverBehavior aBehavior) {
        registerCover(aStackList, aCover, (CoverBehaviorBase<?>) aBehavior);
    }

    /**
     * Registers multiple Cover Items. I use that for the OreDict Functionality.
     *
     * @param aBehavior can be null
     */
    public static void registerCover(Collection<ItemStack> aStackList, ITexture aCover,
        CoverBehaviorBase<?> aBehavior) {
        if (aCover.isValidTexture()) aStackList.forEach(tStack -> registerCover(tStack, aCover, aBehavior));
    }

    @NotNull
    public static CoverBehaviorBase<?> getCoverBehaviorNew(ItemStack aStack) {
        if (aStack == null || aStack.getItem() == null) return GregTechAPI.sNoBehavior;
        CoverBehaviorBase<?> rCover = sCoverBehaviors.get(new GTItemStack(aStack));
        if (rCover != null) return rCover;
        rCover = sCoverBehaviors.get(new GTItemStack(aStack, true));
        if (rCover != null) return rCover;
        return GregTechAPI.sDefaultBehavior;
    }

    @NotNull
    public static CoverBehaviorBase<?> getCoverBehaviorNew(int aStack) {
        if (aStack == 0) return GregTechAPI.sNoBehavior;
        return getCoverBehaviorNew(GTUtility.intToStack(aStack));
    }

    public static boolean isCover(@NotNull ItemStack itemStack) {
        return sCovers.containsKey(new GTItemStack(itemStack));
    }

    public static ITexture getCoverTexture(int coverId) {
        return sCovers.get(new GTItemStack(coverId));
    }

    public static void reloadCoverColorOverrides() {
        sCoverBehaviors.values()
            .forEach(CoverBehaviorBase::reloadColorOverride);
    }
}
