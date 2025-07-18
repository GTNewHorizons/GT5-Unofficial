package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.EntityAccessor;

@Mixin(Entity.class)
public class EntityMixin implements EntityAccessor {

    @Shadow
    protected boolean isImmuneToFire;

    @Override
    public void gt5u$setImmuneToFire(boolean b) {
        this.isImmuneToFire = b;
    }
}
