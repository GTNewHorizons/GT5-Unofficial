package gregtech.loaders.materials;

import java.util.function.Consumer;

import net.minecraft.enchantment.Enchantment;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;

/// `Materials` fields that generate no items themselves but must exist as `Materials` instances so the rest of
/// the code can name, identity-match, or associate composition against them:
///
/// - Name resolution: `gregtech.common.GTProxy#registerOre` calls `Materials.get(...)` on every ore-dictionary
/// registration another mod fires, so a foreign entry such as `gemCitrine`, `oreFluorite`, or `dustAgate`
/// unifies and ore-processes against one of these instead of falling through unrecognized.
/// - Ore-unification identity: the same handler compares the resolved material by identity (`aMaterial ==
/// Materials.Fluix`, `== Materials.Quartz`) to steer specific ore-dictionary families, so those instances
/// must be present and distinct.
/// - Composition association: `gregtech.loaders.preload.LoaderGTItemData` references a few of these as the
/// material of an `ItemData` (for example `Materials.Sand` for sand and sandstone recycling), so the field
/// must resolve to a real instance for that association to bind.
///
/// These fields carry no MaterialLib data and generate nothing themselves: `MaterialBuilder` never assigns
/// them a meta-item sub-id, so their `addDustItems`/`addGemItems`/`addOreItems`/`addCell` flags produce no
/// items, fluids, or composition. The flags are still reproduced faithfully because the original declarations
/// are the source of truth for the material's recognized attributes.
///
/// The declarations live in one [#MARKERS] table. Every marker sets a name and a display name, so those are
/// the two explicit columns; the remaining attributes vary so widely that a uniform primitive table would be
/// mostly empty columns and could not express the two tool-material entries (`InfusedDull`, `InfusedVis`) or
/// `Peanutwood`'s macerating link. Each row instead pairs its target field with a `Consumer<MaterialBuilder>`
/// that applies exactly the attributes that marker declares, so common markers stay one-liner rows and the
/// rich few remain exact.
public class RecognitionMaterials {

    private RecognitionMaterials() {}

    public static void load() {
        for (Marker marker : MARKERS) {
            MaterialBuilder builder = new MaterialBuilder().setName(marker.name())
                .setDefaultLocalName(marker.localName());
            marker.config()
                .accept(builder);
            marker.field()
                .accept(builder.constructMaterial());
        }
    }

    private record Marker(Consumer<Materials> field, String name, String localName, Consumer<MaterialBuilder> config) {}

    private static Marker marker(Consumer<Materials> field, String name, String localName,
        Consumer<MaterialBuilder> config) {
        return new Marker(field, name, localName, config);
    }

