package com.elisis.gtnhlanth.common.tileentity;

import static com.elisis.gtnhlanth.util.DescTextLocalization.BLUEPRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;

import com.elisis.gtnhlanth.loader.RecipeAdder;
import com.elisis.gtnhlanth.util.DescTextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;


public class Digester extends GT_MetaTileEntity_EnhancedMultiBlockBase<Digester> implements IConstructable {
	
    protected int casingAmount = 0;
    protected int height = 0;
    
    private HeatingCoilLevel heatLevel;
    
	private IStructureDefinition<Digester> multiDefinition = StructureDefinition.<Digester>builder()
    		.addShape(mName, transpose(new String[][] {
    			{"-------", "-ttttt-", "-t---t-", "-t---t-", "-t---t-", "-ttttt-", "-------"},
    			{"--ttt--", "-t---t-", "t-----t", "t-----t", "t-----t", "-t---t-", "--ttt--"},
    			{"-tccct-", "tc---ct", "c-----c", "c-----c", "c-----c", "tc---ct", "-tccct-"},
    			{"-tt~tt-", "thhhhht", "thsssht", "thsssht", "thsssht", "thhhhht", "-ttttt-"},
    			
    			
    			
    			
    			
    		}))
    		.addElement('t', ofChain(
    					ofHatchAdder(Digester::addInputToMachineList, 47, 1),
    					ofHatchAdder(Digester::addOutputToMachineList, 47, 1),
    					ofHatchAdder(Digester::addEnergyInputToMachineList, 47, 1),
    					ofHatchAdder(Digester::addMaintenanceToMachineList, 47, 1),
    					ofHatchAdder(Digester::addMufflerToMachineList, 47, 1),
    					ofBlock(GregTech_API.sBlockCasings4, 0)
    		))
    		.addElement('h', ofBlock(GregTech_API.sBlockCasings1, 11))
    		.addElement('s', ofBlock(GregTech_API.sBlockCasings4, 1))
    		.addElement('c', ofCoil(Digester::setCoilLevel, Digester::getCoilLevel))
    		.build();
    		

    //private int mHeat;
    //private int mNeededHeat;
    

    public Digester(String name) {
    	super(name);
    }

	public Digester(int id, String name, String nameRegional) {
		super(id, name, nameRegional);
	}
	
	@Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(mName, 3, 3, 0);
    }
	
	@Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }
	
	public HeatingCoilLevel getCoilLevel() {
		return this.heatLevel;
	}
	
	public void setCoilLevel(HeatingCoilLevel level) {
		this.heatLevel = level;	
	}
	
	@Override
	public boolean checkRecipe(ItemStack itemStack) {
		
		ArrayList<FluidStack> tFluidInputs = this.getStoredFluids();
		FluidStack[] tFluidInputArray = tFluidInputs.toArray(new FluidStack[0]);
		ArrayList<ItemStack> tItems = this.getStoredInputs();
		long tVoltage = this.getMaxInputVoltage();
		
		//Collection<GT_Recipe> tRecipes = RecipeAdder.instance.DigesterRecipes.mRecipeList;
		GT_Recipe tRecipe = RecipeAdder.instance.DigesterRecipes.findRecipe(
				this.getBaseMetaTileEntity(), 
				this.doTickProfilingInThisTick, 
				tVoltage, 
				this.mOutputFluids,
				this.mInventory
			);
		
		if (tRecipe == null)
			return false;
		
		if (tRecipe.isRecipeInputEqual(true, tFluidInputArray, mInventory[1])) {
			
			this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;
			this.calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
			
			if (mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
				return false;
			
			if (tRecipe.mSpecialValue > this.getCoilLevel().getHeat())
				return false;
			
			if (this.mEUt > 0)
				this.mEUt = (-this.mEUt);
			
			this.mOutputFluids = new FluidStack[] {
					tRecipe.getFluidOutput(0)
			};
			return true;
		}
		return false;
	}
	
	@Override
	public int getMaxEfficiency(ItemStack itemStack) {
		return 10000;
	}
	
	@Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 200;
    }
	
	@Override
    public Object getClientGUI(int id, InventoryPlayer playerInventory, IGregTechTileEntity metaTileEntity) {
        return new GT_GUIContainer_MultiMachine(playerInventory, metaTileEntity, getLocalName(), "Digester.png");
    }
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
		return new Digester(this.mName);
	}

	@Override
	public void construct(ItemStack itemStack, boolean b) {
		buildPiece(mName,itemStack, b, 3, 3, 0);
		
	}

	@Override
	public String[] getStructureDescription(ItemStack arg0) {
		return DescTextLocalization.addText("UniversalChemicalFuelEngine.hint", 11);
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity arg0, byte arg1, byte arg2, byte arg3, boolean arg4,
			boolean arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Digester")
                .addInfo("Controller block for the Digester")
                .addInfo("Input ores and fluid, output water.")
                .addInfo("You will need to balance the heat provided")
                .addInfo("to be able to complete the recipe.")
                .addInfo(BLUEPRINT_INFO)
                .addSeparator()
                .addController("Front bottom")
                .addInputHatch("Hint block with dot 1")
                .addInputBus("Hint block with dot 1")
                .addOutputHatch("Hint block with dot 2")
                .addOutputBus("Hint block with dot 2")
                .addMaintenanceHatch("Hint block with dot 2")
                .addOtherStructurePart("Neutron Accelerator", "Hint block with dot 2")
                .addOtherStructurePart("Neutron Sensor", "Hint block with dot 2")
                .addCasingInfo("Clean Stainless Steel Machine Casing", 7)
                .toolTipFinisher("GTNH: Lanthanides");
        return tt;
	}

	@Override
	public IStructureDefinition<Digester> getStructureDefinition() {
		return multiDefinition;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDamageToComponent(ItemStack arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
