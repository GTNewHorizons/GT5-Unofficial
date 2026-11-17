package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import goodgenerator.blocks.tileEntity.MTELargeEssentiaSmeltery;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;

public class MTELargeEssentiaSmelteryGui extends TTMultiblockBaseGui<MTELargeEssentiaSmeltery> {

    public MTELargeEssentiaSmelteryGui(MTELargeEssentiaSmeltery multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue expectedPower = syncManager.findSyncHandler("expectedPower", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "gg.gui.text.large_essentia_smeltery.requires",
                    formatNumber(expectedPower.getIntValue())))
                .asWidget());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("expectedPower", new IntSyncValue(multiblock::expectedPower));
    }
}
