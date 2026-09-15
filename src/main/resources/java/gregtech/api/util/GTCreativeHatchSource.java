package gregtech.api.util;

import static gregtech.api.enums.MetaTileEntityIDs.AutoTapingMaintenanceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.DebugDataAccessHatch;
import static gregtech.api.enums.MetaTileEntityIDs.DebugDataHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_DEBUG;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Debug_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_DEBUG;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_DEBUG;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_ME;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IItemSource;

import gregtech.api.GregTechAPI;

public class GTCreativeHatchSource implements IItemSource {

    public static GTCreativeHatchSource instance = new GTCreativeHatchSource();

    private final List<ItemStack> allHatches = new ArrayList<>();

    public GTCreativeHatchSource() {
        addHatchById(ENERGY_HATCH_DEBUG.ID);
        addHatchById(AutoTapingMaintenanceHatch.ID);
        addHatchById(MUFFLER_HATCH_UHV.ID);
        addHatchById(INPUT_BUS_DEBUG.ID);
        addHatchById(INPUT_HATCH_DEBUG.ID);
        addHatchById(OUTPUT_BUS_ME.ID);
        addHatchById(OUTPUT_HATCH_ME.ID);
        addHatchById(DebugDataHatch.ID);
        addHatchById(DebugDataAccessHatch.ID);
        addHatchById(Hatch_Input_Debug_Steam.ID);

        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                addHatchById(i);
            }
        }
    }

    private void addHatchById(int id) {
        allHatches.add(new ItemStack(GregTechAPI.sBlockMachines, 1, id));
    }

    @Override
    public @NotNull Map<ItemStack, Integer> take(Predicate<ItemStack> predicate, boolean simulate, int count) {
        Map<ItemStack, Integer> store = new HashMap<>();
        for (ItemStack hatch : allHatches) {
            if (predicate.test(hatch)) {
                store.put(hatch, Integer.MAX_VALUE);
                return store;
            }
        }
        return store;
    }

    @Override
    public boolean takeOne(ItemStack stack, boolean simulate) {
        return true;
    }

    @Override
    public boolean takeAll(ItemStack stack, boolean simulate) {
        return true;
    }
}
