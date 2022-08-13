package gregtech.common;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.internal.IGT_Mod;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;

public class GT_GameRuleManager extends GT_Proxy {

    public GT_GameRuleManager() {
        MinecraftForge.EVENT_BUS.register(new GT_GameRuleManager());
    }

    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public boolean isBukkitSide() {
        return false;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return null;
    }

    @Override
    public int addArmor(String aArmorPrefix) {
        return 0;
    }

    @Override
    public void doSonictronSound(ItemStack aStack, World aWorld, double aX, double aY, double aZ) {

    }

    public static class GameRule {
        public static final String WEATHER_CYCLE = "runakaii:doWeatherCycle";
    }

    private static void addRule(GameRules rules, String name, String defaultValue) {
        if (!rules.hasRule(name)) rules.addGameRule(name, defaultValue);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load evt) {
        final GameRules rules = evt.world.getGameRules();
        addRule(rules, GameRule.WEATHER_CYCLE, "true");
    }

//orld.getWorldInfo().isRaining()
//    worldinfo.setRaining(false);

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onPlayerDrops(PlayerDropsEvent event) {
        World world = event.entityPlayer.worldObj;
        final GameRules gameRules = world.getGameRules();
        if (gameRules.getGameRuleBooleanValue(GameRule.WEATHER_CYCLE))
            if (world.isRemote) return;
        final EntityPlayer player = event.entityPlayer;
    }
}
