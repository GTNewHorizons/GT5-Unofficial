package gregtech.api.items;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.GregTechAPI;

public class ItemCoolantCell extends GTGenericItem {

    protected final int heatStorage;

    public ItemCoolantCell(String aUnlocalized, String aEnglish, int aMaxStore) {
        super(aUnlocalized, aEnglish, null);
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
        setNoRepair();
        this.heatStorage = aMaxStore;
        this.setCreativeTab(GregTechAPI.TAB_GREGTECH);
    }

    protected static int getHeatOfStack(ItemStack aStack) {
        return ItemStackNBT.getInteger(aStack, "heat");
    }

    protected void setHeatForStack(ItemStack aStack, int aHeat) {
        ItemStackNBT.setInteger(aStack, "heat", aHeat);
        if (this.heatStorage > 0) {
            double heatRatio = (double) aHeat / (double) this.heatStorage;
            int damage = (int) (aStack.getMaxDamage() * heatRatio);
            if (damage >= aStack.getMaxDamage()) {
                damage = aStack.getMaxDamage() - 1;
            }
            aStack.setItemDamage(damage);
        }
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        int rHeat = getHeatOfStack(aStack) * 10 / this.heatStorage;
        EnumChatFormatting color = switch (rHeat) {
            case 0 -> EnumChatFormatting.BLUE;
            case 1, 2 -> EnumChatFormatting.GREEN;
            case 3, 4, 5, 6 -> EnumChatFormatting.YELLOW;
            case 7, 8 -> EnumChatFormatting.RED;
            default -> EnumChatFormatting.DARK_RED;
        };
        aList.add(
            EnumChatFormatting.WHITE
                + translateToLocalFormatted("gt.item.desc.stored_heat", "" + color + getHeatOfStack(aStack)));
        if (getControlTagOfStack(aStack) == 1) {
            aList.add(translateToLocal("ic2.reactoritem.heatwarning.line1"));
            aList.add(translateToLocal("ic2.reactoritem.heatwarning.line2"));
        }
    }

    public int getControlTagOfStack(ItemStack stack) {
        return ItemStackNBT.getInteger(stack, "tag");
    }

    public void setControlTagOfStack(ItemStack stack, int tag) {
        ItemStackNBT.setInteger(stack, "tag", tag);
    }
}
