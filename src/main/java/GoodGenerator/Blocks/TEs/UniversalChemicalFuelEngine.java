package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;

public class UniversalChemicalFuelEngine extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<UniversalChemicalFuelEngine> multiDefinition = null;

    public UniversalChemicalFuelEngine(String name){super(name);}

    public UniversalChemicalFuelEngine(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    public final boolean addMaintenance(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addMuffler(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addInputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<UniversalChemicalFuelEngine> getStructure_EM(){
        if (multiDefinition == null){
            multiDefinition = StructureDefinition
                    .<UniversalChemicalFuelEngine>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"TTTTT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTTTT"},
                                    {"TTTTT","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","TTTTT"},
                                    {"TT~TT","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","TTETT"},
                                    {"TTWTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT"}
                            })
                    ).addElement(
                            'T',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings4, 2
                            )
                    ).addElement(
                            'W',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addMaintenance, 50,
                                     1
                            )
                    ).addElement(
                            'M',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addMuffler, 50,
                                    2
                            )
                    ).addElement(
                            'S',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addInputHatch, 50,
                                    3
                            )
                    ).addElement(
                            'E',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addDynamoHatch, 50,
                                    4
                            )
                    ).addElement(
                            'P',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings2, 14
                            )
                    ).addElement(
                            'C',
                            ofBlockAnyMeta(
                                    Loaders.titaniumPlatedCylinder
                            )
                    ).addElement(
                            'G',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings2, 4
                            )
                    ).addElement(
                            'I',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings4, 13
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 2, 2, 0);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        structureBuild_EM(mName, 2, 2, 0, b, itemStack);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone){
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW).glow().build()
            };
            return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW).glow().build()
            };
        }
        return new ITexture[]{casingTexturePages[0][50]};
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new UniversalChemicalFuelEngine(this.mName);
    }
}
