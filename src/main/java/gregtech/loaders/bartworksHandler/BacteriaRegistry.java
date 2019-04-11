package gregtech.loaders.bartworksHandler;

import com.github.bartimaeusnek.bartworks.API.BioObjectAdder;
import com.github.bartimaeusnek.bartworks.API.BioRecipeAdder;
import com.github.bartimaeusnek.bartworks.util.*;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.LinkedHashMap;

import static com.github.bartimaeusnek.bartworks.API.BioObjectAdder.*;
import static com.github.bartimaeusnek.bartworks.API.BioRecipeAdder.addBacterialVatRecipe;
import static com.github.bartimaeusnek.bartworks.API.BioRecipeAdder.addBioLabRecipeIncubation;
import static gregtech.api.enums.Materials.*;

public class BacteriaRegistry {

    private final static LinkedHashMap<String,BioCulture> CultureSet = new LinkedHashMap<>();

    public void runAllPostinit(){
        runLateBioOBJs();
        runBWRecipes();
        CultureSet.clear(); //deletes map, not used anymore
    }

    private void runLateBioOBJs(){
        BioData bioData = createAndRegisterBioData( //BioData because Plasmis == DNA
                "Barnadafis Arboriatoris", //Name
                EnumRarity.rare, //rare (only visual)
                750, //7.5% chance of getting it
                500000 //UV
        );
        BioCulture bioCulture = createAndRegisterBioCulture(
                new Color(133, 0, 128), //color = violet
                "Barnadafis Arboriatoris", //name
                BioPlasmid.convertDataToPlasmid(bioData), //BioData -> plasmid
                BioDNA.convertDataToDNA(bioData), ///BioData -> DNA
                EnumRarity.rare, //rare (only visual)
                true //can be multiplied in the BioVat
        );
        CultureSet.put("BarnadaCBac", bioCulture); //save it in a Map to get it later

        //TCetiE culture, same as above
        bioData = createAndRegisterBioData("TCetiEis Fucus Serratus",EnumRarity.rare, 750,2);
        bioCulture = createAndRegisterBioCulture(new Color(27, 153, 94),"TCetiEis Fucus Serratus",BioPlasmid.convertDataToPlasmid(bioData),BioDNA.convertDataToDNA(bioData),EnumRarity.rare,true);
        CultureSet.put("TcetiEBac", bioCulture);

        //combined Culture
        bioCulture = createAndRegisterBioCulture(
                new Color(54, 119, 181),
                "Xenoxene Xenoxsis",
                CultureSet.get("BarnadaCBac").getPlasmid(), //Barnada Plasmid
                CultureSet.get("TcetiEBac").getdDNA(), //TcetiE DNA
                EnumRarity.epic,
                false
        );

        CultureSet.put("CombinedBac",bioCulture);
        regenerateBioFluids(); //this will generate bacteria fluids. needs to be called AFTER ALL breedable bacterias have been registered.
    }

    private void runBWRecipes(){
    	if(Loader.isModLoaded("GalaxySpace")){
        addBioLabRecipeIncubation(GT_ModHandler.getModItem("GalaxySpace","barnardaClog",1L),CultureSet.get("BarnadaCBac"),new int[]{250}, FluidRegistry.getFluidStack("unknowwater",8000),500, 500000,0);
        addBioLabRecipeIncubation(GT_ModHandler.getModItem("GalaxySpace", "tcetiedandelions", 1L, 0),CultureSet.get("TcetiEBac"),new int[]{250}, FluidRegistry.getFluidStack("unknowwater",8000),500, 500000,0);
        addBioLabRecipeIncubation(GT_ModHandler.getModItem("GalaxySpace", "tcetiedandelions", 1L, 3),CultureSet.get("TcetiEBac"),new int[]{250}, FluidRegistry.getFluidStack("unknowwater",8000),500, 500000,0);
        addBioLabRecipeIncubation(GT_ModHandler.getModItem("GalaxySpace", "tcetiedandelions", 1L, 4),CultureSet.get("TcetiEBac"),new int[]{250}, FluidRegistry.getFluidStack("unknowwater",8000),500, 500000,0);
        addBioLabRecipeIncubation(GT_ModHandler.getModItem("GalaxySpace", "tcetiedandelions", 1L, 5),CultureSet.get("TcetiEBac"),new int[]{250}, FluidRegistry.getFluidStack("unknowwater",8000),500, 500000,0);
        
        addBacterialVatRecipe(new ItemStack[]{Materials.AntimonyTrioxide.getDust(16),Materials.Osmium.getDust(16)},CultureSet.get("CombinedBac"),new FluidStack[]{Materials.Oil.getFluid(1000)},new FluidStack[]{Materials.Xenoxene.getFluid(1)},3600,500000,Materials.NaquadahEnriched,8,0,false);
    	}
    }
}
