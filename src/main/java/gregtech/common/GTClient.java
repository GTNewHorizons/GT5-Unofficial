// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: GT_Client.java

package gregtech.common;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GregTech;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.glodblock.github.nei.recipes.FluidRecipe;
import com.glodblock.github.nei.recipes.extractor.GregTech5RecipeExtractor;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;

import appeng.api.util.IOrientable;
import appeng.tile.misc.TileInterface;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Translation;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.hazards.HazardProtectionTooltip;
import gregtech.api.interfaces.IBlockOnWalkOver;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.net.GTPacketClientPreference;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.util.ColorsMetadataSection;
import gregtech.api.util.ColorsMetadataSectionSerializer;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTMusicSystem;
import gregtech.api.util.GTPlayedSound;
import gregtech.api.util.GTUtility;
import gregtech.client.GTMouseEventHandler;
import gregtech.client.SeekingOggCodec;
import gregtech.client.capes.GTCapesLoader;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.config.Client;
import gregtech.common.handlers.SprayColorInfiniteKeybindHandler;
import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionRenderer;
import gregtech.common.render.BlackholeRenderer;
import gregtech.common.render.DroneRender;
import gregtech.common.render.FlaskRenderer;
import gregtech.common.render.FluidDisplayStackRenderer;
import gregtech.common.render.GTRendererBlock;
import gregtech.common.render.GTRendererCasing;
import gregtech.common.render.LaserRenderer;
import gregtech.common.render.MetaGeneratedToolRenderer;
import gregtech.common.render.WormholeRenderer;
import gregtech.common.render.items.DataStickRenderer;
import gregtech.common.render.items.InfiniteSprayCanRenderer;
import gregtech.common.render.items.MetaGeneratedItemRenderer;
import gregtech.common.tileentities.debug.MTEAdvDebugStructureWriter;
import gregtech.loaders.ExtraIcons;
import gregtech.loaders.misc.GTBees;
import gregtech.loaders.preload.GTPreLoad;
import gregtech.nei.NEIGTConfig;
import ic2.api.tile.IWrenchable;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;

// Referenced classes of package gregtech.common:
// GTProxy

public class GTClient extends GTProxy {

    private static final List<Block> ROTATABLE_VANILLA_BLOCKS;

    private static final int[][] GRID_SWITCH_TABLE = new int[][] { { 0, 5, 3, 1, 2, 4 }, { 5, 0, 1, 3, 2, 4 },
        { 1, 3, 0, 5, 2, 4 }, { 3, 1, 5, 0, 2, 4 }, { 4, 2, 3, 1, 0, 5 }, { 2, 4, 3, 1, 5, 0 }, };

    // don't ask. these "just works"
    private static final Transformation ROTATION_MARKER_TRANSFORM_CENTER = new Scale(0.5);
    private static final Transformation[] ROTATION_MARKER_TRANSFORMS_SIDES_TRANSFORMS = {
        new Scale(0.25).with(new Translation(0, 0, 0.375))
            .compile(),
        new Scale(0.25).with(new Translation(0.375, 0, 0))
            .compile(),
        new Scale(0.25).with(new Translation(0, 0, -0.375))
            .compile(),
        new Scale(0.25).with(new Translation(-0.375, 0, 0))
            .compile(), };
    private static final int[] ROTATION_MARKER_TRANSFORMS_SIDES = { -1, -1, 2, 0, 3, 1, -1, -1, 0, 2, 3, 1, 0, 2, -1,
        -1, 3, 1, 2, 0, -1, -1, 3, 1, 1, 3, 2, 0, -1, -1, 3, 1, 2, 0, -1, -1 };
    private static final Transformation[] ROTATION_MARKER_TRANSFORMS_CORNER = {
        new Scale(0.25).with(new Translation(0.375, 0, 0.375))
            .compile(),
        new Scale(0.25).with(new Translation(-0.375, 0, 0.375))
            .compile(),
        new Scale(0.25).with(new Translation(0.375, 0, -0.375))
            .compile(),
        new Scale(0.25).with(new Translation(-0.375, 0, -0.375))
            .compile(), };
    private static int rotationMarkerDisplayList;
    private static boolean rotationMarkerDisplayListCompiled = false;

