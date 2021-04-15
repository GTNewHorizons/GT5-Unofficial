package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class MultiNqGenerator extends GT_MetaTileEntity_MultiBlockBase {

    @SideOnly(Side.CLIENT)
    protected String textureNames;
    protected String name;

    public MultiNqGenerator(String name){super(name);}

    public MultiNqGenerator(int id, String name, String nameRegional){
        super(id,name,nameRegional);
        this.name = name;
        textureNames = GoodGenerator.MOD_ID+":"+name;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        ArrayList<FluidStack> tFluids = getStoredFluids();

        FluidStack f1=null,f2=null;
        float booster = 1.0f;
        int times = 1;
        if(tFluids.size() > 0){
            if(tFluids.contains(Materials.Cryotheum.getFluid(50L)) && tFluids.get(tFluids.indexOf(Materials.Cryotheum.getFluid(50L))).amount >= 50){
                booster = 2.75f;
                f1=Materials.Cryotheum.getFluid(50L);
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

        if(tFluids.size() > 0){
            if(tFluids.contains(Materials.Naquadah.getMolten(1L)) && tFluids.get(tFluids.indexOf(Materials.Naquadah.getMolten(1L))).amount >= 1){
                times = 4;
                f2=Materials.Naquadah.getMolten(1L);
            }
            else if(tFluids.contains(Materials.Uranium235.getMolten(9L)) && tFluids.get(tFluids.indexOf(Materials.Uranium235.getMolten(9L))).amount >= 9){
                times = 3;
                f2=Materials.Uranium235.getMolten(9L);
            }
            else if (tFluids.contains(Materials.Caesium.getMolten(9L)) && tFluids.get(tFluids.indexOf(Materials.Caesium.getMolten(9L))).amount >= 9){
                times = 2;
                f2=Materials.Caesium.getMolten(9L);
            }
        }

        if(tFluids.size()>0){
            if(tFluids.contains(Materials.NaquadahEnriched.getMolten(times)) && tFluids.get(tFluids.indexOf(Materials.NaquadahEnriched.getMolten(times))).amount >= times){
                if(f1 != null)
                    depleteInput(f1);
                if(f2 != null)
                    depleteInput(f2);
                if(mRuntime == 0 || mRuntime%1200 == 0){
                    depleteInput(Materials.NaquadahEnriched.getMolten(times));
                    this.mOutputFluids = new FluidStack[]{Materials.Naquadah.getMolten(times)};
                }
                else this.mOutputFluids = null;
                if(tFluids.contains(Materials.LiquidAir.getFluid(120)) && tFluids.get(tFluids.indexOf(Materials.LiquidAir.getFluid(120))).amount >= 120){
                    depleteInput(Materials.LiquidAir.getFluid(120));
                    addEnergyOutput((long)(32768*times*booster));
                    this.mEUt = (int)(32768*times*booster);
                }
                else{
                    addEnergyOutput(0);
                    this.mEUt = 0;
                }
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                return true;
            }
            if(tFluids.contains(Materials.Naquadria.getMolten(times)) && tFluids.get(tFluids.indexOf(Materials.Naquadria.getMolten(times))).amount >= times){
                if(f1 != null)
                    depleteInput(f1);
                if(f2 != null)
                    depleteInput(f2);
                if(mRuntime == 0 || mRuntime%100 == 0){
                    depleteInput(Materials.Naquadria.getMolten(times));
                    this.mOutputFluids = new FluidStack[]{Materials.Naquadah.getMolten(times)};
                }
                else this.mOutputFluids = null;
                if(tFluids.contains(Materials.LiquidAir.getFluid(120)) && tFluids.get(tFluids.indexOf(Materials.LiquidAir.getFluid(120))).amount >= 120){
                    depleteInput(Materials.LiquidAir.getFluid(120));
                    addEnergyOutput((long)(262144*times*booster));
                    this.mEUt = (int)(262144*times*booster);
                }
                else{
                    addEnergyOutput(0);
                    this.mEUt = 0;
                }
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                return true;
            }
        }
        this.mEUt = 0;
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
        int casingAmount = 0;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 8; y++) {
                    if (x + xDir == 0 && y == 0 && z + zDir == 0) {
                        continue;
                    }
                    IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x + xDir, y, z + zDir);
                    Block block = aBaseMetaTileEntity.getBlockOffset(x + xDir, y, z + zDir);

                    if (y != 0 && y != 8 && x != -2 && x != 2 && z != -2 && z != 2) {
                        if (block == Blocks.air) continue;
                        else return false;
                    }

                    if (y != 0){
                        if(block == Loaders.MAR_Casing) continue;
                        else return false;
                    }

                    if (!addInputToMachineList(tileEntity, 44) && !addOutputToMachineList(tileEntity, 44)
                            && !addMaintenanceToMachineList(tileEntity, 44)
                            && !addDynamoToMachineList(tileEntity,44)) {
                        if (block == GregTech_API.sBlockCasings3 && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == 12) {
                            casingAmount++;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return casingAmount >= 10 && mDynamoHatches.size() == 1 ;
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
        return new String[0];
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44],new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE)};
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44],new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44]};
    }
}
