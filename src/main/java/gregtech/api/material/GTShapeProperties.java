package gregtech.api.material;

import java.util.List;
import java.util.Set;

import com.ruling_0.materiallib.api.Property;

import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.objects.MaterialStack;

/// Typed property keys GregTech attaches to MaterialLib shapes, holding the per-form data a form knows about
/// itself independently of the material taking it: a plate is one ingot of whatever it is made of.
///
/// [ShapeData] declares the values, and every shape-backed
/// [gregtech.api.enums.OrePrefixes] takes them from here at load, so this is the only place the 88 of them
/// are written. The 220 prefixes no shape serves declare their own on the prefix.
///
/// The keys mirror the prefix accessors one for one, and their defaults match the
/// [gregtech.api.enums.OrePrefixBuilder] defaults, so a shape that declares nothing reads what an undeclared
/// prefix would.
public class GTShapeProperties {

    /// Units of material in one item of this shape, in 3628800ths of a unit
    /// ([gregtech.api.enums.OrePrefixes#getMaterialAmount]). This is what [gregtech.api.objects.ItemData] turns
    /// into a [MaterialStack], and therefore the number every reverse recipe divides by.
    public static final Property<Long> MATERIAL_AMOUNT = Property.of("gregtech", "materialAmount", -1L);

    /// The second material an item of this shape is partly made of, contributed as a byproduct to the reverse
    /// recipes ([gregtech.api.enums.OrePrefixes#mSecondaryMaterial]).
    public static final Property<MaterialStack> SECONDARY_MATERIAL = Property.of("gregtech", "secondaryMaterial");

    /// Index into a material's [gregtech.api.enums.TextureSet] for this shape's icon
    /// ([gregtech.api.enums.OrePrefixes#getTextureIndex]); see [gregtech.api.enums.OrePrefixTextureID].
    public static final Property<Integer> TEXTURE_INDEX = Property.of("gregtech", "textureIndex", -1);

    public static final Property<Integer> DEFAULT_STACK_SIZE = Property.of("gregtech", "defaultStackSize", 64);

    /// The material generation groups a material must belong to for this shape's item to be generated for it
    /// ([gregtech.api.enums.OrePrefixes#getGenerationFlags]). Mirrors [GTMaterialProperties#GENERATION_FLAGS]
    /// on the other side: the material declares which groups it is in, the shape which groups it accepts, and
    /// an item exists where the two overlap.
    public static final Property<Set<GTMaterialGenerationFlag>> GENERATION_FLAGS = Property
        .of("gregtech", "generationFlags", Set.of());

    /// Damage dealt per tick while an item of this shape is held, added to the material's own
    /// ([gregtech.api.enums.OrePrefixes#mHeatDamage]). Negative is frost damage.
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage", 0.0F);

    /// The Thaumcraft aspects an item of this shape carries before the material's own are added.
    public static final Property<List<TC_AspectStack>> ASPECTS = Property.of("gregtech", "aspects");

    /// The display-name format, with `%s` standing for the material name (e.g. `"%s Gear"`).
    public static final Property<String> LOCAL_NAME_FORMAT = Property.of("gregtech", "localNameFormat");

    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "unifiable", false);
    public static final Property<Boolean> MATERIAL_BASED = Property.of("gregtech", "materialBased", false);
    public static final Property<Boolean> SELF_REFERENCING = Property.of("gregtech", "selfReferencing", false);
    public static final Property<Boolean> CONTAINER = Property.of("gregtech", "container", false);
    public static final Property<Boolean> RECYCLABLE = Property.of("gregtech", "recyclable", false);
    public static final Property<Boolean> ENCHANTABLE = Property.of("gregtech", "enchantable", false);
    public static final Property<Boolean> SKIP_ACTIVE_UNIFICATION = Property
        .of("gregtech", "skipActiveUnification", false);

    private GTShapeProperties() {}
}
