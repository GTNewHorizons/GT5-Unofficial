package GoodGenerator.Blocks.TEs;

import GoodGenerator.Blocks.TEs.MetaTE.NeutronAccelerator;
import GoodGenerator.Blocks.TEs.MetaTE.NeutronSensor;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static GoodGenerator.util.StructureHelper.addFrame;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class NeutronActivator extends GT_MetaTileEntity_MultiblockBase_EM {

    protected IStructureDefinition<NeutronActivator> multiDefinition = null;
    protected final ArrayList<NeutronAccelerator> mNeutronAccelerator = new ArrayList<>();
    protected final ArrayList<NeutronSensor> mNeutronSensor = new ArrayList<>();
    protected int casingAmount = 0;
    protected int KeV;

    public NeutronActivator(String name) {
        super(name);
    }

    public NeutronActivator(int id, String name, String nameRegional) {
        super(id,name,nameRegional);
    }

    @Override
    public IStructureDefinition<NeutronActivator> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<NeutronActivator>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"CCCCC","CDDDC","CDDDC","CDDDC","CCCCC"},
                                    {"F   F"," GGG "," GPG "," GGG ","F   F"},
                                    {"F   F"," GGG "," GPG "," GGG ","F   F"},
                                    {"F   F"," GGG "," GPG "," GGG ","F   F"},
                                    {"F   F"," GGG "," GPG "," GGG ","F   F"},
                                    {"XX~XX","XDDDX","XDDDX","XDDDX","XXXXX"},
                            })
                    )
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(
                                            NeutronActivator::addClassicInputToMachineList, 49,
                                            1
                                    ),
                                    onElementPass(
                                            x -> x.casingAmount ++,
                                            ofBlock(
                                                    GregTech_API.sBlockCasings4, 1
                                            )
                                    )
                            )
                    )
                    .addElement(
                            'D',
                            ofBlock(
                                    GregTech_API.sBlockCasings2, 6
                            )
                    )
                    .addElement(
                            'F',
                            addFrame(
                                    Materials.Steel
                            )
                    )
                    .addElement(
                            'G',
                            ofBlock(
                                    Block.getBlockFromItem(Ic2Items.reinforcedGlass.getItem()), 0
                            )
                    )
                    .addElement(
                            'P',
                            ofBlock(
                                    GregTech_API.sBlockCasings2, 13
                            )
                    )
                    .addElement(
                            'X',
                            ofChain(
                                    ofHatchAdder(
                                            NeutronActivator::addClassicOutputToMachineList, 49,
                                            2
                                    ),
                                    ofHatchAdder(
                                            NeutronActivator::addMaintenanceToMachineList, 49,
                                            2
                                    ),
                                    ofHatchAdder(
                                            NeutronActivator::addAccelerator, 49,
                                            2
                                    ),
                                    onElementPass(
                                            x -> x.casingAmount ++,
                                            ofBlock(
                                                    GregTech_API.sBlockCasings4, 1
                                            )
                                    )
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmount = 0;
        this.mNeutronAccelerator.clear();
        this.mNeutronSensor.clear();
        return structureCheck_EM(mName, 2, 5, 0) && casingAmount >= 7;
    }

    public final boolean addAccelerator(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof NeutronAccelerator){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronAccelerator.add((NeutronAccelerator)aMetaTileEntity);
            } else if (aMetaTileEntity instanceof NeutronSensor){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronSensor.add((NeutronSensor)aMetaTileEntity);
            }
        }
        return false;
    }

    public int maxNeutronKineticEnergy() {
        return 10000000;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new NeutronActivator(this.mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        boolean anyWorking = false;
        if (this.getBaseMetaTileEntity().isServerSide()) {
            for (NeutronAccelerator tHatch : mNeutronAccelerator) {
                if (tHatch.isRunning) {
                    anyWorking = true;
                    this.KeV += nextInt(tHatch.getMaxEUConsume(), tHatch.getMaxEUConsume() * 2 + 1) / 10;
                }
            }
            if (!anyWorking) {
                if (this.KeV >= 80 && aTick % 20 == 0) {
                    this.KeV -= 80;
                }
                else if (this.KeV > 0 && aTick % 20 == 0) {
                    this.KeV = 0;
                }
            }
            if (this.KeV < 0) this.KeV = 0;
            if (this.KeV > maxNeutronKineticEnergy()) doExplosion(4 * 32);
        }
    }
}
