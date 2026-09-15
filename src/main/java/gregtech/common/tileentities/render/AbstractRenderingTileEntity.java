package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class AbstractRenderingTileEntity extends TileEntity {

    protected final BoundingBoxConstructor boundingBoxConstructor;
    protected AxisAlignedBB renderBoundingBox;

    /**
     * New tileEntity with full block bounding box
     */
    protected AbstractRenderingTileEntity() {
        this(0);
    }

    /**
     * New tileEntity with full block bounding box plus a radius
     * 
     * @param boundingRadius increasing radius for the bounding box
     */
    protected AbstractRenderingTileEntity(double boundingRadius) {
        this(boundingRadius, boundingRadius);
    }

    /**
     * New tileEntity with middle centered bounding box
     * 
     * @param width  horizontal diameter + 1
     * @param height vertical diameter + 1
     */
    protected AbstractRenderingTileEntity(double width, double height) {
        this(width, height, width, width, height, width);
    }

    protected AbstractRenderingTileEntity(double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ) {
        this(
            (te) -> AxisAlignedBB.getBoundingBox(
                te.xCoord - minX,
                te.yCoord - minY,
                te.zCoord - minZ,
                te.xCoord + maxX + 1,
                te.yCoord + maxY + 1,
                te.zCoord + maxZ + 1));
    }

    protected AbstractRenderingTileEntity(BoundingBoxConstructor boundingBoxConstructor) {
        this.boundingBoxConstructor = boundingBoxConstructor;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderBoundingBox == null) renderBoundingBox = boundingBoxConstructor.construct(this);
        return renderBoundingBox;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    public interface BoundingBoxConstructor {

        AxisAlignedBB construct(TileEntity te);
    }

}
