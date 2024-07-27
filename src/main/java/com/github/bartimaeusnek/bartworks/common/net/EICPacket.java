package com.github.bartimaeusnek.bartworks.common.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ElectricImplosionCompressor;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_New;
import io.netty.buffer.ByteBuf;

public class EICPacket extends GT_Packet_New {

    private Coords coords;
    private boolean bool;

    public EICPacket() {
        super(true);
    }

    public EICPacket(Coords coords, boolean bool) {
        super(false);
        this.coords = coords;
        this.bool = bool;
    }

    @Override
    public byte getPacketID() {
        return 5;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.coords.x);
        aOut.writeInt(this.coords.y);
        aOut.writeInt(this.coords.z);
        aOut.writeBoolean(this.bool);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new EICPacket(new Coords(aData.readInt(), aData.readInt(), aData.readInt()), aData.readBoolean());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (SideReference.Side.Client) {
            TileEntity te = aWorld.getTileEntity(this.coords.x, this.coords.y, this.coords.z);
            if (!(te instanceof IGregTechTileEntity)) return;
            IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
            if (!(mte instanceof GT_TileEntity_ElectricImplosionCompressor)) return;
            if (this.bool && !((IGregTechTileEntity) te).hasMufflerUpgrade())
                ((IGregTechTileEntity) te).addMufflerUpgrade();
        }
    }
}
