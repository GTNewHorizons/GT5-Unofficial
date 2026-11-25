package gregtech.common.hydrodam;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Comparison;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

/// Emits a redstone signal from its front face based on the litres stored in an adjacent dam. Placed next to dam water;
/// if several volumes touch it, the first one found is used.
public class MTEHatchDamWaterLevelSensor extends MTEHatchConfigurableBase {

    /// How often the dam is polled, in ticks.
    private static final int POLL_INTERVAL = 20;

    /// Stands in for {@link #litres} while no dam water is adjacent.
    public static final long NO_DAM = -1;

    private long threshold;
    private Comparison comparison = Comparison.GTEQ;

    /// The litres in the adjacent dam at the last poll, or {@link #NO_DAM}.
    private long litres = NO_DAM;

    private Lazy<List<String>> tooltip = null;

    public MTEHatchDamWaterLevelSensor(int id, String name) {
        super(id, name, VoltageIndex.LV, null);
    }

    protected MTEHatchDamWaterLevelSensor(MTEHatchDamWaterLevelSensor prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    /// The litres in the adjacent dam at the last poll, or {@link #NO_DAM}.
    public long getLitres() {
        return litres;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchDamWaterLevelSensor(this);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);

        if (baseMetaTileEntity.isServerSide() && tick % POLL_INTERVAL == 0) poll();
    }

    /// Reads the litres of the first dam volume next to this sensor, and updates the output.
    private void poll() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        DamVolumes volumes = DamVolumes.get(base.getWorld());

        litres = NO_DAM;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            WaterVolume volume = volumes.getVolumeAt(
                base.getXCoord() + dir.offsetX,
                base.getYCoord() + dir.offsetY,
                base.getZCoord() + dir.offsetZ);

            if (volume != null) {
                litres = (long) volume.getLitres();
                break;
            }
        }

        refreshOutput();
    }

    public void refreshOutput() {
        setOutput(litres != NO_DAM && comparison.test(litres, threshold));
    }

    @Override
    protected void saveConfig(NBTTagCompound tag) {
        tag.setInteger("comparison", comparison.ordinal());
        tag.setLong("threshold", threshold);
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound tag) {
        comparison = tag == null || !tag.hasKey("comparison") ? Comparison.GTEQ
            : Comparison.values()[tag.getInteger("comparison")];
        threshold = tag == null ? 0 : tag.getLong("threshold");
        refreshOutput();
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "dam-water-level-sensor";
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD.loadStandardPath(
                    new ResourceLocation(Mods.ModIDs.GREG_TECH, "dam-water-level-sensor"),
                    new HashMap<>()));
        }

        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.SolidSteelMachineCasing.getCasingTexture();
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDamWaterLevelSensorGui(this).build(data, syncManager, uiSettings);
    }
}
