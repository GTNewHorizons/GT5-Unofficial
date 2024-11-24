package gtPlusPlus.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.network.handler.AbstractClientMessageHandler;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTurbineHatchUpdate extends AbstractClientMessageHandler<PacketTurbineHatchUpdate>
    implements AbstractPacket {

    private int x;
    private int y;
    private int z;
    private boolean hasTurbine;
    private boolean formed;
    private BlockPos controller;

    public PacketTurbineHatchUpdate() {}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(hasTurbine);
        buf.writeBoolean(formed);
        if (controller != null) {
            buf.writeBoolean(true);
            buf.writeInt(controller.xPos);
            buf.writeInt(controller.yPos);
            buf.writeInt(controller.zPos);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        hasTurbine = buf.readBoolean();
        formed = buf.readBoolean();
        if (buf.readBoolean()) {
            controller = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt(), 0);
        } else {
            controller = null;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isHasTurbine() {
        return hasTurbine;
    }

    public void setHasTurbine(boolean hasTurbine) {
        this.hasTurbine = hasTurbine;
    }

    public boolean isFormed() {
        return formed;
    }

    public void setFormed(boolean formed) {
        this.formed = formed;
    }

    public BlockPos getController() {
        return controller;
    }

    public void setController(BlockPos controller) {
        this.controller = controller;
    }

    @Override
    public String getPacketName() {
        return "Packet_VoluemtricFlaskSetter_ToClient";
    }

    @Override
    public IMessage handleClientMessage(EntityPlayer player, PacketTurbineHatchUpdate message, MessageContext ctx) {
        TileEntity te = player.getEntityWorld().getTileEntity(message.x, message.y, message.z);
        if (!(te instanceof BaseMetaTileEntity mteHost)) return null;
        IMetaTileEntity mte = mteHost.getMetaTileEntity();
        if (!(mte instanceof MTEHatchTurbine hatch)) return null;
        hatch.receiveUpdate(message);

        return null;
    }
}
