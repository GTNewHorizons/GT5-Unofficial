package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;
import static gregtech.api.enums.Textures.BlockIcons.PIPE_RESTRICTOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.ItemUtil;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.factory.itempipes.ItemPipeFactoryElement;
import gregtech.api.factory.itempipes.ItemPipeFactoryGrid;
import gregtech.api.factory.itempipes.ItemPipeFactoryNetwork;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityPipe;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEItemPipe extends MetaPipeEntity implements IMetaTileEntityPipe, ItemPipeFactoryElement {

    public final float mThickNess;
    public final Materials mMaterial;
    public final int mStepSize;
    public final int mTickTime;
    public int mTransferredItems = 0, stepCounter;
    public long mCurrentTransferStartTick = 0, prevStacksTransferred = 0;
    public ForgeDirection mLastReceivedFrom = ForgeDirection.UNKNOWN, oLastReceivedFrom = ForgeDirection.UNKNOWN;
    public boolean mIsRestrictive;
    private int[] cacheSides;
    private final int stackSizeMultiplier;

    private ItemPipeFactoryNetwork network;

    private boolean needsGraphUpdate = false;

    private ItemPipeFactoryNetwork.PipeJumpTransferState transferState;

    public MTEItemPipe(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive, int aTickTime, int stackSizeMultiplier) {
        super(aID, aName, aNameRegional, aInvSlotCount, false);
        mIsRestrictive = aIsRestrictive;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mStepSize = aStepSize;
        mTickTime = aTickTime;
        this.stackSizeMultiplier = stackSizeMultiplier;
        addInfo(aID);
    }

    public MTEItemPipe(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive, int aTickTime) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, aTickTime, 1);
    }

    public MTEItemPipe(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, 20, 1);
    }

    public MTEItemPipe(MTEItemPipe prototype) {
        super(prototype.mName, prototype.mInventory.length);
        mIsRestrictive = prototype.mIsRestrictive;
        mThickNess = prototype.mThickNess;
        mMaterial = prototype.mMaterial;
        mStepSize = prototype.mStepSize;
        mTickTime = prototype.mTickTime;
        stackSizeMultiplier = prototype.stackSizeMultiplier;
    }

    @Override
    public byte getTileEntityBaseType() {
        final int level = (mMaterial == null) ? 0 : GTUtility.clamp(mMaterial.mToolQuality, 0, 3);

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
        return new MTEItemPipe(this);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean redstoneLevel) {
        if (mIsRestrictive) {
            if (aConnected) {
                float tThickNess = getThickness();
                if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipe.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.374F) // 0.375
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.getTextureIndex()],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.499F) // 0.500
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.getTextureIndex()],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.749F) // 0.750
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.getTextureIndex()],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.874F) // 0.825
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.getTextureIndex()],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
            }
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.getTextureIndex()],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
        }
        if (aConnected) {
            float tThickNess = getThickness();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.getTextureIndex()],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.getTextureIndex()],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.getTextureIndex()],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
        }
        return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[OrePrefixes.pipe.getTextureIndex()],
            Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
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
        return getTransferredStackCount() * 64;
    }

    @Override
    public int maxProgresstime() {
        return getMaxTransferableStacks() * 64;
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
    }

    @Override
    public void onUnload() {
        super.onUnload();

        ItemPipeFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        ItemPipeFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public int connect(ForgeDirection side) {
        needsGraphUpdate = true;

        return super.connect(side);
    }

    @Override
    public void disconnect(ForgeDirection side) {
        needsGraphUpdate = true;

        super.disconnect(side);
    }

    @Override
    public void getNeighbours(Collection<ItemPipeFactoryElement> neighbours) {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null) return;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (!this.isConnectedAtSide(side)) continue;

            if (!(igte.getTileEntityAtSide(side) instanceof IGregTechTileEntity te)) continue;
            if (!(te.getMetaTileEntity() instanceof MTEItemPipe itemPipe)) continue;
            if (!itemPipe.isConnectedAtSide(side.getOpposite())) continue;

            neighbours.add(itemPipe);
        }
    }

    @Override
    public ItemPipeFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(ItemPipeFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public boolean isConnectedToInventory() {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null) return false;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (!this.isConnectedAtSide(side)) continue;

            TileEntity te = igte.getTileEntityAtSide(side);

            if (te instanceof IGregTechTileEntity te2 && te2.getMetaTileEntity() instanceof MTEItemPipe) continue;

            if (te instanceof IInventory) return true;
        }

        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setLong(
            "stacksxfered",
            transferState != null ? transferState.getTransferredStacks() : prevStacksTransferred);

        NBTTagList items = new NBTTagList();

        for (var e : GTUtility.getItemStackHistogram(Arrays.asList(mInventory))
            .object2LongEntrySet()) {
            NBTTagCompound itemTag = e.getKey()
                .getItemStack()
                .writeToNBT(new NBTTagCompound());

            itemTag.setLong("stackSize", e.getLongValue());

            items.appendTag(itemTag);
        }

        tag.setTag("items", items);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, ss, accessor, config);

        long xfered = accessor.getNBTData()
            .getLong("stacksxfered");

        EnumChatFormatting colour = EnumChatFormatting.GREEN;

        if (xfered > (long) (mInventory.length * 0.9)) {
            colour = EnumChatFormatting.RED;
        } else if (xfered > (long) (mInventory.length * 0.7)) {
            colour = EnumChatFormatting.GOLD;
        }

        ss.add(
            new GTTextBuilder("GT5U.waila.pipe_throughput").setBase(EnumChatFormatting.GRAY)
                .add(colour, GTUtility.formatNumbers(xfered))
                .addNumber(mInventory.length)
                .toString());

        if (!GuiScreen.isShiftKeyDown()) {
            ss.add(GTUtility.translate("GT5U.waila.shift_for_info"));
            return;
        }

        List<ObjectLongPair<String>> stacks = new ArrayList<>();

        for (NBTTagCompound tag : GTUtility.getCompoundTagList(accessor.getNBTData(), "items")) {
            stacks.add(
                ObjectLongPair.of(
                    ItemStack.loadItemStackFromNBT(tag)
                        .getDisplayName(),
                    tag.getLong("stackSize")));
        }

        stacks.sort(Comparator.comparing(ObjectLongPair::left));

        ss.add(GTUtility.translate("GT5U.gui.text.contents"));

        if (stacks.isEmpty()) {
            ss.add(GTUtility.translate("GT5U.gui.text.nothing"));
        } else {
            stacks.forEach(s -> {
                ss.add(
                    new GTTextBuilder("GT5U.gui.text.content-entry").setBase(EnumChatFormatting.GRAY)
                        .addText(s.left())
                        .addNumber(s.rightLong())
                        .toString());
            });
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        data.add("Pipe Network: " + GTTextBuilder.NUMERIC + (network == null ? "null" : network.id));
        int stepCounter = transferState != null ? transferState.getStepCounter() : this.stepCounter;
        data.add("Most recent routing step: " + GTTextBuilder.NUMERIC + stepCounter);
        if (transferState != null) {
            data.add("This pipe is skipped while pathing because it doesn't branch or interact with inventories");
        }

        return data.toArray(GTValues.emptyStringArray);
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (igte.isServerSide() && needsGraphUpdate) {
            needsGraphUpdate = false;

            ItemPipeFactoryGrid.INSTANCE.updateElement(this);
        }

        if (igte.isServerSide() && (aTick - mCurrentTransferStartTick) % 10 == 0) {
            if ((aTick - mCurrentTransferStartTick) % mTickTime == 0) {
                if (transferState != null && transferState.master() == this) {
                    transferState.resetTransfers();
                }

                prevStacksTransferred = mTransferredItems;
                mTransferredItems = 0;
                mCurrentTransferStartTick = 0;
            }

            if (!GTMod.proxy.gt6Pipe || mCheckConnections) checkConnections();

            doTickProfilingInThisTick = true;
            if (oLastReceivedFrom == mLastReceivedFrom) {
                doTickProfilingInThisTick = false;

                if (!isInventoryEmpty()) {
                    network.walkPipes(this, step -> {
                        ItemPipeFactoryNetwork.TransferInfo transferInfo = step.getTransferInfo();

                        int transferredStacks = step.pipe()
                            .sendItemStack(igte, transferInfo.stacksToTransfer(), transferInfo.stackMultiplier());

                        if (transferredStacks > 0) {
                            step.markChainUsed(transferredStacks);
                        }

                        return !isInventoryEmpty() && canTransferMoreItemsThisTick();
                    });
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
                GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("215", "Disconnected"));
            } else {
                if (connect(tSide) > 0) GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    public void onPlungerRightClick(ForgeDirection side, EntityPlayer player) {
        Object2LongOpenHashMap<GTUtility.ItemId> items = network.plungerNetwork();

        List<ItemStack> stacks = new ArrayList<>();

        Object2LongMaps.fastForEach(items, e -> {
            long amount = e.getLongValue();

            while (amount > 0) {
                int inStack = (int) Math.min(amount, Integer.MAX_VALUE);
                amount -= Integer.MAX_VALUE;

                stacks.add(
                    e.getKey()
                        .getItemStack(inStack));
            }
        });

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        GTUtility.dropItemsOrClusters(
            igte.getWorld(),
            igte.getOffsetX(side, 1) + 0.5f,
            igte.getOffsetY(side, 1) + 0.5f,
            igte.getOffsetZ(side, 1) + 0.5f,
            stacks);
    }

    @Override
    public List<ItemStack> removeAllItems() {
        List<ItemStack> list = new ArrayList<>();

        for (ItemStack stack : mInventory) {
            if (stack != null && stack.getItem() != null && stack.stackSize > 0) {
                list.add(stack);
            }
        }

        Arrays.fill(mInventory, null);
        markDirty();

        return list;
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
    public void incrementTransferCounter(int aIncrement) {
        if (mTransferredItems == 0) mCurrentTransferStartTick = getBaseMetaTileEntity().getTimer();
        mTransferredItems += aIncrement;
        canTransferMoreItemsThisTick();
    }

    @Override
    public int sendItemStack(IGregTechTileEntity sender, int maxStackToTransfer, int stackMultiplier) {
        if (!canTransferMoreItemsThisTick()) return 0;

        int offset = getBaseMetaTileEntity().getRandomNumber(6);

        for (final byte i : ALL_VALID_SIDES) {
            final ForgeDirection side = ForgeDirection.getOrientation((i + offset) % 6);

            if (!isConnectedAtSide(side)) continue;

            if (isInventoryEmpty() || side != mLastReceivedFrom || sender != getBaseMetaTileEntity()) {
                int transferredStacks = transferOneStack(sender, side, maxStackToTransfer, stackMultiplier);

                if (transferredStacks > 0) return transferredStacks;
            }
        }

        return 0;
    }

    private int transferOneStack(IGregTechTileEntity sender, ForgeDirection outputSide, int maxStackToTransfer,
        int stackMultiplier) {
        if (!getBaseMetaTileEntity().getCoverAtSide(outputSide)
            .letsItemsOut(-1)) return 0;

        final TileEntity sink = getBaseMetaTileEntity().getTileEntityAtSide(outputSide);

        if (sink == null) return 0;
        if (sink instanceof BaseMetaPipeEntity) return 0;

        if (sink instanceof TileEntityHopper || sink instanceof TileEntityDispenser) {
            if (getBaseMetaTileEntity().getMetaIDAtSide(outputSide) == outputSide.getOpposite()
                .ordinal()) return 0;
        }

        GTItemTransfer transfer = new GTItemTransfer();

        transfer.source(sender, ForgeDirection.UNKNOWN);
        transfer.sink(sink, outputSide.getOpposite());

        transfer.setMaxItemsPerTransfer(stackMultiplier * 64);
        transfer.setStacksToTransfer(maxStackToTransfer);

        transfer.transfer();

        return transfer.getPrevStacksTransferred();
    }

    @Override
    public boolean canTransferMoreItemsThisTick() {
        return mTransferredItems < getMaxTransferableStacks();
    }

    @Override
    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    @Override
    public int getRemainingTransferrableStacks() {
        return getMaxTransferableStacks() - mTransferredItems;
    }

    private int getTransferredStackCount() {
        return mTransferredItems;
    }

    @Override
    public int getMaxTransferableStacks() {
        return mInventory.length;
    }

    @Override
    public int getStackMultiplier() {
        return stackSizeMultiplier;
    }

    @Override
    public void setJumpTransferState(ItemPipeFactoryNetwork.PipeJumpTransferState transferState) {
        this.transferState = transferState;
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return GTUtility.longToInt((stack == null ? 64 : stack.getMaxStackSize()) * (long) stackSizeMultiplier);
    }

    @Override
    public int getStepSize() {
        return mStepSize;
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);

        if (side == ForgeDirection.UNKNOWN) return true;

        return isConnectedAtSide(side) && super.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);

        if (side == ForgeDirection.UNKNOWN) return true;

        return isConnectedAtSide(side);
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
        return mLastReceivedFrom == side && mInventory[aIndex] == null;
    }

    @Override
    public String[] getDescription() {
        List<String> desc = new ArrayList<>();

        if (mTickTime == 20) {
            desc.add("Item Capacity: %%%" + getMaxTransferableStacks() + "%%% Stacks/sec");
        } else if (mTickTime % 20 == 0) {
            desc.add(
                "Item Capacity: %%%" + getMaxTransferableStacks() + "%%% Stacks/%%%" + (mTickTime / 20) + "%%% sec");
        } else {
            desc.add("Item Capacity: %%%" + getMaxTransferableStacks() + "%%% Stacks/%%%" + mTickTime + "%%% ticks");
        }

        if (stackSizeMultiplier > 1) {
            desc.add("Stack size multiplier: %%%" + GTUtility.formatNumbers(stackSizeMultiplier));
        }

        desc.add("Routing Value: %%%" + GTUtility.formatNumbers(mStepSize));

        return desc.toArray(new String[0]);
    }

    private boolean isInventoryEmpty() {
        for (ItemStack tStack : mInventory) if (tStack != null) return false;
        return true;
    }

    @Override
    public float getCollisionThickness() {
        return mThickNess;
    }
}
