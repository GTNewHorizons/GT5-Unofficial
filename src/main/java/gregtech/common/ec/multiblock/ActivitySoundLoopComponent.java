package gregtech.common.ec.multiblock;

import net.minecraft.client.Minecraft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ec.component.IActivitySoundLoopComponent;
import gregtech.api.enums.SoundResource;
import gregtech.client.GTSoundLoop;
import gregtech.client.volumetric.ISoundPosition;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;

public class ActivitySoundLoopComponent extends BaseMultiblockComponent implements IActivitySoundLoopComponent {

    protected final @NotNull SoundResource activitySound;

    @SideOnly(Side.CLIENT)
    protected @Nullable GTSoundLoop activitySoundLoop;

    public ActivitySoundLoopComponent(@NotNull SoundResource activitySound) {
        this.activitySound = activitySound;
    }

    private float soundRadiusMultiplier = 4f;

    public ActivitySoundLoopComponent setSoundRadiusMultiplier(float multiplier) {
        soundRadiusMultiplier = multiplier;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void startActivitySound() {
        if (activitySoundLoop != null && activitySoundLoop.isDonePlaying()) {
            activitySoundLoop = null;
        }

        doActivitySound(activitySound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void restartActivitySound() {
        if (activitySoundLoop != null) {
            activitySoundLoop.stop();
            activitySoundLoop = null;
        }

        startActivitySound();
    }

    @SideOnly(Side.CLIENT)
    protected void doActivitySound(SoundResource activitySound) {
        var mte = getMachine().getBaseMetaTileEntity();
        if (mte.isActive() && activitySound != null && !mte.isMuffled()) {
            if (activitySoundLoop == null) {
                activitySoundLoop = createSoundLoop(activitySound);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop.setFadeMe(true);
                activitySoundLoop = null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected @NotNull GTSoundLoop createSoundLoop(SoundResource activitySound) {
        GTSoundLoop loop = new GTSoundLoop(
            activitySound.resourceLocation,
            getMachine().getBaseMetaTileEntity(),
            false,
            true).setPosition(getSoundPosition());
        loop.setVolume(Math.max(1f, getMachine().getApproximateRadius() / 16f * soundRadiusMultiplier));
        return loop;
    }

    @SideOnly(Side.CLIENT)
    protected ISoundPosition getSoundPosition() {
        return getMachine()::getCenter;
    }
}
