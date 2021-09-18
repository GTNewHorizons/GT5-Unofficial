package GoodGenerator.Blocks.TEs;

import GoodGenerator.Blocks.TEs.MetaTE.YottaFluidTankOutputHatch;
import GoodGenerator.Loader.Loaders;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.render.TextureFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static GoodGenerator.util.StructureHelper.addFrame;
import static GoodGenerator.util.StructureHelper.addTieredBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.*;

public class YottaFluidTank extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<YottaFluidTank> multiDefinition = null;

    protected BigInteger mStorage = new BigInteger("0", 10);
    protected BigInteger mStorageCurrent = new BigInteger("0", 10);
    protected String mFluidName = "";
    protected int glassMeta;
    protected final String YOTTANK_BOTTOM = mName + "buttom";
    protected final String YOTTANK_MID = mName + "mid";
    protected final String YOTTANK_TOP = mName + "top";
    protected List<YottaFluidTankOutputHatch> mYottaOutput = new ArrayList<>();

    public YottaFluidTank(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public YottaFluidTank(String name) {
        super(name);
    }

    public int getMeta() {
        return glassMeta;
    }

    public void setMeta(int meta) {
        glassMeta = meta;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        String tAmount = aNBT.getString("mStorage");
        String tAmountCurrent = aNBT.getString("mStorageCurrent");
        if (tAmount == null || tAmount.equals("")) tAmount = "0";
        if (tAmountCurrent == null || tAmountCurrent.equals("")) tAmountCurrent = "0";
        mStorage = new BigInteger(tAmount, 10);
        mStorageCurrent = new BigInteger(tAmountCurrent, 10);
        mFluidName = aNBT.getString("mFluidName");
        glassMeta = aNBT.getInteger("glassMeta");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mStorage", mStorage.toString(10));
        aNBT.setString("mStorageCurrent", mStorageCurrent.toString(10));
        aNBT.setString("mFluidName", mFluidName);
        aNBT.setInteger("glassMeta", glassMeta);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        return true;
    }

    public boolean reduceFluid(int amount) {
        BigInteger tmp = new BigInteger(amount + "");
        if (mStorageCurrent.compareTo(tmp) < 0) {
            return false;
        }
        else {
            mStorageCurrent = mStorageCurrent.subtract(tmp);
            return true;
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        for (YottaFluidTankOutputHatch tHatch : mYottaOutput) tHatch.unBounded();
        mYottaOutput.clear();
        if (!structureCheck_EM(YOTTANK_BOTTOM, 2, 0, 0)) return false;
        int cnt = 0;
        while (structureCheck_EM(YOTTANK_MID, 2, cnt + 1, 0)) {
            cnt ++;
        }
        if (cnt > 15 || cnt < 1) return false;
        if (!structureCheck_EM(YOTTANK_TOP, 2, cnt + 2, 0)) return false;
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    public IStructureDefinition<YottaFluidTank> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<YottaFluidTank>builder()
                    .addShape(YOTTANK_BOTTOM, transpose(new String[][]{
                            {"MM~MM","MCCCM","MCCCM","MCCCM","MMMMM"},
                            {"     "," CCC "," COC "," CCC ","     "}
                    }))
                    .addShape(YOTTANK_MID, transpose(new String[][]{
                            {"GGGGG","GRRRG","GRRRG","GRRRG","GGGGG"}
                    }))
                    .addShape(YOTTANK_TOP, transpose(new String[][]{
                            {"FFFFF","F   F","F   F","F   F","FFFFF"},
                            {"CCCCC","CIIIC","CIIIC","CIIIC","CCCCC"}
                    }))
                    .addElement(
                            'C',
                            ofBlock(
                                    Loaders.yottaFluidTankCasing, 0
                            )
                    )
                    .addElement(
                            'G',
                            addTieredBlock(
                                    ItemRegistry.bw_realglas, YottaFluidTank::setMeta, YottaFluidTank::getMeta, 12
                            )
                    )
                    .addElement(
                            'R',
                            ofChain(
                                    cells(10)
                            )
                    )
                    .addElement(
                            'F',
                            addFrame(Materials.Steel)
                    )
                    .addElement(
                            'I',
                            ofHatchAdderOptional(
                                    YottaFluidTank::addInput,
                                    1537,
                                    1,
                                    Loaders.yottaFluidTankCasing,
                                    0
                            )
                    )
                    .addElement(
                            'M',
                            ofHatchAdderOptional(
                                    YottaFluidTank::addClassicMaintenanceToMachineList,
                                    1537,
                                    2,
                                    Loaders.yottaFluidTankCasing,
                                    0
                            )
                    )
                    .addElement(
                            'O',
                            ofHatchAdder(
                                    YottaFluidTank::addOutput,
                                    1537,
                                    3
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    public List<IStructureElement<YottaFluidTank>> cells(int num) {
        List<IStructureElement<YottaFluidTank>> out = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            int finalI = i;
            out.add(
                    onElementPass(
                            x -> x.mStorage = x.mStorage.add(calStorage(finalI)),
                            ofBlock(Loaders.yottaFluidTankCell, i)
                    )
            );
        }
        return out;
    }

    public final boolean addInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                    ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
                }
            }
        }
        return false;
    }

    public final boolean addOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if ((aMetaTileEntity instanceof YottaFluidTankOutputHatch) && boundOutput(aTileEntity)) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean boundOutput(IGregTechTileEntity output) {
        IMetaTileEntity tHatch = output.getMetaTileEntity();
        if (tHatch instanceof YottaFluidTankOutputHatch) {
            YottaFluidTankOutputHatch Hatch = (YottaFluidTankOutputHatch) tHatch;
            if (!Hatch.isBounded()) {
                Hatch.setControl(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord());
                mYottaOutput.add(Hatch);
                return true;
            }
        }
        return false;
    }

    public BigInteger calStorage(int meta) {
        StringBuilder cap = new StringBuilder();
        cap.append("1000000");
        for (int i = 0; i < meta; ++i)
            cap.append("00");
        return new BigInteger(cap.toString());
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        super.onRunningTick(aStack);
        List<FluidStack> tStore = getStoredFluids();
        for (FluidStack tFluid : tStore) {
            if (tFluid == null) continue;
            if (mFluidName == null || mFluidName.equals("") || tFluid.getFluid().getName().equals(mFluidName)) {
                if (mFluidName == null || mFluidName.equals("")) {
                    mFluidName = tFluid.getFluid().getName();
                }
                if (mStorageCurrent.add(new BigInteger(tFluid.amount + "")).compareTo(mStorage) < 0) {
                    mStorageCurrent = mStorageCurrent.add(new BigInteger(tFluid.amount + ""));
                    tFluid.amount = 0;
                } else {
                    BigInteger delta = mStorage.subtract(mStorageCurrent);
                    mStorageCurrent = mStorageCurrent.add(delta);
                    tFluid.amount -= delta.intValue();
                }
            }
        }
        BigInteger outputAmount = mStorageCurrent.divide(new BigInteger("100", 10));
        if (outputAmount.compareTo(new BigInteger(Integer.MAX_VALUE + "", 10)) > 0) outputAmount = new BigInteger(Integer.MAX_VALUE + "");
        if (outputAmount.compareTo(BigInteger.ONE) <= 0) outputAmount = new BigInteger("1", 10);

        if (mStorageCurrent.compareTo(BigInteger.ZERO) <= 0) mFluidName = "";

        if (mFluidName != null && !mFluidName.equals("")) {
            if (mYottaOutput.size() > 0) {
                mYottaOutput.get(0).setFluid(FluidRegistry.getFluidStack(mFluidName, outputAmount.intValue()));
                return true;
            }
        }
        mYottaOutput.get(0).setFluid(null);
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(YOTTANK_BOTTOM, 2, 0, 0, hintsOnly, stackSize);
        int height = stackSize.stackSize;
        if (height > 15) height = 15;
        structureBuild_EM(YOTTANK_TOP, 2, height + 2, 0, hintsOnly, stackSize);
        while (height > 0) {
            structureBuild_EM(YOTTANK_MID, 2, height, 0, hintsOnly, stackSize);
            height --;
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new YottaFluidTank(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone){
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1537),
                    TextureFactory.of(textureFontOn),
                    TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build()
            };
            else return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1537),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build()
            };
        }
        else return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(1537)};
    }
}
