package gregtech.common.tileentities.debug;

import static gregtech.GTMod.GT_FML_LOGGER;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.singleblock.MTEDebugStructureWriterGui;

public class MTEDebugStructureWriter extends MTETieredMachineBlock {

    private static final HashMap<MTEDebugStructureWriter, BoundHighlighter> bondingBoxes = new HashMap<>(1);
    private final BoundHighlighter boundingBox = new BoundHighlighter();
    private final short[] numbers = new short[6];
    private boolean transpose = false;
    private boolean showHighlightBox = true;
    private String[] result = new String[] { translateToLocal("GT5U.infodata.undefined") };

    public MTEDebugStructureWriter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
    }

    public MTEDebugStructureWriter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public short getNumber(int index) {
        return numbers[index];
    }

    public void setNumber(int index, short number) {
        numbers[index] = number;
    }

    public boolean getTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    public boolean getShowHighlightBox() {
        return showHighlightBox;
    }

    public void setShowHighlightBox(boolean showHighlightBox) {
        this.showHighlightBox = showHighlightBox;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugStructureWriter(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1], sideDirection != facingDirection
                ? TextureFactory.of(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE)
                        .glow()
                        .build())
                : TextureFactory.of(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.STRUCTURE_MARK)
                        .glow()
                        .build()) };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection b,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection b,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < numbers.length; i++) {
            aNBT.setShort("eData" + i, numbers[i]);
        }
        aNBT.setBoolean("Transpose", transpose);
        aNBT.setBoolean("HighlightBox", showHighlightBox);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = aNBT.getShort("eData" + i);
        }
        transpose = aNBT.getBoolean("Transpose");
        showHighlightBox = aNBT.getBoolean("HighlightBox");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        bondingBoxes.put(this, boundingBox);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            ExtendedFacing writerFacing = ExtendedFacing.of(aBaseMetaTileEntity.getFrontFacing());
            double[] abc = new double[3];
            double[] xyz = new double[3];
            boundingBox.dim = aBaseMetaTileEntity.getWorld().provider.dimensionId;
            boundingBox.showHighlightBox = showHighlightBox;
            abc[0] = numbers[0] - 0.5;
            abc[1] = numbers[1] - 0.5;
            abc[2] = numbers[2] - 0.5;
            writerFacing.getWorldOffset(abc, xyz);
            boundingBox.pos1 = new Vec3Impl(
                aBaseMetaTileEntity.getXCoord() + (int) (xyz[0] + 0.5),
                aBaseMetaTileEntity.getYCoord() + (int) (xyz[1] + 0.5),
                aBaseMetaTileEntity.getZCoord() + (int) (xyz[2] + 0.5));
            abc[0] = numbers[0] + numbers[3] - 0.5;
            abc[1] = numbers[1] + numbers[4] - 0.5;
            abc[2] = numbers[2] + numbers[5] - 0.5;
            writerFacing.getWorldOffset(abc, xyz);
            boundingBox.pos2 = new Vec3Impl(
                aBaseMetaTileEntity.getXCoord() + (int) (xyz[0] + 0.5),
                aBaseMetaTileEntity.getYCoord() + (int) (xyz[1] + 0.5),
                aBaseMetaTileEntity.getZCoord() + (int) (xyz[2] + 0.5));
        }
    }

    @Override
    public void onRemoval() {
        bondingBoxes.remove(this);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();
        printStructure(aPlayer);
        aBaseMetaTileEntity.disableWorking();
    }

    public void printStructure(EntityPlayer aPlayer) {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();
        String pseudoJavaCode = StructureUtility.getPseudoJavaCode(
            aBaseMetaTileEntity.getWorld(),
            ExtendedFacing.of(aBaseMetaTileEntity.getFrontFacing()),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord(),
            numbers[0],
            numbers[1],
            numbers[2],
            te -> te.getClass()
                .getCanonicalName(),
            numbers[3],
            numbers[4],
            numbers[5],
            transpose);
        GT_FML_LOGGER.info(pseudoJavaCode);
        result = pseudoJavaCode.split("\\n");
        aPlayer.addChatMessage(
            new ChatComponentTranslation(translateToLocal("GT5U.machines.debugstructurewriter.printed")));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[] { translateToLocal("GT5U.machines.debugstructurewriter.tooltip"), // Scans Blocks Around
            translateToLocal("GT5U.machines.debugstructurewriter.tooltip.1"), // Prints Multiblock NonTE
                                                                              // structure check code
            translateToLocal("GT5U.machines.debugstructurewriter.tooltip.2") // ABC axes aligned to machine front
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return result;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEDebugStructureWriterGui(this).build(data, syncManager, uiSettings);
    }

    public static class EventHandler {

        @SuppressWarnings("unused")
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent e) {
            for (BoundHighlighter boundingBox : bondingBoxes.values()) {
                boundingBox.renderHighlightedBlock(e);
            }
        }
    }

    private static class BoundHighlighter {

        public Vec3Impl pos1;
        public Vec3Impl pos2;
        public boolean showHighlightBox;
        public int dim;

        @SideOnly(Side.CLIENT)
        private void renderHighlightedBlock(RenderWorldLastEvent event) {
            if (pos1 == null || pos2 == null || !showHighlightBox) {
                return;
            }
            Minecraft mc = Minecraft.getMinecraft();
            int dimension = mc.theWorld.provider.dimensionId;

            if (dimension != dim) {
                pos1 = null;
                pos2 = null;
                return;
            }

            EntityPlayerSP p = mc.thePlayer;
            double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * event.partialTicks;
            double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * event.partialTicks;
            double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * event.partialTicks;

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glLineWidth(3);
            GL11.glTranslated(-doubleX, -doubleY, -doubleZ);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            renderHighLightedArenaOutline(pos1.get0(), pos1.get1(), pos1.get2(), pos2.get0(), pos2.get1(), pos2.get2());

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }

        @SideOnly(Side.CLIENT)
        static void renderHighLightedArenaOutline(double x1, double y1, double z1, double x2, double y2, double z2) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawing(GL11.GL_LINE_STRIP);

            tess.addVertex(x1, y1, z1);
            tess.addVertex(x1, y2, z1);
            tess.addVertex(x1, y2, z2);
            tess.addVertex(x1, y1, z2);
            tess.addVertex(x1, y1, z1);

            tess.addVertex(x2, y1, z1);
            tess.addVertex(x2, y2, z1);
            tess.addVertex(x2, y2, z2);
            tess.addVertex(x2, y1, z2);
            tess.addVertex(x2, y1, z1);

            tess.addVertex(x1, y1, z1);
            tess.addVertex(x2, y1, z1);
            tess.addVertex(x2, y1, z2);
            tess.addVertex(x1, y1, z2);
            tess.addVertex(x1, y2, z2);
            tess.addVertex(x2, y2, z2);
            tess.addVertex(x2, y2, z1);
            tess.addVertex(x2, y1, z1);
            tess.addVertex(x1, y1, z1);
            tess.addVertex(x2, y1, z1);
            tess.addVertex(x2, y2, z1);
            tess.addVertex(x1, y2, z1);
            tess.addVertex(x1, y2, z2);
            tess.addVertex(x2, y2, z2);
            tess.addVertex(x2, y1, z2);
            tess.addVertex(x1, y1, z2);

            tess.draw();
        }
    }
}
