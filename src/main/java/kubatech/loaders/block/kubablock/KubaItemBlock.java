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

package kubatech.loaders.block.kubablock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class KubaItemBlock extends ItemBlock {

    public KubaItemBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
        setHasSubtypes(true);
    }

    @Override
    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return KubaBlock.blocks.get(p_77667_1_.getItemDamage())
            .getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return KubaBlock.blocks.get(p_77653_1_.getItemDamage())
            .getDisplayName(p_77653_1_);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean adv) {
        KubaBlock.blocks.get(stack.getItemDamage())
            .addInformation(stack, player, tooltip, adv);
    }

    @Override
    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
}
