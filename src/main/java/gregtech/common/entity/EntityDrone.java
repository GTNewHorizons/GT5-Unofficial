package gregtech.common.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDrone extends EntityLivingBase {

    private static final int DW_DRONE_LEVEL = 20;
    private static final int DW_AUTO_MODE = 21;
    private static final double SPEED = 0.1;
    private static final int ORBIT_TOTAL_TICKS = 200;
    private static final double ORBIT_RADIUS = 3.0;
    private static final int MAX_LIFETIME_TICKS = 6000;

    private final List<double[]> waypoints = new ArrayList<>();
    private int currentWaypointIndex = 0;
    private int orbitAfterWaypointIndex = -1;
    private boolean orbiting = false;
    private int orbitTicks = 0;
    private double orbitCenterX;
    private double orbitCenterY;
    private double orbitCenterZ;
    private double orbitStartAngle = 0;
    private boolean flightDone = false;
    private int totalTicksAlive = 0;

    public EntityDrone(World world) {
        super(world);
        this.noClip = false;
        this.setSize(0.2F, 0.2F);
        this.yOffset = 1.62F;
        this.isImmuneToFire = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DW_DRONE_LEVEL, 1);
        dataWatcher.addObject(DW_AUTO_MODE, (byte) 0);
    }

    public int getDroneLevel() {
        return dataWatcher.getWatchableObjectInt(DW_DRONE_LEVEL);
    }

    public boolean isAutoMode() {
        return dataWatcher.getWatchableObjectByte(DW_AUTO_MODE) != 0;
    }

    @Override
    public void onUpdate() {
        if (isAutoMode()) {
            super.onUpdate();
            if (!worldObj.isRemote) {
                totalTicksAlive++;
                if (totalTicksAlive > MAX_LIFETIME_TICKS) {
                    setDead();
                    return;
                }
                tickAutoFlight();
            }
        } else {
            this.lastTickPosX = this.prevPosX;
            this.lastTickPosY = this.prevPosY;
            this.lastTickPosZ = this.prevPosZ;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
        }
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {}

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float hw = this.width / 2.0F;
        float hh = this.height / 2.0F;
        this.boundingBox.setBounds(x - hw, y - hh, z - hw, x + hw, y + hh, z + hw);
    }

    @Override
    public void moveEntity(double dx, double dy, double dz) {
        super.moveEntity(dx, dy, dz);
        this.posY = (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D;
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound tag) {
        return false;
    }

    @Override
    public boolean writeMountToNBT(NBTTagCompound tag) {
        return false;
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {}

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[5];
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {}

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {}

    public void initAutoFlight(int hatchX, int hatchY, int hatchZ, int ctrlX, int ctrlY, int ctrlZ, int droneLevel) {
        dataWatcher.updateObject(DW_AUTO_MODE, (byte) 1);
        dataWatcher.updateObject(DW_DRONE_LEVEL, droneLevel);
        this.noClip = true;
        setPosition(hatchX + 0.5, hatchY + 0.5, hatchZ + 0.5);
        int cruiseY = findSafeCruiseAltitude(worldObj, hatchX, hatchY, hatchZ, ctrlX, ctrlY, ctrlZ);
        orbitCenterX = ctrlX + 0.5;
        orbitCenterY = ctrlY + 2.5;
        orbitCenterZ = ctrlZ + 0.5;
        double approachAngle = Math.atan2((hatchZ + 0.5) - orbitCenterZ, (hatchX + 0.5) - orbitCenterX);
        double orbitEntryX = orbitCenterX + ORBIT_RADIUS * Math.cos(approachAngle);
        double orbitEntryZ = orbitCenterZ + ORBIT_RADIUS * Math.sin(approachAngle);
        orbitStartAngle = approachAngle;
        waypoints.add(new double[] { hatchX + 0.5, cruiseY, hatchZ + 0.5 });
        waypoints.add(new double[] { ctrlX + 0.5, cruiseY, ctrlZ + 0.5 });
        waypoints.add(new double[] { orbitEntryX, orbitCenterY, orbitEntryZ });
        orbitAfterWaypointIndex = 2;
        waypoints.add(new double[] { ctrlX + 0.5, cruiseY, ctrlZ + 0.5 });
        waypoints.add(new double[] { hatchX + 0.5, cruiseY, hatchZ + 0.5 });
        waypoints.add(new double[] { hatchX + 0.5, hatchY + 0.5, hatchZ + 0.5 });
        double dx = waypoints.get(0)[0] - posX;
        double dz = waypoints.get(0)[2] - posZ;
        if (Math.abs(dx) > 0.01 || Math.abs(dz) > 0.01) {
            rotationYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        }
        prevRotationYaw = rotationYaw;
    }

    private void tickAutoFlight() {
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        if (flightDone) {
            setDead();
            return;
        }
        if (orbiting) {
            tickOrbit();
            return;
        }
        if (currentWaypointIndex >= waypoints.size()) {
            flightDone = true;
            return;
        }
        double[] target = waypoints.get(currentWaypointIndex);
        double dx = target[0] - posX;
        double dy = target[1] - posY;
        double dz = target[2] - posZ;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist <= SPEED) {
            setPosition(target[0], target[1], target[2]);
            if (currentWaypointIndex == orbitAfterWaypointIndex) {
                startOrbit();
            }
            currentWaypointIndex++;
        } else {
            double ratio = SPEED / dist;
            setPosition(posX + dx * ratio, posY + dy * ratio, posZ + dz * ratio);
        }
        updateFlightRotation(dx, dy, dz);
    }

    private void startOrbit() {
        orbiting = true;
        orbitTicks = 0;
    }

    private void tickOrbit() {
        orbitTicks++;
        if (orbitTicks >= ORBIT_TOTAL_TICKS) {
            orbiting = false;
            setPosition(
                orbitCenterX + ORBIT_RADIUS * Math.cos(orbitStartAngle),
                orbitCenterY,
                orbitCenterZ + ORBIT_RADIUS * Math.sin(orbitStartAngle));
            rotationPitch = 0;
            return;
        }
        double angularSpeed = 2.0 * Math.PI / ORBIT_TOTAL_TICKS;
        double angle = orbitStartAngle + orbitTicks * angularSpeed;
        setPosition(
            orbitCenterX + ORBIT_RADIUS * Math.cos(angle),
            orbitCenterY,
            orbitCenterZ + ORBIT_RADIUS * Math.sin(angle));
        double faceDx = orbitCenterX - posX;
        double faceDz = orbitCenterZ - posZ;
        rotationYaw = (float) Math.toDegrees(Math.atan2(-faceDx, faceDz));
        rotationPitch = -10.0f;
    }

    private void updateFlightRotation(double dx, double dy, double dz) {
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        if (horizontalDist > 0.01) {
            float targetYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            rotationYaw = lerpAngle(rotationYaw, targetYaw, 0.15f);
        }
        float targetPitch = 0;
        double totalDist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (totalDist > 0.01) {
            targetPitch = (float) -Math.toDegrees(Math.atan2(dy, Math.max(horizontalDist, 0.01)));
            targetPitch = Math.max(-25.0f, Math.min(25.0f, targetPitch));
        }
        rotationPitch = lerpAngle(rotationPitch, targetPitch, 0.1f);
    }

    private static int findSafeCruiseAltitude(World world, int hatchX, int hatchY, int hatchZ, int ctrlX, int ctrlY,
        int ctrlZ) {
        int safeY = Math.max(hatchY, ctrlY) + 3;
        for (int[] xz : bresenhamLine2D(hatchX, hatchZ, ctrlX, ctrlZ)) {
            while (safeY < 250
                && (!world.isAirBlock(xz[0], safeY, xz[1]) || !world.isAirBlock(xz[0], safeY + 1, xz[1]))) {
                safeY++;
            }
        }
        return Math.min(safeY, 250);
    }

    private static List<int[]> bresenhamLine2D(int x0, int z0, int x1, int z1) {
        List<int[]> points = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dz = Math.abs(z1 - z0);
        int sx = x0 < x1 ? 1 : -1;
        int sz = z0 < z1 ? 1 : -1;
        int err = dx - dz;
        while (true) {
            points.add(new int[] { x0, z0 });
            if (x0 == x1 && z0 == z1) break;
            int e2 = 2 * err;
            if (e2 > -dz) {
                err -= dz;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                z0 += sz;
            }
        }
        return points;
    }

    private static float lerpAngle(float from, float to, float factor) {
        float diff = to - from;
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;
        return from + diff * factor;
    }
}
