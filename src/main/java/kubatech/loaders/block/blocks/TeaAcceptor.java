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

package kubatech.loaders.block.blocks;

import java.util.List;

import kubatech.loaders.block.BlockProxy;
import kubatech.loaders.block.IProxyTileEntityProvider;
import kubatech.tileentity.TeaAcceptorTile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TeaAcceptor extends BlockProxy implements IProxyTileEntityProvider {

    public TeaAcceptor() {
        super("tea_acceptor", "tea_acceptor", "blank");
    }

    @Override
    public TileEntity createTileEntity(World world) {
        return new TeaAcceptorTile();
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (world.isRemote) return;
        if (!(player instanceof EntityPlayerMP)) return;
        ((TeaAcceptorTile) world.getTileEntity(x, y, z)).setTeaOwner(player.getPersistentID());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add("Accepts Tea items and adds them to your network");
        tooltipList.add("Can accept up to 10 stacks per tick");
    }

    @Override
    public float getResistance() {
        return 999999999999.f;
    }
}
