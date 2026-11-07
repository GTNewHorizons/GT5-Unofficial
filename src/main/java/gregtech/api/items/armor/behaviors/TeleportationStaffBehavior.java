package gregtech.api.items.armor.behaviors;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.MovementStaffClientState;

// Implemented in MovementStaffClientState.java and MechArmorBase.java
public class TeleportationStaffBehavior implements IArmorBehavior {

    public static final TeleportationStaffBehavior INSTANCE = new TeleportationStaffBehavior();

    protected TeleportationStaffBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.TeleportationStaff;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) {
            MovementStaffClientState.updateClientTick(context.getPlayer(), context.getArmorStack());
        }
    }

}
