package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.CyclotronCoil;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.OCMethod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchBEC;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECDiode extends MTEBECMultiblockBase<MTEBECDiode> {

    private MTEHatchBEC inputHatch;
    private boolean wasWorking;
    private Fluid condensateFilter;

    public MTEBECDiode(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBECDiode(MTEBECDiode prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECDiode(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_DIODE;
    }

    @Override
    public IStructureDefinition<MTEBECDiode> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 8, Arrays.asList(Energy, ExoticEnergy));
        structure.addCasing('C', CyclotronCoil);
        structure.addCasing('D', AdvancedFusionCoilII);
        structure.addCasing('1', ElectromagneticallyIsolatedCasing)
            .withHatches(2, 1, Arrays.asList(INPUT_HATCH));
        structure.addCasing('2', ElectromagneticallyIsolatedCasing)
            .withHatches(3, 1, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    protected ITexture getCasingTexture() {
        return ElectromagneticallyIsolatedCasing.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_MAXWELL_GATE_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    protected List<? extends BECFactoryElement> getRoutedDiscoverySeeds() {
        if (inputHatch == null) return Collections.emptyList();
        if (mMaxProgresstime == 0) return Collections.emptyList();

        return Arrays.asList(inputHatch);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        inputHatch = null;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        if (wasWorking) {
            wasWorking = false;
            network.routeTracker.onElementAdded(this);
            network.invalidateRoutes();
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        condensateFilter = aNBT.hasKey("condensateFilter") ? FluidRegistry.getFluid(aNBT.getString("condensateFilter"))
            : null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        if (condensateFilter != null) {
            aNBT.setString("condensateFilter", FluidRegistry.getFluidName(condensateFilter));
        }
    }

    @Override
    public boolean allowsCondensateThrough(Fluid condensate) {
        return condensateFilter == null || condensate == condensateFilter;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECDiode> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Diode, Storage Bus")
            .addMarkdown(new ResourceLocation("gregtech", "bec-diode"));

        tt.beginStructureBlock();
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.becConnectorHatch.get(1));
        tt.addAllCasingInfo();

        tt.toolTipFinisher(GTAuthors.AuthorPineapple);

        return tt;
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        useLongPower = true;
        lEUt = -TierEU.RECIPE_UIV;
        mMaxProgresstime = 1;
        mEfficiency = 10000;

        if (!wasWorking) {
            wasWorking = true;
            network.routeTracker.onElementAdded(this);
            network.invalidateRoutes();
        }

        return SimpleCheckRecipeResult.ofSuccess("routing");
    }

    @OCMethod
    public Fluid getCondensateFilter() {
        return condensateFilter;
    }

    @OCMethod
    public void setCondensateFilter(Fluid condensateFilter) {
        this.condensateFilter = condensateFilter;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new Gui();
    }

    private static final IHatchElement<MTEBECMultiblockBase<?>> INPUT_HATCH = new GTStructureUtility.ProxyHatchElement<>(
        BECHatches.Hatch) {

        @Override
        public IGTHatchAdder<? super MTEBECMultiblockBase<?>> adder() {
            var adder = super.adder();

            return (multi, hatch, textureId) -> {
                boolean success = adder.apply(multi, hatch, textureId);

                if (success) {
                    ((MTEBECDiode) multi).inputHatch = (MTEHatchBEC) hatch.getMetaTileEntity();
                }

                return success;
            };
        }
    };

    private class Gui extends TTMultiblockBaseGui<MTEBECDiode> {

        public Gui() {
            super(MTEBECDiode.this);
        }

        @Override
        protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
            GenericSyncValue<CondensateList> contents = GenericSyncValue.builder(CondensateList.class)
                .getter(() -> network == null ? new CondensateList() : network.getStoredCondensate(MTEBECDiode.this))
                .adapter(new CondensateListAdapter())
                .build();

            syncManager.syncValue("contents", contents);

            TextWidget<?> contentsWidget = IKey.dynamic(() -> {
                StringBuilder ret = new StringBuilder();

                ret.append(EnumChatFormatting.GRAY)
                    .append("Available Condensate:\n");

                if (contents.getValue()
                    .isEmpty()) {
                    ret.append(EnumChatFormatting.GRAY)
                        .append("None");
                }

                for (var e : contents.getValue()
                    .object2LongEntrySet()) {
                    ret.append("  ")
                        .append(EnumChatFormatting.AQUA)
                        .append(CondensateType.getCondensateName(e.getKey()))
                        .append(EnumChatFormatting.GRAY)
                        .append(" x ")
                        .append(EnumChatFormatting.GOLD)
                        .append(NumberFormatUtil.formatFluid(e.getLongValue()))
                        .append(EnumChatFormatting.GRAY)
                        .append('\n');
                }

                return ret.toString();
            })
                .asWidget()
                .widthRel(1);

            return super.createTerminalTextWidget(syncManager, parent).child(contentsWidget);
        }

        @Override
        protected boolean isParametrized() {
            return true;
        }

        @Override
        protected Widget<?> getParameterEditor(ModularPanel panel, PanelSyncManager syncManager) {
            return SettingsPanel.builder()
                .setDividerPosition(35)
                .addHeader(IKey.str("Parameters"))
                .addPhantomFluidSlot(IKey.str("Filter"), () -> condensateFilter, f -> condensateFilter = f)
                .build(panel, syncManager)
                .size(100, 50);
        }
    }
}
