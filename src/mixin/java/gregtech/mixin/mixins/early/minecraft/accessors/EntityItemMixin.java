package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.entity.item.EntityItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.EntityItemAccessor;

@Mixin(EntityItem.class)
public class EntityItemMixin implements EntityItemAccessor {

    @Shadow
    private int health;

    @Override
    public void gt5$setHealth(int health) {
        this.health = health;
    }
}
