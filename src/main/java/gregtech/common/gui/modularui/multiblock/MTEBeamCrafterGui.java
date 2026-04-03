package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumberCompact;
import static gtnhlanth.common.beamline.Particle.getParticleFromId;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
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
        syncManager.syncValue("currentRecipeParticleIDA", new IntSyncValue(multiblock::getCurrentRecipeParticleIDA));
        syncManager.syncValue("currentRecipeParticleIDB", new IntSyncValue(multiblock::getCurrentRecipeParticleIDB));

    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        Map<Integer, Integer> bufferMap = multiblock.getBufferMap();
        ListWidget<IWidget, ?> outputWidget = new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        IKey guiHeaderKeyCrafting = IKey.dynamic(this::formatGuiHeaderCrafting);
        IKey guiHeaderKeyBuffer = IKey.dynamic(this::formatGuiHeaderBuffer);

        IntSyncValue syncIDA = syncManager.findSyncHandler("currentRecipeParticleIDA", IntSyncValue.class);
        IntSyncValue syncIDB = syncManager.findSyncHandler("currentRecipeParticleIDB", IntSyncValue.class);

        outputWidget.child(new TextWidget<>(guiHeaderKeyCrafting).marginBottom(4));
        IKey particleKeyA = IKey.dynamic(() -> EnumChatFormatting.AQUA + getParticleNameFromID(syncIDA.getIntValue()));
        IKey particleKeyB = IKey.dynamic(() -> EnumChatFormatting.AQUA + getParticleNameFromID(syncIDB.getIntValue()));
        outputWidget.child(new TextWidget<>(particleKeyA));
        outputWidget.child(new TextWidget<>(particleKeyB))
            .marginBottom(4);

        outputWidget.child(new TextWidget<>(guiHeaderKeyBuffer).marginBottom(4));

        for (Integer key : bufferMap.keySet()) {

            IntSyncValue valueSync = syncManager.findSyncHandler("particleID" + key, IntSyncValue.class);

            IKey particleKey = IKey.dynamic(
                () -> EnumChatFormatting.WHITE + getParticleNameFromID(key)
                    + ": "
                    + formatNumberCompact(valueSync.getValue()));

            outputWidget.child(
                new IDrawable.DrawableWidget(getParticleTexture(key)).setEnabledIf(w -> valueSync.getValue() > 0));
            outputWidget.child(new TextWidget<>(particleKey).setEnabledIf(w -> valueSync.getValue() > 0));
        }

        return outputWidget;
    }

    private String formatGuiHeaderBuffer() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheaderbuffer");
    }

    private String formatGuiHeaderCrafting() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheadercrafting");
    }

    private String getParticleNameFromID(int particleID) {
        return getParticleFromId(particleID).getLocalisedName();
    }

    private UITexture getParticleTexture(int particleID) {
        return getParticleFromId(particleID).getTexture();
    }
}
