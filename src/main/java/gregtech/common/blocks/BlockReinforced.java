package gregtech.common.blocks;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
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
import gregtech.common.entity.EntityPowderBarrelPrimed;

public class BlockReinforced extends GTGenericBlock {

    public BlockReinforced(String name) {
        super(ItemStorage.class, name, new MaterialReinforced());
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
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Powder Barrel");
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
        BlockDispenser.dispenseBehaviorRegistry
            .putObject(ItemList.Block_Powderbarrel.getItem(), new BehaviorDefaultDispenseItem() {

                @Override
                protected ItemStack dispenseStack(IBlockSource dispenser, ItemStack dispensedItem) {
                    if (dispensedItem.getItemDamage() != 5) return super.dispenseStack(dispenser, dispensedItem);
                    EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
                    World world = dispenser.getWorld();
                    int x = dispenser.getXInt() + enumfacing.getFrontOffsetX();
                    int y = dispenser.getYInt() + enumfacing.getFrontOffsetY();
                    int z = dispenser.getZInt() + enumfacing.getFrontOffsetZ();

                    EntityPowderBarrelPrimed primedBarrel = new EntityPowderBarrelPrimed(
                        world,
                        x + 0.5F,
                        y + 0.5F,
                        z + 0.5F,
                        null);
                    world.spawnEntityInWorld(primedBarrel);
                    new WorldSpawnedEventBuilder.SoundAtEntityEventBuilder().setPitch(1f)
                        .setVolume(1f)
                        .setIdentifier(SoundResource.GAME_TNT_PRIMED)
                        .setEntity(primedBarrel)
                        .setWorld(world)
                        .run();

                    dispensedItem.stackSize--;
                    return dispensedItem;
                }
            });

        EntityRegistry
            .registerModEntity(EntityPowderBarrelPrimed.class, "PowderBarrelPrimed", 0, GTMod.GT, 64, 10, true);
    }

    @Override
    public String getHarvestTool(int meta) {
        return switch (meta) {
            case 4, 5, 6, 7 -> "axe";
            case 2 -> "wrench";
            default -> "pickaxe";
        };
    }

    @Override
    public int getHarvestLevel(int meta) {
        return switch (meta) {
            case 4, 5, 6, 7 -> 1;
            case 2 -> 2;
            case 1, 3, 9 -> 5;
            case 10, 11 -> 7;
            default -> 4;
        };
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return switch (meta) {
            case 0 -> Textures.BlockIcons.BLOCK_BRONZEPREIN.getIcon();
            case 1 -> Textures.BlockIcons.BLOCK_IRREIN.getIcon();
            case 2 -> Textures.BlockIcons.BLOCK_PLASCRETE.getIcon();
            case 3 -> Textures.BlockIcons.BLOCK_TSREIN.getIcon();
            case 4, 6, 7 -> Blocks.coal_block.getIcon(0, 0);
            case 5 -> Textures.BlockIcons.BLOCK_POWDER.getIcon();
            case 8 -> Textures.BlockIcons.BLOCK_STEELPREIN.getIcon();
            case 9 -> Textures.BlockIcons.BLOCK_TITANIUMPREIN.getIcon();
            case 10 -> Textures.BlockIcons.BLOCK_NAQUADAHPREIN.getIcon();
            case 11 -> Textures.BlockIcons.BLOCK_NEUTRONIUMPREIN.getIcon();
            case 12 -> Textures.BlockIcons.BLOCK_DEEP_DARK_RAW.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        if (world == null) {
            return 0.0F;
        }
        if (world.isAirBlock(x, y, z)) {
            return 0.0F;
        }
        int meta = world.getBlockMetadata(x, y, z);
        return switch (meta) {
            case 0 -> 60.0F;
            case 1 -> 400.0F;
            case 2 -> 5.0F;
            case 3 -> 250.0F;
            case 4, 5, 6, 7 -> 0.5F;
            case 8 -> 150.0F;
            case 9 -> 200.0F;
            case 10 -> 500.0F;
            case 11 -> 750.0F;
            default -> Blocks.iron_block.getBlockHardness(world, x, y, z);
        };
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        if (world == null) {
            return 0.0F;
        }
        int meta = world.getBlockMetadata(x, y, z);
        return switch (meta) {
            case 0 -> 150.0F;
            case 1 -> 600.0F;
            case 2 -> 6.0F;
            case 3 -> 400.0F;
            case 4, 6, 7 -> 8.0F;
            case 5 -> 1.0F;
            case 8 -> 200.0F;
            case 9 -> 300.0F;
            case 10 -> 1000.0F;
            case 11 -> 2500.0F;
            default -> super.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
        };
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
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
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
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        if (meta == 4) {
            this.dropBlockAsItem(world, x, y, z, new ItemStack(Items.coal, XSTR_INSTANCE.nextInt(2) + 1, 1));
        } else {
            super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, fortune);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        if (!world.isRemote && world.getBlockMetadata(x, y, z) == 5) {
            EntityPowderBarrelPrimed primedBarrel = new EntityPowderBarrelPrimed(
                world,
                x + 0.5F,
                y + 0.5F,
                z + 0.5F,
                player);
            world.spawnEntityInWorld(primedBarrel);
            new WorldSpawnedEventBuilder.SoundAtEntityEventBuilder().setPitch(1f)
                .setVolume(1f)
                .setIdentifier(SoundResource.GAME_TNT_PRIMED)
                .setEntity(primedBarrel)
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
            EntityPowderBarrelPrimed primedBarrel = new EntityPowderBarrelPrimed(
                world,
                x + 0.5F,
                y + 0.5F,
                z + 0.5F,
                explosion.getExplosivePlacedBy());
            primedBarrel.fuse = (world.rand.nextInt(primedBarrel.fuse / 4) + primedBarrel.fuse / 8);
            world.spawnEntityInWorld(primedBarrel);
        }
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int ordinalSide,
        float xOffset, float yOffset, float zOffset) {
        if ((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem()
            .getItem() instanceof ItemFlintAndSteel) && world.getBlockMetadata(x, y, z) == 5) {
            removedByPlayer(world, player, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, ordinalSide, xOffset, yOffset, zOffset);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List<ItemStack> list) {
        for (int i = 0; i < 16; i++) {
            ItemStack stack = new ItemStack(item, 1, i);
            if (!stack.getDisplayName()
                .contains(".name")) list.add(stack);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return !(entity instanceof EntityWither);
    }
}
