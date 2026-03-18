package gregtech.common.gui.modularui.multiblock;

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
        syncManager
            .syncValue("currentRecipeCurrentAmountA", new IntSyncValue(multiblock::getCurrentRecipeCurrentAmountA));
        syncManager
            .syncValue("currentRecipeCurrentAmountB", new IntSyncValue(multiblock::getCurrentRecipeCurrentAmountB));
        syncManager.syncValue("currentRecipeMaxAmountA", new IntSyncValue(multiblock::getCurrentRecipeMaxAmountA));
        syncManager.syncValue("currentRecipeMaxAmountB", new IntSyncValue(multiblock::getCurrentRecipeMaxAmountB));
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

        IKey guiHeaderKey = IKey.dynamic(this::formatGuiHeader);
        IKey particleAProgressKey = IKey
            .dynamic(() -> formatParticle(1, currentRecipeCurrentAmountA, currentRecipeMaxAmountA));
        IKey particleBProgressKey = IKey
            .dynamic(() -> formatParticle(2, currentRecipeCurrentAmountB, currentRecipeMaxAmountB));

        return new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(new TextWidget<>(guiHeaderKey).marginBottom(18))
            .child(new TextWidget<>(particleAProgressKey).marginBottom(9))
            .child(new TextWidget<>(particleBProgressKey).marginBottom(9));
    }

    private String formatGuiHeader() {
        return EnumChatFormatting.WHITE
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.beamcrafter.guiheader");
    }

    private String formatParticle(int particleNum, IntSyncValue currAmount, IntSyncValue maxAmount) {
        String output;
        if (particleNum == 1) {
            output = EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.gui.text.beamcrafter.particleA");
        } else {
            output = EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.gui.text.beamcrafter.particleB");
        }
        output += ": " + EnumChatFormatting.GRAY
            + Math.min(currAmount.getIntValue(), maxAmount.getIntValue())
            + "/"
            + maxAmount.getIntValue();
        return output;
    }

}
