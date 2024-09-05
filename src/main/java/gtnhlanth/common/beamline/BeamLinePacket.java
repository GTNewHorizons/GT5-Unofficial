package gtnhlanth.common.beamline;

import net.minecraft.nbt.NBTTagCompound;

import tectech.mechanics.dataTransport.DataPacket;

public class BeamLinePacket extends DataPacket<BeamInformation> {

    public BeamLinePacket(BeamInformation content) {
        super(content);
    }

    public BeamLinePacket(NBTTagCompound compound) {
        super(compound);
    }

    @Override
    protected BeamInformation contentFromNBT(NBTTagCompound nbt) {
        /*
         * NBTTagCompound compound = nbt.getCompoundTag("beamline");
         */
        return new BeamInformation(
            nbt.getFloat("energy"),
            nbt.getInteger("rate"),
            nbt.getInteger("particleId"),
            nbt.getInteger("focus"));
    }

    @Override
    protected NBTTagCompound contentToNBT() {

        NBTTagCompound compound = new NBTTagCompound();

        compound.setFloat("energy", content.getEnergy());
        compound.setInteger("rate", content.getRate());
        compound.setInteger("particleId", content.getParticleId());
        compound.setFloat("focus", content.getFocus());

        return compound;
    }

    @Override
    public boolean extraCheck() {
        return true;
    }

    @Override
    protected BeamInformation unifyContentWith(BeamInformation arg0) {
        throw new NoSuchMethodError("Unavailable to unify beam info data packet");
    }
}
