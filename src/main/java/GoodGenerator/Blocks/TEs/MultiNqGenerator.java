package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import ic2.core.Ic2Fluid;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class MultiNqGenerator extends GT_MetaTileEntity_MultiBlockBase {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;
    protected String textureNames;
    protected String name;
    private static final IIcon[] iIcons = new IIcon[4];
    private static final IIconContainer[] iIconContainers = new IIconContainer[4];
    private static final ITexture[][] iTextures = new ITexture[4][1];
    public ItemStack[] circuits = new ItemStack[5];
    private long mStorage;

    public MultiNqGenerator(String name){super(name);}

    public MultiNqGenerator(int id, String name, String nameRegional){
        super(id,name,nameRegional);
        this.name = name;
        textureNames = GoodGenerator.MOD_ID+":"+name;
    }



    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {

        for (int i = 0; i < MultiNqGenerator.iTextures.length; i++) {
            MultiNqGenerator.iIcons[i] = aBlockIconRegister.registerIcon(GoodGenerator.MOD_ID + ":test");
            int finalI = i;
            MultiNqGenerator.iIconContainers[i] = new IIconContainer() {
                @Override
                public IIcon getIcon() {
                    return MultiNqGenerator.iIcons[finalI];
                }

                @Override
                public IIcon getOverlayIcon() {
                    return MultiNqGenerator.iIcons[finalI];
                }

                @Override
                public ResourceLocation getTextureFile() {
                    return new ResourceLocation(GoodGenerator.MOD_ID + ":test");
                }
            };
        }

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
            if(tFluids.contains(Materials.Cryotheum.getFluid(2L))){
                booster = 2.75f;
                f1=Materials.Cryotheum.getFluid(2L);
            }
            else if(tFluids.contains(Materials.SuperCoolant.getFluid(2L))){
                booster = 1.5f;
                f1=Materials.SuperCoolant.getFluid(2L);
            }
            else if(tFluids.contains(FluidRegistry.getFluidStack("ic2coolant",2))){
                booster = 1.05f;
                f1=FluidRegistry.getFluidStack("ic2coolant",2);
            }
        }

        if(tFluids.size() > 0){
            if (tFluids.contains(Materials.Caesium.getMolten(9L))){
                times = 2;
                f2=Materials.Caesium.getMolten(9L);
            }
            else if(tFluids.contains(Materials.Uranium235.getMolten(9L))){
                times = 3;
                f2=Materials.Uranium235.getMolten(9L);
            }
            else if(tFluids.contains(Materials.Naquadah.getMolten(1L))){
                times = 4;
                f2=Materials.Naquadah.getMolten(1L);
            }
        }

        if(tFluids.size()>0){
            if(tFluids.contains(Materials.NaquadahEnriched.getMolten(times))){
                if(f1 != null)
                    depleteInput(f1);
                if(f2 != null)
                    depleteInput(f2);
                if(mRuntime == 0 || mRuntime%1200 == 0){
                    depleteInput(Materials.NaquadahEnriched.getMolten(times));
                    this.mOutputFluids = new FluidStack[]{Materials.Naquadah.getMolten(times)};
                }
                else this.mOutputFluids = null;
                if(tFluids.contains(Materials.LiquidAir.getFluid(20))){
                    depleteInput(Materials.LiquidAir.getFluid(20));
                    addEnergyOutput((long)(32768*times*booster));
                    this.mEUt = (int)(32768*times*booster);
                }
                else{
                    this.mEUt = 0;
                }
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                return true;
            }
            if(tFluids.contains(Materials.Naquadria.getMolten(times))){
                if(f1 != null)
                    depleteInput(f1);
                if(f2 != null)
                    depleteInput(f2);
                if(mRuntime == 0 || mRuntime%300 == 0){
                    depleteInput(Materials.Naquadria.getMolten(times));
                    this.mOutputFluids = new FluidStack[]{Materials.Naquadah.getMolten(times)};
                }
                else this.mOutputFluids = null;
                if(tFluids.contains(Materials.LiquidAir.getFluid(20))){
                    depleteInput(Materials.LiquidAir.getFluid(20));
                    addEnergyOutput((long)(524288*times*booster));
                    this.mEUt = (int)(524288*times*booster);
                }
                else{
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
