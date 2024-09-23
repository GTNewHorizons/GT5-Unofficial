package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.JsonElement;
import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import appeng.api.AEApi;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.util.AEColor;
import appeng.helpers.ICustomNameObject;
import appeng.helpers.IOreFilterable;
import appeng.items.tools.ToolMemoryCard;
import appeng.parts.AEBasePart;
import appeng.parts.automation.UpgradeInventory;
import appeng.parts.p2p.PartP2PTunnel;
import appeng.tile.AEBaseTile;
import appeng.util.SettingsFrom;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.Lazy;
import gregtech.common.covers.CoverInfo;

public class BlockAnalyzer {

    private static final ToolMemoryCard memoryCard = (ToolMemoryCard) AEApi.instance()
        .definitions()
        .items()
        .memoryCard()
        .maybeItem()
        .get();

    private BlockAnalyzer() {}

    public static @Nullable TileAnalysisResult analyze(BlockActionContext context) {
        if (context.world.isRemote) {
            throw new IllegalStateException("Cannot analyze a block on the client because it needs a fake player.");
        }

        TileEntity te = context.getTileEntity();

        if (te == null) {
            return null;
        }

        TileAnalysisResult result = new TileAnalysisResult(context.getFakePlayerLazy(), te);

        return result;
    }

    public static class BlockActionContext {

        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public PendingBuild build;
        public ItemStack manipulator;
        public Lazy<FakePlayer> fakePlayer;

        public static final double EU_PER_ACTION = 8192;

        public Lazy<FakePlayer> getFakePlayerLazy() {
            if (fakePlayer == null) {
                fakePlayer = new Lazy<>(
                    () -> new FakePlayer((WorldServer) player.getEntityWorld(), player.getGameProfile()));
            }

            return fakePlayer;
        }

        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        public boolean tryConsumePower(double mult) {
            return build.tryConsumePower(manipulator, x, y, z, EU_PER_ACTION * mult);
        }

