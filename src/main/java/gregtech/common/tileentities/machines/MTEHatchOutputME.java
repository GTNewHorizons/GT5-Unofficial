package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.glodblock.github.common.item.FCBaseItemCell;
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
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.core.stats.Stats;
import appeng.items.contents.CellConfig;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.me.storage.CellInventory;
import appeng.me.storage.CellInventoryHandler;
import appeng.util.ReadableNumberConverter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchOutputME extends MTEHatchOutput implements IPowerChannelState, IMEConnectable, IDataCopyable {

    private static final long DEFAULT_CAPACITY = 128_000;
    private long baseCapacity = DEFAULT_CAPACITY;
    public static final String COPIED_DATA_IDENTIFIER = "outputHatchME";

    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    final IItemList<IAEFluidStack> fluidCache = AEApi.instance()
        .storage()
        .createFluidList();
    long lastOutputTick = 0;
    long lastInputTick = 0;
    long tickCounter = 0;
    boolean additionalConnection = false;
    EntityPlayer lastClickedPlayer = null;
    List<String> lockedFluids = new ArrayList<>();

    boolean hadCell = false;

    public MTEHatchOutputME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            4,
            new String[] { "Fluid Output for Multiblocks", "Stores directly into ME",
                "Can cache up to 128kL of fluids by default", "Change cache size by inserting a fluid storage cell",
                "Change ME connection behavior by right-clicking with wire cutter",
                "Partition the inserted Storage Cell to filter accepted outputs" },
            1);
    }

    public MTEHatchOutputME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputME(mName, mTier, mDescriptionArray, mTextures);
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
        return (byte) (GTValues.V.length - 2);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (!lockedFluids.isEmpty()) {
            boolean isOk = false;

            for (String lockedFluid : lockedFluids) {
                if (lockedFluid.equals(
                    aFluid.getFluid()
                        .getName())) {
                    isOk = true;

                    break;
                }
            }

            if (!isOk) {
                return 0;
            }
        }

        if (doFill) {
            return tryFillAE(aFluid);
        } else {
            if (aFluid == null) return 0;
            return aFluid.amount;
        }
    }

    @Override
    public boolean canStoreFluid(@NotNull FluidStack fluidStack) {
        if (!isFluidLocked()) {
            return true;
        }

        for (String lockedFluid : lockedFluids) {
            if (lockedFluid.equals(
                fluidStack.getFluid()
                    .getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isFluidLocked() {
        return this.mMode == 10;
    }

    private void checkFluidLock() {
        ItemStack upgradeItemStack = mInventory[0];

        if ((hadCell && upgradeItemStack != null) || (!hadCell && upgradeItemStack == null)) {
            return;
        }

        if (upgradeItemStack != null && upgradeItemStack.getItem() instanceof FCBaseItemCell fcbc) {
            hadCell = true;

            if (this.mMode == 0) {
                CellConfig cfg = (CellConfig) fcbc.getConfigAEInventory(upgradeItemStack);

                if (!cfg.isEmpty()) {
                    StringBuilder builder = new StringBuilder();

                    boolean hadFilters = false;
                    boolean isFirst = true;

                    lockedFluids.clear();

                    for (int i = 0; i < cfg.getSizeInventory(); i++) {
                        IAEStack<?> stack = cfg.getAEStackInSlot(i);

                        if (!(stack instanceof IAEFluidStack ifs)) continue;

                        FluidStack tFluid = ifs.getFluidStack();

                        if (tFluid != null) {
                            hadFilters = true;

                            lockedFluids.add(
                                tFluid.getFluid()
                                    .getName());

                            if (isFirst) {
                                builder.append(tFluid.getLocalizedName());

                                isFirst = false;
                            } else {
                                builder.append(", ")
                                    .append(tFluid.getLocalizedName());
                            }
                        }
                    }

                    if (hadFilters) {
                        if (lastClickedPlayer != null) {
                            GTUtility.sendChatToPlayer(
                                lastClickedPlayer,
                                StatCollector.translateToLocalFormatted("GT5U.hatch.fluid.filter.enable", builder));
                        }

                        this.mMode = 10;

                        markDirty();
                    }
                }
            }
        } else {
            hadCell = false;

            if (this.mMode == 10) {
                lockedFluids.clear();

                this.mMode = 0;

                markDirty();

                GTUtility.sendChatTrans(lastClickedPlayer, "GT5U.hatch.fluid.filter.disable");
            }
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

        if (upgradeItemStack == null) return baseCapacity;
        if (!(upgradeItemStack.getItem() instanceof FCBaseItemCell storageCell)) return baseCapacity;

        final IMEInventoryHandler<?> inventory = AEApi.instance()
            .registries()
            .cell()
            .getCellInventory(upgradeItemStack, null, StorageChannel.FLUIDS);

        long capacity = storageCell.getBytesLong(upgradeItemStack) * 2048L;

        if (inventory instanceof CellInventoryHandler<?>handler) {
            final CellInventory<?> cellInventory = (CellInventory<?>) handler.getCellInv();

            long restriction = (long) cellInventory.getRestriction()
                .get(0);

            if (restriction > 0) {
                capacity = Math.min(capacity, restriction);
            }
        }

        return capacity;
    }

    /**
     * Check if the internal cache can still fit more fluids in it for a recipe check
     */
    public boolean canAcceptFluid() {
        return getCachedAmount() < getCacheCapacity();
    }

    /**
     * Check if there is space for fluids or if we can overfill.
     */
    public boolean canFillFluid() {
        return canAcceptFluid() || lastInputTick == tickCounter;
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return mMode == 0;
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
        lastClickedPlayer = aPlayer;

        openGui(aPlayer);

        return true;
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
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
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
        if (!isActive() || fluidCache.isEmpty()) return;
        AENetworkProxy proxy = getProxy();
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

        checkFluidLock();

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
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("cacheCapacity", getCacheCapacity());
        tag.setInteger("stackCount", fluidCache.size());

        IAEFluidStack[] stacks = fluidCache.toArray(new IAEFluidStack[0]);

        Arrays.sort(
            stacks,
            Comparator.comparingLong(IAEFluidStack::getStackSize)
                .reversed());

        if (stacks.length > 10) {
            stacks = Arrays.copyOf(stacks, 10);
        }

        NBTTagList tagList = new NBTTagList();
        tag.setTag("stacks", tagList);

        for (IAEFluidStack stack : stacks) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.getFluidStack()
                .writeToNBT(stackTag);
            stackTag.setLong("Amount", stack.getStackSize());
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
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.hatch.output_me.fluid_cache_capacity",
                EnumChatFormatting.GOLD,
                GTUtility.formatNumbers(tag.getLong("cacheCapacity")),
                EnumChatFormatting.RESET));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasWailaAdvancedBody(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaAdvancedBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaAdvancedBody(itemStack, ss, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        NBTTagList stacks = tag.getTagList("stacks", 10);
        int stackCount = tag.getInteger("stackCount");

        if (stackCount == 0) {
            ss.add("This hatch has no cached fluids");
        } else {
            ss.add(
                String.format(
                    "The hatch contains %s%d%s cached fluid%s: ",
                    EnumChatFormatting.GOLD,
                    stackCount,
                    EnumChatFormatting.RESET,
                    stackCount > 1 ? "s" : ""));

            for (int i = 0; i < stacks.tagCount(); i++) {
                NBTTagCompound stackTag = stacks.getCompoundTagAt(i);
                FluidStack stack = FluidStack.loadFluidStackFromNBT(stackTag);

                ss.add(
                    String.format(
                        "%s: %s%s L%s",
                        stack.getLocalizedName(),
                        EnumChatFormatting.GOLD,
                        GTUtility.formatNumbers(stackTag.getLong("Amount")),
                        EnumChatFormatting.RESET));
            }

            if (stackCount > stacks.tagCount()) {
                ss.add(EnumChatFormatting.ITALIC + "And " + (stackCount - stacks.tagCount()) + " more...");
            }
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

        NBTTagList lockedFluidsTag = new NBTTagList();

        for (String fluid : this.lockedFluids) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            fluidTag.setString("fluid", fluid);
            lockedFluidsTag.appendTag(fluidTag);
        }

        aNBT.setTag("lockedFluids", lockedFluidsTag);

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
        aNBT.setBoolean("hadCell", hadCell);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase lockedFluidsTag = aNBT.getTag("lockedFluids");

        if (lockedFluidsTag instanceof NBTTagList lockedFluidsList) {
            for (int i = 0; i < lockedFluidsList.tagCount(); i++) {
                NBTTagCompound fluidTag = lockedFluidsList.getCompoundTagAt(i);
                this.lockedFluids.add(fluidTag.getString("fluid"));
            }
        }

        NBTBase t = aNBT.getTag("cachedFluids");
        if (t instanceof NBTTagList l) {
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound tag = l.getCompoundTagAt(i);
                NBTTagCompound tagFluidStack = tag.getCompoundTag("fluidStack");
                final IAEFluidStack s = AEApi.instance()
                    .storage()
                    .createFluidStack(GTUtility.loadFluid(tagFluidStack));
                if (s != null) {
                    s.setStackSize(tag.getLong("size"));
                    fluidCache.add(s);
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Output Hatch. This fluid has been voided: "
                            + tagFluidStack);
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
                "GT5U.infodata.hatch.output_me.cache_capacity",
                EnumChatFormatting.GOLD + GTUtility.formatNumbers(getCacheCapacity())
                    + " L"
                    + EnumChatFormatting.RESET));
        if (fluidCache.isEmpty()) {
            ss.add(StatCollector.translateToLocal("GT5U.infodata.hatch.output_me.empty"));
        } else {
            ss.add(
                StatCollector.translateToLocalFormatted("GT5U.infodata.hatch.output_me.contains", fluidCache.size()));
            int counter = 0;
            for (IAEFluidStack s : fluidCache) {
                ss.add(
                    s.getFluidStack()
                        .getLocalizedName() + ": "
                        + EnumChatFormatting.GOLD
                        + GTUtility.formatNumbers(s.getStackSize())
                        + " L"
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
