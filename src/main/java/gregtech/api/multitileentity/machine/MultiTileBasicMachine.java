package gregtech.api.multitileentity.machine;

import static com.google.common.primitives.Ints.saturatedCast;
import static gregtech.api.enums.GT_Values.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.*;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.PollutionLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;
import gregtech.api.logic.interfaces.PollutionLogicHost;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.TickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileMachine;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.client.GT_SoundLoop;
import gregtech.common.GT_Pollution;

public abstract class MultiTileBasicMachine extends TickableMultiTileEntity
    implements IMultiTileMachine, ItemInventoryLogicHost {

    protected static final int ACTIVE = B[0];
    protected static final int TICKS_BETWEEN_RECIPE_CHECKS = 5 * TickTime.SECOND;
    protected static final int POLLUTION_TICK = TickTime.SECOND;
    protected static final byte INTERRUPT_SOUND_INDEX = 8;
    protected static final byte PROCESS_START_SOUND_INDEX = 1;

    protected static final IItemHandlerModifiable EMPTY_INVENTORY = new ItemStackHandler(0);

    public ITexture activeOverlayTexture = null;
    public ITexture activeOverlayGlowTexture = null;
    public ITexture inactiveOverlayTexture = null;
    public ITexture inactiveOverlayGlowTexture = null;

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
    protected boolean powerShutDown = false;
    protected boolean wasEnabled = false;
    protected boolean canWork = true;
    protected boolean isElectric = true;
    protected boolean isSteam = false;
    protected boolean acceptsFuel = false;
    protected boolean canUseWireless = false;
    protected boolean canUseLaser = false;
    protected byte soundEvent = 0;
    protected int soundEventValue = 0;
    protected ItemInventoryLogic input;
    protected ItemInventoryLogic output;

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
        input = new ItemInventoryLogic(Math.max(nbt.getInteger(NBT.INV_OUTPUT_SIZE), 0));
        output = new ItemInventoryLogic(Math.max(nbt.getInteger(NBT.INV_OUTPUT_SIZE), 0));
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
            outputTanks[i] = new FluidTankGT(capacity).setCapacityMultiplier(maxParallel * 2L)
                .readFromNBT(nbt, NBT.TANK_OUT + i);
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
    public void loadTextures(String folder) {
        super.loadTextures(folder);
        for (StatusTextures textureName : StatusTextures.TEXTURES) {
            ITexture texture = null;
            try {
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(
                        new ResourceLocation(
                            Mods.GregTech.ID,
                            "textures/blocks/multitileentity/" + folder + "/" + textureName.getName() + ".png"));
            } catch (IOException ignored) {
                texture = TextureFactory.of(Textures.BlockIcons.VOID);
            }
            if (texture == null) {
                if (textureName.hasGlow()) {
                    texture = TextureFactory.builder()
                        .addIcon(new CustomIcon("multitileentity/" + folder + "/" + textureName.getName()))
                        .glow()
                        .build();
                } else {
                    texture = TextureFactory
                        .of(new CustomIcon("multitileentity/" + folder + "/" + textureName.getName()));
                }
            }
            switch (textureName) {
                case Active -> activeOverlayTexture = texture;
                case ActiveWithGlow -> activeOverlayGlowTexture = texture;
                case Inactive -> inactiveOverlayTexture = texture;
                case InactiveWithGlow -> inactiveOverlayGlowTexture = texture;
            }
        }
    }

    @Override
    public void copyTextures() {
        super.copyTextures();
        final TileEntity tCanonicalTileEntity = MultiTileEntityRegistry
            .getCanonicalTileEntity(getMultiTileEntityRegistryID(), getMultiTileEntityID());
        if (!(tCanonicalTileEntity instanceof MultiTileBasicMachine)) {
            return;
        }
        final MultiTileBasicMachine canonicalEntity = (MultiTileBasicMachine) tCanonicalTileEntity;
        activeOverlayTexture = canonicalEntity.activeOverlayTexture;
        activeOverlayGlowTexture = canonicalEntity.activeOverlayGlowTexture;
        inactiveOverlayTexture = canonicalEntity.inactiveOverlayTexture;
        inactiveOverlayGlowTexture = canonicalEntity.inactiveOverlayGlowTexture;
    }

    @Override
    public ITexture getTexture(ForgeDirection side) {
        final ITexture texture = super.getTexture(side);
        if (side == facing) {
            if (isActive()) {
                return TextureFactory.of(texture, activeOverlayTexture, activeOverlayGlowTexture);
            }

            return TextureFactory.of(texture, inactiveOverlayTexture, inactiveOverlayGlowTexture);
        }

        return TextureFactory.of(texture, getCoverTexture(side));
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
    public boolean isLiquidInput(ForgeDirection side) {
        return side != facing;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return side != facing;
    }

    @Override
    protected IFluidTank[] getFluidTanks(ForgeDirection side) {
        final boolean fluidInput = isLiquidInput(side);
        final boolean fluidOutput = isLiquidOutput(side);

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
    public IFluidTank getFluidTankFillable(ForgeDirection side, FluidStack aFluidToFill) {
        return getFluidTankFillable(facing, side, aFluidToFill);
    }

    public IFluidTank getFluidTankFillable(ForgeDirection sideSource, ForgeDirection sideDestination,
        FluidStack fluidToFill) {
        if (sideSource.compareTo(sideDestination) != 0) return null;
        for (FluidTankGT tankGT : inputTanks) if (tankGT.contains(fluidToFill)) return tankGT;
        // if (!mRecipes.containsInput(aFluidToFill, this, slot(mRecipes.mInputItemsCount +
        // mRecipes.mOutputItemsCount))) return null;
        for (FluidTankGT fluidTankGT : inputTanks) if (fluidTankGT.isEmpty()) return fluidTankGT;
        return null;
    }

    @Override
    protected IFluidTank getFluidTankDrainable(ForgeDirection side, FluidStack aFluidToDrain) {
        return getFluidTankDrainable(facing, side, aFluidToDrain);
    }

    protected IFluidTank getFluidTankDrainable(ForgeDirection sideSource, ForgeDirection sideDestination,
        FluidStack fluidToDrain) {
        if (sideSource.compareTo(sideDestination) != 0) return null;
        for (FluidTankGT fluidTankGT : outputTanks)
            if (fluidToDrain == null ? fluidTankGT.has() : fluidTankGT.contains(fluidToDrain)) return fluidTankGT;

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
        return true;
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
                stopMachine(true);
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
        boolean result = logic.setInputItems(getInputItems())
            .setInputFluids(getInputFluids())
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
        PowerLogic logic = ((PowerLogicHost) this).getPowerLogic(ForgeDirection.UNKNOWN);

        if (logic == null) {
            return;
        }

        if (!logic.removeEnergyUnsafe(eut)) {
            stopMachine(true);
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

    @SideOnly(Side.CLIENT)
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

    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySoundLoop() {
        return null;
    }

    protected ItemStack[] getInputItems() {
        return inputInventory.getStacks()
            .toArray(new ItemStack[0]);
    }

    protected FluidStack[] getInputFluids() {
        return Arrays.stream(inputTanks)
            .map(FluidTankGT::get)
            .toArray(FluidStack[]::new);
    }

    protected void outputItems() {
        outputItems(itemsToOutput);
        itemsToOutput = null;
    }

    protected void outputItems(ItemStack... itemsToOutput) {
        outputItems(outputInventory, itemsToOutput);
    }

    protected void outputItems(IItemHandlerModifiable inventory, ItemStack... itemsToOutput) {
        if (itemsToOutput == null || inventory == null) {
            return;
        }
        for (ItemStack item : itemsToOutput) {
            int index = 0;
            while (item != null && item.stackSize > 0 && index < inventory.getSlots()) {
                item = inventory.insertItem(index++, item.copy(), false);
            }
        }
    }

    protected void outputFluids() {
        outputFluids(fluidsToOutput);
        fluidsToOutput = null;
    }

    protected void outputFluids(FluidStack... fluidsToOutput) {
        outputFluids(outputTanks, fluidsToOutput);
    }

    protected void outputFluids(FluidTankGT[] tankArray, FluidStack... fluidsToOutput) {
        if (fluidsToOutput == null) {
            return;
        }
        for (FluidStack fluid : fluidsToOutput) {
            tryToFillTanks(fluid, tankArray);
        }
    }

    protected void tryToFillTanks(FluidStack fluid, FluidTankGT... tanks) {
        for (FluidTankGT tank : tanks) {
            if (tank.canFillAll(fluid)) {
                fluid.amount -= tank.add(fluid.amount, fluid);
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

    protected boolean canUseWireless() {
        return canUseWireless;
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
        list.add(
            GT_Utility.trans("186", "Owned by: ") + EnumChatFormatting.BLUE
                + getOwnerName()
                + EnumChatFormatting.RESET
                + " ("
                + EnumChatFormatting.AQUA
                + getOwnerUuid()
                + EnumChatFormatting.RESET
                + ")");

        if (acceptsFuel()) {
            list.add("Fuel: " + EnumChatFormatting.GOLD + burnTime + "/" + totalBurnTime);
        }

        if (this instanceof PowerLogicHost powerLogicHost) {
            PowerLogic logic = powerLogicHost.getPowerLogic(facing);
            if (isElectric) {
                list.add(
                    StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(logic.getStoredEnergy())
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(logic.getCapacity())
                        + EnumChatFormatting.RESET
                        + " EU");
                list.add(
                    StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(eut)
                        + EnumChatFormatting.RESET
                        + " EU/t");
                list.add(
                    StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(logic.getVoltage())
                        + EnumChatFormatting.RESET
                        // TODO: Put ampere getter here, once that's variable
                        + " EU/t(*2A) "
                        + StatCollector.translateToLocal("GT5U.machines.tier")
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + VN[GT_Utility.getTier(logic.getVoltage())]
                        + EnumChatFormatting.RESET);
            }
        }

        addProgressStringToScanner(player, logLevel, list);

        // TODO: Add CPU load calculator
        list.add(
            "Average CPU load of ~" + GT_Utility.formatNumbers(0)
                + "ns over "
                + GT_Utility.formatNumbers(0)
                + " ticks with worst time of "
                + GT_Utility.formatNumbers(0)
                + "ns.");
    }

    protected void addProgressStringToScanner(EntityPlayer player, int logLevel, ArrayList<String> list) {
        list.add(
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(progressTime > 20 ? progressTime / 20 : progressTime)
                + EnumChatFormatting.RESET
                + (progressTime > 20 ? " s / " : " ticks / ")
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxProgressTime > 20 ? maxProgressTime / 20 : maxProgressTime)
                + EnumChatFormatting.RESET
                + (maxProgressTime > 20 ? " s" : " ticks"));
    }

    protected void stopMachine(boolean powerShutDown) {
        progressTime = 0;
        setActive(false);
        disableWorking();
        if (powerShutDown) {
            setSound(GregTechTileClientEvents.STOP_SOUND_LOOP, INTERRUPT_SOUND_INDEX);
        }
        issueClientUpdate();
    }

    protected void updateSlots() {
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            ItemStack item = inputInventory.getStackInSlot(i);
            if (item != null && item.stackSize <= 0) {
                inputInventory.setStackInSlot(i, null);
            }
        }

        for (FluidTankGT inputTank : inputTanks) {
            if (inputTank == null) {
                continue;
            }

            if (inputTank.get() != null && inputTank.get().amount <= 0) {
                inputTank.setEmpty();
                continue;
            }

            FluidStack afterRecipe = inputTank.get();
            FluidStack beforeRecipe = inputTank.get(Integer.MAX_VALUE);
            if (afterRecipe == null || beforeRecipe == null) {
                continue;
            }
            int difference = beforeRecipe.amount - afterRecipe.amount;
            inputTank.remove(difference);
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
        setActive((booleans & ACTIVE) == ACTIVE);
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

    public ItemInventoryLogic getItemLogic(InventoryType type) {
        return switch (type) {
            case Input -> input;
            case Output -> output;
        };
    }
}
