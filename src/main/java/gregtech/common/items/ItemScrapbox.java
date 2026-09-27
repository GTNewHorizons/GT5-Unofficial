package gregtech.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.api.objects.XSTR;

public class ItemScrapbox extends GTGenericItem {

    private static float weightTotal;

    public ItemScrapbox(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new BehaviorDefaultDispenseItem() {

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                doDispense(
                    source.getWorld(),
                    ScrapDrop.getDrop(),
                    6,
                    EnumFacing.getFront(source.getBlockMetadata()),
                    BlockDispenser.func_149939_a(source));
                --stack.stackSize;
                return stack;
            }
        });
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

        public static void addDrop(float weight, ItemStack drop) {
            possibleDrops.add(new ScrapDrop(weight, drop));
            weightTotal += weight;
        }

        public static ItemStack getDrop() {
            float roll = XSTR.XSTR_INSTANCE.nextFloat() * weightTotal;
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
