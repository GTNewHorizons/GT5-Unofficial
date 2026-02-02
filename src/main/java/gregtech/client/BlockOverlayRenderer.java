package gregtech.client;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;

import appeng.api.util.IOrientable;
import appeng.tile.misc.TileInterface;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Translation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.metatileentity.implementations.MTEBuffer;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.config.Client;
import ic2.api.tile.IWrenchable;

public class BlockOverlayRenderer {

    private static final List<Block> ROTATABLE_VANILLA_BLOCKS;
    private static final int ROTATION_MARKER_RESOLUTION = 120;

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

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        final Block aBlock = event.player.worldObj
            .getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
        final TileEntity aTileEntity = event.player.worldObj
            .getTileEntity(event.target.blockX, event.target.blockY, event.target.blockZ);

        if (GTUtility.isStackInList(event.currentItem, GregTechAPI.sWrenchList)) {
            if (aTileEntity instanceof ITurnable || ROTATABLE_VANILLA_BLOCKS.contains(aBlock)
                || aTileEntity instanceof IWrenchable
                || (aTileEntity instanceof IOrientable orientable && orientable.canBeRotated())
                || (aBlock instanceof BlockFrameBox)) drawGrid(event, false, true, event.player.isSneaking());
            return;
        }

        // If there is no tile entity and the block is a frame box block, still draw the grid if a cover is held
        if (aTileEntity == null && aBlock instanceof BlockFrameBox) {
            if (CoverRegistry.isCover(event.currentItem)) {
                drawGrid(event, true, false, event.player.isSneaking());
            }
            return;
        }

        if (!(aTileEntity instanceof ICoverable)) return;

        if (event.player.isSneaking() && aTileEntity instanceof IGregTechTileEntity gtEntity
            && gtEntity.getMetaTileEntity() instanceof MetaPipeEntity) {
            if (event.currentItem != null && event.currentItem.getItem() instanceof ItemMachines
                && GregTechAPI.METATILEENTITIES[event.currentItem.getItemDamage()] instanceof MetaPipeEntity) {
                drawGrid(event, false, false, false);
            }
        }

