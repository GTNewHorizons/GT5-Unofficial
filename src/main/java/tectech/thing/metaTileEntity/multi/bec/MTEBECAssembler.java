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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.api.storage.data.IAEFluidStack;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchNanite;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.gui.bec.MTEBECAssemblerGui;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchLoS;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECAssembler extends MTEBECMultiblockBase<MTEBECAssembler> {

    private final List<MTEHatchLoS> losHatches = new ArrayList<>();

    private final List<MTEHatchNanite> naniteHatches = new ArrayList<>();

    private boolean nanitesDirty = false;
    private NaniteTier currentNaniteTier;
    private int availableNanites;

    public MTEBECAssembler(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEBECAssembler(MTEBECAssembler prototype) {
        super(prototype);
    }

    public NaniteTier getCurrentNaniteTier() {
        return currentNaniteTier;
    }

    public void setCurrentNaniteTier(NaniteTier currentNaniteTier) {
        this.currentNaniteTier = currentNaniteTier;
    }

    public int getAvailableNanites() {
        return availableNanites;
    }

    public void setAvailableNanites(int availableNanites) {
        this.availableNanites = availableNanites;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECAssembler(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_ASSEMBLER;
    }

    @Override
    public IStructureDefinition<MTEBECAssembler> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, NaniteHatchElement.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ConflictInducementCasing);
        structure.addCasing('E', PeaceEnforcementCasing);
        structure.addCasing('F', CondensateTransformativeCoil);
        structure.addCasing('G', CondensateGuidanceCoil);
        structure.addCasing('H', ElectromagneticWaveguide);
        structure.addCasing('1', FineStructureConstantManipulator)
            .withHatches(2, 2, Arrays.asList(BECHatches.Hatch));
        structure.addCasing('2', FineStructureConstantManipulator)
            .withHatches(3, 16, Arrays.asList(AssemblerLineOfSightHatch.INSTANCE));

        return structure.buildStructure(definition);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        for (MTEHatchLoS hatch : this.losHatches) {
            hatch.setOwner(null);
        }
        this.losHatches.clear();
        this.naniteHatches.clear();
    }

    @Override
    protected void onStructureCheckFinished(IGregTechTileEntity igte) {
        super.onStructureCheckFinished(igte);

        this.nanitesDirty = true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECAssembler> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Assembler, Observation Array")
            .addMarkdown(new ResourceLocation("gregtech", "bec-assembler"))
            .addSupportAny();

        tt.beginStructureBlock(31, 61, 31, true)
            .addController(StatCollector.translateToLocal("GT5U.tooltip.bec-assembler.controller-pos"))
            .addCasing("1700", FineStructureConstantManipulator.getLocalizedName(), false)
            .addCasing("1515", SuperconductivePlasmaEnergyConduit.getLocalizedName(), false)
            .addCasing("0-1458", ElectromagneticallyIsolatedCasing.getLocalizedName(), false)
            .addCasing("838", ConflictInducementCasing.getLocalizedName(), false)
            .addCasing("790", PeaceEnforcementCasing.getLocalizedName(), false)
            .addCasing("664", ElectromagneticWaveguide.getLocalizedName(), false)
            .addCasing("560", CondensateTransformativeCoil.getLocalizedName(), false)
            .addCasing("464", CondensateGuidanceCoil.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("GT5U.tooltip.bec-assembler.hatch-pos"), 1)
            .addMiscHatch(
                "1+",
                "Nanite Containment Bus",
                StatCollector.translateToLocal("GT5U.tooltip.bec-assembler.hatch-pos"),
                1)
            .addMiscHatch(
                "1+",
                "Line-of-Sight Connector Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-assembler.los-hatch-pos"),
                3)
            .addMiscHatch(
                "1-2",
                "Bose-Einstein Condensate Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-assembler.bec-hatch-pos"),
                2)
            .toolTipFinisher(GTAuthors.AuthorPineapple);
        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return SuperconductivePlasmaEnergyConduit.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_ASSEMBLER_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.condensateAssemblingRecipes;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEBECAssemblerGui(this);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        useLongPower = true;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public List<MTEBECIONode> getIONodes() {
        return losHatches.stream()
            .map(MTEHatchLoS::getConnectedHatch)
            .filter(Objects::nonNull)
            .map(los -> los.getOwner() instanceof MTEBECIONode node ? node : null)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (GTUtility.isServer()) {
            for (MTEHatchNanite hatch : naniteHatches) {
                if (hatch.hasChanged()) {
                    this.nanitesDirty = true;
                    hatch.unmarkChanged();
                }
            }

            List<MTEBECIONode> nodes = getIONodes();

            if (this.nanitesDirty) {
                this.nanitesDirty = false;
                this.currentNaniteTier = null;
                this.availableNanites = 0;

                for (MTEHatchNanite hatch : this.naniteHatches) {
                    NaniteTier tier = NaniteTier.fromStack(hatch.getItemStack());

                    if (tier == null) continue;

                    if (this.currentNaniteTier == null || tier.ordinal() < this.currentNaniteTier.ordinal()) {
                        this.currentNaniteTier = tier;
                    }

                    this.availableNanites += hatch.getItemCount();
                }

                for (var node : nodes) {
                    // Intentionally share the same nanite count between every io node even though it doesn't make
                    // physical sense, so that proper automation is incentivized even more.
                    node.setNaniteShare(this.currentNaniteTier, this.availableNanites);
                }

                igte.setActive(!nodes.isEmpty());
            }

            lEUt = 0;

            long euInput = getMaxInputEu();

            for (var node : nodes) {
                node.setPowered(false);

                long request = node.getAssemblerEUt();

                if (euInput >= request) {
                    lEUt -= request;
                    euInput -= request;
                    node.setPowered(true);
                }
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        if (reason.wasCritical()) {
            // you really don't want to powerfail :tootroll:
            for (MTEBECIONode node : getIONodes()) {
                if (node.isPowered()) {
                    node.stopMachine(reason);
                }
            }
        }
    }

    public void drainCondensate(IAEFluidStack stack) {
        if (network == null) return;

        network.drainCondensate(this, stack);
    }

    public int getSlowdowns(Collection<Fluid> validMaterials) {
        return network == null ? 0 : network.getSlowdowns(this, validMaterials);
    }

    private enum NaniteHatchElement implements IHatchElement<MTEBECAssembler> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Arrays.asList(MTEHatchNanite.class);
        }

        @Override
        public IGTHatchAdder<? super MTEBECAssembler> adder() {
            return NaniteHatchElement::adder;
        }

        private static boolean adder(MTEBECAssembler assembler, IGregTechTileEntity igte, Short texture) {
            if (igte.getMetaTileEntity() instanceof MTEHatchNanite naniteHatch) {
                assembler.naniteHatches.add(naniteHatch);
                naniteHatch.updateTexture(texture);
                naniteHatch.updateCraftingIcon(assembler.getMachineCraftingIcon());

                return true;
            }

            return false;
        }

        @Override
        public String getDisplayName() {
            return ItemList.Hatch_Nanite.getDisplayName();
        }

        @Override
        public long count(MTEBECAssembler assembler) {
            return assembler.naniteHatches.size();
        }
    }

    public enum AssemblerLineOfSightHatch implements IHatchElement<MTEBECAssembler> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return List.of(MTEHatchLoS.class);
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_LineOfSight_Connector.getDisplayName();
        }

        @Override
        public long count(MTEBECAssembler self) {
            return self.losHatches.size();
        }

        @Override
        public IGTHatchAdder<MTEBECAssembler> adder() {
            return (self, igtme, id) -> {
                IMetaTileEntity imte = igtme.getMetaTileEntity();

                if (imte instanceof MTEHatchLoS hatch) {
                    hatch.updateTexture(id);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());
                    hatch.setOwner(self);

                    self.losHatches.add(hatch);

                    return true;
                } else {
                    return false;
                }
            };
        }
    }
}
