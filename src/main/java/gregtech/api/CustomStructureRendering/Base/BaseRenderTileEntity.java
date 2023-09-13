package gregtech.api.CustomStructureRendering.Base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import static gregtech.api.CustomStructureRendering.Base.BaseRenderTESR.MODEL_NAME_NBT_TAG;

public class BaseRenderTileEntity extends TileEntity {

    // Prevent culling when block is out of frame so model can remain active.
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }

    public String modelName = "Default";

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Read the custom NBT data
        if (compound.hasKey(MODEL_NAME_NBT_TAG)) {
            modelName = compound.getString(MODEL_NAME_NBT_TAG);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        // Write the custom NBT data
        compound.setString(MODEL_NAME_NBT_TAG, modelName);
    }
}
