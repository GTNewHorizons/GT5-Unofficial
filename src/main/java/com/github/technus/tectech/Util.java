package com.github.technus.tectech;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by Tec on 21.03.2017.
 */
public class Util {
    public static String intBitsToString(int number) {
        StringBuilder result = new StringBuilder();

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % 8 == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String intBitsToShortString0(int number) {
        StringBuilder result = new StringBuilder();

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0)
                result.append("|");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String intBitsToShortString1(int number) {
        StringBuilder result = new StringBuilder();

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? ";" : ",");

            if (i % 8 == 0)
                result.append("|");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StructureChecker(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR-1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           IGregTechTileEntity aBaseMetaTileEntity,
                                           boolean forceCheck) {
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) return false;
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();

        int x, y, z, a, b, c, pointer;
        final int
                baseX=aBaseMetaTileEntity.getXCoord(),
                baseZ=aBaseMetaTileEntity.getZCoord(),
                baseY=aBaseMetaTileEntity.getYCoord();
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
                    if (block < ' ') {//Control chars allow skipping
                        b -= block;
                        break;
                    } if (block > '@')//characters allow to skip check a-1 skip, b-2 skips etc.
                        a += block - '@';
                    else if (block < '+')//used to mark THINGS
                        a++;
                    else if (block=='.')
                        a++;
                    else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4:
                                x = baseX + c;
                                z = baseZ + a;
                                y = baseY + b;
                                break;
                            case 3:
                                x = baseX + a;
                                z = baseZ - c;
                                y = baseY + b;
                                break;
                            case 5:
                                x = baseX - c;
                                z = baseZ - a;
                                y = baseY + b;
                                break;
                            case 2:
                                x = baseX - a;
                                z = baseZ + c;
                                y = baseY + b;
                                break;
                            //Things get odd if the block faces up or down...
                            case 1:
                                x = baseX + a;
                                z = baseZ + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 0:
                                x = baseX - a;
                                z = baseZ - b;
                                y = baseY + c;
                                break;//similar to 2
                            default:
                                return false;
                        }

