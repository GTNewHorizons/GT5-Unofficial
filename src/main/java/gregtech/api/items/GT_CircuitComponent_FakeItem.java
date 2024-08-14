package gregtech.api.items;

public class GT_CircuitComponent_FakeItem extends GT_Generic_Item {

    public GT_CircuitComponent_FakeItem() {
        super("gt.fakecircuitcomponent", "Fake Circuit Component Item", null);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
}
