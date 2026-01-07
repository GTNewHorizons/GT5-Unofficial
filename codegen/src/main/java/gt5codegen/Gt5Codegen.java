package gt5codegen;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.ClassFileFormatVersion;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public final class Gt5Codegen {

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

        final List<String> csvHeader = materialsCsv.getFirst();
        final List<List<String>> csvRows = materialsCsv.subList(1, materialsCsv.size() - 1);

        if (!csvHeader.get(0)
            .equals("id")) {
            throw new IllegalArgumentException("The CSV's first column must be the numeric ID");
        }
        if (!csvHeader.get(1)
            .equals("name")) {
            throw new IllegalArgumentException("The CSV's first column must be the name");
        }

        final byte[] mainClass = ClassFile.of()
            .build(ClassDesc.of("gregtech.generated.MaterialsAccess"), cb -> {
                cb.withVersion(ClassFileFormatVersion.RELEASE_8.major(), 0)
                    .withFlags(AccessFlag.PUBLIC, AccessFlag.INTERFACE);
                for (final List<String> row : csvRows) {
                    final int matId = Integer.parseInt(row.get(0));
                    final String matName = row.get(1);
                    cb.withMethodBody(
                        matName,
                        MethodTypeDesc.ofDescriptor("()V"),
                        AccessFlag.PUBLIC.mask(),
                        code -> { code.return_(); });
                }
            });

        final Path packageDir = outputDir.resolve("gregtech")
            .resolve("generated");
        Files.createDirectories(packageDir);
        Files.write(packageDir.resolve("MaterialsAccess.class"), mainClass);
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
