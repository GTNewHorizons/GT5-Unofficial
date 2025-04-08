package gregtech.api.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasInventory;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.util.GTByteBuffer;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server: Update machine configuration data
 */
public class GTPacketSetConfigurationCircuit extends GTPacket {

    protected int mX;
    protected short mY;
    protected int mZ;
    protected int dimId;

    protected ItemStack circuit;

    public GTPacketSetConfigurationCircuit() {
        super();
    }

    public GTPacketSetConfigurationCircuit(IGregTechTileEntity tile, ItemStack circuit) {
        this(tile.getXCoord(), tile.getYCoord(), tile.getZCoord(), circuit);
    }

    public GTPacketSetConfigurationCircuit(BaseTileEntity tile, ItemStack circuit) {
        this(tile.getXCoord(), tile.getYCoord(), tile.getZCoord(), circuit);
    }

    public GTPacketSetConfigurationCircuit(int x, short y, int z, ItemStack circuit) {
        super();

        this.mX = x;
        this.mY = y;
        this.mZ = z;

        this.circuit = circuit;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SET_CONFIGURATION_CIRCUIT.id;
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
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketSetConfigurationCircuit(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            GTByteBuffer.readItemStackFromGreggyByteBuf(aData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        final World world = DimensionManager.getWorld(dimId);
        if (world == null) return;

        final TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (!(tile instanceof BaseTileEntity) || ((BaseTileEntity) tile).isDead()) return;

        final IConfigurationCircuitSupport machine = ((BaseTileEntity) tile).getConfigurationCircuitSupport();
        if (machine == null) return;
        if (!machine.allowSelectCircuit()) return;
        GTUtility.getAllIntegratedCircuits()
            .stream()
            .filter(stack -> GTUtility.areStacksEqual(stack, circuit))
            .findFirst()
            .ifPresent(stack -> ((IHasInventory) tile).setInventorySlotContents(machine.getCircuitSlot(), stack));
    }
}
