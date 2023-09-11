package gregtech.api.ModernMaterials.Blocks.DumbBase.Simple;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

public abstract class DumbTileEntity extends TileEntity {

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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound NBTTag = new NBTTagCompound();
        this.writeToNBT(NBTTag);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, NBTTag);
    }


}
