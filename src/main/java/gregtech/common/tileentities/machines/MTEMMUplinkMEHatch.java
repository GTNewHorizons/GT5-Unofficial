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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableSet;

import appeng.api.AEApi;
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
import appeng.helpers.PatternHelper;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTEMMUplinkMEHatch extends MTEHatch
    implements IGridProxyable, IPowerChannelState, ICraftingProvider, ICraftingRequester {

    public static final long REQUEST_TIMEOUT = 20 * 60;

    protected BaseActionSource requestSource = null;
    protected AENetworkProxy gridProxy = null;
    protected boolean additionalConnection = false;

    private final List<ManipulatorRequest> manualRequests = new ArrayList<>();
    private final List<ManipulatorRequest> autoRequests = new ArrayList<>();

    private List<ItemStack> pendingCraft;

    public MTEMMUplinkMEHatch(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            8,
            0,
            new String[] { "Quantum Uplink ME connector hatch.",
                "Change ME connection behavior by right-clicking with wire cutter.",
                "Clear all stored plans and cancel all jobs by right-clicking with a screw driver." });
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

                pushPendingCraft();
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    private void pollRequests() {
        Iterator<ManipulatorRequest> iter = autoRequests.iterator();

        while (iter.hasNext()) {
            ManipulatorRequest request = iter.next();

            if (!request.poll()) {
                EntityPlayer player = GTUtility.getPlayerById(request.requester);
                if (player != null) {
                    GTUtility.sendErrorToPlayer(player, "Craft for plan " + request.requestName + " failed.");
                }
                iter.remove();
                onRequestsChanged();
            }
        }
    }

    private void updateValidGridProxySides() {
        if (additionalConnection) {
            getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
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
                gridProxy = new AENetworkProxy(this, "proxy", ItemList.Hatch_MatterManipulatorUplink_ME.get(1), true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) {
                    gridProxy.setOwner(
                        getBaseMetaTileEntity().getWorld()
                            .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
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
            if (request.link != null) jobs.add(request.link);
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

    public void saveRequests(NBTTagCompound tag) {
        NBTTagList auto = new NBTTagList();

        for (ManipulatorRequest request : autoRequests) {
            auto.appendTag(request.writeToNBT(new NBTTagCompound()));
        }

        tag.setTag("auto", auto);

        NBTTagList manual = new NBTTagList();

        for (ManipulatorRequest request : manualRequests) {
            manual.appendTag(request.writeToNBT(new NBTTagCompound()));
        }

        tag.setTag("manual", manual);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("additionalConnection", additionalConnection);

        saveRequests(aNBT);

        getProxy().writeToNBT(aNBT);
    }

    private void loadRequestsImpl(NBTTagCompound tag) {
        autoRequests.clear();
        manualRequests.clear();

        @SuppressWarnings("unchecked")
        List<NBTTagCompound> auto = ((NBTTagList) tag.getTag("auto")).tagList;
        for (NBTTagCompound request : auto) {
            autoRequests.add(ManipulatorRequest.readFromNBT(this, request));
        }

        @SuppressWarnings("unchecked")
        List<NBTTagCompound> manual = ((NBTTagList) tag.getTag("manual")).tagList;
        for (NBTTagCompound request : manual) {
            manualRequests.add(ManipulatorRequest.readFromNBT(this, request));
        }

        autoRequests.remove(null);
        manualRequests.remove(null);
    }

    public void loadRequests(NBTTagCompound tag) {
        loadRequestsImpl(tag);
        onRequestsChanged();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        additionalConnection = aNBT.getBoolean("additionalConnection");

        loadRequestsImpl(aNBT);

        getProxy().readFromNBT(aNBT);

        onRequestsChanged();
    }

    private void pushPendingCraft() {
        if (pendingCraft == null) return;

        IStorageGrid grid = getStorageGrid();

        if (grid == null) return;

        IMEMonitor<IAEItemStack> itemInventory = grid.getItemInventory();

        if (itemInventory == null) return;

        Iterator<ItemStack> iter = pendingCraft.iterator();

        while (iter.hasNext()) {
            ItemStack current = iter.next();

            IAEItemStack result = itemInventory
                .injectItems(AEItemStack.create(current), Actionable.MODULATE, getRequestSource());

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

        PatternHelper pattern = (PatternHelper) patternDetails;

        IAEItemStack hologram = pattern.getCondensedOutputs()[0];

        Iterator<ManipulatorRequest> iter = autoRequests.iterator();

        while (iter.hasNext()) {
            ManipulatorRequest request = iter.next();

            if (hologram.isSameType(request.hologram)) {
                if (request.link != null) {
                    request.link.cancel();

                    EntityPlayer player = GTUtility.getPlayerById(request.requester);
                    if (player != null) {
                        GTUtility.sendInfoToPlayer(player, "'" + request.requestName + "' has finished");
                    }
                }

                iter.remove();
            }
        }

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
            try {
                PatternHelper pattern = new PatternHelper(
                    request.getPattern(),
                    this.getBaseMetaTileEntity()
                        .getWorld());
                craftingTracker.addCraftingOption(this, pattern);
            } catch (IllegalStateException e) {
                GTMod.GT_FML_LOGGER.error("Could not load matter manipulator plan", e);
                continue;
            }
        }

        for (ManipulatorRequest request : autoRequests) {
            try {
                PatternHelper pattern = new PatternHelper(
                    request.getPattern(),
                    this.getBaseMetaTileEntity()
                        .getWorld());
                craftingTracker.addCraftingOption(this, pattern);
            } catch (IllegalStateException e) {
                GTMod.GT_FML_LOGGER.error("Could not load matter manipulator plan", e);
                continue;
            }
        }
    }

    public void addRequest(EntityPlayer requester, String requestName, List<IAEItemStack> requiredItems,
        boolean autocraft) {
        ManipulatorRequest request = new ManipulatorRequest(
            requester.getGameProfile()
                .getId(),
            requestName,
            requiredItems);

        if (autocraft) {
            autoRequests.add(request);
        } else {
            manualRequests.add(request);
        }

        onRequestsChanged();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {

        for (var req : autoRequests) {
            if (req.link != null) {
                req.link.cancel();
            }

            if (req.job != null) {
                req.job.cancel(false);
            }
        }

        autoRequests.clear();
        manualRequests.clear();

        onRequestsChanged();

        GTUtility.sendInfoToPlayer(aPlayer, "Cleared all requests and cancelled pending craft jobs.");
    }

    public void clearManualPlans(EntityPlayer player) {
        manualRequests.removeIf(
            request -> request.requester.equals(
                player.getGameProfile()
                    .getId()));

        onRequestsChanged();

        GTUtility.sendInfoToPlayer(player, "Cleared your manual requests.");
    }

    public void cancelAutoPlans(EntityPlayer player) {
        Iterator<ManipulatorRequest> iter = autoRequests.iterator();

        while (iter.hasNext()) {
            ManipulatorRequest req = iter.next();

            if (!req.requester.equals(
                player.getGameProfile()
                    .getId())) {
                continue;
            }

            if (req.link != null) {
                req.link.cancel();
            }

            if (req.job != null) {
                req.job.cancel(false);
            }

            iter.remove();
        }

        onRequestsChanged();

        GTUtility.sendInfoToPlayer(player, "Cleared your auto requests and cancelled their crafting jobs.");
    }

    private void onRequestsChanged() {
        try {
            getProxy().getGrid()
                .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (final GridAccessException e) {
            // :P
        }
    }

    public boolean hasAnyRequests() {
        return !autoRequests.isEmpty() || !manualRequests.isEmpty();
    }

    private class ManipulatorRequest {

        public final UUID requester;
        public final String requestName;
        public final List<IAEItemStack> requiredItems;
        public final ItemStack hologram;

        public Future<ICraftingJob> job;
        public ICraftingLink link;

        public ManipulatorRequest(UUID requester, String requestName, List<IAEItemStack> requiredItems) {
            this.requester = requester;
            this.requestName = requestName;
            this.requiredItems = requiredItems;

            hologram = ItemList.MatterManipulatorHologram.get(1);
            hologram.setStackDisplayName(EnumChatFormatting.RESET + requestName);
        }

        private ManipulatorRequest(UUID requester, String requestName, List<IAEItemStack> requiredItems,
            ICraftingLink link) {
            this.requester = requester;
            this.requestName = requestName;
            this.requiredItems = requiredItems;
            this.link = link;

            hologram = ItemList.MatterManipulatorHologram.get(1);
            hologram.setStackDisplayName(EnumChatFormatting.RESET + requestName);
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setLong("r1", requester.getMostSignificantBits());
            tag.setLong("r2", requester.getLeastSignificantBits());
            tag.setString("rn", requestName);

            if (link != null) {
                NBTTagCompound linkTag = new NBTTagCompound();
                link.writeToNBT(linkTag);
                tag.setTag("link", linkTag);
            }

            NBTTagList items = new NBTTagList();

            for (IAEItemStack item : requiredItems) {
                NBTTagCompound itemTag = new NBTTagCompound();
                item.writeToNBT(itemTag);
                items.appendTag(itemTag);
            }

            tag.setTag("items", items);

            return tag;
        }

        public static ManipulatorRequest readFromNBT(MTEMMUplinkMEHatch hatch, NBTTagCompound tag) {
            UUID requester = new UUID(tag.getLong("r1"), tag.getLong("r2"));
            String requestName = tag.getString("rn");
            ICraftingLink link = null;

            if (tag.hasKey("link")) {
                link = AEApi.instance()
                    .storage()
                    .loadCraftingLink(tag.getCompoundTag("link"), hatch);
            }

            ArrayList<IAEItemStack> requiredItems = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<NBTTagCompound> items = ((NBTTagList) tag.getTag("items")).tagList;

            for (NBTTagCompound item : items) {
                requiredItems.add(AEItemStack.loadItemStackFromNBT(item));
            }

            requiredItems.remove(null);

            if (requiredItems.isEmpty()) return null;

            return hatch.new ManipulatorRequest(requester, requestName, requiredItems, link);
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
                job = cg.beginCraftingJob(
                    getBaseMetaTileEntity().getWorld(),
                    getGrid(),
                    getRequestSource(),
                    AEItemStack.create(hologram),
                    null);
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

                    EntityPlayer player = GTUtility.getPlayerById(requester);

                    if (player != null) {
                        GTUtility.sendInfoToPlayer(player, "Submitted job for plan '" + requestName + "'.");
                    }
                }
            } catch (final InterruptedException | ExecutionException e) {
                // :P
            }

            return true;
        }

        public ItemStack getPattern() {
            NBTTagCompound tag = new NBTTagCompound();

            tag.setBoolean("crafting", false);
            tag.setBoolean("substitute", false);
            tag.setBoolean("beSubstitute", false);

            EntityPlayer player = GTUtility.getPlayerById(requester);
            tag.setString(
                "author",
                player != null ? player.getGameProfile()
                    .getName() : requester.toString());

            NBTTagList out = new NBTTagList();

            out.appendTag(Platform.writeItemStackToNBT(this.hologram, new NBTTagCompound()));

            tag.setTag("out", out);

            NBTTagList in = new NBTTagList();

            for (IAEItemStack stack : requiredItems) {
                in.appendTag(Platform.writeItemStackToNBT(stack.getItemStack(), new NBTTagCompound()));
            }

            tag.setTag("in", in);

            ItemStack pattern = AEApi.instance()
                .definitions()
                .items()
                .encodedPattern()
                .maybeStack(1)
                .get();

            pattern.setTagCompound(tag);

            return pattern;
        }
    }
}
