package gregtech.api.multitileentity.machine;

import static com.google.common.primitives.Ints.saturatedCast;
import static gregtech.api.enums.GT_Values.B;
import static gregtech.api.enums.GT_Values.emptyIconContainerArray;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TickTime;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.PollutionLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.PollutionLogicHost;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.TickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileMachine;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.client.GT_SoundLoop;
import gregtech.common.GT_Pollution;

public abstract class MultiTileBasicMachine extends TickableMultiTileEntity implements IMultiTileMachine {

    protected static final int ACTIVE = B[0];
    protected static final int TICKS_BETWEEN_RECIPE_CHECKS = 5 * TickTime.SECOND;
    protected static final int POLLUTION_TICK = TickTime.SECOND;
    protected static final byte INTERRUPT_SOUND_INDEX = 8;
    protected static final byte PROCESS_START_SOUND_INDEX = 1;

    protected static final IItemHandlerModifiable EMPTY_INVENTORY = new ItemStackHandler(0);

    private static final String TEXTURE_LOCATION = "multitileentity/machines/";
    public IIconContainer[] texturesInactive = emptyIconContainerArray;
    public IIconContainer[] texturesActive = emptyIconContainerArray;

    protected int maxParallel = 1;
    protected boolean active = false;
    protected long storedEnergy = 0;
    protected long voltage = 0;
    protected long amperage = 2;
    protected long eut = 0;
    protected int tier = 0;
    protected long maxProgressTime = 0;
    protected long progressTime = 0;
    protected long burnTime = 0;
    protected long totalBurnTime = 0;
    protected FluidTankGT[] inputTanks = GT_Values.emptyFluidTankGT;
    protected FluidTankGT[] outputTanks = GT_Values.emptyFluidTankGT;
    protected FluidStack[] fluidsToOutput = GT_Values.emptyFluidStack;
    protected ItemStack[] itemsToOutput = GT_Values.emptyItemStackArray;

    protected IItemHandlerModifiable inputInventory = EMPTY_INVENTORY;
    protected IItemHandlerModifiable outputInventory = EMPTY_INVENTORY;
    protected boolean outputInventoryChanged = false;
    private boolean powerShutDown = false;
    private boolean wasEnabled = false;
    private boolean canWork = true;
    private boolean isElectric = true;
    private boolean isSteam = false;
    private boolean acceptsFuel = false;
    private boolean isWireless = false;
    private byte soundEvent = 0;
    private int soundEventValue = 0;

    @SideOnly(Side.CLIENT)
    protected GT_SoundLoop activitySoundLoop;

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.machine.basic";
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        super.writeMultiTileNBT(nbt);
        if (maxParallel > 0) {
            nbt.setInteger(NBT.PARALLEL, maxParallel);
        }

        if (active) {
            nbt.setBoolean(NBT.ACTIVE, active);
        }

        if (inputInventory != null && inputInventory.getSlots() > 0) {
            writeInventory(nbt, inputInventory, NBT.INV_INPUT_LIST);
        }

        if (outputInventory != null && outputInventory.getSlots() > 0) {
            writeInventory(nbt, outputInventory, NBT.INV_OUTPUT_LIST);
        }

        for (int i = 0; i < inputTanks.length; i++) {
            inputTanks[i].writeToNBT(nbt, NBT.TANK_IN + i);
        }

        for (int i = 0; i < outputTanks.length; i++) {
            outputTanks[i].writeToNBT(nbt, NBT.TANK_OUT + i);
        }

        if (fluidsToOutput != null && fluidsToOutput.length > 0) {
            writeFluids(nbt, fluidsToOutput, NBT.FLUID_OUT);
        }

        if (itemsToOutput != null) {
            saveItemsToOutput(nbt);
        }

