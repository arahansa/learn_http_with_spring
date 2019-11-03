package com.arahansa.extended;

import groovy.util.logging.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.resource.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;

@Slf4j
public class CustomEncodedResourceResolver extends AbstractResourceResolver {

    /**
     * The default content codings.
     */
    public static final List<String> DEFAULT_CODINGS = Arrays.asList("br", "gzip");


    private final List<String> contentCodings = new ArrayList<>(DEFAULT_CODINGS);

    private final Map<String, String> extensions = new LinkedHashMap<>();


    public CustomEncodedResourceResolver() {
        this.extensions.put("gzip", ".gz");
        this.extensions.put("br", ".br");
    }


    /**
     * Configure the supported content codings in order of preference. The first
     * coding that is present in the {@literal "Accept-Encoding"} header for a
     * given request, and that has a file present with the associated extension,
     * is used.
     * <p><strong>Note:</strong> Each coding must be associated with a file
     * extension via {@link #registerExtension} or {@link #setExtensions}. Also
     * customizations to the list of codings here should be matched by
     * customizations to the same list in {@link CachingResourceResolver} to
     * ensure encoded variants of a resource are cached under separate keys.
     * <p>By default this property is set to {@literal ["br", "gzip"]}.
     * @param codings one or more supported content codings
     */
    public void setContentCodings(List<String> codings) {
        Assert.notEmpty(codings, "At least one content coding expected");
        this.contentCodings.clear();
        this.contentCodings.addAll(codings);
    }

    /**
     * Return a read-only list with the supported content codings.
     */
    public List<String> getContentCodings() {
        return Collections.unmodifiableList(this.contentCodings);
    }

    /**
     * Configure mappings from content codings to file extensions. A dot "."
     * will be prepended in front of the extension value if not present.
     * <p>By default this is configured with {@literal ["br" -> ".br"]} and
     * {@literal ["gzip" -> ".gz"]}.
     * @param extensions the extensions to use.
     * @see #registerExtension(String, String)
     */
    public void setExtensions(Map<String, String> extensions) {
        extensions.forEach(this::registerExtension);
    }

    /**
     * Return a read-only map with coding-to-extension mappings.
     */
    public Map<String, String> getExtensions() {
        return Collections.unmodifiableMap(this.extensions);
    }

    /**
     * Java config friendly alternative to {@link #setExtensions(Map)}.
     * @param coding the content coding
     * @param extension the associated file extension
     */
    public void registerExtension(String coding, String extension) {
        this.extensions.put(coding, (extension.startsWith(".") ? extension : "." + extension));
    }


    @Override
    protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath,
                                               List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("resolve Resource Internal : " + request + " , locations : "+locations + " ,  requestPath : "+requestPath);
        Resource resource = chain.resolveResource(request, requestPath, locations);
        logger.info("resource : "+ resource);
        if (resource == null || request == null) {
            return resource;
        }

        String acceptEncoding = getAcceptEncoding(request);
        logger.info("accept Encoding : "+ acceptEncoding);
        if (acceptEncoding == null) {
            return resource;
        }

        for (String coding : this.contentCodings) {
            logger.info("coding : "+coding);
            if (acceptEncoding.contains(coding)) {
                try {
                    logger.info("acceptEncoding :"+acceptEncoding +", with coding :"+coding);
                    String extension = getExtension(coding);
                    logger.info("extension :"+extension);
                    Resource encoded = new CustomEncodedResourceResolver.EncodedResource(resource, coding, extension);
                    logger.info("encoded :"+encoded+", exists():"+encoded.exists());
                    if (encoded.exists()) {
                        return encoded;
                    }
                }
                catch (IOException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("No " + coding + " resource for [" + resource.getFilename() + "]", ex);
                    }
                }
            }
        }
        return resource;
    }

    @Nullable
    private String getAcceptEncoding(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        return (header != null ? header.toLowerCase() : null);
    }

    private String getExtension(String coding) {
        String extension = this.extensions.get(coding);
        Assert.state(extension != null, () -> "No file extension associated with content coding " + coding);
        return extension;
    }

    @Override
    protected String resolveUrlPathInternal(String resourceUrlPath,
                                            List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("resolve URl path Internal : "+resourceUrlPath);
        return chain.resolveUrlPath(resourceUrlPath, locations);
    }


    /**
     * An encoded {@link HttpResource}.
     */
    static final class EncodedResource extends AbstractResource implements HttpResource {

        private final Resource original;

        private final String coding;

        private final Resource encoded;

        EncodedResource(Resource original, String coding, String extension) throws IOException {
            this.original = original;
            this.coding = coding;
            this.encoded = original.createRelative(original.getFilename() + extension);
        }


        @Override
        public InputStream getInputStream() throws IOException {
            return this.encoded.getInputStream();
        }

        @Override
        public boolean exists() {
            return this.encoded.exists();
        }

        @Override
        public boolean isReadable() {
            return this.encoded.isReadable();
        }

        @Override
        public boolean isOpen() {
            return this.encoded.isOpen();
        }

        @Override
        public boolean isFile() {
            return this.encoded.isFile();
        }

        @Override
        public URL getURL() throws IOException {
            return this.encoded.getURL();
        }

        @Override
        public URI getURI() throws IOException {
            return this.encoded.getURI();
        }

        @Override
        public File getFile() throws IOException {
            return this.encoded.getFile();
        }

        @Override
        public long contentLength() throws IOException {
            return this.encoded.contentLength();
        }

        @Override
        public long lastModified() throws IOException {
            return this.encoded.lastModified();
        }

        @Override
        public Resource createRelative(String relativePath) throws IOException {
            return this.encoded.createRelative(relativePath);
        }

        @Override
        @Nullable
        public String getFilename() {
            return this.original.getFilename();
        }

        @Override
        public String getDescription() {
            return this.encoded.getDescription();
        }

        @Override
        public HttpHeaders getResponseHeaders() {
            HttpHeaders headers;
            if (this.original instanceof HttpResource) {
                headers = ((HttpResource) this.original).getResponseHeaders();
            }
            else {
                headers = new HttpHeaders();
            }
            headers.add(HttpHeaders.CONTENT_ENCODING, this.coding);
            headers.add(HttpHeaders.VARY, HttpHeaders.ACCEPT_ENCODING);
            return headers;
        }
    }

}
