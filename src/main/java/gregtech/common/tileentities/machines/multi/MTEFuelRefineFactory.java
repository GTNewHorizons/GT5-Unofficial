package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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

public class MTEFuelRefineFactory extends TTMultiblockBase implements ISurvivalConstructable {

    private IStructureDefinition<MTEFuelRefineFactory> multiDefinition = null;
    private int tier = -1;
    private static final Block[] coils = new Block[] { Loaders.FRF_Coil_1, Loaders.FRF_Coil_2, Loaders.FRF_Coil_3,
        Loaders.FRF_Coil_4 };

    private static final int OFFSET_X = 13;
    private static final int OFFSET_Y = 13;
    private static final int OFFSET_Z = 0;

    public MTEFuelRefineFactory(String name) {
        super(name);
        useLongPower = true;
    }

    public MTEFuelRefineFactory(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        useLongPower = true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, OFFSET_X, OFFSET_Y, OFFSET_Z, itemStack, hintsOnly);
    }

    @Override
    public IStructureDefinition<MTEFuelRefineFactory> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEFuelRefineFactory>builder()
                .addShape(
                    mName,
                    new String[][] {
                        { "                           ", "          AAAAAAA          ", "        AA       AA        ",
                            "      AA           AA      ", "     A               A     ", "    A                 A    ",
                            "   A                   A   ", "   A                   A   ", "  A                     A  ",
                            "  A                     A  ", " A           A           A ", " A          AAA          A ",
                            " A         AAAAA         A ", " A        AAA~AAA        A ", " A         AAAAA         A ",
                            " A          AAA          A ", " A           A           A ", "  A                     A  ",
                            "  A                     A  ", "   A                   A   ", "   A                   A   ",
                            "    A                 A    ", "     A               A     ", "      AA           AA      ",
                            "        AA       AA        ", "          AAAAAAA          ",
                            "                           " },
                        { "          AAAAAAA          ", "        AACCCCCCCAA        ", "      AACCEAAAAAECCAA      ",
                            "     ACCEE   A   EECCA     ", "    AFEE     A     EEFA    ", "   AFE               EFA   ",
                            "  ACE                 ECA  ", "  ACE                 ECA  ", " ACE                   ECA ",
                            " ACE         A         ECA ", "ACE         A A         ECA", "ACA        A   A        ACA",
                            "ACA       A     A       ACA", "ACAAA    A       A    AAACA", "ACA       A     A       ACA",
                            "ACA        A   A        ACA", "ACE         A A         ECA", " ACE         A         ECA ",
                            " ACE                   ECA ", "  ACE                 ECA  ", "  ACE                 ECA  ",
                            "   AFE               EFA   ", "    AFEE     A     EEFA    ", "     ACCEE   A   EECCA     ",
                            "      AACCEAAAAAECCAA      ", "        AACCCCCCCAA        ",
                            "          AAAAAAA          " },
                        { "          CCCCCCC          ", "        CCBBBBBBBCC        ", "      CCBBAAABAAABBCC      ",
                            "     CBBEE FAFAF EEBBC     ", "    CBEE   FAFAF   EEBC    ", "   CBE     F A F     EBC   ",
                            "  CBE        A        EBC  ", "  CBE        A        EBC  ", " CBE         A         EBC ",
                            " CBE        AFA        EBC ", "CBA        AF FA        ABC", "CBAFFF    AF   FA    FFFABC",
                            "CBAAA    AF     FA    AAABC", "CBBFFAAAAF       FAAAAFFBBC", "CBAAA    AF     FA    AAABC",
                            "CBAFFF    AF   FA    FFFABC", "CBA        AF FA        ABC", " CBE        AFA        EBC ",
                            " CBE         A         EBC ", "  CBE        A        EBC  ", "  CBE        A        EBC  ",
                            "   CBE     F A F     EBC   ", "    CBEE   FAFAF   EEBC    ", "     CBBEE FAFAF EEBBC     ",
                            "      CCBBAAABAAABBCC      ", "        CCBBBBBBBCC        ",
                            "          CCCCCCC          " },
                        { "          AAAAAAA          ", "        AACCCCCCCAA        ", "      AACCEAAAAAECCAA      ",
                            "     ACCEE   A   EECCA     ", "    AFEE     A     EEFA    ", "   AFE               EFA   ",
                            "  ACE                 ECA  ", "  ACE                 ECA  ", " ACE                   ECA ",
                            " ACE         A         ECA ", "ACE         A A         ECA", "ACA        A   A        ACA",
                            "ACA       A     A       ACA", "ACAAA    A       A    AAACA", "ACA       A     A       ACA",
                            "ACA        A   A        ACA", "ACE         A A         ECA", " ACE         A         ECA ",
                            " ACE                   ECA ", "  ACE                 ECA  ", "  ACE                 ECA  ",
                            "   AFE               EFA   ", "    AFEE     A     EEFA    ", "     ACCEE   A   EECCA     ",
                            "      AACCEAAAAAECCAA      ", "        AACCCCCCCAA        ",
                            "          AAAAAAA          " },
                        { "                           ", "          AAAAAAA          ", "        AA       AA        ",
                            "      AA           AA      ", "     A               A     ", "    A                 A    ",
                            "   A                   A   ", "   A                   A   ", "  A                     A  ",
                            "  A                     A  ", " A           A           A ", " A          AAA          A ",
                            " A         AAAAA         A ", " A        AAAAAAA        A ", " A         AAAAA         A ",
                            " A          AAA          A ", " A           A           A ", "  A                     A  ",
                            "  A                     A  ", "   A                   A   ", "   A                   A   ",
                            "    A                 A    ", "     A               A     ", "      AA           AA      ",
                            "        AA       AA        ", "          AAAAAAA          ",
                            "                           " } })
                .addElement(
                    'A',
                    buildHatchAdder(MTEFuelRefineFactory.class)
                        .atLeast(
                            Maintenance,
                            InputHatch,
                            InputBus,
                            OutputHatch,
                            EnergyMulti.or(Energy),
                            DynamoMulti.or(Dynamo))
                        .casingIndex(179)
                        .hint(1)
                        .buildAndChain(ofBlock(Loaders.FRF_Casings, 0)))
                .addElement('C', ofBlock(Loaders.fieldRestrictingGlass, 0))
                .addElement(
                    'B',
                    ofBlocksTiered(
                        fieldCoilTierConverter(),
                        getAllFieldCoilTiers(),
                        -1,
                        MTEFuelRefineFactory::setCoilTier,
                        MTEFuelRefineFactory::getCoilTier))
                .addElement('D', Casings.SuperconductingCoilBlock.asElement())
                .addElement('E', Casings.EuropiumReinforcedRadiationProofMachineCasing.asElement())
                .addElement('F', ofBlock(Loaders.radiationProtectionSteelFrame, 0))
                .build();
        }
        return multiDefinition;
    }

    public static ITierConverter<Integer> fieldCoilTierConverter() {
        return (block, meta) -> {
            for (int i = 0; i < coils.length; i++) {
                if (block.equals(coils[i])) {
                    return i + 1;
                }
            }
            return null;
        };
    }

    public static List<Pair<Block, Integer>> getAllFieldCoilTiers() {
        ArrayList<Pair<Block, Integer>> tiers = new ArrayList<>();
        for (Block coil : coils) {
            tiers.add(Pair.of(coil, 0));
        }
        return tiers;
    }

    private void setCoilTier(int tier) {
        this.tier = tier;
    }

    private int getCoilTier() {
        return this.tier;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Naquadah Fuel Refinery, NFR")
            .addInfo("Produces naquadah fuels")
            .addInfo(
                "Gains " + TooltipHelper.parallelText(4) + " Parallels per " + EnumChatFormatting.WHITE + "Coil Tier")
            .addInfo("Needs field restriction coils to control the fatal radiation")
            .addInfo("Use higher tier coils to unlock more fuel types and perform more perfect overclocks")
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(5, 27, 27, false)
            .addController("Front center")
            .addCasingInfoExactly("Naquadah Fuel Refinery Casing", 483, false)
            .addCasingInfoExactly("Field Restriction Coil", 72, true)
            .addCasingInfoExactly("Field Restriction Glass", 192, false)
            .addCasingInfoExactly("Radiation Proof Steel Frame Box", 64, false)
            .addCasingInfoExactly("Europium Reinforced Radiation Proof Machine Casing", 124, false)
            .addMaintenanceHatch("Any Naquadah Fuel Refinery Casing", 1)
            .addInputHatch("Any Naquadah Fuel Refinery Casing", 1)
            .addInputBus("Any Naquadah Fuel Refinery Casing", 1)
            .addOutputHatch("Any Naquadah Fuel Refinery Casing", 1)
            .addEnergyHatch("Any Naquadah Fuel Refinery Casing", 1)
            .addDynamoHatch("Any Naquadah Fuel Refinery Casing", 1)
            .addStructureAuthors("GregTech Odyssey")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.tier = aNBT.getInteger("mTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTier", this.tier);
        super.saveNBTData(aNBT);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("FuelRefineFactory.hint", 8);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tier = -1;
        return structureCheck_EM(mName, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > tier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setMaxOverclocks(tier - recipe.mSpecialValue);
            }
        }.enablePerfectOverclock()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return tier * 4;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        super.setProcessingLogicPower(logic);
        logic.setUnlimitedTierSkips();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFuelRefineFactory(this.mName);
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = new String[super.getInfoData().length + 1];
        System.arraycopy(super.getInfoData(), 0, infoData, 0, super.getInfoData().length);
        infoData[super.getInfoData().length] = StatCollector.translateToLocal("scanner.info.FRF") + " " + this.tier;
        return infoData;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
