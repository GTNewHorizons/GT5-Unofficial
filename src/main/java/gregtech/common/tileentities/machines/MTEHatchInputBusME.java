package gregtech.common.tileentities.machines;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.ArrayList;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import appeng.api.config.Actionable;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusMEGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchInputBusME extends MTEHatchInputBus implements IRecipeProcessingAwareHatch, IAddGregtechLogo,
    IPowerChannelState, ISmartInputHatch, IDataCopyable, IMEConnectable {

    public static final int SLOT_COUNT = 16;
    public static final String COPIED_DATA_IDENTIFIER = "stockingBus";
    protected BaseActionSource requestSource = null;
    protected @Nullable AENetworkProxy gridProxy = null;
    protected final Slot[] slots = new Slot[SLOT_COUNT];
    protected boolean processingRecipe = false;
    public final boolean autoPullAvailable;
    protected boolean autoPullItemList = false;
    protected int minAutoPullStackSize = 1;
    protected int autoPullRefreshTime = 100;
    protected static final int CONFIG_WINDOW_ID = 10;
    protected boolean additionalConnection = false;
    protected boolean justHadNewItems = false;
    protected boolean expediteRecipeCheck = false;
    /**
     * The cached activity for this bus. Only valid while processing a recipe. This avoids several expensive operations.
     */
    protected boolean cachedActivity = false;

    public MTEHatchInputBusME(int aID, boolean autoPullAvailable, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, autoPullAvailable ? 6 : 4, 2, null);
        this.autoPullAvailable = autoPullAvailable;
        disableSort = true;
    }

    public MTEHatchInputBusME(String aName, boolean autoPullAvailable, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 2, aDescription, aTextures);
        this.autoPullAvailable = autoPullAvailable;
        disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBusME(mName, autoPullAvailable, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTimer % autoPullRefreshTime == 0 && autoPullItemList) {
                refreshItemList();
            }
            if (aTimer % 20 == 0) {
                aBaseMetaTileEntity.setActive(isActive());
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    public boolean isAllowedToWork() {
        if (processingRecipe) return cachedActivity;

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null || !igte.isAllowedToWork()) return false;

        AENetworkProxy proxy = getProxy();

        if (!proxy.isActive()) return false;

        return true;
    }

    @Override
    public void onEnableWorking() {
        super.onEnableWorking();

        if (expediteRecipeCheck) {
            justHadNewItems = true;
        }
    }

    @Override
    public void onDisableWorking() {
        super.onDisableWorking();

        if (autoPullItemList) {
            clearSlotConfigs();
        } else {
            clearExtractedStacks();
        }
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        updateAE2ProxyColor();
    }

    public void updateAE2ProxyColor() {
        AENetworkProxy proxy = getProxy();
        byte color = this.getColor();
        if (color == -1) {
            proxy.setColor(AEColor.Transparent);
        } else {
            proxy.setColor(AEColor.values()[Dyes.transformDyeIndex(color)]);
        }
        if (proxy.getNode() != null) {
            proxy.getNode()
                .updateState();
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    protected void updateValidGridProxySides() {
        if (additionalConnection) {
            getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
    }

    @Override
    public void onFacingChange() {
        updateValidGridProxySides();
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        additionalConnection = !additionalConnection;
        updateValidGridProxySides();
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    @Override
    public boolean connectsToAllSides() {
        return additionalConnection;
    }

    @Override
    public void setConnectsToAllSides(boolean connects) {
        additionalConnection = connects;
        updateValidGridProxySides();
    }

    @Override
    public @NotNull AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    autoPullAvailable ? ItemList.Hatch_Input_Bus_ME_Advanced.get(1)
                        : ItemList.Hatch_Input_Bus_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    @Override
    public boolean isPowered() {
        return getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy().isActive();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("version", 1);
        aNBT.setBoolean("autoStock", autoPullItemList);
        aNBT.setInteger("minAutoPullStackSize", minAutoPullStackSize);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setBoolean("expediteRecipeCheck", expediteRecipeCheck);
        aNBT.setInteger("refreshTime", autoPullRefreshTime);
        getProxy().writeToNBT(aNBT);

        NBTTagList slotList = new NBTTagList();
        aNBT.setTag("slots", slotList);

        for (int i = 0; i < slots.length; i++) {
            Slot slot = slots[i];

            if (slot == null) continue;

            NBTTagCompound tag = new NBTTagCompound();
            slot.writeToNBT(tag);
            tag.setInteger("index", i);

            slotList.appendTag(tag);
        }
    }

    public int getMinAutoPullStackSize() {
        return minAutoPullStackSize;
    }

    public void setMinAutoPullStackSize(int minAutoPullStackSize) {
        this.minAutoPullStackSize = minAutoPullStackSize;
    }

    public int getAutoPullRefreshTime() {
        return autoPullRefreshTime;
    }

    public void setAutoPullRefreshTime(int autoPullRefreshTime) {
        this.autoPullRefreshTime = autoPullRefreshTime;
    }

    public boolean isAutoPullItemList() {
        return autoPullItemList;
    }

    public void setAutoPullItemList(boolean pullItemList) {
        if (!autoPullAvailable) {
            return;
        }

        // check isServerSide to avoid refreshing twice
        if (autoPullItemList != pullItemList && getBaseMetaTileEntity().isServerSide()) {
            autoPullItemList = pullItemList;

            clearSlotConfigs();

            if (autoPullItemList) {
                refreshItemList();
            }

            updateAllInformationSlots();
        }
    }

    @Override
    public boolean doFastRecipeCheck() {
        return expediteRecipeCheck;
    }

    public void setRecipeCheck(boolean value) {
        expediteRecipeCheck = value;

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        // Changing this field requires a structure check/update, so let's do that automatically
        if (igte.isServerSide()) {
            GregTechAPI.causeMachineUpdate(igte.getWorld(), igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        autoPullItemList = aNBT.getBoolean("autoStock");
        minAutoPullStackSize = aNBT.getInteger("minAutoPullStackSize");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        expediteRecipeCheck = aNBT.getBoolean("expediteRecipeCheck");
        if (aNBT.hasKey("refreshTime")) {
            autoPullRefreshTime = aNBT.getInteger("refreshTime");
        }
        getProxy().readFromNBT(aNBT);
        updateAE2ProxyColor();

        clearSlotConfigs();

        switch (aNBT.getInteger("version")) {
            case 0 -> {
                int[] sizes = aNBT.hasKey("sizes") ? aNBT.getIntArray("sizes") : new int[0];

                final NBTTagList inventory = aNBT.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

                ItemStack[] oldInventory = new ItemStack[SLOT_COUNT * 2 + 2];

                // Copy of the current mInventory loading code, because otherwise the upper stacks are discarded due to
                // the reduced mInventory size.
                // noinspection unchecked
                for (NBTTagCompound tag : (List<NBTTagCompound>) inventory.tagList) {
                    oldInventory[tag.getInteger("IntSlot")] = GTUtility.loadItem(tag);
                }

                // Migrate the circuit and manual slots
                mInventory[0] = oldInventory[SLOT_COUNT * 2];
                mInventory[1] = oldInventory[SLOT_COUNT * 2 + 1];

                // Migrate the config from the old system (raw item stacks) to the new system (dedicated slot objects)
                for (int i = 0; i < SLOT_COUNT; i++) {
                    if (oldInventory[i] != null) {
                        Slot slot = new Slot(oldInventory[i]);

                        if (i < sizes.length && sizes[i] > 0) {
                            slot.extracted = slot.config.copy();
                            slot.extractedAmount = sizes[i];
                        }

                        slots[i] = slot;
                    }
                }
            }
            case 1 -> {
                NBTTagList slotList = aNBT.getTagList("slots", Constants.NBT.TAG_COMPOUND);

                // noinspection unchecked
                for (NBTTagCompound tag : (List<NBTTagCompound>) slotList.tagList) {
                    Slot slot = Slot.readFromNBT(tag);

                    if (slot != null) {
                        slots[tag.getInteger("index")] = slot;
                    }
                }
            }
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            getProxy().isActive() ? StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.bus.online")
                : StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.crafting_input_me.bus.offline",
                    getAEDiagnostics()) };
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == getManualSlot();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == getManualSlot();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == getManualSlot();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!autoPullAvailable) {
            return;
        }

        setAutoPullItemList(!autoPullItemList);
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                "GT5U.machines.stocking_bus.auto_pull_toggle." + (autoPullItemList ? "enabled" : "disabled")));
    }

    @Override
    public void updateSlots() {
        if (mInventory[getManualSlot()] != null && mInventory[getManualSlot()].stackSize <= 0)
            mInventory[getManualSlot()] = null;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);

        if (!pasteCopiedData(aPlayer, dataStick.stackTagCompound)) return false;

        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        dataStick.stackTagCompound = getCopiedData(aPlayer);
        dataStick.setStackDisplayName("Stocking Input Bus Configuration");
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.saved"));
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) return false;

        ItemStack circuit = GTUtility.loadItem(nbt, "circuit");
        if (GTUtility.isStackInvalid(circuit)) circuit = null;
        setInventorySlotContents(getCircuitSlot(), circuit);

        if (autoPullAvailable) {
            setAutoPullItemList(nbt.getBoolean("autoPull"));
            minAutoPullStackSize = nbt.getInteger("minStackSize");
            // Data sticks created before refreshTime was implemented should not cause stocking buses to spam divide by
            // zero errors
            if (nbt.hasKey("refreshTime")) {
                autoPullRefreshTime = nbt.getInteger("refreshTime");
            }
            expediteRecipeCheck = nbt.getBoolean("expediteRecipeCheck");
        }

        additionalConnection = nbt.getBoolean("additionalConnection");

        if (!autoPullItemList) {
            NBTTagList stockingItems = nbt.getTagList("itemsToStock", Constants.NBT.TAG_COMPOUND);

            clearSlotConfigs();

            for (int i = 0; i < stockingItems.tagCount(); i++) {
                slots[i] = new Slot(GTUtility.loadItem(stockingItems.getCompoundTagAt(i)));
            }
        }

        updateValidGridProxySides();
        byte color = nbt.getByte("color");
        this.getBaseMetaTileEntity()
            .setColorization(color);

        updateAllInformationSlots();
        return true;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        tag.setInteger("refreshTime", autoPullRefreshTime);
        tag.setBoolean("expediteRecipeCheck", expediteRecipeCheck);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setByte("color", this.getColor());
        tag.setTag("circuit", GTUtility.saveItem(getStackInSlot(getCircuitSlot())));

        if (!autoPullItemList) {
            NBTTagList stockingItems = new NBTTagList();

            for (Slot slot : slots) {
                if (slot == null) continue;

                stockingItems.appendTag(slot.config.writeToNBT(new NBTTagCompound()));
            }

            tag.setTag("itemsToStock", stockingItems);
        }

        return tag;
    }

    public int getManualSlot() {
        return 1;
    }

    @Override
    public int getCircuitSlot() {
        return 0;
    }

    @Override
    public int getCircuitSlotX() {
        return 85;
    }

    @Override
    public int getCircuitSlotY() {
        return 64;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        if (processingRecipe) {
            return true;
        }
        return aIndex != getManualSlot();
    }

    @Override
    public boolean justUpdated() {
        if (expediteRecipeCheck && isAllowedToWork()) {
            boolean ret = justHadNewItems;
            justHadNewItems = false;
            return ret;
        }
        return false;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusMEGui(this, slots).build(data, syncManager, uiSettings);
    }

    @Override
    public int getSizeInventory() {
        // Add fake slots so that multis can detect the stocked items properly
        // 0 to 15: stocked items
        // 16: circuit
        // 17: manual slot
        return SLOT_COUNT + 2;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        // Used to offset the slot index due to recipe checks, when doing other things it will return 0,
        // which allows for changing phantom circuit using mouse clicks
        int virtualSlotOffset = processingRecipe ? SLOT_COUNT : 0;

        if (slotIndex < 0 || slotIndex >= getSizeInventory()) return null;

        // Put the circuit + manual slots at the end. The stocked slots come first, then the circuit, then the manual.
        // Since machines reverse this order prior to recipe checks, the actual order is: manual, then circuit, then
        // stocked.
        if (slotIndex == getCircuitSlot() + virtualSlotOffset) return mInventory[getCircuitSlot()];
        if (slotIndex == getManualSlot() + virtualSlotOffset) return mInventory[getManualSlot()];
        if (!processingRecipe) return null;

        if (!isAllowedToWork()) {
            return null;
        }

        Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

        if (slot == null) return null;

        // Must pass the reference out to the multi
        return slot.extracted;
    }

    protected BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public void startRecipeProcessing() {
        // Call isAllowedToWork before setting processingRecipe to true
        cachedActivity = isAllowedToWork();
        processingRecipe = true;
        updateAllInformationSlots();
    }

    protected void refreshItemList() {
        if (!isAllowedToWork()) return;

        IMEMonitor<IAEItemStack> sg;
        Iterator<IAEItemStack> iterator;

        try {
            sg = getProxy().getStorage()
                .getItemInventory();
            iterator = sg.getStorageList()
                .iterator();
        } catch (final GridAccessException ignored) {
            return;
        }

        int index = 0;

        clearSlotConfigs();

        while (iterator.hasNext() && index < SLOT_COUNT) {
            IAEItemStack curr = iterator.next();

            if (curr.getStackSize() < minAutoPullStackSize) continue;

            Slot oldSlot = slots[index];

            // Prevent weird reference problems by copying the slot
            if (oldSlot != null) oldSlot = oldSlot.copy();

            setSlotConfig(index, GTUtility.copyAmount(1, curr.getItemStack()));

            Slot newSlot = slots[index];

            if (newSlot != null) {
                newSlot.extracted = curr.getItemStack();
                newSlot.extractedAmount = newSlot.extracted.stackSize;
            }

            if (!Objects.equals(oldSlot, newSlot)) {
                justHadNewItems = true;
            }

            index++;
        }
    }

    protected void updateAllInformationSlots() {
        if (isAllowedToWork()) {
            try {
                for (int index = 0; index < SLOT_COUNT; index++) {
                    updateInformationSlot(index);
                }
            } catch (GridAccessException e) {
                // :P
            }
        } else {
            clearExtractedStacks();
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.SUCCESSFUL;

        IMEMonitor<IAEItemStack> sg;
        IEnergyGrid energy;

        try {
            AENetworkProxy proxy = getProxy();

            // on some setup endRecipeProcessing() somehow runs before onFirstTick();
            // test world
            // https://discord.com/channels/181078474394566657/522098956491030558/1441490828760449124
            if (!proxy.isReady()) proxy.onReady();

            sg = proxy.getStorage()
                .getItemInventory();
            energy = getProxy().getEnergy();
        } catch (GridAccessException e) {
            controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("stocking_bus_fail_extraction");
        }

        for (Slot slot : slots) {
            if (slot == null || slot.extracted == null || slot.extractedAmount == 0) continue;

            int toExtract = slot.extractedAmount - slot.extracted.stackSize;

            if (toExtract <= 0) continue;

            IAEItemStack request = slot.createAEStack(toExtract);

            IAEItemStack result = Platform.poweredExtraction(energy, sg, request, getRequestSource());

            if (result == null || result.getStackSize() != toExtract) {
                controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                checkRecipeResult = SimpleCheckRecipeResult.ofFailurePersistOnShutdown("stocking_bus_fail_extraction");
            }
        }

        processingRecipe = false;

        return checkRecipeResult;
    }

    public void setSlotConfig(int index, ItemStack config) {
        slots[index] = config == null ? null : new Slot(config.copy());
    }

    /**
     * Polls the AE network to update the available items for the given slot.
     */
    public void updateInformationSlot(int index) throws GridAccessException {
        Slot slot = GTDataUtils.getIndexSafe(slots, index);

        if (slot == null) return;

        if (!isAllowedToWork()) {
            slot.resetExtracted();
            return;
        }

        IMEMonitor<IAEItemStack> sg = getProxy().getStorage()
            .getItemInventory();

        IAEItemStack request = slot.createAEStack(Integer.MAX_VALUE);

        IAEItemStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());

        ItemStack previous = slot.extracted;

        slot.extracted = result != null ? result.getItemStack() : null;
        slot.extractedAmount = slot.extracted == null ? 0 : slot.extracted.stackSize;

        // We want to track changes in any ItemStack to notify any connected controllers to make a recipe check early
        if (expediteRecipeCheck && slot.extracted != null) {
            justHadNewItems = !ItemStack.areItemStacksEqual(slot.extracted, previous);
        }
    }

    protected void clearSlotConfigs() {
        Arrays.fill(slots, null);
    }

    protected void clearExtractedStacks() {
        for (Slot slot : slots) {
            if (slot == null) continue;

            slot.resetExtracted();
        }
    }

    /**
     * Gets the first non-null extracted item stack.
     *
     * @return The first extracted item stack, or null if it doesn't exist.
     */
    public ItemStack getFirstValidStack() {
        return getFirstValidStack(false);
    }

    /**
     * Gets the first non-null extracted item stack.
     *
     * @param slotsMustMatch When true, every slot in this input bus must be the same (ignores stack sizes).
     * @return The first extracted item stack, or null if it doesn't exist.
     */
    public ItemStack getFirstValidStack(boolean slotsMustMatch) {
        if (slotsMustMatch) {
            ItemStack firstValid = null;

            for (Slot slot : slots) {
                if (slot == null || slot.extracted == null) continue;

                if (firstValid == null) {
                    firstValid = slot.extracted;
                } else {
                    if (!GTUtility.areStacksEqual(firstValid, slot.extracted)) {
                        return null;
                    }
                }
            }

            return firstValid;
        } else {
            for (Slot slot : slots) {
                if (slot == null || slot.extracted == null) continue;

                return slot.extracted;
            }

            return null;
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (!autoPullAvailable) {
            super.getWailaBody(itemStack, currenttip, accessor, config);
            return;
        }

        NBTTagCompound tag = accessor.getNBTData();
        boolean autopull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minStackSize");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.waila.stocking_bus.auto_pull." + (autopull ? "enabled" : "disabled")));
        if (autopull) {
            currenttip.add(
                StatCollector
                    .translateToLocalFormatted("GT5U.waila.stocking_bus.min_stack_size", formatNumber(minSize)));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        if (!autoPullAvailable) {
            super.getWailaNBTData(player, tile, tag, world, x, y, z);
            return;
        }

        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public String[] getDescription() {
        if (autoPullAvailable) return GTSplit
            .splitLocalizedFormatted("gt.blockmachines.input_bus_me.autopull.desc", TIER_COLORS[6] + VN[6]);
        return GTSplit.splitLocalizedFormatted("gt.blockmachines.input_bus_me.desc", TIER_COLORS[4] + VN[4]);
    }

    public static class Slot {

        /** The item to pull into this slot. */
        public final ItemStack config;

        /** The amount of stuff initially in the ME system when the recipe check started. */
        public int extractedAmount;
        /**
         * The extracted stack (almost certainly equal to config). This is shared as a reference to the multiblock,
         * which decrements the stored amount as it gets consumed. After the recipe check has finished, the amount in
         * this stack is compared to {@link #extractedAmount} and the difference is subtracted from the ME system. If
         * this operation fails, the machine is shut down.
         */
        public ItemStack extracted;

        /** A cached AE stack for {@link #config}, to speed up extractions. */
        private final IAEItemStack prototypeStack;

        public Slot(ItemStack config) {
            this.config = config;
            this.prototypeStack = AEItemStack.create(config)
                .setStackSize(0);
        }

        /** Resets the extracted amount. */
        public void resetExtracted() {
            extracted = null;
            extractedAmount = 0;
        }

        public IAEItemStack createAEStack(long amount) {
            return prototypeStack.copy()
                .setStackSize(amount);
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof Slot slot)) return false;

            return extractedAmount == slot.extractedAmount && Objects.equals(config, slot.config)
                && Objects.equals(extracted, slot.extracted);
        }

        public Slot copy() {
            Slot copy = new Slot(this.config);

            copy.extracted = this.extracted;
            copy.extractedAmount = this.extractedAmount;

            return copy;
        }

        public void writeToNBT(NBTTagCompound tag) {
            tag.setTag("config", config.writeToNBT(new NBTTagCompound()));
            if (extracted != null) {
                tag.setTag("extracted", extracted.writeToNBT(new NBTTagCompound()));
                tag.setInteger("extractedAmount", extractedAmount);
            }
        }

        public static Slot readFromNBT(NBTTagCompound tag) {
            Slot slot = new Slot(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("config")));

            if (slot.config == null) return null;

            if (tag.hasKey("extracted")) {
                slot.extracted = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("extracted"));
                slot.extractedAmount = tag.getInteger("extractedAmount");
            }

            return slot;
        }
    }

    @Override
    public ItemStack removeResource(ItemStack[] targets, int amount) {
        if (targets == null || targets.length == 0 || amount <= 0 || getBaseMetaTileEntity() == null) {
            return null;
        }

        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null || slotStack.stackSize < amount) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    ItemStack removed = decrStackSize(i, amount);
                    if (removed != null) {
                        updateSlots();
                        return removed;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack removeAllResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0 || getBaseMetaTileEntity() == null) {
            return null;
        }

        ItemStack result = null;
        boolean updated = false;
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    ItemStack removed = decrStackSize(i, slotStack.stackSize);
                    if (removed != null) {
                        if (result == null) {
                            result = removed.copy();
                        } else if (GTUtility.areStacksEqual(result, removed)) {
                            result.stackSize += removed.stackSize;
                        }
                        updated = true;
                    }
                }
            }
        }
        if (updated) {
            updateSlots();
        }
        return result;
    }

    @Override
    public ItemStack findResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0) {
            return null;
        }

        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    return slotStack.copy();
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0) {
            return false;
        }

        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    return true;
                }
            }
        }
        return false;
    }
}
