/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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

package kubatech.loaders.item.items;

import java.util.List;

import kubatech.loaders.item.ItemProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Tea extends ItemProxy {

    private final int heal;
    private final float saturation;

    public Tea(String unlocalizedName, int heal, float saturation) {
        super("tea." + unlocalizedName, "tea/" + unlocalizedName);
        this.heal = heal;
        this.saturation = saturation;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add("Tea");
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entity) {
        entity.setItemInUse(stack, getMaxItemUseDuration());
        return stack;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer entity) {
        if (!entity.capabilities.isCreativeMode) --stack.stackSize;
        entity.getFoodStats()
            .addStats(heal, saturation);
        world.playSoundAtEntity(entity, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return stack;
    }

    @Override
    public int getMaxItemUseDuration() {
        return 32;
    }
}
