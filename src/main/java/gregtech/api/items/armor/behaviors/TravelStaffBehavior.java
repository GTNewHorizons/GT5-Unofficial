package gregtech.api.items.armor.behaviors;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.MovementStaffClientState;

// Implemented in MovementStaffClientState.java and MechArmorBase.java
public class TravelStaffBehavior implements IArmorBehavior {

    public static final TravelStaffBehavior INSTANCE = new TravelStaffBehavior();

    protected TravelStaffBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.TravelStaff;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) {
            MovementStaffClientState.updateClientTick(context.getPlayer(), context.getArmorStack());
        }
    }

}
