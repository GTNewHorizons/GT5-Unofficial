package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class FuelRefineFactory extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    @SideOnly(Side.CLIENT)
    protected String name;
    private IStructureDefinition<FuelRefineFactory> multiDefinition = null;
    private int Tire = -1;
    private final HashSet<Coords> vis = new HashSet<>(64);

    public FuelRefineFactory(String name){super(name);}

    public FuelRefineFactory(int id, String name, String nameRegional){
        super(id,name,nameRegional);
        this.name = name;
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
        structureBuild_EM(name, 7,12,1, hintsOnly, itemStack);
    }

    @Override
    public IStructureDefinition<FuelRefineFactory> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<FuelRefineFactory>builder()
                    .addShape(name,
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
                        Tire = j;
                        return true;
                    }
            }
        return false;
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
        return structureCheck_EM(name, 7,12,1) && checkCoil();
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack){
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
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
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
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL),new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)};
            return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL),new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)};
        }
        return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL)};
    }
}
