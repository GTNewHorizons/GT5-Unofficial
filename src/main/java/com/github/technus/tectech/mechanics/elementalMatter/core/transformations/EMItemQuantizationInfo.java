package com.github.technus.tectech.mechanics.elementalMatter.core.transformations;

import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Tec on 23.05.2017.
 */
public class EMItemQuantizationInfo {
    private final ItemStack in;
    private final boolean skipNBT;
    private final IEMStack out;

    public EMItemQuantizationInfo(ItemStack itemStackIn, boolean skipNBT, IEMStack emOut) {
        in = itemStackIn;
        out = emOut;
        this.skipNBT = skipNBT;
    }

    public EMItemQuantizationInfo(OrePrefixes prefix, Materials material, int amount, boolean skipNBT, IEMStack emOut) {
        in = GT_OreDictUnificator.get(prefix, material, amount);
        out = emOut;
        this.skipNBT = skipNBT;
    }

    public ItemStack input() {
        return in.copy();
    }

    public IEMStack output() {
        return out.clone();
    }

    @Override
    public int hashCode() {
        return (GameRegistry.findUniqueIdentifierFor(in.getItem()) + ":" + in.getUnlocalizedName() + ':'
                        + in.getItemDamage())
                .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EMItemQuantizationInfo) {
            // alias
            ItemStack stack = ((EMItemQuantizationInfo) obj).in;
            if (!in.getUnlocalizedName().equals(((EMItemQuantizationInfo) obj).in.getUnlocalizedName())) {
                return false;
            }

            if (!GameRegistry.findUniqueIdentifierFor(in.getItem())
                    .equals(GameRegistry.findUniqueIdentifierFor(((EMItemQuantizationInfo) obj).in.getItem()))) {
                return false;
            }

            if (in.getItemDamage() != OreDictionary.WILDCARD_VALUE
                    && stack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
                if (in.getItemDamage() != stack.getItemDamage()) {
                    return false;
                }
            }
            return skipNBT || ItemStack.areItemStackTagsEqual(in, stack);
        }
        return false;
    }
}
