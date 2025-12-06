package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.INERTIA_CANCELING_KEYBIND;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class InertiaCancelingBehavior implements IArmorBehavior {

    public static InertiaCancelingBehavior INSTANCE = new InertiaCancelingBehavior();

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.getArmorState()
            .toggle(context, BehaviorName.InertiaCanceling);
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.InertiaCanceling;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(INERTIA_CANCELING_KEYBIND);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        if (player.moveForward == 0 && player.moveStrafing == 0 && player.capabilities.isFlying) {
            player.motionX *= 0.5;
            player.motionZ *= 0.5;
        }
    }
}
