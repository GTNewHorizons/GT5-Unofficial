package kubatech.api.helpers;

import crazypants.enderio.EnderIO;
import kubatech.api.LoaderReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.IBossDisplayData;

public class EnderIOHelper {
    public static boolean canEntityBeCapturedWithSoulVial(Entity entity, String entityID) {
        if (!LoaderReference.EnderIO) return true;
        if (ReflectionHelper.<Boolean>callMethod(EnderIO.itemSoulVessel, "isBlackListed", false, entityID))
            return false;
        return crazypants.enderio.config.Config.soulVesselCapturesBosses || !(entity instanceof IBossDisplayData);
    }

    public static boolean canEntityBeCapturedWithSoulVial(Entity entity) {
        return canEntityBeCapturedWithSoulVial(entity, EntityList.getEntityString(entity));
    }
}
