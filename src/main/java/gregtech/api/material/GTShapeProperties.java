package gregtech.api.material;

import java.util.List;

import com.ruling_0.materiallib.api.Property;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTLog;

/// Typed property keys GregTech attaches to MaterialLib shapes, holding the per-form data that used to live
/// only on [OrePrefixes].
///
/// A form's data belongs to the form, not to the material taking it: a plate is one ingot of whatever it is
/// made of. [OrePrefixes] carried it because there was nowhere else to put it, which is what kept the prefix
/// load-bearing for every consumer of that data. These keys are the shape-side home, so a prefix that has a
/// shape reads its values from the shape and the 220 prefixes with no shape keep their own.
///
/// The keys deliberately mirror the prefix accessors one for one, so the move is a change of source rather
/// than of meaning. Their defaults match the [gregtech.api.enums.OrePrefixBuilder] defaults, so a shape that
/// declares nothing reads what an undeclared prefix would.
public class GTShapeProperties {

    /// Units of material in one item of this shape, in 3628800ths of a unit ([OrePrefixes#getMaterialAmount]).
    /// This is what [gregtech.api.objects.ItemData] turns into a [MaterialStack], and therefore the number every
    /// reverse recipe divides by.
    public static final Property<Long> MATERIAL_AMOUNT = Property.of("gregtech", "materialAmount", -1L);

    /// The second material an item of this shape is partly made of, contributed as a byproduct to the reverse
    /// recipes ([OrePrefixes#mSecondaryMaterial]).
    public static final Property<MaterialStack> SECONDARY_MATERIAL = Property.of("gregtech", "secondaryMaterial");

    /// Index into a material's [gregtech.api.enums.TextureSet] for this shape's icon
    /// ([OrePrefixes#getTextureIndex]); see [gregtech.api.enums.OrePrefixTextureID].
    public static final Property<Integer> TEXTURE_INDEX = Property.of("gregtech", "textureIndex", -1);

    public static final Property<Integer> DEFAULT_STACK_SIZE = Property.of("gregtech", "defaultStackSize", 64);

    /// The generation-flag bits a material must carry for this shape's item to be generated for it
    /// ([OrePrefixes#getMaterialGenerationBits]).
    public static final Property<Integer> MATERIAL_GENERATION_BITS = Property
        .of("gregtech", "materialGenerationBits", 0);

    /// Damage dealt per tick while an item of this shape is held, added to the material's own
    /// ([OrePrefixes#mHeatDamage]). Negative is frost damage.
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

    /// Reports every shape whose declared property disagrees with the [OrePrefixes] field it is about to
    /// overwrite.
    ///
    /// Called from `OrePrefixes#hydrateFromShapes` immediately before it copies, which is the only moment both
    /// values exist: afterwards the prefix holds what the shape gave it and a comparison would compare a value
    /// with itself. It catches `scripts/mu/gen_shape_data.py` being re-run against a stale dump, and goes away
    /// with the prefix literals it reads.
    public static void verifyAgainstPrefixes() {
        int checked = 0;
        int mismatches = 0;
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            for (Shape shape : MaterialParts.shapes(prefix)) {
                checked++;
                mismatches += compare(shape, prefix, MATERIAL_AMOUNT, prefix.getMaterialAmount());
                mismatches += compare(shape, prefix, TEXTURE_INDEX, prefix.getTextureIndex());
                mismatches += compare(shape, prefix, DEFAULT_STACK_SIZE, prefix.getDefaultStackSize());
                mismatches += compare(shape, prefix, MATERIAL_GENERATION_BITS, prefix.getMaterialGenerationBits());
                mismatches += compare(shape, prefix, HEAT_DAMAGE, prefix.mHeatDamage);
                mismatches += compare(
                    shape,
                    prefix,
                    LOCAL_NAME_FORMAT,
                    prefix.getMaterialPrefix() + "%s" + prefix.getMaterialPostfix());
                mismatches += compare(shape, prefix, UNIFIABLE, prefix.isUnifiable());
                mismatches += compare(shape, prefix, MATERIAL_BASED, prefix.isMaterialBased());
                mismatches += compare(shape, prefix, SELF_REFERENCING, prefix.isSelfReferencing());
                mismatches += compare(shape, prefix, CONTAINER, prefix.isContainer());
                mismatches += compare(shape, prefix, RECYCLABLE, prefix.isRecyclable());
                mismatches += compare(shape, prefix, ENCHANTABLE, prefix.isEnchantable());
                mismatches += compare(shape, prefix, SKIP_ACTIVE_UNIFICATION, prefix.skipActiveUnification());
                mismatches += compare(shape, prefix, SECONDARY_MATERIAL, prefix.mSecondaryMaterial);
                mismatches += compareAspects(shape, prefix);
            }
        }
        if (mismatches == 0) {
            GTLog.out.println("GTShapeProperties: " + checked + " shape/prefix pairs agree");
        } else {
            GTLog.err.println(
                "GTShapeProperties: " + mismatches
                    + " shape properties disagree with their prefix across "
                    + checked
                    + " pairs; re-run scripts/mu/gen_shape_data.py");
        }
    }

    /// Compared field by field rather than through [java.util.Objects#equals]: [TC_AspectStack] declares no
    /// `equals`, so two lists holding the same aspects would otherwise never match.
    private static int compareAspects(Shape shape, OrePrefixes prefix) {
        List<TC_AspectStack> fromShape = shape.getProperty(ASPECTS);
        List<TC_AspectStack> fromPrefix = prefix.mAspects;
        if (fromShape == null) fromShape = List.of();
        boolean same = fromShape.size() == fromPrefix.size();
        for (int i = 0; same && i < fromShape.size(); i++) {
            same = fromShape.get(i).mAspect == fromPrefix.get(i).mAspect
                && fromShape.get(i).mAmount == fromPrefix.get(i).mAmount;
        }
        if (same) return 0;
        GTLog.err.println(
            "GTShapeProperties: shape " + shape.getName()
                + " has "
                + ASPECTS
                + " = "
                + describe(fromShape)
                + " but prefix "
                + prefix.getName()
                + " declares "
                + describe(fromPrefix));
        return 1;
    }

    private static String describe(List<TC_AspectStack> aspects) {
        StringBuilder text = new StringBuilder("[");
        for (TC_AspectStack aspect : aspects) {
            if (text.length() > 1) text.append(", ");
            text.append(aspect.mAspect)
                .append('x')
                .append(aspect.mAmount);
        }
        return text.append(']')
            .toString();
    }

    private static <T> int compare(Shape shape, OrePrefixes prefix, Property<T> property, T fromPrefix) {
        T fromShape = shape.getProperty(property);
        if (java.util.Objects.equals(fromShape, fromPrefix)) return 0;
        GTLog.err.println(
            "GTShapeProperties: shape " + shape.getName()
                + " has "
                + property
                + " = "
                + fromShape
                + " but prefix "
                + prefix.getName()
                + " declares "
                + fromPrefix);
        return 1;
    }
}
