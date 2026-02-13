package gregtech.api.items.armor.behaviors;

public class JetpackPerfectHoverBehavior implements IArmorBehavior {

    public static JetpackPerfectHoverBehavior INSTANCE = new JetpackPerfectHoverBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.JetpackPerfectHover;
    }
}
