package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.Vec3Impl;

import java.util.*;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class StructureDefinition<T> implements IStructureDefinition<T> {
    private final Map<Character, IStructureElement<T>> elements;
    private final Map<String, String> shapes;
    private final Map<String, IStructureElement<T>[]> structures;

    public static <B> Builder<B> builder() {
        return new Builder<>();
    }

    private StructureDefinition(
            Map<Character, IStructureElement<T>> elements,
            Map<String, String> shapes,
            Map<String, IStructureElement<T>[]> structures) {
        this.elements =elements;
        this.shapes=shapes;
        this.structures = structures;
    }

    public static class Builder<T> {
        private static final char A='\uA000';
        private static final char B='\uB000';
        private static final char C='\uC000';
        private char d ='\uD000';
        private final Map<Vec3Impl,Character> navigates;
        private final Map<Character, IStructureElement<T>> elements;
        private final Map<String, String> shapes;

        private Builder() {
            navigates=new HashMap<>();
            elements = new HashMap<>();
            shapes = new HashMap<>();
        }

        public Map<Character, IStructureElement<T>> getElements() {
            return elements;
        }

        public Map<String, String> getShapes() {
            return shapes;
        }

        /**
         * Casings go: 0 1 2 3 4 5 6 7 8 9 : ; < = > ?
         * <br/>
         * HatchAdders go: space ! " # $ % & ' ( ) *
         * @param name
         * @param structurePiece
         * @return
         */
        @Deprecated
        public Builder<T> addShapeOldApi(String name, String[][] structurePiece) {
            StringBuilder builder = new StringBuilder();
            if (structurePiece.length > 0) {
                for (String[] strings : structurePiece) {


                    if (strings.length > 0) {
                        for (String string : strings) {


                            for (int i = 0; i < string.length(); i++) {
                                char ch = string.charAt(i);
                                if(ch<' '){
                                    for (int c = 0; c < ch; c++) {
                                        builder.append(B);
                                    }
                                }else if(ch>'@'){
                                    for (int c = '@'; c < ch; c++) {
                                        builder.append(A);
                                    }
                                }else{
                                    builder.append(ch);
                                }
                            }


                            builder.append(B);
                        }
                        builder.setLength(builder.length() - 1);
                    }


                    builder.append(C);
                }
                builder.setLength(builder.length() - 1);
            }
            if(DEBUG_MODE){
                Exception exception = new Exception();
                exception.getStackTrace();

                TecTech.LOGGER.info("Structure shape normal:");


                TecTech.LOGGER.info("Structure shape transposed:");

            }
            int a=0,b=0,c=0;
            for (int i = 0; i < builder.length(); i++) {
                char ch = builder.charAt(i);
                if(ch =='.'){
                    builder.setCharAt(i,A);
                    ch=A;
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
                    Vec3Impl vec3 = new Vec3Impl(a, b, c);
                    Character navigate = navigates.get(vec3);
                    if(navigate==null){
                        navigate= d++;
                        navigates.put(vec3,navigate);
                        addElement(navigate,step(vec3));
                    }
                    builder.setCharAt(i-1,navigate);
                    a=0;
                    b=0;
                    c=0;
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

        /**
         * Adds shape
         * +- is air/no air checks
         * space bar is skip
         * . is also skip (but marks controller position, optional and logically it is a space...)
         * rest needs to be defined
         *
         * next char is next block(a)
         * next string is next line(b)
         * next string[] is next slice(c)
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
            for (int i = 0; i < builder.length(); i++) {
                char ch = builder.charAt(i);
                if(ch ==' ' || ch =='.'){
                    builder.setCharAt(i,A);
                    ch=A;
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
                    Vec3Impl vec3 = new Vec3Impl(a, b, c);
                    Character navigate = navigates.get(vec3);
                    if(navigate==null){
                        navigate=d++;
                        navigates.put(vec3,navigate);
                        addElement(navigate,step(vec3));
                    }
                    builder.setCharAt(i-1,navigate);
                    a=0;
                    b=0;
                    c=0;
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

        public Builder<T> addElement(Character name, IStructureElement<T> structurePiece) {
            elements.putIfAbsent(name, structurePiece);
            return this;
        }

        public IStructureDefinition<T> build() {
            Map<String, IStructureElement<T>[]> structures = compileStructureMap();
            if(DEBUG_MODE){
                return new StructureDefinition<>(new HashMap<>(elements), new HashMap<>(shapes), structures);
            }else {
                return structures::get;
            }
        }

        @SuppressWarnings("unchecked")
        private Map<String, IStructureElement<T>[]> compileElementSetMap() {
            Set<Integer> missing = new HashSet<>();
            shapes.values().stream().map(CharSequence::chars).forEach(intStream -> intStream.forEach(c -> {
                IStructureElement<T> iStructureElement = elements.get((char) c);
                if (iStructureElement == null) {
                    missing.add(c);
                }
            }));
            if (missing.isEmpty()) {
                Map<String, IStructureElement<T>[]> map = new HashMap<>();
                shapes.forEach((key, value) -> {
                    Set<Character> chars=new HashSet<>();
                    for (char c : value.toCharArray()) {
                        chars.add(c);
                    }
                    IStructureElement<T>[] compiled = new IStructureElement[chars.size()];
                    int i=0;
                    for (Character aChar : chars) {
                        compiled[i++]=elements.get(aChar);
                    }
                    map.put(key, compiled);
                });
                return map;
            } else {
                throw new RuntimeException("Missing Structure Element bindings for (chars as integers): " +
                        Arrays.toString(missing.toArray()));
            }
        }

        @SuppressWarnings("unchecked")
        private Map<String, IStructureElement<T>[]> compileStructureMap() {
            Set<Integer> mising = new HashSet<>();
            shapes.values().stream().map(CharSequence::chars).forEach(intStream -> intStream.forEach(c -> {
                IStructureElement<T> iStructureElement = elements.get((char) c);
                if (iStructureElement == null) {
                    mising.add(c);
                }
            }));
            if (mising.isEmpty()) {
                Map<String, IStructureElement<T>[]> map = new HashMap<>();
                shapes.forEach((key, value) -> {
                    IStructureElement<T>[] compiled = new IStructureElement[value.length()];
                    for (int i = 0; i < value.length(); i++) {
                        compiled[i] = elements.get(value.charAt(i));
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

    public Map<Character, IStructureElement<T>> getElements(){
        return elements;
    }

    public Map<String, String> getShapes() {
        return shapes;
    }

    public Map<String, IStructureElement<T>[]> getStructures() {
        return structures;
    }

    @Override
    public IStructureElement<T>[] getStructureFor(String name) {
        return structures.get(name);
    }
}