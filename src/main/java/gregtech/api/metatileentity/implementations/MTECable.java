package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import gregtech.api.util.tooltip.TooltipHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyReceiver;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.HarvestTool;
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
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTGCCompat;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverSolarPanel;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorChamber;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTECable extends MetaPipeEntity implements IMetaTileEntityCable {

    public final float mThickNess;
    public final Materials mMaterial;
    public final long mCableLossPerMeter, mAmperage, mVoltage;
    public final boolean mInsulated, mCanShock;

    public int mTransferredAmperage = 0;

    public MTECable(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial,
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

    public MTECable(String aName, float aThickNess, Materials aMaterial, long aCableLossPerMeter, long aAmperage,
        long aVoltage, boolean aInsulated, boolean aCanShock) {
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
        if (mInsulated) return HarvestTool.CutterLevel1.toTileEntityBaseType();
        return HarvestTool.CutterLevel0.toTileEntityBaseType();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECable(
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
            float tThickNess = getThickness();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                Textures.BlockIcons.INSULATION_FULL,
                Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.374F) // 0.375 x1
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_TINY,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.499F) // 0.500 x2
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_SMALL,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.624F) // 0.625 x4
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.749F) // 0.750 x8
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM_PLUS,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.874F) // 0.825 x12
                return new ITexture[] {
                    TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_LARGE,
                        Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            return new ITexture[] {
                TextureFactory.of(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa),
                TextureFactory.of(
                    Textures.BlockIcons.INSULATION_HUGE,
                    Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
        }
        return new ITexture[] { TextureFactory
            .of(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(colorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {

        if (!mCanShock) return;

        final BaseMetaPipeEntity baseEntity = (BaseMetaPipeEntity) getBaseMetaTileEntity();

        if (!(aEntity instanceof EntityLivingBase livingEntity)) return;
        if (!(baseEntity.getNodePath() instanceof PowerNodePath powerPath)) return;

        if (isCoverOnSide(baseEntity, livingEntity)) return;

        final long amperage = powerPath.getAmperage();
        final long voltage = powerPath.getVoltage();

        if (amperage == 0L) return;

        GTUtility.applyElectricityDamage(livingEntity, voltage, amperage);
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
        return mTransferredAmperage * 64;
    }

    @Override
    public int maxProgresstime() {
        return (int) mAmperage * 64;
    }

    @Override
    public long injectEnergyUnits(ForgeDirection side, long voltage, long amperage) {
        if (!isConnectedAtSide(side) && side != ForgeDirection.UNKNOWN) return 0;
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .letsEnergyIn()) return 0;
        return transferElectricity(side, voltage, amperage, null);
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0 && aBaseMetaTileEntity.isServerSide() && (!GTMod.proxy.gt6Cable || mCheckConnections)) {
            checkConnections();
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Only perform the logic if the player is sneaking.
        if (!aPlayer.isSneaking()) {
            return;
        }

        ItemStack handItem = aPlayer.inventory.getCurrentItem();
        if (handItem == null) {
            return;
        }

        IMetaTileEntity meta = ItemMachines.getMetaTileEntity(handItem);
        if (!(meta instanceof MTECable handCable)) {
            return;
        }

        // 1) Record & disconnect old cable from all sides where it was connected
        MTECable oldCable = this;
        byte oldConnections = oldCable.mConnections;

        // We'll remember which sides were connected, so we can reconnect the new cable
        List<ForgeDirection> oldConnectedSides = new ArrayList<>();
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (oldCable.isConnectedAtSide(side)) {
                oldConnectedSides.add(side);
            }
        }

        short newMetaID = (short) handItem.getItemDamage();
        long oldVoltage = this.mVoltage;
        long oldAmperage = this.mAmperage;

        // If the existing cable has the same specs as what we're holding, skip.
        if (this.getClass() == handCable.getClass() && this.mMaterial == handCable.mMaterial
            && this.mVoltage == handCable.mVoltage
            && this.mAmperage == handCable.mAmperage) {
            return;
        }

        short oldMetaID = (short) aBaseMetaTileEntity.getMetaTileID();

        // Construct the new cable
        MTECable newCable = new MTECable(
            handCable.mName,
            handCable.mThickNess,
            handCable.mMaterial,
            handCable.mCableLossPerMeter,
            handCable.mAmperage,
            handCable.mVoltage,
            handCable.mInsulated,
            handCable.mCanShock);

        // Swap in the new cable
        aBaseMetaTileEntity.setMetaTileID(newMetaID);
        newCable.setBaseMetaTileEntity(aBaseMetaTileEntity);

        aBaseMetaTileEntity.markDirty();
        aBaseMetaTileEntity.issueBlockUpdate();

        // 7) Reconnect the *new* cable to the old sides (modified for both cables and machines)
        if (newCable.getBaseMetaTileEntity() != null) {
            // For each side that was previously connected
            for (ForgeDirection side : oldConnectedSides) {
                IGregTechTileEntity neighborTile = newCable.getBaseMetaTileEntity()
                    .getIGregTechTileEntityAtSide(side);
                int result = newCable.connect(side);
                // If there is a neighbor tile, then:
                if (neighborTile != null) {
                    // If the neighbor is a cable (implements IConnectable), then require a successful connection
                    if (neighborTile.getMetaTileEntity() instanceof IConnectable) {
                        if (result > 0) {
                            ((IConnectable) neighborTile.getMetaTileEntity()).connect(side.getOpposite());
                        } else {
                            newCable.disconnect(side);
                        }
                    }
                    // Otherwise, if the neighbor is a machine (or any non-cable tile), don't connect.
                } else {
                    // No neighbor exists at this side, so disconnect
                    newCable.disconnect(side);
                }
            }
        }
        aBaseMetaTileEntity.issueTextureUpdate();

        // Handle inventory changes (old <-> new cable) if not creative
        if (!aPlayer.capabilities.isCreativeMode) {
            ItemStack oldCableStack = new ItemStack(handItem.getItem(), 1, oldMetaID);
            boolean addedToInventory = false;

            if (oldCableStack != null) {
                // Try stacking with existing inventory
                for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
                    ItemStack slot = aPlayer.inventory.mainInventory[i];
                    if (slot != null && slot.getItem() == oldCableStack.getItem()
                        && slot.getItemDamage() == oldCableStack.getItemDamage()
                        && slot.stackSize < slot.getMaxStackSize()) {
                        slot.stackSize++;
                        addedToInventory = true;
                        break;
                    }
                }

                // If not stacked, add new item
                if (!addedToInventory) {
                    addedToInventory = aPlayer.inventory.addItemStackToInventory(oldCableStack);
                }
                // If still unsuccessful, drop it
                if (!addedToInventory) {
                    aPlayer.dropPlayerItemWithRandomChoice(oldCableStack, false);
                }
            }

            // Decrease the held cable quantity
            handItem.stackSize--;
            if (handItem.stackSize <= 0) {
                aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
            }
        }

        // Optionally, notify the player if voltage or amperage changed
        if (oldAmperage != handCable.mAmperage || oldVoltage != handCable.mVoltage) {
            StringBuilder message = new StringBuilder();
            if (oldAmperage != handCable.mAmperage) {
                message.append(oldAmperage)
                    .append("A → ")
                    .append(handCable.mAmperage > oldAmperage ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                    .append(handCable.mAmperage)
                    .append("A")
                    .append(EnumChatFormatting.RESET);
            }
            if (oldAmperage != handCable.mAmperage && oldVoltage != handCable.mVoltage) {
                message.append(" | ");
            }
            if (oldVoltage != handCable.mVoltage) {
                message.append(oldVoltage)
                    .append("V → ")
                    .append(handCable.mVoltage > oldVoltage ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                    .append(handCable.mVoltage)
                    .append("V")
                    .append(EnumChatFormatting.RESET);
            }
            GTUtility
                .sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.item.cable.swapped") + " " + message);
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (GTMod.proxy.gt6Cable
            && GTModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(wrenchingSide)) {
                disconnect(wrenchingSide);
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("215", "Disconnected"));
            } else if (!GTMod.proxy.costlyCableConnection) {
                if (connect(wrenchingSide) > 0)
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (GTMod.proxy.gt6Cable
            && GTModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 500, aPlayer)) {
            if (isConnectedAtSide(wrenchingSide)) {
                disconnect(wrenchingSide);
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("215", "Disconnected"));
            } else if (!GTMod.proxy.costlyCableConnection || GTModHandler.consumeSolderingMaterial(aPlayer)) {
                if (connect(wrenchingSide) > 0)
                    GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("214", "Connected"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsIn(Cover cover) {
        return cover.letsEnergyIn();
    }

    @Override
    public boolean letsOut(Cover cover) {
        return cover.letsEnergyOut();
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        final ForgeDirection oppositeSide = side.getOpposite();

        // GT Machine handling
        if (((tileEntity instanceof IEnergyConnected energyConnected)
            && (energyConnected.inputEnergyFrom(oppositeSide, false)
                || energyConnected.outputsEnergyTo(oppositeSide, false))))
            return true;

        // Solar Panel Compat
        if (baseMetaTile.getCoverAtSide(side) instanceof CoverSolarPanel) return true;

        // ((tIsGregTechTileEntity && tIsTileEntityCable) && (tAlwaysLookConnected || tLetEnergyIn || tLetEnergyOut) )
        // --> Not needed
        if (GalacticraftCore.isModLoaded() && GTGCCompat.canConnect(tileEntity, oppositeSide)) return true;

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
            if (GTMod.proxy.ic2EnergySourceCompat && (ic2Energy instanceof IEnergySource)) {
                if (((IEnergySource) ic2Energy).emitsEnergyTo((TileEntity) baseMetaTile, oppositeSide)) {
                    return true;
                }
            }
        }
        // RF Output Compat
        if (GregTechAPI.mOutputRF && tileEntity instanceof IEnergyReceiver
            && ((IEnergyReceiver) tileEntity).canConnectEnergy(oppositeSide)) return true;

        // RF Input Compat
        return GregTechAPI.mInputRF && (tileEntity instanceof IEnergyEmitter
            && ((IEnergyEmitter) tileEntity).emitsEnergyTo((TileEntity) baseMetaTile, oppositeSide));
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 Cables are enabled
        return GTMod.proxy.gt6Cable;
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
        // The %%% are required for ItemMachines#addDescription
        return new String[] {
            StatCollector.translateToLocal("GT5U.item.cable.max_voltage") + " %%%"
                + TooltipHelper.voltageText(mVoltage),
            StatCollector.translateToLocal("GT5U.item.cable.max_amperage") + ": %%%"
                + TooltipHelper.ampText(mAmperage),
            StatCollector.translateToLocal("GT5U.item.cable.loss") + ": %%%"
                + TooltipHelper.cableLossText(mCableLossPerMeter) };
    }

    @Override
    public float getCollisionThickness() {
        return mThickNess;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (GTMod.proxy.gt6Cable) aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (GTMod.proxy.gt6Cable) {
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
            return new String[] { EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.infodata.cable.failed")
                + EnumChatFormatting.RESET };

        path.reloadLocks();

        final long currAmp = path.getAmperage();
        final long currVoltage = path.getVoltage();

        final double avgAmp = path.getAvgAmperage();
        final double avgVoltage = path.getAvgVoltage();

        final long maxVoltageOut = (mVoltage - mCableLossPerMeter) * mAmperage;

        return new String[] {
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.cable.amperage",
                EnumChatFormatting.GREEN + formatNumber(currAmp) + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + formatNumber(mAmperage) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.cable.voltage_out",
                EnumChatFormatting.GREEN + formatNumber(currVoltage) + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + formatNumber(maxVoltageOut) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.cable.avg_amperage",
                EnumChatFormatting.YELLOW + formatNumber(avgAmp) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.cable.avg_output",
                EnumChatFormatting.YELLOW + formatNumber(avgVoltage) + EnumChatFormatting.RESET) };
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        if (!GTMod.proxy.ic2EnergySourceCompat) return false;

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
                    final Cover cover = pipe.getCoverAtSide(side);
                    if (!cover.isValid()) continue;
                    if (!letsIn(cover) || !letsOut(cover)) {
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
                    final Cover cover = pipe.getCoverAtSide(side);
                    if (!cover.isValid()) continue;

                    if (!letsIn(cover) || !letsOut(cover)) {
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
                + formatNumber(mVoltage)
                + " ("
                + GTUtility.getColoredTierNameFromVoltage(mVoltage)
                + EnumChatFormatting.GREEN
                + ")");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.cable.max_amperage") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(mAmperage));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.cable.loss") + ": "
                + EnumChatFormatting.RED
                + formatNumber(mCableLossPerMeter)
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.item.cable.eu_volt"));
    }
}
