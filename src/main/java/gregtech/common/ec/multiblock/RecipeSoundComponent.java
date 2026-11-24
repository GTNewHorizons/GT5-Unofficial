package gregtech.common.ec.multiblock;

import static gregtech.api.metatileentity.implementations.MTEMultiBlockBase.PROCESS_START_SOUND_INDEX;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.IClientSoundComponent;
import gregtech.api.ec.component.IOnRecipeStartComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;

public class RecipeSoundComponent extends BaseMultiblockComponent
    implements IOnRecipeStartComponent, IClientSoundComponent {

    private final SoundResource processStartSound;
    private final int timeBetweenProcessSounds;

    public RecipeSoundComponent(SoundResource processStartSound, int timeBetweenProcessSounds) {
        this.processStartSound = processStartSound;
        this.timeBetweenProcessSounds = timeBetweenProcessSounds;
    }

    @Override
    public void onRecipeStart(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        getMachine().sendLoopStart(PROCESS_START_SOUND_INDEX);
    }

    @Override
    public int getRecipeStartPriority() {
        return RecipeStartPriority.SOUND.priority;
    }

    @Override
    public boolean onDoSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            GTUtility.doSoundAtClient(processStartSound, timeBetweenProcessSounds, 1.0F, aX, aY, aZ);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStartSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            GTUtility.doSoundAtClient(processStartSound, timeBetweenProcessSounds, 1.0F, aX, aY, aZ);
            return true;
        }
        return false;
    }
}
