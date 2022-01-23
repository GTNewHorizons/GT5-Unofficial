package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureElement;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_CubicMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;

public class GT_MetaTileEntity_ImplosionCompressor extends GT_MetaTileEntity_CubicMultiBlockBase<GT_MetaTileEntity_ImplosionCompressor> {
    public GT_MetaTileEntity_ImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "gt.recipe.implosioncompressor");
    }

    public GT_MetaTileEntity_ImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ImplosionCompressor(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Implosion Compressor")
                .addInfo("Explosions are fun")
                .addInfo("Controller block for the Implosion Compressor")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 3, 3, true)
                .addController("Front center")
                .addCasingInfo("Solid Steel Machine Casing", 16)
                .addStructureInfo("Casings can be replaced with Explosion Warning Signs")
                .addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1)
                .addMufflerHatch("Any casing", 1)
                .addInputBus("Any casing", 1)
                .addOutputBus("Any casing", 1)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || addMufflerToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[]{
                    BlockIcons.casingTexturePages[0][16],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                    BlockIcons.casingTexturePages[0][16],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][16]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ImplosionCompressor.png");
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS=tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                    if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                        tInputList.remove(j--); tInputList_sS=tInputList.size();
                    } else {
                        tInputList.remove(i--); tInputList_sS=tInputList.size();
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
        if (!tInputList.isEmpty()) {
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
            if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, null, tInputs))) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                //OC THAT EXPLOSIVE SHIT!!!
                calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, getMaxInputVoltage());
                //In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0), tRecipe.getOutput(1)};
                sendLoopStart((byte) 20);
                updateSlots();
                return true;
            }
        }
        return false;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(5), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    protected IStructureElement<GT_MetaTileEntity_CubicMultiBlockBase<?>> getCasingElement() {
        return ofChain(ofBlock(GregTech_API.sBlockCasings2, 0), ofBlock(GregTech_API.sBlockCasings3, 4));
    }

    @Override
    protected int getHatchTextureIndex() {
        return 16;
    }

    @Override
    protected int getRequiredCasingCount() {
        return 16;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack){
        return GT_Mod.gregtechproxy.mPollutionImplosionCompressorPerSecond;
    }
    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
