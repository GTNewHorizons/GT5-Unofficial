package gregtech.api.items.armor.behaviors;

/**
 * This behavior simply adds an NBT tag associated with forestry apiarist's armor. It is up to individual armor
 * to inherit the IArmorApiarist interface and check for this tag.
 */
public class ApiaristBehavior implements IArmorBehavior {

    public static final ApiaristBehavior INSTANCE = new ApiaristBehavior();

    protected ApiaristBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Apiarist;
    }
}
