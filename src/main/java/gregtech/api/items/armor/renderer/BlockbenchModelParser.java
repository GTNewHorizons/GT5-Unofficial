package gregtech.api.items.armor.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class BlockbenchModelParser {

    private static final float PIXEL_SCALE = 1.0F / 16.0F;
    private static final float MODEL_ORIGIN_Y = 24.0F;
    private static final String IGNORED_GROUP_PREFIX = "_";

    private BlockbenchModelParser() {}

    public static Map<String, BakedArmorModel> parse(JsonObject root) {
        mergeGroupDataIntoOutliner(root);

        float[] textureOffsetsV = textureOffsetsV(root);
        int textureWidth = combinedTextureWidth(root);
        int textureHeight = combinedTextureHeight(root);

        Map<String, JsonObject> elementsByUuid = new HashMap<>();
        for (JsonElement element : root.getAsJsonArray("elements")) {
            JsonObject elementObject = element.getAsJsonObject();
            elementsByUuid.put(elementObject.get("uuid").getAsString(), elementObject);
        }

        Map<String, BakedArmorModel> modelsByComponentId = new LinkedHashMap<>();
        for (JsonElement outlinerEntry : root.getAsJsonArray("outliner")) {
            if (!outlinerEntry.isJsonObject()) continue;

            JsonObject componentGroup = outlinerEntry.getAsJsonObject();
            String componentId = componentGroup.get("name").getAsString();
            if (componentId.startsWith(IGNORED_GROUP_PREFIX)) continue;

            BakedArmorModel componentModel = new BakedArmorModel(textureWidth, textureHeight);

            for (JsonElement child : componentGroup.getAsJsonArray("children")) {
                if (!child.isJsonObject()) continue;

                JsonObject bodyPartGroup = child.getAsJsonObject();
                if (bodyPartGroup.get("name").getAsString().startsWith(IGNORED_GROUP_PREFIX)) continue;

                BakedBone bone = bakeBodyPart(bodyPartGroup, componentGroup, elementsByUuid, textureOffsetsV);
                componentModel.bonesByName.put(bone.name, bone);
            }

            modelsByComponentId.put(componentId, componentModel);
        }
        return modelsByComponentId;
    }

    public static BufferedImage readEmbeddedTexture(JsonObject root) throws IOException {
        JsonArray textures = root.getAsJsonArray("textures");
        if (textures == null || textures.size() == 0) return null;

        List<BufferedImage> images = new ArrayList<>(textures.size());
        for (JsonElement textureElement : textures) {
            BufferedImage image = decodeTexture(textureElement.getAsJsonObject());
            if (image == null) return null;
            images.add(image);
        }
        if (images.size() == 1) return images.get(0);

        float pixelsPerUV = images.get(0).getHeight() / textureSizeV(textures.get(0).getAsJsonObject(), root);
        int combinedWidth = Math.round(combinedTextureWidth(root) * pixelsPerUV);
        int combinedHeight = Math.round(combinedTextureHeight(root) * pixelsPerUV);
        float[] offsetsV = textureOffsetsV(root);

        BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = combined.createGraphics();
        for (int index = 0; index < images.size(); index++) {
            graphics.drawImage(images.get(index), 0, Math.round(offsetsV[index] * pixelsPerUV), null);
        }
        graphics.dispose();
        return combined;
    }

    private static BufferedImage decodeTexture(JsonObject texture) throws IOException {
        if (!texture.has("source")) return null;

        String dataUrl = texture.get("source").getAsString();
        byte[] pngBytes = Base64.getDecoder().decode(dataUrl.substring(dataUrl.indexOf(',') + 1));
        return ImageIO.read(new ByteArrayInputStream(pngBytes));
    }

    private static BakedBone bakeBodyPart(JsonObject bodyPartGroup, JsonObject componentGroup,
                                          Map<String, JsonObject> elementsByUuid, float[] textureOffsetsV) {
        float[] pivot = readVector(bodyPartGroup, "origin");
        BakedBone bone = new BakedBone(
            bodyPartGroup.get("name").getAsString(),
            toModelX(pivot[0]),
            toModelY(pivot[1]),
            toModelZ(pivot[2]));

        Deque<JsonObject> groupChain = new ArrayDeque<>();
        groupChain.addFirst(componentGroup);
        collectGroup(bodyPartGroup, groupChain, elementsByUuid, bone, textureOffsetsV);
        return bone;
    }

    private static void collectGroup(JsonObject group, Deque<JsonObject> groupChain,
                                     Map<String, JsonObject> elementsByUuid, BakedBone bone, float[] textureOffsetsV) {
        groupChain.addFirst(group);

        for (JsonElement child : group.getAsJsonArray("children")) {
            if (child.isJsonObject()) {
                JsonObject childGroup = child.getAsJsonObject();
                if (childGroup.get("name").getAsString().startsWith(IGNORED_GROUP_PREFIX)) continue;

                collectGroup(childGroup, groupChain, elementsByUuid, bone, textureOffsetsV);
                continue;
            }

            JsonObject element = elementsByUuid.get(child.getAsString());
            if (element == null || !isExported(element)) continue;

            List<BakedPolygon> elementPolygons = switch (elementType(element)) {
                case "cube" -> bakeCube(element, textureOffsetsV);
                case "mesh" -> bakeMesh(element, textureOffsetsV);
                default -> List.of();
            };

            for (BakedPolygon polygon : elementPolygons) {
                for (JsonObject ancestorGroup : groupChain) {
                    rotatePolygon(polygon, readVector(ancestorGroup, "origin"), readVector(ancestorGroup, "rotation"));
                }
                convertToModelSpace(polygon);
                bone.polygons.add(polygon);
            }
        }

        groupChain.removeFirst();
    }

    private static List<BakedPolygon> bakeCube(JsonObject cube, float[] textureOffsetsV) {
        float[] from = readVector(cube, "from");
        float[] to = readVector(cube, "to");
        float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 0.0F;

        float minX = Math.min(from[0], to[0]) - inflate, maxX = Math.max(from[0], to[0]) + inflate;
        float minY = Math.min(from[1], to[1]) - inflate, maxY = Math.max(from[1], to[1]) + inflate;
        float minZ = Math.min(from[2], to[2]) - inflate, maxZ = Math.max(from[2], to[2]) + inflate;

        JsonObject faces = cube.getAsJsonObject("faces");
        List<BakedPolygon> polygons = new ArrayList<>(6);

        // spotless:off
        addCubeFace(polygons, faces, textureOffsetsV, "north",  0,  0, -1,
            minX, maxY, minZ,   maxX, maxY, minZ,   maxX, minY, minZ,   minX, minY, minZ);
        addCubeFace(polygons, faces, textureOffsetsV, "south",  0,  0,  1,
            maxX, maxY, maxZ,   minX, maxY, maxZ,   minX, minY, maxZ,   maxX, minY, maxZ);
        addCubeFace(polygons, faces, textureOffsetsV, "east",   1,  0,  0,
            maxX, maxY, minZ,   maxX, maxY, maxZ,   maxX, minY, maxZ,   maxX, minY, minZ);
        addCubeFace(polygons, faces, textureOffsetsV, "west",  -1,  0,  0,
            minX, maxY, maxZ,   minX, maxY, minZ,   minX, minY, minZ,   minX, minY, maxZ);
        addCubeFace(polygons, faces, textureOffsetsV, "up",     0,  1,  0,
            maxX, maxY, minZ,   minX, maxY, minZ,   minX, maxY, maxZ,   maxX, maxY, maxZ);
        addCubeFace(polygons, faces, textureOffsetsV, "down",   0, -1,  0,
            maxX, minY, maxZ,   minX, minY, maxZ,   minX, minY, minZ,   maxX, minY, minZ);
        // spotless:on

        float[] cubeOrigin = readVector(cube, "origin");
        float[] cubeRotation = readVector(cube, "rotation");
        for (BakedPolygon polygon : polygons) {
            rotatePolygon(polygon, cubeOrigin, cubeRotation);
        }
        return polygons;
    }

    private static void addCubeFace(List<BakedPolygon> polygons, JsonObject faces, float[] textureOffsetsV,
                                    String faceName,
                                    float normalX, float normalY, float normalZ,
                                    float topRightX, float topRightY, float topRightZ,
                                    float topLeftX, float topLeftY, float topLeftZ,
                                    float bottomLeftX, float bottomLeftY, float bottomLeftZ,
                                    float bottomRightX, float bottomRightY, float bottomRightZ) {

        if (faces == null || !faces.has(faceName)) return;
        JsonObject face = faces.getAsJsonObject(faceName);
        if (hasNoTexture(face) || !face.has("uv")) return;

        float[] uv = readArray(face.getAsJsonArray("uv"));
        float textureOffsetV = textureOffsetV(face, textureOffsetsV);
        uv[1] += textureOffsetV;
        uv[3] += textureOffsetV;
        float[][] cornerUVs = { { uv[2], uv[1] }, { uv[0], uv[1] }, { uv[0], uv[3] }, { uv[2], uv[3] } };


        int rotationSteps = face.has("rotation") ? (face.get("rotation").getAsInt() / 90) & 3 : 0;

        float[][] cornerPositions = {
            { topRightX, topRightY, topRightZ },
            { topLeftX, topLeftY, topLeftZ },
            { bottomLeftX, bottomLeftY, bottomLeftZ },
            { bottomRightX, bottomRightY, bottomRightZ } };

        BakedPolygon polygon = new BakedPolygon(4);
        for (int corner = 0; corner < 4; corner++) {
            float[] cornerUV = cornerUVs[(corner + rotationSteps) & 3];
            float[] position = cornerPositions[corner];
            polygon.setVertex(corner, position[0], position[1], position[2], cornerUV[0], cornerUV[1]);
        }
        polygon.normalX = normalX;
        polygon.normalY = normalY;
        polygon.normalZ = normalZ;
        polygons.add(polygon);
    }

    private static List<BakedPolygon> bakeMesh(JsonObject mesh, float[] textureOffsetsV) {
        float[] meshOrigin = readVector(mesh, "origin");
        float[] meshRotation = readVector(mesh, "rotation");
        JsonObject vertexPositions = mesh.getAsJsonObject("vertices");
        List<BakedPolygon> polygons = new ArrayList<>();

        for (Map.Entry<String, JsonElement> faceEntry : mesh.getAsJsonObject("faces")
            .entrySet()) {
            JsonObject face = faceEntry.getValue()
                .getAsJsonObject();
            if (hasNoTexture(face)) continue;

            List<String> vertexKeys = new ArrayList<>();
            for (JsonElement key : face.getAsJsonArray("vertices")) {
                vertexKeys.add(key.getAsString());
            }
            if (vertexKeys.size() < 3 || vertexKeys.size() > 4) continue;
            if (vertexKeys.size() == 4) vertexKeys = sortQuadVertices(vertexKeys, vertexPositions);

            JsonObject faceUVs = face.getAsJsonObject("uv");
            float textureOffsetV = textureOffsetV(face, textureOffsetsV);
            BakedPolygon polygon = new BakedPolygon(vertexKeys.size());
            for (int vertexIndex = 0; vertexIndex < vertexKeys.size(); vertexIndex++) {
                String key = vertexKeys.get(vertexIndex);
                float[] localPosition = readArray(vertexPositions.getAsJsonArray(key));
                float[] uv = readArray(faceUVs.getAsJsonArray(key));
                polygon.setVertex(
                    vertexIndex,
                    localPosition[0] + meshOrigin[0],
                    localPosition[1] + meshOrigin[1],
                    localPosition[2] + meshOrigin[2],
                    uv[0],
                    uv[1] + textureOffsetV);
            }
            polygon.computeNormal();
            rotatePolygon(polygon, meshOrigin, meshRotation);
            polygons.add(polygon);
        }
        return polygons;
    }

    private static List<String> sortQuadVertices(List<String> keys, JsonObject vertexPositions) {
        int[][] candidateOrders = { { 0, 1, 2, 3 }, { 0, 2, 1, 3 }, { 0, 1, 3, 2 } };
        int[] bestOrder = candidateOrders[0];
        float bestArea = -1.0F;

        for (int[] order : candidateOrders) {
            float[][] corners = new float[4][];
            for (int index = 0; index < 4; index++) {
                corners[index] = readArray(vertexPositions.getAsJsonArray(keys.get(order[index])));
            }
            float area = quadArea(corners);
            if (area > bestArea) {
                bestArea = area;
                bestOrder = order;
            }
        }

        List<String> sorted = new ArrayList<>(4);
        for (int index : bestOrder) sorted.add(keys.get(index));
        return sorted;
    }

    private static float quadArea(float[][] corners) {
        float sumX = 0, sumY = 0, sumZ = 0;
        for (int index = 0; index < 4; index++) {
            float[] current = corners[index];
            float[] next = corners[(index + 1) & 3];
            sumX += current[1] * next[2] - current[2] * next[1];
            sumY += current[2] * next[0] - current[0] * next[2];
            sumZ += current[0] * next[1] - current[1] * next[0];
        }
        return 0.5F * (float) Math.sqrt(sumX * sumX + sumY * sumY + sumZ * sumZ);
    }

    private static void rotatePolygon(BakedPolygon polygon, float[] pivot, float[] rotationDegrees) {
        if (rotationDegrees[0] == 0 && rotationDegrees[1] == 0 && rotationDegrees[2] == 0) return;

        float[] point = new float[3];
        for (int vertexIndex = 0; vertexIndex < polygon.vertexCount; vertexIndex++) {
            int offset = vertexIndex * 3;
            point[0] = polygon.positions[offset] - pivot[0];
            point[1] = polygon.positions[offset + 1] - pivot[1];
            point[2] = polygon.positions[offset + 2] - pivot[2];
            rotateXThenYThenZ(point, rotationDegrees);
            polygon.positions[offset] = point[0] + pivot[0];
            polygon.positions[offset + 1] = point[1] + pivot[1];
            polygon.positions[offset + 2] = point[2] + pivot[2];
        }

        point[0] = polygon.normalX;
        point[1] = polygon.normalY;
        point[2] = polygon.normalZ;
        rotateXThenYThenZ(point, rotationDegrees);
        polygon.normalX = point[0];
        polygon.normalY = point[1];
        polygon.normalZ = point[2];
    }

    private static void rotateXThenYThenZ(float[] point, float[] rotationDegrees) {
        double angleX = Math.toRadians(rotationDegrees[0]);
        double angleY = Math.toRadians(rotationDegrees[1]);
        double angleZ = Math.toRadians(rotationDegrees[2]);
        float sinX = (float) Math.sin(angleX), cosX = (float) Math.cos(angleX);
        float sinY = (float) Math.sin(angleY), cosY = (float) Math.cos(angleY);
        float sinZ = (float) Math.sin(angleZ), cosZ = (float) Math.cos(angleZ);

        // X
        float rotatedY = point[1] * cosX - point[2] * sinX;
        float rotatedZ = point[1] * sinX + point[2] * cosX;
        point[1] = rotatedY;
        point[2] = rotatedZ;

        // Y
        float rotatedX = point[0] * cosY + point[2] * sinY;
        rotatedZ = -point[0] * sinY + point[2] * cosY;
        point[0] = rotatedX;
        point[2] = rotatedZ;

        // Z
        rotatedX = point[0] * cosZ - point[1] * sinZ;
        rotatedY = point[0] * sinZ + point[1] * cosZ;
        point[0] = rotatedX;
        point[1] = rotatedY;
    }

    private static void convertToModelSpace(BakedPolygon polygon) {
        for (int vertexIndex = 0; vertexIndex < polygon.vertexCount; vertexIndex++) {
            int offset = vertexIndex * 3;
            polygon.positions[offset] = toModelX(polygon.positions[offset]);
            polygon.positions[offset + 1] = toModelY(polygon.positions[offset + 1]);
            polygon.positions[offset + 2] = toModelZ(polygon.positions[offset + 2]);
        }
        polygon.normalX = -polygon.normalX;
        polygon.normalY = -polygon.normalY;
    }

    private static float toModelX(float blockbenchX) {
        return -blockbenchX * PIXEL_SCALE;
    }

    private static float toModelY(float blockbenchY) {
        return (MODEL_ORIGIN_Y - blockbenchY) * PIXEL_SCALE;
    }

    private static float toModelZ(float blockbenchZ) {
        return blockbenchZ * PIXEL_SCALE;
    }

    private static String elementType(JsonObject element) {
        return element.has("type") ? element.get("type")
            .getAsString() : "cube";
    }

    private static boolean isExported(JsonObject element) {
        return !element.has("export") || element.get("export")
            .getAsBoolean();
    }

    private static boolean hasNoTexture(JsonObject face) {
        return face.has("texture") && face.get("texture")
            .isJsonNull();
    }

    private static float[] readVector(JsonObject object, String key) {
        if (!object.has(key) || !object.get(key)
            .isJsonArray()) return new float[] { 0, 0, 0 };
        return readArray(object.getAsJsonArray(key));
    }

    private static float[] readArray(JsonArray array) {
        float[] values = new float[array.size()];
        for (int index = 0; index < values.length; index++) {
            values[index] = array.get(index)
                .getAsFloat();
        }
        return values;
    }

    private static void mergeGroupDataIntoOutliner(JsonObject root) {
        if (!root.has("groups")) return;

        Map<String, JsonObject> groupsByUuid = new HashMap<>();
        for (JsonElement group : root.getAsJsonArray("groups")) {
            JsonObject groupObject = group.getAsJsonObject();
            groupsByUuid.put(groupObject.get("uuid").getAsString(), groupObject);
        }

        for (JsonElement outlinerEntry : root.getAsJsonArray("outliner")) {
            if (outlinerEntry.isJsonObject()) mergeGroupData(outlinerEntry.getAsJsonObject(), groupsByUuid);
        }
    }

    private static void mergeGroupData(JsonObject outlinerNode, Map<String, JsonObject> groupsByUuid) {
        JsonObject groupData = groupsByUuid.get(outlinerNode.get("uuid").getAsString());
        if (groupData != null) {
            for (String key : new String[] { "name", "origin", "rotation", "export" }) {
                if (groupData.has(key) && !outlinerNode.has(key)) outlinerNode.add(key, groupData.get(key));
            }
        }

        for (JsonElement child : outlinerNode.getAsJsonArray("children")) {
            if (child.isJsonObject()) mergeGroupData(child.getAsJsonObject(), groupsByUuid);
        }
    }

    private static float[] textureOffsetsV(JsonObject root) {
        JsonArray textures = root.getAsJsonArray("textures");
        int textureCount = textures == null ? 0 : textures.size();
        float[] offsets = new float[Math.max(1, textureCount)];

        float currentOffset = 0;
        for (int index = 0; index < textureCount; index++) {
            offsets[index] = currentOffset;
            currentOffset += textureSizeV(textures.get(index).getAsJsonObject(), root);
        }
        return offsets;
    }

    private static int combinedTextureWidth(JsonObject root) {
        JsonArray textures = root.getAsJsonArray("textures");
        if (textures == null || textures.size() == 0) return resolution(root, "width");

        float width = 0;
        for (JsonElement texture : textures) {
            JsonObject textureObject = texture.getAsJsonObject();
            width = Math.max(width, textureObject.has("uv_width") ? textureObject.get("uv_width").getAsFloat()
                : resolution(root, "width"));
        }
        return Math.round(width);
    }

    private static int combinedTextureHeight(JsonObject root) {
        JsonArray textures = root.getAsJsonArray("textures");
        if (textures == null || textures.size() == 0) return resolution(root, "height");

        float height = 0;
        for (JsonElement texture : textures) {
            height += textureSizeV(texture.getAsJsonObject(), root);
        }
        return Math.round(height);
    }

    private static float textureSizeV(JsonObject texture, JsonObject root) {
        return texture.has("uv_height") ? texture.get("uv_height").getAsFloat() : resolution(root, "height");
    }

    private static int resolution(JsonObject root, String key) {
        return root.getAsJsonObject("resolution").get(key).getAsInt();
    }

    private static float textureOffsetV(JsonObject face, float[] textureOffsetsV) {
        if (!face.has("texture") || !face.get("texture").isJsonPrimitive()) return 0;
        int textureIndex = face.get("texture").getAsInt();
        return textureIndex >= 0 && textureIndex < textureOffsetsV.length ? textureOffsetsV[textureIndex] : 0;
    }
}
