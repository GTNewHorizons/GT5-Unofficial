package gregtech.common.blocks;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.WorldSpawnedEventBuilder;

public class BlockReinforced extends GTGenericBlock {

    public BlockReinforced(String aName) {
        super(ItemStorage.class, aName, new MaterialReinforced());
        for (int i = 0; i < 16; i++) {
            Textures.BlockIcons.casingTexturePages[1][i + 80] = TextureFactory.of(this, i);
        }
        setStepSound(soundTypeStone);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Bronzeplate Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Iridium Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Plascrete Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Tungstensteel Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Brittle Charcoal");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Powderbarrel");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Solid Super Fuel");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Magic Solid Super Fuel");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Steel Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Titanium Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Naquadah Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Neutronium Reinforced Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Raw Deep Dark Portal Block");
        ItemList.Block_BronzePlate.set(
            new ItemStack(
                this.setHardness(60.0f)
                    .setResistance(150.0f),
                1,
                0));
        ItemList.Block_IridiumTungstensteel.set(
            new ItemStack(
                this.setHardness(400.0f)
                    .setResistance(600.0f),
                1,
                1));
        ItemList.Block_Plascrete.set(
            new ItemStack(
                this.setHardness(5.0f)
                    .setResistance(6.0f),
                1,
                2));
        ItemList.Block_TungstenSteelReinforced.set(
            new ItemStack(
                this.setHardness(250.0f)
                    .setResistance(400.0f),
                1,
                3));
        ItemList.Block_BrittleCharcoal.set(
            new ItemStack(
                this.setHardness(0.5f)
                    .setResistance(8.0f),
                1,
                4));
        ItemList.Block_Powderbarrel.set(
            new ItemStack(
                this.setHardness(2.5f)
                    .setResistance(2.0f),
                1,
                5));
        ItemList.Block_SSFUEL.set(
            new ItemStack(
                this.setHardness(2.5f)
                    .setResistance(2.0f),
                1,
                6));
        ItemList.Block_MSSFUEL.set(
            new ItemStack(
                this.setHardness(2.5f)
                    .setResistance(2.0f),
                1,
                7));
        ItemList.Block_SteelPlate.set(
            new ItemStack(
                this.setHardness(150.0f)
                    .setResistance(200.0f),
                1,
                8));
        ItemList.Block_TitaniumPlate.set(
            new ItemStack(
                this.setHardness(200.0f)
                    .setResistance(300.0f),
                1,
                9));
        ItemList.Block_NaquadahPlate.set(
            new ItemStack(
                this.setHardness(500.0f)
                    .setResistance(1000.0f),
                1,
                10));
        ItemList.Block_NeutroniumPlate.set(
            new ItemStack(
                this.setHardness(750.0f)
                    .setResistance(2500.0f),
                1,
                11));
        ItemList.Block_BedrockiumCompressed.set(
            new ItemStack(
                this.setHardness(1500.0f)
                    .setResistance(5000.0f),
                1,
                12));
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.coal, 1, 1),
            new Object[] { ItemList.Block_BrittleCharcoal.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Block_Powderbarrel.get(1L),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "WSW", "GGG", "WGW", 'W', OrePrefixes.plate.get(Materials.Wood), 'G',
                new ItemStack(Items.gunpowder, 1), 'S', new ItemStack(Items.string, 1) });
    }

    @Override
    public String getHarvestTool(int aMeta) {
        if (aMeta == 5 || aMeta == 4 || aMeta == 6 || aMeta == 7) return "axe";
        if (aMeta == 2) return "wrench";
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        if (aMeta == 4 || aMeta == 5 || aMeta == 6 || aMeta == 7) return 1;
        if (aMeta == 2) return 2;
        if (aMeta == 9 || aMeta == 3 || aMeta == 1) return 5;
        if (aMeta == 10 || aMeta == 11) return 7;
        return 4;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0 -> {
                    return Textures.BlockIcons.BLOCK_BRONZEPREIN.getIcon();
                }
                case 1 -> {
                    return Textures.BlockIcons.BLOCK_IRREIN.getIcon();
                }
                case 2 -> {
                    return Textures.BlockIcons.BLOCK_PLASCRETE.getIcon();
                }
                case 3 -> {
                    return Textures.BlockIcons.BLOCK_TSREIN.getIcon();
                }
                case 4, 6, 7 -> {
                    return Blocks.coal_block.getIcon(0, 0);
                }
                case 5 -> {
                    return Textures.BlockIcons.COVER_WOOD_PLATE.getIcon();
                }
                case 8 -> {
                    return Textures.BlockIcons.BLOCK_STEELPREIN.getIcon();
                }
                case 9 -> {
                    return Textures.BlockIcons.BLOCK_TITANIUMPREIN.getIcon();
                }
                case 10 -> {
                    return Textures.BlockIcons.BLOCK_NAQUADAHPREIN.getIcon();
                }
                case 11 -> {
                    return Textures.BlockIcons.BLOCK_NEUTRONIUMPREIN.getIcon();
                }
                case 12 -> {
                    return Textures.BlockIcons.BLOCK_DEEP_DARK_RAW.getIcon();
                }
            }
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        if (aWorld == null) {
            return 0.0F;
        }
        if (aWorld.isAirBlock(aX, aY, aZ)) {
            return 0.0F;
        }
        int tMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        if (tMeta == 0) {
            return 60.0F;
        }
        if (tMeta == 1) {
            return 400.0F;
        }
        if (tMeta == 2) {
            return 5.0F;
        }
        if (tMeta == 3) {
            return 250.0F;
        }
        if (tMeta == 4 || tMeta == 5 || tMeta == 6 || tMeta == 7) {
            return 0.5F;
        }
        if (tMeta == 8) {
            return 150.0F;
        }
        if (tMeta == 9) {
            return 200.0F;
        }
        if (tMeta == 10) {
            return 500.0F;
        }
        if (tMeta == 11) {
            return 750.0F;
        }
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        if (world == null) {
            return 0.0F;
        }
        int tMeta = world.getBlockMetadata(x, y, z);
        if (tMeta == 0) {
            return 150.0F;
        }
        if (tMeta == 1) {
            return 600.0F;
        }
        if (tMeta == 2) {
            return 6.0F;
        }
        if (tMeta == 3) {
            return 400.0F;
        }
        if (tMeta == 4 || tMeta == 6 || tMeta == 7) {
            return 8.0F;
        }
        if (tMeta == 5) {
            return 1.0F;
        }
        if (tMeta == 8) {
            return 200.0F;
        }
        if (tMeta == 9) {
            return 300.0F;
        }
        if (tMeta == 10) {
            return 1000.0F;
        }
        if (tMeta == 11) {
            return 2500.0F;
        }
        return super.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    public String getUnlocalizedName() {
        return this.mUnlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.mUnlocalizedName + ".name");
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public void dropBlockAsItemWithChance(World aWorld, int aX, int aY, int aZ, int aMetadata, float chance,
        int aFortune) {
        if (aMetadata == 4) {
            this.dropBlockAsItem(aWorld, aX, aY, aZ, new ItemStack(Items.coal, XSTR_INSTANCE.nextInt(2) + 1, 1));
        } else {
            super.dropBlockAsItemWithChance(aWorld, aX, aY, aZ, aMetadata, chance, aFortune);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        if (!world.isRemote && world.getBlockMetadata(x, y, z) == 5) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, x + 0.5F, y + 0.5F, z + 0.5F, player);
            world.spawnEntityInWorld(entitytntprimed);
            new WorldSpawnedEventBuilder.SoundAtEntityEventBuilder().setPitch(1f)
                .setVolume(1f)
                .setIdentifier(SoundResource.GAME_TNT_PRIMED)
                .setEntity(entitytntprimed)
                .setWorld(world)
                .run();
            world.setBlockToAir(x, y, z);
            return false;
        }
        return super.removedByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (world.isBlockIndirectlyGettingPowered(x, y, z) && world.getBlockMetadata(x, y, z) == 5) {
            removedByPlayer(world, null, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) && world.getBlockMetadata(x, y, z) == 5) {
            removedByPlayer(world, null, x, y, z);
        }
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        if (!world.isRemote && world.getBlockMetadata(x, y, z) == 5) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(
                world,
                x + 0.5F,
                y + 0.5F,
                z + 0.5F,
                explosion.getExplosivePlacedBy());
            entitytntprimed.fuse = (world.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8);
            world.spawnEntityInWorld(entitytntprimed);
        }
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int ordinalSide,
        float xOffset, float yOffset, float zOffset) {
        if ((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem()
            .getItem() == Items.flint_and_steel) && world.getBlockMetadata(x, y, z) == 5) {
            removedByPlayer(world, player, x, y, z);

            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, ordinalSide, xOffset, yOffset, zOffset);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        for (int i = 0; i < 16; i++) {
            ItemStack aStack = new ItemStack(aItem, 1, i);
            if (!aStack.getDisplayName()
                .contains(".name")) aList.add(aStack);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return !(entity instanceof EntityWither);
    }
}
