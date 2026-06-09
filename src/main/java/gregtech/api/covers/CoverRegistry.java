package gregtech.api.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
     * The List of Cover Registrations for the Covers containing cover factories, placement conditions and base textures
     */
    private static final Map<GTItemStack, CoverRegistration> covers = new ConcurrentHashMap<>();
    public static final Cover NO_COVER = new CoverNone(new CoverContext(null, ForgeDirection.UNKNOWN, null));
    private static final CoverRegistration coverNone = new CoverRegistration(
        CoverNone::new,
        PRIMITIVE_COVER_PLACER,
        null);

    private static GUIColorOverride colorOverride;
    private static final String guiTexturePath = "gregtech:textures/gui/GuiCover.png";
    private static final String NBT_ID = "id";

    public static void registerDecorativeCover(@NotNull ItemStack coverItem, ITexture cover) {
        registerCover(coverItem, cover, CoverDecorative::new, PRIMITIVE_COVER_PLACER);
    }

    public static void registerCover(@NotNull ItemStack coverItem, ITexture cover, @NotNull CoverFactory constructor) {
        registerCover(coverItem, cover, constructor, DEFAULT_COVER_PLACER);
    }

    public static void registerCover(@NotNull ItemStack coverItem, ITexture coverTexture,
        @NotNull CoverFactory constructor, CoverPlacer factory) {
        GTItemStack key = new GTItemStack(coverItem);
        if (!covers.containsKey(key)) {
            CoverRegistration coverRegistration = new CoverRegistration(
                constructor,
                factory,
                sanitizeTexture(coverTexture));
            covers.put(key, coverRegistration);
        }
    }

    private static ITexture sanitizeTexture(ITexture coverTexture) {
        return coverTexture == null || !coverTexture.isValidTexture() ? Textures.BlockIcons.ERROR_RENDERING[0]
            : coverTexture;
    }

    @NotNull
    private static CoverRegistration getRegistration(ItemStack coverItem) {
        if (coverItem == null || coverItem.getItem() == null) {
            return coverNone;
        }
        CoverRegistration factory = covers.get(new GTItemStack(coverItem));
        if (factory == null) {
            factory = covers.get(new GTItemStack(coverItem, true));
        }
        return factory == null ? coverNone : factory;
    }

    public static @NotNull Cover buildCover(ItemStack coverItem, ForgeDirection side, ICoverable coverable) {
        CoverRegistration registration = getRegistration(coverItem);
        return registration.getFactory()
            .buildCover(new CoverContext(coverItem, side, coverable));
    }

    public static CoverPlacer getCoverPlacer(ItemStack coverItem) {
        return getRegistration(coverItem).getCoverPlacer();
    }

    public static boolean isCover(ItemStack coverItem) {
        return GTUtility.isStackInList(new GTItemStack(coverItem), covers.keySet());
    }

    public static ITexture getCoverTexture(ItemStack coverItem) {
        return covers.get(new GTItemStack(coverItem))
            .getCoverTexture();
    }

    public static void reloadCoverColorOverrides() {
        colorOverride = GUIColorOverride.get(guiTexturePath);
    }

    public static int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    public static Cover buildCoverFromNbt(NBTTagCompound nbt, ForgeDirection side, ICoverable coverable) {
        ItemStack coverItem = GTUtility.intToStack(nbt.getInteger(NBT_ID));
        CoverRegistration registration = getRegistration(coverItem);
        Cover cover = registration.getFactory()
            .buildCover(new CoverContext(coverItem, side, coverable));
        cover.readFromNbt(nbt);
        return cover;
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
        coverable.attachCover(buildCover(GTUtility.intToStack(down), ForgeDirection.DOWN, coverable));
        coverable.attachCover(buildCover(GTUtility.intToStack(up), ForgeDirection.UP, coverable));
        coverable.attachCover(buildCover(GTUtility.intToStack(north), ForgeDirection.NORTH, coverable));
        coverable.attachCover(buildCover(GTUtility.intToStack(south), ForgeDirection.SOUTH, coverable));
        coverable.attachCover(buildCover(GTUtility.intToStack(west), ForgeDirection.WEST, coverable));
        coverable.attachCover(buildCover(GTUtility.intToStack(east), ForgeDirection.EAST, coverable));
    }
}
