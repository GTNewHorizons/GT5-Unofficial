package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import bartworks.common.loaders.ItemRegistry;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class AntimatterForge extends MTEExtendedPowerMultiBlockBase
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    public static final String MAINNAME = "antimatterForge";
    public static final int M = 1000000;
    private boolean isLoadedChunk;
    public GTRecipe mLastRecipe;
    public int para;
    protected OverclockDescriber overclockDescriber;
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTUREDEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {

        @Override
        protected IStructureDefinition<AntimatterForge> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterForge>builder()
                .addShape(MAINNAME, transpose(new String[][] { L0, L1, L2, L3, L2, L1, L0 }))
                .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
                .addElement(
                    'I',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
                                HatchElement.InputHatch.or(HatchElement.InputBus))
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex())
                            .dot(1)
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                            .buildAndChain(x.getFrameBlock(), x.getFrameMeta())))
                .addElement(
                    'J',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
                                HatchElement.OutputHatch)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex())
                            .dot(2)
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                            .build()))
                .addElement(
                    'E',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(HatchElement.Energy)
                            .adder(AntimatterForge::addEnergyInjector)
                            .casingIndex(x.textureIndex())
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                            .dot(3)
                            .build()))
                .addElement('F', lazy(x -> ofBlock(x.getFrameBlock(), x.getFrameMeta())))
                .build();
        }
    };

    static {
        Textures.BlockIcons.setCasingTextureForId(
            52,
            TextureFactory.of(
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER_GLOW)
                    .extFacing()
                    .glow()
                    .build()));
    }

    public AntimatterForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public AntimatterForge(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new AntimatterForge(this.MAINNAME);
    }

    protected OverclockDescriber createOverclockDescriber() {
        return new FusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Antimatter Forge")
            .addInfo("Dimensions not included!")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public IStructureDefinition<AntimatterForge> getStructureDefinition() {
        return STRUCTUREDEFINITION.get(getClass());
    }

    public int tier() {
        return 1;
    }

    @Override
    public long maxEUStore() {
        return 100000000;
    }

    /**
     * Unlike {@link #maxEUStore()}, this provides theoretical limit of startup EU, without considering the amount of
     * hatches nor the room for extra energy. Intended for simulation.
     */

    public long capableStartupCanonical() {
        return 160000000;
    }

    public Block getCasingBlock() {
        return Loaders.antimatterContainmentCasing;
    }

    public int getCasingMeta() {
        return 0;
    }

    public Block getCoilBlock() {
        return Loaders.protomatterActivationCoil;
    }

    public int getCoilMeta() {
        return 0;
    }

    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    public int getGlassMeta() {
        return 3;
    }

    public int hatchTier() {
        return 6;
    }

    public Block getFrameBlock() {
        return Loaders.magneticFluxCasing;
    }

    public int getFrameMeta() {
        return 6;
    }

    public int getMaxPara() {
        return 64;
    }

    public int extraPara(int startEnergy) {
        return 1;
    }

    public int textureIndex() {
        return 53;
    }

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1_GLOW)
            .extFacing()
            .glow()
            .build());

    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(MAINNAME, 23, 3, 40);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(MAINNAME, itemStack, b, 23, 3, 40);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(MAINNAME, stackSize, 23, 3, 40, realBudget, env, false, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            this.isLoadedChunk = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !this.isLoadedChunk) {
            // load a 3x3 area when machine is running
            GTChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
            int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() - 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() - 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() - 1 + offZ));
            this.isLoadedChunk = true;
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
                checkRecipe();
            }
            if (mUpdated) {
                mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0 || --mStartUpCheck == 0
                || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                if (mUpdate <= -1000) {
                    mUpdate = 5000;
                }
                checkStructure(true, aBaseMetaTileEntity);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                        criticalStopMachine();
                    }

                    long energyLimit = getSingleHatchPower();
                    List<MTEHatch> hatches = getExoticAndNormalEnergyHatchList();
                    for (MTEHatch hatch : filterValidMTEs(hatches)) {
                        long consumableEnergy = Math.min(hatch.getEUVar(), energyLimit);
                        long receivedEnergy = Math
                            .min(consumableEnergy, maxEUStore() - aBaseMetaTileEntity.getStoredEU());
                        if (receivedEnergy > 0) {
                            hatch.getBaseMetaTileEntity()
                                .decreaseStoredEnergyUnits(receivedEnergy, false);
                            aBaseMetaTileEntity.increaseStoredEnergyUnits(receivedEnergy, true);
                        }
                    }

                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity()
                            .decreaseStoredEnergyUnits(-lEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null)
                                for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math
                                .max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mOutputFluids = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            para = 0;
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe();
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                            || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe()) {
                                    if (aBaseMetaTileEntity.getStoredEU()
                                        < this.mLastRecipe.mSpecialValue + this.lEUt) {
                                        mMaxProgresstime = 0;
                                        turnCasingActive(false);
                                        criticalStopMachine();
                                    }
                                    getBaseMetaTileEntity()
                                        .decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue + this.lEUt, false);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else {
                    turnCasingActive(false);
                    this.mLastRecipe = null;
                    stopMachine();
                }
            }
            aBaseMetaTileEntity
                .setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        }
    }

    /**
     * @return The power one hatch can deliver to the reactor
     */
    protected long getSingleHatchPower() {
        return GTValues.V[tier()] * getMaxPara() * extraPara(100) / 32;
    }

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
        }
        //if (this.eEnergyMulti != null) {
        //    for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
        //        hatch.updateTexture(status ? 52 : 53);
        //    }
        //}
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(), getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build() };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            FluidStack[] antimatterStored = new FluidStack[16];
            long totalAntimatterAmount = 0;
            for (int i = 0; i < antimatterOutputHatches.size(); i++) {
                if (antimatterOutputHatches.get(i) == null || !antimatterOutputHatches.get(i).isValid() || antimatterOutputHatches.get(i).getFluid() == null) continue;
                antimatterStored[i] = antimatterOutputHatches.get(i).getFluid().copy();
                totalAntimatterAmount += antimatterStored[i].amount;
            }
            drainEnergyInput(calculateEnergyContainmentCost(totalAntimatterAmount));
        }
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        FluidStack[] antimatterStored = new FluidStack[16];
        long totalAntimatterAmount = 0;
        long minAntimatterAmount = Long.MAX_VALUE;
        for (int i = 0; i < antimatterOutputHatches.size(); i++) {
            if (antimatterOutputHatches.get(i) == null || !antimatterOutputHatches.get(i).isValid() || antimatterOutputHatches.get(i).getFluid() == null) continue;
            antimatterStored[i] = antimatterOutputHatches.get(i).getFluid().copy();
            totalAntimatterAmount += antimatterStored[i].amount;
            minAntimatterAmount = Math.min(minAntimatterAmount, antimatterStored[i].amount);
        }

