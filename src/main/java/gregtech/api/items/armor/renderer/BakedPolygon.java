package gregtech.api.items.armor.renderer;

public final class BakedPolygon {

    public final int vertexCount;
    public final float[] positions;
    public final float[] textureUVs;
    public float normalX, normalY, normalZ;

    public BakedPolygon(int vertexCount) {
        this.vertexCount = vertexCount;
        this.positions = new float[vertexCount * 3];
        this.textureUVs = new float[vertexCount * 2];
    }

    public void setVertex(int vertexIndex, float x, float y, float z, float u, float v) {
        positions[vertexIndex * 3] = x;
        positions[vertexIndex * 3 + 1] = y;
        positions[vertexIndex * 3 + 2] = z;
        textureUVs[vertexIndex * 2] = u;
        textureUVs[vertexIndex * 2 + 1] = v;
    }

    public void computeNormal() {
        float edgeAX = positions[3] - positions[0];
        float edgeAY = positions[4] - positions[1];
        float edgeAZ = positions[5] - positions[2];
        float edgeBX = positions[6] - positions[0];
        float edgeBY = positions[7] - positions[1];
        float edgeBZ = positions[8] - positions[2];

        float crossX = edgeAY * edgeBZ - edgeAZ * edgeBY;
        float crossY = edgeAZ * edgeBX - edgeAX * edgeBZ;
        float crossZ = edgeAX * edgeBY - edgeAY * edgeBX;
        float length = (float) Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
        if (length < 1e-6f) return;

        normalX = crossX / length;
        normalY = crossY / length;
        normalZ = crossZ / length;
    }

    public BakedPolygon copyWithUVTransform(float scaleU, float scaleV, float offsetU, float offsetV) {
        BakedPolygon copy = new BakedPolygon(vertexCount);
        System.arraycopy(positions, 0, copy.positions, 0, positions.length);
        for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            copy.textureUVs[vertexIndex * 2] = textureUVs[vertexIndex * 2] * scaleU + offsetU;
            copy.textureUVs[vertexIndex * 2 + 1] = textureUVs[vertexIndex * 2 + 1] * scaleV + offsetV;
        }
        copy.normalX = normalX;
        copy.normalY = normalY;
        copy.normalZ = normalZ;
        return copy;
    }
}
