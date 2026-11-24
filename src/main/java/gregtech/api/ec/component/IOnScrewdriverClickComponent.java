package gregtech.api.ec.component;

import java.util.Comparator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnScrewdriverClickComponent extends IComponent {

    ComponentKey.ListKey<IOnScrewdriverClickComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnScrewdriverClickComponent::getOnScrewdriverClickPriority));

    boolean onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool);

    enum OnScrewdriverClickPriority {

        INPUT_SEPARATION(1000),
        MACHINE_MODE(2000),
        RECIPE_LOCKING(3000);

        public final int priority;

        OnScrewdriverClickPriority(int priority) {
            this.priority = priority;
        }
    }

    int getOnScrewdriverClickPriority();
}
