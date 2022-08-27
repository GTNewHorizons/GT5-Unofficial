/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.recipe;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.capsuleMolten;
import static gregtech.api.enums.OrePrefixes.*;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import java.util.Objects;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MoltenCellLoader implements IWerkstoffRunnable {
    @Override
    public void run(Werkstoff werkstoff) {
        if (!werkstoff.hasItemType(WerkstoffLoader.cellMolten)) return;

        if (!werkstoff.hasItemType(ingot)) {
            if (!werkstoff.hasItemType(dust)) return;
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(dust),
                    null,
                    werkstoff.getMolten(144),
                    0,
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(dustSmall),
                    null,
                    werkstoff.getMolten(36),
                    0,
                    (int) ((double) werkstoff.getStats().getMass() / 4D),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(dustTiny),
                    null,
                    werkstoff.getMolten(16),
                    0,
                    (int) ((double) werkstoff.getStats().getMass() / 9D),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
        } else {

            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(ingot),
                    null,
                    werkstoff.getMolten(144),
                    0,
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);

            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(nugget),
                    null,
                    werkstoff.getMolten(16),
                    0,
                    (int) ((double) werkstoff.getStats().getMass() / 9D),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);

            GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Ingot.get(0),
                    werkstoff.getMolten(144),
                    werkstoff.get(ingot),
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            // GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0), werkstoff.getMolten(144),
            // werkstoff.get(block), (int) werkstoff.getStats().getMass(), werkstoff.getStats().getMass() > 128 ? 64 :
            // 30);
            GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Nugget.get(0),
                    werkstoff.getMolten(16),
                    werkstoff.get(nugget),
                    (int) ((double) werkstoff.getStats().getMass() / 9D),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Block.get(0),
                    werkstoff.getMolten(1296),
                    werkstoff.get(block),
                    (int) werkstoff.getStats().getMass() * 9,
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);

            if (!werkstoff.hasItemType(plate)) return;

            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(stickLong),
                    null,
                    werkstoff.getMolten(144),
                    0,
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(plate),
                    null,
                    werkstoff.getMolten(144),
                    0,
                    (int) werkstoff.getStats().getMass(),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
            GT_Values.RA.addFluidExtractionRecipe(
                    werkstoff.get(stick),
                    null,
                    werkstoff.getMolten(72),
                    0,
                    (int) ((double) werkstoff.getStats().getMass() / 2D),
                    werkstoff.getStats().getMass() > 128 ? 64 : 30);
        }

        // Tank "Recipe"
        final FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144),
                werkstoff.get(WerkstoffLoader.cellMolten),
                Materials.Empty.getCells(1));
        FluidContainerRegistry.registerFluidContainer(
                werkstoff.getMolten(144), werkstoff.get(WerkstoffLoader.cellMolten), Materials.Empty.getCells(1));
        GT_Utility.addFluidContainerData(data);
        GT_Values.RA.addFluidCannerRecipe(
                Materials.Empty.getCells(1),
                werkstoff.get(WerkstoffLoader.cellMolten),
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144),
                GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(
                werkstoff.get(WerkstoffLoader.cellMolten),
                Materials.Empty.getCells(1),
                GT_Values.NF,
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144));

        if (!LoaderReference.Forestry) return;

        final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144),
                werkstoff.get(capsuleMolten),
                GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
        FluidContainerRegistry.registerFluidContainer(
                werkstoff.getMolten(144),
                werkstoff.get(capsuleMolten),
                GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
        GT_Utility.addFluidContainerData(emptyData);
        GT_Values.RA.addFluidCannerRecipe(
                werkstoff.get(capsuleMolten),
                GT_Values.NI,
                GT_Values.NF,
                new FluidStack(Objects.requireNonNull(WerkstoffLoader.molten.get(werkstoff)), 144));
    }
}
