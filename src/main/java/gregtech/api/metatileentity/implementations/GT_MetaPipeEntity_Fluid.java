package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.Translocator;
import static gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid.Border.BOTTOM;
import static gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid.Border.LEFT;
import static gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid.Border.RIGHT;
import static gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid.Border.TOP;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Optional;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.GT_Cover_Drain;
import gregtech.common.covers.GT_Cover_FluidRegulator;

public class GT_MetaPipeEntity_Fluid extends MetaPipeEntity {

    protected static final EnumMap<ForgeDirection, EnumMap<Border, ForgeDirection>> FACE_BORDER_MAP = new EnumMap<>(
        ForgeDirection.class);

    static {
        FACE_BORDER_MAP.put(DOWN, borderMap(NORTH, SOUTH, EAST, WEST));
        FACE_BORDER_MAP.put(UP, borderMap(NORTH, SOUTH, WEST, EAST));
        FACE_BORDER_MAP.put(NORTH, borderMap(UP, DOWN, EAST, WEST));
        FACE_BORDER_MAP.put(SOUTH, borderMap(UP, DOWN, WEST, EAST));
        FACE_BORDER_MAP.put(WEST, borderMap(UP, DOWN, NORTH, SOUTH));
        FACE_BORDER_MAP.put(EAST, borderMap(UP, DOWN, SOUTH, NORTH));
    }

    protected static final Map<Integer, IIconContainer> RESTR_TEXTURE_MAP = new HashMap<>();

