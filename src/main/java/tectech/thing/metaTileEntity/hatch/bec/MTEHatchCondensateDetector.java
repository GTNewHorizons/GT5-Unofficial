package tectech.thing.metaTileEntity.hatch.bec;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;

import gregtech.api.enums.Comparison;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.hatch.MTEHatchCondensateDetectorGui;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;
import tectech.thing.metaTileEntity.multi.bec.MTEBECStorage;

public class MTEHatchCondensateDetector extends MTEHatchConfigurableBase {

    private Fluid condensateFilter;
    private long requestedAmount, actualAmount;
    private Comparison comparison = Comparison.EQ;
    private MTEBECStorage becStorage;

    private Lazy<List<String>> tooltip = null;

    public MTEHatchCondensateDetector(int aID, String aName) {
        super(aID, aName, VoltageIndex.UIV, null);
    }

    protected MTEHatchCondensateDetector(MTEHatchCondensateDetector prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    public Fluid getCondensateFilter() {
        return condensateFilter;
    }

    public void setCondensateFilter(Fluid condensateFilter) {
        this.condensateFilter = condensateFilter;
    }

    public long getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(long requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public long getActualAmount() {
        return actualAmount;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchCondensateDetector(this);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_HATCH_CONDENSATE_DETECTOR) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_HATCH_CONDENSATE_DETECTOR_GLOW)
            .glow()
            .build() };
    }

    public void bindBECStorage(MTEBECStorage becStorage) {
        this.becStorage = becStorage;
    }

    public void refreshOutput() {
        if (this.becStorage != null) {
            if (this.becStorage.isValid()) {
                this.becStorage.refreshContentForHatch();
            } else {
                this.becStorage = null;
            }
        }
    }

    public void updateAmount(BECInventory inv) {
        long amount = 0;

        if (condensateFilter != null) {
            amount = inv.getContents()
                .getLong(condensateFilter);
        } else {
            for (var e : inv.getContents()
                .object2LongEntrySet()) {
                amount += e.getLongValue();
            }
        }

        actualAmount = amount;

        setOutput(comparison.test(actualAmount, requestedAmount));
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD.loadStandardPath(
                    new ResourceLocation(Mods.ModIDs.GREG_TECH, "condensate-detector-hatch"),
                    new HashMap<>()));
        }
        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "condensate-detector";
    }

    @Override
    protected void saveConfig(NBTTagCompound aNBT) {
        if (condensateFilter != null) {
            aNBT.setString("filter", FluidRegistry.getFluidName(condensateFilter));
        }

        aNBT.setInteger("comparison", comparison.ordinal());
        aNBT.setLong("requestedAmount", requestedAmount);
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound aNBT) {
        if (aNBT == null || !aNBT.hasKey("filter")) {
            condensateFilter = null;
        } else {
            condensateFilter = FluidRegistry.getFluid(aNBT.getString("filter"));
        }
        comparison = Comparison.values()[aNBT == null ? 0 : aNBT.getInteger("comparison")];
        requestedAmount = aNBT == null ? 0 : aNBT.getLong("requestedAmount");
        refreshOutput();
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchCondensateDetectorGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TECTECH_STANDARD;
    }

}
