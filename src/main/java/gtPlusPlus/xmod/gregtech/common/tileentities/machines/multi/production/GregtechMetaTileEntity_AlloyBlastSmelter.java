package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_AlloyBlastSmelter extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_AlloyBlastSmelter> implements ISurvivalConstructable {

    private int mMode = 0;
    private boolean isUsingControllerCircuit = false;
    private static Item circuit;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_AlloyBlastSmelter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_AlloyBlastSmelter(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_AlloyBlastSmelter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Fluid Alloy Cooker";
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("isBussesSeparate")) {
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Alloy Blast Smelter")
                .addInfo("20% Faster than the Electric Blast Furnace")
                .addInfo("Allows Complex GT++ alloys to be created").addInfo("Accepts only one Energy Hatch")
                .addInfo("Circuit for recipe goes in the Input Bus or GUI slot")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 4, 3, true)
                .addController("Bottom Center").addCasingInfoMin("Blast Smelter Casings", 5, false)
                .addCasingInfoMin("Blast Smelter Heat Containment Coils", 16, false).addInputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_AlloyBlastSmelter>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_AlloyBlastSmelter.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(15)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 15))))
                    .addElement('H', ofBlock(ModBlocks.blockCasingsMisc, 14)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 3, 0) && mCasing >= 5 && mEnergyHatches.size() == 1 && checkHatch();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(15);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.alloyBlastSmelterRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        if (this.getBaseMetaTileEntity().isServerSide()) {
            // Get Controller Circuit
            if (circuit == null) {
                circuit = CI.getNumberedCircuit(0).getItem();
            }
            if (aStack != null && aStack.getItem() == circuit) {
                this.mMode = aStack.getItemDamage();
                return this.isUsingControllerCircuit = true;
            } else {
                if (aStack == null) {
                    this.isUsingControllerCircuit = false;
                    return true; // Allowed empty
                }
                Logger.WARNING("Not circuit in GUI inputs.");
                return this.isUsingControllerCircuit = false;
            }
        }
        Logger.WARNING("No Circuit, clientside.");
        return this.isUsingControllerCircuit = false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiABS;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
