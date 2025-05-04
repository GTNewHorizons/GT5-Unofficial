package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.GregTechAPI.sBlockCoilACR;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
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
import gregtech.common.blocks.BlockCasings8;

/*
The problem in this code is that variables
tierCool, tierHeat, tierCompress, tierVacuum
are static, so these will be messed up when there
are a lot of ACRs, and because of this problem when
there are two modules, both of them become 0 tier

and  also the modules are not checked for some reason
so hatches on them won't be considered a part of the structure
 */


public class MTEAdvancedChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEAdvancedChemicalReactor>
    implements ISurvivalConstructable {

    private static final Logger log = LogManager.getLogger(MTEAdvancedChemicalReactor.class);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String HEAT_MODULE_L = "tempHeatL";
    private static final String HEAT_MODULE_R = "tempHeatR";
    private static final String COOL_MODULE_L = "tempCoolL";
    private static final String COOL_MODULE_R = "tempCoolR";
    private static final String VACUUM_MODULE_L = "VacuumL";
    private static final String VACUUM_MODULE_R = "VacuumR";
    private static final String COMPRESSION_MODULE_L = "PressureL";
    private static final String COMPRESSION_MODULE_R = "PressureR";

    private double CurrentTemp = 0;
    private double CurrentPressure = 0;

    protected int CoolCoilTier = 0;
    protected int VacuumCoilTier = 0;
    protected int HeatCoilTier = 0;
    protected int CompressCoilTier = 0;

    private boolean isHeatModule;
    private boolean isCoolModule;
    private boolean isVacuumModule;
    private boolean isCompressModule;

    private static final IStructureDefinition<MTEAdvancedChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvancedChemicalReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{
                {" AAA ", " AAA ", " AAA ", " AAA ", " AAA ", "AA~AA"},
                {"AAAAA", "A   A", "P   P", "A   A", "A   A", "AAAAA"},
                {"AAAAA", "A   A", "A   A", "A   A", "A   A", "AAAAA"},
                {"AAAAA", "A   A", "P   P", "A   A", "A   A", "AAAAA"},
                {" AAA ", " AAA ", " AAA ", " AAA ", " AAA ", "AAAAA"}}
        )
        .addShape(
            HEAT_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {" HH", " H ", " H ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {" HH", " H ", " H ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            HEAT_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {"HH ", " H ", " H ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {"HH ", " H ", " H ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            COOL_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {" CC", " C ", " C ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {" CC", " C ", " C ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            COOL_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {"CC ", " C ", " C ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {"CC ", " C ", " C ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            VACUUM_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {" VV", " V ", " V ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {" VV", " V ", " V ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            VACUUM_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {"VV ", " V ", " V ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {"VV ", " V ", " V ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            COMPRESSION_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {"KK ", " K ", " K ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {"KK ", " K ", " K ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )
        .addShape(
            COMPRESSION_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "BBB"},
                {" KK", " K ", " K ", "BDB"},
                {"   ", "   ", "   ", "BBB"},
                {" KK", " K ", " K ", "BDB"},
                {"   ", "   ", "   ", "BBB"}}
        )// spotless:on
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'D',
            buildHatchAdder(MTEAdvancedChemicalReactor.class).atLeast(InputHatch, OutputHatch)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'C',
            withChannel(
                "cool_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getCoolCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilACR, 0),
                        Pair.of(sBlockCoilACR, 1),
                        Pair.of(sBlockCoilACR, 2),
                        Pair.of(sBlockCoilACR, 3)),
                    -1,
                    (t, m) -> t.CoolCoilTier = m,
                    t -> t.CoolCoilTier)))
        .addElement(
            'H',
            withChannel(
                "heat_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getHeatCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilACR, 4),
                        Pair.of(sBlockCoilACR, 5),
                        Pair.of(sBlockCoilACR, 6),
                        Pair.of(sBlockCoilACR, 7)),
                    -1,
                    (t, m) -> t.HeatCoilTier = m,
                    t -> t.HeatCoilTier)))
        .addElement(
            'K',
            withChannel(
                "compress_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getCompressCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilACR, 8),
                        Pair.of(sBlockCoilACR, 9),
                        Pair.of(sBlockCoilACR, 10),
                        Pair.of(sBlockCoilACR, 11)),
                    -1,
                    (t, m) -> t.CompressCoilTier = m,
                    t -> t.CompressCoilTier)))
        .addElement(
            'V',
            withChannel(
                "vacuum_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getVacuumCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilACR, 12),
                        Pair.of(sBlockCoilACR, 13),
                        Pair.of(sBlockCoilACR, 14),
                        Pair.of(sBlockCoilACR, 15)),
                    -1,
                    (t, m) -> t.VacuumCoilTier = m,
                    t -> t.VacuumCoilTier)))
        .addElement(
            'A',
            buildHatchAdder(MTEAdvancedChemicalReactor.class)
                .atLeast(OutputHatch, InputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 0)))
        .build();

    public MTEAdvancedChemicalReactor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvancedChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEAdvancedChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvancedChemicalReactor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Advanced Chemical Reactor, ACR")
            .addInfo("I have no idea what to type here")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Chemically Inert Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Chemically Inert Casing", 1)
            .addOutputBus("Any Chemically Inert Casing", 1)
            .addInputHatch("Any Chemically Inert Casing", 1)
            .addOutputHatch("Any Chemically Inert Casing", 1)
            .addEnergyHatch("Any Chemically Inert Casing", 1)
            .addMaintenanceHatch("Any Chemically Inert Casing", 1)
            .toolTipFinisher(EnumChatFormatting.BLUE + "VorTex");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 5, 0);
        Pair<Integer, Integer> modules = create_modules(stackSize);
        int MODULE_LEFT = modules.getLeft();
        int MODULE_RIGHT = modules.getRight();

        switch (MODULE_LEFT) {
            case 1 -> buildPiece(HEAT_MODULE_L, stackSize, hintsOnly, L_module_offset, V_module_offset, 0);
            case 2 -> buildPiece(COOL_MODULE_L, stackSize, hintsOnly, L_module_offset, V_module_offset, 0);
            case 3 -> buildPiece(COMPRESSION_MODULE_L, stackSize, hintsOnly, L_module_offset, V_module_offset, 0);
            case 4 -> buildPiece(VACUUM_MODULE_L, stackSize, hintsOnly, L_module_offset, V_module_offset, 0);
        }
        switch (MODULE_RIGHT) {
            case 1 -> buildPiece(HEAT_MODULE_R, stackSize, hintsOnly, R_module_offset, V_module_offset, 0);
            case 2 -> buildPiece(COOL_MODULE_R, stackSize, hintsOnly, R_module_offset, V_module_offset, 0);
            case 3 -> buildPiece(COMPRESSION_MODULE_R, stackSize, hintsOnly, R_module_offset, V_module_offset, 0);
            case 4 -> buildPiece(VACUUM_MODULE_R, stackSize, hintsOnly, R_module_offset, V_module_offset, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine && (create_modules(stackSize).getLeft() == 0) && (create_modules(stackSize).getRight() == 0))
            return -1;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 5, 0, elementBudget, env, false, true);
        Pair<Integer, Integer> modules = create_modules(stackSize);
        switch (modules.getLeft()) {
            case 1 -> built += survivialBuildPiece(
                HEAT_MODULE_L,
                stackSize,
                L_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                COOL_MODULE_L,
                stackSize,
                L_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_L,
                stackSize,
                L_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_L,
                stackSize,
                L_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        switch (modules.getRight()) {
            case 1 -> built += survivialBuildPiece(
                HEAT_MODULE_R,
                stackSize,
                R_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                COOL_MODULE_R,
                stackSize,
                R_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_R,
                stackSize,
                R_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_R,
                stackSize,
                R_module_offset,
                V_module_offset,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        return built;
    }

    public Pair<Integer, Integer> create_modules(ItemStack stackSize) {
        /*
         * 0 - none
         * 1 - heat
         * 2 - freeze
         * 3 - compress
         * 4 - pump
         */
        int MODULE_LEFT = 0;
        int MODULE_RIGHT = 0;
        boolean COOL_PIPE = false;
        boolean HEAT_PIPE = false;
        boolean VACUUM_PIPE = false;
        boolean COMPRESS_PIPE = false;
        if (stackSize.getTagCompound() != null) {
            if (stackSize.getTagCompound()
                .getCompoundTag("channels") != null) {
                HEAT_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("heat_pipe") > 0;
                COOL_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("cool_pipe") > 0;
                COMPRESS_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("compress_pipe") > 0;
                VACUUM_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("vacuum_pipe") > 0;
            }
        }
        // right
        if (HEAT_PIPE) MODULE_LEFT = 1;
        if (COOL_PIPE && (MODULE_LEFT == 0)) MODULE_LEFT = 2;
        if (COMPRESS_PIPE && (MODULE_LEFT == 0)) MODULE_LEFT = 3;
        if (VACUUM_PIPE && (MODULE_LEFT == 0)) MODULE_LEFT = 4;
        if (MODULE_LEFT != 0) {
            MODULE_RIGHT = MODULE_LEFT;
            MODULE_LEFT = 0;
        }
        // left
        if (HEAT_PIPE && !(MODULE_RIGHT == 1)) MODULE_LEFT = 1;
        if (COOL_PIPE && (MODULE_LEFT == 0) && !(MODULE_RIGHT == 2)) MODULE_LEFT = 2;
        if (COMPRESS_PIPE && (MODULE_LEFT == 0) && !(MODULE_RIGHT == 3)) MODULE_LEFT = 3;
        if (VACUUM_PIPE && (MODULE_LEFT == 0) && !(MODULE_RIGHT == 4)) MODULE_LEFT = 4;
        return Pair.of(MODULE_LEFT, MODULE_RIGHT);
    }

    int V_module_offset = 3;
    int L_module_offset = 5;
    int R_module_offset = -3;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        isHeatModule = false;
        isCoolModule = false;
        isVacuumModule = false;
        isCompressModule = false;

        isHeatModule = checkPiece(HEAT_MODULE_R, R_module_offset, V_module_offset, 0) || checkPiece(HEAT_MODULE_L, L_module_offset, V_module_offset, 0);
        isCoolModule = checkPiece(COOL_MODULE_R, R_module_offset, V_module_offset, 0) || checkPiece(COOL_MODULE_L, L_module_offset, V_module_offset, 0);
        isVacuumModule = checkPiece(VACUUM_MODULE_R, R_module_offset, V_module_offset, 0) || checkPiece(VACUUM_MODULE_L, L_module_offset, V_module_offset, 0);
        isCompressModule = checkPiece(COMPRESSION_MODULE_R, R_module_offset, V_module_offset, 0) || checkPiece(COMPRESSION_MODULE_L, L_module_offset, V_module_offset, 0);
        int moduleCount = 0;
        if (isHeatModule) moduleCount++;
        if (isCoolModule) moduleCount++;
        if (isVacuumModule) moduleCount++;
        if (isCompressModule) moduleCount++;
        boolean mainStructureValid = checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0);
        if (moduleCount == 0) {
            return mainStructureValid;
        } else if (moduleCount == 1) {
            return mainStructureValid && (isHeatModule || isCoolModule || isVacuumModule || isCompressModule);
        } else return mainStructureValid;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockAdvancedChemicalReactorRecipes;
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

    @Nullable
    public static Integer getCoolCoilMeta(Block block, int meta) {
        if (block == sBlockCoilACR) {
            switch (meta) {
                case 0: return 1;
                case 1: return 2;
                case 2: return 3;
                case 3: return 4;
                default: return 0;
            }
        }
        return null;
    }

    @Nullable
    public static Integer getHeatCoilMeta(Block block, int meta) {
        if (block == sBlockCoilACR) {
            switch (meta) {
                case 4: return 1;
                case 5: return 2;
                case 6: return 3;
                case 7: return 4;
                default: return 0;
            }
        }
        return null;
    }

    @Nullable
    public static Integer getCompressCoilMeta(Block block, int meta) {
        if (block == sBlockCoilACR) {
            switch (meta) {
                case 8: return 1;
                case 9: return 2;
                case 10: return 3;
                case 11: return 4;
                default: return 0;
            }
        }
        return null;
    }

    @Nullable
    public static Integer getVacuumCoilMeta(Block block, int meta) {
        if (block == sBlockCoilACR) {
            switch (meta) {
                case 12: return 1;
                case 13: return 2;
                case 14: return 3;
                case 15: return 4;
                default: return 0;
            }
        }
        return null;
    }

    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.ACR.pressure") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(this.CurrentPressure)
                + EnumChatFormatting.RESET
                + " kPa",
            StatCollector.translateToLocal("GT5U.ACR.temperature") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(this.CurrentTemp)
                + EnumChatFormatting.RESET
                + " K" };
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (mMachine && (aTick % 20 == 0)) {
            if (isHeatModule) {
                CurrentTemp = 0;
            } else CurrentTemp = 300;

            if (isCompressModule) {
                CurrentPressure = 0;
            } else CurrentPressure = 101000;
        }
    }
}
