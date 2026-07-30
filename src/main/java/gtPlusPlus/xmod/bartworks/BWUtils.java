package gtPlusPlus.xmod.bartworks;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.LegacyWerkstoffIndex;
import gregtech.api.material.MaterialParts;

public class BWUtils {

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, short werkstoffID, int amount) {
        Material material = LegacyWerkstoffIndex.get(werkstoffID);
        if (material == null) return null;
        return MaterialParts.stack(orePrefixes, material, amount);
    }
}
