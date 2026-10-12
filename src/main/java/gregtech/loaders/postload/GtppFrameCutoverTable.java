package gregtech.loaders.postload;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.Materials;

/// Save migration table for gtPlusPlus frame blocks (`miscutils:blockFrameGt<Name>`). [PosteaTransformers] migrates
/// each listed material's block onto the [gregtech.api.enums.materials.TEBlockShapes#frameGt] shape.
///
/// Rows are never removed. A missing material orphans that block in existing worlds.
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
