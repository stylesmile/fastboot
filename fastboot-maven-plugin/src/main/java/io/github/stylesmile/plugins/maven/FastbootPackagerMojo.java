package io.github.stylesmile.plugins.maven;

import io.github.stylesmile.plugins.maven.tools.tool.ArtifactsLibraries;
import io.github.stylesmile.plugins.maven.tools.tool.Libraries;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * FastBoot Maven 打包插件主类。
 * <p>
 * 该插件用于将普通的 JAR 包重新打包为可执行的 Fat JAR，
 * 包含所有依赖库并配置正确的启动类。
 * <p>
 * 使用示例：
 * <pre>{@code
 * <build>
 *     <plugins>
 *         <plugin>
 *             <groupId>io.github.stylesmile</groupId>
 *             <artifactId>fastboot-maven-plugin</artifactId>
 *             <version>${fastboot.version}</version>
 *             <executions>
 *                 <execution>
 *                     <phase>package</phase>
 *                     <goals>
 *                         <goal>repackage</goal>
 *                     </goals>
 *                 </execution>
 *             </executions>
 *         </plugin>
 *     </plugins>
 * </build>
 * }</pre>
 *
 * @author Stylesmile
 * @since 2.10.0
 */
@Mojo(name = "repackage", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class FastbootPackagerMojo extends AbstractMojo {

    /**
     * Maven 项目对象，由 Maven 自动注入。
     */
    @Component
    private MavenProject project;

    /**
     * 输出目录，默认为 ${project.build.directory}（通常是 target/）。
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    /**
     * 分类器，用于区分不同类型的构建产物。
     * 如果设置，会添加到文件名中，例如：myapp-exec.jar
     */
    @Parameter
    private String classifier;

    /**
     * 最终文件名，默认为 ${project.build.finalName}。
     */
    @Parameter(defaultValue = "${project.build.finalName}", required = true)
    private String finalName;

    /**
     * Maven 日志记录器。
     */
    private Log logger = getLog();

    /**
     * 执行插件的主要入口点。
     * <p>
     * 该方法会被 Maven 在指定的生命周期阶段调用，执行以下操作：
     * <ol>
     *     <li>重新打包 JAR 文件，嵌入所有依赖</li>
     *     <li>处理 ClassLoader，确保正确的类加载顺序</li>
     * </ol>
     *
     * @throws MojoExecutionException 当打包过程中发生错误时抛出
     * @throws MojoFailureException   当插件执行失败时抛出
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        repackage();
        // 处理 loader，确保类加载器正确配置
        try {
            CopyLoader.start(getTargetFile());
        } catch (Exception e) {
            throw new MojoExecutionException("write loader exception", e);
        }
    }

    /**
     * 执行 JAR 重新打包操作。
     * <p>
     * 该方法会：
     * <ol>
     *     <li>获取原始的 JAR 文件</li>
     *     <li>创建 Repackager 实例</li>
     *     <li>收集项目的所有依赖库</li>
     *     <li>将依赖库嵌入到目标 JAR 中</li>
     * </ol>
     *
     * @throws MojoExecutionException 当重新打包失败时抛出
     */
    private void repackage() throws MojoExecutionException {
        File sourceFile = project.getArtifact().getFile();
        Repackager repackager = new Repackager(sourceFile, logger);
        File target = getTargetFile();
        Set<Artifact> artifacts = project.getArtifacts();
        Libraries libraries = new ArtifactsLibraries(artifacts, Collections.emptyList(), getLog());
        try {
            repackager.repackage(target, libraries);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * 计算目标文件的路径。
     * <p>
     * 根据分类器和输出目录生成最终的 JAR 文件路径。
     * 如果分类器不为空，会自动添加 "-" 前缀。
     *
     * @return 目标文件的 File 对象
     */
    private File getTargetFile() {
        String classifier = (this.classifier != null ? this.classifier.trim() : "");
        if (classifier.length() > 0 && !classifier.startsWith("-")) {
            classifier = "-" + classifier;
        }
        if (!this.outputDirectory.exists()) {
            this.outputDirectory.mkdirs();
        }
        String name = this.finalName + classifier + "." + this.project.getArtifact().getArtifactHandler().getExtension();
        return new File(this.outputDirectory, name);
    }
}