                        //that must be here since in some cases other axis (b,c) controls y
                        if (y < 0 || y >= 256) return false;

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(x, y, z).getMaterial() != Material.air)
                                        return false;
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(x, y, z).getMaterial() == Material.air)
                                        return false;
                                    break;
                                default: //check for block (countable)
                                    pointer = block - '0';
                                    //countable air -> net.minecraft.block.BlockAir
                                    if (world.getBlock(x, y, z) != blockType[pointer]) {
                                        if (DEBUG_MODE)
                                            TecTech.Logger.info("Struct-block-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + " " + blockType[pointer].getUnlocalizedName());
                                        return false;
                                    }
                                    if (world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                        if (DEBUG_MODE)
                                            TecTech.Logger.info("Struct-meta-id-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlockMetadata(x, y, z) + " " + blockMeta[pointer]);
                                        return false;
                                    }
                            }
                        } else if (forceCheck) return false;
                        a++;//block in horizontal layer
                    }
                }
                b--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StructureCheckerAdvanced(
            String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR-1 blocks
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            Method adder,
            String[] addingMethods,
            short[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset,
            IGregTechTileEntity aBaseMetaTileEntity,
            boolean forceCheck) {
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) return false;
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();

        IGregTechTileEntity igt;
        IMetaTileEntity imt = aBaseMetaTileEntity.getMetaTileEntity();

        int x, y, z, a, b, c, pointer;
        final int
                baseX=aBaseMetaTileEntity.getXCoord(),
                baseZ=aBaseMetaTileEntity.getZCoord(),
                baseY=aBaseMetaTileEntity.getYCoord();
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
                    if (block < ' ') {//Control chars allow skipping
                        b -= block;
                        break;
                    } else if (block > '@') //characters allow to skip check a-1 skip, b-2 skips etc.
                        a += block - '@';
                  //else if (block < '+')//used to mark THINGS
                  //    a++;
                    else if (block=='.')
                        a++;
                    else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4:
                                x = baseX + c;
                                z = baseZ + a;
                                y = baseY + b;
                                break;
                            case 3:
                                x = baseX + a;
                                z = baseZ - c;
                                y = baseY + b;
                                break;
                            case 5:
                                x = baseX - c;
                                z = baseZ - a;
                                y = baseY + b;
                                break;
                            case 2:
                                x = baseX - a;
                                z = baseZ + c;
                                y = baseY + b;
                                break;
                            //Things get odd if the block faces up or down...
                            case 1:
                                x = baseX + a;
                                z = baseZ + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 0:
                                x = baseX - a;
                                z = baseZ - b;
                                y = baseY + c;
                                break;//similar to 2
                            default:
                                return false;
                        }

                        //that must be here since in some cases other axis (b,c) controls y
                        if (y < 0 || y >= 256) return false;

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(x, y, z).getMaterial() != Material.air)
                                        return false;
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(x, y, z).getMaterial() == Material.air)
                                        return false;
                                    break;
                                default://check for block (countable)
                                    if ((pointer = block - '0') >= 0) {
                                        //countable air -> net.minecraft.block.BlockAir
                                        if (world.getBlock(x, y, z) != blockType[pointer]) {
                                            if (DEBUG_MODE)
                                                TecTech.Logger.info("Struct-block-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + " " + blockType[pointer].getUnlocalizedName());
                                            return false;
                                        }
                                        if (world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                            if (DEBUG_MODE)
                                                TecTech.Logger.info("Struct-meta-id-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlockMetadata(x, y, z) + " " + blockMeta[pointer]);
                                            return false;
                                        }
                                    } else if ((pointer = block - ' ') >= 0) {
                                        igt = aBaseMetaTileEntity.getIGregTechTileEntity(x, y, z);
                                        try {
                                            if (igt == null || !(boolean) adder.invoke(imt, addingMethods[pointer], igt, casingTextures[pointer])) {
                                                if (world.getBlock(x, y, z) != blockTypeFallback[pointer]) {
                                                    if (DEBUG_MODE)
                                                        TecTech.Logger.info("Fallback-struct-block-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + " " + blockTypeFallback[pointer].getUnlocalizedName());
                                                    return false;
                                                }
                                                if (world.getBlockMetadata(x, y, z) != blockMetaFallback[pointer]) {
                                                    if (DEBUG_MODE)
                                                        TecTech.Logger.info("Fallback-Struct-meta-id-error " + x + " " + y + " " + z + " / " + a + " " + b + " " + c + " / " + world.getBlockMetadata(x, y, z) + " " + blockMetaFallback[pointer]);
                                                    return false;
                                                }
                                            }
                                        } catch (InvocationTargetException | IllegalAccessException e) {
                                            if (DEBUG_MODE) e.printStackTrace();
                                            return false;
                                        }
                                    }
                            }
                        } else if (forceCheck) return false;
                        a++;//block in horizontal layer
                    }
                }
                b--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    public static boolean StructureBuilder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           IGregTechTileEntity aBaseMetaTileEntity, boolean hintsOnly) {
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        return StructureBuilder(structure,blockType,blockMeta,
                horizontalOffset,verticalOffset,depthOffset,
                aBaseMetaTileEntity.getWorld().getTileEntity(aBaseMetaTileEntity.getXCoord(),aBaseMetaTileEntity.getYCoord(),aBaseMetaTileEntity.getZCoord()),
                facing,hintsOnly);
    }

    public static boolean StructureBuilder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           TileEntity tileEntity, int facing, boolean hintsOnly) {
        if(!tileEntity.hasWorldObj()) return false;
        World world = tileEntity.getWorldObj();
        if (!world.isRemote && hintsOnly) return false;

        //TE Rotation

        int x, y, z, a, b, c, pointer;
        final int
                baseX=tileEntity.xCoord,
                baseZ=tileEntity.zCoord,
                baseY=tileEntity.yCoord;
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!

        //perform your duties
        c = -depthOffset;
        for (String[] _structure : structure) {//front to back
            b = verticalOffset;
            for (String __structure : _structure) {//top to bottom
                a = -horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if (block < ' ') {//Control chars allow skipping
                        b -= block;
                        break;
                    } if (block > '@')//characters allow to skip check a-1 skip, b-2 skips etc.
                        a += block - '@';
                  //else if (block < '+')//used to mark THINGS
                  //    a++;
                    else if (block=='.')// this TE
                        a++;
                    else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4:
                                x = baseX + c;
                                z = baseZ + a;
                                y = baseY + b;
                                break;
                            case 3:
                                x = baseX + a;
                                z = baseZ - c;
                                y = baseY + b;
                                break;
                            case 5:
                                x = baseX - c;
                                z = baseZ - a;
                                y = baseY + b;
                                break;
                            case 2:
                                x = baseX - a;
                                z = baseZ + c;
                                y = baseY + b;
                                break;
                            //Things get odd if the block faces up or down...
                            case 1:
                                x = baseX + a;
                                z = baseZ + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 0:
                                x = baseX - a;
                                z = baseZ - b;
                                y = baseY + c;
                                break;//similar to 2
                            default:
                                return false;
                        }

                        //that must be here since in some cases other axis (b,c) controls y
                        if (y < 0 || y >= 256) return false;

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded
                            if(hintsOnly){
                                switch (block) {
                                case '-'://must be air
                                    TecTech.proxy.hint_particle(world,x, y, z, TT_Container_Casings.sHintCasingsTT, 13);
                                    break;
                                case '+'://must not be air
                                    TecTech.proxy.hint_particle(world,x, y, z, TT_Container_Casings.sHintCasingsTT, 14);
                                    break;
                                default: //check for block
                                    if ((pointer = block - '0') >= 0) {
                                        if(world.getBlock(x,y,z)!=blockType[pointer] || world.getBlockMetadata(x,y,z)!=blockMeta[pointer])
                                            TecTech.proxy.hint_particle(world,x, y, z, blockType[pointer], blockMeta[pointer]);
                                    } else if ((pointer = block - ' ') >= 0) {
                                        switch(pointer){
                                            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                                                TecTech.proxy.hint_particle(world,x, y, z, TT_Container_Casings.sHintCasingsTT, pointer); break;
                                            default:TecTech.proxy.hint_particle(world,x, y, z, TT_Container_Casings.sHintCasingsTT, 12);
                                        }
                                    } else TecTech.proxy.hint_particle(world,x, y, z, TT_Container_Casings.sHintCasingsTT, 15);
                                }
                            }else{
                                switch (block) {
                                case '-'://must be air
                                    world.setBlock(x, y, z, Blocks.air, 0, 2);
                                    break;
                                case '+'://must not be air
                                    world.setBlock(x, y, z, TT_Container_Casings.sBlockCasingsTT, 14, 2);
                                    break;
                                default: //check for block
                                    if ((pointer = block - '0') >= 0) {
                                        world.setBlock(x, y, z, blockType[pointer], blockMeta[pointer], 2);
                                    } else if ((pointer = block - ' ') >= 0) {
                                        //switch(pointer){
                                        //    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                                        //        world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, pointer, 2); break;
                                        //    default:world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, 12, 2);
                                        //}
                                    } else world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, 15,2);
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

    public static String[] StructureWriter(IGregTechTileEntity aBaseMetaTileEntity,
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           int horizontalSize, int verticalSize, int depthSize, boolean ignoreAir) {
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) return new String[]{"Not at Client m8"};

        ItemStack[] array = new ItemStack[10];

        int x, y, z, a, b, c;
        final int
                baseX=aBaseMetaTileEntity.getXCoord(),
                baseZ=aBaseMetaTileEntity.getZCoord(),
                baseY=aBaseMetaTileEntity.getYCoord();
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        //yPos  - absolute height of checked block

        //perform your duties - #1 - count block types
        c = -depthOffset;
        for (int cz = 0; cz < depthSize; cz++) {//front to back
            b = verticalOffset;
            for (int by = 0; by < verticalSize; by++) {//top to bottom
                a = -horizontalOffset;
                for (int az = 0; az < horizontalSize; az++) {//left to right
                    //get x y z from rotation
                    switch (facing) {//translation
                        case 4:
                            x = baseX + c;
                            z = baseZ + a;
                            y = baseY + b;
                            break;
                        case 3:
                            x = baseX + a;
                            z = baseZ - c;
                            y = baseY + b;
                            break;
                        case 5:
                            x = baseX - c;
                            z = baseZ - a;
                            y = baseY + b;
                            break;
                        case 2:
                            x = baseX - a;
                            z = baseZ + c;
                            y = baseY + b;
                            break;
                        //Things get odd if the block faces up or down...
                        case 1:
                            x = baseX + a;
                            z = baseZ + b;
                            y = baseY - c;
                            break;//similar to 3
                        case 0:
                            x = baseX - a;
                            z = baseZ - b;
                            y = baseY + c;
                            break;//similar to 2
                        default:
                            return new String[]{"Invalid rotation"};
                    }

                    //that must be here since in some cases other axis (b,c) controls y
                    if (y < 0 || y >= 256) return new String[]{"Invalid position"};

                    //Check block
                    Block block = world.getBlock(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);

                    if (!block.hasTileEntity(meta) && block.getMaterial() != Material.air) {
                        boolean err = true;
                        final ItemStack is = new ItemStack(block, 1, meta);
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] == null) {
                                array[i] = is;
                                err = false;
                                break;
                            } else if (is.getItem() == array[i].getItem() && is.getItemDamage() == array[i].getItemDamage()) {
                                err = false;
                                break;
                            }
                        }
                        if (err) return new String[]{"Too much different blocks"};
                    }

                    a++;//block in horizontal layer
                }
                b--;//horizontal layer
            }
            c++;//depth
        }

        List<String> output = new ArrayList<>();

        output.add("Offsets: " + horizontalOffset + " " + verticalOffset + " " + depthOffset);
        output.add("Sizes: " + horizontalSize + " " + verticalSize + " " + depthSize);
        output.add("");

        output.add("ID[]: Name[]");
        output.add("");
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                output.add(i + ": " + array[i].getDisplayName());
            }
        }
        output.add("");
        output.add("ID[]: Block[] BlockMetaID[]");
        output.add("");
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                output.add(i + ": " + array[i].getItem().getUnlocalizedName() + " " + array[i].getItemDamage());
            }
        }
        output.add("");
        output.add("String[][]");
        //perform your duties - #2 - write strings
        output.add("{");
        c = -depthOffset;
        for (int cz = 0; cz < depthSize; cz++) {//front to back
            b = verticalOffset;
            String addMe = "{";
            for (int by = 0; by < verticalSize; by++) {//top to bottom
                a = -horizontalOffset;
                String line = "";
                for (int az = 0; az < horizontalSize; az++) {//left to right
                    //get x y z from rotation
                    switch (facing) {//translation
                        case 4:
                            x = baseX + c;
                            z = baseZ + a;
                            y = baseY + b;
                            break;
                        case 3:
                            x = baseX + a;
                            z = baseZ - c;
                            y = baseY + b;
                            break;
                        case 5:
                            x = baseX - c;
                            z = baseZ - a;
                            y = baseY + b;
                            break;
                        case 2:
                            x = baseX - a;
                            z = baseZ + c;
                            y = baseY + b;
                            break;
                        //Things get odd if the block faces up or down...
                        case 1:
                            x = baseX + a;
                            z = baseZ + b;
                            y = baseY - c;
                            break;//similar to 3
                        case 0:
                            x = baseX - a;
                            z = baseZ - b;
                            y = baseY + c;
                            break;//similar to 2
                        default:
                            return new String[]{"Invalid rotation"};
                    }

                    //Check block
                    Block block = world.getBlock(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);

                    if (a == 0 && b == 0 && c == 0) {
                        line += '.';
                    } else if (block.getMaterial() == Material.air) {
                        line += '-';
                    } else if (block.hasTileEntity(meta)) {
                        line += '*';
                    } else {
                        ItemStack stack = new ItemStack(block, 1, meta);
                        String str = "?";//OH YEAH NPEs
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] != null && stack.getItem() == array[i].getItem() && stack.getItemDamage() == array[i].getItemDamage()) {
                                str = Integer.toString(i);
                                break;
                            }
                        }
                        line += str;
                    }
                    a++;//block in horizontal layer
                }
                if (ignoreAir) {
                    String l = "";
                    char temp = '@';
                    for (char ch : line.toCharArray()) {
                        if (ch == '-') {
                            temp += 1;
                            if (temp == '~') {
                                l += '~';
                                temp = '@';
                            }
                        } else {
                            if (temp > '@') {
                                l += temp;
                                temp = '@';
                            }
                            l += ch;
                        }
                    }
                    while (l.length() > 0 && l.toCharArray()[l.length() - 1] == '~')
                        l = l.substring(0, l.length() - 1);
                    if (l.length() == 0)
                        l = "E,";
                    else {
                        l = "\"" + l + "\",";
                    }
                    addMe += l;
                } else {
                    if (line.length() == 0)
                        line = "E,";
                    else {
                        line = "\"" + line + "\",";
                    }
                    addMe += line;
                }
                b--;//horizontal layer
            }
            //region less verbose
            addMe=(addMe + "},").replaceAll("(E,)+(?=})",E/*Remove Empty strings at end*/);
            Matcher m = matchE_.matcher(addMe);
            while (m.find()) {
                byte lenEE = (byte)(m.group(1).length()>>1);
                addMe=addMe.replaceFirst("E,(E,)+","\"\\\\u00"+String.format("%02X", lenEE-1)+"\",");
                //addMe=addMe.replaceFirst("E,(E,)+\"","\"\\\\u00"+String.format("%02X", lenEE));
            }
            //endregion
            output.add(addMe);
            c++;//depth
        }
        output.add("}");
        return output.toArray(new String[0]);
    }

    private static final Pattern matchE_ = Pattern.compile("(E,(E,)+)");

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

    public static String getUniqueIdentifier(ItemStack is) {
        return GameRegistry.findUniqueIdentifierFor(is.getItem()).modId + ":" + is.getUnlocalizedName();
    }


    public static final String[] VN = new String[]{"ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV", "UIV", "UMV", "UXV", "OpV", "MAX"};
    public static final long[] V = new long[]{8L, 32L, 128L, 512L, 2048L, 8192L, 32768L, 131072L, 524288L, 2097152L, 8388608L, 33554432L, 134217728L, 536870912L, 1073741824L, Integer.MAX_VALUE-7};

    public static byte getTier(long l) {
        byte i = -1;

        do {
            ++i;
            if (i >= V.length) {
                return i;
            }
        } while(l > V[i]);

        return i;
    }

    public static String[] splitButDifferent(String string,String delimiter){
        String[] strings= new String[StringUtils.countMatches(string,delimiter)+1];
        int lastEnd=0;
        for(int i=0;i<strings.length-1;i++){
            int nextEnd=string.indexOf(delimiter,lastEnd);
            strings[i]=string.substring(lastEnd,nextEnd);
            lastEnd=nextEnd+delimiter.length();
        }
        strings[strings.length-1]=string.substring(lastEnd);
        return strings;
    }

    public static String[] infoFromNBT(NBTTagCompound nbt) {
        final String[] strings = new String[nbt.getInteger("i")];
        for (int i = 0; i < strings.length; i++)
            strings[i] = nbt.getString(Integer.toString(i));
        return strings;
    }

    public static boolean areBitsSet(int setBits,int testedValue){
        return (testedValue&setBits)==setBits;
    }
}
