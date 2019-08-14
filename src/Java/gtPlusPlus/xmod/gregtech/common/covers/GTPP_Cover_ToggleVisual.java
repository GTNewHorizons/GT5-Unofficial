package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.xmod.gregtech.common.items.MetaCustomCoverItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class GTPP_Cover_ToggleVisual extends GT_CoverBehavior {

	private static final Map<String, Boolean> sConnectionStateForEntityMap = new ConcurrentHashMap<String, Boolean>();
	private static final Map<String, String> sPrefixMap = new ConcurrentHashMap<String, String>();
		
	public static String generateUniqueKey(byte aSide, ICoverable aEntity) {
		try {
			BlockPos aPos = new BlockPos(aEntity.getIGregTechTileEntity(aEntity.getXCoord(), aEntity.getYCoord(), aEntity.getZCoord()));
			ForgeDirection aDir = ForgeDirection.getOrientation(aSide);
			String s = aEntity.getInventoryName()+"."+aPos.getUniqueIdentifier()+aDir.name();
			return s;
		}
		catch (Throwable t) {}		
		XSTR x = new XSTR();
		return "ERROR."+x.getSeed()+x.hashCode()+x.nextDouble()+".ID";
	}

	public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		return true;
	}

	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {		
		return aCoverVariable;
	}

	public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return this.trans("756", "Connectable: ") + getConnectionState(aSide, aTileEntity);
	}

	public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 0;
	}

	@Override
	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			long aTimer) {
		return aCoverVariable;
	}

	@Override
	public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	@Override
	public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aSide, aTileEntity);
	}

	@Override
	public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return super.alwaysLookConnected(aSide, aCoverID, aCoverVariable, aTileEntity);
	}

	@Override
	public byte getRedstoneInput(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
			ICoverable aTileEntity) {
		if (!getConnectionState(aSide, aTileEntity)) {
			return 0;
		}
		return super.getRedstoneInput(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity);
	}

	@Override
	public void placeCover(byte aSide, ItemStack aCover, ICoverable aTileEntity) {
		String aKey = generateUniqueKey(aSide, aTileEntity);
		sPrefixMap.put(aKey, aCover.getUnlocalizedName());
		sConnectionStateForEntityMap.put(aKey, MetaCustomCoverItem.getCoverConnections(aCover));
		super.placeCover(aSide, aCover, aTileEntity);
	}

	@Override
	public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			boolean aForced) {
		sConnectionStateForEntityMap.remove(generateUniqueKey(aSide, aTileEntity));
		return true;
	}
	
	public static boolean getConnectionState(byte aSide, ICoverable aTile) {
		return sConnectionStateForEntityMap.get(generateUniqueKey(aSide, aTile));
	}
}