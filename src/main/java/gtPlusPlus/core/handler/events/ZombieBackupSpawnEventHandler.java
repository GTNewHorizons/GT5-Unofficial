package gtPlusPlus.core.handler.events;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.objects.Logger;
import java.lang.reflect.Field;
import net.minecraftforge.event.entity.living.ZombieEvent;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ZombieBackupSpawnEventHandler {

    /**
     *
     * Do we really need this pathetic mechanic to exist when it doesn't work properly at all?
     * Or , well, maybe you enjoy Zombies spawning IN YOUR FUCKING FACE?!
     *
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onZombieReinforcement(final ZombieEvent.SummonAidEvent event) {
        try {
            try {
                Field mChance = FieldUtils.getDeclaredField(this.getClass(), "summonChance", true);
                FieldUtils.removeFinalModifier(mChance, true);
                mChance.set(this, 0);
            } catch (Throwable t) {
            }
            if (event.attacker != null) {
                // SegmentHelper.getInstance().trackUser(event.attacker.getUniqueID().toString(), "Zombie Backup");
            }
            Logger.WARNING("[Zombie] ZombieEvent.SummonAidEvent.");
            event.setResult(Result.DENY);
        } catch (Throwable t) {
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onZombieReinforcement(final ZombieEvent event) {
        try {
            Logger.WARNING("[Zombie] ZombieEvent.");
            if (event.entity != null) {
                Logger.WARNING("Event Entity: " + event.entity.getCommandSenderName());
            }
            event.setResult(Result.DENY);
        } catch (Throwable t) {

        }
    }
}
