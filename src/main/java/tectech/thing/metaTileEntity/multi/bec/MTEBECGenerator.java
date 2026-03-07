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
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.NotNull;

import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.client.volumetric.ISoundPosition;
import gregtech.client.volumetric.LinearSound;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECGenerator extends MTEBECMultiblockBase<MTEBECGenerator> {

    public MTEBECGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
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

        tt.addMachineType("BEC Generator")
            .addInfo("Makes fancy atoms");

        tt.beginStructureBlock();
        tt.addController("Front Center");
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.becConnectorHatch.get(1));
        tt.addHatchLocationOverride(
            Arrays.asList(InputBus, InputHatch, Energy, ExoticEnergy),
            "Any " + ElectromagneticallyIsolatedCasing.getLocalizedName() + " in the first slice");
        tt.addHatchLocationOverride(BECHatches.Hatch, "The centre casing in the last slice");
        tt.addAllCasingInfo(
            Arrays.asList(
                ElectromagneticallyIsolatedCasing,
                SuperconductivePlasmaEnergyConduit,
                AdvancedFusionCoilII,
                ElectromagneticWaveguide),
            null);

        tt.toolTipFinisher(EnumChatFormatting.WHITE, 0, GTValues.AuthorPineapple);

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
            IAEFluidStack[] outputs = GTDataUtils.mapToArray(outputFluids, IAEFluidStack[]::new, AEFluidStack::create);

            for (BECInventory inv : network.getComponents(BECInventory.class)) {
                for (IAEFluidStack stack : outputs) {
                    inv.addCondensate(stack);
                }
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

    @SideOnly(Side.CLIENT)
    @Override
    protected ISoundPosition getSoundPosition() {
        return new LinearSound(this, 0, 0, 2, 0, 0, 25, 32).setCentre(2, 1, 10);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        MutableLong euQuota = new MutableLong(0);

        int baseProcessingTime = 1 * SECONDS;

        for (MTEHatch hatch : filterValidMTEs(getExoticAndNormalEnergyHatchList())) {
            euQuota.add(hatch.maxEUInput() * hatch.maxAmperesIn() * baseProcessingTime);
        }

        long startingQuota = euQuota.longValue();

        ArrayList<FluidStack> outputs = new ArrayList<>();

        for (FluidStack input : getStoredFluids()) {
            tryDrainFluid(outputs, euQuota, input);
        }

        if (outputs.isEmpty()) {
            mMaxProgresstime = 0;

            return CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            Object2LongOpenHashMap<Fluid> fluids = new Object2LongOpenHashMap<>();

            for (FluidStack output : outputs) {
                fluids.addTo(output.getFluid(), output.amount);
            }

            outputs.clear();

            Object2LongMaps.fastForEach(fluids, e -> {
                while (e.getLongValue() > 0) {
                    int amount = GTUtility.longToInt(e.getLongValue());

                    outputs.add(new FluidStack(e.getKey(), amount));

                    e.setValue(e.getLongValue() - amount);
                }
            });

            mOutputFluids = outputs.toArray(GTValues.emptyFluidStackArray);
            mMaxProgresstime = baseProcessingTime;
            mEfficiency = 10_000;
            useLongPower = true;
            lEUt = -(startingQuota - euQuota.longValue()) / mMaxProgresstime;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    private void tryDrainFluid(ArrayList<FluidStack> outputs, MutableLong euQuota, FluidStack fluidStack) {
         GTRecipe recipe = TecTechRecipeMaps.condensateGeneratorRecipes.findRecipeQuery()
            .fluids(new FluidStack(fluidStack.getFluid(), fluidStack.amount))
            .find();

        if (recipe == null) {
            return;
        }

        int parallels = (int) recipe.maxParallelCalculatedByInputs(Integer.MAX_VALUE, new FluidStack[]{ fluidStack }, GTValues.emptyItemStackArray);

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

        FluidStack output = recipe.mFluidOutputs[0].copy();
        output.amount *= parallels;
        outputs.add(output);

        this.mMaxProgresstime = Math.max(this.mMaxProgresstime, recipe.mDuration);
    }
}
