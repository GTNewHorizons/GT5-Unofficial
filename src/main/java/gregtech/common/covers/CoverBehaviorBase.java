package gregtech.common.covers;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.gui.widgets.CoverTickRateButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;

/**
 * For Covers with a special behavior.
 *
 * @author glease
 */
public abstract class CoverBehaviorBase<T extends ISerializableObject> extends Cover {

    private static final String NBT_DATA = "d";
    private static final String NBT_TICK_RATE_ADDITION = "tra";

    protected @NotNull T coverData;

    private final ITexture coverFGTexture;

    public CoverBehaviorBase(@NotNull CoverContext context, ITexture coverFGTexture) {
        super(context);
        this.coverData = initializeData(context.getCoverInitializer());
        this.coverFGTexture = coverFGTexture;
        // Calling after data was initialized since overrides may depend on data.
        setTickRateAddition(initializeTickRateAddition(context.getCoverInitializer()));
    }

    private T initializeData(Object coverData) {
        if (coverData instanceof ItemStack coverStack) {
            return loadFromItemStack(coverStack);
        } else if (coverData instanceof NBTTagCompound nbt && nbt.hasKey(NBT_DATA)) {
            return loadFromNbt(nbt.getTag(NBT_DATA));
        } else if (coverData instanceof ByteArrayDataInput byteData) {
            return loadFromPacket(byteData);
        }
        return initializeData();
    }

    private int initializeTickRateAddition(Object coverData) {
        if (coverData instanceof NBTTagCompound nbt && nbt.hasKey(NBT_TICK_RATE_ADDITION)) {
            return nbt.getInteger(NBT_TICK_RATE_ADDITION);
        } else if (coverData instanceof ByteArrayDataInput byteData) {
            return byteData.readInt();
        }
        return getDefaultTickRateAddition();
    }

    private int getDefaultTickRateAddition() {
        if (!allowsTickRateAddition()) return 0;
        return getDefaultTickRate() - this.getMinimumTickRate();
    }

    protected abstract T initializeData();

    protected T loadFromItemStack(ItemStack cover) {
        return initializeData();
    }

    protected T loadFromNbt(NBTBase nbt) {
        final T ret = initializeData();
        ret.loadDataFromNBT(nbt);
        return ret;
    }

    protected T loadFromPacket(ByteArrayDataInput byteData) {
        final T ret = initializeData();
        ret.readFromPacket(byteData);
        return ret;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag(NBT_DATA, coverData.saveDataToNBT());
        nbt.setInteger(NBT_TICK_RATE_ADDITION, tickRateAddition);
        return nbt;
    }

    @Override
    public void writeToByteBuf(ByteBuf byteBuf) {
        coverData.writeToByteBuf(byteBuf);
        byteBuf.writeInt(tickRateAddition);
    }

    // region facade

    @Override
    public ITexture getOverlayTexture() {
        return coverFGTexture;
    }

    @Override
    public void doCoverThings(byte aRedstone, long aTickTimer) {}

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    // endregion

    // region UI stuff

    @Override
    protected ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new UIFactory<>(buildContext).createWindow();
    }

    /**
     * Creates {@link ModularWindow} for this cover. This is separated from base class, as attaching the same covers in
     * different sides of the same tile needs different UI with different context.
     */
    protected static class UIFactory<C extends Cover> {

        private final CoverUIBuildContext uiBuildContext;

        protected UIFactory(CoverUIBuildContext buildContext) {
            this.uiBuildContext = buildContext;
        }

        public ModularWindow createWindow() {
            ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight());
            builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
            builder.setGuiTint(getUIBuildContext().getGuiColorization());
            maybeBindPlayerInventory(builder);
            addTitleToUI(builder);
            addUIWidgets(builder);
            if (getUIBuildContext().isAnotherWindow()) {
                builder.widget(
                    ButtonWidget.closeWindowButton(true)
                        .setPos(getGUIWidth() - 15, 3));
            }

            final Cover cover = uiBuildContext.getTile()
                .getCoverAtSide(uiBuildContext.getCoverSide());
            if (cover.getMinimumTickRate() > 0 && cover.allowsTickRateAddition()) {
                builder.widget(new CoverTickRateButton(cover, builder).setPos(getGUIWidth() - 24, getGUIHeight() - 24));
            }

            return builder.build();
        }

        protected void maybeBindPlayerInventory(ModularWindow.Builder builder) {
            if (doesBindPlayerInventory() && !getUIBuildContext().isAnotherWindow()) {
                builder.bindPlayerInventory(getUIBuildContext().getPlayer(), 7, GUITextureSet.DEFAULT.getItemSlot());
            }
        }

        /**
         * Override this to add widgets for your UI.
         */
        protected void addUIWidgets(ModularWindow.Builder builder) {}

        protected CoverUIBuildContext getUIBuildContext() {
            return uiBuildContext;
        }

        /**
         * Can return null when the cover is invalid e.g. tile is broken or cover is removed
         */

        protected @Nullable C getCover() {
            if (isCoverValid()) {
                return adaptCover(
                    getUIBuildContext().getTile()
                        .getCoverAtSide(getUIBuildContext().getCoverSide()));
            } else {
                return null;
            }
        }

        protected @Nullable C adaptCover(Cover cover) {
            return null;
        }

        protected @NotNull Supplier<String> getCoverString(Function<C, String> toString) {
            return () -> {
                C cover = getCover();
                if (cover == null) return "";
                return toString.apply(cover);
            };
        }

        protected boolean coverMatches(Predicate<C> predicate) {
            C cover = getCover();
            if (cover == null) return false;
            return predicate.test(cover);
        }

        protected void ifCoverValid(Consumer<C> coverConsumer) {
            Optional.ofNullable(getCover())
                .ifPresent(coverConsumer);
        }

        private boolean isCoverValid() {
            ICoverable tile = getUIBuildContext().getTile();
            return !tile.isDead() && tile.getCoverAtSide(getUIBuildContext().getCoverSide())
                .isValid();
        }

        protected void addTitleToUI(ModularWindow.Builder builder) {
            ItemStack coverItem = GTUtility.intToStack(getUIBuildContext().getCoverID());
            if (coverItem != null) {
                builder.widget(
                    new ItemDrawable(coverItem).asWidget()
                        .setPos(5, 5)
                        .setSize(16, 16))
                    .widget(
                        new TextWidget(coverItem.getDisplayName()).setDefaultColor(COLOR_TITLE.get())
                            .setPos(25, 9));
            }
        }

        protected int getGUIWidth() {
            return 176;
        }

        protected int getGUIHeight() {
            return 107;
        }

        protected boolean doesBindPlayerInventory() {
            return false;
        }

        protected final Supplier<Integer> COLOR_TITLE = () -> CoverRegistry.getTextColorOrDefault("title", 0x222222);
        protected final Supplier<Integer> COLOR_TEXT_GRAY = () -> CoverRegistry
            .getTextColorOrDefault("text_gray", 0x555555);
        protected final Supplier<Integer> COLOR_TEXT_WARN = () -> CoverRegistry
            .getTextColorOrDefault("text_warn", 0xff0000);
    }
    // endregion
}
