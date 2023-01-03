package com.github.bartimaeusnek.bartworks.common.net;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ExtremeIndustrialGreenhouse;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_New;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class EIGPacket extends GT_Packet_New {
    private Coords coords;
    private int mMaxSlots;

    public EIGPacket() {
        super(true);
    }

    public EIGPacket(Coords coords, int mMaxSlots) {
        super(false);
        this.coords = coords;
        this.mMaxSlots = mMaxSlots;
    }

    @Override
    public byte getPacketID() {
        return 6;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(coords.x);
        aOut.writeInt(coords.y);
        aOut.writeInt(coords.z);
        aOut.writeInt(mMaxSlots);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new EIGPacket(new Coords(aData.readInt(), aData.readInt(), aData.readInt()), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (SideReference.Side.Client) {
            TileEntity te = aWorld.getTileEntity(coords.x, coords.y, coords.z);
            if (!(te instanceof IGregTechTileEntity)) return;
            IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
            if (!(mte instanceof GT_TileEntity_ExtremeIndustrialGreenhouse)) return;
            ((GT_TileEntity_ExtremeIndustrialGreenhouse) mte).mMaxSlots = this.mMaxSlots;
        }
    }
}
