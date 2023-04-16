package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.Mods.GalacticraftCore;

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
import gregtech.api.enums.*;
import gregtech.api.graphs.Node;
import gregtech.api.graphs.NodeList;
import gregtech.api.graphs.PowerNode;
import gregtech.api.graphs.PowerNodes;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.ITexture;
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
import gregtech.api.util.*;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;
import gregtech.common.covers.GT_Cover_SolarPanel;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorChamber;

public class GT_MetaPipeEntity_Cable extends MetaPipeEntity implements IMetaTileEntityCable {

    public final float mThickNess;
    public final Materials mMaterial;
    public final long mCableLossPerMeter, mAmperage, mVoltage;
    public final boolean mInsulated, mCanShock;
    public int mTransferredAmperage = 0, mTransferredAmperageLast20 = 0, mTransferredAmperageLast20OK = 0,
        mTransferredAmperageOK = 0;
    public long mTransferredVoltageLast20 = 0, mTransferredVoltage = 0, mTransferredVoltageLast20OK = 0,
        mTransferredVoltageOK = 0;
    public long mRestRF;
    public int mOverheat;
    public static short mMaxOverheat = (short) (GT_Mod.gregtechproxy.mWireHeatingTicks * 100);

    private int[] lastAmperage;
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
        mCableLossPerMeter = aCableLossPerMeter;
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
        mCableLossPerMeter = aCableLossPerMeter;
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
        byte aColorIndex, boolean aConnected, boolean aRedstone) {
        if (!mInsulated) return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
            Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
        if (aConnected) {
            float tThickNess = getThickNess();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                Textures.BlockIcons.INSULATION_FULL,
                Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375 x1
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_TINY,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500 x2
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_SMALL,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.624F) // 0.625 x4
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750 x8
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM_PLUS,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825 x12
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_LARGE,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            return new ITexture[] {
                TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                TextureFactory.of(
                    Textures.BlockIcons.INSULATION_HUGE,
                    Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
        }
        return new ITexture[] { TextureFactory
            .of(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
        if (mCanShock && (((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0
            && aEntity instanceof EntityLivingBase
            && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity))
            GT_Utility.applyElectricityDamage(
                (EntityLivingBase) aEntity,
                mTransferredVoltageLast20,
                mTransferredAmperageLast20);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public final boolean renderInside(byte aSide) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return mTransferredAmperage * 64;
    }

    @Override
    public int maxProgresstime() {
        return (int) mAmperage * 64;
    }

    @Override
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!isConnectedAtSide(aSide) && aSide != 6) return 0;
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(aSide)
            .letsEnergyIn()) return 0;
        final HashSet<TileEntity> nul = null;
        return transferElectricity(aSide, aVoltage, aAmperage, nul);
    }

    @Override
    @Deprecated
    public long transferElectricity(byte aSide, long aVoltage, long aAmperage,
        ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
        return transferElectricity(aSide, aVoltage, aAmperage, new HashSet<>(aAlreadyPassedTileEntityList));
    }

    @Override
    public long transferElectricity(byte aSide, long aVoltage, long aAmperage, HashSet<TileEntity> aAlreadyPassedSet) {
        if (!getBaseMetaTileEntity().isServerSide() || !isConnectedAtSide(aSide) && aSide != 6) return 0;
        final BaseMetaPipeEntity tBase = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (!(tBase.getNode() instanceof PowerNode tNode)) return 0;
        if (tNode != null) {
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
            return PowerNodes.powerNode(tNode, null, new NodeList(tToPower), (int) aVoltage, (int) aAmperage);
        }
        return 0;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            lastAmperage = new int[16];
            lastWorldTick = aBaseMetaTileEntity.getWorld()
                .getTotalWorldTime() - 1; // sets initial value -1 since it is
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
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        if (GT_Mod.gregtechproxy.gt6Cable
            && GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(aWrenchingSide)) {
                disconnect(aWrenchingSide);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("215", "Disconnected"));
            } else if (!GT_Mod.gregtechproxy.costlyCableConnection) {
                if (connect(aWrenchingSide) > 0)
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        if (GT_Mod.gregtechproxy.gt6Cable
            && GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(aWrenchingSide)) {
                disconnect(aWrenchingSide);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("215", "Disconnected"));
            } else if (!GT_Mod.gregtechproxy.costlyCableConnection || GT_ModHandler.consumeSolderingMaterial(aPlayer)) {
                if (connect(aWrenchingSide) > 0)
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsIn(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsEnergyIn(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return coverBehavior.letsEnergyOut(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsIn(GT_CoverBehaviorBase<?> coverBehavior, byte aSide, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsEnergyIn(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehaviorBase<?> coverBehavior, byte aSide, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsEnergyOut(aSide, aCoverID, aCoverVariable, aTileEntity);
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
    public boolean canConnect(byte aSide, TileEntity tTileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        final GT_CoverBehaviorBase<?> coverBehavior = baseMetaTile.getCoverBehaviorAtSideNew(aSide);
        final byte tSide = GT_Utility.getOppositeSide(aSide);
        final ForgeDirection tDir = ForgeDirection.getOrientation(tSide);

        // GT Machine handling
        if ((tTileEntity instanceof PowerLogicHost
            && ((PowerLogicHost) tTileEntity).getPowerLogic(ForgeDirection.getOrientation(aSide)) != null)
            || ((tTileEntity instanceof IEnergyConnected)
                && (((IEnergyConnected) tTileEntity).inputEnergyFrom(tSide, false)
                    || ((IEnergyConnected) tTileEntity).outputsEnergyTo(tSide, false))))
            return true;

        // Solar Panel Compat
        if (coverBehavior instanceof GT_Cover_SolarPanel) return true;

        // ((tIsGregTechTileEntity && tIsTileEntityCable) && (tAlwaysLookConnected || tLetEnergyIn || tLetEnergyOut) )
        // --> Not needed
        if (GalacticraftCore.isModLoaded() && GT_GC_Compat.canConnect(tTileEntity, tDir)) return true;

        // AE2-p2p Compat
        if (GT_Mod.gregtechproxy.mAE2Integration) {
            if (tTileEntity instanceof appeng.tile.powersink.IC2
                && ((appeng.tile.powersink.IC2) tTileEntity).acceptsEnergyFrom((TileEntity) baseMetaTile, tDir))
                return true;
        }

        // IC2 Compat
        {
            final TileEntity ic2Energy;

            if (tTileEntity instanceof IReactorChamber)
                ic2Energy = (TileEntity) ((IReactorChamber) tTileEntity).getReactor();
            else ic2Energy = (tTileEntity == null || tTileEntity instanceof IEnergyTile || EnergyNet.instance == null)
                ? tTileEntity
                : EnergyNet.instance.getTileEntity(
                    tTileEntity.getWorldObj(),
                    tTileEntity.xCoord,
                    tTileEntity.yCoord,
                    tTileEntity.zCoord);

            // IC2 Sink Compat
            if ((ic2Energy instanceof IEnergySink)
                && ((IEnergySink) ic2Energy).acceptsEnergyFrom((TileEntity) baseMetaTile, tDir)) return true;

            // IC2 Source Compat
            if (GT_Mod.gregtechproxy.ic2EnergySourceCompat && (ic2Energy instanceof IEnergySource)) {
                if (((IEnergySource) ic2Energy).emitsEnergyTo((TileEntity) baseMetaTile, tDir)) {
                    return true;
                }
            }
        }
        // RF Output Compat
        if (GregTech_API.mOutputRF && tTileEntity instanceof IEnergyReceiver
            && ((IEnergyReceiver) tTileEntity).canConnectEnergy(tDir)) return true;

        // RF Input Compat
        return GregTech_API.mInputRF && (tTileEntity instanceof IEnergyEmitter
            && ((IEnergyEmitter) tTileEntity).emitsEnergyTo((TileEntity) baseMetaTile, tDir));
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 Cables are enabled
        return GT_Mod.gregtechproxy.gt6Cable;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
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
        long amps = 0;
        long volts = 0;
        if (path != null) {
            amps = path.getAmps();
            volts = path.getVoltage(this);
        }
        return new String[] {
            // EnumChatFormatting.BLUE + mName + EnumChatFormatting.RESET,
            "Heat: " + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(mOverheat)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxOverheat)
                + EnumChatFormatting.RESET,
            "Max Load (1t):",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(amps)
                + EnumChatFormatting.RESET
                + " A / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mAmperage)
                + EnumChatFormatting.RESET
                + " A",
            "Max EU/p (1t):",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(volts)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mVoltage)
                + EnumChatFormatting.RESET
                + " EU",
            "Max Load (20t): " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mTransferredAmperageLast20OK)
                + EnumChatFormatting.RESET
                + " A",
            "Max EU/p (20t): " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mTransferredVoltageLast20OK)
                + EnumChatFormatting.RESET
                + " EU" };
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0)
            return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
        else return getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    private AxisAlignedBB getActualCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - mThickNess) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 0) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 1) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 2) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 3) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 4) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 5) != 0) {
            tSide0 = tSide2 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }

        byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & (1 << ForgeDirection.DOWN.ordinal())) != 0) tSide0 = 0f;
        if ((tConn & (1 << ForgeDirection.UP.ordinal())) != 0) tSide1 = 1f;
        if ((tConn & (1 << ForgeDirection.NORTH.ordinal())) != 0) tSide2 = 0f;
        if ((tConn & (1 << ForgeDirection.SOUTH.ordinal())) != 0) tSide3 = 1f;
        if ((tConn & (1 << ForgeDirection.WEST.ordinal())) != 0) tSide4 = 0f;
        if ((tConn & (1 << ForgeDirection.EAST.ordinal())) != 0) tSide5 = 1f;

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
    public boolean shouldJoinIc2Enet() {
        if (!GT_Mod.gregtechproxy.ic2EnergySourceCompat) return false;

        if (mConnections != 0) {
            final IGregTechTileEntity baseMeta = getBaseMetaTileEntity();
            for (byte aSide = 0; aSide < 6; aSide++) if (isConnectedAtSide(aSide)) {
                final TileEntity tTileEntity = baseMeta.getTileEntityAtSide(aSide);
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
        return false;
    }

    @Override
    public void reloadLocks() {
        final BaseMetaPipeEntity pipe = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (pipe.getNode() != null) {
            for (byte tSide : ALL_VALID_SIDES) {
                if (isConnectedAtSide(tSide)) {
                    final CoverInfo coverInfo = pipe.getCoverInfoAtSide(tSide);
                    if (coverInfo.getCoverBehavior() instanceof GT_Cover_None) continue;
                    if (!letsIn(coverInfo) || !letsOut(coverInfo)) {
                        pipe.addToLock(pipe, tSide);
                    } else {
                        pipe.removeFromLock(pipe, tSide);
                    }
                }
            }
        } else {
            boolean dontAllow = false;
            for (byte tSide : ALL_VALID_SIDES) {
                if (isConnectedAtSide(tSide)) {
                    final CoverInfo coverInfo = pipe.getCoverInfoAtSide(tSide);
                    if (coverInfo.getCoverBehavior() instanceof GT_Cover_None) continue;

                    if (!letsIn(coverInfo) || !letsOut(coverInfo)) {
                        dontAllow = true;
                    }
                }
            }
            if (dontAllow) {
                pipe.addToLock(pipe, 0);
            } else {
                pipe.removeFromLock(pipe, 0);
            }
        }
    }
}
