package gregtech.loaders.postload;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.Materials;

/// The materials whose gtPlusPlus frame block (`miscutils:blockFrameGt<Name>`, one distinct registered block
/// per material) a saved world can still hold, for [PosteaTransformers]' migration onto the
/// [gregtech.api.enums.materials.PipeShapes#frameGt] shape. Frozen: a name leaving this list orphans that
/// block in every world that has one placed.
public final class GtppFrameCutoverTable {

    private GtppFrameCutoverTable() {}

    // spotless:off
    public static Material[] materials() {
        return new Material[] {
        Materials.AbyssalAlloy, Materials.Arcanite, Materials.ArceusAlloy2B,
        Materials.AstralTitanium, Materials.BlackMetal, Materials.BloodSteel,
        Materials.Botmium, Materials.CelestialTungsten, Materials.ChromaticGlass,
        Materials.CinobiteA243, Materials.Dragonblood, Materials.EglinSteel,
        Materials.EnergyCrystal, Materials.Germanium, Materials.Grisium,
        Materials.HS188A, Materials.HastelloyC276, Materials.HastelloyN,
        Materials.HastelloyW, Materials.HastelloyX, Materials.HeLiCoPtEr,
        Materials.Hypogen, Materials.Incoloy020, Materials.IncoloyDS,
        Materials.IncoloyMA956, Materials.Inconel625, Materials.Inconel690,
        Materials.Inconel792, Materials.Iodine, Materials.LafiumCompound,
        Materials.Laurenium, Materials.MaragingSteel250, Materials.MaragingSteel300,
        Materials.MaragingSteel350, Materials.NiobiumCarbide, Materials.Nitinol60,
        Materials.Octiron, Materials.Pikyonium64B, Materials.Potin,
        Materials.Quantum, Materials.Rhenium, Materials.Selenium,
        Materials.SiliconCarbide, Materials.Staballoy, Materials.Stellite,
        Materials.Talonite, Materials.Tantalloy60, Materials.Tantalloy61,
        Materials.TantalumCarbide, Materials.Thallium, Materials.Titansteel,
        Materials.TriniumNaquadahCarbonite, Materials.TriniumTitaniumAlloy, Materials.Tumbaga,
        Materials.TungstenTitaniumCarbide, Materials.WatertightSteel, Materials.Zeron100,
        Materials.ZirconiumCarbide };
    }
    // spotless:on
}
