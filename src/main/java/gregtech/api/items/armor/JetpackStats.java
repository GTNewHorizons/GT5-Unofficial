package gregtech.api.items.armor;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.ParticleFX;

public enum JetpackStats {

    JETPACK(2.5f, 1.3f, 0.34f, 0.03f, 0.13f, 0.48f, 0.14f, 2.0f, ParticleFX.CLOUD),
    VECTORED_JETPACK(2.5f, 1.3f, 0.4f, 0f, 0.16f, 0.6f, 0.14f, 2.0f, ParticleFX.CLOUD);

    private final float sprintEnergyMod;
    private final float sprintSpeedMod;
    private final float hoverSpeed;
    private final float hoverSlowSpeed;
    private final float acceleration;
    private final float verticalSpeed;
    private final float horizontalSpeed;
    private final float fallDamageReduction;
    private final ParticleFX particle;

    JetpackStats(float sprintEnergyMod, float sprintSpeedMod, float hoverSpeed, float hoverSlowSpeed,
        float acceleration, float verticalSpeed, float horizontalSpeed, float fallDamageReduction,
        ParticleFX particle) {
        this.sprintEnergyMod = sprintEnergyMod;
        this.sprintSpeedMod = sprintSpeedMod;
        this.hoverSpeed = hoverSpeed;
        this.hoverSlowSpeed = hoverSlowSpeed;
        this.acceleration = acceleration;
        this.verticalSpeed = verticalSpeed;
        this.horizontalSpeed = horizontalSpeed;
        this.fallDamageReduction = fallDamageReduction;
        this.particle = particle;
    }

    public double getSprintEnergyModifier() {
        return sprintEnergyMod;
    }

    public double getSprintSpeedModifier() {
        return sprintSpeedMod;
    }

    public double getVerticalHoverSpeed() {
        return hoverSpeed;
    }

    public double getVerticalHoverSlowSpeed() {
        return hoverSlowSpeed;
    }

    public double getVerticalAcceleration() {
        return acceleration;
    }

    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    public double getSidewaysSpeed() {
        return horizontalSpeed;
    }

    public float getFallDamageReduction() {
        return fallDamageReduction;
    }

    public @Nullable ParticleFX getParticle() {
        return particle;
    }
}
