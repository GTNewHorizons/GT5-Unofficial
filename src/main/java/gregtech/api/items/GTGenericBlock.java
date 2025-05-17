package gregtech.api.items;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTLanguageManager;

public class GTGenericBlock extends Block {

    protected final String mUnlocalizedName;

    protected GTGenericBlock(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial) {
        super(aMaterial);
        setBlockName(mUnlocalizedName = aName);
        GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
        GTLanguageManager
            .addStringLocalization(getUnlocalizedName() + "." + WILDCARD + ".name", "Any Sub Block of this one");
    }
}
