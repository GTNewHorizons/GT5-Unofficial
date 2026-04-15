package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.COKE_OVEN_CASING;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialCokeOven extends MTEExtendedPowerMultiBlockBase<MTEIndustrialCokeOven>
    implements ISurvivalConstructable {

    private int tier = 0;
    private int width = 0;
    private int casingAmount;
    private int casingAmount2;
    private int casingAmount3;
    private HeatingCoilLevel coilLevel;
    private static final int OFFSET_X_MAIN = 3;
    private static final int OFFSET_Y_MAIN = 7;
    private static final int OFFSET_Z_MAIN = 1;
    private static final int OFFSET_X_SLICE = -2;
    private static final int OFFSET_Y_SLICE = 7;
    private static final int OFFSET_Z_SLICE = 1;
    private static final String STRUCTURE_PIECE_FIRST = "main";
    private static final String STRUCTURE_PIECE_NEXT = "slice";

    public MTEIndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCokeOven(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCokeOven(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Coke Oven, ICO")
            .addInfo("Processes Logs and Coal into Charcoal and Coal Coke.")
            .addInfo(TooltipHelper.parallelText(18) + " Parallels with Heat Resistant Casings")
            .addInfo(TooltipHelper.parallelText(30) + " Parallels with Heat Proof Casings")
            .addDynamicEuEffInfo(0.04f, TooltipTier.VOLTAGE)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 11, 7, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Structural Coke Oven Casings", 8, false)
            .addCasingInfoExactly("Heat Resistant/Proof Coke Oven Casings", 8, false)
            .addInputBus("Any Structural Coke Oven Casing", 1)
            .addOutputBus("Any Structural Coke Oven Casing", 1)
            .addInputHatch("Any Structural Coke Oven Casing", 1)
            .addOutputHatch("Any Structural Coke Oven Casing", 1)
            .addEnergyHatch("Any Structural Coke Oven Casing", 1)
            .addMaintenanceHatch("Any Structural Coke Oven Casing", 1)
            .addMufflerHatch("Any Structural Coke Oven Casing", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addSubChannelUsage(GTStructureChannels.COKE_OVEN_CASING)
            .toolTipFinisher();
        return tt;
    }


    @Override
    public IStructureDefinition<MTEIndustrialCokeOven> getStructureDefinition() {
        IStructureDefinition<MTEIndustrialCokeOven> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialCokeOven>builder()
        .addShape(
            STRUCTURE_PIECE_FIRST,
            new String[][] {
                { "         ", "         ", "      D  ", "      D  ", "      D  ", "      D  ", "      D  ",
                    "      D  ", "      D  ", "      AAA", "AAAAAAAAA" },
                { "         ", "         ", "      E  ", "     ACA ", "     ACA ", "   AAACA ", "  AAAACA ",
                    "  A~AACA ", "  AAAACA ", "  AAAAAAA", "AAAAAAAAA" },
                { "         ", "         ", "      E  ", "     A A ", "   AAA A ", "  A  A A ", " A   A A ",
                    " A   A A ", " A   A A ", " A   AAAA", "AAAAAAAAA" },
                { "    BBB  ", "    B B  ", "    B B  ", "    BA A ", "   ABE E ", "  A  E E ", " A   E E ",
                    " A   E E ", " A   A A ", " A   AAAA", "AAAAAAAAA" },
                { "         ", "         ", "      E  ", "     A A ", "   AAA A ", "  A  A A ", " A   A A ",
                    " A   A A ", " A   A A ", " A   AAAA", "AAAAAAAAA" },
                { "         ", "         ", "      E  ", "     ACA ", "     ACA ", "   AAACA ", "  AAAACA ",
                    "  AAAACA ", "  AAAACA ", "  AAAAAAA", "AAAAAAAAA" },
                { "         ", "         ", "      D  ", "      D  ", "      D  ", "      D  ", "      D  ",
                    "      D  ", "      D  ", "      AAA", "AAAAAAAAA" } })
        .addShape(
            STRUCTURE_PIECE_NEXT,
            new String[][]{{
    "    ",
    "    ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " AAA",
    "AAAA"
},{
    "    ",
    "    ",
    " E  ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " AAA",
    " AAA"
},{
    "    ",
    "    ",
    " E  ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    " AAA",
    " AAA"
},{
    "BB  ",
    " B  ",
    " B  ",
    "  A ",
    "  E ",
    "  E ",
    "  E ",
    "  E ",
    "  A ",
    " AAA",
    " AAA"
},{
    "    ",
    "    ",
    " E  ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    "  A ",
    " AAA",
    " AAA"
},{
    "    ",
    "    ",
    " E  ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " CA ",
    " AAA",
    " AAA"
},{
    "    ",
    "    ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " D  ",
    " AAA",
    " AAA"
}})
        .addElement(
            'A',
            buildHatchAdder(MTEIndustrialCokeOven.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.SolidSteelMachineCasing.asElement())))
        .addElement('B', Casings.SteelPipeCasing.asElement())
        .addElement(
            'C',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEIndustrialCokeOven::setCoilLevel, MTEIndustrialCokeOven::getCoilLevel))))
        .addElement('D', ofFrame(Materials.Steel))
