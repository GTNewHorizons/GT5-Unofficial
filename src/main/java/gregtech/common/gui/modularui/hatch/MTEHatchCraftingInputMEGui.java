package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.tileentities.machines.MTEHatchCraftingInputME.SLOT_MANUAL_START;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import appeng.api.config.TerminalFontSize;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.storage.data.IAEStack;
import appeng.client.render.StackSizeRenderer;
import appeng.items.misc.ItemEncodedPattern;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

public class MTEHatchCraftingInputMEGui extends MTEHatchBaseGui<MTEHatchCraftingInputME> {

    private static final String PATTERN_INV_NAME = "pattern_inv";
    private static final String MANUAL_ITEM_INV_NAME = "manual_item_inv";
    private static final int PATTERN_SLOT_ROW = 4;
    private static final int PATTERN_SLOT_PER_ROW = 9;
    private static final int MANUAL_SLOT_ROW = 3;
    private static final int MANUAL_SLOT_PER_ROW = 3;

    public MTEHatchCraftingInputMEGui(MTEHatchCraftingInputME hatch) {
        super(hatch);
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + 16;
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    private SlotGroupWidget createSlots(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(PATTERN_INV_NAME, PATTERN_SLOT_ROW);

        String[] matrix = new String[PATTERN_SLOT_ROW];
        String repeat = StringUtils.getRepetitionOf('s', PATTERN_SLOT_PER_ROW);
        Arrays.fill(matrix, repeat);

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new PatternSlot().slot(
                    new ModularSlot(hatch.inventoryHandler, index)
                        .filter(itemStack -> itemStack.getItem() instanceof ICraftingPatternItem)
                        .changeListener((itemStack, onlyAmount, client, init) -> {
                            if (!client) {
                                hatch.onPatternChange(index, itemStack);
                            }
                        })
                        .slotGroup(PATTERN_INV_NAME)))
            .build()
            .coverChildren()
            .alignX(Alignment.CENTER);
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {

        return super.createLeftCornerFlow(panel, syncManager).child(createOptimizerButton())
            .child(createShowPatternButton())
            .child(createExportButton())
            .child(createDoublePatternButton())
            .child(createManualItemsButton(syncManager));
    }

    private ToggleButton createOptimizerButton() {
        BooleanSyncValue optimizerSync = new BooleanSyncValue(
            () -> !hatch.disablePatternOptimization,
            val -> hatch.disablePatternOptimization = !val);

        return new ToggleButton().value(optimizerSync)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_PATTERN_OPTIMIZE)
            .addTooltipLine(StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.optimize_pattern"))
            .addTooltip(
                true,
                StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.enable"))
            .addTooltip(
                false,
                StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.disabled"));
    }

    private ToggleButton createShowPatternButton() {
        BooleanSyncValue showPatternSync = new BooleanSyncValue(
            () -> hatch.showPattern,
            val -> hatch.showPattern = val);

        return new ToggleButton().value(showPatternSync)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
            .addTooltip(
                true,
                StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.show_pattern.enable"))
            .addTooltip(
                false,
                StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.show_pattern.disabled"));
    }

    private ButtonWidget<?> createExportButton() {
        InteractionSyncHandler exportSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
            if (!mouseDelta.isClient() && mouseDelta.mouseButton == 0) {
                hatch.refundAll(false);
            }
        });

        return new ButtonWidget<>().syncHandler(exportSyncHandler)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
            .addTooltipLine(StatCollector.translateToLocal("GT5U.gui.tooltip.hatch.crafting_input_me.export"));
    }

    private ButtonWidget<?> createDoublePatternButton() {
        InteractionSyncHandler doubleSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
            if (!mouseDelta.isClient()) {
                int val = mouseDelta.shift ? 1 : 0;
                if (mouseDelta.mouseButton == 1) val |= 0b10;
                hatch.doublePatterns(val);
            }
        });

        return new ButtonWidget<>().syncHandler(doubleSyncHandler)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_X2)
            .addTooltipLine(StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.DoublePatterns"));
    }

    private ButtonWidget<?> createManualItemsButton(PanelSyncManager syncManager) {
        IPanelHandler popupPanel = syncManager
            .syncedPanel("manual_slots_panel", true, (manager, handler) -> createManualSlotUI(manager));

        return new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_PLUS_LARGE)
            .addTooltipLine(
                StatCollector.translateToLocal("GT5U.gui.tooltip.hatch.crafting_input_me.place_manual_items"))
            .onMousePressed(mouseButton -> {
                popupPanel.openPanel();
                return popupPanel.isPanelOpen();
            });
    }

    private ModularPanel createManualSlotUI(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(MANUAL_ITEM_INV_NAME, MANUAL_SLOT_ROW);

        String[] matrix = new String[MANUAL_SLOT_ROW];
        String repeat = StringUtils.getRepetitionOf('s', MANUAL_SLOT_PER_ROW);
        Arrays.fill(matrix, repeat);

        return createPopUpPanel("manual_slots_panel").size(68, 76)
            .bottomRelOffset(0.5f, 52)
            .child(
                SlotGroupWidget.builder()
                    .matrix(matrix)
                    .key(
                        's',
                        index -> new ItemSlot().slot(
                            new ModularSlot(hatch.inventoryHandler, SLOT_MANUAL_START + index)
                                .slotGroup(MANUAL_ITEM_INV_NAME)

                        ))
                    .build()
                    .coverChildren()
                    .top(16)
                    .alignX(Alignment.CENTER));
    }

    private static class PatternSlot extends ItemSlot {

        PatternSlot() {}

        @Override
        public @Nullable IDrawable getCurrentBackground(ITheme theme, WidgetThemeEntry<?> widgetTheme) {
            IDrawable background = super.getCurrentBackground(theme, widgetTheme);
            return IDrawable.of(background, GTGuiTextures.OVERLAY_SLOT_PATTERN_ME);
        }

        @Override
        protected ItemStack getItemStackForRendering(ItemStack stack, boolean dragging) {
            if (dragging || stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
                return stack;
            }
            IAEStack<?> outputAE = patternItem.getOutputAE(stack);
            if (outputAE == null) {
                return stack;
            }
            ItemStack output = outputAE.getItemStackForNEI(0);
            return output != null ? output : stack;
        }

        @Override
        protected void drawSlotAmountText(int amount, String format) {
            ModularSlot slot = this.getSlot();
            ItemStack stack = slot.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
                super.drawSlotAmountText(amount, format);
                return;
            }
            IAEStack<?> outputAE = patternItem.getOutputAE(stack);
            if (outputAE == null) {
                super.drawSlotAmountText(amount, format);
                return;
            }

            long stackSize = outputAE.getStackSize();
            if (stackSize > 1) {
                StackSizeRenderer
                    .drawStackSize(1, 1, stackSize, getContext().getFontRenderer(), TerminalFontSize.SMALL);
            }
        }
    }
}
