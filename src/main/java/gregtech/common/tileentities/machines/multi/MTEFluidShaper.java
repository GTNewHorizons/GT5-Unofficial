package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTAuthors.AuthorOmdaCZ;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * DEPRECATED! Will be removed after 2.9 is released. see
 * {@link gregtech.common.tileentities.machines.multi.MTEMassSolidifier} instead
 */
public class MTEFluidShaper extends MTEExtendedPowerMultiBlockBase<MTEFluidShaper> implements ISurvivalConstructable {

    private static final String MS_LEFT_MID = "leftmid";
    private static final String MS_RIGHT_MID = "rightmid";
    private static final String MS_END = "end";

    private static final int BASE_PARALLELS = 2;
    private static final int PARALLELS_PER_WIDTH = 3;
    private static final double DECAY_RATE = 0.025;

    private int glassTier = -1;
    protected int width;
    private int casingAmount;
    private float speedup = 1;
    private int runningTickCounter = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEFluidShaper> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEFluidShaper>builder()
        .addShape(
            MS_LEFT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB", }, { "  ", "AA", "D ", "AA", }, { "  ", "AA", "  ", "AA", },
                    { "  ", "CC", "FC", "CC", }, { "  ", "BB", "BB", "BB", } })))
        .addShape(
            MS_RIGHT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB" }, { "  ", "AA", " D", "AA" }, { "  ", "AA", "  ", "AA" },
                    { "  ", "CC", "CF", "CC" }, { "  ", "BB", "BB", "BB" } })))
        .addShape(
            MS_END,
            (transpose(
                new String[][] { { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" },
                    { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" } })))
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "       ", "BBBBBBB", "BBBBBBB", "BBBBBBB", "       " },
                    { "BBBBBBB", "       ", "D D D D", "       ", "BBBBBBB" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCCCCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement(
            'B',
            buildHatchAdder(MTEFluidShaper.class).atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(13))
                .hint(1)
                .buildAndChain(onElementPass(MTEFluidShaper::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 13))))

        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 14))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .build();

    public MTEFluidShaper(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFluidShaper(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFluidShaper(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier")
            .addInfo(
                EnumChatFormatting.RED + "DEPRECATED!"
                    + EnumChatFormatting.GRAY
                    + " Will be removed in next major version. Use the "
                    + EnumChatFormatting.YELLOW
                    + "Mass Solidifier"
                    + EnumChatFormatting.GRAY
                    + " instead")
            .addInfo(
                "Can use " + EnumChatFormatting.YELLOW
                    + "Solidifier Hatches"
                    + EnumChatFormatting.GRAY
                    + " to hold different molds")
            .addVoltageParallelInfo(BASE_PARALLELS)
            .addInfo(
                "Gains " + TooltipHelper.parallelText(PARALLELS_PER_WIDTH)
                    + " Parallels per "
                    + EnumChatFormatting.WHITE
                    + "Voltage "
                    + EnumChatFormatting.GRAY
                    + "Tier per width expansion")
            .addInfo("Speeds up to a maximum of " + TooltipHelper.speedText(3f))
            .addInfo("Decays at double the rate that it speeds up at")
            .addStaticEuEffInfo(0.8f)
            .addGlassEnergyLimitInfo(VoltageIndex.UMV)
            .addInfo(EnumChatFormatting.BLUE + "Pretty Ⱄⱁⰾⰻⰴ, isn't it")
            .beginVariableStructureBlock(9, 33, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoRange("Solidifier Casing", 91, 211, false)
            .addCasingInfoRange("Solidifier Radiator", 13, 73, false)
            .addCasingInfoRange("Heat Proof Machine Casing", 4, 16, false)
            .addCasingInfoRange("Clean Stainless Steel Machine Casing", 4, 16, false)
            .addCasingInfoRange("Any Tiered Glass", 14, 117, true)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(AuthorOmdaCZ);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        // max Width, minimal mid-pieces to build on each side
        int totalWidth = Math.min(stackSize.stackSize - 1, 6);
        for (int i = 0; i < totalWidth; i++) {
            // pieces are 2 wide so offset 5 from controller and number of pieces times width of each piece
            buildPiece(MS_LEFT_MID, stackSize, hintsOnly, 5 + 2 * i, 4, 0);
            // the extra offset to account for piece width isn't needed in this direction
            buildPiece(MS_RIGHT_MID, stackSize, hintsOnly, -4 - 2 * i, 4, 0);
        }
        buildPiece(MS_END, stackSize, hintsOnly, 4 + 2 * totalWidth, 4, 0);
        buildPiece(MS_END, stackSize, hintsOnly, -4 - 2 * totalWidth, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int totalWidth = Math.min(stackSize.stackSize - 1, 6);
        for (int i = 0; i < totalWidth; i++) {
            built = survivalBuildPiece(MS_LEFT_MID, stackSize, 5 + 2 * i, 4, 0, elementBudget, env, false, true);
            built += survivalBuildPiece(MS_RIGHT_MID, stackSize, -4 - 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        built = survivalBuildPiece(MS_END, stackSize, -4 - 2 * totalWidth, 4, 0, elementBudget, env, false, true);
        built += survivalBuildPiece(MS_END, stackSize, 4 + 2 * totalWidth, 4, 0, elementBudget, env, false, true);
        return built;
    }

    @Override
    public IStructureDefinition<MTEFluidShaper> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        width = 0;
        casingAmount = 0;
        glassTier = -1;

        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (width < (6)) {
                if (checkPiece(MS_LEFT_MID, 5 + 2 * width, 4, 0) && checkPiece(MS_RIGHT_MID, -4 - 2 * width, 4, 0)) {
                    width++;
                } else break;
            }
        } else return false;
        if (!checkPiece(MS_END, -4 - 2 * width, 4, 0) || !checkPiece(MS_END, 4 + 2 * width, 4, 0)) {
            return false;
        }

        for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
            if (glassTier < VoltageIndex.UMV & mEnergyHatch.mTier > glassTier) {
                return false;
            }
        }

        return casingAmount >= (91 + width * 20);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
                if (!inv.shouldBeCached()) {
                    return true;
                }

                if (dualInvWithPatternToRecipeCache.containsKey(inv)) {
                    activeDualInv = inv;
                    return true;
                }

                GTDualInputPattern inputs = inv.getPatternInputs();
                setInputItems(inputs.inputItems);
                setInputFluids(inputs.inputFluid);
                Set<GTRecipe> recipes = findRecipeMatches(RecipeMaps.fluidSolidifierRecipes)
                    .collect(Collectors.toSet());
                if (!recipes.isEmpty()) {
                    dualInvWithPatternToRecipeCache.put(inv, recipes);
                    activeDualInv = inv;
                    return true;
                }
                return false;
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                setSpeedBonus(1F / speedup);
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifier(0.8F);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        runningTickCounter++;
        if (runningTickCounter % 10 == 0 && speedup < 3) {
            runningTickCounter = 0;
            speedup += 0.025F;
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (mMaxProgresstime == 0 && speedup > 1) {
            if (aTick % 5 == 0) {
                speedup = (float) Math.max(1, speedup - DECAY_RATE);
            }
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return (BASE_PARALLELS + (width * PARALLELS_PER_WIDTH)) * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("speedup")) speedup = aNBT.getFloat("speedup");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("speedup", speedup);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setFloat("speedup", speedup);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + String.format("%.1f%%", 100 * tag.getFloat("speedup")));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Nonnull
    @Override
    protected CheckRecipeResult checkRecipeForCustomHatches(CheckRecipeResult lastResult) {
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                List<ItemStack> items = hatch.getNonConsumableItems();
                FluidStack fluid = solidifierHatch.getFluid();

                if (items != null && fluid != null) {
                    processingLogic.setInputItems(items);
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        lastResult = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        return lastResult;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
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
        }
        return false;
    }
}
