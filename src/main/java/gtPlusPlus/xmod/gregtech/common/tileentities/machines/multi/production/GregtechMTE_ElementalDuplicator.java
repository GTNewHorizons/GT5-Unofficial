package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GregtechMTE_ElementalDuplicator extends GregtechMeta_MultiBlockBase<GregtechMTE_ElementalDuplicator> {

	private ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder> mReplicatorDataOrbHatches = new ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder>();
	private static final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 3);
	private int mCasing = 0;

	public GregtechMTE_ElementalDuplicator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_ElementalDuplicator(final String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_ElementalDuplicator(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Replicator";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {

		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Produces Elemental Material from UU Matter")
				.addInfo("This multiblock cannot be overclocked")
				.addInfo("Maximum 1x of each bus/hatch.")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(7, 7, 7, false)
				.addController("Top Center")
				.addCasingInfo("Robust Tungstensteel Machine Casing", 40)
				.addCasingInfo("Tungstensteel Coils", 16)
				.addCasingInfo("Molecular Containment Casing", 52)
				.addCasingInfo("High Voltage Current Capacitor", 32)
				.addCasingInfo("Particle Containment Casing", 4)
				.addCasingInfo("Resonance Chamber I", 5)
				.addCasingInfo("Modulator I", 4)
				.addInputBus("Any Robust Tungstensteel Machine Casing", 1)
				.addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
				.addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
				.addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
				.addMufflerHatch("Any Robust Tungstensteel Machine Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}
	

	private static final String STRUCTURE_PIECE_MAIN = "main";
	private IStructureDefinition<GregtechMTE_ElementalDuplicator> STRUCTURE_DEFINITION = null;
	
	@Override
	public IStructureDefinition<GregtechMTE_ElementalDuplicator> getStructureDefinition() {
		STRUCTURE_DEFINITION = null;
		log("Reset Structure Def");
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_ElementalDuplicator>builder()
					
					// h = Hatch
					// c = Casing
					
					// a = MF Casing 1
					// b = Matter Gen Coil
					
					// d = Current Capacitor
					// e = Particle
					
					// f = Resonance III
					// g = Modulator III
					
					.addShape(STRUCTURE_PIECE_MAIN, (new String[][]{
						{"   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccch~hccc", "ccchhhccc", " ccccccc ", "  ccccc  ", "   ccc   "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
						{"         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         "},
					}))						

					
					//.addElement('c', ofBlock(getCasingBlock(), getCasingMeta()))
					.addElement('c', lazy(t -> onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
					.addElement('h', lazy(t -> ofChain(
							ofHatchAdder(GregtechMTE_ElementalDuplicator::addGenericHatch, getCasingTextureIndex(), 1),
							onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock3(), getTungstenCasingMeta()))
							)))
					.build();	
		}
		return STRUCTURE_DEFINITION;
	}
	
	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(STRUCTURE_PIECE_MAIN , stackSize, hintsOnly, 3, 3, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0);
		if (this.mInputBusses.size() != 1 || this.mOutputBusses.size() != 1 || this.mEnergyHatches.size() != 1) {
			return false;
		}
		return aDidBuild && mCasing >= 40 && checkHatch();
	}	

	protected static int getCasingTextureIndex() {
		return CASING_TEXTURE_ID;
	}

	protected static Block getCasingBlock() {
		return ModBlocks.blockCasings5Misc;
	}
	
	protected static Block getCasingBlock2() {
		return ModBlocks.blockSpecialMultiCasings2;
	}
	
	protected static Block getCasingBlock3() {
		return GregTech_API.sBlockCasings4;
	}
	
	protected static Block getCoilBlock() {
		return GregTech_API.sBlockCasings5;
	}
	
	protected static int getCasingMeta() {
		return 3;
	}
	
	protected static int getCasingMeta2() {
		return 12;
	}
	
	protected static int getCasingMeta3() {
		return 13;
	}
	
	protected static int getTungstenCasingMeta() {
		return 0;
	}

	public final boolean addGenericHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} 
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) {
	            ((GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) aTileEntity).mRecipeMap = getRecipeMap();            
	            return addToMachineListInternal(mReplicatorDataOrbHatches, aMetaTileEntity, aBaseCasingIndex);
	        } 
		}
		log("Bad Hatch");
		return false;
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
					new GT_RenderedTexture((IIconContainer) (aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced))};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}
	
	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "VacuumFreezer";
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {				
		return GTPP_Recipe_Map.sMolecularTransformerRecipes;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(1, 100, 100);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiMolecularTransformer;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
	
	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);		
		// Fix GT bug
		/*if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1); 
		}*/
	}
	
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mReplicatorDataOrbHatches.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
    
    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_ElementalDataOrbHolder tHatch : mReplicatorDataOrbHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {                
                tItems.addAll(tHatch.getInventory());                
            }
        }        
        return tItems;
    }

}