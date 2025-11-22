package gregtech.api.items.armor.behaviors;

import org.jetbrains.annotations.NotNull;

import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;

public class HazmatBehavior implements IArmorBehavior {

    public static final HazmatBehavior INSTANCE = new HazmatBehavior();

    protected HazmatBehavior() {/**/}

    @Override
    public boolean protectsAgainst(@NotNull ArmorContext context, Hazard hazard) {
        // Protect against non-space and non-temperature hazards
        return switch (hazard) {
            case BIOLOGICAL -> true;
            case FROST -> false;
            case HEAT -> false;
            case RADIOLOGICAL -> true;
            case ELECTRICAL -> true;
            case GAS -> true;
            case SPACE -> false;
        };
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.HazmatProtection;
    }
}
