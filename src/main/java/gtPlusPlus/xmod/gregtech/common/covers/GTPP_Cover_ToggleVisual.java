package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class GTPP_Cover_ToggleVisual extends GT_CoverBehavior {

    private static final Map<String, Integer> sConnectionStateForEntityMap = new ConcurrentHashMap<>();
    private static final Map<String, String> sPrefixMap = new ConcurrentHashMap<>();
    private static final int VALUE_OFF = 0;
    private static final int VALUE_ON = 1;

    public static String generateUniqueKey(ForgeDirection side, ICoverable aEntity) {
        try {
            BlockPos aPos = new BlockPos(
                    aEntity.getIGregTechTileEntity(aEntity.getXCoord(), aEntity.getYCoord(), aEntity.getZCoord()));

            String s = aEntity.getInventoryName() + "." + aPos.getUniqueIdentifier() + side.name();
            return s;
        } catch (Throwable t) {}
        XSTR x = new XSTR();
        return "ERROR." + x.getSeed() + x.hashCode() + x.nextDouble() + ".ID";
    }

    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        PlayerUtils
                .messagePlayer(aPlayer, GT_Utility.trans("756", "Connectable: ") + getConnectionState(aCoverVariable));
        return super.onCoverRightclick(side, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
    }

    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return super.onCoverScrewdriverclick(side, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
    }

    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
            ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
            ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
            ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
            ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
            ICoverable aTileEntity, long aTimer) {
        try {
            String aKey = generateUniqueKey(side, aTileEntity);
            Integer b = sConnectionStateForEntityMap.get(aKey);
            // Logger.INFO("Val: "+aCoverVariable);
            if (b != null && aCoverVariable != b) {
                aCoverVariable = b;
            }
            if (b == null) {
                b = aCoverVariable;
                sConnectionStateForEntityMap.put(aKey, b);
                trySetState(side, b == VALUE_ON ? VALUE_ON : VALUE_OFF, aTileEntity);
            }
        } catch (Throwable t) {

        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return getConnectionState(aCoverVariable);
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return super.alwaysLookConnected(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public byte getRedstoneInput(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
            ICoverable aTileEntity) {
        if (!getConnectionState(aCoverVariable)) {
            return 0;
        }
        return super.getRedstoneInput(side, aInputRedstone, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public void placeCover(ForgeDirection side, ItemStack aCover, ICoverable aTileEntity) {
        String aKey = generateUniqueKey(side, aTileEntity);
        boolean state = getCoverConnections(aCover);
        sPrefixMap.put(aKey, aCover.getUnlocalizedName());
        Logger.INFO("Mapping key " + aKey + " to " + state);
        sConnectionStateForEntityMap.put(aKey, state ? VALUE_ON : VALUE_OFF);
        Logger.INFO("Key Value: " + (state ? VALUE_ON : VALUE_OFF));
        // Try set cover state directly
        // trySetState(aSide, state ? VALUE_ON : VALUE_OFF, aTileEntity);
        super.placeCover(side, aCover, aTileEntity);
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            boolean aForced) {
        String aKey = generateUniqueKey(side, aTileEntity);
        sConnectionStateForEntityMap.remove(aKey);
        // Logger.INFO("Unmapping key "+aKey+".");
        return true;
    }

    public static boolean getConnectionState(int aCoverVar) {
        return aCoverVar == VALUE_ON;
    }

    private static void trySetState(ForgeDirection side, int aState, ICoverable aTile) {
        // Try set cover state directly
        if (aTile instanceof IGregTechTileEntity gTileEntity) {
            gTileEntity.setCoverDataAtSide(side, new ISerializableObject.LegacyCoverData(aState));
        }
    }

    public static boolean getConnectionState(ForgeDirection side, ICoverable aTile) {
        String aKey = generateUniqueKey(side, aTile);
        return getConnectionState(aKey);
    }

    public static boolean getConnectionState(String aKey) {
        Integer b = sConnectionStateForEntityMap.get(aKey);
        // Logger.INFO("Get State: "+b+" | "+aKey);
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
