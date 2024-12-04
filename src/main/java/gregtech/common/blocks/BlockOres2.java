package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IBlockOre;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy.OreDropSystem;
import gregtech.common.render.GTRendererBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockOres2 extends GTGenericBlock implements IBlockOre, IBlockWithTextures {
    
    /** Don't generate ores for these materials. */
    public final static Set<Materials> DISABLED_ORES = new HashSet<>();
    
    public BlockOres2() {
        super(ItemOres2.class, "gt.blockores2", Material.rock);
        setStepSound(soundTypeStone);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_ORES);

        for (StoneType stoneType : StoneType.STONE_TYPES) {
            for (int matId = 0; matId < 1000; matId++) {
                Materials mat = getMaterial(matId);

                if (mat == null) continue;

                GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + getMeta(stoneType, matId, false, false) + ".name", getLocalizedNameFormat(mat));
                GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + getMeta(stoneType, matId, true, false) + ".name", "Small " + getLocalizedNameFormat(mat));

                GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + getMeta(stoneType, matId, false, false) + ".tooltip", mat.getToolTip());
                GTLanguageManager.addStringLocalization(getUnlocalizedName() + "." + getMeta(stoneType, matId, true, false) + ".tooltip", mat.getToolTip());

                if ((mat.mTypes & 0x8) == 0) continue;
                if (DISABLED_ORES.contains(mat)) continue;

                if (stoneType.prefix.mIsUnificatable) {
                    GTOreDictUnificator.set(
                        stoneType.prefix,
                        mat,
                        new ItemStack(this, 1, getMeta(stoneType, matId, false, false)));
                } else {
                    GTOreDictUnificator.registerOre(
                        stoneType.prefix,
                        new ItemStack(this, 1, getMeta(stoneType, matId, false, false)));
                }
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores2";
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int matId = 0; matId < 1000; matId++) {
            for (StoneType stoneType : StoneType.STONE_TYPES) {
                Materials mat = getMaterial(matId);

                if (mat == null) continue;
                if ((mat.mTypes & 0x8) == 0) continue;
                if (DISABLED_ORES.contains(mat)) continue;

                list.add(new ItemStack(this, 1, getMeta(stoneType, matId, false, false)));
            }
        }

        for (int matId = 0; matId < 1000; matId++) {
            for (StoneType stoneType : StoneType.STONE_TYPES) {
                Materials mat = getMaterial(matId);

                if (mat == null) continue;
                if ((mat.mTypes & 0x8) == 0) continue;
                if (DISABLED_ORES.contains(mat)) continue;

                list.add(new ItemStack(this, 1, getMeta(stoneType, matId, true, false)));
            }
        }
    }

    @Override
    public ITexture[][] getTextures(int metadata) {
        StoneType stoneType = getStoneType(metadata);
        int matId = getMaterialId(metadata);
        boolean small = isSmallOre(metadata);

        Materials mat = getMaterial(matId);

        ITexture[] textures;

        if (mat != null) {
            ITexture iTexture = TextureFactory.builder()
                .addIcon(mat.mIconSet.mTextures[small ? OrePrefixes.oreSmall.mTextureIndex : OrePrefixes.ore.mTextureIndex])
                .setRGBA(mat.mRGBa)
                .stdOrient()
                .build();

            textures = new ITexture[] {
                stoneType.getTexture(),
                iTexture
            };
        } else {
            textures = new ITexture[] {
                stoneType.getTexture(),
                TextureFactory.builder()
                    .addIcon(TextureSet.SET_NONE.mTextures[OrePrefixes.ore.mTextureIndex])
                    .stdOrient()
                    .build()
            };
        }

        return new ITexture[][] { textures, textures, textures, textures, textures, textures };
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        StoneType stoneType = getStoneType(meta);

        return switch(stoneType) {
            case Stone -> Blocks.stone.getIcon(side, 0);
            case Netherrack -> Blocks.netherrack.getIcon(side, 0);
            case Endstone -> Blocks.end_stone.getIcon(side, 0);
            case BlackGranite -> BlockIcons.GRANITE_BLACK_STONE.getIcon();
            case RedGranite -> BlockIcons.GRANITE_RED_STONE.getIcon();
            case Marble -> BlockIcons.MARBLE_STONE.getIcon();
            case Basalt -> BlockIcons.BASALT_STONE.getIcon();
        };
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.mRenderID;
    }

    public String getLocalizedNameFormat(Materials material) {
        String base = switch (material.mName) {
            case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> "%material Infused Stone";
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> "%material";
            default -> "%material" + OrePrefixes.ore.mLocalizedMaterialPost;
        };

        if (GTLanguageManager.i18nPlaceholder) {
            return base;
        } else {
            return material.getDefaultLocalizedNameForItem(base);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityDragon) return false;

        return super.canEntityDestroy(world, x, y, z, entity);
    }

    @Override
    public boolean isToolEffective(String type, int metadata) {
        return "pickaxe".equals(type);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "pickaxe";
    }

    @Override
    public int damageDropped(int meta) {
        return withNaturalFlag(meta, false);
    }

    @Override
    public int getHarvestLevel(int meta) {
        Materials mat = getMaterial(meta);

        int smallOreBonus = isSmallOre(meta) ? -1 : 0;

        int harvestLevel = GTMod.gregtechproxy.mChangeHarvestLevels ? GTMod.gregtechproxy.mHarvestLevel[mat.mMetaItemSubID] : mat.mToolQuality;

        return GTUtility.clamp(harvestLevel + smallOreBonus, 0, GTMod.gregtechproxy.mMaxHarvestLevel);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);

        return 1.0F + getHarvestLevel(meta);
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        int meta = world.getBlockMetadata(x, y, z);

        return 1.0F + getHarvestLevel(meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if (!world.isRemote && player.capabilities.isCreativeMode && player.getHeldItem() == null) {
            int meta = world.getBlockMetadata(x, y, z);
            StoneType stone = BlockOres2.getStoneType(meta);
            int matId = BlockOres2.getMaterialId(meta);
            boolean small = BlockOres2.isSmallOre(meta);

            meta = getMeta(stone, matId, small, true);

            world.setBlockMetadataWithNotify(x, y, z, meta, 3);
            GTUtility.sendChatToPlayer(player, "Set ore natural flag to true.");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return !isSmallOre(metadata);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    public static final int SMALL_ORE_META_OFFSET = 16000, NATURAL_ORE_META_OFFSET = 8000;

    public static int getMeta(StoneType stone, int materialId, boolean small, boolean natural) {
        int meta = GTUtility.clamp(materialId, 0, 999);

        meta += stone.offset;

        if (small) meta += SMALL_ORE_META_OFFSET;
        if (natural) meta += NATURAL_ORE_META_OFFSET;

        return meta;
    }

    public static int withNaturalFlag(int meta, boolean natural) {
        StoneType stone = BlockOres2.getStoneType(meta);
        int matId = BlockOres2.getMaterialId(meta);
        boolean small = BlockOres2.isSmallOre(meta);

        return getMeta(stone, matId, small, natural);
    }

    public static ItemStack getStack(StoneType stoneType, Materials material, boolean small, boolean natural, int amount) {
        return new ItemStack(GregTechAPI.sBlockOres2, amount, getMeta(stoneType, material.mMetaItemSubID, small, natural));
    }

    public static void setOre(World world, int x, int y, int z, StoneType stone, Materials material, boolean small, boolean natural) {
        world.setBlock(x, y, z, GregTechAPI.sBlockOres2, getMeta(stone, material.mMetaItemSubID, small, natural), 3);
    }

    public static boolean setOreForWorldGen(World world, int x, int y, int z, Materials material, boolean small) {
        if (y < 0 || y >= world.getActualHeight()) return false;

        StoneType existing = StoneType.fromWorldBlock(world, x, y, z);

        if (existing == null) return false;

        world.setBlock(x, y, z, GregTechAPI.sBlockOres2, getMeta(existing, material.mMetaItemSubID, small, true), 3);

        return true;
    }

    public static boolean setExistingOreStoneType(World world, int x, int y, int z, StoneType newStoneType) {
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockOres2) return false;

        int meta = world.getBlockMetadata(x, y, z);

        int matId = getMaterialId(meta);
        boolean small = isSmallOre(meta);
        boolean natural = isNatural(meta);

        world.setBlock(x, y, z, GregTechAPI.sBlockOres2, getMeta(newStoneType, matId, small, natural), 3);

        return true;
    }

    public static boolean isSmallOre(int meta) {
        return meta >= SMALL_ORE_META_OFFSET;
    }

    public static boolean isNatural(int meta) {
        return (meta % SMALL_ORE_META_OFFSET) >= NATURAL_ORE_META_OFFSET;
    }

    public static int getMaterialId(int meta) {
        return meta % 1000;
    }

    public static Materials getMaterial(int meta) {
        return GregTechAPI.sGeneratedMaterials[getMaterialId(meta)];
    }

    public static StoneType getStoneType(int meta) {
        meta %= SMALL_ORE_META_OFFSET;
        meta %= NATURAL_ORE_META_OFFSET;

        int stoneType = meta / 1000;

        if (stoneType < 0 || stoneType >= StoneType.STONE_TYPES.size()) return null;

        return StoneType.STONE_TYPES.get(stoneType);
    }

    public static enum StoneType {
        Stone(0, OrePrefixes.ore, Materials.Stone),
        Netherrack(1000, OrePrefixes.oreNetherrack, Materials.Netherrack),
        Endstone(2000, OrePrefixes.oreEndstone, Materials.Endstone),
        BlackGranite(3000, OrePrefixes.oreBlackgranite, Materials.GraniteBlack),
        RedGranite(4000, OrePrefixes.oreRedgranite, Materials.GraniteRed),
        Marble(5000, OrePrefixes.oreMarble, Materials.Marble),
        Basalt(6000, OrePrefixes.oreBasalt, Materials.Basalt);

        public static final ImmutableList<StoneType> STONE_TYPES = ImmutableList.copyOf(values());

        public final int offset;
        public final OrePrefixes prefix;
        public final Materials material;

        private StoneType(int offset, OrePrefixes prefix, Materials material) {
            this.offset = offset;
            this.prefix = prefix;
            this.material = material;
        }

        public ITexture getTexture() {
            return switch (this) {
                case Stone -> TextureFactory.of(Blocks.stone);
                case Netherrack -> TextureFactory.of(Blocks.netherrack);
                case Endstone -> TextureFactory.of(Blocks.end_stone);
                case BlackGranite -> TextureFactory.builder().addIcon(BlockIcons.GRANITE_BLACK_STONE).stdOrient().build();
                case RedGranite -> TextureFactory.builder().addIcon(BlockIcons.GRANITE_RED_STONE).stdOrient().build();
                case Marble -> TextureFactory.builder().addIcon(BlockIcons.MARBLE_STONE).stdOrient().build();
                case Basalt -> TextureFactory.builder().addIcon(BlockIcons.BASALT_STONE).stdOrient().build();
            };
        }

        public static StoneType fromWorldBlock(World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);

            if (block.isReplaceableOreGen(world, x, y, z, Blocks.stone)) {
                return StoneType.Stone;
            }

            if (block.isReplaceableOreGen(world, x, y, z, Blocks.netherrack)) {
                return StoneType.Netherrack;
            }

            if (block.isReplaceableOreGen(world, x, y, z, Blocks.end_stone)) {
                return StoneType.Endstone;
            }

            if (block.isReplaceableOreGen(world, x, y, z, GregTechAPI.sBlockGranites)) {
                return meta < 8 ? StoneType.BlackGranite : StoneType.RedGranite;
            }

            if (block.isReplaceableOreGen(world, x, y, z, GregTechAPI.sBlockStones)) {
                return meta < 8 ? StoneType.Marble : StoneType.Basalt;
            }

            return null;
        }

        public static StoneType fromHypotheticalWorldBlock(World world, int x, int y, int z, Block potentialBlock, int potentialMeta) {
            if (potentialBlock.isReplaceableOreGen(world, x, y, z, Blocks.stone)) {
                return StoneType.Stone;
            }

            if (potentialBlock.isReplaceableOreGen(world, x, y, z, Blocks.netherrack)) {
                return StoneType.Netherrack;
            }

            if (potentialBlock.isReplaceableOreGen(world, x, y, z, Blocks.end_stone)) {
                return StoneType.Endstone;
            }

            if (potentialBlock.isReplaceableOreGen(world, x, y, z, GregTechAPI.sBlockGranites)) {
                return potentialMeta < 8 ? StoneType.BlackGranite : StoneType.RedGranite;
            }

            if (potentialBlock.isReplaceableOreGen(world, x, y, z, GregTechAPI.sBlockStones)) {
                return potentialMeta < 8 ? StoneType.Marble : StoneType.Basalt;
            }

            return null;
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        EntityPlayer harvester = this.harvesters.get();

        boolean doFortune = !(harvester instanceof FakePlayer);
        boolean doSilktouch = harvester != null && EnchantmentHelper.getSilkTouchModifier(harvester);

        return (ArrayList<ItemStack>) getDropsForMachine(world, x, y, z, metadata, doSilktouch, doFortune ? fortune : 0);
    }

    @Override
    public List<ItemStack> getDropsForMachine(World world, int x, int y, int z, int metadata, boolean silktouch, int fortune) {
        boolean isNatural = isNatural(metadata);

        return getDrops(world.rand, GTMod.gregtechproxy.oreDropSystem, this, metadata, silktouch, isNatural ? fortune : 0);
    }

    public static List<ItemStack> getPotentialDrops(Materials material, boolean small) {
        if (small) {
            return SmallOreDrops.getDropList(material);
        } else {
            return getBigOreDrops(null, OreDropSystem.Item, GregTechAPI.sBlockOres2, StoneType.Stone, material, false, 0);
        }
    }

    public static ArrayList<ItemStack> getDrops(Random random, OreDropSystem oreDropMode, Block oreBlock, int meta, boolean silktouch, int fortune) {
        if (meta <= 0) {
            ArrayList<ItemStack> drops = new ArrayList<>();
            drops.add(new ItemStack(Blocks.cobblestone, 1, 0));
            return drops;
        }

        StoneType stone = getStoneType(meta);
        Materials material = getMaterial(meta);

        if (isSmallOre(meta)) {
            return getSmallOreDrops(random, stone, material, fortune);
        } else {
            return getBigOreDrops(random, oreDropMode, oreBlock, stone, material, silktouch, fortune);
        }
    }

    private static enum SmallOreDrops {
        gemExquisite(OrePrefixes.gemExquisite, OrePrefixes.gem, 1),
        gemFlawless(OrePrefixes.gemFlawless, OrePrefixes.gem, 2),
        gem(OrePrefixes.gem, null, 12),
        gemFlawed(OrePrefixes.gemFlawed, OrePrefixes.crushed, 5),
        crushed(OrePrefixes.crushed, null, 10),
        gemChipped(OrePrefixes.gemChipped, OrePrefixes.dustImpure, 5),
        dustImpure(OrePrefixes.dustImpure, null, 10);

        public static final ImmutableList<SmallOreDrops> DROPS = ImmutableList.copyOf(values());

        public final OrePrefixes primary;
        public final OrePrefixes fallback;
        public final int weight;

        private SmallOreDrops(OrePrefixes primary, OrePrefixes fallback, int weight) {
            this.primary = primary;
            this.fallback = fallback;
            this.weight = weight;
        }

        public static ArrayList<ItemStack> getDropList(Materials material) {
            ArrayList<ItemStack> drops = new ArrayList<>();

            for (SmallOreDrops drop : DROPS) {
                ItemStack fallback = drop.fallback == null ? null : GTOreDictUnificator.get(drop.fallback, material, 1L);
                ItemStack primary = GTOreDictUnificator.get(drop.primary, material, fallback, 1L);

                if (primary != null) {
                    for (int i = 0; i < drop.weight; i++) {
                        drops.add(primary);
                    }
                }
            }

            return drops;
        }
    }

    public static ArrayList<ItemStack> getSmallOreDrops(Random random, StoneType stone, Materials material, int fortune) {
        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(material);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(1, material.mOreMultiplier + (fortune > 0 ? random.nextInt(1 + fortune * material.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(GTOreDictUnificator.get(random.nextInt(3) > 0 ? OrePrefixes.dustImpure : OrePrefixes.dust, stone.material, 1L));
        }

        return drops;
    }

    public static ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, Block oreBlock, StoneType stone, Materials material, boolean silktouch, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        
        // For Sake of god of balance!

        boolean isRich = switch (stone) {
            case Netherrack -> GTMod.gregtechproxy.mNetherOreYieldMultiplier;
            case Endstone -> GTMod.gregtechproxy.mEndOreYieldMultiplier;
            default -> false;
        };
        
        if (silktouch) oreDropMode = OreDropSystem.Block;

        switch (oreDropMode) {
            case Item -> {
                drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, material, isRich ? 2 : 1));
            }
            case FortuneItem -> {
                if (fortune > 0) {
                    // Max applicable fortune
                    if (fortune > 3) fortune = 3;

                    int amount = 1 + Math.max(random.nextInt(fortune * (isRich ? 2 : 1) + 2) - 1, 0);

                    for (int i = 0; i < amount; i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, material, 1));
                    }
                } else {
                    for (int i = 0; i < (isRich ? 2 : 1); i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, material, 1));
                    }
                }
            }
            case UnifiedBlock -> {
                for (int i = 0; i < (isRich ? 2 : 1); i++) {
                    drops.add(new ItemStack(oreBlock, 1, material.mMetaItemSubID));
                }
            }
            case PerDimBlock -> {
                if (stone == StoneType.Netherrack || stone == StoneType.Endstone) {
                    drops.add(new ItemStack(oreBlock, 1, getMeta(stone, material.mMetaItemSubID, false, false)));
                } else {
                    drops.add(new ItemStack(oreBlock, 1, material.mMetaItemSubID));
                }
            }
            case Block -> {
                drops.add(new ItemStack(oreBlock, 1, getMeta(stone, material.mMetaItemSubID, false, false)));
            }
        }

        return drops;
    }
}
