package gregtech.common.gui.modularui.multiblock;

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
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.purification.LinkedPurificationUnit;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;

public class MTEPurificationPlantGui extends MTEMultiBlockBaseGui<MTEPurificationPlant> {

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

        for (Object obj : linkedPurifications.getValue()) {
            if (obj instanceof LinkedPurificationUnit unit) {
                widget.child(machineRow(unit));

            }
        }
        return widget;
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

    public Flow machineRow(LinkedPurificationUnit LinkedUnit) {
        return new Row().paddingBottom(4)
            .paddingTop(4)
            .coverChildrenHeight()
            .child(
                new ItemDisplayWidget().background(IDrawable.EMPTY)
                    .disableHoverBackground()
                    .size(14)
                    .item(
                        LinkedUnit.metaTileEntity()
                            .getStackForm(1)))
            .child(
                new ListWidget<>().paddingLeft(4)
                    .coverChildrenHeight()
                    // size of the terminal - size of the icon - size of border
                    .width(186 - 16 - 22)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(
                        IKey.str(
                            LinkedUnit.metaTileEntity()
                                .getLocalName()
                                .replaceAll("Purification Unit", ""))
                            .asWidget())
                    .child(
                        IKey.str(LinkedUnit.getStatusString())
                            .asWidget()));
    }
}
