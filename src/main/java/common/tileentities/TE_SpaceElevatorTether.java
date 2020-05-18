package common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;

public class TE_SpaceElevatorTether extends TileEntity {

    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared() {
        // 4k is standard, 65k is what the vanilla beacon uses
        return 65536.0D;
    }
}
