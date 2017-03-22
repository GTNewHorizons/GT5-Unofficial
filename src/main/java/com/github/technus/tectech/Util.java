package com.github.technus.tectech;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 21.03.2017.
 */
public class Util {
    public static String intToString(int number, int groupSize) {
        StringBuilder result = new StringBuilder();

        for(int i = 31; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % groupSize == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StuctureCheck(String[][] structure,//0-9 casing, +- air no air, a... ignore 'a'-CHAR-1 blocks
                                        Block[] blockType,//use numbers 0-9 for casing types
                                        byte[] blockMeta,//use numbers 0-9 for casing types
                                        int horizontalOffset, int verticalOffset, int depthOffset,
                                        IGregTechTileEntity aBaseMetaTileEntity,
                                        boolean forceCheck) {
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        World world=aBaseMetaTileEntity.getWorld();

        int x, y, z, a, b, c,yPos;
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        //yPos  - absolute height of checked block

        //perform your duties
        c = -depthOffset;
        for (String[] _structure : structure) {//front to back
            b = verticalOffset;
            for (String __structure : _structure) {//top to bottom
                a = -horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if (block > '`') {//characters allow to skip check a-1 skip, b-2 skips etc.
                        a += block - '`';
                    } else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4: x = +c; z = +a; y = +b; break;
                            case 3: x = +a; z = -c; y = +b; break;
                            case 5: x = -c; z = -a; y = +b; break;
                            case 2: x = -a; z = +c; y = +b; break;
                            //Things get odd if the block faces up or down...
                            case 1: x = +a; y = -c; z = +b; break;//similar to 3
                            case 0: x = -a; y = +c; z = -b; break;//similar to 2
                            default: return false;
                        }
                        //that must be here since in some cases other axis (a,b,c) controls y
                        yPos=aBaseMetaTileEntity.getYCoord()+y;
                        if(yPos<0 || yPos>=256) return false;
                        //Check block
                        if (forceCheck||world.blockExists(x,y,z))
                            switch (block) {
                                case '-'://must be air
                                    if (!aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                    break;
                                case '+'://must not be air
                                    if (aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                    break;
                                default: {//check for block (countable)
                                    int pointer = block - '0';
                                    //countable air -> net.minecraft.block.BlockAir
                                    if (aBaseMetaTileEntity.getBlockOffset(x, y, z) != blockType[pointer]) {
                                        if (TecTech.ModConfig.DEBUG_MODE)
                                            TecTech.Logger.info("Struct-block-error " + x + " " + y + " " + z + "/" + a + " " + c + "/" + aBaseMetaTileEntity.getBlockOffset(x, y, z) + " " + blockType[pointer]);
                                        return false;
                                    }
                                    if (aBaseMetaTileEntity.getMetaIDOffset(x, y, z) != blockMeta[pointer]) {
                                        if (TecTech.ModConfig.DEBUG_MODE)
                                            TecTech.Logger.info("Struct-meta-id-error " + x + " " + y + " " + z + "/" + a + " " + c + "/" + aBaseMetaTileEntity.getMetaIDOffset(x, y, z) + " " + blockMeta[pointer]);
                                        return false;
                                    }
                                }
                            }
                        a++;//block in horizontal layer
                    }
                }
                b--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    public static boolean isInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes, FluidStack[] requiredFluidInputs, ItemStack[] requiredInputs, FluidStack[] givenFluidInputs, ItemStack... givenInputs) {
        if (!GregTech_API.sPostloadFinished) return false;
        if (requiredFluidInputs.length > 0 && givenFluidInputs == null) return false;
        int amt;
        for (FluidStack tFluid : requiredFluidInputs)
            if (tFluid != null) {
                boolean temp = true;
                amt = tFluid.amount;
                for (FluidStack aFluid : givenFluidInputs)
                    if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aFluid.amount;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                if (temp) return false;
            }

        if (requiredInputs.length > 0 && givenInputs == null) return false;
        for (ItemStack tStack : requiredInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : givenInputs) {
                    if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aStack.stackSize;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) return false;
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            if (givenFluidInputs != null) {
                for (FluidStack tFluid : requiredFluidInputs) {
                    if (tFluid != null) {
                        amt = tFluid.amount;
                        for (FluidStack aFluid : givenFluidInputs) {
                            if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                                if (aDontCheckStackSizes) {
                                    aFluid.amount -= amt;
                                    break;
                                }
                                if (aFluid.amount < amt) {
                                    amt -= aFluid.amount;
                                    aFluid.amount = 0;
                                } else {
                                    aFluid.amount -= amt;
                                    amt = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (givenInputs != null) {
                for (ItemStack tStack : requiredInputs) {
                    if (tStack != null) {
                        amt = tStack.stackSize;
                        for (ItemStack aStack : givenInputs) {
                            if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                                if (aDontCheckStackSizes) {
                                    aStack.stackSize -= amt;
                                    break;
                                }
                                if (aStack.stackSize < amt) {
                                    amt -= aStack.stackSize;
                                    aStack.stackSize = 0;
                                } else {
                                    aStack.stackSize -= amt;
                                    amt = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
