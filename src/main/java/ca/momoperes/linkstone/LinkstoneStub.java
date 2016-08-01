package ca.momoperes.linkstone;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LinkstoneStub {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar linkstone-stub.jar <pathToBuildToolsDirectory>");
            System.exit(1);
        }
        File buildToolsDir = new File(args[0]);
        if (!buildToolsDir.exists()) {
            System.out.println("Error: No such directory: " + args[0]);
            System.exit(1);
        }
        if (!buildToolsDir.isDirectory()) {
            System.out.println("Error: Path '" + args[0] + "' is not a directory.");
            System.exit(1);
        }
        File cbPath = new File(buildToolsDir, "CraftBukkit");
        if (!cbPath.exists()) {
            System.out.println("Error: Path '" + args[0] + "' is not a valid BuildTools directory.\n       " +
                    "Either you have not built archives using BuildTools yet, or the specified directory is not appropriate.");
            System.exit(1);
        }
        File srcPath = new File(cbPath, "src" + File.separator + "main" + File.separator + "java");
        if (!srcPath.exists()) {
            System.out.println("Error: Path '" + args[0] + "' is not a valid BuildTools directory.\n       " +
                    "Either you have not built archives using BuildTools yet, or the specified directory is not appropriate.");
            System.exit(1);
        }
        File output = new File("src" + File.separator + "main" + File.separator + "java");
        output.mkdirs();


        // CraftBukkit
        {
            String thePackage = "org.bukkit.craftbukkit".replace(".", File.separator);
            File pkgDir = new File(srcPath, thePackage);
            Collection<File> files = getAllFilesRecurive(pkgDir);
            for (File file : files) {
                if (file.getName().endsWith(".java")) {
                    System.out.print("Status: Applying vanish for " + file.getName() + "... ");
                    String pkg = file.getParentFile().getAbsolutePath().replace(pkgDir.getAbsolutePath(), "").replace(File.separator, ".");
                    String content = applyVanish(file, thePackage.replace(File.separator, ".") + pkg);
                    File destination = new File(output, thePackage + pkg.replace(".", File.separator));
                    destination.mkdirs();
                    try {
                        Files.deleteIfExists(new File(destination, file.getName()).toPath());
                        Files.createFile(new File(destination, file.getName()).toPath());
                        Files.write(new File(destination, file.getName()).toPath(), content.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Done");
                }
            }
            System.out.println("Done: CraftBukkit");
        }
        // Decompiled NMS
        {
            File workFolder = new File(buildToolsDir, "work");
            File decompFolder = null;
            for (File file : workFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            })) {
                if (file.getName().startsWith("decompile-")) {
                    decompFolder = file;
                }
            }
            if (decompFolder == null) {
                System.out.println("Error: Cannot find decompiled sources, aborting.");
                System.exit(1);
            }
            String thePackage = "net.minecraft.server".replace(".", File.separator);
            File pkgDir = new File(decompFolder, thePackage);
            Collection<File> files = getAllFilesRecurive(pkgDir);
            for (File file : files) {
                if (file.getName().endsWith(".java")) {
                    System.out.print("Status: Applying vanish for " + file.getName() + "... ");
                    String pkg = file.getParentFile().getAbsolutePath().replace(pkgDir.getAbsolutePath(), "").replace(File.separator, ".");
                    String content = applyVanish(file, thePackage.replace(File.separator, ".") + pkg);
                    File destination = new File(output, thePackage + pkg.replace(".", File.separator));
                    destination.mkdirs();
                    try {
                        Files.deleteIfExists(new File(destination, file.getName()).toPath());
                        Files.createFile(new File(destination, file.getName()).toPath());
                        Files.write(new File(destination, file.getName()).toPath(), content.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Done");
                }
            }
            System.out.println("Done: Decompiled NMS");
        }
        // Recompiled NMS
        {
            String thePackage = "net.minecraft.server".replace(".", File.separator);
            File pkgDir = new File(srcPath, thePackage);
            Collection<File> files = getAllFilesRecurive(pkgDir);
            for (File file : files) {
                if (file.getName().endsWith(".java")) {
                    System.out.print("Status: Applying vanish for " + file.getName() + "... ");
                    String pkg = file.getParentFile().getAbsolutePath().replace(pkgDir.getAbsolutePath(), "").replace(File.separator, ".");
                    String content = applyVanish(file, thePackage.replace(File.separator, ".") + pkg);
                    File destination = new File(output, thePackage + pkg.replace(".", File.separator));
                    destination.mkdirs();
                    try {
                        Files.deleteIfExists(new File(destination, file.getName()).toPath());
                        Files.createFile(new File(destination, file.getName()).toPath());
                        Files.write(new File(destination, file.getName()).toPath(), content.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Done");
                }
            }
            System.out.println("Done: Recompiled NMS");
        }
    }

    static String applyVanish(File sourceFile, String packageName) {
        try {
            String fileContent = new String(Files.readAllBytes(sourceFile.toPath()));
            fileContent = fileContent
                    .replace("Float float", "Float varFloat")
                    .replace("float.", "varFloat.")
                    .replace("varFloat...", "float...")
                    .replace("float =", "aFloat =")
                    .replace(".do,", "._do,")
                    .replace(" do;", " _do;")
                    .replace("do =", "_do =")
            ;

            CompilationUnit cu = JavaParser.parse(new StringBufferInputStream(fileContent));
            cu.setPackage(new PackageDeclaration(new NameExpr()));
            if (cu.getTypes() == null)
                return cu.toString();
            for (TypeDeclaration types : cu.getTypes()) {
                if (types.getMembers() == null)
                    continue;
                cleanType(types);
            }
            cu.setPackage(new PackageDeclaration(new NameExpr(packageName)));
            return cu.toString();
        } catch (ParseException | IOException e) {
            System.out.println("Error: Failed source file " + sourceFile.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }

    static void cleanType(TypeDeclaration type) {
        if (type.getMembers() == null)
            return;
        for (BodyDeclaration body : type.getMembers()) {
            if (body instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) body;
                if (ModifierSet.isAbstract(method.getModifiers()))
                    continue;
                if (method.getType() instanceof VoidType) {
                    List<Statement> statements = new ArrayList<>();
                    method.setBody(new BlockStmt(statements));
                } else {
                    List<Statement> statements = new ArrayList<>();
                    statements.add(new ReturnStmt(defaultForType(method.getType())));
                    method.setBody(new BlockStmt(statements));
                }
            } else if (body instanceof ConstructorDeclaration) {
                List<Statement> statements = new ArrayList<>();
                ((ConstructorDeclaration) body).setBlock(new BlockStmt(statements));
            } else if (body instanceof InitializerDeclaration) {
                List<Statement> statements = new ArrayList<>();
                ((InitializerDeclaration) body).setBlock(new BlockStmt(statements));
            } else if (body instanceof TypeDeclaration) {
                cleanType((TypeDeclaration) body);
            } else if (body instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) body;
                List<VariableDeclarator> variables = new ArrayList<>();
                for (VariableDeclarator variableDeclarator : field.getVariables()) {
                    variableDeclarator.setInit(defaultForType(field.getType()));
                    variables.add(variableDeclarator);
                }
                field.setVariables(variables);
            }
        }
    }

    static Expression defaultForType(Type type) {
        if (type instanceof PrimitiveType) {
            PrimitiveType.Primitive primitive = ((PrimitiveType) type).getType();
            if (primitive == PrimitiveType.Primitive.Boolean) {
                return new BooleanLiteralExpr(false);
            } else if (primitive == PrimitiveType.Primitive.Int) {
                return new IntegerLiteralExpr("0");
            } else if (primitive == PrimitiveType.Primitive.Double) {
                return new DoubleLiteralExpr("0.0");
            } else if (primitive == PrimitiveType.Primitive.Char) {
                return new CharLiteralExpr("?");
            } else if (primitive == PrimitiveType.Primitive.Byte) {
                return new IntegerLiteralExpr("0");
            } else if (primitive == PrimitiveType.Primitive.Float) {
                return new DoubleLiteralExpr("0.0F");
            } else if (primitive == PrimitiveType.Primitive.Long) {
                return new LongLiteralExpr("0");
            } else if (primitive == PrimitiveType.Primitive.Short) {
                return new IntegerLiteralExpr("0");
            }
        }
        return new NullLiteralExpr();
    }

    static Collection<File> getAllFilesRecurive(File origin) {
        Collection<File> files = new ArrayList<>();
        for (File file : getFiles(origin)) {
            if (file.isDirectory()) {
                files.addAll(getAllFilesRecurive(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }

    static File[] getFiles(File origin) {
        return origin.listFiles();
    }
}
