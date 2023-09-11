package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
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
    private int minAutoPullStackSize = 1;
    private final int fetchIntervalSec = 5;
    private static final int CONFIG_WINDOW_ID = 10;
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
        if (aTimer % (fetchIntervalSec * 20) == 0) {
            if (autoPullItemList) {
                refreshAutoPullItemList();
            }
            updateAllStockedItems();
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
        aNBT.setInteger("minAutoPullStackSize", minAutoPullStackSize);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        for (int i = 16; i <= 31; i++) {
            // Migration from old save: We used to store stocked items in ghost format with `mInventory`
            mInventory[i] = null;
        }
        if (aNBT.hasKey("stockedInventory", Constants.NBT.TAG_COMPOUND)) {
            stockedInventory.deserializeNBT(aNBT.getCompoundTag("stockedInventory"));
        }
        autoPullItemList = aNBT.getBoolean("autoStock");
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
        GT_Utility.sendChatToPlayer(aPlayer, "Automatic Item Pull " + autoPullItemList);
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
        minAutoPullStackSize = nbt.getInteger("minStackSize");
        additionalConnection = nbt.getBoolean("additionalConnection");
        if (!autoPullItemList) {
            NBTTagList stockingItems = nbt.getTagList("itemsToStock", 10);
            for (int i = 0; i < stockingItems.tagCount(); i++) {
                this.mInventory[i] = GT_Utility.loadItem(stockingItems.getCompoundTagAt(i));
            }
        }
        setInventorySlotContents(getCircuitSlot(), circuit);
        setAdditionalConnectionOption();
        aPlayer.addChatMessage(new ChatComponentText("Loaded Config From Data Stick"));
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
        aPlayer.addChatMessage(new ChatComponentText("Saved Config to Data Stick"));
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
        updateAllStockedItems();
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
                    ItemStack itemstack = GT_Utility.copyAmount(1, currItem.getItemStack());
                    this.mInventory[index] = itemstack;
                    index++;
                }
            }
            for (int i = index; i < SLOT_COUNT; i++) {
                mInventory[i] = null;
            }

        } catch (final GridAccessException ignored) {}
    }

    private void updateAllStockedItems() {
        for (int index = 0; index < SLOT_COUNT; index++) {
            updateStockedItem(index);
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
            tryPushBackItem(index);
            return;
        }
        ItemStack currentStored = stockedInventory.getStackInSlot(index);
        if (currentStored != null && !GT_Utility.areStacksEqual(toPull, currentStored)) {
            // Wrong item stocked, push it back
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
            int amount = Integer.MAX_VALUE - (currentStored != null ? currentStored.stackSize : 0);
            IAEItemStack request = AEItemStack.create(toPull)
                .setStackSize(amount);
            IAEItemStack result = Platform.poweredExtraction(proxy.getEnergy(), sg, request, getRequestSource());
            ItemStack pulled = (result != null) ? result.getItemStack() : null;
            stockedInventory.insertItem(index, pulled, false);
        } catch (final GridAccessException ignored) {}
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
     * Tries to push back stocked item to the ME network.
     *
     * @param index Index of inventory trying to push back
     * @return If successfully pushed all items
     */
    private boolean tryPushBackItem(int index) {
        AENetworkProxy proxy = getProxy();
        ItemStack stored = stockedInventory.getStackInSlot(index);
        if (stored == null) {
            return true;
        }
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            IAEItemStack toPush = AEApi.instance()
                .storage()
                .createItemStack(stored);
            if (toPush.getStackSize() == 0) {
                return true;
            }
            IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg, toPush, getRequestSource());
            if (rest != null && rest.getStackSize() > 0) {
                stored.stackSize = (int) rest.getStackSize();
                return false;
            }
            stockedInventory.setStackInSlot(index, null);
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
        buildContext.addSyncedWindow(CONFIG_WINDOW_ID, this::createStackSizeConfigurationWindow);
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .phantom(true)
                .background(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_ARROW_ME)
                .widgetCreator(slot -> new SlotWidget(slot) {

                    @Override
                    protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                        if (clickData.mouseButton != 0 || autoPullItemList) return;
                        final int aSlotIndex = getMcSlot().getSlotIndex();
                        if (cursorStack == null) {
                            getMcSlot().putStack(null);
                        } else {
                            if (containsSuchStack(cursorStack)) return;
                            getMcSlot().putStack(GT_Utility.copyAmount(1L, cursorStack));
                        }
                        if (getBaseMetaTileEntity().isServerSide()) {
                            updateStockedItem(aSlotIndex);
                        }
                    }

                    private boolean containsSuchStack(ItemStack tStack) {
                        for (int i = 0; i < 16; ++i) {
                            if (GT_Utility.areStacksEqual(mInventory[i], tStack, false)) return true;
                        }
                        return false;
                    }
                })
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
                if (clickData.mouseButton == 0) {
                    setAutoPullItemList(!autoPullItemList);
                } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(CONFIG_WINDOW_ID);
                }
            })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (autoPullItemList) ret.add(GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME);
                    else ret.add(GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltips(
                    ImmutableList.of(
                        "Click to toggle automatic item pulling from ME.",
                        "Right-Click to edit minimum stack size for item pulling."))
                .setSize(18, 18)
                .setPos(7, 79))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullItemList, this::setAutoPullItemList))
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
                    .setBackground(getGUITextureSet().getItemSlot())
                    .setPos(79, 42));
    }

    protected ModularWindow createStackSizeConfigurationWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 40;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)));
        builder.widget(
            new TextWidget("Min Stack Size").setPos(3, 2)
                .setSize(74, 14))
            .widget(
                new TextFieldWidget().setSetterInt(val -> minAutoPullStackSize = val)
                    .setGetterInt(() -> minAutoPullStackSize)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(36, 18)
                    .setPos(19, 18)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
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
        NBTTagCompound tag = accessor.getNBTData();
        boolean autopull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minStackSize");
        currenttip.add(String.format("Auto-Pull from ME: %s", autopull ? "Enabled" : "Disabled"));
        if (autopull) currenttip.add(String.format("Minimum Stack Size: %d", minSize));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }
}
