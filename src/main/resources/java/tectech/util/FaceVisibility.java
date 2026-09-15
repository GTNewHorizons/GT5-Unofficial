package tectech.util;

public class FaceVisibility {

    public boolean front = true, back = true;
    public boolean left = true, right = true;
    public boolean top = true, bottom = true;

    public boolean isEntireObscured() {
        return !front && !back && !left && !right && !top && !bottom;
    }
}
