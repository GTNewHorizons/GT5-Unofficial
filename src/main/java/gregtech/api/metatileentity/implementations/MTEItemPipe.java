package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;
import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;
import static gregtech.api.enums.Textures.BlockIcons.PIPE_RESTRICTOR;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.ItemUtil;
import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials.PipeProperties;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityItemPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.MaterialUtils;
import gregtech.api.material.PipeStats;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.PipeShapeBlock;
import gregtech.common.blocks.PipeShapeItemBlock;
import gregtech.common.covers.Cover;

@IMetaTileEntity.SkipGenerateDescription
public class MTEItemPipe extends MetaPipeEntity implements IMetaTileEntityItemPipe, ILocalizedMetaPipeEntity {

    private final float mThickNess;
    public int mTransferredItems = 0;
    public long mCurrentTransferStartTick = 0;
    public ForgeDirection mLastReceivedFrom = ForgeDirection.UNKNOWN, oLastReceivedFrom = ForgeDirection.UNKNOWN;
    public boolean mIsRestrictive = false;
    private int[] cacheSides;
    private final String mPrefixKey;
    /// Inventory NBT stashed while the host material was unresolvable (chunk load runs before the world is
    /// bound), applied by [#onShapeMaterialResolved] once the inventory can be sized.
    private NBTTagList pendingInventory;

    /// The shape-scoped constructor: identity comes from the hosting [PipeShapeBlock], and material, stats,
    /// and inventory size resolve from the host block's metadata and [PipeProperties] through
    /// [PipeStats]. The inventory starts empty and is sized on the first material resolution.
    public MTEItemPipe(int aID, String aName, PipeShapeBlock shape) {
        super(aID, aName, 0, false, shape, shape.getSizeIndex());
        mPrefixKey = shape.getPrefixKey();
        mIsRestrictive = shape.getFamily() == PipeShapeBlock.PipeFamily.ITEM_RESTRICTIVE;
        mThickNess = shape.getThickness();
    }

    public MTEItemPipe(String aName, PipeShapeBlock shape) {
        super(aName, 0, shape, shape.getSizeIndex());
        mPrefixKey = shape.getPrefixKey();
        mIsRestrictive = shape.getFamily() == PipeShapeBlock.PipeFamily.ITEM_RESTRICTIVE;
        mThickNess = shape.getThickness();
    }

    @Override
    public byte getTileEntityBaseType() {
        final int level = GTUtility.clamp(MaterialUtils.toolQuality(shapeMaterial()), 0, 3);

        HarvestTool tool = switch (level) {
            case 0 -> HarvestTool.WrenchPipeLevel0;
            case 1 -> HarvestTool.WrenchPipeLevel1;
            case 2 -> HarvestTool.WrenchPipeLevel2;
            case 3 -> HarvestTool.WrenchPipeLevel3;
            default -> throw new IllegalStateException("Unexpected tool quality level: " + level);
        };

        return tool.toTileEntityBaseType();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEItemPipe(mName, (PipeShapeBlock) getShapeHost());
    }

    /// The huge-pipe slot count of the resolved material, or 0 while unresolved.
    private int hugeSlots() {
        Material material = shapeMaterial();
        Integer slots = material == null ? null : material.getProperty(PipeProperties.BASE_ITEM_PIPE_SLOTS);
        return slots == null ? 0 : slots;
    }

