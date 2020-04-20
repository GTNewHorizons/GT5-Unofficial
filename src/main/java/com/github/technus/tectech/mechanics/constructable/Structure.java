package com.github.technus.tectech.mechanics.constructable;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static gregtech.api.enums.GT_Values.E;

public class Structure {
    private static final Pattern matchE_ = Pattern.compile("(E,(E,)+)");

    private Structure(){}

    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean checker(
            String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR-1 blocks
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            IHatchAdder[] addingMethods,
            short[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset,
            IGregTechTileEntity aBaseMetaTileEntity,
            ExtendedFacing extendedFacing,
            boolean forceCheck) {
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) {
            return false;
        }
        //TE Rotation
        if(extendedFacing==null){
            extendedFacing=ExtendedFacing.of(ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing()));
        }

        IGregTechTileEntity igt;

        int[] xyz =new int[3];
        int[] abc =new int[3];
        int pointer;
        int baseX = aBaseMetaTileEntity.getXCoord(),
                baseZ = aBaseMetaTileEntity.getZCoord(),
                baseY = aBaseMetaTileEntity.getYCoord();
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        //yPos  - absolute height of checked block

        //perform your duties
        abc[2] = -depthOffset;
        for (String[] _structure : structure) {//front to back
            abc[1] = verticalOffset;
            for (String __structure : _structure) {//top to bottom
                abc[0] = -horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if (block < ' ') {//Control chars allow skipping
                        abc[1] -= block;
                        break;
                    } else if (block > '@') {//characters allow to skip check A-1 skip, B-2 skips etc.
                        abc[0] += block - '@';
                    }//else if (block < '+')//used to mark THINGS
                    //    a++;
                    else if (block == '.') {
                        abc[0]++;
                    } else {
                        //get x y z from rotation
                        extendedFacing.getWorldOffset(abc,xyz);
                        xyz[0]+=baseX;
                        xyz[1]+=baseY;
                        xyz[2]+=baseZ;

                        //that must be here since in some cases other axis (b,c) controls y
                        if (xyz[1] < 0 || xyz[1] >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(xyz[0], xyz[1], xyz[2]).getMaterial() != Material.air) {
                                        return false;
                                    }
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(xyz[0], xyz[1], xyz[2]).getMaterial() == Material.air) {
                                        return false;
                                    }
                                    break;
                                default://check for block (countable)
                                    if ((pointer = block - '0') >= 0) {
                                        //countable air -> net.minecraft.block.BlockAir
                                        if (world.getBlock(xyz[0], xyz[1], xyz[2]) != blockType[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-block-error " + xyz[0] + ' ' + xyz[1] + ' ' + xyz[2] + " / " + abc[0] + ' ' + abc[1] + ' ' + abc[2] + " / " + world.getBlock(xyz[0], xyz[1], xyz[2]).getUnlocalizedName() + ' ' + blockType[pointer].getUnlocalizedName());
                                            }
                                            return false;
                                        }
                                        if (world.getBlockMetadata(xyz[0], xyz[1], xyz[2]) != blockMeta[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-meta-id-error " + xyz[0] + ' ' + xyz[1] + ' ' + xyz[2] + " / " + abc[0] + ' ' + abc[1] + ' ' + abc[2] + " / " + world.getBlockMetadata(xyz[0], xyz[1], xyz[2]) + ' ' + blockMeta[pointer]);
                                            }
                                            return false;
                                        }
                                    } else //noinspection ConstantConditions
                                        if ((pointer = block - ' ') >= 0) {
                                            igt = aBaseMetaTileEntity.getIGregTechTileEntity(xyz[0], xyz[1], xyz[2]);
                                            if (igt == null || !addingMethods[pointer].apply(igt, casingTextures[pointer])) {
                                                if (world.getBlock(xyz[0], xyz[1], xyz[2]) != blockTypeFallback[pointer]) {
                                                    if (DEBUG_MODE) {
                                                        TecTech.LOGGER.info("Fallback-struct-block-error " + xyz[0] + ' ' + xyz[1] + ' ' + xyz[2] + " / " + abc[0] + ' ' + abc[1] + ' ' + abc[2] + " / " + world.getBlock(xyz[0], xyz[1], xyz[2]).getUnlocalizedName() + ' ' + (blockTypeFallback[pointer] == null ? "null" : blockTypeFallback[pointer].getUnlocalizedName()));
                                                    }
                                                    return false;
                                                }
                                                if (world.getBlockMetadata(xyz[0], xyz[1], xyz[2]) != blockMetaFallback[pointer]) {
                                                    if (DEBUG_MODE) {
                                                        TecTech.LOGGER.info("Fallback-Struct-meta-id-error " + xyz[0] + ' ' + xyz[1] + ' ' + xyz[2] + " / " + abc[0] + ' ' + abc[1] + ' ' + abc[2] + " / " + world.getBlockMetadata(xyz[0], xyz[1], xyz[2]) + ' ' + blockMetaFallback[pointer]);
                                                    }
                                                    return false;
                                                }
                                            }
                                        }
                            }
                        } else if (forceCheck) {
                            return false;
                        }
                        abc[0]++;//block in horizontal layer
                    }
                }
                abc[1]--;//horizontal layer
            }
            abc[2]++;//depth
        }
        return true;
    }

    public static boolean builder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                  Block[] blockType,//use numbers 0-9 for casing types
                                  byte[] blockMeta,//use numbers 0-9 for casing types
                                  int horizontalOffset, int verticalOffset, int depthOffset,
                                  IGregTechTileEntity tileEntity, ExtendedFacing extendedFacing, boolean hintsOnly) {
        return builder(structure, blockType, blockMeta, horizontalOffset, verticalOffset, depthOffset,
                tileEntity.getWorld(),tileEntity.getXCoord(),tileEntity.getYCoord(),tileEntity.getZCoord(),
                extendedFacing, hintsOnly);
    }

    public static boolean builder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                  Block[] blockType,//use numbers 0-9 for casing types
                                  byte[] blockMeta,//use numbers 0-9 for casing types
                                  int horizontalOffset, int verticalOffset, int depthOffset,
                                  TileEntity tileEntity, ExtendedFacing extendedFacing, boolean hintsOnly) {
        return builder(structure, blockType, blockMeta, horizontalOffset, verticalOffset, depthOffset,
                tileEntity.getWorldObj(),tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord,
                extendedFacing, hintsOnly);
    }

    public static boolean builder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                  Block[] blockType,//use numbers 0-9 for casing types
                                  byte[] blockMeta,//use numbers 0-9 for casing types
                                  int horizontalOffset, int verticalOffset, int depthOffset,
                                  World world,int baseX,int baseZ,int baseY, ExtendedFacing extendedFacing, boolean hintsOnly) {
        if (world==null || (!world.isRemote && hintsOnly)) {
            return false;
        }

        //TE Rotation
        int[] xyz =new int[3];
        int[] abc =new int[3];
        int pointer;

        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!

        //perform your duties
        abc[2] = -depthOffset;
        for (String[] _structure : structure) {//front to back
            abc[1] = verticalOffset;
            for (String __structure : _structure) {//top to bottom
                abc[0] = -horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if (block < ' ') {//Control chars allow skipping
                        abc[1] -= block;
                        break;
                    }
                    if (block > '@')//characters allow to skip check a-1 skip, b-2 skips etc.
                    {
                        abc[0] += block - '@';
                    }//else if (block < '+')//used to mark THINGS
                    //    a++;
                    else if (block == '.')// this TE
                    {
                        abc[0]++;
                    } else {
                        //get x y z from rotation
                        extendedFacing.getWorldOffset(abc,xyz);
                        xyz[0]+=baseX;
                        xyz[1]+=baseY;
                        xyz[2]+=baseZ;

                        //that must be here since in some cases other axis (b,c) controls y
                        if (xyz[1] < 0 || xyz[1] >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {//this actually checks if the chunk is loaded
                            if (hintsOnly) {
                                switch (block) {
                                    case '-'://must be air
                                        TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, 13);
                                        break;
                                    case '+'://must not be air
                                        TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, 14);
                                        break;
                                    default: //check for block
                                        if ((pointer = block - '0') >= 0) {
                                            if (world.getBlock(xyz[0], xyz[1], xyz[2]) != blockType[pointer] || world.getBlockMetadata(xyz[0], xyz[1], xyz[2]) != blockMeta[pointer]) {
                                                TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], blockType[pointer], blockMeta[pointer]);
                                            }
                                        } else if ((pointer = block - ' ') >= 0) {
                                            if (pointer >= 0 && pointer < 12) {
                                                TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, pointer);
                                            } else {
                                                TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, 12);
                                            }
                                        } else {
                                            TecTech.proxy.hint_particle(world, xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, 15);
                                        }
                                }
                            } else {
                                switch (block) {
                                    case '-'://must be air
                                        world.setBlock(xyz[0], xyz[1], xyz[2], Blocks.air, 0, 2);
                                        break;
                                    case '+'://must not be air
                                        world.setBlock(xyz[0], xyz[1], xyz[2], TT_Container_Casings.sBlockCasingsTT, 14, 2);
                                        break;
                                    default: //check for block
                                        if ((pointer = block - '0') >= 0) {
                                            world.setBlock(xyz[0], xyz[1], xyz[2], blockType[pointer], blockMeta[pointer], 2);
                                        } else if (block - ' ' < 0) {
                                            world.setBlock(xyz[0], xyz[1], xyz[2], TT_Container_Casings.sHintCasingsTT, 15, 2);
                                        } //else {
                                        //switch(pointer){
                                        //    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                                        //        world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, pointer, 2); break;
                                        //    default:world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, 12, 2);
                                        //}
                                        //}
                                }
                            }
                        }
                        abc[0]++;//block in horizontal layer
                    }
                }
                abc[1]--;//horizontal layer
            }
            abc[2]++;//depth
        }
        return true;
    }


    public static String[] writer(IGregTechTileEntity aBaseMetaTileEntity,
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           int horizontalSize, int verticalSize, int depthSize, boolean ignoreAir) {
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) {
            return new String[]{"Not at Client m8"};
        }

        ItemStack[] array = new ItemStack[10];

        int x, y, z, a, b, c;
        int
                baseX = aBaseMetaTileEntity.getXCoord(),
                baseZ = aBaseMetaTileEntity.getZCoord(),
                baseY = aBaseMetaTileEntity.getYCoord();
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
                    if (y < 0 || y >= 256) {
                        return new String[]{"Invalid position"};
                    }

                    //Check block
                    Block block = world.getBlock(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);

                    if (!block.hasTileEntity(meta) && block.getMaterial() != Material.air) {
                        boolean err = true;
                        ItemStack is = new ItemStack(block, 1, meta);
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
                        if (err) {
                            return new String[]{"Too much different blocks"};
                        }
                    }

                    a++;//block in horizontal layer
                }
                b--;//horizontal layer
            }
            c++;//depth
        }

        List<String> output = new ArrayList<>();

        output.add("Offsets: " + horizontalOffset + ' ' + verticalOffset + ' ' + depthOffset);
        output.add("Sizes: " + horizontalSize + ' ' + verticalSize + ' ' + depthSize);
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
                output.add(i + ": " + array[i].getItem().getUnlocalizedName() + ' ' + array[i].getItemDamage());
            }
        }
        output.add("");
        output.add("String[][]");
        //perform your duties - #2 - write strings
        output.add("{");
        c = -depthOffset;
        for (int cz = 0; cz < depthSize; cz++) {//front to back
            b = verticalOffset;
            StringBuilder addMe = new StringBuilder().append('{');
            for (int by = 0; by < verticalSize; by++) {//top to bottom
                a = -horizontalOffset;
                StringBuilder line = new StringBuilder();
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
                        line.append('.');
                    } else if (block.getMaterial() == Material.air) {
                        line.append('-');
                    } else if (block.hasTileEntity(meta)) {
                        line.append('*');
                    } else {
                        ItemStack stack = new ItemStack(block, 1, meta);
                        String str = "?";//OH YEAH NPEs
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] != null && stack.getItem() == array[i].getItem() && stack.getItemDamage() == array[i].getItemDamage()) {
                                str = Integer.toString(i);
                                break;
                            }
                        }
                        line.append(str);
                    }
                    a++;//block in horizontal layer
                }
                if (ignoreAir) {
                    StringBuilder builder = new StringBuilder();
                    char temp = '@';
                    for (char ch : line.toString().toCharArray()) {
                        if (ch == '-') {
                            temp += 1;
                            if (temp == '~') {
                                builder.append('~');
                                temp = '@';
                            }
                        } else {
                            if (temp > '@') {
                                builder.append(temp);
                                temp = '@';
                            }
                            builder.append(ch);
                        }
                    }
                    while (builder.length() > 0 && builder.charAt(builder.length() - 1) == '~') {
                        builder.deleteCharAt(builder.length() - 1);
                    }
                    if (builder.length() == 0) {
                        builder.append("E,");
                    } else {
                        builder.insert(0, '"');
                        builder.append('"').append(',');
                    }
                    addMe.append(builder);
                } else {
                    if (line.length() == 0) {
                        line.append("E,");
                    } else {
                        line.insert(0, '"');
                        line.append('"').append(',');
                    }
                    addMe.append(line);
                }
                b--;//horizontal layer
            }
            //region less verbose
            addMe.append('}').append(',');
            String builtStr = addMe.toString().replaceAll("(E,)+(?=})", E/*Remove Empty strings at end*/);
            Matcher matcher = matchE_.matcher(builtStr);
            while (matcher.find()) {
                byte lenEE = (byte) (matcher.group(1).length() >> 1);
                builtStr = builtStr.replaceFirst("E,(E,)+", "\"\\\\u00" + String.format("%02X", lenEE - 1) + "\",");
                //builtStr=builtStr.replaceFirst("E,(E,)+\"","\"\\\\u00"+String.format("%02X", lenEE));
            }
            //endregion
            output.add(builtStr);
            c++;//depth
        }
        output.add("}");
        return output.toArray(new String[0]);
    }
}
