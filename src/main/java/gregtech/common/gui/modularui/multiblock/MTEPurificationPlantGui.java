package gregtech.common.gui.modularui.multiblock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.purification.LinkedPurificationUnit;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;

public class MTEPurificationPlantGui extends MTEMultiBlockBaseGui<MTEPurificationPlant> {

    private static final Map<String, Integer> MODULE_TIERS = new HashMap<>();
    private static final List<String> TIER_ORDER = Arrays.asList(
        "multimachine.purificationunitclarifier",
        "multimachine.purificationunitozonation",
        "multimachine.purificationunitflocculator",
        "multimachine.purificationunitphadjustment",
        "multimachine.purificationunitplasmaheater",
        "multimachine.purificationunituvtreatment",
        "multimachine.purificationunitdegasifier",
        "multimachine.purificationunitextractor");

    static {
        for (int i = 0; i < TIER_ORDER.size(); i++) {
            MODULE_TIERS.put(TIER_ORDER.get(i), i + 1);
        }
    }

    public MTEPurificationPlantGui(MTEPurificationPlant multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue debugMode = new BooleanSyncValue(multiblock::isDebugMode, multiblock::setDebugMode);

        GenericListSyncHandler<LinkedPurificationUnit> linkedPurifierUnits = new GenericListSyncHandler.Builder<LinkedPurificationUnit>()
            .getter(multiblock::getLinkedUnits)
            .setter(links -> {
                multiblock.getLinkedUnits()
                    .clear();
                multiblock.getLinkedUnits()
                    .addAll(links);
            })
            .serializer((buf, unit) -> {
                buf.writeNBTTagCompoundToBuffer(unit.writeLinkDataToNBT());

            })
            .deserializer(buffer -> new LinkedPurificationUnit(buffer.readNBTTagCompoundFromBuffer()))
            .copy(unit -> new LinkedPurificationUnit(unit.writeLinkDataToNBT()))
            .build();

        syncManager.syncValue("debugMode", debugMode);
        syncManager.syncValue("linkedPurifierUnits", linkedPurifierUnits);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> widget = super.createTerminalTextWidget(syncManager, parent);

        GenericListSyncHandler linkedPurifications = syncManager
            .findSyncHandler("linkedPurifierUnits", GenericListSyncHandler.class);

        Map<Integer, LinkedPurificationUnit> unitByTier = new HashMap<>();

        for (Object obj : linkedPurifications.getValue()) {
            if (obj instanceof LinkedPurificationUnit unit) {
                Integer tier = getModuleTier(unit.metaTileEntity());
                if (tier != null) {
                    unitByTier.put(tier, unit);
                }
            }
        }

        widget.child(machineTierRow(unitByTier));
        widget.childIf(
            multiblock.getBaseMetaTileEntity()
                .isActive(),
            createProgressBar().align(Alignment.TopLeft));
        return widget;
    }

    public Row machineTierRow(Map<Integer, LinkedPurificationUnit> unitByTier) {
        Row row = new Row();
        row.paddingTop(4)
            .paddingBottom(4)
            .coverChildrenHeight();
        for (int tier = 1; tier <= 8; tier++) {
            LinkedPurificationUnit unit = unitByTier.get(tier);

            if (unit != null) {
                MetaTileEntity mte = unit.metaTileEntity();
                String moduleName = getLocalizedModuleName(mte);

                int finalTier = tier;
                row.child(
                    new ItemDisplayWidget().disableHoverBackground()
                        .size(20)
                        .background(IDrawable.EMPTY)
                        .overlay(getOverlayIcon(unit))
                        .item(mte.getStackForm(1))
                        .tooltip(tooltip -> {
                            tooltip.addLine(
                                StatCollector.translateToLocalFormatted("GT5U.gui.purification.tier", finalTier));
                            tooltip.addLine(moduleName);
                            tooltip.addLine(unit.getStatusString());
                        }));
            }
        }

        return row;
    }

    private IDrawable getOverlayIcon(LinkedPurificationUnit LinkedUnit) {
        String status = LinkedUnit.getStatusUnlocalized();
        return switch (status) {
            case "active" -> GTGuiTextures.WATER_PURIFICATION_ONLINE;
            case "disabled", "idle" -> GTGuiTextures.WATER_PURIFICATION_IDLE; // only incomplete left
            default -> GTGuiTextures.WATER_PURIFICATION_OFFLINE;
        };
    }

    private ParentWidget<Flow> createProgressBar() {
        ParentWidget<Flow> holder = new ParentWidget<>();
        ProgressWidget widget = new ProgressWidget();
        widget.progress(() -> (float) multiblock.getProgresstime() / multiblock.getMaxProgresstime())
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
        holder.widthRel(1);
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

    private String getLocalizedModuleName(MetaTileEntity mte) {
        String metaName = mte.getMetaName();
        String translationKey = "GT5U.gui." + metaName;
        return StatCollector.translateToLocal(translationKey);
    }

    private Integer getModuleTier(MetaTileEntity mte) {
        return MODULE_TIERS.get(mte.mName);
    }
}
