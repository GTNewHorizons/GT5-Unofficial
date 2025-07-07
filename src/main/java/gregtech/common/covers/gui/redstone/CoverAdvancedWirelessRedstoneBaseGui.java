package gregtech.common.covers.gui.redstone;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.common.covers.gui.CoverGui;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;

import static net.minecraft.util.StatCollector.translateToLocal;

public class CoverAdvancedWirelessRedstoneBaseGui extends CoverGui<CoverAdvancedWirelessRedstoneBase> {

    public CoverAdvancedWirelessRedstoneBaseGui(CoverAdvancedWirelessRedstoneBase cover) {super(cover);}


    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        StringSyncValue frequencySyncer = new StringSyncValue(cover::getFrequency,cover::setFrequency);
        syncManager.syncValue("frequency", frequencySyncer);

    }

    @Override
    protected int getGUIWidth() {
        return 250;
    }

    private Flow makeFrequencyRow(StringSyncValue freqSync)
    {
        return Flow.row().child(
            new TextFieldWidget().width(80).value(freqSync))
            .child(new TextWidget(translateToLocal("gt.interact.desc.freq")).color(Color.GREY.main))
            ;
    }
    private static Flow makePrivateFrequencyRow()
    {
        //TODO: implement uuid
        return Flow.row();}
}
