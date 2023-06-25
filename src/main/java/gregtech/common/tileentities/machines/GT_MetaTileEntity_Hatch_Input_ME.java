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
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.ItemFluidPacket;
import com.glodblock.github.util.Util;
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
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEFluidStack;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.AESlotWidget;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/*
* TODO:
*  fix fluid update in right side
*/

public class GT_MetaTileEntity_Hatch_Input_ME extends GT_MetaTileEntity_Hatch_Input
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState {

    private final static int SLOT_COUNT = 16;
    private final FluidStack[] storedFluid = new FluidStack[SLOT_COUNT];
    private final int[] savedFluidAmount = new int[SLOT_COUNT];
    private final static int TOTAL_SLOT_COUNT = SLOT_COUNT * 2 + 2;
    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;
    private boolean processingRecipe = false;
    private boolean autoPullItemList = false;
    private int minAutoPullStackSize = 1;
    private static final int CONFIG_WINDOW_ID = 10;

    @Override
    public String[] getDescription() {
        return new String[] { "Advanced fluid input for Multiblocks", "Retrieves directly from ME",
            "Keeps " + SLOT_COUNT + " fluids types in stock",
            "Auto-Pull from ME mode will automatically stock the first " + SLOT_COUNT
                + " fluids in the ME system, updated every 5 seconds.",
            "Toggle by right-clicking with screwdriver, or use the GUI.",
            "Use the GUI to limit the minimum fluids amount for Auto-Pulling.",
            "Configuration data can be copy+pasted using a data stick." };
    }

    public GT_MetaTileEntity_Hatch_Input_ME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1);
    }

    public GT_MetaTileEntity_Hatch_Input_ME(String aName, int aSlots, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aSlots * 2 + 2, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Input_ME(mName, SLOT_COUNT, mTier, mDescriptionArray, mTextures);
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
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    ItemList.Hatch_Input_ME.get(1),
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
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (GregTech_API.mAE2) {
            return new String[] {
                "The hatch is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                    : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET };
        } else return new String[] {};
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) { // TODO:
                                                                                                                   // remove
                                                                                                                   // before
                                                                                                                   // commmit->
                                                                                                                   // finish
                                                                                                                   // this
                                                                                                                   // !
        setAutoPullItemList(!autoPullItemList);
        GT_Utility.sendChatToPlayer(aPlayer, "Automatic Item Pull " + autoPullItemList);
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
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, true, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        if (!dataStick.hasTagCompound() || !"stockingHatch".equals(dataStick.stackTagCompound.getString("type")))
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
        tag.setString("type", "stockingHatch");
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
        dataStick.setStackDisplayName("Stocking Input Hatch Configuration");
        aPlayer.addChatMessage(new ChatComponentText("Saved Config to Data Stick"));
    }

    private int getManualSlot() {
        return TOTAL_SLOT_COUNT - 1;
    }

    @Override
    public int getCircuitSlot() {
        return SLOT_COUNT * 2;
    }

    public ItemStack getCircuit() {
        return getStackInSlot(getCircuitSlot());
    }

    public ItemStack getManualItem() {
        return getStackInSlot(getManualSlot());
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
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
        if (aIndex == getCircuitSlot() || aIndex == getManualSlot()) return mInventory[aIndex];
        if (aIndex < 0 || aIndex >= SLOT_COUNT) return null;
        return super.getStackInSlot(aIndex);
    }

    public FluidStack[] getStoredFluid() {
        if (!processingRecipe) return storedFluid;

        if (GregTech_API.mAE2) {
            AENetworkProxy proxy = getProxy();
            if (proxy == null) {
                return null;
            }
            for (int i = 0; i < SLOT_COUNT; i++) {
                if (mInventory[i] == null) continue;
                try {
                    IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                        .getFluidInventory();
                    IAEFluidStack request = AEFluidStack.create(ItemFluidPacket.getFluidStack(mInventory[i]));
                    request.setStackSize(Integer.MAX_VALUE);
                    IAEFluidStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
                    if (result != null) {
                        setStoredFluid(i, result.getFluidStack());
                    } else {
                        // Request failed
                        setStoredFluid(i, null);
                    }
                } catch (final GridAccessException ignored) {
                    setStoredFluid(i, null);
                }
            }
        } else {
            // AE available but no items requested
            for (int i = 0; i < SLOT_COUNT; i++) {
                setStoredFluid(i + SLOT_COUNT, null);
            }
        }
        return storedFluid;
    }

    public void setStoredFluid(int aIndex, FluidStack aFluid) {
        markDirty();
        if (aIndex >= 0 && aIndex < SLOT_COUNT) {
            if (aFluid == null) {
                savedFluidAmount[aIndex] = 0;
                storedFluid[aIndex] = null;
            } else {
                storedFluid[aIndex] = aFluid;
                savedFluidAmount[aIndex] = aFluid.amount;
            }
        }
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public void onExplosion() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            mInventory[i] = null;
            storedFluid[i] = null;
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
                IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                    .getFluidInventory();
                Iterator<IAEFluidStack> iterator = sg.getStorageList()
                    .iterator();
                int index = 0;
                while (iterator.hasNext() && index < SLOT_COUNT) {
                    IAEFluidStack currItem = iterator.next();
                    if (currItem.getStackSize() >= minAutoPullStackSize) {
                        ItemStack itemstack = GT_Utility.copyAmount(1, convertToFluidPocket(currItem.getFluidStack()));
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
            updateInformationSlot(index, ItemFluidPacket.getFluidStack(mInventory[index + SLOT_COUNT]));
        }
    }

    public void endRecipeProcessing() {
        if (GregTech_API.mAE2) {
            for (int i = 0; i < SLOT_COUNT; ++i) {
                if (savedFluidAmount[i] != 0) {
                    FluidStack oldStack = storedFluid[i];
                    if (oldStack == null || oldStack.amount < savedFluidAmount[i]) {
                        AENetworkProxy proxy = getProxy();
                        try {
                            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                                .getFluidInventory();
                            IAEFluidStack request = AEFluidStack.create(ItemFluidPacket.getFluidStack(mInventory[i]));
                            request.setStackSize(savedFluidAmount[i] - (oldStack == null ? 0 : oldStack.amount));
                            sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                            proxy.getEnergy()
                                .extractAEPower(request.getStackSize(), Actionable.MODULATE, PowerMultiplier.CONFIG);
                            setStoredFluid(i + SLOT_COUNT, oldStack);
                        } catch (final GridAccessException ignored) {}
                    }
                    // setStoredFluid(i, null); // TODO: remove before commmit-> what is is?
                }
            }
        }
        processingRecipe = false;
    }

    public ItemStack updateInformationSlot(int aIndex, FluidStack aFluid) {
        if (GregTech_API.mAE2 && aIndex >= 0 && aIndex < SLOT_COUNT) {
            if (aFluid == null) {
                super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
            } else {
                AENetworkProxy proxy = getProxy();
                if (!proxy.isActive()) {
                    super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
                try {
                    IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                        .getFluidInventory();
                    IAEFluidStack request = AEFluidStack.create(aFluid);
                    request.setStackSize(Integer.MAX_VALUE);
                    IAEFluidStack result = sg.getStorageList()
                        .findPrecise(request);
                    ItemStack s = null;
                    if (result != null) {
                        s = convertToFluidPocket(result.getFluidStack());
                        s.stackSize = (int) result.getStackSize();
                    }
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
        final SlotWidget[] aeSlotWidgets = new SlotWidget[SLOT_COUNT];
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
                        FluidStack fluidStack;

                        if (cursorStack == null) {
                            getMcSlot().putStack(null);
                            fluidStack = null;
                        } else {
                            fluidStack = Util.getFluidFromItem(cursorStack);
                            if (fluidStack == null) return;
                            ItemStack fluidPocket = convertToFluidPocket(fluidStack);
                            if (containsSuchFluidPocket(fluidPocket)) return;
                            getMcSlot().putStack(fluidPocket);
                        }
                        setStoredFluid(aSlotIndex, fluidStack);
                        if (getBaseMetaTileEntity().isServerSide()) {
                            final ItemStack newInfo = updateInformationSlot(aSlotIndex, fluidStack);
                            aeSlotWidgets[aSlotIndex].getMcSlot()
                                .putStack(newInfo);
                        }

                    }

                    private boolean containsSuchFluidPocket(ItemStack fluidPocket) {
                        for (int i = 0; i < SLOT_COUNT; ++i) {
                            if (GT_Utility.areStacksEqual(mInventory[i], fluidPocket, false)) return true;
                        }
                        return false;
                    }
                })
                .build()
                .setPos(7, 9))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 4)
                    .startFromSlot(SLOT_COUNT)
                    .endAtSlot(31)
                    .phantom(true)
                    .background(GT_UITextures.SLOT_DARK_GRAY)
                    .widgetCreator(
                        slot -> aeSlotWidgets[slot.getSlotIndex() - SLOT_COUNT] = new AESlotWidget(slot)
                            .disableInteraction())
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

    private ItemStack convertToFluidPocket(FluidStack fluid) {
        return ItemFluidPacket.newStack(fluid)
            .setStackDisplayName(fluid.getLocalizedName());
    }

    private FluidStack convertToFluidStack(ItemStack itemStack) {
        return Util.getFluidFromItem(itemStack);
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
        boolean autoPull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minStackSize");
        currenttip.add(String.format("Auto-Pull from ME: %s", autoPull ? "Enabled" : "Disabled"));
        if (autoPull) currenttip.add(String.format("Minimum Stack Size: %d", minSize));
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
