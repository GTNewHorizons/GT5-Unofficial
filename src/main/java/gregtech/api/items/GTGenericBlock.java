package gregtech.api.items;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GTGenericBlock extends Block {

    protected final String mUnlocalizedName;

    protected GTGenericBlock(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial) {
        super(aMaterial);
        setBlockName(mUnlocalizedName = aName);
        GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
        HashMap<String, String> entry = new HashMap<>();
        entry.put(getUnlocalizedName() + ".32767.name", "Any Sub Block of this");
        LanguageRegistry.instance()
            .injectLanguage("en_US", entry);
    }
}
