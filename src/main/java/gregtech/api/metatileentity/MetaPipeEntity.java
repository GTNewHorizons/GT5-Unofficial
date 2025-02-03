package gregtech.api.metatileentity;

import static gregtech.api.enums.GTValues.GT;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtil;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.covers.CoverInfo;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaPipe Call the Constructor with the desired ID at the load-phase (not preload and
 * also not postload!) Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it. "new
 * GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
public abstract class MetaPipeEntity extends CommonMetaTileEntity implements IConnectable {

    /**
     * This variable tells, which directions the Block is connected to. It is a Bitmask.
     */
    public byte mConnections = 0;

    protected boolean mCheckConnections = false;
    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    /**
     * This registers your Machine at the List. Use only ID's larger than 2048 - the ones lower are reserved by GT.
     * See also the list in the API package - it has a description that contains all the reservations.
     * <p>
     * The constructor can be overloaded as follows:
     * <blockquote>
     *
     * <pre>
     *
     * public GT_MetaTileEntity_EBench(int id, String name, String nameRegional) {
     *     super(id, name, nameRegional);
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param aID the machine ID
     */
    public MetaPipeEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        this(aID, aBasicName, aRegionalName, aInvSlotCount, true);
    }

    public MetaPipeEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount, boolean aAddInfo) {
        super(aID, aBasicName, aRegionalName, aInvSlotCount);
        setBaseMetaTileEntity(new BaseMetaPipeEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);

        if (aAddInfo && GT.isClientSide()) {
            addInfo(aID);
        }
    }

    protected final void addInfo(int aID) {
        if (!GT.isClientSide()) return;

        ItemStack tStack = new ItemStack(GregTechAPI.sBlockMachines, 1, aID);
        Objects.requireNonNull(tStack.getItem())
            .addInformation(tStack, null, new ArrayList<>(), true);
    }

    /**
     * This is the normal Constructor.
     */
    public MetaPipeEntity(String aName, int aInvSlotCount) {
        super(aName, aInvSlotCount);
    }

    /**
     * For Pipe Rendering
     */
    public abstract float getThickNess();

    /**
     * For Pipe Rendering
     */
    public abstract boolean renderInside(ForgeDirection side);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    @Override
    public IGregTechTileEntity getBaseMetaTileEntity() {
        return mBaseMetaTileEntity;
    }

    @Override
    public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
            mBaseMetaTileEntity.getMetaTileEntity()
                .inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(GregTechAPI.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
    }

    public boolean isCoverOnSide(BaseMetaPipeEntity aPipe, EntityLivingBase aEntity) {
        ForgeDirection side = ForgeDirection.UNKNOWN;
        double difference = aEntity.posY - (double) aPipe.yCoord;
        if (difference > 0.6 && difference < 0.99) {
            side = ForgeDirection.UP;
        }
        if (difference < -1.5 && difference > -1.99) {
            side = ForgeDirection.DOWN;
        }
        difference = aEntity.posZ - (double) aPipe.zCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.NORTH;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.SOUTH;
        }
        difference = aEntity.posX - (double) aPipe.xCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.WEST;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.EAST;
        }
        boolean tCovered = side != ForgeDirection.UNKNOWN && mBaseMetaTileEntity.getCoverIDAtSide(side) > 0;
        if (isConnectedAtSide(side)) {
            tCovered = true;
        }
        // GT_FML_LOGGER.info("Cover: "+mBaseMetaTileEntity.getCoverIDAtSide(aSide));
        // toDo: filter cover ids that actually protect against temperature (rubber/plastic maybe?, more like asbestos)
        return tCovered;
    }

    @Deprecated
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        /* Do nothing */
    }

    @Deprecated
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Deprecated
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Deprecated
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool) {
        return onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public void onExplosion() {
        /* Do nothing */
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return false;
    }

    public int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill(aFluid, doFill);
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill_default(side, aFluid, doFill);
    }

    @Override
    public float getExplosionResistance(ForgeDirection side) {
        return (mConnections & IConnectable.HAS_FOAM) != 0 ? 50.0F : 5.0F;
    }

    @Override
    public void markDirty() {
        //
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        setCheckConnections();
    }

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    public void setCheckConnections() {
        mCheckConnections = true;
    }

    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return 0;
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        float tStrength = GTValues.getExplosionPowerForVoltage(aExplosionPower);
        int tX = getBaseMetaTileEntity().getXCoord(), tY = getBaseMetaTileEntity().getYCoord(),
            tZ = getBaseMetaTileEntity().getZCoord();
        World tWorld = getBaseMetaTileEntity().getWorld();
        tWorld.setBlock(tX, tY, tZ, Blocks.air);
        if (GregTechAPI.sMachineExplosions) {
            new WorldSpawnedEventBuilder.ExplosionEffectEventBuilder().setStrength(tStrength)
                .setSmoking(true)
                .setPosition(tX + 0.5, tY + 0.5, tZ + 0.5)
                .setWorld(tWorld)
                .run();
        }
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    protected boolean connectableColor(TileEntity tTileEntity) {
        // Determine if two entities are connectable based on their colorization:
        // Uncolored can connect to anything
        // If both are colored they must be the same color to connect.
        if (tTileEntity instanceof IColoredTileEntity) {
            if (getBaseMetaTileEntity().getColorization() >= 0) {
                final byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                return tColor < 0 || tColor == getBaseMetaTileEntity().getColorization();
            }
        }

        return true;
    }

    @Override
    public int connect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return 0;

        final ForgeDirection oppositeSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null || !baseMetaTile.isServerSide()) return 0;

        final CoverInfo coverInfo = baseMetaTile.getCoverInfoAtSide(side);

        final boolean alwaysLookConnected = coverInfo.alwaysLookConnected();
        final boolean letsIn = letsIn(coverInfo);
        final boolean letsOut = letsOut(coverInfo);

        // Careful - tTileEntity might be null, and that's ok -- so handle it
        final TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (!connectableColor(tTileEntity)) return 0;

        if ((alwaysLookConnected || letsIn || letsOut)) {
            // Are we trying to connect to a pipe? let's do it!
            final IMetaTileEntity tPipe = tTileEntity instanceof IGregTechTileEntity
                ? ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()
                : null;
            if (getClass().isInstance(tPipe) || (tPipe != null && tPipe.getClass()
                .isInstance(this))) {
                connectAtSide(side);
                if (!((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
                    // Make sure pipes all get together -- connect back to us if we're connecting to a pipe
                    ((IConnectable) tPipe).connect(oppositeSide);
                }
                return 1;
            } else if ((getGT6StyleConnection() && baseMetaTile.getAirAtSide(side)) || canConnect(side, tTileEntity)) {
                // Allow open connections to Air, if the GT6 style pipe/cables are enabled, so that it'll connect to
                // the next block placed down next to it
                connectAtSide(side);
                return 1;
            }
            if (!baseMetaTile.getWorld()
                .getChunkProvider()
                .chunkExists(baseMetaTile.getOffsetX(side, 1) >> 4, baseMetaTile.getOffsetZ(side, 1) >> 4)) {
                // Target chunk unloaded
                return -1;
            }
        }
        return 0;
    }

    protected void checkConnections() {
        // Verify connections around us. If GT6 style cables are not enabled then revert to old behavior and try
        // connecting to everything around us
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((!getGT6StyleConnection() || isConnectedAtSide(side)) && connect(side) == 0) {
                disconnect(side);
            }
        }
        mCheckConnections = false;
    }

    private void connectAtSide(ForgeDirection side) {
        mConnections |= side.flag;
    }

    @Override
    public void disconnect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return;
        mConnections &= ~side.flag;
        final ForgeDirection oppositeSide = side.getOpposite();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSide(side);
        IMetaTileEntity tPipe = tTileEntity == null ? null : tTileEntity.getMetaTileEntity();
        if ((this.getClass()
            .isInstance(tPipe)
            || (tPipe != null && tPipe.getClass()
                .isInstance(this)))
            && ((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
            ((IConnectable) tPipe).disconnect(oppositeSide);
        }
    }

    @Override
    public boolean isConnectedAtSide(ForgeDirection sideDirection) {
        return (mConnections & sideDirection.flag) != 0;
    }

    public boolean letsIn(CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    public boolean letsIn(CoverInfo coverInfo) {
        return false;
    }

    public boolean letsOut(CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    public boolean letsOut(CoverInfo coverInfo) {
        return false;
    }

    public boolean letsIn(CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    public boolean letsOut(CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    public boolean canConnect(ForgeDirection side, TileEntity tTileEntity) {
        return false;
    }

    public boolean getGT6StyleConnection() {
        return false;
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return false;
    }

    public void reloadLocks() {}

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (GregTechAPI.sColoredGUI) {
            if (GregTechAPI.sMachineMetalGUI) {
                dye = Dyes.MACHINE_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = Dyes.getDyeFromIndex(getBaseMetaTileEntity().getColorization());
            }
        }
        return GTUtil.getRGBInt(dye.getRGBA());
    }
}
