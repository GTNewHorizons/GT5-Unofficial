package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

import cofh.api.energy.IEnergyReceiver;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.graphs.Node;
import gregtech.api.graphs.NodeList;
import gregtech.api.graphs.PowerNode;
import gregtech.api.graphs.PowerNodes;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_Cover_None;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_GC_Compat;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.GT_Cover_SolarPanel;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorChamber;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaPipeEntity_Cable extends MetaPipeEntity implements IMetaTileEntityCable {

    public final float mThickNess;
    public final Materials mMaterial;
    public final long mCableLossPerMeter, mAmperage, mVoltage;
    public final boolean mInsulated, mCanShock;

    public int mTransferredAmperage = 0;
    public long mTransferredVoltage = 0;

    @Deprecated
    public int mTransferredAmperageLast20 = 0, mTransferredAmperageLast20OK = 0, mTransferredAmperageOK = 0;
    @Deprecated
    public long mTransferredVoltageLast20 = 0, mTransferredVoltageLast20OK = 0, mTransferredVoltageOK = 0;

    public long mRestRF;
    public int mOverheat;
    public static short mMaxOverheat = (short) (GT_Mod.gregtechproxy.mWireHeatingTicks * 100);

    private long lastWorldTick;

    public GT_MetaPipeEntity_Cable(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
        long aCableLossPerMeter, long aAmperage, long aVoltage, boolean aInsulated, boolean aCanShock) {
        super(aID, aName, aNameRegional, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mAmperage = aAmperage;
        mVoltage = aVoltage;
        mInsulated = aInsulated;
        mCanShock = aCanShock;
        mCableLossPerMeter = 0;
    }

    public GT_MetaPipeEntity_Cable(String aName, float aThickNess, Materials aMaterial, long aCableLossPerMeter,
        long aAmperage, long aVoltage, boolean aInsulated, boolean aCanShock) {
        super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mAmperage = aAmperage;
        mVoltage = aVoltage;
        mInsulated = aInsulated;
        mCanShock = aCanShock;
        mCableLossPerMeter = 0;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mInsulated ? 9 : 8);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Cable(
            mName,
            mThickNess,
            mMaterial,
            mCableLossPerMeter,
            mAmperage,
            mVoltage,
            mInsulated,
            mCanShock);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        int facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (!mInsulated) return new ITexture[] { TextureFactory
            .of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], Dyes.getModulation(colorIndex, mMaterial.mRGBa)) };
        if (active) {
            float tThickNess = getThickNess();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory
                .of(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375 x1
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_TINY,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500 x2
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_SMALL,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.624F) // 0.625 x4
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750 x8
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM_PLUS,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825 x12
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_LARGE,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            return new ITexture[] {
                TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                TextureFactory.of(
                    Textures.BlockIcons.INSULATION_HUGE,
                    Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
        }
        return new ITexture[] { TextureFactory
            .of(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {

        if (!mCanShock) return;

        final BaseMetaPipeEntity baseEntity = (BaseMetaPipeEntity) getBaseMetaTileEntity();

        if (!(aEntity instanceof EntityLivingBase livingEntity)) return;
        if (!(baseEntity.getNodePath() instanceof PowerNodePath powerPath)) return;

        if (isCoverOnSide(baseEntity, livingEntity)) return;
        if ((baseEntity.mConnections & IConnectable.HAS_HARDENEDFOAM) == 1) return;

        final long amperage = powerPath.getAmperage();
        final long voltage = powerPath.getVoltage();

        if (amperage == 0L) return;

        GT_Utility.applyElectricityDamage(livingEntity, voltage, amperage);
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
        return true;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return (int) mTransferredAmperage * 64;
    }

    @Override
    public int maxProgresstime() {
        return (int) mAmperage * 64;
    }

    @Override
    public long injectEnergyUnits(ForgeDirection side, long voltage, long amperage) {
        if (!isConnectedAtSide(side) && side != ForgeDirection.UNKNOWN) return 0;
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .letsEnergyIn()) return 0;
        return transferElectricity(side, voltage, amperage, (HashSet<TileEntity>) null);
    }

    @Override
    @Deprecated
    public long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage,
        ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
        return transferElectricity(side, aVoltage, aAmperage, new HashSet<>(aAlreadyPassedTileEntityList));
    }

    @Override
    public long transferElectricity(ForgeDirection side, long voltage, long amperage,
        HashSet<TileEntity> alreadyPassedSet) {
        if (!getBaseMetaTileEntity().isServerSide() || !isConnectedAtSide(side) && side != ForgeDirection.UNKNOWN)
            return 0;
        final BaseMetaPipeEntity tBase = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (!(tBase.getNode() instanceof PowerNode tNode)) return 0;
        int tPlace = 0;
        final Node[] tToPower = new Node[tNode.mConsumers.size()];
        if (tNode.mHadVoltage) {
            for (ConsumerNode consumer : tNode.mConsumers) {
                if (consumer.needsEnergy()) tToPower[tPlace++] = consumer;
            }
        } else {
            tNode.mHadVoltage = true;
            for (ConsumerNode consumer : tNode.mConsumers) {
                tToPower[tPlace++] = consumer;
            }
        }
        return PowerNodes.powerNode(tNode, null, new NodeList(tToPower), (int) voltage, (int) amperage);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            lastWorldTick = aBaseMetaTileEntity.getWorld()
                .getTotalWorldTime() - 1;
            // sets initial value -1 since it is
            // in the same tick as first on post
            // tick
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0 && aBaseMetaTileEntity.isServerSide()
            && (!GT_Mod.gregtechproxy.gt6Cable || mCheckConnections)) {
            checkConnections();
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (GT_Mod.gregtechproxy.gt6Cable
            && GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(wrenchingSide)) {
                disconnect(wrenchingSide);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("215", "Disconnected"));
            } else if (!GT_Mod.gregtechproxy.costlyCableConnection) {
                if (connect(wrenchingSide) > 0)
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (GT_Mod.gregtechproxy.gt6Cable
            && GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(wrenchingSide)) {
                disconnect(wrenchingSide);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("215", "Disconnected"));
            } else if (!GT_Mod.gregtechproxy.costlyCableConnection || GT_ModHandler.consumeSolderingMaterial(aPlayer)) {
                if (connect(wrenchingSide) > 0)
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsIn(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsEnergyIn(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsEnergyOut(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsIn(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsEnergyIn(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsEnergyOut(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsIn(CoverInfo coverInfo) {
        return coverInfo.letsEnergyIn();
    }

    @Override
    public boolean letsOut(CoverInfo coverInfo) {
        return coverInfo.letsEnergyOut();
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        final GT_CoverBehaviorBase<?> coverBehavior = baseMetaTile.getCoverBehaviorAtSideNew(side);
        final ForgeDirection oppositeSide = side.getOpposite();

        // GT Machine handling
        if ((tileEntity instanceof PowerLogicHost powerLogic && powerLogic.getPowerLogic(oppositeSide) != null)
            || ((tileEntity instanceof IEnergyConnected energyConnected)
                && (energyConnected.inputEnergyFrom(oppositeSide, false)
                    || energyConnected.outputsEnergyTo(oppositeSide, false))))
            return true;

        // Solar Panel Compat
        if (coverBehavior instanceof GT_Cover_SolarPanel) return true;

        // ((tIsGregTechTileEntity && tIsTileEntityCable) && (tAlwaysLookConnected || tLetEnergyIn || tLetEnergyOut) )
        // --> Not needed
        if (GalacticraftCore.isModLoaded() && GT_GC_Compat.canConnect(tileEntity, oppositeSide)) return true;

        // AE2-p2p Compat
        if (tileEntity instanceof appeng.tile.powersink.IC2 ic2sink
            && ic2sink.acceptsEnergyFrom((TileEntity) baseMetaTile, oppositeSide)) return true;

        // IC2 Compat
        {
            final TileEntity ic2Energy;

            if (tileEntity instanceof IReactorChamber)
                ic2Energy = (TileEntity) ((IReactorChamber) tileEntity).getReactor();
            else ic2Energy = (tileEntity == null || tileEntity instanceof IEnergyTile || EnergyNet.instance == null)
                ? tileEntity
                : EnergyNet.instance
                    .getTileEntity(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            // IC2 Sink Compat
            if ((ic2Energy instanceof IEnergySink)
                && ((IEnergySink) ic2Energy).acceptsEnergyFrom((TileEntity) baseMetaTile, oppositeSide)) return true;

            // IC2 Source Compat
            if (GT_Mod.gregtechproxy.ic2EnergySourceCompat && (ic2Energy instanceof IEnergySource)) {
                if (((IEnergySource) ic2Energy).emitsEnergyTo((TileEntity) baseMetaTile, oppositeSide)) {
                    return true;
                }
            }
        }
        // RF Output Compat
        if (GregTech_API.mOutputRF && tileEntity instanceof IEnergyReceiver
            && ((IEnergyReceiver) tileEntity).canConnectEnergy(oppositeSide)) return true;

        // RF Input Compat
        return GregTech_API.mInputRF && (tileEntity instanceof IEnergyEmitter
            && ((IEnergyEmitter) tileEntity).emitsEnergyTo((TileEntity) baseMetaTile, oppositeSide));
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 Cables are enabled
        return GT_Mod.gregtechproxy.gt6Cable;
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
    public String[] getDescription() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.item.cable.max_voltage") + ": %%%"
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mVoltage)
                + " ("
                + GT_Utility.getColoredTierNameFromVoltage(mVoltage)
                + EnumChatFormatting.GREEN
                + ")"
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.item.cable.max_amperage") + ": %%%"
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mAmperage)
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.item.cable.loss") + ": %%%"
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(mCableLossPerMeter)
                + EnumChatFormatting.GRAY
                + "%%% "
                + StatCollector.translateToLocal("GT5U.item.cable.eu_volt") };
    }

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) return 0.0625F;
        return mThickNess;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (GT_Mod.gregtechproxy.gt6Cable) aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (GT_Mod.gregtechproxy.gt6Cable) {
            mConnections = aNBT.getByte("mConnections");
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        final BaseMetaPipeEntity base = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        final PowerNodePath path = (PowerNodePath) base.getNodePath();

        if (path == null)
            return new String[] { EnumChatFormatting.RED + "Failed to get Power Node info" + EnumChatFormatting.RESET };

        final long currAmp = path.getAmperage();
        final long currVoltage = path.getVoltage();

        final double avgAmp = path.getAvgAmperage();
        final double avgVoltage = path.getAvgVoltage();

        final long maxVoltageOut = (mVoltage - mCableLossPerMeter) * mAmperage;

        return new String[] {
            "Heat: " + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(mOverheat)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxOverheat)
                + EnumChatFormatting.RESET,
            "Amperage: " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(currAmp)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mAmperage)
                + EnumChatFormatting.RESET
                + " A",
            "Voltage Out: " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(currVoltage)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxVoltageOut)
                + EnumChatFormatting.RESET
                + " EU/t",
            "Avg Amperage (20t): " + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(avgAmp)
                + EnumChatFormatting.RESET
                + " A",
            "Avg Output (20t): " + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(avgVoltage)
                + EnumChatFormatting.RESET
                + " EU/t" };
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0)
            return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
        else return getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    private AxisAlignedBB getActualCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - mThickNess) / 2;
        float spaceDown = tSpace;
        float spaceUp = 1f - tSpace;
        float spaceNorth = tSpace;
        float spaceSouth = 1f - tSpace;
        float spaceWest = tSpace;
        float spaceEast = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide(DOWN) != 0) {
            spaceDown = spaceNorth = spaceWest = 0;
            spaceSouth = spaceEast = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(UP) != 0) {
            spaceNorth = spaceWest = 0;
            spaceUp = spaceSouth = spaceEast = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(NORTH) != 0) {
            spaceDown = spaceNorth = spaceWest = 0;
            spaceUp = spaceEast = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(SOUTH) != 0) {
            spaceDown = spaceWest = 0;
            spaceUp = spaceSouth = spaceEast = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(WEST) != 0) {
            spaceDown = spaceNorth = spaceWest = 0;
            spaceUp = spaceSouth = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(EAST) != 0) {
            spaceDown = spaceNorth = 0;
            spaceUp = spaceSouth = spaceEast = 1;
        }

        byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & DOWN.flag) != 0) spaceDown = 0f;
        if ((tConn & UP.flag) != 0) spaceUp = 1f;
        if ((tConn & NORTH.flag) != 0) spaceNorth = 0f;
        if ((tConn & SOUTH.flag) != 0) spaceSouth = 1f;
        if ((tConn & WEST.flag) != 0) spaceWest = 0f;
        if ((tConn & EAST.flag) != 0) spaceEast = 1f;

        return AxisAlignedBB.getBoundingBox(
            aX + spaceWest,
            aY + spaceDown,
            aZ + spaceNorth,
            aX + spaceEast,
            aY + spaceUp,
            aZ + spaceSouth);
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
    public boolean shouldJoinIc2Enet() {
        if (!GT_Mod.gregtechproxy.ic2EnergySourceCompat) return false;

        if (mConnections != 0) {
            final IGregTechTileEntity baseMeta = getBaseMetaTileEntity();
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (isConnectedAtSide(side)) {
                    final TileEntity tTileEntity = baseMeta.getTileEntityAtSide(side);
                    final TileEntity tEmitter = (tTileEntity == null || tTileEntity instanceof IEnergyTile
                        || EnergyNet.instance == null)
                            ? tTileEntity
                            : EnergyNet.instance.getTileEntity(
                                tTileEntity.getWorldObj(),
                                tTileEntity.xCoord,
                                tTileEntity.yCoord,
                                tTileEntity.zCoord);

                    if (tEmitter instanceof IEnergyEmitter) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void reloadLocks() {
        final BaseMetaPipeEntity pipe = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (pipe.getNode() != null) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (isConnectedAtSide(side)) {
                    final CoverInfo coverInfo = pipe.getCoverInfoAtSide(side);
                    if (coverInfo.getCoverBehavior() instanceof GT_Cover_None) continue;
                    if (!letsIn(coverInfo) || !letsOut(coverInfo)) {
                        pipe.addToLock(pipe, side);
                    } else {
                        pipe.removeFromLock(pipe, side);
                    }
                }
            }
        } else {
            boolean dontAllow = false;
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (isConnectedAtSide(side)) {
                    final CoverInfo coverInfo = pipe.getCoverInfoAtSide(side);
                    if (coverInfo.getCoverBehavior() instanceof GT_Cover_None) continue;

                    if (!letsIn(coverInfo) || !letsOut(coverInfo)) {
                        dontAllow = true;
                    }
                }
            }
            if (dontAllow) {
                pipe.addToLock(pipe, DOWN);
            } else {
                pipe.removeFromLock(pipe, DOWN);
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.cable.max_voltage") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mVoltage)
                + " ("
                + GT_Utility.getColoredTierNameFromVoltage(mVoltage)
                + EnumChatFormatting.GREEN
                + ")");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.cable.max_amperage") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mAmperage));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.cable.loss") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(mCableLossPerMeter)
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.item.cable.eu_volt"));
    }
}
