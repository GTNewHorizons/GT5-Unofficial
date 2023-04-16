package gregtech.api.graphs;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyReceiver;
import gregtech.api.GregTech_API;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.EmptyPowerConsumer;
import gregtech.api.graphs.consumers.NodeEnergyConnected;
import gregtech.api.graphs.consumers.NodeEnergyReceiver;
import gregtech.api.graphs.consumers.NodeEnergySink;
import gregtech.api.graphs.consumers.NodeGTBaseMetaTile;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import ic2.api.energy.tile.IEnergySink;

// node map generator for power distribution
public class GenerateNodeMapPower extends GenerateNodeMap {

    public GenerateNodeMapPower(BaseMetaPipeEntity aTileEntity) {
        generateNode(aTileEntity, null, 1, null, -1, new ArrayList<>(), new HashSet<>());
    }

    @Override
    protected boolean isPipe(TileEntity aTileEntity) {
        return super.isPipe(aTileEntity)
            && ((BaseMetaPipeEntity) aTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable;
    }

    @Override
    protected boolean addConsumer(TileEntity aTileEntity, ForgeDirection aSide, int aNodeValue,
        ArrayList<ConsumerNode> aConsumers) {
        if (aTileEntity instanceof BaseMetaTileEntity tBaseTileEntity) {
            if (tBaseTileEntity.inputEnergyFrom(aSide, false)) {
                ConsumerNode tConsumerNode = new NodeGTBaseMetaTile(aNodeValue, tBaseTileEntity, aSide, aConsumers);
                aConsumers.add(tConsumerNode);
                return true;
            }

        } else if (aTileEntity instanceof IEnergyConnected tTileEntity) {
            if (tTileEntity.inputEnergyFrom(aSide, false)) {
                ConsumerNode tConsumerNode = new NodeEnergyConnected(aNodeValue, tTileEntity, aSide, aConsumers);
                aConsumers.add(tConsumerNode);
                return true;
            }
        } else if (aTileEntity instanceof IEnergySink) {
            // ic2 wants the tilentity next to it of that side not going to add a bunch of arguments just for ic2
            // crossborder checks to not load chuncks just to make sure
            int dX = aTileEntity.xCoord + ForgeDirection.getOrientation(aSide).offsetX;
            int dY = aTileEntity.yCoord + ForgeDirection.getOrientation(aSide).offsetY;
            int dZ = aTileEntity.zCoord + ForgeDirection.getOrientation(aSide).offsetZ;
            boolean crossesChuncks = dX >> 4 != aTileEntity.xCoord >> 4 || dZ >> 4 != aTileEntity.zCoord >> 4;
            TileEntity tNextTo = null;
            if (!crossesChuncks || !aTileEntity.getWorldObj()
                .blockExists(dX, dY, dZ))
                tNextTo = aTileEntity.getWorldObj()
                    .getTileEntity(dX, dY, dZ);

            if (((IEnergySink) aTileEntity).acceptsEnergyFrom(tNextTo, ForgeDirection.getOrientation(aSide))) {
                ConsumerNode tConsumerNode = new NodeEnergySink(
                    aNodeValue,
                    (IEnergySink) aTileEntity,
                    aSide,
                    aConsumers);
                aConsumers.add(tConsumerNode);
                return true;
            }
        } else if (GregTech_API.mOutputRF && aTileEntity instanceof IEnergyReceiver) {
            ConsumerNode tConsumerNode = new NodeEnergyReceiver(
                aNodeValue,
                (IEnergyReceiver) aTileEntity,
                aSide,
                aConsumers);
            aConsumers.add(tConsumerNode);
            return true;
        }
        return false;
    }

    @Override
    protected NodePath getNewPath(MetaPipeEntity[] aPipes) {
        return new PowerNodePath(aPipes);
    }

    // used to apply voltage on dead ends
    @Override
    protected Node getEmptyNode(int aNodeValue, ForgeDirection aSide, TileEntity aTileEntity,
        ArrayList<ConsumerNode> aConsumers) {
        ConsumerNode tNode = new EmptyPowerConsumer(aNodeValue, aTileEntity, aSide, aConsumers);
        aConsumers.add(tNode);
        return tNode;
    }

    @Override
    protected Node getPipeNode(int aNodeValue, ForgeDirection aSide, TileEntity aTileEntity,
        ArrayList<ConsumerNode> aConsumers) {
        return new PowerNode(aNodeValue, aTileEntity, aConsumers);
    }
}
