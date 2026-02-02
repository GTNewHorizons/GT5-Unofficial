package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.GTValues.VN;
import static net.minecraft.util.EnumChatFormatting.RESET;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeFluidExtractor;

public class MTELargeFluidExtractorGui extends MTEMultiBlockBaseGui<MTELargeFluidExtractor> {

    public MTELargeFluidExtractorGui(MTELargeFluidExtractor multiblock) {
        super(multiblock);
    }

    private static final int BASE_CASING_COUNT = 24 + 24 + 9;
    private static final int MAX_HATCHES_ALLOWED = 16;

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue structureBadCasingCountSyncer = new BooleanSyncValue(multiblock::getStructureBadCasingCount);
        BooleanSyncValue structureBadGlassTierSyncer = new BooleanSyncValue(multiblock::getStructureBadGlassTier);

        IntSyncValue hatchTierSyncer = new IntSyncValue(multiblock::getHatchTier);
        IntSyncValue glassTierSyncer = new IntSyncValue(multiblock::getGlassTier);
        IntSyncValue casingCountSyncer = new IntSyncValue(multiblock::getCasingAmount);

        syncManager.syncValue("badCasingCount", structureBadCasingCountSyncer);
        syncManager.syncValue("badGlassTier", structureBadGlassTierSyncer);

        syncManager.syncValue("hatchTier", hatchTierSyncer);
        syncManager.syncValue("glassTier", glassTierSyncer);
        syncManager.syncValue("casingCount", casingCountSyncer);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue badCasingSyncer = syncManager.findSyncHandler("badCasingCount", BooleanSyncValue.class);
        BooleanSyncValue badGlassSyncer = syncManager.findSyncHandler("badGlassTier", BooleanSyncValue.class);

        IntSyncValue hatchTierSyncer = syncManager.findSyncHandler("hatchTier", IntSyncValue.class);
        IntSyncValue glassTierSyncer = syncManager.findSyncHandler("glassTier", IntSyncValue.class);
        IntSyncValue casingCountSyncer = syncManager.findSyncHandler("casingCount", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.DARK_RED
                        + StatCollector
                            .translateToLocalFormatted(
                                "GT5U.gui.text.large_fluid_extractor.not_enough_casings",
                                BASE_CASING_COUNT - MAX_HATCHES_ALLOWED,
                                casingCountSyncer.getIntValue())
                            .replace("\\n", "\n")
                        + EnumChatFormatting.RESET)
                    .asWidget()
                    .marginBottom(2)
                    .setEnabledIf(useless -> badCasingSyncer.getBoolValue()))
            .child(
                IKey.dynamic(
                    () -> String.format(
                        "%sEnergy hatch tier (%s) is too high\nfor the glass tier (%s).%s",
                        EnumChatFormatting.DARK_RED,
                        VN[GTUtility.clamp(hatchTierSyncer.getIntValue(), 0, VN.length - 1)],
                        VN[GTUtility.clamp(glassTierSyncer.getIntValue(), 0, VN.length - 1)],
                        RESET))
                    .asWidget()
                    .setEnabledIf(useless -> badGlassSyncer.getBoolValue()));

    }
}
