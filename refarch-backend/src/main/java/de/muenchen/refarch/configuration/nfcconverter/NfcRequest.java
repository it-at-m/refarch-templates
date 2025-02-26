package de.muenchen.refarch.configuration.nfcconverter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;

/**
 * Wrapper for HttpServletRequest that performs NFC conversion.
 *
 * @see java.text.Normalizer
 */
@Slf4j
public class NfcRequest extends HttpServletRequestWrapper implements HttpServletRequest {

    private Map<String, String[]> params;

    private Cookie[] cookies;

    private Map<String, List<String>> headers;

    @SuppressWarnings("unused")
    final private Set<String> contentTypes;

    public NfcRequest(final HttpServletRequest request, final Set<String> contentTypes) {
        super(request);
        this.contentTypes = Set.copyOf(contentTypes);
    }

    private void convert() {
        if (params != null) {
            return;
        }
        this.params = NfcHelper.nfcConverter(getRequest().getParameterMap());
        this.cookies = NfcHelper.nfcConverter(getOriginalRequest().getCookies());
        this.headers = NfcHelper.nfcConverterForHeadersFromOriginalRequest(getOriginalRequest());
    }

    @Override
    public Cookie[] getCookies() {
        convert();
        return Arrays.copyOf(this.cookies, this.cookies.length);
    }

    @Override
    public String getHeader(final String name) {
        convert();
        final List<String> values = headers.get(NfcHelper.nfcConverter(name));
        return (values == null) ? null : values.getFirst();
    }

    @Override
    public Enumeration<String> getHeaders(final String name) {
        convert();
        final List<String> values = this.headers.get(NfcHelper.nfcConverter(name));
        return (values == null) ? Collections.emptyEnumeration() : IteratorUtils.asEnumeration(values.iterator());
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        convert();
        return IteratorUtils.asEnumeration(this.headers.keySet().iterator());
    }

    @Override
    public String getPathInfo() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getPathInfo());
    }

    @Override
    public String getPathTranslated() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getPathTranslated());
    }

    @Override
    public String getContextPath() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getContextPath());
    }

    @Override
    public String getQueryString() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getQueryString());
    }

    @Override
    public String getRemoteUser() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRemoteUser());
    }

    @Override
    public String getRequestedSessionId() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestedSessionId());
    }

    @Override
    public String getRequestURI() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestURI());
    }

    @Override
    public StringBuffer getRequestURL() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestURL());
    }

    /**
     * {@inheritDoc}
     *
     * Only the username is converted to nfc. Password won't be touched!
     */
    @Override
    public void login(final String username, final String password) throws ServletException {
        getOriginalRequest().login(NfcHelper.nfcConverter(username), password);
    }

    @Override
    public String getParameter(final String name) {
        convert();
        final String[] values = this.params.get(NfcHelper.nfcConverter(name));
        return (values == null) ? null : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        convert();
        return Map.copyOf(this.params);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        convert();
        return IteratorUtils.asEnumeration(this.params.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(final String name) {
        convert();
        return this.params.get(NfcHelper.nfcConverter(name));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        log.debug("getReader()");
        return new BufferedReader(new NfcReader(getOriginalRequest().getReader()));
    }

    @Override
    public String getRemoteHost() {
        return NfcHelper.nfcConverter(getRequest().getRemoteHost());
    }

    @Override
    public Part getPart(final String name) throws IOException, ServletException {
        log.debug("getPart({})", name);
        return getOriginalRequest().getPart(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        log.debug("getParts()");
        return getOriginalRequest().getParts();
    }

    @SuppressFBWarnings(value = "DM_DEFAULT_ENCODING", matchType = SuppressMatchType.EXACT)
    @Override
    public ServletInputStream getInputStream() throws IOException {

        final String encoding = getOriginalRequest().getCharacterEncoding();

        final String content;
        try (InputStream is = getOriginalRequest().getInputStream()) {
            content = new String(IOUtils.toByteArray(is), encoding);
        }

        log.debug("Converting InputStream data to NFC.");
        final String nfcConvertedContent = NfcHelper.nfcConverter(content);
        return new NfcServletInputStream(new ByteArrayInputStream(nfcConvertedContent.getBytes()));
    }

    private HttpServletRequest getOriginalRequest() {
        return (HttpServletRequest) getRequest();
    }

}
