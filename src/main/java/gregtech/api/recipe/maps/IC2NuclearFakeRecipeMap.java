package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

import java.util.HashSet;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;

public class IC2NuclearFakeRecipeMap extends RecipeMap {

    public IC2NuclearFakeRecipeMap() {
        super(
            new HashSet<>(10),
            "gt.recipe.ic2nuke",
            "Fission",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true);
        setLogo(GT_UITextures.PICTURE_RADIATION_WARNING);
        setLogoPos(152, 24);
        setNEIBackgroundSize(172, 60);
        setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    }

    /**
     * Add a breeder cell.
     *
     * @param input          raw stack. should be undamaged.
     * @param output         breed output
     * @param heatMultiplier bonus progress per neutron pulse per heat step
     * @param heatStep       divisor for hull heat
     * @param reflector      true if also acts as a neutron reflector, false otherwise.
     * @param requiredPulses progress required to complete breeding
     * @return added fake recipe
     */
    public GT_Recipe addBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
        int heatMultiplier, int requiredPulses) {
        return addFakeRecipe(
            input,
            output,
            reflector ? "Neutron reflecting breeder cell" : "Heat neutral Breeder Cell",
            String.format("Every %d reactor hull heat", heatStep),
            String.format("increase speed by %d00%%", heatMultiplier),
            String.format("Required pulses: %d", requiredPulses));
    }

    public GT_Recipe addFakeRecipe(ItemStack input, ItemStack output, String... neiDesc) {
        GT_Recipe r = new GT_Recipe(
            new ItemStack[] { input },
            new ItemStack[] { output },
            null,
            new int[] { 10000 },
            null,
            null,
            0,
            0,
            0);
        r.setNeiDesc(neiDesc);
        return addRecipe(r, true, true, false);
    }
}
