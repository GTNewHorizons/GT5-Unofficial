package gregtech.api.interfaces.tileentity;

import java.util.List;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;

/**
 * Interface to provide localization for meta pipe entity with a united format.
 */
public interface ILocalizedMetaPipeEntity {

    /**
     * the material of GT.
     *
     * @apiNote if this returns <code>null</code>, you need rewrite the <code>getLocalizedName()</code>.
     */
    Materials getMaterial();

    /**
     * the key of the format, it should contain "%s".
     *
     * @apiNote if this returns <code>null</code>, you need rewrite the <code>getLocalizedName()</code>.
     */
    String getPrefixKey();

    /**
     * if you want to use a different name instead of material's localized name, e.g. Polybenzimidazole - PBI.
     */
    default String getMaterialNewNameKey() {
        return null;
    }

    /**
     * Get the localized name of the meta pipe entity.
     */
    default String getLocalizedName() {
        if (getPrefixKey() != null && getMaterial() != null) {
            if (getMaterialNewNameKey() == null) {
                return StatCollector.translateToLocalFormatted(getPrefixKey(), getMaterial().getLocalizedName());
            }
            return StatCollector
                .translateToLocalFormatted(getPrefixKey(), StatCollector.translateToLocal(getMaterialNewNameKey()));
        }
        return "Unnamed with ILocalizedMetaPipeEntity";
    }

    /**
     * Do not add the Material Tooltip when this returns <code>true</code>.
     */
    default boolean isNotAddMaterialTooltip() {
        return false;
    }

    /**
     * Add material tooltip when {@link #isNotAddMaterialTooltip()} returns <code>false</false>.
     * 
     * @param desc The list of tooltip (addInformation).
     */
    default void addMaterialTooltip(List<String> desc) {
        if (isNotAddMaterialTooltip()) return;
        if (getMaterial() == null) return;
        getMaterial().addTooltips(desc);
    }
}
