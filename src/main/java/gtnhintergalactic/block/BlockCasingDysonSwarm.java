package gtnhintergalactic.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gtnhintergalactic.GTNHIntergalactic;

public class BlockCasingDysonSwarm extends Block {

    private IIcon[][] textures;
    public static final String[] names = { "ReceiverCasing", "ReceiverDish", "DeploymentUnitCasing",
        "DeploymentUnitCore", "DeploymentUnitMagnet", "ControlCasing", "ControlPrimary", "ControlSecondary",
        "ControlToroid", "Floor" };

    public BlockCasingDysonSwarm() {
        super(Material.iron);
        setBlockName("DysonSwarmPart");
        setCreativeTab(GTNHIntergalactic.tab);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        textures = new IIcon[names.length][2];
        for (int i = 0; i < names.length; i++) {
            textures[i][0] = register
                .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":dysonSwarm/" + names[i] + "_Side");
            textures[i][1] = register.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":dysonSwarm/" + names[i]);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return textures[meta % textures.length][side < 2 ? 1 : 0];
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List variants) {
        for (int i = 0; i < names.length; i++) {
            variants.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        if (world.getBlockMetadata(x, y, z) == 9) {
            return 1500.0f;
        }
        return 6.0f;
    }

    @Override
    public float getExplosionResistance(Entity entity) {
        return 6.0f;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == 9) {
            return 800.0f;
        }
        return 1.0f;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
}
