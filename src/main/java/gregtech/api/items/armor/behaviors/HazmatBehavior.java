package gregtech.api.items.armor.behaviors;

import org.jetbrains.annotations.NotNull;

import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;

public class HazmatBehavior implements IArmorBehavior {

    public static final HazmatBehavior INSTANCE = new HazmatBehavior();

    protected HazmatBehavior() {/**/}

    @Override
    public boolean protectsAgainstFully(@NotNull ArmorContext context, Hazard hazard) {
        return hazard != Hazard.SPACE;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.HazmatProtection;
    }
}
