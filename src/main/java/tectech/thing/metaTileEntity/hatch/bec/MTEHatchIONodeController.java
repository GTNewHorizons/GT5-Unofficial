package tectech.thing.metaTileEntity.hatch.bec;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchIONodeController extends MTEHatchConfigurableBase implements IConfigurationCircuitSupport {

    private static final int PHYSICAL_CIRCUIT_SLOT = 0;
    private static final int GHOST_CIRCUIT_SLOT = 1;

    private Lazy<List<String>> tooltip = null;

    private Control control = Control.PAUSE;

    private PauseMode pauseMode = PauseMode.PAUSE_INSTANT;
    private byte baseParallel = 0;

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
        super(aID, aName, VoltageIndex.UIV, 2, null);
    }

    protected MTEHatchIONodeController(MTEHatchIONodeController prototype) {
        super(prototype, 2);

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

    public PauseMode getPauseMode() {
        return pauseMode;
    }

    public boolean receivingSignal() {
        return getBaseMetaTileEntity().getRedstone();
    }

    public byte signalReceived() {
        return getBaseMetaTileEntity().getStrongestRedstone();
    }

    public int getParallel() {
        return baseParallel * (1 + signalReceived());
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
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isValidSlot(int index) {
        return index == PHYSICAL_CIRCUIT_SLOT;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return index == PHYSICAL_CIRCUIT_SLOT && GTUtility.isAnyIntegratedCircuit(itemStack);
    }

    @Override
    public int getCircuitSlot() {
        return GHOST_CIRCUIT_SLOT;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlotX() {
        return 153;
    }

    @Override
    public int getCircuitSlotY() {
        return 63;
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);

        if (baseMetaTileEntity.hasInventoryBeenModified()) {
            ItemStack circuit = getStackInSlot(PHYSICAL_CIRCUIT_SLOT);

            if (circuit == null) {
                circuit = getStackInSlot(GHOST_CIRCUIT_SLOT);
            }

            if (circuit != null) {
                baseParallel = (byte) circuit.getItemDamage();
            } else {
                baseParallel = 0;
            }
        }
    }

    @Override
    protected void saveConfig(NBTTagCompound aNBT) {
        aNBT.setInteger("control", control.ordinal());
        aNBT.setInteger("mode", pauseMode.ordinal());
        aNBT.setByte("baseParallel", baseParallel);
    }

    @Override
    protected void loadConfig(@Nullable NBTTagCompound aNBT) {
        control = Control.values()[aNBT == null ? 0 : aNBT.getInteger("control")];
        pauseMode = PauseMode.values()[aNBT == null ? 0 : aNBT.getInteger("mode")];
        baseParallel = aNBT == null ? 0 : aNBT.getByte("baseParallel");
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

    private class Gui extends MTEHatchBaseGui<MTEHatchIONodeController> {

        public Gui() {
            super(MTEHatchIONodeController.this);
        }

        protected EnumSyncValue<Control, ?> getControlSync(PanelSyncManager syncManager) {
            return syncManager.getOrCreateSyncHandler(
                "control",
                EnumSyncValue.class,
                () -> new EnumSyncValue<>(Control.class, () -> control, value -> control = value).allowC2S());
        }

        @Override
        protected int getBasePanelHeight() {
            return super.getBasePanelHeight() + SLOT_SIZE / 2;
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            var controlSync = getControlSync(syncManager);

            Flow tabButtons = Flow.row()
                .coverChildren()
                .childPadding(2)
                .horizontalCenter()
                .child(
                    new SelectButton().value(LinkedBoolValue.of(controlSync, Control.PAUSE))
                        .size(78, 18)
                        .overlay(IKey.lang("GT5U.gui.text.control-pause"))
                        .tooltip(new RichTooltip().add(Control.PAUSE.getTooltip())))
                .child(
                    new SelectButton().value(LinkedBoolValue.of(controlSync, Control.PARALLEL))
                        .size(78, 18)
                        .overlay(IKey.lang("GT5U.gui.text.control-parallel"))
                        .tooltip(new RichTooltip().add(Control.PARALLEL.getTooltip())));

            Flow pages = Flow.column()
                .fullWidth()
                .coverChildrenHeight()
                .childPadding(4)
                .collapseDisabledChild()
                .child(tabButtons)
                .child(
                    createPauseControlPage(panel, syncManager)
                        .setEnabledIf(widget -> controlSync.getValue() == Control.PAUSE))
                .child(
                    createParallelControlPage(panel, syncManager)
                        .setEnabledIf(widget -> controlSync.getValue() == Control.PARALLEL));

            return super.createContentSection(panel, syncManager)
                .child(pages);
            // spotless:on
        }

        private ParentWidget<?> createPauseControlPage(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return new ParentWidget<>().fullWidth()
                .child(SettingsPanel.builder()
                    .addEnumCycleButton(
                        IKey.lang("GT5U.gui.text.bec-mode"),
                        PauseMode.class,
                        () -> pauseMode,
                        value -> pauseMode = value,
                        (_, _, button) -> button.tooltip(
                            (PauseMode pauseMode) -> new RichTooltip().add(pauseMode.getTooltip())))
                    .build(panel, syncManager, getContentHolderHeight())
                    .horizontalCenter());
            // spotless:on
        }

        private ParentWidget<?> createParallelControlPage(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return new ParentWidget<>().fullWidth()
                .child(SettingsPanel.builder()
                    .addReadout(
                        IKey.lang("GT5U.gui.text.control-parallel.base"),
                        new ByteSyncValue(() -> baseParallel),
                        amount -> IKey.str(NumberFormatUtil.formatNumber(amount)))
                    .addReadout(
                        IKey.lang("GT5U.gui.text.control-parallel.multiplier"),
                        new IntSyncValue(() -> 1 + signalReceived()),
                        amount -> IKey.lang("GT5U.gui.text.control-parallel.multiplier.value", amount))
                    .addReadout(
                        IKey.lang("GT5U.gui.text.control-parallel.parallel"),
                        new IntSyncValue(MTEHatchIONodeController.this::getParallel),
                        amount -> IKey.str(NumberFormatUtil.formatNumber(amount))
                    )
                    .build(panel, syncManager, getContentHolderHeight())
                    .horizontalCenter());
            // spotless:on
        }

        @Override
        protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return super.createBottomLeftCornerFlow(panel, syncManager)
                .child(
                    new ItemSlot()
                        .slot(
                            new ModularSlot(MTEHatchIONodeController.this.inventoryHandler, PHYSICAL_CIRCUIT_SLOT))
                        .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CIRCUIT)
                        .setEnabledIf(widget -> getControlSync(syncManager).getValue() == Control.PARALLEL)
                );
            // spotless:on
        }

        @Override
        protected Widget<?> createCircuitSlot(PanelSyncManager syncManager) {
            return super.createCircuitSlot(syncManager)
                .setEnabledIf(widget -> getControlSync(syncManager).getValue() == Control.PARALLEL);
        }
    }
}
