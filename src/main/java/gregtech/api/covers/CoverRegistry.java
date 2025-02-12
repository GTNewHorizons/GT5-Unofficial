package gregtech.api.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverBehaviorBase;
import gregtech.common.covers.CoverDefault;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.CoverNone;
import gregtech.common.covers.SimpleCoverPlacer;

public class CoverRegistry {

    /**
     * The Icon List for Covers
     */
    private static final Map<GTItemStack, ITexture> coverTextures = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    private static final Map<GTItemStack, CoverBehaviorBase<?>> coverBehaviors = new ConcurrentHashMap<>();
    private static final Map<GTItemStack, CoverRegistration<?>> coverFactories = new ConcurrentHashMap<>();
    public static CoverRegistration<ISerializableObject.LegacyCoverData> coverNoneInfoFactory = new CoverRegistration<>(
        0,
        CoverNone::new,
        new SimpleCoverPlacer());
    private static final CoverPlacer DEFAULT_COVER_PLACER = new CoverPlacerBase();
    public static final CoverPlacer SIMPLE_COVER_PLACER = new SimpleCoverPlacer();

    private static GUIColorOverride colorOverride;
    private static final String guiTexturePath = "gregtech:textures/gui/GuiCover.png";
    private static final String NBT_ID = "id";

    static {
        GregTechAPI.sItemStackMappings.add(coverTextures);
        GregTechAPI.sItemStackMappings.add(coverBehaviors);
    }

    public static @NotNull ISerializableObject getEmptyCoverData() {
        return new ISerializableObject.LegacyCoverData();
    }

    public static void registerSimpleCover(@NotNull ItemStack stack, ITexture cover) {
        CoverRegistry.registerCover(stack, cover, CoverDefault::new, SIMPLE_COVER_PLACER);
    }

    public static void registerCover(@NotNull ItemStack stack, ITexture cover, @NotNull CoverFactory constructor) {
        CoverRegistry.registerCover(stack, cover, constructor, DEFAULT_COVER_PLACER);
    }

    public static <T extends ISerializableObject> void registerCover(@NotNull ItemStack stack, ITexture cover,
        @NotNull CoverFactory constructor, @NotNull CoverPlacer factory) {
        if (!coverTextures.containsKey(new GTItemStack(stack))) coverTextures.put(
            new GTItemStack(stack),
            cover == null || !cover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : cover);
        coverFactories
            .put(new GTItemStack(stack), new CoverRegistration<T>(GTUtility.stackToInt(stack), constructor, factory));
    }

    @NotNull
    public static CoverRegistration<?> getRegistration(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return coverNoneInfoFactory;
        CoverRegistration<?> factory = coverFactories.get(new GTItemStack(stack));
        if (factory == null) {
            factory = coverFactories.get(new GTItemStack(stack, true));
        }
        return factory == null ? coverNoneInfoFactory : factory;
    }

    @NotNull
    public static CoverRegistration<?> getRegistration(int coverId) {
        return getRegistration(GTUtility.intToStack(coverId));
    }

    @NotNull
    public static CoverPlacer getCoverPlacer(ItemStack stack) {
        return getRegistration(stack).getCoverPlacer();
    }

    @NotNull
    public static CoverPlacer getCoverPlacer(int coverId) {
        return getCoverPlacer(GTUtility.intToStack(coverId));
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

    public static CoverInfo buildCover(ICoverable coverable, NBTTagCompound nbt) {
        int coverID = nbt.getInteger(NBT_ID);
        return CoverRegistry.getRegistration(coverID)
            .buildCover(coverable, nbt);
    }

    /**
     * Set cover data on all sides of a coverable object.
     *
     * @param coverable the coverable
     * @param down      cover id
     * @param up        cover id
     * @param north     cover id
     * @param south     cover id
     * @param west      cover id
     * @param east      cover id
     */
    public static void cover(ICoverable coverable, int down, int up, int north, int south, int west, int east) {
        coverable.attachCover(getRegistration(down).buildCover(ForgeDirection.DOWN, coverable), ForgeDirection.DOWN);
        coverable.attachCover(getRegistration(up).buildCover(ForgeDirection.UP, coverable), ForgeDirection.UP);
        coverable.attachCover(getRegistration(north).buildCover(ForgeDirection.NORTH, coverable), ForgeDirection.NORTH);
        coverable.attachCover(getRegistration(south).buildCover(ForgeDirection.SOUTH, coverable), ForgeDirection.SOUTH);
        coverable.attachCover(getRegistration(west).buildCover(ForgeDirection.WEST, coverable), ForgeDirection.WEST);
        coverable.attachCover(getRegistration(east).buildCover(ForgeDirection.EAST, coverable), ForgeDirection.EAST);
    }
}
