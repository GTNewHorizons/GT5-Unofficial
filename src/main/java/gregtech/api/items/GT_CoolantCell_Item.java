package gregtech.api.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.GregTech_API;
import ic2.core.util.StackUtil;

public class GT_CoolantCell_Item extends GT_Generic_Item {

    protected final int heatStorage;

    public GT_CoolantCell_Item(String aUnlocalized, String aEnglish, int aMaxStore) {
        super(aUnlocalized, aEnglish, null);
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
        setNoRepair();
        this.heatStorage = aMaxStore;
        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
    }

    protected static int getHeatOfStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        return tNBT.getInteger("heat");
    }

    protected void setHeatForStack(ItemStack aStack, int aHeat) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("heat", aHeat);
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
                + String.format(transItem("000", "Stored Heat: %s"), "" + color + getHeatOfStack(aStack)));
        if (getControlTagOfStack(aStack) == 1) {
            aList.add(StatCollector.translateToLocal("ic2.reactoritem.heatwarning.line1"));
            aList.add(StatCollector.translateToLocal("ic2.reactoritem.heatwarning.line2"));
        }
    }

    public int getControlTagOfStack(ItemStack stack) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        return nbtData.getInteger("tag");
    }

    public void setControlTagOfStack(ItemStack stack, int tag) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        nbtData.setInteger("tag", tag);
    }
}
