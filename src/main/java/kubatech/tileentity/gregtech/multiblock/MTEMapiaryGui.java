package kubatech.tileentity.gregtech.multiblock;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import gregtech.api.modularui2.GTGuiTextures;
import kubatech.tileentity.gregtech.multiblock.modularui2.EasyIntSyncValue;
import net.minecraft.util.StatCollector;

public class MTEMapiaryGui extends MTEKubaExtraInventoryGui<MTEMegaIndustrialApiary> {

    public final EasyIntSyncValue mPrimaryMode = new EasyIntSyncValue("mPrimaryMode", multiblock::getPrimaryMode, multiblock::setPrimaryMode);
    public final EasyIntSyncValue mSecondaryMode = new EasyIntSyncValue("mSecondaryMode", multiblock::getSecondaryMode, multiblock::setSecondaryMode);
    public MTEMapiaryGui(MTEMegaIndustrialApiary multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        mPrimaryMode.registerSyncValue(syncManager);
        mSecondaryMode.registerSyncValue(syncManager);
    }
    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager)
            .child(new PrimaryModeButton(syncManager))
            .child(new SecondaryModeButton(syncManager));
    }

    private class PrimaryModeButton extends KubaCycleButtonWidget {
        protected PrimaryModeButton(PanelSyncManager syncManager) {
            super();
            this.baseDynamicOverlay(() -> primaryModeIcon[mPrimaryMode.getValue()])
                .size(18, 18)
                .syncHandler(mPrimaryMode.name)
                .length(primaryModeIcon.length)
                .tooltipBuilder(this::createPrimaryModeSwitchTooltip);
        }
        private static final UITexture[] primaryModeIcon = {
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH
        };
        private static final String[] primaryModeDesc = {
            "kubatech.infodata.mia.running_mode.input",
            "kubatech.infodata.mia.running_mode.output",
            "kubatech.infodata.mia.running_mode.operating"
        };
        private void createPrimaryModeSwitchTooltip(RichTooltip t){
            t.addLine(getTranslationMode())
                .addLine(IKey.dynamic(() -> StatCollector.translateToLocal(primaryModeDesc[multiblock.mPrimaryMode])));
        }
    }
    private class SecondaryModeButton extends KubaCycleButtonWidget {
        protected SecondaryModeButton(PanelSyncManager syncManager) {
            super();
            this.baseDynamicOverlay(() -> secondaryModeIcon[mSecondaryMode.getValue()])
                .size(18, 18)
                .syncHandler(mSecondaryMode.name)
                .length(secondaryModeIcon.length)
                .tooltipBuilder(this::createSecondaryModeSwitchTooltip);
        }
        private static final UITexture[] secondaryModeIcon = {
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR
        };

        private final String[] secondaryModeDesc = {
            "kubatech.infodata.mia.running_mode.operating.normal",
            "kubatech.infodata.mia.running_mode.operating.swarmer"
        };
        private void createSecondaryModeSwitchTooltip(RichTooltip t){
            t.addLine(getTranslationMode())
                .addLine(IKey.dynamic(() -> StatCollector.translateToLocal(secondaryModeDesc[multiblock.mSecondaryMode])));
        }
    }
}
