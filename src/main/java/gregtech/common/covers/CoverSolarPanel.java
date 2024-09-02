package gregtech.common.covers;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;

public class CoverSolarPanel extends CoverBehavior {

    private final int mVoltage;

    public CoverSolarPanel(int aVoltage) {
        this.mVoltage = aVoltage;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (side != ForgeDirection.UP) return 0;
        int coverState = aCoverVariable & 0x3;
        int coverNum = aCoverVariable >> 2;
        if (aTimer % 100L == 0L) {
            if (aTileEntity.getWorld()
                .isThundering()) {
                return aTileEntity.getBiome().rainfall > 0.0F && aTileEntity.getSkyAtSide(side)
                    ? Math.min(20, coverNum) << 2
                    : coverNum << 2;
            } else {
                if (aTileEntity.getWorld()
                    .isRaining() && aTileEntity.getBiome().rainfall > 0.0F) { // really rains
                    if (aTileEntity.getSkyAtSide(side)) coverNum = Math.min(30, coverNum);
                    if (aTileEntity.getWorld().skylightSubtracted >= 4) {
                        if (aTileEntity.getWorld()
                            .isDaytime()) {
                            coverState = 2;
                        } else {
                            return coverNum << 2;
                        }
                    }
                } else { // not rains
                    if (aTileEntity.getWorld()
                        .isDaytime()) {
                        coverState = 1;
                    } else {
                        coverState = 2;
                    }
                }
            }
        }
        if (coverState == 1) {
            aTileEntity.injectEnergyUnits(
                ForgeDirection.UNKNOWN,
                ((100L - (long) coverNum) * ((long) this.mVoltage)) / 100L,
                1L);
        }
        if (aTimer % 28800L == 0L && coverNum < 100 && (coverNum > 10 || XSTR_INSTANCE.nextInt(3) == 2)) coverNum++;
        return coverState + (coverNum << 2);
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        if (aPlayer.capabilities.isCreativeMode) {
            GTUtility.sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (aCoverVariable.get() >> 2) + "% dirt");
            aCoverVariable.set(aCoverVariable.get() & 0x3);
            return true;
        }
        for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
            ItemStack is = aPlayer.inventory.mainInventory[i];
            if (is == null) continue;
            if (is.getUnlocalizedName()
                .equals(new ItemStack(Items.water_bucket).getUnlocalizedName())) {
                aPlayer.inventory.mainInventory[i] = new ItemStack(Items.bucket);
                if (aPlayer.inventoryContainer != null) aPlayer.inventoryContainer.detectAndSendChanges();
                GTUtility
                    .sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (aCoverVariable.get() >> 2) + "% dirt");
                aCoverVariable.set(aCoverVariable.get() & 0x3);
                return true;
            }
        }
        GTUtility.sendChatToPlayer(aPlayer, "You need water bucket in inventory to clean the panel.");
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.capabilities.isCreativeMode) {
            GTUtility.sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (aCoverVariable >> 2) + "% dirt");
            aTileEntity.setCoverDataAtSide(side, (aCoverVariable & 0x3));
            return true;
        }
        for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
            ItemStack is = aPlayer.inventory.mainInventory[i];
            if (is == null) continue;
            if (is.getUnlocalizedName()
                .equals(new ItemStack(Items.water_bucket).getUnlocalizedName())) {
                aPlayer.inventory.mainInventory[i] = new ItemStack(Items.bucket);
                if (aPlayer.inventoryContainer != null) aPlayer.inventoryContainer.detectAndSendChanges();
                GTUtility.sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (aCoverVariable >> 2) + "% dirt");
                aTileEntity.setCoverDataAtSide(side, (aCoverVariable & 0x3));
                return true;
            }
        }
        GTUtility.sendChatToPlayer(aPlayer, "You need water bucket in inventory to clean the panel.");
        return false;
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }
}
