/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material;

import com.github.bartimaeusnek.bartworks.common.items.BW_ItemBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BW_MetaGeneratedOre_Item extends BW_ItemBlocks {

    public BW_MetaGeneratedOre_Item(Block par1) {
        super(par1);
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    public String getItemStackDisplayName(ItemStack aStack) {
        return GT_LanguageManager.getTranslation( getUnlocalizedName(aStack) + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if (!GT_Utility.isStackValid(aStack) || aPlayer == null || aStack.getItemDamage() <= 0)
            return;
        if (aList == null)
            aList = new ArrayList<String>();
        aList.add(Werkstoff.werkstoffHashMap.get((short) aStack.getItemDamage()).getToolTip());
    }

    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta) {
        short tDamage = (short) this.getDamage(aStack);
        if (tDamage > 0) {
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tDamage, 3)) {
                return false;
            }
            BW_MetaGeneratedOreTE tTileEntity = (BW_MetaGeneratedOreTE) aWorld.getTileEntity(aX, aY, aZ);
            tTileEntity.mMetaData = tDamage;
        } else if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3)) {
            return false;
        }
        if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a) {
            this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
            this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
        }
        return true;
    }
}