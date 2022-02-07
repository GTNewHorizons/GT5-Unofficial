package goodgenerator.blocks.tileEntity;

import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static goodgenerator.util.StructureHelper.addTieredBlock;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class PreciseAssembler extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<PreciseAssembler> multiDefinition = null;
    protected int casingAmount;
    protected int casingTier;
    protected int machineTier;

    public PreciseAssembler(String name) {
        super(name);
    }

    public PreciseAssembler(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<PreciseAssembler> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<PreciseAssembler>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC"},
                                    {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                                    {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                                    {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                                    {"CCCC~CCCC", "CMMMMMMMC", "CMMMMMMMC", "CMMMMMMMC", "CCCCCCCCC"}
                            })
                    )
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(
                                            PreciseAssembler::addToPAssList, 0, 1
                                    ),
                                    onElementPass(
                                            x -> x.casingAmount++,
                                            addTieredBlock(
                                                    Loaders.preciseUnitCasing, PreciseAssembler::setCasingTier, PreciseAssembler::getCasingTier, 3
                                            )
                                    )
                            )
                    )
                    .addElement(
                            'F',
                            ofFrame(
                                    Materials.TungstenSteel
                            )
                    )
                    .addElement(
                            'G',
                            ofBlock(
                                    Block.getBlockFromItem(Ic2Items.reinforcedGlass.getItem()), 0
                            )
                    )
                    .addElement(
                            'M',
                            addTieredBlock(
                                    GregTech_API.sBlockCasings1, PreciseAssembler::setMachineTier, PreciseAssembler::getMachineTier, 10
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    public boolean addToPAssList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        machineTier = aNBT.getInteger("machineTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setInteger("machineTier", machineTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 4, 4, 0, hintsOnly, stackSize);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = 0;
        this.casingAmount = 0;
        this.casingTier = 0;
        if (structureCheck_EM(mName, 4, 4, 0)) {
            if (casingTier != 0) {
                reUpdate(1538 + casingTier);
            }
            return casingAmount >= 12 && machineTier != 0 && casingTier != 0 && mMaintenanceHatches.size() == 1 && !mMufflerHatches.isEmpty();
        }
        return false;
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Precise Assembler/Assembler")
                .addInfo("Controller block for the Precise Assembler")
                .addInfo("The error is no more than 0.03mm.")
                .addInfo("Can assemble precise component in Precise Mode.")
                .addInfo("Can work like a normal assembler in Normal Mode.")
                .addInfo("Use screwdriver to change mode.")
                .addInfo("The Machine Casing limits the voltage tier the machine can work on.")
                .addInfo("UHV Machine Casing will unlock all voltage.")
                .addInfo("Precise Electronic Unit Casing won't lock recipe in Normal Mode.")
                .addInfo("But gives more parallel with more advanced one.")
                .addInfo("MK-I = 32x, MK-II = 64x, MK-III = 128x")
                .addInfo("Providing 10L/s Lubricant can make it 50% faster.")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addController("Front bottom")
                .addInputHatch("Any Casing")
                .addInputBus("Any Casing")
                .addOutputHatch("Any Casing")
                .addOutputBus("Any Casing")
                .addEnergyHatch("Any Casing")
                .addMufflerHatch("Any Casing")
                .addMaintenanceHatch("Any Casing")
                .toolTipFinisher("Good Generator");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getInformation();
        } else {
            return tt.getStructureInformation();
        }
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack){
        return 120;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("PreciseAssembler.hint", 6);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new PreciseAssembler(this.mName);
    }

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    public int getMachineTier() {
        return machineTier;
    }

    public void setMachineTier(int i) {
        machineTier = i;
    }

    public void reUpdate(int texture) {
        for (GT_MetaTileEntity_Hatch hatch : mInputHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mInputBusses) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mOutputHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mOutputBusses) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mEnergyHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mMaintenanceHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mMufflerHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : eEnergyMulti) {
            hatch.updateTexture(texture);
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone){
        int t = 1;
        if (getCasingTier() != 0) t = getCasingTier();
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1538 + t),
                    TextureFactory.of(textureFontOn),
                    TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build()
            };
            else return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1538 + t),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build()
            };
        }
        else return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(1538 + t)};
    }

    @SideOnly(Side.CLIENT)
    public int getCasingTierClient() {
        if (this.getBaseMetaTileEntity().getWorld() == null) {
            return 0;
        }
        try {
            for (byte i = 0; i < 6; i++) {
                Block casing = getBaseMetaTileEntity().getBlockAtSide(i);
                if (casing != null && casing.equals(Loaders.preciseUnitCasing)) {
                    return getBaseMetaTileEntity().getMetaIDAtSide(i) + 1;
                }
            }
        }
        catch (Throwable t) {
            return 0;
        }
        return 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide()) {
            if (this.getBaseMetaTileEntity() != null && this.getBaseMetaTileEntity().getWorld() != null) {
                this.casingTier = getCasingTierClient();
                markDirty();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
}
