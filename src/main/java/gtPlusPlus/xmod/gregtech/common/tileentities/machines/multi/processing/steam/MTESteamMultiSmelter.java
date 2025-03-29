package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasingsSteam;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_STEAM_SMELTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_STEAM_SMELTER_INACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasingsSteam;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamMultiSmelter extends MTEBetterSteamMultiBase<MTESteamMultiSmelter>
    implements ISurvivalConstructable {

    public MTESteamMultiSmelter(String aName) {
        super(aName);
    }

    public MTESteamMultiSmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Smelts up to 32 items at once")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "There is no way you can smelt the thousands of ingots you will need with")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "simple stone furnaces or steam ovens... Fortunately, the new metals you have")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "invented could easily allow you to meet these demands.")
            .addInfo("Author: " + AuthorSteamIsTheNumber)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public String getMachineType() {
        return "Steam Furnace";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (tInputList.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        int fakeOriginalMaxParallel = 1;
        OverclockCalculator calculator = new OverclockCalculator().setEUt(getAverageInputVoltage())
            .setAmperage(getMaxInputAmps())
            .setRecipeEUt(30)
            .setDuration(160)
            .setAmperageOC(mEnergyHatches.size() != 1)
            .setParallel(fakeOriginalMaxParallel);

        int maxParallel = 32;
        int originalMaxParallel = maxParallel;
        double tickTimeAfterOC = calculator.calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = GTUtility.safeInt((long) (maxParallel / tickTimeAfterOC), 0);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
        }

        // Calculate parallel
        int currentParallel = 0;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null) {
                if (item.stackSize <= (maxParallel - currentParallel)) {
                    currentParallel += item.stackSize;
                } else {
                    currentParallel = maxParallel;
                    break;
                }
            }
        }
        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);
        int fakeCurrentParallel = (int) Math.ceil((double) currentParallelBeforeBatchMode / originalMaxParallel);

        calculator.setCurrentParallel(fakeCurrentParallel)
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
            batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
        }
        int finalParallel = (int) (batchMultiplierMax * currentParallelBeforeBatchMode);

        // Consume inputs and generate outputs
        ArrayList<ItemStack> smeltedOutputs = new ArrayList<>();
        int remainingCost = finalParallel;
        for (ItemStack item : tInputList) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);
            if (smeltedOutput != null && remainingCost > 0) {
                if (remainingCost >= item.stackSize) {
                    remainingCost -= item.stackSize;
                    smeltedOutput.stackSize *= item.stackSize;
                    item.stackSize = 0;
                    smeltedOutputs.add(smeltedOutput);
                } else {
                    smeltedOutput.stackSize *= remainingCost;
                    item.stackSize -= remainingCost;
                    smeltedOutputs.add(smeltedOutput);
                    break;
                }
            }
        }
        this.mOutputItems = smeltedOutputs.toArray(new ItemStack[0]);

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
        this.lEUt = calculator.getConsumption();

        if (this.lEUt > 0) this.lEUt = -this.lEUt;

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_STEAM_SMELTER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_STEAM_SMELTER_ACTIVE)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_STEAM_SMELTER_INACTIVE)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)) };
        }
        return rTexture;
    }

    private static final IStructureDefinition<MTESteamMultiSmelter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESteamMultiSmelter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "ccc", "ccc" }, { "CCC", "C-C", "CCC" }, { "c~c", "ccc", "ccc" } }))
        .addElement(
            'c',
            ofChain(
                buildSteamInput(MTESteamMultiSmelter.class)
                    .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(8))
                    .dot(1)
                    .build(),
                buildHatchAdder(MTESteamMultiSmelter.class)
                    .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                    .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(8))
                    .dot(1)
                    .buildAndChain(),
                ofBlock(sBlockCasingsSteam, 8)))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasingsSteam, 7))
        .build();

    @Override
    public IStructureDefinition<MTESteamMultiSmelter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamMultiSmelter(this.mName);
    }
}