    /// Sizes the inventory for a freshly resolved material, carrying over what fits, dropping overflow in
    /// the world, and applying an inventory NBT list stashed before the material was resolvable.
    @Override
    protected void onShapeMaterialResolved(Material material) {
        Integer baseSlots = material.getProperty(PipeProperties.BASE_ITEM_PIPE_SLOTS);
        int slots = baseSlots == null ? 0 : PipeStats.itemPipeSlots(baseSlots, getShapeSizeIndex());
        if (mInventory.length != slots) {
            ItemStack[] resized = new ItemStack[slots];
            int kept = Math.min(mInventory.length, slots);
            System.arraycopy(mInventory, 0, resized, 0, kept);
            for (int i = kept; i < mInventory.length; i++) {
                if (mInventory[i] != null) dropOverflow(mInventory[i]);
            }
            mInventory = resized;
            cacheSides = null;
        }
        if (pendingInventory != null) {
            NBTTagList stash = pendingInventory;
            pendingInventory = null;
            for (int i = 0; i < stash.tagCount(); i++) {
                NBTTagCompound tag = stash.getCompoundTagAt(i);
                int slot = tag.getInteger("IntSlot");
                ItemStack loaded = GTUtility.loadItem(tag);
                if (loaded == null || loaded.getItem() == ItemList.Display_Fluid.getItem()) continue;
                if (slot >= 0 && slot < mInventory.length) {
                    mInventory[slot] = loaded;
                } else {
                    dropOverflow(loaded);
                }
            }
        }
    }

    private void dropOverflow(ItemStack stack) {
        if (!(getBaseMetaTileEntity() instanceof BaseMetaPipeEntity pipe)) return;
        if (pipe.getWorldObj() == null || pipe.isClientSide()) return;
        pipe.getWorldObj()
            .spawnEntityInWorld(
                new EntityItem(pipe.getWorldObj(), pipe.xCoord + 0.5, pipe.yCoord + 0.5, pipe.zCoord + 0.5, stack));
    }

    /// The live inventory, resolving the host material first so the pipe is sized before use.
    private ItemStack[] inv() {
        shapeMaterial();
        return mInventory;
    }

    @Override
    public ItemStack[] getRealInventory() {
        return inv();
    }

    @Override
    public int getSizeInventory() {
        return inv().length;
    }

    @Override
    protected void onShapeSwapped() {
        shapeMaterial();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean redstoneLevel) {
        final Material material = getMaterial();
        final short[] materialRgba = MaterialUtils.rgba(material);
        if (material == null || materialRgba == null) return Textures.BlockIcons.ERROR_RENDERING;
        String iconName = aConnected ? getShapeHost().getName() : "pipeSide";
        final IIconContainer bodyIcon = GTMaterialIcons.block(iconName, material);
        final short[] rgba = bodyIcon.hasOverrideIcon() ? UNCOLORED_RGBA : materialRgba;
        ITexture body = TextureFactory.of(bodyIcon, Dyes.getModulation(aColorIndex, rgba));
        if (mIsRestrictive) return new ITexture[] { body, TextureFactory.of(PIPE_RESTRICTOR) };
        return new ITexture[] { body };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int ignoredSlotIndex) {
        return true;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return getPipeContent() * 64;
    }

    @Override
    public int maxProgresstime() {
        return getMaxPipeCapacity() * 64;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mLastReceivedFrom", (byte) mLastReceivedFrom.ordinal());
        if (GTMod.proxy.gt6Pipe) aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mLastReceivedFrom = ForgeDirection.getOrientation(aNBT.getByte("mLastReceivedFrom"));
        if (GTMod.proxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
        }
        if (mInventory.length == 0) {
            pendingInventory = aNBT.getTagList("Inventory", 10);
        } else {
            dropSlotsBeyondInventory(aNBT.getTagList("Inventory", 10));
        }
    }

    /// Drops the stacks of inventory NBT slots beyond the current slot count, which the base inventory load
    /// skips; they occur when a swap or reload lands on a material with fewer slots.
    private void dropSlotsBeyondInventory(NBTTagList inventoryTags) {
        for (int i = 0; i < inventoryTags.tagCount(); i++) {
            NBTTagCompound tag = inventoryTags.getCompoundTagAt(i);
            if (tag.getInteger("IntSlot") < mInventory.length) continue;
            ItemStack loaded = GTUtility.loadItem(tag);
            if (loaded != null && loaded.getItem() != ItemList.Display_Fluid.getItem()) {
                dropOverflow(loaded);
            }
        }
    }

