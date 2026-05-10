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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
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
    private static final double RENDER_OFFSET_UP = 0.25D;
    private static final double RENDER_OFFSET_BACK = 1.0D;
    private static final double BLADE_WIDTH = 3.0D;
    private static final double BLADE_HEIGHT = 1.5D;
    private static final double BLADE_PADDING = 0.25D;
    private static final int BLADE_FRAMES = 3;
    private static final long BLADE_FRAME_TICKS = 3L;
    private static final ResourceLocation BLADE_TEXTURE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/model/cutter.png");
    private int casingAmount;
    private boolean stopAllRendering;
    private ExtendedFacing cachedBladeRenderFacing;
    private final BladeRenderContext bladeRenderContext = new BladeRenderContext();

    private static final int[] PARALLEL_PER_VOLTAGE_TIER = { 0, 2, 3, 4, 6 };
    private static final double[] SPEED_MULTIPLIER = { 1.0D, 2.0D, 2.5D, 3.0D, 4.0D };
    private static final double[] EU_MODIFIER = { 1.0D, 0.95D, 0.9D, 0.85D, 0.8D };

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
            .addInfo("Requires a sawblade in the controller slot")
            .addInfo("Sawblade upgrades:")
            .addInfo("Tier 1 (IV/LuV): 2 parallels/Voltage Tier, 200% speed, 95% EU/t")
            .addInfo("Tier 2 (ZPM/UV): 3 parallels/Voltage Tier, 250% speed, 90% EU/t")
            .addInfo("Tier 3 (UHV/UEV): 4 parallels/Voltage Tier, 300% speed, 85% EU/t")
            .addInfo("Tier 4 (UIV+): 6 parallels/Voltage Tier, 400% speed, 80% EU/t")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 11) + "+"
                    + EnumChatFormatting.GRAY
                    + " glass allows for single multi-amp energy hatch")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 12) + EnumChatFormatting.GRAY
                    + "-glass unlocks all above energy tiers")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 4, 3, false)
            .addController("Front left, 2nd layer")
            .addCasingInfoMin("Cutting Factory Frame", 10, false)
            .addCasingInfoExactly("Maraging Steel 300 Frame Box", 18, false)
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
                        { "BCCDDDDD ", "CD     C ", "CDB    C ", "CDCCCCCC " },
                        { "  DBBBBB ", " DDAAAADB", " DDAAAADB", " DDDDDDDB" } })
                .addElement('A', chainAllGlasses())
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
        if (checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 10
            && !mMufflerHatches.isEmpty()) {
            int sawbladeTier = getSawbladeTier(aStack);
            if (sawbladeTier == 0) return false;
            if (!mExoticEnergyHatches.isEmpty()) {
                if (!mEnergyHatches.isEmpty()) return false;
                return sawbladeTier == 4 && mExoticEnergyHatches.size() == 1;
            }

            int maxAllowedEnergyHatchTier = getMaxAllowedEnergyHatchTier(sawbladeTier);
            for (MTEHatchEnergy hatch : mEnergyHatches) {
                if (hatch.mTier > maxAllowedEnergyHatchTier) return false;
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
        return new ProcessingLogic().setSpeedBonusSupplier(this::getSpeedBonus)
            .setEuModifierSupplier(this::getEuModifier)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (!isCorrectMachinePart(getControllerSlot())) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        return super.checkProcessing();
    }

    @Override
    public int getMaxParallelRecipes() {
        return getParallelPerVoltageTier(getControllerSlot()) * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getSawbladeTier(aStack) > 0;
    }

    @Override
    protected boolean canUseControllerSlotForRecipe() {
        return false;
    }

    private double getSpeedBonus() {
        return getSpeedBonus(getControllerSlot());
    }

    private double getEuModifier() {
        return getEuModifier(getControllerSlot());
    }

    private static int getSawbladeTier(ItemStack stack) {
        if (ItemList.T1Sawblade.isStackEqual(stack, false, true)) return 1;
        if (ItemList.T2Sawblade.isStackEqual(stack, false, true)) return 2;
        if (ItemList.T3Sawblade.isStackEqual(stack, false, true)) return 3;
        if (ItemList.T4Sawblade.isStackEqual(stack, false, true)) return 4;
        return 0;
    }

    private static int getParallelPerVoltageTier(ItemStack stack) {
        return PARALLEL_PER_VOLTAGE_TIER[getSawbladeTier(stack)];
    }

    private static int getMaxAllowedEnergyHatchTier(int sawbladeTier) {
        return switch (sawbladeTier) {
            case 1 -> VoltageIndex.LuV;
            case 2 -> VoltageIndex.UV;
            case 3 -> VoltageIndex.UEV;
            case 4 -> Integer.MAX_VALUE;
            default -> 0;
        };
    }

    private static double getSpeedBonus(ItemStack stack) {
        return 1D / SPEED_MULTIPLIER[getSawbladeTier(stack)];
    }

    private static double getEuModifier(ItemStack stack) {
        return EU_MODIFIER[getSawbladeTier(stack)];
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

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        stopAllRendering = !stopAllRendering;
        GTUtility.sendChatTrans(aPlayer, stopAllRendering ? "GT5U.chat.rendering.off" : "GT5U.chat.rendering.on");
        getBaseMetaTileEntity().issueTileUpdate();
        getBaseMetaTileEntity().markDirty();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("stopAllRendering", stopAllRendering);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        stopAllRendering = aNBT.getBoolean("stopAllRendering");
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        data.setBoolean("stopAllRendering", stopAllRendering);
        data.setBoolean("machineFormed", mMachine);
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        stopAllRendering = data.getBoolean("stopAllRendering");
        mMachine = data.getBoolean("machineFormed");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_CUTTING_MACHINE_LOOP;
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (stopAllRendering || !mMachine) {
            return;
        }

        BladeRenderContext context = getBladeRenderContext();
        int frame = base.isActive() ? (int) ((base.getTimer() / BLADE_FRAME_TICKS) % BLADE_FRAMES) : 0;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(BLADE_TEXTURE);

        GL11.glTranslated(x + context.centerX, y + context.centerY, z + context.centerZ);

        renderBladeFrame(frame, context);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        BladeRenderContext context = getBladeRenderContext();

        return AxisAlignedBB.getBoundingBox(
            x + context.centerX - context.halfX,
            y + context.centerY - context.halfY,
            z + context.centerZ - context.halfZ,
            x + context.centerX + context.halfX,
            y + context.centerY + context.halfY,
            z + context.centerZ + context.halfZ);
    }

    private void renderBladeFrame(int frame, BladeRenderContext context) {
        double halfWidth = BLADE_WIDTH / 2.0D;
        double halfHeight = BLADE_HEIGHT / 2.0D;
        double minU = 0.0D;
        double maxU = 1.0D;
        double minV = frame / (double) BLADE_FRAMES;
        double maxV = (frame + 1) / (double) BLADE_FRAMES;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        addBladeVertex(tessellator, context, -halfWidth, -halfHeight, minU, maxV);
        addBladeVertex(tessellator, context, -halfWidth, halfHeight, minU, minV);
        addBladeVertex(tessellator, context, halfWidth, halfHeight, maxU, minV);
        addBladeVertex(tessellator, context, halfWidth, -halfHeight, maxU, maxV);
        tessellator.draw();
    }

    private void addBladeVertex(Tessellator tessellator, BladeRenderContext context, double localX, double localY,
        double u, double v) {
        tessellator.addVertexWithUV(
            localX * context.horizontalX + localY * context.verticalX,
            localX * context.horizontalY + localY * context.verticalY,
            localX * context.horizontalZ + localY * context.verticalZ,
            u,
            v);
    }

    private BladeRenderContext getBladeRenderContext() {
        ExtendedFacing facing = getExtendedFacing();
        if (cachedBladeRenderFacing != facing) {
            cachedBladeRenderFacing = facing;
            bladeRenderContext.update(facing);
        }
        return bladeRenderContext;
    }

    private static double getRenderCenterCoordinate(int horizontalOffset, int verticalOffset, int depthOffset) {
        return 0.5D + RENDER_OFFSET_RIGHT * horizontalOffset
            + RENDER_OFFSET_UP * verticalOffset
            + RENDER_OFFSET_BACK * depthOffset;
    }

    private static double getHalfExtent(int horizontalOffset, int verticalOffset, int depthOffset, double halfWidth,
        double halfHeight, double halfDepth) {
        return Math.abs(horizontalOffset) * halfWidth + Math.abs(verticalOffset) * halfHeight
            + Math.abs(depthOffset) * halfDepth;
    }

    private static final class BladeRenderContext {

        private double centerX;
        private double centerY;
        private double centerZ;
        private double halfX;
        private double halfY;
        private double halfZ;
        private int horizontalX;
        private int horizontalY;
        private int horizontalZ;
        private int verticalX;
        private int verticalY;
        private int verticalZ;

        private void update(ExtendedFacing facing) {
            ForgeDirection horizontal = facing.getRelativeRightInWorld();
            ForgeDirection vertical = facing.getRelativeUpInWorld();
            ForgeDirection depth = facing.getRelativeBackInWorld();
            ForgeDirection centerHorizontal = facing.getRelativeLeftInWorld();
            double halfWidth = BLADE_WIDTH / 2.0D + BLADE_PADDING;
            double halfHeight = BLADE_HEIGHT / 2.0D + BLADE_PADDING;
            double halfDepth = BLADE_PADDING;

            horizontalX = horizontal.offsetX;
            horizontalY = horizontal.offsetY;
            horizontalZ = horizontal.offsetZ;
            verticalX = vertical.offsetX;
            verticalY = vertical.offsetY;
            verticalZ = vertical.offsetZ;
            centerX = getRenderCenterCoordinate(centerHorizontal.offsetX, vertical.offsetX, depth.offsetX);
            centerY = getRenderCenterCoordinate(centerHorizontal.offsetY, vertical.offsetY, depth.offsetY);
            centerZ = getRenderCenterCoordinate(centerHorizontal.offsetZ, vertical.offsetZ, depth.offsetZ);
            halfX = getHalfExtent(
                horizontal.offsetX,
                vertical.offsetX,
                depth.offsetX,
                halfWidth,
                halfHeight,
                halfDepth);
            halfY = getHalfExtent(
                horizontal.offsetY,
                vertical.offsetY,
                depth.offsetY,
                halfWidth,
                halfHeight,
                halfDepth);
            halfZ = getHalfExtent(
                horizontal.offsetZ,
                vertical.offsetZ,
                depth.offsetZ,
                halfWidth,
                halfHeight,
                halfDepth);
        }
    }
}
