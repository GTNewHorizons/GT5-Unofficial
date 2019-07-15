package com.github.technus.tectech;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.IFrontRotation;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static gregtech.api.enums.GT_Values.E;
import static java.nio.charset.Charset.forName;

/**
 * Created by Tec on 21.03.2017.
 */
public final class Util {
    private Util() {
    }

    @SuppressWarnings("ComparatorMethodParameterNotUsed")
    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
                (e1, e2) -> {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static String intBitsToString(int number) {
        StringBuilder result = new StringBuilder(16);

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % 8 == 0) {
                result.append(' ');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String intBitsToShortString(int number) {
        StringBuilder result = new StringBuilder(35);

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0) {
                result.append('|');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String longBitsToShortString(long number) {
        StringBuilder result = new StringBuilder(71);

        for (int i = 63; i >= 0; i--) {
            long mask = 1L << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0) {
                result.append('|');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    //region junk
    /*
    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StructureChecker(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR-1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           IGregTechTileEntity aBaseMetaTileEntity,
                                           boolean forceCheck) {
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) {
            return false;
        }
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();

        int x, y, z, a, b, c, pointer;
        int
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
                    {
                        a += block - '@';
                    } else if (block < '+')//used to mark THINGS
                    {
                        a++;
                    } else if (block=='.') {
                        a++;
                    } else {
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
                        if (y < 0 || y >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(x, y, z).getMaterial() != Material.air) {
                                        return false;
                                    }
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(x, y, z).getMaterial() == Material.air) {
                                        return false;
                                    }
                                    break;
                                default: //check for block (countable)
                                    pointer = block - '0';
                                    //countable air -> net.minecraft.block.BlockAir
                                    if (world.getBlock(x, y, z) != blockType[pointer]) {
                                        if (DEBUG_MODE) {
                                            TecTech.LOGGER.info("Struct-block-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + ' ' + blockType[pointer].getUnlocalizedName());
                                        }
                                        return false;
                                    }
                                    if (world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                        if (DEBUG_MODE) {
                                            TecTech.LOGGER.info("Struct-meta-id-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlockMetadata(x, y, z) + ' ' + blockMeta[pointer]);
                                        }
                                        return false;
                                    }
                            }
                        } else if (forceCheck) {
                            return false;
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
        if (world.isRemote) {
            return false;
        }
        //TE Rotation
        byte facing = aBaseMetaTileEntity.getFrontFacing();

        IGregTechTileEntity igt;
        IMetaTileEntity imt = aBaseMetaTileEntity.getMetaTileEntity();

        int x, y, z, a, b, c, pointer;
        int     baseX=aBaseMetaTileEntity.getXCoord(),
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
                    } else if (block > '@') //characters allow to skip check A-1 skip, B-2 skips etc.
                    {
                        a += block - '@';
                    }//else if (block < '+')//used to mark THINGS
                  //    a++;
                    else if (block=='.') {
                        a++;
                    } else {
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
                        if (y < 0 || y >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(x, y, z).getMaterial() != Material.air) {
                                        return false;
                                    }
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(x, y, z).getMaterial() == Material.air) {
                                        return false;
                                    }
                                    break;
                                default://check for block (countable)
                                    if ((pointer = block - '0') >= 0) {
                                        //countable air -> net.minecraft.block.BlockAir
                                        if (world.getBlock(x, y, z) != blockType[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-block-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + ' ' + blockType[pointer].getUnlocalizedName());
                                            }
                                            return false;
                                        }
                                        if (world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-meta-id-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlockMetadata(x, y, z) + ' ' + blockMeta[pointer]);
                                            }
                                            return false;
                                        }
                                    } else if ((pointer = block - ' ') >= 0) {
                                        igt = aBaseMetaTileEntity.getIGregTechTileEntity(x, y, z);
                                        try {
                                            if (igt == null || !(boolean) adder.invoke(imt, addingMethods[pointer], igt, casingTextures[pointer])) {
                                                if (world.getBlock(x, y, z) != blockTypeFallback[pointer]) {
                                                    if (DEBUG_MODE) {
                                                        TecTech.LOGGER.info("Fallback-struct-block-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + ' ' + (blockTypeFallback[pointer] == null ? "null" : blockTypeFallback[pointer].getUnlocalizedName()));
                                                    }
                                                    return false;
                                                }
                                                if (world.getBlockMetadata(x, y, z) != blockMetaFallback[pointer]) {
                                                    if (DEBUG_MODE) {
                                                        TecTech.LOGGER.info("Fallback-Struct-meta-id-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlockMetadata(x, y, z) + ' ' + blockMetaFallback[pointer]);
                                                    }
                                                    return false;
                                                }
                                            }
                                        } catch (InvocationTargetException | IllegalAccessException e) {
                                            if (DEBUG_MODE) {
                                                e.printStackTrace();
                                            }
                                            return false;
                                        }
                                    }
                            }
                        } else if (forceCheck) {
                            return false;
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
    */
    //endregion


    //Check Machine Structure based on string[][] (effectively char[][][]), ond offset of the controller
    //This only checks for REGULAR BLOCKS!
    public static boolean StructureCheckerExtreme(
            String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR-1 blocks
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            IHatchAdder[] addingMethods,
            short[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset,
            IGregTechTileEntity aBaseMetaTileEntity,
            IFrontRotation frontRotation,
            boolean forceCheck) {
        World world = aBaseMetaTileEntity.getWorld();
        if (world.isRemote) {
            return false;
        }
        //TE Rotation
        int facingAndRotation = aBaseMetaTileEntity.getFrontFacing() + (frontRotation == null ? 0 : (frontRotation.getFrontRotation() << 3));

        IGregTechTileEntity igt;
        IMetaTileEntity imt = aBaseMetaTileEntity.getMetaTileEntity();

        int x, y, z, a, b, c, pointer;
        int baseX = aBaseMetaTileEntity.getXCoord(),
                baseZ = aBaseMetaTileEntity.getZCoord(),
                baseY = aBaseMetaTileEntity.getYCoord();
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
                    } else if (block > '@') //characters allow to skip check A-1 skip, B-2 skips etc.
                    {
                        a += block - '@';
                    }//else if (block < '+')//used to mark THINGS
                    //    a++;
                    else if (block == '.') {
                        a++;
                    } else {
                        //get x y z from rotation
                        switch (facingAndRotation) {//translation
                            case 4:
                                x = baseX + c;
                                z = baseZ + a;
                                y = baseY + b;
                                break;
                            case 12:
                                x = baseX + c;
                                y = baseY - a;
                                z = baseZ + b;
                                break;
                            case 20:
                                x = baseX + c;
                                z = baseZ - a;
                                y = baseY - b;
                                break;
                            case 28:
                                x = baseX + c;
                                y = baseY + a;
                                z = baseZ - b;
                                break;

                            case 3:
                                x = baseX + a;
                                z = baseZ - c;
                                y = baseY + b;
                                break;
                            case 11:
                                y = baseY - a;
                                z = baseZ - c;
                                x = baseX + b;
                                break;
                            case 19:
                                x = baseX - a;
                                z = baseZ - c;
                                y = baseY - b;
                                break;
                            case 27:
                                y = baseY + a;
                                z = baseZ - c;
                                x = baseX - b;
                                break;

                            case 5:
                                x = baseX - c;
                                z = baseZ - a;
                                y = baseY + b;
                                break;
                            case 13:
                                x = baseX - c;
                                y = baseY - a;
                                z = baseZ - b;
                                break;
                            case 21:
                                x = baseX - c;
                                z = baseZ + a;
                                y = baseY - b;
                                break;
                            case 29:
                                x = baseX - c;
                                y = baseY + a;
                                z = baseZ + b;
                                break;

                            case 2:
                                x = baseX - a;
                                z = baseZ + c;
                                y = baseY + b;
                                break;
                            case 10:
                                y = baseY - a;
                                z = baseZ + c;
                                x = baseX - b;
                                break;
                            case 18:
                                x = baseX + a;
                                z = baseZ + c;
                                y = baseY - b;
                                break;
                            case 26:
                                y = baseY + a;
                                z = baseZ + c;
                                x = baseX + b;
                                break;
                            //Things get odd if the block faces up or down...
                            case 1:
                                x = baseX + a;
                                z = baseZ - b;
                                y = baseY - c;
                                break;//similar to 3
                            case 9:
                                z = baseZ + a;
                                x = baseX + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 17:
                                x = baseX - a;
                                z = baseZ + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 25:
                                z = baseZ - a;
                                x = baseX - b;
                                y = baseY - c;
                                break;//similar to 3

                            case 0:
                                x = baseX - a;
                                z = baseZ - b;
                                y = baseY + c;
                                break;//similar to 2
                            case 8:
                                z = baseZ + a;
                                x = baseX - b;
                                y = baseY + c;
                                break;
                            case 16:
                                x = baseX + a;
                                z = baseZ + b;
                                y = baseY + c;
                                break;
                            case 24:
                                z = baseZ - a;
                                x = baseX + b;
                                y = baseY + c;
                                break;
                            default:
                                if (DEBUG_MODE) {
                                    TecTech.LOGGER.info("facing = " + facingAndRotation);
                                }
                                return false;
                        }

                        //that must be here since in some cases other axis (b,c) controls y
                        if (y < 0 || y >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded at this pos
                            switch (block) {
                                case '-'://must be air
                                    if (world.getBlock(x, y, z).getMaterial() != Material.air) {
                                        return false;
                                    }
                                    break;
                                case '+'://must not be air
                                    if (world.getBlock(x, y, z).getMaterial() == Material.air) {
                                        return false;
                                    }
                                    break;
                                default://check for block (countable)
                                    if ((pointer = block - '0') >= 0) {
                                        //countable air -> net.minecraft.block.BlockAir
                                        if (world.getBlock(x, y, z) != blockType[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-block-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + ' ' + blockType[pointer].getUnlocalizedName());
                                            }
                                            return false;
                                        }
                                        if (world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                            if (DEBUG_MODE) {
                                                TecTech.LOGGER.info("Struct-meta-id-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlockMetadata(x, y, z) + ' ' + blockMeta[pointer]);
                                            }
                                            return false;
                                        }
                                    } else //noinspection ConstantConditions
                                        if ((pointer = block - ' ') >= 0) {
                                        igt = aBaseMetaTileEntity.getIGregTechTileEntity(x, y, z);
                                        if (igt == null || !addingMethods[pointer].apply(igt, casingTextures[pointer])) {
                                            if (world.getBlock(x, y, z) != blockTypeFallback[pointer]) {
                                                if (DEBUG_MODE) {
                                                    TecTech.LOGGER.info("Fallback-struct-block-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlock(x, y, z).getUnlocalizedName() + ' ' + (blockTypeFallback[pointer] == null ? "null" : blockTypeFallback[pointer].getUnlocalizedName()));
                                                }
                                                return false;
                                            }
                                            if (world.getBlockMetadata(x, y, z) != blockMetaFallback[pointer]) {
                                                if (DEBUG_MODE) {
                                                    TecTech.LOGGER.info("Fallback-Struct-meta-id-error " + x + ' ' + y + ' ' + z + " / " + a + ' ' + b + ' ' + c + " / " + world.getBlockMetadata(x, y, z) + ' ' + blockMetaFallback[pointer]);
                                                }
                                                return false;
                                            }
                                        }
                                    }
                            }
                        } else if (forceCheck) {
                            return false;
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

    public static boolean StructureBuilder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           IGregTechTileEntity aBaseMetaTileEntity, boolean hintsOnly) {
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        return StructureBuilderExtreme(structure, blockType, blockMeta,
                horizontalOffset, verticalOffset, depthOffset,
                aBaseMetaTileEntity.getWorld().getTileEntity(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord()), null,
                facing, hintsOnly);
    }

    public static boolean StructureBuilderExtreme(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                                  Block[] blockType,//use numbers 0-9 for casing types
                                                  byte[] blockMeta,//use numbers 0-9 for casing types
                                                  int horizontalOffset, int verticalOffset, int depthOffset,
                                                  IGregTechTileEntity aBaseMetaTileEntity, IFrontRotation frontRotation, boolean hintsOnly) {
        byte facing = aBaseMetaTileEntity.getFrontFacing();
        return StructureBuilderExtreme(structure, blockType, blockMeta,
                horizontalOffset, verticalOffset, depthOffset,
                aBaseMetaTileEntity.getWorld().getTileEntity(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord()), frontRotation,
                facing, hintsOnly);
    }

    public static boolean StructureBuilder(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                           Block[] blockType,//use numbers 0-9 for casing types
                                           byte[] blockMeta,//use numbers 0-9 for casing types
                                           int horizontalOffset, int verticalOffset, int depthOffset,
                                           TileEntity tileEntity, int facing, boolean hintsOnly) {
        return StructureBuilderExtreme(structure, blockType, blockMeta, horizontalOffset, verticalOffset, depthOffset, tileEntity, null, facing, hintsOnly);
    }

    public static boolean StructureBuilderExtreme(String[][] structure,//0-9 casing, +- air no air, A... ignore 'A'-CHAR+1 blocks
                                                  Block[] blockType,//use numbers 0-9 for casing types
                                                  byte[] blockMeta,//use numbers 0-9 for casing types
                                                  int horizontalOffset, int verticalOffset, int depthOffset,
                                                  TileEntity tileEntity, IFrontRotation frontRotation, int facing, boolean hintsOnly) {
        if (!tileEntity.hasWorldObj()) {
            return false;
        }
        World world = tileEntity.getWorldObj();
        if (!world.isRemote && hintsOnly) {
            return false;
        }

        //TE Rotation

        int x, y, z, a, b, c, pointer;
        int
                baseX = tileEntity.xCoord,
                baseZ = tileEntity.zCoord,
                baseY = tileEntity.yCoord;
        //a,b,c - relative to block face!
        //x,y,z - relative to block position on map!
        if (frontRotation != null) {
            facing += frontRotation.getFrontRotation() << 3;
        }

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
                    }
                    if (block > '@')//characters allow to skip check a-1 skip, b-2 skips etc.
                    {
                        a += block - '@';
                    }//else if (block < '+')//used to mark THINGS
                    //    a++;
                    else if (block == '.')// this TE
                    {
                        a++;
                    } else {
                        //get x y z from rotation
                        switch (facing) {
                            case 4:
                                x = baseX + c;
                                z = baseZ + a;
                                y = baseY + b;
                                break;
                            case 12:
                                x = baseX + c;
                                y = baseY - a;
                                z = baseZ + b;
                                break;
                            case 20:
                                x = baseX + c;
                                z = baseZ - a;
                                y = baseY - b;
                                break;
                            case 28:
                                x = baseX + c;
                                y = baseY + a;
                                z = baseZ - b;
                                break;

                            case 3:
                                x = baseX + a;
                                z = baseZ - c;
                                y = baseY + b;
                                break;
                            case 11:
                                y = baseY - a;
                                z = baseZ - c;
                                x = baseX + b;
                                break;
                            case 19:
                                x = baseX - a;
                                z = baseZ - c;
                                y = baseY - b;
                                break;
                            case 27:
                                y = baseY + a;
                                z = baseZ - c;
                                x = baseX - b;
                                break;

                            case 5:
                                x = baseX - c;
                                z = baseZ - a;
                                y = baseY + b;
                                break;
                            case 13:
                                x = baseX - c;
                                y = baseY - a;
                                z = baseZ - b;
                                break;
                            case 21:
                                x = baseX - c;
                                z = baseZ + a;
                                y = baseY - b;
                                break;
                            case 29:
                                x = baseX - c;
                                y = baseY + a;
                                z = baseZ + b;
                                break;

                            case 2:
                                x = baseX - a;
                                z = baseZ + c;
                                y = baseY + b;
                                break;
                            case 10:
                                y = baseY - a;
                                z = baseZ + c;
                                x = baseX - b;
                                break;
                            case 18:
                                x = baseX + a;
                                z = baseZ + c;
                                y = baseY - b;
                                break;
                            case 26:
                                y = baseY + a;
                                z = baseZ + c;
                                x = baseX + b;
                                break;
                            //Things get odd if the block faces up or down...
                            case 1:
                                x = baseX + a;
                                z = baseZ - b;
                                y = baseY - c;
                                break;//similar to 3
                            case 9:
                                z = baseZ + a;
                                x = baseX + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 17:
                                x = baseX - a;
                                z = baseZ + b;
                                y = baseY - c;
                                break;//similar to 3
                            case 25:
                                z = baseZ - a;
                                x = baseX - b;
                                y = baseY - c;
                                break;//similar to 3

                            case 0:
                                x = baseX - a;
                                z = baseZ - b;
                                y = baseY + c;
                                break;//similar to 2
                            case 8:
                                z = baseZ + a;
                                x = baseX - b;
                                y = baseY + c;
                                break;
                            case 16:
                                x = baseX + a;
                                z = baseZ + b;
                                y = baseY + c;
                                break;
                            case 24:
                                z = baseZ - a;
                                x = baseX + b;
                                y = baseY + c;
                                break;
                            default:
                                if (DEBUG_MODE) {
                                    TecTech.LOGGER.info("facing = " + facing);
                                }
                                return false;
                        }

                        //that must be here since in some cases other axis (b,c) controls y
                        if (y < 0 || y >= 256) {
                            return false;
                        }

                        //Check block
                        if (world.blockExists(x, y, z)) {//this actually checks if the chunk is loaded
                            if (hintsOnly) {
                                switch (block) {
                                    case '-'://must be air
                                        TecTech.proxy.hint_particle(world, x, y, z, TT_Container_Casings.sHintCasingsTT, 13);
                                        break;
                                    case '+'://must not be air
                                        TecTech.proxy.hint_particle(world, x, y, z, TT_Container_Casings.sHintCasingsTT, 14);
                                        break;
                                    default: //check for block
                                        if ((pointer = block - '0') >= 0) {
                                            if (world.getBlock(x, y, z) != blockType[pointer] || world.getBlockMetadata(x, y, z) != blockMeta[pointer]) {
                                                TecTech.proxy.hint_particle(world, x, y, z, blockType[pointer], blockMeta[pointer]);
                                            }
                                        } else if ((pointer = block - ' ') >= 0) {
                                            if (pointer >= 0 && pointer < 12) {
                                                TecTech.proxy.hint_particle(world, x, y, z, TT_Container_Casings.sHintCasingsTT, pointer);
                                            } else {
                                                TecTech.proxy.hint_particle(world, x, y, z, TT_Container_Casings.sHintCasingsTT, 12);
                                            }
                                        } else {
                                            TecTech.proxy.hint_particle(world, x, y, z, TT_Container_Casings.sHintCasingsTT, 15);
                                        }
                                }
                            } else {
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
                                        } else if (block - ' ' < 0) {
                                            world.setBlock(x, y, z, TT_Container_Casings.sHintCasingsTT, 15, 2);
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

    private static final Pattern matchE_ = Pattern.compile("(E,(E,)+)");

    public static boolean isInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes, FluidStack[] requiredFluidInputs, ItemStack[] requiredInputs, FluidStack[] givenFluidInputs, ItemStack... givenInputs) {
        if (!GregTech_API.sPostloadFinished) {
            return false;
        }
        if (requiredFluidInputs.length > 0 && givenFluidInputs == null) {
            return false;
        }
        int amt;
        for (FluidStack tFluid : requiredFluidInputs) {
            if (tFluid != null) {
                boolean temp = true;
                amt = tFluid.amount;
                for (FluidStack aFluid : givenFluidInputs) {
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
                }
                if (temp) {
                    return false;
                }
            }
        }

        if (requiredInputs.length > 0 && givenInputs == null) {
            return false;
        }
        for (ItemStack tStack : requiredInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : givenInputs) {
                    if (GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) {
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
                if (temp) {
                    return false;
                }
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
                            if (GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) {
                                if (aDontCheckStackSizes) {
                                    aStack.stackSize -= amt;
                                    break;
                                }
                                if (aStack.stackSize < amt) {
                                    amt -= aStack.stackSize;
                                    aStack.stackSize = 0;
                                } else {
                                    aStack.stackSize -= amt;
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
        return GameRegistry.findUniqueIdentifierFor(is.getItem()).modId + ':' + is.getUnlocalizedName();
    }

    public static byte getTier(long l) {
        byte b = -1;

        do {
            ++b;
            if (b >= CommonValues.V.length) {
                return b;
            }
        } while (l > CommonValues.V[b]);

        return b;
    }

    public static String[] splitButDifferent(String string, String delimiter) {
        String[] strings = new String[StringUtils.countMatches(string, delimiter) + 1];
        int lastEnd = 0;
        for (int i = 0; i < strings.length - 1; i++) {
            int nextEnd = string.indexOf(delimiter, lastEnd);
            strings[i] = string.substring(lastEnd, nextEnd);
            lastEnd = nextEnd + delimiter.length();
        }
        strings[strings.length - 1] = string.substring(lastEnd);
        return strings;
    }

    public static String[] infoFromNBT(NBTTagCompound nbt) {
        String[] strings = new String[nbt.getInteger("i")];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = nbt.getString(Integer.toString(i));
        }
        return strings;
    }

    public static boolean areBitsSet(int setBits, int testedValue) {
        return (testedValue & setBits) == setBits;
    }

    public static class ItemStack_NoNBT implements Comparable<ItemStack_NoNBT> {
        public final Item mItem;
        public final int mStackSize;
        public final int mMetaData;

        public ItemStack_NoNBT(Item aItem, long aStackSize, long aMetaData) {
            this.mItem = aItem;
            this.mStackSize = (byte) ((int) aStackSize);
            this.mMetaData = (short) ((int) aMetaData);
        }

        public ItemStack_NoNBT(ItemStack aStack) {
            if (aStack == null) {
                mItem = null;
                mStackSize = mMetaData = 0;
            } else {
                mItem = aStack.getItem();
                mStackSize = aStack.stackSize;
                mMetaData = Items.feather.getDamage(aStack);
            }
        }

        @Override
        public int compareTo(ItemStack_NoNBT o) {
            if (mMetaData > o.mMetaData) return 1;
            if (mMetaData < o.mMetaData) return -1;
            if (mStackSize > o.mStackSize) return 1;
            if (mStackSize < o.mStackSize) return -1;
            if (mItem != null && o.mItem != null)
                return mItem.getUnlocalizedName().compareTo(o.mItem.getUnlocalizedName());
            if (mItem == null && o.mItem == null) return 0;
            if (mItem != null) return 1;
            return -1;
        }

        @Override
        public boolean equals(Object aStack) {
            return aStack == this ||
                    (aStack instanceof ItemStack_NoNBT &&
                            ((mItem == ((ItemStack_NoNBT) aStack).mItem) || ((ItemStack_NoNBT) aStack).mItem.getUnlocalizedName().equals(this.mItem.getUnlocalizedName())) &&
                            ((ItemStack_NoNBT) aStack).mStackSize == this.mStackSize &&
                            ((ItemStack_NoNBT) aStack).mMetaData == this.mMetaData);
        }

        @Override
        public int hashCode() {
            return (mItem != null ? mItem.getUnlocalizedName().hashCode() : 0) ^ (mMetaData << 16) ^ (mStackSize << 24);
        }

        @Override
        public String toString() {
            return Integer.toString(hashCode()) + ' ' + (mItem == null ? "null" : mItem.getUnlocalizedName()) + ' ' + mMetaData + ' ' + mStackSize;
        }
    }

    public static void setTier(int tier,Object me){
        try{
            Field field=GT_MetaTileEntity_TieredMachineBlock.class.getField("mTier");
            field.setAccessible(true);
            field.set(me,(byte)tier);
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    public static double receiveDouble(double previousValue, int startIndex, int index, int value){
        return Double.longBitsToDouble(receiveLong(Double.doubleToLongBits(previousValue),startIndex,index,value));
    }

    public static long receiveLong(long previousValue, int startIndex, int index, int value){
        value &=0xFFFF;
        switch (index-startIndex){
            case 0:
                previousValue&= 0xFFFF_FFFF_FFFF_0000L;
                previousValue|=value;
                break;
            case 1:
                previousValue&=0xFFFF_FFFF_0000_FFFFL;
                previousValue|=value<<16;
                break;
            case 2:
                previousValue&=0xFFFF_0000_FFFF_FFFFL;
                previousValue|=(long)value<<32;
                break;
            case 3:
                previousValue&=0x0000_FFFF_FFFF_FFFFL;
                previousValue|=(long)value<<48;
                break;
        }
        return previousValue;
    }

    public static void sendDouble(double value,Container container, ICrafting crafter,int startIndex){
        sendLong(Double.doubleToLongBits(value),container,crafter,startIndex);
    }

    public static void sendLong(long value,Container container, ICrafting crafter,int startIndex){
        crafter.sendProgressBarUpdate(container, startIndex++, (int)(value & 0xFFFFL));
        crafter.sendProgressBarUpdate(container, startIndex++, (int)((value & 0xFFFF0000L)>>>16));
        crafter.sendProgressBarUpdate(container, startIndex++, (int)((value & 0xFFFF00000000L)>>>32));
        crafter.sendProgressBarUpdate(container, startIndex,   (int)((value & 0xFFFF000000000000L)>>>48));
    }

    public static float receiveFloat(float previousValue, int startIndex, int index, int value){
        return Float.intBitsToFloat(receiveInteger(Float.floatToIntBits(previousValue),startIndex,index,value));
    }

    public static int receiveInteger(int previousValue, int startIndex, int index, int value){
        value &=0xFFFF;
        switch (index-startIndex){
            case 0:
                previousValue&= 0xFFFF_0000;
                previousValue|=value;
                break;
            case 1:
                previousValue&=0x0000_FFFF;
                previousValue|=value<<16;
                break;
        }
        return previousValue;
    }

    public static void sendFloat(float value,Container container, ICrafting crafter,int startIndex){
        sendInteger(Float.floatToIntBits(value),container,crafter,startIndex);
    }

    public static void sendInteger(int value,Container container, ICrafting crafter,int startIndex){
        crafter.sendProgressBarUpdate(container, startIndex++, (int)(value & 0xFFFFL));
        crafter.sendProgressBarUpdate(container, startIndex, (value & 0xFFFF0000)>>>16);
    }

    public static String doubleToString(double value){
        if(value==(long)value){
            return Long.toString((long)value);
        }
        return Double.toString(value);
    }

    public static boolean checkChunkExist(World world, ChunkCoordIntPair chunk){
        int x=chunk.getCenterXPos();
        int z=chunk.getCenterZPosition();
        return world.checkChunksExist(x, 0, z, x, 0, z);
    }

    public static NBTTagCompound getPlayerData(UUID uuid1,UUID uuid2,String extension) {
        try {
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                if (uuid1 != null && uuid2!=null) {
                    IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler().getSaveHandler();
                    SaveHandler sh = (SaveHandler)playerNBTManagerObj;
                    File dir = ObfuscationReflectionHelper.getPrivateValue(SaveHandler.class, sh, new String[]{"playersDirectory", "field_75771_c"});
                    String id1=uuid1.toString();
                    NBTTagCompound tagCompound=read(new File(dir, id1 + "."+extension));
                    if(tagCompound!=null){
                        return tagCompound;
                    }
                    tagCompound=readBackup(new File(dir, id1 + "."+extension+"_bak"));
                    if(tagCompound!=null){
                        return tagCompound;
                    }
                    String id2=uuid2.toString();
                    tagCompound=read(new File(dir, id2 + "."+extension));
                    if(tagCompound!=null){
                        return tagCompound;
                    }
                    tagCompound=readBackup(new File(dir, id2 + "."+extension+"_bak"));
                    if(tagCompound!=null){
                        return tagCompound;
                    }
                }
            }
        } catch (Exception ignored) {}
        return new NBTTagCompound();
    }

    public static void savePlayerFile(EntityPlayer player,String extension, NBTTagCompound data) {
        try {
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                if (player != null) {
                    IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler().getSaveHandler();
                    SaveHandler sh = (SaveHandler)playerNBTManagerObj;
                    File dir = ObfuscationReflectionHelper.getPrivateValue(SaveHandler.class, sh, new String[]{"playersDirectory", "field_75771_c"});
                    String id1=player.getUniqueID().toString();
                    write(new File(dir, id1 + "."+extension),data);
                    write(new File(dir, id1 + "."+extension+"_bak"),data);
                    String id2=UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8"))).toString();
                    write(new File(dir, id2 + "."+extension),data);
                    write(new File(dir, id2 + "."+extension+"_bak"),data);
                }
            }
        } catch (Exception ignored) {}
    }

    private static NBTTagCompound read(File file){
        if (file != null && file.exists()) {
            try(FileInputStream fileInputStream= new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(fileInputStream);
            } catch (Exception var9) {
                TecTech.LOGGER.error("Cannot read NBT File: "+file.getAbsolutePath());
            }
        }
        return null;
    }

    private static NBTTagCompound readBackup(File file){
        if (file != null && file.exists()) {
            try(FileInputStream fileInputStream= new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(fileInputStream);
            } catch (Exception var9) {
                TecTech.LOGGER.error("Cannot read NBT File: "+file.getAbsolutePath());
                return new NBTTagCompound();
            }
        }
        return null;
    }

    private static void write(File file,NBTTagCompound tagCompound){
        if (file != null) {
            if(tagCompound==null){
                if(file.exists()) file.delete();
            }else {
                try(FileOutputStream fileOutputStream= new FileOutputStream(file)) {
                    CompressedStreamTools.writeCompressed(tagCompound,fileOutputStream);
                } catch (Exception var9) {
                    TecTech.LOGGER.error("Cannot write NBT File: "+file.getAbsolutePath());
                }
            }
        }
    }

    public static AxisAlignedBB fromChunkCoordIntPair(ChunkCoordIntPair chunkCoordIntPair){
        int x=chunkCoordIntPair.chunkXPos<<4;
        int z=chunkCoordIntPair.chunkZPos<<4;
        return AxisAlignedBB.getBoundingBox(x,-128,z,x+16,512,z+16);
    }

    public static AxisAlignedBB fromChunk(Chunk chunk){
        int x=chunk.xPosition<<4;
        int z=chunk.zPosition<<4;
        return AxisAlignedBB.getBoundingBox(x,-128,z,x+16,512,z+16);
    }
}
