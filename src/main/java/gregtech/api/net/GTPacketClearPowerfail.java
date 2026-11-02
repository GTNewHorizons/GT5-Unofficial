package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Mods;
import gregtech.common.data.GTPowerfailTracker;
import gregtech.crossmod.navigator.PowerfailLayerManager;
import io.netty.buffer.ByteBuf;

public class GTPacketClearPowerfail extends GTPacket {

    private int dim, x, y, z;

    GTPacketClearPowerfail() {}

    public GTPacketClearPowerfail(GTPowerfailTracker.Powerfail powerfail) {
        this.dim = powerfail.dim;
        this.x = powerfail.x;
        this.y = powerfail.y;
        this.z = powerfail.z;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.CLEAR_POWERFAIL.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(dim);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPacketClearPowerfail packet = new GTPacketClearPowerfail();

        packet.dim = buffer.readInt();
        packet.x = buffer.readInt();
        packet.y = buffer.readInt();
        packet.z = buffer.readInt();

        return packet;
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            clearClient();
        } else {
            clearServer();
        }
    }

    @SideOnly(Side.CLIENT)
    private void clearClient() {
        var powerfails = GTMod.clientProxy().powerfailRenderer.powerfails.get(dim);

        if (powerfails != null) {
            long coord = CoordinatePacker.pack(x, y, z);
            powerfails.remove(coord);
            if (Mods.Navigator.isModLoaded()) PowerfailLayerManager.INSTANCE.removePowerfail(coord);
        }
    }

    private void clearServer() {
        GTMod.proxy.powerfailTracker.removePowerfailEvents(dim, x, y, z);
    }
}
