package gt5codegen;

import static java.lang.constant.ConstantDescs.*;

import java.io.IOException;
import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.MethodParameterInfo;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.lang.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.ClassFileFormatVersion;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

public final class Gt5Codegen {

    // Naming here follows the convention in java.lang.constant.ConstantDescs
    static final ClassDesc CD_Material = ClassDesc.of("gregtech.codegen.Material");
    static final ClassDesc CD_MaterialInfo = ClassDesc.of("gregtech.codegen.MaterialInfo");
    static final ClassDesc CD_MaterialsBackend = ClassDesc.of("gregtech.codegen.MaterialsBackend");
    static final MethodTypeDesc MTD_Material = MethodTypeDesc.of(CD_Material);
    static final DirectMethodHandleDesc MHD_MaterialsBackend_accessBootstrap = ofCallsiteBootstrap(
        CD_MaterialsBackend,
        "accessBootstrap",
        CD_CallSite,
        CD_String);

    final Path inputDir, outputDir;

    /**
     * Parses arguments as passed by Gradle
     */
    Gt5Codegen() {
        inputDir = Path.of(Objects.requireNonNull(System.getProperty("gt5codegen.inputDir")));
        outputDir = Path.of(Objects.requireNonNull(System.getProperty("gt5codegen.outputDir")));
    }

