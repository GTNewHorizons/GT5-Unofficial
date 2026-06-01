package gregtech.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.GregTechAPI;
import gregtech.api.items.GTGenericItem;

public class ItemRubberTreeTap extends GTGenericItem {

    public ItemRubberTreeTap(String unlocalized, String english, String tooltip, int maxUses) {
        super(unlocalized, english, tooltip);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setMaxStackSize(1);
        setMaxDamage(maxUses);
        setNoRepair();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
        tooltip.add(StatCollector.translateToLocal(getUnlocalizedName() + ".tooltip.0"));
        tooltip.add(StatCollector.translateToLocal(getUnlocalizedName() + ".tooltip.1"));
        tooltip.add(StatCollector.translateToLocal(getUnlocalizedName() + ".tooltip.2"));
    }
}
