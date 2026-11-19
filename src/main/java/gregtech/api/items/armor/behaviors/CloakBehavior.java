package gregtech.api.items.armor.behaviors;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class CloakBehavior implements IArmorBehavior {

    public static final CloakBehavior INSTANCE = new CloakBehavior();

    private static final double EU_PER_TICK = 8;
    private static final int AGGRO_CHECK_INTERVAL = 10;
    private static final double AGGRO_RANGE = 32.0;

    protected CloakBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Cloak;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.Cloak);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        if (context.isBehaviorActive(BehaviorName.Cloak) && context.drainEnergy(EU_PER_TICK)) {
            EntityPlayer player = context.getPlayer();
            player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 40, 0, true));
            if (context.getWorld()
                .getTotalWorldTime() % AGGRO_CHECK_INTERVAL == 0) {
                dropAggro(player);
            }
        } else {
            removeCloak(context);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        removeCloak(context);
    }

    private static void removeCloak(ArmorContext context) {
        EntityPlayer player = context.getPlayer();
        PotionEffect effect = player.getActivePotionEffect(Potion.invisibility);
        if (effect != null && effect.getIsAmbient()) {
            player.removePotionEffect(Potion.invisibility.id);
        }
    }

    private static void dropAggro(EntityPlayer player) {
        AxisAlignedBB box = player.boundingBox.expand(AGGRO_RANGE, AGGRO_RANGE, AGGRO_RANGE);
        List<EntityLiving> mobs = player.worldObj.getEntitiesWithinAABB(EntityLiving.class, box);
        for (EntityLiving mob : mobs) {
            if (mob.getAITarget() == player) continue;
            if (mob.getAttackTarget() == player) {
                mob.setAttackTarget(null);
            }
            if (mob instanceof EntityCreature creature && creature.getEntityToAttack() == player) {
                creature.setTarget(null);
            }
        }
    }
}
