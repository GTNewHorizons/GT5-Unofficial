package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.Translocator;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.BOTTOM;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.LEFT;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.RIGHT;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.TOP;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.tuple.MutableTriple;

import cpw.mods.fml.common.Optional;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolModes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.config.Other;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverDrain;
import gregtech.common.covers.CoverFluidRegulator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEFluidPipe extends MetaPipeEntity implements ILocalizedMetaPipeEntity {

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
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NR); // not right
        RESTR_TEXTURE_MAP.put(RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_RIGHT);
        RESTR_TEXTURE_MAP.put(TOP.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UR);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DR);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NL); // not left
        RESTR_TEXTURE_MAP.put(LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_LR);
        RESTR_TEXTURE_MAP.put(TOP.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_ND); // not down
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NU); // not up
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
    private String mPrefixKey;
    private String materialKeyOverride;
    private boolean shouldSkipMaterialTooltip = false;

    public MTEFluidPipe(int aID, String aName, String aPrefixKey, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aPrefixKey, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public MTEFluidPipe(int aID, String aName, String aPrefixKey, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aID, aName, 0, false);
        mPrefixKey = aPrefixKey;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
        addInfo(aID);
    }

    public MTEFluidPipe(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance,
        boolean aGasProof, int aFluidTypes) {
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
        return new MTEFluidPipe(mName, mThickNess, mMaterial, mCapacity, mHeatResistance, mGasProof, mPipeAmount);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, int connections, int colorIndex,
        boolean connected, boolean redstoneLevel) {
        List<ITexture> textures = new ArrayList<>();

        textures.add(getBaseTexture(connected, colorIndex));

        if (mDisableInput != 0) {
            int borderMask = 0;

            for (Border border : Border.values()) {
                if (isInputDisabledAtSide(getSideAtBorder(side, border))) borderMask |= border.mask;
            }

            textures.add(getRestrictorTexture(borderMask));
        }

        return textures.toArray(new ITexture[0]);
    }

    protected ITexture getBaseTexture(boolean connected, int colorIndex) {
        return getBaseTexture(mThickNess, mPipeAmount, mMaterial.mIconSet, mMaterial.mRGBa, connected, colorIndex);
    }

    protected static ITexture getBaseTexture(float aThickNess, int aPipeAmount, TextureSet textureSet, short[] rgba,
        boolean connected, int colorIndex) {
        IIconContainer texture = textureSet.mTextures[OrePrefixes.pipeHuge.getTextureIndex()];

        if (!connected) {
            texture = textureSet.mTextures[OrePrefixes.pipe.getTextureIndex()];
        } else if (aPipeAmount >= 9) {
            texture = textureSet.mTextures[OrePrefixes.pipeNonuple.getTextureIndex()];
        } else if (aPipeAmount >= 4) {
            texture = textureSet.mTextures[OrePrefixes.pipeQuadruple.getTextureIndex()];
        } else if (aThickNess < 0.124F) {
            texture = textureSet.mTextures[OrePrefixes.pipe.getTextureIndex()];
        } else if (aThickNess < 0.374F) {
            texture = textureSet.mTextures[OrePrefixes.pipeTiny.getTextureIndex()];
        } else if (aThickNess < 0.499F) {
            texture = textureSet.mTextures[OrePrefixes.pipeSmall.getTextureIndex()];
        } else if (aThickNess < 0.749F) {
            texture = textureSet.mTextures[OrePrefixes.pipeMedium.getTextureIndex()];
        } else if (aThickNess < 0.874F) {
            texture = textureSet.mTextures[OrePrefixes.pipeLarge.getTextureIndex()];
        }

        rgba = Dyes.getModulation(colorIndex, rgba);

        return TextureFactory.of(texture, rgba);
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
        if (GTMod.proxy.gt6Pipe) {
            aNBT.setByte("mConnections", mConnections);
            aNBT.setByte("mDisableInput", mDisableInput);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < mPipeAmount; i++)
            mFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid" + (i == 0 ? "" : i)));
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
        if (GTMod.proxy.gt6Pipe) {
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
                        GTUtility.applyHeatDamage((EntityLivingBase) aEntity, (tTemperature - 300) / 50.0F);
                        break;
                    } else if (tTemperature < 260
                        && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                            GTUtility.applyFrostDamage((EntityLivingBase) aEntity, (270 - tTemperature) / 25.0F);
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

            if (!GTMod.proxy.gt6Pipe || mCheckConnections) checkConnections();

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
                    GTLog.exp.println(
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
                GTLog.exp.println(
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
                        GTUtility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
                    }
                } else if (tTemperature < 260) {
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
                        GTUtility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
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
                && getBaseMetaTileEntity().getCoverAtSide(side)
                    .letsFluidOut(tFluid.getFluid())
                && (gTank == null || gTank.getCoverAtSide(oppositeSide)
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
            if (connect(side) > 0) GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("214", "Connected"));
        } else {
            disconnect(side);
            GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("215", "Disconnected"));
        }
    }

    public void blockPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer, byte mask) {
        if (isInputDisabledAtSide(side)) {
            mDisableInput &= ~mask;
            GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("212", "Input enabled"));
        } else {
            mDisableInput |= mask;
            GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("213", "Input disabled"));
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Only trigger if the player is sneaking
        if (!aPlayer.isSneaking()) {
            return;
        }

        // Retrieve the item's MetaTileEntity
        final ItemStack handItem = aPlayer.inventory.getCurrentItem();
        if (handItem == null) return;

        IMetaTileEntity meta = ItemMachines.getMetaTileEntity(handItem);
        if (!(meta instanceof MTEFluidPipe handFluid)) return;

        // Preserve old connections and meta ID
        byte oldConnections = this.mConnections;
        short oldMetaID = (short) aBaseMetaTileEntity.getMetaTileID();

        // Create the new fluid pipe
        MTEFluidPipe newPipe = (MTEFluidPipe) handFluid.newMetaEntity(aBaseMetaTileEntity);
        if (newPipe == null) return;

        // Preserve old connections
        newPipe.mConnections = oldConnections;
        newPipe.mDisableInput = this.mDisableInput;

        // Record old pipe parameters
        long oldCapacity = this.mCapacity;
        boolean oldGasProof = this.mGasProof;
        int oldHeatResistance = this.mHeatResistance;

        // Add fluid to the new pipe
        if (this.mPipeAmount <= newPipe.mPipeAmount) {
            for (int i = 0; i < mPipeAmount; i++) {
                if (this.mFluids[i] != null) {
                    newPipe.mFluids[i] = this.mFluids[i].copy();
                    newPipe.mFluids[i].amount = Math.min(this.mFluids[i].amount, newPipe.mCapacity);
                }
            }
        }

        // Update to the new pipe
        aBaseMetaTileEntity.setMetaTileID((short) handItem.getItemDamage());
        newPipe.setBaseMetaTileEntity(aBaseMetaTileEntity);

        // Construct a change message if needed
        StringBuilder message = new StringBuilder();

        // Compare capacity changes
        if (oldCapacity != newPipe.mCapacity) {
            message.append(oldCapacity * 20)
                .append("L/seconds → ");
            message.append(newPipe.mCapacity > oldCapacity ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                .append(newPipe.mCapacity * 20)
                .append("L/secs")
                .append(EnumChatFormatting.RESET);
        }

        // Compare heat resistance
        if (oldHeatResistance != newPipe.mHeatResistance) {
            if (message.length() > 0) message.append(" | ");
            message.append(oldHeatResistance)
                .append("K → ");
            message
                .append(newPipe.mHeatResistance > oldHeatResistance ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                .append(newPipe.mHeatResistance)
                .append("K")
                .append(EnumChatFormatting.RESET);
        }

        // Compare gas handling
        if (oldGasProof != newPipe.mGasProof) {
            if (message.length() > 0) message.append(" | ");
            if (newPipe.mGasProof) {
                message.append(EnumChatFormatting.GREEN)
                    .append("Now Gas-Proof");
            } else {
                message.append(EnumChatFormatting.RED)
                    .append("No Longer Gas-Proof");
            }
            message.append(EnumChatFormatting.RESET);
        }

        // Send a chat message if anything changed
        if (message.length() > 0) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.item.pipe.swap") + " " + message.toString());
        }

        // Force updates to sync changes
        aBaseMetaTileEntity.markDirty();
        aBaseMetaTileEntity.issueTextureUpdate();
        aBaseMetaTileEntity.issueBlockUpdate();
        aBaseMetaTileEntity.issueClientUpdate();

        // Handle inventory operations unless in creative mode
        if (!aPlayer.capabilities.isCreativeMode) {
            ItemStack oldPipe = new ItemStack(handItem.getItem(), 1, oldMetaID);
            boolean addedToInventory = false;

            // Attempt to stack with existing items
            if (oldPipe != null) {
                for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
                    ItemStack slot = aPlayer.inventory.mainInventory[i];
                    if (slot != null && slot.getItem() == oldPipe.getItem()
                        && slot.getItemDamage() == oldPipe.getItemDamage()
                        && slot.stackSize < slot.getMaxStackSize()) {
                        slot.stackSize++;
                        addedToInventory = true;
                        break;
                    }
                }
                // Add new stack if stacking failed
                if (!addedToInventory) {
                    addedToInventory = aPlayer.inventory.addItemStackToInventory(oldPipe);
                }
                // If still unsuccessful, drop the item
                if (!addedToInventory) {
                    aPlayer.dropPlayerItemWithRandomChoice(oldPipe, false);
                }
            }

            // Decrement the placed pipe from the player's hand
            handItem.stackSize--;
            if (handItem.stackSize <= 0) {
                aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (GTMod.proxy.gt6Pipe) {
            final int mode = MetaGeneratedTool.getToolMode(aTool);
            IGregTechTileEntity currentPipeBase = getBaseMetaTileEntity();
            MTEFluidPipe currentPipe = (MTEFluidPipe) currentPipeBase.getMetaTileEntity();
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
            final byte tMask = (byte) (tSide.flag);

            if (mode == ToolModes.REGULAR.get()) {
                if (entityPlayer.isSneaking()) {
                    currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                } else currentPipe.connectPipeOnSide(tSide, entityPlayer);
                return true;
            }

            if (mode == ToolModes.WRENCH_LINE.get()) {

                boolean initialState = entityPlayer.isSneaking() ? currentPipe.isInputDisabledAtSide(tSide)
                    : currentPipe.isConnectedAtSide(tSide);

                boolean wasActionPerformed = false;

                int limit = Other.pipeWrenchingChainRange;
                for (int connected = 0; connected < limit; connected++) {

                    TileEntity nextPipeBaseTile = currentPipeBase.getTileEntityAtSide(tSide);

                    // if next tile doesn't exist or if next tile is not GT tile
                    if (!(nextPipeBaseTile instanceof IGregTechTileEntity nextPipeBase)) {
                        return wasActionPerformed;
                    }

                    // if next tile is wrong color
                    if (!currentPipe.connectableColor(nextPipeBaseTile)) {
                        return wasActionPerformed;
                    }

                    MTEFluidPipe nextPipe = nextPipeBase.getMetaTileEntity() instanceof MTEFluidPipe
                        ? (MTEFluidPipe) nextPipeBase.getMetaTileEntity()
                        : null;

                    // if next tile entity is not a pipe
                    if (nextPipe == null) {
                        return wasActionPerformed;
                    }

                    // if pipes are same size
                    if (mPipeAmount != nextPipe.mPipeAmount) {
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
                     * Making sure next pipe will have same action applied to it e.g. Connecting pipe won`t trigger
                     * disconnect if next pipe is already connected
                     */
                    if (currentState != initialState) {
                        return wasActionPerformed;
                    }

                    if (entityPlayer.isSneaking()) {
                        currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                    } else currentPipe.connectPipeOnSide(tSide, entityPlayer);

                    wasActionPerformed = true;

                    currentPipeBase = nextPipeBase;
                    currentPipe = nextPipe;

                }
                return wasActionPerformed;
            }
        }
        return false;
    }

    @Override
    public boolean letsIn(Cover cover) {
        return cover.letsFluidIn(null);
    }

    @Override
    public boolean letsOut(Cover cover) {
        return cover.letsFluidOut(null);
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        if (tileEntity == null) return false;

        final ForgeDirection oppositeSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null) return false;

        final Cover cover = baseMetaTile.getCoverAtSide(side);
        final IGregTechTileEntity gTileEntity = (tileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tileEntity
            : null;

        if (cover instanceof CoverDrain || (TinkerConstruct.isModLoaded() && isTConstructFaucet(tileEntity)))
            return true;

        final IFluidHandler fTileEntity = (tileEntity instanceof IFluidHandler) ? (IFluidHandler) tileEntity : null;

        if (fTileEntity != null) {
            final FluidTankInfo[] tInfo = fTileEntity.getTankInfo(oppositeSide);
            if (tInfo != null) {
                return tInfo.length > 0 || (Translocator.isModLoaded() && isTranslocator(tileEntity))
                    || gTileEntity != null && gTileEntity.getCoverAtSide(oppositeSide) instanceof CoverFluidRegulator;
            }
        }
        return false;
    }

    @Optional.Method(modid = Mods.ModIDs.TINKER_CONSTRUCT)
    private boolean isTConstructFaucet(TileEntity tTileEntity) {
        // Tinker Construct Faucets return a null tank info, so check the class
        return tTileEntity instanceof tconstruct.smeltery.logic.FaucetLogic;
    }

    @Optional.Method(modid = Mods.ModIDs.TRANSLOCATOR)
    private boolean isTranslocator(TileEntity tTileEntity) {
        // Translocators return a TankInfo, but it's of 0 length - so check the class if we see this pattern
        return tTileEntity instanceof codechicken.translocator.TileLiquidTranslocator;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.proxy.gt6Pipe;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GTUtility.doSoundAtClient(SoundResource.RANDOM_FIZZ, 5, 1.0F, aX, aY, aZ);

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
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().isSteampowered()) return GTValues.emptyFluidTankInfo;
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
    public String[] getDescription() {
        List<String> descriptions = new ArrayList<>();
        descriptions.add(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.fluidpipe.capacity.desc", formatNumber(mCapacity * 20L)));
        descriptions.add(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.fluidpipe.heat.desc", formatNumber(mHeatResistance)));
        if (!mGasProof) {
            descriptions.add(StatCollector.translateToLocal("gt.blockmachines.fluidpipe.no_gas_proof.desc"));
        }
        if (mPipeAmount != 1) {
            descriptions.add(
                StatCollector.translateToLocalFormatted("gt.blockmachines.fluidpipe.pipe_amount.desc", mPipeAmount));
        }
        return descriptions.toArray(new String[0]);
    }

    @Override
    public float getCollisionThickness() {
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
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (aFluid == null) return null;
        for (int i = 0; i < mFluids.length; ++i) {
            final FluidStack f = mFluids[i];
            if (f == null || !f.isFluidEqual(aFluid)) continue;
            return drainFromIndex(aFluid.amount, doDrain, i);
        }
        return null;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        // Basic pipe stats
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.pipe.capacity") + ": "
                + EnumChatFormatting.BLUE
                + formatNumber(mCapacity * 20L)
                + " L/s");

        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.pipe.heat_resistance") + ": "
                + EnumChatFormatting.RED
                + formatNumber(mHeatResistance)
                + "K");

        // Gas handling info
        if (mGasProof) {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.gas_proof") + ": "
                    + EnumChatFormatting.GREEN
                    + StatCollector.translateToLocal("GT5U.item.pipe.gas_proof.yes"));
        } else {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.gas_proof") + ": "
                    + EnumChatFormatting.RED
                    + StatCollector.translateToLocal("GT5U.item.pipe.gas_proof.no"));
        }

        // Multi-pipe info
        if (mPipeAmount > 1) {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.amount") + ": " + EnumChatFormatting.AQUA + mPipeAmount);
        }
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

    @Override
    public IOreMaterial getMaterial() {
        return mMaterial;
    }

    @Override
    public String getPrefixKey() {
        return mPrefixKey;
    }

    @Override
    public String getMaterialKeyOverride() {
        return materialKeyOverride;
    }

    @Override
    public boolean shouldSkipMaterialTooltip() {
        return shouldSkipMaterialTooltip;
    }

    public MTEFluidPipe renameMaterial(String newName) {
        if (newName == null) return this;
        final String key = mMaterial.getLocalizedNameKey() + ".fluidpipe.newname";
        GTLanguageManager.addStringLocalization(key, newName);
        this.materialKeyOverride = key;
        return this;
    }

    public MTEFluidPipe setShouldSkipMaterialTooltip(boolean shouldSkipMaterialTooltip) {
        this.shouldSkipMaterialTooltip = shouldSkipMaterialTooltip;
        return this;
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
