package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class InertiaCancelingBehavior implements IArmorBehavior {

    public static final InertiaCancelingBehavior INSTANCE = new InertiaCancelingBehavior();
    private static final Set<SyncedKeybind> LISTENED_KEYS = Collections.singleton(
        ArmorActionManager.getAction("inertia_canceling")
            .getKeybind());

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.InertiaCanceling);
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.InertiaCanceling;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return LISTENED_KEYS;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        if (context.isBehaviorActive(BehaviorName.InertiaCanceling) && player.moveForward == 0
            && player.moveStrafing == 0
            && player.capabilities.isFlying) {
            player.motionX *= 0.5;
            player.motionZ *= 0.5;
        }
    }
}