        public boolean tryConsumeItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("consume: " + item);
                return true;
            } else {
                return build.tryConsumeItems(items);
            }
        }

        public void givePlayerItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("give: " + item);
            } else {
                build.givePlayerItems(items);
            }
        }

        public void givePlayerFluids(FluidStack... fluids) {
            build.givePlayerFluids(fluids);
        }
    }

    private static ForgeDirection nullIfUnknown(ForgeDirection dir) {
        return dir == ForgeDirection.UNKNOWN ? null : dir;
    }

    public static class TileAnalysisResult {

        public Byte mConnections = null;
        public Byte mGTColour = null;
        public ForgeDirection mGTFront = null, mGTMainFacing = null;
        public Byte mGTBasicIOFlags = null;
        public ExtendedFacing mGTFacing = null;
        public CoverData[] mCovers = null;
        public Byte mStrongRedstone = null;
        public String mGTCustomName = null;

        public AEColor mAEColour = null;
        public ForgeDirection mAEUp = null, mAEForward = null;
        public NBTTagCompound mAEConfig = null;
        public UniqueIdentifier[] mAEUpgrades = null;
        public String mAECustomName = null;
        public AEPartData[] mAEParts = null;

        private static int counter = 0;
        public static final byte GT_BASIC_IO_PUSH_ITEMS = (byte) (0b1 << counter++);
        public static final byte GT_BASIC_IO_PUSH_FLUIDS = (byte) (0b1 << counter++);
        public static final byte GT_BASIC_IO_DISABLE_FILTER = (byte) (0b1 << counter++);
        public static final byte GT_BASIC_IO_DISABLE_MULTISTACK = (byte) (0b1 << counter++);
        public static final byte GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE = (byte) (0b1 << counter++);

        private static final ForgeDirection[] ALL_DIRECTIONS = ForgeDirection.values();

        public TileAnalysisResult(Lazy<FakePlayer> fakePlayer, TileEntity te) {
            if (te instanceof IGregTechTileEntity gte) {
                IMetaTileEntity mte = gte.getMetaTileEntity();

                if (gte.getColorization() != -1) mGTColour = gte.getColorization();

                if (mte instanceof MTEBasicMachine basicMachine) {
                    mGTMainFacing = basicMachine.mMainFacing;

                    byte flags = 0;

                    if (basicMachine.mItemTransfer) flags |= GT_BASIC_IO_PUSH_ITEMS;
                    if (basicMachine.mFluidTransfer) flags |= GT_BASIC_IO_PUSH_FLUIDS;
                    if (basicMachine.mDisableFilter) flags |= GT_BASIC_IO_DISABLE_FILTER;
                    if (basicMachine.mDisableMultiStack) flags |= GT_BASIC_IO_DISABLE_MULTISTACK;
                    if (basicMachine.mAllowInputFromOutputSide) flags |= GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE;

                    if (flags != 0) mGTBasicIOFlags = flags;
                }

                if (mte instanceof IConnectable connectable) {
                    byte con = 0;

                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if (connectable.isConnectedAtSide(dir)) {
                            con |= dir.flag;
                        }
                    }

                    mConnections = con;
                }

                if (mte instanceof IAlignmentProvider provider) {
                    IAlignment alignment = provider.getAlignment();

                    mGTFacing = alignment != null ? alignment.getExtendedFacing() : null;
                } else {
                    mGTFront = nullIfUnknown(gte.getFrontFacing());
                }

                CoverData[] covers = new CoverData[6];
                boolean hasCover = false;
                byte strongRedstone = 0;

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    if (gte.getCoverIDAtSide(dir) != 0) {
                        covers[dir.ordinal()] = CoverData.fromInfo(gte.getCoverInfoAtSide(dir));
                        hasCover = true;

                        if (gte.getRedstoneOutputStrength(dir)) {
                            strongRedstone |= dir.flag;
                        }
                    }
                }

                if (hasCover) mCovers = covers;
                if (strongRedstone != 0) mStrongRedstone = strongRedstone;

                if (mte instanceof ICustomNameObject customName && customName.hasCustomName()) {
                    mGTCustomName = customName.getCustomName();
                }
            }

            if (te instanceof AEBaseTile ae) {
                mAEUp = nullIfUnknown(ae.getUp());
                mAEForward = nullIfUnknown(ae.getForward());
                mAEConfig = ae.downloadSettings(SettingsFrom.MEMORY_CARD);
                mAECustomName = ae.getCustomName();

                if (ae instanceof IPartHost partHost) {
                    mAEParts = new AEPartData[ALL_DIRECTIONS.length];

                    for (ForgeDirection dir : ALL_DIRECTIONS) {
                        IPart part = partHost.getPart(dir);

                        if (part instanceof AEBasePart basePart) {
                            mAEParts[dir.ordinal()] = new AEPartData(fakePlayer, basePart);
                        }
                    }
                }
            }
        }

        public boolean doesAnything() {
            return true;
        }

        @SuppressWarnings("unused")
        public boolean apply(BlockActionContext ctx) {
            TileEntity te = ctx.getTileEntity();

            if (te instanceof IGregTechTileEntity gte) {
                IMetaTileEntity mte = gte.getMetaTileEntity();

                if (mGTColour != null) {
                    gte.setColorization(mGTColour);
                }

                if (mte instanceof MTEBasicMachine basicMachine) {
                    if (mGTMainFacing != null) basicMachine.mMainFacing = mGTMainFacing;

                    mGTMainFacing = basicMachine.mMainFacing;

                    basicMachine.mItemTransfer = (mGTBasicIOFlags & GT_BASIC_IO_PUSH_ITEMS) != 0;
                    basicMachine.mFluidTransfer = (mGTBasicIOFlags & GT_BASIC_IO_PUSH_FLUIDS) != 0;
                    basicMachine.mDisableFilter = (mGTBasicIOFlags & GT_BASIC_IO_DISABLE_FILTER) != 0;
                    basicMachine.mDisableMultiStack = (mGTBasicIOFlags & GT_BASIC_IO_DISABLE_MULTISTACK) != 0;
                    basicMachine.mAllowInputFromOutputSide = (mGTBasicIOFlags & GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE)
                        != 0;
                }

                if (mte instanceof IConnectable connectable) {
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        boolean shouldBeConnected = (mConnections & dir.flag) != 0;
                        if (connectable.isConnectedAtSide(dir) != shouldBeConnected) {
                            if (shouldBeConnected) {
                                connectable.connect(dir);
                            } else {
                                connectable.disconnect(dir);
                            }
                        }
                    }
                }

                if (mte instanceof IAlignmentProvider provider) {
                    IAlignment alignment = provider.getAlignment();

                    if (mGTFacing != null && alignment != null) alignment.setExtendedFacing(mGTFacing);
                } else {
                    if (mGTFront != null) gte.setFrontFacing(mGTFront);
                }

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    CoverData expected = mCovers == null ? null : mCovers[dir.ordinal()];
                    CoverInfo actual = new CoverInfo(
                        dir,
                        gte.getCoverIDAtSide(dir),
                        gte,
                        gte.getComplexCoverDataAtSide(dir));

                    if (actual == null && expected != null) {
                        installCover(ctx, gte, dir, expected);
                    } else if (actual != null && expected == null) {
                        removeCover(ctx, gte, dir);
                    } else if (actual != null && expected != null) {
                        if (actual.getCoverID() != expected.getCoverID()) {
                            removeCover(ctx, gte, dir);
                            installCover(ctx, gte, dir, expected);
                        } else if (!Objects.equals(actual.getCoverData(), expected.getCoverData())) {
                            updateCover(ctx, gte, dir, expected);
                        }
                    }

                    if (mStrongRedstone != null) {
                        gte.setRedstoneOutputStrength(dir, (mStrongRedstone & dir.flag) != 0);
                    }
                }

                if (mte instanceof ICustomNameObject customName && mGTCustomName != null) {
                    customName.setCustomName(mGTCustomName);
                }
            }

            if (te instanceof AEBaseTile ae) {
                if (mAEUp != null && mAEForward != null) {
                    ae.setOrientation(mAEForward, mAEUp);
                }

                if (mAEConfig != null) {
                    ae.uploadSettings(SettingsFrom.MEMORY_CARD, mAEConfig);
                }

                if (mAECustomName != null) {
                    ae.setCustomName(mAECustomName);
                }

                if (ae instanceof IPartHost partHost && mAEParts != null) {
                    for (ForgeDirection dir : ALL_DIRECTIONS) {
                        IPart part = partHost.getPart(dir);
                        AEPartData expected = mAEParts[dir.ordinal()];

                        ItemId actualItem = part == null ? null
                            : ItemId.createWithoutNBT(part.getItemStack(PartItemStack.Break));
                        ItemId expectedItem = expected == null ? null
                            : ItemId.createWithoutNBT(expected.getPartStack());

                        if ((expectedItem == null || !Objects.equals(actualItem, expectedItem)) && actualItem != null) {
                            removePart(ctx, partHost, dir);
                            actualItem = null;
                        }

                        if (actualItem == null && expectedItem != null) {
                            if (!installPart(ctx, partHost, dir, expected)) {
                                return false;
                            }
                        }

                        if (expected != null) {
                            if (!expected.updatePart(ctx, partHost, dir)) {
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }

        private void removeCover(BlockActionContext context, IGregTechTileEntity gte, ForgeDirection side) {
            if (gte.getCoverIDAtSide(side) != 0) {
                context.givePlayerItems(gte.removeCoverAtSide(side, true));
            }
        }

        private void installCover(BlockActionContext context, IGregTechTileEntity gte, ForgeDirection side,
            CoverData cover) {
            if (gte.getCoverIDAtSide(side) == 0 && gte.canPlaceCoverItemAtSide(side, cover.getCover())
                && context.tryConsumeItems(cover.getCover())) {
                gte.setCoverIdAndDataAtSide(
                    side,
                    cover.getCoverID(),
                    cover.getCoverBehaviour()
                        .allowsCopyPasteTool() ? cover.getCoverData() : null);
            }
        }

        private void updateCover(BlockActionContext context, IGregTechTileEntity gte, ForgeDirection side,
            CoverData target) {
            if (gte.getCoverIDAtSide(side) == target.getCoverID() && gte.getCoverBehaviorAtSideNew(side)
                .allowsCopyPasteTool()) {
                gte.setCoverDataAtSide(side, target.getCoverData());
            }
        }

        private void removePart(BlockActionContext context, IPartHost partHost, ForgeDirection side) {
            IPart part = partHost.getPart(side);

            if (part == null) return;

            List<ItemStack> drops = new ArrayList<>();

            part.getDrops(drops, false);

            context.givePlayerItems(drops.toArray(new ItemStack[drops.size()]));

            ItemStack partStack = part.getItemStack(PartItemStack.Break);

            NBTTagCompound tag = partStack.getTagCompound();

            if (tag != null) {
                tag.removeTag("display");

                if (tag.hasNoTags()) {
                    partStack.setTagCompound(null);
                }
            }

            context.givePlayerItems(partStack);

            partHost.removePart(side, false);
        }

        private boolean installPart(BlockActionContext context, IPartHost partHost, ForgeDirection side,
            AEPartData partData) {
            ItemStack partStack = partData.getPartStack();

            if (!partHost.canAddPart(partStack, side)) {
                return false;
            }

            context.tryConsumeItems(partStack);

            if (partHost.addPart(partStack, side, context.player) == null) {
                context.givePlayerItems(partStack);
                return false;
            }

            return true;
        }
    }

    public static class CoverData {

        public PortableItemStack cover;
        public JsonElement coverData;
        public Integer tickRateAddition;

        public transient Integer coverID;
        public transient CoverBehaviorBase<?> behaviour;
        public transient ISerializableObject coverDataObject;

        public CoverData(PortableItemStack cover, NBTBase coverData, int tickRateAddition) {
            this.cover = cover;
            this.coverData = NBTState.toJsonObject(coverData);
            this.tickRateAddition = tickRateAddition == 0 ? null : tickRateAddition;
        }

        public ItemStack getCover() {
            return cover.toStack();
        }

        public int getCoverID() {
            if (coverID == null) {
                ItemStack stack = getCover();

                coverID = (Item.getIdFromItem(stack.getItem()) & 0xFFFF)
                    | ((Items.feather.getDamage(stack) & 0xFFFF) << 16);
            }

            return coverID;
        }

        public CoverBehaviorBase<?> getCoverBehaviour() {
            if (behaviour == null) {
                behaviour = GregTechAPI.getCoverBehaviorNew(getCoverID());
            }

            return behaviour;
        }

        public ISerializableObject getCoverData() {
            if (coverDataObject == null) {
                coverDataObject = getCoverBehaviour().createDataObject(NBTState.toNbt(coverData));
            }

            return coverDataObject;
        }

        public static CoverData fromInfo(CoverInfo info) {
            if (info.getCoverID() == 0) return null;

            int itemId = info.getCoverID() & 0xFFFF;
            int metadata = (info.getCoverID() >> 16) & 0xFFFF;

            Item item = Item.getItemById(itemId);

            return new CoverData(
                new PortableItemStack(item, metadata),
                info.getCoverData()
                    .saveDataToNBT(),
                info.getTickRateAddition());
        }
    }

    public static class AEPartData {

        public PortableItemStack mPart;
        public String mP2PConfigName = null;
        public NBTTagCompound mAESettings = null, mP2PSettings = null;
        public PortableItemStack[] mAEUpgrades = null;
        public String mAEOreDict = null;
        public String mAECustomName = null;

        public transient ItemStack partStack;

        public AEPartData(Lazy<FakePlayer> fakePlayer, AEBasePart part) {
            mPart = new PortableItemStack(part.getItemStack(PartItemStack.Break));

            mAECustomName = part.hasCustomName() ? part.getCustomName() : null;

            if (part instanceof IOreFilterable filterable) {
                mAEOreDict = filterable.getFilter();
            }

            if (part instanceof PartP2PTunnel tunnel) {
                FakePlayer player = fakePlayer.get();

                ItemStack cardStack = AEApi.instance()
                    .definitions()
                    .items()
                    .memoryCard()
                    .maybeStack(1)
                    .get();
                player.inventory.mainInventory[0] = cardStack;

                tunnel.onPartShiftActivate(player, Vec3.createVectorHelper(0, 0, 0));

                player.inventory.mainInventory[0] = null;

                mP2PSettings = memoryCard.getData(cardStack);
            } else {
                if (part.getConfigManager() != null && mAESettings != null) {
                    part.getConfigManager()
                        .writeToNBT(mAESettings);
                }

                IInventory upgrades = part.getInventoryByName("upgrades");

                if (upgrades != null) {
                    mAEUpgrades = GTUtility.streamInventory(upgrades)
                        .filter(x -> x != null)
                        .map(PortableItemStack::new)
                        .toArray(PortableItemStack[]::new);
                }
            }
        }

        public boolean updatePart(BlockActionContext context, IPartHost partHost, ForgeDirection side) {
            if (partHost.getPart(side) instanceof AEBasePart part) {
                if (mAECustomName != null) part.setCustomName(mAECustomName);

                if (part instanceof PartP2PTunnel tunnel) {
                    if (mP2PSettings != null) {
                        FakePlayer player = context.getFakePlayerLazy()
                            .get();

                        ItemStack cardStack = AEApi.instance()
                            .definitions()
                            .items()
                            .memoryCard()
                            .maybeStack(1)
                            .get();

                        memoryCard.setMemoryCardContents(cardStack, mAECustomName, mP2PSettings);
                        player.inventory.mainInventory[0] = cardStack;

                        tunnel.onPartActivate(player, Vec3.createVectorHelper(0, 0, 0));

                        player.inventory.mainInventory[0] = null;
                    }
                } else {
                    UpgradeInventory upgradeInv = (UpgradeInventory) part.getInventoryByName("upgrades");

                    if (upgradeInv != null) {
                        ItemStackMap<Long> targetMap = GTUtility.getItemStackHistogram(
                            Arrays.stream(mAEUpgrades)
                                .map(PortableItemStack::toStack)
                                .toArray(ItemStack[]::new));
                        ItemStackMap<Long> actualMap = GTUtility.getItemStackHistogram(
                            GTUtility.streamInventory(upgradeInv)
                                .filter(x -> x != null)
                                .toArray(ItemStack[]::new));

                        if (!targetMap.equals(actualMap)) {
                            emptyInventory(context, upgradeInv);

                            targetMap.replaceAll((item, amount) -> {
                                if (item.getItem() instanceof IUpgradeModule upgrade) {
                                    int max = upgradeInv.getMaxInstalled(upgrade.getType(item));

                                    return Math.min(max, amount);
                                } else {
                                    return 0l;
                                }
                            });

                            List<ItemStack> upgradeList = GTUtility.getStacksOfSize(targetMap, 1);

                            ItemStack[] upgrades = upgradeList
                                .subList(0, Math.min(upgradeList.size(), upgradeInv.getSizeInventory()))
                                .toArray(new ItemStack[0]);

                            if (context.tryConsumeItems(upgrades)) {
                                for (int i = 0; i < upgrades.length; i++) {
                                    upgradeInv.setInventorySlotContents(i, upgrades[i]);
                                }
                            }
                        }
                    }

                    if (part.getConfigManager() != null && mAESettings != null) {
                        part.getConfigManager()
                            .readFromNBT(mAESettings);
                    }

                    if (part instanceof IOreFilterable filterable) {
                        filterable.setFilter(mAEOreDict);
                    }
                }
            }

            return true;
        }

        public ItemStack getPartStack() {
            return mPart.toStack();
        }
    }

    private static void emptyInventory(BlockActionContext context, IInventory inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                inv.setInventorySlotContents(i, null);

                context.givePlayerItems(stack);
            }
        }

        inv.markDirty();
    }

    public static class PortableItemStack {

        public UniqueIdentifier item;
        public Integer metadata, amount;

        public transient ItemStack itemStack;

        public PortableItemStack() {}

        public PortableItemStack(Item item, int metadata) {
            this.item = GameRegistry.findUniqueIdentifierFor(item);
            this.metadata = metadata == 0 ? null : metadata;
            this.amount = null;
        }

        public PortableItemStack(ItemStack stack) {
            item = GameRegistry.findUniqueIdentifierFor(stack.getItem());
            metadata = Items.feather.getDamage(stack);
            if (metadata == 0) metadata = null;
            amount = stack.stackSize == 1 ? null : stack.stackSize;
        }

        public ItemStack toStack() {
            if (itemStack == null) {
                itemStack = new ItemStack(
                    GameRegistry.findItem(item.modId, item.name),
                    amount == null ? 1 : amount,
                    metadata);
            }

            return itemStack;
        }
    }
}