    @Override
    public boolean needsClientTick() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && (aTick - mCurrentTransferStartTick) % 10 == 0) {
            if ((aTick - mCurrentTransferStartTick) % getTickTime() == 0) {
                mTransferredItems = 0;
                mCurrentTransferStartTick = 0;
            }

            if (!GTMod.proxy.gt6Pipe || mCheckConnections) checkConnections();

            doTickProfilingInThisTick = true;
            if (oLastReceivedFrom == mLastReceivedFrom) {
                doTickProfilingInThisTick = false;

                final ArrayList<IMetaTileEntityItemPipe> tPipeList = new ArrayList<>();

                for (boolean temp = true; temp && !isInventoryEmpty() && pipeCapacityCheck();) {
                    temp = false;
                    tPipeList.clear();
                    for (IMetaTileEntityItemPipe tTileEntity : GTUtility
                        .sortMapByValuesAcending(
                            IMetaTileEntityItemPipe.Util.scanPipes(this, new HashMap<>(), 0, false, false))
                        .keySet()) {
                        if (temp) break;
                        tPipeList.add(tTileEntity);
                        while (!temp && !isInventoryEmpty() && tTileEntity.sendItemStack(aBaseMetaTileEntity))
                            for (IMetaTileEntityItemPipe tPipe : tPipeList)
                                if (!tPipe.incrementTransferCounter(1)) temp = true;
                    }
                }
            }

            if (isInventoryEmpty()) mLastReceivedFrom = ForgeDirection.UNKNOWN;
            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (GTMod.proxy.gt6Pipe) {
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
            if (isConnectedAtSide(tSide)) {
                disconnect(tSide);
                GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.disconnected");
            } else {
                if (connect(tSide) > 0) GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.connected");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aPlayer.isSneaking()) return;

        final ItemStack handItem = aPlayer.inventory.getCurrentItem();
        if (handItem == null) return;

        trySwapShape(aBaseMetaTileEntity, aPlayer, handItem);
    }

    @Override
    public boolean letsIn(Cover cover) {
        return cover.letsItemsOut(-1);
    }

    @Override
    public boolean letsOut(Cover cover) {
        return cover.letsItemsOut(-1);
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        if (tileEntity == null) return false;

        final ForgeDirection oppositeSide = side.getOpposite();
        boolean connectable = GTUtility.isConnectableNonInventoryPipe(tileEntity, oppositeSide);

        final IGregTechTileEntity gTileEntity = (tileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tileEntity
            : null;
        if (gTileEntity != null) {
            if (gTileEntity.getMetaTileEntity() == null) return false;
            if (gTileEntity.getMetaTileEntity()
                .connectsToItemPipe(oppositeSide)) return true;
            connectable = true;
        }

        if (!connectable && ItemUtil.getItemIO(tileEntity, side) != null) {
            connectable = true;
        }

        return connectable;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.proxy.gt6Pipe;
    }

    @Override
    public boolean incrementTransferCounter(int aIncrement) {
        if (mTransferredItems == 0) mCurrentTransferStartTick = getBaseMetaTileEntity().getTimer();
        mTransferredItems += aIncrement;
        return pipeCapacityCheck();
    }

    @Override
    public boolean sendItemStack(Object aSender) {
        if (pipeCapacityCheck()) {
            final byte tOffset = (byte) getBaseMetaTileEntity().getRandomNumber(6);
            for (final byte i : ALL_VALID_SIDES) {
                final ForgeDirection tSide = ForgeDirection.getOrientation((i + tOffset) % 6);
                if (isConnectedAtSide(tSide)
                    && (isInventoryEmpty() || (tSide != mLastReceivedFrom || aSender != getBaseMetaTileEntity()))) {
                    if (insertItemStackIntoTileEntity(aSender, tSide)) return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean insertItemStackIntoTileEntity(Object aSender, ForgeDirection side) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .letsItemsOut(-1)) return false;

        final TileEntity neighbour = getBaseMetaTileEntity().getTileEntityAtSide(side);

        if (neighbour instanceof TileEntityHopper || neighbour instanceof TileEntityDispenser) {
            if (getBaseMetaTileEntity().getMetaIDAtSide(side) == side.getOpposite()
                .ordinal()) return false;
        }

        if (neighbour instanceof BaseMetaPipeEntity) return false;

        GTItemTransfer transfer = new GTItemTransfer();

        transfer.source(aSender, ForgeDirection.UNKNOWN);
        transfer.sink(neighbour, side.getOpposite());

        return transfer.transfer() > 0;
    }

    @Override
    public boolean pipeCapacityCheck() {
        return mTransferredItems <= 0 || getPipeContent() < getMaxPipeCapacity();
    }

    private int getPipeContent() {
        return mTransferredItems;
    }

    private int getMaxPipeCapacity() {
        return Math.max(1, getPipeCapacity());
    }

    /**
     * Amount of ItemStacks this Pipe can conduct per Second.
     */
    public int getPipeCapacity() {
        return inv().length;
    }

    @Override
    public int getStepSize() {
        int slots = hugeSlots();
        return slots <= 0 ? 0 : PipeStats.itemPipeStepSize(slots, getShapeSizeIndex(), mIsRestrictive);
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (side == ForgeDirection.UNKNOWN) return true;
        if (!isConnectedAtSide(side)) return false;
        return super.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        return side == ForgeDirection.UNKNOWN || isConnectedAtSide(side);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        final Cover cover = tTileEntity.getCoverAtSide(ForgeDirection.getOrientation(ordinalSide));
        final boolean tAllow = cover.letsItemsIn(-2) || cover.letsItemsOut(-2);
        if (tAllow) {
            if (cacheSides == null) cacheSides = super.getAccessibleSlotsFromSide(ordinalSide);
            return cacheSides;
        } else {
            return GTValues.emptyIntArray;
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == ForgeDirection.UNKNOWN || isConnectedAtSide(side);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (side == ForgeDirection.UNKNOWN) return true;
        if (!isConnectedAtSide(side)) return false;
        if (isInventoryEmpty()) mLastReceivedFrom = side;
        return mLastReceivedFrom == side && inv()[aIndex] == null;
    }

    @Override
    public String[] getDescription() {
        final int tickTime = getTickTime();
        final String capacity;
        if (tickTime == 20) {
            capacity = StatCollector
                .translateToLocalFormatted("gt.blockmachines.itempipe.capacity.persecond", getMaxPipeCapacity());
        } else if (tickTime % 20 == 0) {
            capacity = StatCollector.translateToLocalFormatted(
                "gt.blockmachines.itempipe.capacity.second",
                getMaxPipeCapacity(),
                tickTime / 20);
        } else {
            capacity = StatCollector
                .translateToLocalFormatted("gt.blockmachines.itempipe.capacity.tick", getMaxPipeCapacity(), tickTime);
        }
        return new String[] { capacity, StatCollector
            .translateToLocalFormatted("gt.blockmachines.itempipe.rounting_value", formatNumber(getStepSize())) };
    }

    private boolean isInventoryEmpty() {
        for (ItemStack tStack : inv()) if (tStack != null) return false;
        return true;
    }

    @Override
    public float getCollisionThickness() {
        return mThickNess;
    }

    public int getTickTime() {
        int slots = hugeSlots();
        return slots <= 0 ? 20 : PipeStats.itemPipeTickTime(slots, getShapeSizeIndex());
    }

    @Override
    public Material getMaterial() {
        return shapeMaterial();
    }

    @Override
    public String getPrefixKey() {
        return mPrefixKey;
    }

    @Override
    public String getMaterialKeyOverride() {
        return PipeShapeItemBlock.overrideKeyFor(shapeMaterial(), ".itempipe.newname");
    }
}
