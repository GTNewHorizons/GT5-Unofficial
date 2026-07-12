package gregtech.common.ores;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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

import bartworks.system.material.BWItemMetaGeneratedOre;
import bartworks.system.material.BWMetaGeneratedOres;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.WerkstoffData;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.GTProxy.OreDropSystem;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

/// The `Werkstoff`-based [IOreAdapter]: worldgen, mining, prospecting, and the void miner place and read BW ore
/// blocks through this singleton (via [OreManager], never [BWMetaGeneratedOres] or MaterialLib directly),
/// mirroring [GTOreAdapter]'s port of the same pattern for GT's own ores.
///
/// BW only ever generated ore on two [StoneType]s, both of which already have a [Materials2OreShapes] variant
/// declared for GT's own ores: `StoneType.Stone` -> variant `"stone"` (index 0) and `StoneType.Moon` -> variant
/// `"moon"` (index 7). No new ore variants were needed for the BW cutover; BW ore/small-ore joins the exact same
/// `ore`/`oreSmall` shapes GT ore uses.
///
/// [#init] still constructs the eight legacy [BWMetaGeneratedOres] instances (2 stone types x {big, small} x
/// {natural, non-natural} -- see [#legacyOres]) exactly as before, registered under their original `bw.blockores*`
/// ids -- the same "construct, then get superseded by the MaterialLib association" pattern stage 07/08
/// established for `BlockMetal`/`GTBlockOre`. A reconstructed werkstoff's placed/inventory legacy ore actively
/// converts to the MaterialLib equivalent as it loads (see [#registerCurrentGenTransformers]); a third-party
/// (non-reconstructed) werkstoff has no MaterialLib ore material at all -- [#getBlock] falls back to the legacy
/// block for it, same as before the cutover, so it keeps every legacy path.
///
/// Legacy `isNatural` distinguished a "world-generated" ore meta from a "player-placeable" one purely for the
/// fortune-multiplier drop rule in [#getBigOreDrops]; [Materials2OreShapes]' shape space has no natural axis
/// (mirroring [GTOreAdapter]'s own collapse of the same distinction), so both legacy natural and non-natural
/// metas of the same (material, stone, size) collapse onto the same MaterialLib block, which always behaves as
/// the natural case (fortune applies) once cut over.
public final class BWOreAdapter implements IOreAdapter<Werkstoff> {

    public static BWOreAdapter INSTANCE = new BWOreAdapter();

    private BWOreAdapter() {}

    private final EnumMap<StoneType, Ores> legacyOres = new EnumMap<>(StoneType.class);

    private static final boolean[] BOOLEANS = { false, true };

    private static class Ores {

        final StoneType stoneType;
        final String bigSuffix, smallSuffix;
        final BWMetaGeneratedOres big, bigNatural, small, smallNatural;

        Ores(StoneType stoneType, String bigSuffix, String smallSuffix) {
            this.stoneType = stoneType;
            this.bigSuffix = bigSuffix;
            this.smallSuffix = smallSuffix;

            big = new BWMetaGeneratedOres("bw.blockores", stoneType, false, false);
            bigNatural = new BWMetaGeneratedOres("bw.blockores.natural", stoneType, false, true);
            small = new BWMetaGeneratedOres("bw.blockoresSmall", stoneType, true, false);
            smallNatural = new BWMetaGeneratedOres("bw.blockoresSmall.natural", stoneType, true, true);

            GameRegistry.registerBlock(big, BWItemMetaGeneratedOre.class, "bw.blockores." + bigSuffix);
            GameRegistry.registerBlock(bigNatural, BWItemMetaGeneratedOre.class, "bw.blockores.natural." + bigSuffix);
            GameRegistry.registerBlock(small, BWItemMetaGeneratedOre.class, "bw.blockores." + smallSuffix);
            GameRegistry
                .registerBlock(smallNatural, BWItemMetaGeneratedOre.class, "bw.blockores.natural." + smallSuffix);
        }

        void registerOredict() {
            big.registerOredict();
            bigNatural.registerOredict();
            small.registerOredict();
            smallNatural.registerOredict();
        }

