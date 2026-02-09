package gregtech.api.interfaces;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

public interface EssentiaMultiblock {

    default int getEssentiaInputHatchCount() {
        return 0;
    }

    default boolean addEssentiaInputHatch(EssentiaInputHatch hatch) {
        return false;
    }

    default int getEssentiaOutputHatchCount() {
        return 0;
    }

    default boolean addEssentiaOutputHatch(EssentiaOutputHatch hatch) {
        return false;
    }

    static <T extends MTEMultiBlockBase> boolean addEssentiaInput(T multi, IGregTechTileEntity hatchGTE,
        int casingIndex) {
        if (!(multi instanceof EssentiaMultiblock emulti)) {
            throw new IllegalStateException("Multiblock MTE must implement EssentiaMultiblock: " + multi);
        }

        if (!(hatchGTE.getMetaTileEntity() instanceof EssentiaInputHatch hatch)) {
            throw new IllegalStateException(
                "Essentia hatch MTE must implement EssentiaInputHatch: " + hatchGTE.getMetaTileEntity());
        }

        if (emulti.addEssentiaInputHatch(hatch)) {
            hatch.updateCraftingIcon(multi.getMachineCraftingIcon());
            hatch.updateTexture(casingIndex);
            return true;
        } else {
            return false;
        }
    }

    static <T extends MTEMultiBlockBase> boolean addEssentiaOutput(T multi, IGregTechTileEntity hatchGTE,
        int casingIndex) {
        if (!(multi instanceof EssentiaMultiblock emulti)) {
            throw new IllegalStateException("Multiblock MTE must implement EssentiaMultiblock: " + multi);
        }

        if (!(hatchGTE.getMetaTileEntity() instanceof EssentiaOutputHatch hatch)) {
            throw new IllegalStateException(
                "Essentia hatch MTE must implement EssentiaOutputHatch: " + hatchGTE.getMetaTileEntity());
        }

        if (emulti.addEssentiaOutputHatch(hatch)) {
            hatch.updateCraftingIcon(multi.getMachineCraftingIcon());
            hatch.updateTexture(casingIndex);
            return true;
        } else {
            return false;
        }
    }
}
