package GoodGenerator.Blocks.TEs;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.Loader.Loaders;
import GoodGenerator.util.MyRecipeAdder;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class MultiNqGenerator extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    protected IStructureDefinition<MultiNqGenerator> multiDefinition = null;
    protected int ticker = 0;
    protected long leftEnergy = 0;
    protected boolean fluidLocker = true;
    protected FluidStack lockedFluid = null;
    protected int times = 1;

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 3,7,0, hintsOnly, itemStack);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[]{
                "6x TungstenSteel Pipe Casing",
                "48x Field Restriction Casing",
                "36x Radiation Proof Steel Frame Box",
                "At least 77x Radiation Proof Machine Casing",
                "1~3x Input Hatch",
                "0~1x Output Hatch",
                "1x Maintenance Hatch",
                "1x Dynamo Hatch"
        };
    }

    public final boolean addToGeneratorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
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
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
                    return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                    return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
                    return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti)aMetaTileEntity);
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public IStructureDefinition<MultiNqGenerator> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<MultiNqGenerator>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA","AAAAAAA"},
                                    {"N     N","       ","  CCC  ","  CPC  ","  CCC  ","       ","N     N"},
                                    {"N     N","       ","  CCC  ","  CPC  ","  CCC  ","       ","N     N"},
                                    {"N     N","       ","  CCC  ","  CPC  ","  CCC  ","       ","N     N"},
                                    {"N     N","       ","  CCC  ","  CPC  ","  CCC  ","       ","N     N"},
                                    {"AAAAAAA","A     A","A CCC A","A CPC A","A CCC A","A     A","AAAAAAA"},
                                    {"ANNNNNA","N     N","N CCC N","N CPC N","N CCC N","N     N","ANNNNNA"},
                                    {"XXX~XXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX"},
                            })
                    ).addElement(
                            'X',
                            ofChain(
                                    ofHatchAdder(
                                            MultiNqGenerator::addToGeneratorList, 44,
                                            GregTech_API.sBlockCasings3, 12
                                    ),
                                    ofBlock(
                                            GregTech_API.sBlockCasings3, 12
                                    )
                            )
                    ).addElement(
                            'A',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings3, 12
                            )
                    ).addElement(
                            'N',
                            ofBlockAnyMeta(
                                    Loaders.radiationProtectionSteelFrame
                            )
                    ).addElement(
                            'C',
                            ofBlockAnyMeta(
                                    Loaders.MAR_Casing
                            )
                    ).addElement(
                            'P',
                            ofBlockAnyMeta(
                                    GregTech_API.sBlockCasings2,15
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    public MultiNqGenerator(String name){super(name);}

    public MultiNqGenerator(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT){
        super.loadNBTData(aNBT);
        this.ticker = aNBT.getInteger("mTicker");
        this.fluidLocker = aNBT.getBoolean("mIsLocked");
        this.times = aNBT.getInteger("mTimes");
        this.leftEnergy = aNBT.getLong("mLeftEnergy");
        if (FluidRegistry.getFluid(aNBT.getString("mLockedFluidName")) != null)
            this.lockedFluid = new FluidStack(FluidRegistry.getFluid(aNBT.getString("mLockedFluidName")), aNBT.getInteger("mLockedFluidAmount"));
        else this.lockedFluid = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT){
        super.saveNBTData(aNBT);
        aNBT.setInteger("mTicker", this.ticker);
        aNBT.setBoolean("mIsLocked", this.fluidLocker);
        aNBT.setInteger("mTimes", this.times);
        aNBT.setLong("mLeftEnergy", this.leftEnergy);
        if (lockedFluid != null){
            aNBT.setString("mLockedFluidName", this.lockedFluid.getFluid().getName());
            aNBT.setInteger("mLockedFluidAmount", this.lockedFluid.amount);
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {

        ArrayList<FluidStack> tFluids = getStoredFluids();
        Collection<GT_Recipe> tRecipes = MyRecipeAdder.instance.NqGFuels.mRecipeList;

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

        int cnt = 0;

        for (GT_Recipe recipe : tRecipes)  if (tFluids.contains(recipe.mFluidInputs[0])) cnt ++;

        if (cnt > 1) doExplosion(4 * 4);

        FluidStack f1=null;
        float booster = 1.0f;
        if(tFluids.size() > 0){
            if(tFluids.contains(FluidRegistry.getFluidStack("cryotheum", 50)) && tFluids.get(tFluids.indexOf(FluidRegistry.getFluidStack("cryotheum", 50))).amount >= 50){
                booster = 2.75f;
                f1=FluidRegistry.getFluidStack("cryotheum", 50);
            }
            else if(tFluids.contains(Materials.SuperCoolant.getFluid(50L)) && tFluids.get(tFluids.indexOf(Materials.SuperCoolant.getFluid(50L))).amount >= 50){
                booster = 1.5f;
                f1=Materials.SuperCoolant.getFluid(50L);
            }
            else if(tFluids.contains(FluidRegistry.getFluidStack("ic2coolant",50)) && tFluids.get(tFluids.indexOf(FluidRegistry.getFluidStack("ic2coolant",50))).amount >= 50){
                booster = 1.05f;
                f1=FluidRegistry.getFluidStack("ic2coolant",50);
            }
        }

        if (fluidLocker && lockedFluid != null){
            if (!(tFluids.contains(lockedFluid) && tFluids.get(tFluids.indexOf(lockedFluid)).amount >= lockedFluid.amount)){
                times = 1;
            }
            else {
                if (lockedFluid.getFluid() == MyMaterial.atomicSeparationCatalyst.getMolten(1).getFluid())
                    times = 16;
                else if (lockedFluid.getFluid() == Materials.Naquadah.getMolten(1L).getFluid())
                    times = 4;
                else if (lockedFluid.getFluid() == Materials.Uranium235.getMolten(9L).getFluid())
                    times = 3;
                else if (lockedFluid.getFluid() == Materials.Caesium.getMolten(9L).getFluid())
                    times = 2;
            }
        }

        if (tFluids.size()>0){
            for (GT_Recipe recipe : tRecipes){
                FluidStack recipeFluid = recipe.mFluidInputs[0].copy();
                FluidStack recipeFluidOut = recipe.mFluidOutputs[0].copy();
                recipeFluid.amount = times;
                recipeFluidOut.amount = times;
                int lasting = recipe.mDuration;
                int outputEU = recipe.mSpecialValue;
                if (tFluids.contains(recipeFluid) && tFluids.get(tFluids.indexOf(recipeFluid)).amount >= times){
                    if(f1 != null)
                        depleteInput(f1);
                    if(lockedFluid != null && times != 1)
                        depleteInput(lockedFluid);
                    if (ticker == 0 || ticker%lasting == 0){
                        fluidLocker = false;
                        if(tFluids.size() > 0){
                            if (tFluids.contains((MyMaterial.atomicSeparationCatalyst.getMolten(1))) && tFluids.get(tFluids.indexOf(MyMaterial.atomicSeparationCatalyst.getMolten(1))).amount >= 1){
                                times = 16;
                                lockedFluid = MyMaterial.atomicSeparationCatalyst.getMolten(1);
                            }
                            else if(tFluids.contains(Materials.Naquadah.getMolten(1L)) && tFluids.get(tFluids.indexOf(Materials.Naquadah.getMolten(1L))).amount >= 1){
                                times = 4;
                                lockedFluid = Materials.Naquadah.getMolten(1L);
                            }
                            else if(tFluids.contains(Materials.Uranium235.getMolten(9L)) && tFluids.get(tFluids.indexOf(Materials.Uranium235.getMolten(9L))).amount >= 9){
                                times = 3;
                                lockedFluid = Materials.Uranium235.getMolten(9L);
                            }
                            else if (tFluids.contains(Materials.Caesium.getMolten(9L)) && tFluids.get(tFluids.indexOf(Materials.Caesium.getMolten(9L))).amount >= 9){
                                times = 2;
                                lockedFluid = Materials.Caesium.getMolten(9L);
                            }
                            else {
                                times = 1;
                                lockedFluid = null;
                            }
                            fluidLocker = true;
                            recipeFluid.amount = times;
                            recipeFluidOut.amount = times;
                        }
                        depleteInput(recipeFluid);
                        this.mOutputFluids = new FluidStack[]{recipeFluidOut};
                    }
                    else this.mOutputFluids = null;
                    if (tFluids.contains(Materials.LiquidAir.getFluid(120)) && tFluids.get(tFluids.indexOf(Materials.LiquidAir.getFluid(120))).amount >= 120){
                        depleteInput(Materials.LiquidAir.getFluid(120));
                        addAutoEnergy((long)(outputEU*times*booster));
                        this.mEUt = (int)(outputEU*times*booster);
                    }
                    else{
                        addEnergyOutput_EM(0,0);
                        this.mEUt = 0;
                    }
                    this.mProgresstime = 1;
                    this.mMaxProgresstime = 1;
                    return true;
                }
            }
        }
        this.mEUt = 0;
        return false;
    }

    public void addAutoEnergy(long outputPower){
        if (this.eDynamoMulti.size() > 0)
            for (GT_MetaTileEntity_Hatch tHatch : this.eDynamoMulti){
              long voltage = tHatch.maxEUOutput();
              long power = voltage * tHatch.maxAmperesOut();
              long outputAmperes;
              if (outputPower > power) doExplosion(4 * GT_Utility.getTier(power));
              if (outputPower >= voltage){
                  leftEnergy += outputPower;
                  outputAmperes = leftEnergy / voltage;
                  leftEnergy -= outputAmperes * voltage;
                  addEnergyOutput_EM(voltage, outputAmperes);
              }
              else{
                  addEnergyOutput_EM(outputPower, 1);
              }
            }
        if (this.mDynamoHatches.size() > 0)
            for (GT_MetaTileEntity_Hatch tHatch : this.mDynamoHatches){
                long voltage = tHatch.maxEUOutput();
                long power = voltage * tHatch.maxAmperesOut();
                long outputAmperes;
                if (outputPower > power) doExplosion(8 * GT_Utility.getTier(power));
                if (outputPower >= voltage){
                    leftEnergy += outputPower;
                    outputAmperes = leftEnergy / voltage;
                    leftEnergy -= outputAmperes * voltage;
                    addEnergyOutput_EM(voltage, outputAmperes);
                }
                else{
                    addEnergyOutput_EM(outputPower, 1);
                }
            }
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (getBaseMetaTileEntity().isAllowedToWork()) {
            mRuntime ++;
            ticker ++;
        }
        if (!getBaseMetaTileEntity().isActive() || !getBaseMetaTileEntity().isAllowedToWork()) {
            mRuntime = 0;
            ticker = 0;
            leftEnergy = 0;
            fluidLocker = false;
            lockedFluid = null;
            times = 1;
        }
        if (ticker > 3 * 17 * 19 * 1000000) ticker = 0;
        return true;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 3, 7, 0) && mMaintenanceHatches.size() == 1 && mDynamoHatches.size() + eDynamoMulti.size() == 1;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MultiNqGenerator(this.mName);
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Naquadah Reactor")
                      .addInfo("Controller block for the Naquadah Reactor")
                      .addInfo("Environmental Friendly!")
                      .addInfo("Generate power with the High-energy liquid.")
                      .addInfo("Input liquid nuclear fuel or liquid naquadah fuel.")
                      .addInfo("The reactor will explode when there are more than ONE types of fuel in the hatch!")
                      .addInfo("Consume coolant 50mb/t to increase the efficiency:")
                      .addInfo("IC2 Coolant 105%, Super Coolant 150%, Cryotheum 275%")
                      .addInfo("Consume excited liquid to increase the output power:")
                      .addInfo("molten caesium | 2x power | 9mb/t ")
                      .addInfo("molten uranium-235 | 3x power | 9mb/t")
                      .addInfo("molten naquadah | 4x power | 1mb/t")
                      .addInfo("molten Atomic Separation Catalyst | 16x power | 1mb/t")
                      .addSeparator()
                      .beginStructureBlock(7, 8, 7, true)
                      .addController("Front bottom")
                      .addInfo("The structure is too complex!")
                      .addInfo("Follow the TecTech blueprint to build the main structure.")
                      .addEnergyHatch("Any bottom layer casing, only accept ONE!")
                      .addInputHatch("Any bottom layer casing")
                      .addOutputHatch("Any bottom layer casing")
                      .addMaintenanceHatch("Any bottom layer casing")
                      .toolTipFinisher("Good Generator");
               if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                   return tt.getInformation();
               } else {
                   return tt.getStructureInformation();
               }
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44), new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE), TextureFactory.builder().addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW).glow().build()};
            return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44), new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT)};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(44)};
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

}
