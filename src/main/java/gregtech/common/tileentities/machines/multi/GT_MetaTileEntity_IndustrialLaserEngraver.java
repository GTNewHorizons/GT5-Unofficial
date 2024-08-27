package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
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

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GTVoltageIndex;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.LaserRenderingUtil;
import gregtech.common.blocks.GT_Block_Casings10;
import gregtech.common.tileentities.render.TileLaser;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_IndustrialLaserEngraver
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_IndustrialLaserEngraver>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialLaserEngraver> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialLaserEngraver>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "  f  ", "     ", "     ", "     ", " a~a " },
                { " fsf ", "  g  ", "  g  ", "a g a", "aaraa" }, { "faaaf", "f   f", "f   f", "a   a", "aaaaa" },
                { "aaaaa", "a a a", "a a a", "a a a", "aaaaa" }, { "aaaaa", "aaaaa", "aaaaa", "aaaaa", "aaaaa" } }))
        .addElement(
            'a',
            buildHatchAdder(GT_MetaTileEntity_IndustrialLaserEngraver.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(1))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_IndustrialLaserEngraver::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 1))))
        .addElement('f', ofFrame(Materials.TungstenSteel))
        .addElement(
            'g',
            BorosilicateGlass
                .ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement(
            'r',
            LaserRenderingUtil.ofBlockAdder(
                GT_MetaTileEntity_IndustrialLaserEngraver::laserRendererAdder,
                GregTech_API.sLaserRender,
                0))
        .addElement(
            's',
            buildHatchAdder(GT_MetaTileEntity_IndustrialLaserEngraver.class)
                .adder(GT_MetaTileEntity_IndustrialLaserEngraver::addLaserSource)
                .hatchClass(GT_MetaTileEntity_Hatch_DynamoTunnel.class)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(1))
                .dot(3)
                .build())
        .build();

    protected TileLaser renderer;
    private byte glassTier = 0;
    private GT_MetaTileEntity_Hatch_DynamoTunnel laserSource = null;
    private int laserAmps = 0;
    private int laserTier = 0;
    private String tierName = "LV";

    private boolean addLaserSource(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoTunnel) {
                laserSource = (GT_MetaTileEntity_Hatch_DynamoTunnel) aMetaTileEntity;
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
                tierName = GT_Values.VN[laserTier];
                return true;
            }
        }
        return false;
    }

    private boolean laserRendererAdder(Block block, int meta, World world, int x, int y, int z) {
        if (block != GregTech_API.sLaserRender || world == null) {
            return false;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileLaser) {
            renderer = (TileLaser) te;
            renderer.setRotationFields(getDirection(), getRotation(), getFlip());
            return true;
        }
        return false;
    }

    public GT_MetaTileEntity_IndustrialLaserEngraver(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialLaserEngraver(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialLaserEngraver> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialLaserEngraver(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 1)),
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
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 1)),
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
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 1)) };
        }
        return rTexture;
    }

    private boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        stopAllRendering = !stopAllRendering;
        if (stopAllRendering) {
            PlayerUtils.messagePlayer(aPlayer, "Rendering off");
            if (renderer != null) renderer.setShouldRender(false);
        } else PlayerUtils.messagePlayer(aPlayer, "Rendering on");
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        } else {
            if (renderer != null) {
                renderer.realism = !renderer.realism;
                PlayerUtils.messagePlayer(aPlayer, "Toggling realism!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisableWorking() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onDisableWorking();
    }

    @Override
    public void onBlockDestroyed() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onBlockDestroyed();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Laser Engraver")
            .addInfo("Controller Block for the Hyper-Intensity Laser Engraver")
            .addInfo("200% faster than single block machines of the same voltage")
            .addInfo("Uses 80% of the EU normally required")
            .addInfo("Laser source hatch determines maximum recipe tier and parallels")
            .addInfo("Parallels equal to the cube root of laser source amperage input")
            .addInfo("Glass tier determines maximum laser source tier")
            .addInfo("Only accepts borosilicate glass (no, really)")
            .addInfo("UMV glass accepts all laser source hatches")
            .addInfo("Use screwdriver to disable laser rendering")
            .addInfo("Use wire cutter to toggle realism mode if you hate angled lasers")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front Center")
            .addCasingInfoMin("Laser Containment Casing", 45, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 9, false)
            .addOtherStructurePart("Laser Resistant Plate", "x1")
            .addOtherStructurePart("Borosilicate Glass", "x3")
            .addOtherStructurePart("Laser Source Hatch", "x1", 3)
            .addInputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0)) return false;
        if (mCasingAmount < 45) return false;
        if (laserSource == null) return false;
        if (renderer == null) return false;
        if (glassTier < GTVoltageIndex.UMV && laserSource.mTier > glassTier) return false;

        return true;
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
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                if (laserTier < GTVoltageIndex.UXV && recipe.mEUt > GT_Values.V[laserTier]) {
                    return SimpleCheckRecipeResult.ofFailure("laser_insufficient");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GT_Recipe recipe) {
                Colors c = Colors.Red;
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    String uid = getUniqueIdentifier(recipe.mInputs[i]);
                    if (lensColors.containsKey(uid)) {
                        c = lensColors.get(uid);
                    }
                }
                if (renderer != null) {
                    renderer.setColors(c.r, c.g, c.b);
                    if (!stopAllRendering) {
                        renderer.setShouldRender(true);
                    }
                }
                return super.onRecipeStart(recipe);
            }

            @Override
            public ProcessingLogic clear() {
                if (renderer != null) renderer.setShouldRender(false);
                return super.clear();
            }
        }.setSpeedBonus(1F / 3F)
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return true;
    }

    private int getMaxParallelRecipes() {
        return laserAmps;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.laserEngraverRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
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
        tag.setInteger("laserAmps", laserAmps);
        tag.setString("tierName", tierName);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("laserAmps")
                + EnumChatFormatting.RESET);
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
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 1)), Colors.Black);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 1)),
            Colors.Black);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEntropy, 1)),
            Colors.Black);

        // White lenses
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.NetherStar, 1)),
            Colors.White);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 1)), Colors.White);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Glass, 1)), Colors.White);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)), Colors.Cyan);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedOrder, 1)),
            Colors.White);

        // Green lenses
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1)), Colors.Green);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Olivine, 1)), Colors.Green);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GreenSapphire, 1)),
            Colors.Green);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEarth, 1)),
            Colors.Green);

        // Red lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Ruby, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Firestone, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Jasper, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.FoolsRuby, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GarnetRed, 1)), Colors.Red);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedFire, 1)), Colors.Red);

        // Blue lenses
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)), Colors.Blue);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Opal, 1)), Colors.Blue);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedWater, 1)),
            Colors.Blue);

        // Yellow lenses
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GarnetYellow, 1)),
            Colors.Yellow);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Force, 1)), Colors.Yellow);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedAir, 1)),
            Colors.Yellow);

        // Purple lenses
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amethyst, 1)), Colors.Purple);
        lensColors.put(
            getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Tanzanite, 1)),
            Colors.Purple);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Sapphire, 1)), Colors.Purple);

        // Cyan lenses
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)), Colors.Cyan);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)), Colors.Cyan);

        // Orange lenses
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Topaz, 1)), Colors.Orange);
        lensColors
            .put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amber, 1)), Colors.Orange);

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
        lensColors.put("MU-metaitem.0132140", Colors.Purple);

        //
    }
}
