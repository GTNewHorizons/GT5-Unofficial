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

import java.nio.DoubleBuffer;
import java.util.List;

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
import org.lwjgl.BufferUtils;
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
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
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
    private static final double RENDER_OFFSET_UP = -0.5D;
    private static final double RENDER_OFFSET_BACK = 1.0D;
    private static final double BLADE_WIDTH = 3.0D;
    private static final double BLADE_HEIGHT = 3.0D;
    private static final double BLADE_PADDING = 0.25D;
    private static final float BLADE_ROTATION_SPEED = 18.0F;
    private static final long BLADE_TICKS_PER_ROTATION = Math.round(360.0F / BLADE_ROTATION_SPEED);
    private static final double BLADE_CLIP_BOTTOM = 1.0D;
    @SideOnly(Side.CLIENT)
    private static DoubleBuffer CLIP_PLANE_BUFFER;
    private static final ResourceLocation[] BLADE_TEXTURES = new ResourceLocation[] {
        new ResourceLocation(GregTech.resourceDomain, "textures/model/cutter_t1.png"),
        new ResourceLocation(GregTech.resourceDomain, "textures/model/cutter_t2.png"),
        new ResourceLocation(GregTech.resourceDomain, "textures/model/cutter_t3.png"),
        new ResourceLocation(GregTech.resourceDomain, "textures/model/cutter_t4.png") };
    private int casingAmount;
    private boolean stopAllRendering;
    private int renderSawbladeTier = -1;
    private ExtendedFacing cachedBladeRenderFacing;
    private final BladeRenderContext bladeRenderContext = new BladeRenderContext();

    public enum SawbladeTiers {

        TungstenTitaniumCarbide(2, 2.5F, 0.9F, VoltageIndex.LuV, false),
        MysteriousCrystal(3, 3.0F, 0.8F, VoltageIndex.UV, false),
        Neutronium(4, 3.5F, 0.7F, VoltageIndex.UEV, false),
        TranscendentMetal(6, 4.5F, 0.6F, Integer.MAX_VALUE, true);

        final int parallelPerVoltageTier;
        final float speedBoost, euModifier;
        final int maxAllowedEnergyHatchTier;
        final boolean supportsExotic;

        SawbladeTiers(int parallelPerVoltageTier, float speedBoost, float euModifier, int maxAllowedEnergyHatchTier,
            boolean supportsExotic) {
            this.parallelPerVoltageTier = parallelPerVoltageTier;
            this.speedBoost = 1F / speedBoost;
            this.euModifier = euModifier;
            this.maxAllowedEnergyHatchTier = maxAllowedEnergyHatchTier;
            this.supportsExotic = supportsExotic;
        }

        public static String buildSawbladeTooltip(SawbladeTiers sawblade) {
            String hatchTierLimit = sawblade.maxAllowedEnergyHatchTier == Integer.MAX_VALUE
                ? GTUtility.translate("gt.sawblade.tooltip.hatch_tier_unlimited")
                : GTUtility.translate(
                    "gt.sawblade.tooltip.hatch_tier_limit",
                    GTUtility.getColoredTierNameFromTier((byte) sawblade.maxAllowedEnergyHatchTier));
            String tooltip = GTUtility.translate(
                "gt.sawblade.tooltip.base",
                hatchTierLimit,
                sawblade.parallelPerVoltageTier,
                Math.round(1F / sawblade.speedBoost * 100),
                Math.round(sawblade.euModifier * 100));

            if (sawblade.supportsExotic) {
                tooltip = tooltip + "\\n" + GTUtility.translate("gt.sawblade.tooltip.exotic");
            }

            return tooltip;
        }
    }

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
            .addInfo(
                "Requires a " + EnumChatFormatting.AQUA
                    + "Sawblade"
                    + EnumChatFormatting.GRAY
                    + " in the controller slot to use")
            .addInfo(
                "Better " + EnumChatFormatting.AQUA + "Sawblades" + EnumChatFormatting.GRAY + " give increased bonuses")
            .addInfo(
                "With a " + EnumChatFormatting.DARK_GREEN
                    + "Transcendent Metal Sawblade"
                    + EnumChatFormatting.GRAY
                    + ", one multi-amp hatch is allowed")
            .addInfo("Use screwdriver to disable sawblade rendering")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 4, 3, false)
            .addController("Front left, 2nd layer")
            .addCasingInfoMin("Cutting Factory Frame", 10, false)
            .addCasingInfoExactly("Tantalum Carbide Frame Box", 18, false)
            .addCasingInfoExactly("Any Tiered Glass", 16, false)
            .addCasingInfoExactly("Black Steel Sheetmetal", 13, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addStructureAuthors(EnumChatFormatting.LIGHT_PURPLE + "Auynonymous")
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
                .addElement('B', ofFrame(MaterialsAlloy.TANTALUM_CARBIDE))
                .addElement('C', ofSheetMetal(Materials.BlackSteel))
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 10);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
            if (mExoticEnergyHatches.size() != 1) errors.add(StructureErrorRegistry.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER);
        } else {
            checkHasEnergyHatch(errors);
        }
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
        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        updateSawbladeRenderTier();
        if (sawbladeTier == null) {
            return SimpleCheckRecipeResult.ofFailure("sawblade_missing");
        }
        if (!canSawbladeAcceptEnergyHatches(sawbladeTier)) {
            return SimpleCheckRecipeResult.ofFailure("sawblade_tier_not_high_enough");
        }
        return super.checkProcessing();
    }

    @Override
    public int getMaxParallelRecipes() {
        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        if (sawbladeTier == null) return 0;
        return sawbladeTier.parallelPerVoltageTier * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return isValidSawblade(aStack);
    }

    @Override
    protected boolean canUseControllerSlotForRecipe() {
        return false;
    }

    private double getSpeedBonus() {
        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        if (sawbladeTier == null) return 1D;
        return sawbladeTier.speedBoost;
    }

    private double getEuModifier() {
        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        if (sawbladeTier == null) return 1D;
        return sawbladeTier.euModifier;
    }

    private static SawbladeTiers getSawbladeTier(ItemStack stack) {
        if (ItemList.T1Sawblade.isStackEqual(stack, false, true)) return SawbladeTiers.TungstenTitaniumCarbide;
        if (ItemList.T2Sawblade.isStackEqual(stack, false, true)) return SawbladeTiers.MysteriousCrystal;
        if (ItemList.T3Sawblade.isStackEqual(stack, false, true)) return SawbladeTiers.Neutronium;
        if (ItemList.T4Sawblade.isStackEqual(stack, false, true)) return SawbladeTiers.TranscendentMetal;
        return null;
    }

    public static boolean isValidSawblade(ItemStack stack) {
        return getSawbladeTier(stack) != null;
    }

    private boolean canSawbladeAcceptEnergyHatches(SawbladeTiers sawbladeTier) {
        if (!mExoticEnergyHatches.isEmpty()) return sawbladeTier.supportsExotic;
        if (sawbladeTier.maxAllowedEnergyHatchTier == Integer.MAX_VALUE) return true;

        for (MTEHatchEnergy hatch : mEnergyHatches) {
            if (hatch.mTier > sawbladeTier.maxAllowedEnergyHatchTier) return false;
        }
        return true;
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (aTick % 100 != 0) return;
        updateSawbladeRenderTier();
    }

    private void updateSawbladeRenderTier() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || !base.isServerSide()) return;

        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        int sawbladeTierIndex = sawbladeTier == null ? -1 : sawbladeTier.ordinal();
        if (renderSawbladeTier == sawbladeTierIndex) return;

        renderSawbladeTier = sawbladeTierIndex;
        base.issueTileUpdate();
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
        SawbladeTiers sawbladeTier = getSawbladeTier(getControllerSlot());
        data.setBoolean("stopAllRendering", stopAllRendering);
        data.setBoolean("machineFormed", mMachine);
        data.setInteger("renderSawbladeTier", sawbladeTier == null ? -1 : sawbladeTier.ordinal());
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        stopAllRendering = data.getBoolean("stopAllRendering");
        mMachine = data.getBoolean("machineFormed");
        renderSawbladeTier = data.getInteger("renderSawbladeTier");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_CUTTING_MACHINE_LOOP;
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {

        if (CLIP_PLANE_BUFFER == null) {
            CLIP_PLANE_BUFFER = BufferUtils.createDoubleBuffer(4);
        }

        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || stopAllRendering
            || !mMachine
            || renderSawbladeTier < 0
            || renderSawbladeTier >= BLADE_TEXTURES.length) {
            return;
        }

        BladeRenderContext context = getBladeRenderContext();

        float angle = 0.0F;
        if (base.isActive()) {
            float ticksIntoRotation = (base.getTimer() % BLADE_TICKS_PER_ROTATION) + timeSinceLastTick;
            angle = ticksIntoRotation * BLADE_ROTATION_SPEED;
        }

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(BLADE_TEXTURES[renderSawbladeTier]);

        GL11.glTranslated(x + context.centerX, y + context.centerY, z + context.centerZ);

        double clipD = BLADE_HEIGHT / 2.0 - BLADE_CLIP_BOTTOM;
        CLIP_PLANE_BUFFER.clear();
        CLIP_PLANE_BUFFER.put(0.0)
            .put(1.0)
            .put(0.0)
            .put(clipD);
        CLIP_PLANE_BUFFER.flip();
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, CLIP_PLANE_BUFFER);
        GL11.glEnable(GL11.GL_CLIP_PLANE0);

        GL11.glRotatef(-angle, context.depthX, context.depthY, context.depthZ);

        renderBlade(context);

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

    private void renderBlade(BladeRenderContext context) {
        double halfWidth = BLADE_WIDTH / 2.0D;
        double halfHeight = BLADE_HEIGHT / 2.0D;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        addBladeVertex(tessellator, context, -halfWidth, -halfHeight, 0.0D, 1.0D);
        addBladeVertex(tessellator, context, -halfWidth, halfHeight, 0.0D, 0.0D);
        addBladeVertex(tessellator, context, halfWidth, halfHeight, 1.0D, 0.0D);
        addBladeVertex(tessellator, context, halfWidth, -halfHeight, 1.0D, 1.0D);
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
        private int depthX;
        private int depthY;
        private int depthZ;

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
            depthX = depth.offsetX;
            depthY = depth.offsetY;
            depthZ = depth.offsetZ;
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
