package gregtech.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.api.objects.XSTR;

public class ItemScrapbox extends GTGenericItem {

    public ItemScrapbox(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            ItemStack itemStack = ScrapDrop.getDrop();
            if (itemStack != null) {
                player.dropPlayerItemWithRandomChoice(itemStack, false);
            }
        }
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return stack;
    }

    public record ScrapDrop(float weight, ItemStack stack) {

        private static final List<ScrapDrop> possibleDrops = new ArrayList<>();

        public static void addDrop(float chance, ItemStack drop) {
            possibleDrops.add(new ScrapDrop(chance, drop));
        }

        public static ItemStack getDrop() {
            float total = 0f;
            for (ScrapDrop drop : possibleDrops) {
                total += drop.weight();
            }

            float roll = XSTR.XSTR_INSTANCE.nextFloat() * total;
            float cumulative = 0f;
            for (ScrapDrop drop : possibleDrops) {
                cumulative += drop.weight();
                if (roll < cumulative) {
                    return drop.stack()
                        .copy();
                }
            }
            return ItemList.Scrap.get(1);
        }
    }
}
