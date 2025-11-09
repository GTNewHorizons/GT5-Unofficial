package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public class FuelConfigPanel {

    private static final int WIDTH = 78;
    private static final int HEIGHT = 130;

    public static ModularPanel openPanel(PanelSyncManager syncManager, ForgeOfGodsData data, ModularPanel panel,
        ModularPanel parent) {
        panel.relative(parent)
            .size(WIDTH, HEIGHT)
            .topRel(0)
            .leftRelOffset(1, -3);
        panel.onClose();

        Flow column = new Column().size(WIDTH, HEIGHT);

        // Header
        column.child(
            IKey.str(translateToLocal("gt.blockmachines.multimachine.FOG.fuelconsumption"))
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(WIDTH - 4)
                .alignX(0.5f)
                .marginTop(5));

        // Textbox
        TextFieldWidget textBox = new TextFieldWidget().setFormatAsInteger(true)
            .setNumbers(() -> 1, () -> GodforgeMath.calculateMaxFuelFactor(data))
            .setTextAlignment(Alignment.CENTER)
            .value(new IntSyncValue(data::getFuelConsumptionFactor, data::setFuelConsumptionFactor))
            .setTooltipOverride(true)
            .size(70, 18)
            .marginLeft(4)
            .marginTop(3);
        column.child(textBox);

        // todo actually these are probably not needed
        /*
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_CFCE,
         * CFCE,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_GEM,
         * GEM,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_TSE,
         * TSE,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         */

        // Info widget todo

        // Fuel type header

        // Fuel selector

        // Fuel usage text

        return panel.child(column);
    }
}
