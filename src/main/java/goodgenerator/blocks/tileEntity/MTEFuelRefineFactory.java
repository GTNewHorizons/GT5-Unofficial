package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;

public class MTEFuelRefineFactory extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

    private IStructureDefinition<MTEFuelRefineFactory> multiDefinition = null;
    private int Tier = -1;
    private static final Block[] coils = new Block[] { Loaders.FRF_Coil_1, Loaders.FRF_Coil_2, Loaders.FRF_Coil_3,
        Loaders.FRF_Coil_4 };

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
        structureBuild_EM(mName, 7, 12, 1, itemStack, hintsOnly);
    }

    @Override
    public IStructureDefinition<MTEFuelRefineFactory> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEFuelRefineFactory>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "               ", "      CCC      ", "               " },
                            { "      XGX      ", "    CCFFFCC    ", "      XGX      " },
                            { "    CC   CC    ", "   CFFCCCFFC   ", "    CC   CC    " },
                            { "   C       C   ", "  CFCC   CCFC  ", "   C       C   " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { " X           X ", "CFC         CFC", " X           X " },
                            { " G           G ", "CFC         CFC", " G           G " },
                            { " X           X ", "CFC         CFC", " X           X " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "  C         C  ", " CFC       CFC ", "  C         C  " },
                            { "   C       C   ", "  CFCC   CCFC  ", "   C       C   " },
                            { "    CC   CC    ", "   CFFC~CFFC   ", "    CC   CC    " },
                            { "      XGX      ", "    CCFFFCC    ", "      XGX      " },
                            { "               ", "      CCC      ", "               " } }))
                .addElement(
                    'X',
                    buildHatchAdder(MTEFuelRefineFactory.class)
                        .atLeast(
                            gregtech.api.enums.HatchElement.Maintenance,
                            gregtech.api.enums.HatchElement.InputHatch,
                            gregtech.api.enums.HatchElement.InputBus,
                            gregtech.api.enums.HatchElement.OutputHatch,
                            tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti
                                .or(gregtech.api.enums.HatchElement.Energy))
                        .casingIndex(179)
                        .dot(1)
                        .buildAndChain(ofBlock(Loaders.FRF_Casings, 0)))
                .addElement('C', ofBlock(Loaders.FRF_Casings, 0))
                .addElement('G', ofBlock(Loaders.fieldRestrictingGlass, 0))
                .addElement(
                    'F',
                    ofBlocksTiered(
                        fieldCoilTierConverter(),
                        getAllFieldCoilTiers(),
                        -1,
                        MTEFuelRefineFactory::setCoilTier,
                        MTEFuelRefineFactory::getCoilTier))
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
        this.Tier = tier;
    }

    private int getCoilTier() {
        return this.Tier;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Naquadah Fuel Refinery")
            .addInfo("But at what cost?")
            .addInfo("Produces naquadah fuels.")
            .addInfo("Needs field restriction coils to control the fatal radiation.")
            .addInfo("Use higher tier coils to unlock more fuel types and perform more overclocks.")
            .addInfo(StatCollector.translateToLocal("GT5U.machines.perfectoc.tooltip"))
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 15, 15, false)
            .addController("Mid of the third layer")
            .addCasingInfoExactly("Naquadah Fuel Refinery Casing", 114, false)
            .addCasingInfoExactly("Field Restriction Coil", 32, true)
            .addCasingInfoExactly("Field Restriction Glass", 8, false)
            .addInputHatch("The casings adjacent to field restriction glass.")
            .addInputBus("The casings adjacent to field restriction glass.", 1)
            .addOutputHatch("The casings adjacent to field restriction glass.", 1)
            .addEnergyHatch("The casings adjacent to field restriction glass.", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.Tier = aNBT.getInteger("mTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTier", this.Tier);
        super.saveNBTData(aNBT);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("FuelRefineFactory.hint", 8);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        Tier = -1;
        return structureCheck_EM(mName, 7, 12, 1);
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
                if (recipe.mSpecialValue > Tier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                int overclockAmount = Tier - recipe.mSpecialValue;
                return super.createOverclockCalculator(recipe).limitOverclockCount(overclockAmount);
            }
        }.enablePerfectOverclock();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFuelRefineFactory(this.mName);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = new String[super.getInfoData().length + 1];
        System.arraycopy(super.getInfoData(), 0, infoData, 0, super.getInfoData().length);
        infoData[super.getInfoData().length] = StatCollector.translateToLocal("scanner.info.FRF") + " " + this.Tier;
        return infoData;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        batchMode = !batchMode;
        if (batchMode) {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
        } else {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
        }
        return true;
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
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179),
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE), TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179),
                new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE), TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(179) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 7, 12, 1, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
