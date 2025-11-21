package gregtech.api.items.armor.behaviors;

public class HazmatBehavior implements IArmorBehavior {

    public static final HazmatBehavior INSTANCE = new HazmatBehavior();

    protected HazmatBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.HazmatProtection;
    }
}
