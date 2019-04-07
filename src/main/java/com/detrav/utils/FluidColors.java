package com.detrav.utils;

import gregtech.api.enums.Materials;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

public class FluidColors {

    public static final HashMap<Integer, Integer> COLORS = new HashMap<Integer, Integer>(); //contains all FluidIDs and their color counterpath
    public static final ArrayList<Integer> USEDC = new ArrayList<Integer>(); //contains all used Colors


    public static synchronized void setup_colors_from_fluid_registry(){ //should populate COLORS with all the fluids from the Fluid registry and an unique color
        if (FluidRegistry.getMaxID()>0){
            for (int i = 0; i < FluidRegistry.getMaxID(); i++) {
                if(!USEDC.contains(FluidRegistry.getFluid(i).getColor())) {
                    USEDC.add(FluidRegistry.getFluid(i).getColor());
                    COLORS.put(i,FluidRegistry.getFluid(i).getColor());
                }else{
                    int nuclor=getnucolor();
                        USEDC.add(nuclor);
                        COLORS.put(i,nuclor);
                }

            }
        }
    }

    public static synchronized void setup_gt_colors(){ //should populate COLORS with all the fluids from the Materials and an unique color
        for (Materials M : Materials.getAll()){
            final int LCOLOR = M.getRGBA()[0]<<24 | M.getRGBA()[1]<<16 | M.getRGBA()[2]<<8 | M.getRGBA()[3];

            if (     M.mHasGas ||       //if Material has a Gas
                    (M.mFluid != null && !M.mFluid.equals(Materials._NULL.mFluid)) || //or a fluid
                    (M.getMolten(0).getFluid() != null && !M.getMolten(0).equals(Materials._NULL.mFluid))) //or can be molten add it here
                    if (!addnucolor(M,LCOLOR)){
                        boolean nosucess;
                        do {
                            nosucess=!addnucolor(M,getnucolor());
                        }while (nosucess);
                    }
        }
    }

    /**
     *
     * @return a new and unique color
     */
    private static synchronized int getnucolor(){ //gets a new unique color
        int nucolor = makenu();
        return !USEDC.contains(nucolor) ? nucolor : getnucolor(); //if the color already is in the list, recall this method and get a new random color.
    }

    /**
     *
     * @return a new random color
     */
    private static synchronized int makenu() {
        return  new SplittableRandom().nextInt(0,256)<<24 & 0xFF  | //r
                new SplittableRandom().nextInt(0,256)<<16 & 0xFF  | //g
                new SplittableRandom().nextInt(0,256)<<8  & 0xFF  | //b
                new SplittableRandom().nextInt(0,256)     & 0xFF;   //a
    }

    /**
     *  Should add a new and unique Color linked to a Material
     * @return if the color was added
     */
    private static synchronized boolean addnucolor(Materials M, int color){
        if (!USEDC.contains(color)){
            USEDC.add(color);
            if(M.mHasGas)
                COLORS.put(M.mGas.getID(), color);
            else if (M.mFluid != null && !M.mFluid.equals(Materials._NULL.mFluid))
                COLORS.put(M.mFluid.getID(), color);
            else if (M.getMolten(0).getFluid() != null && !M.getMolten(0).equals(Materials._NULL.mFluid))
                COLORS.put(M.getMolten(0).getFluid().getID(), color);
            return true;
        }
        return false;
    }
}
