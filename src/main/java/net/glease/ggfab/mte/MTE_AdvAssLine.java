package net.glease.ggfab.mte;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_AssemblyLine;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.glease.ggfab.GGConstants;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static net.glease.ggfab.BlockIcons.*;

/*
Dev note:
1. This multi will be an assline but with greater throughput. it will take one input every
2.
 */
public class MTE_AdvAssLine extends GT_MetaTileEntity_AssemblyLine {
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    public static final String TAG_KEY_CURRENT_STICK = "mCurrentStick";
    public static final String TAG_KEY_PROGRESS_TIMES = "mProgressTimeArray";
    private static final IStructureDefinition<GT_MetaTileEntity_AssemblyLine> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_AssemblyLine>builder()
                    .addShape(STRUCTURE_PIECE_FIRST, transpose(new String[][] {
                            {" ", "e", " "},
                            {"~", "l", "G"},
                            {"g", "m", "g"},
                            {"b", "i", "b"},
                    }))
                    .addShape(STRUCTURE_PIECE_LATER, transpose(new String[][] {
                            {" ", "e", " "},
                            {"d", "l", "d"},
                            {"g", "m", "g"},
                            {"b", "I", "b"},
                    }))
                    .addShape(STRUCTURE_PIECE_LAST, transpose(new String[][] {
                            {" ", "e", " "},
                            {"d", "l", "d"},
                            {"g", "m", "g"},
                            {"o", "i", "b"},
                    }))
                    .addElement('G', ofBlock(GregTech_API.sBlockCasings3, 10)) // grate machine casing
                    .addElement('l', ofBlock(GregTech_API.sBlockCasings2, 9)) // assembler machine casing
                    .addElement('m', ofBlock(GregTech_API.sBlockCasings2, 5)) // assembling line casing
                    .addElement(
                            'g',
                            ofChain(
                                    ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 0, true),
                                    // warded glass
                                    ofBlockUnlocalizedName("Thaumcraft", "blockCosmeticOpaque", 2, false)))
                    .addElement(
                            'e',
                            ofChain(
                                    Energy.or(ExoticEnergy).newAny(16, 1, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH),
                                    ofBlock(GregTech_API.sBlockCasings2, 0)))
                    .addElement(
                            'd',
                            buildHatchAdder(GT_MetaTileEntity_AssemblyLine.class)
                                    .atLeast(DataHatchElement.DataAccess)
                                    .dot(2)
                                    .casingIndex(42)
                                    .allowOnly(ForgeDirection.NORTH)
                                    .buildAndChain(GregTech_API.sBlockCasings3, 10))
                    .addElement(
                            'b',
                            buildHatchAdder(GT_MetaTileEntity_AssemblyLine.class)
                                    .atLeast(InputHatch, InputHatch, InputHatch, InputHatch, Maintenance)
                                    .casingIndex(16)
                                    .dot(3)
                                    .allowOnly(ForgeDirection.DOWN)
                                    .buildAndChain(
                                            ofBlock(GregTech_API.sBlockCasings2, 0),
                                            ofHatchAdder(
                                                    GT_MetaTileEntity_AssemblyLine::addOutputToMachineList, 16, 4)))
                    .addElement(
                            'I',
                            ofChain(
                                    // all blocks nearby use solid steel casing, so let's use the texture of that
                                    InputBus.newAny(16, 5, ForgeDirection.DOWN),
                                    ofHatchAdder(GT_MetaTileEntity_AssemblyLine::addOutputToMachineList, 16, 4)))
                    .addElement('i', InputBus.newAny(16, 5, ForgeDirection.DOWN))
                    .addElement('o', OutputBus.newAny(16, 4, ForgeDirection.DOWN))
                    .build();
    private ItemStack currentStick;
    private GT_Recipe.GT_Recipe_AssemblyLine currentRecipe;
    private final Slice[] slices = IntStream.range(0, 16).mapToObj(Slice::new).toArray(Slice[]::new);
    private boolean processing;
    private long inputVoltage;
    private long baseEUt;
    private boolean stuck;

    public MTE_AdvAssLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTE_AdvAssLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTE_AdvAssLine(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (stuck) {
                return new ITexture[]{
                        casingTexturePages[0][16],
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_ADV_ASSLINE_STUCK)
                                .extFacing()
                                .build(),
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_ADV_ASSLINE_STUCK_GLOW)
                                .extFacing()
                                .glow()
                                .build()
                };
            }
            if (aActive)
                return new ITexture[]{
                        casingTexturePages[0][16],
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_ADV_ASSLINE_ACTIVE)
                                .extFacing()
                                .build(),
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_ADV_ASSLINE_ACTIVE_GLOW)
                                .extFacing()
                                .glow()
                                .build()
                };
            return new ITexture[]{
                    casingTexturePages[0][16],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ADV_ASSLINE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ADV_ASSLINE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
            };
        }
        return new ITexture[]{casingTexturePages[0][16]};
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Assembling Line")
                .addInfo("Controller block for the Advanced Assembling Line")
                .addInfo("Built exactly the same as standard Assembling Line")
                .addInfo("Assembling Line with item pipelining")
                .addInfo("All fluids are however consumed at start")
                .addInfo("Use voltage of worst energy hatch for overclocking")
                .addInfo("EU/t is (number of slices working) * (overclocked EU/t)")
                .addSeparator()
                .beginVariableStructureBlock(5, 16, 4, 4, 3, 3, false)
                .addStructureInfo("From Bottom to Top, Left to Right")
                .addStructureInfo(
                        "Layer 1 - Solid Steel Machine Casing, Input Bus (last can be Output Bus), Solid Steel Machine Casing")
                .addStructureInfo(
                        "Layer 2 - Borosilicate Glass(any)/Warded Glass/Reinforced Glass, Assembling Line Casing, Reinforced Glass")
                .addStructureInfo("Layer 3 - Grate Machine Casing, Assembler Machine Casing, Grate Machine Casing")
                .addStructureInfo("Layer 4 - Empty, Solid Steel Machine Casing, Empty")
                .addStructureInfo("Up to 16 repeating slices, each one allows for 1 more item in recipes")
                .addController("Either Grate on layer 3 of the first slice")
                .addEnergyHatch("Any layer 4 casing", 1)
                .addMaintenanceHatch("Any layer 1 casing", 3)
                .addInputBus("As specified on layer 1", 4, 5)
                .addInputHatch("Any layer 1 casing", 3)
                .addOutputBus("Replaces Input Bus on final slice or on any solid steel casing on layer 1", 4)
                .addOtherStructurePart("Data Access Hatch", "Optional, next to controller", 2)
                .toolTipFinisher(GGConstants.GGMARK);
        return tt;
    }

    private void setCurrentRecipe(ItemStack stick, GT_Recipe.GT_Recipe_AssemblyLine recipe) {
        currentRecipe = recipe;
        currentStick = stick;
    }

    private void clearCurrentRecipe() {
        currentRecipe = null;
        currentStick = null;
        for (Slice slice : slices) {
            slice.reset();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        // we need to check for active here.
        // if machine was turned off via soft mallet it will not call checkRecipe() on recipe end
        // in that case we don't have a current recipe, so this should be ignored
        if (getBaseMetaTileEntity().isActive() && GT_Utility.isStackValid(currentStick)) {
            aNBT.setTag(TAG_KEY_CURRENT_STICK, currentStick.writeToNBT(new NBTTagCompound()));
            aNBT.setInteger("mRecipeHash", currentRecipe.getPersistentHash());
            aNBT.setIntArray(TAG_KEY_PROGRESS_TIMES, Arrays.stream(slices).limit(currentRecipe.mInputs.length).mapToInt(s -> s.progress).toArray());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        ItemStack loadedStack = null;
        GT_Recipe.GT_Recipe_AssemblyLine recipe = null;
        if (aNBT.hasKey(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_INT_ARRAY)) {
            int[] arr = aNBT.getIntArray(TAG_KEY_PROGRESS_TIMES);
            for (int i = 0; i < slices.length; i++) {
                if (i < arr.length) {
                    slices[i].progress = arr[i];
                    if (arr[i] == 0)
                        // this will be synced to client by first MTE packet to client
                        stuck = true;
                } else
                    slices[i].reset();
            }
        }
        if (aNBT.hasKey(TAG_KEY_CURRENT_STICK, Constants.NBT.TAG_COMPOUND)) {
            loadedStack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(TAG_KEY_CURRENT_STICK));
            GT_AssemblyLineUtils.LookupResult lookupResult = GT_AssemblyLineUtils.findAssemblyLineRecipeFromDataStick(loadedStack, true);
            switch (lookupResult.getType()) {
                case VALID_STACK_AND_VALID_HASH:
                    recipe = lookupResult.getRecipe();
                    break;
                case VALID_STACK_AND_VALID_RECIPE:
                    // recipe is there, but it has been changed. to prevent issues, abort the current recipe
                    // TODO finish the last recipe instead of aborting
                default:
                    // recipe is gone. to prevent issues, abort the current recipe
                    criticalStopMachine();
                    loadedStack = null;
                    break;
            }
        }
        setCurrentRecipe(loadedStack, recipe);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_AssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (super.checkMachine(aBaseMetaTileEntity, aStack)) {
            if (mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty())
                return false;
            inputVoltage = Integer.MAX_VALUE;
            for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches) {
                inputVoltage = Math.min(inputVoltage, tHatch.maxEUInput());
            }
            for (GT_MetaTileEntity_Hatch tHatch : mExoticEnergyHatches) {
                inputVoltage = Math.min(inputVoltage, tHatch.maxEUInput());
            }
            return true;
        } else {
            inputVoltage = V[0];
            return false;
        }
    }

    @Override
    protected void startRecipeProcessing() {
        if (!processing)
            super.startRecipeProcessing();
    }

    @Override
    protected void endRecipeProcessing() {
        if (!processing)
            return;
        super.endRecipeProcessing();
        processing = false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        stuck = (aValue & 1) == 1;
    }

    @Override
    public byte getUpdateData() {
        return (byte) (stuck ? 1 : 0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (currentRecipe == null) {
            criticalStopMachine();
            return false;
        }
        if (!super.onRunningTick(aStack))
            return false;

        if (mInputBusses.size() <= currentRecipe.mInputs.length) {
            criticalStopMachine();
            return false;
        }
        boolean oStuck = stuck;
        stuck = false;

        for (int i = slices.length - 1; i >= 0; i--) {
            slices[i].tick();
        }

        if (oStuck != stuck)
            // send the status as it has changed
            getBaseMetaTileEntity().issueClientUpdate();

        boolean foundWorking = false;
        int tEUt = 0;
        for (Slice slice : slices) {
            if (slice.progress >= 0) {
                if (!foundWorking) {
                    foundWorking = true;
                    mProgresstime = (slice.id + 1) * (mMaxProgresstime / currentRecipe.mInputs.length) - slice.progress;
                }
            }
            if (slice.progress > 0)
                tEUt += baseEUt;
        }
        mEUt = tEUt;

        if (slices[0].canStart() && getBaseMetaTileEntity().isAllowedToWork()) {
            if (hasAllFluids(currentRecipe)) {
                drainAllFluids(currentRecipe);
                slices[0].start();
                mProgresstime = 0;
            }
        }

        endRecipeProcessing();
        return true;
    }

    private GT_Recipe.GT_Recipe_AssemblyLine findRecipe(ItemStack tDataStick) {
        GT_AssemblyLineUtils.LookupResult tLookupResult =
                GT_AssemblyLineUtils.findAssemblyLineRecipeFromDataStick(tDataStick, false);

        if (tLookupResult.getType() == GT_AssemblyLineUtils.LookupResultType.INVALID_STICK) return null;

        GT_Recipe.GT_Recipe_AssemblyLine tRecipe = tLookupResult.getRecipe();
        // Check if the recipe on the data stick is the current recipe for it's given output, if not we update it
        // and continue to next.
        if (tLookupResult.getType() != GT_AssemblyLineUtils.LookupResultType.VALID_STACK_AND_VALID_HASH) {
            tRecipe = GT_AssemblyLineUtils.processDataStick(tDataStick);
            if (tRecipe == null) {
                return null;
            }
        }

        // So here we check against the recipe found on the data stick.
        // If we run into missing buses/hatches or bad inputs, we go to the next data stick.
        // This check only happens if we have a valid up-to-date data stick.

        // Check Inputs allign
        int aItemCount = tRecipe.mInputs.length;
        for (int i = 0; i < aItemCount; i++) {
            GT_MetaTileEntity_Hatch_InputBus tInputBus = mInputBusses.get(i);
            if (tInputBus == null) {
                return null;
            }
            ItemStack tSlotStack = tInputBus.getStackInSlot(0);
            int tRequiredStackSize = isStackValidIngredient(tSlotStack, tRecipe.mInputs[i], tRecipe.mOreDictAlt[i]);
            if (tRequiredStackSize < 0) return null;

            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Item: " + i + " accepted");
            }
        }

        // Check Fluid Inputs allign
        if (!hasAllFluids(tRecipe))
            return null;

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Check overclock");
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Find available recipe");
        }
        return tRecipe;
    }

    private boolean hasAllFluids(GT_Recipe.GT_Recipe_AssemblyLine tRecipe) {
        int aFluidCount = tRecipe.mFluidInputs.length;
        for (int i = 0; i < aFluidCount; i++) {
            GT_MetaTileEntity_Hatch_Input tInputHatch = mInputHatches.get(i);
            if (!isValidMetaTileEntity(tInputHatch)) {
                return false;
            }
            FluidStack drained = tInputHatch.drain(ForgeDirection.UNKNOWN, tRecipe.mFluidInputs[i], false);
            if (drained == null || drained.amount < tRecipe.mFluidInputs[i].amount) {
                return false;
            }
            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Fluid:" + i + " accepted");
            }
        }
        return true;
    }

    // this is only called when all slices have finished their work
    // and the first slice cannot find a input/fluid cannot be found
    // so we are safe to assume the old recipe no longer works
    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Start ALine recipe check");
        }
        clearCurrentRecipe();
        ArrayList<ItemStack> tDataStickList = getDataItems(2);
        if (tDataStickList.isEmpty()) {
            return false;
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Stick accepted, " + tDataStickList.size() + " Data Sticks found");
        }

        GT_Recipe.GT_Recipe_AssemblyLine recipe = null;
        for (ItemStack stack : tDataStickList) {
            recipe = findRecipe(stack);
            if (recipe != null) {
                setCurrentRecipe(stack, recipe);
                calculateOverclockedNessMulti(currentRecipe.mEUt, Math.max(recipe.mDuration / recipe.mInputs.length, 1), 1, inputVoltage);
                // correct the recipe duration
                mMaxProgresstime *= recipe.mInputs.length;
                // In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) {
                    if (GT_Values.D1) {
                        GT_FML_LOGGER.info("Recipe too OP");
                    }
                    continue;
                }
                break;
            }
        }

        if (recipe == null) {
            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Did not find a recipe");
            }
            return false;
        }

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("All checked start consuming inputs");
        }

        if (!slices[0].start()) {
            clearCurrentRecipe();
            // something very very wrong...
            return false;
        }
        drainAllFluids(recipe);

        mOutputItems = new ItemStack[]{recipe.mOutput};

        if (this.mEUt > 0) {
            this.mEUt = -this.mEUt;
        }
        baseEUt = mEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        updateSlots();

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Recipe successful");
        }
        return true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        String machineProgressString = GT_Waila.getMachineProgressString(
                tag.getBoolean("isActive"), tag.getInteger("maxProgress"), tag.getInteger("progress"));
        currentTip.remove(machineProgressString);

        int duration = tag.getInteger("mDuration");
        if (tag.hasKey(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_LIST)) {
            NBTTagList tl = tag.getTagList(TAG_KEY_PROGRESS_TIMES, Constants.NBT.TAG_INT);
            @SuppressWarnings("unchecked") List<NBTTagInt> list = tl.tagList;
            for (int i = 0, listSize = list.size(); i < listSize; i++) {
                NBTTagInt t = list.get(i);
                int progress = t.func_150287_d();
                if (progress > 20) {
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice", i, progress / 20, duration / 20));
                } else if (progress > 0) {
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice.small", i, progress));
                } else if (progress == 0) {
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice.stuck", i));
                } else
                    currentTip.add(I18n.format("ggfab.waila.advassline.slice.idle", i));
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (currentRecipe == null || !getBaseMetaTileEntity().isActive()) return;
        NBTTagList l = new NBTTagList();
        for (int i = 0; i < currentRecipe.mInputs.length; i++) {
            l.appendTag(new NBTTagInt(slices[i].progress));
        }
        tag.setTag(TAG_KEY_PROGRESS_TIMES, l);
        tag.setInteger("mDuration", currentRecipe.mDuration / currentRecipe.mInputs.length);
    }

    private void drainAllFluids(GT_Recipe.GT_Recipe_AssemblyLine recipe) {
        for (int i = 0; i < recipe.mFluidInputs.length; i++) {
            depleteInput(recipe.mFluidInputs[i]);
        }
    }

    @Override
    public void stopMachine() {
        for (Slice slice : slices) {
            slice.reset();
        }
        super.stopMachine();
    }

    private static int isStackValidIngredient(ItemStack aSlotStack, ItemStack aIngredient, ItemStack[] alts) {
        if (alts == null || alts.length == 0) return isStackValidIngredient(aSlotStack, aIngredient);
        for (ItemStack tAltStack : alts) {
            int i = isStackValidIngredient(aSlotStack, tAltStack);
            if (i >= 0) return i;
        }
        return -1;
    }

    private static int isStackValidIngredient(ItemStack aSlotStack, ItemStack aIngredient) {
        if (GT_Utility.areStacksEqual(aSlotStack, aIngredient, true) && aIngredient.stackSize <= aSlotStack.stackSize)
            return aIngredient.stackSize;
        return -1;
    }

    private class Slice {
        private final int id;
        private int progress = -1;

        public Slice(int id) {
            this.id = id;
        }

        public void reset() {
            progress = -1;
        }

        public void tick() {
            if (progress < 0)
                return;
            if (progress == 0 || --progress == 0) {
                // id==0 will be end of chain if 1 input, so we need a +1 here
                if (id + 1 >= currentRecipe.mInputs.length) {
                    addOutput(currentRecipe.mOutput);
                    reset();
                } else {
                    if (slices[id + 1].start())
                        reset();
                    else
                        stuck = true;
                }
            }
        }

        public boolean start() {
            if (progress >= 0)
                return false;
            startRecipeProcessing();
            GT_MetaTileEntity_Hatch_InputBus bus = mInputBusses.get(id);
            ItemStack stack = bus.getStackInSlot(0);
            int size = isStackValidIngredient(stack, currentRecipe.mInputs[id], currentRecipe.mOreDictAlt[id]);
            if (size < 0)
                return false;
            progress = mMaxProgresstime / currentRecipe.mInputs.length;
            stack.stackSize -= size;
            bus.updateSlots();
            return true;
        }

        public boolean canStart() {
            if (progress >= 0) {
                return false;
            }
            return hasInput();
        }

        public boolean hasInput() {
            ItemStack stack = mInputBusses.get(id).getStackInSlot(0);
            return isStackValidIngredient(stack, currentRecipe.mInputs[id], currentRecipe.mOreDictAlt[id]) >= 0;
        }

        @Override
        public String toString() {
            return "Slice{" +
                    "id=" + id +
                    ", progress=" + progress +
                    '}';
        }
    }

    private enum DataHatchElement implements IHatchElement<GT_MetaTileEntity_AssemblyLine> {
        DataAccess;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_DataAccess.class);
        }

        @Override
        public IGT_HatchAdder<GT_MetaTileEntity_AssemblyLine> adder() {
            return GT_MetaTileEntity_AssemblyLine::addDataAccessToMachineList;
        }

        @Override
        public long count(GT_MetaTileEntity_AssemblyLine t) {
            return t.mDataAccessHatches.size();
        }
    }
}
