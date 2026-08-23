package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class InertiaCancelingBehavior implements IArmorBehavior {

    public static final InertiaCancelingBehavior INSTANCE = new InertiaCancelingBehavior();

    private static final double CREATIVE_FLIGHT_DAMPING_FACTOR = 0.5;
    private static final double JETPACK_DAMPING_FACTOR = 0.175;

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
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayer player = context.getPlayer();
        boolean creativeFlight = player.capabilities.isFlying;
        boolean jetpackHovering = context.hasBehavior(BehaviorName.VectoredJetpack)
            && context.isBehaviorActive(BehaviorName.JetpackHover)
            && !player.onGround;

        if (!context.isBehaviorActive(BehaviorName.InertiaCanceling) || player.moveForward != 0
            || player.moveStrafing != 0
            || !(creativeFlight || jetpackHovering)) {
            return;
        }

        double dampingFactor = creativeFlight ? CREATIVE_FLIGHT_DAMPING_FACTOR : JETPACK_DAMPING_FACTOR;
        double retained = 1.0 - dampingFactor;
        player.motionX *= retained;
        player.motionZ *= retained;
    }
}
