package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
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

public class MTEAdvancedChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEAdvancedChemicalReactor>
    implements ISurvivalConstructable {

    private static final Logger log = LogManager.getLogger(MTEAdvancedChemicalReactor.class);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String TEMP_HEAT_MODULE_L = "tempHeatL";
    private static final String TEMP_HEAT_MODULE_R = "tempHeatR";
    private static final String TEMP_COOL_MODULE_L = "tempCoolL";
    private static final String TEMP_COOL_MODULE_R = "tempCoolR";
    private static final String VACUUM_MODULE_L = "VacuumL";
    private static final String VACUUM_MODULE_R = "VacuumR";
    private static final String COMPRESSION_MODULE_L = "PressureL";
    private static final String COMPRESSION_MODULE_R = "PressureR";

    private boolean isbuilt = false;
    private static boolean isTempModule = false;
    private static boolean isPressureModule = false;

    private double CurrentTemp = 0;
    private double CurrentPressure = 0;

    protected int CoolCoilTier = 0;
    protected int VacuumCoilTier = 0;
    protected int HeatCoilTier = 0;
    protected int CompressCoilTier = 0;

    private int getHeatCoilTier() {
        return HeatCoilTier;
    }

    private int getCoolCoilTier() {
        return CoolCoilTier;
    }

    private int getVacuumCoilTier() {
        return VacuumCoilTier;
    }

    private int getCompressCoilTier() {
        return CompressCoilTier;
    }

    private void setHeatCoilTier(int tier) {
        HeatCoilTier = tier;
    }

    private void setCoolCoilTier(int tier) {
        CoolCoilTier = tier;
    }

    private void setVacuumCoilTier(int tier) {
        VacuumCoilTier = tier;
    }

    private void setCompressCoilTier(int tier) {
        CompressCoilTier = tier;
    }

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
            TEMP_HEAT_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {" HH", " H ", " H ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {" HH", " H ", " H ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            TEMP_HEAT_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {"HH ", " H ", " H ", "AAA"},
                {"   ", "   ", "   ", "AAA"},
                {"HH ", " H ", " H ", "AAA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            TEMP_COOL_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {" CC", " C ", " C ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {" CC", " C ", " C ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            TEMP_COOL_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {"CC ", " C ", " C ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {"CC ", " C ", " C ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            VACUUM_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {" VV", " V ", " V ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {" VV", " V ", " V ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            VACUUM_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {"VV ", " V ", " V ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {"VV ", " V ", " V ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            COMPRESSION_MODULE_R,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {"KK ", " K ", " K ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {"KK ", " K ", " K ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )
        .addShape(
            COMPRESSION_MODULE_L,
            new String[][]{
                {"   ", "   ", "   ", "AAA"},
                {" KK", " K ", " K ", "ADA"},
                {"   ", "   ", "   ", "AAA"},
                {" KK", " K ", " K ", "ADA"},
                {"   ", "   ", "   ", "AAA"}}
        )// spotless:on
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement(
            'D',
            buildHatchAdder(MTEAdvancedChemicalReactor.class).atLeast(InputHatch, OutputHatch)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(1))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 1))
        .addElement(
            'C',
            withChannel(
                "cool_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getCoolCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 0),
                        Pair.of(GregTechAPI.sBlockCoilACR, 1),
                        Pair.of(GregTechAPI.sBlockCoilACR, 2),
                        Pair.of(GregTechAPI.sBlockCoilACR, 3)),
                    -1,
                    MTEAdvancedChemicalReactor::setCoolCoilTier,
                    MTEAdvancedChemicalReactor::getCoolCoilTier)))
        .addElement(
            'H',
            withChannel(
                "heat_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getHeatCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 4),
                        Pair.of(GregTechAPI.sBlockCoilACR, 5),
                        Pair.of(GregTechAPI.sBlockCoilACR, 6),
                        Pair.of(GregTechAPI.sBlockCoilACR, 7)),
                    -1,
                    MTEAdvancedChemicalReactor::setHeatCoilTier,
                    MTEAdvancedChemicalReactor::getHeatCoilTier)))
        .addElement(
            'K',
            withChannel(
                "compress_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getCompressCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 8),
                        Pair.of(GregTechAPI.sBlockCoilACR, 9),
                        Pair.of(GregTechAPI.sBlockCoilACR, 10),
                        Pair.of(GregTechAPI.sBlockCoilACR, 11)),
                    -1,
                    MTEAdvancedChemicalReactor::setCompressCoilTier,
                    MTEAdvancedChemicalReactor::getCompressCoilTier)))
        .addElement(
            'V',
            withChannel(
                "vacuum_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getVacuumCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 12),
                        Pair.of(GregTechAPI.sBlockCoilACR, 13),
                        Pair.of(GregTechAPI.sBlockCoilACR, 14),
                        Pair.of(GregTechAPI.sBlockCoilACR, 15)),
                    -1,
                    MTEAdvancedChemicalReactor::setVacuumCoilTier,
                    MTEAdvancedChemicalReactor::getVacuumCoilTier)))
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
            case 1 -> buildPiece(TEMP_HEAT_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
            case 2 -> buildPiece(TEMP_COOL_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
            case 3 -> buildPiece(COMPRESSION_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
            case 4 -> buildPiece(VACUUM_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
        }
        switch (MODULE_RIGHT) {
            case 1 -> buildPiece(TEMP_HEAT_MODULE_R, stackSize, hintsOnly, -3, 3, 0);
            case 2 -> buildPiece(TEMP_COOL_MODULE_R, stackSize, hintsOnly, -3, 3, 0);
            case 3 -> buildPiece(COMPRESSION_MODULE_R, stackSize, hintsOnly, -3, 3, 0);
            case 4 -> buildPiece(VACUUM_MODULE_R, stackSize, hintsOnly, -3, 3, 0);
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
                TEMP_HEAT_MODULE_L,
                stackSize,
                5,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                TEMP_COOL_MODULE_L,
                stackSize,
                5,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_L,
                stackSize,
                5,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_L,
                stackSize,
                5,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        switch (modules.getRight()) {
            case 1 -> built += survivialBuildPiece(
                TEMP_HEAT_MODULE_R,
                stackSize,
                -3,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                TEMP_COOL_MODULE_R,
                stackSize,
                -3,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_R,
                stackSize,
                -3,
                3,
                0,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_R,
                stackSize,
                -3,
                3,
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

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            return (mExoticEnergyHatches.size() == 1);
        }

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0)) {
            isbuilt = false;
            return false;
        }
        isbuilt = true;
        // spotless:off
        isTempModule =
            checkPiece(TEMP_HEAT_MODULE_R, -3, 3, 0)
            || checkPiece(TEMP_HEAT_MODULE_L, 5, 3, 0)
            || checkPiece(TEMP_COOL_MODULE_R, -3, 3, 0)
            || checkPiece(TEMP_COOL_MODULE_L, 5, 3, 0);
        isPressureModule =
            checkPiece(VACUUM_MODULE_R, -3, 3, 0)
            || checkPiece(VACUUM_MODULE_L, 5, 3, 0)
            || checkPiece(COMPRESSION_MODULE_R, -3, 3, 0)
            || checkPiece(COMPRESSION_MODULE_L, 5, 3, 0);
        //spotless:on
        return true;
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
    private static Integer getCoolCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID > 3) return null;
        return metaID + 1;
    }

    @Nullable
    private static Integer getHeatCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID < 4 || metaID > 7) return null;
        return metaID - 3;
    }

    @Nullable
    private static Integer getCompressCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID < 8 || metaID > 11) return null;
        return metaID - 7;
    }

    @Nullable
    private static Integer getVacuumCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID < 12) return null;
        return metaID - 11;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (isbuilt && (aTick % 20 == 0)) {
            if (isTempModule) {
                CurrentTemp = 0;
            } else CurrentTemp = 300;

            if (isPressureModule) {
                CurrentPressure = 0;
            } else CurrentPressure = 101;
            System.out.println(CurrentPressure);
            System.out.println(CurrentTemp);
        }
    }
}
