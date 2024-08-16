package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH_ACTIVE;

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
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.FCBaseItemCell;
import com.glodblock.github.common.storage.IStorageFluidCell;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

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
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Hatch_Output_ME extends GT_MetaTileEntity_Hatch_Output implements IPowerChannelState {

    private long baseCapacity = 128_000;

    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    final IItemList<IAEFluidStack> fluidCache = AEApi.instance()
        .storage()
        .createFluidList();
    long lastOutputTick = 0;
    long lastInputTick = 0;
    long tickCounter = 0;
    boolean additionalConnection = false;

    public GT_MetaTileEntity_Hatch_Output_ME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            3,
            new String[] { "Fluid Output for Multiblocks", "Stores directly into ME",
                "Can cache up to 128kL of fluids by default", "Change cache size by inserting a fluid storage cell",
                "Change ME connection behavior by right-clicking with wire cutter" },
            1);
    }

    public GT_MetaTileEntity_Hatch_Output_ME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Output_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH) };
    }

    @Override
    public byte getTierForStructure() {
        return (byte) (GT_Values.V.length - 2);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (doFill) {
            return tryFillAE(aFluid);
        } else {
            if (aFluid == null) return 0;
            return aFluid.amount;
        }
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    private long getCachedAmount() {
        long fluidAmount = 0;
        for (IAEFluidStack fluid : fluidCache) {
            fluidAmount += fluid.getStackSize();
        }
        return fluidAmount;
    }

    private long getCacheCapacity() {
        ItemStack upgradeItemStack = mInventory[0];
        if (upgradeItemStack != null && upgradeItemStack.getItem() instanceof IStorageFluidCell) {
            return ((FCBaseItemCell) upgradeItemStack.getItem()).getBytes(upgradeItemStack) * 2048;
        }
        return baseCapacity;
    }

    /**
     * Check if the internal cache can still fit more fluids in it
     */
    public boolean canAcceptFluid() {
        if (getCachedAmount() < getCacheCapacity()) {
            return true;
        }
        return false;
    }

    /**
     * Attempt to store fluid in connected ME network. Returns how much fluid is accepted (if the network was down e.g.)
     *
     * @param aFluid input fluid
     * @return amount of fluid filled
     */
    public int tryFillAE(final FluidStack aFluid) {
        if (aFluid == null) return 0;
        // Always allow insertion on the same tick so we can output the entire recipe
        if (canAcceptFluid() || (lastInputTick == tickCounter)) {
            fluidCache.add(
                AEApi.instance()
                    .storage()
                    .createFluidStack(aFluid));
            lastInputTick = tickCounter;
            return aFluid.amount;
        }
        return 0;
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection side) {
        return isOutputFacing(side) ? AECableType.SMART : AECableType.NONE;
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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Don't allow to lock fluid in me fluid hatch
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .isGUIClickable()) return;
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
                    ItemList.Hatch_Output_ME.get(1),
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

    private void flushCachedStack() {
        if (fluidCache.isEmpty()) return;
        AENetworkProxy proxy = getProxy();
        if (proxy == null) {
            return;
        }
        try {
            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                .getFluidInventory();
            for (IAEFluidStack s : fluidCache) {
                if (s.getStackSize() == 0) continue;
                IAEFluidStack rest = fluidAEInsert(proxy.getEnergy(), sg, s, getRequest());
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (getBaseMetaTileEntity().isServerSide()) {
            tickCounter = aTick;
            if (tickCounter > (lastOutputTick + 40)) flushCachedStack();
            if (tickCounter % 20 == 0) getBaseMetaTileEntity().setActive(isActive());
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("baseCapacity")) {
            tooltip.add(
                "Current cache capacity: " + EnumChatFormatting.YELLOW
                    + ReadableNumberConverter.INSTANCE
                        .toWideReadableForm(stack.stackTagCompound.getLong("baseCapacity"))
                    + "L");
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setLong("baseCapacity", baseCapacity);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
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
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setLong("baseCapacity", baseCapacity);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTBase t = aNBT.getTag("cachedFluids");
        if (t instanceof NBTTagList l) {
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
                        "An error occurred while loading contents of ME Output Hatch. This fluid has been voided: "
                            + tagFluidStack);
                }
            }
        }
        additionalConnection = aNBT.getBoolean("additionalConnection");
        baseCapacity = aNBT.getLong("baseCapacity");
        // Set the base capacity of existing hatches to be infinite
        if (baseCapacity == 0) {
            baseCapacity = Long.MAX_VALUE;
        }
        getProxy().readFromNBT(aNBT);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> ss = new ArrayList<>();
        ss.add(
            "The hatch is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
        IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;
        ss.add("Fluid cache capacity: " + nc.toWideReadableForm(getCacheCapacity()) + " mB");
        if (fluidCache.isEmpty()) {
            ss.add("The bus has no cached fluids");
        } else {
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

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add1by1Slot(builder);
    }
}
