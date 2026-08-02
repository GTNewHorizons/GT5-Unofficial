package gregtech.loaders.postload;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;

/// The frozen per-material pipe-family MTE ids, mapped to the MaterialLib shape and material each id's
/// wire/cable/fluid-pipe/item-pipe served, for [PosteaTransformers]' save migration. The id layout is:
///
/// - wire/cable rows occupy startId+0..5 as wireGt01..16 and +6..11 as cableGt01..16 (wire-only rows just
/// the first six),
/// - GregTech fluid rows startId+0..4 as pipeTiny..Huge plus a quadruple/nonuple pair at multiFluidStartId
/// (startId+5 unless displaced); gtPlusPlus/goodgenerator fluid rows registered no multi-channel pipes,
/// - the wooden and High Pressure pipes three sequential small/medium/large ids,
/// - item rows ten ids ordered tiny..huge then restrictiveTiny..Huge, six (medium..huge, restrictive
/// likewise) for the materials without tiny/small sizes, with the Brass through Osmium rows keeping their
/// historical non-sequential id lists.
///
/// Ids absent from the table (gaps inside the freed ranges) have no migration and must stay untouched.
public final class LegacyPipeCutoverTable {

    public record Entry(Shape shape, Material material) {}

    private static Map<Integer, Entry> table;

    public static Map<Integer, Entry> entries() {
        if (table == null) {
            table = new HashMap<>();
            build();
        }
        return table;
    }

