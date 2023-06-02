package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine.isValidForLowGravity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_CubicMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Single_Recipe_Check;
import gregtech.api.util.GT_Single_Recipe_Check_Processing_Array;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MetaTileEntity_ProcessingArray
    extends GT_MetaTileEntity_CubicMultiBlockBase<GT_MetaTileEntity_ProcessingArray> {

    private GT_Recipe_Map mLastRecipeMap;
    private GT_Recipe mLastRecipe;
    private int tTier = 0;
    private int mMult = 0;
    private boolean downtierUEV = true;
    private String mMachineName = "";
    // Value needed so that the PA can use energy above MAX voltage
    private long mEUPerTick = 0;

    public GT_MetaTileEntity_ProcessingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ProcessingArray(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ProcessingArray(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Processing Array")
            .addInfo("Runs supplied machines as if placed in the world")
            .addInfo("Place up to 64 singleblock GT machines into the controller")
            .addInfo("Note that you still need to supply power to them all")
            .addInfo("Use a screwdriver to enable separate input busses")
            .addInfo("Use a wire cutter to disable UEV+ downtiering")
            .addInfo("Doesn't work on certain machines, deal with it")
            .addInfo("Use it if you hate GT++, or want even more speed later on")
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoRange("Robust Tungstensteel Machine Casing", 14, 24, false)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addOutputHatch("Any casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48] };
    }

    // TODO: Expand so it also does the non recipe map recipes
    /*
     * public void remoteRecipeCheck() { if (mInventory[1] == null) return; String tmp =
     * mInventory[1].getUnlocalizedName().replaceAll("gt.blockmachines.basicmachine.", ""); if
     * (tmp.startsWith("replicator")) { } else if (tmp.startsWith("brewery")) { } else if (tmp.startsWith("packer")) { }
     * else if (tmp.startsWith("printer")) { } else if (tmp.startsWith("disassembler")) { } else if
     * (tmp.startsWith("massfab")) { } else if (tmp.startsWith("scanner")) { } }
     */

    // Gets the recipe map for the given machine through its unlocalized name
    @Override
    public GT_Recipe_Map getRecipeMap() {
        if (isCorrectMachinePart(mInventory[1])) {
            int length = mInventory[1].getUnlocalizedName()
                .length();
            String aMachineName = mInventory[1].getUnlocalizedName()
                .substring(17, length - 8);
            return GT_ProcessingArray_Manager.giveRecipeMap(aMachineName);
        }
        return null;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return aStack != null && aStack.getUnlocalizedName()
            .startsWith("gt.blockmachines.");
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        SoundResource sound = SoundResource.get(aIndex < 0 ? aIndex + 256 : 0);
        if (sound != null) {
            GT_Utility.doSoundAtClient(sound, getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    @Override
    protected boolean checkRecipe() {
        startRecipeProcessing();
        boolean result = checkRecipe(mInventory[1]);
        if (result) {
            int length = mInventory[1].getUnlocalizedName()
                .length();
            String aMachineName = mInventory[1].getUnlocalizedName()
                .substring(17, length - 8);
            SoundResource sound = GT_ProcessingArray_Manager.getSoundResource(aMachineName);
            if (sound != null) {
                sendLoopStart((byte) sound.id);
            }
        }
        endRecipeProcessing();
        return result;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (mLockedToSingleRecipe && mSingleRecipeCheck != null) {
            return processLockedRecipe();
        }

        if (!isCorrectMachinePart(mInventory[1])) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map map = getRecipeMap();
        if (map == null) return false;

        if (!mMachineName.equals(mInventory[1].getUnlocalizedName())) {
            mLastRecipe = null;
            mMachineName = mInventory[1].getUnlocalizedName();
        }

        if (mLastRecipe == null) {
            setTierAndMult();
        }
        ArrayList<FluidStack> tFluidList = getStoredFluids();
        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
        if (inputSeparation) {
            ArrayList<ItemStack> tInputList = new ArrayList<>();
            for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
                IGregTechTileEntity tInputBus = tHatch.getBaseMetaTileEntity();
                for (int i = tInputBus.getSizeInventory() - 1; i >= 0; i--) {
                    if (tInputBus.getStackInSlot(i) != null) tInputList.add(tInputBus.getStackInSlot(i));
                }
                ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
                if (processRecipe(tInputs, tFluids, map)) return true;
                else tInputList.clear();
            }
        } else {
            ArrayList<ItemStack> tInputList = getStoredInputs();
            ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
            return processRecipe(tInputs, tFluids, map);
        }
        return false;
    }

    private void setTierAndMult() {
        IMetaTileEntity aMachine = GT_Item_Machines.getMetaTileEntity(mInventory[1]);
        if (aMachine != null) tTier = ((GT_MetaTileEntity_TieredMachineBlock) aMachine).mTier;
        mMult = 0;
        if (downtierUEV && tTier > 9) {
            tTier--; // Lowers down the tier by 1 to allow for bigger parallel
            mMult = 2; // Multiplies Parallels by 4x, keeping the energy cost
        }
    }

    public boolean processLockedRecipe() {
        GT_Single_Recipe_Check_Processing_Array tSingleRecipeCheck = (GT_Single_Recipe_Check_Processing_Array) mSingleRecipeCheck;

        if (mLastRecipe == null) {
            setTierAndMult();
            mLastRecipe = tSingleRecipeCheck.getRecipe();
        }

        int machines = mInventory[1].stackSize << mMult;
        int parallel = tSingleRecipeCheck.checkRecipeInputs(true, machines);

        return processRecipeOutputs(
            tSingleRecipeCheck.getRecipe(),
            tSingleRecipeCheck.getRecipeAmperage(),
            parallel,
            1);
    }

    public boolean processRecipe(ItemStack[] tInputs, FluidStack[] tFluids, GT_Recipe.GT_Recipe_Map map) {
        if (tInputs.length <= 0 && tFluids.length <= 0) return false;
        GT_Recipe tRecipe = map.findRecipe(
            getBaseMetaTileEntity(),
            mLastRecipe,
            false,
            gregtech.api.enums.GT_Values.V[tTier],
            tFluids,
            tInputs);
        if (tRecipe == null) return false;
        if (GT_Mod.gregtechproxy.mLowGravProcessing && tRecipe.mSpecialValue == -100
            && !isValidForLowGravity(tRecipe, getBaseMetaTileEntity().getWorld().provider.dimensionId)) return false;

        GT_Single_Recipe_Check_Processing_Array.Builder tSingleRecipeCheckBuilder = null;
        if (mLockedToSingleRecipe) {
            // We're locked to a single recipe, but haven't built the recipe checker yet.
            // Build the checker on next successful recipe.
            tSingleRecipeCheckBuilder = GT_Single_Recipe_Check_Processing_Array.processingArrayBuilder(this)
                .setBefore(tInputs, tFluids);
        }

        boolean recipeLocked = false;
        mLastRecipe = tRecipe;
        int machines = mInventory[1].stackSize << mMult;
        int i = 0;
        for (; i < machines; i++) {
            if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                break;
            } else if (mLockedToSingleRecipe && !recipeLocked) {
                // We want to lock to a single run of the recipe.
                mSingleRecipeCheck = tSingleRecipeCheckBuilder.setAfter(tInputs, tFluids)
                    .setRecipe(tRecipe)
                    .setRecipeAmperage(map.mAmperage)
                    .build();
                recipeLocked = true;
            }
        }

        // Check how many times we can run the same recipe
        int multiplier = 1;
        if (batchMode && i == machines) {
            for (; multiplier < 128; ++multiplier) {
                if (!tRecipe.isRecipeInputEqual(true, false, machines, tFluids, tInputs)) {
                    break;
                }
            }
        }
        return processRecipeOutputs(tRecipe, map.mAmperage, i, multiplier);
    }

    public boolean processRecipeOutputs(GT_Recipe aRecipe, int aAmperage, int parallel, int multiplier) {
        this.mEUPerTick = 0;
        this.mOutputItems = null;
        this.mOutputFluids = null;
        if (parallel == 0) {
            return false;
        }

        this.mMaxProgresstime = aRecipe.mDuration * multiplier;

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        ProcessingArrayCalculateOverclock(
            aRecipe.mEUt,
            aRecipe.mDuration * multiplier,
            aAmperage,
            GT_Values.V[tTier],
            false);
        // In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUPerTick == Long.MAX_VALUE - 1) return false;
        mEUPerTick = mEUPerTick * parallel;
        if (mEUPerTick == Long.MAX_VALUE - 1) return false;

        if (mEUPerTick > 0) {
            mEUPerTick = (-mEUPerTick);
        }
        ItemStack[] tOut = new ItemStack[aRecipe.mOutputs.length];
        for (int h = 0; h < aRecipe.mOutputs.length; h++) {
            if (aRecipe.getOutput(h) != null) {
                tOut[h] = aRecipe.getOutput(h)
                    .copy();
                tOut[h].stackSize = 0;
            }
        }
        FluidStack[] tFOut = new FluidStack[aRecipe.mFluidOutputs.length];
        for (int i = 0; i < aRecipe.mFluidOutputs.length; i++)
            if (aRecipe.getFluidOutput(i) != null) tFOut[i] = aRecipe.getFluidOutput(i)
                .copy();
        for (int f = 0; f < tOut.length; f++) {
            if (aRecipe.mOutputs[f] != null && tOut[f] != null) {
                for (int g = 0; g < parallel * multiplier; g++) {
                    if (getBaseMetaTileEntity().getRandomNumber(10000) < aRecipe.getOutputChance(f))
                        tOut[f].stackSize += aRecipe.mOutputs[f].stackSize;
                }
            }
        }
        byte oNumber = 0;
        for (FluidStack fluidStack : tFOut) {
            if (fluidStack != null) {
                int tSize = fluidStack.amount;
                tFOut[oNumber].amount = tSize * parallel * multiplier;
            }
            oNumber++;
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputItems = Arrays.stream(tOut)
            .filter(Objects::nonNull)
            .flatMap(GT_MetaTileEntity_ProcessingArray::splitOversizedStack)
            .filter(is -> is.stackSize > 0)
            .toArray(ItemStack[]::new);
        this.mOutputFluids = tFOut;
        updateSlots();
        return true;
    }

    private static Stream<ItemStack> splitOversizedStack(ItemStack aStack) {
        int tMaxStackSize = aStack.getMaxStackSize();
        if (aStack.stackSize <= tMaxStackSize) return Stream.of(aStack);
        int tRepeat = aStack.stackSize / tMaxStackSize;
        aStack.stackSize = aStack.stackSize % tMaxStackSize;
        Stream.Builder<ItemStack> tBuilder = Stream.builder();
        tBuilder.add(aStack);
        for (int i = 0; i < tRepeat; i++) {
            ItemStack rStack = aStack.copy();
            rStack.stackSize = tMaxStackSize;
            tBuilder.add(rStack);
        }
        return tBuilder.build();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (mMachine && aTick % 20 == 0) {
            GT_Recipe_Map tCurrentMap = getRecipeMap();
            if (tCurrentMap != mLastRecipeMap) {
                for (GT_MetaTileEntity_Hatch_InputBus tInputBus : mInputBusses) tInputBus.mRecipeMap = tCurrentMap;
                for (GT_MetaTileEntity_Hatch_Input tInputHatch : mInputHatches) tInputHatch.mRecipeMap = tCurrentMap;
                mLastRecipeMap = tCurrentMap;
            }
        }
    }

    @Override
    protected IStructureElement<GT_MetaTileEntity_CubicMultiBlockBase<?>> getCasingElement() {
        return ofBlock(GregTech_API.sBlockCasings4, 0);
    }

    @Override
    protected int getHatchTextureIndex() {
        return 48;
    }

    @Override
    protected int getRequiredCasingCount() {
        return 14;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("downtierUEV", downtierUEV);
        aNBT.setLong("mEUPerTick", mEUPerTick);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
        downtierUEV = aNBT.getBoolean("downtierUEV");
        mEUPerTick = aNBT.getLong("mEUPerTick");
    }

    @Override
    protected GT_Single_Recipe_Check loadSingleRecipeChecker(NBTTagCompound aNBT) {
        return GT_Single_Recipe_Check_Processing_Array.tryLoad(this, getRecipeMap(), aNBT, mInventory[1]);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            // Lock to single recipe
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        } else {
            inputSeparation = !inputSeparation;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, "Batch recipes");
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "Don't batch recipes");
            }
            return true;
        } else {
            downtierUEV = !downtierUEV;
            mLastRecipe = null; // clears last recipe
            GT_Utility.sendChatToPlayer(aPlayer, "Treat UEV+ machines as multiple UHV " + downtierUEV);
            return true;
        }
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
    protected List<IHatchElement<? super GT_MetaTileEntity_CubicMultiBlockBase<?>>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, ExoticEnergy);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mExoticEnergyHatches.clear();
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return GT_ExoticEnergyInputHelper.drainEnergy(aEU, getExoticAndNormalEnergyHatchList());
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : mExoticEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(-mEUPerTick)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(
                    GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + GT_Utility
                    .formatNumbers(GT_ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList()))
                + "A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility
                    .getTier(GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))]
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.PA.machinetier") + ": "
                + EnumChatFormatting.GREEN
                + tTier
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.PA.discount")
                + ": "
                + EnumChatFormatting.GREEN
                + 1
                + EnumChatFormatting.RESET
                + " x",
            StatCollector.translateToLocal("GT5U.PA.parallel") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers((mInventory[1] != null) ? (mInventory[1].stackSize << mMult) : 0)
                + EnumChatFormatting.RESET };
    }

    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> tHatches = new ArrayList<>();
        tHatches.addAll(mExoticEnergyHatches);
        tHatches.addAll(mEnergyHatches);
        return tHatches;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUPerTick < 0) {
            if (!drainEnergyInput(-mEUPerTick)) {
                mEUPerTick = 0;
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

    protected void ProcessingArrayCalculateOverclock(long aEUt, int aDuration, int mAmperage, long maxInputVoltage,
        boolean perfectOC) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage));
        if (mTier == 0) {
            // Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                // make impossible if too long
                mEUPerTick = Long.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUPerTick = aEUt >> 2;
                mMaxProgresstime = (int) xMaxProgresstime;
            }
        } else {
            // Long EUt calculation
            long xEUt = aEUt;
            // Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            mMaxProgresstime = aDuration;

            final int ocTimeShift = perfectOC ? 2 : 1;

            while (tempEUt <= V[mTier - 1] * mAmperage) {
                tempEUt <<= 2; // this actually controls overclocking
                // xEUt *= 4;//this is effect of everclocking
                int oldTime = mMaxProgresstime;
                mMaxProgresstime >>= ocTimeShift; // this is effect of overclocking
                if (mMaxProgresstime < 1) {
                    if (oldTime == 1) break;
                    xEUt *= oldTime * (perfectOC ? 1 : 2);
                    break;
                } else {
                    xEUt <<= 2;
                }
            }
            if (xEUt > Long.MAX_VALUE - 1) {
                mEUPerTick = Long.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUPerTick = xEUt;
                if (mEUPerTick == 0) mEUPerTick = 1;
                if (mMaxProgresstime == 0) mMaxProgresstime = 1; // set time to 1 tick
            }
        }
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> downtierUEV = !downtierUEV)
                .setPlayClickSound(true)
                .setBackground(() -> {
                    if (downtierUEV) {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                            GT_UITextures.OVERLAY_BUTTON_DOWN_TIERING_ON };
                    } else {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                            GT_UITextures.OVERLAY_BUTTON_DOWN_TIERING_OFF };
                    }
                })
                .setPos(80, 91)
                .setSize(16, 16)
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.down_tier"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> downtierUEV, val -> downtierUEV = val));
    }
}
