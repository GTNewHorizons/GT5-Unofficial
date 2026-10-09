package tectech.thing.metaTileEntity.hatch.bec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
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
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.DisableableTextFieldWidget;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
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
        return new Gui().build(data, syncManager, uiSettings);
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TECTECH_STANDARD;
    }

    private class Gui extends MTEHatchBaseGui<MTEHatchNaniteDetector> {

        public Gui() {
            super(MTEHatchNaniteDetector.this);
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return super.createContentSection(panel, syncManager)
                .child(SettingsPanel.builder()
                    .addEnumCycleButton(
                        IKey.lang("GT5U.gui.text.bec-operation"),
                        ComparisonWithAnalog.class,
                        () -> comparison,
                        v -> comparison = v)
                    .addIntEditor(
                        IKey.lang("GT5U.gui.text.bec-threshold"),
                        () -> configuredTier,
                        i -> {
                            if (comparison != ComparisonWithAnalog.ANALOG) {
                                configuredTier = i;
                            }
                        },
                        i -> Math.clamp(i, 1, Arrays.stream(NaniteTier.values()).mapToInt(NaniteTier::getTier).max().getAsInt()),
                        (_, _, textField) -> {
                            textField.setEditable(() -> comparison != ComparisonWithAnalog.ANALOG);
                        },
                        DisableableTextFieldWidget::new)
                    .addReadout(
                        IKey.lang("GT5U.gui.text.bec-current"),
                        new IntSyncValue(() -> requiredTier == null ? -1 : requiredTier.ordinal()),
                        nanite -> {
                            NaniteTier tier = GTDataUtils.getIndexSafe(NaniteTier.values(), nanite);

                            if (tier == null) {
                                return IKey.lang("GT5U.gui.text.nil");
                            } else {
                                return IKey.lang("GT5U.gui.text.nanite-detector-tier", tier.tier);
                            }
                        })
                    .build(panel, syncManager, getContentHolderHeight())
                    .horizontalCenter());
            // spotless:on
        }
    }
}
