package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.VALID_SIDES;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityClassContainer;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.SyncedMultiTileEntity;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.net.GT_Packet_New;
import gregtech.api.net.data.CommonData;
import gregtech.api.net.data.CoordinateData;
import gregtech.api.net.data.MultiTileEntityData;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.MultiTileBasicRender;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MultiTileEntity extends CoverableTileEntity
    implements IMultiTileEntity, MultiTileBasicRender, SyncedMultiTileEntity {

    private ITexture baseTexture = null;
    private ITexture topOverlayTexture = null;
    private ITexture bottomOverlayTexture = null;
    private ITexture leftOverlayTexture = null;
    private ITexture rightOverlayTexture = null;
    private ITexture backOverlayTexture = null;
    private ITexture frontOverlayTexture = null;

    // A Bounding Box without having to constantly specify the Offset Coordinates.
    protected static final float[] PX_BOX = { 0, 0, 0, 1, 1, 1 };

    public Materials material = Materials._NULL;
    protected final boolean isTicking; // If this TileEntity is ticking at all

    // Checks if this TileEntity should refresh when the Block is being set.
    // This way you can toggle this check at any time.
    protected boolean shouldRefresh = true;

    protected boolean needsBlockUpdate = false; // This Variable is for a buffered Block Update.
    protected boolean forceFullSelectionBox = false; // This Variable is for forcing the Selection Box to be full.
    protected boolean needsUpdate = false;
    protected boolean hasInventoryChanged = false;
    protected boolean isPainted = false;
    @Nonnull
    protected ForgeDirection facing = ForgeDirection.WEST; // Default to WEST, so it renders facing Left in the
                                                           // inventory
    protected byte color;
    protected int rgba = GT_Values.UNCOLORED;
    private short mteID = GT_Values.W, mteRegistry = GT_Values.W;
    private String customName = null;
    private String ownerName = "";
    private UUID ownerUUID = GT_Utility.defaultUuid;
    private boolean lockUpgrade = false;

    private final GT_Packet_MultiTileEntity fullPacket = new GT_Packet_MultiTileEntity(false);
    private final GT_Packet_MultiTileEntity timedPacket = new GT_Packet_MultiTileEntity(false);
    private final GT_Packet_MultiTileEntity graphicPacket = new GT_Packet_MultiTileEntity(false);

    public MultiTileEntity(boolean isTicking) {
        this.isTicking = isTicking;
    }

    @Override
    public short getMultiTileEntityID() {
        return mteID;
    }

    @Override
    public short getMultiTileEntityRegistryID() {
        return mteRegistry;
    }

    @Override
    public void onRegistrationFirst(MultiTileEntityRegistry registry, short id) {
        GameRegistry.registerTileEntity(getClass(), getTileEntityName());
    }

    @Override
    public void initFromNBT(NBTTagCompound nbt, short muteID, short muteRegistry) {
        if (this.mteID == muteID && this.mteRegistry == muteRegistry) {
            return;
        }
        // Set ID and Registry ID.
        this.mteID = muteID;
        this.mteRegistry = muteRegistry;
        // Read the Default Parameters from NBT.
        if (nbt != null) readFromNBT(nbt);
    }

    @Override
    public void loadTextures(String folder) {
        // Loading the registry
        for (SidedTextureNames textureName : SidedTextureNames.TEXTURES) {
            ITexture texture;
            try {
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(
                        new ResourceLocation(
                            Mods.GregTech.ID,
                            "textures/blocks/multitileentity/" + folder + "/" + textureName.getName() + ".png"));
                texture = TextureFactory.of(new CustomIcon("multitileentity/" + folder + "/" + textureName.getName()));
            } catch (IOException ignored) {
                texture = TextureFactory.of(Textures.BlockIcons.VOID);
            }
            switch (textureName) {
                case Top -> topOverlayTexture = texture;
                case Bottom -> bottomOverlayTexture = texture;
                case Back -> backOverlayTexture = texture;
                case Front -> frontOverlayTexture = texture;
                case Left -> leftOverlayTexture = texture;
                case Right -> rightOverlayTexture = texture;
                case Base -> baseTexture = texture;
            }
        }
    }

    @Override
    public void copyTextures() {
        // Loading an instance
        final TileEntity referenceTileEntity = MultiTileEntityRegistry
            .getReferenceTileEntity(getMultiTileEntityRegistryID(), getMultiTileEntityID());
        if (!(referenceTileEntity instanceof MultiTileEntity canonicalEntity)) {
            return;
        }
        baseTexture = canonicalEntity.baseTexture;
        topOverlayTexture = canonicalEntity.topOverlayTexture;
        bottomOverlayTexture = canonicalEntity.bottomOverlayTexture;
        leftOverlayTexture = canonicalEntity.leftOverlayTexture;
        rightOverlayTexture = canonicalEntity.rightOverlayTexture;
        backOverlayTexture = canonicalEntity.backOverlayTexture;
        frontOverlayTexture = canonicalEntity.frontOverlayTexture;
    }

    @Override
    public ITexture getTexture(ForgeDirection side) {
        if (facing == side) {
            return TextureFactory.of(baseTexture, frontOverlayTexture);
        }

        if (facing.getOpposite() == side) {
            return TextureFactory.of(baseTexture, backOverlayTexture);
        }

        if (side == ForgeDirection.UP) {
            return TextureFactory.of(baseTexture, topOverlayTexture);
        }

        if (side == ForgeDirection.DOWN) {
            return TextureFactory.of(baseTexture, bottomOverlayTexture);
        }

        if (facing.getRotation(ForgeDirection.DOWN) == side) {
            return TextureFactory.of(baseTexture, rightOverlayTexture);
        } else {
            return TextureFactory.of(baseTexture, leftOverlayTexture);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // Check if it is a World/Chunk-Loading Process calling readFromNBT
        if (mteID == GT_Values.W || mteRegistry == GT_Values.W) {
            // Read the ID Tags first
            mteID = nbt.getShort(NBT.MTE_ID);
            mteRegistry = nbt.getShort(NBT.MTE_REG);
            // Add additional Default Parameters in case the Mod updated with new ones
            final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(mteRegistry);
            if (tRegistry != null) {
                final MultiTileEntityClassContainer tClass = tRegistry.getClassContainer(mteID);
                if (tClass != null) {
                    // Add the Default Parameters. Useful for things that differ between different tiers/types of the
                    // same machine
                    nbt = GT_Util.fuseNBT(nbt, tClass.getParameters());
                }
            }
        }
        // read the Coords if it has them.
        if (nbt.hasKey("x")) xCoord = nbt.getInteger("x");
        if (nbt.hasKey("y")) yCoord = nbt.getInteger("y");
        if (nbt.hasKey("z")) zCoord = nbt.getInteger("z");
        // read the custom Name.
        if (nbt.hasKey(NBT.DISPLAY)) customName = nbt.getCompoundTag(NBT.DISPLAY)
            .getString(NBT.CUSTOM_NAME);

        // And now everything else.
        try {
            if (nbt.hasKey(NBT.MATERIAL)) material = Materials.get(nbt.getString(NBT.MATERIAL));
            if (nbt.hasKey(NBT.COLOR)) rgba = nbt.getInteger(NBT.COLOR);

            ownerName = nbt.getString(NBT.OWNER);
            try {
                ownerUUID = UUID.fromString(nbt.getString(NBT.OWNER_UUID));
            } catch (IllegalArgumentException e) {
                ownerUUID = null;
            }
            if (nbt.hasKey(NBT.LOCK_UPGRADE)) lockUpgrade = nbt.getBoolean(NBT.LOCK_UPGRADE);
            if (nbt.hasKey(NBT.FACING)) facing = ForgeDirection.getOrientation(nbt.getInteger(NBT.FACING));

            readCoverNBT(nbt);
            readTasksNBT(nbt);
            readMultiTileNBT(nbt);

            if (NetworkUtils.isDedicatedClient()) {
                if (GregTech_API.sBlockIcons == null && nbt.hasKey(NBT.TEXTURE_FOLDER)) {
                    loadTextures(nbt.getString(NBT.TEXTURE_FOLDER));
                } else {
                    copyTextures();
                }
            }

            if (mSidedRedstone.length != 6) mSidedRedstone = new byte[] { 15, 15, 15, 15, 15, 15 };

            updateCoverBehavior();

        } catch (Throwable e) {
            GT_FML_LOGGER.error("readFromNBT", e);
        }
    }

    public void readMultiTileNBT(NBTTagCompound aNBT) {
        /* Do Nothing */
    }

    protected void readTasksNBT(NBTTagCompound nbt) {}

    @Override
    public final void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        // write the IDs
        nbt.setShort(NBT.MTE_ID, mteID);
        nbt.setShort(NBT.MTE_REG, mteRegistry);
        // write the Custom Name
        if (GT_Utility.isStringValid(customName)) {
            final NBTTagCompound displayNBT;
            if (nbt.hasKey(NBT.DISPLAY)) {
                displayNBT = nbt.getCompoundTag(NBT.DISPLAY);
            } else {
                displayNBT = new NBTTagCompound();
                nbt.setTag(NBT.DISPLAY, displayNBT);
            }
            displayNBT.setString(NBT.CUSTOM_NAME, customName);
        }

        // write the rest
        try {
            nbt.setString(NBT.OWNER, ownerName);
            nbt.setString(NBT.OWNER_UUID, ownerUUID == null ? "" : ownerUUID.toString());
            nbt.setBoolean(NBT.LOCK_UPGRADE, lockUpgrade);
            nbt.setInteger(NBT.FACING, facing.ordinal());

            writeCoverNBT(nbt, false);
            writeTasksNBT(nbt);
            writeMultiTileNBT(nbt);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("writeToNBT", e);
        }
    }

    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        /* Do Nothing */
    }

    protected void writeTasksNBT(NBTTagCompound aNBT) {}

    @Override
    public NBTTagCompound writeItemNBT(NBTTagCompound aNBT) {
        writeCoverNBT(aNBT, true);
        if (shouldSaveNBTToItemStack()) {
            writeTasksNBT(aNBT);
            writeMultiTileNBT(aNBT);
        }
        return aNBT;
    }

    protected boolean shouldSaveNBTToItemStack() {
        return false;
    }

    @Override
    public boolean useModularUI() {
        return false;
    }

    @Override
    public long getTimer() {
        return 0;
    }

    @Override
    public int getRandomNumber(int aRange) {
        return XSTR.XSTR_INSTANCE.nextInt(aRange);
    }

    @Override
    public TileEntity getTileEntity(int aX, int aY, int aZ) {
        if (worldObj == null
            || (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ))) return null;
        return GT_Util.getTileEntity(worldObj, aX, aY, aZ, true);
    }

    @Override
    public boolean canUpdate() {
        return isTicking && shouldRefresh;
    }

    @Override
    public boolean shouldRefresh(Block aOldBlock, Block aNewBlock, int aOldMeta, int aNewMeta, World aWorld, int aX,
        int aY, int aZ) {
        return shouldRefresh || aOldBlock != aNewBlock;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (needsBlockUpdate) doBlockUpdate();
    }

    public void doBlockUpdate() {
        final Block tBlock = getBlock(getCoords());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, tBlock);
        if (this.providesStrongPower()) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (getBlockAtSide(side)
                    .isNormalCube(worldObj, xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)) {
                    worldObj.notifyBlocksOfNeighborChange(
                        xCoord + side.offsetX,
                        yCoord + side.offsetY,
                        zCoord + side.offsetZ,
                        tBlock,
                        side.getOpposite()
                            .ordinal());
                }
            }
        }
        needsBlockUpdate = false;
    }

    @Override
    public boolean shouldSideBeRendered(ForgeDirection side) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(side, 1);
        // TODO: check to an interface
        // if (getBlockAtSide(aSide) == Blocks.glass) return false;
        return tTileEntity instanceof IMultiTileEntity mte ? !mte.isSurfaceOpaque(side.getOpposite())
            : !getBlockAtSide(side).isOpaqueCube();
    }

    @Override
    public boolean isSurfaceOpaque(ForgeDirection side) {
        return true;
    }

    @Override
    public void setCustomName(String aName) {
        customName = aName;
    }

    @Override
    public String getCustomName() {
        return GT_Utility.isStringValid(customName) ? customName : null;
    }

    @Override
    public byte getColorization() {
        // TODO
        return 0;
    }

    @Override
    public boolean unpaint() {
        return false;
    }

    @Override
    public byte setColorization(byte aColor) {
        // TODO
        return 0;
    }

    @Override
    public boolean isPainted() {
        return false;
    }

    @Override
    public boolean paint(int aRGB) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public ForgeDirection getFrontFacing() {
        return facing;
    }

    /**
     * Sets the main facing to {aSide} and update as appropriately
     *
     * @return Whether the facing was changed
     */
    @Override
    public boolean setMainFacing(ForgeDirection side) {
        if (!isValidFacing(side)) return false;
        facing = side;

        issueClientUpdate();
        issueBlockUpdate();
        onFacingChange();
        checkDropCover();
        doEnetUpdate();

        if (shouldTriggerBlockUpdate()) {
            // If we're triggering a block update this will call onMachineBlockUpdate()
            GregTech_API.causeMachineUpdate(worldObj, xCoord, yCoord, zCoord);
        } else {
            // If we're not trigger a cascading one, call the update here.
            onMachineBlockUpdate();
        }
        return true;
    }

    @Override
    public int getPaint() {
        return this.rgba;
    }

    @Override
    public ForgeDirection getBackFacing() {
        return facing.getOpposite();
    }

    @Override
    public boolean isValidFacing(ForgeDirection side) {
        return side != ForgeDirection.UNKNOWN && getValidFacings()[side.ordinal()];
    }

    @Override
    public boolean[] getValidFacings() {
        return VALID_SIDES;
    }

    @Override
    public void issueCoverUpdate(ForgeDirection side) {
        super.issueCoverUpdate(side);
        issueClientUpdate();
    }

    public AxisAlignedBB box(double[] aBox) {
        return AxisAlignedBB.getBoundingBox(
            xCoord + aBox[0],
            yCoord + aBox[1],
            zCoord + aBox[2],
            xCoord + aBox[3],
            yCoord + aBox[4],
            zCoord + aBox[5]);
    }

    public boolean box(AxisAlignedBB aAABB, List<AxisAlignedBB> aList, double aMinX, double aMinY, double aMinZ,
        double aMaxX, double aMaxY, double aMaxZ) {
        final AxisAlignedBB tBox = box(aMinX, aMinY, aMinZ, aMaxX, aMaxY, aMaxZ);
        return tBox.intersectsWith(aAABB) && aList.add(tBox);
    }

    @Override
    public void onFacingChange() {
        /* Do nothing */
    }

    public AxisAlignedBB box(double aMinX, double aMinY, double aMinZ, double aMaxX, double aMaxY, double aMaxZ) {
        return AxisAlignedBB.getBoundingBox(
            xCoord + aMinX,
            yCoord + aMinY,
            zCoord + aMinZ,
            xCoord + aMaxX,
            yCoord + aMaxY,
            zCoord + aMaxZ);
    }

    @Override
    public boolean shouldTriggerBlockUpdate() {
        return false;
    }

    public boolean box(AxisAlignedBB aAABB, List<AxisAlignedBB> aList, double[] aBox) {
        final AxisAlignedBB tBox = box(aBox[0], aBox[1], aBox[2], aBox[3], aBox[4], aBox[5]);
        return tBox.intersectsWith(aAABB) && aList.add(tBox);
    }

    @Override
    public void onMachineBlockUpdate() {
        /* Do nothing */
    }

    public boolean box(AxisAlignedBB aAABB, List<AxisAlignedBB> aList, float[] aBox) {
        final AxisAlignedBB tBox = box(aBox[0], aBox[1], aBox[2], aBox[3], aBox[4], aBox[5]);
        return tBox.intersectsWith(aAABB) && aList.add(tBox);
    }

    public boolean box(AxisAlignedBB aAABB, List<AxisAlignedBB> aList) {
        final AxisAlignedBB tBox = box(PX_BOX);
        return tBox.intersectsWith(aAABB) && aList.add(tBox);
    }

    public AxisAlignedBB box(float[] aBox) {
        return AxisAlignedBB.getBoundingBox(
            xCoord + aBox[0],
            yCoord + aBox[1],
            zCoord + aBox[2],
            xCoord + aBox[3],
            yCoord + aBox[4],
            zCoord + aBox[5]);
    }

    public boolean box(Block aBlock) {
        aBlock.setBlockBounds(0, 0, 0, 1, 1, 1);
        return true;
    }

    /**
     * Causes a general Texture update.
     * <p/>
     * Only used Client Side to mark Blocks dirty.
     */
    @Override
    public void issueTextureUpdate() {
        if (!isTicking) {
            markBlockForUpdate();
        } else {
            needsUpdate = true;
        }
    }

    public boolean box(Block aBlock, double[] aBox) {
        aBlock.setBlockBounds(
            (float) aBox[0],
            (float) aBox[1],
            (float) aBox[2],
            (float) aBox[3],
            (float) aBox[4],
            (float) aBox[5]);
        return true;
    }

    protected void markBlockForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        // worldObj.func_147479_m(xCoord, yCoord, zCoord);
        needsUpdate = false;
    }

    public boolean box(Block aBlock, float[] aBox) {
        aBlock.setBlockBounds(aBox[0], aBox[1], aBox[2], aBox[3], aBox[4], aBox[5]);
        return true;
    }

    @Override
    public void onTileEntityPlaced() {
        /* empty */
    }

    public boolean box(Block aBlock, double aMinX, double aMinY, double aMinZ, double aMaxX, double aMaxY,
        double aMaxZ) {
        aBlock.setBlockBounds((float) aMinX, (float) aMinY, (float) aMinZ, (float) aMaxX, (float) aMaxY, (float) aMaxZ);
        return true;
    }

    @Override
    public void setShouldRefresh(boolean aShouldRefresh) {
        shouldRefresh = aShouldRefresh;
    }

    /**
     * shouldJoinIc2Enet - defaults to false, override to change
     */
    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    @Override
    public final void addCollisionBoxesToList(AxisAlignedBB aAABB, List<AxisAlignedBB> aList, Entity aEntity) {
        box(getCollisionBoundingBoxFromPool(), aAABB, aList);
    }

    /**
     * Simple Function to prevent Block Updates from happening multiple times within the same Tick.
     */
    @Override
    public final void issueBlockUpdate() {
        if (isTicking) needsBlockUpdate = true;
        else doBlockUpdate();
    }

    @Override
    public boolean isStillValid() {
        return !isInvalid();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
        return true;
    }

    public AxisAlignedBB box() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public boolean box(AxisAlignedBB aBox, AxisAlignedBB aAABB, List<AxisAlignedBB> aList) {
        return aBox != null && aBox.intersectsWith(aAABB) && aList.add(aBox);
    }

    public float[] shrunkBox() {
        return PX_BOX;
    }

    @Override
    public void setBlockBoundsBasedOnState(Block aBlock) {
        box(aBlock);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool() {
        return box();
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool() {
        if (forceFullSelectionBox) return box();
        return box(shrunkBox());
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition aTarget) {
        final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(mteRegistry);
        return tRegistry == null ? null : tRegistry.getItem(mteID, writeItemNBT(new NBTTagCompound()));
    }

    @Override
    public void onBlockAdded() {}

    @Override
    public String getOwnerName() {
        if (GT_Utility.isStringInvalid(ownerName)) return "Player";
        return ownerName;
    }

    @Override
    public String setOwnerName(String aName) {
        if (GT_Utility.isStringInvalid(aName)) return ownerName = "Player";
        return ownerName = aName;
    }

    @Override
    public UUID getOwnerUuid() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {
        ownerUUID = uuid;
    }

    @Override
    public boolean onPlaced(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        ForgeDirection side, float aHitX, float aHitY, float aHitZ) {
        facing = getSideForPlayerPlacing(aPlayer, facing, getValidFacings());
        setOwnerUuid(aPlayer.getUniqueID());
        setOwnerName(aPlayer.getDisplayName());
        onFacingChange();
        return true;
    }

    @Override
    public boolean allowInteraction(Entity aEntity) {
        return true;
    }

    public boolean allowRightclick(Entity aEntity) {
        return allowInteraction(aEntity);
    }

    @Override
    public boolean onBlockActivated(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ) {
        try {
            return allowRightclick(aPlayer) && onRightClick(aPlayer, side, aX, aY, aZ);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("onBlockActivated Failed", e);
            e.printStackTrace(GT_Log.err);
            return true;
        }
    }

    @Override
    public boolean onRightClick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ) {
        if (isClientSide()) {
            // Configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                final ForgeDirection tSide = (getCoverIDAtSide(side) == 0)
                    ? GT_Utility.determineWrenchingSide(side, aX, aY, aZ)
                    : side;
                return (getCoverBehaviorAtSideNew(tSide).hasCoverGUI());
            } else if (getCoverBehaviorAtSideNew(side).onCoverRightclickClient(side, this, aPlayer, aX, aY, aZ)) {
                return true;
            }

            if (!getCoverInfoAtSide(side).isGUIClickable()) return false;
        } else { // server side
            if (!privateAccess() || aPlayer.getDisplayName()
                .equalsIgnoreCase(getOwnerName())) {
                final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
                final ForgeDirection wrenchSide = GT_Utility.determineWrenchingSide(side, aX, aY, aZ);

                if (tCurrentItem != null) {
                    if (getColorization() >= 0
                        && GT_Utility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                        // TODO (Colorization)
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList))
                        return onWrenchRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList))
                        return onScrewdriverRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sHardHammerList))
                        return onHammerRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList))
                        return onMalletRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList))
                        return onSolderingRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWireCutterList))
                        return onWireCutterRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ, tCurrentItem);

                    final ForgeDirection coverSide = getCoverIDAtSide(side) == 0 ? wrenchSide : side;

                    if (getCoverIDAtSide(coverSide) == 0) {
                        if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCovers.keySet())) {
                            if (GregTech_API.getCoverBehaviorNew(tCurrentItem)
                                .isCoverPlaceable(coverSide, tCurrentItem, this)
                                && allowCoverOnSide(coverSide, new GT_ItemStack(tCurrentItem))) {
                                setCoverItemAtSide(coverSide, tCurrentItem);
                                if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
                                GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.IC2_TOOLS_WRENCH,
                                    1.0F,
                                    -1,
                                    xCoord,
                                    yCoord,
                                    zCoord);
                                issueClientUpdate();
                            }
                            sendCoverDataIfNeeded();
                            return true;
                        }
                    } else {
                        if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCrowbarList)) {
                            if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                                GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.RANDOM_BREAK,
                                    1.0F,
                                    -1,
                                    xCoord,
                                    yCoord,
                                    zCoord);
                                dropCover(coverSide, side, false);
                            }
                            sendCoverDataIfNeeded();
                            return true;
                        }
                    }
                } else if (aPlayer.isSneaking()) { // Sneak click, no tool -> open cover config if possible.
                    side = (getCoverIDAtSide(side) == 0) ? GT_Utility.determineWrenchingSide(side, aX, aY, aZ) : side;
                    return getCoverIDAtSide(side) > 0 && getCoverBehaviorAtSideNew(side).onCoverShiftRightClick(
                        side,
                        getCoverIDAtSide(side),
                        getComplexCoverDataAtSide(side),
                        this,
                        aPlayer);
                }

                if (getCoverBehaviorAtSideNew(side).onCoverRightClick(
                    side,
                    getCoverIDAtSide(side),
                    getComplexCoverDataAtSide(side),
                    this,
                    aPlayer,
                    aX,
                    aY,
                    aZ)) return true;

                if (!getCoverInfoAtSide(side).isGUIClickable()) return false;

                if (aPlayer.getHeldItem() != null && aPlayer.getHeldItem()
                    .getItem() instanceof ItemBlock) {
                    return false;
                }

                return openModularUi(aPlayer, side);
            }
        }
        return false;
    }

    public boolean hasGui(ForgeDirection side) {
        return false;
    }

    boolean openModularUi(EntityPlayer aPlayer, ForgeDirection side) {
        if (!hasGui(side) || !isServerSide()) {
            System.out.println("No GUI or Not Serverside");
            return false;
        }

        GT_UIInfos.openGTTileEntityUI(this, aPlayer);
        System.out.println("Trying to open a UI");
        return true;
    }

    public boolean onWrenchRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ, ItemStack aTool) {
        if (setMainFacing(wrenchSide)) {
            GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
            GT_Utility.sendSoundToPlayers(worldObj, SoundResource.IC2_TOOLS_WRENCH, 1.0F, -1, xCoord, yCoord, zCoord);
        }
        return onWrenchRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    public boolean onScrewdriverRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
            setCoverDataAtSide(
                wrenchSide,
                getCoverBehaviorAtSideNew(wrenchSide).onCoverScrewdriverClick(
                    wrenchSide,
                    getCoverIDAtSide(wrenchSide),
                    getComplexCoverDataAtSide(wrenchSide),
                    this,
                    aPlayer,
                    aX,
                    aY,
                    aZ));
            // TODO: Update connections!
            GT_Utility.sendSoundToPlayers(worldObj, SoundResource.IC2_TOOLS_WRENCH, 1.0F, -1, xCoord, yCoord, zCoord);
        }
        return onScrewdriverRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    public boolean onHammerRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ, ItemStack aTool) {

        return onHammerRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ, ItemStack aTool) {

        return onMalletRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    public boolean onSolderingRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ, ItemStack aTool) {

        return onSolderingRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    public boolean onWireCutterRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ, ItemStack aTool) {

        return onWireCutterRightClick(aPlayer, tCurrentItem, wrenchSide, aX, aY, aZ);
    }

    @Deprecated
    public boolean onHammerRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        return true;
    }

    @Deprecated
    public boolean onSolderingRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ) {
        return true;
    }

    @Deprecated
    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        return true;
    }

    @Deprecated
    public boolean onWireCutterRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ) {
        return true;
    }

    @Deprecated
    public boolean onWrenchRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        return true;
    }

    @Deprecated
    public boolean onScrewdriverRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide,
        float aX, float aY, float aZ) {
        return true;
    }

    @Override
    public float getExplosionResistance(Entity aExploder, double aExplosionX, double aExplosionY, double aExplosionZ) {
        return getExplosionResistance();
    }

    @Override
    public float getExplosionResistance() {
        return 10.0F;
    }

    @Override
    public void onExploded(Explosion aExplosion) {}

    @Override
    public boolean isSideSolid(ForgeDirection side) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(int aFortune, boolean aSilkTouch) {
        final ArrayList<ItemStack> rList = new ArrayList<>();
        final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID());
        if (tRegistry != null) rList.add(tRegistry.getItem(getMultiTileEntityID(), writeItemNBT(new NBTTagCompound())));
        return rList;
    }

    @Override
    public boolean onBlockBroken() {
        isDead = true;
        onBaseTEDestroyed();
        return false;
    }

    @Override
    public boolean getSubItems(MultiTileEntityBlock block, Item item, CreativeTabs tab, List<ItemStack> list,
        short aID) {
        return true;
    }

    @Override
    public boolean recolourBlock(ForgeDirection side, byte aColor) {
        // if (aColor > 15 || aColor < -1) aColor = -1;
        // if(paint((byte) (aColor + 1))) {
        //// updateClientData();
        //// causeBlockUpdate();
        // return true;
        // }
        // if (unpaint()) {updateClientData(); causeBlockUpdate(); return T;}
        // mColor = (byte) (aColor + 1);
        //// if (canAccessData()) mMetaTileEntity.onColorChangeServer(aColor);
        return false;
    }

    @Override
    public boolean playerOwnsThis(EntityPlayer aPlayer, boolean aCheckPrecicely) {
        if (aCheckPrecicely || privateAccess() || (ownerName.length() == 0))
            if ((ownerName.length() == 0) && isServerSide()) {
                setOwnerName(aPlayer.getDisplayName());
                setOwnerUuid(aPlayer.getUniqueID());
            } else return !privateAccess() || aPlayer.getDisplayName()
                .equals("Player") || ownerName.equals("Player") || ownerName.equals(aPlayer.getDisplayName());
        return true;
    }

    @Override
    public boolean privateAccess() {
        return lockUpgrade;
    }

    /**
     * @return a Packet containing all Data which has to be synchronised to the Client - Override as needed
     */
    public GT_Packet_MultiTileEntity getClientDataPacket() {

        final GT_Packet_MultiTileEntity packet = new GT_Packet_MultiTileEntity(false);
        return packet;
    }

    @Override
    public void sendClientData(EntityPlayerMP aPlayer) {
        if (worldObj == null || worldObj.isRemote) return;
        final GT_Packet_New tPacket = getClientDataPacket();
        if (aPlayer == null) {
            GT_Values.NW.sendPacketToAllPlayersInRange(worldObj, tPacket, getXCoord(), getZCoord());
        } else {
            GT_Values.NW.sendToPlayer(tPacket, aPlayer);
        }
        sendCoverDataIfNeeded();
    }

    @Override
    public boolean receiveClientData(int aEventID, int aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (isClientSide()) {
            issueTextureUpdate();
            switch (aEventID) {
                case GregTechTileClientEvents.CHANGE_COMMON_DATA:
                    facing = ForgeDirection.getOrientation(aValue & 7);
                    // mActive = ((aValue & 8) != 0);
                    mRedstone = ((aValue & 16) != 0);
                    // mLockUpgrade = ((aValue&32) != 0);
                    // mWorks = ((aValue & 64) != 0);
                    break;
                case GregTechTileClientEvents.CHANGE_CUSTOM_DATA:
                    // Nothing here, currently
                    break;
                case GregTechTileClientEvents.CHANGE_COLOR:
                    if (aValue > 16 || aValue < 0) aValue = 0;
                    color = (byte) aValue;
                    break;
                case GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT:
                    mSidedRedstone[0] = (byte) ((aValue & 1) == 1 ? 15 : 0);
                    mSidedRedstone[1] = (byte) ((aValue & 2) == 2 ? 15 : 0);
                    mSidedRedstone[2] = (byte) ((aValue & 4) == 4 ? 15 : 0);
                    mSidedRedstone[3] = (byte) ((aValue & 8) == 8 ? 15 : 0);
                    mSidedRedstone[4] = (byte) ((aValue & 16) == 16 ? 15 : 0);
                    mSidedRedstone[5] = (byte) ((aValue & 32) == 32 ? 15 : 0);
                    break;
                // case GregTechTileClientEvents.DO_SOUND:
                // if (mTickTimer > 20)
                // doSound((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                // break;
                // case GregTechTileClientEvents.START_SOUND_LOOP:
                // if (mTickTimer > 20)
                // startSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                // break;
                // case GregTechTileClientEvents.STOP_SOUND_LOOP:
                // if (mTickTimer > 20)
                // stopSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                // break;
                // case GregTechTileClientEvents.CHANGE_LIGHT:
                // mLightValue = (byte) aValue;
                // break;
            }
        }
        return true;
    }

    @Override
    public Packet getDescriptionPacket() {
        issueClientUpdate();
        return null;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(String.format("Facing: %s", getFrontFacing().name()));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        final ArrayList<String> tList = new ArrayList<>();
        if (aLogLevel > 2) {
            tList.add(
                "MultiTileRegistry-ID: " + EnumChatFormatting.BLUE
                    + mteRegistry
                    + EnumChatFormatting.RESET
                    + " MultiTile-ID: "
                    + EnumChatFormatting.BLUE
                    + mteID
                    + EnumChatFormatting.RESET);
        }

        addDebugInfo(aPlayer, aLogLevel, tList);

        return tList;
    }

    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        /* Do nothing */
    }

    /**
     * Energy - Do nothing by Default
     */
    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        return false;
    }

    @Override
    public long getUniversalEnergyStored() {
        return 0;
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return 0;
    }

    @Override
    public long getOutputAmperage() {
        return 0;
    }

    @Override
    public long getOutputVoltage() {
        return 0;
    }

    @Override
    public long getInputAmperage() {
        return 0;
    }

    @Override
    public long getInputVoltage() {
        return 0;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long energy, boolean ignoreTooLittleEnergy) {
        return false;
    }

    @Override
    public boolean increaseStoredEnergyUnits(long energy, boolean ignoreTooMuchEnergy) {
        return false;
    }

    @Override
    public boolean drainEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return false;
    }

    @Override
    public long getAverageElectricInput() {
        return 0;
    }

    @Override
    public long getAverageElectricOutput() {
        return 0;
    }

    @Override
    public long getStoredEU() {
        return 0;
    }

    @Override
    public long getEUCapacity() {
        return 0;
    }

    @Override
    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return 0;
    }

    @Override
    public boolean inputEnergyFrom(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side) {
        return false;
    }

    /**
     * Inventory - Do nothing by default
     */

    @Override
    public boolean hasInventoryBeenModified() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        return false;
    }

    @Override
    public void markInventoryBeenModified() {
        hasInventoryChanged = true;
    }

    /*
     * Cover Helpers
     */

    public boolean coverLetsFluidIn(ForgeDirection side, Fluid aFluid) {
        return getCoverInfoAtSide(side).letsFluidIn(aFluid);
    }

    public boolean coverLetsFluidOut(ForgeDirection side, Fluid aFluid) {
        return getCoverInfoAtSide(side).letsFluidOut(aFluid);
    }

    public boolean coverLetsEnergyIn(ForgeDirection side) {
        return getCoverInfoAtSide(side).letsEnergyIn();
    }

    public boolean coverLetsEnergyOut(ForgeDirection side) {
        return getCoverInfoAtSide(side).letsEnergyOut();
    }

    public boolean coverLetsItemsIn(ForgeDirection side, int aSlot) {
        return getCoverInfoAtSide(side).letsItemsIn(aSlot);
    }

    public boolean coverLetsItemsOut(ForgeDirection side, int aSlot) {
        return getCoverInfoAtSide(side).letsItemsOut(aSlot);
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(Item.getItemById(getMultiTileEntityRegistryID()), (int) aAmount, getMultiTileEntityID());
    }

    protected enum SidedTextureNames {

        Base("base"),
        Left("left"),
        Right("right"),
        Top("top"),
        Bottom("bottom"),
        Back("back"),
        Front("front");

        private final String name;
        public static final SidedTextureNames[] TEXTURES = { Base, Left, Right, Top, Bottom, Back, Front };

        SidedTextureNames(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    protected enum StatusTextures {

        Active("active", false),
        ActiveWithGlow("active_glow", true),
        Inactive("inactive", false),
        InactiveWithGlow("inactive_glow", true);

        private final String name;
        private final boolean hasGlow;
        public static final StatusTextures[] TEXTURES = { Active, ActiveWithGlow, Inactive, InactiveWithGlow };

        StatusTextures(String name, boolean hasGlow) {
            this.name = name;
            this.hasGlow = hasGlow;
        }

        public String getName() {
            return name;
        }

        public boolean hasGlow() {
            return hasGlow;
        }
    }

    @Override
    public void getFullPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new CommonData(mStrongRedstone, color, (byte) 0));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void getGraphicPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void getTimedPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void sendFullPacket(@Nonnull EntityPlayerMP player) {
        fullPacket.clearData();
        getFullPacketData(fullPacket);
        GT_Values.NW.sendToPlayer(fullPacket, player);
    }

    @Override
    public void sendGraphicPacket() {
        graphicPacket.clearData();
        getGraphicPacketData(graphicPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(worldObj, graphicPacket, getXCoord(), getZCoord());
    }

    @Override
    public void sendTimedPacket() {
        timedPacket.clearData();
        getTimedPacketData(timedPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(worldObj, timedPacket, getXCoord(), getZCoord());
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
    }
}
