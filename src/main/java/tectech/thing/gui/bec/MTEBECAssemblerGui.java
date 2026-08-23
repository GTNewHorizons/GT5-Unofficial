package tectech.thing.gui.bec;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.common.modularui2.sync.NaniteTierSyncValue;
import tectech.thing.metaTileEntity.multi.bec.MTEBECAssembler;

public class MTEBECAssemblerGui extends MTEBECMultiblockBaseGui<MTEBECAssembler> {

    public MTEBECAssemblerGui(MTEBECAssembler multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        NaniteTierSyncValue naniteTierSyncer = new NaniteTierSyncValue(
            multiblock::getCurrentNaniteTier,
            multiblock::setCurrentNaniteTier);
        IntSyncValue naniteCountSyncer = new IntSyncValue(
            multiblock::getAvailableNanites,
            multiblock::setAvailableNanites);

        syncManager.syncValue("naniteTier", naniteTierSyncer);
        syncManager.syncValue("naniteCount", naniteCountSyncer);

        TextWidget<?> naniteWidget = IKey
            .dynamic(
                () -> GRAY + StatCollector.translateToLocal("GT5U.gui.text.providing-nanites")
                    + "\n  "
                    + AQUA
                    + (naniteTierSyncer.getEnumValue() == null ? StatCollector.translateToLocal("GT5U.gui.text.nil")
                        : naniteTierSyncer.getEnumValue()
                            .describe())
                    + GRAY
                    + " x "
                    + GOLD
                    + NumberFormatUtil.formatNumber(naniteCountSyncer.getIntValue())
                    + GRAY)
            .asWidget()
            .widthRel(1);

        return super.createTerminalTextWidget(syncManager, parent).child(naniteWidget)
            .child(createCondensateWidget(syncManager, parent));
    }
}