        nbt.setInteger(NBT.TIER, tier);
        nbt.setLong(NBT.EUT_CONSUMPTION, eut);
        nbt.setLong(NBT.BURN_TIME_LEFT, burnTime);
        nbt.setLong(NBT.TOTAL_BURN_TIME, totalBurnTime);
        nbt.setBoolean(NBT.ALLOWED_WORK, canWork);
        nbt.setBoolean(NBT.ACTIVE, active);
    }

    protected void writeFluids(NBTTagCompound nbt, FluidStack[] fluids, String fluidListTag) {
        if (fluids != null && fluids.length > 0) {
            final NBTTagList tList = new NBTTagList();
            for (final FluidStack tFluid : fluids) {
                if (tFluid != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tFluid.writeToNBT(tag);
                    tList.appendTag(tag);
                }
            }
            nbt.setTag(fluidListTag, tList);
        }
    }

    protected void writeInventory(NBTTagCompound nbt, IItemHandlerModifiable inv, String invListTag) {
        if (inv != null && inv.getSlots() > 0) {
            final NBTTagList tList = new NBTTagList();
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                final ItemStack tStack = inv.getStackInSlot(slot);
                if (tStack != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("s", (byte) slot);
                    tStack.writeToNBT(tag);
                    tList.appendTag(tag);
                }
            }
            nbt.setTag(invListTag, tList);
        }
    }

    protected void saveItemsToOutput(NBTTagCompound aNBT) {
        final NBTTagList nbtList = new NBTTagList();
        for (int slot = 0; slot < itemsToOutput.length; slot++) {
            final ItemStack itemStack = itemsToOutput[slot];
            if (itemStack != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("s", (byte) slot);
                itemStack.writeToNBT(tag);
                nbtList.appendTag(tag);
            }
        }
        aNBT.setTag(NBT.ITEM_OUT, nbtList);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);
        if (nbt.hasKey(NBT.PARALLEL)) {
            maxParallel = Math.max(1, nbt.getInteger(NBT.PARALLEL));
        }

        if (nbt.hasKey(NBT.ACTIVE)) {
            active = nbt.getBoolean(NBT.ACTIVE);
        }

        /* Inventories */
        inputInventory = new ItemStackHandler(Math.max(nbt.getInteger(NBT.INV_INPUT_SIZE), 0));
        outputInventory = new ItemStackHandler(Math.max(nbt.getInteger(NBT.INV_OUTPUT_SIZE), 0));
        loadInventory(nbt, inputInventory, NBT.INV_INPUT_LIST);
        loadInventory(nbt, outputInventory, NBT.INV_OUTPUT_LIST);

        /* Tanks */
        long capacity = 1000;
        if (nbt.hasKey(NBT.TANK_CAPACITY)) {
            capacity = saturatedCast(nbt.getLong(NBT.TANK_CAPACITY));
        }

        inputTanks = new FluidTankGT[getFluidInputCount()];
        outputTanks = new FluidTankGT[getFluidOutputCount()];
        fluidsToOutput = new FluidStack[getFluidOutputCount()];

        // TODO: See if we need the adjustable map here `.setCapacity(mRecipes, mParallel * 2L)` in place of the
        // `setCapacityMultiplier`
        for (int i = 0; i < inputTanks.length; i++) {
            inputTanks[i] = new FluidTankGT(capacity).setCapacityMultiplier(maxParallel * 2L)
                .readFromNBT(nbt, NBT.TANK_IN + i);
        }
        for (int i = 0; i < outputTanks.length; i++) {
            outputTanks[i] = new FluidTankGT().readFromNBT(nbt, NBT.TANK_OUT + i);
        }

        for (int i = 0; i < fluidsToOutput.length; i++) {
            fluidsToOutput[i] = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(NBT.FLUID_OUT + "." + i));
        }

        loadItemsToOutput(nbt);

        tier = nbt.getInteger(NBT.TIER);
        eut = nbt.getLong(NBT.EUT_CONSUMPTION);
        burnTime = nbt.getLong(NBT.BURN_TIME_LEFT);
        totalBurnTime = nbt.getLong(NBT.TOTAL_BURN_TIME);
        canWork = nbt.getBoolean(NBT.ALLOWED_WORK);
        active = nbt.getBoolean(NBT.ACTIVE);
    }

    protected void loadInventory(NBTTagCompound aNBT, IItemHandlerModifiable inv, String invListTag) {
        final NBTTagList tList = aNBT.getTagList(invListTag, 10);
        for (int i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final int tSlot = tNBT.getShort("s");
            if (tSlot >= 0 && tSlot < inv.getSlots()) inv.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
        }
    }

    protected void loadItemsToOutput(NBTTagCompound aNBT) {
        final NBTTagList tList = aNBT.getTagList(NBT.ITEM_OUT, 10);
        itemsToOutput = new ItemStack[tList.tagCount()];
        for (int i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final int tSlot = tNBT.getByte("s");
            if (tSlot >= 0 && tSlot < itemsToOutput.length) itemsToOutput[tSlot] = GT_Utility.loadItem(tNBT);
        }
    }

    @Override
    public void loadTextureNBT(NBTTagCompound aNBT) {
        // Loading the registry
        final String textureName = aNBT.getString(NBT.TEXTURE);
        textures = new IIconContainer[] {
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/bottom"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/top"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/left"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/front"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/right"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/side") };
        texturesInactive = new IIconContainer[] {
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/bottom"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/top"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/left"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/front"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/right"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/back") };
        texturesActive = new IIconContainer[] {
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/bottom"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/top"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/left"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/front"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/right"),
            new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/back") };
    }

    @Override
    public void copyTextures() {
        // Loading an instance
        final TileEntity tCanonicalTileEntity = MultiTileEntityRegistry
            .getCanonicalTileEntity(getMultiTileEntityRegistryID(), getMultiTileEntityID());
        if (tCanonicalTileEntity instanceof MultiTileBasicMachine) {
            textures = ((MultiTileBasicMachine) tCanonicalTileEntity).textures;
            texturesInactive = ((MultiTileBasicMachine) tCanonicalTileEntity).texturesInactive;
            texturesActive = ((MultiTileBasicMachine) tCanonicalTileEntity).texturesActive;
        } else {
            textures = texturesInactive = texturesActive = emptyIconContainerArray;
        }
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        if (aSide != facing) {
            return new ITexture[] {
                TextureFactory.of(textures[GT_Values.FACING_ROTATIONS[facing][aSide]], GT_Util.getRGBaArray(rgba)) };
        }
        return new ITexture[] {
            TextureFactory.of(textures[GT_Values.FACING_ROTATIONS[facing][aSide]], GT_Util.getRGBaArray(rgba)),
            TextureFactory
                .of((active ? texturesActive : texturesInactive)[GT_Values.FACING_ROTATIONS[facing][aSide]]) };
    }

    @Override
    public GT_Packet_MultiTileEntity getClientDataPacket() {
        final GT_Packet_MultiTileEntity packet = super.getClientDataPacket();
        int booleans = getBooleans();
        packet.setBooleans(booleans);
        packet.setSoundEvent(soundEvent, soundEventValue);
        return packet;
    }

    /*
     * Fluids
     */

    /**
     * The number of fluid (input) slots available for this machine
     */
    public int getFluidInputCount() {
        return 7;
    }

    /**
     * The number of fluid (output) slots available for this machine
     */
    public int getFluidOutputCount() {
        return 3;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public String getInventoryName() {
        final String name = getCustomName();
        if (name != null) return name;
        final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID());
        return tRegistry == null ? getClass().getName() : tRegistry.getLocal(getMultiTileEntityID());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return playerOwnsThis(aPlayer, false) && mTickTimer > 40
            && getTileEntityOffset(0, 0, 0) == this
            && aPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64
            && allowInteraction(aPlayer);
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return aSide != facing;
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
        return aSide != facing;
    }

    @Override
    protected IFluidTank[] getFluidTanks(byte aSide) {
        final boolean fluidInput = isLiquidInput(aSide);
        final boolean fluidOutput = isLiquidOutput(aSide);

        if (fluidInput && fluidOutput) {
            final IFluidTank[] rTanks = new IFluidTank[inputTanks.length + outputTanks.length];
            System.arraycopy(inputTanks, 0, rTanks, 0, inputTanks.length);
            System.arraycopy(outputTanks, 0, rTanks, inputTanks.length, outputTanks.length);
            return rTanks;
        } else if (fluidInput) {
            return inputTanks;
        } else if (fluidOutput) {
            return outputTanks;
        }
        return GT_Values.emptyFluidTank;
    }

    @Override
    public IFluidTank getFluidTankFillable(byte aSide, FluidStack aFluidToFill) {
        if (!isLiquidInput(aSide)) return null;
        for (FluidTankGT tankGT : inputTanks) if (tankGT.contains(aFluidToFill)) return tankGT;
        // if (!mRecipes.containsInput(aFluidToFill, this, slot(mRecipes.mInputItemsCount +
        // mRecipes.mOutputItemsCount))) return null;
        for (FluidTankGT fluidTankGT : inputTanks) if (fluidTankGT.isEmpty()) return fluidTankGT;
        return null;
    }

    @Override
    protected IFluidTank getFluidTankDrainable(byte aSide, FluidStack aFluidToDrain) {
        if (!isLiquidOutput(aSide)) return null;
        for (FluidTankGT fluidTankGT : outputTanks)
            if (aFluidToDrain == null ? fluidTankGT.has() : fluidTankGT.contains(aFluidToDrain)) return fluidTankGT;

        return null;
    }

    /*
     * Inventory
     */

    @Override
    public boolean hasInventoryBeenModified() {
        // True if the input inventory has changed
        return hasInventoryChanged;
    }

    public void markOutputInventoryBeenModified() {
        outputInventoryChanged = true;
    }

    public boolean hasOutputInventoryBeenModified() {
        // True if the output inventory has changed
        return outputInventoryChanged;
    }

    public void markInputInventoryBeenModified() {
        hasInventoryChanged = true;
    }

    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    // #region Machine

    @Override
    public void onPostTick(long tick, boolean isServerSide) {
        if (isServerSide) {
            runMachine(tick);
        } else {
            doActivitySound(getActivitySoundLoop());
        }
    }

    /**
     * Runs only on server side
     *
     * @param tick The current tick of the machine
     */
    protected void runMachine(long tick) {
        if (acceptsFuel() && isActive()) {
            if (!consumeFuel()) {
                stopMachine();
                return;
            }
        }

        if (hasThingsToDo()) {
            markDirty();
            runningTick(tick);
        } else {
            if (tick % TICKS_BETWEEN_RECIPE_CHECKS == 0 || hasWorkJustBeenEnabled() || hasInventoryBeenModified()) {
                if (isAllowedToWork()) {
                    wasEnabled = false;
                    if (checkRecipe()) {
                        setActive(true);
                        setSound(GregTechTileClientEvents.START_SOUND_LOOP, PROCESS_START_SOUND_INDEX);
                        updateSlots();
                        markDirty();
                        issueClientUpdate();
                    }
                }
            }
        }
    }

    /**
     * Runs only on server side
     *
     * @param tick The current tick of the machine
     */
    protected void runningTick(long tick) {
        if (this instanceof PowerLogicHost) {
            consumeEnergy();
        }

        if (maxProgressTime > 0 && ++progressTime >= maxProgressTime) {
            progressTime = 0;
            maxProgressTime = 0;
            outputItems();
            outputFluids();
            if (isAllowedToWork()) {
                if (!checkRecipe()) {
                    setActive(false);
                    issueClientUpdate();
                }
            }
            updateSlots();
        }

        if (this instanceof PollutionLogicHost && tick % POLLUTION_TICK == 0) {
            doPollution();
        }
        emitEnergy();
    }

    /**
     * Runs only on server side
     */
    protected boolean checkRecipe() {
        if (!(this instanceof ProcessingLogicHost)) {
            return false;
        }
        ProcessingLogic logic = ((ProcessingLogicHost) this).getProcessingLogic();
        logic.clear();
        boolean result = logic.setInputItems(
            inputInventory.getStacks()
                .toArray(new ItemStack[0]))
            .setCurrentOutputItems(
                outputInventory.getStacks()
                    .toArray(new ItemStack[0]))
            .process();
        setDuration(logic.getDuration());
        setEut(logic.getEut());
        setItemOutputs(logic.getOutputItems());
        setFluidOutputs(logic.getOutputFluids());
        return result;
    }

    /**
     * Runs only on server side
     */
    protected void doPollution() {
        PollutionLogic logic = ((PollutionLogicHost) this).getPollutionLogic();

        if (logic == null) {
            return;
        }

        GT_Pollution.addPollution(getWorld(), getXCoord() >> 4, getZCoord() >> 4, logic.getPollutionAmount());
    }

    /**
     * Runs only on server side
     */
    protected void emitEnergy() {}

    /**
     * Runs only on server side
     */
    protected void consumeEnergy() {
        PowerLogic logic = ((PowerLogicHost) this).getPowerLogic(GT_Values.SIDE_UNKNOWN);

        if (logic == null) {
            return;
        }

        if (logic.removeEnergyUnsafe(eut)) {
            stopMachine();
        }
    }

    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        switch (aIndex) {
            case PROCESS_START_SOUND_INDEX -> {
                if (getProcessStartSound() != null)
                    GT_Utility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
            }
            case INTERRUPT_SOUND_INDEX -> GT_Utility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            if (getProcessStartSound() != null)
                GT_Utility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    protected ResourceLocation getProcessStartSound() {
        return null;
    }

    protected int getTimeBetweenProcessSounds() {
        return 100;
    }

    protected void doActivitySound(ResourceLocation activitySound) {
        if (isActive() && activitySound != null) {
            if (activitySoundLoop == null) {
                activitySoundLoop = new GT_SoundLoop(activitySound, this, false, true);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop = null;
            }
        }
    }

    protected ResourceLocation getActivitySoundLoop() {
        return null;
    }

    protected void outputItems() {
        if (itemsToOutput == null) {
            return;
        }
        for (ItemStack item : itemsToOutput) {
            int index = 0;
            while (item != null && item.stackSize > 0 && index < outputInventory.getSlots()) {
                item = outputInventory.insertItem(index++, item.copy(), false);
            }
        }
        itemsToOutput = null;
    }

    protected void outputFluids() {
        if (fluidsToOutput == null) {
            return;
        }
        for (FluidStack fluid : fluidsToOutput) {
            tryToFillTanks(fluid, outputTanks);
        }
    }

    protected void tryToFillTanks(FluidStack fluid, FluidTankGT... tanks) {
        for (FluidTankGT tank : tanks) {
            if (tank.canFillAll(fluid)) {
                tank.add(fluid.amount, fluid);
            }
        }
    }

    public long getProgress() {
        return progressTime;
    }

    public long getMaxProgress() {
        return maxProgressTime;
    }

    public boolean increaseProgress(int aProgressAmountInTicks) {
        progressTime += aProgressAmountInTicks;
        return true;
    }

    public boolean hasThingsToDo() {
        return getMaxProgress() > 0;
    }

    public boolean hasWorkJustBeenEnabled() {
        return wasEnabled;
    }

    public void enableWorking() {
        wasEnabled = true;
        canWork = true;
    }

    public void disableWorking() {
        canWork = false;
    }

    public boolean wasShutdown() {
        return powerShutDown;
    }

    public boolean isAllowedToWork() {
        return canWork;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected boolean isElectric() {
        return isElectric;
    }

    protected void setElectric(boolean isElectric) {
        this.isElectric = isElectric;
    }

    protected boolean isSteam() {
        return isSteam;
    }

    protected void setSteam(boolean isSteam) {
        this.isSteam = isSteam;
    }

    protected boolean acceptsFuel() {
        return acceptsFuel;
    }

    protected void setFuel(boolean acceptsFuel) {
        this.acceptsFuel = acceptsFuel;
    }

    protected boolean isWireless() {
        return isWireless;
    }

    protected void setWireless(boolean isWireless) {
        this.isWireless = isWireless;
    }

    protected boolean drainEut(long eut) {
        return decreaseStoredEnergyUnits(eut, false);
    }

    protected boolean generateEut(long eut) {
        return increaseStoredEnergyUnits(eut, true);
    }

    protected boolean isGenerator() {
        return false;
    }

    protected boolean consumeFuel() {
        if (isActive() && burnTime <= 0) {
            for (int i = 0; i < inputInventory.getSlots(); i++) {
                if (inputInventory.getStackInSlot(i) != null) {
                    int checkBurnTime = TileEntityFurnace.getItemBurnTime(inputInventory.getStackInSlot(i)) / 10;
                    if (checkBurnTime <= 0) continue;
                    inputInventory.getStackInSlot(i).stackSize--;
                    burnTime = checkBurnTime;
                    totalBurnTime = checkBurnTime;
                    break;
                }
            }
            updateSlots();
        }

        if (--burnTime < 0) {
            burnTime = 0;
            totalBurnTime = 0;
            return false;
        }

        return true;
    }

    @Override
    protected void addDebugInfo(EntityPlayer player, int logLevel, ArrayList<String> list) {
        if (isElectric()) {
            list.add(
                "Energy: " + EnumChatFormatting.GOLD + getUniversalEnergyStored() + "/" + getUniversalEnergyCapacity());
        }

        if (acceptsFuel()) {
            list.add("Fuel: " + EnumChatFormatting.GOLD + burnTime + "/" + totalBurnTime);
        }
    }

    protected void stopMachine() {
        progressTime = 0;
        setActive(false);
        disableWorking();
        setSound(GregTechTileClientEvents.STOP_SOUND_LOOP, INTERRUPT_SOUND_INDEX);
        issueClientUpdate();
    }

    protected void updateSlots() {
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            ItemStack item = inputInventory.getStackInSlot(i);
            if (item != null && item.stackSize <= 0) {
                inputInventory.setStackInSlot(i, null);
            }
        }
    }

    /**
     * Must always be a positive. If the multi generates Eu/t isGenerator() should be overridden to true
     */
    protected void setEut(long eut) {
        if (eut < 0) {
            eut = -eut;
        }

        this.eut = eut;
    }

    protected void setDuration(long duration) {
        if (duration < 0) {
            duration = -duration;
        }

        maxProgressTime = duration;
    }

    @Override
    public int getBooleans() {
        int booleans = 0;
        if (isActive()) {
            booleans |= ACTIVE;
        }
        return booleans;
    }

    @Override
    public void setBooleans(int booleans) {
        if ((booleans & ACTIVE) == ACTIVE) {
            setActive(true);
        }
    }

    protected boolean hasItemInput() {
        return true;
    }

    protected boolean hasItemOutput() {
        return true;
    }

    protected boolean hasFluidInput() {
        return true;
    }

    protected boolean hasFluidOutput() {
        return true;
    }

    protected void setItemOutputs(ItemStack... outputs) {
        itemsToOutput = outputs;
    }

    protected void setFluidOutputs(FluidStack... outputs) {
        fluidsToOutput = outputs;
    }

    @Override
    public void setSound(byte soundEvent, int soundEventValue) {
        this.soundEvent = soundEvent;
        this.soundEventValue = soundEventValue;
        if (isClientSide()) {
            switch (soundEventValue) {
                case PROCESS_START_SOUND_INDEX -> {
                    if (getProcessStartSound() != null) GT_Utility.doSoundAtClient(
                        getProcessStartSound(),
                        getTimeBetweenProcessSounds(),
                        1.0F,
                        getXCoord(),
                        getYCoord(),
                        getZCoord());
                }
                case INTERRUPT_SOUND_INDEX -> GT_Utility.doSoundAtClient(
                    SoundResource.IC2_MACHINES_INTERRUPT_ONE,
                    100,
                    1.0F,
                    getXCoord(),
                    getYCoord(),
                    getZCoord());
            }
        }
    }
}
