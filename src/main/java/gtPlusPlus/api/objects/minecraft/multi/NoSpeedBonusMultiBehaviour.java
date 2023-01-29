package gtPlusPlus.api.objects.minecraft.multi;

import net.minecraft.item.ItemStack;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NoSpeedBonusMultiBehaviour extends SpecialMultiBehaviour {

    public NoSpeedBonusMultiBehaviour() {
        // Used by other mods which may wish to not obtain bonus outputs on their Sifting or Maceration recipes.
    }

    @Override
    public ItemStack getTriggerItem() {
        return GregtechItemList.Chip_MultiNerf_NoSpeedBonus.get(1);
    }

    @Override
    public String getTriggerItemTooltip() {
        return "Prevents speed bonuses on GT++ multiblocks when used";
    }

    @Override
    public int getSpeedBonusPercent() {
        return 0;
    }
}
