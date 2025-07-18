package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.ColoredBlockContainer;
import gregtech.api.util.GTLanguageManager;

public class BehaviourSprayColorRemover extends BehaviourSprayColor {

    public BehaviourSprayColorRemover(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses) {
        super(aEmpty, aUsed, aFull, aUses);
        this.mTooltip = GTLanguageManager
            .addStringLocalization("gt.behaviour.paintspray.solvent.tooltip", "Can remove paint from things");
    }

    @Override
    protected boolean colorize(World aWorld, int aX, int aY, int aZ, ForgeDirection side, EntityPlayer player) {
        return ColoredBlockContainer.getInstance(player, aX, aY, aZ, side)
            .removeColor();
    }
}
