package bartworks.common.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import bartworks.API.SideReference;
import bartworks.common.tileentities.multis.MTEElectricImplosionCompressor;
import bartworks.util.Coords;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacket;
import io.netty.buffer.ByteBuf;

public class PacketEIC extends GTPacket {

    private Coords coords;
    private boolean bool;

    public PacketEIC() {
        super();
    }

    public PacketEIC(Coords coords, boolean bool) {
        super();
        this.coords = coords;
        this.bool = bool;
    }

    @Override
    public byte getPacketID() {
        return 27;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.coords.x);
        aOut.writeInt(this.coords.y);
        aOut.writeInt(this.coords.z);
        aOut.writeBoolean(this.bool);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new PacketEIC(new Coords(aData.readInt(), aData.readInt(), aData.readInt()), aData.readBoolean());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (SideReference.Side.Client) {
            TileEntity te = aWorld.getTileEntity(this.coords.x, this.coords.y, this.coords.z);
            if (!(te instanceof IGregTechTileEntity)) return;
            IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
            if (!(mte instanceof MTEElectricImplosionCompressor)) return;
            if (this.bool && !((IGregTechTileEntity) te).isMuffled()) ((IGregTechTileEntity) te).addMufflerUpgrade();
        }
    }
}
