package gtPlusPlus.core.block.base;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.render.GTRendererBlock;
import gtPlusPlus.core.item.base.itemblock.ItemBlockOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockBaseOre extends BasicBlock implements IBlockWithTextures {

    private final Material blockMaterial;
    protected static boolean shouldFortune = false;
    protected static boolean shouldSilkTouch = false;

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
            GTOreDictUnificator.registerOre(
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
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.mRenderID;
    }

    @Override
    @Nullable
    public ITexture[][] getTextures(int metadata) {
        ITexture[] layers;

        ITexture aIconSet = TextureFactory
            .of(blockMaterial.getTextureSet().mTextures[OrePrefixes.ore.mTextureIndex], blockMaterial.getRGBA());
        layers = new ITexture[] { StoneType.Stone.getTexture(0), aIconSet };

        return new ITexture[][] { layers, layers, layers, layers, layers, layers };
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            shouldSilkTouch = true;
            super.harvestBlock(worldIn, player, x, y, z, meta);
            if (shouldSilkTouch) {
                shouldSilkTouch = false;
            }
            return;
        }

        if (!(player instanceof FakePlayer)) {
            shouldFortune = true;
        }
        super.harvestBlock(worldIn, player, x, y, z, meta);
        if (shouldFortune) {
            shouldFortune = false;
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (shouldSilkTouch) {
            drops.add(ItemUtils.simpleMetaStack(this, metadata, 1));
        } else {
            switch (GTMod.gregtechproxy.oreDropSystem) {
                case Item -> drops.add(
                    ItemUtils
                        .getItemStackOfAmountFromOreDictNoBroken("oreRaw" + this.blockMaterial.getLocalizedName(), 1));
                case FortuneItem -> {
                    // if shouldFortune and isNatural then get fortune drops
                    // if not shouldFortune or not isNatural then get normal drops
                    // if not shouldFortune and isNatural then get normal drops
                    // if shouldFortune and not isNatural then get normal drops
                    if (shouldFortune && fortune > 0) {
                        int aMinAmount = 1;
                        // Max applicable fortune
                        if (fortune > 3) fortune = 3;
                        long amount = (long) new Random().nextInt(fortune) + aMinAmount;
                        for (int i = 0; i < amount; i++) {
                            drops.add(
                                ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
                                    "oreRaw" + this.blockMaterial.getLocalizedName(),
                                    1));
                        }
                    } else {
                        drops.add(
                            ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
                                "oreRaw" + this.blockMaterial.getLocalizedName(),
                                1));
                    }
                }
                // Unified ore
                case UnifiedBlock -> drops.add(ItemUtils.simpleMetaStack(this, metadata, 1));
                // Per Dimension ore
                case PerDimBlock -> drops.add(ItemUtils.simpleMetaStack(this, metadata, 1));
                // Regular ore
                case Block -> drops.add(ItemUtils.simpleMetaStack(this, metadata, 1));
            }
        }
        return drops;
    }

}
