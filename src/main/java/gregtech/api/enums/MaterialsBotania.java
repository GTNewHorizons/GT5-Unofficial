package gregtech.api.enums;

import gregtech.api.objects.MaterialStack;

import java.util.Arrays;

public class MaterialsBotania {

    public static Materials ManaSteel = new MaterialBuilder(796, TextureSet.SET_DULL, "Diphenylmethane 4,4′-diisocyanate").setName("DiphenylmethaneDiisocyanate").addDustItems().setRGB(255, 230, 50).setColor(Dyes.dyeYellow).setMeltingPoint(310).setMaterialList(new MaterialStack(Materials.Carbon, 15), new MaterialStack(Materials.Hydrogen, 10), new MaterialStack(Materials.Nitrogen, 2), new MaterialStack(Materials.Oxygen, 2)).setAspects(Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1), new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1))).constructMaterial();//C15H10N2O2



    /**
     * called by Materials. Can be safely called multiple times. exists to allow Materials ensure this class is initialized
     */

    public static void init() {
        // no-op. all work is done by <clinit>
    }
}
