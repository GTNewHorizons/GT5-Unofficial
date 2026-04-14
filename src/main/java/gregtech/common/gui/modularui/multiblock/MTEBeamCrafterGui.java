package gregtech.common.gui.modularui.multiblock;

import static gtnhlanth.common.beamline.Particle.getParticleFromId;

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

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter;

public class MTEBeamCrafterGui extends MTEMultiBlockBaseGui<MTEBeamCrafter> {

    public MTEBeamCrafterGui(MTEBeamCrafter multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager
            .syncValue("currentRecipeCurrentAmountA", new IntSyncValue(multiblock::getCurrentRecipeCurrentAmountA));
        syncManager
            .syncValue("currentRecipeCurrentAmountB", new IntSyncValue(multiblock::getCurrentRecipeCurrentAmountB));
        syncManager.syncValue("currentRecipeMaxAmountA", new IntSyncValue(multiblock::getCurrentRecipeMaxAmountA));
        syncManager.syncValue("currentRecipeMaxAmountB", new IntSyncValue(multiblock::getCurrentRecipeMaxAmountB));
        syncManager.syncValue("currentRecipeParticleIDA", new IntSyncValue(multiblock::getCurrentRecipeParticleIDA));
        syncManager.syncValue("currentRecipeParticleIDB", new IntSyncValue(multiblock::getCurrentRecipeParticleIDB));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        IntSyncValue currentRecipeCurrentAmountA = syncManager
            .findSyncHandler("currentRecipeCurrentAmountA", IntSyncValue.class);
        IntSyncValue currentRecipeCurrentAmountB = syncManager
            .findSyncHandler("currentRecipeCurrentAmountB", IntSyncValue.class);
        IntSyncValue currentRecipeMaxAmountA = syncManager
            .findSyncHandler("currentRecipeMaxAmountA", IntSyncValue.class);
        IntSyncValue currentRecipeMaxAmountB = syncManager
            .findSyncHandler("currentRecipeMaxAmountB", IntSyncValue.class);
        IntSyncValue currentRecipeParticleIDA = syncManager
            .findSyncHandler("currentRecipeParticleIDA", IntSyncValue.class);
        IntSyncValue currentRecipeParticleIDB = syncManager
            .findSyncHandler("currentRecipeParticleIDB", IntSyncValue.class);

        IKey guiHeaderKey = IKey.dynamic(this::formatGuiHeader);
        IKey particleAProgressKey = IKey.dynamic(
            () -> formatParticle(currentRecipeParticleIDA, currentRecipeCurrentAmountA, currentRecipeMaxAmountA));
        IKey particleBProgressKey = IKey.dynamic(
            () -> formatParticle(currentRecipeParticleIDB, currentRecipeCurrentAmountB, currentRecipeMaxAmountB));

        return new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(new TextWidget<>(guiHeaderKey).marginBottom(18))
            .child(
                new TextWidget<>(particleAProgressKey).marginBottom(9)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT))
            .child(
                new TextWidget<>(particleBProgressKey).marginBottom(9)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));
    }

    private String formatGuiHeader() {
        return EnumChatFormatting.WHITE
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheader");
    }

    private String getParticleNameFromID(int particleID) {
        return getParticleFromId(particleID).getLocalisedName();
    }

    private String formatParticle(IntSyncValue currParticleID, IntSyncValue currAmount, IntSyncValue maxAmount) {
        return EnumChatFormatting.WHITE + getParticleNameFromID(currParticleID.getIntValue())
            + ": "
            + EnumChatFormatting.GRAY
            + Math.min(currAmount.getIntValue(), maxAmount.getIntValue())
            + "/"
            + maxAmount.getIntValue();
    }

}
