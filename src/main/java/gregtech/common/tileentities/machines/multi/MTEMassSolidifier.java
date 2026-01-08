package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorOmdaCZ;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

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
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
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
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMassSolidifier extends MTEExtendedPowerMultiBlockBase<MTEMassSolidifier>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_T1 = "t1";
    private static final String STRUCTURE_PIECE_T2 = "t2";
    private static final double DECAY_RATE = 0.025;
    private static final int HORIZONTAL_OFFSET = 1;
    private static final int VERTICAL_OFFSET = 2;
    private static final int DEPTH_OFFSET = 0;

    private int tier = 1;
    private float speedup = 1;
    private int runningTickCounter = 0;

    private static final IStructureDefinition<MTEMassSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMassSolidifier>builder()
        .addShape(
            STRUCTURE_PIECE_T1,
            // spotless:off
            new String[][]{{
                "BBB",
                "BBB",
                "B~B",
                "BBB",
                "C C"
            },{
                "BBB",
                "A A",
                "A A",
                "BBB",
                "   "
            },{
                "BBB",
                "BAB",
                "BAB",
                "BBB",
                "C C"
            }})
        .addShape(
            STRUCTURE_PIECE_T2,
            // spotless:off
            new String[][]{{
                "BBB",
                "BBB",
                "B~B",
                "BBB",
                "D D"
            },{
                "BBB",
                "A A",
                "A A",
                "BBB",
                "   "
            },{
                "BBB",
                "BAB",
                "BAB",
                "BBB",
                "D D"
            }})
        //spotless:on
        .addElement(
            'B',
            buildHatchAdder(MTEMassSolidifier.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(15))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEMassSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 15))))
        .addElement('A', chainAllGlasses())
        .addElement('C', ofFrame(Materials.Steel))
        .addElement('D', ofFrame(Materials.Naquadah))
        .build();

    public MTEMassSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMassSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEMassSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMassSolidifier(this.mName);
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
        tt.addMachineType("Fluid Solidifier, MS")
            .addInfo(
                "Gains " + TooltipHelper.parallelText(8)
                    + " parallels per "
                    + TooltipHelper.tierText(TooltipTier.VOLTAGE)
                    + " tier")
            .addInfo("Structure can be upgraded to " + TooltipHelper.parallelText("double") + " parallels per tier")
            .addInfo("Speeds up to a maximum of " + TooltipHelper.speedText(3f))
            .addInfo("Decays at double the rate that it speeds up at")
            .addStaticEuEffInfo(0.8f)
            .addGlassEnergyLimitInfo(VoltageIndex.UMV)
            .addInfo(EnumChatFormatting.BLUE + "Pretty Ⱄⱁⰾⰻⰴ, isn't it")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(AuthorOmdaCZ);
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("multiTier", tier);
        aNBT.setFloat("speedup", speedup);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tier = aNBT.getInteger("multiTier");
        if (aNBT.hasKey("speedup")) speedup = aNBT.getFloat("speedup");
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
    public void construct(ItemStack holoStack, boolean hintsOnly) {
        if (holoStack.stackSize == 1) {
            buildPiece(STRUCTURE_PIECE_T1, holoStack, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
        }
        if (holoStack.stackSize >= 2) {
            buildPiece(STRUCTURE_PIECE_T2, holoStack, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
        }
    }

    @Override
    public int survivalConstruct(ItemStack holoStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (holoStack.stackSize == 1) {
            return survivalBuildPiece(
                STRUCTURE_PIECE_T1,
                holoStack,
                HORIZONTAL_OFFSET,
                VERTICAL_OFFSET,
                DEPTH_OFFSET,
                elementBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize >= 2) {
            return survivalBuildPiece(
                STRUCTURE_PIECE_T2,
                holoStack,
                HORIZONTAL_OFFSET,
                VERTICAL_OFFSET,
                DEPTH_OFFSET,
                elementBudget,
                env,
                false,
                true);
        }
        return 0;
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tier = 1;
        casingAmount = 0;
        // check tier
        if (checkPiece(STRUCTURE_PIECE_T2, 1, 2, 0)) {
            tier = 2;
            return casingAmount >= 14;
        }

        resetParameters();

        if (checkPiece(STRUCTURE_PIECE_T1, 1, 2, 0)) {
            return casingAmount >= 14;
        }

        return false;
    }

    private void resetParameters() {
        clearHatches();
        casingAmount = 0;
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
        }.setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
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
    public int getMaxParallelRecipes() {
        return (tier * 8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
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

}
