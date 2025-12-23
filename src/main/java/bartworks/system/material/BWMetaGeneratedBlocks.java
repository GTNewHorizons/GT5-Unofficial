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

import static bartworks.system.material.BWMetaGeneratedItems.metaTab;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import bartworks.client.textures.PrefixTextureLinker;
import bartworks.common.blocks.BWTileEntityContainer;
import bartworks.util.BWUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTRendererBlock;

public abstract class BWMetaGeneratedBlocks extends BWTileEntityContainer implements IBlockWithTextures {

    public static ThreadLocal<TileEntityMetaGeneratedBlock> mTemporaryTileEntity = new ThreadLocal<>();
    protected final OrePrefixes prefix;

    public BWMetaGeneratedBlocks(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName,
        OrePrefixes types) {
        super(p_i45386_1_, tileEntity, blockName);
        this.setHardness(5.0F);
        this.setResistance(5.0F);
        this.setBlockTextureName("stone");
        this.setCreativeTab(metaTab);
        this.prefix = types;
        Werkstoff.werkstoffHashSet.forEach(this::doRegistrationStuff);
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
    }

    @Override
    public @Nullable ITexture[][] getTextures(int meta) {
        ITexture baseTexture = null;

        Werkstoff mat = Werkstoff.werkstoffHashMap.get((short) meta);

        if (mat != null) {
            TextureSet set = mat.getTexSet();

            IIconContainer baseIcon = PrefixTextureLinker.texMapBlocks.getOrDefault(prefix, Collections.emptyMap())
                .get(set);

            if (baseIcon == null) {
                baseIcon = TextureSet.SET_NONE.mTextures[OrePrefixes.block.getTextureIndex()];
            }

            baseTexture = TextureFactory.of(baseIcon, mat.getRGBA());
        }

        if (baseTexture == null) {
            baseTexture = TextureFactory.of(TextureSet.SET_NONE.mTextures[OrePrefixes.block.getTextureIndex()]);
        }

        ITexture[] texture = new ITexture[] { TextureFactory.of(Blocks.iron_block), baseTexture };

        return new ITexture[][] { texture, texture, texture, texture, texture, texture };
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        // Waste some time to allow the TE to be set, do not use thread sleep here, it doesnt allow for nanoseconds.
        // This will just waste a few cpu cycles to allow the TE to be set
        BWUtil.shortSleep(0);
    }

    @SideOnly(Side.CLIENT)
    public final TileEntityMetaGeneratedBlock getProperTileEntityForRendering() {
        return (TileEntityMetaGeneratedBlock) this.createNewTileEntity(null, 0);
    }

    protected abstract void doRegistrationStuff(Werkstoff w);

    @Override
    public String getHarvestTool(int metadata) {
        return "pickaxe";
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof TileEntityMetaGeneratedBlock metaTE) {
            return metaTE.mMetaData;
        }
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return ((ITileEntityProvider) this).createNewTileEntity(world, metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tTileEntity = world.getTileEntity(x, y, z);
        if (tTileEntity instanceof TileEntityMetaGeneratedBlock metaTE) {
            mTemporaryTileEntity.set(metaTE);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof TileEntityMetaGeneratedBlock metaTE) {
            return metaTE.getDrops(aFortune);
        }
        return mTemporaryTileEntity.get() == null ? new ArrayList<>()
            : mTemporaryTileEntity.get()
                .getDrops(aFortune);
    }
}
