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
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverDecorative;
import gregtech.common.covers.CoverNone;

public final class CoverRegistry {

    private static final CoverPlacer DEFAULT_COVER_PLACER = CoverPlacer.builder()
        .build();
    public static final CoverPlacer PRIMITIVE_COVER_PLACER = CoverPlacer.builder()
        .allowOnPrimitiveBlock()
        .build();
    public static final CoverPlacer INTERCEPTS_RIGHT_CLICK_COVER_PLACER = CoverPlacer.builder()
        .blocksCoverableGuiOpening()
        .build();

    /**
     * The Icon List for Covers
     */
    private static final Map<GTItemStack, ITexture> coverTextures = new ConcurrentHashMap<>();
    /**
     * The List of Cover Behaviors for the Covers
     */
    private static final Map<GTItemStack, CoverRegistration> coverFactories = new ConcurrentHashMap<>();
    private static final CoverRegistration coverNone = new CoverRegistration(
        null,
        CoverNone::new,
        PRIMITIVE_COVER_PLACER);
    public static final Cover NO_COVER = coverNone.buildCover(ForgeDirection.UNKNOWN, null);

    private static GUIColorOverride colorOverride;
    private static final String guiTexturePath = "gregtech:textures/gui/GuiCover.png";
    private static final String NBT_ID = "id";

    static {
        GregTechAPI.sItemStackMappings.add(coverTextures);
        GregTechAPI.sItemStackMappings.add(coverFactories);
    }

    public static void registerDecorativeCover(@NotNull ItemStack stack, ITexture cover) {
        registerCover(stack, cover, CoverDecorative::new, PRIMITIVE_COVER_PLACER);
    }

    public static void registerCover(@NotNull ItemStack stack, ITexture cover, @NotNull CoverFactory constructor) {
        registerCover(stack, cover, constructor, DEFAULT_COVER_PLACER);
    }

    public static void registerCover(@NotNull ItemStack stack, ITexture cover, @NotNull CoverFactory constructor,
        CoverPlacer factory) {
        if (!coverTextures.containsKey(new GTItemStack(stack))) {
            coverTextures.put(
                new GTItemStack(stack),
                cover == null || !cover.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0] : cover);
        }
        coverFactories.put(new GTItemStack(stack), new CoverRegistration(stack, constructor, factory));
    }

    @NotNull
    public static CoverRegistration getRegistration(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return coverNone;
        }
        CoverRegistration factory = coverFactories.get(new GTItemStack(stack));
        if (factory == null) {
            factory = coverFactories.get(new GTItemStack(stack, true));
        }
        return factory == null ? coverNone : factory;
    }

    @NotNull
    public static CoverRegistration getRegistration(int coverId) {
        return getRegistration(GTUtility.intToStack(coverId));
    }

    public static CoverPlacer getCoverPlacer(ItemStack stack) {
        return getRegistration(stack).getCoverPlacer();
    }

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

    public static CoverRegistration getRegistrationFromNbt(NBTTagCompound nbt) {
        int coverID = nbt.getInteger(NBT_ID);
        return getRegistration(coverID);
    }

    public static void writeCoverToNbt(Cover cover, NBTTagCompound nbt) {
        nbt.setInteger(NBT_ID, cover.getCoverID());
        cover.writeToNBT(nbt);
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
        coverable.attachCover(getRegistration(down).buildCover(ForgeDirection.DOWN, coverable));
        coverable.attachCover(getRegistration(up).buildCover(ForgeDirection.UP, coverable));
        coverable.attachCover(getRegistration(north).buildCover(ForgeDirection.NORTH, coverable));
        coverable.attachCover(getRegistration(south).buildCover(ForgeDirection.SOUTH, coverable));
        coverable.attachCover(getRegistration(west).buildCover(ForgeDirection.WEST, coverable));
        coverable.attachCover(getRegistration(east).buildCover(ForgeDirection.EAST, coverable));
    }
}
