package gregtech.common.bees;

import java.awt.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpeciesCustom;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleBeeSpecies;

public class GT_AlleleBeeSpecies extends AlleleBeeSpecies {

    public GT_AlleleBeeSpecies(String uid, boolean dominant, String unlocalizedName, String authority,
        String unlocalizedDescription, IClassification branch, String binomial, Color primaryColor,
        Color secondaryColor) {
        super(
            uid,
            unlocalizedName,
            authority,
            unlocalizedDescription,
            dominant,
            branch,
            binomial,
            primaryColor.getRGB(),
            secondaryColor.getRGB());
        AlleleManager.alleleRegistry.registerAllele(this, EnumBeeChromosome.SPECIES);
    }

    @Override
    public IAlleleBeeSpeciesCustom addProduct(ItemStack product, Float chance) {
        if (product == null || product.getItem() == null) {
            product = new ItemStack(Items.boat);
        }
        if (chance <= 0.0f || chance > 1.0f) {
            chance = 0.1f;
        }
        return super.addProduct(product, chance);
    }

    @Override
    public IAlleleBeeSpeciesCustom addSpecialty(ItemStack specialty, Float chance) {
        if (specialty == null || specialty.getItem() == null) {
            specialty = new ItemStack(Items.boat);
        }
        if (chance <= 0.0f || chance > 1.0f) {
            chance = 0.1f;
        }
        return super.addSpecialty(specialty, chance);
    }
}
