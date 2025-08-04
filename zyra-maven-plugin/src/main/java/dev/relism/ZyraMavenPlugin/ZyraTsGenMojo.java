package dev.relism.ZyraMavenPlugin;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.generator.TypeScriptGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Mojo that generates TypeScript definitions from @Zyra annotated classes.
 */
@Mojo(name = "zyra-tsgen")
public class ZyraTsGenMojo extends AbstractMojo {

    /**
     * Directory where compiled `.class` files are located.
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
    private File classesDir;

    /**
     * Output directory for generated `.ts` files. Defaults to `${project.build.directory}/zyra/tsout`.
     */
    @Parameter(property = "zyra.outputDir", defaultValue = "${project.build.directory}/zyra/tsout", readonly = true)
    private File outputDir;

    @Override
    public void execute() {
        try {
            if (!classesDir.exists()) {
                getLog().warn("Classes directory not found: " + classesDir.getAbsolutePath());
                return;
            }

            cleanOutputDirectory(outputDir);

            URL[] urls = {classesDir.toURI().toURL()};
            try (URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader())) {
                List<Class<?>> zyraClasses = scanForZyraAnnotatedClasses(classesDir, classLoader);

                getLog().info("Found " + zyraClasses.size() + " @Zyra annotated classes:");
                zyraClasses.forEach(clazz -> getLog().info(" - " + clazz.getName()));

                TypeScriptGenerator generator = new TypeScriptGenerator();
                generator.load(zyraClasses);
                List<TypeScriptGenerator.GeneratedFile> generatedFiles = generator.generate();

                for (TypeScriptGenerator.GeneratedFile file : generatedFiles) {
                    File outputFile = new File(outputDir, file.filename());
                    File parentDir = outputFile.getParentFile();

                    if (!parentDir.exists() && !parentDir.mkdirs()) {
                        getLog().error("Failed to create directories: " + parentDir.getAbsolutePath());
                        continue;
                    }

                    try (FileOutputStream out = new FileOutputStream(outputFile)) {
                        out.write(file.content().getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        getLog().info("Generated file: " + outputFile.getAbsolutePath());
                    } catch (IOException e) {
                        getLog().error("Failed to write file: " + outputFile.getAbsolutePath(), e);
                    }
                }
            }
        } catch (Exception e) {
            getLog().error("Error during class scanning or TypeScript generation", e);
        }
    }

    private void cleanOutputDirectory(File dir) {
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    cleanOutputDirectory(file);
                }
                if (!file.delete()) {
                    getLog().warn("Failed to delete: " + file.getAbsolutePath());
                }
            }
        }
    }


    private List<Class<?>> scanForZyraAnnotatedClasses(File classesDir, ClassLoader classLoader) throws ClassNotFoundException {
        List<Class<?>> result = new ArrayList<>();
        final int prefixLength = classesDir.getAbsolutePath().length() + 1;

        for (File file : listClassFiles(classesDir)) {
            String path = file.getAbsolutePath();
            String className = path
                    .substring(prefixLength)
                    .replace(File.separatorChar, '.')
                    .replaceAll("\\.class$", "");

            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(Zyra.class)) {
                    result.add(clazz);
                }
            } catch (Throwable t) {
                getLog().warn("Skipping class due to load error: " + className, t);
            }
        }

        return result;
    }

    private List<File> listClassFiles(File dir) {
        List<File> files = new ArrayList<>();
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File f : children) {
                    files.addAll(listClassFiles(f));
                }
            }
        } else if (dir.getName().endsWith(".class")) {
            files.add(dir);
        }
        return files;
    }
}
