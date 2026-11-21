package gregtech.api.objects;

import gregtech.api.enums.Element;

public class ElementStack implements Cloneable {

    public int mAmount;
    public Element mElement;

    public ElementStack(Element aElement, int aAmount) {
        mElement = aElement == null ? Element._NULL : aElement;
        mAmount = aAmount;
    }

    public ElementStack copy(int aAmount) {
        return new ElementStack(mElement, aAmount);
    }

    @Override
    public ElementStack clone() {
        try {
            return (ElementStack) super.clone();
        } catch (Exception e) {
            return new ElementStack(mElement, mAmount);
        }
    }

    @Override
    public boolean equals(Object aObject) {
        if (aObject == this) return true;
        return switch (aObject) {
            case null -> false;
            case Element element -> aObject == mElement;
            case ElementStack elementStack -> elementStack.mElement == mElement
                && (mAmount < 0 || elementStack.mAmount < 0 || elementStack.mAmount == mAmount);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return mElement.toString() + mAmount;
    }

    @Override
    public int hashCode() {
        return mElement.hashCode();
    }
}
