package gregtech.api.items.armor.behaviors;

// Marker behavior so other augments can distinguish VectoredJetpack from the base Jetpack tier, since both
// JetpackBehavior instances report BehaviorName.Jetpack.
public class VectoredJetpackBehavior implements IArmorBehavior {

    public static final VectoredJetpackBehavior INSTANCE = new VectoredJetpackBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.VectoredJetpack;
    }
}
