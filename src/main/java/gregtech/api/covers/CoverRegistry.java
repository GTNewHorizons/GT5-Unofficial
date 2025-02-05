package gregtech.api.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;

public class CoverRegistry {

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

    public static void registerCover(ItemStack aStack, ITexture aCover, CoverBehaviorBase<?> aBehavior) {
        if (!sCovers.containsKey(new GTItemStack(aStack))) sCovers.put(
            new GTItemStack(aStack),
            aCover == null || !aCover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : aCover);
        if (aBehavior != null) sCoverBehaviors.put(new GTItemStack(aStack), aBehavior);
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
