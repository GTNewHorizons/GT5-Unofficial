package gregtech.loaders.postload;

import java.lang.reflect.Field;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.parts.AEBasePart;
import appeng.parts.p2p.PartP2PIC2Power;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PartP2PGTPower extends PartP2PIC2Power implements IGridTickable {
    public PartP2PGTPower(ItemStack is) {
        super(is);
    }

    public final World getWorld() {
        return getTile().getWorldObj();
    }

    public final int getXCoord() {
        return getTile().xCoord;
    }

    public final short getYCoord() {
        return (short) getTile().yCoord;
    }

    public final int getZCoord() {
        return getTile().zCoord;
    }

    public final int getOffsetX(byte aSide, int aMultiplier) {
        return getXCoord() + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier;
    }

    public final short getOffsetY(byte aSide, int aMultiplier) {
        return (short) (getYCoord() + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);
    }

    public final int getOffsetZ(byte aSide, int aMultiplier) {
        return getZCoord() + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier;
    }

    public final TileEntity getTileEntity(int aX, int aY, int aZ) {
        return getWorld().getTileEntity(aX, aY, aZ);
    }

    public final TileEntity getTileEntityAtSide(byte aSide) {
        int tX = getOffsetX(aSide, 1), tY = getOffsetY(aSide, 1), tZ = getOffsetZ(aSide, 1);
        return getWorld().getTileEntity(tX, tY, tZ);
    }

    public boolean outputEnergy() {
        if (getOfferedEnergy() == 0) {
            return false;
        }
        TileEntity t = getTileEntityAtSide((byte) getSide().ordinal());
        if (t instanceof IEnergyConnected) {
            long voltage = 8 << (getSourceTier() * 2);
            if (voltage > getOfferedEnergy()) {
                voltage = (long) getOfferedEnergy();
            }
            if (((IEnergyConnected) t).injectEnergyUnits(GT_Utility.getOppositeSide(getSide().ordinal()), voltage, 1) > 0) {
                drawEnergy(voltage);
                return true;
            }
        }
        return false;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(1, 20, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        return outputEnergy() ? TickRateModulation.FASTER : TickRateModulation.SLOWER;
    }

    public ForgeDirection getSide(){
    	try {
    		Field fSide = AEBasePart.class.getDeclaredField("side");
			fSide.setAccessible(true);
    		return (ForgeDirection) fSide.get(this);
    	} catch (Exception e) {
    		GT_Log.out.println("A fatal error occured at the P2P tunnel for GT electricity");
            e.printStackTrace(GT_Log.out);
    		throw new RuntimeException(e);
    	}
    }
}
