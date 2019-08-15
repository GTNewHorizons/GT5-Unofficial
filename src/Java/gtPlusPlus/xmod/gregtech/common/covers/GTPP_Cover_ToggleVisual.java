package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehavior;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class GTPP_Cover_ToggleVisual extends GT_CoverBehavior {

	private static final Map<String, Integer> sConnectionStateForEntityMap = new ConcurrentHashMap<String, Integer>();
	private static final Map<String, String> sPrefixMap = new ConcurrentHashMap<String, String>();
	private static final int VALUE_OFF = 0;
	private static final int VALUE_ON = 1;

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
		PlayerUtils.messagePlayer(aPlayer, this.trans("756", "Connectable: ") + getConnectionState(aCoverVariable));
		return super.onCoverRightclick(aSide, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
	}

	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {		
		return super.onCoverScrewdriverclick(aSide, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
	}

	public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return this.trans("756", "Connectable: ") + getConnectionState(aCoverVariable);
	}

	public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 1;
	}

	@Override
	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			long aTimer) {		
		try {
			String aKey = generateUniqueKey(aSide, aTileEntity);
			Integer b = sConnectionStateForEntityMap.get(aKey);			
			//Logger.INFO("Val: "+aCoverVariable);
			if (b != null && aCoverVariable != b) {
				aCoverVariable = b;
			}			
			if (b == null) {
				b = aCoverVariable;
				sConnectionStateForEntityMap.put(aKey, b);
				trySetState(aSide, b == VALUE_ON ? VALUE_ON : VALUE_OFF, aTileEntity);
			}
		}
		catch (Throwable t) {

		}
		return aCoverVariable;
	}

	@Override
	public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	@Override
	public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getConnectionState(aCoverVariable);
	}

	@Override
	public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return super.alwaysLookConnected(aSide, aCoverID, aCoverVariable, aTileEntity);
	}

	@Override
	public byte getRedstoneInput(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
			ICoverable aTileEntity) {
		if (!getConnectionState(aCoverVariable)) {
			return 0;
		}
		return super.getRedstoneInput(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity);
	}

	@Override
	public void placeCover(byte aSide, ItemStack aCover, ICoverable aTileEntity) {
		String aKey = generateUniqueKey(aSide, aTileEntity);
		boolean state = getCoverConnections(aCover);
		sPrefixMap.put(aKey, aCover.getUnlocalizedName());
		Logger.INFO("Mapping key "+aKey+" to "+state);
		sConnectionStateForEntityMap.put(aKey, state ? VALUE_ON : VALUE_OFF);	
		Logger.INFO("Key Value: "+(state ? VALUE_ON : VALUE_OFF));	
		//Try set cover state directly
		//trySetState(aSide, state ? VALUE_ON : VALUE_OFF, aTileEntity);		
		super.placeCover(aSide, aCover, aTileEntity);
	}

	@Override
	public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			boolean aForced) {
		String aKey = generateUniqueKey(aSide, aTileEntity);
		sConnectionStateForEntityMap.remove(aKey);
		//Logger.INFO("Unmapping key "+aKey+".");
		return true;
	}

	public static boolean getConnectionState(int aCoverVar) {
		return aCoverVar == VALUE_ON;
	}

	private static final void trySetState(byte aSide, int aState, ICoverable aTile) {
		//Try set cover state directly
		if (aTile instanceof IGregTechTileEntity) {
			IGregTechTileEntity gTileEntity = (IGregTechTileEntity) aTile;
			if (gTileEntity != null) {
				gTileEntity.setCoverDataAtSide(aSide, aState);
			}
		}
	}


	public static boolean getConnectionState(byte aSide, ICoverable aTile) {
		String aKey = generateUniqueKey(aSide, aTile);
		return getConnectionState(aKey);
	}
	
	public static boolean getConnectionState(String aKey) {
		Integer b = sConnectionStateForEntityMap.get(aKey);
		//Logger.INFO("Get State: "+b+" | "+aKey);
		return b != null ? b == VALUE_ON : false;
	}

	public static final boolean getCoverConnections(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("CustomCoverMeta");
			if (aNBT != null) {
				return aNBT.getBoolean("AllowConnections");
			}
		}
		return false;
	}
}