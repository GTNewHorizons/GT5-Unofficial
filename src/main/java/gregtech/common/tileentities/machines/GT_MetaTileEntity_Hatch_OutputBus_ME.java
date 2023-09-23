package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

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
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Hatch_OutputBus_ME extends GT_MetaTileEntity_Hatch_OutputBus
    implements IPowerChannelState {

    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    final IItemList<IAEItemStack> itemCache = AEApi.instance()
        .storage()
        .createItemList();
    long lastOutputTick = 0;
    long tickCounter = 0;
    boolean lastOutputFailed = false;
    boolean infiniteCache = true;
    boolean additionalConnection = false;

    public GT_MetaTileEntity_Hatch_OutputBus_ME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            1,
            new String[] { "Item Output for Multiblocks", "Stores directly into ME",
                "Can cache infinite amount of items.", "Change cache behavior by right-clicking with screwdriver.",
                "Change ME connection behavior by right-clicking with wire cutter" },
            0);
    }

    public GT_MetaTileEntity_Hatch_OutputBus_ME(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputBus_ME(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean storeAll(ItemStack aStack) {
        aStack.stackSize = store(aStack);
        return aStack.stackSize == 0;
    }

    /**
     * Attempt to store items in connected ME network. Returns how many items did not fit (if the network was down e.g.)
     *
     * @param stack input stack
     * @return amount of items left over
     */
    public int store(final ItemStack stack) {
        if (!infiniteCache && lastOutputFailed) return stack.stackSize;
        itemCache.add(
            AEApi.instance()
                .storage()
                .createItemStack(stack));
        return 0;
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .isGUIClickable()) return;
        infiniteCache = !infiniteCache;
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.infiniteCache." + infiniteCache));
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
    public void gridChanged() {}

    private void flushCachedStack() {
        lastOutputFailed = false;
        AENetworkProxy proxy = getProxy();
        if (proxy == null) {
            lastOutputFailed = true;
            return;
        }
        try {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            for (IAEItemStack s : itemCache) {
                if (s.getStackSize() == 0) continue;
                IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg, s, getRequest());
                if (rest != null && rest.getStackSize() > 0) {
                    lastOutputFailed = true;
                    s.setStackSize(rest.getStackSize());
                    break;
                }
                s.setStackSize(0);
            }
        } catch (final GridAccessException ignored) {
            lastOutputFailed = true;
        }
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (GT_Values.GT.isServerSide()) {
            tickCounter = aTick;
            if (tickCounter > (lastOutputTick + 40)) flushCachedStack();
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagList items = new NBTTagList();
        for (IAEItemStack s : itemCache) {
            if (s.getStackSize() == 0) continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("itemStack", GT_Utility.saveItem(s.getItemStack()));
            tag.setLong("size", s.getStackSize());
            items.appendTag(tag);
        }
        aNBT.setBoolean("infiniteCache", infiniteCache);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setTag("cachedItems", items);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase t = aNBT.getTag("cachedStack"); // legacy
        if (t instanceof NBTTagCompound) itemCache.add(
            AEApi.instance()
                .storage()
                .createItemStack(GT_Utility.loadItem((NBTTagCompound) t)));
        t = aNBT.getTag("cachedItems");
        if (t instanceof NBTTagList l) {
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound tag = l.getCompoundTagAt(i);
                if (!tag.hasKey("itemStack")) { // legacy #868
                    itemCache.add(
                        AEApi.instance()
                            .storage()
                            .createItemStack(GT_Utility.loadItem(l.getCompoundTagAt(i))));
                    continue;
                }
                NBTTagCompound tagItemStack = tag.getCompoundTag("itemStack");
                final IAEItemStack s = AEApi.instance()
                    .storage()
                    .createItemStack(GT_Utility.loadItem(tagItemStack));
                if (s != null) {
                    s.setStackSize(tag.getLong("size"));
                    itemCache.add(s);
                } else {
                    GT_Mod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Output Bus. This item has been voided: "
                            + tagItemStack);
                }
            }
        }
        infiniteCache = aNBT.getBoolean("infiniteCache");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        getProxy().readFromNBT(aNBT);
    }

    public boolean isLastOutputFailed() {
        return lastOutputFailed;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> ss = new ArrayList<>();
        ss.add(
            "The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
        if (itemCache.isEmpty()) {
            ss.add("The bus has no cached items");
        } else {
            IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;
            ss.add(String.format("The bus contains %d cached stacks: ", itemCache.size()));
            int counter = 0;
            for (IAEItemStack s : itemCache) {
                ss.add(
                    s.getItem()
                        .getItemStackDisplayName(s.getItemStack()) + ": "
                        + EnumChatFormatting.GOLD
                        + nc.toWideReadableForm(s.getStackSize())
                        + EnumChatFormatting.RESET);
                if (++counter > 100) break;
            }
        }
        return ss.toArray(new String[itemCache.size() + 2]);
    }

    @Override
    public boolean useModularUI() {
        return false;
    }
}
