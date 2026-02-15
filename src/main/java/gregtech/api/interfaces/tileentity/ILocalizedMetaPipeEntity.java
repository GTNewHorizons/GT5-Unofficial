package gregtech.api.interfaces.tileentity;

import java.util.List;

import gregtech.api.interfaces.IOreMaterial;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;

/**
 * Interface to provide localization for meta pipe entity with a united format.
 */
public interface ILocalizedMetaPipeEntity {

    /**
     * Get the GT material of the pipe.
     *
     * @apiNote if this returns <code>null</code>, you need to rewrite {@link #getLocalizedName()}.
     */
    IOreMaterial getMaterial();

    /**
     * Get the language key of the unformatted name.
     * The translation should contain one "%s", which will get formatted with the name of the pipe material.
     *
     * @apiNote if this returns <code>null</code>, you need to rewrite {@link #getLocalizedName()}.
     */
    String getPrefixKey();

    /**
     * Returns the overridden language key if the pipe uses a different name than material's localized name, e.g. PBI
     * instead of Polybenzimidazole,
     * Otherwise, returns null.
     */
    default String getMaterialKeyOverride() {
        return null;
    }

    /**
     * Get the localized name of the meta pipe entity.
     */
    default String getLocalizedName() {
        final String prefixKey = getPrefixKey();
        final IOreMaterial material = getMaterial();
        if (prefixKey != null && material != null) {
            final String materialKeyOverride = getMaterialKeyOverride();
            if (materialKeyOverride == null) {
                return StatCollector.translateToLocalFormatted(prefixKey, material.getLocalizedName());
            }
            return StatCollector.translateToLocalFormatted(prefixKey, StatCollector.translateToLocal(materialKeyOverride));
        }
        return "Unnamed with ILocalizedMetaPipeEntity";
    }

    /**
     * Do not add the Material Tooltip when this returns <code>true</code>.
     */
    default boolean shouldSkipMaterialTooltip() {
        return false;
    }

    /**
     * Add material tooltip when {@link #shouldSkipMaterialTooltip()} returns <code>false</false>.
     *
     * @param desc The list of tooltip (addInformation).
     */
    default void addMaterialTooltip(List<String> desc) {
        if (shouldSkipMaterialTooltip()) return;
        if (getMaterial() == null) return;
        getMaterial().addTooltips(desc);
    }
}
