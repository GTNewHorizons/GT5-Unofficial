package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Client -> Server: Update machine configuration data
 */

public class GT_Packet_SetConfigurationCircuit extends GT_Packet_New {
    protected int mX;
    protected short mY;
    protected int mZ;
    protected int dimId;

    protected ItemStack circuit;

    public GT_Packet_SetConfigurationCircuit() {
        super(true);
    }

    public GT_Packet_SetConfigurationCircuit(IGregTechTileEntity tile, ItemStack circuit) {
        this(tile.getXCoord(), tile.getYCoord(), tile.getZCoord(), circuit);
    }

    public GT_Packet_SetConfigurationCircuit(int x, short y, int z, ItemStack circuit) {
        super(false);

        this.mX = x;
        this.mY = y;
        this.mZ = z;

        this.circuit = circuit;
    }

    @Override
    public byte getPacketID() {
        return 12;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        // no null check needed. ByteBufUtils will handle it
        ByteBufUtils.writeItemStack(aOut, this.circuit);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            dimId = ((NetHandlerPlayServer) aHandler).playerEntity.dimension;
        } else {
            // packet sent to wrong side, so we need to ignore this one
            // but there is no way to disrupt packet pipeline
            // so we will instead go find world -2, which (hopefully) doesn't exist
            // then we will fail silently in process()
            dimId = -2;
        }
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_SetConfigurationCircuit(
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),

                ISerializableObject.readItemStackFromGreggyByteBuf(aData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World world = DimensionManager.getWorld(dimId);
        if (world == null) return;
        TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (!(tile instanceof IGregTechTileEntity) || ((IGregTechTileEntity) tile).isDead()) return;
        IMetaTileEntity mte = ((IGregTechTileEntity) tile).getMetaTileEntity();
        if (!(mte instanceof GT_MetaTileEntity_BasicMachine)) return;
        GT_MetaTileEntity_BasicMachine machine = (GT_MetaTileEntity_BasicMachine) mte;
        if (!machine.allowSelectCircuit()) return;
        machine.getConfigurationCircuits().stream()
                .filter(stack -> GT_Utility.areStacksEqual(stack, circuit))
                .findFirst()
                .ifPresent(stack -> ((IGregTechTileEntity) tile).setInventorySlotContents(machine.getCircuitSlot(), stack));
    }
}
