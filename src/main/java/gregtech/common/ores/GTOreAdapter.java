package gregtech.common.ores;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.BlockMaterialInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import codechicken.nei.api.API;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneCategory;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.GTProxy.OreDropSystem;
import gregtech.common.blocks.GTBlockOre;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

/// The `Materials`-based [IOreAdapter]: worldgen, mining, prospecting, and the void miner place and read
/// [Materials2OreShapes] blocks through this singleton (via [OreManager], never `Materials2OreShapes` or
/// MaterialLib directly), preserving the same public surface the legacy `GTBlockOre`-backed adapter had.
///
/// [#init] still constructs the six legacy [GTBlockOre] instances (`gt.blockores2`..`gt.blockores7`) exactly as
/// before -- their oredict registration loop (in [GTBlockOre]'s constructor) now resolves through this class's
/// [#getBlock]/[#supports], so it registers the MaterialLib stack as the canonical association without any
/// change to [GTBlockOre] itself, the same "construct, then get overwritten by the MaterialLib association"
/// pattern `BlockMetal` uses. The instances stay registered (never removed) purely so
/// pre-migration saves have somewhere for their placed blocks to resolve to; [#registerCurrentGenTransformers]
/// actively converts any of those placed blocks to the MaterialLib equivalent as their chunk loads, so in
/// practice a fully-loaded world never has a live `GTBlockOre` block a player can interact with.
public final class GTOreAdapter implements IOreAdapter<Materials> {

    public static GTOreAdapter INSTANCE = new GTOreAdapter();

    private GTOreAdapter() {}

    private final Map<StoneType, GTBlockOre> oreBlocksByStoneType = new EnumMap<>(StoneType.class);

    private GTBlockOre[] ores;

    /// Used to iterate both `isSmall`/`isNatural` axes of a legacy ore meta in [#hideOres].
    private static final boolean[] BOOLEANS = { false, true };

    // spotless:off
    private static final StoneType[] LEGACY_STONES = {
        StoneType.Stone,
        StoneType.Netherrack,
        StoneType.Endstone,
        StoneType.BlackGranite,
        StoneType.RedGranite,
        StoneType.Marble,
        StoneType.Basalt,
    };
    // spotless:on

    public void init() {
        // spotless:off
        GTBlockOre ores1 = new GTBlockOre(2, new StoneType[] {
            StoneType.Stone,
            StoneType.Netherrack,
            StoneType.Endstone,
            GTMod.proxy.enableBlackGraniteOres ? StoneType.BlackGranite : null,
            GTMod.proxy.enableRedGraniteOres ? StoneType.RedGranite : null,
            GTMod.proxy.enableMarbleOres ? StoneType.Marble : null,
            GTMod.proxy.enableBasaltOres ? StoneType.Basalt : null,
            StoneType.Moon,
        });
        GTBlockOre ores2 = new GTBlockOre(3, new StoneType[] {
            StoneType.Mars,
            StoneType.Asteroid,
            StoneType.Phobos,
            StoneType.Deimos,
            StoneType.Ceres,
            StoneType.Io,
            StoneType.Europa,
            StoneType.Ganymede,
        });
        GTBlockOre ores3 = new GTBlockOre(4, new StoneType[] {
            StoneType.Callisto,
            StoneType.Enceladus,
            StoneType.Titan,
            StoneType.Miranda,
            StoneType.Oberon,
            StoneType.Proteus,
            StoneType.Triton,
            StoneType.Pluto,
        });
        GTBlockOre ores4 = new GTBlockOre(5, new StoneType[] {
            StoneType.Venus,
            StoneType.Mercury,
        });
        GTBlockOre ores5 = new GTBlockOre(6, new StoneType[] {
            StoneType.Haumea,
            StoneType.MakeMake,
            StoneType.AlphaCentauri,
            StoneType.TCetiE,
            StoneType.VegaB,
            StoneType.BarnardaE,
            StoneType.BarnardaF,
            StoneType.Horus,
        });
        GTBlockOre ores6 = new GTBlockOre(7, new StoneType[] {
            StoneType.AnubisAndMaahes,
            StoneType.PackedIce,
            StoneType.SethIce,
            StoneType.SethClay,
            StoneType.Deepslate,
            StoneType.Tuff,
            StoneType.BlueIce,
        });

        ores = new GTBlockOre[] { ores1, ores2, ores3, ores4, ores5, ores6 };
        // spotless:on

        TileEntityReplacementManager.tileEntityTransformer("GT_TileEntity_Ores", (tag, world, chunk) -> {
            int meta = tag.getInteger("m");
            boolean natural = tag.getBoolean("n");

            ImmutableBlockMeta bm = resolveLegacyMeta(meta, natural);

            return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
        });

        ItemStackReplacementManager.addTransformationHandler("gregtech:gt.blockores", (originalId, tag) -> {
            int meta = tag.getInteger("Damage");
            ImmutableBlockMeta bm = resolveLegacyMeta(meta, false);
            if (bm.getBlock() == Blocks.air) return false;

            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(Item.getItemFromBlock(bm.getBlock())));
            tag.setShort("Damage", (short) bm.getBlockMeta());
            return true;
        });

