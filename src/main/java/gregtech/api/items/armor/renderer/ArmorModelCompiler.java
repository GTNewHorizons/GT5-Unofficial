package gregtech.api.items.armor.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class ArmorModelCompiler {

    public static final class CompileResult {

        public final BakedArmorModel mergedModel;
        public final BufferedImage mergedImage;

        CompileResult(BakedArmorModel mergedModel, BufferedImage mergedImage) {
            this.mergedModel = mergedModel;
            this.mergedImage = mergedImage;
        }
    }

    private ArmorModelCompiler() {}

    public static CompileResult compile(ArmorComponent baseArmor, List<ArmorComponent> augments) {
        List<ArmorComponent> components = new ArrayList<>(1 + augments.size());
        components.add(baseArmor);
        components.addAll(augments);

        int atlasWidth = 0;
        int atlasHeight = 0;
        for (ArmorComponent component : components) {
            atlasWidth = Math.max(atlasWidth, component.texture.getWidth());
            atlasHeight += component.texture.getHeight();
        }

        BufferedImage atlas = new BufferedImage(atlasWidth, atlasHeight, BufferedImage.TYPE_INT_ARGB);
        BakedArmorModel mergedModel = new BakedArmorModel(atlasWidth, atlasHeight);
        Graphics2D graphics = atlas.createGraphics();

        int atlasOffsetY = 0;
        for (ArmorComponent component : components) {
            BufferedImage texture = component.texture;
            graphics.drawImage(texture, 0, atlasOffsetY, null);

            float uvScaleU = (float) texture.getWidth() / component.model.textureWidth;
            float uvScaleV = (float) texture.getHeight() / component.model.textureHeight;

            for (BakedBone sourceBone : component.model.bonesByName.values()) {
                BakedBone targetBone = mergedModel.bonesByName.computeIfAbsent(
                    sourceBone.name,
                    boneName -> new BakedBone(boneName, sourceBone.pivotX, sourceBone.pivotY, sourceBone.pivotZ));

                for (BakedPolygon polygon : sourceBone.polygons) {
                    targetBone.polygons.add(polygon.copyWithUVTransform(uvScaleU, uvScaleV, 0, atlasOffsetY));
                }
            }

            atlasOffsetY += texture.getHeight();
        }

        graphics.dispose();
        return new CompileResult(mergedModel, atlas);
    }
}
