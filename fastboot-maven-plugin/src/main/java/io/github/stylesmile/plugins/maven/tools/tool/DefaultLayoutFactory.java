package io.github.stylesmile.plugins.maven.tools.tool;

import java.io.File;

/**
 * Default implementation of {@link LayoutFactory}.
 *
 * @author Phillip Webb
 * @since 1.5.0
 */
public class DefaultLayoutFactory implements LayoutFactory {

	@Override
	public Layout getLayout(File source) {
		return Layouts.forFile(source);
	}

}
