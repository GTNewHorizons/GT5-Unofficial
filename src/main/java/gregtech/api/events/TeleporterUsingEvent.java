package gregtech.api.events;

import net.minecraft.entity.Entity;

@cpw.mods.fml.common.eventhandler.Cancelable
public class TeleporterUsingEvent extends net.minecraftforge.event.entity.EntityEvent {

    public final Entity mEntity;
    public final int mTargetX, mTargetY, mTargetZ, mTargetD;
    public final boolean mHasEgg;

    public TeleporterUsingEvent(Entity aEntity, int aTargetX, int aTargetY, int aTargetZ, int aTargetD, boolean aHasEgg) {
        super(aEntity);
        mEntity = aEntity;
        mTargetX = aTargetX;
        mTargetY = aTargetY;
        mTargetZ = aTargetZ;
        mTargetD = aTargetD;
        mHasEgg = aHasEgg;
    }
}