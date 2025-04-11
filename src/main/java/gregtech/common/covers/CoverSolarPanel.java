package gregtech.common.covers;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;

public class CoverSolarPanel extends CoverLegacyData {

    private final int mVoltage;

    public CoverSolarPanel(CoverContext context, int aVoltage) {
        super(context);
        this.mVoltage = aVoltage;
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if (coverSide != ForgeDirection.UP) {
            coverData = 0;
            return;
        }
        int coverState = this.coverData & 0x3;
        int coverNum = this.coverData >> 2;
        if (aTimer % 100L == 0L) {
            if (coverable.getWorld()
                .isThundering()) {
                coverData = coverable.getBiome().rainfall > 0.0F && coverable.getSkyAtSide(coverSide)
                    ? Math.min(20, coverNum) << 2
                    : coverNum << 2;
                return;
            } else {
                if (coverable.getWorld()
                    .isRaining() && coverable.getBiome().rainfall > 0.0F) { // really rains
                    if (coverable.getSkyAtSide(coverSide)) coverNum = Math.min(30, coverNum);
                    if (coverable.getWorld().skylightSubtracted >= 4) {
                        if (coverable.getWorld()
                            .isDaytime()) {
                            coverState = 2;
                        } else {
                            coverData = coverNum << 2;
                            return;
                        }
                    }
                } else { // not rains
                    if (coverable.getWorld()
                        .isDaytime()) {
                        coverState = 1;
                    } else {
                        coverState = 2;
                    }
                }
            }
        }
        if (coverState == 1) {
            coverable.injectEnergyUnits(
                ForgeDirection.UNKNOWN,
                ((100L - (long) coverNum) * ((long) this.mVoltage)) / 100L,
                1L);
        }
        if (aTimer % 28800L == 0L && coverNum < 100 && (coverNum > 10 || XSTR_INSTANCE.nextInt(3) == 2)) coverNum++;
        coverData = coverState + (coverNum << 2);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.capabilities.isCreativeMode) {
            GTUtility.sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (coverData >> 2) + "% dirt");
            coverData = coverData & 0x3;
            return true;
        }
        for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
            ItemStack is = aPlayer.inventory.mainInventory[i];
            if (is == null) continue;
            if (is.getUnlocalizedName()
                .equals(new ItemStack(Items.water_bucket).getUnlocalizedName())) {
                aPlayer.inventory.mainInventory[i] = new ItemStack(Items.bucket);
                if (aPlayer.inventoryContainer != null) aPlayer.inventoryContainer.detectAndSendChanges();
                GTUtility.sendChatToPlayer(aPlayer, "Cleaned solar panel from " + (coverData >> 2) + "% dirt");
                coverData = coverData & 0x3;
                return true;
            }
        }
        GTUtility.sendChatToPlayer(aPlayer, "You need water bucket in inventory to clean the panel.");
        return false;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }
}
