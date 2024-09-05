// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: GT_Client.java

package gregtech.common;

import static gregtech.api.enums.GTValues.calculateMaxPlasmaTurbineEfficiency;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GregTech;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
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
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import gregtech.api.net.GTPacketClientPreference;
import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.util.ColorsMetadataSection;
import gregtech.api.util.ColorsMetadataSectionSerializer;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTMusicSystem;
import gregtech.api.util.GTPlayedSound;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.client.SeekingOggCodec;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.render.DroneRender;
import gregtech.common.render.FlaskRenderer;
import gregtech.common.render.FluidDisplayStackRenderer;
import gregtech.common.render.GTCapeRenderer;
import gregtech.common.render.GTRendererBlock;
import gregtech.common.render.LaserRenderer;
import gregtech.common.render.MetaGeneratedToolRenderer;
import gregtech.common.render.MultiTileRenderer;
import gregtech.common.render.PollutionRenderer;
import gregtech.common.render.WormholeRenderer;
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

public class GTClient extends GTProxy implements Runnable {

    public static final String GTNH_CAPE_LIST_URL = "https://raw.githubusercontent.com/GTNewHorizons/CustomGTCapeHook-Cape-List/master/capes.txt";
    public static final String GT_CAPE_LIST_URL = "http://gregtech.overminddl1.com/com/gregoriust/gregtech/supporterlist.txt";
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

    private final HashSet<String> mCapeList = new HashSet<>();
    public static final PollutionRenderer mPollutionRenderer = new PollutionRenderer();
    private final GTCapeRenderer mCapeRenderer;
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
        mCapeRenderer = new GTCapeRenderer(mCapeList);
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
                    if (iCoverable.getCoverIDAtSide(tSide) != 0) tConnections |= tSide.flag;
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
    public boolean isServerSide() {
        return true;
    }

    @Override
    public boolean isClientSide() {
        return true;
    }

