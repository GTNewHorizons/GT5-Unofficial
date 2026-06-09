/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.OrePrefixes;

public class BWMetaGeneratedWerkstoffBlocks extends BWMetaGeneratedBlocks {

    public BWMetaGeneratedWerkstoffBlocks(Material p_i45386_1_, Class<? extends TileEntity> tileEntity,
        String blockName) {
        super(p_i45386_1_, tileEntity, blockName, OrePrefixes.block);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff tMaterial) {}

    @Override
    public String getUnlocalizedName() {
        return "bw.werkstoffblocks.01";
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.iron_block.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side) {
        return Blocks.iron_block.getIcon(0, 0);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs tab, List<ItemStack> aList) {
        Werkstoff.werkstoffHashSet.stream()
            .filter(tMaterial -> tMaterial.hasItemType(OrePrefixes.gem) || tMaterial.hasItemType(OrePrefixes.ingot))
            .map(tMaterial -> new ItemStack(aItem, 1, tMaterial.getmID()))
            .forEach(aList::add);
    }
}
