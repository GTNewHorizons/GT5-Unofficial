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
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.blockstate.properties.IntegerBlockProperty;
import com.gtnewhorizon.gtnhlib.blockstate.registry.BlockPropertyRegistry;

import bartworks.client.textures.PrefixTextureLinker;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.BoundedTextureCache;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTRendererBlock;

public abstract class BWMetaGeneratedBlocks extends Block implements IBlockWithTextures {

    public static final IntegerBlockProperty MATERIAL_ID = IntegerBlockProperty.meta("material_id", 0xFFFF, 0);
    public final OrePrefixes prefix;
    public final BoundedTextureCache textureCache = new BoundedTextureCache();

    public BWMetaGeneratedBlocks(Material material, String blockName, OrePrefixes types) {
        super(material);
        this.setBlockName(blockName);
        this.setHardness(5.0F);
        this.setResistance(5.0F);
        this.setBlockTextureName("stone");
        this.setCreativeTab(metaTab);
        this.prefix = types;
        BlockPropertyRegistry.registerProperty(this, MATERIAL_ID);
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
        ITexture[][] cached = textureCache.get(meta);
        if (cached != null) return cached;
        return cacheTextures(meta);
    }

    private synchronized ITexture[][] cacheTextures(int meta) {
        // Another render thread may have populated the cache while this thread waited for the monitor.
        ITexture[][] cached = textureCache.get(meta);
        if (cached != null) return cached;

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

        cached = new ITexture[][] { texture, texture, texture, texture, texture, texture };
        textureCache.put(meta, cached);
        return cached;
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
        return (short) MATERIAL_ID.getIntValue(aWorld, aX, aY, aZ);
    }

    @Override
    public int damageDropped(int metadata) {
        return (short) metadata;
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

    @Override
    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        ArrayList<ItemStack> drops = new ArrayList<>(1);
        short materialId = (short) aMeta;
        drops.add(materialId > 0 ? new ItemStack(this, 1, materialId) : new ItemStack(Blocks.cobblestone));
        return drops;
    }
}
