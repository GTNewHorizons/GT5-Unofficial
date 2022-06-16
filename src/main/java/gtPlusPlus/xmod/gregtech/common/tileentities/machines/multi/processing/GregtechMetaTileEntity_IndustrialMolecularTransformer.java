package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
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
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntity_IndustrialMolecularTransformer extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialMolecularTransformer> {

	private static final int CASING_TEXTURE_ID = 48;
	private int mCasing = 0;

	public GregtechMetaTileEntity_IndustrialMolecularTransformer(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialMolecularTransformer(final String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialMolecularTransformer(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Molecular Transformer";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {

		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Changes the structure of items to produce new ones")
				.addInfo("Speed: 100% | Eu Usage: 100%")
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
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialMolecularTransformer> STRUCTURE_DEFINITION = null;
	
	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialMolecularTransformer> getStructureDefinition() {
		STRUCTURE_DEFINITION = null;
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialMolecularTransformer>builder()
					.addShape(STRUCTURE_PIECE_MAIN, (new String[][]{
						{"       ", "       ", "  xxx  ", "  x~x  ", "  xxx  ", "       ", "       "},
						{"       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       "},
						{"       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       "},
						{"       ", "  xxx  ", " xyyyx ", " xyzyx ", " xyyyx ", "  xxx  ", "       "},
						{"   t   ", " ttxtt ", " tyyyt ", "txyzyxt", " tyyyt ", " ttxtt ", "   t   "},						
						{"   c   ", " ccecc ", " cxfxc ", "cefefec", " cxfxc ", " ccecc ", "   c   "},						
						{"   h   ", " hhhhh ", " hhhhh ", "hhhhhhh", " hhhhh ", " hhhhh ", "   h   "},
					}))						

					.addElement('x', ofBlock(getCasingBlock(), getCasingMeta()))
					.addElement('y', ofBlock(getCasingBlock(), getCasingMeta2()))
					.addElement('z', ofBlock(getCasingBlock(), getCasingMeta3()))
					.addElement('e', ofBlock(getCasingBlock2(), 0))
					.addElement('f', ofBlock(getCasingBlock2(), 4))
					.addElement('c', ofBlock(getCoilBlock(), 3))
					.addElement('t', lazy(t -> onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock3(), getTungstenCasingMeta()))))
					.addElement('h', lazy(t -> ofChain(
							ofHatchAdder(GregtechMetaTileEntity_IndustrialMolecularTransformer::addGenericHatch, getCasingTextureIndex(), 1),
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
		return ModBlocks.blockSpecialMultiCasings;
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
		return 11;
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
		}
		log("Bad Hatch");
		return false;
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44),
					new GT_RenderedTexture((IIconContainer) (aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced))};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44)};
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
		if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1); 
		}
	}
       
}
