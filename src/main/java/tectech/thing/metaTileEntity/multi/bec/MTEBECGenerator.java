package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTBECRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.client.GTSoundLoop;
import gregtech.client.ISoundLoopAware;
import gregtech.client.volumetric.LinearSound;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.loaders.load.BECRecipeLoader;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.mechanics.boseEinsteinCondensate.CondensateStack;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECGenerator extends MTEBECMultiblockBase<MTEBECGenerator> implements ISoundLoopAware {

    private List<CondensateStack> mOutputCondensate;

    private final LinearSound soundPos;

    public MTEBECGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        this.soundPos = new LinearSound().setCoords(0, 0, 2, 0, 0, 25)
            .setCentre(2, 1, 10);
    }

    protected MTEBECGenerator(MTEBECGenerator prototype) {
        super(prototype);

        this.soundPos = prototype.soundPos;
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
        structure.addCasing('O', ElectromagneticallyIsolatedCasing).withUnlimitedHatches(2, Arrays.asList(BECHatches.Hatch));
        structure.addCasing('1', ElectromagneticallyIsolatedCasing).withHatches(1, 16, Arrays.asList(InputBus, InputHatch, Energy, ExoticEnergy));

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

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(
            TextWidget.dynamicString(this::generateCondensateOutputText)
                .setTextAlignment(Alignment.CenterLeft));
    }

    protected String generateCondensateOutputText() {
        StringBuffer ret = new StringBuffer();

        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        if (mOutputCondensate != null) {
            for (var mat : mOutputCondensate) {
                if (mat == null) continue;
                ret.append(EnumChatFormatting.AQUA)
                    .append(
                        mat.getPreview()
                            .getDisplayName())
                    .append(EnumChatFormatting.WHITE)
                    .append(" x ")
                    .append(EnumChatFormatting.GOLD);
                numberFormat.format(mat.amount, ret);
                ret.append(" L")
                    .append(EnumChatFormatting.WHITE);
                double processPerTick = (double) mat.amount / mMaxProgresstime * 20;
                if (processPerTick > 1) {
                    ret.append(" (");
                    numberFormat.format(Math.round(processPerTick * 10) / 10.0, ret);
                    ret.append("L/s)");
                } else {
                    ret.append(" (");
                    numberFormat.format(Math.round(1 / processPerTick * 10) / 10.0, ret);
                    ret.append("s/L)");
                }
                ret.append('\n');
            }
        }
        return ret.toString();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        if (mOutputCondensate != null) {
            aNBT.setTag("condensate", CondensateStack.save(mOutputCondensate));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mOutputCondensate = null;

        if (aNBT.getTag("condensate") instanceof NBTTagList list) {
            mOutputCondensate = CondensateStack.load(list);
        }
    }

    // #endregion

    @Override
    public void outputAfterRecipe_EM() {
        if (mOutputCondensate != null && !mOutputCondensate.isEmpty()) {
            if (network != null) {
                for (BECInventory inv : network.getComponents(BECInventory.class)) {
                    inv.addCondensate(mOutputCondensate);
                }
            }

            mOutputCondensate = null;
        }
    }

    @Override
    @Nonnull
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            TecTechRecipeMaps.condensateCreationFromItemRecipes,
            TecTechRecipeMaps.condensateCreationFromFluidRecipes);
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_BEC_GENERATOR;
    }

    @Override
    public void modifySoundLoop(GTSoundLoop loop) {
        Vector3f v = soundPos.getPosition(this);

        if (v != null) loop.setPosition(v);
    }

    @Override
    public void onSoundLoopTicked(GTSoundLoop loop) {
        modifySoundLoop(loop);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        long euQuota = 0;

        int baseProcessingTime = 1 * SECONDS;

        for (MTEHatch hatch : filterValidMTEs(getExoticAndNormalEnergyHatchList())) {
            if (hatch instanceof MTEHatchEnergyMulti multi) {
                euQuota += V[multi.mTier] * multi.Amperes * baseProcessingTime;
            }
        }

        long startingQuota = euQuota;

        Object2LongOpenHashMap<Object> outputMaterials = new Object2LongOpenHashMap<>();

        for (MTEHatchInputBus inputBus : filterValidMTEs(mInputBusses)) {
            for (int i = inputBus.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack slot = inputBus.getStackInSlot(i);

                if (slot == null) continue;

                GTBECRecipe recipe = (GTBECRecipe) TecTechRecipeMaps.condensateCreationFromItemRecipes.findRecipeQuery()
                    .items(slot)
                    .find();

                if (recipe == null) continue;

                long cost = BECRecipeLoader.getRecipeCost(recipe);

                long quotaRemaining = euQuota / cost;
                long toRemove = Math.min(Math.min(quotaRemaining, slot.stackSize), Integer.MAX_VALUE);

                if (toRemove == 0) continue;

                inputBus.decrStackSize(i, (int) toRemove);

                euQuota -= cost * toRemove;

                for (CondensateStack stack : recipe.mCOutput) {
                    long toAdd = toRemove * stack.amount;
                    outputMaterials.addTo(stack.material, toAdd);
                }
            }
        }

        for (MTEHatchInput inputHatch : filterValidMTEs(mInputHatches)) {
            if (inputHatch instanceof MTEHatchMultiInput multiInputHatch) {
                for (FluidStack tFluid : multiInputHatch.getStoredFluid()) {
                    if (tFluid != null && tFluid.amount > 0) {
                        euQuota = tryDrainFluid(outputMaterials, euQuota, inputHatch, tFluid);
                    }
                }
            } else if (inputHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack fluidStack : meHatch.getStoredFluids()) {
                    if (fluidStack != null && fluidStack.amount > 0) {
                        euQuota = tryDrainFluid(outputMaterials, euQuota, inputHatch, fluidStack);
                    }
                }
            } else {
                if (inputHatch.getFillableStack() != null && inputHatch.getFillableStack().amount > 0) {
                    euQuota = tryDrainFluid(outputMaterials, euQuota, inputHatch, inputHatch.getFillableStack());
                }
            }
        }

        if (outputMaterials.isEmpty()) {
            mMaxProgresstime = 0;
            mOutputCondensate = null;

            return CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            mOutputCondensate = outputMaterials.object2LongEntrySet()
                .stream()
                .map(e -> new CondensateStack(e.getKey(), e.getLongValue()))
                .collect(Collectors.toList());
            mMaxProgresstime = baseProcessingTime;
            mEfficiency = 10_000;

            useLongPower = true;
            lEUt = -(startingQuota - euQuota) / mMaxProgresstime;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    private long tryDrainFluid(Object2LongOpenHashMap<Object> outputMaterials, long euQuota, MTEHatchInput inputHatch,
        FluidStack fluidStack) {
        GTBECRecipe recipe = (GTBECRecipe) TecTechRecipeMaps.condensateCreationFromFluidRecipes.findRecipeQuery()
            .fluids(new FluidStack(fluidStack.getFluid(), 1))
            .find();

        if (recipe == null) {
            return 0;
        }

        long cost = BECRecipeLoader.getRecipeCost(recipe);

        long availableQuota = euQuota * 1000 / cost;
        long toRemove = GTUtility.clamp(fluidStack.amount, 0, availableQuota);

        if (toRemove <= 0) {
            return 0;
        }

        euQuota -= GTUtility.ceilDiv(toRemove * cost, 1000);

        long toDrain = GTUtility.min(fluidStack.amount, toRemove, Integer.MAX_VALUE);

        FluidStack drained = inputHatch.drain(ForgeDirection.UNKNOWN, new FluidStack(fluidStack, (int) toDrain), true);

        for (CondensateStack stack : recipe.mCOutput) {
            outputMaterials.addTo(stack.material, drained.amount);
        }

        return euQuota;
    }
}
