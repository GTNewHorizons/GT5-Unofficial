package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.joml.Vector3i;
import gregtech.common.items.matterManipulator.NBTState.Config;
import gregtech.common.items.matterManipulator.NBTState.Location;
import net.minecraft.block.Block;
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
import com.mojang.authlib.GameProfile;

import appeng.api.implementations.items.IMemoryCard;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.implementations.items.MemoryCardMessages;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.util.AEColor;
import appeng.helpers.ICustomNameObject;
import appeng.helpers.IOreFilterable;
import appeng.parts.AEBasePart;
import appeng.parts.automation.UpgradeInventory;
import appeng.tile.AEBaseTile;
import appeng.tile.networking.TileCableBus;
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

    private BlockAnalyzer() {}

    public static @Nullable TileAnalysisResult analyze(IBlockAnalysisContext context) {
        TileEntity te = context.getTileEntity();

        if (te == null) {
            return null;
        }

        TileAnalysisResult result = new TileAnalysisResult(context.getFakePlayer(), te);

        return result.doesAnything() ? result : null;
    }

    public static RegionAnalysis analyzeRegion(World world, Location a, Location b) {
        if (a == null || b == null || world.provider.dimensionId != a.worldId || a.worldId != b.worldId) return null;

        RegionAnalysis analysis = new RegionAnalysis();

        Vector3i deltas = Config.getRegionDeltas(a, b);
        analysis.sX = Math.abs(deltas.x);
        analysis.sY = Math.abs(deltas.y);
        analysis.sZ = Math.abs(deltas.z);
        
        int count = analysis.sX * analysis.sY * analysis.sZ;

        analysis.blocks = new Block[count];
        analysis.meta = new int[count];
        analysis.tiles = new TileAnalysisResult[count];

        List<Vector3i> voxels = Config.getBlocksInBB(a, deltas);

        BlockAnalysisContext context = new BlockAnalysisContext(world);

        for (int i = 0; i < count; i++) {
            Vector3i voxel = voxels.get(i);

            analysis.blocks[i] = world.getBlock(voxel.x, voxel.y, voxel.z);
            analysis.meta[i] = world.getBlockMetadata(voxel.x, voxel.y, voxel.z);
            analysis.tiles[i] = analyze(context);
        }
        
        return analysis;
    }

    public static class RegionAnalysis {
        public int sX, sY, sZ;
        public Block[] blocks;
        public int[] meta;
        public TileAnalysisResult[] tiles;
    }

    public static interface IBlockAnalysisContext {
        public Supplier<EntityPlayer> getFakePlayer();

        public TileEntity getTileEntity();
    }

    public static class BlockAnalysisContext implements IBlockAnalysisContext {

        public World world;
        public final Lazy<FakePlayer> fakePlayer;
        public Vector3i voxel;

        public BlockAnalysisContext(World world) {
            this.world = world;
            fakePlayer = new Lazy<>(() -> new FakePlayer((WorldServer) world, new GameProfile(UUID.randomUUID(), "BlockAnalyzer Fake Player")));
        }

        @Override
        public Supplier<EntityPlayer> getFakePlayer() {
            return fakePlayer::get;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(voxel.x, voxel.y, voxel.z);
        }
    }

    public static interface IBlockApplyContext extends IBlockAnalysisContext {
        public EntityPlayer getPlacingPlayer();

        public boolean tryApplyAction(double complexity);

        public boolean tryConsumeItems(ItemStack... items);
        public void givePlayerItems(ItemStack... items);
        public void givePlayerFluids(FluidStack... fluids);
    }

    public static class BlockActionContext implements IBlockApplyContext {

        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public PendingBuild build;
        public ItemStack manipulator;
        public Lazy<FakePlayer> fakePlayer;

        public static final double EU_PER_ACTION = 8192;

        @Override
        public Supplier<EntityPlayer> getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new Lazy<>(
                    () -> new FakePlayer((WorldServer) player.getEntityWorld(), player.getGameProfile()));
            }

            return fakePlayer::get;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        @Override
        public EntityPlayer getPlacingPlayer() {
            return player;
        }

        @Override
        public boolean tryApplyAction(double complexity) {
            return build.tryConsumePower(manipulator, x, y, z, EU_PER_ACTION * complexity);
        }

        @Override
        public boolean tryConsumeItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("consume: " + item);
                return true;
            } else {
                return build.tryConsumeItems(items);
            }
        }

        @Override
        public void givePlayerItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("give: " + item);
            } else {
                build.givePlayerItems(items);
            }
        }

        @Override
        public void givePlayerFluids(FluidStack... fluids) {
            build.givePlayerFluids(fluids);
        }
    }

    private static ForgeDirection nullIfUnknown(ForgeDirection dir) {
        return dir == ForgeDirection.UNKNOWN ? null : dir;
    }

    private static void emptyInventory(IBlockApplyContext context, IInventory inv) {
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
        public JsonElement mAEConfig = null;
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

        private TileAnalysisResult() {

        }

        public TileAnalysisResult(Supplier<EntityPlayer> fakePlayer, TileEntity te) {
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
                mAEConfig = NBTState.toJsonObject(ae.downloadSettings(SettingsFrom.MEMORY_CARD));
                mAECustomName = !(ae instanceof TileCableBus) && ae.hasCustomName() ? ae.getCustomName() : null;

                if (ae instanceof IPartHost partHost) {
                    mAEParts = new AEPartData[ALL_DIRECTIONS.length];

                    for (ForgeDirection dir : ALL_DIRECTIONS) {
                        if (partHost.getPart(dir) instanceof AEBasePart basePart) {
                            mAEParts[dir.ordinal()] = new AEPartData(fakePlayer.get(), basePart);
                        }
                    }
                }
            }
        }

        private static final TileAnalysisResult NO_OP = new TileAnalysisResult();

        public boolean doesAnything() {
            return !this.equals(NO_OP);
        }

        @SuppressWarnings("unused")
        public boolean apply(IBlockApplyContext ctx) {
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
                    ae.uploadSettings(SettingsFrom.MEMORY_CARD, (NBTTagCompound) NBTState.toNbt(mAEConfig));
                }

                if (mAECustomName != null && !(ae instanceof TileCableBus)) {
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

        private void removeCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side) {
            if (gte.getCoverIDAtSide(side) != 0) {
                context.givePlayerItems(gte.removeCoverAtSide(side, true));
            }
        }

        private void installCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side,
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

        private void updateCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side,
            CoverData target) {
            if (gte.getCoverIDAtSide(side) == target.getCoverID() && gte.getCoverBehaviorAtSideNew(side)
                .allowsCopyPasteTool()) {
                gte.setCoverDataAtSide(side, target.getCoverData());
            }
        }

        private void removePart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
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

        private boolean installPart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side,
            AEPartData partData) {
            ItemStack partStack = partData.getPartStack();

            if (!partHost.canAddPart(partStack, side)) {
                return false;
            }

            context.tryConsumeItems(partStack);

            if (partHost.addPart(partStack, side, context.getPlacingPlayer()) == null) {
                context.givePlayerItems(partStack);
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mConnections == null) ? 0 : mConnections.hashCode());
            result = prime * result + ((mGTColour == null) ? 0 : mGTColour.hashCode());
            result = prime * result + ((mGTFront == null) ? 0 : mGTFront.hashCode());
            result = prime * result + ((mGTMainFacing == null) ? 0 : mGTMainFacing.hashCode());
            result = prime * result + ((mGTBasicIOFlags == null) ? 0 : mGTBasicIOFlags.hashCode());
            result = prime * result + ((mGTFacing == null) ? 0 : mGTFacing.hashCode());
            result = prime * result + Arrays.hashCode(mCovers);
            result = prime * result + ((mStrongRedstone == null) ? 0 : mStrongRedstone.hashCode());
            result = prime * result + ((mGTCustomName == null) ? 0 : mGTCustomName.hashCode());
            result = prime * result + ((mAEColour == null) ? 0 : mAEColour.hashCode());
            result = prime * result + ((mAEUp == null) ? 0 : mAEUp.hashCode());
            result = prime * result + ((mAEForward == null) ? 0 : mAEForward.hashCode());
            result = prime * result + ((mAEConfig == null) ? 0 : mAEConfig.hashCode());
            result = prime * result + Arrays.hashCode(mAEUpgrades);
            result = prime * result + ((mAECustomName == null) ? 0 : mAECustomName.hashCode());
            result = prime * result + Arrays.hashCode(mAEParts);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TileAnalysisResult other = (TileAnalysisResult) obj;
            if (mConnections == null) {
                if (other.mConnections != null)
                    return false;
            } else if (!mConnections.equals(other.mConnections))
                return false;
            if (mGTColour == null) {
                if (other.mGTColour != null)
                    return false;
            } else if (!mGTColour.equals(other.mGTColour))
                return false;
            if (mGTFront != other.mGTFront)
                return false;
            if (mGTMainFacing != other.mGTMainFacing)
                return false;
            if (mGTBasicIOFlags == null) {
                if (other.mGTBasicIOFlags != null)
                    return false;
            } else if (!mGTBasicIOFlags.equals(other.mGTBasicIOFlags))
                return false;
            if (mGTFacing != other.mGTFacing)
                return false;
            if (!Arrays.equals(mCovers, other.mCovers))
                return false;
            if (mStrongRedstone == null) {
                if (other.mStrongRedstone != null)
                    return false;
            } else if (!mStrongRedstone.equals(other.mStrongRedstone))
                return false;
            if (mGTCustomName == null) {
                if (other.mGTCustomName != null)
                    return false;
            } else if (!mGTCustomName.equals(other.mGTCustomName))
                return false;
            if (mAEColour != other.mAEColour)
                return false;
            if (mAEUp != other.mAEUp)
                return false;
            if (mAEForward != other.mAEForward)
                return false;
            if (mAEConfig == null) {
                if (other.mAEConfig != null)
                    return false;
            } else if (!mAEConfig.equals(other.mAEConfig))
                return false;
            if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades))
                return false;
            if (mAECustomName == null) {
                if (other.mAECustomName != null)
                    return false;
            } else if (!mAECustomName.equals(other.mAECustomName))
                return false;
            if (!Arrays.equals(mAEParts, other.mAEParts))
                return false;
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cover == null) ? 0 : cover.hashCode());
            result = prime * result + ((coverData == null) ? 0 : coverData.hashCode());
            result = prime * result + ((tickRateAddition == null) ? 0 : tickRateAddition.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CoverData other = (CoverData) obj;
            if (cover == null) {
                if (other.cover != null)
                    return false;
            } else if (!cover.equals(other.cover))
                return false;
            if (coverData == null) {
                if (other.coverData != null)
                    return false;
            } else if (!coverData.equals(other.coverData))
                return false;
            if (tickRateAddition == null) {
                if (other.tickRateAddition != null)
                    return false;
            } else if (!tickRateAddition.equals(other.tickRateAddition))
                return false;
            return true;
        }

    }

    public static class AEPartData {

        public PortableItemStack mPart;
        public String mSettingsName = null;
        public JsonElement mSettings = null, mMemoryCardData = null;
        public PortableItemStack[] mAEUpgrades = null, mConfig = null;
        public String mOreDict = null;
        public String mCustomName = null;

        public transient ItemStack partStack;

        public AEPartData(EntityPlayer fakePlayer, AEBasePart part) {
            mPart = new PortableItemStack(part.getItemStack(PartItemStack.Break));

            mCustomName = part.hasCustomName() ? part.getCustomName() : null;

            if (part instanceof IOreFilterable filterable) {
                mOreDict = filterable.getFilter();

                if ("".equals(mOreDict)) mOreDict = null;
            }

            fakePlayer.inventory.mainInventory[0] = new ItemStack(new FakeMemoryCard(this));

            part.onShiftActivate(fakePlayer, Vec3.createVectorHelper(0, 0, 0));

            fakePlayer.inventory.mainInventory[0] = null;

            IInventory upgrades = part.getInventoryByName("upgrades");

            if (upgrades != null) {
                mAEUpgrades = GTUtility.streamInventory(upgrades)
                    .filter(x -> x != null)
                    .map(PortableItemStack::new)
                    .toArray(PortableItemStack[]::new);
            }

            IInventory config = part.getInventoryByName("config");

            if (config != null) {
                mConfig = GTUtility.streamInventory(config)
                    .filter(x -> x != null)
                    .map(PortableItemStack::withoutStackSize)
                    .distinct()
                    .toArray(PortableItemStack[]::new);
            }
        }

        public boolean updatePart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
            if (partHost.getPart(side) instanceof AEBasePart part) {
                if (mCustomName != null) part.setCustomName(mCustomName);

                EntityPlayer fakePlayer = context.getFakePlayer().get();

                fakePlayer.inventory.mainInventory[0] = new ItemStack(new FakeMemoryCard(this));

                part.onActivate(fakePlayer, Vec3.createVectorHelper(0, 0, 0));

                fakePlayer.inventory.mainInventory[0] = null;
                
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

                IInventory config = part.getInventoryByName("config");

                if (config != null) {
                    for (int i = 0; i < config.getSizeInventory(); i++) {
                        config.setInventorySlotContents(i, null);
                    }

                    if (mConfig != null) {
                        int n = Math.min(config.getSizeInventory(), mConfig.length);
                        for (int i = 0; i < n; i++) {
                            config.setInventorySlotContents(i, mConfig[i] == null ? null : mConfig[i].toStack());
                        }
                    }

                    config.markDirty();
                }

                if (part instanceof IOreFilterable filterable) {
                    filterable.setFilter(mOreDict == null ? "" : mOreDict);
                }
            }

            return true;
        }

        public ItemStack getPartStack() {
            return mPart.toStack();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mPart == null) ? 0 : mPart.hashCode());
            result = prime * result + ((mSettingsName == null) ? 0 : mSettingsName.hashCode());
            result = prime * result + ((mSettings == null) ? 0 : mSettings.hashCode());
            result = prime * result + ((mMemoryCardData == null) ? 0 : mMemoryCardData.hashCode());
            result = prime * result + Arrays.hashCode(mAEUpgrades);
            result = prime * result + Arrays.hashCode(mConfig);
            result = prime * result + ((mOreDict == null) ? 0 : mOreDict.hashCode());
            result = prime * result + ((mCustomName == null) ? 0 : mCustomName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AEPartData other = (AEPartData) obj;
            if (mPart == null) {
                if (other.mPart != null)
                    return false;
            } else if (!mPart.equals(other.mPart))
                return false;
            if (mSettingsName == null) {
                if (other.mSettingsName != null)
                    return false;
            } else if (!mSettingsName.equals(other.mSettingsName))
                return false;
            if (mSettings == null) {
                if (other.mSettings != null)
                    return false;
            } else if (!mSettings.equals(other.mSettings))
                return false;
            if (mMemoryCardData == null) {
                if (other.mMemoryCardData != null)
                    return false;
            } else if (!mMemoryCardData.equals(other.mMemoryCardData))
                return false;
            if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades))
                return false;
            if (!Arrays.equals(mConfig, other.mConfig))
                return false;
            if (mOreDict == null) {
                if (other.mOreDict != null)
                    return false;
            } else if (!mOreDict.equals(other.mOreDict))
                return false;
            if (mCustomName == null) {
                if (other.mCustomName != null)
                    return false;
            } else if (!mCustomName.equals(other.mCustomName))
                return false;
            return true;
        }
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

        public static PortableItemStack withoutStackSize(ItemStack stack) {
            stack = stack.copy();
            stack.stackSize = 1;
            return new PortableItemStack(stack);
        }

        public ItemStack toStack() {
            if (itemStack == null) {
                itemStack = new ItemStack(
                    GameRegistry.findItem(item.modId, item.name),
                    amount == null ? 1 : amount,
                    metadata == null ? 0 : metadata);
            }

            return itemStack;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((item == null) ? 0 : item.hashCode());
            result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
            result = prime * result + ((amount == null) ? 0 : amount.hashCode());
            result = prime * result + ((itemStack == null) ? 0 : itemStack.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            PortableItemStack other = (PortableItemStack) obj;
            if (item == null) {
                if (other.item != null) return false;
            } else if (!item.equals(other.item)) return false;
            if (metadata == null) {
                if (other.metadata != null) return false;
            } else if (!metadata.equals(other.metadata)) return false;
            if (amount == null) {
                if (other.amount != null) return false;
            } else if (!amount.equals(other.amount)) return false;
            if (itemStack == null) {
                if (other.itemStack != null) return false;
            } else if (!itemStack.equals(other.itemStack)) return false;
            return true;
        }
    }

    private static class FakeMemoryCard extends Item implements IMemoryCard {

        private final AEPartData partData;

        public FakeMemoryCard(AEPartData partData) {
            this.partData = partData;
        }

        @Override
        public void setMemoryCardContents(ItemStack is, String SettingsName, NBTTagCompound data) {
            partData.mSettingsName = SettingsName;
            partData.mMemoryCardData = NBTState.toJsonObject(data);
        }

        @Override
        public String getSettingsName(ItemStack is) {
            return partData.mSettingsName;
        }

        @Override
        public NBTTagCompound getData(ItemStack is) {
            return (NBTTagCompound) NBTState.toNbt(partData.mMemoryCardData);
        }

        @Override
        public void notifyUser(EntityPlayer player, MemoryCardMessages msg) {
            
        }
    }
}
