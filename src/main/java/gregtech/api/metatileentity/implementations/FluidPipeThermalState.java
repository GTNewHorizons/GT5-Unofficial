package gregtech.api.metatileentity.implementations;

import static gregtech.api.metatileentity.implementations.FluidPipeThermals.FROST_LEVELS;
import static gregtech.api.metatileentity.implementations.FluidPipeThermals.HEAT_DAMAGE_TEMPERATURE;
import static gregtech.api.metatileentity.implementations.FluidPipeThermals.HEAT_GLOW_LEVELS;
import static gregtech.api.metatileentity.implementations.FluidPipeThermals.NONE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;

/**
 * Tracks how hot or frosted a fluid pipe is. Also handles the pipe's block light, contact damage and overheating
 * warning.
 *
 * @see FluidPipeThermals
 */
public final class FluidPipeThermalState {

    /**
     * Updates (every 5 ticks) to wait before cooling or thawing, so intermittent flow doesn't flicker. 30s, so fluid
     * circulating round a loop keeps the whole loop hot rather than heating each pipe as it passes.
     */
    private static final int HOLD_UPDATES = 120;
    /** Updates the overheating warning stays up after the fluid leaves. */
    private static final int OVERHEATING_HOLD_UPDATES = 20;
    /**
     * Levels per tick the pipe moves towards {@link #level}. Full glow takes 3s to heat up and 5s to cool. Full frost
     * takes 4s to form and 6s to thaw.
     */
    private static final float HEAT_UP_RATE = HEAT_GLOW_LEVELS / 60.0F, COOL_DOWN_RATE = HEAT_GLOW_LEVELS / 100.0F;
    private static final float FREEZE_RATE = FROST_LEVELS / 80.0F, THAW_RATE = FROST_LEVELS / 120.0F;
    /** Collision leaves entities exactly flush, which strict AABB intersection doesn't count as touching. */
    private static final double CONTACT_MARGIN = 1.0E-3D;
    private static final String WAILA_OVERHEATING = "overheating";

    private final MTEFluidPipe pipe;
    /** Target level set by the contents. Synced to the client. */
    private int level = NONE;
    private int holdTimer = 0;
    /** Current level, fading towards {@link #level}. Used on both sides for damage and rendering. */
    private float shownLevel = 0F;
    /** Client side. False until the first sync, which is shown without fading. */
    private boolean synced = false;
    /** {@link #shownLevel} rounded, used for the pipe texture and block light. */
    private int renderedLevel = NONE;
    /**
     * Temperature that makes the pipe dangerous to touch, or null if safe. Kept while the pipe is still hot or frosted,
     * since fluid only sits in a flowing pipe part of the time. Server side only.
     */
    private @Nullable Integer hazardTemperature;
    /** Updates left to keep showing the overheating warning. */
    private int overheatingTimer = 0;

    FluidPipeThermalState(MTEFluidPipe pipe) {
        this.pipe = pipe;
    }

    public float getShownLevel() {
        return shownLevel;
    }

    public int getRenderedLevel() {
        return renderedLevel;
    }

    /** @return whether the pipe is hot or frosted, or still fading in or out */
    public boolean isActive() {
        return level != NONE || shownLevel != 0F;
    }

    /** Server side, every pipe update (5 ticks). */
    void update(IGregTechTileEntity base) {
        updateLevel(base);
        advanceFade(5);
        updateHazard();
        if (isOverheating()) overheatingTimer = OVERHEATING_HOLD_UPDATES;
        else if (overheatingTimer > 0) overheatingTimer--;
        hurtTouchingEntities(base);
        // Every update, so a pipe swapped in for a hot one doesn't keep its light
        applyLight();
    }

    /**
     * Sets {@link #level} from the contents. Hotter or colder applies straight away, cooling or thawing waits
     * {@link #HOLD_UPDATES} updates.
     */
    private void updateLevel(IGregTechTileEntity base) {
        final int current = FluidPipeThermals.getLevel(getContentsTemperature());

        if (current == level) {
            if (current != NONE) holdTimer = HOLD_UPDATES;
            return;
        }

        final boolean immediate = level == NONE || (current > NONE && current > level)
            || (current < NONE && level < NONE && current < level);
        if (!immediate && --holdTimer > 0) return;

        level = current;
        holdTimer = current == NONE ? 0 : HOLD_UPDATES;
        base.issueTileUpdate();
    }

