package tectech.thing.metaTileEntity.hatch.bec;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;

import gregtech.api.enums.ComparisonWithAnalog;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.hatch.MTEHatchNaniteDetectorGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchNaniteDetector extends MTEHatchConfigurableBase {

    private int configuredTier;
    private @Nullable NaniteTier requiredTier;
    private ComparisonWithAnalog comparison = ComparisonWithAnalog.EQ;

    private Lazy<List<String>> tooltip = null;

    public MTEHatchNaniteDetector(int aID, String aName) {
        super(aID, aName, VoltageIndex.UIV, null);
    }

    protected MTEHatchNaniteDetector(MTEHatchNaniteDetector prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    public int getConfiguredTier() {
        return configuredTier;
    }

    public void setConfiguredTier(int configuredTier) {
        this.configuredTier = configuredTier;
    }

    public ComparisonWithAnalog getComparison() {
        return comparison;
    }

    public void setComparison(ComparisonWithAnalog comparison) {
        this.comparison = comparison;
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
        comparison = ComparisonWithAnalog.values()[tag == null ? 0 : tag.getInteger("op")];
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD.loadStandardPath(
                    new ResourceLocation(Mods.ModIDs.GREG_TECH, "nanite-detector-hatch"),
                    new HashMap<>()));
        }
        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
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

    @Nullable
    public NaniteTier getRequiredTier() {
        return requiredTier;
    }

    public void setRequiredTier(@Nullable NaniteTier requiredTier) {
        this.requiredTier = requiredTier;
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);

        if (comparison == ComparisonWithAnalog.ANALOG) {
            setOutput(requiredTier == null ? 0 : requiredTier.tier);
        } else {
            setOutput(requiredTier != null && comparison.test(requiredTier.tier, configuredTier));
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchNaniteDetectorGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TECTECH_STANDARD;
    }
}
