package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.forge.ItemHandlerHelper;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import appeng.api.AEApi;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.items.contents.CellConfig;
import appeng.items.storage.ItemBasicStorageCell;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchOutputBusME extends MTEHatchOutputBus implements IPowerChannelState, IMEConnectable {

    protected static final long DEFAULT_CAPACITY = 1_600;
    protected long baseCapacity = DEFAULT_CAPACITY;
    public static final String COPIED_DATA_IDENTIFIER = "outputBusME";

    protected BaseActionSource requestSource = null;
    protected @Nullable AENetworkProxy gridProxy = null;
    final IItemList<IAEItemStack> itemCache = AEApi.instance()
        .storage()
        .createItemList();
    long lastOutputTick = 0;
    long lastInputTick = 0;
    long tickCounter = 0;
    boolean additionalConnection = false;
    EntityPlayer lastClickedPlayer = null;
    List<ItemStack> lockedItems = new ArrayList<>();

    boolean hadCell = false;

    public MTEHatchOutputBusME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            3,
            new String[] { "Item Output for Multiblocks", "Stores directly into ME", "Can cache 1600 items by default",
                "Change cache size by inserting a storage cell",
                "Change ME connection behavior by right-clicking with wire cutter",
                "Partition the inserted Storage Cell to filter accepted outputs" },
            1);
    }

    public MTEHatchOutputBusME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    public List<ItemStack> getLockedItems() {
        return lockedItems;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBusME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_HATCH) };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        if (!lockedItems.isEmpty()) {
            boolean isOk = false;

            for (ItemStack lockedItem : lockedItems) {
                if (lockedItem.isItemEqual(stack)) {
                    isOk = true;

                    break;
                }
            }

            if (!isOk) {
                return false;
            }
        }

        // Always allow insertion on the same tick so we can output the entire recipe
        if (canAcceptItem() || (lastInputTick == tickCounter)) {
            if (!simulate) {
                itemCache.add(
                    AEApi.instance()
                        .storage()
                        .createItemStack(stack));
                lastInputTick = tickCounter;
            }
            stack.stackSize = 0;
            return true;
        }

        return false;
    }

    protected long getCachedAmount() {
        long itemAmount = 0;
        for (IAEItemStack item : itemCache) {
            itemAmount += item.getStackSize();
        }
        return itemAmount;
    }

    protected long getCacheCapacity() {
        ItemStack upgradeItemStack = mInventory[0];
        if (upgradeItemStack != null && upgradeItemStack.getItem() instanceof ItemBasicStorageCell) {
            return ((ItemBasicStorageCell) upgradeItemStack.getItem()).getBytesLong(upgradeItemStack) * 8;
        }
        return baseCapacity;
    }

    /**
     * Check if the internal cache can still fit more items in it
     */
    public boolean canAcceptItem() {
        return getCachedAmount() < getCacheCapacity();
    }

    protected BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        this.lastClickedPlayer = aPlayer;

        openGui(aPlayer);

        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
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
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    ItemList.Hatch_Output_Bus_ME.get(1),
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

    protected void flushCachedStack() {
        if (!isActive() || itemCache.isEmpty()) return;
        AENetworkProxy proxy = getProxy();
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            for (IAEItemStack s : itemCache) {
                if (s.getStackSize() == 0) continue;
                IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg, s, getRequest());
                if (rest != null && rest.getStackSize() > 0) {
                    s.setStackSize(rest.getStackSize());
                    continue;
                }
                s.setStackSize(0);
            }
        } catch (final GridAccessException ignored) {}
        lastOutputTick = tickCounter;
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
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean pushOutputInventory() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        checkItemLock();

        if (getBaseMetaTileEntity().isServerSide()) {
            tickCounter = aTick;
            if (tickCounter > (lastOutputTick + 40)) flushCachedStack();
            if (tickCounter % 20 == 0) getBaseMetaTileEntity().setActive(isActive());
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
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
    public boolean isLocked() {
        return !this.lockedItems.isEmpty();
    }

    private void checkItemLock() {
        ItemStack upgradeItemStack = mInventory[0];

        if ((hadCell && upgradeItemStack != null) || (!hadCell && upgradeItemStack == null)) {
            return;
        }

        if (upgradeItemStack != null && upgradeItemStack.getItem() instanceof ItemBasicStorageCell) {
            hadCell = true;

            if (this.lockedItems.isEmpty()) {
                CellConfig cfg = (CellConfig) ((ItemBasicStorageCell) upgradeItemStack.getItem())
                    .getConfigInventory(upgradeItemStack);

                if (!cfg.isEmpty()) {
                    StringBuilder builder = new StringBuilder();

                    boolean hadFilters = false;
                    boolean isFirst = true;

                    for (int i = 0; i < cfg.getSizeInventory(); i++) {
                        ItemStack stack = cfg.getStackInSlot(i);

                        if (stack != null) {
                            hadFilters = true;

                            lockedItems.add(ItemHandlerHelper.copyStackWithSize(stack, 1));

                            if (isFirst) {
                                builder.append(stack.getDisplayName());

                                isFirst = false;
                            } else {
                                builder.append(", ")
                                    .append(stack.getDisplayName());
                            }
                        }
                    }

                    if (hadFilters) {
                        if (lastClickedPlayer != null) {
                            GTUtility.sendChatToPlayer(
                                lastClickedPlayer,
                                StatCollector.translateToLocalFormatted("GT5U.hatch.item.filter.enable", builder));
                        }

                        markDirty();
                    }
                }
            }
        } else {
            hadCell = false;

            if (!this.lockedItems.isEmpty()) {
                this.lockedItems.clear();

                markDirty();

                GTUtility.sendChatToPlayer(
                    lastClickedPlayer,
                    StatCollector.translateToLocal("GT5U.hatch.item.filter.disable"));
            }
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("baseCapacity")) {
            tooltip.add(
                "Current cache capacity: " + EnumChatFormatting.YELLOW
                    + ReadableNumberConverter.INSTANCE
                        .toWideReadableForm(stack.stackTagCompound.getLong("baseCapacity")));
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (baseCapacity != DEFAULT_CAPACITY) aNBT.setLong("baseCapacity", baseCapacity);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagList lockedItemsTag = new NBTTagList();

        for (ItemStack stack : this.lockedItems) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            lockedItemsTag.appendTag(stackTag);
        }

        aNBT.setTag("lockedItems", lockedItemsTag);

        NBTTagList items = new NBTTagList();
        for (IAEItemStack s : itemCache) {
            if (s.getStackSize() == 0) continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("itemStack", GTUtility.saveItem(s.getItemStack()));
            tag.setLong("size", s.getStackSize());
            items.appendTag(tag);
        }
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setTag("cachedItems", items);
        aNBT.setLong("baseCapacity", baseCapacity);
        aNBT.setBoolean("hadCell", hadCell);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase lockedItemsTag = aNBT.getTag("lockedItems");

        if (lockedItemsTag instanceof NBTTagList lockedItemsList) {
            for (int i = 0; i < lockedItemsList.tagCount(); i++) {
                NBTTagCompound stackTag = lockedItemsList.getCompoundTagAt(i);
                this.lockedItems.add(GTUtility.loadItem(stackTag));
            }
        }

        NBTBase t = aNBT.getTag("cachedStack"); // legacy
        if (t instanceof NBTTagCompound) itemCache.add(
            AEApi.instance()
                .storage()
                .createItemStack(GTUtility.loadItem((NBTTagCompound) t)));
        t = aNBT.getTag("cachedItems");
        if (t instanceof NBTTagList l) {
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound tag = l.getCompoundTagAt(i);
                if (!tag.hasKey("itemStack")) { // legacy #868
                    itemCache.add(
                        AEApi.instance()
                            .storage()
                            .createItemStack(GTUtility.loadItem(l.getCompoundTagAt(i))));
                    continue;
                }
                NBTTagCompound tagItemStack = tag.getCompoundTag("itemStack");
                final IAEItemStack s = AEApi.instance()
                    .storage()
                    .createItemStack(GTUtility.loadItem(tagItemStack));
                if (s != null) {
                    s.setStackSize(tag.getLong("size"));
                    itemCache.add(s);
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Output Bus. This item has been voided: "
                            + tagItemStack);
                }
            }
        }
        additionalConnection = aNBT.getBoolean("additionalConnection");
        baseCapacity = aNBT.getLong("baseCapacity");
        if (baseCapacity == 0) baseCapacity = DEFAULT_CAPACITY;
        hadCell = aNBT.getBoolean("hadCell");
        getProxy().readFromNBT(aNBT);
        updateAE2ProxyColor();
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setByte("color", this.getColor());
        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) return false;
        additionalConnection = nbt.getBoolean("additionalConnection");
        updateValidGridProxySides();
        byte color = nbt.getByte("color");
        this.getBaseMetaTileEntity()
            .setColorization(color);

        return true;
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        // Sync the hatch capacity to the client so that MM can show its exchanging preview properly
        // This is only called when the hatch is placed since it will never change over its lifetime

        tag.setLong("baseCapacity", baseCapacity);

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        baseCapacity = data.getLong("baseCapacity");
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("cacheCapacity", getCacheCapacity());
        tag.setInteger("stackCount", itemCache.size());

        IAEItemStack[] stacks = itemCache.toArray(new IAEItemStack[0]);

        Arrays.sort(
            stacks,
            Comparator.comparingLong(IAEItemStack::getStackSize)
                .reversed());

        if (stacks.length > 10) {
            stacks = Arrays.copyOf(stacks, 10);
        }

        NBTTagList tagList = new NBTTagList();
        tag.setTag("stacks", tagList);

        for (IAEItemStack stack : stacks) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            tagList.appendTag(stackTag);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, ss, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        ss.add(
            String.format(
                "Item cache capacity: %s%s%s",
                EnumChatFormatting.GOLD,
                GTUtility.formatNumbers(tag.getLong("cacheCapacity")),
                EnumChatFormatting.RESET));

        if (!GuiScreen.isShiftKeyDown()) {
            ss.add("Hold Shift for more info");
            return;
        }

        NBTTagList stacks = tag.getTagList("stacks", 10);
        int stackCount = tag.getInteger("stackCount");

        if (stackCount == 0) {
            ss.add("This bus has no cached stacks");
        } else {
            ss.add(
                String.format(
                    "The bus contains %s%d%s cached stack%s: ",
                    EnumChatFormatting.GOLD,
                    stackCount,
                    EnumChatFormatting.RESET,
                    stackCount > 1 ? "s" : ""));

            for (int i = 0; i < stacks.tagCount(); i++) {
                IAEItemStack stack = AEItemStack.loadItemStackFromNBT(stacks.getCompoundTagAt(i));

                ss.add(
                    String.format(
                        "%s: %s%s%s",
                        stack.getItemStack()
                            .getDisplayName(),
                        EnumChatFormatting.GOLD,
                        GTUtility.formatNumbers(stack.getStackSize()),
                        EnumChatFormatting.RESET));
            }

            if (stackCount > stacks.tagCount()) {
                ss.add(EnumChatFormatting.ITALIC + "And " + (stackCount - stacks.tagCount()) + " more...");
            }
        }
    }

    @Override
    public String[] getInfoData() {
        List<String> ss = new ArrayList<>();
        ss.add(
            (getProxy() != null && getProxy().isActive())
                ? StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.bus.online")
                : StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.crafting_input_me.bus.offline",
                    getAEDiagnostics()));
        ss.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.hatch.output_bus_me.cache_capacity",
                EnumChatFormatting.GOLD + GTUtility.formatNumbers(getCacheCapacity()) + EnumChatFormatting.RESET));
        if (itemCache.isEmpty()) {
            ss.add(StatCollector.translateToLocal("GT5U.infodata.hatch.output_bus_me.empty"));
        } else {
            ss.add(
                StatCollector
                    .translateToLocalFormatted("GT5U.infodata.hatch.output_bus_me.contains", itemCache.size()));
            int counter = 0;
            for (IAEItemStack s : itemCache) {
                ss.add(
                    s.getItem()
                        .getItemStackDisplayName(s.getItemStack()) + ": "
                        + EnumChatFormatting.GOLD
                        + GTUtility.formatNumbers(s.getStackSize())
                        + EnumChatFormatting.RESET);
                if (++counter > 100) break;
            }
        }
        return ss.toArray(new String[itemCache.size() + 2]);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add1by1Slot(builder);
    }

    @Override
    public boolean acceptsItemLock() {
        return false;
    }
}