    @Override
    public boolean isBukkitSide() {
        return false;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public int addArmor(String aPrefix) {
        return RenderingRegistry.addNewArmourRendererPrefix(aPrefix);
    }

    @Override
    public void onPreLoad() {
        super.onPreLoad();

        MinecraftForge.EVENT_BUS.register(new ExtraIcons());
        Minecraft.getMinecraft()
            .getResourcePackRepository().rprMetadataSerializer
                .registerMetadataSectionType(new ColorsMetadataSectionSerializer(), ColorsMetadataSection.class);

        new MTEAdvDebugStructureWriter.ForgeEventHandler();

        final String[] arr = { "renadi", "hanakocz", "MysteryDump", "Flaver4", "x_Fame", "Peluche321",
            "Goshen_Ithilien", "manf", "Bimgo", "leagris", "IAmMinecrafter02", "Cerous", "Devilin_Pixy", "Bkarlsson87",
            "BadAlchemy", "CaballoCraft", "melanclock", "Resursator", "demanzke", "AndrewAmmerlaan", "Deathlycraft",
            "Jirajha", "Axlegear", "kei_kouma", "Dracion", "dungi", "Dorfschwein", "Zero Tw0", "mattiagraz85",
            "sebastiank30", "Plem", "invultri", "grillo126", "malcanteth", "Malevolence_", "Nicholas_Manuel", "Sirbab",
            "kehaan", "bpgames123", "semig0d", "9000bowser", "Sovereignty89", "Kris1432", "xander_cage_", "samuraijp",
            "bsaa", "SpwnX", "tworf", "Kadah", "kanni", "Stute", "Hegik", "Onlyme", "t3hero", "Hotchi", "jagoly",
            "Nullav", "BH5432", "Sibmer", "inceee", "foxxx0", "Hartok", "TMSama", "Shlnen", "Carsso", "zessirb",
            "meep310", "Seldron", "yttr1um", "hohounk", "freebug", "Sylphio", "jmarler", "Saberawr", "r00teniy",
            "Neonbeta", "yinscape", "voooon24", "Quintine", "peach774", "lepthymo", "bildeman", "Kremnari", "Aerosalo",
            "OndraSter", "oscares91", "mr10movie", "Daxx367x2", "EGERTRONx", "aka13_404", "Abouttabs", "Johnstaal",
            "djshiny99", "megatronp", "DZCreeper", "Kane_Hart", "Truculent", "vidplace7", "simon6689", "MomoNasty",
            "UnknownXLV", "goreacraft", "Fluttermine", "Daddy_Cecil", "MrMaleficus", "TigersFangs", "cublikefoot",
            "chainman564", "NikitaBuker", "Misha999777", "25FiveDetail", "AntiCivilBoy", "michaelbrady",
            "xXxIceFirexXx", "Speedynutty68", "GarretSidzaka", "HallowCharm977", "mastermind1919", "The_Hypersonic",
            "diamondguy2798", "zF4ll3nPr3d4t0r", "CrafterOfMines57", "XxELIT3xSNIP3RxX", "SuterusuKusanagi",
            "xavier0014", "adamros", "alexbegt" };
        for (String tName : arr) {
            mCapeList.add(tName.toLowerCase());
        }
        new Thread(this).start();

        mPollutionRenderer.preLoad();

        mPreference = new GTClientPreference();

        Materials.initClient();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        GTRendererBlock.register();
        new MultiTileRenderer();
        new DroneRender();
        new LaserRenderer();
        new WormholeRenderer();

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
        MinecraftForge.EVENT_BUS.register(new NEIGTConfig());
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
                    GregTechAPI.sCoverBehaviors.values()
                        .forEach(CoverBehaviorBase::reloadColorOverride);
                }
            });
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

    @SubscribeEvent
    @SuppressWarnings("unused") // used by the event bus
    public void onSoundSetup(SoundSetupEvent event) {
        try {
            SoundSystemConfig.setCodec(SeekingOggCodec.EXTENSION, SeekingOggCodec.class);
        } catch (SoundSystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        GTLog.out.println("GTMod: Downloading Cape List.");
        try (final Scanner tScanner = new Scanner(new URL(GT_CAPE_LIST_URL).openStream())) {
            while (tScanner.hasNextLine()) {
                this.mCapeList.add(
                    tScanner.nextLine()
                        .toLowerCase());
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
        GTLog.out.println("GT New Horizons: Downloading Cape List.");
        try (final Scanner tScanner = new Scanner(new URL(GTNH_CAPE_LIST_URL).openStream())) {
            while (tScanner.hasNextLine()) {
                final String tName = tScanner.nextLine()
                    .toLowerCase();
                if (tName.contains(":")) {
                    if (!this.mCapeList.contains(tName.substring(0, tName.indexOf(":")))) {
                        this.mCapeList.add(tName);
                    }
                } else {
                    this.mCapeList.add(tName);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
    }

    @Override
    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent aEvent) {
        mFirstTick = true;
        mReloadCount++;
        GTMusicSystem.ClientSystem.reset();
        // For utility methods elsewhere.
        calculateMaxPlasmaTurbineEfficiency();
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
    public void receiveRenderSpecialsEvent(net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre aEvent) {
        mCapeRenderer.receiveRenderSpecialsEvent(aEvent);
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
        if (GregTech.ID.equals(e.modID) && "kekztech/client".equals(e.configID)) {
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
            if (GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sCovers.keySet())) {
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
            || (GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sSoftHammerList)
                && aTileEntity instanceof MultiBlockPart) && aEvent.player.isSneaking()) {
            if (((ICoverable) aTileEntity).getCoverIDAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)) == 0)
                drawGrid(aEvent, false, false, aEvent.player.isSneaking());
            return;
        }

        if ((aEvent.currentItem == null && aEvent.player.isSneaking())
            || GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sCrowbarList)
            || GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sScrewdriverList)) {
            if (((ICoverable) aTileEntity).getCoverIDAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)) == 0)
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    if (((ICoverable) aTileEntity).getCoverIDAtSide(tSide) > 0) {
                        drawGrid(aEvent, true, false, true);
                        return;
                    }
                }
            return;
        }

        if (GTUtility.isStackInList(aEvent.currentItem, GregTechAPI.sCovers.keySet())) {
            if (((ICoverable) aTileEntity).getCoverIDAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)) == 0)
                drawGrid(aEvent, true, false, aEvent.player.isSneaking());
        }

        if (GTUtility.areStacksEqual(ItemList.Tool_Cover_Copy_Paste.get(1), aEvent.currentItem, true)) {
            if (((ICoverable) aTileEntity).getCoverIDAtSide(ForgeDirection.getOrientation(aEvent.target.sideHit)) == 0)
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
    public void onRenderStart(cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent aEvent) {
        if (aEvent.phase == TickEvent.Phase.START) {
            renderTickTime = aEvent.renderTickTime;
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent aEvent) {
        if (aEvent.phase == cpw.mods.fml.common.gameevent.TickEvent.Phase.END) {
            GTMusicSystem.ClientSystem.tick();

            if (changeDetected > 0) changeDetected--;
            final int newHideValue = shouldHeldItemHideThings();
            if (newHideValue != hideValue) {
                hideValue = newHideValue;
                changeDetected = 5;
            }
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

    public short getSafeRGBValue(short aRBG, int aDelta) {
        int tmp = aRBG + aDelta;
        if (tmp > 255) tmp = 255;
        if (tmp < 0) tmp = 0;
        return (short) tmp;
    }

    @Override
    public long getAnimationTicks() {
        return mAnimationTick;
    }

    @Override
    public float getPartialRenderTicks() {
        return renderTickTime;
    }

    @Override
    public void doSonictronSound(ItemStack aStack, World aWorld, double aX, double aY, double aZ) {
        if (GTUtility.isStackInvalid(aStack)) return;
        String tString = SoundResource.NOTE_HARP.toString();
        int i = 0;
        int j = mSoundItems.size();
        do {
            if (i >= j) break;
            if (GTUtility.areStacksEqual(mSoundItems.get(i), aStack)) {
                tString = mSoundNames.get(i);
                break;
            }
            i++;
        } while (true);
        if (tString.startsWith(SoundResource.RANDOM_EXPLODE.toString()))
            if (aStack.stackSize == 3) tString = SoundResource.RANDOM_FUSE.toString();
            else if (aStack.stackSize == 2) tString = "random.old_explode";
        if (tString.startsWith("streaming.")) tString = switch (aStack.stackSize) {
            case 1 -> // '\001'
                tString + "13";
            case 2 -> // '\002'
                tString + "cat";
            case 3 -> // '\003'
                tString + "blocks";
            case 4 -> // '\004'
                tString + "chirp";
            case 5 -> // '\005'
                tString + "far";
            case 6 -> // '\006'
                tString + "mall";
            case 7 -> // '\007'
                tString + "mellohi";
            case 8 -> // '\b'
                tString + "stal";
            case 9 -> // '\t'
                tString + "strad";
            case 10 -> // '\n'
                tString + "ward";
            case 11 -> // '\013'
                tString + "11";
            case 12 -> // '\f'
                tString + "wait";
            default -> tString + "wherearewenow";
        };
        if (tString.startsWith("streaming.")) {
            new WorldSpawnedEventBuilder.RecordEffectEventBuilder().setIdentifier(tString.substring(10))
                .setPosition(aX, aY, aZ)
                .run();
        } else {
            new WorldSpawnedEventBuilder.SoundEventBuilder().setVolume(3f)
                .setPitch(
                    tString.startsWith("note.") ? (float) Math.pow(2D, (double) (aStack.stackSize - 13) / 12D) : 1.0F)
                .setIdentifier(tString)
                .setPosition(aX, aY, aZ)
                .run();
        }
    }

    public static int hideValue = 0;

    /**
     * <p>
     * Client tick counter that is set to 5 on hiding pipes and covers.
     * </p>
     * <p>
     * It triggers a texture update next client tick when reaching 4, with provision for 3 more update tasks, spreading
     * client change detection related work and network traffic on different ticks, until it reaches 0.
     * </p>
     */
    public static int changeDetected = 0;

    private static int shouldHeldItemHideThings() {
        try {
            final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return 0;
            final ItemStack tCurrentItem = player.getCurrentEquippedItem();
            if (tCurrentItem == null) return 0;
            final int[] ids = OreDictionary.getOreIDs(tCurrentItem);
            int hide = 0;
            for (int i : ids) {
                if (OreDictionary.getOreName(i)
                    .equals("craftingToolSolderingIron")) {
                    hide |= 0x1;
                    break;
                }
            }
            if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWrenchList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sScrewdriverList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sHardHammerList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSoftHammerList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWireCutterList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSolderingToolList)
                || GTUtility.isStackInList(tCurrentItem, GregTechAPI.sCrowbarList)
                || GregTechAPI.sCovers.containsKey(new GTItemStack(tCurrentItem))
                || (tCurrentItem.getItem() instanceof ItemMachines
                    && GregTechAPI.METATILEENTITIES[tCurrentItem.getItemDamage()] instanceof MetaPipeEntity
                    && player.isSneaking())) {
                hide |= 0x2;
            }
            return hide;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void recieveChunkPollutionPacket(ChunkCoordIntPair chunk, int pollution) {
        mPollutionRenderer.processPacket(chunk, pollution);
    }
}