        BWMetaGeneratedOres get(boolean small, boolean natural) {
            if (small) {
                return natural ? this.smallNatural : this.small;
            } else {
                return natural ? this.bigNatural : this.big;
            }
        }

        /// The registry name (`GameRegistry.registerBlock`'s third argument) for one of this stone type's four
        /// legacy ore blocks -- distinct from [BWMetaGeneratedOres#blockName] (the shared unlocalized name), see
        /// this class's javadoc.
        String registryName(boolean small, boolean natural) {
            String suffix = small ? smallSuffix : bigSuffix;
            return natural ? "bw.blockores.natural." + suffix : "bw.blockores." + suffix;
        }
    }

    public void init() {
        legacyOres.put(StoneType.Stone, new Ores(StoneType.Stone, "01", "02"));
        legacyOres.put(StoneType.Moon, new Ores(StoneType.Moon, "03", "04"));

        TileEntityReplacementManager.tileEntityTransformer("bw.blockoresTE", (tag, world, chunk) -> {
            ImmutableBlockMeta bm = resolveLegacyTE(tag.getInteger("m"), false);
            return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
        });
        TileEntityReplacementManager.tileEntityTransformer("bw.blockoresSmallTE", (tag, world, chunk) -> {
            ImmutableBlockMeta bm = resolveLegacyTE(tag.getInteger("m"), true);
            return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
        });

        registerCurrentGenTransformers();
    }

