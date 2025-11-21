package gregtech.api.items.armor;

import java.util.Collection;

import net.minecraft.item.ItemStack;

import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;

public interface IArmorPart {

    ItemStack getItem(int amount);

    String getId();
    String getItemId();

    String getLocalizedName();
    boolean hasTooltip();

    String getTooltip();

    Collection<IArmorBehavior> getProvidedBehaviors();
    Collection<BehaviorName> getRequiredBehaviors();
    Collection<BehaviorName> getIncompatibleBehaviors();
}
