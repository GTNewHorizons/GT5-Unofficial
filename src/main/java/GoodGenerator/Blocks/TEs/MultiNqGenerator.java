package GoodGenerator.Blocks.TEs;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.Loader.Loaders;
import GoodGenerator.Main.GoodGenerator;
import GoodGenerator.util.MyRecipeAdder;
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
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<GT_Recipe> tRecipes = MyRecipeAdder.instance.NqGFuels.mRecipeList;

        FluidStack f1=null,f2=null;
        float booster = 1.0f;
        int times = 1;
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

        if (tFluids.size()>0 && tRecipes != null){
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
                    if(f2 != null)
                        depleteInput(f2);
                    if (mRuntime == 0 || mRuntime%lasting == 0){
                        depleteInput(recipeFluid);
                        this.mOutputFluids = new FluidStack[]{recipeFluidOut};
                    }
                    else this.mOutputFluids = null;
                    if (tFluids.contains(Materials.LiquidAir.getFluid(120)) && tFluids.get(tFluids.indexOf(Materials.LiquidAir.getFluid(120))).amount >= 120){
                        depleteInput(Materials.LiquidAir.getFluid(120));
                        addEnergyOutput((long)(outputEU*times*booster));
                        this.mEUt = (int)(outputEU*times*booster);
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
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Naquadah Reactor")
                      .addInfo("Controller block for the Naquadah Reactor")
                      .addInfo("Environmental Friendly!")
                      .addInfo("Generate power with the High-energy molten metal.")
                      .addInfo("Input molten naquadria or enriched naquadah.")
                      .addInfo("Consume coolant 50mb/t to increase the efficiency:")
                      .addInfo("IC2 Coolant 105%, Super Coolant 150%, Cryotheum 275%")
                      .addInfo("Consume excited liquid to increase the output voltage:")
                      .addInfo("molten caesium | 2x output | 9mb/t ")
                      .addInfo("molten uranium-235 | 3x output | 9mb/t")
                      .addInfo("molten naquadah | 4x output | 1mb/t")
                      .addSeparator()
                      .beginStructureBlock(5, 9, 5, true)
                      .addController("Front bottom")
                      .addOtherStructurePart("Radiation Proof Machine Casing","Bottom, at least 10")
                      .addOtherStructurePart("Field Restricting Casing Block","The rest part of the machine")
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
            if(aActive) return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44],new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE)};
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44],new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44]};
    }
}
