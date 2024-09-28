package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;

@Mixin(EntityPlayerMP.class)
public class EntityPlayerMPMixin implements EntityPlayerMPAccessor {

    @Shadow
    private String translator;

    @Override
    public String gt5u$getTranslator() {
        return this.translator;
    }

}
