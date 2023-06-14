package gregtech.common.gui.modularui.uifactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

/**
 * Creates UI for selecting item from given list. This is client-only UI to allow using client-preferred settings.
 */
public class SelectItemUIFactory {

    private final String header;
    private final ItemStack headerItem;
    public static final int UNSELECTED = -1;
    private static final int cols = 9;
    private final Consumer<ItemStack> selectedCallback;
    // passed in stack
    private final List<ItemStack> stacks;
    private final boolean noDeselect;
    private int selected;
    private boolean anotherWindow = false;
    private AtomicBoolean dialogOpened;
    private int guiTint = GT_Util.getRGBInt(Dyes.MACHINE_METAL.getRGBA());
    private final ItemStackHandler currentDisplayItemHandler = new ItemStackHandler();
    private Supplier<ItemStack> currentGetter;

    private final GT_GUIColorOverride colorOverride = GT_GUIColorOverride.get("SelectItemUIFactory");

    private int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    private final Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x222222);
    private final Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x555555);

    public SelectItemUIFactory(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
        List<ItemStack> stacks) {
        this(header, headerItem, selectedCallback, stacks, UNSELECTED);
    }

    public SelectItemUIFactory(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
        List<ItemStack> stacks, int selected) {
        this(header, headerItem, selectedCallback, stacks, selected, false);
    }

    /**
     * Constructor for a dialog to select an item from given list. Given callback may be called zero or more times
     * depending on user action.
     *
     * @param header           Header text
     * @param headerItem       ItemStack to use as Dialog icon
     * @param selectedCallback callback upon selected
     * @param stacks           list to choose from
     * @param selected         preselected item. Use {@link #UNSELECTED} for unselected. Invalid selected will be
     *                         clamped to 0 or highest index
     * @param noDeselect       true if player cannot deselect, false otherwise. If this is set to true, selectedCallback
     *                         is guaranteed to be called with a nonnull stack
     */
    public SelectItemUIFactory(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
        List<ItemStack> stacks, int selected, boolean noDeselect) {
        this.header = header;
        this.headerItem = headerItem;
        this.selectedCallback = selectedCallback;
        this.stacks = stacks;
        this.noDeselect = noDeselect;
        this.selected = noDeselect ? Math.max(0, selected) : selected;
        this.currentDisplayItemHandler.setStackInSlot(0, getCandidate(selected));
    }

    /**
     * @param anotherWindow If UI is shown on top of another window
     * @param dialogOpened  Flag to store whether this UI is opened and hence it should block duplicated creation of
     *                      this UI
     */
    public SelectItemUIFactory setAnotherWindow(boolean anotherWindow, AtomicBoolean dialogOpened) {
        this.anotherWindow = anotherWindow;
        this.dialogOpened = dialogOpened;
        return this;
    }

    public SelectItemUIFactory setGuiTint(int guiTint) {
        this.guiTint = guiTint;
        return this;
    }

    /**
     * @param currentGetter Getter for "current" item displayed that may change from external reasons
     */
    public SelectItemUIFactory setCurrentGetter(Supplier<ItemStack> currentGetter) {
        this.currentGetter = currentGetter;
        return this;
    }

    public ModularWindow createWindow(UIBuildContext buildContext) {
        ModularWindow.Builder builder = ModularWindow
            .builder(getGUIWidth(), 53 + 18 * ((stacks.size() - 1) / cols + 1));
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.setGuiTint(guiTint);

        if (headerItem != null) {
            builder.widget(
                new ItemDrawable(headerItem).asWidget()
                    .setPos(5, 5)
                    .setSize(16, 16));
        }
        builder.widget(
            new TextWidget(header).setDefaultColor(COLOR_TITLE.get())
                .setPos(25, 9));

        builder.widget(new SlotWidget(BaseSlot.phantom(currentDisplayItemHandler, 0)) {

            @Override
            public void draw(float partialTicks) {
                if (currentGetter != null) {
                    ItemStack current = currentGetter.get();
                    currentDisplayItemHandler.setStackInSlot(0, current);
                    selected = GT_Utility.findMatchingStackInList(stacks, current);
                }
                super.draw(partialTicks);
            }
        }.disableInteraction()
            .setBackground(GT_UITextures.SLOT_DARK_GRAY)
            .setPos(
                9 + getFontRenderer().getStringWidth(StatCollector.translateToLocal("GT5U.gui.select.current")),
                24))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.select.current"))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(8, 25 + (18 - getFontRenderer().FONT_HEIGHT) / 2));

        for (int i = 0; i < stacks.size(); i++) {
            final int index = i;
            builder.widget(
                new SlotWidget(new BaseSlot(new ItemStackHandler(new ItemStack[] { stacks.get(index) }), 0, true)) {

                    @Override
                    public ClickResult onClick(int buttonId, boolean doubleClick) {
                        if (buttonId == 0) {
                            setSelected(index);
                        } else if (buttonId == 1) {
                            setSelected(UNSELECTED);
                        } else {
                            return ClickResult.ACCEPT;
                        }
                        selectedCallback.accept(getCandidate(getSelected()));
                        return ClickResult.SUCCESS;
                    }

                    @Override
                    public IDrawable[] getBackground() {
                        return new IDrawable[] {
                            index == selected ? GT_UITextures.SLOT_DARK_GRAY : ModularUITextures.ITEM_SLOT };
                    }
                }.disableInteraction()
                    .setPos(7 + 18 * (index % cols), 43 + 18 * (index / cols)));
        }

        if (anotherWindow) {
            dialogOpened.set(true);
            builder.widget(new ButtonWidget() {

                @Override
                public void onDestroy() {
                    dialogOpened.set(false);
                }
            }.setOnClick(
                (clickData, widget) -> widget.getWindow()
                    .tryClose())
                .setSynced(false, false)
                .setBackground(ModularUITextures.VANILLA_BACKGROUND, new Text("x"))
                .setPos(getGUIWidth() - 15, 3)
                .setSize(12, 12));
        }

        return builder.build();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        if (selected == this.selected) return;
        int newSelected = GT_Utility.clamp(selected, UNSELECTED, stacks.size() - 1);
        if (noDeselect && newSelected == UNSELECTED) return;

        this.selected = newSelected;
        currentDisplayItemHandler.setStackInSlot(0, getCandidate(this.selected));
    }

    private ItemStack getCandidate(int listIndex) {
        return listIndex < 0 || listIndex >= stacks.size() ? null : stacks.get(listIndex);
    }

    private FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    private int getGUIWidth() {
        return 176;
    }
}
