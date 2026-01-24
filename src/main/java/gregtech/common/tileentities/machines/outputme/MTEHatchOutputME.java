package gregtech.common.tileentities.machines.outputme;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import appeng.api.AEApi;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.helpers.IPriorityHost;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.ReadableNumberConverter;
import appeng.util.item.AEFluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
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
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import gregtech.common.tileentities.machines.outputme.filter.MEFilterFluid;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchOutputME extends MTEHatchOutput implements IPowerChannelState, IMEConnectable, IDataCopyable,
    ICellContainer, IGridProxyable, IPriorityHost, MTEHatchOutputMEBase.Environment<IAEFluidStack> {

    public MTEHatchOutputME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            4,
            new String[] { "Fluid Output for Multiblocks", "Stores directly into ME",
                "Can cache up to 128kL of fluids by default", "Change cache size by inserting a fluid storage cell",
                "Change ME connection behavior by right-clicking with wire cutter",
                "Partition the inserted Storage Cell to filter accepted outputs",
                "Right click with screwdriver to toggle Cache Mode",
                "Shift right click with screwdriver to toggle Check Mode" },
            1);
    }

    private final MTEHatchOutputMEBase<IAEFluidStack, MEFilterFluid, FluidStack> provider = new MTEHatchOutputMEBase<IAEFluidStack, MEFilterFluid, FluidStack>(
        this,
        new MEFilterFluid(),
        128_000) {};

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
        provider.updateState();
    }

    @MENetworkEventSubscribe
    public void updateCell(final MENetworkChannelsChanged c) {
        provider.updateCell();
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        boolean ok = provider.storePartial(aFluid, !doFill);
        return ok ? aFluid.amount : 0;
    }

    public int tryFillAE(final FluidStack aFluid) {
        if (aFluid == null) return 0;
        if (canAcceptFluid() || (provider.getTickCounter() == provider.getLastInputTick())) {
            provider.addToCache(aFluid);
            return aFluid.amount;
        }
        return 0;
    }

    @Override
    public boolean canStoreFluid(@NotNull FluidStack fluidStack) {
        return provider.canStore(fluidStack);
    }

    @Override
    public boolean isFluidLocked() {
        return provider.isFiltered();
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    /**
     * Check if the internal cache can still fit more fluids in it for a recipe check
     */
    public boolean canAcceptFluid() {
        return provider.hasAvailableSpace();
    }

    /**
     * Check if there is space for fluids or if we can overfill.
     */
    public boolean canFillFluid() {
        return provider.canAcceptAnyInput();
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return !provider.isFiltered() && !provider.getCheckMode();
    }

    BaseActionSource requestSource;

    @Override
    public BaseActionSource getActionSource() {
        if (requestSource == null) requestSource = new MachineSource(this);
        return requestSource;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public void onFacingChange() {
        provider.updateValidGridProxySides();
    }

    EntityPlayer lastClickedPlayer = null;

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        lastClickedPlayer = aPlayer;

        openGui(aPlayer);

        return true;
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        provider.updateAE2ProxyColor();
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
        provider.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return provider.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean connectsToAllSides() {
        return provider.getAdditionalConnection();
    }

    @Override
    public void setConnectsToAllSides(boolean connects) {
        provider.setAdditionalConnection(connects);
    }

    @Override
    public AENetworkProxy getProxy() {
        return provider.getProxy();
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
        provider.onPostTick(aBaseMetaTileEntity, aTick);
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
        provider.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, ss, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        ss.add(
            String.format(
                "Fluid cache capacity: %s%s L%s",
                EnumChatFormatting.GOLD,
                formatNumber(tag.getLong("cacheCapacity")),
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
                        formatNumber(stackTag.getLong("Amount")),
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
        provider.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        provider.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        provider.loadNBTData(aNBT);

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
                    provider.addToCache(s);
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Output Hatch. This fluid has been voided: "
                            + tagFluidStack);
                }
            }
        }
    }

    @Override
    public IGridProxyable getIGridProxyable() {
        return this;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.FLUIDS;
    }

    @Override
    public ItemStack getCellStack() {
        return mInventory[0];
    }

    @Override
    public ISaveProvider getISaveProvider() {
        return this;
    }

    @Override
    public EntityPlayer getLastClickedPlayer() {
        return lastClickedPlayer;
    }

    @Override
    public IMEInventory<IAEFluidStack> getNetworkInvtory() throws GridAccessException {
        return getProxy().getStorage()
            .getFluidInventory();
    }

    @Override
    public NBTTagCompound saveStackToNBT(IAEFluidStack s) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound tagFluidStack = new NBTTagCompound();
        s.getFluidStack()
            .writeToNBT(tagFluidStack);
        tag.setTag("fluidStack", tagFluidStack);
        tag.setLong("size", s.getStackSize());
        return tag;
    }

    @Override
    public IAEFluidStack loadStackFromNBT(NBTTagCompound tag) {
        return AEFluidStack.create(GTUtility.loadFluid(tag));
    }

    public static final String COPIED_DATA_IDENTIFIER = "outputHatchME";

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public ItemStack getVisual() {
        return ItemList.Hatch_Output_ME.get(1);
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        return provider.getCopiedData(player);
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        return provider.pasteCopiedData(player, nbt);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        // Sync the hatch capacity to the client so that MM can show its exchanging preview properly
        // This is only called when the hatch is placed since it will never change over its lifetime

        provider.writeToClientPacket(tag);

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        provider.readFromClientPacket(data);
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
                EnumChatFormatting.GOLD + formatNumber(provider.getCacheCapacity()) + " L" + EnumChatFormatting.RESET));
        List<IAEFluidStack> fluidList = provider.getCacheList();
        if (fluidList.isEmpty()) {
            ss.add(StatCollector.translateToLocal("GT5U.infodata.hatch.output_me.empty"));
        } else {
            ss.add(StatCollector.translateToLocalFormatted("GT5U.infodata.hatch.output_me.contains", fluidList.size()));
            fluidList.stream()
                .limit(100)
                .forEach(s -> {
                    ss.add(
                        s.getFluidStack()
                            .getLocalizedName() + ": "
                            + EnumChatFormatting.GOLD
                            + formatNumber(s.getStackSize())
                            + " L"
                            + EnumChatFormatting.RESET);
                });
        }
        return ss.toArray(new String[Math.min(fluidList.size(), 100) + 2]);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        provider.addUIWidgets(builder, buildContext);
    }

    @Override
    public void onContentsChanged(int slot) {
        provider.onContentsChanged(slot);
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(final StorageChannel channel) {
        return provider.getCellArray(channel);
    }

    @Override
    public int getPriority() {
        return provider.getPriority();
    }

    @Override
    public void setPriority(int newValue) {
        provider.setPriority(newValue);
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        markDirty();
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    public DimensionalCoord getLocation() {
        IGregTechTileEntity gtm = this.getBaseMetaTileEntity();
        return new DimensionalCoord(gtm.getWorld(), gtm.getXCoord(), gtm.getYCoord(), gtm.getZCoord());
    }

    @Override
    public void securityBreak() {}

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        return getProxy().getNode();
    }

    public void dispatchMarkDirty() {
        this.markDirty();
    }
}
