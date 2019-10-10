/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.ASM;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class BWCoreTransformer implements IClassTransformer {
    public static final String[] DESCRIPTIONFORCONFIG = {
            "REMOVING RAIN FROM LAST MILLENIUM (EXU)",
            "REMVOING CREATURES FROM LAST MILLENIUM (EXU)",
            "PATCHING GLOBAL RENDERER FOR USE WITH MY GALACTIC DIMS",
            "PATCHING THAUMCRAFT WAND PEDESTAL TO PREVENT VIS DUPLICATION",
            "PLACING MY GLASS-BLOCK RUNNABLE INTO THE GT_API",
            "DUCTTAPING RWG WORLDEN FAILS"
           // "ADD EXECTION HANDLEING TO FIND OREIDS/OREDICT"
    };
    public static final String[] CLASSESBEEINGTRANSFORMED = {
            "com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime",
            "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime",
            "net.minecraft.client.renderer.RenderGlobal",
            "thaumcraft.common.tiles.TileWandPedestal",
            "gregtech.GT_Mod",
            "rwg.world.ChunkGeneratorRealistic"
           // "net.minecraftforge.oredict.OreDictionary"
    };
    static boolean obfs;

    public static boolean[] shouldTransform = new boolean[BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length];

    /**
     * Made by DarkShaddow44
     */
    private static MethodNode transformThaumcraftWandPedestal(MethodNode method) {
        InsnList nu = new InsnList();
        for (int j = 0; j < method.instructions.size(); j++) {
            AbstractInsnNode instruction = method.instructions.get(j);
            nu.add(instruction);
            if (instruction.getOpcode() == INVOKEVIRTUAL) {
                MethodInsnNode invokevirtual = (MethodInsnNode) instruction;
                if (invokevirtual.name.equals("addVis")) {
                    AbstractInsnNode beginning = method.instructions.get(j - 7);
                    LabelNode label = new LabelNode();
                    nu.insertBefore(beginning, new VarInsnNode(ALOAD, 0));
                    nu.insertBefore(beginning, new FieldInsnNode(GETFIELD, "thaumcraft/common/tiles/TileWandPedestal", obfs ? "field_145850_b" : "worldObj", "Lnet/minecraft/world/World;"));
                    nu.insertBefore(beginning, new FieldInsnNode(GETFIELD, "net/minecraft/world/World", obfs ? "field_72995_K" : "isRemote", "Z"));
                    nu.insertBefore(beginning, new JumpInsnNode(IFNE, label));
                    nu.add(new InsnNode(POP));
                    nu.add(label);
                    j++; // Skip actual Pop
                }
            }
        }
        method.instructions = nu;
        return method;
    }

    public static byte[] transform(int id, byte[] basicClass) {
        if (!BWCoreTransformer.shouldTransform[id]) {
            BWCore.BWCORE_LOG.info("Patch: " + BWCoreTransformer.DESCRIPTIONFORCONFIG[id] + " is disabled, will not patch!");
            return basicClass;
        }

        if (id < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length) {
            BWCore.BWCORE_LOG.info(BWCoreTransformer.DESCRIPTIONFORCONFIG[id]);
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);
            List<MethodNode> methods = classNode.methods;
            scase:
            switch (id) {
                case 0: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "canDoRainSnowIce";

                    String dsc_deObfs = "(Lnet/minecraft/world/chunk/Chunk;)Z";
                    String dsc_Obfs = "(Lapx;)Z";
                    for (int i = 0; i < methods.size(); i++) {
                        if (methods.get(i).name.equalsIgnoreCase(name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found " + name_deObfs + "! Removing!");
                            methods.remove(i);
                            break;
                        }
                    }
                    BWCore.BWCORE_LOG.info("Creating new " + name_deObfs + "!");
                    MethodNode nu = new MethodNode(ACC_PUBLIC, name_deObfs,
                            /*obfs ? dsc_Obfs :*/ dsc_deObfs,
                            null,
                            new String[0]
                    );
                    InsnList insnList = new InsnList();
                    insnList.add(new InsnNode(ICONST_0));
                    insnList.add(new InsnNode(IRETURN));
                    nu.instructions = insnList;
                    nu.maxLocals = 1;
                    nu.maxStack = 1;
                    methods.add(nu);
                    break;
                }
                case 1: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "getPossibleCreatures";
                    String name_src = "func_73155_a";
                    String name_Obfs = "a";
                    String dsc_deObfs = "(Lnet/minecraft/entity/EnumCreatureType;III)Ljava/util/List;";
                    String dsc_Obfs = "(Lsx;III)Ljava/util/List;";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src) && ASMUtils.isCorrectMethod(methods.get(i), dsc_deObfs, dsc_Obfs)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            MethodNode toPatch = methods.get(i);
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(ACONST_NULL));
                            insnList.add(new InsnNode(ARETURN));
                            toPatch.instructions = insnList;
                            toPatch.maxStack = 1;
                            toPatch.maxLocals = 5;
                            methods.set(i, toPatch);
                            break scase;
                        }
                    }
                }
                case 2: {
                    String name_deObfs = "renderSky";
                    String name_src = "func_72714_a";
                    String name_Obfs = "a";
                    String dsc_universal = "(F)V";
                    String field_deObfs = "locationSunPng";
                    String field_src = "field_110928_i";
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    for (int i = 0; i < methods.size(); i++) {
                        MethodNode toPatch = methods.get(i);
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src) && ASMUtils.isCorrectMethod(methods.get(i), dsc_universal)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            InsnList nu = new InsnList();
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};

                            String theWorld_src = "field_72769_h";
                            String renderEngine_src = "field_72770_i";
                            String provider_src = "field_73011_w";
                            String bindTexture_src = "func_110577_a";
                            String nameFieldToPatch;

                            for (int j = 0; j < toPatch.instructions.size(); j++) {
                                if (toPatch.instructions.get(j) instanceof FieldInsnNode && toPatch.instructions.get(j).getOpcode() == GETSTATIC && !(nameFieldToPatch = ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j)).name, field_deObfs, field_src)).isEmpty()) {
                                    boolean useSrc = nameFieldToPatch.equals(field_src);
                                    if (useSrc)
                                        BWCore.BWCORE_LOG.info("Found either Optifine or Fastcraft... this patch was annoying to make compatible to them...");

                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", useSrc ? theWorld_src : "theWorld", "Lnet/minecraft/client/multiplayer/WorldClient;"));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/multiplayer/WorldClient", useSrc ? provider_src : "provider", "Lnet/minecraft/world/WorldProvider;"));
                                    nu.add(new TypeInsnNode(INSTANCEOF, "com/github/bartimaeusnek/crossmod/galacticraft/planets/AbstractWorldProviderSpace"));
                                    nu.add(new JumpInsnNode(IFEQ, LabelNodes[0]));
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", useSrc ? renderEngine_src : "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(GETSTATIC, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128b/SkyProviderRoss128b", "sunTex", "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", useSrc ? bindTexture_src : "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
                                    nu.add(new JumpInsnNode(GOTO, LabelNodes[1]));
                                    nu.add(LabelNodes[0]);
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", useSrc ? renderEngine_src : "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(GETSTATIC, "net/minecraft/client/renderer/RenderGlobal", useSrc ? field_src : "locationSunPng", "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", useSrc ? bindTexture_src : "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
                                    nu.add(LabelNodes[1]);
                                    j++;

                                } else {
                                    if (j < toPatch.instructions.size() - 2) {
                                        if (toPatch.instructions.get(j + 2) instanceof FieldInsnNode && toPatch.instructions.get(j + 2).getOpcode() == GETSTATIC && !ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j + 2)).name, field_deObfs, field_src).isEmpty())
                                            continue;
                                        if (toPatch.instructions.get(j + 1) instanceof FieldInsnNode && toPatch.instructions.get(j + 1).getOpcode() == GETSTATIC && !ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j + 1)).name, field_deObfs, field_src).isEmpty())
                                            continue;
                                    }
                                    nu.add(toPatch.instructions.get(j));
                                }
                            }
                            toPatch.instructions = nu;
                            break scase;
                        }
                    }
                }
                case 3: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "updateEntity";
                    String name_src = "func_145845_h";
                    String name_Obfs = "h";
                    String dsc_universal = "()V";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src) && ASMUtils.isCorrectMethod(methods.get(i), dsc_universal, dsc_universal)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            methods.set(i, BWCoreTransformer.transformThaumcraftWandPedestal(methods.get(i)));
                            break scase;
                        }
                    }
                }
                case 4 : {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "<clinit>";
                    for (int i = 0; i < methods.size(); i++) {
                        MethodNode toPatch = methods.get(i);
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs) && (methods.get(i).access & ACC_STATIC) != 0) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            InsnList nu = new InsnList();
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};
                            for (int j = 0; j < 2; j++) {
                                nu.add(toPatch.instructions.get(j));
                            }
                            nu.add(new FieldInsnNode(GETSTATIC, "gregtech/api/GregTech_API", "sBeforeGTPreload", "Ljava/util/List;"));
                            nu.add(new TypeInsnNode(NEW, "com/github/bartimaeusnek/bartworks/common/loaders/BeforeGTPreload"));
                            nu.add(new InsnNode(DUP));
                            nu.add(new MethodInsnNode(INVOKESPECIAL, "com/github/bartimaeusnek/bartworks/common/loaders/BeforeGTPreload", "<init>", "()V", false));
                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
                            nu.add(new InsnNode(POP));
                            for (int j = 2; j < toPatch.instructions.size(); j++) {
                                nu.add(toPatch.instructions.get(j));
                            }
                            toPatch.instructions = nu;
                            break scase;
                        }
                    }
                }
                case 5: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "getNewNoise";
                    for (int i = 0; i < methods.size(); i++) {
                        MethodNode toPatch = methods.get(i);

                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};
                            InsnList nu = new InsnList();
                            // if (x < -28675) x %= -28675;
                            nu.add(new VarInsnNode(ILOAD, 2));
                            nu.add(new IntInsnNode(SIPUSH, -28675));
                            nu.add(new JumpInsnNode(IF_ICMPGE, LabelNodes[0]));
                            nu.add(new VarInsnNode(ILOAD, 2));
                            nu.add(new LdcInsnNode(-28675));
                            nu.add(new InsnNode(IREM));
                            nu.add(new VarInsnNode(ISTORE, 2));
                            nu.add(LabelNodes[0]);
                            // if (y < -28675) y %= -28675;
                            nu.add(new VarInsnNode(ILOAD, 3));
                            nu.add(new IntInsnNode(SIPUSH, -28675));
                            nu.add(new JumpInsnNode(IF_ICMPGE, LabelNodes[1]));
                            nu.add(new VarInsnNode(ILOAD, 3));
                            nu.add(new LdcInsnNode(-28675));
                            nu.add(new InsnNode(IREM));
                            nu.add(new VarInsnNode(ISTORE, 3));
                            nu.add(LabelNodes[1]);

                            for (int j = 1; j < methods.get(i).instructions.size(); j++) {
                                nu.add(methods.get(i).instructions.get(j));
                            }

                            methods.get(i).instructions = nu;
                            break scase;
                        }
                    }