    /**
     * Moves {@link #shownLevel} towards {@link #level} by {@code ticks} ticks. Going between hot and frosted passes
     * through normal first. Redraws the pipe when the rounded level changes.
     */
    public void advanceFade(int ticks) {
        final float target = level;
        float shown = shownLevel;
        if (shown == target) return;
        if (target > shown) {
            final boolean thawing = shown < 0;
            final float step = (thawing ? THAW_RATE : HEAT_UP_RATE) * ticks;
            shown = Math.min(shown + step, thawing ? Math.min(target, 0F) : target);
        } else {
            final boolean cooling = shown > 0;
            final float step = (cooling ? COOL_DOWN_RATE : FREEZE_RATE) * ticks;
            shown = Math.max(shown - step, cooling ? Math.max(target, 0F) : target);
        }
        shownLevel = shown;

        final int rendered = Math.round(shown);
        if (rendered == renderedLevel) return;
        renderedLevel = rendered;
        applyLight();
        final IGregTechTileEntity base = pipe.getBaseMetaTileEntity();
        if (base != null && base.isClientSide() && base.getWorld() != null) {
            final int x = base.getXCoord(), y = base.getYCoord(), z = base.getZCoord();
            base.getWorld()
                .markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
    }

    private void applyLight() {
        final IGregTechTileEntity base = pipe.getBaseMetaTileEntity();
        if (base != null) base.setLightValue((byte) FluidPipeThermals.getLightLevel(renderedLevel));
    }

    void writeToStream(ByteBuf buffer) {
        buffer.writeByte(level);
    }

    void readFromStream(ByteBuf buffer) {
        level = buffer.readByte();
        // First sync is shown straight away, later changes fade
        if (!synced) {
            synced = true;
            shownLevel = level;
            renderedLevel = level;
        }
        applyLight();
        final IGregTechTileEntity base = pipe.getBaseMetaTileEntity();
        // The renderer fades tracked pipes and drops them once back to normal
        if (base != null && base.getWorld() != null && base.isClientSide() && isActive()) {
            GTMod.proxy.trackThermalPipe(base);
        }
    }

    /** Saved so the pipe's light matches the chunk's saved light on load. */
    void saveNBTData(NBTTagCompound tag) {
        tag.setByte("mThermalLevel", (byte) level);
        tag.setFloat("mShownThermal", shownLevel);
    }

    void loadNBTData(NBTTagCompound tag) {
        level = tag.getByte("mThermalLevel");
        shownLevel = tag.hasKey("mShownThermal") ? tag.getFloat("mShownThermal") : level;
        renderedLevel = Math.round(shownLevel);
        applyLight();
    }

    /** Only the overheating warning, as the client doesn't know the pipe's fluids. */
    void getWailaNBTData(NBTTagCompound tag) {
        if (overheatingTimer > 0) tag.setBoolean(WAILA_OVERHEATING, true);
    }

    /**
     * Client side. The contact warning comes from the synced level so it shows straight away, the overheating warning
     * waits for the server's WAILA data.
     */
    void addWailaBody(List<String> tooltip, @Nullable NBTTagCompound tag) {
        // A hot or frosted pipe hurts on contact, scaled by how far it has faded in
        if (shownLevel != 0F && canHurtOnContact()) {
            final boolean hot = shownLevel > 0F;
            tooltip.add(
                (hot ? EnumChatFormatting.RED : EnumChatFormatting.AQUA) + StatCollector
                    .translateToLocal(hot ? "GT5U.item.pipe.hazard.burn" : "GT5U.item.pipe.hazard.freeze"));
        }
        if (tag != null && tag.getBoolean(WAILA_OVERHEATING)) {
            tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.item.pipe.hazard.overheating"));
        }
    }

    /** @return the average temperature of the fluids in the pipe (not weighted by amount), or null if empty */
    private @Nullable Integer getContentsTemperature() {
        long total = 0;
        int count = 0;
        for (FluidStack fluid : pipe.mFluids) {
            if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) continue;
            total += fluid.getFluid()
                .getTemperature(fluid);
            count++;
        }
        return count == 0 ? null : (int) Math.round((double) total / count);
    }

