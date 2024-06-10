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

@Mojo(name = "repackage", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class FastbootPackagerMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    @Parameter
    private String classifier;

    @Parameter(defaultValue = "${project.build.finalName}", required = true)
    private String finalName;

    private Log logger = getLog();

    @Override
    public void execute() throws MojoExecutionException {
        repackage();
        //处理loader
        try {
            CopyLoader.start(getTargetFile());
        } catch (Exception e) {
            throw new MojoExecutionException("write loader exception", e);
        }
    }

    private void repackage() throws MojoExecutionException {
        File sourceFile = project.getArtifact().getFile();
        Repackager repackager = new Repackager(sourceFile,logger);
        File target = getTargetFile();
        Set<Artifact> artifacts = project.getArtifacts();
        Libraries libraries = new ArtifactsLibraries(artifacts, Collections.emptyList(), getLog());
        try {
            repackager.repackage(target, libraries);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

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
