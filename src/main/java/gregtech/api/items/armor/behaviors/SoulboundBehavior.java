package gregtech.api.items.armor.behaviors;

public class SoulboundBehavior implements IArmorBehavior {

    public static final SoulboundBehavior INSTANCE = new SoulboundBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.Soulbound;
    }
}
