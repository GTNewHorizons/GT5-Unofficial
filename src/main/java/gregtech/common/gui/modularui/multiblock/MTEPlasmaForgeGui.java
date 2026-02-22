package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEPlasmaForge;

public class MTEPlasmaForgeGui extends MTEMultiBlockBaseGui<MTEPlasmaForge> {

    public MTEPlasmaForgeGui(MTEPlasmaForge multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue convergenceSyncer = new BooleanSyncValue(
            multiblock::getConvergenceStatus,
            multiblock::setConvergenceStatus);
        syncManager.syncValue("convergence", convergenceSyncer);
        IntSyncValue catalystTypeSyncer = new IntSyncValue(
            multiblock::getCatalystTypeForRecipesWithoutCatalyst,
            multiblock::setCatalystTypeForRecipesWithoutCatalyst);
        syncManager.syncValue("catalystType", catalystTypeSyncer);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createConvergenceButton(syncManager, panel));

    }

    protected IWidget createConvergenceButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler catalystSelectPanel = syncManager
            .syncedPanel("catalystPanel", true, (p_syncManager, syncHandler) -> openCatalystPanel(syncManager, parent));

        BooleanSyncValue convergenceSyncer = syncManager.findSyncHandler("convergence", BooleanSyncValue.class);
        return new ButtonWidget<>().size(18)
            .marginBottom(2)
            .tooltip(
                t -> t.addLine(translateToLocal("GT5U.DTPF.convergencebutton"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.0"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.1")))
            .overlay(new DynamicDrawable(() -> {
                boolean convergenceActive = convergenceSyncer.getBoolValue();
                if (convergenceActive) {
                    return GTGuiTextures.TT_SAFE_VOID_ON;
                }
                return GTGuiTextures.TT_SAFE_VOID_OFF;
            }))
            .onMousePressed(mouseButton -> {
                if (mouseButton == 1) { // right click, open ui
                    if (!catalystSelectPanel.isPanelOpen()) {
                        catalystSelectPanel.openPanel();
                    } else {
                        catalystSelectPanel.closePanel();
                    }
                } else if (mouseButton == 0) { // left click, toggle convergence value
                    ItemStack controllerStack = multiblock.getControllerSlot();
                    if (controllerStack == null) return false;
                    if (!controllerStack.isItemEqual(ItemList.Transdimensional_Alignment_Matrix.get(1))) return false;

                    convergenceSyncer.setBoolValue(!convergenceSyncer.getBoolValue());
                }

                return true;
            });
    }

    private static final int WIDTH = 60;
    private static final int HEIGHT = 52;
    private static final int PADDING_SIDES = 4;

    private ModularPanel openCatalystPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel returnPanel = new ModularPanel("catalystPanel").size(WIDTH, HEIGHT)
            .relative(parent)
            .leftRel(1)
            .topRel(0.9f);
        IntSyncValue catalystSyncer = syncManager.findSyncHandler("catalystType", IntSyncValue.class);
        Flow holdingColumn = Flow.column()
            .sizeRel(1)
            .paddingTop(4);
        holdingColumn.child(
            IKey.lang("GT5U.DTPF.catalysttier")
                .alignment(Alignment.Center)
                .asWidget()
                .marginBottom(2));
        holdingColumn.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, 5)
                .setTextAlignment(Alignment.CENTER)
                .setDefaultNumber(1)
                .value(catalystSyncer)
                .size(WIDTH - PADDING_SIDES * 2, 18));

        returnPanel.child(holdingColumn);

        return returnPanel;
    }

}
