package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.core.stats.Stats;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.ReadableNumberConverter;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Hatch_Output_ME extends GT_MetaTileEntity_Hatch_Output implements IPowerChannelState {

    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;
    IItemList<IAEFluidStack> fluidCache = GregTech_API.mAE2 ? AEApi.instance()
                                                                   .storage()
                                                                   .createFluidList()
            : null;
    long lastOutputTick = 0;
    long tickCounter = 0;
    boolean lastOutputFailed = false;
    boolean infiniteCache = true;

    public GT_MetaTileEntity_Hatch_Output_ME(int aID, String aName, String aNameRegional) {
        super(
                aID,
                aName,
                aNameRegional,
                1,
                new String[] { "Fluid Output for Multiblocks", "Stores directly into ME", },
                0);
    }

    public GT_MetaTileEntity_Hatch_Output_ME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Output_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH_ACTIVE) };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (!GregTech_API.mAE2) return 0;
        if (doFill) {
            return tryFillAE(aFluid);
        } else {
            if ((!infiniteCache && lastOutputFailed) || aFluid == null) return 0;
            return aFluid.amount;
        }
    }

    /**
     * Attempt to store fluid in connected ME network. Returns how much fluid is accepted (if the network was down e.g.)
     *
     * @param aFluid input fluid
     * @return amount of fluid filled
     */
    public int tryFillAE(final FluidStack aFluid) {
        if ((!infiniteCache && lastOutputFailed) || aFluid == null) return 0;
        fluidCache.add(
                AEApi.instance()
                     .storage()
                     .createFluidStack(aFluid));
        return aFluid.amount;
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing((byte) forgeDirection.ordinal()) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public void updateFluidDisplayItem() {}

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Don't allow to lock fluid in me fluid hatch
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(aSide)
                                    .isGUIClickable())
            return;
        infiniteCache = !infiniteCache;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.hatch.infiniteCacheFluid." + infiniteCache));
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                        (IGridProxyable) getBaseMetaTileEntity(),
                        "proxy",
                        ItemList.Hatch_Output_ME.get(1),
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

    private void flushCachedStack() {
        lastOutputFailed = false;
        AENetworkProxy proxy = getProxy();
        if (proxy == null) {
            lastOutputFailed = true;
            return;
        }
        try {
            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                                                .getFluidInventory();
            for (IAEFluidStack s : fluidCache) {
                if (s.getStackSize() == 0) continue;
                IAEFluidStack rest = fluidAEInsert(proxy.getEnergy(), sg, s, getRequest());
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
        if (GregTech_API.mAE2) {
            NBTTagList fluids = new NBTTagList();
            for (IAEFluidStack s : fluidCache) {
                if (s.getStackSize() == 0) continue;
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagCompound tagFluidStack = new NBTTagCompound();
                s.getFluidStack()
                 .writeToNBT(tagFluidStack);
                tag.setTag("fluidStack", tagFluidStack);
                tag.setLong("size", s.getStackSize());
                fluids.appendTag(tag);
            }
            aNBT.setTag("cachedFluids", fluids);
            gridProxy.writeToNBT(aNBT);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (GregTech_API.mAE2) {
            NBTBase t = aNBT.getTag("cachedFluids");
            if (t instanceof NBTTagList) {
                NBTTagList l = (NBTTagList) t;
                for (int i = 0; i < l.tagCount(); ++i) {
                    NBTTagCompound tag = l.getCompoundTagAt(i);
                    NBTTagCompound tagFluidStack = tag.getCompoundTag("fluidStack");
                    final IAEFluidStack s = AEApi.instance()
                                                 .storage()
                                                 .createFluidStack(GT_Utility.loadFluid(tagFluidStack));
                    if (s != null) {
                        s.setStackSize(tag.getLong("size"));
                        fluidCache.add(s);
                    } else {
                        GT_Mod.GT_FML_LOGGER.warn(
                                "An error occurred while loading contents of ME Output Hatch, some fluids have been voided");
                    }
                }
            }
            getProxy().readFromNBT(aNBT);
        }
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
        if (!GregTech_API.mAE2) return new String[] {};
        List<String> ss = new ArrayList<>();
        ss.add(
                "The hatch is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                        : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
        if (fluidCache.isEmpty()) {
            ss.add("The bus has no cached fluids");
        } else {
            IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;
            ss.add(String.format("The hatch contains %d cached fluids: ", fluidCache.size()));
            int counter = 0;
            for (IAEFluidStack s : fluidCache) {
                ss.add(
                        s.getFluidStack()
                         .getLocalizedName() + ": "
                                + EnumChatFormatting.GOLD
                                + nc.toWideReadableForm(s.getStackSize())
                                + " mB"
                                + EnumChatFormatting.RESET);
                if (++counter > 100) break;
            }
        }
        return ss.toArray(new String[fluidCache.size() + 2]);
    }

    public static IAEFluidStack fluidAEInsert(final IEnergySource energy, final IMEInventory<IAEFluidStack> cell,
            final IAEFluidStack input, final BaseActionSource src) {
        final IAEFluidStack possible = cell.injectItems(input.copy(), Actionable.SIMULATE, src);

        long stored = input.getStackSize();
        if (possible != null) {
            stored -= possible.getStackSize();
        }
        // 1000 mb fluid will be considered as 1 item
        long power = Math.max(1, stored / 1000);

        final double availablePower = energy.extractAEPower(power, Actionable.SIMULATE, PowerMultiplier.CONFIG);

        final long itemToAdd = Math.min((long) (availablePower + 0.9) * 1000, stored);

        if (itemToAdd > 0) {
            energy.extractAEPower(power, Actionable.MODULATE, PowerMultiplier.CONFIG);

            if (itemToAdd < input.getStackSize()) {
                final long original = input.getStackSize();
                final IAEFluidStack split = input.copy();
                split.decStackSize(itemToAdd);
                input.setStackSize(itemToAdd);
                split.add(cell.injectItems(input, Actionable.MODULATE, src));

                if (src.isPlayer()) {
                    final long diff = original - split.getStackSize();
                    Stats.ItemsInserted.addToPlayer(((PlayerSource) src).player, (int) diff);
                }

                return split;
            }

            final IAEFluidStack ret = cell.injectItems(input, Actionable.MODULATE, src);

            if (src.isPlayer()) {
                final long diff = ret == null ? input.getStackSize() : input.getStackSize() - ret.getStackSize();
                Stats.ItemsInserted.addToPlayer(((PlayerSource) src).player, (int) diff);
            }

            return ret;
        }

        return input;
    }
}
