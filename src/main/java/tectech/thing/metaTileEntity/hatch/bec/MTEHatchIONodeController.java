package tectech.thing.metaTileEntity.hatch.bec;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.EnumCycleButtonWidgetMUI1;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchIONodeController extends MTEHatchConfigurableBase {

    private Mode mode = Mode.PAUSE_INSTANT;

    public enum Mode {

        PAUSE_INSTANT,
        PAUSE_STEP;

        @Override
        public String toString() {
            return switch (this) {
                case PAUSE_INSTANT -> "Pause Immediately";
                case PAUSE_STEP -> "Pause Next Step";
            };
        }

        public String getTooltip() {
            return switch (this) {
                case PAUSE_INSTANT -> "The I/O Node will stop processing when it receives a redstone signal";
                case PAUSE_STEP -> "The I/O Node will stop upon transitioning into a new input step when it receives a redstone signal";
            };
        }
    }

    public MTEHatchIONodeController(int aID, String aName) {
        super(aID, aName, VoltageIndex.UEV, null);
    }

    protected MTEHatchIONodeController(MTEHatchIONodeController prototype) {
        super(prototype);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchIONodeController(this);
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

    public Mode getMode() {
        return mode;
    }

    public boolean receivingSignal() {
        return getBaseMetaTileEntity().getRedstone();
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

    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widgets(
            new FakeSyncWidget.IntegerSyncer(() -> mode.ordinal(), index -> mode = Mode.values()[index]),

            new TextWidget("Mode:").setDefaultColor(COLOR_TEXT_GRAY.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(10, 14),
            new EnumCycleButtonWidgetMUI1<>(Mode.class, () -> mode, value -> mode = value).setPos(40, 9)
                .setSize(100, 18));
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new Gui().build(data, syncManager, uiSettings);
    }

    private class Gui extends MTEHatchBaseGui<MTEHatchIONodeController> {

        public Gui() {
            super(MTEHatchIONodeController.this);
        }

        @Override
        protected UITexture getLogoTexture() {
            return GTGuiTextures.TT_PICTURE_TECTECH_LOGO;
        }

        @Override
        protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
            // spotless:off
            return super.createContentSection(panel, syncManager)
                .child(SettingsPanel.builder()
                    .setDividerPosition(40)
                    .addEnumCycleButton(
                        IKey.str("Mode"),
                        Mode.class,
                        () -> mode,
                        v -> mode = v,
                        (panel2, sync, button) -> {
                            button.tooltip((Mode mode) -> new RichTooltip().add(mode.getTooltip()));
                        })
                    .build(panel, syncManager)
                    .widthRel(1)
                    .height(getContentRowHeight()));
            // spotless:on
        }
    }
}
