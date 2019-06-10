/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.OrePrefixes.*;

public class AdditionalRecipes implements Runnable {

    @Override
    public void run() {
        //Cubic Circonia
        GT_Values.RA.addChemicalRecipe(Materials.Yttrium.getDust(2), GT_Utility.getIntegratedCircuit(11),Materials.Oxygen.getGas(3000),null, WerkstoffLoader.YttriumOxide.get(dust,5),64, BW_Util.getMachineVoltageFromTier(4));
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(false, new ItemStack[]{WerkstoffLoader.Zirconium.get(dust,10), WerkstoffLoader.YttriumOxide.get(dust)}, new ItemStack[]{WerkstoffLoader.YttriumOxide.get(dust), WerkstoffLoader.Zirconia.get(gemFlawed, 40)}, (Object) null, (int[]) null, new FluidStack[]{Materials.Oxygen.getGas(20000)}, null, 14400, BW_Util.getMachineVoltageFromTier(4), 2953);
        //Thorium/Yttrium Glas
        GT_Values.RA.addBlastRecipe(WerkstoffLoader.YttriumOxide.get(dustSmall,2),WerkstoffLoader.Thorianit.get(dustSmall,2),Materials.Glass.getMolten(144),null,new ItemStack(ItemRegistry.bw_glasses[0],1,12),null,800,BW_Util.getMachineVoltageFromTier(5),3663);
        //Thorianit recipes
        GT_Values.RA.addSifterRecipe(WerkstoffLoader.Thorianit.get(crushedPurified),
                new ItemStack[]{
                        WerkstoffLoader.Thorianit.get(dust),
                        WerkstoffLoader.Thorianit.get(dust),
                        WerkstoffLoader.Thorianit.get(dust),
                        Materials.Thorium.getDust(1),
                        Materials.Thorium.getDust(1),
                        WerkstoffLoader.Thorium232.get(dust),
                }, new int[]{7000,1300,700,600,300,100},
                400,
                BW_Util.getMachineVoltageFromTier(5)
                );
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(dust),Materials.Aluminium.getDust(1),Materials.Thorium.getDust(1),1000);
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(dust),Materials.Magnesium.getDust(1),Materials.Thorium.getDust(1),1000);
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(crushed), ItemList.Crop_Drop_Thorium.get(9),Materials.Water.getFluid(1000),Materials.Thorium.getMolten(144),WerkstoffLoader.Thorianit.get(crushedPurified,4),96,24);
    }

}