    /** @return whether any fluid is hotter than the pipe's heat resistance */
    private boolean isOverheating() {
        for (FluidStack fluid : pipe.mFluids) {
            if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) continue;
            if (fluid.getFluid()
                .getTemperature(fluid) > pipe.mHeatResistance) return true;
        }
        return false;
    }

    /** Updates {@link #hazardTemperature} from the contents, clearing it once the pipe is back to normal. */
    private void updateHazard() {
        final Integer temperature = getContentsTemperature();
        if (temperature != null && FluidPipeThermals.getContactDamage(temperature) > 0) {
            hazardTemperature = temperature;
        } else if (!isActive()) {
            hazardTemperature = null;
        }
    }

    /** @return how far the pipe has faded in towards this temperature, from 0 to 1 */
    private float getHazardScale(int temperature) {
        final float full = temperature > HEAT_DAMAGE_TEMPERATURE ? FluidPipeThermals.getHeatGlowLevel(temperature)
            : FluidPipeThermals.getFrostLevel(temperature);
        return GTUtility.clamp(shownLevel / full, 0F, 1F);
    }

    /** Same check as the old collision based damage, which skipped pipes with the top bit of mConnections set. */
    private boolean canHurtOnContact() {
        return (((BaseMetaPipeEntity) pipe.getBaseMetaTileEntity()).mConnections & -128) == 0;
    }

    /**
     * Burns or freezes anything touching the pipe. Not done in {@code onEntityCollidedWithBlock} as that fires for
     * anything inside the block space, so thin pipes hurt from a distance, and never for entities standing against it.
     */
    private void hurtTouchingEntities(IGregTechTileEntity base) {
        if (!canHurtOnContact()) return;
        final Integer temperature = hazardTemperature;
        if (temperature == null) return;
        final float damage = FluidPipeThermals.getContactDamage(temperature) * getHazardScale(temperature);
        if (damage <= 0) return;

        final int x = base.getXCoord(), y = base.getYCoord(), z = base.getZCoord();
        final List<EntityLivingBase> nearby = base.getWorld()
            .getEntitiesWithinAABB(
                EntityLivingBase.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
                    .expand(CONTACT_MARGIN, CONTACT_MARGIN, CONTACT_MARGIN));
        if (nearby.isEmpty()) return;

        final List<AxisAlignedBB> shape = getContactShape(x, y, z);
        final boolean hot = temperature > HEAT_DAMAGE_TEMPERATURE;
        for (EntityLivingBase living : nearby) {
            if (!isTouching(living.boundingBox, shape) || isBehindCover(base, living.boundingBox)) continue;
            if (hot) GTUtility.applyHeatDamage(living, damage);
            else GTUtility.applyFrostDamage(living, damage);
        }
    }

    /** @return whether a cover sits between the pipe and this entity */
    private static boolean isBehindCover(IGregTechTileEntity base, AxisAlignedBB entityBox) {
        final int x = base.getXCoord(), y = base.getYCoord(), z = base.getZCoord();
        final double eps = 1.0E-3D;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (!base.hasCoverAtSide(side)) continue;
            final boolean outside = switch (side) {
                case DOWN -> entityBox.maxY <= y + eps;
                case UP -> entityBox.minY >= y + 1 - eps;
                case NORTH -> entityBox.maxZ <= z + eps;
                case SOUTH -> entityBox.minZ >= z + 1 - eps;
                case WEST -> entityBox.maxX <= x + eps;
                case EAST -> entityBox.minX >= x + 1 - eps;
                default -> false;
            };
            if (outside) return true;
        }
        return false;
    }

    private static boolean isTouching(AxisAlignedBB entityBox, List<AxisAlignedBB> shape) {
        for (AxisAlignedBB part : shape) {
            if (part.expand(CONTACT_MARGIN, CONTACT_MARGIN, CONTACT_MARGIN)
                .intersectsWith(entityBox)) return true;
        }
        return false;
    }

    /** The pipe's shape as a centre box plus an arm per connection. */
    private List<AxisAlignedBB> getContactShape(int x, int y, int z) {
        final List<AxisAlignedBB> shape = new ArrayList<>(7);
        final double thickness = Math.min(1.0D, pipe.getCollisionThickness());
        final double lo = (1.0D - thickness) / 2.0D, hi = 1.0D - lo;
        shape.add(AxisAlignedBB.getBoundingBox(x + lo, y + lo, z + lo, x + hi, y + hi, z + hi));
        if (thickness >= 1.0D) return shape;
        final byte connections = ((BaseMetaPipeEntity) pipe.getBaseMetaTileEntity()).mConnections;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((connections & side.flag) == 0) continue;
            shape.add(
                AxisAlignedBB.getBoundingBox(
                    x + (side.offsetX > 0 ? hi : side.offsetX < 0 ? 0 : lo),
                    y + (side.offsetY > 0 ? hi : side.offsetY < 0 ? 0 : lo),
                    z + (side.offsetZ > 0 ? hi : side.offsetZ < 0 ? 0 : lo),
                    x + (side.offsetX > 0 ? 1 : side.offsetX < 0 ? lo : hi),
                    y + (side.offsetY > 0 ? 1 : side.offsetY < 0 ? lo : hi),
                    z + (side.offsetZ > 0 ? 1 : side.offsetZ < 0 ? lo : hi)));
        }
        return shape;
    }
}
