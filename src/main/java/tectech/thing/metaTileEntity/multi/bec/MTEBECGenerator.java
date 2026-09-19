package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.CoherencePreservingPlasmaConduit;
import static gregtech.api.casing.Casings.CondensateTransformativeCoil;
import static gregtech.api.casing.Casings.ConflictInducementCasing;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.PeaceEnforcementCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.util.item.AEFluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.client.volumetric.ISoundPosition;
import gregtech.client.volumetric.LinearSound;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

@IMetaTileEntity.SkipGenerateDescription
public class MTEBECGenerator extends MTEBECMultiblockBase<MTEBECGenerator> {

    public MTEBECGenerator(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEBECGenerator(MTEBECGenerator prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECGenerator(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_GENERATOR;
    }

    @Override
    public IStructureDefinition<MTEBECGenerator> compile(String[][] definition) {
        structure.addCasing('A', CoherencePreservingPlasmaConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing);
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ConflictInducementCasing);
        structure.addCasing('E', PeaceEnforcementCasing);
        structure.addCasing('F', CondensateTransformativeCoil);
        structure.addCasing('G', ElectromagneticWaveguide);
        structure.addCasing('1', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(InputBus, InputHatch, Energy, ExoticEnergy));
        structure.addCasing('2', FineStructureConstantManipulator)
            .withUnlimitedHatches(2, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECGenerator> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Generator, Condensate Entangler, Input Hatch")
            .addMarkdown(new ResourceLocation(Mods.ModIDs.GREG_TECH, "bec-generator"))
            .addSupportAny();

        tt.beginStructureBlock(19, 19, 34, true)
            .addController(StatCollector.translateToLocal("GT5U.tooltip.bec-generator.controller-pos"))
            .addCasing("236", ConflictInducementCasing.getLocalizedName(), false)
            .addCasing("232", FineStructureConstantManipulator.getLocalizedName(), false)
            .addCasing("216", CoherencePreservingPlasmaConduit.getLocalizedName(), false)
            .addCasing("184", PeaceEnforcementCasing.getLocalizedName(), false)
            .addCasing("136-158", ElectromagneticallyIsolatedCasing.getLocalizedName(), false)
            .addCasing("148", ElectromagneticWaveguide.getLocalizedName(), false)
            .addCasing("101", CondensateTransformativeCoil.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("GT5U.tooltip.bec-generator.hatch-pos"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("GT5U.tooltip.bec-generator.hatch-pos"), 1)
            .addMiscHatch(
                "1",
                "Bose-Einstein Condensate Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-generator.bec-hatch-pos"),
                2)
            .toolTipFinisher(GTAuthors.AuthorPineapple);
        return tt;
    }

    @Override
    public ITexture getCasingTexture() {
        return ElectromagneticallyIsolatedCasing.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_GENERATOR_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    // #endregion

    @Override
    protected boolean addFluidOutputs(FluidStack[] outputFluids) {
        if (network != null) {
            boolean succeed = true;
            for (FluidStack output : outputFluids) {
                AEFluidStack stack = AEFluidStack.create(output);
                network.injectCondensate(this, stack);
                if (stack.getStackSize() > 0) succeed = false;
            }
            return succeed;
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.condensateGeneratorRecipes;
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_BEC_GENERATOR;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected ISoundPosition getSoundPosition() {
        return new LinearSound(this, 0, 0, 2, 0, 0, 25, 32).setCentre(2, 1, 10);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        long maxPower = getMaxInputEu();

        Map<Fluid, Integer> combinedFluids = new HashMap<>();
        for (FluidStack input : getStoredFluids()) {
            if (input != null && input.amount > 0) {
                combinedFluids.merge(input.getFluid(), input.amount, Integer::sum);
            }
        }
        if (combinedFluids.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        List<FluidCandidate> fluidCandidates = new ArrayList<>();
        for (Map.Entry<Fluid, Integer> entry : combinedFluids.entrySet()) {
            Fluid fluid = entry.getKey();
            int totalAmount = entry.getValue();
            GTRecipe recipe = TecTechRecipeMaps.condensateGeneratorRecipes.findRecipeQuery()
                .fluids(new FluidStack(fluid, totalAmount))
                .find();
            if (recipe == null) continue;

            int maxParallelsByInput = (int) recipe.maxParallelCalculatedByInputs(
                Integer.MAX_VALUE,
                new FluidStack[] { new FluidStack(fluid, totalAmount) },
                GTValues.emptyItemStackArray);
            if (maxParallelsByInput <= 0) continue;

            fluidCandidates.add(new FluidCandidate(recipe, maxParallelsByInput, fluid, totalAmount));
        }
        if (fluidCandidates.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int maxDuration = fluidCandidates.stream()
            .mapToInt(c -> c.recipe.mDuration)
            .max()
            .orElse(0);
        if (maxDuration <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        for (FluidCandidate candidate : fluidCandidates) {
            candidate.adjustedEUt = (double) candidate.recipe.mEUt * candidate.recipe.mDuration / maxDuration;
        }

        int fluidCount = fluidCandidates.size();
        double remainingPower = maxPower;
        Map<Fluid, Integer> allocatedParallels = new HashMap<>();

        boolean[] canIncrease = new boolean[fluidCount];
        Arrays.fill(canIncrease, true);

        for (int i = 0; i < fluidCount; i++) {
            double powerShare = maxPower / fluidCount;
            FluidCandidate candidate = fluidCandidates.get(i);
            int parallels = calculateParallels(candidate, powerShare);
            allocatedParallels.put(candidate.fluid, parallels);
            remainingPower -= parallels * candidate.adjustedEUt;
            if (parallels >= candidate.maxParallelsByInput) {
                canIncrease[i] = false;
            }
        }

        int maxIterations = 10;
        for (int iter = 0; iter < maxIterations && remainingPower > 0.01; iter++) {
            List<Integer> increasableIndices = new ArrayList<>();
            for (int i = 0; i < fluidCount; i++) {
                if (canIncrease[i]) {
                    increasableIndices.add(i);
                }
            }
            if (increasableIndices.isEmpty()) break;

            double powerShare = remainingPower / increasableIndices.size();
            boolean anyChanged = false;
            for (int idx : increasableIndices) {
                FluidCandidate candidate = fluidCandidates.get(idx);
                int currentParallels = allocatedParallels.getOrDefault(candidate.fluid, 0);
                int additionalParallels = (int) Math.floor(powerShare / candidate.adjustedEUt);
                if (additionalParallels <= 0) continue;

                int newParallels = Math.min(candidate.maxParallelsByInput, currentParallels + additionalParallels);
                int delta = newParallels - currentParallels;
                if (delta > 0) {
                    allocatedParallels.put(candidate.fluid, newParallels);
                    remainingPower -= delta * candidate.adjustedEUt;
                    if (newParallels >= candidate.maxParallelsByInput) {
                        canIncrease[idx] = false;
                    }
                    anyChanged = true;
                }
            }
            if (!anyChanged) break;
        }

        CondensateList outputs = new CondensateList();
        mMaxProgresstime = maxDuration;
        boolean anySuccess = false;

        for (FluidCandidate candidate : fluidCandidates) {
            int parallels = allocatedParallels.getOrDefault(candidate.fluid, 0);
            if (parallels <= 0) continue;

            int drainAmount = parallels * candidate.recipe.mFluidInputs[0].amount;
            if (!depleteFluidAcrossInputs(candidate.fluid, drainAmount)) {
                continue;
            }

            outputs.addTo(
                candidate.recipe.mFluidOutputs[0].getFluid(),
                candidate.recipe.mFluidOutputs[0].amount * (long) parallels);
            anySuccess = true;
        }

        if (!anySuccess) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        mOutputFluids = outputs.toFluidStacks()
            .toArray(GTValues.emptyFluidStackArray);
        mEfficiency = 10_000;
        useLongPower = true;
        long actualPower = (long) (maxPower - remainingPower);
        lEUt = -actualPower;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int calculateParallels(FluidCandidate candidate, double availablePower) {
        int maxByPower = (int) Math.floor(availablePower / candidate.adjustedEUt);
        return Math.min(candidate.maxParallelsByInput, maxByPower);
    }

    private boolean depleteFluidAcrossInputs(Fluid fluid, int amount) {
        int remaining = amount;
        for (FluidStack slot : getStoredFluids()) {
            if (remaining <= 0) break;
            if (slot != null && slot.getFluid() == fluid && slot.amount > 0) {
                int drain = Math.min(slot.amount, remaining);
                FluidStack toDrain = new FluidStack(fluid, drain);
                if (depleteInput(toDrain)) {
                    remaining -= drain;
                } else {
                    return false;
                }
            }
        }
        return remaining == 0;
    }

    private static class FluidCandidate {

        final GTRecipe recipe;
        final int maxParallelsByInput;
        final Fluid fluid;
        final int totalAmount;
        double adjustedEUt;

        FluidCandidate(GTRecipe recipe, int maxParallelsByInput, Fluid fluid, int totalAmount) {
            this.recipe = recipe;
            this.maxParallelsByInput = maxParallelsByInput;
            this.fluid = fluid;
            this.totalAmount = totalAmount;
        }
    }
}
