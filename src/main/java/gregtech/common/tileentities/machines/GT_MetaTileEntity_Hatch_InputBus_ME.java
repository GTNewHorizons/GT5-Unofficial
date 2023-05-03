package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

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
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
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
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_InputBus_ME extends GT_MetaTileEntity_Hatch_InputBus
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState {

    private static final int SLOT_COUNT = 16;
    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;
    private final ItemStack[] shadowInventory = new ItemStack[SLOT_COUNT];
    private final int[] savedStackSizes = new int[SLOT_COUNT];
    private boolean processingRecipe = false;
    private boolean autoPullItemList = false;
    private int minAutoPullStackSize = 1;
    private static final int CONFIG_WINDOW_ID = 10;

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
        if (aTimer % 100 == 0 && autoPullItemList) {
            refreshItemList();
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
        return isOutputFacing((byte) forgeDirection.ordinal()) ? AECableType.SMART : AECableType.NONE;
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
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    @Override
    public void gridChanged() {}

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
        if (GregTech_API.mAE2) {
            gridProxy.writeToNBT(aNBT);
        }
    }

    private void setAutoPullItemList(boolean pullItemList) {
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
        if (GregTech_API.mAE2) {
            getProxy().readFromNBT(aNBT);
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (GregTech_API.mAE2) {
            return new String[] {
                "The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                    : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET };
        } else return new String[] {};
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setAutoPullItemList(!autoPullItemList);
        GT_Utility.sendChatToPlayer(aPlayer, "Automatic Item Pull " + autoPullItemList);
    }

    @Override
    public void updateSlots() {
        if (mInventory[getManualSlot()] != null && mInventory[getManualSlot()].stackSize <= 0)
            mInventory[getManualSlot()] = null;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
        float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, true, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
        if (!dataStick.hasTagCompound() || !"stockingBus".equals(dataStick.stackTagCompound.getString("type")))
            return false;

        NBTTagCompound nbt = dataStick.stackTagCompound;

        ItemStack circuit = GT_Utility.loadItem(dataStick.stackTagCompound, "circuit");
        if (GT_Utility.isStackInvalid(circuit)) circuit = null;
        setAutoPullItemList(nbt.getBoolean("autoPull"));
        minAutoPullStackSize = nbt.getInteger("minStackSize");
        if (!autoPullItemList) {
            NBTTagList stockingItems = nbt.getTagList("itemsToStock", 10);
            for (int i = 0; i < stockingItems.tagCount(); i++) {
                this.mInventory[i] = GT_Utility.loadItem(stockingItems.getCompoundTagAt(i));
            }
        }
        setInventorySlotContents(getCircuitSlot(), circuit);
        aPlayer.addChatMessage(new ChatComponentText("Loaded Config From Data Stick"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, true, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "stockingBus");
        tag.setBoolean("autoPull", autoPullItemList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
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
        return 64;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return aIndex != getManualSlot();
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (!processingRecipe) return super.getStackInSlot(aIndex);
        if (aIndex < 0 || aIndex > mInventory.length) return null;
        if (aIndex >= SLOT_COUNT && aIndex < SLOT_COUNT * 2)
            // Display slots
            return null;
        if (aIndex == getCircuitSlot() || aIndex == getManualSlot()) return mInventory[aIndex];
        if (GregTech_API.mAE2 && mInventory[aIndex] != null) {
            AENetworkProxy proxy = getProxy();
            if (proxy == null) {
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
    }

    private void refreshItemList() {
        if (GregTech_API.mAE2) {
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
    }

    private void updateAllInformationSlots() {
        for (int index = 0; index < SLOT_COUNT; index++) {
            updateInformationSlot(index, mInventory[index]);
        }
    }

    @Override
    public void endRecipeProcessing() {
        if (GregTech_API.mAE2) {
            for (int i = 0; i < SLOT_COUNT; ++i) {
                if (savedStackSizes[i] != 0) {
                    ItemStack oldStack = shadowInventory[i];
                    if (oldStack == null || oldStack.stackSize < savedStackSizes[i]) {
                        AENetworkProxy proxy = getProxy();
                        try {
                            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                                .getItemInventory();
                            IAEItemStack request = AEItemStack.create(mInventory[i]);
                            request.setStackSize(savedStackSizes[i] - (oldStack == null ? 0 : oldStack.stackSize));
                            sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                            proxy.getEnergy()
                                .extractAEPower(request.getStackSize(), Actionable.MODULATE, PowerMultiplier.CONFIG);
                            setInventorySlotContents(i + SLOT_COUNT, oldStack);
                        } catch (final GridAccessException ignored) {}
                    }
                    savedStackSizes[i] = 0;
                    shadowInventory[i] = null;
                }
            }
        }
        processingRecipe = false;
    }

    public ItemStack updateInformationSlot(int aIndex, ItemStack aStack) {
        if (GregTech_API.mAE2 && aIndex >= 0 && aIndex < SLOT_COUNT) {
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
                    setInventorySlotContents(aIndex + SLOT_COUNT, s);
                    return s;
                } catch (final GridAccessException ignored) {}
            }
        }
        return null;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == getManualSlot();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final SlotWidget[] aeSlotWidgets = new SlotWidget[16];
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
                            final ItemStack newInfo = updateInformationSlot(aSlotIndex, cursorStack);
                            aeSlotWidgets[getMcSlot().getSlotIndex()].getMcSlot()
                                .putStack(newInfo);
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
                .setPos(7, 9))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 4)
                    .startFromSlot(16)
                    .endAtSlot(31)
                    .phantom(true)
                    .background(GT_UITextures.SLOT_DARK_GRAY)
                    .widgetCreator(
                        slot -> aeSlotWidgets[slot.getSlotIndex() - 16] = new AESlotWidget(slot).disableInteraction())
                    .build()
                    .setPos(97, 9))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_DOUBLE)
                    .setPos(82, 30)
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
                .setSize(16, 16)
                .setPos(80, 10))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullItemList, this::setAutoPullItemList))
            .widget(
                new SlotWidget(inventoryHandler, getManualSlot()).setBackground(getGUITextureSet().getItemSlot())
                    .setPos(79, 45));
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
