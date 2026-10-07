package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.Translocator;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.BOTTOM;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.LEFT;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.RIGHT;
import static gregtech.api.metatileentity.implementations.MTEFluidPipe.Border.TOP;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.Optional;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolModes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.client.PipeThermalEffectsRenderer;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.config.Client;
import gregtech.common.config.Other;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverDrain;
import gregtech.common.covers.CoverFluidRegulator;
import io.netty.buffer.ByteBuf;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEFluidPipe extends MetaPipeEntity implements ILocalizedMetaPipeEntity {

    protected static final EnumMap<ForgeDirection, EnumMap<Border, ForgeDirection>> FACE_BORDER_MAP = new EnumMap<>(
        ForgeDirection.class);

    static {
        FACE_BORDER_MAP.put(DOWN, borderMap(NORTH, SOUTH, EAST, WEST));
        FACE_BORDER_MAP.put(UP, borderMap(NORTH, SOUTH, WEST, EAST));
        FACE_BORDER_MAP.put(NORTH, borderMap(UP, DOWN, EAST, WEST));
        FACE_BORDER_MAP.put(SOUTH, borderMap(UP, DOWN, WEST, EAST));
        FACE_BORDER_MAP.put(WEST, borderMap(UP, DOWN, NORTH, SOUTH));
        FACE_BORDER_MAP.put(EAST, borderMap(UP, DOWN, SOUTH, NORTH));
    }

    protected static final Map<Integer, IIconContainer> RESTR_TEXTURE_MAP = new HashMap<>();

    static {
        RESTR_TEXTURE_MAP.put(TOP.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UP);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DOWN);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UD);
        RESTR_TEXTURE_MAP.put(LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_LEFT);
        RESTR_TEXTURE_MAP.put(TOP.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UL);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DL);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | LEFT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NR); // not right
        RESTR_TEXTURE_MAP.put(RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_RIGHT);
        RESTR_TEXTURE_MAP.put(TOP.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_UR);
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_DR);
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NL); // not left
        RESTR_TEXTURE_MAP.put(LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_LR);
        RESTR_TEXTURE_MAP.put(TOP.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_ND); // not down
        RESTR_TEXTURE_MAP.put(BOTTOM.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR_NU); // not up
        RESTR_TEXTURE_MAP.put(TOP.mask | BOTTOM.mask | LEFT.mask | RIGHT.mask, Textures.BlockIcons.PIPE_RESTRICTOR);
    }

    public final float mThickNess;
    public final Materials mMaterial;
    public final int mCapacity, mHeatResistance, mPipeAmount;
    public final boolean mGasProof;
    public final FluidStack[] mFluids;
    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;
    /**
     * Bitmask for whether disable fluid input form each side.
     */
    public byte mDisableInput = 0;

    /** Fluids above this temperature (K) burn on contact and give off heat haze. */
    public static final int HEAT_DAMAGE_TEMPERATURE = 320;
    /** Fluids below this temperature (K) freeze on contact and frost the pipe over. */
    public static final int FROST_DAMAGE_TEMPERATURE = 260;
    /** Fluids at or above this temperature (K) give the brightest glow. */
    public static final int MAX_GLOW_TEMPERATURE = 4000;
    /** Heat levels from {@link #HEAT_DAMAGE_TEMPERATURE} to {@link #MAX_GLOW_TEMPERATURE}. */
    protected static final int HEAT_GLOW_LEVELS = 12;
    /** Fluids at or above this temperature (K) make the pipe glow. */
    public static final int GLOW_START_TEMPERATURE = 800;
    /** Heat level of {@link #GLOW_START_TEMPERATURE}. */
    private static final int GLOW_START_LEVEL = getHeatGlowLevel(GLOW_START_TEMPERATURE);
    /** Glow colour from faintest to brightest: dull red, orange, then yellow-white. */
    private static final short[][] HEAT_GLOW_COLORS = { { 150, 25, 8 }, { 225, 60, 12 }, { 255, 135, 30 },
        { 255, 215, 130 } };
    /** Tint for the pipe texture as it glows: {heat, red, green, blue, strength}, heat and strength in thousandths. */
    private static final int[][] HEAT_TINT_STOPS = { { 364, 150, 35, 15, 450 }, { 550, 225, 80, 20, 500 },
        { 1000, 255, 215, 130, 550 } };
    /** Block light at the faintest and brightest glow. */
    private static final int HEAT_LIGHT_MIN = 3, HEAT_LIGHT_MAX = 15;

    /** Fluids at or below this temperature (K) give the heaviest frost. */
    public static final int MIN_FROST_TEMPERATURE = 4;
    /** Frost levels from {@link #FROST_DAMAGE_TEMPERATURE} to {@link #MIN_FROST_TEMPERATURE}. */
    public static final int FROST_LEVELS = 4;
    /** Tint for the pipe texture as it frosts over, and its strength at the lightest and heaviest frost. */
    private static final short[] FROST_TINT_COLOR = { 215, 235, 255 };
    private static final float FROST_TINT_MIN = 0.15F, FROST_TINT_MAX = 0.55F;

    protected static final byte THERMAL_NONE = 0;
    /** Updates (every 5 ticks) to wait before cooling or thawing, so intermittent flow doesn't flicker. */
    private static final int THERMAL_HOLD_UPDATES = 8;
    /** Updates the overheating warning stays up after the fluid leaves. */
    private static final int OVERHEATING_HOLD_UPDATES = 20;
    /**
     * Levels per tick the pipe moves towards {@link #mThermalLevel}. Full glow takes 3s to heat up and 5s to cool;
     * full frost takes 4s to form and 6s to thaw. As fluid moves along a line, this makes the change travel as a wave.
     */
    private static final float HEAT_UP_RATE = HEAT_GLOW_LEVELS / 60.0F, COOL_DOWN_RATE = HEAT_GLOW_LEVELS / 100.0F;
    private static final float FREEZE_RATE = FROST_LEVELS / 80.0F, THAW_RATE = FROST_LEVELS / 120.0F;

    /**
     * Level the contents are taking the pipe towards: 1 to {@link #HEAT_GLOW_LEVELS} hot, -1 to -{@link #FROST_LEVELS}
     * frosted, or {@link #THERMAL_NONE}. Synced to the client.
     */
    protected byte mThermalLevel = THERMAL_NONE;
    private int mThermalHoldTimer = 0;
    /** Current level, fading towards {@link #mThermalLevel}. Kept on both sides for damage and rendering. */
    private float mShownThermal = 0F;
    /** Client side: false until the first sync, which is shown without fading. */
    private boolean mThermalSynced = false;
    /** {@link #mShownThermal} rounded, used for the pipe texture and block light. */
    protected byte mRenderedThermal = THERMAL_NONE;
    /**
     * Temperature that makes the pipe dangerous to touch, or null if safe. Kept while the pipe is still hot or frosted,
     * since fluid only sits in a flowing pipe part of the time. Server side only.
     */
    private @Nullable Integer mContactHazardTemperature;
    /** Updates left to keep showing the overheating warning. */
    private int mOverheatingTimer = 0;
    private String mPrefixKey;
    private String materialKeyOverride;
    private boolean shouldSkipMaterialTooltip = false;

    public MTEFluidPipe(int aID, String aName, String aPrefixKey, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aPrefixKey, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public MTEFluidPipe(int aID, String aName, String aPrefixKey, float aThickNess, Materials aMaterial, int aCapacity,
        int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aID, aName, 0, false);
        mPrefixKey = aPrefixKey;
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
        addInfo(aID);
    }

    public MTEFluidPipe(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance,
        boolean aGasProof, int aFluidTypes) {
        super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
    }

    @Override
    public byte getTileEntityBaseType() {
        final int level = (mMaterial == null) ? 0 : GTUtility.clamp(mMaterial.mToolQuality, 0, 3);

        HarvestTool tool = switch (level) {
            case 0 -> HarvestTool.WrenchPipeLevel0;
            case 1 -> HarvestTool.WrenchPipeLevel1;
            case 2 -> HarvestTool.WrenchPipeLevel2;
            case 3 -> HarvestTool.WrenchPipeLevel3;
            default -> throw new IllegalStateException("Unexpected tool quality level: " + level);
        };

        return tool.toTileEntityBaseType();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFluidPipe(mName, mThickNess, mMaterial, mCapacity, mHeatResistance, mGasProof, mPipeAmount);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, int connections, int colorIndex,
        boolean connected, boolean redstoneLevel) {
        List<ITexture> textures = new ArrayList<>();

        // Close the face down to a thinner neighbour's size, like a reducer
        final float reducedTo = connected ? getThinnerNeighbourThickness(base, side) : 0;
        textures.add(
            reducedTo > 0 ? getBaseTexture(reducedTo, 1, true, colorIndex) : getBaseTexture(connected, colorIndex));

        if (mDisableInput != 0) {
            int borderMask = 0;

            for (Border border : Border.values()) {
                if (isInputDisabledAtSide(getSideAtBorder(side, border))) borderMask |= border.mask;
            }

            textures.add(getRestrictorTexture(borderMask));
        }

        return textures.toArray(new ITexture[0]);
    }

    protected ITexture getBaseTexture(boolean connected, int colorIndex) {
        return getBaseTexture(mThickNess, mPipeAmount, connected, colorIndex);
    }

    /** Base texture as if this pipe were {@code thickness} thick and carried {@code pipeAmount} fluids. */
    protected ITexture getBaseTexture(float thickness, int pipeAmount, boolean connected, int colorIndex) {
        return getBaseTexture(
            thickness,
            pipeAmount,
            mMaterial.mIconSet,
            mMaterial.mRGBa,
            connected,
            colorIndex,
            mRenderedThermal);
    }

    /** @return the thickness of the pipe on that side if it's thinner than this one, otherwise 0 */
    private float getThinnerNeighbourThickness(IGregTechTileEntity base, ForgeDirection side) {
        if (base == null || base.getWorld() == null) return 0;
        if (!(base.getTileEntityAtSide(side) instanceof IGregTechTileEntity neighbour)
            || !(neighbour.getMetaTileEntity() instanceof MetaPipeEntity neighbourPipe)) return 0;
        final float theirs = neighbourPipe.getCollisionThickness();
        return theirs < getCollisionThickness() - 0.01F ? theirs : 0;
    }

    protected static ITexture getBaseTexture(float aThickNess, int aPipeAmount, TextureSet textureSet, short[] rgba,
        boolean connected, int colorIndex, byte thermalLevel) {
        IIconContainer texture = textureSet.mTextures[OrePrefixes.pipeHuge.getTextureIndex()];

        if (!connected) {
            texture = textureSet.mTextures[OrePrefixes.pipe.getTextureIndex()];
        } else if (aPipeAmount >= 9) {
            texture = textureSet.mTextures[OrePrefixes.pipeNonuple.getTextureIndex()];
        } else if (aPipeAmount >= 4) {
            texture = textureSet.mTextures[OrePrefixes.pipeQuadruple.getTextureIndex()];
        } else if (aThickNess < 0.124F) {
            texture = textureSet.mTextures[OrePrefixes.pipe.getTextureIndex()];
        } else if (aThickNess < 0.374F) {
            texture = textureSet.mTextures[OrePrefixes.pipeTiny.getTextureIndex()];
        } else if (aThickNess < 0.499F) {
            texture = textureSet.mTextures[OrePrefixes.pipeSmall.getTextureIndex()];
        } else if (aThickNess < 0.749F) {
            texture = textureSet.mTextures[OrePrefixes.pipeMedium.getTextureIndex()];
        } else if (aThickNess < 0.874F) {
            texture = textureSet.mTextures[OrePrefixes.pipeLarge.getTextureIndex()];
        }

        rgba = Dyes.getModulation(colorIndex, rgba);

        if (thermalLevel == THERMAL_NONE || !Client.render.renderFluidPipeThermalEffects) {
            return TextureFactory.of(texture, rgba);
        }
        if (thermalLevel < THERMAL_NONE) return TextureFactory.of(texture, getFrostTint(rgba, thermalLevel));
        // Hot but not glowing (e.g. steam) is left untinted; the heat haze shows it
        if (getGlowFraction(thermalLevel) <= 0) return TextureFactory.of(texture, rgba);

        // Glowing: tinted towards the glow colour; the pipe's own block light lights it up
        return TextureFactory.of(texture, getHeatTint(rgba, getHeat(thermalLevel)));
    }

    /** @return 0 at the lowest heat level up to 1 at the highest */
    private static float getHeat(byte thermalLevel) {
        return (float) (Math.min(thermalLevel, HEAT_GLOW_LEVELS) - 1) / (HEAT_GLOW_LEVELS - 1);
    }

    /** @return block light given off at this level, 0 if not glowing */
    protected static int getHeatLightLevel(byte thermalLevel) {
        final float glow = getGlowFraction(thermalLevel);
        if (glow <= 0) return 0;
        return Math.round(HEAT_LIGHT_MIN + glow * (HEAT_LIGHT_MAX - HEAT_LIGHT_MIN));
    }

    /** @return glow strength from just above 0 (dull red) to 1 (yellow-white), or 0 if not hot enough to glow */
    public static float getGlowFraction(float thermalLevel) {
        final int start = GLOW_START_LEVEL;
        return Math.max(0F, (Math.min(thermalLevel, HEAT_GLOW_LEVELS) - start + 1) / (HEAT_GLOW_LEVELS - start + 1));
    }

    /** Sets the pipe's block light from {@link #mRenderedThermal}. */
    private void applyThermalLight() {
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null) base.setLightValue((byte) getHeatLightLevel(mRenderedThermal));
    }

    /** @param heat 0 to 1, see {@link #getHeat} */
    private static short[] getHeatTint(short[] rgba, float heat) {
        final int milli = Math.round(heat * 1000);
        int index = 0;
        while (index < HEAT_TINT_STOPS.length - 2 && milli > HEAT_TINT_STOPS[index + 1][0]) index++;
        final int[] from = HEAT_TINT_STOPS[index], to = HEAT_TINT_STOPS[index + 1];
        final float along = GTUtility.clamp(milli - from[0], 0, to[0] - from[0]) / (float) (to[0] - from[0]);
        final float tint = (from[4] + (to[4] - from[4]) * along) / 1000.0F;
        final short[] result = new short[] { 0, 0, 0, rgba[3] };
        for (int i = 0; i < 3; i++) {
            final float target = from[i + 1] + (to[i + 1] - from[i + 1]) * along;
            result[i] = (short) Math.round(rgba[i] + (target - rgba[i]) * tint);
        }
        return result;
    }

    /** @return glow colour as {r, g, b} (0 to 255) for a glow from 0 to 1 */
    private static float[] getGlowColorAt(float glow) {
        final float position = glow * (HEAT_GLOW_COLORS.length - 1);
        final int index = Math.min((int) position, HEAT_GLOW_COLORS.length - 2);
        final float along = position - index;
        final float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = HEAT_GLOW_COLORS[index][i]
                + (HEAT_GLOW_COLORS[index + 1][i] - HEAT_GLOW_COLORS[index][i]) * along;
        }
        return result;
    }

    /** @return glow colour as {r, g, b} (0 to 255) for a thermal level */
    public static float[] getHeatGlowColor(float thermalLevel) {
        final int start = GLOW_START_LEVEL;
        final float level = GTUtility.clamp(thermalLevel, start, HEAT_GLOW_LEVELS);
        return getGlowColorAt((level - start) / Math.max(1, HEAT_GLOW_LEVELS - start));
    }

    /** @return heat from 0 to 1, or 0 if not hot */
    public static float getHeatFraction(float thermalLevel) {
        if (thermalLevel <= THERMAL_NONE) return 0F;
        return Math.max(0F, (Math.min(thermalLevel, HEAT_GLOW_LEVELS) - 1) / (HEAT_GLOW_LEVELS - 1));
    }

    /** @return frost from 0 (none) to {@link #FROST_LEVELS} (heaviest) */
    public static float getFrostAmount(float thermalLevel) {
        return thermalLevel >= THERMAL_NONE ? 0F : Math.min(-thermalLevel, FROST_LEVELS);
    }

    /** @see #mShownThermal */
    public float getShownThermal() {
        return mShownThermal;
    }

    /** @return whether the pipe is hot or frosted, or still fading in or out */
    public boolean hasThermalEffect() {
        return mThermalLevel != THERMAL_NONE || mShownThermal != 0F;
    }

    /**
     * Moves {@link #mShownThermal} towards {@link #mThermalLevel} by {@code ticks} ticks. Going between hot and frosted
     * passes through normal first. Redraws the pipe when the rounded level changes.
     */
    public void advanceThermalFade(int ticks) {
        final float target = mThermalLevel;
        float shown = mShownThermal;
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
        mShownThermal = shown;

        final byte rendered = (byte) Math.round(shown);
        if (rendered == mRenderedThermal) return;
        mRenderedThermal = rendered;
        applyThermalLight();
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isClientSide() && base.getWorld() != null) {
            final int x = base.getXCoord(), y = base.getYCoord(), z = base.getZCoord();
            base.getWorld()
                .markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
    }

    /** Jumps straight to {@link #mThermalLevel} without fading. */
    private void snapThermalFade() {
        mShownThermal = mThermalLevel;
        mRenderedThermal = mThermalLevel;
    }

    /** Pulls the pipe colour towards icy blue-white, more so the heavier the frost. */
    private static short[] getFrostTint(short[] rgba, byte thermalLevel) {
        final float frost = (float) (getFrostStage(thermalLevel) - 1) / (FROST_LEVELS - 1);
        final float tint = FROST_TINT_MIN + frost * (FROST_TINT_MAX - FROST_TINT_MIN);
        final short[] result = new short[] { 0, 0, 0, rgba[3] };
        for (int i = 0; i < 3; i++) {
            result[i] = (short) Math.round(rgba[i] + (FROST_TINT_COLOR[i] - rgba[i]) * tint);
        }
        return result;
    }

    /** @return frost stage from 1 to {@link #FROST_LEVELS}, or 0 if not frosted */
    public static int getFrostStage(byte thermalLevel) {
        return thermalLevel >= THERMAL_NONE ? 0 : Math.min(-thermalLevel, FROST_LEVELS);
    }

    protected static ITexture getRestrictorTexture(int borderMask) {
        final IIconContainer restrictorIcon = RESTR_TEXTURE_MAP.get(borderMask);
        return restrictorIcon != null ? TextureFactory.of(restrictorIcon) : null;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mDisableInput = aValue;
    }

    @Override
    public byte getUpdateData() {
        return mDisableInput;
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        buffer.writeByte(mThermalLevel);
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        mThermalLevel = buffer.readByte();
        // Show the first sync as is; later changes fade
        if (!mThermalSynced) {
            mThermalSynced = true;
            snapThermalFade();
        }
        applyThermalLight();
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        // The renderer fades tracked pipes and drops them once back to normal
        if (base != null && base.getWorld() != null && base.isClientSide() && hasThermalEffect()) {
            PipeThermalEffectsRenderer.setTracked(base, true);
        }
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return getFluidAmount();
    }

    @Override
    public int maxProgresstime() {
        return getCapacity();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < mPipeAmount; i++) if (mFluids[i] != null)
            aNBT.setTag("mFluid" + (i == 0 ? "" : i), mFluids[i].writeToNBT(new NBTTagCompound()));
        aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
        // Saved so the pipe's light matches the chunk's saved light on load
        aNBT.setByte("mThermalLevel", mThermalLevel);
        aNBT.setFloat("mShownThermal", mShownThermal);
        if (GTMod.proxy.gt6Pipe) {
            aNBT.setByte("mConnections", mConnections);
            aNBT.setByte("mDisableInput", mDisableInput);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < mPipeAmount; i++)
            mFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid" + (i == 0 ? "" : i)));
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
        mThermalLevel = aNBT.getByte("mThermalLevel");
        mShownThermal = aNBT.hasKey("mShownThermal") ? aNBT.getFloat("mShownThermal") : mThermalLevel;
        mRenderedThermal = (byte) Math.round(mShownThermal);
        applyThermalLight();
        if (GTMod.proxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
            mDisableInput = aNBT.getByte("mDisableInput");
        }
    }

    /**
     * Burns or freezes anything touching the pipe itself. {@code onEntityCollidedWithBlock} isn't used as it fires for
     * anything in the block space (thin pipes hurt from a distance) and never for anything on the outside of it.
     */
    private void hurtTouchingEntities(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!canHurtOnContact()) return;
        final Integer temperature = mContactHazardTemperature;
        if (temperature == null) return;
        // Scales with how far the pipe has faded in
        final float damage = getContactDamage(temperature) * getContactHazardScale(temperature);
        if (damage <= 0) return;

        final int x = aBaseMetaTileEntity.getXCoord(), y = aBaseMetaTileEntity.getYCoord(),
            z = aBaseMetaTileEntity.getZCoord();
        final List<EntityLivingBase> nearby = aBaseMetaTileEntity.getWorld()
            .getEntitiesWithinAABB(
                EntityLivingBase.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
                    .expand(CONTACT_MARGIN, CONTACT_MARGIN, CONTACT_MARGIN));
        if (nearby.isEmpty()) return;

        final List<AxisAlignedBB> shape = getContactShape(x, y, z);

        final boolean hot = temperature > HEAT_DAMAGE_TEMPERATURE;
        for (EntityLivingBase living : nearby) {
            if (!isTouching(living.boundingBox, shape) || isBehindCover(aBaseMetaTileEntity, living.boundingBox))
                continue;
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

    /** Collision leaves entities exactly flush, which strict AABB intersection doesn't count as touching. */
    private static final double CONTACT_MARGIN = 1.0E-3D;

    private static boolean isTouching(AxisAlignedBB entityBox, List<AxisAlignedBB> shape) {
        for (AxisAlignedBB part : shape) {
            if (part.expand(CONTACT_MARGIN, CONTACT_MARGIN, CONTACT_MARGIN)
                .intersectsWith(entityBox)) return true;
        }
        return false;
    }

    /** The pipe's visible shape: its centre plus an arm per connection (the collision box fills in bends). */
    private List<AxisAlignedBB> getContactShape(int x, int y, int z) {
        final List<AxisAlignedBB> shape = new ArrayList<>(7);
        final double thickness = Math.min(1.0D, getCollisionThickness());
        final double lo = (1.0D - thickness) / 2.0D, hi = 1.0D - lo;
        shape.add(AxisAlignedBB.getBoundingBox(x + lo, y + lo, z + lo, x + hi, y + hi, z + hi));
        if (thickness >= 1.0D) return shape;
        final byte connections = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
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

    /** Updates {@link #mContactHazardTemperature} from the contents, clearing it once the pipe is back to normal. */
    private void updateContactHazard() {
        final Integer current = getContactHazardTemperature();
        if (current != null) mContactHazardTemperature = current;
        else if (!hasThermalEffect()) mContactHazardTemperature = null;
    }

    /** @return how far the pipe has faded in towards this temperature, from 0 to 1 */
    private float getContactHazardScale(int temperature) {
        final float full = temperature > HEAT_DAMAGE_TEMPERATURE ? getHeatGlowLevel(temperature)
            : getFrostLevel(temperature);
        return GTUtility.clamp(mShownThermal / full, 0F, 1F);
    }

    /** Pipes boxed in (e.g. by a frame) can't be touched. */
    private boolean canHurtOnContact() {
        return (((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0;
    }

    /** @return the contents' temperature if touching the pipe would hurt, otherwise null */
    private @Nullable Integer getContactHazardTemperature() {
        final Integer temperature = getContentsTemperature();
        return temperature != null && getContactDamage(temperature) > 0 ? temperature : null;
    }

    /** @return contact damage at this temperature before armour and difficulty scaling, 0 if safe */
    public static float getContactDamage(int temperature) {
        if (temperature > HEAT_DAMAGE_TEMPERATURE) return (temperature - 300) / 50.0F;
        if (temperature < FROST_DAMAGE_TEMPERATURE) return (270 - temperature) / 25.0F;
        return 0;
    }

    @Override
    public boolean needsClientTick() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 5 == 0) {
            updateThermalLevel(aBaseMetaTileEntity);
            advanceThermalFade(5);
            updateContactHazard();
            if (isOverheating()) mOverheatingTimer = OVERHEATING_HOLD_UPDATES;
            else if (mOverheatingTimer > 0) mOverheatingTimer--;
            hurtTouchingEntities(aBaseMetaTileEntity);
            // Every update, so a pipe swapped in for a hot one doesn't keep its light
            applyThermalLight();

            mLastReceivedFrom &= 63;
            if (mLastReceivedFrom == 63) {
                mLastReceivedFrom = 0;
            }

            if (!GTMod.proxy.gt6Pipe || mCheckConnections) checkConnections();

            final boolean shouldDistribute = (oLastReceivedFrom == mLastReceivedFrom);
            for (int i = 0, j = aBaseMetaTileEntity.getRandomNumber(mPipeAmount); i < mPipeAmount; i++) {
                final int index = (i + j) % mPipeAmount;
                if (mFluids[index] != null && mFluids[index].amount <= 0) mFluids[index] = null;
                if (mFluids[index] == null) continue;

                if (checkEnvironment(index, aBaseMetaTileEntity)) return;

                if (shouldDistribute) {
                    distributeFluid(index, aBaseMetaTileEntity);
                    mLastReceivedFrom = 0;
                }
            }

            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    /**
     * Sets {@link #mThermalLevel} from the contents. Hotter or colder applies straight away; cooling or thawing waits
     * {@link #THERMAL_HOLD_UPDATES} updates.
     */
    private void updateThermalLevel(IGregTechTileEntity aBaseMetaTileEntity) {
        final byte current = getThermalLevelOfContents();

        if (current == mThermalLevel) {
            if (current != THERMAL_NONE) mThermalHoldTimer = THERMAL_HOLD_UPDATES;
            return;
        }

        final boolean immediate = mThermalLevel == THERMAL_NONE || (current > THERMAL_NONE && current > mThermalLevel)
            || (current < THERMAL_NONE && mThermalLevel < THERMAL_NONE && current < mThermalLevel);
        if (!immediate && --mThermalHoldTimer > 0) return;

        mThermalLevel = current;
        mThermalHoldTimer = current == THERMAL_NONE ? 0 : THERMAL_HOLD_UPDATES;
        aBaseMetaTileEntity.issueTileUpdate();
    }

    /** @return the thermal level for {@link #getContentsTemperature()} */
    private byte getThermalLevelOfContents() {
        final Integer temperature = getContentsTemperature();
        if (temperature == null) return THERMAL_NONE;
        if (temperature > HEAT_DAMAGE_TEMPERATURE) return getHeatGlowLevel(temperature);
        if (temperature < FROST_DAMAGE_TEMPERATURE) return getFrostLevel(temperature);
        return THERMAL_NONE;
    }

    /** @return the average temperature of the fluids in the pipe, each counted once, or null if empty */
    private @Nullable Integer getContentsTemperature() {
        long total = 0;
        int count = 0;
        for (FluidStack tFluid : mFluids) {
            if (tFluid == null || tFluid.amount <= 0 || tFluid.getFluid() == null) continue;
            total += tFluid.getFluid()
                .getTemperature(tFluid);
            count++;
        }
        return count == 0 ? null : (int) Math.round((double) total / count);
    }

    /** @return frost level, -1 to -{@link #FROST_LEVELS}, on a log scale below {@link #FROST_DAMAGE_TEMPERATURE} */
    protected static byte getFrostLevel(int temperature) {
        final double fraction = Math.log((double) FROST_DAMAGE_TEMPERATURE / Math.max(1, temperature))
            / Math.log((double) FROST_DAMAGE_TEMPERATURE / MIN_FROST_TEMPERATURE);
        return (byte) -(1 + GTUtility.clamp((int) (fraction * FROST_LEVELS), 0, FROST_LEVELS - 1));
    }

    /** @return heat level, 1 to {@link #HEAT_GLOW_LEVELS}, on a log scale above {@link #HEAT_DAMAGE_TEMPERATURE} */
    protected static byte getHeatGlowLevel(int temperature) {
        final int levels = HEAT_GLOW_LEVELS;
        final double fraction = Math.log((double) temperature / HEAT_DAMAGE_TEMPERATURE)
            / Math.log((double) MAX_GLOW_TEMPERATURE / HEAT_DAMAGE_TEMPERATURE);
        return (byte) (1 + GTUtility.clamp((int) (fraction * levels), 0, levels - 1));
    }

    private boolean checkEnvironment(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        // Check for hot liquids that melt the pipe or gasses that escape and burn/freeze people
        final FluidStack tFluid = mFluids[index];

        if (tFluid != null && tFluid.amount > 0) {
            final int tTemperature = tFluid.getFluid()
                .getTemperature(tFluid);
            if (tTemperature > mHeatResistance) {
                if (aBaseMetaTileEntity.getRandomNumber(100) == 0) {
                    // Poof
                    GTLog.writeExplosionLog(aBaseMetaTileEntity, mName, "set to fire due to low heat resistance");
                    aBaseMetaTileEntity.setToFire();
                    return true;
                }
                // Mmhmm, Fire
                aBaseMetaTileEntity.setOnFire();

                GTLog.writeExplosionLog(
                    aBaseMetaTileEntity,
                    mName,
                    "set blocks around to fire due to low heat resistance");
            }
            if (!mGasProof && tFluid.getFluid()
                .isGaseous(tFluid)) {
                tFluid.amount -= 5;
                sendSound((byte) 9);
                if (tTemperature > HEAT_DAMAGE_TEMPERATURE) {
                    for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorld()
                        .getEntitiesWithinAABB(
                            EntityLivingBase.class,
                            AxisAlignedBB.getBoundingBox(
                                getBaseMetaTileEntity().getXCoord() - 2,
                                getBaseMetaTileEntity().getYCoord() - 2,
                                getBaseMetaTileEntity().getZCoord() - 2,
                                getBaseMetaTileEntity().getXCoord() + 3,
                                getBaseMetaTileEntity().getYCoord() + 3,
                                getBaseMetaTileEntity().getZCoord() + 3))) {
                        GTUtility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
                    }
                } else if (tTemperature < FROST_DAMAGE_TEMPERATURE) {
                    for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorld()
                        .getEntitiesWithinAABB(
                            EntityLivingBase.class,
                            AxisAlignedBB.getBoundingBox(
                                getBaseMetaTileEntity().getXCoord() - 2,
                                getBaseMetaTileEntity().getYCoord() - 2,
                                getBaseMetaTileEntity().getZCoord() - 2,
                                getBaseMetaTileEntity().getXCoord() + 3,
                                getBaseMetaTileEntity().getYCoord() + 3,
                                getBaseMetaTileEntity().getZCoord() + 3))) {
                        GTUtility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
                    }
                }
            }
            if (tFluid.amount <= 0) mFluids[index] = null;
        }
        return false;
    }

    private void distributeFluid(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        final FluidStack tFluid = mFluids[index];
        if (tFluid == null) return;

        // Tank, From, Amount to receive
        final List<MutableTriple<IFluidHandler, ForgeDirection, Integer>> tTanks = new ArrayList<>();
        final int amount = tFluid.amount;
        final byte tOffset = (byte) getBaseMetaTileEntity().getRandomNumber(6);
        for (final byte i : ALL_VALID_SIDES) {
            // Get a list of tanks accepting fluids, and what side they're on
            final ForgeDirection side = ForgeDirection.getOrientation((i + tOffset) % 6);
            final ForgeDirection oppositeSide = side.getOpposite();
            final IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(side);
            final IGregTechTileEntity gTank = tTank instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTank : null;

            if (isConnectedAtSide(side) && tTank != null
                && (mLastReceivedFrom & side.flag) == 0
                && getBaseMetaTileEntity().getCoverAtSide(side)
                    .letsFluidOut(tFluid.getFluid())
                && (gTank == null || gTank.getCoverAtSide(oppositeSide)
                    .letsFluidIn(tFluid.getFluid()))) {
                if (tTank.fill(oppositeSide, tFluid, false) > 0) {
                    tTanks.add(new MutableTriple<>(tTank, oppositeSide, 0));
                }
                tFluid.amount = amount; // Because some mods do actually modify input fluid stack
            }
        }

        // How much of this fluid is available for distribution?
        final double tAmount = Math.max(1, Math.min(mCapacity * 10, tFluid.amount));

        final FluidStack maxFluid = tFluid.copy();
        maxFluid.amount = Integer.MAX_VALUE;

        double availableCapacity = 0;
        // Calculate available capacity for distribution from all tanks
        for (final MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry : tTanks) {
            tEntry.right = tEntry.left.fill(tEntry.middle, maxFluid, false);
            availableCapacity += tEntry.right;
        }

        // Now distribute
        for (final MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry : tTanks) {
            // Distribue fluids based on percentage available space at destination
            if (availableCapacity > tAmount)
                tEntry.right = (int) Math.floor(tEntry.right * tAmount / availableCapacity);

            // If the percent is not enough to give at least 1L, try to give 1L
            if (tEntry.right == 0) tEntry.right = (int) Math.min(1, tAmount);

            if (tEntry.right <= 0) continue;

            final int tFilledAmount = tEntry.left
                .fill(tEntry.middle, drainFromIndex(tEntry.right, false, index), false);

            if (tFilledAmount > 0) tEntry.left.fill(tEntry.middle, drainFromIndex(tFilledAmount, true, index), true);

            if (mFluids[index] == null || mFluids[index].amount <= 0) return;
        }
    }

    public void connectPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer) {
        if (!isConnectedAtSide(side)) {
            if (connect(side) > 0) GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.connected");
        } else {
            disconnect(side);
            GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.disconnected");
        }
    }

    public void blockPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer, byte mask) {
        if (isInputDisabledAtSide(side)) {
            mDisableInput &= ~mask;
            GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.pipe.input.enable");
        } else {
            mDisableInput |= mask;
            GTUtility.sendChatTrans(entityPlayer, "GT5U.chat.pipe.input.disable");
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Only trigger if the player is sneaking
        if (!aPlayer.isSneaking()) {
            return;
        }

        // Retrieve the item's MetaTileEntity
        final ItemStack handItem = aPlayer.inventory.getCurrentItem();
        if (handItem == null) return;

        IMetaTileEntity meta = ItemMachines.getMetaTileEntity(handItem);
        if (!(meta instanceof MTEFluidPipe handFluid)) return;

        // Preserve old connections and meta ID
        byte oldConnections = this.mConnections;
        short oldMetaID = (short) aBaseMetaTileEntity.getMetaTileID();

        // Create the new fluid pipe
        MTEFluidPipe newPipe = (MTEFluidPipe) handFluid.newMetaEntity(aBaseMetaTileEntity);
        if (newPipe == null) return;

        // Preserve old connections
        newPipe.mConnections = oldConnections;
        newPipe.mDisableInput = this.mDisableInput;

        // Record old pipe parameters
        long oldCapacity = this.mCapacity;
        boolean oldGasProof = this.mGasProof;
        int oldHeatResistance = this.mHeatResistance;

        // Add fluid to the new pipe
        if (this.mPipeAmount <= newPipe.mPipeAmount) {
            for (int i = 0; i < mPipeAmount; i++) {
                if (this.mFluids[i] != null) {
                    newPipe.mFluids[i] = this.mFluids[i].copy();
                    newPipe.mFluids[i].amount = Math.min(this.mFluids[i].amount, newPipe.getCapacity());
                }
            }
        }

        // Update to the new pipe
        aBaseMetaTileEntity.setMetaTileID((short) handItem.getItemDamage());
        newPipe.setBaseMetaTileEntity(aBaseMetaTileEntity);

        // Construct a change message if needed
        IChatComponent message = new ChatComponentText("");
        boolean hasContent = false;

        // Compare capacity changes
        if (oldCapacity != newPipe.mCapacity) {
            EnumChatFormatting capacityColor = newPipe.mCapacity > oldCapacity ? EnumChatFormatting.GREEN
                : EnumChatFormatting.RED;
            message.appendText(formatNumber(oldCapacity * 20));
            message.appendSibling(new ChatComponentTranslation("gt.unit.liter_per_second"));
            message.appendText(" → ");
            message.appendSibling(
                new ChatComponentText(formatNumber(newPipe.mCapacity * 20L))
                    .setChatStyle(new ChatStyle().setColor(capacityColor)));
            message.appendSibling(
                new ChatComponentTranslation("gt.unit.liter_per_second")
                    .setChatStyle(new ChatStyle().setColor(capacityColor)));
            hasContent = true;
        }

        // Compare heat resistance
        if (oldHeatResistance != newPipe.mHeatResistance) {
            if (hasContent) message.appendText(" | ");
            EnumChatFormatting heatColor = newPipe.mHeatResistance > oldHeatResistance ? EnumChatFormatting.GREEN
                : EnumChatFormatting.RED;
            message.appendText(formatNumber(oldHeatResistance) + "K → ");
            message.appendSibling(
                new ChatComponentText(formatNumber(newPipe.mHeatResistance) + "K")
                    .setChatStyle(new ChatStyle().setColor(heatColor)));
            hasContent = true;
        }

        // Compare gas handling
        if (oldGasProof != newPipe.mGasProof) {
            if (hasContent) message.appendText(" | ");
            EnumChatFormatting gasProofColor = newPipe.mGasProof ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
            message.appendSibling(
                new ChatComponentTranslation(
                    newPipe.mGasProof ? "GT5U.item.pipe.swap.now_gas_proof" : "GT5U.item.pipe.swap.no_longer_gas_proof")
                        .setChatStyle(new ChatStyle().setColor(gasProofColor)));
            hasContent = true;
        }

        // Send a chat message if anything changed. Only send server-side, since this method also runs
        // client-side for responsive placement and would otherwise send the message twice.
        if (hasContent && aBaseMetaTileEntity.isServerSide()) {
            GTUtility.sendChatTrans(aPlayer, "GT5U.item.pipe.swap.s", message);
        }

        // Force updates to sync changes
        aBaseMetaTileEntity.markDirty();
        aBaseMetaTileEntity.issueTextureUpdate();
        aBaseMetaTileEntity.issueBlockUpdate();
        aBaseMetaTileEntity.issueTileUpdate();

        // Handle inventory operations unless in creative mode
        if (!aPlayer.capabilities.isCreativeMode) {
            ItemStack oldPipe = new ItemStack(handItem.getItem(), 1, oldMetaID);
            boolean addedToInventory = false;

            // Attempt to stack with existing items
            if (oldPipe != null) {
                for (int i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
                    ItemStack slot = aPlayer.inventory.mainInventory[i];
                    if (slot != null && slot.getItem() == oldPipe.getItem()
                        && slot.getItemDamage() == oldPipe.getItemDamage()
                        && slot.stackSize < slot.getMaxStackSize()) {
                        slot.stackSize++;
                        addedToInventory = true;
                        break;
                    }
                }
                // Add new stack if stacking failed
                if (!addedToInventory) {
                    addedToInventory = aPlayer.inventory.addItemStackToInventory(oldPipe);
                }
                // If still unsuccessful, drop the item
                if (!addedToInventory) {
                    aPlayer.dropPlayerItemWithRandomChoice(oldPipe, false);
                }
            }

            // Decrement the placed pipe from the player's hand
            handItem.stackSize--;
            if (handItem.stackSize <= 0) {
                aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (GTMod.proxy.gt6Pipe) {
            final int mode = MetaGeneratedTool.getToolMode(aTool);
            IGregTechTileEntity currentPipeBase = getBaseMetaTileEntity();
            MTEFluidPipe currentPipe = (MTEFluidPipe) currentPipeBase.getMetaTileEntity();
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
            final byte tMask = (byte) (tSide.flag);

            if (mode == ToolModes.REGULAR.get()) {
                if (entityPlayer.isSneaking()) {
                    currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                } else currentPipe.connectPipeOnSide(tSide, entityPlayer);
                return true;
            }

            if (mode == ToolModes.WRENCH_LINE.get()) {

                boolean initialState = entityPlayer.isSneaking() ? currentPipe.isInputDisabledAtSide(tSide)
                    : currentPipe.isConnectedAtSide(tSide);

                boolean wasActionPerformed = false;

                int limit = Other.pipeWrenchingChainRange;
                for (int connected = 0; connected < limit; connected++) {

                    TileEntity nextPipeBaseTile = currentPipeBase.getTileEntityAtSide(tSide);

                    // if next tile doesn't exist or if next tile is not GT tile
                    if (!(nextPipeBaseTile instanceof IGregTechTileEntity nextPipeBase)) {
                        return wasActionPerformed;
                    }

                    // if next tile is wrong color
                    if (!currentPipe.connectableColor(nextPipeBaseTile)) {
                        return wasActionPerformed;
                    }

                    MTEFluidPipe nextPipe = nextPipeBase.getMetaTileEntity() instanceof MTEFluidPipe
                        ? (MTEFluidPipe) nextPipeBase.getMetaTileEntity()
                        : null;

                    // if next tile entity is not a pipe
                    if (nextPipe == null) {
                        return wasActionPerformed;
                    }

                    // if pipes are same size
                    if (mPipeAmount != nextPipe.mPipeAmount) {
                        return wasActionPerformed;
                    }

                    // making sure next pipe has same fluid
                    for (int i = 0; i < mPipeAmount; i++) {
                        if (mFluids[i] != null && nextPipe.mFluids[i] != null) {
                            if (!mFluids[i].isFluidEqual(nextPipe.mFluids[i])) {
                                return wasActionPerformed;
                            }
                        } else if (mFluids[i] == null || nextPipe.mFluids[i] == null) {
                            // one pipe is empty, so it doesn't matter if the other has fluid.
                        } else if (mFluids[i] != nextPipe.mFluids[i]) {
                            return wasActionPerformed;
                        }
                    }

                    boolean currentState = entityPlayer.isSneaking() ? currentPipe.isInputDisabledAtSide(tSide)
                        : currentPipe.isConnectedAtSide(tSide);

                    /*
                     * Making sure next pipe will have same action applied to it e.g. Connecting pipe won`t trigger
                     * disconnect if next pipe is already connected
                     */
                    if (currentState != initialState) {
                        return wasActionPerformed;
                    }

                    if (entityPlayer.isSneaking()) {
                        currentPipe.blockPipeOnSide(tSide, entityPlayer, tMask);
                    } else currentPipe.connectPipeOnSide(tSide, entityPlayer);

                    wasActionPerformed = true;

                    currentPipeBase = nextPipeBase;
                    currentPipe = nextPipe;

                }
                return wasActionPerformed;
            }
        }
        return false;
    }

    @Override
    public boolean letsIn(Cover cover) {
        return cover.letsFluidIn(null);
    }

    @Override
    public boolean letsOut(Cover cover) {
        return cover.letsFluidOut(null);
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        if (tileEntity == null) return false;

        final ForgeDirection oppositeSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null) return false;

        final Cover cover = baseMetaTile.getCoverAtSide(side);
        final IGregTechTileEntity gTileEntity = (tileEntity instanceof IGregTechTileEntity)
            ? (IGregTechTileEntity) tileEntity
            : null;

        if (cover instanceof CoverDrain || (TinkerConstruct.isModLoaded() && isTConstructFaucet(tileEntity)))
            return true;

        final IFluidHandler fTileEntity = (tileEntity instanceof IFluidHandler) ? (IFluidHandler) tileEntity : null;

        if (fTileEntity != null) {
            final FluidTankInfo[] tInfo = fTileEntity.getTankInfo(oppositeSide);
            if (tInfo != null) {
                return tInfo.length > 0 || (Translocator.isModLoaded() && isTranslocator(tileEntity))
                    || gTileEntity != null && gTileEntity.getCoverAtSide(oppositeSide) instanceof CoverFluidRegulator;
            }
        }
        return false;
    }

    @Optional.Method(modid = Mods.ModIDs.TINKER_CONSTRUCT)
    private boolean isTConstructFaucet(TileEntity tTileEntity) {
        // Tinker Construct Faucets return a null tank info, so check the class
        return tTileEntity instanceof tconstruct.smeltery.logic.FaucetLogic;
    }

    @Optional.Method(modid = Mods.ModIDs.TRANSLOCATOR)
    private boolean isTranslocator(TileEntity tTileEntity) {
        // Translocators return a TankInfo, but it's of 0 length - so check the class if we see this pattern
        return tTileEntity instanceof codechicken.translocator.TileLiquidTranslocator;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.proxy.gt6Pipe;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GTUtility.doSoundAtClient(SoundResource.RANDOM_FIZZ, 5, 1.0F, aX, aY, aZ);

            new ParticleEventBuilder().setIdentifier(ParticleFX.CLOUD)
                .setWorld(getBaseMetaTileEntity().getWorld())
                .<ParticleEventBuilder>times(
                    6,
                    (x, i) -> x
                        .setMotion(
                            ForgeDirection.getOrientation(i).offsetX / 5.0,
                            ForgeDirection.getOrientation(i).offsetY / 5.0,
                            ForgeDirection.getOrientation(i).offsetZ / 5.0)
                        .setPosition(
                            aX - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aY - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aZ - 0.5 + XSTR_INSTANCE.nextFloat())
                        .run());
        }
    }

    @Override
    public final int getCapacity() {
        return mCapacity * 20 * mPipeAmount;
    }

    @Override
    public FluidTankInfo getInfo() {
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) return new FluidTankInfo(tFluid, mCapacity * 20);
        }
        return new FluidTankInfo(null, mCapacity * 20);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().isSteampowered()) return GTValues.emptyFluidTankInfo;
        ArrayList<FluidTankInfo> tList = new ArrayList<>();
        for (FluidStack tFluid : mFluids) tList.add(new FluidTankInfo(tFluid, mCapacity * 20));
        return tList.toArray(new FluidTankInfo[mPipeAmount]);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final FluidStack getFluid() {
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) return tFluid;
        }
        return null;
    }

    @Override
    public final int getFluidAmount() {
        int rAmount = 0;
        for (FluidStack tFluid : mFluids) {
            if (tFluid != null) rAmount += tFluid.amount;
        }
        return rAmount;
    }

    @Override
    public final int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0) return 0;

        int index = -1;
        for (int i = 0; i < mPipeAmount; i++) {
            if (mFluids[i] != null && mFluids[i].isFluidEqual(aFluid)) {
                index = i;
                break;
            } else if ((mFluids[i] == null || mFluids[i].getFluid()
                .getID() <= 0) && index < 0) {
                    index = i;
                }
        }

        return fill_default_intoIndex(side, aFluid, doFill, index);
    }

    private int fill_default_intoIndex(ForgeDirection side, FluidStack aFluid, boolean doFill, int index) {
        if (index < 0 || index >= mPipeAmount) return 0;
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0) return 0;

        final int ordinalSide = side.ordinal();

        if (mFluids[index] == null || mFluids[index].getFluid()
            .getID() <= 0) {
            if (aFluid.amount * mPipeAmount <= getCapacity()) {
                if (doFill) {
                    mFluids[index] = aFluid.copy();
                    mLastReceivedFrom |= (1 << ordinalSide);
                }
                return aFluid.amount;
            }
            if (doFill) {
                mFluids[index] = aFluid.copy();
                mLastReceivedFrom |= (1 << ordinalSide);
                mFluids[index].amount = getCapacity() / mPipeAmount;
            }
            return getCapacity() / mPipeAmount;
        }

        if (!mFluids[index].isFluidEqual(aFluid)) return 0;

        final int space = getCapacity() / mPipeAmount - mFluids[index].amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                mFluids[index].amount += aFluid.amount;
                mLastReceivedFrom |= (1 << ordinalSide);
            }
            return aFluid.amount;
        }
        if (doFill) {
            mFluids[index].amount = getCapacity() / mPipeAmount;
            mLastReceivedFrom |= (1 << ordinalSide);
        }
        return space;
    }

    @Override
    public final FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack drained;
        for (int i = 0; i < mPipeAmount; i++) {
            if ((drained = drainFromIndex(maxDrain, doDrain, i)) != null) return drained;
        }
        return null;
    }

    private FluidStack drainFromIndex(int maxDrain, boolean doDrain, int index) {
        if (index < 0 || index >= mPipeAmount) return null;
        if (mFluids[index] == null) return null;
        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
            return null;
        }

        int used = maxDrain;
        if (mFluids[index].amount < used) used = mFluids[index].amount;

        if (doDrain) {
            mFluids[index].amount -= used;
        }

        final FluidStack drained = mFluids[index].copy();
        drained.amount = used;

        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
        }

        return drained;
    }

    @Override
    public String[] getDescription() {
        List<String> descriptions = new ArrayList<>();
        descriptions.add(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.fluidpipe.capacity.desc", formatNumber(mCapacity * 20L)));
        descriptions.add(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.fluidpipe.heat.desc", formatNumber(mHeatResistance)));
        if (!mGasProof) {
            descriptions.add(StatCollector.translateToLocal("gt.blockmachines.fluidpipe.no_gas_proof.desc"));
        }
        if (mPipeAmount != 1) {
            descriptions.add(
                StatCollector.translateToLocalFormatted("gt.blockmachines.fluidpipe.pipe_amount.desc", mPipeAmount));
        }
        return descriptions.toArray(new String[0]);
    }

    @Override
    public float getCollisionThickness() {
        return mThickNess;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return !isInputDisabledAtSide(side);
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return true;
    }

    public boolean isInputDisabledAtSide(ForgeDirection side) {
        return (mDisableInput & side.flag) != 0;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (aFluid == null) return null;
        for (int i = 0; i < mFluids.length; ++i) {
            final FluidStack f = mFluids[i];
            if (f == null || !f.isFluidEqual(aFluid)) continue;
            return drainFromIndex(aFluid.amount, doDrain, i);
        }
        return null;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        // Basic pipe stats
        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.pipe.capacity") + ": "
                + EnumChatFormatting.BLUE
                + formatNumber(mCapacity * 20L)
                + " L/s");

        currenttip.add(
            StatCollector.translateToLocal("GT5U.item.pipe.heat_resistance") + ": "
                + EnumChatFormatting.RED
                + formatNumber(mHeatResistance)
                + "K");

        // Gas handling info
        if (mGasProof) {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.gas_proof") + ": "
                    + EnumChatFormatting.GREEN
                    + StatCollector.translateToLocal("GT5U.item.pipe.gas_proof.yes"));
        } else {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.gas_proof") + ": "
                    + EnumChatFormatting.RED
                    + StatCollector.translateToLocal("GT5U.item.pipe.gas_proof.no"));
        }

        // Multi-pipe info
        if (mPipeAmount > 1) {
            currenttip.add(
                StatCollector.translateToLocal("GT5U.item.pipe.amount") + ": " + EnumChatFormatting.AQUA + mPipeAmount);
        }

        // Overheating takes priority over the contact warning
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag != null && tag.getBoolean(WAILA_OVERHEATING)) {
            currenttip
                .add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.item.pipe.hazard.overheating"));
        } else if (tag != null && tag.hasKey(WAILA_HAZARD_TEMPERATURE)) {
            final int temperature = tag.getInteger(WAILA_HAZARD_TEMPERATURE);
            final boolean hot = temperature > HEAT_DAMAGE_TEMPERATURE;
            currenttip.add(
                (hot ? EnumChatFormatting.RED : EnumChatFormatting.AQUA) + StatCollector
                    .translateToLocal(hot ? "GT5U.item.pipe.hazard.burn" : "GT5U.item.pipe.hazard.freeze"));
        }
    }

    private static final String WAILA_HAZARD_TEMPERATURE = "contactHazardTemperature";
    private static final String WAILA_OVERHEATING = "overheating";

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final Integer temperature = mContactHazardTemperature;
        if (temperature != null && canHurtOnContact() && getContactHazardScale(temperature) > 0) {
            tag.setInteger(WAILA_HAZARD_TEMPERATURE, temperature);
        }
        if (mOverheatingTimer > 0) tag.setBoolean(WAILA_OVERHEATING, true);
    }

    /** @return whether any fluid is hotter than the pipe's heat resistance */
    private boolean isOverheating() {
        for (FluidStack tFluid : mFluids) {
            if (tFluid == null || tFluid.amount <= 0 || tFluid.getFluid() == null) continue;
            if (tFluid.getFluid()
                .getTemperature(tFluid) > mHeatResistance) return true;
        }
        return false;
    }

    private static EnumMap<Border, ForgeDirection> borderMap(ForgeDirection topSide, ForgeDirection bottomSide,
        ForgeDirection leftSide, ForgeDirection rightSide) {
        final EnumMap<Border, ForgeDirection> sideMap = new EnumMap<>(Border.class);
        sideMap.put(TOP, topSide);
        sideMap.put(BOTTOM, bottomSide);
        sideMap.put(LEFT, leftSide);
        sideMap.put(RIGHT, rightSide);
        return sideMap;
    }

    protected static ForgeDirection getSideAtBorder(ForgeDirection side, Border border) {
        return FACE_BORDER_MAP.get(side)
            .get(border);
    }

    @Override
    public IOreMaterial getMaterial() {
        return mMaterial;
    }

    @Override
    public String getPrefixKey() {
        return mPrefixKey;
    }

    @Override
    public String getMaterialKeyOverride() {
        return materialKeyOverride;
    }

    @Override
    public boolean shouldSkipMaterialTooltip() {
        return shouldSkipMaterialTooltip;
    }

    public MTEFluidPipe renameMaterial(String newName) {
        if (newName == null) return this;
        final String key = mMaterial.getLocalizedNameKey() + ".fluidpipe.newname";
        GTLanguageManager.addStringLocalization(key, newName);
        this.materialKeyOverride = key;
        return this;
    }

    public MTEFluidPipe setShouldSkipMaterialTooltip(boolean shouldSkipMaterialTooltip) {
        this.shouldSkipMaterialTooltip = shouldSkipMaterialTooltip;
        return this;
    }

    protected enum Border {

        TOP(),
        BOTTOM(),
        LEFT(),
        RIGHT();

        public final int mask;

        Border() {
            mask = 1 << this.ordinal();
        }
    }
}