//                    String name_deObfs = "getOreIDs";
//                    String dsc_deObfs = "(Lnet/minecraft/item/ItemStack;)[I";
//                    String dsc_Obfs = "(Ladd;)[I";
//
//                    for (int i = 0; i < methods.size(); i++) {
//                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs) && ASMUtils.isCorrectMethod(methods.get(i), dsc_deObfs, dsc_Obfs)) {
//                            MethodNode toPatch = methods.get(i);
//                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode(), new LabelNode(),new LabelNode(),new LabelNode(),new LabelNode(),new LabelNode()};
//                            InsnList nu = new InsnList();
//                            nu.add(new VarInsnNode(ALOAD, 0));
//                            nu.add(new JumpInsnNode(IFNULL, LabelNodes[0])); //L1
//                            nu.add(new VarInsnNode(ALOAD, 0));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false));
//                            nu.add(new JumpInsnNode(IFNONNULL, LabelNodes[1])); //L2
//                            nu.add(LabelNodes[0]); //L1
//                            nu.add(new TypeInsnNode(NEW, "java/lang/IllegalArgumentException"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new TypeInsnNode(LDC,"Stack can not be invalid!"));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL,"java/lang/IllegalArgumentException","<init>","(Ljava/lang/String;)V",false));
//                            nu.add(new InsnNode(ATHROW));
//                            nu.add(LabelNodes[1]); //L2
//                            nu.add(new TypeInsnNode(NEW, "java/util/HashSet"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL,"java/util/HashSet","<init>","()V",false));
//                            nu.add(new VarInsnNode(ASTORE, 1));
//                            nu.add(new VarInsnNode(ALOAD, 0));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItem", "()Lnet/minecraft/item/Item;", false));
//                            nu.add(new FieldInsnNode(GETFIELD,"net/minecraft/item/Item","delegate","Lcpw/mods/fml/common/registry/RegistryDelegate;"));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "cpw/mods/fml/common/registry/RegistryDelegate", "name", "()Ljava/lang/String;", true));
//                            nu.add(new VarInsnNode(ASTORE, 2));
//                            nu.add(new VarInsnNode(ALOAD, 2));
//                            nu.add(new JumpInsnNode(IFNONNULL, LabelNodes[2])); //L5
//                            nu.add(new FieldInsnNode(GETSTATIC,"org/apache/logging/log4j/Level","DEBUG","Lorg/apache/logging/log4j/Level;"));
//                            nu.add(new TypeInsnNode(LDC,"Attempted to find the oreIDs for an unregistered object (%s). This won't work very well."));
//                            nu.add(new InsnNode(ICONST_1));
//                            nu.add(new TypeInsnNode(ANEWARRAY,"java/lang/Object"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new InsnNode(ICONST_0));
//                            nu.add(new VarInsnNode(ALOAD, 0));
//                            nu.add(new InsnNode(AASTORE));
//                            nu.add(new MethodInsnNode(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "log", "(Lorg/apache/logging/log4j/Level;Ljava/lang/String;[Ljava/lang/Object;)V", false));
//                            nu.add(new TypeInsnNode(NEW, "java/io/StringWriter"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new MethodInsnNode(INVOKESPECIAL, "java/io/StringWriter", "<init>", "()V", false));
//                            nu.add(new VarInsnNode(ASTORE, 4));
//                            nu.add(new TypeInsnNode(NEW, "java/io/PrintWriter"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new VarInsnNode(ALOAD, 4));
//                            nu.add(new MethodInsnNode(INVOKESPECIAL, "java/io/PrintWriter", "<init>", "(Ljava/io/Writer;)V", false));
//                            nu.add(new VarInsnNode(ASTORE, 5));
//                            nu.add(new TypeInsnNode(NEW, "java/lang/Exception"));
//                            nu.add(new InsnNode(DUP));
//                            nu.add(new TypeInsnNode(LDC,"FINDME!"));
//                            nu.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Exception", "<init>", "(Ljava/lang/String;)V", false));
//                            nu.add(new VarInsnNode(ALOAD, 5));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "(Ljava/io/PrintWriter;)V", false));
//                            nu.add(new FieldInsnNode(GETSTATIC,"org/apache/logging/log4j/Level","DEBUG","Lorg/apache/logging/log4j/Level;"));
//                            nu.add(new VarInsnNode(ALOAD, 5));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false));
//                            nu.add(new InsnNode(ICONST_0));
//                            nu.add(new TypeInsnNode(ANEWARRAY,"java/lang/Object"));
//                            nu.add(new MethodInsnNode(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "log", "(Lorg/apache/logging/log4j/Level;Ljava/lang/String;[Ljava/lang/Object;)V", false));
//                            nu.add(new InsnNode(ICONST_0));
//                            nu.add(new IntInsnNode(NEWARRAY,T_INT));
//                            nu.add(new InsnNode(ARETURN));
//                            nu.add(LabelNodes[2]); //L5
//                            nu.add(new MethodInsnNode(INVOKESTATIC, "cpw/mods/fml/common/registry/GameData", "getItemRegistry", "()Lcpw/mods/fml/common/registry/FMLControlledNamespacedRegistry;", false));
//                            nu.add(new VarInsnNode(ALOAD, 2));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "cpw/mods/fml/common/registry/FMLControlledNamespacedRegistry", "getId", "(Ljava/lang/String;)I", false));
//                            nu.add(new VarInsnNode(ISTORE, 3));
//                            nu.add(new FieldInsnNode(GETSTATIC,"net/minecraftforge/oredict/OreDictionary","stackToId","Ljava/util/Map;"));
//                            nu.add(new VarInsnNode(ILOAD, 3));
//                            nu.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false));
//                            nu.add(new TypeInsnNode(CHECKCAST,"java/util/List"));
//                            nu.add(new VarInsnNode(ASTORE, 4));
//                            nu.add(new VarInsnNode(ALOAD, 4));
//                            nu.add(new JumpInsnNode(IFNULL, LabelNodes[3])); //L14
//                            nu.add(new VarInsnNode(ALOAD, 1));
//                            nu.add(new VarInsnNode(ALOAD, 4));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "addAll", "(Ljava/util/Collection;)Z", true));
//                            nu.add(new InsnNode(POP));
//                            nu.add(LabelNodes[3]); //L14
//                            nu.add(new FieldInsnNode(GETSTATIC,"net/minecraftforge/oredict/OreDictionary","stackToId","Ljava/util/Map;"));
//                            nu.add(new VarInsnNode(ILOAD, 3));
//                            nu.add(new VarInsnNode(ALOAD, 0));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getItemDamage", "()I", false));
//                            nu.add(new InsnNode(ICONST_1));
//                            nu.add(new InsnNode(IADD));
//                            nu.add(new VarInsnNode(BIPUSH, 16));
//                            nu.add(new InsnNode(ISHL));
//                            nu.add(new InsnNode(IOR));
//                            nu.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
//                            nu.add(new TypeInsnNode(CHECKCAST,"java/util/List"));
//                            nu.add(new VarInsnNode(ASTORE, 4));
//                            nu.add(new VarInsnNode(ALOAD, 4));
//                            nu.add(new JumpInsnNode(IFNULL, LabelNodes[4])); //L16
//                            nu.add(new VarInsnNode(ALOAD, 1));
//                            nu.add(new VarInsnNode(ALOAD, 4));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "addAll", "(Ljava/util/Collection;)Z", true));
//                            nu.add(new InsnNode(POP));
//                            nu.add(LabelNodes[4]); //L16
//                            nu.add(new VarInsnNode(ALOAD, 1));
//                            nu.add(new VarInsnNode(ALOAD, 1));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "size", "()I", true));
//                            nu.add(new TypeInsnNode(ANEWARRAY,"java/lang/Integer"));
//                            nu.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", true));
//                            nu.add(new TypeInsnNode(CHECKCAST,"[java/util/Integer"));
//                            nu.add(new VarInsnNode(ASTORE, 5));
//                            nu.add(new VarInsnNode(ALOAD, 5));
//                            nu.add(new InsnNode(ARRAYLENGTH));
//                            nu.add(new IntInsnNode(NEWARRAY,T_INT));
//                            nu.add(new VarInsnNode(ASTORE, 6));
//                            nu.add(new InsnNode(ICONST_0));
//                            nu.add(new VarInsnNode(ISTORE, 7));
//                            nu.add(LabelNodes[6]); //L19
//                            nu.add(new VarInsnNode(ILOAD, 7));
//                            nu.add(new VarInsnNode(ALOAD, 5));
//                            nu.add(new InsnNode(ARRAYLENGTH));
//                            nu.add(new JumpInsnNode(IF_ICMPGE, LabelNodes[5])); //L20
//                            nu.add(new VarInsnNode(ALOAD, 6));
//                            nu.add(new VarInsnNode(ILOAD, 7));
//                            nu.add(new VarInsnNode(ALOAD, 5));
//                            nu.add(new VarInsnNode(ILOAD, 7));
//                            nu.add(new InsnNode(AALOAD));
//                            nu.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false));
//                            nu.add(new InsnNode(IASTORE));
//                            nu.add(new IincInsnNode(7,1));
//                            nu.add(new JumpInsnNode(GOTO, LabelNodes[6])); //L19
//                            nu.add(LabelNodes[5]); //L20
//                            nu.add(new VarInsnNode(ALOAD, 6));
//                            nu.add(new InsnNode(ARETURN));
//                            toPatch.instructions = nu;
//                            toPatch.maxStack = 6;
//                            toPatch.maxLocals = 8;
//                            methods.set(i, toPatch);
//                            break;
//                        }
                }
                default: {
                    BWCore.BWCORE_LOG.info("Could not find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    return basicClass;
                }
            }

            classNode.methods = methods;
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            byte[] ret = classWriter.toByteArray();
            if (Arrays.hashCode(basicClass) == Arrays.hashCode(ret))
                BWCore.BWCORE_LOG.warn("Could not patch: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
            return ret;
        }
        return basicClass;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++) {
            if (name.equalsIgnoreCase(BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i]) || transformedName.equalsIgnoreCase(BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i]))
                return BWCoreTransformer.transform(i, basicClass);
        }
        return basicClass;
    }


}
