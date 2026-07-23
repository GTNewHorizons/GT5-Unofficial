package gregtech.loaders.postload;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.materials2.Materials2Markers;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2PipeShapes;

/// The retired per-material pipe-family MTE ids, mapped to the MaterialLib shape and material each id's
/// wire/cable/fluid-pipe/item-pipe served, for [PosteaTransformers]' save migration. Rows reproduce the id
/// layout of the deleted registrations (GregTech's pipe MTE loader, gtPlusPlus `GregtechConduits`,
/// goodgenerator `CrackRecipeAdder`):
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
        wireCable(Materials2Materials.Cobalt, 1200);
        wireCable(Materials2Materials.Lead, 1220);
        wireCable(Materials2Materials.Tin, 1240);
        wireCable(Materials2Materials.Zinc, 1260);
        wireCable(Materials2Materials.SolderingAlloy, 1280);
        wireCable(Materials2Materials.Iron, 1300);
        wireCable(Materials2Materials.Nickel, 1320);
        wireCable(Materials2Materials.Cupronickel, 1340);
        wireCable(Materials2Materials.Copper, 1360);
        wireCable(Materials2Materials.AnnealedCopper, 1380);
        wireCable(Materials2Materials.Kanthal, 1400);
        wireCable(Materials2Materials.Gold, 1420);
        wireCable(Materials2Materials.Electrum, 1440);
        wireCable(Materials2Materials.Silver, 1460);
        wireCable(Materials2Materials.BlueAlloy, 1480);
        wireCable(Materials2Materials.Nichrome, 1500);
        wireCable(Materials2Materials.Steel, 1520);
        wireCable(Materials2Materials.BlackSteel, 1540);
        wireCable(Materials2Materials.Titanium, 1560);
        wireCable(Materials2Materials.Aluminium, 1580);
        wireOnly(Materials2Materials.Graphene, 1600);
        wireCable(Materials2Materials.Osmium, 1620);
        wireCable(Materials2Materials.Platinum, 1640);
        wireCable(Materials2Materials.TungstenSteel, 1660);
        wireCable(Materials2Materials.Tungsten, 1680);
        wireCable(Materials2Materials.HSSG, 1700);
        wireCable(Materials2Materials.NiobiumTitanium, 1720);
        wireCable(Materials2Materials.VanadiumGallium, 1740);
        wireCable(Materials2Materials.YttriumBariumCuprate, 1760);
        wireCable(Materials2Materials.Naquadah, 1780);
        wireCable(Materials2Materials.NaquadahAlloy, 1800);
        wireCable(Materials2Materials.Duranium, 1820);
        wireCable(Materials2Materials.TPVAlloy, 1840);
        wireCable(Materials2Materials.RedAlloy, 2000);
        wireOnly(Materials2Markers.SuperconductorUHV, 2020);
        wireOnly(Materials2Markers.SuperconductorUEV, 2026);
        wireOnly(Materials2Materials.SuperconductorUEVBase, 2032);
        wireOnly(Materials2Materials.SuperconductorUIVBase, 2052);
        wireOnly(Materials2Materials.SuperconductorUMVBase, 2072);
        wireOnly(Materials2Markers.SuperconductorUIV, 2081);
        wireOnly(Materials2Markers.SuperconductorUMV, 2089);
        wireOnly(Materials2Materials.Pentacadmiummagnesiumhexaoxid, 2200);
        wireOnly(Materials2Materials.Titaniumonabariumdecacoppereikosaoxid, 2220);
        wireOnly(Materials2Materials.Uraniumtriplatinid, 2240);
        wireOnly(Materials2Materials.Vanadiumtriindinid, 2260);
        wireOnly(Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 2280);
        wireOnly(Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid, 2300);
        wireOnly(Materials2Markers.SuperconductorMV, 2320);
        wireOnly(Materials2Markers.SuperconductorHV, 2340);
        wireOnly(Materials2Markers.SuperconductorEV, 2360);
        wireOnly(Materials2Markers.SuperconductorIV, 2380);
        wireOnly(Materials2Markers.SuperconductorLuV, 2400);
        wireOnly(Materials2Markers.SuperconductorZPM, 2420);
        wireOnly(Materials2Markers.SuperconductorUV, 2440);
        wireOnly(Materials2Materials.Longasssuperconductornameforuvwire, 2500);
        wireOnly(Materials2Materials.Longasssuperconductornameforuhvwire, 2520);
        wireOnly(Materials2Materials.Ichorium, 2600);
        wireOnly(Materials2Materials.SpaceTime, 2606);
        wireOnly(Materials2Materials.Hypogen, 30585);
        wireCable(Materials2Materials.RedstoneAlloy, 30645);
        wireCable(Materials2Materials.Lumiium, 32737);
        wireCable(Materials2Materials.Signalium, 32749);

        threeSizeFluidPipe(Materials2Materials.Wood, 5101);
        fluidPipe(Materials2Materials.Copper, 5110);
        fluidPipe(Materials2Materials.Bronze, 5120);
        fluidPipe(Materials2Materials.Steel, 5130);
        fluidPipe(Materials2Materials.StainlessSteel, 5140);
        fluidPipe(Materials2Materials.Titanium, 5150);
        fluidPipe(Materials2Materials.TungstenSteel, 5160, 5270);
        threeSizeFluidPipe(Materials2Materials.Redstone, 5165);
        fluidPipe(Materials2Materials.Plastic, 5170);
        fluidPipe(Materials2Materials.NiobiumTitanium, 5180);
        fluidPipe(Materials2Materials.Enderium, 5190);
        fluidPipe(Materials2Materials.Naquadah, 5200);
        fluidPipe(Materials2Materials.Neutronium, 5210);
        fluidPipe(Materials2Materials.NetherStar, 5220);
        fluidPipe(Materials2Materials.MysteriousCrystal, 5230);
        fluidPipe(Materials2Materials.DraconiumAwakened, 5240);
        fluidPipe(Materials2Materials.Infinity, 5250);
        fluidPipe(Materials2Materials.CastIron, 5260);
        fluidPipe(Materials2Materials.Polybenzimidazole, 5280, 5290);
        fluidPipe(Materials2Materials.SpaceTime, 5300);
        fluidPipe(Materials2Materials.TranscendentMetal, 5310);
        fluidPipe(Materials2Materials.Polytetrafluoroethylene, 5680);
        fluidPipe(Materials2Materials.RadoxPoly, 5760);
        fluidPipeNoMulti(Materials2Materials.TriniumNaquadahCarbonite, 30500);
        fluidPipeNoMulti(Materials2Materials.Staballoy, 30700);
        fluidPipeNoMulti(Materials2Materials.Tantalloy60, 30705);
        fluidPipeNoMulti(Materials2Materials.Tantalloy61, 30710);
        fluidPipeNoMulti(Materials2Materials.Void, 30715);
        fluidPipeNoMulti(Materials2Materials.Europium, 30720);
        fluidPipeNoMulti(Materials2Materials.Potin, 30725);
        fluidPipeNoMulti(Materials2Materials.MaragingSteel300, 30730);
        fluidPipeNoMulti(Materials2Materials.MaragingSteel350, 30735);
        fluidPipeNoMulti(Materials2Materials.Inconel690, 30740);
        fluidPipeNoMulti(Materials2Materials.Inconel792, 30745);
        fluidPipeNoMulti(Materials2Materials.HastelloyX, 30750);
        fluidPipeNoMulti(Materials2Materials.Tungsten, 30755);
        fluidPipeNoMulti(Materials2Materials.DarkSteel, 30760);
        fluidPipeNoMulti(Materials2Materials.Clay, 30765);
        fluidPipeNoMulti(Materials2Materials.Lead, 30770);
        fluidPipeNoMulti(Materials2Materials.Incoloy903, 30995);

        itemPipe(Materials2Materials.Tin, 5589);
        itemPipeIds(Materials2Materials.Brass, 5600, 5601, 5602, 5603, 5604, 5640, 5641, 5605, 5606, 5607);
        itemPipeIds(Materials2Materials.Electrum, 5610, 5611, 5612, 5613, 5614, 5642, 5643, 5615, 5616, 5617);
        itemPipeIds(Materials2Materials.Platinum, 5620, 5621, 5622, 5623, 5624, 5644, 5645, 5625, 5626, 5627);
        itemPipeIds(Materials2Materials.Osmium, 5630, 5631, 5632, 5633, 5634, 5646, 5647, 5635, 5636, 5637);
        itemPipe(Materials2Materials.ElectrumFlux, 5650);
        itemPipe(Materials2Materials.BlackPlutonium, 5660);
        itemPipe(Materials2Materials.Bedrockium, 5670);
        itemPipeNoSmallSizes(Materials2Materials.PolyvinylChloride, 5690);
        itemPipeNoSmallSizes(Materials2Materials.Nickel, 5700);
        itemPipeNoSmallSizes(Materials2Materials.Cobalt, 5710);
        itemPipeNoSmallSizes(Materials2Materials.Aluminium, 5720);
        itemPipe(Materials2Materials.Quantium, 5730);
    }

    private static void wireCable(Material material, int startId) {
        wireOnly(material, startId);
        putRange(
            material,
            startId + 6,
            Materials2PipeShapes.cableGt01,
            Materials2PipeShapes.cableGt02,
            Materials2PipeShapes.cableGt04,
            Materials2PipeShapes.cableGt08,
            Materials2PipeShapes.cableGt12,
            Materials2PipeShapes.cableGt16);
    }

    private static void wireOnly(Material material, int startId) {
        putRange(
            material,
            startId,
            Materials2PipeShapes.wireGt01,
            Materials2PipeShapes.wireGt02,
            Materials2PipeShapes.wireGt04,
            Materials2PipeShapes.wireGt08,
            Materials2PipeShapes.wireGt12,
            Materials2PipeShapes.wireGt16);
    }

    private static void fluidPipe(Material material, int startId) {
        fluidPipe(material, startId, startId + 5);
    }

    private static void fluidPipe(Material material, int startId, int multiFluidStartId) {
        fluidPipeNoMulti(material, startId);
        putRange(material, multiFluidStartId, Materials2PipeShapes.pipeQuadruple, Materials2PipeShapes.pipeNonuple);
    }

    private static void fluidPipeNoMulti(Material material, int startId) {
        putRange(
            material,
            startId,
            Materials2PipeShapes.pipeTiny,
            Materials2PipeShapes.pipeSmall,
            Materials2PipeShapes.pipeMedium,
            Materials2PipeShapes.pipeLarge,
            Materials2PipeShapes.pipeHuge);
    }

    private static void threeSizeFluidPipe(Material material, int startId) {
        putRange(
            material,
            startId,
            Materials2PipeShapes.pipeSmall,
            Materials2PipeShapes.pipeMedium,
            Materials2PipeShapes.pipeLarge);
    }

    private static void itemPipe(Material material, int startId) {
        itemPipeIds(
            material,
            IntStream.range(startId, startId + 10)
                .toArray());
    }

    private static void itemPipeIds(Material material, int... ids) {
        Shape[] sizes = { Materials2PipeShapes.itemPipeTiny, Materials2PipeShapes.itemPipeSmall,
            Materials2PipeShapes.itemPipeMedium, Materials2PipeShapes.itemPipeLarge, Materials2PipeShapes.itemPipeHuge,
            Materials2PipeShapes.itemPipeRestrictiveTiny, Materials2PipeShapes.itemPipeRestrictiveSmall,
            Materials2PipeShapes.itemPipeRestrictiveMedium, Materials2PipeShapes.itemPipeRestrictiveLarge,
            Materials2PipeShapes.itemPipeRestrictiveHuge };
        for (int i = 0; i < sizes.length; i++) {
            put(ids[i], sizes[i], material);
        }
    }

    private static void itemPipeNoSmallSizes(Material material, int startId) {
        putRange(
            material,
            startId,
            Materials2PipeShapes.itemPipeMedium,
            Materials2PipeShapes.itemPipeLarge,
            Materials2PipeShapes.itemPipeHuge,
            Materials2PipeShapes.itemPipeRestrictiveMedium,
            Materials2PipeShapes.itemPipeRestrictiveLarge,
            Materials2PipeShapes.itemPipeRestrictiveHuge);
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
