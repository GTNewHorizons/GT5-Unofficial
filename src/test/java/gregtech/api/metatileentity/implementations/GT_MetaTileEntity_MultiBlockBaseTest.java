package gregtech.api.metatileentity.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Answers;
import org.mockito.Mockito;

/**
 * Tests some functions of {@link GT_MetaTileEntity_MultiBlockBase}.
 * <p>
 * The classes and tests are non-public because JUnit5
 * <a href="https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-classes-and-methods">recommends</a>
 * to omit the {@code public} modifier.
 */
@SuppressWarnings("NewClassNamingConvention") /*
                                               * The name of the original class does not fit the convention,
                                               * but refactoring that is a story for another time.
                                               */
class GT_MetaTileEntity_MultiBlockBaseTest {

    @ParameterizedTest
    @CsvSource({ "0,0,false", "2,0,false", "1,0,true", "1,1,false", "0,1,true", "0,2,true", "0,3,false" })
    void checkExoticAndNormalEnergyHatches_parametrizedTest(int exoticEnergyHatchesCount, int normalEnergyHatchesCount,
        boolean expectedResult) {
        GT_MetaTileEntity_MultiBlockBase testedClassInstance = Mockito
            .mock(GT_MetaTileEntity_MultiBlockBase.class, Answers.CALLS_REAL_METHODS);

        testedClassInstance.setEnergyHatches(fillList(GT_MetaTileEntity_Hatch_Energy.class, normalEnergyHatchesCount));
        testedClassInstance.setExoticEnergyHatches(fillList(GT_MetaTileEntity_Hatch.class, exoticEnergyHatchesCount));

        assertEquals(expectedResult, testedClassInstance.checkExoticAndNormalEnergyHatches());
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
