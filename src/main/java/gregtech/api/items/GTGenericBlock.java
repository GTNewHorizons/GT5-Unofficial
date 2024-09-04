package gregtech.api.items;

import static gregtech.api.enums.GTValues.W;

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
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + W + ".name", "Any Sub Block of this one");
    }
}