    public void run() throws Exception {
        if (!Files.exists(inputDir)) {
            throw new IllegalStateException("Missing input directory " + inputDir);
        }
        // Clean up output before proceeding to avoid stale file presence
        deleteDirectoryTree(outputDir);
        Files.createDirectories(outputDir);

        // TODO: handle quoting etc., probably just pull in a csv library
        final List<List<String>> materialsCsv = Files.readString(inputDir.resolve("materials.csv"))
            .lines()
            .filter(l -> !l.isBlank())
            .map(l -> List.of(l.split(",")))
            .toList();

        final List<String> csvHeaderRaw = materialsCsv.getFirst();
        final List<List<String>> csvRows = materialsCsv.subList(1, materialsCsv.size());
        final List<MaterialProp> csvHeader = csvHeaderRaw.stream()
            .map(MaterialProp::new)
            .toList();
        final String[] csvHeaderNames = csvHeader.stream()
            .map(MaterialProp::name)
            .toArray(String[]::new);
        final ClassDesc[] csvHeaderDescs = csvHeader.stream()
            .map(MaterialProp::descriptor)
            .toArray(ClassDesc[]::new);

        if (!csvHeaderRaw.get(0)
            .equals("id:I")) {
            throw new IllegalArgumentException("The CSV's first column must be the numeric ID");
        }
        if (!csvHeaderRaw.get(1)
            .equals("name:Ljava/lang/String;")) {
            throw new IllegalArgumentException("The CSV's first column must be the name");
        }

        final Path packageDir = outputDir.resolve("gregtech")
            .resolve("generated");
        Files.createDirectories(packageDir);

        final ClassDesc CD_GeneratedMaterialProperties = ClassDesc.of("gregtech.generated.GeneratedMaterialProperties");
        final byte[] generatedPropertiesClass = ClassFile.of()
            .build(CD_GeneratedMaterialProperties, cb -> {
                cb.withVersion(ClassFileFormatVersion.RELEASE_8.major(), 0)
                    .withFlags(AccessFlag.PUBLIC);
                for (final MaterialProp prop : csvHeader) {
                    // Property backing field
                    int fieldAccess = AccessFlag.PROTECTED.mask();
                    if (prop.name()
                        .equals("id")
                        || prop.name()
                            .equals("name")) {
                        fieldAccess |= AccessFlag.FINAL.mask();
                    }
                    cb.withField(prop.name(), prop.descriptor(), fieldAccess);
                    // Property getter
                    cb.withMethodBody(
                        prop.name(),
                        MethodTypeDesc.of(prop.descriptor()),
                        AccessFlag.PUBLIC.mask(),
                        code -> {
                            code.aload(0);
                            code.getfield(CD_GeneratedMaterialProperties, prop.name(), prop.descriptor());
                            code.areturn();
                        });
                }
                // Copy constructor
                cb.withMethod(
                    INIT_NAME,
                    MethodTypeDesc.of(CD_void, CD_GeneratedMaterialProperties),
                    AccessFlag.PUBLIC.mask(),
                    mtd -> {
                        mtd.with(MethodParametersAttribute.of(MethodParameterInfo.of(Optional.of("copyFrom"))));
                        mtd.withCode(code -> {
                            final int S_THIS = 0;
                            final int S_ARG0 = 1;

                            code.aload(S_THIS);
                            code.invokespecial(CD_Object, INIT_NAME, MTD_void, false);

                            for (final MaterialProp prop : csvHeader) {
                                code.aload(S_THIS);
                                code.aload(S_ARG0);
                                code.getfield(CD_GeneratedMaterialProperties, prop.name(), prop.descriptor());
                                code.putfield(CD_GeneratedMaterialProperties, prop.name(), prop.descriptor());
                            }
                            code.return_();
                        });
                    });
                // Main constructor
                cb.withMethod(INIT_NAME, MethodTypeDesc.of(CD_void, csvHeaderDescs), AccessFlag.PUBLIC.mask(), mtd -> {
                    MethodParameterInfo[] methodParams = Arrays.stream(csvHeaderNames)
                        .map(n -> MethodParameterInfo.of(Optional.of(n)))
                        .toArray(MethodParameterInfo[]::new);
                    mtd.with(MethodParametersAttribute.of(methodParams));
                    mtd.withCode(code -> {
                        final int S_THIS = 0;

                        code.aload(S_THIS);
                        code.invokespecial(CD_Object, INIT_NAME, MTD_void, false);

                        for (int i = 0; i < csvHeader.size(); i++) {
                            final MaterialProp prop = csvHeader.get(i);
                            code.aload(S_THIS);
                            code.aload(1 + i);
                            code.putfield(CD_GeneratedMaterialProperties, prop.name(), prop.descriptor());
                        }
                        code.return_();
                    });
                });
                // Parser constructor, takes in a String[] of csv row arguments
                cb.withMethod(
                    INIT_NAME,
                    MethodTypeDesc.of(CD_void, CD_String.arrayType()),
                    AccessFlag.PUBLIC.mask(),
                    mtd -> {
                        mtd.with(MethodParametersAttribute.of(MethodParameterInfo.of(Optional.of("csvRow"))));
                        mtd.withCode(code -> {
                            final int S_THIS = 0;
                            final int S_CSV_ROW = 1;

                            code.aload(S_THIS);
                            code.invokespecial(CD_Object, INIT_NAME, MTD_void, false);

                            for (int i = 0; i < csvHeader.size(); i++) {
                                final MaterialProp prop = csvHeader.get(i);
                                code.aload(S_THIS); // putfield object
                                // compute putfield value
                                code.aload(S_CSV_ROW);
                                code.loadConstant(i);
                                code.aaload(); // csvRow[i] (String)
                                final ClassDesc desc = prop.descriptor();
                                if (desc.equals(CD_String)) {
                                    // no-op
                                } else if (desc.equals(CD_int)) {
                                    code.invokestatic(
                                        CD_Integer,
                                        "parseInt",
                                        MethodTypeDesc.of(CD_int, CD_String),
                                        false);
                                } else {
                                    throw new UnsupportedOperationException(
                                        "Missing material deserialization implementation in codegen for type " + desc);
                                }
                                code.putfield(CD_GeneratedMaterialProperties, prop.name(), prop.descriptor());
                            }
                            code.return_();
                        });
                    });
            });
        Files.write(packageDir.resolve("GeneratedMaterialProperties.class"), generatedPropertiesClass);

        final byte[] accessClass = ClassFile.of()
            .build(ClassDesc.of("gregtech.generated.MaterialsAccess"), cb -> {
                cb.withVersion(ClassFileFormatVersion.RELEASE_8.major(), 0)
                    .withFlags(AccessFlag.PUBLIC, AccessFlag.INTERFACE);
                for (final List<String> row : csvRows) {
                    final String matName = row.get(1);
                    cb.withMethod(matName, MTD_Material, AccessFlag.PUBLIC.mask() | AccessFlag.STATIC.mask(), mtd -> {
                        final StringBuilder ideInfo = new StringBuilder();
                        for (int i = 0; i < csvHeader.size(); i++) {
                            ideInfo.append(
                                csvHeader.get(i)
                                    .name());
                            ideInfo.append('=');
                            ideInfo.append(row.get(i));
                            ideInfo.append(' ');
                        }
                        ideInfo.deleteCharAt(ideInfo.length() - 1);
                        mtd.with(
                            RuntimeInvisibleAnnotationsAttribute.of(
                                Annotation
                                    .of(CD_MaterialInfo, AnnotationElement.ofString("info", ideInfo.toString()))));
                        mtd.withCode(code -> {
                            code.invokedynamic(
                                DynamicCallSiteDesc.of(MHD_MaterialsBackend_accessBootstrap, matName, MTD_Material));
                            code.areturn();
                        });

                    });
                }
            });
        Files.write(packageDir.resolve("MaterialsAccess.class"), accessClass);
    }

    static void deleteDirectoryTree(Path root) throws IOException {
        if (Files.exists(root)) {
            Files.walkFileTree(
                root,
                EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE,
                new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                        if (exc != null) {
                            throw exc;
                        }
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
        }
    }

    static void main() throws Exception {
        new Gt5Codegen().run();
    }
}

record MaterialProp(String name, ClassDesc descriptor) {

    public MaterialProp(String csvDef) {
        final String[] parts = csvDef.split(":", 2);
        this(parts[0], ClassDesc.ofDescriptor(parts[1]));
    }
}
