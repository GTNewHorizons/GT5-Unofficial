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

package kubatech.loaders.block.kubablock.blocks;

import static kubatech.api.Variables.numberFormat;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import kubatech.loaders.block.kubablock.BlockProxy;
import kubatech.loaders.block.kubablock.IProxyTileEntityProvider;
import kubatech.tileentity.TeaStorageTile;

public class BlockTeaStorage extends BlockProxy implements IProxyTileEntityProvider {

    public BlockTeaStorage() {
        super("tea_storage", "tea_storage");
    }

    @Override
    public TileEntity createTileEntity(World world) {
        return new TeaStorageTile();
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (world.isRemote) return;
        if (!(player instanceof EntityPlayerMP)) return;
        ((TeaStorageTile) world.getTileEntity(x, y, z)).setTeaOwner(player.getPersistentID());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add("Extends Tea Storage by " + EnumChatFormatting.RED + numberFormat.format(Long.MAX_VALUE));
    }

    @Override
    public float getResistance() {
        return 999999999999.f;
    }
}
