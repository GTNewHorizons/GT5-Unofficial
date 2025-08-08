package gregtech.api.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public abstract class WorldSpawnedEventBuilder implements Runnable {

    private static final String ILLEGAL_STATE_STR1 = "Position, identifier and world must be set";
    /* Variables */

    private World world;

    /* Getters, Setters */

    public World getWorld() {
        return world;
    }

    public WorldSpawnedEventBuilder setWorld(World world) {
        this.world = world;
        return this;
    }

    /* Methods */

    @SuppressWarnings("unchecked")
    public <U extends WorldSpawnedEventBuilder> void times(int times, Consumer<U> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < times; i++) {
            action.accept((U) this);
        }
    }

    @SuppressWarnings("unchecked")
    public <U extends WorldSpawnedEventBuilder> void times(int times, BiConsumer<U, Integer> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < times; i++) {
            action.accept((U) this, i);
        }
    }

    /* Interfaces */

    private interface IPositionedWorldSpawnedEvent {

        Vec3 getPosition();

        IPositionedWorldSpawnedEvent setPosition(Vec3 position);

        IPositionedWorldSpawnedEvent setPosition(double x, double y, double z);
    }

    private interface IEntityWorldSpawnedEvent {

        Entity getEntity();

        IEntityWorldSpawnedEvent setEntity(Entity entity);
    }

    private interface IEntityPlayerWorldSpawnedEvent {

        EntityPlayer getEntityPlayer();

        IEntityPlayerWorldSpawnedEvent setEntityPlayer(EntityPlayer entity);
    }

    private interface IStringIdentifierWorldSpawnedEvent {

        String getIdentifier();

        IStringIdentifierWorldSpawnedEvent setIdentifier(String identifier);

        IStringIdentifierWorldSpawnedEvent setIdentifier(Enum<?> identifier);
    }

    private interface ISoundWorldSpawnedEvent {

        float getPitch();

        float getVolume();

        ISoundWorldSpawnedEvent setPitch(float pitch);

        ISoundWorldSpawnedEvent setVolume(float volume);
    }

    /* Abstract Classes */

    private abstract static class EntityWorldSpawnedEventBuilder extends WorldSpawnedEventBuilder
        implements IEntityWorldSpawnedEvent {

        private Entity entity;

        @Override
        public Entity getEntity() {
            return entity;
        }

        @Override
        public EntityWorldSpawnedEventBuilder setEntity(Entity entity) {
            this.entity = entity;
            return this;
        }
    }

    private abstract static class PositionedEntityWorldSpawnedEventBuilder extends EntityWorldSpawnedEventBuilder
        implements IPositionedWorldSpawnedEvent {

        private Vec3 position;

        @Override
        public Vec3 getPosition() {
            return position;
        }

        @Override
        public PositionedEntityWorldSpawnedEventBuilder setPosition(Vec3 position) {
            this.position = position;
            return this;
        }

        @Override
        public PositionedEntityWorldSpawnedEventBuilder setPosition(double x, double y, double z) {
            this.position = Vec3.createVectorHelper(x, y, z);
            return this;
        }
    }

    private abstract static class PositionedWorldSpawnedEventBuilder extends WorldSpawnedEventBuilder
        implements IPositionedWorldSpawnedEvent {

        private Vec3 position;

        @Override
        public Vec3 getPosition() {
            return position;
        }

        @Override
        public PositionedWorldSpawnedEventBuilder setPosition(Vec3 position) {
            this.position = position;
            return this;
        }

        @Override
        public PositionedWorldSpawnedEventBuilder setPosition(double x, double y, double z) {
            this.position = Vec3.createVectorHelper(x, y, z);
            return this;
        }
    }

    private abstract static class StringIdentifierPositionedWorldSpawnedEventBuilder
        extends PositionedWorldSpawnedEventBuilder implements IStringIdentifierWorldSpawnedEvent {

        private String identifier;

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public StringIdentifierPositionedWorldSpawnedEventBuilder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        @Override
        public StringIdentifierPositionedWorldSpawnedEventBuilder setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return this;
        }
    }

    private abstract static class SoundStringIdentifierPositionedWorldSpawnedEventBuilder
        extends StringIdentifierPositionedWorldSpawnedEventBuilder implements ISoundWorldSpawnedEvent {

        private float pitch;
        private float volume;

        @Override
        public float getPitch() {
            return pitch;
        }

        @Override
        public float getVolume() {
            return volume;
        }

        @Override
        public SoundStringIdentifierPositionedWorldSpawnedEventBuilder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        @Override
        public SoundStringIdentifierPositionedWorldSpawnedEventBuilder setVolume(float volume) {
            this.volume = volume;
            return this;
        }
    }

    /* Implementations */

    public static final class ParticleEventBuilder extends StringIdentifierPositionedWorldSpawnedEventBuilder {

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
        public ParticleEventBuilder setWorld(World world) {
            return (ParticleEventBuilder) super.setWorld(world);
        }

        @Override
        public ParticleEventBuilder setPosition(Vec3 position) {
            return (ParticleEventBuilder) super.setPosition(position);
        }

        @Override
        public ParticleEventBuilder setPosition(double x, double y, double z) {
            return (ParticleEventBuilder) super.setPosition(x, y, z);
        }

        @Override
        public ParticleEventBuilder setIdentifier(String identifier) {
            return (ParticleEventBuilder) super.setIdentifier(identifier);
        }

        @Override
        public ParticleEventBuilder setIdentifier(Enum<?> identifier) {
            return (ParticleEventBuilder) super.setIdentifier(identifier);
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

    public static final class SoundEffectEventBuilder extends SoundStringIdentifierPositionedWorldSpawnedEventBuilder {

        @Override
        public SoundEffectEventBuilder setWorld(World world) {
            return (SoundEffectEventBuilder) super.setWorld(world);
        }

        @Override
        public SoundEffectEventBuilder setPosition(Vec3 position) {
            return (SoundEffectEventBuilder) super.setPosition(position);
        }

        @Override
        public SoundEffectEventBuilder setPosition(double x, double y, double z) {
            return (SoundEffectEventBuilder) super.setPosition(x, y, z);
        }

        @Override
        public SoundEffectEventBuilder setIdentifier(String identifier) {
            return (SoundEffectEventBuilder) super.setIdentifier(identifier);
        }

        @Override
        public SoundEffectEventBuilder setIdentifier(Enum<?> identifier) {
            return (SoundEffectEventBuilder) super.setIdentifier(identifier);
        }

        @Override
        public SoundEffectEventBuilder setPitch(float pitch) {
            return (SoundEffectEventBuilder) super.setPitch(pitch);
        }

        @Override
        public SoundEffectEventBuilder setVolume(float volume) {
            return (SoundEffectEventBuilder) super.setVolume(volume);
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

    public static final class SoundEventBuilder extends SoundStringIdentifierPositionedWorldSpawnedEventBuilder {

        private boolean proximity;

        public boolean isProximity() {
            return proximity;
        }

        @Override
        public SoundEventBuilder setWorld(World world) {
            return (SoundEventBuilder) super.setWorld(world);
        }

        @Override
        public SoundEventBuilder setPosition(Vec3 position) {
            return (SoundEventBuilder) super.setPosition(position);
        }

        @Override
        public SoundEventBuilder setPosition(double x, double y, double z) {
            return (SoundEventBuilder) super.setPosition(x, y, z);
        }

        @Override
        public SoundEventBuilder setIdentifier(String identifier) {
            return (SoundEventBuilder) super.setIdentifier(identifier);
        }

        @Override
        public SoundEventBuilder setPitch(float pitch) {
            return (SoundEventBuilder) super.setPitch(pitch);
        }

        @Override
        public SoundEventBuilder setVolume(float volume) {
            return (SoundEventBuilder) super.setVolume(volume);
        }

        public SoundEventBuilder setProximity(boolean proximity) {
            this.proximity = proximity;
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
    public static final class RecordEffectEventBuilder extends StringIdentifierPositionedWorldSpawnedEventBuilder {

        @Override
        public RecordEffectEventBuilder setWorld(World world) {
            return (RecordEffectEventBuilder) super.setWorld(world);
        }

        @Override
        public RecordEffectEventBuilder setPosition(Vec3 position) {
            return (RecordEffectEventBuilder) super.setPosition(position);
        }

        @Override
        public RecordEffectEventBuilder setPosition(double x, double y, double z) {
            return (RecordEffectEventBuilder) super.setPosition(x, y, z);
        }

        @Override
        public RecordEffectEventBuilder setIdentifier(String identifier) {
            return (RecordEffectEventBuilder) super.setIdentifier(identifier);
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

    public static final class ExplosionEffectEventBuilder extends PositionedEntityWorldSpawnedEventBuilder {

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
        public ExplosionEffectEventBuilder setWorld(World world) {
            return (ExplosionEffectEventBuilder) super.setWorld(world);
        }

        @Override
        public ExplosionEffectEventBuilder setEntity(Entity entity) {
            return (ExplosionEffectEventBuilder) super.setEntity(entity);
        }

        @Override
        public ExplosionEffectEventBuilder setPosition(double x, double y, double z) {
            return (ExplosionEffectEventBuilder) super.setPosition(x, y, z);
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
    public static final class ExtinguishFireEffectEventBuilder extends PositionedWorldSpawnedEventBuilder
        implements IEntityPlayerWorldSpawnedEvent {

        private int side;
        private EntityPlayer entityPlayer;

        public int getSide() {
            return side;
        }

        public ExtinguishFireEffectEventBuilder setSide(int side) {
            this.side = side;
            return this;
        }

        @Override
        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        @Override
        public ExtinguishFireEffectEventBuilder setEntityPlayer(EntityPlayer entity) {
            this.entityPlayer = entity;
            return this;
        }

        @Override
        public ExtinguishFireEffectEventBuilder setWorld(World world) {
            return (ExtinguishFireEffectEventBuilder) super.setWorld(world);
        }

        @Override
        public ExtinguishFireEffectEventBuilder setPosition(Vec3 position) {
            return (ExtinguishFireEffectEventBuilder) super.setPosition(position);
        }

        @Override
        public ExtinguishFireEffectEventBuilder setPosition(double x, double y, double z) {
            return (ExtinguishFireEffectEventBuilder) super.setPosition(x, y, z);
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

    public static final class SoundAtEntityEventBuilder extends EntityWorldSpawnedEventBuilder
        implements ISoundWorldSpawnedEvent, IStringIdentifierWorldSpawnedEvent {

        private float pitch;
        private float volume;
        private String identifier;

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public SoundAtEntityEventBuilder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        @Override
        public SoundAtEntityEventBuilder setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return this;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        @Override
        public float getVolume() {
            return volume;
        }

        @Override
        public SoundAtEntityEventBuilder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        @Override
        public SoundAtEntityEventBuilder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        @Override
        public SoundAtEntityEventBuilder setWorld(World world) {
            return (SoundAtEntityEventBuilder) super.setWorld(world);
        }

        @Override
        public SoundAtEntityEventBuilder setEntity(Entity entity) {
            return (SoundAtEntityEventBuilder) super.setEntity(entity);
        }

        @Override
        public void run() {
            if (getWorld() == null || getIdentifier() == null || getEntity() == null)
                throw new IllegalStateException("World, Identifier and entity must be set!");

            getWorld().playSoundAtEntity(getEntity(), getIdentifier(), volume, pitch);
        }
    }

    public static final class SoundToNearExceptEventBuilder extends WorldSpawnedEventBuilder
        implements ISoundWorldSpawnedEvent, IStringIdentifierWorldSpawnedEvent, IEntityPlayerWorldSpawnedEvent {

        private float pitch;
        private float volume;
        private String identifier;
        private EntityPlayer entityPlayer;

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public SoundToNearExceptEventBuilder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        @Override
        public SoundToNearExceptEventBuilder setIdentifier(Enum<?> identifier) {
            this.identifier = identifier.toString();
            return this;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        @Override
        public float getVolume() {
            return volume;
        }

        @Override
        public SoundToNearExceptEventBuilder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        @Override
        public SoundToNearExceptEventBuilder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        @Override
        public SoundToNearExceptEventBuilder setWorld(World world) {
            return (SoundToNearExceptEventBuilder) super.setWorld(world);
        }

        @Override
        public void run() {
            if (getWorld() == null || getIdentifier() == null || getEntityPlayer() == null)
                throw new IllegalStateException("World, Identifier and EntityPlayer must be set!");

            getWorld().playSoundAtEntity(getEntityPlayer(), getIdentifier(), volume, pitch);
        }

        @Override
        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        @Override
        public SoundToNearExceptEventBuilder setEntityPlayer(EntityPlayer entity) {
            entityPlayer = entity;
            return this;
        }
    }
}
