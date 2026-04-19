package gregtech.api.items.armor.behaviors;

import org.jetbrains.annotations.NotNull;

import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;

public class SpaceSuitBehavior implements IArmorBehavior {

    public static final SpaceSuitBehavior INSTANCE = new SpaceSuitBehavior();

    protected SpaceSuitBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.SpaceSuit;
    }

    @Override
    public boolean protectsAgainst(@NotNull ArmorContext context, Hazard hazard) {
        return hazard == Hazard.SPACE;
    }
}
