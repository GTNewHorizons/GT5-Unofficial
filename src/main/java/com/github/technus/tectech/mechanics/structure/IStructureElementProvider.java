package com.github.technus.tectech.mechanics.structure;

public interface IStructureElementProvider<T> {
    IStructureElement<T> getStructureElement(T object);
}
