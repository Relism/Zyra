package dev.relism.ZyraMavenPlugin;

import dev.relism.Zyra.annotations.Zyra;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Mojo that generates TypeScript definitions from @Zyra annotated classes.
 */
@Mojo(name = "zyra-tsgen")
public class ZyraTsGenMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
    private File classesDir;

    @Override
    public void execute() {
        try {
            if (!classesDir.exists()) {
                getLog().warn("Directory target was not found: " + classesDir.getAbsolutePath());
                return;
            }

            URL[] urls = {classesDir.toURI().toURL()};
            try (URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader())) {
                List<Class<?>> zyraClasses = scanForZyraAnnotatedClasses(classesDir, classLoader);

                getLog().info("Found " + zyraClasses.size() + " @Zyra annotated classes:");
                for (Class<?> clazz : zyraClasses) {
                    getLog().info(" - " + clazz.getName());
                }

                // TODO: pass to tsgen
            }
        } catch (Exception e) {
            getLog().error("Error during classes scanning", e);
        }
    }

    private List<Class<?>> scanForZyraAnnotatedClasses(File classesDir, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        List<Class<?>> result = new ArrayList<>();
        final int prefixLength = classesDir.getAbsolutePath().length() + 1;

        for (File file : listClassFiles(classesDir)) {
            String path = file.getAbsolutePath();
            String className = path
                    .substring(prefixLength)
                    .replace(File.separatorChar, '.')
                    .replaceAll("\\.class$", "");

            Class<?> clazz = classLoader.loadClass(className);
            if (clazz.isAnnotationPresent(Zyra.class)) {
                result.add(clazz);
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