<<<<<<< HEAD
    @Override
    protected ProcessingLogic createProcessingLogic() {

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                // When the fusion first loads and is still processing, it does the recipe check without consuming.
                return super.createParallelHelper(recipe).setConsumption(!mRunningOnLoad);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return overclockDescriber.createCalculator(super.createOverclockCalculator(recipe), recipe);
=======
        for (int i = 0; i < antimatterOutputHatches.size(); i++) {
            if (antimatterOutputHatches.get(i) == null || !antimatterOutputHatches.get(i).isValid() || antimatterOutputHatches.get(i).getFluid() == null) continue;
        }

<<<<<<< HEAD
            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!mRunningOnLoad) {
                    if (recipe.mSpecialValue > maxEUStore()) {
                        return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mSpecialValue);
                    }
                    if (recipe.mEUt > GTValues.V[tier()]) {
                        return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                    }
                }
                maxParallel = getMaxPara() * extraPara(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
<<<<<<< HEAD
=======
=======
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return CheckRecipeResultRegistry.insufficientPower(energyCost);
        }

        long protomatterCost = calculateProtoMatterCost(totalAntimatterAmount);
        long containedProtomatter = 0;
        List<FluidStack> inputFluids = getStoredFluids();
        for (int i = 0; i < inputFluids.size(); i++) {
            if (inputFluids.get(i).isFluidEqual(Materials.Antimatter.getFluid(1))) {
                containedProtomatter += Math.min(inputFluids.get(i).amount, protomatterCost - containedProtomatter);
                inputFluids.get(i).amount -= Math.min(protomatterCost - containedProtomatter, inputFluids.get(i).amount);
            }
        }

        distributeAntimatterToHatch(antimatterOutputHatches, totalAntimatterAmount, ((float) containedProtomatter)/((float) protomatterCost));
        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = speed;

