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
    private int tier = -1;
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
                                .or(gregtech.api.enums.HatchElement.Energy),
                            tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti
                                .or(gregtech.api.enums.HatchElement.Dynamo))
                        .casingIndex(179)
                        .hint(1)
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
            .addInfo("Needs field restriction coils to control the fatal radiation")
            .addInfo("Use higher tier coils to unlock more fuel types and perform more perfect overclocks")
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(3, 15, 15, false)
            .addController("Middle of the third layer")
            .addCasingInfoExactly("Naquadah Fuel Refinery Casing", 114, false)
            .addCasingInfoExactly("Field Restriction Coil", 32, true)
            .addCasingInfoExactly("Field Restriction Glass", 8, false)
            .addInputHatch("The casings adjacent to field restriction glass.")
            .addInputBus("The casings adjacent to field restriction glass.", 1)
            .addOutputHatch("The casings adjacent to field restriction glass.", 1)
            .addEnergyHatch("The casings adjacent to field restriction glass.", 1)
            .addDynamoHatch("The casings adjacent to field restriction glass.", 1)
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
        }.enablePerfectOverclock();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
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
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(StatCollector.translateToLocal("scanner.info.FRF") + " " + this.tier);
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
        return survivalBuildPiece(mName, stackSize, 7, 12, 1, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
