package com.gtnewhorizons.gtnhintergalactic.tile;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.gtnewhorizons.gtnhintergalactic.config.Config;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;

/**
 * TE of the Space Elevator Cable
 *
 * @author minecraft7771
 */
public class TileEntitySpaceElevatorCable extends TileEntityAdvanced {

    public enum ClimberAnimation {
        /** Let the climber sit on its spot */
        NO_ANIMATION,
        /** Animation that drives the climber into orbit, pauses a bit and then drives down */
        DELIVER_ANIMATION,
        /** Animation that spawns the climber in orbit and then drives it down */
        FORMATION_ANIMATION,
    }

    /** Vertical movement speed of the climber */
    private static final float VERTICAL_SPEED = 1.0f;
    /** Rotation speed of the climber */
    private static final float ROTATION_SPEED = 0.5f;
    /** Max height which the climber will drive up to */
    private static final float MAX_HEIGHT = 250.0f;
    /** Time in ticks that determines how long the climber will wait in its up position */
    private static final int STANDBY_TIME_TICKS = 200;
    /** Distance which the climber uses to (de-)accelerate */
    private static final int ACCELERATION_DISTANCE = 30;
    /** Factor with which the climber (de-)accelerates */
    private static final float ACCELERATION_FACTOR = 1.0f / ACCELERATION_DISTANCE;

    /** NBT tag prefix of this */
    private static final String ELEVATOR_CLIMBER_NBT_TAG = "SE:";
    /** NBT tag to save the current climber height */
    private static final String HEIGHT_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "height";
    /** NBT tag to save the current climber vertical direction */
    private static final String VERTICAL_DIRECTION_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "verticalDirection";
    /** NBT tag to save if the climber is currently moving vertical */
    private static final String VERTICAL_MOVEMENT_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "verticalMovement";
    /** NBT tag to save the current climber rotation */
    private static final String ROTATION_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "rotation";
    /** NBT tag to save the current climber rotation direction */
    private static final String ROTATION_DIRECTION_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "rotationDirection";
    /** NBT tag to save if the climber is currently rotating */
    private static final String ROTATION_MOVEMENT_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "rotationMovement";
    /** NBT tag to save the wait time of the climber */
    private static final String WAIT_TIME_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "waitTime";
    /** NBT tag to save if the cable should be rendered */
    private static final String SHOULD_RENDER_NBT_TAG = ELEVATOR_CLIMBER_NBT_TAG + "shouldRender";

    /** Flag if the cable should be rendered */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = false;
    /** Current height of the climber */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float currentHeight = MAX_HEIGHT;
    /** Current rotation of the climber */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float currentRotation = 0;
    /** Flag if the climber is moving */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean isMoving = false;
    /** Flag if the climber is rotating */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean isRotating = false;
    /** Flag if the climber is currently moving up or down */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean isMovingDirectionUp = true;
    /** Flag if the climber is currently moving clockwise or anti-clockwise */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean isRotationDirectionClockwise = true;
    /** Current time that the climber is waiting for to move vertical again */
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public int currentWaitTime = 0;
    /** Current animation of the climber */
    public ClimberAnimation animation = ClimberAnimation.NO_ANIMATION;

    /** Helper variable used to detect a change to shouldRender client-side */
    private boolean shouldRenderConfirmation = false;

    /**
     * Get the bounding box of this renderer
     *
     * @return Infinite since the climber should always be rendered
     */
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    /**
     * Get the maximum distance squared in which this should be rendered
     *
     * @return Squared render distance of this
     */
    @Override
    public double getMaxRenderDistanceSquared() {
        return 20 * super.getMaxRenderDistanceSquared();
    }

    /**
     * Get the current height of the climber
     *
     * @return Current height of the climber
     */
    public float getClimberHeight() {
        return currentHeight;
    }

    /**
     * Get the current rotation of the climber
     *
     * @return Current rotation of the climber
     */
    public float getClimberRotation() {
        return currentRotation;
    }

    /**
     * Set if the cable should be rendered
     *
     * @param shouldRender True if it should be rendered, else false
     */
    public void setShouldRender(boolean shouldRender) {
        // If we just started rendering start the formation animation to let the climber spawn in smoothly
        if (!this.shouldRender && shouldRender) {
            startAnimation(ClimberAnimation.FORMATION_ANIMATION);
            // If we stopped rendering reset the climber so it can come from orbit again
        } else if (this.shouldRender && !shouldRender) {
            currentRotation = 0f;
            currentHeight = MAX_HEIGHT;
        }
        this.shouldRender = shouldRender;
    }

    /**
     * Check if this cable should be rendered
     *
     * @return True if it should be rendered, else false
     */
    public boolean shouldRender() {
        return shouldRender;
    }

    /**
     * Update this TE
     */
    @Override
    public void updateEntity() {
        if (!Config.isCableRenderingEnabled) return;
        super.updateEntity();
        // If the render state changed we need to issue a block renderer update to switch to the block renderer
        if (worldObj != null && worldObj.isRemote) {
            if (shouldRender != shouldRenderConfirmation) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                shouldRenderConfirmation = shouldRender;
            }
        }

