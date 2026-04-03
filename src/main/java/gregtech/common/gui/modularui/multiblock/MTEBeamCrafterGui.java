package gregtech.common.gui.modularui.multiblock;

import static gtnhlanth.common.beamline.Particle.getParticleFromId;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter;

public class MTEBeamCrafterGui extends MTEMultiBlockBaseGui<MTEBeamCrafter> {

    public MTEBeamCrafterGui(MTEBeamCrafter multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        Map<Integer, Integer> bufferMap = multiblock.getBufferMap();

        for (Integer key : bufferMap.keySet()) {
            syncManager.syncValue("particleID" + key, new IntSyncValue(() -> bufferMap.getOrDefault(key, 0)));
        }

    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        Map<Integer, Integer> bufferMap = multiblock.getBufferMap();
        ListWidget<IWidget, ?> outputWidget = new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        IKey guiHeaderKey = IKey.dynamic(this::formatGuiHeader);

        outputWidget.child(new TextWidget<>(guiHeaderKey).marginBottom(4));

        for (Integer key : bufferMap.keySet()) {

            IntSyncValue valueSync = syncManager.findSyncHandler("particleID" + key, IntSyncValue.class);

            IKey particleKey = IKey
                .dynamic(() -> EnumChatFormatting.WHITE + getParticleNameFromID(key) + ": " + valueSync.getValue());

            outputWidget.child(new TextWidget<>(particleKey));
        }

        return outputWidget;
    }

    private String formatGuiHeader() {
        return EnumChatFormatting.GOLD + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheader");
    }

    private String getParticleNameFromID(int particleID) {
        return getParticleFromId(particleID).getLocalisedName();
    }
}
