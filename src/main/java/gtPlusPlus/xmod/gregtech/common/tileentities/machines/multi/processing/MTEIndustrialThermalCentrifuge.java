package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialThermalCentrifuge extends GTPPMultiBlockBase<MTEIndustrialThermalCentrifuge>
    implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTEIndustrialThermalCentrifuge> STRUCTURE_DEFINITION = null;

    public MTEIndustrialThermalCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialThermalCentrifuge(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialThermalCentrifuge(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Thermal Centrifuge, LTR";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addBulkMachineInfo(8, 2.5f, 0.8f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 2, 3, false)
            .addController("Front Center")
            .addCasingInfoMin("Thermal Processing Casings/Noise Hazard Sign Blocks", 8, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialThermalCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialThermalCentrifuge>builder()
                .addShape(mName, transpose(new String[][] { { "C~C", "CCC", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEIndustrialThermalCentrifuge.class)
                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                            .casingIndex(getCasingTextureIndex())
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0)),
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTechAPI.sBlockCasings3, 9))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 0, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 0, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 0, 0) && mCasing >= 8 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialThermalCentrifugeActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialThermalCentrifugeActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialThermalCentrifuge;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialThermalCentrifugeGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingTextureIndex();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.thermalCentrifugeRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 2.5F)
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialThermalCentrifuge;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_THERMAL_CENTRIFUGE_LOOP;
    }
}
