package gregtech.api.multitileentity.machine;

import static gregtech.api.enums.GT_Values.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.*;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.PollutionLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;
import gregtech.api.logic.interfaces.PollutionLogicHost;
import gregtech.api.logic.interfaces.PowerLogicHost;
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
    implements IMultiTileMachine, ItemInventoryLogicHost, FluidInventoryLogicHost {

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
    protected FluidStack[] fluidsToOutput = GT_Values.emptyFluidStack;
    protected ItemStack[] itemsToOutput = GT_Values.emptyItemStackArray;

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
    protected ItemInventoryLogic itemInput;
    protected ItemInventoryLogic itemOutput;
    protected FluidInventoryLogic fluidInput;
    protected FluidInventoryLogic fluidOutput;

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

        if (itemsToOutput != null) {
            saveItemsToOutput(nbt);
        }

        saveItemLogic(nbt);
        saveFluidLogic(nbt);

        nbt.setInteger(NBT.TIER, tier);
        nbt.setLong(NBT.EUT_CONSUMPTION, eut);
        nbt.setLong(NBT.BURN_TIME_LEFT, burnTime);
        nbt.setLong(NBT.TOTAL_BURN_TIME, totalBurnTime);
        nbt.setBoolean(NBT.ALLOWED_WORK, canWork);
        nbt.setBoolean(NBT.ACTIVE, active);
    }

    protected void saveItemLogic(NBTTagCompound nbt) {
        NBTTagCompound nbtListInput = itemInput.saveToNBT();
        nbt.setTag(NBT.INV_INPUT_LIST, nbtListInput);
        NBTTagCompound nbtListOutput = itemOutput.saveToNBT();
        nbt.setTag(NBT.INV_OUTPUT_LIST, nbtListOutput);
    }

    protected void saveFluidLogic(NBTTagCompound nbt) {
        NBTTagCompound fluidInputNBT = fluidInput.saveToNBT();
        nbt.setTag(NBT.TANK_IN, fluidInputNBT);
        NBTTagCompound fluidOutputNBT = fluidOutput.saveToNBT();
        nbt.setTag(NBT.TANK_OUT, fluidOutputNBT);
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

        fluidsToOutput = new FluidStack[getFluidOutputCount()];

        for (int i = 0; i < fluidsToOutput.length; i++) {
            fluidsToOutput[i] = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(NBT.FLUID_OUT + "." + i));
        }

        loadItemsToOutput(nbt);
        loadItemLogic(nbt);
        loadFluidLogic(nbt);

        tier = nbt.getInteger(NBT.TIER);
        eut = nbt.getLong(NBT.EUT_CONSUMPTION);
        burnTime = nbt.getLong(NBT.BURN_TIME_LEFT);
        totalBurnTime = nbt.getLong(NBT.TOTAL_BURN_TIME);
        canWork = nbt.getBoolean(NBT.ALLOWED_WORK);
        active = nbt.getBoolean(NBT.ACTIVE);
    }

    protected void loadItemLogic(NBTTagCompound nbt) {
        itemInput = new ItemInventoryLogic(Math.max(nbt.getInteger(NBT.INV_OUTPUT_SIZE), tier));
        itemOutput = new ItemInventoryLogic(Math.max(nbt.getInteger(NBT.INV_OUTPUT_SIZE), tier));
        itemInput.loadFromNBT(nbt.getCompoundTag(NBT.INV_INPUT_LIST));
        itemOutput.loadFromNBT(nbt.getCompoundTag(NBT.INV_OUTPUT_LIST));
    }

    protected void loadFluidLogic(NBTTagCompound nbt) {
        fluidInput = new FluidInventoryLogic(16, 10000, tier);
        fluidOutput = new FluidInventoryLogic(16, 10000, tier);
        fluidInput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_IN));
        fluidOutput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_OUT));
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
            outputItems(null);
            outputFluids(null);
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
        return false;
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
        return itemInput.getStoredItems();
    }

    protected FluidStack[] getInputFluids() {
        return fluidInput.getStoredFluids();
    }

    protected void outputItems(@Nullable UUID inventoryID) {
        if (itemsToOutput == null) return;
        for (int i = 0; i < itemsToOutput.length; i++) {
            itemOutput.insertItem(itemsToOutput[i]);
        }
        itemsToOutput = null;
    }

    protected void outputFluids(@Nullable UUID inventoryID) {
        if (fluidsToOutput == null) return;
        for (int i = 0; i < fluidsToOutput.length; i++) {
            fluidOutput.fill(fluidsToOutput[i]);
        }
        fluidsToOutput = null;
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
        if (isElectric() || isSteam()) return false;
        if (isActive() && burnTime <= 0) {
            for (int i = 0; i < itemInput.getSlots(); i++) {
                ItemStack item = itemInput.getItemInSlot(i);
                if (item == null) continue;
                int checkBurnTime = TileEntityFurnace.getItemBurnTime(item) / 10;
                if (checkBurnTime <= 0) continue;
                item.stackSize--;
                burnTime = checkBurnTime;
                totalBurnTime = checkBurnTime;
                break;
            }
            updateSlots();
        }

        if (--burnTime < 0) {
            burnTime = 0;
            totalBurnTime = 0;
            return false;
        }
        return false;
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
        itemInput.update(false);
        itemOutput.update(false);
        fluidInput.update();
        fluidOutput.update();
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

    @Nullable
    public ItemInventoryLogic getItemLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type) {
        if (side == facing) return null;
        return switch (type) {
            case Input -> itemInput;
            case Output -> itemOutput;
            default -> null;
        };
    }

    @Nullable
    public FluidInventoryLogic getFluidLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type) {
        if (side == facing) return null;
        return switch (type) {
            case Input -> fluidInput;
            case Output -> fluidOutput;
            default -> null;
        };
    }

}
