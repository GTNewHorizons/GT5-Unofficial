package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;

import java.util.Arrays;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.util.item.AEFluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
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
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

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
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing);
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ElectromagneticWaveguide);
        structure.addCasing('E', AdvancedFusionCoilII);
        structure.addCasing('O', ElectromagneticallyIsolatedCasing)
            .withUnlimitedHatches(2, Arrays.asList(BECHatches.Hatch));
        structure.addCasing('1', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(InputBus, InputHatch, Energy, ExoticEnergy));

        return structure.buildStructure(definition);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECGenerator> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Generator, Condensate Entangler")
            .addMarkdown(new ResourceLocation("gregtech", "bec-generator"));

        tt.beginStructureBlock();
        tt.addController("Front Center");
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.Hatch_BEC_Connector.get(1));
        tt.addHatchLocationOverride(
            Arrays.asList(InputBus, InputHatch, Energy, ExoticEnergy),
            "Any " + ElectromagneticallyIsolatedCasing.getLocalizedName() + " in the first slice");
        tt.addHatchLocationOverride(BECHatches.Hatch, "The center casing in the last slice");
        tt.addAllCasingInfo(
            Arrays.asList(
                ElectromagneticallyIsolatedCasing,
                SuperconductivePlasmaEnergyConduit,
                AdvancedFusionCoilII,
                ElectromagneticWaveguide),
            null);

        tt.toolTipFinisher(GTAuthors.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
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
    protected void addFluidOutputs(FluidStack[] outputFluids) {
        if (network != null) {
            for (FluidStack output : outputFluids) {
                network.injectCondensate(this, AEFluidStack.create(output));
            }
        }
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
        MutableLong euQuota = new MutableLong(getMaxInputEu());

        long startingQuota = euQuota.longValue();

        CondensateList outputs = new CondensateList();

        // Clear the recipe time here. It's mutated in tryDrainFluid.
        mMaxProgresstime = 0;

        for (FluidStack input : getStoredFluids()) {
            tryDrainFluid(outputs, euQuota, input);
        }

        if (outputs.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            mOutputFluids = outputs.toFluidStacks()
                .toArray(GTValues.emptyFluidStackArray);
            mEfficiency = 10_000;
            useLongPower = true;
            lEUt = -(startingQuota - euQuota.longValue()) / mMaxProgresstime;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    private void tryDrainFluid(CondensateList outputs, MutableLong euQuota, FluidStack fluidStack) {
        GTRecipe recipe = TecTechRecipeMaps.condensateGeneratorRecipes.findRecipeQuery()
            .fluids(new FluidStack(fluidStack.getFluid(), fluidStack.amount))
            .find();

        if (recipe == null) {
            return;
        }

        int parallels = (int) recipe.maxParallelCalculatedByInputs(
            Integer.MAX_VALUE,
            new FluidStack[] { fluidStack },
            GTValues.emptyItemStackArray);

        parallels = Math.min(parallels, (int) (euQuota.longValue() / recipe.mEUt));

        if (parallels <= 0) {
            return;
        }

        FluidStack toDrain = fluidStack.copy();
        toDrain.amount = parallels * recipe.mFluidInputs[0].amount;

        if (!depleteInput(toDrain)) {
            return;
        }

        euQuota.subtract(toDrain.amount / recipe.mFluidInputs[0].amount * (long) recipe.mEUt);

        outputs.addTo(recipe.mFluidOutputs[0].getFluid(), recipe.mFluidOutputs[0].amount * (long) parallels);

        this.mMaxProgresstime = Math.max(this.mMaxProgresstime, recipe.mDuration);
    }
}
