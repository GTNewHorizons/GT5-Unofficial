package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import appeng.api.AEApi;
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
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.AESlotWidget;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_InputBus_ME extends GT_MetaTileEntity_Hatch_InputBus
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState {

    private static final int SLOT_COUNT = 16;
    private static final int FETCH_INTERVAL_DEFAULT = 5;
    private static final int STOCK_CONFIG_WINDOW_ID = 98;
    private static final int AUTO_PULL_CONFIG_WINDOW_ID = 99;

    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    private final ItemStackHandler stockedInventory = new ItemStackHandler(SLOT_COUNT) {

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return Integer.MAX_VALUE;
        }
    };
    private boolean processingRecipe = false;
    private boolean autoPullItemList = false;
    private int[] fetchIntervals = new int[SLOT_COUNT];
    private int[] fetchLimits = new int[SLOT_COUNT];
    private int autoPullInterval = FETCH_INTERVAL_DEFAULT;
    private int minAutoPullStackSize = 1;
    private static IDrawable WRENCH_DRAWABLE;
    private boolean additionalConnection = false;

    public GT_MetaTileEntity_Hatch_InputBus_ME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            1,
            SLOT_COUNT * 2 + 2,
            new String[] { "Advanced item input for Multiblocks", "Retrieves directly from ME",
                "Keeps 16 item types in stock",
                "Auto-Pull from ME mode will automatically stock the first 16 items in the ME system, updated every 5 seconds.",
                "Toggle by right-clicking with screwdriver, or use the GUI.",
                "Use the GUI to limit the minimum stack size for Auto-Pulling.",
                "Change ME connection behavior by right-clicking with wire cutter",
                "Configuration data can be copy+pasted using a data stick." });
        disableSort = true;
    }

    public GT_MetaTileEntity_Hatch_InputBus_ME(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, SLOT_COUNT * 2 + 2, aDescription, aTextures);
        disableSort = true;
        Arrays.fill(fetchIntervals, FETCH_INTERVAL_DEFAULT);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputBus_ME(mName, mTier, mDescriptionArray, mTextures);
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
        if (autoPullItemList && aTimer % (autoPullInterval * 20L) == 0) {
            refreshAutoPullItemList();
        }
        updateAllStockedItems(index -> aTimer % (fetchIntervals[index] * 20L) == 0);
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

    public void setAdditionalConnectionOption() {
        if (additionalConnection) {
            gridProxy.setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            gridProxy.setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        additionalConnection = !additionalConnection;
        setAdditionalConnectionOption();
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
                    ItemList.Hatch_Output_Bus_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                setAdditionalConnectionOption();
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
        aNBT.setTag("stockedInventory", stockedInventory.serializeNBT());
        aNBT.setBoolean("autoStock", autoPullItemList);
        aNBT.setTag("fetchIntervals", new NBTTagIntArray(fetchIntervals));
        aNBT.setTag("fetchLimits", new NBTTagIntArray(fetchLimits));
        aNBT.setInteger("autoPullInterval", autoPullInterval);
        aNBT.setInteger("minAutoPullStackSize", minAutoPullStackSize);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        for (int i = 0; i <= 15; i++) {
            // Migration from old save: Make sure configuration slots have 0 stack size
            if (mInventory[i] != null) {
                mInventory[i].stackSize = 0;
            }
        }
        for (int i = 16; i <= 31; i++) {
            // Migration from old save: We used to store stocked items in ghost format with `mInventory`
            mInventory[i] = null;
        }
        if (aNBT.hasKey("stockedInventory", Constants.NBT.TAG_COMPOUND)) {
            stockedInventory.deserializeNBT(aNBT.getCompoundTag("stockedInventory"));
        }
        autoPullItemList = aNBT.getBoolean("autoStock");
        NBTBase intervalTag = aNBT.getTag("fetchIntervals");
        if (intervalTag instanceof NBTTagIntArray intervals) {
            fetchIntervals = intervals.func_150302_c();
        }
        NBTBase limitTag = aNBT.getTag("fetchLimits");
        if (limitTag instanceof NBTTagIntArray limits) {
            fetchLimits = limits.func_150302_c();
        }
        if (aNBT.hasKey("autoPullInterval")) {
            // Check existence for backward compat
            autoPullInterval = aNBT.getInteger("autoPullInterval");
        }
        minAutoPullStackSize = aNBT.getInteger("minAutoPullStackSize");
        additionalConnection = aNBT.getBoolean("additionalConnection");
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
        setAutoPullItemList(!autoPullItemList);
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                "GT5U.machines.stocking_bus.auto_pull_toggle." + (autoPullItemList ? "enabled" : "disabled")));
    }

    @Override
    public void updateSlots() {
        if (mInventory[getManualSlot()] != null && mInventory[getManualSlot()].stackSize <= 0) {
            mInventory[getManualSlot()] = null;
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stocked = stockedInventory.getStackInSlot(i);
            if (stocked != null && stocked.stackSize <= 0) {
                stockedInventory.setStackInSlot(i, null);
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        if (!dataStick.hasTagCompound() || !"stockingBus".equals(dataStick.stackTagCompound.getString("type")))
            return false;

        NBTTagCompound nbt = dataStick.stackTagCompound;

        ItemStack circuit = GT_Utility.loadItem(dataStick.stackTagCompound, "circuit");
        if (GT_Utility.isStackInvalid(circuit)) circuit = null;
        setAutoPullItemList(nbt.getBoolean("autoPull"));
        NBTBase intervalTag = nbt.getTag("fetchIntervals");
        if (intervalTag instanceof NBTTagIntArray intervals) {
            fetchIntervals = intervals.func_150302_c();
        }
        NBTBase limitTag = nbt.getTag("fetchLimits");
        if (limitTag instanceof NBTTagIntArray limits) {
            fetchLimits = limits.func_150302_c();
        }
        if (nbt.hasKey("autoPullInterval")) {
            // Check existence for backward compat
            autoPullInterval = nbt.getInteger("autoPullInterval");
        }
        minAutoPullStackSize = nbt.getInteger("minStackSize");
        additionalConnection = nbt.getBoolean("additionalConnection");
        if (!autoPullItemList) {
            NBTTagList stockingItems = nbt.getTagList("itemsToStock", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < stockingItems.tagCount(); i++) {
                this.mInventory[i] = GT_Utility.loadItem(stockingItems.getCompoundTagAt(i));
            }
        }
        setInventorySlotContents(getCircuitSlot(), circuit);
        setAdditionalConnectionOption();
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "stockingBus");
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setTag("fetchIntervals", new NBTTagIntArray(fetchIntervals));
        tag.setTag("fetchLimits", new NBTTagIntArray(fetchLimits));
        tag.setInteger("autoPullInterval", autoPullInterval);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setTag("circuit", GT_Utility.saveItem(getStackInSlot(getCircuitSlot())));

        NBTTagList stockingItems = new NBTTagList();

        if (!autoPullItemList) {
            for (int index = 0; index < SLOT_COUNT; index++) {
                stockingItems.appendTag(GT_Utility.saveItem(mInventory[index]));
            }
            tag.setTag("itemsToStock", stockingItems);
        }
        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName("Stocking Input Bus Configuration");
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.saved"));
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
        return 61;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        // 0 - 15: stock configuration slots
        // 16 - 31: stocked items
        // 32: ghost circuit slot
        // 33: manual slot
        if (aIndex < 0 || aIndex > mInventory.length) {
            return null;
        }
        if (processingRecipe && aIndex < SLOT_COUNT) {
            // While processing recipe, configuration slots should not be accessible
            return null;
        }
        if (aIndex >= SLOT_COUNT && aIndex < SLOT_COUNT * 2) {
            return stockedInventory.getStackInSlot(aIndex - SLOT_COUNT);
        }
        return mInventory[aIndex];
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public void onExplosion() {
        super.onExplosion();
        tryPushBackAllItems();
    }

    @Override
    public void onBlockDestroyed() {
        tryPushBackAllItems();
    }

    @Override
    public void startRecipeProcessing() {
        processingRecipe = true;
    }

    @Override
    public void endRecipeProcessing() {
        processingRecipe = false;
    }

    private void setAutoPullItemList(boolean pullItemList) {
        autoPullItemList = pullItemList;
        if (!autoPullItemList) {
            for (int i = 0; i < SLOT_COUNT; i++) {
                mInventory[i] = null;
            }
        } else {
            refreshAutoPullItemList();
        }
        // Update all slots immediately
        updateAllStockedItems(index -> true);
    }

    /**
     * Fetches 16 items stored in storage and updates configuration. Does not pull actual items.
     * Used for auto-pull mode.
     */
    private void refreshAutoPullItemList() {
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
                    ItemStack itemstack = GT_Utility.copyAmount(0, currItem.getItemStack());
                    this.mInventory[index] = itemstack;
                    index++;
                }
            }
            for (int i = index; i < SLOT_COUNT; i++) {
                mInventory[i] = null;
            }

        } catch (final GridAccessException ignored) {}
    }

    /**
     * Pulls items stored in the ME network or pushes excess items to the ME network.
     *
     * @param ifUpdate Whether to update slot on given index.
     */
    private void updateAllStockedItems(Predicate<Integer> ifUpdate) {
        if (autoPullItemList) {
            sortStockedItems();
        }
        for (int index = 0; index < SLOT_COUNT; index++) {
            if (ifUpdate.test(index)) {
                updateStockedItem(index);
            }
        }
    }

    /**
     * Pulls items stored in the ME network or pushes excess items to the ME network.
     *
     * @param index Index to refer pull configuration
     */
    private void updateStockedItem(int index) {
        if (index < 0 || index >= SLOT_COUNT) {
            return;
        }
        ItemStack toPull = mInventory[index];
        if (toPull == null) {
            // Configured to empty
            if (!autoPullItemList) {
                // We don't push back wrong item in auto-pull mode,
                // in case the network is configured to be un-insertable
                tryPushBackItem(index);
            }
            return;
        }
        ItemStack currentStored = stockedInventory.getStackInSlot(index);
        if (currentStored != null && !GT_Utility.areStacksEqual(toPull, currentStored)) {
            // Wrong item stocked
            if (autoPullItemList) {
                // When in auto-pull mode, let it there
                return;
            }
            // Otherwise push it back
            if (!tryPushBackItem(index)) {
                // If failed, we can't pull new item
                return;
            }
        }
        AENetworkProxy proxy = getProxy();
        if (!proxy.isActive()) {
            return;
        }
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            final int limit = fetchLimits[index] != 0 ? fetchLimits[index] : Integer.MAX_VALUE;
            final int amount = limit - (currentStored != null ? currentStored.stackSize : 0);
            if (amount <= 0) {
                if (amount < 0) {
                    // Amount of items stocked exceeds the configuration, presumably due to config update.
                    // Push back the excess items.
                    tryPushBackItem(index, -amount);
                }
                return;
            }
            IAEItemStack request = AEItemStack.create(toPull)
                .setStackSize(amount);
            IAEItemStack result = Platform.poweredExtraction(proxy.getEnergy(), sg, request, getRequestSource());
            ItemStack pulled = (result != null) ? result.getItemStack() : null;
            stockedInventory.insertItem(index, pulled, false);
        } catch (final GridAccessException ignored) {}
    }

    /**
     * Sorts stocked items to match order of auto-pull configurations
     */
    private void sortStockedItems() {
        List<ItemStack> stash = new ArrayList<>();
        for (int i = 0; i < SLOT_COUNT; i++) {
            stash.add(stockedInventory.getStackInSlot(i));
        }

        stash.sort(Comparator.comparingInt(stack -> {
            if (stack == null) {
                return Integer.MAX_VALUE;
            }
            for (int configIndex = 0; configIndex < SLOT_COUNT; configIndex++) {
                if (GT_Utility.areStacksEqual(stack, mInventory[configIndex])) {
                    return configIndex;
                }
            }
            return Integer.MAX_VALUE;
        }));

        for (int stockIndex = 0; stockIndex < SLOT_COUNT; stockIndex++) {
            stockedInventory.setStackInSlot(stockIndex, stash.get(stockIndex));
        }
    }

    /**
     * Tries to push back all the stocked items to the ME network.
     *
     * @return If successfully pushed all items
     */
    private boolean tryPushBackAllItems() {
        boolean success = true;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!tryPushBackItem(i)) {
                success = false;
            }
        }
        return success;
    }

    /**
     * Tries to push back all the stocked items to the ME network.
     *
     * @param index Index of inventory trying to push back
     * @return If successfully pushed all items
     */
    private boolean tryPushBackItem(int index) {
        return tryPushBackItem(index, Integer.MAX_VALUE);
    }

    /**
     * Tries to push back stocked item to the ME network.
     *
     * @param index           Index of inventory trying to push back
     * @param maxAmountToPush Maximum amount of items to push back
     * @return If successfully pushed all items of the required amount
     */
    private boolean tryPushBackItem(int index, int maxAmountToPush) {
        AENetworkProxy proxy = getProxy();
        ItemStack stored = stockedInventory.getStackInSlot(index);
        if (stored == null) {
            return true;
        }
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            final int originalAmount = stored.stackSize;
            final int amountToPush = Math.min(originalAmount, maxAmountToPush);
            IAEItemStack toPush = AEApi.instance()
                .storage()
                .createItemStack(stored)
                .setStackSize(amountToPush);
            if (toPush.getStackSize() == 0) {
                return true;
            }
            IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg, toPush, getRequestSource());
            if (rest != null && rest.getStackSize() > 0) {
                // Not all items are pushed. Refund the rest.
                stored.stackSize = (int) rest.getStackSize() + (originalAmount - amountToPush);
                return false;
            }
            // Successfully pushed all items.
            stored.stackSize = originalAmount - amountToPush;
            if (stored.stackSize == 0) {
                stockedInventory.setStackInSlot(index, null);
            }
            return true;
        } catch (final GridAccessException ignored) {
            return false;
        }
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == getManualSlot();
    }

    @Override
    public int getGUIHeight() {
        return 182;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (WRENCH_DRAWABLE == null) {
            WRENCH_DRAWABLE = new ItemDrawable(
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(124, 1, Materials._NULL, Materials._NULL, null))
                    .withFixedSize(16, 16, 1, 1);
        }
        buildContext.addSyncedWindow(STOCK_CONFIG_WINDOW_ID, this::createStockConfigWindow);
        buildContext.addSyncedWindow(AUTO_PULL_CONFIG_WINDOW_ID, this::createAutoPullConfigWindow);
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

                    @Override
                    public void putStack(ItemStack stack) {
                        if (stack != null) {
                            stack.stackSize = 0;
                        }
                        super.putStack(stack);
                    }
                })
                .widgetCreator(StockConfigSlotWidget::new)
                .build()
                .setPos(7, 6))
            .widget(
                SlotGroup.ofItemHandler(stockedInventory, 4)
                    .startFromSlot(0)
                    .endAtSlot(15)
                    .background(GT_UITextures.SLOT_DARK_GRAY)
                    .widgetCreator(slot -> new AESlotWidget(slot).disableInteraction())
                    .build()
                    .setPos(97, 6))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_DOUBLE)
                    .setPos(82, 27)
                    .setSize(12, 12))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (widget.isClient()) return;
                if (clickData.mouseButton == 0) {
                    setAutoPullItemList(!autoPullItemList);
                    if (autoPullItemList && widget.getContext()
                        .isWindowOpen(STOCK_CONFIG_WINDOW_ID)) {
                        widget.getContext()
                            .closeWindow(STOCK_CONFIG_WINDOW_ID);
                    }
                } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(AUTO_PULL_CONFIG_WINDOW_ID);
                }
            })
                .setBackground(() -> {
                    if (autoPullItemList) {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                            GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME };
                    } else {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                            GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED };
                    }
                })
                .addTooltips(
                    Arrays.asList(
                        StatCollector.translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip"),
                        StatCollector.translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip.1")))
                .setSize(18, 18)
                .setPos(7, 79))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullItemList, this::setAutoPullItemList))
            .widget(TextWidget.dynamicString(() -> {
                boolean isActive = isActive();
                boolean isPowered = isPowered();
                boolean isBooting = isBooting();
                EnumChatFormatting color = (isActive && isPowered) ? EnumChatFormatting.GREEN
                    : EnumChatFormatting.DARK_RED;
                return color + WailaText.getPowerState(isActive, isPowered, isBooting);
            })
                .setTextAlignment(Alignment.Center)
                .setSize(90, 9)
                .setPos(43, 84))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (NetworkUtils.isClient()) return;
                for (int i = 0; i < SLOT_COUNT; i++) {
                    mInventory[i] = null;
                }
                if (tryPushBackAllItems()) {
                    int x = getBaseMetaTileEntity().getXCoord();
                    int y = getBaseMetaTileEntity().getYCoord();
                    int z = getBaseMetaTileEntity().getZCoord();
                    World world = getBaseMetaTileEntity().getWorld();
                    Block block = world.getBlock(x, y, z);
                    int blockMeta = world.getBlockMetadata(x, y, z);

                    block.dropBlockAsItem(world, x, y, z, blockMeta, 0);
                    world.setBlock(x, y, z, Blocks.air);
                    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
                } else {
                    widget.getContext()
                        .getPlayer()
                        .addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.failed_dump"));
                }
            })
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.dismantle_machine"))
                .setBackground(GT_UITextures.BUTTON_STANDARD, WRENCH_DRAWABLE)
                .setSize(18, 18)
                .setPos(151, 79))
            .widget(
                new SlotWidget(inventoryHandler, getManualSlot())
                    // ghost slots are prioritized over manual slot
                    .setShiftClickPriority(11)
                    .setPos(79, 42));
    }

    private ModularWindow createStockConfigWindow(EntityPlayer player) {
        ModularWindow.Builder builder = createWindow();
        builder.widget(
            new FakeSyncWidget.IntegerSyncer(
                () -> stockConfigWindowSelectedIndex,
                val -> stockConfigWindowSelectedIndex = val) {

                @Override
                public void onDestroy() {
                    // Reset on window close
                    stockConfigWindowSelectedIndex = -1;
                }
            })
            .widget(
                TextWidget.localised("GT5U.machines.stocking_bus.normal_mode_config")
                    .setPos(5, 7))
            .widget(
                TextWidget.localised("GT5U.machines.stocking_bus.fetch_interval")
                    .setPos(8, 22))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                if (stockConfigWindowSelectedIndex == -1) return;
                fetchIntervals[stockConfigWindowSelectedIndex] = val;
            })
                .setGetterInt(() -> {
                    if (stockConfigWindowSelectedIndex == -1) return FETCH_INTERVAL_DEFAULT;
                    return fetchIntervals[stockConfigWindowSelectedIndex];
                })
                .setNumbers(1, Integer.MAX_VALUE)
                .setOnScrollNumbers(1, 4, 64)
                .setTextAlignment(Alignment.CenterLeft)
                .setTextColor(Color.WHITE.normal)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                .setSize(36, 14)
                .setPos(8, 32)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
            .widget(
                TextWidget.localised("GT5U.machines.stocking_bus.stack_size_limit")
                    .setPos(8, 52))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                if (stockConfigWindowSelectedIndex == -1) return;
                fetchLimits[stockConfigWindowSelectedIndex] = val;
            })
                .setGetterInt(() -> {
                    if (stockConfigWindowSelectedIndex == -1) return 0;
                    return fetchLimits[stockConfigWindowSelectedIndex];
                })
                .setNumbers(0, Integer.MAX_VALUE)
                .setOnScrollNumbers(1, 4, 64)
                .setTextAlignment(Alignment.CenterLeft)
                .setTextColor(Color.WHITE.normal)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                .setSize(64, 14)
                .setPos(8, 62)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (widget.isClient() || stockConfigWindowSelectedIndex == -1) return;
                Arrays.fill(fetchIntervals, fetchIntervals[stockConfigWindowSelectedIndex]);
                Arrays.fill(fetchLimits, fetchLimits[stockConfigWindowSelectedIndex]);
            })
                .addTooltip(StatCollector.translateToLocal("GT5U.machines.stocking_bus.apply_to_all"))
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_APPLY_TO_ALL)
                .setSize(16, 16)
                .setPos(110, 63));
        return builder.build();
    }

    private ModularWindow createAutoPullConfigWindow(EntityPlayer player) {
        ModularWindow.Builder builder = createWindow();
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.auto_pull_mode_config")
                .setPos(5, 7))
            .widget(
                TextWidget.localised("GT5U.machines.stocking_bus.fetch_interval")
                    .setPos(8, 22))
            .widget(
                new TextFieldWidget().setSetterInt(val -> autoPullInterval = val)
                    .setGetterInt(() -> autoPullInterval)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.normal)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                    .setSize(36, 14)
                    .setPos(8, 32)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
            .widget(
                TextWidget.localised("GT5U.machines.stocking_bus.min_stack_size")
                    .setPos(8, 52))
            .widget(
                new TextFieldWidget().setSetterInt(val -> minAutoPullStackSize = val)
                    .setGetterInt(() -> minAutoPullStackSize)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.normal)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                    .setSize(64, 14)
                    .setPos(8, 62)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
        return builder.build();
    }

    private ModularWindow.Builder createWindow() {
        final int width = 130;
        final int height = 83;
        final int parentWidth = getGUIWidth();
        final int parentHeight = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(width, height);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(parentWidth, parentHeight))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(parentWidth, parentHeight), new Size(width, height))
                        .add(width - 3, 0)));
        return builder;
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
        NBTTagCompound tag = accessor.getNBTData();
        boolean autopull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minStackSize");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.waila.stocking_bus.auto_pull." + (autopull ? "enabled" : "disabled")));
        if (autopull) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.stocking_bus.min_stack_size",
                    GT_Utility.formatNumbers(minSize)));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    private int stockConfigWindowSelectedIndex = -1;

    private class StockConfigSlotWidget extends SlotWidget {

        public StockConfigSlotWidget(BaseSlot slot) {
            super(slot);
        }

        @Override
        protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
            if (!getMcSlot().isEnabled()) return;
            if (!getContext().isClient() && clickData.mouseButton != 0 && cursorStack == null) {
                if (!getContext().isWindowOpen(STOCK_CONFIG_WINDOW_ID)) {
                    getContext().openSyncedWindow(STOCK_CONFIG_WINDOW_ID);
                }
                stockConfigWindowSelectedIndex = getMcSlot().getSlotIndex();
                return;
            }
            final int index = getMcSlot().getSlotIndex();
            if (cursorStack == null) {
                getMcSlot().putStack(null);
            } else {
                if (containsSuchStack(cursorStack)) return;
                getMcSlot().putStack(GT_Utility.copyAmount(1L, cursorStack));
            }
            if (getBaseMetaTileEntity().isServerSide()) {
                updateStockedItem(index);
            }
        }

        @Override
        public IDrawable[] getBackground() {
            List<IDrawable> ret = new ArrayList<>();
            if (autoPullItemList) {
                ret.add(GT_UITextures.SLOT_DARK_GRAY);
            } else {
                ret.add(ModularUITextures.ITEM_SLOT);
            }
            if (stockConfigWindowSelectedIndex == getMcSlot().getSlotIndex()) {
                ret.add(GT_UITextures.OVERLAY_SLOT_BORDER);
            }
            ret.add(GT_UITextures.OVERLAY_SLOT_ARROW_ME);
            return ret.toArray(new IDrawable[0]);
        }

        @Override
        public List<Text> getTooltip() {
            if (autoPullItemList) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(
                    Text.localised("GT5U.machines.stocking_bus.right_click_config")
                        .color(Color.WHITE.normal));
            }
        }

        @Override
        public List<String> getExtraTooltip() {
            if (autoPullItemList) {
                return Collections
                    .singletonList(StatCollector.translateToLocal("GT5U.machines.stocking_bus.cannot_set_slot"));
            } else {
                return Arrays.asList(
                    StatCollector.translateToLocal("modularui.phantom.single.clear"),
                    StatCollector.translateToLocal("GT5U.machines.stocking_bus.right_click_config"));
            }
        }

        private boolean containsSuchStack(ItemStack stack) {
            for (int i = 0; i < SLOT_COUNT; ++i) {
                if (GT_Utility.areStacksEqual(mInventory[i], stack, false)) {
                    return true;
                }
            }
            return false;
        }
    }
}
