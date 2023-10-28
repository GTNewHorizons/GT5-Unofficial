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

package kubatech.loaders.block.kubablock;

import static kubatech.loaders.block.kubablock.KubaBlock.defaultTileEntityUI;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;

import kubatech.Tags;

public class BlockProxy {

    private final String unlocalizedName;
    private final String sideTexturePath;
    private final String topBottomTexturePath;
    private IIcon sideIcon;
    private IIcon topBottomIcon;

    public BlockProxy(String unlocalizedName, String texture) {
        this.unlocalizedName = "kubablock." + unlocalizedName;
        sideTexturePath = topBottomTexturePath = Tags.MODID + ":" + texture;
    }

    public BlockProxy(String unlocalizedName, String sideTexture, String topBottomTexture) {
        this.unlocalizedName = "kubablock." + unlocalizedName;
        sideTexturePath = Tags.MODID + ":" + sideTexture;
        topBottomTexturePath = Tags.MODID + ":" + topBottomTexture;
    }

    public void itemInit(int ID) {}

    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (this instanceof IProxyTileEntityProvider) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof ITileWithModularUI) {
                if (world.isRemote) return true;
                if (te instanceof KubaBlock.IModularUIProvider) ((KubaBlock.IModularUIProvider) te).getUI()
                    .open(player, world, x, y, z);
                else defaultTileEntityUI.open(player, world, x, y, z);
                return true;
            }
        }
        return false;
    }

    public void onBlockPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {}

    public void registerIcon(IIconRegister iconRegister) {
        sideIcon = iconRegister.registerIcon(sideTexturePath);
        if (sideTexturePath.equals(topBottomTexturePath)) topBottomIcon = sideIcon;
        else topBottomIcon = iconRegister.registerIcon(topBottomTexturePath);
    }

    public IIcon getIcon(int side) {
        if (side <= 1) return topBottomIcon;
        else return sideIcon;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public String getDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal(this.unlocalizedName + ".name")
            .trim();
    }

    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {}

    public float getHardness() {
        return 10.f;
    }

    public Material getMaterial() {
        return Material.anvil;
    }

    public float getResistance() {
        return 5.f;
    }
}
