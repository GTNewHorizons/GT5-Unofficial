package kubatech.loaders;

import cpw.mods.fml.common.registry.GameRegistry;
import kubatech.api.enums.ItemList;
import kubatech.loaders.block.KubaBlock;
import kubatech.loaders.block.KubaItemBlock;
import kubatech.loaders.block.blocks.TeaAcceptor;
import kubatech.tileentity.TeaAcceptorTile;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockLoader {

    public static final KubaBlock kubaBlock = new KubaBlock(Material.anvil);
    public static final ItemBlock kubaItemBlock = new KubaItemBlock(kubaBlock);

    public static void registerBlocks() {
        GameRegistry.registerTileEntity(TeaAcceptorTile.class, "KT_TeaAcceptor");
        GameRegistry.registerBlock(kubaBlock, null, "kubablocks");
        GameRegistry.registerItem(kubaItemBlock, "kubablocks");

        ItemList.TeaAcceptor.set(kubaBlock.registerProxyBlock(new TeaAcceptor()));
    }
}
