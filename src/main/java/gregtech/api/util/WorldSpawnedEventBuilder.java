package gregtech.api.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public abstract class WorldSpawnedEventBuilder<This extends WorldSpawnedEventBuilder<This>> implements Runnable {

    private static final String ILLEGAL_STATE_STR1 = "Position, identifier and world must be set";
    /* Variables */

    private World world;

    /* Getters, Setters */

    public World getWorld() {
        return world;
    }

    public This setWorld(World world) {
        this.world = world;
        return getThis();
    }

    /* Methods */

    protected abstract This getThis();

    public void times(int times, Consumer<? super This> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < times; i++) {
            action.accept(getThis());
        }
    }

    public void times(int times, BiConsumer<? super This, Integer> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < times; i++) {
            action.accept(getThis(), i);
        }
    }

    /* Abstract Classes */

    private abstract static class EntityWorldSpawnedEventBuilder<This extends EntityWorldSpawnedEventBuilder<This>>
        extends WorldSpawnedEventBuilder<This> {

        private Entity entity;

        public Entity getEntity() {
            return entity;
        }

        public This setEntity(Entity entity) {
            this.entity = entity;
            return getThis();
        }
    }

    private abstract static class PositionedEntityWorldSpawnedEventBuilder<This extends PositionedEntityWorldSpawnedEventBuilder<This>>
        extends EntityWorldSpawnedEventBuilder<This> {

        private Vec3 position;

        public Vec3 getPosition() {
            return position;
        }

        public This setPosition(Vec3 position) {
            this.position = position;
            return getThis();
        }

        public This setPosition(double x, double y, double z) {
            this.position = Vec3.createVectorHelper(x, y, z);
            return getThis();
        }
    }

    private abstract static class PositionedWorldSpawnedEventBuilder<This extends PositionedWorldSpawnedEventBuilder<This>>
        extends WorldSpawnedEventBuilder<This> {

        private Vec3 position;

        public Vec3 getPosition() {
            return position;
        }

        public This setPosition(Vec3 position) {
            this.position = position;
            return getThis();
        }

        public This setPosition(double x, double y, double z) {
            this.position = Vec3.createVectorHelper(x, y, z);
            return getThis();
        }
    }

    private abstract static class StringIdentifierPositionedWorldSpawnedEventBuilder<This extends StringIdentifierPositionedWorldSpawnedEventBuilder<This>>
        extends PositionedWorldSpawnedEventBuilder<This> {

        private String identifier;

        public String getIdentifier() {
            return identifier;
        }

        public This setIdentifier(String identifier) {
            this.identifier = identifier;
            return getThis();
        }

        public This setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return getThis();
        }
    }

    private abstract static class SoundStringIdentifierPositionedWorldSpawnedEventBuilder<This extends SoundStringIdentifierPositionedWorldSpawnedEventBuilder<This>>
        extends StringIdentifierPositionedWorldSpawnedEventBuilder<This> {

        private float pitch;
        private float volume;

        public float getPitch() {
            return pitch;
        }

        public float getVolume() {
            return volume;
        }

        public This setPitch(float pitch) {
            this.pitch = pitch;
            return getThis();
        }

        public This setVolume(float volume) {
            this.volume = volume;
            return getThis();
        }
    }

    /* Implementations */

    public static final class ParticleEventBuilder
        extends StringIdentifierPositionedWorldSpawnedEventBuilder<ParticleEventBuilder> {

        private Vec3 motion;

        public Vec3 getMotion() {
            return motion;
        }

        public ParticleEventBuilder setMotion(double x, double y, double z) {
            this.motion = Vec3.createVectorHelper(x, y, z);
            return this;
        }

        public ParticleEventBuilder setMotion(Vec3 motion) {
            this.motion = motion;
            return this;
        }

        @Override
        protected ParticleEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getPosition() == null || getIdentifier() == null || getMotion() == null || getWorld() == null)
                throw new IllegalStateException("Position, identifier, motion and world must be set");

            getWorld().spawnParticle(
                getIdentifier(),
                getPosition().xCoord,
                getPosition().yCoord,
                getPosition().zCoord,
                getMotion().xCoord,
                getMotion().yCoord,
                getMotion().zCoord);
        }
    }

    public static final class SoundEffectEventBuilder
        extends SoundStringIdentifierPositionedWorldSpawnedEventBuilder<SoundEffectEventBuilder> {

        @Override
        protected SoundEffectEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getPosition() == null || getIdentifier() == null || getWorld() == null)
                throw new IllegalStateException(ILLEGAL_STATE_STR1);

            getWorld().playSoundEffect(
                getPosition().xCoord,
                getPosition().yCoord,
                getPosition().zCoord,
                getIdentifier(),
                getPitch(),
                getVolume());
        }
    }

    public static final class SoundEventBuilder
        extends SoundStringIdentifierPositionedWorldSpawnedEventBuilder<SoundEventBuilder> {

        private boolean proximity;

        public boolean isProximity() {
            return proximity;
        }

        public SoundEventBuilder setProximity(boolean proximity) {
            this.proximity = proximity;
            return this;
        }

        @Override
        protected SoundEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getPosition() == null || getIdentifier() == null || getWorld() == null)
                throw new IllegalStateException(ILLEGAL_STATE_STR1);

            getWorld().playSound(
                getPosition().xCoord,
                getPosition().yCoord,
                getPosition().zCoord,
                getIdentifier(),
                getPitch(),
                getVolume(),
                isProximity());
        }
    }

    /**
     * Positional Data is rounded down due to this targeting a block.
     */
    public static final class RecordEffectEventBuilder
        extends StringIdentifierPositionedWorldSpawnedEventBuilder<RecordEffectEventBuilder> {

        @Override
        protected RecordEffectEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getPosition() == null || getIdentifier() == null || getWorld() == null)
                throw new IllegalStateException(ILLEGAL_STATE_STR1);

            getWorld().playRecord(
                getIdentifier(),
                (int) getPosition().xCoord,
                (int) getPosition().yCoord,
                (int) getPosition().zCoord);
        }
    }

    public static final class ExplosionEffectEventBuilder
        extends PositionedEntityWorldSpawnedEventBuilder<ExplosionEffectEventBuilder> {

        private boolean isFlaming, isSmoking;
        private float strength;

        public float getStrength() {
            return strength;
        }

        public ExplosionEffectEventBuilder setStrength(float strength) {
            this.strength = strength;
            return this;
        }

        public boolean isFlaming() {
            return isFlaming;
        }

        public ExplosionEffectEventBuilder setFlaming(boolean flaming) {
            isFlaming = flaming;
            return this;
        }

        public boolean isSmoking() {
            return isSmoking;
        }

        public ExplosionEffectEventBuilder setSmoking(boolean smoking) {
            isSmoking = smoking;
            return this;
        }

        @Override
        protected ExplosionEffectEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getPosition() == null || getWorld() == null)
                throw new IllegalStateException("Position and world must be set");

            getWorld().newExplosion(
                getEntity(),
                getPosition().xCoord,
                getPosition().yCoord,
                getPosition().zCoord,
                strength,
                isFlaming,
                isSmoking);
        }
    }

    /**
     * Positional Data is rounded down due to this targeting a block.
     */
    public static final class ExtinguishFireEffectEventBuilder
        extends PositionedWorldSpawnedEventBuilder<ExtinguishFireEffectEventBuilder> {

        private int side;
        private EntityPlayer entityPlayer;

        public int getSide() {
            return side;
        }

        public ExtinguishFireEffectEventBuilder setSide(int side) {
            this.side = side;
            return this;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public ExtinguishFireEffectEventBuilder setEntityPlayer(EntityPlayer entity) {
            this.entityPlayer = entity;
            return this;
        }

        @Override
        protected ExtinguishFireEffectEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getEntityPlayer() == null || getPosition() == null || getWorld() == null)
                throw new IllegalStateException("EntityPlayer, position and world must be set");

            getWorld().extinguishFire(
                getEntityPlayer(),
                (int) getPosition().xCoord,
                (int) getPosition().yCoord,
                (int) getPosition().zCoord,
                side);
        }
    }

    public static final class SoundAtEntityEventBuilder
        extends EntityWorldSpawnedEventBuilder<SoundAtEntityEventBuilder> {

        private float pitch;
        private float volume;
        private String identifier;

        public String getIdentifier() {
            return identifier;
        }

        public SoundAtEntityEventBuilder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public SoundAtEntityEventBuilder setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return this;
        }

        public float getPitch() {
            return pitch;
        }

        public float getVolume() {
            return volume;
        }

        public SoundAtEntityEventBuilder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public SoundAtEntityEventBuilder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        @Override
        protected SoundAtEntityEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getWorld() == null || getIdentifier() == null || getEntity() == null)
                throw new IllegalStateException("World, Identifier and entity must be set!");

            getWorld().playSoundAtEntity(getEntity(), getIdentifier(), volume, pitch);
        }
    }

    public static final class SoundToNearExceptEventBuilder
        extends WorldSpawnedEventBuilder<SoundToNearExceptEventBuilder> {

        private float pitch;
        private float volume;
        private String identifier;
        private EntityPlayer entityPlayer;

        public String getIdentifier() {
            return identifier;
        }

        public SoundToNearExceptEventBuilder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public SoundToNearExceptEventBuilder setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return this;
        }

        public float getPitch() {
            return pitch;
        }

        public float getVolume() {
            return volume;
        }

        public SoundToNearExceptEventBuilder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public SoundToNearExceptEventBuilder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public SoundToNearExceptEventBuilder setEntityPlayer(EntityPlayer entity) {
            entityPlayer = entity;
            return this;
        }

        @Override
        protected SoundToNearExceptEventBuilder getThis() {
            return this;
        }

        @Override
        public void run() {
            if (getWorld() == null || getIdentifier() == null || getEntityPlayer() == null)
                throw new IllegalStateException("World, Identifier and EntityPlayer must be set!");

            getWorld().playSoundAtEntity(getEntityPlayer(), getIdentifier(), volume, pitch);
        }
    }
}
