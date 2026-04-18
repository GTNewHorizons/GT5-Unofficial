package tectech.thing.metaTileEntity.hatch.bec;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.enums.Comparison;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTDataUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchNaniteDetector extends MTEHatchConfigurableBase {

    private int configuredTier;
    private @Nullable NaniteTier requiredTier;
    private Comparison comparison = Comparison.EQ;

    public MTEHatchNaniteDetector(int aID, String aName) {
        super(aID, aName, VoltageIndex.UEV, null);
    }

    protected MTEHatchNaniteDetector(MTEHatchNaniteDetector prototype) {
        super(prototype);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchNaniteDetector(this);
    }

    @Override
    protected void saveConfig(NBTTagCompound tag) {
        tag.setInteger("configuredTier", configuredTier);
        tag.setInteger("op", comparison.ordinal());
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound tag) {
        configuredTier = tag == null ? 0 : tag.getInteger("configuredTier");
        comparison = Comparison.values()[tag == null ? 0 : tag.getInteger("op")];
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "nanite-detector";
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_HATCH_NANITE_DETECTOR) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_HATCH_NANITE_DETECTOR_GLOW)
            .glow()
            .build() };
    }

    public void setRequiredTier(@Nullable NaniteTier requiredTier) {
        this.requiredTier = requiredTier;
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);

        setOutput(requiredTier != null && comparison.test(requiredTier.tier, configuredTier));
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new Gui().build(data, syncManager, uiSettings);
    }

    private class Gui extends MTEHatchBaseGui<MTEHatchNaniteDetector> {

        public Gui() {
            super(MTEHatchNaniteDetector.this);
        }

        @Override
        protected UITexture getLogoTexture() {
            return GTGuiTextures.PICTURE_TECTECH_LOGO;
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return super.createContentSection(panel, syncManager)
                .child(SettingsPanel.builder()
                    .setDividerPosition(60)
                    .addEnumCycleButton(
                        IKey.str("Operation"),
                        Comparison.class,
                        () -> comparison,
                        v -> comparison = v)
                    .addLongEditor(
                        IKey.str("Threshold"),
                        () -> configuredTier,
                        l -> configuredTier = (int) l,
                        (panel1, syncManager1, widget) -> {
                            widget.setNumbers(1, Arrays.stream(NaniteTier.values()).mapToInt(NaniteTier::getTier).max().getAsInt());
                        })
                    .addReadout(
                        IKey.str("Current:"),
                        new IntSyncValue(() -> requiredTier == null ? -1 : requiredTier.ordinal()),
                        nanite -> {
                            NaniteTier tier = GTDataUtils.getIndexSafe(NaniteTier.values(), nanite);

                            return IKey.str(tier == null ? "None" : tier.describe());
                        })
                    .build(panel, syncManager)
                    .widthRel(1)
                    .height(getContentRowHeight()));
            // spotless:on
        }
    }
}