    static {
        ROTATABLE_VANILLA_BLOCKS = Arrays.asList(
            Blocks.piston,
            Blocks.sticky_piston,
            Blocks.furnace,
            Blocks.lit_furnace,
            Blocks.dropper,
            Blocks.dispenser,
            Blocks.chest,
            Blocks.trapped_chest,
            Blocks.ender_chest,
            Blocks.hopper,
            Blocks.pumpkin,
            Blocks.lit_pumpkin);
    }

    public static final PollutionRenderer mPollutionRenderer = new PollutionRenderer();
    private final List<Materials> mPosR;
    private final List<Materials> mPosG;
    private final List<Materials> mPosB;
    private final List<Materials> mPosA = Collections.emptyList();
    private final List<Materials> mNegR;
    private final List<Materials> mNegG;
    private final List<Materials> mNegB;
    private final List<Materials> mNegA = Collections.emptyList();
    private final List<Materials> mMoltenPosR;
    private final List<Materials> mMoltenPosG;
    private final List<Materials> mMoltenPosB;
    private final List<Materials> mMoltenPosA = Collections.emptyList();
    private final List<Materials> mMoltenNegR;
    private final List<Materials> mMoltenNegG;
    private final List<Materials> mMoltenNegB;
    private final List<Materials> mMoltenNegA = Collections.emptyList();
    private long mAnimationTick;
    /**
     * This is the place to def the value used below
     **/
    private long afterSomeTime;

    private boolean mAnimationDirection;
    private GTClientPreference mPreference;
    private boolean mFirstTick = false;
    public static final int ROTATION_MARKER_RESOLUTION = 120;
    private int mReloadCount;
    private float renderTickTime;
    public static MetaGeneratedItemRenderer metaGeneratedItemRenderer;

