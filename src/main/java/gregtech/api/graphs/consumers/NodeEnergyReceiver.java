package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyReceiver;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.pollution.Pollution;

// consumer for RF machines
public class NodeEnergyReceiver extends ConsumerNode {

    int mRestRF = 0;

    public NodeEnergyReceiver(int aNodeValue, IEnergyReceiver aTileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, (TileEntity) aTileEntity, side, aConsumers);
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        ForgeDirection tDirection = mSide;
        int rfOut = GTUtility.safeInt(aVoltage * GregTechAPI.mEUtoRF / 100);
        int ampsUsed = 0;
        if (mRestRF < rfOut) {
            mRestRF += rfOut;
            ampsUsed = 1;
        }
        if (((IEnergyReceiver) mTileEntity).receiveEnergy(tDirection, mRestRF, true) > 0) {
            int consumed = ((IEnergyReceiver) mTileEntity).receiveEnergy(tDirection, mRestRF, false);
            mRestRF -= consumed;
            return ampsUsed;
        }
        if (GregTechAPI.mRFExplosions && GregTechAPI.sMachineExplosions
            && ((IEnergyReceiver) mTileEntity).getMaxEnergyStored(tDirection) < rfOut * 600L) {
            explode(rfOut);
        }
        return 0;
    }

    // copied from IEnergyConnected
    private void explode(int aRfOut) {
        if (aRfOut > 32L * GregTechAPI.mEUtoRF / 100L) {
            float tStrength = GTValues.getExplosionPowerForVoltage(aRfOut);
            int tX = mTileEntity.xCoord, tY = mTileEntity.yCoord, tZ = mTileEntity.zCoord;
            World tWorld = mTileEntity.getWorldObj();
            GTUtility.sendSoundToPlayers(
                tWorld,
                SoundResource.IC2_MACHINES_MACHINE_OVERLOAD,
                1.0F,
                -1,
                tX + .5,
                tY + .5,
                tZ + .5);
            tWorld.setBlock(tX, tY, tZ, Blocks.air);
            if (GregTechAPI.sMachineExplosions) if (GTMod.proxy.mPollution)
                Pollution.addPollution(tWorld.getChunkFromBlockCoords(tX, tZ), GTMod.proxy.mPollutionOnExplosion);

            new WorldSpawnedEventBuilder.ExplosionEffectEventBuilder().setStrength(tStrength)
                .setSmoking(true)
                .setPosition(tX + 0.5, tY + 0.5, tZ + 0.5)
                .setWorld(tWorld)
                .run();
        }
    }
}
