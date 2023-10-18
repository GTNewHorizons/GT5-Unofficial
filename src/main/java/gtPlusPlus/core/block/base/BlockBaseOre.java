package gtPlusPlus.core.block.base;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.interfaces.ITexturedBlock;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.item.base.itemblock.ItemBlockOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_CopiedBlockTexture;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_RenderedTexture;

public class BlockBaseOre extends BasicBlock implements ITexturedBlock {

    private final Material blockMaterial;

    public BlockBaseOre(final Material material, final BlockTypes blockType) {
        super(
                blockType,
                Utils.sanitizeString(material.getUnlocalizedName()),
                net.minecraft.block.material.Material.rock,
                Math.min(Math.max(material.vTier, 1), 6));
        int aMaterialTierForMining = Math.min(Math.max(material.vTier, 1), 6);
        this.blockMaterial = material;
        this.setHardness(1.0f * aMaterialTierForMining);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHarvestLevel("pickaxe", aMaterialTierForMining);
        this.setStepSound(soundTypeStone);
        this.setBlockName("Ore" + Utils.sanitizeString(Utils.sanitizeString(material.getUnlocalizedName())));
        this.setBlockTextureName("stone");
        try {
            GameRegistry.registerBlock(
                    this,
                    ItemBlockOre.class,
                    Utils.sanitizeString("ore" + Utils.sanitizeString(this.blockMaterial.getLocalizedName())));
            GT_OreDictUnificator.registerOre(
                    "ore" + Utils.sanitizeString(this.blockMaterial.getLocalizedName()),
                    ItemUtils.getSimpleStack(this));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
            final int z) {
        return false;
    }

    public Material getMaterialEx() {
        return this.blockMaterial;
    }

    @Override
    public int getRenderType() {
        try {
            if (CustomOreBlockRenderer.INSTANCE != null) {
                return CustomOreBlockRenderer.INSTANCE.mRenderID;
            }
            return super.getRenderType();
        } catch (NullPointerException n) {
            return 0;
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    /**
     * GT Texture Handler
     */

    // .08 compat
    public static IIconContainer[] hiddenTextureArray;

    @Override
    public ITexture[] getTexture(ForgeDirection side) {
        return getTexture(null, side);
    }

    @Override
    public ITexture[] getTexture(Block block, ForgeDirection side) {
        if (this.blockMaterial != null) {
            GTPP_RenderedTexture aIconSet = new GTPP_RenderedTexture(
                    blockMaterial.getTextureSet().mTextures[OrePrefixes.ore.mTextureIndex],
                    this.blockMaterial.getRGBA());
            return new ITexture[] { new GTPP_CopiedBlockTexture(Blocks.stone, 0, 0), aIconSet };
        }

        if (hiddenTextureArray == null) {
            try {
                Field o = ReflectionUtils.getField(Textures.BlockIcons.class, "STONES");
                if (o != null) {
                    hiddenTextureArray = (IIconContainer[]) o.get(Textures.BlockIcons.class);
                }
                if (hiddenTextureArray == null) {
                    hiddenTextureArray = new IIconContainer[6];
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                hiddenTextureArray = new IIconContainer[6];
            }
        }
        return new ITexture[] { new GTPP_RenderedTexture(hiddenTextureArray[0], new short[] { 240, 240, 240, 0 }) };
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}

}
