package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialCuttingMachine extends MTEExtendedPowerMultiBlockBase<MTEIndustrialCuttingMachine>
    implements ISurvivalConstructable, IMTERenderer {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final double RENDER_OFFSET_RIGHT = 2.5D;
    private static final double RENDER_OFFSET_UP = 0.0D;
    private static final double RENDER_OFFSET_BACK = 1.0D;
    private static final double BLADE_WIDTH = 2.0D;
    private static final double BLADE_HEIGHT = 1.0D;
    private static final double BLADE_PADDING = 0.25D;
    private static final long BLADE_FRAME_TICKS = 3L;
    private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/model/cutter.png");
    private int casingAmount;
    private int glassTier = -1;

    private static IStructureDefinition<MTEIndustrialCuttingMachine> STRUCTURE_DEFINITION = null;

    public MTEIndustrialCuttingMachine(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCuttingMachine(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCuttingMachine(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Cutting Machine, ICF")
            .addBulkMachineInfo(4, 3f, 0.75f)
            .addInfo("Energy Hatch Tier is limited by Glass Tier")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 12) + EnumChatFormatting.GRAY
                    + "-glass unlocks all above energy tiers")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 4, 3, false)
            .addController("Front left, 2nd layer")
            .addCasingInfoMin("Cutting Factory Frame", 10, false)
            .addCasingInfoExactly("Maraging Steel 300 Frame Box", 20, false)
            .addCasingInfoExactly("Any Tiered Glass", 16, true)
            .addCasingInfoExactly("Blue Steel Sheetmetal", 13, false)
            .addInputBus("Any Cutting Factory Frame", 1)
            .addOutputBus("Any Cutting Factory Frame", 1)
            .addInputHatch("Any Cutting Factory Frame", 1)
            .addEnergyHatch("Any Cutting Factory Frame", 1)
            .addMaintenanceHatch("Any Cutting Factory Frame", 1)
            .addMufflerHatch("Any Cutting Factory Frame", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Auynonymous")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialCuttingMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCuttingMachine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "  DBBBBB ", " DDAAAADB", " D~AAAADB", " DDDDDDDB" },
                        { "BCCDDDDD ", "CD     C ", "CDB BB C ", "CDCCCCCC " },
                        { "  DBBBBB ", " DDAAAADB", " DDAAAADB", " DDDDDDDB" } })
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('B', ofFrame(MaterialsAlloy.MARAGING300))
                .addElement('C', ofSheetMetal(Materials.BlueSteel))
                .addElement(
                    'D',
                    buildHatchAdder(MTEIndustrialCuttingMachine.class)
                        .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy.or(MultiAmpEnergy), Muffler)
                        .casingIndex(Casings.CuttingFactoryFrame.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.CuttingFactoryFrame.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        glassTier = -1;
        if (checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 10
            && !mMufflerHatches.isEmpty()) {
            int inputTier = (int) getInputVoltageTier();
            if (glassTier < VoltageIndex.UMV && inputTier > glassTier) {
                return false;
            }
            if (!mExoticEnergyHatches.isEmpty()) {
                if (!mEnergyHatches.isEmpty()) return false;
                return (mExoticEnergyHatches.size() == 1) && glassTier >= VoltageIndex.UIV;
            }
            return true;
        }
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.CuttingFactoryFrame.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialCuttingMachineActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialCuttingMachineActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.CuttingFactoryFrame.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialCuttingMachine)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialCuttingMachineGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.CuttingFactoryFrame.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cutterRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCuttingMachine;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_CUTTING_MACHINE_LOOP;
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (!base.isActive()) {
            return;
        }

        double[] renderCenter = getRenderCenter();
        int frame = (int) ((base.getTimer() / BLADE_FRAME_TICKS) % 3L);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(BLADE_TEXTURE);

        GL11.glTranslated(x + renderCenter[0], y + renderCenter[1], z + renderCenter[2]);
        if (base.getFrontFacing().offsetX != 0) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        }

        renderBladeFrame(frame);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        double[] renderCenter = getRenderCenter();
        double halfWidth = BLADE_WIDTH / 2.0D + BLADE_PADDING;
        double halfHeight = BLADE_HEIGHT / 2.0D + BLADE_PADDING;
        return AxisAlignedBB.getBoundingBox(
            x + renderCenter[0] - halfWidth,
            y + renderCenter[1] - halfHeight,
            z + renderCenter[2] - halfWidth,
            x + renderCenter[0] + halfWidth,
            y + renderCenter[1] + halfHeight,
            z + renderCenter[2] + halfWidth);
    }

    private void renderBladeFrame(int frame) {
        double halfWidth = BLADE_WIDTH / 2.0D;
        double halfHeight = BLADE_HEIGHT / 2.0D;
        double minU = 0.5D / 32.0D;
        double maxU = 1.0D - minU;
        double frameHeight = 48.0D / 3.0D;
        double minV = (frame * frameHeight + 0.5D) / 48.0D;
        double maxV = ((frame + 1) * frameHeight - 0.5D) / 48.0D;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(-halfWidth, -halfHeight, 0.0D, minU, maxV);
        tessellator.addVertexWithUV(-halfWidth, halfHeight, 0.0D, minU, minV);
        tessellator.addVertexWithUV(halfWidth, halfHeight, 0.0D, maxU, minV);
        tessellator.addVertexWithUV(halfWidth, -halfHeight, 0.0D, maxU, maxV);
        tessellator.draw();
    }

    private double[] getRenderCenter() {
        int[] horizontal = getWorldOffset(1, 0, 0);
        int[] vertical = getWorldOffset(0, 1, 0);
        int[] depth = getWorldOffset(0, 0, 1);
        return new double[] {
            0.5D + RENDER_OFFSET_RIGHT * horizontal[0] + RENDER_OFFSET_UP * vertical[0] + RENDER_OFFSET_BACK * depth[0],
            0.5D + RENDER_OFFSET_RIGHT * horizontal[1] + RENDER_OFFSET_UP * vertical[1] + RENDER_OFFSET_BACK * depth[1],
            0.5D + RENDER_OFFSET_RIGHT * horizontal[2]
                + RENDER_OFFSET_UP * vertical[2]
                + RENDER_OFFSET_BACK * depth[2] };
    }

    private int[] getWorldOffset(int localX, int localY, int localZ) {
        int[] xyz = new int[3];
        getExtendedFacing().getWorldOffset(new int[] { localX, localY, localZ }, xyz);
        return xyz;
    }
}
