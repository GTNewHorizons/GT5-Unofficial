package gregtech.common.tileentities.machines.multi.nanochip;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;

public class MTENanochipAssemblyModuleBaseAdapter implements IByteBufAdapter<MTENanochipAssemblyModuleBase<?>> {

    @Override
    public MTENanochipAssemblyModuleBase<?> deserialize(PacketBuffer buffer) throws IOException {
        if (!buffer.readBoolean() || !buffer.readBoolean()) { // MTE or GTE are null
            return null;
        }

        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        World world = DimensionManager.getWorld(buffer.readInt());

        TileEntity te = GTUtil.getTileEntity(world, x, y, z, true);
        if (te == null) return null;

        IMetaTileEntity mte = GTUtility.getMetaTileEntity(te);
        if (mte instanceof MTENanochipAssemblyModuleBase<?>module) {
            return module;
        }
        return null;
    }

    @Override
    public void serialize(PacketBuffer buffer, MTENanochipAssemblyModuleBase module) throws IOException {
        buffer.writeBoolean(module != null);
        if (module != null) {
            IGregTechTileEntity gte = module.getBaseMetaTileEntity();
            buffer.writeBoolean(gte != null);
            if (gte != null) {
                buffer.writeInt(gte.getXCoord());
                buffer.writeInt(gte.getYCoord());
                buffer.writeInt(gte.getZCoord());
                buffer.writeInt(gte.getWorld().provider.dimensionId);
            }
        }
    }

    @Override
    public boolean areEqual(@NotNull MTENanochipAssemblyModuleBase t1, @NotNull MTENanochipAssemblyModuleBase t2) {
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
