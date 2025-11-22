package gregtech.api.items.armor.behaviors;

public class InfiniteEnergyBehavior implements IArmorBehavior {

    public static final InfiniteEnergyBehavior INSTANCE = new InfiniteEnergyBehavior();

    protected InfiniteEnergyBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.InfiniteEnergy;
    }
}
