package gregtech.common.gui.mui1.cover;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.gui.widgets.CoverTickRateButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;

public class CoverUIFactory<C> {

    private final CoverUIBuildContext uiBuildContext;

    public CoverUIFactory(CoverUIBuildContext buildContext) {
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
            // Close button background
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                }
            })
                .setBackground(ModularUITextures.VANILLA_BACKGROUND)
                .setSize(12, 12)
                .setPos(getGUIWidth() - 15, 3));
            builder.widget(
                new TextWidget(StatCollector.translateToLocal("mui.button.close")).setPos(getGUIWidth() - 12, 5));
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
