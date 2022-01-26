package com.elisis.gtnhlanth.common.register;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff.GenerationFeatures;

import gregtech.api.enums.TextureSet;

@SuppressWarnings({"unchecked"})
public class WerkstoffMaterialPool implements Runnable {
    
    private static final int offsetID = 11_000;
    
    //Misc.
    public static final Werkstoff Hafnium = new Werkstoff(
            new short[] {232, 224, 219},
            "Hafnium",
            subscriptNumbers("Hf"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust().addMetalItems().enforceUnification(), //Perhaps use hafnia liquid in elemental hafnium synthesis
            offsetID,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff LowPurityHafnium = new Werkstoff(
            new short[] {240, 223, 208},
            "Low-Purity Hafnium",
            subscriptNumbers("??Hf??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(), //Perhaps use hafnia liquid in elemental hafnium synthesis
            offsetID + 1,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff Hafnia = new Werkstoff(
            new short[] {247, 223, 203},
            "Hafnia",
            subscriptNumbers("HfO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(), //Perhaps use hafnia liquid in elemental hafnium synthesis
            offsetID + 2,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff HafniumTetrachloride = new Werkstoff(
            new short[] {238, 247, 249},
            "Hafnium Tetrachloride",
            subscriptNumbers("HfCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 3,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff HafniumTetrachlorideSolution = new Werkstoff(
            new short[] {238, 247, 249},
            "Hafnium Tetrachloride Solution",
            subscriptNumbers("HfCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 4,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff HafniumIodide = new Werkstoff(
            new short[] {216, 60, 1},
            "Hafnium Iodide",
            subscriptNumbers("HfI4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 5,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff HafniumRunoff = new Werkstoff(
            new short[] {74, 65, 42}, //Literally the statistically ugliest colour
            "Hafnium Runoff",
            subscriptNumbers("??????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 6,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff Zirconium = new Werkstoff(
            new short[] {225,230,225},
            "Zirconium",
            subscriptNumbers("Zr"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addMetalItems().enforceUnification(),
            offsetID + 7,
            TextureSet.SET_DULL
            
        );
    
    public static final Werkstoff Zirconia = new Werkstoff(
            new short[] {177,152,101},
            "Zirconia",
            subscriptNumbers("ZrO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 8,
            TextureSet.SET_DULL
            
        );
    
    public static final Werkstoff ZirconiumTetrachloride = new Werkstoff(
            new short[] {179, 164, 151},
            "Zirconium Tetrachloride",
            subscriptNumbers("ZrCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 9,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff ZirconiumTetrachlorideSolution = new Werkstoff(
            new short[] {179, 164, 151},
            "Zirconium Tetrachloride Solution",
            subscriptNumbers("ZrCl4"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells(), //Blast Furnace needs liquid input because it can't do 3 item inputs so have a shitty material
            offsetID + 10,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff HafniaZirconiaBlend = new Werkstoff(
            new short[] {247, 223, 203},
            "Hafnia-Zirconia Blend", // Maybe Hafnon??
            subscriptNumbers("??HfZr??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 11,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff Iodine = new Werkstoff(
            new short[] {171, 40, 175},
            "Iodine",
            subscriptNumbers("I"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(),
            offsetID + 12,
            TextureSet.SET_FLUID
        );
    
    
    //Lanthanide Line
    public static final Werkstoff MuddyRareEarthSolution = new Werkstoff(
            new short[] {111, 78, 55},
            "Muddy Rare Earth Solution",
            subscriptNumbers("??LaNdZr??"),
            new Werkstoff.Stats(),     
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 14,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff DilutedRareEarthMud = new Werkstoff(
            new short[] {160, 120, 90},
            "Diluted Rare Earth Mud",
            subscriptNumbers("??LaNdHf??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 15,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff DilutedMonaziteSulfate = new Werkstoff(
            new short[] {237, 201, 175},
            "Diluted Monazite Sulfate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 16,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff NitratedRareEarthConcentrate = new Werkstoff(
            new short[] {250, 223, 173},
            "Nitrated Rare Earth Concentrate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 17,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff NitricLeachedConcentrate = new Werkstoff(
            new short[] {244, 202, 22},
            "Nitric Leached Concentrate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 18,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff MonaziteSulfate = new Werkstoff(
            new short[] {152, 118, 84},
            "Monazite Sulfate",
            subscriptNumbers("??CeEu??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 19,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff AcidicMonazitePowder = new Werkstoff(
            new short[] {50, 23, 77},
            "Acidic Monazite Powder",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 20,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff RareEarthFiltrate = new Werkstoff(
            new short[] {72, 60, 50},
            "Rare Earth Filtrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 21,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff NeutralizedRareEarthFiltrate = new Werkstoff(
            new short[] {50, 23, 77},
            "Neutralized Rare Earth Filtrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 22,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff RareEarthHydroxideConcentrate = new Werkstoff(
            new short[] {193, 154, 107},
            "Rare Earth Hydroxide Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 23,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff DriedRareEarthConcentrate = new Werkstoff(
            new short[] {250, 214, 165},
            "Dried Rare Earth Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 24,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff CeriumDioxide = new Werkstoff(
            new short[] {255, 255, 255},
            "Cerium Dioxide",
            subscriptNumbers("CeO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust().enforceUnification(),
            offsetID + 25,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff CeriumChloride = new Werkstoff(
            new short[] {255, 255, 255},
            "Cerium Chloride",
            subscriptNumbers("CeCl3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 26,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff CeriumOxalate = new Werkstoff(
            new short[] {255, 255, 224},
            "Cerium Oxalate",
            subscriptNumbers("Ce2(C2O4)3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 27,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff CeriumIIIOxide = new Werkstoff(
            new short[] {255, 255, 102},
            "Cerium (III) Oxide",
            subscriptNumbers("Ce2O3"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 28,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff CeriumRichMixture = new Werkstoff(
    		new short[] {244, 164, 96},
    		"Cerium-Rich Mixture",
    		subscriptNumbers("??Ce??"),
    		new Werkstoff.Stats(),
    		Werkstoff.Types.COMPOUND,
    		new Werkstoff.GenerationFeatures().disable().onlyDust(),
    		offsetID + 29,
    		TextureSet.SET_DULL	
    	);
    
    public static final Werkstoff CooledRareEarthConcentrate = new Werkstoff(
            new short[] {250, 214, 165},
            "Cooled Rare Earth Concentrate",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 30,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff RarerEarthSediment = new Werkstoff(
            new short[] {250, 214, 165},
            "Rarer Earth Sediment",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 31,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff HeterogenousHalogenicRareEarthMixture = new Werkstoff(
            new short[] {250, 214, 165},
            "Heterogenous Halogenic Rare Earth Mixture",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 32,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff SaturatedRareEarthMixture = new Werkstoff(
            new short[] {250, 214, 165},
            "Saturated Rare Earth Mixture",
            subscriptNumbers("????"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().onlyDust(),
            offsetID + 33,
            TextureSet.SET_DULL
        );
    
    public static final Werkstoff SamaricResidue = new Werkstoff(
    		new short[] {248, 243, 231},
    		"Samaric Residue",
    		subscriptNumbers("??SmGd??"),
    		new Werkstoff.Stats(),
    		Werkstoff.Types.MIXTURE,
    		new Werkstoff.GenerationFeatures().disable().onlyDust(),
    		offsetID + 34,
    		TextureSet.SET_DULL
    	);
    
    public static final Werkstoff MonaziteResidue = new Werkstoff(
    		new short[] {64, 69, 62},
    		subscriptNumbers("??ZrHfTh??"),
    		new Werkstoff.Stats(),
    		Werkstoff.Types.MIXTURE,
    		new Werkstoff.GenerationFeatures().disable().onlyDust(),
    		offsetID + 35,
    		TextureSet.SET_DULL
    	);
    		
    		
    
    
    		
    
    @Override
    public void run() {
        
    }
    

}
