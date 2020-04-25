package com.github.technus.tectech.mechanics.structure;

import java.util.*;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class StructureDefinition<T> implements IStructureDefinition<T> {
    private final Map<Character, IStructureElementProvider<T>> elements;
    private final Map<String, String> shapes;
    private final Map<String, IStructureElementProvider<T>[]> compiled;

    public static <B> Builder<B> builder() {
        return new Builder<>();
    }

    private StructureDefinition(
            Map<Character, IStructureElementProvider<T>> elements,
            Map<String, String> shapes,
            Map<String, IStructureElementProvider<T>[]> compiled) {
        this.elements =elements;
        this.shapes=shapes;
        this.compiled =compiled;
    }

    public static class Builder<T> {
        private static final char A='\uA000';
        private static final char B='\uB000';
        private static final char C='\uC000';
        private static final char D='\uD000';
        private final Map<Character, IStructureElementProvider<T>> elements;
        private final Map<String, String> shapes;

        private Builder() {
            elements = new HashMap<>();
            shapes = new HashMap<>();
        }

        public Map<Character, IStructureElementProvider<T>> getElements() {
            return elements;
        }

        public Map<String, String> getShapes() {
            return shapes;
        }

        /**
         * Adds shape
         * +- is air/no air checks
         * space bar is skip
         * . is also skip (but marks controller position, optional and logically it is a space...)
         * rest needs to be defined
         *
         * next char is next block(a)
         * next string is next line(a,b)
         * next string[] is next slice(a,b,c)
         *
         * char A000-FFFF range is reserved for generated skips
         * @param name unlocalized/code name
         * @param structurePiece generated or written struct - DO NOT STORE IT ANYWHERE, or at least set them to null afterwards
         * @return this builder
         */
        public Builder<T> addShape(String name, String[][] structurePiece) {
            StringBuilder builder = new StringBuilder();
            if (structurePiece.length > 0) {
                for (String[] strings : structurePiece) {
                    if (strings.length > 0) {
                        for (String string : strings) {
                            builder.append(string).append(B);
                        }
                        builder.setLength(builder.length() - 1);
                    }
                    builder.append(C);
                }
                builder.setLength(builder.length() - 1);
            }
            int a=0,b=0,c=0;
            char d=D;
            for (int i = 0; i < builder.length(); i++) {
                char ch = builder.charAt(i);
                if(ch ==' ' || ch =='.'){
                    builder.setCharAt(i,A);
                }
                if(ch==A){
                    a++;
                }else if(ch==B){
                    a=0;
                    b++;
                }else if(ch==C){
                    a=0;
                    b=0;
                    c++;
                }else if(a!=0 || b!=0 || c!=0){
                    builder.setCharAt(i-1,d);
                    addElement(d,step(a,b,c));
                    a=0;
                    b=0;
                    c=0;
                    d++;
                }
            }

            String built = builder.toString().replaceAll("[\\uA000\\uB000\\uC000]","");

            if(built.contains("+")){
                addElement('+',notAir());
            }
            if (built.contains("-")) {
                addElement('-', isAir());
            }
            shapes.put(name, built);
            return this;
        }

        public Builder<T> addElement(Character name, IStructureElementProvider<T> structurePiece) {
            elements.put(name, structurePiece);
            return this;
        }

        public IStructureDefinition<T> build() {
            if(DEBUG_MODE){
                return new StructureDefinition<>(new HashMap<>(elements), new HashMap<>(shapes), compileMap());
            }else {
                return compileMap()::get;
            }
        }

        @SuppressWarnings("unchecked")
        private Map<String, IStructureElementProvider<T>[]> compileMap() {
            List<Integer> mising = new ArrayList<>();
            shapes.values().stream().map(CharSequence::chars).forEach(intStream -> intStream.forEach(c -> {
                IStructureElementProvider<T> iStructureElement = elements.get((char) c);
                if (iStructureElement == null) {
                    mising.add(c);
                }
            }));
            if (mising.isEmpty()) {
                Map<String, IStructureElementProvider<T>[]> map = new HashMap<>();
                shapes.forEach((key, value) -> {
                    IStructureElementProvider<T>[] compiled = new IStructureElementProvider[value.length()];
                    for (char i = 0; i < compiled.length; i++) {
                        compiled[i] = this.elements.get(i);
                    }
                    map.put(key, compiled);
                });
                return map;
            } else {
                throw new RuntimeException("Missing Structure Element bindings for (chars as integers): " +
                        Arrays.toString(mising.toArray()));
            }
        }
    }

    public Map<Character, IStructureElementProvider<T>> getElements(){
        return elements;
    }

    public Map<String, String> getShapes() {
        return shapes;
    }

    public Map<String, IStructureElementProvider<T>[]> getCompiled() {
        return compiled;
    }

    @Override
    public IStructureElementProvider<T>[] getElementsFor(String name) {
        return compiled.get(name);
    }
}