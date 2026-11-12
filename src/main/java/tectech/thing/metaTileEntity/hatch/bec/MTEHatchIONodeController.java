package tectech.thing.metaTileEntity.hatch.bec;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;

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
import gregtech.common.gui.modularui.hatch.MTEHatchIONodeControllerGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchIONodeController extends MTEHatchConfigurableBase {

    private Lazy<List<String>> tooltip = null;

    private Mode mode = Mode.PAUSE_INSTANT;

    public enum Mode {

        PAUSE_INSTANT,
        PAUSE_STEP;

        @Override
        public String toString() {
            return switch (this) {
                case PAUSE_INSTANT -> StatCollector.translateToLocal("GT5U.gui.text.pause-immediately");
                case PAUSE_STEP -> StatCollector.translateToLocal("GT5U.gui.text.pause-next-step");
            };
        }

        public String getTooltip() {
            return switch (this) {
                case PAUSE_INSTANT -> StatCollector.translateToLocal("GT5U.gui.text.pause-immediately.tooltip");
                case PAUSE_STEP -> StatCollector.translateToLocal("GT5U.gui.text.pause-next-step.tooltip");
            };
        }
    }

    public MTEHatchIONodeController(int aID, String aName) {
        super(aID, aName, VoltageIndex.UIV, null);
    }

    protected MTEHatchIONodeController(MTEHatchIONodeController prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchIONodeController(this);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_HATCH_IO_NODE_CONTROLLER) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_HATCH_IO_NODE_CONTROLLER_GLOW)
            .glow()
            .build() };
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean receivingSignal() {
        return getBaseMetaTileEntity().getRedstone();
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD.loadStandardPath(
                    new ResourceLocation(Mods.ModIDs.GREG_TECH, "io-node-controller-hatch"),
                    new HashMap<>()));
        }
        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "bec-ionode-controller";
    }

    @Override
    protected void saveConfig(NBTTagCompound aNBT) {
        aNBT.setInteger("mode", mode.ordinal());
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound aNBT) {
        mode = Mode.values()[aNBT == null ? 0 : aNBT.getInteger("mode")];
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchIONodeControllerGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TECTECH_STANDARD;
    }
}
