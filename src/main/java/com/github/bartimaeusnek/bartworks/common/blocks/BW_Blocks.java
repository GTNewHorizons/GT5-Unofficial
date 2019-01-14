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

package com.github.bartimaeusnek.bartworks.common.blocks;

import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BW_Blocks extends Block {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;
    protected String[] textureNames;
    protected String name;

    public BW_Blocks(String name, String[] texture) {
        super(Material.anvil);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.name = name;
        this.textureNames = texture;
        this.setCreativeTab(MainMod.GT2);
    }

    public BW_Blocks(String name, String[] texture, CreativeTabs tabs) {
        super(Material.anvil);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.name = name;
        this.textureNames = texture;
        this.setCreativeTab(tabs);
    }

    public BW_Blocks(String name, String[] texture, CreativeTabs tabs, Material material) {
        super(material);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.name = name;
        this.textureNames = texture;
        this.setCreativeTab(tabs);
    }

    @Override
    public int damageDropped(final int meta) {
        return meta;
    }

    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < textureNames.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < texture.length ? texture[meta] : texture[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        texture = new IIcon[textureNames.length];
        for (int i = 0; i < textureNames.length; i++) {
            texture[i] = par1IconRegister.registerIcon(textureNames[i]);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return name;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }
}