.addElement('E', GTStructureChannels.COKE_OVEN_CASING.use(
    ofBlocksTiered(
        (ITierConverter<Integer>) (block, meta) -> {
            if (block.equals(Casings.HeatResistantCokeOvenCasing.getBlock())) return 1;
            if (block.equals(Casings.HeatProofCokeOvenCasing.getBlock()))     return 2;
            return null;
        },
        new ArrayList<Pair<Block, Integer>>() {{
            add(Pair.of(Casings.HeatResistantCokeOvenCasing.getBlock(), Casings.HeatResistantCokeOvenCasing.getBlockMeta()));
            add(Pair.of(Casings.HeatProofCokeOvenCasing.getBlock(),     Casings.HeatProofCokeOvenCasing.getBlockMeta()));
        }},
        -1,
        (t, v) -> t.tier = v,
        t -> t.tier)))
        .build();
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_FIRST, stackSize, hintsOnly, OFFSET_X_MAIN, OFFSET_Y_MAIN, OFFSET_Z_MAIN);
        int extraSlices = stackSize.stackSize; // 1 = no extra, 2+ = expanded
        for (int i = 1; i <= extraSlices - 1; i++) {
            buildPiece(
                STRUCTURE_PIECE_NEXT,
                stackSize,
                hintsOnly,
                OFFSET_X_SLICE - (i * 2),
                OFFSET_Y_SLICE,
                OFFSET_Z_SLICE);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_FIRST,
            stackSize,
            OFFSET_X_MAIN,
            OFFSET_Y_MAIN,
            OFFSET_Z_MAIN,
            elementBudget,
            env,
            false,
            true);
        if (built >= 0) return built;
        int extraSlices = stackSize.stackSize;
        for (int i = 1; i <= extraSlices - 1; i++) {
            built = survivalBuildPiece(
                STRUCTURE_PIECE_NEXT,
                stackSize,
                OFFSET_X_SLICE - (i * 2),
                OFFSET_Y_SLICE,
                OFFSET_Z_SLICE,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }
        return -1;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        casingAmount2 = 0;
        casingAmount3 = 0;
        tier = 0;
        width = 0;
        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_FIRST, OFFSET_X_MAIN, OFFSET_Y_MAIN, OFFSET_Z_MAIN)) return false;

        // Try to find extra slices
        while (checkPiece(STRUCTURE_PIECE_NEXT, OFFSET_X_SLICE - (width + 1) * 2, OFFSET_Y_SLICE, OFFSET_Z_SLICE)) {
            width++;
        }

        if (casingAmount2 == 8 + width * 8) tier = 1;
        if (casingAmount3 == 8 + width * 8) tier = 2;

        return tier > 0 && casingAmount >= 8 && checkHatch();
    }

    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_OP_CLICK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCACokeOven)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCACokeOvenGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cokeOvenRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setEuModifier((100F - (GTUtility.getTier(getMaxInputVoltage()) * 4)) / 100F);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 + tier * 12) * (1 + width);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCokeOven;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    public int getCoilTier() {
        return this.coilLevel == null ? 0 : (int) this.coilLevel.getTier() + 1;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }
}
