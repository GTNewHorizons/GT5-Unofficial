package tectech.thing.metaTileEntity.hatch.bec;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.Comparison;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.thing.metaTileEntity.hatch.MTEHatchConfigurableBase;

public class MTEHatchCondensateDetector extends MTEHatchConfigurableBase {

    private Fluid condensateFilter;
    private long requestedAmount, actualAmount;
    private Comparison comparison = Comparison.EQ;

    public MTEHatchCondensateDetector(int aID, String aName) {
        super(aID, aName, VoltageIndex.UEV, null);
    }

    protected MTEHatchCondensateDetector(MTEHatchCondensateDetector prototype) {
        super(prototype);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchCondensateDetector(this);
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
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new Gui().build(data, syncManager, uiSettings);
    }

    private class Gui extends MTEHatchBaseGui<MTEHatchCondensateDetector> {

        public Gui() {
            super(MTEHatchCondensateDetector.this);
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
                    .addPhantomFluidSlot(IKey.str("Filter"), () -> condensateFilter, f -> condensateFilter = f, null)
                    .addEnumCycleButton(
                        IKey.str("Operation"),
                        Comparison.class,
                        () -> comparison,
                        v -> comparison = v)
                    .addLongEditor(
                        IKey.str("Threshold"),
                        () -> requestedAmount,
                        l -> requestedAmount = (int) l,
                        (panel1, syncManager1, widget) -> {
                            widget.setNumbersLong(() -> 1L, () -> Long.MAX_VALUE);
                        })
                    .addReadout(
                        IKey.str("Current:"),
                        new LongSyncValue(() -> actualAmount),
                        amount -> IKey.str(NumberFormatUtil.formatFluid(amount)))
                    .build(panel, syncManager)
                    .widthRel(1)
                    .height(getContentRowHeight()));
            // spotless:on
        }
    }
}
