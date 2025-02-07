package gregtech.api.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTCoverDefault;
import gregtech.api.objects.GTCoverNone;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;

public class CoverRegistry {

    /**
     * The Icon List for Covers
     */
    private static final Map<GTItemStack, ITexture> coverTextures = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    private static final Map<GTItemStack, CoverBehaviorBase<?>> coverBehaviors = new ConcurrentHashMap<>();
    /**
     * This is the generic Cover behavior. Used for the default Covers, which have no Behavior.
     */
    private static final CoverBehaviorBase<?> defaultBehavior = new GTCoverDefault(), noBehavior = new GTCoverNone();

    static {
        GregTechAPI.sItemStackMappings.add(coverTextures);
        GregTechAPI.sItemStackMappings.add(coverBehaviors);
    }

    public static @NotNull CoverBehaviorBase<?> getEmptyCover() {
        return noBehavior;
    }

    public static void registerCover(ItemStack stack, ITexture cover, CoverBehaviorBase<?> behavior) {
        if (!coverTextures.containsKey(new GTItemStack(stack))) coverTextures.put(
            new GTItemStack(stack),
            cover == null || !cover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : cover);
        if (behavior != null) coverBehaviors.put(new GTItemStack(stack), behavior);
    }

    @NotNull
    public static CoverBehaviorBase<?> getCoverBehaviorNew(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return noBehavior;
        CoverBehaviorBase<?> behavior = coverBehaviors.get(new GTItemStack(stack));
        if (behavior != null) return behavior;
        behavior = coverBehaviors.get(new GTItemStack(stack, true));
        if (behavior != null) return behavior;
        return defaultBehavior;
    }

    @NotNull
    public static CoverBehaviorBase<?> getCoverBehaviorNew(int coverId) {
        if (coverId == 0) return noBehavior;
        return getCoverBehaviorNew(GTUtility.intToStack(coverId));
    }

    public static boolean isCover(@NotNull ItemStack stack) {
        return GTUtility.isStackInList(new GTItemStack(stack), coverTextures.keySet());
    }

    public static ITexture getCoverTexture(int coverId) {
        return coverTextures.get(new GTItemStack(coverId));
    }

    public static void reloadCoverColorOverrides() {
        coverBehaviors.values()
            .forEach(CoverBehaviorBase::reloadColorOverride);
    }
}
