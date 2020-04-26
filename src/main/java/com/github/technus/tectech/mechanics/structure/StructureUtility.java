package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.util.Vec3Impl;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.*;

import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sHintCasingsTT;

public class StructureUtility {
    private static final String NICE_CHARS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789`~!@#$%^&*()_=|[]{};:'<>,./?";
    @SuppressWarnings("rawtypes")
    private static final Map<Vec3Impl,IStructureNavigate> STEP = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private static final IStructureElement AIR= new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() == Material.air;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z) {
            TecTech.proxy.hint_particle(world,x,y,z,sHintCasingsTT,13);
            return true;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement NOT_AIR= new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return world.getBlock(x, y, z).getMaterial() != Material.air;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z) {
            TecTech.proxy.hint_particle(world,x,y,z,sHintCasingsTT,14);
            return true;
        }
    };
    @SuppressWarnings("rawtypes")
    private static final IStructureElement ERROR= new IStructureElement() {
        @Override
        public boolean check(Object t, World world, int x, int y, int z) {
            return false;
        }

        @Override
        public boolean spawnHint(Object o, World world, int x, int y, int z) {
            TecTech.proxy.hint_particle(world,x,y,z,sHintCasingsTT,15);
            return true;
        }
    };

    private StructureUtility(){

    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> isAir(){
        return AIR;
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> notAir(){
        return NOT_AIR;
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureElement<T> error(){
        return ERROR;
    }    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofHintFlat(Map<Block, Integer> blocsMap,Block hintBlock,int hintMeta){
        if(blocsMap==null || blocsMap.isEmpty() || hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), -1) == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofHint(Map<Block, Set<Integer>> blocsMap,Block hintBlock,int hintMeta){
        if(blocsMap==null || blocsMap.isEmpty() || hintBlock==null){
            throw new IllegalArgumentException();
        }
        for (Set<Integer> value : blocsMap.values()) {
            if(value.isEmpty()){
                throw new IllegalArgumentException();
            }
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), Collections.emptySet()).contains(world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHint(Block block, int meta,Block hintBlock,int hintMeta){
        if(block==null || hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return block == world.getBlock(x, y, z) && meta == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHint(Block block, int meta){
        return ofHint(block, meta,block,meta);
    }

    /**
     * Does not allow Block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofBlocksFlat(Map<Block, Integer> blocsMap,Block hintBlock,int hintMeta){
        if(blocsMap==null || blocsMap.isEmpty() || hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), -1) == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z) {
                world.setBlock(x,y,z,hintBlock,hintMeta,2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    /**
     * Allows block duplicates (with different meta)
     */
    public static <T> IStructureElement<T> ofBlocks(Map<Block, Set<Integer>> blocsMap,Block hintBlock,int hintMeta){
        if(blocsMap==null || blocsMap.isEmpty() || hintBlock==null){
            throw new IllegalArgumentException();
        }
        for (Set<Integer> value : blocsMap.values()) {
            if(value.isEmpty()){
                throw new IllegalArgumentException();
            }
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return blocsMap.getOrDefault(world.getBlock(x, y, z), Collections.emptySet()).contains(world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z) {
                world.setBlock(x,y,z,hintBlock,hintMeta,2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofBlock(Block block, int meta,Block hintBlock,int hintMeta){
        if(block==null || hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return block == world.getBlock(x, y, z) && meta == world.getBlockMetadata(x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z) {
                world.setBlock(x,y,z,hintBlock,hintMeta,2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofBlock(Block block, int meta){
        return ofBlock(block, meta,block,meta);
    }

    public static <T> IStructureElement<T> ofBlockAdder(IBlockAdder<T> iBlockAdder,Block hintBlock,int hintMeta){
        if(iBlockAdder==null ||hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                return iBlockAdder.apply(t,world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z) {
                world.setBlock(x,y,z,hintBlock,hintMeta,2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofTileAdder(ITileAdder<T> iTileAdder,Block hintBlock,int hintMeta){
        if(iTileAdder==null ||hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iTileAdder.apply(t,tileEntity);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdder(IHatchAdder<T> iHatchAdder, Short textureIndex, Block hintBlock, int hintMeta){
        if(iHatchAdder==null ||hintBlock==null){
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && iHatchAdder.apply(t,(IGregTechTileEntity) tileEntity, textureIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z) {
                TecTech.proxy.hint_particle(world,x,y,z,hintBlock,hintMeta);
                return true;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> IStructureNavigate<T> step(Vec3Impl step){
        if(step==null || step.get0()<0 || step.get1()<0 || step.get2()<0){
            throw new IllegalArgumentException();
        }
        return STEP.computeIfAbsent(step, vec3 -> {
            if(vec3.get2()>0){
                return stepC(vec3.get0(), vec3.get1(), vec3.get2());
            }else if(vec3.get1()>0){
                return stepB(vec3.get0(), vec3.get1(), vec3.get2());
            }else {
                return stepA(vec3.get0(), vec3.get1(), vec3.get2());
            }
        });
    }

    public static <T> IStructureNavigate<T> step(int a,int b, int c){
        return step(new Vec3Impl(a,b,c));
    }

    private static <T> IStructureNavigate<T> stepA(int a,int b, int c){
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }
        };
    }

    private static <T> IStructureNavigate<T> stepB(int a,int b, int c){
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }

            @Override
            public boolean resetA() {
                return true;
            }
        };
    }

    private static <T> IStructureNavigate<T> stepC(int a,int b, int c){
        return new IStructureNavigate<T>() {
            @Override
            public int getStepA() {
                return a;
            }

            @Override
            public int getStepB() {
                return b;
            }

            @Override
            public int getStepC() {
                return c;
            }

            @Override
            public boolean resetA() {
                return true;
            }

            @Override
            public boolean resetB() {
                return true;
            }
        };
    }

    @SafeVarargs
    public static <T> IStructureFallback<T> ofElementChain(IStructureElement<T>... elementChain){
        if(elementChain==null || elementChain.length==0){
            throw new IllegalArgumentException();
        }
        for (IStructureElement<T> iStructureElement : elementChain) {
            if(iStructureElement==null){
                throw new IllegalArgumentException();
            }
        }
        return () -> elementChain;
    }

    /**
     * Use only to get pseudo code...
     * @param world
     * @return
     */
    public static String getPseudoJavaCode(World world, ExtendedFacing extendedFacing,
                                           int basePositionX, int basePositionY, int basePositionZ,
                                           int basePositionA, int basePositionB, int basePositionC,
                                           int sizeA,int sizeB, int sizeC) {
        Map<Block, Set<Integer>> blocks = new TreeMap<>(Comparator.comparing(Block::getUnlocalizedName));
        Set<Class<? extends TileEntity>> tiles = new TreeSet<>(Comparator.comparing(Class::getCanonicalName));
        Set<Class<? extends IMetaTileEntity>> gtTiles = new TreeSet<>(Comparator.comparing(Class::getCanonicalName));
        iterate(world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,
                sizeA, sizeB, sizeC, ((w, x, y, z) -> {
                    TileEntity tileEntity = w.getTileEntity(x, y, z);
                    if (tileEntity == null) {
                        Block block = w.getBlock(x, y, z);
                        if (block != null && block != Blocks.air) {
                            blocks.compute(block, (b, set) -> {
                                if (set == null) {
                                    set = new TreeSet<>();
                                }
                                set.add(world.getBlockMetadata(x, y, z));
                                return set;
                            });
                        }
                    } else {
                        if (tileEntity instanceof IGregTechTileEntity) {
                            IMetaTileEntity meta = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                            if (meta != null) {
                                gtTiles.add(meta.getClass());
                            } else {
                                tiles.add(tileEntity.getClass());
                            }
                        } else {
                            tiles.add(tileEntity.getClass());
                        }
                    }
                }));
        Map<String, Character> map = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        {
            int i = 0;
            char c;
            builder.append("\n\nStructure:\n")
                    .append("\nBlocks:\n");
            for (Map.Entry<Block, Set<Integer>> entry : blocks.entrySet()) {
                Block block = entry.getKey();
                Set<Integer> set = entry.getValue();
                for (Integer meta : set) {
                    c = NICE_CHARS.charAt(i++);
                    if(i>NICE_CHARS.length()){
                        return "Too complicated for nice chars";
                    }
                    map.put(block.getUnlocalizedName() + '\0' + meta, c);
                    builder.append(c).append(" -> ofBlock...(")
                            .append(block.getUnlocalizedName()).append(", ").append(meta).append(", ...);\n");
                }
            }
            builder.append("\nTiles:\n");
            for (Class<? extends TileEntity> tile : tiles) {
                c = NICE_CHARS.charAt(i++);
                if(i>NICE_CHARS.length()){
                    return "Too complicated for nice chars";
                }
                map.put(tile.getCanonicalName(), c);
                builder.append(c).append(" -> ofTileAdder(")
                        .append(tile.getCanonicalName()).append(", ...);\n");
            }
            builder.append("\nMeta:\n");
            for (Class<? extends IMetaTileEntity> gtTile : gtTiles) {
                c = NICE_CHARS.charAt(i++);
                if(i>NICE_CHARS.length()){
                    return "Too complicated for nice chars";
                }
                map.put(gtTile.getCanonicalName(), c);
                builder.append(c).append(" -> ofHatchAdder(")
                        .append(gtTile.getCanonicalName()).append(", textureId, ...);\n");
            }
        }
        builder.append("\nOffsets:\n")
                .append(basePositionA).append(' ').append(basePositionB).append(' ').append(basePositionC).append('\n');
        builder.append("\nScan:\n")
                .append("new String[][]{{\n")
                .append("    \"");

        iterate(world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,
                sizeA, sizeB, sizeC, ((w, x, y, z) -> {
                    TileEntity tileEntity = w.getTileEntity(x, y, z);
                    if (tileEntity == null) {
                        Block block = w.getBlock(x, y, z);
                        if (block != null && block != Blocks.air) {
                            builder.append(map.get(block.getUnlocalizedName() + '\0' + world.getBlockMetadata(x, y, z)));
                        }else {
                            builder.append(' ');
                        }
                    } else {
                        if (tileEntity instanceof IGregTechTileEntity) {
                            IMetaTileEntity meta = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                            if (meta != null) {
                                builder.append(map.get(meta.getClass().getCanonicalName()));
                            } else {
                                builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                            }
                        } else {
                            builder.append(map.get(tileEntity.getClass().getCanonicalName()));
                        }
                    }
                }),
                () -> builder.append("\",\n").append("    \""),
                () -> {
                    builder.setLength(builder.length()-7);
                    builder.append("\n").append("},{\n").append("    \"");
                });
        builder.setLength(builder.length()-8);
        builder.append("};\n\n");
        return(builder.toString().replaceAll("\"\"","E"));
    }

    public static void iterate(World world, ExtendedFacing extendedFacing,
                                  int basePositionX, int basePositionY, int basePositionZ,
                                  int basePositionA, int basePositionB, int basePositionC,
                                  int sizeA,int sizeB, int sizeC,
                                  IBlockPosConsumer iBlockPosConsumer){
        sizeA-=basePositionA;
        sizeB-=basePositionB;
        sizeC-=basePositionC;

        int[] abc = new int[3];
        int[] xyz = new int[3];

        for (abc[2]=-basePositionC ; abc[2] < sizeC; abc[2]++) {
            for (abc[1]=-basePositionB; abc[1] < sizeB; abc[1]++) {
                for (abc[0]=-basePositionA ; abc[0] < sizeA; abc[0]++) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    iBlockPosConsumer.consume(world, xyz[0]+basePositionX,xyz[1]+basePositionY,xyz[2]+basePositionZ);
                }

            }
        }
    }

    public static void iterate(World world, ExtendedFacing extendedFacing,
                                  int basePositionX, int basePositionY, int basePositionZ,
                                  int basePositionA, int basePositionB, int basePositionC,
                                  int sizeA, int sizeB, int sizeC,
                                  IBlockPosConsumer iBlockPosConsumer,
                                  Runnable nextB,
                                  Runnable nextC){
        sizeA-=basePositionA;
        sizeB-=basePositionB;
        sizeC-=basePositionC;

        int[] abc = new int[3];
        int[] xyz = new int[3];

        for (abc[2]=-basePositionC ; abc[2] < sizeC; abc[2]++) {
            for (abc[1]=-basePositionB; abc[1] < sizeB; abc[1]++) {
                for (abc[0]=-basePositionA ; abc[0] < sizeA; abc[0]++) {
                    extendedFacing.getWorldOffset(abc, xyz);
                    iBlockPosConsumer.consume(world, xyz[0]+basePositionX,xyz[1]+basePositionY,xyz[2]+basePositionZ);
                }
                nextB.run();
            }
            nextC.run();
        }
    }
}
