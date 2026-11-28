package gregtech.api.ec.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnSolderingToolClickComponent extends IComponent {

    ComponentKey.ListKey<IOnSolderingToolClickComponent> KEY = ComponentKey.list();

    boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool);
}
