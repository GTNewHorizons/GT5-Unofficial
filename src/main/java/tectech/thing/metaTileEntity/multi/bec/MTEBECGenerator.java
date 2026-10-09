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
import java.util.Comparator;
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

        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.bec_generator"))
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

        Map<Fluid, Long> combinedFluids = new HashMap<>();
        for (FluidStack input : getStoredFluids()) {
            if (input != null && input.amount > 0) {
                combinedFluids.merge(input.getFluid(), (long) input.amount, Long::sum);
            }
        }
        if (combinedFluids.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        List<FluidCandidate> fluidCandidates = new ArrayList<>();
        for (Map.Entry<Fluid, Long> entry : combinedFluids.entrySet()) {
            Fluid fluid = entry.getKey();
            long totalAmountLong = entry.getValue();

            GTRecipe recipe = TecTechRecipeMaps.condensateGeneratorRecipes.findRecipeQuery()
                .fluids(new FluidStack(fluid, (int) Math.min(totalAmountLong, Integer.MAX_VALUE)))
                .find();
            if (recipe == null) continue;

            if (recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0) continue;
            int perParallel = recipe.mFluidInputs[0].amount;
            if (perParallel <= 0) continue;

            double recipeAccepts = recipe.maxParallelCalculatedByInputs(
                1,
                new FluidStack[] { new FluidStack(fluid, perParallel) },
                GTValues.emptyItemStackArray);
            if (recipeAccepts <= 0) continue;

            long maxParallelsByInput = totalAmountLong / perParallel;
            if (maxParallelsByInput > Integer.MAX_VALUE) {
                maxParallelsByInput = Integer.MAX_VALUE;
            }

            fluidCandidates.add(new FluidCandidate(recipe, (int) maxParallelsByInput, fluid));
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

        double remainingPower = maxPower;
        fluidCandidates.sort(Comparator.comparingDouble(c -> c.maxParallelsByInput * (double) c.recipe.mEUt));

        for (int i = 0; i < fluidCandidates.size(); i++) {
            FluidCandidate c = fluidCandidates.get(i);
            double share = remainingPower / (fluidCandidates.size() - i);
            c.parallels = (int) Math.min(c.maxParallelsByInput, share / c.recipe.mEUt);
            remainingPower -= c.parallels * (double) c.recipe.mEUt;
        }

        for (FluidCandidate c : fluidCandidates) {
            int added = (int) Math.min(c.maxParallelsByInput - c.parallels, remainingPower / c.recipe.mEUt);
            if (added > 0) {
                c.parallels += added;
                remainingPower -= added * (double) c.recipe.mEUt;
            }
        }

        CondensateList outputs = new CondensateList();
        boolean anySuccess = false;

        for (FluidCandidate candidate : fluidCandidates) {
            int plannedParallels = candidate.parallels;
            if (plannedParallels <= 0) continue;

            int perParallel = candidate.recipe.mFluidInputs[0].amount;
            long plannedDrain = (long) plannedParallels * perParallel;

            long drained = 0;
            long remainingDrain = plannedDrain;
            while (remainingDrain > 0) {
                int chunk = (int) Math.min(remainingDrain, Integer.MAX_VALUE);
                long chunkDrained = depleteInputQuantity(new FluidStack(candidate.fluid, chunk), true);
                if (chunkDrained <= 0) break;
                drained += chunkDrained;
                remainingDrain -= chunkDrained;
                if (chunkDrained < chunk) break;
            }

            int actualParallels = (int) Math.min(drained / perParallel, Integer.MAX_VALUE);
            candidate.parallels = actualParallels;

            if (actualParallels <= 0) continue;
            long actualDrain = (long) actualParallels * perParallel;

            long remainingActual = actualDrain;
            while (remainingActual > 0) {
                int chunk = (int) Math.min(remainingActual, Integer.MAX_VALUE);
                long chunkDrained = depleteInputQuantity(new FluidStack(candidate.fluid, chunk), false);
                if (chunkDrained <= 0) break;
                remainingActual -= chunkDrained;
            }

            outputs.addTo(
                candidate.recipe.mFluidOutputs[0].getFluid(),
                candidate.recipe.mFluidOutputs[0].amount * (long) actualParallels);
            anySuccess = true;
        }

        if (!anySuccess) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int actualMaxDuration = 0;
        for (FluidCandidate candidate : fluidCandidates) {
            if (candidate.parallels > 0) {
                actualMaxDuration = Math.max(actualMaxDuration, candidate.recipe.mDuration);
            }
        }
        if (actualMaxDuration <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        mMaxProgresstime = actualMaxDuration;

        mOutputFluids = outputs.toFluidStacks()
            .toArray(GTValues.emptyFluidStackArray);
        mEfficiency = 10_000;
        useLongPower = true;

        double actualPowerDouble = 0;
        for (FluidCandidate candidate : fluidCandidates) {
            if (candidate.parallels <= 0) continue;
            actualPowerDouble += candidate.parallels * (double) candidate.recipe.mEUt;
        }

        long actualPower = (long) Math.ceil(actualPowerDouble);
        lEUt = -actualPower;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private static class FluidCandidate {

        final GTRecipe recipe;
        final int maxParallelsByInput;
        final Fluid fluid;
        int parallels;

        FluidCandidate(GTRecipe recipe, int maxParallelsByInput, Fluid fluid) {
            this.recipe = recipe;
            this.maxParallelsByInput = maxParallelsByInput;
            this.fluid = fluid;
        }
    }
}
