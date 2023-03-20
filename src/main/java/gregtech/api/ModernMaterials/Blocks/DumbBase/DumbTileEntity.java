package gregtech.api.ModernMaterials.Blocks.DumbBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class DumbTileEntity extends TileEntity {

    private int materialID;

    public int getMaterialID() {
        return materialID;
    }

    @Override
    public int getBlockMetadata() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
        this.markDirty();
    }

    private static final String NBT_TAG_MATERIAL_ID = "materialID";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(NBT_TAG_MATERIAL_ID, materialID);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        materialID = compound.getInteger(NBT_TAG_MATERIAL_ID);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound NBTTag = new NBTTagCompound();
        this.writeToNBT(NBTTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, NBTTag);
    }
}
