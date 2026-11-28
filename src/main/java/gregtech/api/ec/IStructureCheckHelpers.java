package gregtech.api.ec;

import static gregtech.api.enums.ECHatchElement.Dynamo;
import static gregtech.api.enums.ECHatchElement.Energy;
import static gregtech.api.enums.ECHatchElement.ExoticDynamo;
import static gregtech.api.enums.ECHatchElement.ExoticEnergy;
import static gregtech.api.enums.ECHatchElement.InputBus;
import static gregtech.api.enums.ECHatchElement.InputHatch;
import static gregtech.api.enums.ECHatchElement.OutputBus;
import static gregtech.api.enums.ECHatchElement.OutputHatch;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.multiblock.IMultiblockEnergyInputLogicComponent;
import gregtech.api.enums.ECHatchElement;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;

public interface IStructureCheckHelpers {

    @NotNull
    IECMultiblock asMultiblock();

    default void checkHatchMin(List<StructureError> errors, ECHatchElement element, int min) {
        int count = (int) element.count(asMultiblock());
        if (count < min) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, element, count, min));
        }
    }

    default void checkHatchExact(List<StructureError> errors, ECHatchElement element, int target) {
        int count = (int) element.count(asMultiblock());
        if (count != target) {
            errors.add(StructureErrors.hatchCount(ErrorType.NOT_MATCH, element, count, target));
        }
    }

    default void checkHatchMax(List<StructureError> errors, ECHatchElement element, int max) {
        int count = (int) element.count(asMultiblock());
        if (count > max) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, element, count, max));
        }
    }

    default void checkCasingMin(List<StructureError> errors, int current, int required) {
        if (current < required) {
            errors.add(StructureErrors.missingCasings(current, required));
        }
    }

    default void checkHasEnergyHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.Energy, 1);
    }

    default void checkOneEnergyHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.Energy, 1);
    }

    default void checkHasDynamoHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.Dynamo, 1);
    }

    default void checkHasMaintenanceHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.Maintenance, 1);
    }

    default void checkOneMaintenanceHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.Maintenance, 1);
    }

    default void checkOneDynamoHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.Dynamo, 1);
    }

    default void checkHasMufflerHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.Muffler, 1);
    }

    default void checkOneMufflerHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.Muffler, 1);
    }

    default void checkHasInputBus(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.InputBus, 1);
    }

    default void checkOneInputBus(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.InputBus, 1);
    }

    default void checkHasInputHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.InputHatch, 1);
    }

    default void checkOneInputHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.InputHatch, 1);
    }

    default void checkHasOutputBus(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.OutputBus, 1);
    }

    default void checkOneOutputBus(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.OutputBus, 1);
    }

    default void checkHasOutputHatch(List<StructureError> errors) {
        checkHatchMin(errors, ECHatchElement.OutputHatch, 1);
    }

    default void checkOneOutputHatch(List<StructureError> errors) {
        checkHatchExact(errors, ECHatchElement.OutputHatch, 1);
    }

    default void checkHasAnyInput(List<StructureError> errors) {
        if ((InputBus.count(asMultiblock()) + InputHatch.count(asMultiblock())) == 0) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_any_input"));
        }
    }

    default void checkHasAnyOutput(List<StructureError> errors) {
        if ((OutputBus.count(asMultiblock()) + OutputHatch.count(asMultiblock())) == 0) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_any_output"));
        }
    }

    private int countEnergyHatches() {
        return asMultiblock().getHatchCountOf(
            IMultiblockEnergyInputLogicComponent.KEY,
            IMultiblockEnergyInputLogicComponent::getEnergyHatches);
    }

    default void checkHasAnyEnergy(List<StructureError> errors) {
        if (countEnergyHatches() == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, ECHatchElement.Energy, 0, 1));
        }
    }

    default void checkHasAnyDynamo(List<StructureError> errors) {
        if ((Dynamo.count(asMultiblock()) + ExoticDynamo.count(asMultiblock())) == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, ECHatchElement.Dynamo, 0, 1));
        }
    }

    default void checkOneEnergyHatchMaybeExotic(List<StructureError> errors) {
        int count = countEnergyHatches();
        if (count == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, ECHatchElement.Energy, 0, 1));
        } else if (count > 1) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, ECHatchElement.Energy, count, 1));
        }
    }

    default void checkOneDynamoHatchMaybeExotic(List<StructureError> errors) {
        int count = (int) (Dynamo.count(asMultiblock()) + ExoticDynamo.count(asMultiblock()));
        if (count == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, ECHatchElement.Dynamo, 0, 1));
        } else if (count > 1) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, ECHatchElement.Dynamo, count, 1));
        }
    }

    /**
     * Check if there is 1 TT Energy Hatch OR up to 2 Energy Hatches
     */
    default void checkExoticAndNormalEnergyHatches(List<StructureError> errors) {
        var energyCount = Energy.count(asMultiblock());
        var exoticCount = ExoticEnergy.count(asMultiblock());
        if ((energyCount + exoticCount) == 0) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Energy, 0, 1));
        }

        if (exoticCount != 0) {
            if (energyCount != 0) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.one_tt_or_two_energy"));
                return;
            }

            if (exoticCount != 1) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.one_tt_or_two_energy"));
                return;
            }
        }
        if (energyCount > 2) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.one_tt_or_two_energy"));
        }
    }
}
