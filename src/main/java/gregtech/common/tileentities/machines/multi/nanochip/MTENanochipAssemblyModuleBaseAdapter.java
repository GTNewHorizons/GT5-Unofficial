package gregtech.common.tileentities.machines.multi.nanochip;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;

public class MTENanochipAssemblyModuleBaseAdapter implements IByteBufAdapter<MTENanochipAssemblyModuleBase<?>> {

    @Override
    public MTENanochipAssemblyModuleBase<?> deserialize(PacketBuffer buffer) throws IOException {
        if (!buffer.readBoolean()) { // MTE, GTE are null
            return null;
        }
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        World world = DimensionManager.getWorld(buffer.readInt());
        // on the client, world is null here. this sets it to the client value in this case without a side check
        if (world == null) world = GTMod.GT.getThePlayer().worldObj;
        TileEntity te = GTUtil.getTileEntity(world, x, y, z, false);
        if (te == null) return null;

        IMetaTileEntity mte = GTUtility.getMetaTileEntity(te);
        if (mte instanceof MTENanochipAssemblyModuleBase<?>module) {
            return module;
        }
        return null;
    }

    @Override
    public void serialize(PacketBuffer buffer, MTENanochipAssemblyModuleBase module) throws IOException {
        if (module != null && module.getBaseMetaTileEntity() != null) {
            buffer.writeBoolean(true);
            IGregTechTileEntity gte = module.getBaseMetaTileEntity();
            buffer.writeInt(gte.getXCoord());
            buffer.writeInt(gte.getYCoord());
            buffer.writeInt(gte.getZCoord());
            buffer.writeInt(gte.getWorld().provider.dimensionId);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public boolean areEqual(@NotNull MTENanochipAssemblyModuleBase t1, @NotNull MTENanochipAssemblyModuleBase t2) {
        IGregTechTileEntity t1b = t1.getBaseMetaTileEntity();
        IGregTechTileEntity t2b = t2.getBaseMetaTileEntity();
        if (t1b == null || t2b == null) return false;

        if (t1b.getXCoord() != t2b.getXCoord()) return false;
        if (t1b.getYCoord() != t2b.getYCoord()) return false;
        if (t1b.getZCoord() != t2b.getZCoord()) return false;
        return t1b.getWorld().provider.dimensionId == t2b.getWorld().provider.dimensionId;
    }
}