    static {
        RESTR_TEXTURE_MAP.put(TOP.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UP);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DOWN);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UD);
        RESTR_TEXTURE_MAP.put(LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_LEFT);
        RESTR_TEXTURE_MAP.put(TOP.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UL);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DL);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NR);
        RESTR_TEXTURE_MAP.put(RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_RIGHT);
        RESTR_TEXTURE_MAP.put(TOP.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UR);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DR);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NL);
        RESTR_TEXTURE_MAP.put(LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_LR);
        RESTR_TEXTURE_MAP.put(TOP.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_ND);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NU);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR);
    }

    public final float mThickNess;
    public final Materials mMaterial;
    public final int mCapacity, mHeatResistance, mPipeAmount;
    public final boolean mGasProof;
    public final FluidStack[] mFluids;
    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;
    /**
     * Bitmask for whether disable fluid input form each side.
     */
    public byte mDisableInput = 0;

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aID, aName, aNameRegional, 0, false);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
        addInfo(aID);
    }

    @Deprecated
    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof) {
        this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mMaterial == null ? 4 : (byte) (4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Fluid(
            mName,
            mThickNess,
            mMaterial,
            mCapacity,
            mHeatResistance,
            mGasProof,
            mPipeAmount);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean redstoneLevel) {
        if (side == ForgeDirection.UNKNOWN) return Textures.BlockIcons.ERROR_RENDERING;
        final float tThickNess = getThickNess();
        if (mDisableInput == 0)
            return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, colorIndex)
                : TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                    Dyes.getModulation(colorIndex, mMaterial.mRGBa)) };
        int borderMask = 0;
        for (Border border : Border.values()) {
            if (isInputDisabledAtSide(getSideAtBorder(side, border))) borderMask |= border.mask;
        }

        return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, colorIndex)
            : TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(colorIndex, mMaterial.mRGBa)),
            getRestrictorTexture(borderMask) };
    }

    protected static ITexture getBaseTexture(float aThickNess, int aPipeAmount, Materials aMaterial, int colorIndex) {
        if (aPipeAmount >= 9) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aPipeAmount >= 4) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.124F) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.374F) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.499F) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.749F) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.874F) return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
        return TextureFactory.of(
            aMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
            Dyes.getModulation(colorIndex, aMaterial.mRGBa));
    }

    @Deprecated
    protected static ITexture getRestrictorTexture(byte borderMask) {
        return getRestrictorTexture((int) borderMask);
    }

    protected static ITexture getRestrictorTexture(int borderMask) {
        final IIconContainer restrictorIcon = RESTR_TEXTURE_MAP.get(borderMask);
        return restrictorIcon != null ? TextureFactory.of(restrictorIcon) : null;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mDisableInput = aValue;
    }

    @Override
    public byte getUpdateData() {
        return mDisableInput;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return getFluidAmount();
    }

    @Override
    public int maxProgresstime() {
        return getCapacity();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < mPipeAmount; i++) if (mFluids[i] != null)
            aNBT.setTag("mFluid" + (i == 0 ? "" : i), mFluids[i].writeToNBT(new NBTTagCompound()));
        aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
        if (GT_Mod.gregtechproxy.gt6Pipe) {
            aNBT.setByte("mConnections", mConnections);
            aNBT.setByte("mDisableInput", mDisableInput);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < mPipeAmount; i++)
            mFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid" + (i == 0 ? "" : i)));
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
        if (GT_Mod.gregtechproxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
            mDisableInput = aNBT.getByte("mDisableInput");
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
        if ((((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0
            && aEntity instanceof EntityLivingBase) {
            for (FluidStack tFluid : mFluids) {
                if (tFluid != null) {
                    final int tTemperature = tFluid.getFluid()
                        .getTemperature(tFluid);
                    if (tTemperature > 320
                        && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                        GT_Utility.applyHeatDamage((EntityLivingBase) aEntity, (tTemperature - 300) / 50.0F);
                        break;
                    } else if (tTemperature < 260
                        && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                            GT_Utility.applyFrostDamage((EntityLivingBase) aEntity, (270 - tTemperature) / 25.0F);
                            break;
                        }
                }
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 5 == 0) {
            mLastReceivedFrom &= 63;
            if (mLastReceivedFrom == 63) {
                mLastReceivedFrom = 0;
            }

            if (!GT_Mod.gregtechproxy.gt6Pipe || mCheckConnections) checkConnections();

            final boolean shouldDistribute = (oLastReceivedFrom == mLastReceivedFrom);
            for (int i = 0, j = aBaseMetaTileEntity.getRandomNumber(mPipeAmount); i < mPipeAmount; i++) {
                final int index = (i + j) % mPipeAmount;
                if (mFluids[index] != null && mFluids[index].amount <= 0) mFluids[index] = null;
                if (mFluids[index] == null) continue;

                if (checkEnvironment(index, aBaseMetaTileEntity)) return;

                if (shouldDistribute) {
                    distributeFluid(index, aBaseMetaTileEntity);
                    mLastReceivedFrom = 0;
                }
            }

            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    private boolean checkEnvironment(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        // Check for hot liquids that melt the pipe or gasses that escape and burn/freeze people
        final FluidStack tFluid = mFluids[index];

        if (tFluid != null && tFluid.amount > 0) {
            final int tTemperature = tFluid.getFluid()
                .getTemperature(tFluid);
            if (tTemperature > mHeatResistance) {
                if (aBaseMetaTileEntity.getRandomNumber(100) == 0) {
                    // Poof
                    GT_Log.exp.println(
                        "Set Pipe to Fire due to to low heat resistance at " + aBaseMetaTileEntity.getXCoord()
                            + " | "
                            + aBaseMetaTileEntity.getYCoord()
                            + " | "
                            + aBaseMetaTileEntity.getZCoord()
                            + " DIMID: "
                            + aBaseMetaTileEntity.getWorld().provider.dimensionId);
                    aBaseMetaTileEntity.setToFire();
                    return true;
                }
                // Mmhmm, Fire
                aBaseMetaTileEntity.setOnFire();
                GT_Log.exp.println(
                    "Set Blocks around Pipe to Fire due to to low heat resistance at " + aBaseMetaTileEntity.getXCoord()
                        + " | "
                        + aBaseMetaTileEntity.getYCoord()
                        + " | "
                        + aBaseMetaTileEntity.getZCoord()
                        + " DIMID: "
                        + aBaseMetaTileEntity.getWorld().provider.dimensionId);
            }
            if (!mGasProof && tFluid.getFluid()
                .isGaseous(tFluid)) {
                tFluid.amount -= 5;
                sendSound((byte) 9);
                if (tTemperature > 320) {
                    try {
                        for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorld()
                            .getEntitiesWithinAABB(
                                EntityLivingBase.class,
                                AxisAlignedBB.getBoundingBox(
                                    getBaseMetaTileEntity().getXCoord() - 2,
                                    getBaseMetaTileEntity().getYCoord() - 2,
                                    getBaseMetaTileEntity().getZCoord() - 2,
                                    getBaseMetaTileEntity().getXCoord() + 3,
                                    getBaseMetaTileEntity().getYCoord() + 3,
                                    getBaseMetaTileEntity().getZCoord() + 3))) {
                            GT_Utility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
                        }
                    } catch (Throwable e) {
                        if (D1) e.printStackTrace(GT_Log.err);
                    }
                } else if (tTemperature < 260) {
                    try {
                        for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorld()
                            .getEntitiesWithinAABB(
                                EntityLivingBase.class,
                                AxisAlignedBB.getBoundingBox(
                                    getBaseMetaTileEntity().getXCoord() - 2,
                                    getBaseMetaTileEntity().getYCoord() - 2,
                                    getBaseMetaTileEntity().getZCoord() - 2,
                                    getBaseMetaTileEntity().getXCoord() + 3,
                                    getBaseMetaTileEntity().getYCoord() + 3,
                                    getBaseMetaTileEntity().getZCoord() + 3))) {
                            GT_Utility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
                        }
                    } catch (Throwable e) {
                        if (D1) e.printStackTrace(GT_Log.err);
                    }
                }
            }
            if (tFluid.amount <= 0) mFluids[index] = null;
        }
        return false;
    }

    private void distributeFluid(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        final FluidStack tFluid = mFluids[index];
        if (tFluid == null) return;

        // Tank, From, Amount to receive
        final List<MutableTriple<IFluidHandler, ForgeDirection, Integer>> tTanks = new ArrayList<>();
        final int amount = tFluid.amount;
        final byte tOffset = (byte) getBaseMetaTileEntity().getRandomNumber(6);
        for (final byte i : ALL_VALID_SIDES) {
            // Get a list of tanks accepting fluids, and what side they're on
            final ForgeDirection side = ForgeDirection.getOrientation((i + tOffset) % 6);
            final ForgeDirection oppositeSide = side.getOpposite();
            final IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(side);
            final IGregTechTileEntity gTank = tTank instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTank : null;

            if (isConnectedAtSide(side) && tTank != null
                && (mLastReceivedFrom & side.flag) == 0
                && getBaseMetaTileEntity().getCoverInfoAtSide(side)
                    .letsFluidOut(tFluid.getFluid())
                && (gTank == null || gTank.getCoverInfoAtSide(oppositeSide)
                    .letsFluidIn(tFluid.getFluid()))) {
                if (tTank.fill(oppositeSide, tFluid, false) > 0) {
                    tTanks.add(new MutableTriple<>(tTank, oppositeSide, 0));
                }
                tFluid.amount = amount; // Because some mods do actually modify input fluid stack
            }
        }

        // How much of this fluid is available for distribution?
        final double tAmount = Math.max(1, Math.min(mCapacity * 10, tFluid.amount));

        final FluidStack maxFluid = tFluid.copy();
        maxFluid.amount = Integer.MAX_VALUE;

        double availableCapacity = 0;
        // Calculate available capacity for distribution from all tanks
        for (final MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry : tTanks) {
            tEntry.right = tEntry.left.fill(tEntry.middle, maxFluid, false);
            availableCapacity += tEntry.right;
        }

        // Now distribute
        for (final MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry : tTanks) {
            // Distribue fluids based on percentage available space at destination
            if (availableCapacity > tAmount)
                tEntry.right = (int) Math.floor(tEntry.right * tAmount / availableCapacity);

            // If the percent is not enough to give at least 1L, try to give 1L
            if (tEntry.right == 0) tEntry.right = (int) Math.min(1, tAmount);

            if (tEntry.right <= 0) continue;

            final int tFilledAmount = tEntry.left
                .fill(tEntry.middle, drainFromIndex(tEntry.right, false, index), false);

            if (tFilledAmount > 0) tEntry.left.fill(tEntry.middle, drainFromIndex(tFilledAmount, true, index), true);

            if (mFluids[index] == null || mFluids[index].amount <= 0) return;
        }
    }

    public void connectPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer) {
        if (!isConnectedAtSide(side)) {
            if (connect(side) > 0) GT_Utility.sendChatToPlayer(entityPlayer, GT_Utility.trans("214", "Connected"));
        } else {
            disconnect(side);
            GT_Utility.sendChatToPlayer(entityPlayer, GT_Utility.trans("215", "Disconnected"));
        }
    }

    public void blockPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer, byte tMask) {
        if (isInputDisabledAtSide(side)) {
            mDisableInput &= ~tMask;
            GT_Utility.sendChatToPlayer(entityPlayer, GT_Utility.trans("212", "Input enabled"));
            if (!isConnectedAtSide(side)) connect(side);
        } else {
            mDisableInput |= tMask;
            GT_Utility.sendChatToPlayer(entityPlayer, GT_Utility.trans("213", "Input disabled"));
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ) {

        if (GT_Mod.gregtechproxy.gt6Pipe) {
            IGregTechTileEntity currentPipeBase = getBaseMetaTileEntity();
            GT_MetaPipeEntity_Fluid currentPipe = (GT_MetaPipeEntity_Fluid) currentPipeBase.getMetaTileEntity();
            final ForgeDirection tSide = GT_Utility.determineWrenchingSide(side, aX, aY, aZ);
            final byte tMask = (byte) (tSide.flag);

            /*
             * The difference between action with and without ctrl is that with ctrl it will first check if connection
             * is possible
             * with ctrl it won`t connect to empty space for example
             */

            if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                if (entityPlayer.isSneaking()) {
                    currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                } else currentPipe.connectPipeOnSide(tSide, entityPlayer);
                return true;
            }

            boolean initialState = entityPlayer.isSneaking() ? currentPipe.isInputDisabledAtSide(tSide)
                : currentPipe.isConnectedAtSide(tSide);

            boolean wasActionPerformed = false;

            while (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {

                TileEntity nextPipeBaseTile = currentPipeBase.getTileEntityAtSide(tSide);

                // if next tile doesn't exist
                if (nextPipeBaseTile == null) {
                    return wasActionPerformed;
                }

                // if next tile is wrong color
                if (!currentPipe.connectableColor(nextPipeBaseTile)) {
                    return wasActionPerformed;
                }

                IGregTechTileEntity nextPipeBase = (IGregTechTileEntity) nextPipeBaseTile;

                GT_MetaPipeEntity_Fluid nextPipe = nextPipeBase.getMetaTileEntity() instanceof GT_MetaPipeEntity_Fluid
                    ? (GT_MetaPipeEntity_Fluid) nextPipeBase.getMetaTileEntity()
                    : null;

                // if next tile entity is not a pipe
                if (nextPipe == null) {
                    return wasActionPerformed;
                }



                // making sure next pipe has same fluid
                for (int i = 0; i < mPipeAmount; i++) {
                    if (mFluids[i] != null && nextPipe.mFluids[i] != null) {
                        if (!mFluids[i].isFluidEqual(nextPipe.mFluids[i])) {
                            return wasActionPerformed;
                        }
                    } else if (mFluids[i] != nextPipe.mFluids[i]) {
                        return wasActionPerformed;
                    }
                }

                boolean currentState = entityPlayer.isSneaking() ? currentPipe.isInputDisabledAtSide(tSide)
                    : currentPipe.isConnectedAtSide(tSide);

                /*
                 * Making sure next pipe will have same action applied to it
                 * e.g. Connecting pipe won`t trigger disconnect if next pipe is already connected
                 */
                if (currentState != initialState) {
                    return wasActionPerformed;
                }

                if (entityPlayer.isSneaking()) {
                    currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                } else currentPipe.connectPipeOnSide(tSide, entityPlayer);

                wasActionPerformed = true;

                currentPipeBase = (IGregTechTileEntity) nextPipeBase;
                currentPipe = nextPipe;

            }
            return wasActionPerformed;
        }
        return false;
    }

    @Override
    public boolean letsIn(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsFluidIn(side, aCoverID, aCoverVariable, null, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsFluidOut(side, aCoverID, aCoverVariable, null, aTileEntity);
    }

    @Override
    public boolean letsIn(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsFluidIn(side, aCoverID, aCoverVariable, null, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsFluidOut(side, aCoverID, aCoverVariable, null, aTileEntity);
    }

    @Override
    public boolean letsIn(CoverInfo coverInfo) {
        return coverInfo.letsFluidIn(null);
    }

    @Override
    public boolean letsOut(CoverInfo coverInfo) {
        return coverInfo.letsFluidOut(null);
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        if (tileEntity == null) return false;

        final ForgeDirection tSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null) return false;

        final GT_CoverBehaviorBase<?> coverBehavior = baseMetaTile.getCoverBehaviorAtSideNew(side);
        final IGregTechTileEntity gTileEntity = (tileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tileEntity
            : null;

        if (coverBehavior instanceof GT_Cover_Drain
            || (TinkerConstruct.isModLoaded() && isTConstructFaucet(tileEntity))) return true;

        final IFluidHandler fTileEntity = (tileEntity instanceof IFluidHandler) ? (IFluidHandler) tileEntity : null;

        if (fTileEntity != null) {
            final FluidTankInfo[] tInfo = fTileEntity.getTankInfo(tSide);
            if (tInfo != null) {
                return tInfo.length > 0 || (Translocator.isModLoaded() && isTranslocator(tileEntity))
                    || gTileEntity != null
                        && gTileEntity.getCoverBehaviorAtSideNew(tSide) instanceof GT_Cover_FluidRegulator;
            }
        }
        return false;
    }

    @Optional.Method(modid = Mods.Names.TINKER_CONSTRUCT)
    private boolean isTConstructFaucet(TileEntity tTileEntity) {
        // Tinker Construct Faucets return a null tank info, so check the class
        return tTileEntity instanceof tconstruct.smeltery.logic.FaucetLogic;
    }

    @Optional.Method(modid = Mods.Names.TRANSLOCATOR)
    private boolean isTranslocator(TileEntity tTileEntity) {
        // Translocators return a TankInfo, but it's of 0 length - so check the class if we see this pattern
        return tTileEntity instanceof codechicken.translocator.TileLiquidTranslocator;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GT_Mod.gregtechproxy.gt6Pipe;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GT_Utility.doSoundAtClient(SoundResource.RANDOM_FIZZ, 5, 1.0F, aX, aY, aZ);

            new ParticleEventBuilder().setIdentifier(ParticleFX.CLOUD)
                .setWorld(getBaseMetaTileEntity().getWorld())
                .<ParticleEventBuilder>times(
                    6,
                    (x, i) -> x
                        .setMotion(
                            ForgeDirection.getOrientation(i).offsetX / 5.0,
                            ForgeDirection.getOrientation(i).offsetY / 5.0,
                            ForgeDirection.getOrientation(i).offsetZ / 5.0)
                        .setPosition(
                            aX - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aY - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aZ - 0.5 + XSTR_INSTANCE.nextFloat())
                        .run());
        }
    }

    @Override
    public final int getCapacity() {
        return mCapacity * 20 * mPipeAmount;
    }

    @Override
    public FluidTankInfo getInfo() {
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) return new FluidTankInfo(tFluid, mCapacity * 20);
        }
        return new FluidTankInfo(null, mCapacity * 20);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[] {};
        ArrayList<FluidTankInfo> tList = new ArrayList<>();
        for (FluidStack tFluid : mFluids) tList.add(new FluidTankInfo(tFluid, mCapacity * 20));
        return tList.toArray(new FluidTankInfo[mPipeAmount]);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final FluidStack getFluid() {
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) return tFluid;
        }
        return null;
    }

    @Override
    public final int getFluidAmount() {
        int rAmount = 0;
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) rAmount += tFluid.amount;
        }
        return rAmount;
    }

    @Override
    public final int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0) return 0;

        int index = -1;
        for (int i = 0; i < mPipeAmount; i++) {
            if (mFluids[i] != null && mFluids[i].isFluidEqual(aFluid)) {
                index = i;
                break;
            } else if ((mFluids[i] == null || mFluids[i].getFluid()
                .getID() <= 0) && index < 0) {
                    index = i;
                }
        }

        return fill_default_intoIndex(side, aFluid, doFill, index);
    }

    private int fill_default_intoIndex(ForgeDirection side, FluidStack aFluid, boolean doFill, int index) {
        if (index < 0 || index >= mPipeAmount) return 0;
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0) return 0;

        final int ordinalSide = side.ordinal();

        if (mFluids[index] == null || mFluids[index].getFluid()
            .getID() <= 0) {
            if (aFluid.amount * mPipeAmount <= getCapacity()) {
                if (doFill) {
                    mFluids[index] = aFluid.copy();
                    mLastReceivedFrom |= (1 << ordinalSide);
                }
                return aFluid.amount;
            }
            if (doFill) {
                mFluids[index] = aFluid.copy();
                mLastReceivedFrom |= (1 << ordinalSide);
                mFluids[index].amount = getCapacity() / mPipeAmount;
            }
            return getCapacity() / mPipeAmount;
        }

        if (!mFluids[index].isFluidEqual(aFluid)) return 0;

        final int space = getCapacity() / mPipeAmount - mFluids[index].amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                mFluids[index].amount += aFluid.amount;
                mLastReceivedFrom |= (1 << ordinalSide);
            }
            return aFluid.amount;
        }
        if (doFill) {
            mFluids[index].amount = getCapacity() / mPipeAmount;
            mLastReceivedFrom |= (1 << ordinalSide);
        }
        return space;
    }

    @Override
    public final FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack drained;
        for (int i = 0; i < mPipeAmount; i++) {
            if ((drained = drainFromIndex(maxDrain, doDrain, i)) != null) return drained;
        }
        return null;
    }

    private FluidStack drainFromIndex(int maxDrain, boolean doDrain, int index) {
        if (index < 0 || index >= mPipeAmount) return null;
        if (mFluids[index] == null) return null;
        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
            return null;
        }

        int used = maxDrain;
        if (mFluids[index].amount < used) used = mFluids[index].amount;

        if (doDrain) {
            mFluids[index].amount -= used;
        }

        final FluidStack drained = mFluids[index].copy();
        drained.amount = used;

        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
        }

        return drained;
    }

    @Override
    public int getTankPressure() {
        return getFluidAmount() - (getCapacity() / 2);
    }

    @Override
    public String[] getDescription() {
        List<String> descriptions = new ArrayList<>();
        descriptions.add(
            EnumChatFormatting.BLUE + "Fluid Capacity: %%%"
                + GT_Utility.formatNumbers(mCapacity * 20L)
                + "%%% L/sec"
                + EnumChatFormatting.GRAY);
        descriptions.add(
            EnumChatFormatting.RED + "Heat Limit: %%%"
                + GT_Utility.formatNumbers(mHeatResistance)
                + "%%% K"
                + EnumChatFormatting.GRAY);
        if (!mGasProof) {
            descriptions.add(EnumChatFormatting.DARK_GREEN + "Cannot handle gas" + EnumChatFormatting.GRAY);
        }
        if (mPipeAmount != 1) {
            descriptions.add(EnumChatFormatting.AQUA + "Pipe Amount: %%%" + mPipeAmount + EnumChatFormatting.GRAY);
        }
        return descriptions.toArray(new String[0]);
    }

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) return 0.0625F;
        return mThickNess;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return !isInputDisabledAtSide(side);
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return true;
    }

    public boolean isInputDisabledAtSide(ForgeDirection side) {
        return (mDisableInput & side.flag) != 0;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0)
            return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
        else return getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    private AxisAlignedBB getActualCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        final float tSpace = (1f - mThickNess) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.DOWN) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.UP) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.NORTH) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.SOUTH) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.WEST) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.EAST) != 0) {
            tSide0 = tSide2 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }

        final byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & ForgeDirection.DOWN.flag) != 0) tSide0 = 0f;
        if ((tConn & ForgeDirection.UP.flag) != 0) tSide1 = 1f;
        if ((tConn & ForgeDirection.NORTH.flag) != 0) tSide2 = 0f;
        if ((tConn & ForgeDirection.SOUTH.flag) != 0) tSide3 = 1f;
        if ((tConn & ForgeDirection.WEST.flag) != 0) tSide4 = 0f;
        if ((tConn & ForgeDirection.EAST.flag) != 0) tSide5 = 1f;

        return AxisAlignedBB
            .getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0) {
            final AxisAlignedBB aabb = getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
            if (inputAABB.intersectsWith(aabb)) outputAABB.add(aabb);
        }
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (aFluid == null) return null;
        for (int i = 0; i < mFluids.length; ++i) {
            final FluidStack f = mFluids[i];
            if (f == null || !f.isFluidEqual(aFluid)) continue;
            return drainFromIndex(aFluid.amount, doDrain, i);
        }
        return null;
    }

    private static EnumMap<Border, ForgeDirection> borderMap(ForgeDirection topSide, ForgeDirection bottomSide,
        ForgeDirection leftSide, ForgeDirection rightSide) {
        final EnumMap<Border, ForgeDirection> sideMap = new EnumMap<>(Border.class);
        sideMap.put(TOP, topSide);
        sideMap.put(BOTTOM, bottomSide);
        sideMap.put(LEFT, leftSide);
        sideMap.put(RIGHT, rightSide);
        return sideMap;
    }

    protected static ForgeDirection getSideAtBorder(ForgeDirection side, Border border) {
        return FACE_BORDER_MAP.get(side)
            .get(border);
    }

    protected enum Border {

        TOP(),
        BOTTOM(),
        LEFT(),
        RIGHT();

        public final int mask;

        Border() {
            mask = 1 << this.ordinal();
        }
    }
}