>>>>>>> 579cb15fe5 (implement skeleton for antimatter production)
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }
>>>>>>> f158c75f7f (Implement the start of the processing of SSASS)

    private long calculateEnergyContainmentCost(long antimatterAmount) {
        return antimatterAmount;
    }

    private long calculateEnergyCost(long antimatterAmount) {
        return antimatterAmount;
    }

    private long calculateProtoMatterCost(long antimatterAmount) {
        return antimatterAmount;
    }

    private void distributeAntimatterToHatch(List<AntimatterOutputHatch> hatches, long totalAntimatterAmount, float protomatterRequirement) {

    }

    @Override
    protected boolean shouldCheckRecipeThisTick(long aTick) {
        return (aTick % speed) == 0;
    }

    @Override
<<<<<<< HEAD
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[tier()]);
        logic.setAvailableAmperage(getSingleHatchPower() * 32 / GTValues.V[tier()]);
=======
    public void clearHatches() {
        super.clearHatches();
        antimatterOutputHatches.clear();
>>>>>>> f158c75f7f (Implement the start of the processing of SSASS)
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

    private boolean addEnergyInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchEnergy tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergyMulti tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            //return eEnergyMulti.add(tHatch);
        }
        return false;
    }

    private boolean addFluidIO(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof MTEHatchInput tInput) {
            if (tInput.getTierForStructure() < hatchTier()) return false;
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
        }

        if (aMetaTileEntity instanceof MTEHatchOutput tOutput) {

        if (aMetaTileEntity instanceof AntimatterOutputHatch tAntimatter) {
            return antimatterOutputHatches.add(tAntimatter);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output tOutput) {
            if (tOutput.getTierForStructure() < hatchTier()) return false;
            return mOutputHatches.add(tOutput);
        }
        if (aMetaTileEntity instanceof IDualInputHatch tInput) {
            tInput.updateCraftingIcon(this.getMachineCraftingIcon());
            return mDualInputHatches.add(tInput);
        }
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public OverclockDescriber getOverclockDescriber() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        String tier = switch (tier()) {
            case 6 -> EnumChatFormatting.RED + "I" + EnumChatFormatting.RESET;
            case 7 -> EnumChatFormatting.RED + "II" + EnumChatFormatting.RESET;
            case 8 -> EnumChatFormatting.RED + "III" + EnumChatFormatting.RESET;
            case 9 -> EnumChatFormatting.RED + "IV" + EnumChatFormatting.RESET;
            default -> EnumChatFormatting.GOLD + "V" + EnumChatFormatting.RESET;
        };
        double plasmaOut = 0;
        if (mMaxProgresstime > 0) plasmaOut = (double) mOutputFluids[0].amount / mMaxProgresstime;

        return new String[] { EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.RESET + tier,
            StatCollector.translateToLocal("scanner.info.UX.0") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + GTUtility.formatNumbers(this.para)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEUStore())
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(plasmaOut)
                + EnumChatFormatting.RESET
                + "L/t" };
    }

    protected long energyStorageCache;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.0") + " "
                            + numberFormat.format(energyStorageCache)
                            + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::maxEUStore, val -> energyStorageCache = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.1") + " "
                            + numberFormat.format(getEUVar())
                            + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::getEUVar, this::setEUVar));
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    public static final String[] L0 = {
        "                                               ",
        "                                               ",
        "                    FCCCCCF                    ",
        "                   FFFJJJFFF                   ",
        "                FFF FCCCCCF FFF                ",
        "              FF               FF              ",
        "            FF                   FF            ",
        "           F                       F           ",
        "          F                         F          ",
        "         F                           F         ",
        "        F                             F        ",
        "       F                               F       ",
        "      F                                 F      ",
        "      F                                 F      ",
        "     F                                   F     ",
        "     F                                   F     ",
        "    F                                     F    ",
        "    F                                     F    ",
        "    F                                     F    ",
        "   F                                       F   ",
        "  FFF                                     FFF  ",
        "  CFC                                     CFC  ",
        "  CJC                                     CJC  ",
        "  CJC                                     CJC  ",
        "  CJC                                     CJC  ",
        "  CFC                                     CFC  ",
        "  FFF                                     FFF  ",
        "   F                                       F   ",
        "    F                                     F    ",
        "    F                                     F    ",
        "    F                                     F    ",
        "     F                                   F     ",
        "     F                                   F     ",
        "      F                                 F      ",
        "      F                                 F      ",
        "       F                               F       ",
        "        F                             F        ",
        "         F                           F         ",
        "          F                         F          ",
        "           F                       F           ",
        "            FF                   FF            ",
        "              FF               FF              ",
        "                FFF FCCCCCF FFF                ",
        "                   FFFJJJFFF                   ",
        "                    FCCCCCF                    ",
        "                                               ",
        "                                               "};

        public static final String[] L1 = {
            "                                               ",
            "                    FCCCCCF                    ",
            "                   CC     CC                   ",
            "                CCCCC     CCCCC                ",
            "              CCCCCCC     CCCCCCC              ",
            "            CCCCCCC FCCCCCF CCCCCCC            ",
            "           CCCCC               CCCCC           ",
            "          CCCC                   CCCC          ",
            "         CCC                       CCC         ",
            "        CCC                         CCC        ",
            "       CCC                           CCC       ",
            "      CCC                             CCC      ",
            "     CCC                               CCC     ",
            "     CCC                               CCC     ",
            "    CCC                                 CCC    ",
            "    CCC                                 CCC    ",
            "   CCC                                   CCC   ",
            "   CCC                                   CCC   ",
            "   CCC                                   CCC   ",
            "  CCC                                     CCC  ",
            " FCCCF                                   FCCCF ",
            " C   C                                   C   C ",
            " C   C                                   C   C ",
            " C   C                                   C   C ",
            " C   C                                   C   C ",
            " C   C                                   C   C ",
            " FCCCF                                   FCCCF ",
            "  CCC                                     CCC  ",
            "   CCC                                   CCC   ",
            "   CCC                                   CCC   ",
            "   CCC                                   CCC   ",
            "    CCC                                 CCC    ",
            "    CCC                                 CCC    ",
            "     CCC                               CCC     ",
            "     CCC                               CCC     ",
            "      CCC                             CCC      ",
            "       CCC                           CCC       ",
            "        CCC                         CCC        ",
            "         CCC                       CCC         ",
            "          CCCC                   CCCC          ",
            "           CCCCC               CCCCC           ",
            "            CCCCCCC FCCCCCF CCCCCCC            ",
            "              CCCCCCC     CCCCCCC              ",
            "                CCCCC     CCCCC                ",
            "                   CC     CC                   ",
            "                    FCCCCCF                    ",
            "                                               "
        };

        public static final String[] L2 = {
            "                    FCCCCCF                    ",
            "                   CC     CC                   ",
            "                CCCCC     CCCCC                ",
            "              CCCCCHHHHHHHHHCCCCC              ",
            "            CCCCHHHCC     CCHHHCCCC            ",
            "           CCCHHCCCCC     CCCCCHHCCC           ",
            "          CCHHCCCCC FCCCCCF CCCCCHHCC          ",
            "         CCHCCCC               CCCCHCC         ",
            "        CCHCCC                   CCCHCC        ",
            "       CCHCC                       CCHCC       ",
            "      CCHCC                         CCHCC      ",
            "     CCHCC                           CCHCC     ",
            "    CCHCC                             CCHCC    ",
            "    CCHCC                             CCHCC    ",
            "   CCHCC                               CCHCC   ",
            "   CCHCC                               CCHCC   ",
            "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ",
            " CCHCC                                   CCHCC ",
            "FCCHCCF                                 FCCHCCF",
            "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C",
            "FCCHCCF                                 FCCHCCF",
            " CCHCC                                   CCHCC ",
            "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ",
            "   CCHCC                               CCHCC   ",
            "   CCHCC                               CCHCC   ",
            "    CCHCC                             CCHCC    ",
            "    CCHCC                             CCHCC    ",
            "     CCHCC                           CCHCC     ",
            "      CCHCC                         CCHCC      ",
            "       CCHCC                       CCHCC       ",
            "        CCHCCC                   CCCHCC        ",
            "         CCHCCCC               CCCCHCC         ",
            "          CCHHCCCCC FCCCCCF CCCCCHHCC          ",
            "           CCCHHCCCCC     CCCCCHHCCC           ",
            "            CCCCHHHCC     CCHHHCCCC            ",
            "              CCCCCHHHHHHHHHCCCCC              ",
            "                CCCCC     CCCCC                ",
            "                   CC     CC                   ",
            "                    FCCCCCF                    "
        };

        public static final String[] L3 = {
            "                   FFFEEEFFF                   ",
            "                FFFCC     CCFFF                ",
            "              FFCCCHHHHHHHHHCCCFF              ",
            "            FFCCHHHHHHHHHHHHHHHCCFF            ",
            "           FCCHHHHHHHHHHHHHHHHHHHCCF           ",
            "          FCHHHHHHHCC     CCHHHHHHHCF          ",
            "         FCHHHHHCCCFFFIIIFFFCCCHHHHHCF         ",
            "        FCHHHHCCFFF         FFFCCHHHHCF        ",
            "       FCHHHCCFF               FFCCHHHCF       ",
            "      FCHHHCFF                   FFCHHHCF      ",
            "     FCHHHCF                       FCHHHCF     ",
            "    FCHHHCF                         FCHHHCF    ",
            "   FCHHHCF                           FCHHHCF   ",
            "   FCHHHCF                           FCHHHCF   ",
            "  FCHHHCF                             FCHHHCF  ",
            "  FCHHHCF                             FCHHHCF  ",
            " FCHHHCF                               FCHHHCF ",
            " FCHHHCF                               FCHHHCF ",
            " FCHHHCF                               FCHHHCF ",
            "FCHHHCF                                 FCHHHCF",
            "FCHHHCF                                 FCHHHCF",
            "F HHH F                                 F HHH F",
            "E HHH I                                 I HHH E",
            "E HHH I                                 I HHH E",
            "E HHH I                                 I HHH E",
            "F HHH F                                 F HHH F",
            "FCHHHCF                                 FCHHHCF",
            "FCHHHCF                                 FCHHHCF",
            " FCHHHCF                               FCHHHCF ",
            " FCHHHCF                               FCHHHCF ",
            " FCHHHCF                               FCHHHCF ",
            "  FCHHHCF                             FCHHHCF  ",
            "  FCHHHCF                             FCHHHCF  ",
            "   FCHHHCF                           FCHHHCF   ",
            "   FCHHHCF                           FCHHHCF   ",
            "    FCHHHCF                         FCHHHCF    ",
            "     FCHHHCF                       FCHHHCF     ",
            "      FCHHHCFF                   FFCHHHCF      ",
            "       FCHHHCCFF               FFCCHHHCF       ",
            "        FCHHHHCCFFF         FFFCCHHHHCF        ",
            "         FCHHHHHCCCFFFI~IFFFCCCHHHHHCF         ",
            "          FCHHHHHHHCC     CCHHHHHHHCF          ",
            "           FCCHHHHHHHHHHHHHHHHHHHCCF           ",
            "            FFCCHHHHHHHHHHHHHHHCCFF            ",
            "              FFCCCHHHHHHHHHCCCFF              ",
            "                FFFCC     CCFFF                ",
            "                   FFFEEEFFF                   "
        };
    }
