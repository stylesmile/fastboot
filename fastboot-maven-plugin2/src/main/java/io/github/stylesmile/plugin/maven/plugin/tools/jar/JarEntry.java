package io.github.stylesmile.plugin.maven.plugin.tools.jar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Extended variant of {@link java.util.jar.JarEntry} returned by {@link JarFile}s.
 *
 * @author Phillip Webb
 */
class JarEntry extends java.util.jar.JarEntry implements FileHeader {

	private Certificate[] certificates;

	private CodeSigner[] codeSigners;

	private final JarFile jarFile;

	private long localHeaderOffset;

	JarEntry(JarFile jarFile, CentralDirectoryFileHeader header) {
		super(header.getName().toString());
		this.jarFile = jarFile;
		this.localHeaderOffset = header.getLocalHeaderOffset();
		setCompressedSize(header.getCompressedSize());
		setMethod(header.getMethod());
		setCrc(header.getCrc());
		setSize(header.getSize());
		setExtra(header.getExtra());
		setComment(header.getComment().toString());
		setSize(header.getSize());
		setTime(header.getTime());
	}

	@Override
	public boolean hasName(String name, String suffix) {
		return getName().length() == name.length() + suffix.length()
				&& getName().startsWith(name) && getName().endsWith(suffix);
	}

	/**
	 * Return a {@link URL} for this {@link JarEntry}.
	 * @return the URL for the entry
	 * @throws MalformedURLException if the URL is not valid
	 */
	URL getUrl() throws MalformedURLException {
		return new URL(this.jarFile.getUrl(), getName());
	}

	@Override
	public Attributes getAttributes() throws IOException {
		Manifest manifest = this.jarFile.getManifest();
		return (manifest != null ? manifest.getAttributes(getName()) : null);
	}

	@Override
	public Certificate[] getCertificates() {
		if (this.jarFile.isSigned() && this.certificates == null) {
			this.jarFile.setupEntryCertificates(this);
		}
		return this.certificates;
	}

	@Override
	public CodeSigner[] getCodeSigners() {
		if (this.jarFile.isSigned() && this.codeSigners == null) {
			this.jarFile.setupEntryCertificates(this);
		}
		return this.codeSigners;
	}

	void setCertificates(java.util.jar.JarEntry entry) {
		this.certificates = entry.getCertificates();
		this.codeSigners = entry.getCodeSigners();
	}

	@Override
	public long getLocalHeaderOffset() {
		return this.localHeaderOffset;
	}

}
