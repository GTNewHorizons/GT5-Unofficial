package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import GoodGenerator.util.MyRecipeAdder;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class FuelRefineFactory extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<FuelRefineFactory> multiDefinition = null;
    private int Tier = -1;
    private final HashSet<Coords> vis = new HashSet<>(64);

    public FuelRefineFactory(String name){super(name);}

    public FuelRefineFactory(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 7,12,1, hintsOnly, itemStack);
    }

    @Override
    public IStructureDefinition<FuelRefineFactory> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<FuelRefineFactory>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"               ","      CCC      ","               "},
                                    {"      XGX      ","    CCFFFCC    ","      XGX      "},
                                    {"    CC   CC    ","   CFFCCCFFC   ","    CC   CC    "},
                                    {"   C       C   ","  CFCC   CCFC  ","   C       C   "},
                                    {"  C         C  "," CFC       CFC ","  C         C  "},
                                    {"  C         C  "," CFC       CFC ","  C         C  "},
                                    {" X           X ","CFC         CFC"," X           X "},
                                    {" G           G ","CFC         CFC"," G           G "},
                                    {" X           X ","CFC         CFC"," X           X "},
                                    {"  C         C  "," CFC       CFC ","  C         C  "},
                                    {"  C         C  "," CFC       CFC ","  C         C  "},
                                    {"   C       C   ","  CFCC   CCFC  ","   C       C   "},
                                    {"    CC   CC    ","   CFFC~CFFC   ","    CC   CC    "},
                                    {"      XGX      ","    CCFFFCC    ","      XGX      "},
                                    {"               ","      CCC      ","               "}
                            })
                    ).addElement(
                        'X',
                            ofChain(
                                    ofHatchAdder(
                                            FuelRefineFactory::addToFRFList,60,
                                            Loaders.FRF_Casings,0
                                    ),
                                    ofBlock(
                                            Loaders.FRF_Casings,0
                                    )
                            )
                    ).addElement(
                            'C',
                            ofBlockAnyMeta(
                                    Loaders.FRF_Casings
                            )
                    ).addElement(
                            'G',
                            ofBlockAnyMeta(
                                    Loaders.fieldRestrictingGlass
                            )
                    ).addElement(
                            'F',
                            ofChain(
                                    ofBlock(
                                            Loaders.FRF_Coil_1,0
                                    ),
                                    ofBlock(
                                            Loaders.FRF_Coil_2,0
                                    ),
                                    ofBlock(
                                            Loaders.FRF_Coil_2,0
                                    ),
                                    ofBlockHint(
                                            Loaders.FRF_Coil_1,0
                                    )

                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    //In fact, this check method can't check structure correctly...
    public boolean checkCoil(){
        IGregTechTileEntity aTile = this.getBaseMetaTileEntity();
        Block[] block = {Loaders.FRF_Coil_1,Loaders.FRF_Coil_2,Loaders.FRF_Coil_3};
        int[] dx = {1,-1,0,0,0,0};
        int[] dy = {0,0,1,-1,0,0};
        int[] dz = {0,0,0,0,1,-1};
        for (int i = 0; i < 6; i ++)
            for (int j = 0; j < 3; j ++){
                vis.clear();
                if (aTile.getBlockOffset(dx[i],dy[i],dz[i]) == block[j])
                    if (dfs(block[j], aTile.getWorld(),aTile.getXCoord() + dx[i],aTile.getYCoord() + dy[i],aTile.getZCoord() + dz[i],32)){
                        Tier = j + 1;
                        return true;
                    }
            }
        return false;
    }

    @Override
    public String[] getDescription(){
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Naquadah Fuel Refine Factory")
                .addInfo("But at what cost?")
                .addInfo("Produce the endgame naquadah fuel.")
                .addInfo("Need field restriction coil to control the fatal radiation.")
                .addInfo("Use higher tier coil to unlock more fuel and reduce the process time.")
                .addInfo("The structure is too complex!")
                .addInfo("Follow the TecTech blueprint to build the main structure.")
                .addSeparator()
                .addController("Front bottom")
                .addInputHatch("The casings adjoin the field restriction glass.")
                .addInputBus("The casings adjoin the field restriction glass.")
                .addOutputHatch("The casings adjoin the field restriction glass.")
                .addEnergyHatch("The casings adjoin the field restriction glass.")
                .toolTipFinisher("Good Generator");;
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getInformation();
        } else {
            return tt.getStructureInformation();
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack){
        return new String[]{
                "8x Field Restriction Glass",
                "32x Field Restriction Coil of any tier",
                "At least 104x Naquadah Fuel Refine Factory Casing",
                "1~16x Input Hatch",
                "1~16x Output Hatch",
                "1~16x Input Bus",
                "1~16x Energy Hatch"
        };
    }

    public boolean dfs(Block e, World w,int x,int y,int z, int cnt){
        if (cnt == 0) return true;
        if (w.getBlock(x,y,z) != e) return false;
        int[] dx = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int[] dy = { 1, 1, 1,-1,-1,-1, 0, 0, 1, 1, 1,-1,-1,-1, 0, 0, 0, 1, 1, 1,-1,-1,-1, 0, 0, 0};
        int[] dz = { 1, 0,-1, 1, 0,-1, 1,-1, 1, 0,-1, 1, 0,-1, 1, 0,-1, 1, 0,-1, 1, 0,-1, 1, 0,-1};
        vis.add(new Coords(x,y,z,w.provider.dimensionId));
        for (int i = 0; i < 26; i ++){
            if (vis.contains(new Coords(x + dx[i],y + dy[i], z + dz[i],w.provider.dimensionId))) continue;
            if (dfs(e,w,x + dx[i],y + dy[i], z + dz[i], cnt - 1)) return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
        return structureCheck_EM(mName, 7,12,1) && checkCoil();
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack){

        ArrayList<FluidStack> tFluids = getStoredFluids();
        ArrayList<ItemStack> tItems = getStoredInputs();
        Collection<GT_Recipe> tRecipes = MyRecipeAdder.instance.FRF.mRecipeList;
        long maxVoltage = getMaxInputVoltage();

        for (int i = 0; i < tFluids.size() - 1; i++) {
            for (int j = i + 1; j < tFluids.size(); j++) {
                if (GT_Utility.areFluidsEqual(tFluids.get(i), tFluids.get(j))) {
                    if ((tFluids.get(i)).amount >= (tFluids.get(j)).amount) {
                        tFluids.remove(j--);
                    } else {
                        tFluids.remove(i--);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < tItems.size() - 1; i++) {
            for (int j = i + 1; j < tItems.size(); j++) {
                if (GT_Utility.areStacksEqual(tItems.get(i), tItems.get(j))) {
                    if ((tItems.get(i)).stackSize >= (tItems.get(j)).stackSize) {
                        tItems.remove(j--);
                    } else {
                        tItems.remove(i--);
                        break;
                    }
                }
            }
        }

        FluidStack[] inFluids = tFluids.toArray(new FluidStack[tFluids.size()]);
        ItemStack[] inItems = tItems.toArray(new ItemStack[tItems.size()]);

        for (GT_Recipe recipe : tRecipes){
            checkCoil();
            if (recipe.mSpecialValue > Tier) continue;
            if (recipe.isRecipeInputEqual(true, inFluids, inItems) && recipe.mEUt <= maxVoltage){
                mEUt = recipe.mEUt;
                mMaxProgresstime = recipe.mDuration / (1 << (Tier - recipe.mSpecialValue));
                this.mOutputFluids = recipe.mFluidOutputs;
                this.updateSlots();
                return true;
            }
        }
        return false;
    }

    public final boolean addToFRFList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
                    ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                }
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                    return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                    return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                    return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                    return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
                    return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti)aMetaTileEntity);
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new FuelRefineFactory(this.mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(48), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW).glow().build()};
            return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(48), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW).glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(48)};
    }
}
