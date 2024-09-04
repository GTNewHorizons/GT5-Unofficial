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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import bartworks.common.items.BWItemBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class BWItemMetaGeneratedBlock extends BWItemBlocks {

    public BWItemMetaGeneratedBlock(Block par1) {
        super(par1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        Block block = Block.getBlockFromItem(aStack.getItem());
        if (block instanceof BWMetaGeneratedBlocks metaBlock) {
            int aMetaData = aStack.getItemDamage();
            Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) aMetaData);
            if (werkstoff == null) werkstoff = Werkstoff.default_null_Werkstoff;
            return metaBlock.blockTypeLocalizedName.replace("%material", werkstoff.getLocalizedName());
        }
        return GTLanguageManager.getTranslation(this.getUnlocalizedName(aStack) + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        if (!GTUtility.isStackValid(aStack) || aPlayer == null || aStack.getItemDamage() <= 0) {
            return;
        }
        if (aList == null) {
            aList = new ArrayList<>();
        }
        Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) aStack.getItemDamage());
        if (werkstoff != null) {
            String tooltip = werkstoff.getLocalizedToolTip();
            if (!tooltip.isEmpty()) {
                aList.add(tooltip);
            }
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side,
        float hitX, float hitY, float hitZ, int aMeta) {
        short tDamage = (short) this.getDamage(aStack);
        if (tDamage > 0) {
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tDamage, 3)) {
                return false;
            }
            TileEntityMetaGeneratedBlock tTileEntity = (TileEntityMetaGeneratedBlock) aWorld.getTileEntity(aX, aY, aZ);
            tTileEntity.mMetaData = tDamage;
        } else if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3)) return false;
        if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a) {
            this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
            this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
        }
        return true;
    }
}