    public GTClient() {
        mAnimationTick = 0L;
        mAnimationDirection = false;
        mPosR = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.Thaumium,
            Materials.InfusedVis,
            Materials.InfusedAir,
            Materials.InfusedFire,
            Materials.FierySteel,
            Materials.Firestone);
        mPosG = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.InfusedAir,
            Materials.InfusedEarth);
        mPosB = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.InfusedVis,
            Materials.InfusedWater,
            Materials.Thaumium);
        mNegR = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mNegG = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mNegB = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mMoltenPosR = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.Thaumium,
            Materials.InfusedVis,
            Materials.InfusedAir,
            Materials.InfusedFire,
            Materials.FierySteel,
            Materials.Firestone);
        mMoltenPosG = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.InfusedAir,
            Materials.InfusedEarth);
        mMoltenPosB = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.InfusedVis,
            Materials.InfusedWater,
            Materials.Thaumium);
        mMoltenNegR = Collections.singletonList(Materials.InfusedEntropy);
        mMoltenNegG = Collections.singletonList(Materials.InfusedEntropy);
        mMoltenNegB = Collections.singletonList(Materials.InfusedEntropy);
    }

    private static boolean checkedForChicken = false;

    private static void drawGrid(DrawBlockHighlightEvent aEvent, boolean showCoverConnections, boolean aIsWrench,
        boolean aIsSneaking) {
        if (!checkedForChicken) {
            try {
                Class.forName("codechicken.lib.vec.Rotation");
            } catch (ClassNotFoundException e) {
                return;
            }
            checkedForChicken = true;
        }

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // pause shader
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        GL20.glUseProgram(0);

        MovingObjectPosition target = aEvent.target;
        EntityPlayer player = aEvent.player;
        double camX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) aEvent.partialTicks;
        double camY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) aEvent.partialTicks;
        double camZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) aEvent.partialTicks;
        GL11.glTranslated(target.blockX - (int) camX, target.blockY - (int) camY, target.blockZ - (int) camZ);
        GL11.glTranslated(0.5D - (camX - (int) camX), 0.5D - (camY - (int) camY), 0.5D - (camZ - (int) camZ));
        final int tSideHit = target.sideHit;
        Rotation.sideRotations[tSideHit].glApply();
        // draw grid
        GL11.glTranslated(0.0D, -0.502D, 0.0D);
        GL11.glLineWidth(2.5F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(+.50D, .0D, -.25D);
        GL11.glVertex3d(-.50D, .0D, -.25D);
        GL11.glVertex3d(+.50D, .0D, +.25D);
        GL11.glVertex3d(-.50D, .0D, +.25D);
        GL11.glVertex3d(+.25D, .0D, -.50D);
        GL11.glVertex3d(+.25D, .0D, +.50D);
        GL11.glVertex3d(-.25D, .0D, -.50D);
        GL11.glVertex3d(-.25D, .0D, +.50D);
        final TileEntity tTile = player.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
        final Block block = player.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);
        final int meta = player.worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);

        // draw connection indicators
        int tConnections = 0;
        if (tTile instanceof ICoverable iCoverable) {
            if (showCoverConnections) {
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    if (iCoverable.hasCoverAtSide(tSide)) tConnections |= tSide.flag;
                }
            } else if (tTile instanceof BaseMetaTileEntity baseMetaTile && baseMetaTile.getAlignment() == null) {
                if (!aIsSneaking) tConnections |= baseMetaTile.getFrontFacing().flag;
                else if (baseMetaTile.getMetaTileEntity() instanceof MTEBasicMachine basicMachine) {
                    tConnections |= basicMachine.mMainFacing.flag;
                }
            } else if (tTile instanceof BaseMetaPipeEntity pipeEntity) tConnections = pipeEntity.mConnections;
        } else if (tTile instanceof IWrenchable wrenchable) {
            tConnections |= ForgeDirection.getOrientation(wrenchable.getFacing()).flag;
        } else if (ROTATABLE_VANILLA_BLOCKS.contains(block)) {
            tConnections |= ForgeDirection.getOrientation(meta).flag;
        } else if (tTile instanceof TileInterface tileInterface) tConnections |= tileInterface.getUp()
            .getOpposite().flag;

        if (tConnections != 0) {
            for (ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                if ((tConnections & tSide.flag) != 0) {
                    switch (GRID_SWITCH_TABLE[target.sideHit][tSide.ordinal()]) {
                        case 0 -> {
                            GL11.glVertex3d(+.25D, .0D, +.25D);
                            GL11.glVertex3d(-.25D, .0D, -.25D);
                            GL11.glVertex3d(-.25D, .0D, +.25D);
                            GL11.glVertex3d(+.25D, .0D, -.25D);
                        }
                        case 1 -> {
                            GL11.glVertex3d(-.25D, .0D, +.50D);
                            GL11.glVertex3d(+.25D, .0D, +.25D);
                            GL11.glVertex3d(-.25D, .0D, +.25D);
                            GL11.glVertex3d(+.25D, .0D, +.50D);
                        }
                        case 2 -> {
                            GL11.glVertex3d(-.50D, .0D, -.25D);
                            GL11.glVertex3d(-.25D, .0D, +.25D);
                            GL11.glVertex3d(-.50D, .0D, +.25D);
                            GL11.glVertex3d(-.25D, .0D, -.25D);
                        }
                        case 3 -> {
                            GL11.glVertex3d(-.25D, .0D, -.50D);
                            GL11.glVertex3d(+.25D, .0D, -.25D);
                            GL11.glVertex3d(-.25D, .0D, -.25D);
                            GL11.glVertex3d(+.25D, .0D, -.50D);
                        }
                        case 4 -> {
                            GL11.glVertex3d(+.50D, .0D, -.25D);
                            GL11.glVertex3d(+.25D, .0D, +.25D);
                            GL11.glVertex3d(+.50D, .0D, +.25D);
                            GL11.glVertex3d(+.25D, .0D, -.25D);
                        }
                        case 5 -> {
                            GL11.glVertex3d(+.50D, .0D, +.50D);
                            GL11.glVertex3d(+.25D, .0D, +.25D);
                            GL11.glVertex3d(+.50D, .0D, +.25D);
                            GL11.glVertex3d(+.25D, .0D, +.50D);
                            GL11.glVertex3d(+.50D, .0D, -.50D);
                            GL11.glVertex3d(+.25D, .0D, -.25D);
                            GL11.glVertex3d(+.50D, .0D, -.25D);
                            GL11.glVertex3d(+.25D, .0D, -.50D);
                            GL11.glVertex3d(-.50D, .0D, +.50D);
                            GL11.glVertex3d(-.25D, .0D, +.25D);
                            GL11.glVertex3d(-.50D, .0D, +.25D);
                            GL11.glVertex3d(-.25D, .0D, +.50D);
                            GL11.glVertex3d(-.50D, .0D, -.50D);
                            GL11.glVertex3d(-.25D, .0D, -.25D);
                            GL11.glVertex3d(-.50D, .0D, -.25D);
                            GL11.glVertex3d(-.25D, .0D, -.50D);
                        }
                    }
                }
            }
        }
        GL11.glEnd();
        // draw turning indicator
        Function<ForgeDirection, Transformation[]> getTransform = (ForgeDirection direction) -> {
            try {
                if (direction.ordinal() == tSideHit) return new Transformation[] { ROTATION_MARKER_TRANSFORM_CENTER };
                else if (direction.getOpposite()
                    .ordinal() == tSideHit) {
                        return ROTATION_MARKER_TRANSFORMS_CORNER;
                    } else {
                        return new Transformation[] {
                            ROTATION_MARKER_TRANSFORMS_SIDES_TRANSFORMS[ROTATION_MARKER_TRANSFORMS_SIDES[tSideHit * 6
                                + direction.ordinal()]] };
                    }
            } catch (ArrayIndexOutOfBoundsException e) {
                return new Transformation[] {};
            }

        };

        if (aIsWrench && tTile instanceof IAlignmentProvider) {
            final IAlignment tAlignment = ((IAlignmentProvider) (tTile)).getAlignment();
            if (tAlignment != null) {
                for (var transform : getTransform.apply(tAlignment.getDirection())) {
                    drawExtendedRotationMarker(transform, aIsSneaking, tAlignment);
                }
            }
        }
        if (aIsWrench && tTile instanceof IOrientable orientable
            && !(tTile instanceof TileInterface)
            && orientable.canBeRotated()) {
            for (var transform : getTransform.apply(aIsSneaking ? orientable.getForward() : orientable.getUp())) {
                drawExtendedRotationMarker(transform, aIsSneaking, orientable);
            }
        }
        GL20.glUseProgram(program); // resume shader
        GL11.glPopMatrix(); // get back to player center
    }

    private static void drawExtendedRotationMarker(Transformation transform, boolean sneaking, IAlignment alignment) {
        if (sneaking) {
            if (alignment.isFlipChangeAllowed()) {
                drawFlipMarker(transform);
            }
        } else {
            if (alignment.isRotationChangeAllowed()) {
                drawRotationMarker(transform);
            }
        }
    }

    private static void drawExtendedRotationMarker(Transformation transform, boolean sneaking, IOrientable orientable) {
        drawRotationMarker(transform);
    }

    private static void drawRotationMarker(Transformation transform) {
        if (!rotationMarkerDisplayListCompiled) {
            rotationMarkerDisplayList = GLAllocation.generateDisplayLists(1);
            compileRotationMarkerDisplayList(rotationMarkerDisplayList);
            rotationMarkerDisplayListCompiled = true;
        }
        GL11.glPushMatrix();
        transform.glApply();
        GL11.glCallList(rotationMarkerDisplayList);
        GL11.glPopMatrix();
    }

    private static void compileRotationMarkerDisplayList(int displayList) {
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        GL11.glBegin(GL_LINE_LOOP);
        for (int i = 0; i <= ROTATION_MARKER_RESOLUTION; i++) {
            GL11.glVertex3d(
                Math.cos(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.4,
                0,
                Math.sin(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.4);
        }
        for (int i = ROTATION_MARKER_RESOLUTION; i >= 0; i--) {
            GL11.glVertex3d(
                Math.cos(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.24,
                0,
                Math.sin(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.24);
        }
        GL11.glVertex3d(0.141114561800, 0, 0);
        GL11.glVertex3d(0.32, 0, -0.178885438199);
        GL11.glVertex3d(0.498885438199, 0, 0);
        GL11.glEnd();
        GL11.glEndList();
    }

    private static void drawFlipMarker(Transformation transform) {
        GL11.glPushMatrix();
        transform.glApply();
        final Tessellator t = Tessellator.instance;
        // right shape
        GL11.glLineStipple(4, (short) 0xAAAA);
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        t.startDrawing(GL11.GL_LINE_STRIP);
        t.addVertex(0.1d, 0d, 0.04d);
        t.addVertex(0.1d, 0d, 0.2d);
        t.addVertex(0.35d, 0d, 0.35d);
        t.addVertex(0.35d, 0d, -0.35d);
        t.addVertex(0.1d, 0d, -0.2d);
        t.addVertex(0.1d, 0d, -0.04d);
        t.draw();
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
        // left shape
        t.startDrawing(GL11.GL_LINE_STRIP);
        t.addVertex(-0.1d, 0d, 0.04d);
        t.addVertex(-0.1d, 0d, 0.2d);
        t.addVertex(-0.35d, 0d, 0.35d);
        t.addVertex(-0.35d, 0d, -0.35d);
        t.addVertex(-0.1d, 0d, -0.2d);
        t.addVertex(-0.1d, 0d, -0.04d);
        t.draw();
        // arrow
        t.startDrawing(GL11.GL_LINE_LOOP);
        t.addVertex(0.15d, 0d, -0.04d);
        t.addVertex(0.15d, 0d, -0.1d);
        t.addVertex(0.25d, 0d, 0.d);
        t.addVertex(0.15d, 0d, 0.1d);
        t.addVertex(0.15d, 0d, 0.04d);
        t.addVertex(-0.15d, 0d, 0.04d);
        t.addVertex(-0.15d, 0d, 0.1d);
        t.addVertex(-0.25d, 0d, 0.d);
        t.addVertex(-0.15d, 0d, -0.1d);
        t.addVertex(-0.15d, 0d, -0.04d);
        t.draw();
        GL11.glPopMatrix();
    }

    @Override
    public boolean isClientSide() {
        return true;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void onPreLoad() {
        super.onPreLoad();

        SoundSystemConfig.setNumberNormalChannels(Client.preference.maxNumSounds);

        MinecraftForge.EVENT_BUS.register(new ExtraIcons());
        Minecraft.getMinecraft()
            .getResourcePackRepository().rprMetadataSerializer
                .registerMetadataSectionType(new ColorsMetadataSectionSerializer(), ColorsMetadataSection.class);

        new MTEAdvDebugStructureWriter.ForgeEventHandler();

        new Thread(new GTCapesLoader(), "GT Cape Loader").start();

        mPollutionRenderer.preLoad();

        mPreference = new GTClientPreference();

        Materials.initClient();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        GTRendererBlock.register();
        GTRendererCasing.register();
        new DroneRender();
        new LaserRenderer();
        new WormholeRenderer();
        new BlackholeRenderer();

        metaGeneratedItemRenderer = new MetaGeneratedItemRenderer();
        for (MetaGeneratedItem item : MetaGeneratedItem.sInstances.values()) {
            metaGeneratedItemRenderer.registerItem(item);
        }
        if (Forestry.isModLoaded()) {
            metaGeneratedItemRenderer.registerItem(GTBees.combs);
        }
        new MetaGeneratedToolRenderer();
        new FlaskRenderer();
        new FluidDisplayStackRenderer();
        new DataStickRenderer();
        new InfiniteSprayCanRenderer();
        MinecraftForge.EVENT_BUS.register(new NEIGTConfig());
        MinecraftForge.EVENT_BUS.register(new GTMouseEventHandler());
        SprayColorInfiniteKeybindHandler.init();
    }

    @Override
    public void onPostLoad() {
        super.onPostLoad();

        // reobf doesn't work with lambda, so this must be a class
        // noinspection Convert2Lambda
        ((IReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {

                @Override
                public void onResourceManagerReload(IResourceManager l) {
                    GUIColorOverride.onResourceManagerReload();
                    FallbackableSteamTexture.reload();
                    CoverRegistry.reloadCoverColorOverrides();
                }
            });
        Pollution.onPostInitClient();
    }

    @Override
    public void onLoadComplete() {
        super.onLoadComplete();
        for (RecipeCategory category : RecipeCategory.ALL_RECIPE_CATEGORIES.values()) {
            if (category.recipeMap.getFrontend()
                .getNEIProperties().registerNEI) {
                FluidRecipe.addRecipeMap(
                    category.unlocalizedName,
                    new GregTech5RecipeExtractor(
                        category.unlocalizedName.equals("gt.recipe.scanner")
                            || category.unlocalizedName.equals("gt.recipe.fakeAssemblylineProcess")));
            }
        }
    }

    @Override
    @SubscribeEvent
    public void applyBlockWalkOverEffects(LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        // the player should handle its own movement, rest is handled by the server
        if (entity instanceof EntityClientPlayerMP && entity.onGround) {
            int tX = MathHelper.floor_double(entity.posX),
                tY = MathHelper.floor_double(entity.boundingBox.minY - 0.001F),
                tZ = MathHelper.floor_double(entity.posZ);
            Block tBlock = entity.worldObj.getBlock(tX, tY, tZ);
            if (tBlock instanceof IBlockOnWalkOver)
                ((IBlockOnWalkOver) tBlock).onWalkOver(entity, entity.worldObj, tX, tY, tZ);
        } else {
            super.applyBlockWalkOverEffects(event);
        }
    }

    @SubscribeEvent
    public void onSoundSetup(SoundSetupEvent event) {
        try {
            SoundSystemConfig.setCodec(SeekingOggCodec.EXTENSION, SeekingOggCodec.class);
        } catch (SoundSystemException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent aEvent) {
        mFirstTick = true;
        mReloadCount++;
        GTMusicSystem.ClientSystem.reset();
    }

    @Override
    public void reloadNEICache() {
        mReloadCount++;
    }

    @Override
    public int getNEIReloadCount() {
        return mReloadCount;
    }

    @SubscribeEvent
    public void onPlayerTickEventClient(TickEvent.PlayerTickEvent aEvent) {
        if ((aEvent.side.isClient()) && (aEvent.phase == TickEvent.Phase.END) && (!aEvent.player.isDead)) {
            if (mFirstTick) {
                mFirstTick = false;
                GTValues.NW.sendToServer(new GTPacketClientPreference(mPreference));

                if (!Minecraft.getMinecraft()
                    .isSingleplayer()) {
                    GTModHandler.removeAllIC2Recipes();
                }
            }
            afterSomeTime++;
            if (afterSomeTime >= 100L) {
                afterSomeTime = 0;
            }
            for (Iterator<Map.Entry<GTPlayedSound, Integer>> iterator = GTUtility.sPlayedSoundMap.entrySet()
                .iterator(); iterator.hasNext();) {
                Map.Entry<GTPlayedSound, Integer> tEntry = iterator.next();
                if (tEntry.getValue() < 0) {
                    iterator.remove();
                } else {
                    tEntry.setValue(tEntry.getValue() - 1);
                }
            }
            if (!GregTechAPI.mServerStarted) GregTechAPI.mServerStarted = true;
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
        if (GregTech.ID.equals(e.modID)) {
            // refresh client preference and send to server, since it's the only config we allow changing at runtime.
            mPreference = new GTClientPreference();
            GTPreLoad.loadClientConfig();
            if (e.isWorldRunning) GTValues.NW.sendToServer(new GTPacketClientPreference(mPreference));
        }
    }

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent aEvent) {
        final Block aBlock = aEvent.player.worldObj
            .getBlock(aEvent.target.blockX, aEvent.target.blockY, aEvent.target.blockZ);
        final TileEntity aTileEntity = aEvent.player.worldObj
            .getTileEntity(aEvent.target.blockX, aEvent.target.blockY, aEvent.target.blockZ);

        if (GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sWrenchList)) {
            if (aTileEntity instanceof ITurnable || ROTATABLE_VANILLA_BLOCKS.contains(aBlock)
                || aTileEntity instanceof IWrenchable
                || (aTileEntity instanceof IOrientable orientable && orientable.canBeRotated())
                || (aBlock instanceof BlockFrameBox)) drawGrid(aEvent, false, true, aEvent.player.isSneaking());
            return;
        }

        // If there is no tile entity and the block is a frame box block, still draw the grid if a cover is held
        if (aTileEntity == null && aBlock instanceof BlockFrameBox) {
            if (CoverRegistry.isCover(aEvent.currentItem)) {
                drawGrid(aEvent, true, false, aEvent.player.isSneaking());
            }
            return;
        }

        if (!(aTileEntity instanceof ICoverable)) return;

        if (aEvent.player.isSneaking() && aTileEntity instanceof IGregTechTileEntity gtEntity
            && gtEntity.getMetaTileEntity() instanceof MetaPipeEntity) {
            if (aEvent.currentItem != null && aEvent.currentItem.getItem() instanceof ItemMachines
                && GregTechAPI.METATILEENTITIES[aEvent.currentItem.getItemDamage()] instanceof MetaPipeEntity) {
                drawGrid(aEvent, false, false, false);
            }
        }

        if (GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sWireCutterList)
            || GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sSolderingToolList)
                && aEvent.player.isSneaking()) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)))
                drawGrid(aEvent, false, false, aEvent.player.isSneaking());
            return;
        }

        if ((aEvent.currentItem == null && aEvent.player.isSneaking())
            || GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sCrowbarList)
            || GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sScrewdriverList)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)))
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    if (((ICoverable) aTileEntity).hasCoverAtSide(tSide)) {
                        drawGrid(aEvent, true, false, true);
                        return;
                    }
                }
            return;
        }

        if (CoverRegistry.isCover(aEvent.currentItem)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)))
                drawGrid(aEvent, true, false, aEvent.player.isSneaking());
        }

        if (GTUtility.areStacksEqual(ItemList.Tool_Cover_Copy_Paste.get(1), aEvent.currentItem, true)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)))
                drawGrid(aEvent, true, false, aEvent.player.isSneaking());
        }
    }

    @SubscribeEvent
    public void receiveRenderEvent(net.minecraftforge.client.event.RenderPlayerEvent.Pre aEvent) {
        if (GTUtility.getFullInvisibility(aEvent.entityPlayer)) {
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderStart(TickEvent.RenderTickEvent aEvent) {
        if (aEvent.phase == TickEvent.Phase.START) {
            renderTickTime = aEvent.renderTickTime;
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(ClientTickEvent aEvent) {
        if (aEvent.phase == TickEvent.Phase.END) {
            GTMusicSystem.ClientSystem.tick();

            if (changeDetected > 0) changeDetected--;
            final boolean newHideValue = shouldHeldItemHideThings();
            if (newHideValue != hideThings) {
                hideThings = newHideValue;
                changeDetected = 5;
            }
            forceFullBlockBoundingBoxes = shouldHeldItemForceFullBlockBB();
            mAnimationTick++;
            if (mAnimationTick % 50L == 0L) {
                mAnimationDirection = !mAnimationDirection;
            }
            final int tDirection = mAnimationDirection ? 1 : -1;
            for (Materials tMaterial : mPosR) {
                tMaterial.mRGBa[0] = getSafeRGBValue(tMaterial.mRGBa[0], tDirection);
            }

            for (Materials tMaterial : mPosG) {
                tMaterial.mRGBa[1] = getSafeRGBValue(tMaterial.mRGBa[1], tDirection);
            }

            for (Materials tMaterial : mPosB) {
                tMaterial.mRGBa[2] = getSafeRGBValue(tMaterial.mRGBa[2], tDirection);
            }

            for (Materials tMaterial : mPosA) {
                tMaterial.mRGBa[3] = getSafeRGBValue(tMaterial.mRGBa[3], tDirection);
            }

            for (Materials tMaterial : mNegR) {
                tMaterial.mRGBa[0] = getSafeRGBValue(tMaterial.mRGBa[0], -tDirection);
            }

            for (Materials tMaterial : mNegG) {
                tMaterial.mRGBa[1] = getSafeRGBValue(tMaterial.mRGBa[1], -tDirection);
            }

            for (Materials tMaterial : mNegB) {
                tMaterial.mRGBa[2] = getSafeRGBValue(tMaterial.mRGBa[2], -tDirection);
            }

            for (Materials tMaterial : mNegA) {
                tMaterial.mRGBa[3] = getSafeRGBValue(tMaterial.mRGBa[3], -tDirection);
            }

            for (Materials tMaterial : mMoltenPosR) {
                tMaterial.mMoltenRGBa[0] = getSafeRGBValue(tMaterial.mMoltenRGBa[0], tDirection);
            }

            for (Materials tMaterial : mMoltenPosG) {
                tMaterial.mMoltenRGBa[1] = getSafeRGBValue(tMaterial.mMoltenRGBa[1], tDirection);
            }

            for (Materials tMaterial : mMoltenPosB) {
                tMaterial.mMoltenRGBa[2] = getSafeRGBValue(tMaterial.mMoltenRGBa[2], tDirection);
            }

            for (Materials tMaterial : mMoltenPosA) {
                tMaterial.mMoltenRGBa[3] = getSafeRGBValue(tMaterial.mMoltenRGBa[3], tDirection);
            }

            for (Materials tMaterial : mMoltenNegR) {
                tMaterial.mMoltenRGBa[0] = getSafeRGBValue(tMaterial.mMoltenRGBa[0], -tDirection);
            }

            for (Materials tMaterial : mMoltenNegG) {
                tMaterial.mMoltenRGBa[1] = getSafeRGBValue(tMaterial.mMoltenRGBa[1], -tDirection);
            }

            for (Materials tMaterial : mMoltenNegB) {
                tMaterial.mMoltenRGBa[2] = getSafeRGBValue(tMaterial.mMoltenRGBa[2], -tDirection);
            }

            for (Materials tMaterial : mMoltenNegA) {
                tMaterial.mMoltenRGBa[3] = getSafeRGBValue(tMaterial.mMoltenRGBa[3], -tDirection);
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (HazardProtection.providesFullHazmatProtection(event.itemStack)) {
            addHazmatTooltip(event, HazardProtectionTooltip.FULL_PROTECTION_TRANSLATION_KEY);
            return;
        }

        // TreeSet so it's always the same order
        TreeSet<Hazard> protections = new TreeSet<>();
        for (Hazard hazard : Hazard.values()) {
            if (HazardProtection.protectsAgainstHazard(event.itemStack, hazard)) {
                protections.add(hazard);
            }
        }
        if (protections.containsAll(HazardProtectionTooltip.CBRN_HAZARDS)) {
            protections.removeAll(HazardProtectionTooltip.CBRN_HAZARDS);
            addHazmatTooltip(event, HazardProtectionTooltip.CBRN_TRANSLATION_KEY);
        }

        if (protections.containsAll(HazardProtectionTooltip.TEMPERATURE_HAZARDS)) {
            protections.removeAll(HazardProtectionTooltip.TEMPERATURE_HAZARDS);
            addHazmatTooltip(event, HazardProtectionTooltip.EXTREME_TEMP_TRANSLATION_KEY);
        }
        for (Hazard hazard : protections) {
            addHazmatTooltip(event, HazardProtectionTooltip.singleHazardTranslationKey(hazard));
        }
    }

    private void addHazmatTooltip(ItemTooltipEvent event, String translationKey) {
        event.toolTip.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal(translationKey));
    }

    private int mTicksUntilNextCraftSound = 0;

    // Used for tool sounds in the crafting grid
    @SubscribeEvent
    public void onPlayerCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (this.mTicksUntilNextCraftSound > 0) return;
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = event.craftMatrix.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof MetaGeneratedTool mgt) {
                IToolStats tStats = mgt.getToolStats(stack);
                boolean playBreak = (MetaGeneratedTool.getToolDamage(stack) + tStats.getToolDamagePerContainerCraft())
                    >= MetaGeneratedTool.getToolMaxDamage(stack);
                String sound = playBreak ? tStats.getBreakingSound() : tStats.getCraftingSound();
                GTUtility.doSoundAtClient(sound, 1, 1.0F);
                this.mTicksUntilNextCraftSound = 10;
                return;
            }
        }
    }

    public short getSafeRGBValue(short aRBG, int aDelta) {
        int tmp = aRBG + aDelta;
        if (tmp > 255) tmp = 255;
        if (tmp < 0) tmp = 0;
        return (short) tmp;
    }

    public long getAnimationTicks() {
        return mAnimationTick;
    }

    public float getPartialRenderTicks() {
        return renderTickTime;
    }

    private boolean hideThings = false;

    public boolean shouldHideThings() {
        return hideThings;
    }

    /**
     * <p>
     * Client tick counter that is set to 5 on hiding pipes and covers.
     * </p>
     * <p>
     * It triggers a texture update next client tick when reaching 4, with provision for 3 more update tasks, spreading
     * client change detection related work and network traffic on different ticks, until it reaches 0.
     * </p>
     */
    private int changeDetected = 0;

    public int changeDetected() {
        return changeDetected;
    }

    private static boolean shouldHeldItemHideThings() {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return false;
        final ItemStack tCurrentItem = player.getCurrentEquippedItem();
        if (tCurrentItem == null) return false;
        final int[] ids = OreDictionary.getOreIDs(tCurrentItem);
        for (int i : ids) {
            String oreName = OreDictionary.getOreName(i);
            if (oreName != null && oreName.equals("craftingToolSolderingIron")) {
                return true;
            }
        }
        return false;
    }

    private boolean forceFullBlockBoundingBoxes;

    public boolean forceFullBlockBB() {
        return forceFullBlockBoundingBoxes;
    }

    private static boolean shouldHeldItemForceFullBlockBB() {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return false;
        final ItemStack tCurrentItem = player.getCurrentEquippedItem();
        if (tCurrentItem == null) return false;
        return GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWrenchList)
            || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sHardHammerList)
            || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSoftMalletList)
            || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWireCutterList)
            || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSolderingToolList)
            || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sCrowbarList)
            || CoverRegistry.isCover(tCurrentItem)
            || (tCurrentItem.getItem() instanceof ItemMachines
                && GregTechAPI.METATILEENTITIES[tCurrentItem.getItemDamage()] instanceof MetaPipeEntity
                && player.isSneaking());
    }

    public static void recieveChunkPollutionPacket(ChunkCoordIntPair chunk, int pollution) {
        mPollutionRenderer.processPacket(chunk, pollution);
    }
}
