package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_GLOW;
import static gregtech.api.util.GTStructureUtility.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.render.RenderingTileEntityLaser;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEIndustrialLaserEngraver extends MTEExtendedPowerMultiBlockBase<MTEIndustrialLaserEngraver>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialLaserEngraver> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialLaserEngraver>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "  f  ", "     ", "     ", "     ", " a~a " },
                { " fsf ", "  g  ", "  g  ", "a g a", "aaraa" }, { "faaaf", "f   f", "f   f", "a   a", "aaaaa" },
                { "aaaaa", "a a a", "a a a", "a a a", "aaaaa" }, { "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa" } }))
        .addElement(
            'a',
            buildHatchAdder(MTEIndustrialLaserEngraver.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, MultiAmpEnergy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(1))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEIndustrialLaserEngraver::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 1))))
        .addElement('f', ofFrame(Materials.TungstenSteel))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('r', ofBlock(GregTechAPI.sLaserRender, 0))
        .addElement(
            's',
            buildHatchAdder(MTEIndustrialLaserEngraver.class).adder(MTEIndustrialLaserEngraver::addLaserSource)
                .hatchClass(MTEHatchDynamoTunnel.class)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(1))
                .hint(3)
                .build())
        .build();

    protected RenderingTileEntityLaser renderer;
    private int glassTier = -1;
    private MTEHatchDynamoTunnel laserSource = null;
    private int laserAmps = 0;
    private int laserTier = 0;
    private String tierName = "LV";

    private boolean addLaserSource(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchDynamoTunnel) {
                laserSource = (MTEHatchDynamoTunnel) aMetaTileEntity;
                laserSource.updateTexture(aBaseCasingIndex);
                // Snap the laser source toward the plate. Player can rotate it if they want after but this will look
                // nicer
                switch (getRotation()) {
                    case NORMAL -> laserSource.getBaseMetaTileEntity()
                        .setFrontFacing(ForgeDirection.DOWN);
                    case UPSIDE_DOWN -> laserSource.getBaseMetaTileEntity()
                        .setFrontFacing(ForgeDirection.UP);
                    case CLOCKWISE -> laserSource.getBaseMetaTileEntity()
                        .setFrontFacing(getDirection().getRotation(ForgeDirection.UP));
                    default -> laserSource.getBaseMetaTileEntity()
                        .setFrontFacing(getDirection().getRotation(ForgeDirection.DOWN));
                }
                // Cube root the amperage to get the parallels
                laserAmps = (int) Math.cbrt(laserSource.maxAmperesOut());
                laserTier = (int) laserSource.getOutputTier();
                tierName = GTValues.VN[laserTier + 1];
                return true;
            }
        }
        return false;
    }

    public MTEIndustrialLaserEngraver(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialLaserEngraver(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialLaserEngraver> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialLaserEngraver(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 1)) };
        }
        return rTexture;
    }

    private boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        stopAllRendering = !stopAllRendering;
        if (stopAllRendering) {
            GTUtility.sendChatToPlayer(aPlayer, "Rendering off");
            if (renderer != null) renderer.setShouldRender(false);
        } else GTUtility.sendChatToPlayer(aPlayer, "Rendering on");
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        } else {
            if (renderer != null) {
                renderer.toggleRealism();
                GTUtility.sendChatToPlayer(aPlayer, "Toggling realism!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockDestroyed() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onBlockDestroyed();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Laser Engraver, HILE")
            .addStaticSpeedInfo(3.5F)
            .addStaticEuEffInfo(0.8F)
            .addInfo("Laser source hatch determines maximum recipe tier and parallels")
            .addInfo("Recipe tier and overclocks limited to laser source tier + 1")
            .addInfo(
                "When using a UEV+ laser source, one multi-amp energy hatch is allowed instead of regular energy hatches")
            .addInfo("Parallels equal to the cube root of laser source amperage input")
            .addInfo("Glass tier determines maximum laser source tier")
            .addInfo(
                GTValues.TIER_COLORS[VoltageIndex.UMV] + GTValues.VN[VoltageIndex.UMV]
                    + EnumChatFormatting.GRAY
                    + "-tier glass accepts all laser source hatches")
            .addInfo("Use screwdriver to disable laser rendering")
            .addInfo("Use wire cutter to toggle realism mode if you hate angled lasers")
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front Center")
            .addCasingInfoMin("Laser Containment Casing", 35, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 9, false)
            .addCasingInfoExactly("Any Tiered Glass", 3, true)
            .addOtherStructurePart("Laser Resistant Plate", "x1")
            .addOtherStructurePart(StatCollector.translateToLocal("GT5U.tooltip.structure.laser_source_hatch"), "x1", 3)
            .addInputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private boolean findLaserRenderer(World w, int x, int y, int z) {
        ForgeDirection opposite = getDirection().getOpposite();
        x = x + opposite.offsetX;
        y = y + opposite.offsetY;
        z = z + opposite.offsetZ;
        if (w.getTileEntity(x, y, z) instanceof RenderingTileEntityLaser laser) {
            renderer = laser;
            renderer.setRotationFields(getExtendedFacing());
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        glassTier = -1;
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0)) return false;
        if (mCasingAmount < 35) return false;
        if (laserSource == null) return false;
        if (!findLaserRenderer(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord())) return false;

        // If there are exotic hatches, ensure there is only 1 and that the laser source requirement is met
        if (!mExoticEnergyHatches.isEmpty()) {
            if (laserSource.mTier < VoltageIndex.UEV) return false;
            if (!mEnergyHatches.isEmpty()) return false;
            return (mExoticEnergyHatches.size() == 1);
        }

        return glassTier >= VoltageIndex.UMV || laserSource.mTier <= glassTier;
    }

    private static String getUniqueIdentifier(ItemStack is) {
        return is.getItem()
            .getUnlocalizedName() + is.getItemDamage();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (laserTier < VoltageIndex.UXV && recipe.mEUt > (GTValues.V[laserTier + 1])) {
                    return SimpleCheckRecipeResult.ofFailure("laser_insufficient");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                if (!stopAllRendering) {
                    Colors c = Colors.Red;
                    for (int i = 0; i < recipe.mInputs.length; i++) {
                        String uid = getUniqueIdentifier(recipe.mInputs[i]);
                        if (lensColors.containsKey(uid)) {
                            c = lensColors.get(uid);
                        }
                    }
                    if (renderer != null) {
                        renderer.setColors(c.r, c.g, c.b);
                        renderer.setShouldRender(true);
                    }
                }
                return super.onRecipeStart(recipe);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks((laserSource.mTier + 1) - GTUtility.getTier(recipe.mEUt));
            }

            @Override
            public ProcessingLogic clear() {
                if (renderer != null) renderer.setShouldRender(false);
                return super.clear();
            }
        }.setSpeedBonus(1F / 3.5F)
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return true;
    }

    @Override
    public int getMaxParallelRecipes() {
        return laserAmps;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.laserEngraverRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
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
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("stopAllRendering", stopAllRendering);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("stopAllRendering")) {
            stopAllRendering = aNBT.getBoolean("stopAllRendering");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("tierName", tierName);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.maxtier") + ": "
                + EnumChatFormatting.WHITE
                + tag.getString("tierName")
                + EnumChatFormatting.RESET);
    }

    private enum Colors {

        White(1, 1, 1),
        Red(1, 0, 0),
        Green(0, 1, 0),
        Blue(0, 0, 1),
        Yellow(1, 1, 0),
        Purple(1, 0, 1),
        Cyan(0, 1, 1),
        Orange(1, 0.5F, 0),
        Black(0, 0, 0),
        Brown(0.647F, 0.164F, 0.164F);

        final float r, g, b;

        Colors(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private static final Map<String, Colors> lensColors;
    static {
        lensColors = new HashMap<>();

        // Black lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 1)), Colors.Black);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 1)), Colors.Black);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEntropy, 1)),
            Colors.Black);

        // White lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.NetherStar, 1)), Colors.White);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 1)), Colors.White);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Glass, 1)), Colors.White);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)), Colors.Cyan);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedOrder, 1)),
            Colors.White);

        // Green lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1)), Colors.Green);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Olivine, 1)), Colors.Green);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.GreenSapphire, 1)),
            Colors.Green);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEarth, 1)),
            Colors.Green);

        // Red lenses
        lensColors.put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Ruby, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Firestone, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Jasper, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Spinel, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.GarnetRed, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedFire, 1)), Colors.Red);

        // Blue lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)), Colors.Blue);
        lensColors.put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Opal, 1)), Colors.Blue);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedWater, 1)),
            Colors.Blue);

        // Yellow lenses
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.GarnetYellow, 1)),
            Colors.Yellow);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Force, 1)), Colors.Yellow);
        lensColors.put(
            getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.InfusedAir, 1)),
            Colors.Yellow);

        // Purple lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Amethyst, 1)), Colors.Purple);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Tanzanite, 1)), Colors.Purple);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Sapphire, 1)), Colors.Purple);

        // Cyan lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)), Colors.Cyan);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)), Colors.Cyan);

        // Orange lenses
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Topaz, 1)), Colors.Orange);
        lensColors
            .put(getUniqueIdentifier(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Amber, 1)), Colors.Orange);

        // Time to manually define a bunch of lenses based on id
        lensColors.put("gt.bwMetaGeneratedlens1", Colors.Yellow);
        lensColors.put("gt.bwMetaGeneratedlens4", Colors.White);
        lensColors.put("gt.bwMetaGeneratedlens5", Colors.Black);
        lensColors.put("gt.bwMetaGeneratedlens7", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens8", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens9", Colors.White);
        lensColors.put("gt.bwMetaGeneratedlens19", Colors.Red);
        lensColors.put("gt.bwMetaGeneratedlens20", Colors.White);
        lensColors.put("gt.bwMetaGeneratedlens21", Colors.Brown);
        lensColors.put("gt.bwMetaGeneratedlens22", Colors.Orange);
        lensColors.put("gt.bwMetaGeneratedlens23", Colors.Black);
        lensColors.put("gt.bwMetaGeneratedlens24", Colors.White);
        lensColors.put("gt.bwMetaGeneratedlens25", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens35", Colors.Yellow);
        lensColors.put("gt.bwMetaGeneratedlens36", Colors.Purple);
        lensColors.put("gt.bwMetaGeneratedlens43", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens89", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens91", Colors.Purple);
        lensColors.put("gt.bwMetaGeneratedlens10023", Colors.Red);
        lensColors.put("gt.bwMetaGeneratedlens11499", Colors.Green);
        lensColors.put("gt.bwMetaGeneratedlens11358", Colors.Red);
        lensColors.put("MU-metaitem.0132140", Colors.Purple);

        //
    }
}
