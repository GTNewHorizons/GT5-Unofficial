package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverBehavior;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

// TODO: Figure out what anything in this class is even supposed to do.
public class CoverToggleVisual extends CoverBehavior {

    private static final Map<String, Integer> sConnectionStateForEntityMap = new ConcurrentHashMap<>();
    private static final Map<String, String> sPrefixMap = new ConcurrentHashMap<>();
    private static final int VALUE_OFF = 0;
    private static final int VALUE_ON = 1;

    public CoverToggleVisual(CoverContext context) {
        super(context);
    }

    public static String generateUniqueKey(ForgeDirection side, ICoverable coverable) {
        try {
            if (coverable != null) {
                BlockPos aPos = new BlockPos(
                    coverable
                        .getIGregTechTileEntity(coverable.getXCoord(), coverable.getYCoord(), coverable.getZCoord()));

                return coverable.getInventoryName() + "." + aPos.getUniqueIdentifier() + side.name();
            }
        } catch (Throwable ignored) {}
        XSTR x = new XSTR();
        return "ERROR." + x.getSeed() + x.hashCode() + x.nextDouble() + ".ID";
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        PlayerUtils.messagePlayer(aPlayer, GTUtility.trans("756", "Connectable: ") + getConnectionState());
        return super.onCoverRightClick(aPlayer, aX, aY, aZ);
    }

    public boolean letsEnergyIn() {
        return getConnectionState();
    }

    public boolean letsEnergyOut() {
        return getConnectionState();
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return getConnectionState();
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return getConnectionState();
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return getConnectionState();
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return getConnectionState();
    }

    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public ISerializableObject.LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        int coverDataValue = coverData.get();
        try {
            String aKey = generateUniqueKey(coverSide, coveredTile.get());
            Integer b = sConnectionStateForEntityMap.get(aKey);
            if (b != null && coverDataValue != b) {
                coverDataValue = b;
            }
            if (b == null) {
                b = coverDataValue;
                sConnectionStateForEntityMap.put(aKey, b);
                trySetState(b == VALUE_ON ? VALUE_ON : VALUE_OFF);
            }
        } catch (Throwable ignored) {

        }
        return ISerializableObject.LegacyCoverData.of(coverDataValue);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return getConnectionState();
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return getConnectionState();
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        if (!getConnectionState()) {
            return 0;
        }
        return super.getRedstoneInput(aInputRedstone);
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack coverItem) {
        String aKey = generateUniqueKey(coverSide, coveredTile.get());
        boolean state = getCoverConnections(coverItem);
        sPrefixMap.put(aKey, coverItem.getUnlocalizedName());
        Logger.INFO("Mapping key " + aKey + " to " + state);
        sConnectionStateForEntityMap.put(aKey, state ? VALUE_ON : VALUE_OFF);
        Logger.INFO("Key Value: " + (state ? VALUE_ON : VALUE_OFF));
    }

    @Override
    public void onCoverRemoval() {
        String aKey = generateUniqueKey(coverSide, coveredTile.get());
        sConnectionStateForEntityMap.remove(aKey);
        // Logger.INFO("Unmapping key "+aKey+".");
    }

    public boolean getConnectionState() {
        return coverData.get() == VALUE_ON;
    }

    private void trySetState(int aState) {
        // Try set cover state directly
        if (coveredTile.get() instanceof IGregTechTileEntity gTileEntity) {
            gTileEntity.setCoverDataAtSide(coverSide, new ISerializableObject.LegacyCoverData(aState));
        }
    }

    public static boolean getConnectionState(ForgeDirection side, ICoverable aTile) {
        String aKey = generateUniqueKey(side, aTile);
        return getConnectionState(aKey);
    }

    public static boolean getConnectionState(String aKey) {
        Integer b = sConnectionStateForEntityMap.get(aKey);
        // Logger.INFO("Get State: "+b+" | "+aKey);
        return b != null && b == VALUE_ON;
    }

    public static boolean getCoverConnections(final ItemStack aStack) {
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
