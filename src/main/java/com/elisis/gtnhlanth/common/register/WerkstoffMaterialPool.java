package com.elisis.gtnhlanth.common.register;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.subscriptNumbers;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.TextureSet;

@SuppressWarnings({"unchecked"})
public class WerkstoffMaterialPool implements Runnable {
    
    private static final int offsetID = 11_000;
    
    //Misc.
    public static final Werkstoff Hafnia = new Werkstoff(
            new short[] {247, 223, 203},
            "Hafnia",
            subscriptNumbers("HfO2"),
            new Werkstoff.Stats(),
            Werkstoff.Types.COMPOUND,
            new Werkstoff.GenerationFeatures().disable().onlyDust(), //Perhaps use hafnia liquid in elemental hafnium synthesis
            offsetID,
            TextureSet.SET_DULL
        );
    
    
    //Lanthanide Line
    public static final Werkstoff MuddyRareEarthSolution = new Werkstoff(
            new short[] {111, 78, 55},
            "Muddy Rare Earth Solution",
            subscriptNumbers("??LaNdZr??"),
            new Werkstoff.Stats(),     
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 1,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff DilutedRareEarthMud = new Werkstoff(
            new short[] {160, 120, 90},
            "Diluted Rare Earth Mud",
            subscriptNumbers("??LaNdHf??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 2,
            TextureSet.SET_FLUID
        );
    
    public static final Werkstoff DilutedMonaziteSulfate = new Werkstoff(
            new short[] {237, 201, 175},
            "Diluted Monazite Sulfate",
            subscriptNumbers("??LaNd??"),
            new Werkstoff.Stats(),
            Werkstoff.Types.MIXTURE,
            new Werkstoff.GenerationFeatures().disable().addCells(),
            offsetID + 3,
            TextureSet.SET_FLUID
        );
    

    @Override
    public void run() {
        
    }
    

}
