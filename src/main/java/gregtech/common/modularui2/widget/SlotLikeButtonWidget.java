package gregtech.common.modularui2.widget;

import java.util.function.Supplier;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.integration.recipeviewer.RecipeViewerIngredientProvider;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.SlotTheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.GlStateManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import codechicken.nei.guihook.GuiContainerManager;

/**
 * Button widget that looks like and partially behaves like item slot. Draws supplied ItemStack, draws white overlay
 * when hovered, and allows interaction with NEI.
 */
public class SlotLikeButtonWidget extends ButtonWidget<SlotLikeButtonWidget> implements RecipeViewerIngredientProvider {

    /** Shared dummy inventory and slot used to pass item data to NEI's per-slot overlay rendering. */
    private static final InventoryBasic DUMMY_INVENTORY = new InventoryBasic("", false, 1);
    private static final Slot NEI_DUMMY_SLOT = new Slot(DUMMY_INVENTORY, 0, 1, 1);

    private final Supplier<ItemStack> itemSupplier;
    private final IDrawable itemDrawable;

    /**
     * @param itemStack ItemStack to "put into the slot".
     */
    public SlotLikeButtonWidget(ItemStack itemStack) {
        this.itemSupplier = () -> itemStack;
        this.itemDrawable = new ItemDrawable(itemStack).asIcon();
        disableHoverBackground();
    }

    /**
     * @param itemSupplier Supplier that specifies ItemStack to be present in the slot.
     */
    public SlotLikeButtonWidget(Supplier<ItemStack> itemSupplier) {
        this.itemSupplier = itemSupplier;
        this.itemDrawable = new DynamicDrawable(() -> new ItemDrawable(itemSupplier.get()).asIcon());
        disableHoverBackground();
    }

    @Override
    public WidgetThemeEntry<?> getWidgetThemeInternal(ITheme theme) {
        return theme.getItemSlotTheme();
    }

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetThemeEntry) {
        super.draw(context, widgetThemeEntry);
        renderSlotUnderlayNEI();
        itemDrawable.drawAtZero(context, getArea(), widgetThemeEntry.getTheme());
        renderSlotOverlayNEI();
        if (isHovering()) {
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.colorMask(true, true, true, false);
            GuiDraw.drawRect(1, 1, 16, 16, getHoverColor(widgetThemeEntry.getTheme()));
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.disableBlend();
        }
    }

    private void renderSlotUnderlayNEI() {
        if (!ModularUI.Mods.NEI.isLoaded()) return;
        GuiContainerManager manager = GuiContainerManager.getManager();
        GuiScreen guiScreen = getScreen().getScreenWrapper()
            .getGuiScreen();
        if (manager != null && guiScreen instanceof GuiContainer guiContainer && manager.window == guiContainer) {
            NEI_DUMMY_SLOT.putStack(itemSupplier.get());
            manager.renderSlotUnderlay(NEI_DUMMY_SLOT);
        }
    }

    private void renderSlotOverlayNEI() {
        if (!ModularUI.Mods.NEI.isLoaded()) return;
        GuiContainerManager manager = GuiContainerManager.getManager();
        GuiScreen guiScreen = getScreen().getScreenWrapper()
            .getGuiScreen();
        if (manager != null && guiScreen instanceof GuiContainer guiContainer && manager.window == guiContainer) {
            NEI_DUMMY_SLOT.putStack(itemSupplier.get());
            manager.renderSlotOverlay(NEI_DUMMY_SLOT);
        }
    }

    private int getHoverColor(WidgetTheme widgetTheme) {
        if (widgetTheme instanceof SlotTheme slotTheme) {
            return slotTheme.getSlotHoverColor();
        }
        return ITheme.getDefault()
            .getItemSlotTheme()
            .getTheme()
            .getSlotHoverColor();

    }

    @Override
    public @Nullable ItemStack getStackForRecipeViewer() {
        return itemSupplier.get();
    }

}
