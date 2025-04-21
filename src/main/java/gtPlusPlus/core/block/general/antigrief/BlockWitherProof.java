package gtPlusPlus.core.block.general.antigrief;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

public class BlockWitherProof extends Block {

    public BlockWitherProof() {
        super(Material.redstoneLight);
        this.setBlockName(Utils.sanitizeString("blockBlackGate"));
        this.setBlockTextureName(GTPlusPlus.ID + ":" + "blockFrameGt");
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        this.setHardness(-1F);
        this.setResistance(5000.0F);
        this.setHarvestLevel("pickaxe", 3);
        this.setStepSound(soundTypeMetal);
        GameRegistry.registerBlock(this, Utils.sanitizeString("blockBlackGate"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iIcon) {
        this.blockIcon = iIcon.registerIcon(GTPlusPlus.ID + ":" + "blockFrameGt");
    }

    @Override
    public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
        // prevent from being destroyed by wither and nukes.
    }

    @Override
    public void onBlockDestroyedByExplosion(final World worldIn, final int x, final int y, final int z,
        final Explosion explosionIn) {}

    @Override
    public boolean canDropFromExplosion(final Explosion explosionIn) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z,
        final Entity entity) {
        if ((entity == null) || !entity.isEntityAlive()) {
            return false;
        }
        if (entity instanceof IBossDisplayData) {
            return false;
        } else {
            return super.canEntityDestroy(world, x, y, z, entity);
        }
    }

    // Colour Handling
    private static final int mWitherColour = Utils.rgbtoHexValue(32, 32, 32);

    @Override
    public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        return mWitherColour;
    }

    @Override
    public int getRenderColor(final int aMeta) {
        return mWitherColour;
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
        final int z) {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World worldIn, int x, int y, int z) {
        if (aPlayer instanceof EntityPlayerMP) {
            return 1f;
        }
        return -1f;
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return Float.MAX_VALUE;
    }

    @Override
    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(worldIn, x, y, z, player);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if ((entity == null) || !entity.isEntityAlive()) {
            return;
        }
        if (!(entity instanceof IBossDisplayData)) {
            super.onEntityCollidedWithBlock(world, x, y, z, entity);
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(worldIn, player, x, y, z, meta);
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        if (player instanceof EntityPlayerMP) {
            return true;
        }
        return super.canHarvestBlock(player, meta);
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        return Float.MAX_VALUE;
    }
}