    private static void build() {
        wireCable(Materials.Cobalt, 1200);
        wireCable(Materials.Lead, 1220);
        wireCable(Materials.Tin, 1240);
        wireCable(Materials.Zinc, 1260);
        wireCable(Materials.SolderingAlloy, 1280);
        wireCable(Materials.Iron, 1300);
        wireCable(Materials.Nickel, 1320);
        wireCable(Materials.Cupronickel, 1340);
        wireCable(Materials.Copper, 1360);
        wireCable(Materials.AnnealedCopper, 1380);
        wireCable(Materials.Kanthal, 1400);
        wireCable(Materials.Gold, 1420);
        wireCable(Materials.Electrum, 1440);
        wireCable(Materials.Silver, 1460);
        wireCable(Materials.BlueAlloy, 1480);
        wireCable(Materials.Nichrome, 1500);
        wireCable(Materials.Steel, 1520);
        wireCable(Materials.BlackSteel, 1540);
        wireCable(Materials.Titanium, 1560);
        wireCable(Materials.Aluminium, 1580);
        wireOnly(Materials.Graphene, 1600);
        wireCable(Materials.Osmium, 1620);
        wireCable(Materials.Platinum, 1640);
        wireCable(Materials.TungstenSteel, 1660);
        wireCable(Materials.Tungsten, 1680);
        wireCable(Materials.HSSG, 1700);
        wireCable(Materials.NiobiumTitanium, 1720);
        wireCable(Materials.VanadiumGallium, 1740);
        wireCable(Materials.YttriumBariumCuprate, 1760);
        wireCable(Materials.Naquadah, 1780);
        wireCable(Materials.NaquadahAlloy, 1800);
        wireCable(Materials.Duranium, 1820);
        wireCable(Materials.TPVAlloy, 1840);
        wireCable(Materials.EndSteel, 1860);
        wireCable(Materials.ElectrumFlux, 1900);
        wireCable(Materials.RedAlloy, 2000);
        wireOnly(MaterialFacades.SuperconductorUHV, 2020);
        wireOnly(MaterialFacades.SuperconductorUEV, 2026);
        wireOnly(Materials.SuperconductorUEVBase, 2032);
        wireOnly(Materials.SuperconductorUIVBase, 2052);
        wireOnly(Materials.SuperconductorUMVBase, 2072);
        wireOnly(MaterialFacades.SuperconductorUIV, 2081);
        wireOnly(MaterialFacades.SuperconductorUMV, 2089);
        wireOnly(Materials.Pentacadmiummagnesiumhexaoxid, 2200);
        wireOnly(Materials.Titaniumonabariumdecacoppereikosaoxid, 2220);
        wireOnly(Materials.Uraniumtriplatinid, 2240);
        wireOnly(Materials.Vanadiumtriindinid, 2260);
        wireOnly(Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 2280);
        wireOnly(Materials.Tetranaquadahdiindiumhexaplatiumosminid, 2300);
        wireOnly(MaterialFacades.SuperconductorMV, 2320);
        wireOnly(MaterialFacades.SuperconductorHV, 2340);
        wireOnly(MaterialFacades.SuperconductorEV, 2360);
        wireOnly(MaterialFacades.SuperconductorIV, 2380);
        wireOnly(MaterialFacades.SuperconductorLuV, 2400);
        wireOnly(MaterialFacades.SuperconductorZPM, 2420);
        wireOnly(MaterialFacades.SuperconductorUV, 2440);
        wireOnly(Materials.Longasssuperconductornameforuvwire, 2500);
        wireOnly(Materials.Longasssuperconductornameforuhvwire, 2520);
        wireOnly(Materials.Ichorium, 2600);
        wireOnly(Materials.SpaceTime, 2606);
        wireCable(Materials.Bedrockium, 11310);
        wireCable(Materials.Draconium, 11330);
        wireCable(Materials.NetherStar, 11350);
        wireCable(Materials.Quantium, 11370);
        wireOnly(Materials.BlackPlutonium, 11390);
        wireOnly(Materials.DraconiumAwakened, 11410);
        wireOnly(Materials.Infinity, 11430);
        wireCable(Materials.Trinium, 11450);
        wireCable(Materials.HSSS, 11470);
        wireCable(Materials.ElectricalSteel, 11490);
        wireCable(Materials.EnergeticAlloy, 11510);
        wireCable(Materials.VibrantAlloy, 11530);
        wireCable(Materials.MelodicAlloy, 11550);
        wireCable(Materials.StellarAlloy, 11570);
        wireCable(Materials.HSSE, 11590);
        wireCable(Materials.Osmiridium, 11610);
        wireOnly(Materials.Hypogen, 30585);
        wireCable(Materials.RedstoneAlloy, 30645);
        wireCable(Materials.Lumiium, 32737);
        wireCable(Materials.Signalium, 32749);

        threeSizeFluidPipe(Materials.Wood, 5101);
        fluidPipe(Materials.Copper, 5110);
        fluidPipe(Materials.Bronze, 5120);
        fluidPipe(Materials.Steel, 5130);
        fluidPipe(Materials.StainlessSteel, 5140);
        fluidPipe(Materials.Titanium, 5150);
        fluidPipe(Materials.TungstenSteel, 5160, 5270);
        threeSizeFluidPipe(Materials.Redstone, 5165);
        fluidPipe(Materials.Plastic, 5170);
        fluidPipe(Materials.NiobiumTitanium, 5180);
        fluidPipe(Materials.Enderium, 5190);
        fluidPipe(Materials.Naquadah, 5200);
        fluidPipe(Materials.Neutronium, 5210);
        fluidPipe(Materials.NetherStar, 5220);
        fluidPipe(Materials.MysteriousCrystal, 5230);
        fluidPipe(Materials.DraconiumAwakened, 5240);
        fluidPipe(Materials.Infinity, 5250);
        fluidPipe(Materials.CastIron, 5260);
        fluidPipe(Materials.Polybenzimidazole, 5280, 5290);
        fluidPipe(Materials.SpaceTime, 5300);
        fluidPipe(Materials.TranscendentMetal, 5310);
        fluidPipe(Materials.Polytetrafluoroethylene, 5680);
        fluidPipe(Materials.RadoxPoly, 5760);
        fluidPipeNoMulti(Materials.TriniumNaquadahCarbonite, 30500);
        fluidPipeNoMulti(Materials.Staballoy, 30700);
        fluidPipeNoMulti(Materials.Tantalloy60, 30705);
        fluidPipeNoMulti(Materials.Tantalloy61, 30710);
        fluidPipeNoMulti(Materials.Void, 30715);
        fluidPipeNoMulti(Materials.Europium, 30720);
        fluidPipeNoMulti(Materials.Potin, 30725);
        fluidPipeNoMulti(Materials.MaragingSteel300, 30730);
        fluidPipeNoMulti(Materials.MaragingSteel350, 30735);
        fluidPipeNoMulti(Materials.Inconel690, 30740);
        fluidPipeNoMulti(Materials.Inconel792, 30745);
        fluidPipeNoMulti(Materials.HastelloyX, 30750);
        fluidPipeNoMulti(Materials.Tungsten, 30755);
        fluidPipeNoMulti(Materials.DarkSteel, 30760);
        fluidPipeNoMulti(Materials.Clay, 30765);
        fluidPipeNoMulti(Materials.Lead, 30770);
        fluidPipeNoMulti(Materials.Incoloy903, 30995);

        itemPipe(Materials.Tin, 5589);
        itemPipeIds(Materials.Brass, 5600, 5601, 5602, 5603, 5604, 5640, 5641, 5605, 5606, 5607);
        itemPipeIds(Materials.Electrum, 5610, 5611, 5612, 5613, 5614, 5642, 5643, 5615, 5616, 5617);
        itemPipeIds(Materials.Platinum, 5620, 5621, 5622, 5623, 5624, 5644, 5645, 5625, 5626, 5627);
        itemPipeIds(Materials.Osmium, 5630, 5631, 5632, 5633, 5634, 5646, 5647, 5635, 5636, 5637);
        itemPipe(Materials.ElectrumFlux, 5650);
        itemPipe(Materials.BlackPlutonium, 5660);
        itemPipe(Materials.Bedrockium, 5670);
        itemPipeNoSmallSizes(Materials.PolyvinylChloride, 5690);
        itemPipeNoSmallSizes(Materials.Nickel, 5700);
        itemPipeNoSmallSizes(Materials.Cobalt, 5710);
        itemPipeNoSmallSizes(Materials.Aluminium, 5720);
        itemPipe(Materials.Quantium, 5730);
    }

