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
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class BWCoreTransformer implements IClassTransformer {
    public static boolean obfs = false;
    public static final String[] DESCRIPTIONFORCONFIG = {
            "REMOVING RAIN FROM LAST MILLENIUM (EXU)",
            "REMVOING CREATURES FROM LAST MILLENIUM (EXU)",
            "PATCHING GLOBAL RENDERER FOR USE WITH MY GALACTIC DIMS"
    };

    public static final String[] CLASSESBEEINGTRANSFORMED = {
            "com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime",
            "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime",
            //"micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld",
            "net.minecraft.client.renderer.RenderGlobal",
    };

    public static boolean[] shouldTransform = ArrayUtils.toPrimitive(new Boolean[BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length],true);

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++) {
            if (name.equalsIgnoreCase(BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i]))
                return BWCoreTransformer.transform(i,basicClass);
        }
        return basicClass;
    }

    public static byte[] transform(int id, byte[] basicClass) {
        if (!BWCoreTransformer.shouldTransform[id])
            return basicClass;

        if (id < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length) {
            BWCore.BWCORE_LOG.info(DESCRIPTIONFORCONFIG[id]);
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);
            List<MethodNode> methods = classNode.methods;
            switch (id) {
                case 0: {
                    BWCore.BWCORE_LOG.info("Could find: "+CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "canDoRainSnowIce";

                    String dsc_deObfs = "(Lnet/minecraft/world/chunk/Chunk;)Z";
                    String dsc_Obfs = "(Lapx;)Z";
                    for (int i = 0; i < methods.size(); i++) {
                        if (methods.get(i).name.equalsIgnoreCase(name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found "+name_deObfs+"! Removing!");
                            methods.remove(i);
                            break;
                        }
                    }
                    BWCore.BWCORE_LOG.info("Creating new "+name_deObfs+"!");
                    MethodNode nu = new MethodNode(Opcodes.ACC_PUBLIC, name_deObfs,
                            /*obfs ? dsc_Obfs :*/ dsc_deObfs,
                            name_deObfs+dsc_deObfs.substring(0,dsc_deObfs.length()-1),
                            new String[0]
                    );
                    InsnList insnList = new InsnList();
                    insnList.add(new InsnNode(Opcodes.ICONST_0));
                    insnList.add(new InsnNode(Opcodes.IRETURN));
                    nu.instructions = insnList;
                    nu.maxLocals = 1;
                    nu.maxStack = 1;
                    methods.add(nu);
                    break;
                }
                case 1: {
                    BWCore.BWCORE_LOG.info("Could find: "+CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "getPossibleCreatures";
                    String name_Obfs = "func_73155_a";
                    String dsc_deObfs = "(Lnet/minecraft/entity/EnumCreatureType;III)Ljava/util/List;";
                    String dsc_Obfs = "(Lsx;III)Ljava/util/List;";
                    for (int i = 0; i < methods.size(); i++) {
                        if ((methods.get(i).name.equalsIgnoreCase(obfs?name_Obfs:name_deObfs) && methods.get(i).desc.equalsIgnoreCase(obfs?dsc_Obfs:dsc_deObfs))||(methods.get(i).name.equalsIgnoreCase(!obfs?name_Obfs:name_deObfs) && methods.get(i).desc.equalsIgnoreCase(!obfs?dsc_Obfs:dsc_deObfs))) {
                            BWCore.BWCORE_LOG.info("Found "+(name_deObfs)+"! Patching!");
                            MethodNode toPatch = methods.get(i);
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(Opcodes.ACONST_NULL));
                            insnList.add(new InsnNode(Opcodes.ARETURN));
                            toPatch.instructions = insnList;
                            toPatch.maxStack = 1;
                            toPatch.maxLocals = 5;
                            methods.set(i, toPatch);
                            break;
                        }
                    }
                    break;
                }
                case 2:{
                    String name_deObfs = "renderSky";
                    String name_Obfs = "func_72714_a";
                    String dsc_deObfs = "(F)V";
                    BWCore.BWCORE_LOG.info("Could find: "+CLASSESBEEINGTRANSFORMED[id]);
                    for (int i = 0; i < methods.size(); i++) {
                        MethodNode toPatch = methods.get(i);
                        if ((toPatch.name.equalsIgnoreCase(name_Obfs) || toPatch.name.equalsIgnoreCase(name_deObfs)) && methods.get(i).desc.equalsIgnoreCase(dsc_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found "+(name_deObfs)+"! Patching!");
                            InsnList nu = new InsnList();
                            LabelNode[] LabelNodes = { new LabelNode(),new LabelNode()};
                            for (int j = 0; j < toPatch.instructions.size(); j++) {
                                if (toPatch.instructions.get(j) instanceof LineNumberNode && ((LineNumberNode) toPatch.instructions.get(j)).line == 1190) {
                                    nu.add(toPatch.instructions.get(j));
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", "theWorld", "Lnet/minecraft/client/multiplayer/WorldClient;"));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/multiplayer/WorldClient", "provider", "Lnet/minecraft/world/WorldProvider;"));
                                    nu.add(new TypeInsnNode(INSTANCEOF, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128/world/worldprovider/WorldProviderRoss128b"));
                                    nu.add(new JumpInsnNode(IFEQ, LabelNodes[0]));
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(GETSTATIC, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128/world/worldprovider/SkyProviderRoss128b", "sunTex", "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
                                    nu.add(new JumpInsnNode(GOTO, LabelNodes[1]));
                                    nu.add(LabelNodes[0]);
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(GETSTATIC, "net/minecraft/client/renderer/RenderGlobal", "locationSunPng", "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
                                    nu.add(LabelNodes[1]);
                                    j+=5;

//                                if (toPatch.instructions.get(j) instanceof LineNumberNode && ((LineNumberNode) toPatch.instructions.get(j)).line == 308) {
//                                    nu.add(toPatch.instructions.get(j));
//                                    nu.add(new VarInsnNode(ALOAD, 0));
//                                    nu.add(new TypeInsnNode(INSTANCEOF, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128/world/worldprovider/SkyProviderRoss128b"));
//                                    nu.add(new JumpInsnNode(IFEQ, LabelNodes[0]));
//                                    nu.add(new VarInsnNode(ALOAD, 0));
//                                    nu.add(new FieldInsnNode(GETFIELD, "micdoodle8/mods/galacticraft/core/client/SkyProviderOverworld", "minecraft", "Lnet/minecraft/client/Minecraft;"));
//                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
//                                    nu.add(new FieldInsnNode(GETSTATIC, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128/world/worldprovider/SkyProviderRoss128b", "sunTex", "Lnet/minecraft/util/ResourceLocation;"));
//                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
//                                    nu.add(new JumpInsnNode(GOTO, LabelNodes[1]));
//                                    nu.add(LabelNodes[0]);
//                                    nu.add(new VarInsnNode(ALOAD, 0));
//                                    nu.add(new FieldInsnNode(GETFIELD, "micdoodle8/mods/galacticraft/core/client/SkyProviderOverworld", "minecraft", "Lnet/minecraft/client/Minecraft;"));
//                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", "renderEngine", "Lnet/minecraft/client/renderer/texture/TextureManager;"));
//                                    nu.add(new FieldInsnNode(GETSTATIC, "micdoodle8/mods/galacticraft/core/client/SkyProviderOverworld", "sunTexture", "Lnet/minecraft/util/ResourceLocation;"));
//                                    nu.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureManager", "bindTexture", "(Lnet/minecraft/util/ResourceLocation;)V", false));
//                                    nu.add(LabelNodes[1]);
//                                    j+=5;
                                } else {
                                    nu.add(toPatch.instructions.get(j));
                                }
                            }
                            toPatch.instructions=nu;
                            break;
                        }
                    }
                    break;
                }
                default: {
                    BWCore.BWCORE_LOG.info("Could not find: "+CLASSESBEEINGTRANSFORMED[id]);
                    return basicClass;
                }
            }

            classNode.methods=methods;
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            byte[] ret = classWriter.toByteArray();
            if (Arrays.hashCode(basicClass) == Arrays.hashCode(ret))
                BWCore.BWCORE_LOG.warn("Could not patch: "+CLASSESBEEINGTRANSFORMED[id]);
//            try {
//                OutputStream os = new FileOutputStream(new File("C:/test/"+CLASSESBEEINGTRANSFORMED[id]+".class"));
//                os.write(classWriter.toByteArray());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return ret;
        }
        return basicClass;
    }


}
