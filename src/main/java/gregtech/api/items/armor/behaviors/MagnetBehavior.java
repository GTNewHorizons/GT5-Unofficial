package gregtech.api.items.armor.behaviors;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class MagnetBehavior implements IArmorBehavior {

    public static final MagnetBehavior INSTANCE = new MagnetBehavior();

    private static final double RANGE = 8.0;
    private static final double EU_PER_TICK = 8;

    protected MagnetBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Magnet;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;
        context.toggleBehavior(BehaviorName.Magnet);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;
        if (!context.isBehaviorActive(BehaviorName.Magnet)) return;

        EntityPlayer player = context.getPlayer();
        World world = context.getWorld();
        AxisAlignedBB box = player.boundingBox.expand(RANGE, RANGE, RANGE);
        List<?> items = world.getEntitiesWithinAABB(EntityItem.class, box);
        List<?> orbs = world.getEntitiesWithinAABB(EntityXPOrb.class, box);
        if (items.isEmpty() && orbs.isEmpty()) return;
        if (!context.drainEnergy(EU_PER_TICK)) return;

        for (Object o : items) {
            EntityItem item = (EntityItem) o;
            if (item.isDead) continue;
            if (item.getEntityData()
                .getBoolean("PreventRemoteMovement")) continue;
            item.delayBeforeCanPickup = 0;
            moveToPlayer(item, player);
            item.onCollideWithPlayer(player);
        }
        for (Object o : orbs) {
            EntityXPOrb orb = (EntityXPOrb) o;
            if (orb.isDead) continue;
            moveToPlayer(orb, player);
            orb.onCollideWithPlayer(player);
        }
    }

    private static void moveToPlayer(Entity entity, EntityPlayer player) {
        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;
        entity.setPosition(player.posX, player.posY, player.posZ);
    }
}
