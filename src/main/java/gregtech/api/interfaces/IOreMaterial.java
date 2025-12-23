package gregtech.api.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.Material;

/**
 * A temporary material interface to unify the three material systems.
 * Once the new material system is finished, this should be removed and all the code referencing this should be migrated
 * to the new system.
 */
public interface IOreMaterial extends ISubTagContainer {

    String getLocalizedName();

    String getLocalizedNameKey();

    void addTooltips(List<String> list);

    int getId();

    String getInternalName();

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
            return GTOreDictUnificator.get(prefix.get(this.getInternalName()), 1);
        }
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