    /// The ancient TE-based ore format only ever existed for [StoneType#Stone] (Moon ore was added after the
    /// switch to [BWMetaGeneratedOres]), and never encoded a natural flag -- mirrors the legacy `transform`
    /// method this replaces, which unconditionally resolved through the `Stone` [Ores] entry.
    private ImmutableBlockMeta resolveLegacyTE(int meta, boolean small) {
        try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
            info.material = Werkstoff.werkstoffHashMap.get((short) meta);
            info.stoneType = StoneType.Stone;
            info.isSmall = small;
            info.isNatural = true;

            if (info.material == null || !supports(info)) return new BlockMeta(Blocks.air, 0);

            ImmutableBlockMeta bm = getBlock(info);
            return bm == null ? new BlockMeta(Blocks.air, 0) : bm;
        }
    }

    /// Actively converts placed/inventory legacy `bw.blockores*` blocks into the MaterialLib equivalent as a
    /// chunk/item loads, mirroring [GTOreAdapter#registerCurrentGenTransformers].
    private void registerCurrentGenTransformers() {
        for (Ores ores : legacyOres.values()) {
            registerCurrentGenTransformer(ores, ores.big, false, false);
            registerCurrentGenTransformer(ores, ores.bigNatural, false, true);
            registerCurrentGenTransformer(ores, ores.small, true, false);
            registerCurrentGenTransformer(ores, ores.smallNatural, true, true);
        }
    }

    private void registerCurrentGenTransformer(Ores ores, BWMetaGeneratedOres block, boolean small, boolean natural) {
        String originalId = "bartworks:" + ores.registryName(small, natural);

        BlockReplacementManager.addTransformationHandler(originalId, info -> {
            ImmutableBlockMeta bm = resolveCurrentGenMeta(ores.stoneType, small, info.metadata);
            if (bm == null) return false;
            info.blockID = Block.getIdFromBlock(bm.getBlock());
            info.metadata = bm.getBlockMeta();
            return true;
        });

        ItemStackReplacementManager.addTransformationHandler(originalId, (originalId2, tag) -> {
            ImmutableBlockMeta bm = resolveCurrentGenMeta(ores.stoneType, small, tag.getInteger("Damage"));
            if (bm == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(Item.getItemFromBlock(bm.getBlock())));
            tag.setShort("Damage", (short) bm.getBlockMeta());
            return true;
        });
    }

    private ImmutableBlockMeta resolveCurrentGenMeta(StoneType stoneType, boolean small, int meta) {
        try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
            info.material = Werkstoff.werkstoffHashMap.get((short) meta);
            info.stoneType = stoneType;
            info.isSmall = small;
            info.isNatural = true;

            if (info.material == null || !supports(info)) return null;

            return getBlock(info);
        }
    }

    public void registerOredict() {
        legacyOres.values()
            .forEach(Ores::registerOredict);
    }

    /// Hides every legacy `bw.blockores*` slot whose (material, stone, small-ore) combination now resolves to a
    /// MaterialLib block, mirroring [GTOreAdapter#hideOres]. A combination that stays legacy-canonical (a
    /// third-party werkstoff, or one this shape never covered) is left visible.
    public void hideOres() {
        for (Ores ores : legacyOres.values()) {
            for (Werkstoff w : Werkstoff.werkstoffHashSet) {
                if (w == null) continue;

                for (boolean small : BOOLEANS) {
                    if (!cutOver(w, ores.stoneType, small)) continue;

                    for (boolean natural : BOOLEANS) {
                        API.hideItem(new ItemStack(ores.get(small, natural), 1, w.getmID()));
                    }
                }
            }
        }
    }

    private boolean cutOver(Werkstoff w, StoneType stoneType, boolean small) {
        try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
            info.material = w;
            info.stoneType = stoneType;
            info.isSmall = small;

            if (!supports(info)) return false;

            Material mlMat = MU.material(w.getBridgeMaterial());
            Shape shape = small ? Materials2OreShapes.oreSmall : Materials2OreShapes.ore;
            return mlMat != null && mlMat.hasShape(shape);
        }
    }

    @Override
    public boolean supports(Block block, int meta) {
        if (block instanceof BWMetaGeneratedOres) return true;

        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && isOreShape(info.shape()) && isWerkstoffMaterial(info.material());
    }

    private static boolean isOreShape(Shape shape) {
        return shape == Materials2OreShapes.ore || shape == Materials2OreShapes.oreSmall;
    }

    private static boolean isWerkstoffMaterial(Material material) {
        return material != null && material.getProperty(GTMaterialProperties.WERKSTOFF) != null;
    }

    /// The [Werkstoff] a MaterialLib material was reconstructed from, or null if it carries no
    /// [GTMaterialProperties#WERKSTOFF] data.
    private static Werkstoff werkstoffOf(Material mlMaterial) {
        WerkstoffData data = mlMaterial.getProperty(GTMaterialProperties.WERKSTOFF);
        if (data == null) return null;
        return WerkstoffReconstruction.byId(
            data.ids()
                .get(0));
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (!(info.material instanceof Werkstoff w)) return false;

        IStoneType stone = info.stoneType == null ? StoneType.Stone : info.stoneType;
        if (!(stone instanceof StoneType stoneType)) return false;
        if (!legacyOres.containsKey(stoneType)) return false;

        return w.hasItemType(info.isSmall ? OrePrefixes.oreSmall : OrePrefixes.ore);
    }

    @Override
    public OreInfo<Werkstoff> getOreInfo(Block block, int meta) {
        if (block instanceof BWMetaGeneratedOres oreBlock) {
            Werkstoff w = Werkstoff.werkstoffHashMap.get((short) meta);
            if (w == null) return null;

            OreInfo<Werkstoff> info = OreInfo.getNewInfo();
            info.material = w;
            info.stoneType = oreBlock.stoneType;
            info.isSmall = oreBlock.isSmall;
            info.isNatural = oreBlock.isNatural;
            return info;
        }

        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || !isOreShape(blockInfo.shape()) || !isWerkstoffMaterial(blockInfo.material()))
            return null;

        Werkstoff w = werkstoffOf(blockInfo.material());
        StoneType stoneType = Materials2OreShapes.stoneTypeOf(blockInfo.variant());
        if (w == null || stoneType == null) return null;

        OreInfo<Werkstoff> info = OreInfo.getNewInfo();
        info.material = w;
        info.stoneType = stoneType;
        info.isSmall = blockInfo.shape() == Materials2OreShapes.oreSmall;
        info.isNatural = true;

        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        IStoneType stone = info.stoneType == null ? StoneType.Stone : info.stoneType;
        if (!(stone instanceof StoneType stoneType)) return null;
        if (!(info.material instanceof Werkstoff w)) return null;

        OrePrefixes prefix = info.isSmall ? OrePrefixes.oreSmall : OrePrefixes.ore;
        if (!w.hasItemType(prefix)) return null;

        Material mlMat = MU.material(w.getBridgeMaterial());
        Shape shape = info.isSmall ? Materials2OreShapes.oreSmall : Materials2OreShapes.ore;

        if (mlMat != null && mlMat.hasShape(shape)) {
            String variant = Materials2OreShapes.variantOf(stoneType.name());
            ItemStack stack = MaterialLibAPI.getStack(mlMat, shape, variant, 1);
            if (stack != null) {
                return new BlockMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
            }
        }

        Ores ores = legacyOres.get(stoneType);
        if (ores == null) return null;

        return new BlockMeta(ores.get(info.isSmall, false), w.getmID());
    }

    /// The harvest level for a MaterialLib werkstoff ore/small-ore material -- legacy [BWMetaGeneratedOres] used a
    /// flat level 3 for every material, unlike GT's own per-material [GTOreAdapter#harvestLevel]. `bonus` is
    /// accepted for call-site symmetry with [GTOreAdapter#harvestLevel] but BW never varied it by size either.
    public int harvestLevel(Material mlMaterial, int bonus) {
        return GTUtility.clamp(3 + bonus, 0, GTMod.proxy.mMaxHarvestLevel);
    }

    /// The drops for one MaterialLib werkstoff ore/small-ore block, called from [Materials2OreShapes]' drop hooks
    /// when the material is werkstoff-backed. See [GTOreAdapter#shapeDrops] for the GT-material equivalent.
    public List<ItemStack> shapeDrops(Material mlMaterial, String variant, int fortune, boolean isSilkTouch,
        boolean isSmall) {
        Werkstoff w = werkstoffOf(mlMaterial);
        StoneType stoneType = Materials2OreShapes.stoneTypeOf(variant);
        if (w == null || stoneType == null) return List.of();

        try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
            info.material = w;
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
        OreInfo<Werkstoff> info = (OreInfo<Werkstoff>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;
        if (!info.isNatural) fortune = 0;

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
        OreInfo<Werkstoff> info = (OreInfo<Werkstoff>) info2;

        if (info.isSmall) {
            ObjectLinkedOpenHashSet<ItemId> drops = new ObjectLinkedOpenHashSet<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material.getBridgeMaterial())) {
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

    private ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo<Werkstoff> info, int fortune) {
        Materials bridge = info.material.getBridgeMaterial();

        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(bridge);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(
                1,
                bridge.mOreMultiplier + (fortune > 0 ? random.nextInt(1 + fortune * bridge.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Werkstoff> info,
        int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        switch (oreDropMode) {
            case Item -> {
                drops.add(info.material.get(OrePrefixes.rawOre, info.stoneType.isRich() ? 2 : 1));
            }
            case FortuneItem -> {
                if (fortune > 0) {
                    if (fortune > 3) fortune = 3;

                    int addedDrops = random.nextInt(fortune + 2) - 1;
                    if (addedDrops < 0) addedDrops = 0;

                    int amount = (info.stoneType.isRich() ? 2 : 1) * (addedDrops + 1);

                    for (int i = 0; i < amount; i++) {
                        drops.add(info.material.get(OrePrefixes.rawOre, 1));
                    }
                } else {
                    for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                        drops.add(info.material.get(OrePrefixes.rawOre, 1));
                    }
                }
            }
            case UnifiedBlock -> {
                try (OreInfo<Werkstoff> info2 = info.clone()) {
                    info2.isNatural = false;

                    for (int i = 0; i < (info2.stoneType.isRich() ? 2 : 1); i++) {
                        info2.stoneType = StoneType.Stone;
                        drops.add(getStack(info2, 1));
                    }
                }
            }
            case PerDimBlock -> {
                try (OreInfo<Werkstoff> info2 = info.clone()) {
                    info2.isNatural = false;

                    if (!info2.stoneType.isDimensionSpecific()) {
                        info2.stoneType = StoneType.Stone;
                    }

                    drops.add(getStack(info2, 1));
                }
            }
            case Block -> {
                try (OreInfo<Werkstoff> info2 = info.clone()) {
                    info2.isNatural = false;

                    drops.add(getStack(info2, 1));
                }
            }
        }

        return drops;
    }
}
