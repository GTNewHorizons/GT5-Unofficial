package tectech.thing.metaTileEntity.hatch.bec;

import static gregtech.api.util.GTUtility.mulSafe;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.hatch.MTEHatchIONodeControllerGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchIONodeController extends MTEHatchConfigurableBase implements IConfigurationCircuitSupport {

    private Lazy<List<String>> tooltip = null;

    private Control control = Control.PAUSE;

    private PauseMode pauseMode = PauseMode.PAUSE_INSTANT;

    public enum Control {

        PAUSE,
        PARALLEL;

        @Override
        public String toString() {
            return switch (this) {
                case PAUSE -> StatCollector.translateToLocal("GT5U.gui.text.control-pause");
                case PARALLEL -> StatCollector.translateToLocal("GT5U.gui.text.control-parallel");
            };
        }

        public String getTooltip() {
            return switch (this) {
                case PAUSE -> StatCollector.translateToLocal("GT5U.gui.text.control-pause.tooltip");
                case PARALLEL -> StatCollector.translateToLocal("GT5U.gui.text.control-parallel.tooltip");
            };
        }
    }

    public enum PauseMode {

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
        super(aID, aName, VoltageIndex.UIV, 1, null);
    }

    protected MTEHatchIONodeController(MTEHatchIONodeController prototype) {
        super(prototype, 1);

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

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public PauseMode getPauseMode() {
        return pauseMode;
    }

    public void setPauseMode(PauseMode pauseMode) {
        this.pauseMode = pauseMode;
    }

    public boolean receivingSignal() {
        return getBaseMetaTileEntity().getRedstone();
    }

    public byte signalReceived() {
        return getBaseMetaTileEntity().getStrongestRedstone();
    }

    public int getParallelExponent() {
        ItemStack circuit = getStackInSlot(getCircuitSlot());
        return circuit != null ? circuit.getItemDamage() : 0;
    }

    public int getParallel() {
        return mulSafe(signalReceived(), (int) Math.pow(16, getParallelExponent()));
    }

    @Override
    public int getCircuitSlot() {
        return 0;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlotX() {
        return 0;
    }

    @Override
    public int getCircuitSlotY() {
        return 0;
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD.loadStandardPath(
                    new ResourceLocation(Mods.ModIDs.GREG_TECH, "bec/ionode-controller-hatch"),
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
        aNBT.setInteger("control", control.ordinal());
        aNBT.setInteger("mode", pauseMode.ordinal());
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound aNBT) {
        control = Control.values()[aNBT == null ? 0 : aNBT.getInteger("control")];
        pauseMode = PauseMode.values()[aNBT == null ? 0 : aNBT.getInteger("mode")];
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
