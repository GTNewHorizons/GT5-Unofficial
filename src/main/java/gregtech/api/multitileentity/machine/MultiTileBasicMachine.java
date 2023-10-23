package gregtech.api.multitileentity.machine;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.TickTime.MINUTE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.*;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.gui.GUIHost;
import gregtech.api.gui.GUIProvider;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.TickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileMachine;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.task.tasks.ProcessingTask;
import gregtech.api.util.GT_Utility;
import gregtech.client.GT_SoundLoop;
import gregtech.common.gui.MachineGUIProvider;

public abstract class MultiTileBasicMachine<P extends MuTEProcessingLogic<P>> extends TickableMultiTileEntity
    implements IMultiTileMachine, ProcessingLogicHost<P>, PowerLogicHost, GUIHost {

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
    protected int tier = 0;
    protected long burnTime = 0;
    protected long totalBurnTime = 0;

    protected boolean outputInventoryChanged = false;
    protected boolean powerShutDown = false;
    protected boolean wasEnabled = false;
    protected boolean canWork = true;
    protected boolean isElectric = true;
    protected boolean isSteam = false;
    protected boolean acceptsFuel = false;

    protected byte soundEvent = 0;
    protected int soundEventValue = 0;
    protected ItemInventoryLogic itemInput;
    protected ItemInventoryLogic itemOutput;
    protected FluidInventoryLogic fluidInput;
    protected FluidInventoryLogic fluidOutput;

    protected P processingLogic;
    @Nonnull
    protected VoidingMode voidingMode = VoidingMode.VOID_NONE;
    protected boolean processingUpdate = false;
    @Nonnull
    protected PowerLogic power = createPowerLogic();
    @Nonnull
    protected GUIProvider<?> guiProvider = createGUIProvider();

    @SideOnly(Side.CLIENT)
    protected GT_SoundLoop activitySoundLoop;

    public MultiTileBasicMachine() {
        new ProcessingTask<>(this);
    }

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

        saveItemLogic(nbt);
        saveFluidLogic(nbt);

        if (processingLogic != null) {
            NBTTagCompound processingLogicNBT = processingLogic.saveToNBT();
            nbt.setTag("processingLogic", processingLogicNBT);
        }

        nbt.setInteger(NBT.TIER, tier);
        nbt.setLong(NBT.BURN_TIME_LEFT, burnTime);
        nbt.setLong(NBT.TOTAL_BURN_TIME, totalBurnTime);
        nbt.setBoolean(NBT.ALLOWED_WORK, canWork);
        nbt.setBoolean(NBT.ACTIVE, active);
        power.saveToNBT(nbt);
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

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);
        if (nbt.hasKey(NBT.PARALLEL)) {
            maxParallel = Math.max(1, nbt.getInteger(NBT.PARALLEL));
        }

        if (nbt.hasKey(NBT.ACTIVE)) {
            active = nbt.getBoolean(NBT.ACTIVE);
        }

        loadItemLogic(nbt);
        loadFluidLogic(nbt);

        if (nbt.hasKey("processingLogic")) {
            P processingLogic = getProcessingLogic();
            processingLogic.loadFromNBT(Objects.requireNonNull(nbt.getCompoundTag("processingLogic")));
        }

        tier = nbt.getInteger(NBT.TIER);
        burnTime = nbt.getLong(NBT.BURN_TIME_LEFT);
        totalBurnTime = nbt.getLong(NBT.TOTAL_BURN_TIME);
        canWork = nbt.getBoolean(NBT.ALLOWED_WORK);
        active = nbt.getBoolean(NBT.ACTIVE);
        power.loadFromNBT(nbt);
    }

    protected void loadItemLogic(NBTTagCompound nbt) {
        itemInput = new ItemInventoryLogic(nbt.getInteger(NBT.INV_OUTPUT_SIZE), tier);
        itemOutput = new ItemInventoryLogic(nbt.getInteger(NBT.INV_OUTPUT_SIZE), tier);
        if (nbt.hasKey(NBT.INV_INPUT_LIST)) {
            itemInput.loadFromNBT(nbt.getCompoundTag(NBT.INV_INPUT_LIST));
        }
        if (nbt.hasKey(NBT.INV_OUTPUT_LIST)) {
            itemOutput.loadFromNBT(nbt.getCompoundTag(NBT.INV_OUTPUT_LIST));
        }
    }

    protected void loadFluidLogic(NBTTagCompound nbt) {
        fluidInput = new FluidInventoryLogic(16, 10000, tier);
        fluidOutput = new FluidInventoryLogic(16, 10000, tier);
        fluidInput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_IN));
        fluidOutput.loadFromNBT(nbt.getCompoundTag(NBT.TANK_OUT));
    }

    public boolean checkTexture(String modID, String resourcePath) {
        try {
            Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(new ResourceLocation(modID, resourcePath));
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    @Override
    public void loadTextures(String folder) {
        super.loadTextures(folder);
        for (StatusTextures textureName : StatusTextures.TEXTURES) {
            ITexture texture = null;
            String texturePath = "textures/blocks/multitileentity/" + folder + "/" + textureName.getName() + ".png";
            if (!checkTexture(Mods.GregTech.ID, texturePath)) {
                texture = TextureFactory.of(Textures.BlockIcons.VOID);
            } else {
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
        if (acceptsFuel() && isActive() && !consumeFuel()) {
            stopMachine(true);
            return;
        }

        if (hasThingsToDo()) {
            markDirty();
            runningTick(tick);
            return;
        }

        if (tick % TICKS_BETWEEN_RECIPE_CHECKS == 0 || hasWorkJustBeenEnabled()
            || hasInventoryBeenModified() && isAllowedToWork()) {
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

    /**
     * Runs only on server side
     *
     * @param tick The current tick of the machine
     */
    protected void runningTick(long tick) {
        consumeEnergy();
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
    protected void consumeEnergy() {
        PowerLogic logic = getPowerLogic();

        P processing = getProcessingLogic();

        if (!logic.removeEnergyUnsafe(processing.getCalculatedEut())) {
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
        if (aIndex == PROCESS_START_SOUND_INDEX && getProcessStartSound() != null) {
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
        if (isActive() && activitySound != null && activitySoundLoop == null) {
            activitySoundLoop = new GT_SoundLoop(activitySound, this, false, true);
            Minecraft.getMinecraft()
                .getSoundHandler()
                .playSound(activitySoundLoop);
            return;
        }

        if (activitySoundLoop != null) {
            activitySoundLoop = null;
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

    @Override
    public int getProgress() {
        P processing = getProcessingLogic();
        return processing.getProgress();
    }

    @Override
    public int getMaxProgress() {
        P processing = getProcessingLogic();
        return processing.getDuration();
    }

    @Override
    public boolean increaseProgress(int progressAmount) {
        P processing = getProcessingLogic();
        processing.increaseProgress(progressAmount);
        return true;
    }

    @Override
    public boolean hasThingsToDo() {
        return getMaxProgress() > 0;
    }

    @Override
    public boolean hasWorkJustBeenEnabled() {
        return wasEnabled;
    }

    @Override
    public void enableWorking() {
        wasEnabled = true;
        canWork = true;
    }

    @Override
    public void disableWorking() {
        canWork = false;
    }

    @Override
    public boolean wasShutdown() {
        return powerShutDown;
    }

    @Override
    public boolean isAllowedToWork() {
        return canWork;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
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

        PowerLogic logic = getPowerLogic();
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
                    + GT_Utility.formatNumbers(getProcessingLogic().getCalculatedEut())
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
        P processing = getProcessingLogic();
        int progressTime = processing.getProgress();
        int maxProgressTime = processing.getDuration();
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

    public boolean hasItemInput() {
        return true;
    }

    public boolean hasItemOutput() {
        return true;
    }

    public boolean hasFluidInput() {
        return true;
    }

    public boolean hasFluidOutput() {
        return true;
    }

    @Override
    public void setSound(byte soundEvent, int soundEventValue) {
        this.soundEvent = soundEvent;
        this.soundEventValue = soundEventValue;
        if (isServerSide()) {
            return;
        }

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

    @Override
    @Nonnull
    public P getProcessingLogic() {
        if (processingLogic == null) {
            processingLogic = createProcessingLogic().setMachineHost(this);
        }
        return Objects.requireNonNull(processingLogic);
    }

    @OverrideOnly
    @Nonnull
    protected abstract P createProcessingLogic();

    @Override
    public boolean isInputSeparated() {
        return false;
    }

    @Nonnull
    @Override
    public VoidingMode getVoidMode() {
        return voidingMode;
    }

    @Override
    public boolean needsUpdate() {
        return processingUpdate;
    }

    @Override
    public void setProcessingUpdate(boolean update) {
        processingUpdate = update;
    }

    @Override
    @Nullable
    public PowerLogic getPowerLogic(@Nonnull ForgeDirection side) {
        if (side == facing) return null;
        return power;
    }

    @Override
    @Nonnull
    public ForgeDirection getPowerOutputSide() {
        return Objects.requireNonNull(facing.getOpposite());
    }

    protected void updatePowerLogic() {
        power.setEnergyCapacity(GT_Values.V[tier] * power.getMaxAmperage() * 2 * MINUTE);
        power.setMaxVoltage(GT_Values.V[tier]);
        power.setMaxAmperage(1);
    }

    @Nonnull
    protected PowerLogic createPowerLogic() {
        return new PowerLogic().setMaxAmperage(1)
            .setType(PowerLogic.RECEIVER);
    }

    @Nonnull
    protected GUIProvider<?> createGUIProvider() {
        return new MachineGUIProvider<>(this);
    }

    @Nonnull
    public GUIProvider<?> getGUI(@Nonnull UIBuildContext uiContext) {
        return guiProvider;
    }

    @Override
    public ItemStack getAsItem() {
        return MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID())
            .getItem(getMultiTileEntityID());
    }

    @Override
    public String getMachineName() {
        return StatCollector.translateToLocal(getAsItem().getUnlocalizedName());
    }

}
