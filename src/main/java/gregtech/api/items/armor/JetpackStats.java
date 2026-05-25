package gregtech.api.items.armor;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.ParticleFX;

public enum JetpackStats {

    ELECTRIC(1.0f, 1.0f, 0.18f, 0.1f, 0.12f, 0.3f, 0.08f, 0.0f, ParticleFX.SMOKE),
    ADVANCED(2.5f, 1.3f, 0.34f, 0.03f, 0.13f, 0.48f, 0.14f, 2.0f, ParticleFX.CLOUD),
    NANO(4.0f, 1.8f, 0.4f, 0.005f, 0.14f, 0.8f, 0.19f, 3.5f, null),
    QUANTUM(6.0f, 2.4f, 0.45f, 0.0f, 0.15f, 0.9f, 0.21f, 8.0f, null);

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
