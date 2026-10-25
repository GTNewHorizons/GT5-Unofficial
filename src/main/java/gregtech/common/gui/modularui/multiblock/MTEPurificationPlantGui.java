package gregtech.common.gui.modularui.multiblock;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.purification.LinkedPurificationUnit;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase;
import gregtech.common.tileentities.machines.multi.purification.PurificationUnitStatus;

public class MTEPurificationPlantGui extends MTEMultiBlockBaseGui<MTEPurificationPlant> {

    private static final List<String> TIER_ORDER = Arrays.asList(
        "multimachine.purificationunitclarifier",
        "multimachine.purificationunitozonation",
        "multimachine.purificationunitflocculator",
        "multimachine.purificationunitphadjustment",
        "multimachine.purificationunitplasmaheater",
        "multimachine.purificationunituvtreatment",
        "multimachine.purificationunitdegasifier",
        "multimachine.purificationunitextractor");

    // Index-aligned with TIER_ORDER
    private static final List<ItemList> TIER_ITEMS = Arrays.asList(
        ItemList.Machine_Multi_PurificationUnitClarifier,
        ItemList.Machine_Multi_PurificationUnitOzonation,
        ItemList.Machine_Multi_PurificationUnitFlocculator,
        ItemList.Machine_Multi_PurificationUnitPhAdjustment,
        ItemList.Machine_Multi_PurificationUnitPlasmaHeater,
        ItemList.Machine_Multi_PurificationUnitUVTreatment,
        ItemList.Machine_Multi_PurificationUnitDegasifier,
        ItemList.Machine_Multi_PurificationUnitParticleExtractor);

    private static final int NOT_LINKED = -1;

    public MTEPurificationPlantGui(MTEPurificationPlant multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue debugMode = new BooleanSyncValue(multiblock::isDebugMode, multiblock::setDebugMode).allowC2S();

        syncManager.syncValue("debugMode", debugMode);
        for (int i = 0; i < TIER_ORDER.size(); i++) {
            String unitName = TIER_ORDER.get(i);
            syncManager.syncValue("linkedUnitStatus", i, new IntSyncValue(() -> getLinkedUnitStatus(unitName)));
        }
    }

    /**
     * @return the status ordinal of the linked unit named {@code unitName}, or {@link #NOT_LINKED}
     */
    private int getLinkedUnitStatus(String unitName) {
        for (LinkedPurificationUnit unit : multiblock.getLinkedUnits()) {
            MTEPurificationUnitBase<?> mte = unit.metaTileEntity();
            if (mte.isValid() && mte.mName.equals(unitName)) {
                return mte.status()
                    .ordinal();
            }
        }
        return NOT_LINKED;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> widget = super.createTerminalTextWidget(syncManager, parent);

        widget.child(machineTierRow(syncManager));
        widget.childIf(
            baseMetaTileEntity.isActive(),
            () -> createProgressBar().topRel(0)
                .leftRel(0));
        return widget;
    }

    public Flow machineTierRow(PanelSyncManager syncManager) {
        Flow row = Flow.row()
            .collapseDisabledChild();
        row.paddingTop(4)
            .paddingBottom(4)
            .coverChildrenHeight();
        for (int i = 0; i < TIER_ORDER.size(); i++) {
            IntSyncValue status = syncManager.findSyncHandler("linkedUnitStatus", i, IntSyncValue.class);
            String translationKey = "GT5U.gui." + TIER_ORDER.get(i);
            int tier = i + 1;
            row.child(
                new ItemDisplayWidget().disableHoverBackground()
                    .size(20)
                    .background(IDrawable.EMPTY)
                    .overlay(new DynamicDrawable(() -> getOverlayIcon(toStatus(status))))
                    .item(
                        TIER_ITEMS.get(i)
                            .get(1))
                    .tooltipDynamic(tooltip -> {
                        tooltip.addLine(StatCollector.translateToLocalFormatted("GT5U.gui.purification.tier", tier));
                        tooltip.addLine(StatCollector.translateToLocal(translationKey));
                        tooltip.addLine(getStatusString(toStatus(status)));
                    })
                    .tooltipAutoUpdate(true)
                    .setEnabledIf(w -> status.getIntValue() != NOT_LINKED));
        }

        return row;
    }

    private static PurificationUnitStatus toStatus(IntSyncValue status) {
        return PurificationUnitStatus.values()[status.getIntValue()];
    }

    private IDrawable getOverlayIcon(PurificationUnitStatus status) {
        return switch (status) {
            case ACTIVE -> GTGuiTextures.WATER_PURIFICATION_ONLINE;
            case IDLE, DISABLED -> GTGuiTextures.WATER_PURIFICATION_IDLE;
            case INCOMPLETE_STRUCTURE -> GTGuiTextures.WATER_PURIFICATION_OFFLINE;
        };
    }

    private String getStatusString(PurificationUnitStatus status) {
        return switch (status) {
            case ACTIVE -> EnumChatFormatting.GREEN
                + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.active");
            case IDLE -> EnumChatFormatting.GREEN
                + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.idle");
            case DISABLED -> EnumChatFormatting.YELLOW
                + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.disabled");
            case INCOMPLETE_STRUCTURE -> EnumChatFormatting.RED
                + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.incomplete");
        };
    }

    private ParentWidget<?> createProgressBar() {
        ParentWidget<?> holder = new ParentWidget<>();
        ProgressWidget widget = new ProgressWidget();
        widget.value(new DoubleSyncValue(() -> (float) multiblock.getProgresstime() / multiblock.getMaxProgresstime()))
            .direction(ProgressWidget.Direction.RIGHT)
            .size(182, 11)
            .texture(GTGuiTextures.PROGRESSBAR_PURIFICATION_UNIT, 147);
        TextWidget<?> text = new TextWidget<>(IKey.dynamic(() -> {
            int progress = multiblock.getProgresstime() / 20;
            int maxProgress = multiblock.getMaxProgresstime() / 20;
            return StatCollector.translateToLocalFormatted("gt.item.desc.progress.bare", progress, maxProgress);
        })).center()
            .color(0x999999);

        holder.height(11);
        holder.fullWidth();
        holder.child(widget);
        holder.child(text);
        return holder;
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue debugMode = syncManager.findSyncHandler("debugMode", BooleanSyncValue.class);
        return super.createButtonColumn(panel, syncManager).child(new ButtonWidget<>().onMousePressed((a) -> {
            if (multiblock.getBaseMetaTileEntity()
                .isActive()) {
                return false;
            }
            debugMode.setBoolValue(!debugMode.getBoolValue());
            return true;
        })
            .tooltip(
                new RichTooltip().add(StatCollector.translateToLocal("GT5U.gui.tooltip.purification_plant.debug_mode")))
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.getBaseMetaTileEntity()
                    .isAllowedToWork()) {
                    return GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED;
                }
                return GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT;

            })

            )
            .background(new DynamicDrawable(() -> {
                if (debugMode.getBoolValue()) {
                    return GTGuiTextures.BUTTON_STANDARD_PRESSED;
                } else {
                    return GTGuiTextures.BUTTON_STANDARD;
                }
            }))

        );
    }
}
