package gregtech.api.enums.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.OreDictItemStack;

/// An oredict wildcard such as `plateAnyIron`. Every member material's part also registers under the wildcard's
/// name, so a recipe naming it accepts any member. Each wildcard is a MaterialLib [Family] of its members. Its
/// [#representative] is the member that recycling data counts the wildcard as, and the part a recipe shows first.
///
/// A wildcard is not a material. Its family carries no shapes or properties, since members inherit family
/// properties and a material may belong to several wildcards.
public enum Wildcard {

    AnyIron("Iron", "Iron", "PigIron", "CastIron"),
    AnyCopper("Copper", "Copper", "AnnealedCopper"),
    AnyCarbon("Carbon", "Carbon", "Coal", "Charcoal", "Lignite"),
    AnyRubber("Rubber", "Rubber", "StyreneButadieneRubber", "Silicone"),
    AnySyntheticRubber("Silicone", "StyreneButadieneRubber", "Silicone");

    private final String representativeName;
    private final String[] memberNames;
    private Family family;
    private Material representative;

    Wildcard(String representativeName, String... memberNames) {
        this.representativeName = representativeName;
        this.memberNames = memberNames;
    }

    /// Declares each wildcard's family. Members are named by key, so this may run before or after the members.
    public static void registerFamilies() {
        for (Wildcard wildcard : values()) {
            var builder = MaterialLibAPI.newFamily("gregtech", wildcard.name());
            for (String member : wildcard.memberNames) builder = builder.addMaterial("gregtech", member);
            wildcard.family = builder.build();
        }
    }

    /// The member materials. Only available once MaterialLib has resolved its registry.
    public Set<Material> members() {
        return family.getMaterials();
    }

    /// The member that stands for this wildcard in compositions and displays.
    public Material representative() {
        if (representative == null) representative = MaterialLibAPI.getMaterial("gregtech", representativeName);
        return representative;
    }

    /// The oredict name of `prefix` for this wildcard, such as `plateAnyIron`.
    public String oreDictName(OrePrefixes prefix) {
        return prefix.oreDictName(name());
    }

    /// The crafting ingredient for `prefix` of any member: stringifies to [#oreDictName] and counts as the
    /// [#representative]'s part in reversible recipes' compositions.
    public ItemData ingredient(OrePrefixes prefix) {
        return new ItemData(prefix, representative(), oreDictName(prefix));
    }

    /// A machine-recipe input of `amount` of `prefix` that accepts any member.
    public OreDictItemStack input(OrePrefixes prefix, int amount) {
        return new OreDictItemStack(oreDictName(prefix), amount);
    }

    /// The representative of the wildcard named `name`, such as `AnySyntheticRubber`, or null when no wildcard has
    /// that name.
    public static @Nullable Material representativeNamed(String name) {
        for (Wildcard wildcard : values()) {
            if (wildcard.name()
                .equals(name)) return wildcard.representative();
        }
        return null;
    }

    /// The wildcards `material` is a member of.
    public static List<Wildcard> containing(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<Wildcard> result = new ArrayList<>(1);
        for (Wildcard wildcard : values()) {
            if (material.getFamilies()
                .contains(wildcard.family)) result.add(wildcard);
        }
        return result;
    }

    /// The representative's part for a wildcard oredict name such as `plateAnyIron`, or null when
    /// `oreName` names no wildcard.
    public static @Nullable ItemStack representativeStack(String oreName) {
        for (Wildcard wildcard : values()) {
            String name = wildcard.name();
            if (!oreName.endsWith(name)) continue;
            OrePrefixes prefix = OrePrefixes.getPrefix(oreName.substring(0, oreName.length() - name.length()));
            if (prefix != null) return MaterialParts.stack(prefix, wildcard.representative(), 1);
        }
        return null;
    }
}
