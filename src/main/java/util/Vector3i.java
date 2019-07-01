package util;

public class Vector3i implements Vector3ic {

	public int x, y, z;
	
	public Vector3i() {
		x = 1;
		y = 1;
		z = 1;
	}
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

	
}
