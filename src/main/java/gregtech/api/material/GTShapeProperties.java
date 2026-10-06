package gregtech.api.material;

import java.util.List;

import com.ruling_0.materiallib.api.Property;

import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.objects.MaterialStack;

/// Typed property keys GregTech attaches to MaterialLib shapes, holding the data a form carries whatever material
/// takes it. For example, a plate is one ingot of whatever it is made of.
///
/// [gregtech.api.enums.materials.ShapeData] declares the values, and every shape-backed
/// [gregtech.api.enums.OrePrefixes] reads them at load. Prefixes no shape serves declare their own values.
///
/// The keys mirror the prefix accessors one for one, and their defaults match
/// [gregtech.api.enums.OrePrefixBuilder]'s.
public class GTShapeProperties {

    /// Units of material in one item of this shape, in 3628800ths of a unit
    /// ([gregtech.api.enums.OrePrefixes#getMaterialAmount]).
    public static final Property<Long> MATERIAL_AMOUNT = Property.of("gregtech", "materialAmount", -1L);

    /// The second material an item of this shape is partly made of, contributed as a byproduct to the reverse
    /// recipes ([gregtech.api.enums.OrePrefixes#mSecondaryMaterial]).
    public static final Property<MaterialStack> SECONDARY_MATERIAL = Property.of("gregtech", "secondaryMaterial");

    public static final Property<Integer> DEFAULT_STACK_SIZE = Property.of("gregtech", "defaultStackSize", 64);

    /// Damage dealt per tick while an item of this shape is held, added to the material's own
    /// ([gregtech.api.enums.OrePrefixes#mHeatDamage]). Negative is frost damage.
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "shapeHeatDamage", 0.0F);

    /// The Thaumcraft aspects an item of this shape carries before the material's own are added.
    public static final Property<List<TC_AspectStack>> ASPECTS = Property.of("gregtech", "shapeAspects");

    /// The display-name format, with `%s` standing for the material name (e.g. `"%s Gear"`).
    public static final Property<String> LOCAL_NAME_FORMAT = Property.of("gregtech", "localNameFormat");

    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "shapeUnifiable", false);
    public static final Property<Boolean> MATERIAL_BASED = Property.of("gregtech", "materialBased", false);
    public static final Property<Boolean> SELF_REFERENCING = Property.of("gregtech", "selfReferencing", false);
    public static final Property<Boolean> CONTAINER = Property.of("gregtech", "container", false);
    public static final Property<Boolean> RECYCLABLE = Property.of("gregtech", "recyclable", false);
    public static final Property<Boolean> ENCHANTABLE = Property.of("gregtech", "enchantable", false);

    private GTShapeProperties() {}
}
