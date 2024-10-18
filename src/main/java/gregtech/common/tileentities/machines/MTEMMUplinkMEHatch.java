package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import appeng.api.config.Actionable;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MTEMMUplinkMEHatch extends MTEHatch implements IGridProxyable, IPowerChannelState, ICraftingProvider, ICraftingRequester {

    public static final long REQUEST_TIMEOUT = 20 * 60;

    protected BaseActionSource requestSource = null;
    protected AENetworkProxy gridProxy = null;

    private final List<ManipulatorRequest> manualRequests = new ArrayList<>();
    private final List<ManipulatorRequest> autoRequests = new ArrayList<>();

    public MTEMMUplinkMEHatch(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            8,
            0,
            new String[] { "Matter Manipulator Uplink ME connector hatch." });
    }

    public MTEMMUplinkMEHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMMUplinkMEHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_FLUID_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_FLUID_HATCH) };
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);

        if (getBaseMetaTileEntity().isServerSide()) {
            if (aTimer % 20 == 0) {
                boolean isActive = isActive();

                getBaseMetaTileEntity().setActive(isActive);

                if (isActive && !autoRequests.isEmpty()) {
                    pollRequests();
                }
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    private void pollRequests() {
        ICraftingGrid cg;

        try {
            cg = getProxy().getCrafting();
        } catch (GridAccessException e) {
            // :P
            return;
        }

        Iterator<ManipulatorRequest> iter = autoRequests.iterator();

        while (iter.hasNext()) {
            ManipulatorRequest request = iter.next();

            if (!request.poll()) {
                GTUtility.sendChatToPlayer(request.requester, "Craft for plan " + request.requestName + " failed.");
                iter.remove();
                onRequestsChanged();
            }
        }
    }

    private void updateValidGridProxySides() {
        getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
    }

    @Override
    public void onFacingChange() {
        updateValidGridProxySides();
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    this,
                    "proxy",
                    ItemList.Hatch_MatterManipulatorUplink_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) {
                    gridProxy.setOwner(getBaseMetaTileEntity().getWorld().getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
                }
            }
        }
        return this.gridProxy;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getProxy().getNode();
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    public void securityBreak() {}

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        ImmutableSet.Builder<ICraftingLink> jobs = ImmutableSet.builder();

        for (ManipulatorRequest request : autoRequests) {
            if (request.job != null) jobs.add(request.link);
        }

        return jobs.build();
    }

    @Override
    public IAEItemStack injectCraftedItems(ICraftingLink link, IAEItemStack items, Actionable mode) {
        return items;
    }

    @Override
    public void jobStateChange(ICraftingLink link) {
        
    }

    public IGrid getGrid() {
        try {
            return getProxy().getGrid();
        } catch (GridAccessException e) {
            return null;
        }
    }

    public IStorageGrid getStorageGrid() {
        IGrid grid = getGrid();

        if (grid == null) return null;

        return grid.getCache(IStorageGrid.class);
    }

    public ICraftingGrid getCraftingGrid() {
        IGrid grid = getGrid();

        if (grid == null) return null;

        return grid.getCache(ICraftingGrid.class);
    }

    public BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource(this);
        return requestSource;
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

        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        
        getProxy().readFromNBT(aNBT);
    }

    private List<ItemStack> pendingCraft;

    private void pushPendingCraft() {
        if (pendingCraft == null) return;

        IStorageGrid grid = getStorageGrid();

        if (grid == null) return;

        IMEMonitor<IAEItemStack> itemInventory = grid.getItemInventory();

        if (itemInventory == null) return;

        Iterator<ItemStack> iter = pendingCraft.iterator();

        while (iter.hasNext()) {
            ItemStack current = iter.next();

            IAEItemStack result = itemInventory.injectItems(AEItemStack.create(current), Actionable.MODULATE, getRequestSource());

            if (result != null) {
                current.stackSize = (int) result.getStackSize();
            } else {
                iter.remove();
            }
        }

        if (pendingCraft.isEmpty()) {
            pendingCraft = null;
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        pushPendingCraft();
        
        if (isBusy()) {
            return false;
        }

        pendingCraft = new LinkedList<>(
            GTUtility.streamInventory(table)
                .filter(i -> i != null)
                .collect(Collectors.toList()));

        ManipulatorRequest request = (ManipulatorRequest) patternDetails;

        if (request.link != null) {
            request.link.cancel();
            GTUtility.sendChatToPlayer(request.requester, "'" + request.requestName + "' has finished");
        }

        autoRequests.remove(patternDetails);
        manualRequests.remove(patternDetails);

        onRequestsChanged();

        pushPendingCraft();
        
        return true;
    }

    @Override
    public boolean isBusy() {
        return pendingCraft != null;
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        for (ManipulatorRequest request : manualRequests) {
            craftingTracker.addCraftingOption(this, request);
        }
        for (ManipulatorRequest request : autoRequests) {
            craftingTracker.addCraftingOption(this, request);
        }
    }

    public void addRequest(EntityPlayer requester, String requestName, List<ItemStack> requiredItems, boolean autocraft) {
        ManipulatorRequest request = new ManipulatorRequest(requester.getGameProfile().getId(), requestName, requiredItems);

        if (autocraft) {
            autoRequests.add(request);
        } else {
            manualRequests.add(request);
        }

        onRequestsChanged();
    }

    public void clearManualPlans(EntityPlayer player) {
        manualRequests.removeIf(request -> request.requester.equals(player.getGameProfile().getId()));

        onRequestsChanged();
    }

    private void onRequestsChanged() {
        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (final GridAccessException e) {
            // :P
        }
    }

    public boolean hasAnyRequests() {
        return !autoRequests.isEmpty() || !manualRequests.isEmpty();
    }

    private class ManipulatorRequest implements ICraftingPatternDetails {

        public final UUID requester;
        public final String requestName;
        public final List<ItemStack> requiredItems;
        public final ItemStack hologram;

        public Future<ICraftingJob> job;
        public ICraftingLink link;

        public ManipulatorRequest(UUID requester, String requestName, List<ItemStack> requiredItems) {
            this.requester = requester;
            this.requestName = requestName;
            this.requiredItems = requiredItems;

            hologram = ItemList.MatterManipulatorHologram.get(1);
            hologram.setStackDisplayName(EnumChatFormatting.RESET + requestName);
        }

        boolean poll() {
            if (!isActive()) {
                return true;
            }

            ICraftingGrid cg;
            try {
                cg = getProxy().getCrafting();
            } catch (GridAccessException e) {
                return true;
            }

            if (link != null) {
                if (link.isCanceled()) {
                    return false;
                } else {
                    return true;
                }
            }

            if (job == null) {
                job = cg.beginCraftingJob(getBaseMetaTileEntity().getWorld(), getGrid(), getRequestSource(), AEItemStack.create(hologram), null);
            }

            if (job == null) {
                return false;
            }

            try {
                ICraftingJob job = null;
                if (this.job.isDone()) {
                    job = this.job.get();
                }

                if (job != null) {
                    link = cg.submitJob(job, MTEMMUplinkMEHatch.this, null, false, getRequestSource());

                    if (link == null) {
                        return false;
                    }
                }
            } catch (final InterruptedException | ExecutionException e) {
                // :P
            }

            return true;
        }

        @Override
        public ItemStack getPattern() {
            return hologram;
        }

        @Override
        public boolean isValidItemForSlot(int slotIndex, ItemStack itemStack, World world) {
            return false;
        }

        @Override
        public boolean isCraftable() {
            return false;
        }

        @Override
        public IAEItemStack[] getInputs() {
            return getCondensedInputs();
        }

        @Override
        public IAEItemStack[] getOutputs() {
            return getCondensedOutputs();
        }

        @Override
        public IAEItemStack[] getCondensedInputs() {
            return requiredItems.stream()
                .map(AEItemStack::create)
                .toArray(IAEItemStack[]::new);
        }

        @Override
        public IAEItemStack[] getCondensedOutputs() {
            return new IAEItemStack[] { AEItemStack.create(hologram) };
        }

        @Override
        public boolean canSubstitute() {
            return false;
        }

        @Override
        public ItemStack getOutput(InventoryCrafting craftingInv, World world) {
            return null;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public void setPriority(int priority) {

        }
    }
}