    private static void wireCable(Material material, int startId) {
        wireOnly(material, startId);
        putRange(
            material,
            startId + 6,
            TEBlockShapes.cableGt01,
            TEBlockShapes.cableGt02,
            TEBlockShapes.cableGt04,
            TEBlockShapes.cableGt08,
            TEBlockShapes.cableGt12,
            TEBlockShapes.cableGt16);
    }

    private static void wireOnly(Material material, int startId) {
        putRange(
            material,
            startId,
            TEBlockShapes.wireGt01,
            TEBlockShapes.wireGt02,
            TEBlockShapes.wireGt04,
            TEBlockShapes.wireGt08,
            TEBlockShapes.wireGt12,
            TEBlockShapes.wireGt16);
    }

    private static void fluidPipe(Material material, int startId) {
        fluidPipe(material, startId, startId + 5);
    }

    private static void fluidPipe(Material material, int startId, int multiFluidStartId) {
        fluidPipeNoMulti(material, startId);
        putRange(material, multiFluidStartId, TEBlockShapes.pipeQuadruple, TEBlockShapes.pipeNonuple);
    }

    private static void fluidPipeNoMulti(Material material, int startId) {
        putRange(
            material,
            startId,
            TEBlockShapes.pipeTiny,
            TEBlockShapes.pipeSmall,
            TEBlockShapes.pipeMedium,
            TEBlockShapes.pipeLarge,
            TEBlockShapes.pipeHuge);
    }

    private static void threeSizeFluidPipe(Material material, int startId) {
        putRange(material, startId, TEBlockShapes.pipeSmall, TEBlockShapes.pipeMedium, TEBlockShapes.pipeLarge);
    }

    private static void itemPipe(Material material, int startId) {
        itemPipeIds(
            material,
            IntStream.range(startId, startId + 10)
                .toArray());
    }

    private static void itemPipeIds(Material material, int... ids) {
        Shape[] sizes = { TEBlockShapes.itemPipeTiny, TEBlockShapes.itemPipeSmall, TEBlockShapes.itemPipeMedium,
            TEBlockShapes.itemPipeLarge, TEBlockShapes.itemPipeHuge, TEBlockShapes.itemPipeRestrictiveTiny,
            TEBlockShapes.itemPipeRestrictiveSmall, TEBlockShapes.itemPipeRestrictiveMedium,
            TEBlockShapes.itemPipeRestrictiveLarge, TEBlockShapes.itemPipeRestrictiveHuge };
        for (int i = 0; i < sizes.length; i++) {
            put(ids[i], sizes[i], material);
        }
    }

    private static void itemPipeNoSmallSizes(Material material, int startId) {
        putRange(
            material,
            startId,
            TEBlockShapes.itemPipeMedium,
            TEBlockShapes.itemPipeLarge,
            TEBlockShapes.itemPipeHuge,
            TEBlockShapes.itemPipeRestrictiveMedium,
            TEBlockShapes.itemPipeRestrictiveLarge,
            TEBlockShapes.itemPipeRestrictiveHuge);
    }

    private static void putRange(Material material, int startId, Shape... shapes) {
        for (int i = 0; i < shapes.length; i++) {
            put(startId + i, shapes[i], material);
        }
    }

    private static void put(int id, Shape shape, Material material) {
        if (shape == null) {
            throw new IllegalStateException("Unresolved shape for legacy pipe MTE id " + id);
        }
        if (material == null) {
            throw new IllegalStateException("Unresolved material for legacy pipe MTE id " + id);
        }
        if (table.put(id, new Entry(shape, material)) != null) {
            throw new IllegalStateException("Duplicate legacy pipe MTE id " + id);
        }
    }

    private LegacyPipeCutoverTable() {}
}
