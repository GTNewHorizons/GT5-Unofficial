package gregtech.common.tileentities.machines;

import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

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
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.widget.AESlotWidget;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchInputBusME extends MTEHatchInputBus implements IConfigurationCircuitSupport,
    IRecipeProcessingAwareHatch, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState, ISmartInputHatch, IDataCopyable {

    private static final int SLOT_COUNT = 16;
    public static final String COPIED_DATA_IDENTIFIER = "stockingBus";
    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    private final ItemStack[] shadowInventory = new ItemStack[SLOT_COUNT];
    private final int[] savedStackSizes = new int[SLOT_COUNT];
    private boolean processingRecipe = false;
    private final boolean autoPullAvailable;
    private boolean autoPullItemList = false;
    private int minAutoPullStackSize = 1;
    private int autoPullRefreshTime = 100;
    private static final int CONFIG_WINDOW_ID = 10;
    private boolean additionalConnection = false;
    private boolean justHadNewItems = false;
    private boolean expediteRecipeCheck = false;

    public MTEHatchInputBusME(int aID, boolean autoPullAvailable, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            autoPullAvailable ? 6 : 3,
            SLOT_COUNT * 2 + 2,
            getDescriptionArray(autoPullAvailable));
        this.autoPullAvailable = autoPullAvailable;
        disableSort = true;
    }

    public MTEHatchInputBusME(String aName, boolean autoPullAvailable, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, SLOT_COUNT * 2 + 2, aDescription, aTextures);
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
        if (getBaseMetaTileEntity().isServerSide()) {
            if (aTimer % autoPullRefreshTime == 0 && autoPullItemList) {
                refreshItemList();
            }
            if (aTimer % 20 == 0) {
                getBaseMetaTileEntity().setActive(isActive());
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
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

    private void updateValidGridProxySides() {
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
        float aX, float aY, float aZ) {
        additionalConnection = !additionalConnection;
        updateValidGridProxySides();
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    @Override
    public AENetworkProxy getProxy() {
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
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        int[] sizes = new int[16];
        for (int i = 0; i < 16; ++i) sizes[i] = mInventory[i + 16] == null ? 0 : mInventory[i + 16].stackSize;
        aNBT.setIntArray("sizes", sizes);
        aNBT.setBoolean("autoStock", autoPullItemList);
        aNBT.setInteger("minAutoPullStackSize", minAutoPullStackSize);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setBoolean("expediteRecipeCheck", expediteRecipeCheck);
        aNBT.setInteger("refreshTime", autoPullRefreshTime);
        getProxy().writeToNBT(aNBT);
    }

    private void setAutoPullItemList(boolean pullItemList) {
        if (!autoPullAvailable) {
            return;
        }

        autoPullItemList = pullItemList;
        if (!autoPullItemList) {
            for (int i = 0; i < SLOT_COUNT; i++) {
                mInventory[i] = null;
            }
        } else {
            refreshItemList();
        }
        updateAllInformationSlots();
    }

    public boolean doFastRecipeCheck() {
        return expediteRecipeCheck;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("sizes")) {
            int[] sizes = aNBT.getIntArray("sizes");
            if (sizes.length == 16) {
                for (int i = 0; i < 16; ++i) {
                    if (sizes[i] != 0 && mInventory[i] != null) {
                        ItemStack s = mInventory[i].copy();
                        s.stackSize = sizes[i];
                        mInventory[i + 16] = s;
                    }
                }
            }
        }
        autoPullItemList = aNBT.getBoolean("autoStock");
        minAutoPullStackSize = aNBT.getInteger("minAutoPullStackSize");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        expediteRecipeCheck = aNBT.getBoolean("expediteRecipeCheck");
        if (aNBT.hasKey("refreshTime")) {
            autoPullRefreshTime = aNBT.getInteger("refreshTime");
        }
        getProxy().readFromNBT(aNBT);

    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            "The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET };
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
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

        updateValidGridProxySides();
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

        if (autoPullAvailable) {
            setAutoPullItemList(nbt.getBoolean("autoPull"));
            minAutoPullStackSize = nbt.getInteger("minStackSize");
            // Data sticks created before refreshTime was implemented should not cause stocking buses to
            // spam divide by zero errors
            if (nbt.hasKey("refreshTime")) {
                autoPullRefreshTime = nbt.getInteger("refreshTime");
            }
        }

        additionalConnection = nbt.getBoolean("additionalConnection");
        if (!autoPullItemList) {
            NBTTagList stockingItems = nbt.getTagList("itemsToStock", 10);
            for (int i = 0; i < stockingItems.tagCount(); i++) {
                this.mInventory[i] = GTUtility.loadItem(stockingItems.getCompoundTagAt(i));
            }
        }
        setInventorySlotContents(getCircuitSlot(), circuit);
        return true;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        tag.setInteger("refreshTime", autoPullRefreshTime);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setTag("circuit", GTUtility.saveItem(getStackInSlot(getCircuitSlot())));

        NBTTagList stockingItems = new NBTTagList();

        if (!autoPullItemList) {
            for (int index = 0; index < SLOT_COUNT; index++) {
                stockingItems.appendTag(GTUtility.saveItem(mInventory[index]));
            }
            tag.setTag("itemsToStock", stockingItems);
        }
        return tag;
    }

    private int getManualSlot() {
        return SLOT_COUNT * 2 + 1;
    }

    @Override
    public int getCircuitSlot() {
        return SLOT_COUNT * 2;
    }

    @Override
    public int getCircuitSlotX() {
        return 80;
    }

    @Override
    public int getCircuitSlotY() {
        return 64;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return aIndex != getManualSlot();
    }

    @Override
    public boolean justUpdated() {
        if (expediteRecipeCheck) {
            boolean ret = justHadNewItems;
            justHadNewItems = false;
            return ret;
        }
        return false;
    }

    public void setRecipeCheck(boolean value) {
        expediteRecipeCheck = value;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        if (expediteRecipeCheck && aStack != null) {
            justHadNewItems = true;
        }
        super.setInventorySlotContents(aIndex, aStack);
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (!processingRecipe) return super.getStackInSlot(aIndex);
        if (aIndex < 0 || aIndex > mInventory.length) return null;
        if (aIndex >= SLOT_COUNT && aIndex < SLOT_COUNT * 2)
            // Display slots
            return null;
        if (aIndex == getCircuitSlot() || aIndex == getManualSlot()) return mInventory[aIndex];
        if (mInventory[aIndex] != null) {
            AENetworkProxy proxy = getProxy();
            if (proxy == null || !proxy.isActive()) {
                return null;
            }
            try {
                IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                    .getItemInventory();
                IAEItemStack request = AEItemStack.create(mInventory[aIndex]);
                request.setStackSize(Integer.MAX_VALUE);
                IAEItemStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
                if (result != null) {
                    this.shadowInventory[aIndex] = result.getItemStack();
                    this.savedStackSizes[aIndex] = this.shadowInventory[aIndex].stackSize;
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, this.shadowInventory[aIndex]);
                    return this.shadowInventory[aIndex];
                } else {
                    // Request failed
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
            } catch (final GridAccessException ignored) {}
            return null;
        } else {
            // AE available but no items requested
            this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
        }
        return mInventory[aIndex];
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public void onExplosion() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            mInventory[i] = null;
        }
    }

    @Override
    public void startRecipeProcessing() {
        processingRecipe = true;
        updateAllInformationSlots();
    }

    private void refreshItemList() {
        AENetworkProxy proxy = getProxy();
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            Iterator<IAEItemStack> iterator = sg.getStorageList()
                .iterator();
            int index = 0;
            while (iterator.hasNext() && index < SLOT_COUNT) {
                IAEItemStack currItem = iterator.next();
                if (currItem.getStackSize() >= minAutoPullStackSize) {
                    ItemStack itemstack = GTUtility.copyAmount(1, currItem.getItemStack());
                    if (expediteRecipeCheck) {
                        ItemStack previous = this.mInventory[index];
                        if (itemstack != null) {
                            justHadNewItems = !ItemStack.areItemStacksEqual(itemstack, previous);
                        }
                    }
                    this.mInventory[index] = itemstack;
                    index++;
                }
            }
            for (int i = index; i < SLOT_COUNT; i++) {
                mInventory[i] = null;
            }

        } catch (final GridAccessException ignored) {}
    }

    private void updateAllInformationSlots() {
        for (int index = 0; index < SLOT_COUNT; index++) {
            updateInformationSlot(index, mInventory[index]);
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (int i = 0; i < SLOT_COUNT; ++i) {
            if (savedStackSizes[i] != 0) {
                ItemStack oldStack = shadowInventory[i];
                if (oldStack == null || oldStack.stackSize < savedStackSizes[i]) {
                    AENetworkProxy proxy = getProxy();
                    try {
                        IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                            .getItemInventory();
                        IAEItemStack request = AEItemStack.create(mInventory[i]);
                        int toExtract = savedStackSizes[i] - (oldStack == null ? 0 : oldStack.stackSize);
                        request.setStackSize(toExtract);
                        IAEItemStack result = sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                        proxy.getEnergy()
                            .extractAEPower(request.getStackSize(), Actionable.MODULATE, PowerMultiplier.CONFIG);
                        setInventorySlotContents(i + SLOT_COUNT, oldStack);
                        if (result == null || result.getStackSize() != toExtract) {
                            controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                            checkRecipeResult = SimpleCheckRecipeResult
                                .ofFailurePersistOnShutdown("stocking_bus_fail_extraction");
                        }
                    } catch (final GridAccessException ignored) {
                        controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                        checkRecipeResult = SimpleCheckRecipeResult
                            .ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
                    }
                }
                savedStackSizes[i] = 0;
                shadowInventory[i] = null;
                if (mInventory[i + SLOT_COUNT] != null && mInventory[i + SLOT_COUNT].stackSize <= 0) {
                    mInventory[i + SLOT_COUNT] = null;
                }
            }
        }
        processingRecipe = false;
        return checkRecipeResult;
    }

    /**
     * Update the right side of the GUI, which shows the amounts of items set on the left side
     */
    public ItemStack updateInformationSlot(int aIndex, ItemStack aStack) {
        if (aIndex >= 0 && aIndex < SLOT_COUNT) {
            if (aStack == null) {
                super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
            } else {
                AENetworkProxy proxy = getProxy();
                if (!proxy.isActive()) {
                    super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
                try {
                    IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                        .getItemInventory();
                    IAEItemStack request = AEItemStack.create(mInventory[aIndex]);
                    request.setStackSize(Integer.MAX_VALUE);
                    IAEItemStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
                    ItemStack s = (result != null) ? result.getItemStack() : null;
                    // We want to track changes in any ItemStack to notify any connected controllers to make a recipe
                    // check early
                    if (expediteRecipeCheck) {
                        ItemStack previous = getStackInSlot(aIndex + SLOT_COUNT);
                        if (s != null) {
                            justHadNewItems = !ItemStack.areItemStacksEqual(s, previous);
                        }
                    }
                    setInventorySlotContents(aIndex + SLOT_COUNT, s);
                    return s;
                } catch (final GridAccessException ignored) {}
            }
        }
        return null;
    }

    /**
     * Used to avoid slot update.
     */
    public ItemStack getShadowItemStack(int index) {
        if (index < 0 || index >= shadowInventory.length) {
            return null;
        }
        return shadowInventory[index];
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == getManualSlot();
    }

    @Override
    public int getGUIHeight() {
        return 179;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final SlotWidget[] aeSlotWidgets = new SlotWidget[16];

        if (autoPullAvailable) {
            buildContext.addSyncedWindow(CONFIG_WINDOW_ID, this::createStackSizeConfigurationWindow);
        }

        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .phantom(true)
                .slotCreator(index -> new BaseSlot(inventoryHandler, index, true) {

                    @Override
                    public boolean isEnabled() {
                        return !autoPullItemList && super.isEnabled();
                    }
                })
                .widgetCreator(slot -> (SlotWidget) new SlotWidget(slot) {

                    @Override
                    protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                        if (clickData.mouseButton != 0 || !getMcSlot().isEnabled()) return;
                        final int aSlotIndex = getMcSlot().getSlotIndex();
                        if (cursorStack == null) {
                            getMcSlot().putStack(null);
                        } else {
                            if (containsSuchStack(cursorStack)) return;
                            getMcSlot().putStack(GTUtility.copyAmount(1, cursorStack));
                        }
                        if (getBaseMetaTileEntity().isServerSide()) {
                            final ItemStack newInfo = updateInformationSlot(aSlotIndex, cursorStack);
                            aeSlotWidgets[getMcSlot().getSlotIndex()].getMcSlot()
                                .putStack(newInfo);
                        }
                    }

                    @Override
                    public IDrawable[] getBackground() {
                        IDrawable slot;
                        if (autoPullItemList) {
                            slot = GTUITextures.SLOT_DARK_GRAY;
                        } else {
                            slot = ModularUITextures.ITEM_SLOT;
                        }
                        return new IDrawable[] { slot, GTUITextures.OVERLAY_SLOT_ARROW_ME };
                    }

                    @Override
                    public List<String> getExtraTooltip() {
                        if (autoPullItemList) {
                            return Collections.singletonList(
                                StatCollector.translateToLocal("GT5U.machines.stocking_bus.cannot_set_slot"));
                        } else {
                            return Collections
                                .singletonList(StatCollector.translateToLocal("modularui.phantom.single.clear"));
                        }
                    }

                    private boolean containsSuchStack(ItemStack tStack) {
                        for (int i = 0; i < 16; ++i) {
                            if (GTUtility.areStacksEqual(mInventory[i], tStack, false)) return true;
                        }
                        return false;
                    }
                }.dynamicTooltip(() -> {
                    if (autoPullItemList) {
                        return Collections.singletonList(
                            StatCollector.translateToLocal("GT5U.machines.stocking_bus.cannot_set_slot"));
                    } else {
                        return Collections.emptyList();
                    }
                })
                    .setUpdateTooltipEveryTick(true))
                .build()
                .setPos(7, 9))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 4)
                    .startFromSlot(16)
                    .endAtSlot(31)
                    .phantom(true)
                    .background(GTUITextures.SLOT_DARK_GRAY)
                    .widgetCreator(
                        slot -> aeSlotWidgets[slot.getSlotIndex() - 16] = new AESlotWidget(slot).disableInteraction())
                    .build()
                    .setPos(97, 9))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_DOUBLE)
                    .setPos(82, 30)
                    .setSize(12, 12));

        if (autoPullAvailable) {
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    setAutoPullItemList(!autoPullItemList);
                } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(CONFIG_WINDOW_ID);
                }
            })
                .setBackground(() -> {
                    if (autoPullItemList) {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                            GTUITextures.OVERLAY_BUTTON_AUTOPULL_ME };
                    } else {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                            GTUITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED };
                    }
                })
                .addTooltips(
                    Arrays.asList(
                        StatCollector.translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip.1"),
                        StatCollector.translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip.2")))
                .setSize(16, 16)
                .setPos(80, 10))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullItemList, this::setAutoPullItemList));
        }

        builder.widget(TextWidget.dynamicString(() -> {
            boolean isActive = isActive();
            boolean isPowered = isPowered();
            boolean isBooting = isBooting();
            EnumChatFormatting color = (isActive && isPowered) ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED;
            return color + WailaText.getPowerState(isActive, isPowered, isBooting);
        })
            .setTextAlignment(Alignment.Center)
            .setSize(90, 9)
            .setPos(43, 84))
            .widget(
                new SlotWidget(inventoryHandler, getManualSlot())
                    // ghost slots are prioritized over manual slot
                    .setShiftClickPriority(11)
                    .setPos(79, 45));
    }

    protected ModularWindow createStackSizeConfigurationWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 115;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.min_stack_size")
                .setPos(3, 2)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> minAutoPullStackSize = (int) val)
                    .setGetter(() -> minAutoPullStackSize)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(3, 18)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.refresh_time")
                .setPos(3, 42)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> autoPullRefreshTime = (int) val)
                    .setGetter(() -> autoPullRefreshTime)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(3, 58)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.force_check")
                .setPos(3, 88)
                .setSize(50, 14))
            .widget(
                new CycleButtonWidget().setToggle(() -> expediteRecipeCheck, val -> setRecipeCheck(val))
                    .setTextureGetter(
                        state -> expediteRecipeCheck ? GTUITextures.OVERLAY_BUTTON_CHECKMARK
                            : GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setBackground(GTUITextures.BUTTON_STANDARD)
                    .setPos(53, 87)
                    .setSize(16, 16)
                    .addTooltip(StatCollector.translateToLocal("GT5U.machines.stocking_bus.hatch_warning")));
        return builder.build();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(80, 63));
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
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.stocking_bus.min_stack_size",
                    GTUtility.formatNumbers(minSize)));
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

    private static String[] getDescriptionArray(boolean autoPullAvailable) {
        List<String> strings = new ArrayList<>(8);
        strings.add("Advanced item input for Multiblocks");
        strings.add("Hatch Tier: " + TIER_COLORS[autoPullAvailable ? 6 : 3] + VN[autoPullAvailable ? 6 : 3]);
        strings.add("Retrieves directly from ME");
        strings.add("Keeps 16 item types in stock");

        if (autoPullAvailable) {
            strings.add(
                "Auto-Pull from ME mode will automatically stock the first 16 items in the ME system, updated every 5 seconds.");
            strings.add("Toggle by right-clicking with screwdriver, or use the GUI.");
            strings.add(
                "Use the GUI to limit the minimum stack size for Auto-Pulling, adjust the slot refresh timer and enable fast recipe checks.");
            strings.add("WARNING: Fast recipe checks can be laggy. Use with caution.");
        }

        strings.add("Change ME connection behavior by right-clicking with wire cutter.");
        strings.add("Configuration data can be copy+pasted using a data stick.");
        return strings.toArray(new String[0]);
    }
}
