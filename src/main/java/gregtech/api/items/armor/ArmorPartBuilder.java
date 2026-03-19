package gregtech.api.items.armor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.util.StatCollector;

import gregtech.api.items.armor.MechArmorAugmentRegistries.ArmorType;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.util.GTUtility;

@SuppressWarnings("unchecked")
public abstract class ArmorPartBuilder<Self extends ArmorPartBuilder<Self>> {

    private boolean finished = false;

    /// The part name. Used for localization and controlling which parts are present on armor. Should not be changed
    /// once set.
    private String id;
    private String itemId;
    /// The behaviors that will be activated by this part
    private Collection<IArmorBehavior> providedBehaviors = Collections.emptyList();
    /// The behaviors that are required for this part to be installed
    private Collection<BehaviorName> requiredBehaviors = Collections.emptyList();
    /// The behaviors that prevent this part from being installed
    private Collection<BehaviorName> incompatibleBehaviors = Collections.emptyList();
    /// The armor types this part can fit into
    private Collection<ArmorType> allowedArmorTypes = Arrays.asList(ArmorType.values());

    protected void onMutated() {
        if (finished) {
            throw new IllegalStateException("Cannot modify part builder after it has been finished: " + this);
        }
    }

    public Self finish() {
        finished = true;

        if (id == null) throw new IllegalStateException("Part must have name set");
        if (allowedArmorTypes.isEmpty()) throw new IllegalStateException("Part must fit into at least one armor type");

        return (Self) this;
    }

    public Self setId(String id) {
        onMutated();
        this.id = id;
        return (Self) this;
    }

    public String getId() {
        return id;
    }

    public Self setItemId(String id) {
        onMutated();
        itemId = id;
        return (Self) this;
    }

    public String getItemId() {
        return itemId;
    }

    public String getLocalizedName() {
        return GTUtility.translate("GT5U.armor.part.name." + id);
    }

    public boolean hasTooltip() {
        return StatCollector.canTranslate("GT5U.armor.part.tooltip." + id);
    }

    public String getTooltip() {
        return GTUtility.translate("GT5U.armor.part.tooltip." + id);
    }

    public Collection<IArmorBehavior> getProvidedBehaviors() {
        return providedBehaviors;
    }

    public Collection<BehaviorName> getRequiredBehaviors() {
        return requiredBehaviors;
    }

    public Collection<BehaviorName> getIncompatibleBehaviors() {
        return incompatibleBehaviors;
    }

    public Self providesBehaviors(Collection<IArmorBehavior> behaviors) {
        onMutated();
        this.providedBehaviors = Collections.unmodifiableCollection(behaviors);
        return (Self) this;
    }

    public Self requiresBehaviors(Collection<BehaviorName> behaviors) {
        onMutated();
        this.requiredBehaviors = Collections.unmodifiableCollection(behaviors);
        return (Self) this;
    }

    public Self incompatibleBehaviors(Collection<BehaviorName> behaviors) {
        onMutated();
        this.incompatibleBehaviors = Collections.unmodifiableCollection(behaviors);
        return (Self) this;
    }

    public Self providesBehaviors(IArmorBehavior... behaviors) {
        onMutated();
        this.providedBehaviors = Collections.unmodifiableCollection(Arrays.asList(behaviors));
        return (Self) this;
    }

    public Self requiresBehaviors(BehaviorName... behaviors) {
        onMutated();
        this.requiredBehaviors = Collections.unmodifiableCollection(Arrays.asList(behaviors));
        return (Self) this;
    }

    public Self incompatibleBehaviors(BehaviorName... behaviors) {
        onMutated();
        this.incompatibleBehaviors = Collections.unmodifiableCollection(Arrays.asList(behaviors));
        return (Self) this;
    }

    public Self fitsInto(ArmorType... armors) {
        onMutated();
        this.allowedArmorTypes = Collections.unmodifiableCollection(Arrays.asList(armors));
        return (Self) this;
    }

    public Collection<ArmorType> getAllowedArmorTypes() {
        return allowedArmorTypes;
    }
}
