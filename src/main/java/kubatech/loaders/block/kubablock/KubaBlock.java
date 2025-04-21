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

import static kubatech.kubatech.KT;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.builder.UIBuilder;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;

import kubatech.loaders.BlockLoader;

public class KubaBlock extends Block {

    public static final Function<IModularUIContainerCreator, UIInfo<?, ?>> TileEntityUIFactory = containerConstructor -> UIBuilder
        .of()
        .container((player, world, x, y, z) -> {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof ITileWithModularUI) {
                final UIBuildContext buildContext = new UIBuildContext(player);
                final ModularWindow window = ((ITileWithModularUI) te).createWindow(buildContext);
                return containerConstructor
                    .createUIContainer(new ModularUIContext(buildContext, te::markDirty), window);
            }
            return null;
        })
        .gui(((player, world, x, y, z) -> {
            if (!world.isRemote) return null;
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof ITileWithModularUI) {
                final UIBuildContext buildContext = new UIBuildContext(player);
                final ModularWindow window = ((ITileWithModularUI) te).createWindow(buildContext);
                return new ModularGui(
                    containerConstructor.createUIContainer(new ModularUIContext(buildContext, null), window));
            }
            return null;
        }))
        .build();

    public static final UIInfo<?, ?> defaultTileEntityUI = TileEntityUIFactory.apply(ModularUIContainer::new);

    static final HashMap<Integer, BlockProxy> blocks = new HashMap<>();
    private static int idCounter = 0;

    public KubaBlock(Material materialIn) {
        super(materialIn);
        setCreativeTab(KT);
    }

    public ItemStack registerProxyBlock(BlockProxy block) {
        blocks.put(idCounter, block);
        block.itemInit(idCounter);
        return new ItemStack(BlockLoader.kubaItemBlock, 1, idCounter++);
    }

    private BlockProxy getBlock(int id) {
        return blocks.get(id);
    }

    WeakReference<World> lastAccessor = null;
    int X, Y, Z;

    public void setLastBlockAccess(World accessor, int x, int y, int z) {
        lastAccessor = new WeakReference<>(accessor);
        X = x;
        Y = y;
        Z = z;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return getBlock(meta) instanceof IProxyTileEntityProvider;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (int i = 0; i < blocks.size(); i++) list.add(new ItemStack(itemIn, 1, i));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        blocks.values()
            .forEach(b -> b.registerIcon(reg));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return blocks.get(meta)
            .getIcon(side);
    }

    @Override
    public String getLocalizedName() {
        return "KUBABLOCK";
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (!hasTileEntity(metadata)) return null;
        return ((IProxyTileEntityProvider) getBlock(metadata)).createTileEntity(world);
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        return getBlock(worldIn.getBlockMetadata(x, y, z)).onActivated(worldIn, x, y, z, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        getBlock(itemIn.getItemDamage()).onBlockPlaced(worldIn, x, y, z, placer, itemIn);
    }

    @Override
    public float getBlockHardness(World worldIn, int x, int y, int z) {
        return getBlock(worldIn.getBlockMetadata(x, y, z)).getHardness();
    }

    @Override
    public Material getMaterial() {
        if (lastAccessor == null) return super.getMaterial();
        World world = lastAccessor.get();
        if (world == null) {
            lastAccessor = null;
            return super.getMaterial();
        }
        if (world.getBlock(X, Y, Z) != this) return super.getMaterial();
        return getBlock(world.getBlockMetadata(X, Y, Z)).getMaterial();
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        return getBlock(world.getBlockMetadata(x, y, z)).getResistance();
    }

    @FunctionalInterface
    public interface IModularUIContainerCreator {

        ModularUIContainer createUIContainer(ModularUIContext context, ModularWindow mainWindow);
    }

    @FunctionalInterface
    public interface IModularUIProvider {

        UIInfo<?, ?> getUI();
    }
}