        registerCurrentGenTransformers();
    }

    /// Decodes the pre-`GTBlockOre` era's 7-stone packing (see [#LEGACY_STONES]) and resolves it to the
    /// MaterialLib block/meta -- shared by the `GT_TileEntity_Ores` tile-entity transformer and the single
    /// `gregtech:gt.blockores` item transformer, both of which used this exact packing.
    private ImmutableBlockMeta resolveLegacyMeta(int meta, boolean natural) {
        try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
            info.stoneType = GTUtility.getIndexSafe(LEGACY_STONES, (meta % 16000) / 1000);
            info.material = GregTechAPI.sGeneratedMaterials[meta % 1000];
            info.isSmall = meta >= 16000;
            info.isNatural = true;

            if (!this.supports(info)) {
                return new BlockMeta(Blocks.air, 0);
            } else {
                ImmutableBlockMeta bm = this.getBlock(info);
                return bm == null ? new BlockMeta(Blocks.air, 0) : bm;
            }
        }
    }

    /// Actively converts placed/inventory `gt.blockores2`..`gt.blockores7` (see [#init]'s `ores` array) into
    /// the MaterialLib equivalent as a chunk/item loads, decoding each block's own `stoneTypes` list the same
    /// way [GTBlockOre#getStoneIndex]/[GTBlockOre#getMaterialIndex] do. Mirrors `PosteaTransformers`'
    /// `registerStorageBlockCutoverTransformer`, except the meta space here is too large (up to ~24000 per
    /// block) to enumerate per-meta, so this decodes computationally instead.
    private void registerCurrentGenTransformers() {
        for (int i = 0; i < ores.length; i++) {
            GTBlockOre oreBlock = ores[i];
            String originalId = "gregtech:gt.blockores" + (i + 2);

            BlockReplacementManager.addTransformationHandler(originalId, info -> {
                ImmutableBlockMeta bm = resolveCurrentGenMeta(oreBlock, info.metadata);
                if (bm == null) return false;
                info.blockID = Block.getIdFromBlock(bm.getBlock());
                info.metadata = bm.getBlockMeta();
                return true;
            });

            ItemStackReplacementManager.addTransformationHandler(originalId, (originalId2, tag) -> {
                ImmutableBlockMeta bm = resolveCurrentGenMeta(oreBlock, tag.getInteger("Damage"));
                if (bm == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(Item.getItemFromBlock(bm.getBlock())));
                tag.setShort("Damage", (short) bm.getBlockMeta());
                return true;
            });
        }
    }

    private ImmutableBlockMeta resolveCurrentGenMeta(GTBlockOre oreBlock, int meta) {
        Materials mat = oreBlock.getMaterial(meta);
        StoneType stoneType = oreBlock.getStoneType(meta);
        if (mat == null || stoneType == null) return null;

        try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
            info.material = mat;
            info.stoneType = stoneType;
            info.isSmall = oreBlock.isSmallOre(meta);
            info.isNatural = true;

            return this.getBlock(info);
        }
    }

    public void registerOre(StoneType stoneType, GTBlockOre oreBlock) {
        oreBlocksByStoneType.put(stoneType, oreBlock);
    }

    /// Hides every `gt.blockores2`..`gt.blockores7` slot from NEI, mirroring `BlockMetal`'s precedent:
    /// the legacy block/item stays fully functional (old saves still resolve through it via the transformers
    /// [#init] registers), only its NEI visibility is suppressed. Meta 0 of each block is always hidden --
    /// [GTBlockOre#getSubBlocks]'s doc explains why it always exists as a dummy entry -- and every other meta is
    /// hidden exactly when its (material, stone, small-ore) combination now resolves to a MaterialLib block (see
    /// [#supports(OreInfo)]), leaving any combination that has not cut over (e.g. a material with no MaterialLib
    /// counterpart yet) visible and canonical, same as [GTBlockOre#getSubBlocks] itself already does.
    public void hideOres() {
        for (GTBlockOre ore : ores) {
            API.hideItem(new ItemStack(ore, 1, 0));

            for (int matId = 0; matId < 1000; matId++) {
                Materials material = ore.getMaterial(matId);
                if (material == null) continue;

                for (int stoneIndex = 0; stoneIndex < ore.stoneTypes.size(); stoneIndex++) {
                    StoneType stoneType = ore.stoneTypes.get(stoneIndex);
                    if (stoneType == null) continue;

                    for (boolean isSmall : BOOLEANS) {
                        try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
                            info.material = material;
                            info.stoneType = stoneType;
                            info.isSmall = isSmall;

                            if (!supports(info)) continue;
                        }

                        int baseMeta = matId + stoneIndex * 1000 + (isSmall ? GTBlockOre.SMALL_ORE_META_OFFSET : 0);
                        for (boolean natural : BOOLEANS) {
                            int meta = baseMeta + (natural ? GTBlockOre.NATURAL_ORE_META_OFFSET : 0);
                            API.hideItem(new ItemStack(ore, 1, meta));
                        }
                    }
                }
            }
        }
    }

    /// Materials2OreShapes' 4 config-gated stone types (see legacy `GTOreAdapter#init`'s array literals) are
    /// declared unconditionally as MaterialLib variants -- their availability is a runtime toggle, not a
    /// save-identity concern -- so this config gate moved here instead, alongside [StoneType#isEnabled]'s
    /// mod-loaded gate.
    private static boolean isStoneConfigEnabled(StoneType stoneType) {
        return switch (stoneType) {
            case BlackGranite -> GTMod.proxy.enableBlackGraniteOres;
            case RedGranite -> GTMod.proxy.enableRedGraniteOres;
            case Marble -> GTMod.proxy.enableMarbleOres;
            case Basalt -> GTMod.proxy.enableBasaltOres;
            default -> true;
        };
    }

    @Override
    public boolean supports(Block block, int meta) {
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && isOreShape(info.shape());
    }

    private static boolean isOreShape(Shape shape) {
        return shape == Materials2OreShapes.ore || shape == Materials2OreShapes.oreSmall;
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (!(info.material instanceof Materials gtMat)) return false;

        IStoneType stoneType = info.stoneType == null ? gtMat.getValidStones()
            .get(0) : info.stoneType;

        if (!(stoneType instanceof StoneType stoneType2)) return false;
        if (!stoneType2.isEnabled() || !isStoneConfigEnabled(stoneType2)) return false;
        if (!gtMat.isValidForStone(stoneType2)) return false;
        if (stoneType2.getCategory() == StoneCategory.Ice && info.isSmall) return false;

        Material mlMat = MU.material(gtMat);
        if (mlMat == null) return false;

        return mlMat.hasShape(info.isSmall ? Materials2OreShapes.oreSmall : Materials2OreShapes.ore);
    }

    @Override
    public OreInfo<Materials> getOreInfo(Block block, int meta) {
        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || !isOreShape(blockInfo.shape()) || blockInfo.material() == null) return null;
        // A werkstoff's bridge Materials instance (see BridgeMaterialsLoader) would otherwise also resolve here
        // via MU#materialOf, since it shares the legacy-name index with every other Materials constant; defer
        // to BWOreAdapter, which owns werkstoff ore behavior (see Materials2OreShapes#isWerkstoff).
        if (blockInfo.material()
            .getProperty(GTMaterialProperties.WERKSTOFF) != null) return null;

        Materials mat = MU.materialOf(blockInfo.material());
        StoneType stoneType = Materials2OreShapes.stoneTypeOf(blockInfo.variant());
        if (mat == null || stoneType == null) return null;

        OreInfo<Materials> info = OreInfo.getNewInfo();

        info.material = mat;
        info.stoneType = stoneType;
        info.isSmall = blockInfo.shape() == Materials2OreShapes.oreSmall;
        info.isNatural = true;

        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        if (!(info.material instanceof Materials gtMat)) return null;
        if (!(info.stoneType instanceof StoneType stoneType)) return null;
        if (!stoneType.isEnabled() || !isStoneConfigEnabled(stoneType)) return null;
        if (stoneType.getCategory() == StoneCategory.Ice && info.isSmall) return null;
        if (!gtMat.isValidForStone(stoneType)) return null;

        Material mlMat = MU.material(gtMat);
        if (mlMat == null) return null;

        Shape shape = info.isSmall ? Materials2OreShapes.oreSmall : Materials2OreShapes.ore;
        if (!mlMat.hasShape(shape)) return null;

        ItemStack stack = MaterialLibAPI.getStack(mlMat, shape, Materials2OreShapes.variantOf(stoneType.name()), 1);
        if (stack == null) return null;

        return new BlockMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    /// The harvest level for a MaterialLib ore/small-ore material, porting legacy `GTBlockOre#getHarvestLevel`'s
    /// formula. `bonus` is the small-ore harvest-level discount (`-1`, matching legacy) or `0` for big ore.
    public int harvestLevel(Material mlMaterial, int bonus) {
        Materials mat = MU.materialOf(mlMaterial);
        if (mat == null) return 0;

        int harvestLevel = GTMod.proxy.mChangeHarvestLevels ? GTMod.proxy.mHarvestLevel[mat.mMetaItemSubID]
            : mat.mToolQuality;

        return GTUtility.clamp(harvestLevel + bonus, 0, GTMod.proxy.mMaxHarvestLevel);
    }

    /// The drops for one MaterialLib ore/small-ore block, called from [Materials2OreShapes]' drop hooks. `variant`
    /// resolves back to a [StoneType] via [Materials2OreShapes#stoneTypeOf]; `material` back to a legacy
    /// [Materials] via [MU#materialOf]. See [#getOreDrops] for the shared drop-policy implementation.
    public List<ItemStack> shapeDrops(Material mlMaterial, String variant, int fortune, boolean isSilkTouch,
        boolean isSmall) {
        Materials mat = MU.materialOf(mlMaterial);
        StoneType stoneType = Materials2OreShapes.stoneTypeOf(variant);
        if (mat == null || stoneType == null) return List.of();

        try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
            info.material = mat;
            info.stoneType = stoneType;
            info.isSmall = isSmall;
            info.isNatural = true;

            return getOreDrops(ThreadLocalRandom.current(), info, isSilkTouch, fortune);
        }
    }

    @Override
    public @NotNull ArrayList<ItemStack> getOreDrops(Random random, OreInfo<?> info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        if (info.isSmall) {
            return getSmallOreDrops(random, info, fortune);
        } else {
            OreDropSystem oreDropSystem = GTMod.proxy.oreDropSystem;

            if (silktouch) oreDropSystem = OreDropSystem.Block;

            return getBigOreDrops(random, oreDropSystem, info, fortune);
        }
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo<?> info2) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.isSmall) {
            ObjectLinkedOpenHashSet<ItemId> drops = new ObjectLinkedOpenHashSet<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material)) {
                ItemId id = ItemId.create(stack);

                drops.add(id);
            }

            ArrayList<ItemStack> drops2 = new ArrayList<>();

            for (ItemId id : drops) {
                drops2.add(id.getItemStack());
            }

            return drops2;
        } else {
            return getBigOreDrops(ThreadLocalRandom.current(), GTMod.proxy.oreDropSystem, info, 0);
        }
    }

    private ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo<Materials> info, int fortune) {
        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(info.material);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(
                1,
                info.material.mOreMultiplier
                    + (fortune > 0 ? random.nextInt(1 + fortune * info.material.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Materials> info,
        int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        // For Sake of god of balance!

        switch (oreDropMode) {
            case Item -> {
                drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, info.stoneType.isRich() ? 2 : 1));
            }
            case FortuneItem -> {
                if (fortune > 0) {
                    // Max applicable fortune
                    if (fortune > 3) fortune = 3;

                    int addedDrops = random.nextInt(fortune + 2) - 1;
                    if (addedDrops < 0) addedDrops = 0;

                    int amount = (info.stoneType.isRich() ? 2 : 1) * (addedDrops + 1);

                    for (int i = 0; i < amount; i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, 1));
                    }
                } else {
                    for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, 1));
                    }
                }
            }
            case UnifiedBlock -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    for (int i = 0; i < (info2.stoneType.isRich() ? 2 : 1); i++) {
                        info2.stoneType = StoneType.Stone;
                        drops.add(getStack(info2, 1));
                    }
                }
            }
            case PerDimBlock -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    if (!info2.stoneType.isDimensionSpecific()) {
                        info2.stoneType = StoneType.Stone;
                    }

                    drops.add(getStack(info2, 1));
                }
            }
            case Block -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    drops.add(getStack(info2, 1));
                }
            }
        }

        return drops;
    }
}