    private static final Marker[] MARKERS = {
        marker(
            m -> Materials.Adamite = m,
            "Adamite",
            "Adamite",
            b -> b.setMiningLevel(3)
                .setColor(Dyes.dyeLightGray)
                .addDustItems()
                .addOreItems()),
        marker(m -> Materials.Agate = m, "Agate", "Agate", b -> b.addDustItems()),
        marker(m -> Materials.Alfium = m, "Alfium", "Alfium", b -> b.addDustItems()),
        marker(m -> Materials.Ammonium = m, "Ammonium", "Ammonium", b -> b.addDustItems()),
        marker(m -> Materials.AncientDebris = m, "Debris", "Ancient Debris", b -> b.setARGB(0x351a0b)),
        marker(
            m -> Materials.Andesite = m,
            "Andesite",
            "Andesite",
            b -> b.setMiningLevel(2)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Aquamarine = m,
            "Aquamarine",
            "Aquamarine",
            b -> b.addDustItems()
                .addGemItems()),
        marker(
            m -> Materials.Bitumen = m,
            "Bitumen",
            "Bitumen",
            b -> b.setMiningLevel(2)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Black = m,
            "Black",
            "Black",
            b -> b.setColor(Dyes.dyeBlack)
                .setARGB(0x00000000)),
        marker(
            m -> Materials.Bloodstone = m,
            "Bloodstone",
            "Bloodstone",
            b -> b.setColor(Dyes.dyeRed)
                .addDustItems()),
        marker(
            m -> Materials.BrickNether = m,
            "BrickNether",
            "BrickNether",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_DULL)
                .removeOrePrefix(OrePrefixes.ingot)),
        marker(m -> Materials.Chimerite = m, "Chimerite", "Chimerite", b -> b.addDustItems()),
        marker(m -> Materials.Chrysocolla = m, "Chrysocolla", "Chrysocolla", b -> b.addDustItems()),
        marker(m -> Materials.Citrine = m, "Citrine", "Citrine", b -> b.addDustItems()),
        marker(
            m -> Materials.Cluster = m,
            "Cluster",
            "Cluster",
            b -> b.setColor(Dyes.dyeWhite)
                .setARGB(0x7fffffff)
                .addSubTag(SubTag.TRANSPARENT)),
        marker(
            m -> Materials.Cobblestone = m,
            "Cobblestone",
            "Cobblestone",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_DULL)),
        marker(
            m -> Materials.Coral = m,
            "Coral",
            "Coral",
            b -> b.setARGB(0x00ff80ff)
                .addDustItems()),
        marker(
            m -> Materials.Crystal = m,
            "Crystal",
            "Crystal",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_SHINY)
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)),
        marker(
            m -> Materials.CrystalFlux = m,
            "CrystalFlux",
            "Flux Crystal",
            b -> b.setIconSet(TextureSet.SET_QUARTZ)
                .setARGB(0x00643264)
                .addDustItems()
                .addGemItems()
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)),
        marker(
            m -> Materials.Cyanite = m,
            "Cyanite",
            "Cyanite",
            b -> b.setColor(Dyes.dyeCyan)
                .addDustItems()),
        marker(
            m -> Materials.DarkStone = m,
            "DarkStone",
            "Dark Stone",
            b -> b.setColor(Dyes.dyeBlack)
                .addDustItems()),
        marker(
            m -> Materials.Demonite = m,
            "Demonite",
            "Demonite",
            b -> b.setColor(Dyes.dyeRed)
                .addDustItems()),
        marker(
            m -> Materials.Draconic = m,
            "Draconic",
            "Draconic",
            b -> b.setColor(Dyes.dyeRed)
                .addDustItems()),
        marker(
            m -> Materials.Drulloy = m,
            "Drulloy",
            "Drulloy",
            b -> b.setColor(Dyes.dyeRed)
                .addDustItems()
                .addCell()),
        marker(m -> Materials.Ender = m, "Ender", "Ender", b -> b.addDustItems()),
        marker(m -> Materials.Energized = m, "Energized", "Energized", b -> {}),
        marker(
            m -> Materials.Fluix = m,
            "Fluix",
            "Fluix",
            b -> b.addDustItems()
                .addGemItems()
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.CRYSTALLISABLE)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.QUARTZ)),
        marker(
            m -> Materials.Fluorite = m,
            "Fluorite",
            "Fluorite",
            b -> b.setColor(Dyes.dyeGreen)
                .setMiningLevel(2)
                .addDustItems()
                .addOreItems()),
        marker(m -> Materials.Flux = m, "Flux", "Flux", b -> b.addDustItems()),
        marker(m -> Materials.Infernal = m, "Infernal", "Infernal", b -> {}),
        marker(
            m -> Materials.InfusedDull = m,
            "InfusedDull",
            "Vacuus",
            b -> b.setIconSet(TextureSet.SET_SHARDS)
                .setColor(Dyes.dyeLightGray)
                .setARGB(0x00646464)
                .setTool(64, 3, 32.0f)
                .setArmorEnchantment(() -> Enchantment.blastProtection, 4)
                .setFuel(MaterialBuilder.FuelType.Magic, 160)
                .addDustItems()
                .addGemItems()
                .addOreItems()
                .setOreMultiplier(2)
                .addToolHeadItems()
                .addGearItems()
                .addAspect(TCAspects.PRAECANTATIO, 1)
                .addAspect(TCAspects.VACUOS, 2)
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.MAGICAL)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.TRANSPARENT)
                .addSubTag(SubTag.UNBURNABLE)),
        marker(
            m -> Materials.InfusedTeslatite = m,
            "InfusedTeslatite",
            "Infused Teslatite",
            b -> b.setARGB(0x0064b4ff)),
        marker(
            m -> Materials.InfusedVis = m,
            "InfusedVis",
            "Auram",
            b -> b.setIconSet(TextureSet.SET_SHARDS)
                .setColor(Dyes.dyePurple)
                .setARGB(0x00ff00ff)
                .setTool(64, 3, 8.0f)
                .setToolEnchantment(() -> Enchantment.smite, 5)
                .setArmorEnchantment(() -> Enchantment.protection, 4)
                .setFuel(MaterialBuilder.FuelType.Magic, 240)
                .addDustItems()
                .addGemItems()
                .addOreItems()
                .setOreMultiplier(2)
                .addToolHeadItems()
                .addGearItems()
                .addAspect(TCAspects.PRAECANTATIO, 1)
                .addAspect(TCAspects.AURAM, 2)
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.MAGICAL)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.TRANSPARENT)
                .addSubTag(SubTag.UNBURNABLE)),
        marker(m -> Materials.Invisium = m, "Invisium", "Invisium", b -> b.addDustItems()),
        marker(
            m -> Materials.IridiumSodiumOxide = m,
            "IridiumSodiumOxide",
            "Iridium Sodium Oxide",
            b -> b.addDustItems()),
        marker(
            m -> Materials.Leather = m,
            "Leather",
            "Leather",
            b -> b.setIconSet(TextureSet.SET_ROUGH)
                .setColor(Dyes.dyeOrange)
                .setARGB(0x7f969650)
                .addDustItems()
                .addSubTag(SubTag.TRANSPARENT)),
        marker(m -> Materials.Limestone = m, "Limestone", "Limestone", b -> b.addDustItems()),
        marker(
            m -> Materials.Lodestone = m,
            "Lodestone",
            "Lodestone",
            b -> b.setMiningLevel(0)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Luminite = m,
            "Luminite",
            "Luminite",
            b -> b.setColor(Dyes.dyeWhite)
                .setARGB(0x00fafafa)
                .setMiningLevel(0)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Magma = m,
            "Magma",
            "Magma",
            b -> b.setColor(Dyes.dyeOrange)
                .setARGB(0x00ff4000)),
        marker(m -> Materials.Mawsitsit = m, "Mawsitsit", "Mawsitsit", b -> b.addDustItems()),
        marker(
            m -> Materials.Metal = m,
            "Metal",
            "Metal",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_METALLIC)
                .addSubTag(SubTag.METAL)),
        marker(
            m -> Materials.Meteorite = m,
            "Meteorite",
            "Meteorite",
            b -> b.setColor(Dyes.dyePurple)
                .setARGB(0x0050233c)
                .setMiningLevel(1)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Mimichite = m,
            "Mimichite",
            "Mimichite",
            b -> b.setIconSet(TextureSet.SET_GEM_VERTICAL)
                .setMiningLevel(1)
                .addDustItems()
                .addGemItems()
                .addOreItems()
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)),
        marker(
            m -> Materials.Moonstone = m,
            "Moonstone",
            "Moonstone",
            b -> b.setColor(Dyes.dyeWhite)
                .setMiningLevel(1)
                .addDustItems()
                .addOreItems()
                .addAspect(TCAspects.VITREUS, 1)
                .addAspect(TCAspects.ALIENIS, 1)),
        marker(m -> Materials.Mud = m, "Mud", "Mud", b -> b.setColor(Dyes.dyeBrown)),
        marker(m -> Materials.Mutation = m, "Mutation", "Mutation", b -> b.addDustItems()),
        marker(
            m -> Materials.Nano = m,
            "Nano",
            "Bio",
            b -> b.setColor(Dyes.dyeLightGray)
                .addAspect(TCAspects.ELECTRUM, 11)),
        marker(m -> Materials.Nether = m, "Nether", "Nether", b -> {}),
        marker(m -> Materials.Onyx = m, "Onyx", "Onyx", b -> b.addDustItems()),
        marker(
            m -> Materials.Organic = m,
            "Organic",
            "Organic",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_LEAF)),
        marker(m -> Materials.OsmiumTetroxide = m, "OsmiumTetroxide", "Osmium Tetroxide", b -> b.addDustItems()),
        marker(m -> Materials.Painite = m, "Painite", "Painite", b -> {}),
        marker(
            m -> Materials.Peanutwood = m,
            "Peanutwood",
            "Peanut Wood",
            b -> b.setMaceratingInto(() -> Materials.Wood)
                .addSubTag(SubTag.FLAMMABLE)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.WOOD)),
        marker(
            m -> Materials.Peat = m,
            "Peat",
            "Peat",
            b -> b.setColor(Dyes.dyeBrown)
                .addAspect(TCAspects.POTENTIA, 2)
                .addAspect(TCAspects.IGNIS, 2)),
        marker(
            m -> Materials.Petroleum = m,
            "Petroleum",
            "Petroleum",
            b -> b.setMiningLevel(1)
                .addDustItems()
                .addOreItems()),
        marker(m -> Materials.Pewter = m, "Pewter", "Pewter", b -> {}),
        marker(
            m -> Materials.Piko = m,
            "Piko",
            "Bio",
            b -> b.setColor(Dyes.dyeLightGray)
                .addAspect(TCAspects.ELECTRUM, 12)),
        marker(m -> Materials.Prismarine = m, "Prismarine", "Prismarine", b -> b.addSubTag(SubTag.NO_ORE_PROCESSING)),
        marker(m -> Materials.PurpleAlloy = m, "PurpleAlloy", "Purple Alloy", b -> b.setARGB(0x0064b4ff)),
        marker(
            m -> Materials.Quartz = m,
            "Quartz",
            "Quartz",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_QUARTZ)
                .addSubTag(SubTag.CRYSTAL)
                .addSubTag(SubTag.CRYSTALLISABLE)
                .addSubTag(SubTag.NO_SMASHING)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.QUARTZ)),
        marker(
            m -> Materials.Randomite = m,
            "Randomite",
            "Randomite",
            b -> b.setMiningLevel(1)
                .addDustItems()
                .addOreItems()),
        marker(
            m -> Materials.Red = m,
            "Red",
            "Red",
            b -> b.setColor(Dyes.dyeRed)
                .setARGB(0x00ff0000)),
        marker(m -> Materials.RubberTreeSap = m, "RubberTreeSap", "Rubber Tree Sap", b -> {}),
        marker(
            m -> Materials.Sand = m,
            "Sand",
            "Sand",
            b -> b.setColor(Dyes.dyeYellow)
                .setSmeltingInto(() -> Materials.Glass)
                .addSubTag(SubTag.NO_RECYCLING)),
        marker(m -> Materials.SodiumPeroxide = m, "SodiumPeroxide", "Sodium Peroxide", b -> b.addDustItems()),
        marker(m -> Materials.SolutionBlueVitriol = m, "SolutionBlueVitriol", "Blue Vitriol Solution", b -> {}),
        marker(m -> Materials.SolutionNickelSulfate = m, "SolutionNickelSulfate", "Nickel Sulfate Solution", b -> {}),
        marker(
            m -> Materials.Sunstone = m,
            "Sunstone",
            "Sunstone",
            b -> b.setMiningLevel(1)
                .setColor(Dyes.dyeYellow)
                .addDustItems()
                .addOreItems()
                .addAspect(TCAspects.VITREUS, 1)
                .addAspect(TCAspects.ALIENIS, 1)),
        marker(
            m -> Materials.TNT = m,
            "TNT",
            "TNT",
            b -> b.setColor(Dyes.dyeRed)
                .addAspect(TCAspects.PERDITIO, 7)
                .addAspect(TCAspects.IGNIS, 4)
                .addSubTag(SubTag.FLAMMABLE)
                .addSubTag(SubTag.EXPLOSIVE)
                .addSubTag(SubTag.NO_SMELTING)
                .addSubTag(SubTag.NO_SMASHING)),
        marker(
            m -> Materials.Tar = m,
            "Tar",
            "Tar",
            b -> b.setColor(Dyes.dyeBlack)
                .setARGB(0x000a0a0a)),
        marker(
            m -> Materials.Unknown = m,
            "Unknown",
            "Unknown",
            b -> b.setUnifiable(false)
                .setIconSet(TextureSet.SET_DULL)),
        marker(
            m -> Materials.UnstableIngot = m,
            "Unstableingot",
            "Unstable",
            b -> b.setColor(Dyes.dyeWhite)
                .setARGB(0x7fffffff)
                .addAspect(TCAspects.PERDITIO, 4)
                .addSubTag(SubTag.TRANSPARENT)),
        marker(
            m -> Materials.Voidstone = m,
            "Voidstone",
            "Voidstone",
            b -> b.setARGB(0xc8ffffff)
                .addAspect(TCAspects.VITREUS, 1)
                .addAspect(TCAspects.VACUOS, 1)
                .addSubTag(SubTag.TRANSPARENT)), };
}
