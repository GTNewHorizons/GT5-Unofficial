package gregtech.api.metatileentity.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Answers;
import org.mockito.Mockito;

import gregtech.api.structure.error.StructureError;

/**
 * Tests some functions of {@link MTEMultiBlockBase}.
 * <p>
 * The classes and tests are non-public because JUnit5
 * <a href="https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-classes-and-methods">recommends</a> to omit
 * the {@code public} modifier.
 */
class MTEMultiBlockBaseTest {

    @ParameterizedTest
    @CsvSource({ "0,0,false", "2,0,false", "1,0,true", "1,1,false", "0,1,true", "0,2,true", "0,3,false" })
    void checkExoticAndNormalEnergyHatches_parametrizedTest(int exoticEnergyHatchesCount, int normalEnergyHatchesCount,
        boolean expectedResult) {
        MTEMultiBlockBase testedClassInstance = Mockito.mock(MTEMultiBlockBase.class, Answers.CALLS_REAL_METHODS);
        List<StructureError> errors = new ArrayList<>();

        testedClassInstance.setEnergyHatches(fillList(MTEHatchEnergy.class, normalEnergyHatchesCount));
        testedClassInstance.setExoticEnergyHatches(fillList(MTEHatch.class, exoticEnergyHatchesCount));
        testedClassInstance.checkExoticAndNormalEnergyHatches(errors);
        assertEquals(expectedResult, errors.isEmpty());
    }

    private <T> ArrayList<T> fillList(Class<T> classData, int returnedListSize) {
        T objectToInsert = Mockito.mock(classData);
        ArrayList<T> listToReturn = new ArrayList<>();
        for (int i = 0; i < returnedListSize; i++) {
            listToReturn.add(objectToInsert);
        }
        return listToReturn;
    }
}
