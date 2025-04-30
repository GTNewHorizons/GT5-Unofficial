    package gregtech.common.tileentities.machines.multi;

    import bartworks.common.tileentities.multis.mega.MTEMegaBlastFurnace;
    import com.google.common.collect.ImmutableList;
    import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
    import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
    import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
    import com.gtnewhorizon.structurelib.structure.StructureDefinition;
    import gregtech.api.GregTechAPI;
    import gregtech.api.enums.HeatingCoilLevel;
    import gregtech.api.enums.Materials;
    import gregtech.api.interfaces.ITexture;
    import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
    import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
    import gregtech.api.logic.ProcessingLogic;
    import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
    import gregtech.api.recipe.RecipeMap;
    import gregtech.api.recipe.RecipeMaps;
    import gregtech.api.render.TextureFactory;
    import gregtech.api.util.GTUtility;
    import gregtech.api.util.GlassTier;
    import gregtech.api.util.MultiblockTooltipBuilder;
    import gregtech.common.blocks.BlockCasings8;
    import net.minecraft.block.Block;
    import net.minecraft.item.ItemStack;
    import net.minecraft.nbt.NBTTagCompound;
    import net.minecraft.util.EnumChatFormatting;
    import net.minecraftforge.common.util.ForgeDirection;
    import net.minecraftforge.fluids.FluidStack;
    import org.apache.commons.lang3.tuple.Pair;
    import tectech.util.FluidStackLong;
    import tectech.util.ItemStackLong;

    import javax.annotation.Nullable;
    import java.math.BigInteger;

    import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
    import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
    import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
    import static gregtech.api.enums.HatchElement.*;
    import static gregtech.api.enums.Textures.BlockIcons.*;
    import static gregtech.api.util.GTStructureUtility.*;

    public class MTEAdvancedChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEAdvancedChemicalReactor>
        implements ISurvivalConstructable {

        @Nullable
        private static Integer getFluidTierFromMeta(Block block, Integer metaID) {
            if (block != GregTechAPI.sBlockCasings2) return null;
            if (metaID < 12 || metaID > 15) return null;
            return metaID - 11;
        }
        private int leftModule = 0;
        private int rightModule = 0;
        private static final int MODULE_NONE = 0;
        private static final int MODULE_TEMP = 1;
        private static final int MODULE_PRESSURE = 2;

        private int glassTier = -1;

        private static final String STRUCTURE_PIECE_MAIN = "main";
        private static final String TEMPERATURE_MODULE_L = "temperatureL";
        private static final String TEMPERATURE_MODULE_R = "temperatureR";


        private static boolean isTempModule = false;
        private static boolean isPressureModule = false;

        private static final IStructureDefinition<MTEAdvancedChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
            .<MTEAdvancedChemicalReactor>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                // spotless:off
                new String[][]{{
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    "AA~AA"
                },{
                    "AAAAA",
                    "A   A",
                    "P   P",
                    "A   A",
                    "A   A",
                    "AAAAA"
                },{
                    "AAAAA",
                    "A   A",
                    "A   A",
                    "A   A",
                    "A   A",
                    "AAAAA"
                },{
                    "AAAAA",
                    "A   A",
                    "P   P",
                    "A   A",
                    "A   A",
                    "AAAAA"
                },{
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    " AAA ",
                    "AAAAA"
                }}
            )
            .addShape(
                TEMPERATURE_MODULE_L,
                new String[][]{{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                },{
                    " HH",
                    " H ",
                    " H ",
                    "ADA"
                },{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                },{
                    " HH",
                    " H ",
                    " H ",
                    "ADA"
                },{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                }}
            )
            .addShape(
                TEMPERATURE_MODULE_R,
                new String[][]{{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                },{
                    "HH ",
                    " H ",
                    " H ",
                    "AAA"
                },{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                },{
                    "HH ",
                    " H ",
                    " H ",
                    "AAA"
                },{
                    "   ",
                    "   ",
                    "   ",
                    "AAA"
                }}
                // spotless:on
            )
            .addElement('H', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
            .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
            .addElement('D', withChannel("coil", activeCoils(ofCoil(MTEAdvancedChemicalReactor::setCoilLevel, MTEAdvancedChemicalReactor::getCoilLevel))))
            .addElement(
                'A',
                buildHatchAdder(MTEAdvancedChemicalReactor.class)
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                    .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(GregTechAPI.sBlockCasings8, 0))
            .build();
        public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
            this.mCoilLevel = aCoilLevel;
        }
        public HeatingCoilLevel getCoilLevel() {
            return this.mCoilLevel;
        }
        private HeatingCoilLevel mCoilLevel;

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
            tt.addMachineType("Advanced Chemical Reactor")
                .addInfo("I have no idea what to type here")
                .beginStructureBlock(3, 5, 3, true)
                .addController("Front Center")
                .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
                .addCasingInfoExactly("Any Tiered Glass", 6, false)
                .addCasingInfoExactly("Steel Frame Box", 4, false)
                .addInputBus("Any Wooden Casing", 1)
                .addOutputBus("Any Wooden Casing", 1)
                .addInputHatch("Any Wooden Casing", 1)
                .addOutputHatch("Any Wooden Casing", 1)
                .addEnergyHatch("Any Wooden Casing", 1)
                .addMaintenanceHatch("Any Wooden Casing", 1)
                .addSubChannelUsage("glass", "Glass Tier")
                .toolTipFinisher(EnumChatFormatting.BLUE + "VorTex");
            return tt;
        }


        @Override
        public void construct(ItemStack stackSize, boolean hintsOnly) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 5, 0);
            buildPiece(TEMPERATURE_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
        }

        @Override
        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            if (mMachine) return -1;
            return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 5, 0, elementBudget, env, false, true);
        }

        private int mCasingAmount;

        private void onCasingAdded() {
            mCasingAmount++;
        }

        @Override
        public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
            if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0)) return false;

            isTempModule = checkPiece(TEMPERATURE_MODULE_R, -3, 3, 0) ||
                checkPiece(TEMPERATURE_MODULE_L, 5, 3, 0);
            return true;
        }
        protected int fluidPipeTier = 0;
        private void setFluidPipeTier(int tier) {
            fluidPipeTier = tier;
        }
        private int getFluidPipeTier() {
            return fluidPipeTier;
        }

        @Override
        protected void setProcessingLogicPower(ProcessingLogic logic) {
            logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
            logic.setAvailableAmperage(1L);
        }

        @Override
        protected ProcessingLogic createProcessingLogic() {
            return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
                .setMaxParallelSupplier(this::getTrueParallel);
        }

        @Override
        public int getMaxParallelRecipes() {
            return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
        }

        @Override
        public RecipeMap<?> getRecipeMap() {
            return RecipeMaps.chemicalReactorRecipes;
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

    }
