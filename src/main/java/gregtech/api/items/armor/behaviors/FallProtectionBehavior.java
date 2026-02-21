package gregtech.api.items.armor.behaviors;

public class FallProtectionBehavior implements IArmorBehavior {

    public static final FallProtectionBehavior INSTANCE = new FallProtectionBehavior();

    protected FallProtectionBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.FallProtection;
    }
}
