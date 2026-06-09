package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.ColoredBlockContainer;

public class BehaviourSprayColorRemover extends BehaviourSprayColor {

    public BehaviourSprayColorRemover(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses) {
        super(aEmpty, aUsed, aFull, aUses);
        this.tooltip = () -> StatCollector.translateToLocal("gt.behaviour.paintspray.solvent.tooltip");
    }

    @Override
    protected boolean colorize(World aWorld, int aX, int aY, int aZ, ForgeDirection side, EntityPlayer player) {
        return ColoredBlockContainer.getInstance(player, aX, aY, aZ, side)
            .removeColor();
    }

}