        if (GTUtility.isStackInList(event.currentItem, GregTechAPI.sWireCutterList)
            || GTUtility.isStackInList(event.currentItem, GregTechAPI.sSolderingToolList)
                && event.player.isSneaking()) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(event.target.sideHit)))
                drawGrid(event, false, false, event.player.isSneaking());
            return;
        }

        if (GTUtility.isStackInList(event.currentItem, GregTechAPI.sScrewdriverList)
            && aTileEntity instanceof IGregTechTileEntity gtEntity
            && gtEntity.getMetaTileEntity() instanceof MTEBuffer) {
            drawGrid(event, false, false, event.player.isSneaking());
            return;
        }

        if ((event.currentItem == null && event.player.isSneaking())
            || GTUtility.isStackInList(event.currentItem, GregTechAPI.sCrowbarList)
            || GTUtility.isStackInList(event.currentItem, GregTechAPI.sScrewdriverList)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(event.target.sideHit)))
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    if (((ICoverable) aTileEntity).hasCoverAtSide(tSide)) {
                        drawGrid(event, true, false, true);
                        return;
                    }
                }
            return;
        }

        if (CoverRegistry.isCover(event.currentItem)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(event.target.sideHit)))
                drawGrid(event, true, false, event.player.isSneaking());
        }

        if (GTUtility.areStacksEqual(ItemList.Tool_Cover_Copy_Paste.get(1), event.currentItem, true)) {
            if (!((ICoverable) aTileEntity).hasCoverAtSide(ForgeDirection.getOrientation(event.target.sideHit)))
                drawGrid(event, true, false, event.player.isSneaking());
        }
    }

    private static void drawGrid(DrawBlockHighlightEvent aEvent, boolean showCoverConnections, boolean aIsWrench,
        boolean aIsSneaking) {
        aEvent.setCanceled(true);

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // pause shader
        int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        GL20.glUseProgram(0);
        GL11.glLineWidth(calculateLineWidth());

        MovingObjectPosition target = aEvent.target;
        EntityPlayer player = aEvent.player;
        double camX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) aEvent.partialTicks;
        double camY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) aEvent.partialTicks;
        double camZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) aEvent.partialTicks;
        int red = Client.blockoverlay.red;
        int green = Client.blockoverlay.green;
        int blue = Client.blockoverlay.blue;
        int alpha = Client.blockoverlay.alpha;
        final TileEntity tTile = player.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
        final Block block = player.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);
        final int meta = player.worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);

        // draw block outline
        if (block.getMaterial() != Material.air) {
            final World world = player.worldObj;

            block.setBlockBoundsBasedOnState(world, target.blockX, target.blockY, target.blockZ);

            AxisAlignedBB box = block
                .getSelectedBoundingBoxFromPool(world, target.blockX, target.blockY, target.blockZ);
            box = box.expand(0.002D, 0.002D, 0.002D);
            box = box.getOffsetBoundingBox(-camX, -camY, -camZ);

            Tessellator tess = Tessellator.instance;

            tess.startDrawing(GL11.GL_LINE_STRIP);
            tess.setColorRGBA(red, green, blue, alpha);
            tess.addVertex(box.minX, box.minY, box.minZ);
            tess.addVertex(box.maxX, box.minY, box.minZ);
            tess.addVertex(box.maxX, box.minY, box.maxZ);
            tess.addVertex(box.minX, box.minY, box.maxZ);
            tess.addVertex(box.minX, box.minY, box.minZ);
            tess.draw();

            tess.startDrawing(GL11.GL_LINE_STRIP);
            tess.setColorRGBA(red, green, blue, alpha);
            tess.addVertex(box.minX, box.maxY, box.minZ);
            tess.addVertex(box.maxX, box.maxY, box.minZ);
            tess.addVertex(box.maxX, box.maxY, box.maxZ);
            tess.addVertex(box.minX, box.maxY, box.maxZ);
            tess.addVertex(box.minX, box.maxY, box.minZ);
            tess.draw();

            tess.startDrawing(GL11.GL_LINES);
            tess.setColorRGBA(red, green, blue, alpha);
            tess.addVertex(box.minX, box.minY, box.minZ);
            tess.addVertex(box.minX, box.maxY, box.minZ);
            tess.addVertex(box.maxX, box.minY, box.minZ);
            tess.addVertex(box.maxX, box.maxY, box.minZ);
            tess.addVertex(box.maxX, box.minY, box.maxZ);
            tess.addVertex(box.maxX, box.maxY, box.maxZ);
            tess.addVertex(box.minX, box.minY, box.maxZ);
            tess.addVertex(box.minX, box.maxY, box.maxZ);
            tess.draw();
        }

        GL11.glTranslated(target.blockX - (int) camX, target.blockY - (int) camY, target.blockZ - (int) camZ);
        GL11.glTranslated(0.5D - (camX - (int) camX), 0.5D - (camY - (int) camY), 0.5D - (camZ - (int) camZ));
        final int tSideHit = target.sideHit;
        Rotation.sideRotations[tSideHit].glApply();
        // draw grid
        GL11.glTranslated(0.0D, -0.502D, 0.0D);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL11.GL_LINES);
        tess.setColorRGBA(red, green, blue, alpha);
        tess.addVertex(+.50D, .0D, -.25D);
        tess.addVertex(-.50D, .0D, -.25D);
        tess.addVertex(+.50D, .0D, +.25D);
        tess.addVertex(-.50D, .0D, +.25D);
        tess.addVertex(+.25D, .0D, -.50D);
        tess.addVertex(+.25D, .0D, +.50D);
        tess.addVertex(-.25D, .0D, -.50D);
        tess.addVertex(-.25D, .0D, +.50D);

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
                            tess.addVertex(+.25D, .0D, +.25D);
                            tess.addVertex(-.25D, .0D, -.25D);
                            tess.addVertex(-.25D, .0D, +.25D);
                            tess.addVertex(+.25D, .0D, -.25D);
                        }
                        case 1 -> {
                            tess.addVertex(-.25D, .0D, +.50D);
                            tess.addVertex(+.25D, .0D, +.25D);
                            tess.addVertex(-.25D, .0D, +.25D);
                            tess.addVertex(+.25D, .0D, +.50D);
                        }
                        case 2 -> {
                            tess.addVertex(-.50D, .0D, -.25D);
                            tess.addVertex(-.25D, .0D, +.25D);
                            tess.addVertex(-.50D, .0D, +.25D);
                            tess.addVertex(-.25D, .0D, -.25D);
                        }
                        case 3 -> {
                            tess.addVertex(-.25D, .0D, -.50D);
                            tess.addVertex(+.25D, .0D, -.25D);
                            tess.addVertex(-.25D, .0D, -.25D);
                            tess.addVertex(+.25D, .0D, -.50D);
                        }
                        case 4 -> {
                            tess.addVertex(+.50D, .0D, -.25D);
                            tess.addVertex(+.25D, .0D, +.25D);
                            tess.addVertex(+.50D, .0D, +.25D);
                            tess.addVertex(+.25D, .0D, -.25D);
                        }
                        case 5 -> {
                            tess.addVertex(+.50D, .0D, +.50D);
                            tess.addVertex(+.25D, .0D, +.25D);
                            tess.addVertex(+.50D, .0D, +.25D);
                            tess.addVertex(+.25D, .0D, +.50D);
                            tess.addVertex(+.50D, .0D, -.50D);
                            tess.addVertex(+.25D, .0D, -.25D);
                            tess.addVertex(+.50D, .0D, -.25D);
                            tess.addVertex(+.25D, .0D, -.50D);
                            tess.addVertex(-.50D, .0D, +.50D);
                            tess.addVertex(-.25D, .0D, +.25D);
                            tess.addVertex(-.50D, .0D, +.25D);
                            tess.addVertex(-.25D, .0D, +.50D);
                            tess.addVertex(-.50D, .0D, -.50D);
                            tess.addVertex(-.25D, .0D, -.25D);
                            tess.addVertex(-.50D, .0D, -.25D);
                            tess.addVertex(-.25D, .0D, -.50D);
                        }
                    }
                }
            }
        }
        tess.draw();
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
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
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
        Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL_LINE_LOOP);
        for (int i = 0; i <= ROTATION_MARKER_RESOLUTION; i++) {
            tess.addVertex(
                Math.cos(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.4,
                0,
                Math.sin(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.4);
        }
        for (int i = ROTATION_MARKER_RESOLUTION; i >= 0; i--) {
            tess.addVertex(
                Math.cos(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.24,
                0,
                Math.sin(i * Math.PI * 1.75 / ROTATION_MARKER_RESOLUTION) * 0.24);
        }
        tess.addVertex(0.141114561800, 0, 0);
        tess.addVertex(0.32, 0, -0.178885438199);
        tess.addVertex(0.498885438199, 0, 0);
        tess.draw();
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

    private static float calculateLineWidth() {
        // Assume default resolution has the same height as a standard Full HD monitor
        final float baseHeight = 1080F;

        // Calculate deviation using the actual height of the application window,
        // higher resolutions result in thicker lines,
        // lower resolutions result in thinner lines.
        return Client.blockoverlay.lineWidth * (Minecraft.getMinecraft().displayHeight / baseHeight);
    }
}
