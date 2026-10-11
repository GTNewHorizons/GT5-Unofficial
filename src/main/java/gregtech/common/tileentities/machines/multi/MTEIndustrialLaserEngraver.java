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
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
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
    implements ISurvivalConstructable, ICasingTextureProvider {

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
            buildHatchAdder(MTEIndustrialLaserEngraver.class).anyOf(LaserSource)
                .adder(MTEIndustrialLaserEngraver::addLaserSource)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(1))
                .hint(2)
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
                rTexture = new ITexture[] { getCasingTexture(), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { getCasingTexture(), TextureFactory.builder()
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
            rTexture = new ITexture[] { getCasingTexture() };
        }
        return rTexture;
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 1));
    }

    private boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        stopAllRendering = !stopAllRendering;
        if (stopAllRendering) {
            GTUtility.sendChatTrans(aPlayer, "GT5U.chat.rendering.off");
            if (renderer != null) renderer.setShouldRender(false);
        } else GTUtility.sendChatTrans(aPlayer, "GT5U.chat.rendering.on");
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (!aPlayer.isSneaking()) {
            if (renderer != null) {
                renderer.toggleRealism();
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.laser.engraver.toggling_realism");
                return true;
            }
        }
        return super.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
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
            .addInfo("Laser Source Hatch determines maximum Recipe Tier and parallels")
            .addInfo("Recipe Tier and overclocks limited to Laser Source Tier + 1")
            .addInfo(
                "When using a " + GTValues.TIER_COLORS[VoltageIndex.UEV]
                    + GTValues.VN[VoltageIndex.UEV]
                    + EnumChatFormatting.GRAY
                    + "+ laser source, one multi-amp energy hatch is allowed instead of regular energy hatches")
            .addInfo("Parallels equal to the cube root of laser source amperage input")
            .addInfo(
                EnumChatFormatting.WHITE + "Glass "
                    + EnumChatFormatting.GRAY
                    + "Tier determines maximum Laser Source Tier")
            .addInfo("Use screwdriver to disable laser rendering")
            .addInfo("Use wire cutter to toggle realism mode if you hate angled lasers")
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front bottom center")
            .addCasing("35-58", "Laser Containment Casing", false)
            .addCasing("9", "Tungstensteel Frame Box", false)
            .addCasing("3", "Any Tiered Glass", true)
            .addCasing("1", "Laser Resistant Plate", false)
            .addMiscHatch(
                "1",
                StatCollector.translateToLocal("GT5U.tooltip.structure.laser_source_hatch"),
                "Casing above glass",
                2)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addInputAny("1+", "Any casing", 1)
            .addOutputAny("1+", "Any casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasingAmount = 0;
        glassTier = -1;
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0, errors)) return;
        checkCasingMin(errors, mCasingAmount, 35);
        if (!mExoticEnergyHatches.isEmpty()) {
            if (laserSource.mTier < VoltageIndex.UEV) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.laser_need_uev"));
            } else {
                int count = mEnergyHatches.size() + mExoticEnergyHatches.size();
                if (count != 1) {
                    errors.add(StructureErrors.hatchCount(ErrorType.TOO_MANY, Energy, count, 1));
                }
            }
        } else {
            checkHasEnergyHatch(errors);
        }
        checkHasMaintenanceHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);

        if (glassTier < laserSource.mTier) {
            errors.add(StructureErrors.glassTierNotEnough(laserSource.mTier));
        }
        if (!errors.isEmpty()) return;
        findLaserRenderer(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord());
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
                        Colors lensColor = lensColors().get(uid);
                        if (lensColor != null) {
                            c = lensColor;
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
        }.setSpeedBonus(1.0D / 3.5D)
            .setEuModifier(0.8D)
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
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setString("tierName", tierName);
    }

    @Override
    public void getExtraWailaBody(ItemStack itemStack, List<String> list, NBTTagCompound tag,
        IWailaDataAccessor accessor, IWailaConfigHandler config) {
        list.add(
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

    private static Map<String, Colors> lensColors;

    /// The laser colour each lens renders, keyed by [#getUniqueIdentifier]. Built on first use; a material
    /// carrying no lens contributes no entry.
    private static Map<String, Colors> lensColors() {
        if (lensColors != null) return lensColors;
        Map<String, Colors> colors = new HashMap<>();
        // Black lenses
        putLens(colors, Materials.Forcicium, Colors.Black);
        putLens(colors, Materials.Forcillium, Colors.Black);
        putLens(colors, Materials.InfusedEntropy, Colors.Black);

        // White lenses
        putLens(colors, Materials.NetherStar, Colors.White);
        putLens(colors, Materials.Dilithium, Colors.White);
        putLens(colors, Materials.Glass, Colors.White);
        putLens(colors, Materials.Diamond, Colors.Cyan);
        putLens(colors, Materials.InfusedOrder, Colors.White);

        // Green lenses
        putLens(colors, Materials.Emerald, Colors.Green);
        putLens(colors, Materials.Olivine, Colors.Green);
        putLens(colors, Materials.GreenSapphire, Colors.Green);
        putLens(colors, Materials.InfusedEarth, Colors.Green);

        // Red lenses
        putLens(colors, Materials.Ruby, Colors.Red);
        putLens(colors, Materials.Firestone, Colors.Red);
        putLens(colors, Materials.Jasper, Colors.Red);
        putLens(colors, Materials.FoolsRuby, Colors.Red);
        putLens(colors, Materials.GarnetRed, Colors.Red);
        putLens(colors, Materials.InfusedFire, Colors.Red);

        // Blue lenses
        putLens(colors, Materials.BlueTopaz, Colors.Blue);
        putLens(colors, Materials.Opal, Colors.Blue);
        putLens(colors, Materials.InfusedWater, Colors.Blue);

        // Yellow lenses
        putLens(colors, Materials.GarnetYellow, Colors.Yellow);
        putLens(colors, Materials.Force, Colors.Yellow);
        putLens(colors, Materials.InfusedAir, Colors.Yellow);

        // Purple lenses
        putLens(colors, Materials.Amethyst, Colors.Purple);
        putLens(colors, Materials.Tanzanite, Colors.Purple);
        putLens(colors, Materials.Sapphire, Colors.Purple);

        // Cyan lenses
        putLens(colors, Materials.EnderEye, Colors.Cyan);
        putLens(colors, Materials.EnderPearl, Colors.Cyan);

        // Orange lenses
        putLens(colors, Materials.Topaz, Colors.Orange);
        putLens(colors, Materials.Amber, Colors.Orange);
        putLens(colors, Materials.Bismutite, Colors.Yellow);
        putLens(colors, Materials.CubicZirconia, Colors.White);
        putLens(colors, Materials.FluorBuergerite, Colors.Black);
        putLens(colors, Materials.ChromoAluminoPovondraite, Colors.Green);
        putLens(colors, Materials.VanadioOxyDravite, Colors.Green);
        putLens(colors, Materials.Olenite, Colors.White);
        putLens(colors, Materials.RedZircon, Colors.Red);
        putLens(colors, Materials.Salt, Colors.White);
        putLens(colors, Materials.Spodumene, Colors.Brown);
        putLens(colors, Materials.RockSalt, Colors.Orange);
        putLens(colors, Materials.Fayalite, Colors.Black);
        putLens(colors, Materials.Forsterite, Colors.White);
        putLens(colors, Materials.Hedenbergite, Colors.Green);
        putLens(colors, Materials.Prasiolite, Colors.Yellow);
        putLens(colors, Materials.MagnetoResonatic, Colors.Purple);
        putLens(colors, Materials.BArTiMaEuSNeK, Colors.Green);
        putLens(colors, Materials.Tiberium, Colors.Green);
        putLens(colors, Materials.Fluorspar, Colors.Purple);
        putLens(colors, Materials.Orundum, Colors.Red);
        putLens(colors, Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Colors.Green);
        putLens(colors, Materials.LanthanumHexaboride, Colors.Red);
        colors.put("MU-metaitem.0132140", Colors.Purple);
        lensColors = colors;
        return colors;
    }

    private static void putLens(Map<String, Colors> colors, Material material, Colors color) {
        ItemStack lens = MaterialLibAPI.getStack(material, Shapes.lens, 1);
        colors.put(getUniqueIdentifier(lens), color);
    }
}
