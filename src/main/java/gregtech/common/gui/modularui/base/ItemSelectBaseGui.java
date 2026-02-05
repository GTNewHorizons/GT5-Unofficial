package gregtech.common.gui.modularui.base;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;

/**
 * Creates UI for selecting item from given list. This is client-only UI to allow using client-preferred settings.
 */
public class ItemSelectBaseGui {

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
    private int guiTint = Dyes.GUI_METAL.toInt();
    private Supplier<ItemStack> currentGetter;

    private final GUIColorOverride colorOverride = GUIColorOverride.get("SelectItemUIFactory");

    private int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    private final Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x222222);
    private final Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x555555);

    public ItemSelectBaseGui(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
        List<ItemStack> stacks) {
        this(header, headerItem, selectedCallback, stacks, UNSELECTED);
    }

    public ItemSelectBaseGui(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
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
    public ItemSelectBaseGui(String header, ItemStack headerItem, Consumer<ItemStack> selectedCallback,
        List<ItemStack> stacks, int selected, boolean noDeselect) {
        this.header = header;
        this.headerItem = headerItem;
        this.selectedCallback = selectedCallback;
        this.stacks = stacks;
        this.noDeselect = noDeselect;
        this.selected = noDeselect ? Math.max(0, selected) : selected;
    }

    /**
     * @param anotherWindow If UI is shown on top of another window
     * @param dialogOpened  Flag to store whether this UI is opened and hence it should block duplicated creation of
     *                      this UI
     */
    public ItemSelectBaseGui setAnotherWindow(boolean anotherWindow, AtomicBoolean dialogOpened) {
        this.anotherWindow = anotherWindow;
        this.dialogOpened = dialogOpened;
        return this;
    }

    public ItemSelectBaseGui setGuiTint(int guiTint) {
        this.guiTint = guiTint;
        return this;
    }

    /**
     * @param currentGetter Getter for "current" item displayed that may change from external reasons
     */
    public ItemSelectBaseGui setCurrentGetter(Supplier<ItemStack> currentGetter) {
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

        int currentSlotX = 9
            + getFontRenderer().getStringWidth(StatCollector.translateToLocal("GT5U.gui.select.current"));
        int currentSlotY = 24;
        builder.widget(new DrawableWidget() {

            @Override
            public void onScreenUpdate() {
                super.onScreenUpdate();
                if (currentGetter != null) {
                    ItemStack current = currentGetter.get();
                    selected = GTUtility.findMatchingStackInList(stacks, current);
                }
            }
        }.setDrawable(GTUITextures.SLOT_DARK_GRAY)
            .setPos(currentSlotX, currentSlotY)
            .setSize(18, 18))
            .widget(
                new ItemDrawable(() -> getCandidate(getSelected())).asWidgetWithTooltip()
                    .setPos(currentSlotX + 1, currentSlotY + 1));
        builder.widget(
            new TextWidget(StatCollector.translateToLocal("GT5U.gui.select.current"))
                .setPos(8, 25 + (18 - getFontRenderer().FONT_HEIGHT) / 2));

        for (int i = 0; i < stacks.size(); i++) {
            final int index = i;
            builder.widget(new ButtonWidget() {

                @Override
                public void draw(float partialTicks) {
                    GlStateManager.pushMatrix();
                    // so that item z levels are properly ordered
                    GlStateManager.translate(0, 0, 150 * getWindowLayer());
                    new ItemDrawable(stacks.get(index)).draw(1, 1, 16, 16, partialTicks);
                    GlStateManager.popMatrix();
                }
            }.setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    setSelected(index, widget);
                } else {
                    setSelected(UNSELECTED, widget);
                }
                selectedCallback.accept(getCandidate(getSelected()));
                if (clickData.shift) {
                    widget.getWindow()
                        .tryClose();
                }
            })
                .setSynced(false, false)
                .dynamicTooltip(() -> getItemTooltips(index))
                .setUpdateTooltipEveryTick(true)
                .setBackground(
                    () -> new IDrawable[] {
                        index == selected ? GTUITextures.SLOT_DARK_GRAY : ModularUITextures.ITEM_SLOT, })
                .setPos(7 + 18 * (index % cols), 43 + 18 * (index / cols))
                .setSize(18, 18));
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

    public void setSelected(int selected, Widget widget) {
        if (selected == this.selected) return;
        int newSelected = GTUtility.clamp(selected, UNSELECTED, stacks.size() - 1);
        if (noDeselect && newSelected == UNSELECTED) return;

        this.selected = newSelected;
    }

    protected List<String> getItemTooltips(final int index) {
        return GuiHelper.getItemTooltip(stacks.get(index));
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
