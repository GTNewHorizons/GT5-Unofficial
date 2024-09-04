/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.loaders;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import kubatech.api.enums.ItemList;
import kubatech.loaders.block.defc.BlockDEFCCasing;
import kubatech.loaders.block.kubablock.KubaBlock;
import kubatech.loaders.block.kubablock.KubaItemBlock;
import kubatech.loaders.block.kubablock.blocks.BlockTeaAcceptor;
import kubatech.loaders.block.kubablock.blocks.BlockTeaStorage;
import kubatech.tileentity.TeaAcceptorTile;
import kubatech.tileentity.TeaStorageTile;

public class BlockLoader {

    public static final KubaBlock kubaBlock = new KubaBlock(Material.anvil);
    public static final ItemBlock kubaItemBlock = new KubaItemBlock(kubaBlock);
    public static final BlockDEFCCasing defcCasingBlock = new BlockDEFCCasing();

    public static void registerBlocks() {
        GameRegistry.registerTileEntity(TeaAcceptorTile.class, "KT_TeaAcceptor");
        GameRegistry.registerTileEntity(TeaStorageTile.class, "KT_TeaStorage");
        GameRegistry.registerBlock(kubaBlock, null, "kubablocks");
        GameRegistry.registerItem(kubaItemBlock, "kubablocks");

        ItemList.TeaAcceptor.set(kubaBlock.registerProxyBlock(new BlockTeaAcceptor()));
        ItemList.TeaStorage.set(kubaBlock.registerProxyBlock(new BlockTeaStorage()));
    }
}
