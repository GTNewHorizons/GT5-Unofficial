package gregtech.api.metatileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Sets;

import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.util.GTUtility;
import ic2.api.energy.tile.IEnergySink;

public class TileIC2EnergySink extends TileEntity implements IEnergySink {

    private final IGregTechTileEntity myMeta;
    private MTECable cableMeta = null;

    public TileIC2EnergySink(IGregTechTileEntity meta) {
        if (meta == null) throw new NullPointerException("no null metas");
        myMeta = meta;
        final IMetaTileEntity metaTile = myMeta.getMetaTileEntity();
        if (metaTile instanceof IMetaTileEntityCable) {
            cableMeta = (MTECable) metaTile;
        }
        setWorldObj(meta.getWorld());
        xCoord = meta.getXCoord();
        yCoord = meta.getYCoord();
        zCoord = meta.getZCoord();
    }
    /*
     * IC2 enet compat - IEnergySink
     */

    /**
     * Determine how much energy the sink accepts. Note that you cannot modify the energy net from this method.
     * <p>
     * Make sure that {@link TileIC2EnergySink#injectEnergy} accepts energy if this function returns anything positive.
     *
     * @return max accepted input in eu
     */
    @Override
    public double getDemandedEnergy() {
        if (cableMeta != null) {
            /*
             * We don't want everything to join the enet, treating the cable as a conductor, so we join it as a link.
             * We don't want to traverse all cables connected to this, like we would during distribution, to see if it
             * actually needs any EU, so we just always say we want it all. If there are more than two things attached
             * and one of them is a GT cable that doesn't have anywhere to send its energy, the distribution will be a
             * bit weird. In that case, use only one cable or a transformer.
             */
            return (cableMeta.mVoltage * cableMeta.mAmperage);
        } else return myMeta.getEUCapacity() - myMeta.getStoredEU();
    }

    /**
     * Gets the tier of this energy sink. 1 = LV, 2 = MV, 3 = HV, 4 = EV etc.
     *
     * @return tier of this energy sink or {@link Integer#MAX_VALUE} to allow any voltage
     */
    @Override
    public int getSinkTier() {
        return GTUtility.getTier(cableMeta != null ? cableMeta.mVoltage : myMeta.getInputVoltage());
    }

    /**
     * Transfer energy to the sink.
     * <p>
     * It's highly recommended to accept all energy by letting the internal buffer overflow to increase the performance
     * and accuracy of the distribution simulation.
     *
     * @param directionFrom direction from which the energy comes from
     * @param amount        energy to be transferred
     * @return Energy not consumed (leftover)
     */
    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {

        final long amps = (long) Math
            .max(amount / (cableMeta != null ? cableMeta.mVoltage : myMeta.getInputVoltage() * 1.0), 1.0);
        final long euPerAmp = (long) (amount / (amps * 1.0));

        final IMetaTileEntity metaTile = myMeta.getMetaTileEntity();
        if (metaTile == null) return amount;

        final long usedAmps;
        if (cableMeta != null) {
            usedAmps = ((IMetaTileEntityCable) metaTile).transferElectricity(
                directionFrom,
                Math.min(euPerAmp, cableMeta.mVoltage),
                amps,
                Sets.newHashSet((TileEntity) myMeta));

        } else usedAmps = myMeta.injectEnergyUnits(directionFrom, Math.min(euPerAmp, myMeta.getInputVoltage()), amps);
        return amount - (usedAmps * euPerAmp);

        // transferElectricity for cables
    }

    /**
     * Determine if this acceptor can accept current from an adjacent emitter in a direction.
     * <p>
     * The TileEntity in the emitter parameter is what was originally added to the energy net, which may be normal
     * in-world TileEntity, a delegate or an IMetaDelegate.
     *
     * @param emitter   energy emitter, may also be null or an IMetaDelegate
     * @param direction direction the energy is being received from
     */
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        final IMetaTileEntity metaTile = myMeta.getMetaTileEntity();
        if (metaTile instanceof IMetaTileEntityCable
            && (direction == ForgeDirection.UNKNOWN || ((IConnectable) metaTile).isConnectedAtSide(direction)))
            return true;
        else return myMeta.inputEnergyFrom(direction, false);
    }
}
