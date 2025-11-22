package gregtech.api.items.armor;

import java.util.Collection;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;

// An armor part. May be a frame, core, or augment.
public interface IArmorPart {

    ItemStack getItem(int amount);

    String getId();

    String getItemId();

    String getLocalizedName();

    boolean hasTooltip();

    @NotNull
    EnumRarity getRarity();

    String getTooltip();

    Collection<IArmorBehavior> getProvidedBehaviors();

    Collection<BehaviorName> getRequiredBehaviors();

    Collection<BehaviorName> getIncompatibleBehaviors();
}
