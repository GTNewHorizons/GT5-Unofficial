package gregtech.api.interfaces;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.TextureSet;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import gtPlusPlus.core.material.Material;

/**
 * A temporary material interface to unify the three material systems.
 * Once the new material system is finished, this should be removed and all the code referencing this should be migrated
 * to the new system.
 */
public interface IOreMaterial extends ISubTagContainer {

    /**
     * Add tooltips(mainly chemical formula) for material items。
     *
     * @param list the list parameter in the {@link Item#addInformation} method (for tooltips).
     */
    void addTooltips(List<String> list);

    int getId();

    String getInternalName();

    String getDefaultLocalName();

    default String getLocalizedNameKey() {
        return "Material." + getInternalName().toLowerCase();
    }

    default String getLocalizedName() {
        return StatCollector.translateToLocal(getLocalizedNameKey());
    }

    short[] getRGBA();

    TextureSet getTextureSet();

    List<IStoneType> getValidStones();

    @Nullable
    Materials getGTMaterial();

    boolean generatesPrefix(OrePrefixes prefix);

    default ItemStack getPart(OrePrefixes prefix, int amount) {
        Materials gt = getGTMaterial();

        if (gt != null) {
            return GTOreDictUnificator.get(prefix, gt, amount);
        } else {
            return GTOreDictUnificator.get(prefix.oreDictName(this.getInternalName()), amount);
        }
    }

    /// The molten fluid this material generates, sized to `amount`, or null if it generates none.
    default FluidStack getMolten(long amount) {
        return null;
    }

    /// The plasma fluid this material generates, sized to `amount`, or null if it generates none.
    default FluidStack getPlasma(long amount) {
        return null;
    }

    /// The dust [ItemStack] this material generates, sized to `amount`, or null if it generates none.
    default ItemStack getDust(int amount) {
        return null;
    }

    /// This material's atomic mass, or `0` when it carries no composition data.
    default long getMass() {
        return 0;
    }

    /// This material's proton count, or `0` when it carries no composition data.
    default long getProtons() {
        return 0;
    }

    /// This material's neutron count, or `0` when it carries no composition data.
    default long getNeutrons() {
        return 0;
    }

    /// This material's chemical formula, or the empty string when it carries none.
    default String getChemicalFormula() {
        return "";
    }

    /// This material's chemical-formula tooltip, or the empty string when it carries none.
    default String getChemicalTooltip(boolean showQuestionMarks) {
        return "";
    }

    /// This material's melting point in Kelvin, or `0` when it carries none.
    default int getMeltingPoint() {
        return 0;
    }

    /// This material's composition as [MaterialStack]s, or an empty list when it has no composition.
    default List<MaterialStack> getMaterialList() {
        return Collections.emptyList();
    }

    /// The gas-assisted arc-smelting targets keyed by their required gas [Materials], or an empty map when this
    /// material has none.
    default Map<Materials, Materials> getArcSmeltIntoWithGas() {
        return Collections.emptyMap();
    }

    /// This material's custom item renderer, or null when it has none.
    default GeneratedMaterialRenderer getRenderer() {
        return null;
    }

    /// The heat damage a held item of this material inflicts, or `0` when it inflicts none.
    default float getHeatDamage() {
        return 0;
    }

    /// This material's Thaumcraft aspects, or an empty list when it has none.
    default List<TC_AspectStack> getAspects() {
        return Collections.emptyList();
    }

    /// The enchantment applied to tools made of this material, or null when there is none.
    default Enchantment getToolEnchantment() {
        return null;
    }

    /// The level of [#getToolEnchantment], or `0` when there is none.
    default byte getToolEnchantmentLevel() {
        return 0;
    }

    /// The enchantment applied to armor made of this material, or null when there is none.
    default Enchantment getArmorEnchantment() {
        return null;
    }

    /// The level of [#getArmorEnchantment], or `0` when there is none.
    default byte getArmorEnchantmentLevel() {
        return 0;
    }

    public static IOreMaterial findMaterial(String name) {
        Werkstoff bw = Werkstoff.werkstoffVarNameHashMap.get(name);

        if (bw != null) return bw;

        IOreMaterial gtpp = Material.mMaterialsByName.get(name);

        if (gtpp != null) return gtpp;

        return Materials.getMaterialsMap()
            .get(name);
    }
}
