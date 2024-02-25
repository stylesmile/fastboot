package io.github.stylesmile.plugins.maven.tools.jar;


import io.github.stylesmile.plugins.maven.tools.data.RandomAccessData;

/**
 * Callback visitor triggered by {@link CentralDirectoryParser}.
 *
 * @author Phillip Webb
 */
interface CentralDirectoryVisitor {

	void visitStart(CentralDirectoryEndRecord endRecord,
                    RandomAccessData centralDirectoryData);

	void visitFileHeader(CentralDirectoryFileHeader fileHeader, int dataOffset);

	void visitEnd();

}
