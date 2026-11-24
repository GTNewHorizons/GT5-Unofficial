package gregtech.api.ec.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnWireCutterClickComponent extends IComponent {

    ComponentKey.ListKey<IOnWireCutterClickComponent> KEY = ComponentKey.list();

    boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool);
}
