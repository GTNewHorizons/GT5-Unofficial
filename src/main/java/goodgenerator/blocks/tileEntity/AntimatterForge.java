package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

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

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
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
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class AntimatterForge extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    public static final String MAIN_NAME = "antimatterForge";
    public static final int M = 1_000_000;
    private boolean isLoadedChunk;
    public GT_Recipe mLastRecipe;
    public int para;
    protected OverclockDescriber overclockDescriber;
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {

        @Override
        protected IStructureDefinition<AntimatterForge> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterForge>builder()
                .addShape(MAIN_NAME, transpose(new String[][] { L0, L1, L2, L3, L2, L1, L0 }))
                .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
                .addElement(
                    'I',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
                                GT_HatchElement.InputHatch.or(GT_HatchElement.InputBus))
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex())
                            .dot(1)
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                            .buildAndChain(x.getFrameBlock(), x.getFrameMeta())))
                .addElement(
                    'J',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
                                GT_HatchElement.OutputHatch)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex())
                            .dot(2)
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                            .build()))
                .addElement(
                    'E',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(GT_HatchElement.Energy)
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
        this.overclockDescriber = createOverclockDescriber();
    }

    public AntimatterForge(String aName) {
        super(aName);
        this.overclockDescriber = createOverclockDescriber();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new AntimatterForge(this.MAIN_NAME);
    }

    protected OverclockDescriber createOverclockDescriber() {
        return new FusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Antimatter Forge")
            .addInfo("Dimensions not included!")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Nullable
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    @Override
    public IStructureDefinition<AntimatterForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    public int tier() {
        return 1;
    }

    @Override
    public long maxEUStore() {
        return 100_000_000;
    }

    /**
     * Unlike {@link #maxEUStore()}, this provides theoretical limit of startup EU, without considering the amount of
     * hatches nor the room for extra energy. Intended for simulation.
     */

    public long capableStartupCanonical() {
        return 160_000_000;
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
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // Migration code
        if (lEUt > 0) {
            lEUt = -lEUt;
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(MAIN_NAME, 23, 3, 40);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(MAIN_NAME, itemStack, b, 23, 3, 40);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(MAIN_NAME, stackSize, 23, 3, 40, realBudget, env, false, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            this.isLoadedChunk = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !this.isLoadedChunk) {
            // load a 3x3 area when machine is running
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
            int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() - 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() - 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
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
                    List<GT_MetaTileEntity_Hatch> hatches = getExoticAndNormalEnergyHatchList();
                    for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(hatches)) {
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
        return GT_Values.V[tier()] * getMaxPara() * extraPara(100) / 32;
    }

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        //if (this.eEnergyMulti != null) {
        //    for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : this.eEnergyMulti) {
        //        hatch.updateTexture(status ? 52 : 53);
        //    }
        //}
        if (this.mOutputHatches != null) {
            for (GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fusionRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -2;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected GT_ParallelHelper createParallelHelper(@NotNull GT_Recipe recipe) {
                // When the fusion first loads and is still processing, it does the recipe check without consuming.
                return super.createParallelHelper(recipe).setConsumption(!mRunningOnLoad);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return overclockDescriber.createCalculator(super.createOverclockCalculator(recipe), recipe);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (!mRunningOnLoad) {
                    if (recipe.mSpecialValue > maxEUStore()) {
                        return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mSpecialValue);
                    }
                    if (recipe.mEUt > GT_Values.V[tier()]) {
                        return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                    }
                }
                maxParallel = getMaxPara() * extraPara(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (mRunningOnLoad) mRunningOnLoad = false;
                turnCasingActive(result.wasSuccessful());
                if (result.wasSuccessful()) {
                    mLastRecipe = lastRecipe;
                } else {
                    mLastRecipe = null;
                }
                para = getCurrentParallels();
                return result;
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Values.V[tier()]);
        logic.setAvailableAmperage(getSingleHatchPower() * 32 / GT_Values.V[tier()]);
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk) GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            //return eEnergyMulti.add(tHatch);
        }
        return false;
    }

    private boolean addFluidIO(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tInput) {
            if (tInput.getTierForStructure() < hatchTier()) return false;
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
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
                + GT_Utility.formatNumbers(this.para)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEUStore())
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(plasmaOut)
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