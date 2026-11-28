package gregtech.api.ec.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnWrenchClickComponent extends IComponent {

    ComponentKey.ListKey<IOnWrenchClickComponent> KEY = ComponentKey.list();

    boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer, float aX,
        float aY, float aZ, ItemStack aTool);
}
