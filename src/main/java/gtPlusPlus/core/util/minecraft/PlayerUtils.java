package gtPlusPlus.core.util.minecraft;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils {

    public static List<EntityPlayerMP> getOnlinePlayers() {
        return MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;
    }

    public static boolean isRealPlayer(EntityLivingBase entity) {
        return entity instanceof EntityPlayer p && !p.getClass()
            .getName()
            .contains("Fake");
    }
}
