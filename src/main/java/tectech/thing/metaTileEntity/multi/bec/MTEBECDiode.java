package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.CondensateGuidanceCoil;
import static gregtech.api.casing.Casings.CondensateTransformativeCoil;
import static gregtech.api.casing.Casings.ConflictInducementCasing;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.PeaceEnforcementCasing;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputHatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Mods;
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
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.thing.gui.bec.MTEBECDiodeGui;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchBEC;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.FluidParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

@IMetaTileEntity.SkipGenerateDescription
public class MTEBECDiode extends MTEBECMultiblockBase<MTEBECDiode> implements IParametrized {

    private MTEHatchBEC inputHatch;
    private boolean wasWorking;
    private FluidParameter[] filterParameters;

    public MTEBECDiode(int aID, String aName) {
        super(aID, aName);
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
            .withHatches(1, 16, Arrays.asList(InputHatch, Energy, ExoticEnergy));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ConflictInducementCasing);
        structure.addCasing('E', PeaceEnforcementCasing);
        structure.addCasing('F', CondensateTransformativeCoil);
        structure.addCasing('G', CondensateGuidanceCoil);
        structure.addCasing('H', ElectromagneticWaveguide);
        structure.addCasing('1', FineStructureConstantManipulator)
            .withHatches(2, 1, Arrays.asList(BEC_INPUT_HATCH));
        structure.addCasing('2', FineStructureConstantManipulator)
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && mMachine && !mInputHatches.isEmpty()) {
            deriveFiltersFromInputHatches();
        }
    }

    /*
     * Each fluid found in the input hatches is resolved to a condensate type, accepting either the type's source fluid
     * or the entangled fluid itself, and the entangled fluids of the resulting set are written into the filter slots in
     * ordinal order. Any remaining slots are cleared, so removing a fluid from a hatch drops its filter.
     */
    private void deriveFiltersFromInputHatches() {
        EnumSet<CondensateType> present = EnumSet.noneOf(CondensateType.class);

        for (FluidStack stack : getStoredFluids()) {
            Fluid fluid = stack.getFluid();
            CondensateType type = CondensateType.getCondensateTypeBySource(fluid);
            if (type == null) type = CondensateType.getCondensateType(fluid);
            if (type != null) present.add(type);
        }

        int slot = 0;

        for (CondensateType type : present) {
            Fluid entangled = type.getEntangledFluid();
            if (filterParameters[slot].getValue() != entangled) filterParameters[slot].setValue(entangled);
            slot++;
        }

        for (; slot < filterParameters.length; slot++) {
            if (filterParameters[slot].getValue() != null) filterParameters[slot].setValue(null);
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        if (wasWorking) {
            wasWorking = false;
            network.routeTracker.onElementRemoved(this);
            network.invalidateRoutes();
        }
    }

    @Override
    public boolean allowsCondensateThrough(Fluid condensate) {
        boolean hasFilter = false;

        for (FluidParameter filter : filterParameters) {
            Fluid value = filter.getValue();
            if (value == null) continue;
            if (value == condensate) return true;
            hasFilter = true;
        }

        return !hasFilter;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECDiode> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Diode, Storage Bus")
            .addMarkdown(new ResourceLocation(Mods.ModIDs.GREG_TECH, "bec-diode"))
            .addSupportAny();

        tt.beginStructureBlock(17, 11, 11, true)
            .addController(StatCollector.translateToLocal("GT5U.tooltip.bec-diode.controller-pos"))
            .addCasing("148", SuperconductivePlasmaEnergyConduit.getLocalizedName(), false)
            .addCasing("92", ConflictInducementCasing.getLocalizedName(), false)
            .addCasing("83-90", ElectromagneticallyIsolatedCasing.getLocalizedName(), false)
            .addCasing("84", FineStructureConstantManipulator.getLocalizedName(), false)
            .addCasing("70", ElectromagneticWaveguide.getLocalizedName(), false)
            .addCasing("68", PeaceEnforcementCasing.getLocalizedName(), false)
            .addCasing("40", CondensateTransformativeCoil.getLocalizedName(), false)
            .addCasing("24", CondensateGuidanceCoil.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("GT5U.tooltip.bec-diode.hatch-pos"), 1)
            .addInputHatch("0+", StatCollector.translateToLocal("GT5U.tooltip.bec-diode.hatch-pos"), 1)
            .addMiscHatch(
                "2",
                "Bose-Einstein Condensate Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-diode.bec-hatch-pos"),
                2,
                3)
            .toolTipFinisher(GTAuthors.AuthorPineapple);
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
        return filterParameters[0].getValue();
    }

    @OCMethod
    public void setCondensateFilter(Fluid condensateFilter) {
        filterParameters[0].setValue(condensateFilter);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEBECDiodeGui(this);
    }

    private static final IHatchElement<MTEBECMultiblockBase<?>> BEC_INPUT_HATCH = new GTStructureUtility.ProxyHatchElement<>(
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

    @Override
    public void initParameters() {
        filterParameters = new FluidParameter[CondensateType.VALUES.length];

        for (int i = 0; i < filterParameters.length; i++) {
            filterParameters[i] = new FluidParameter(null, "GT5U.gui.text.bec-filter-n", "filter" + i, i + 1);
        }
    }

    @Override
    public void loadLegacyParameters(NBTTagCompound nbt) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        // Legacy saves stored a single filter under "condensate"
        NBTTagCompound parameterTag = aNBT.getCompoundTag("parameters");
        if (parameterTag.hasKey("condensate")) {
            filterParameters[0].setValue(FluidRegistry.getFluid(parameterTag.getString("condensate")));
        }
    }

    @Override
    public List<Parameter<?, ?>> getParameters() {
        return List.<Parameter<?, ?>>of(filterParameters);
    }
}
