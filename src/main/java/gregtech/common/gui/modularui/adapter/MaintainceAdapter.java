package gregtech.common.gui.modularui.adapter;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MaintainceAdapter implements IByteBufAdapter<MTEMultiBlockBase> {


    @Override
    public MTEMultiBlockBase deserialize(PacketBuffer buffer) throws IOException {
        if( !buffer.readBoolean()) {
            return  null; // if MTE or GTE is null
        }

        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        World world = DimensionManager.getWorld(buffer.readInt());

        TileEntity te = GTUtil.getTileEntity(world, x, y,z, true);
        if (te == null) return  null;

        IMetaTileEntity mte = GTUtility.getMetaTileEntity(te);
        if (mte instanceof MTEMultiBlockBase multi){
            return multi;
        }
        return null;
    }

    @Override
    public void serialize(PacketBuffer buffer, MTEMultiBlockBase multiBase) throws IOException {
        if(multiBase != null && multiBase.getBaseMetaTileEntity() != null){
            buffer.writeBoolean(true);
            IGregTechTileEntity gte= multiBase.getBaseMetaTileEntity();
            buffer.writeInt(gte.getXCoord());
            buffer.writeInt(gte.getYCoord());
            buffer.writeInt(gte.getZCoord());
            buffer.writeInt(gte.getWorld().provider.dimensionId);
        }
        else{
            buffer.writeBoolean(false);
        }

    }

    @Override
    public boolean areEqual(@NotNull MTEMultiBlockBase t1, @NotNull MTEMultiBlockBase t2) {
        if (t1.getBaseMetaTileEntity()
            .getXCoord()
            != t2.getBaseMetaTileEntity()
            .getXCoord())
            return false;
        if (t1.getBaseMetaTileEntity()
            .getYCoord()
            != t2.getBaseMetaTileEntity()
            .getYCoord())
            return false;
        if (t1.getBaseMetaTileEntity()
            .getZCoord()
            != t2.getBaseMetaTileEntity()
            .getZCoord())
            return false;
        if (t1.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId
            != t2.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId)
            return false;

        return true;
    }

}
