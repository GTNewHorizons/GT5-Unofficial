package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.loaders.postload.MachineRecipeLoader.solderingMats;

public class SubstituteFluidStack {

    public final List<FluidStack> fluidStacks;

    public SubstituteFluidStack(FluidStack... fluidStacks) {
        if (fluidStacks == null) {
            this.fluidStacks = Collections.emptyList();
            return;
        }

        List<FluidStack> list = new ArrayList<>();
        for (FluidStack fs : fluidStacks) {
            if (fs != null && fs.getFluid() != null) {
                list.add(fs.copy());
            }
        }
        this.fluidStacks = Collections.unmodifiableList(list);
    }

    public static SubstituteFluidStack soldering(long baseAmount) {
        if (baseAmount <= 0) baseAmount = 144;

        List<FluidStack> fluids = new ArrayList<>();

        for (Materials material : solderingMats) {
            int multiplier = material.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : material.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4
                : 2;

            fluids.add(material.getMolten(baseAmount * multiplier));
        }

        return new SubstituteFluidStack(fluids.toArray(new FluidStack[0]));
    }
}
