package gregtech.api.items.armor.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;

public class ArmorModelCompiler {

    public static class CompileResult {

        public BedrockArmorModel mergedJson;
        public BufferedImage mergedImage;
    }

    public static CompileResult compile(ArmorComponent baseArmor, List<ArmorComponent> augments) throws Exception {
        CompileResult result = new CompileResult();

        BedrockArmorModel baseJson = loadJson(baseArmor.modelLocation);
        BufferedImage baseImage = loadImage(baseArmor.textureLocation);

        result.mergedJson = baseArmor.cloneModel(baseJson);

        int totalWidth = baseImage.getWidth();
        int totalHeight = baseImage.getHeight();
        for (ArmorComponent augment : augments) {
            BufferedImage augImg = loadImage(augment.textureLocation);
            totalWidth = Math.max(totalWidth, augImg.getWidth());
            totalHeight += augImg.getHeight();
        }

        result.mergedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.mergedImage.createGraphics();

        g2d.drawImage(baseImage, 0, 0, null);
        int currentYOffset = baseImage.getHeight();

        for (ArmorComponent augment : augments) {
            BedrockArmorModel augJson = loadJson(augment.modelLocation);
            BufferedImage augImg = loadImage(augment.textureLocation);

            g2d.drawImage(augImg, 0, currentYOffset, null);

            if (augJson.geometryList != null && !augJson.geometryList.isEmpty()) {
                List<BedrockArmorModel.Bone> augBones = augJson.geometryList.get(0).bones;

                for (BedrockArmorModel.Bone augBone : augBones) {
                    remapBoneUVs(augBone, 0, currentYOffset);

                    String targetBoneName = (augBone.parent != null) ? augBone.parent : augBone.name;

                    BedrockArmorModel.Bone targetBaseBone = null;
                    for (BedrockArmorModel.Bone baseBone : result.mergedJson.geometryList.get(0).bones) {
                        if (baseBone.name.equals(targetBoneName)) {
                            targetBaseBone = baseBone;
                            break;
                        }
                    }

                    if (targetBaseBone != null) {
                        if (targetBaseBone.cubes == null) targetBaseBone.cubes = new java.util.ArrayList<>();
                        if (augBone.cubes != null) {
                            targetBaseBone.cubes.addAll(augBone.cubes);
                        }
                    } else {
                        result.mergedJson.geometryList.get(0).bones.add(augBone);
                    }
                }
            }

            currentYOffset += augImg.getHeight();
        }

        g2d.dispose();

        result.mergedJson.geometryList.get(0).description.texture_width = totalWidth;
        result.mergedJson.geometryList.get(0).description.texture_height = totalHeight;

        return result;
    }

    private static void remapBoneUVs(BedrockArmorModel.Bone bone, float uOffset, float vOffset) {
        if (bone.cubes == null) return;
        for (BedrockArmorModel.Cube cube : bone.cubes) {
            if (cube.uv == null) continue;
            shiftFace(cube.uv.north, uOffset, vOffset);
            shiftFace(cube.uv.south, uOffset, vOffset);
            shiftFace(cube.uv.east, uOffset, vOffset);
            shiftFace(cube.uv.west, uOffset, vOffset);
            shiftFace(cube.uv.up, uOffset, vOffset);
            shiftFace(cube.uv.down, uOffset, vOffset);
        }
    }

    private static void shiftFace(BedrockArmorModel.Face face, float uOffset, float vOffset) {
        if (face != null && face.uv != null && face.uv.length >= 2) {
            face.uv[0] += uOffset;
            face.uv[1] += vOffset;
        }
    }

    private static BufferedImage loadImage(ResourceLocation loc) throws Exception {
        try (InputStream is = Minecraft.getMinecraft()
            .getResourceManager()
            .getResource(loc)
            .getInputStream()) {
            return ImageIO.read(is);
        }
    }

    private static BedrockArmorModel loadJson(ResourceLocation loc) throws Exception {
        Gson gson = new Gson();

        try (InputStreamReader reader = new InputStreamReader(
            Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(loc)
                .getInputStream())) {

            return gson.fromJson(reader, BedrockArmorModel.class);

        } catch (Exception e) {
            System.out.println("[GTNH-VoxelArmor] Error while parsing JSON of " + loc.getResourcePath());
            e.printStackTrace();
            return null;
        }
    }
}
