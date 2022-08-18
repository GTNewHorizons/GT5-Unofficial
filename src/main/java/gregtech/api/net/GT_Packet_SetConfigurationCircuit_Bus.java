package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.DimensionManager;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.GT_Container_InputBus_ME;

public class GT_Packet_SetConfigurationCircuit_Bus extends GT_Packet_SetConfigurationCircuit {
    public GT_Packet_SetConfigurationCircuit_Bus() {
        super();
    }

    public GT_Packet_SetConfigurationCircuit_Bus(IGregTechTileEntity tile, ItemStack circuit) {
        super(tile, circuit);
    }

    public GT_Packet_SetConfigurationCircuit_Bus(int x, short y, int z, ItemStack circuit) {
        super(x, y, z, circuit);
    }

    @Override
    public byte getPacketID() {
        return 18;
    }
    @Override
    public void process(IBlockAccess aWorld) {
        World world = DimensionManager.getWorld(dimId);
        if (world == null) return;
        TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (!(tile instanceof IGregTechTileEntity) || ((IGregTechTileEntity) tile).isDead())
            return;
        IMetaTileEntity mte = ((IGregTechTileEntity) tile).getMetaTileEntity();
        if (!(mte instanceof GT_MetaTileEntity_Hatch)) return;
        GT_MetaTileEntity_Hatch hatch = (GT_MetaTileEntity_Hatch) mte;
        GregTech_API.getConfigurationCircuitList(hatch.mTier).stream()
            .filter(stack -> GT_Utility.areStacksEqual(stack, circuit))
            .findFirst()
            .ifPresent(stack -> ((IGregTechTileEntity) tile).setInventorySlotContents(hatch.getCircuitSlot(), stack));
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_SetConfigurationCircuit_Bus(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ISerializableObject.readItemStackFromGreggyByteBuf(aData));
    }
}
