package gregtech.api.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverBehaviorBase;
import gregtech.common.covers.CoverDefault;
import gregtech.common.covers.CoverNone;
import gregtech.common.covers.SimpleCoverFactory;

public class CoverRegistry {

    /**
     * The Icon List for Covers
     */
    private static final Map<GTItemStack, ITexture> coverTextures = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    private static final Map<GTItemStack, CoverBehaviorBase<?>> coverBehaviors = new ConcurrentHashMap<>();
    private static final Map<GTItemStack, CoverFactory<?>> coverFactories = new ConcurrentHashMap<>();
    /**
     * This is the generic Cover behavior. Used for the default Covers, which have no Behavior.
     */
    private static final CoverFactory<ISerializableObject.LegacyCoverData> coverDefaultFactory = new SimpleCoverFactory();
    private static final CoverBehaviorBase<ISerializableObject.LegacyCoverData> defaultBehavior = new CoverDefault();
    private static final CoverBehaviorBase<ISerializableObject.LegacyCoverData> noBehavior = new CoverNone();
    private static GUIColorOverride colorOverride;
    private static final String guiTexturePath = "gregtech:textures/gui/GuiCover.png";

    static {
        GregTechAPI.sItemStackMappings.add(coverTextures);
        GregTechAPI.sItemStackMappings.add(coverBehaviors);
    }

    public static @NotNull ISerializableObject getEmptyCoverData() {
        return CoverFactories.coverNoneFactory.createDataObject();
    }

    public static void registerSimpleCover(@NotNull ItemStack stack, ITexture cover) {
        CoverRegistry.registerCover(stack, cover, defaultBehavior, coverDefaultFactory);
    }

    public static <T extends ISerializableObject> void registerCover(@NotNull ItemStack stack, ITexture cover,
        @NotNull CoverBehaviorBase<T> behavior, @NotNull CoverFactory<T> factory) {
        if (!coverTextures.containsKey(new GTItemStack(stack))) coverTextures.put(
            new GTItemStack(stack),
            cover == null || !cover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : cover);
        coverBehaviors.put(new GTItemStack(stack), behavior);
        coverFactories.put(new GTItemStack(stack), factory);
    }

    @NotNull
    public static CoverBehaviorBase<?> getCoverBehaviorNew(int coverId) {
        if (coverId == 0) return noBehavior;
        ItemStack stack = GTUtility.intToStack(coverId);
        if (stack == null || stack.getItem() == null) return noBehavior;
        CoverBehaviorBase<?> behavior = coverBehaviors.get(new GTItemStack(stack));
        if (behavior != null) return behavior;
        behavior = coverBehaviors.get(new GTItemStack(stack, true));
        if (behavior != null) return behavior;
        return defaultBehavior;
    }

    @NotNull
    public static CoverFactory<?> getCoverFactory(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return CoverFactories.coverNoneFactory;
        CoverFactory<?> factory = coverFactories.get(new GTItemStack(stack));
        if (factory == null) {
            factory = coverFactories.get(new GTItemStack(stack, true));
        }
        return factory == null ? CoverFactories.coverNoneFactory : factory;
    }

    @NotNull
    public static CoverFactory<?> getCoverFactory(int coverId) {
        return getCoverFactory(GTUtility.intToStack(coverId));
    }

    public static boolean isCover(@NotNull ItemStack stack) {
        return GTUtility.isStackInList(new GTItemStack(stack), coverTextures.keySet());
    }

    public static ITexture getCoverTexture(int coverId) {
        return coverTextures.get(new GTItemStack(coverId));
    }

    public static void reloadCoverColorOverrides() {
        colorOverride = GUIColorOverride.get(guiTexturePath);
    }

    public static int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }
}
