package gregtech.common.items.tools;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone decorator's trowel: places a random block from the player's hotbar, for speckled walls.
 * <p/>
 * See {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
public class ToolTrowelItem extends ToolItemBase {

    public ToolTrowelItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        // The trowel has never had an ore dictionary name; nothing crafts with one.
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, null);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return TrowelActions.place(
            stack,
            player,
            world,
            x,
            y,
            z,
            ordinalSide,
            hitX,
            hitY,
            hitZ,
            () -> player.capabilities.isCreativeMode || spendOneUse(stack));
    }

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        list.add(translateToLocal("gt.behaviour.trowel.tooltip1"));
        list.add(translateToLocal("gt.behaviour.trowel.tooltip2"));
    }
}
