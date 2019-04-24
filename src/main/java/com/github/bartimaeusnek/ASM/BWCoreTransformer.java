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
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.*;

public class BWCoreTransformer implements IClassTransformer {
    public static final String[] DESCRIPTIONFORCONFIG = {
            "REMOVING RAIN FROM LAST MILLENIUM (EXU)",
            "REMVOING CREATURES FROM LAST MILLENIUM (EXU)",
            "PATCHING GLOBAL RENDERER FOR USE WITH MY GALACTIC DIMS"
    };
    public static final String[] CLASSESBEEINGTRANSFORMED = {
            "com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime",
            "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime",
            "net.minecraft.client.renderer.RenderGlobal",
    };
    public static boolean obfs = false;

    public static boolean[] shouldTransform = new boolean[CLASSESBEEINGTRANSFORMED.length];

//    public void checkForMods() {
//        //hacky way to detect if the mods are loaded
//        try{
//            Class.forName("com.rwtema.extrautils.core.Tuple");
//            shouldTransform[0] = true;
//            shouldTransform[1] = true;
//        }catch (ClassNotFoundException e){
//            BWCore.BWCORE_LOG.info("Extra Utilities not found!");
//            shouldTransform[0] = false;
//            shouldTransform[1] = false;
//        }
//        try{
//            Class.forName("micdoodle8.mods.galacticraft.core.Constants");
//            shouldTransform[2] = true;
//        }catch (ClassNotFoundException e){
//            BWCore.BWCORE_LOG.info("micdoodle Core not found!");
//            shouldTransform[2] = false;
//        }
//    }

    public static byte[] transform(int id, byte[] basicClass) {
        if (!BWCoreTransformer.shouldTransform[id]) {
            BWCore.BWCORE_LOG.info("Patch: " + DESCRIPTIONFORCONFIG[id] + " is disabled, will not patch!");
            return basicClass;
        }

        if (id < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length) {
            BWCore.BWCORE_LOG.info(DESCRIPTIONFORCONFIG[id]);
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);
            List<MethodNode> methods = classNode.methods;
            switch (id) {
                case 0: {
                    BWCore.BWCORE_LOG.info("Could find: " + CLASSESBEEINGTRANSFORMED[id]);
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
                    BWCore.BWCORE_LOG.info("Could find: " + CLASSESBEEINGTRANSFORMED[id]);
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
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    String name_deObfs = "renderSky";
                    String name_src = "func_72714_a";
                    String name_Obfs = "a";
                    String dsc_universal = "(F)V";
                    String field_deObfs = "locationSunPng";
                    String field_src = "field_110928_i";
                    BWCore.BWCORE_LOG.info("Could find: " + CLASSESBEEINGTRANSFORMED[id]);
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
                                if (toPatch.instructions.get(j) instanceof FieldInsnNode && ((FieldInsnNode) toPatch.instructions.get(j)).getOpcode() == GETSTATIC && !(nameFieldToPatch = ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j)).name, field_deObfs, field_src)).isEmpty()) {
                                    boolean useSrc = nameFieldToPatch.equals(field_src);
                                    if (useSrc)
                                        BWCore.BWCORE_LOG.info("Found either Optifine or Fastcraft... this patch was annoying to make compatible to them...");

                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/RenderGlobal", useSrc ? theWorld_src : "theWorld", "Lnet/minecraft/client/multiplayer/WorldClient;"));
                                    nu.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/multiplayer/WorldClient", useSrc ? provider_src : "provider", "Lnet/minecraft/world/WorldProvider;"));
                                    nu.add(new TypeInsnNode(INSTANCEOF, "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128b/WorldProviderRoss128b"));
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
                                        if (toPatch.instructions.get(j + 2) instanceof FieldInsnNode && ((FieldInsnNode) toPatch.instructions.get(j + 2)).getOpcode() == GETSTATIC && !ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j + 2)).name, field_deObfs, field_src).isEmpty())
                                            continue;
                                        if (toPatch.instructions.get(j + 1) instanceof FieldInsnNode && ((FieldInsnNode) toPatch.instructions.get(j + 1)).getOpcode() == GETSTATIC && !ASMUtils.matchAny(((FieldInsnNode) toPatch.instructions.get(j + 1)).name, field_deObfs, field_src).isEmpty())
                                            continue;
                                    }
                                    nu.add(toPatch.instructions.get(j));
                                }
                            }
                            toPatch.instructions = nu;
                            break;
                        }
                    }
                    break;
                }
                default: {
                    BWCore.BWCORE_LOG.info("Could not find: " + CLASSESBEEINGTRANSFORMED[id]);
                    return basicClass;
                }
            }

            classNode.methods = methods;
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            byte[] ret = classWriter.toByteArray();
            if (Arrays.hashCode(basicClass) == Arrays.hashCode(ret))
                BWCore.BWCORE_LOG.warn("Could not patch: " + CLASSESBEEINGTRANSFORMED[id]);

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