        switch (animation) {
            case DELIVER_ANIMATION:
                animateDelivery();
                break;
            case FORMATION_ANIMATION:
                animateFormation();
                break;
            default:
                break;
        }
    }

    @Override
    public double getPacketRange() {
        return 128;
    }

    @Override
    public int getPacketCooldown() {
        return 20;
    }

    @Override
    public boolean isNetworkedTile() {
        return true;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList) {
        if (!worldObj.isRemote) {
            networkedList.add(animation.ordinal());
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream) {
        if (worldObj.isRemote) {
            int animationOrdinal = dataStream.readInt();
            if (animationOrdinal < ClimberAnimation.values().length) {
                animation = ClimberAnimation.values()[animationOrdinal];
            } else {
                animation = ClimberAnimation.NO_ANIMATION;
            }
        }
    }

    /**
     * Start the climber animation
     */
    public void startAnimation(ClimberAnimation animation) {
        this.animation = animation;
        isMoving = true;
        isRotating = true;
    }

    /**
     * Get the current animation of the climber
     *
     * @return Current climber animation
     */
    public ClimberAnimation getAnimation() {
        return animation;
    }

    /**
     * Start the climber animation
     */
    public void stopAnimation() {
        animation = ClimberAnimation.NO_ANIMATION;
        isRotating = false;
        isMoving = false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(HEIGHT_NBT_TAG, currentHeight);
        compound.setFloat(ROTATION_NBT_TAG, currentRotation);
        compound.setBoolean(VERTICAL_DIRECTION_NBT_TAG, isMovingDirectionUp);
        compound.setBoolean(ROTATION_DIRECTION_NBT_TAG, isRotationDirectionClockwise);
        compound.setBoolean(ROTATION_MOVEMENT_NBT_TAG, isRotating);
        compound.setBoolean(VERTICAL_MOVEMENT_NBT_TAG, isMoving);
        compound.setInteger(WAIT_TIME_NBT_TAG, currentWaitTime);
        compound.setBoolean(SHOULD_RENDER_NBT_TAG, shouldRender);
    }

    /**
     * Read saved data from NBT
     *
     * @param compound NBT compound for loading saved data
     */
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        currentHeight = compound.getFloat(HEIGHT_NBT_TAG);
        currentRotation = compound.getFloat(ROTATION_NBT_TAG);
        isMoving = compound.getBoolean(VERTICAL_MOVEMENT_NBT_TAG);
        isRotating = compound.getBoolean(ROTATION_MOVEMENT_NBT_TAG);
        isMovingDirectionUp = compound.getBoolean(VERTICAL_DIRECTION_NBT_TAG);
        isRotationDirectionClockwise = compound.getBoolean(ROTATION_DIRECTION_NBT_TAG);
        currentHeight = compound.getInteger(WAIT_TIME_NBT_TAG);
        shouldRender = compound.getBoolean(SHOULD_RENDER_NBT_TAG);
    }

    /**
     * Drive the formation animation
     */
    private void animateFormation() {
        if (isRotating) {
            if (isRotationDirectionClockwise) {
                currentRotation = (currentRotation + ROTATION_SPEED) % 360f;
            } else {
                currentRotation = (currentRotation - ROTATION_SPEED) % 360f;
            }
        }
        if (isMoving) {
            if (currentHeight <= 0.0f) {
                isMovingDirectionUp = true;
                stopAnimation();
            } else {
                if (currentHeight < ACCELERATION_DISTANCE) {
                    currentHeight -= VERTICAL_SPEED * ACCELERATION_FACTOR * Math.max(currentHeight, 1);
                } else {
                    currentHeight -= VERTICAL_SPEED;
                }
            }
        }
    }

    /**
     * Drive the delivery animation
     */
    private void animateDelivery() {
        if (isRotating) {
            if (isRotationDirectionClockwise) {
                currentRotation = (currentRotation + ROTATION_SPEED) % 360f;
            } else {
                currentRotation = (currentRotation - ROTATION_SPEED) % 360f;
            }
        }
        if (isMoving && currentWaitTime <= 0) {
            if (isMovingDirectionUp) {
                if (currentHeight >= MAX_HEIGHT) {
                    isMovingDirectionUp = false;
                    currentWaitTime = STANDBY_TIME_TICKS;
                } else {
                    if (currentHeight <= ACCELERATION_DISTANCE) {
                        currentHeight += VERTICAL_SPEED * ACCELERATION_FACTOR * Math.max(currentHeight, 1);
                    } else if (currentHeight >= MAX_HEIGHT - ACCELERATION_DISTANCE) {
                        currentHeight += VERTICAL_SPEED * ACCELERATION_FACTOR * Math.max(MAX_HEIGHT - currentHeight, 1);
                    } else {
                        currentHeight += VERTICAL_SPEED;
                    }
                }
            } else {
                if (currentHeight <= 0.0f) {
                    isMovingDirectionUp = true;
                    stopAnimation();
                } else {
                    if (currentHeight < ACCELERATION_DISTANCE) {
                        currentHeight -= VERTICAL_SPEED * ACCELERATION_FACTOR * Math.max(currentHeight, 1);
                    } else if (currentHeight >= MAX_HEIGHT - ACCELERATION_DISTANCE) {
                        currentHeight -= VERTICAL_SPEED * ACCELERATION_FACTOR * Math.max(MAX_HEIGHT - currentHeight, 1);
                    } else {
                        currentHeight -= VERTICAL_SPEED;
                    }
                }
            }
        } else if (currentWaitTime > 0) {
            currentWaitTime--;
        }
    }
}
