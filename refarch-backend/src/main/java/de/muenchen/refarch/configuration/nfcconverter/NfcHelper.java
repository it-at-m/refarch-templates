package de.muenchen.refarch.configuration.nfcconverter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

/**
 * Utility class for NFC normalization
 *
 * @see Normalizer
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class NfcHelper {

    /**
     * Converting a string to the canonical Unicode normal form (NFC)
     *
     * @param in Input string
     * @return Normalized string
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static String nfcConverter(final String in) {
        if (in == null) {
            log.debug("String BEFORE nfc conversion is \"null\".");
            return null;
        }

        log.debug("String BEFORE nfc conversion: \"{}\".", in);
        log.debug("Length of String BEFORE nfc conversion: {}.", in.length());
        final String nfcConvertedContent = Normalizer.normalize(in, Normalizer.Form.NFC);
        log.debug("String AFTER nfc conversion: \"{}\".", nfcConvertedContent);
        log.debug("Length of String AFTER nfc conversion: {}.", nfcConvertedContent.length());
        return nfcConvertedContent;
    }

    /**
     * Converting {@link StringBuffer} content to canonical Unicode normal form (NFC)
     *
     * @param in Input buffer
     * @return Normalized buffer
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static StringBuffer nfcConverter(final StringBuffer in) {
        return new StringBuffer(nfcConverter(in.toString()));
    }

    /**
     * Converting an array of strings into the canonical Unicode normal form (NFC)
     *
     * @param original Input array
     * @return Array with normalized strings
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static String[] nfcConverter(final String... original) {
        return Arrays.stream(original)
                .map(NfcHelper::nfcConverter)
                .toArray(String[]::new);
    }

    /**
     * Converting a {@link Map} of strings into the canonical Unicode normal form (NFC).
     *
     * @param original Input map
     * @return Map with normalized content
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static Map<String, String[]> nfcConverter(final Map<String, String[]> original) {
        final Map<String, String[]> nfcConverted = new HashMap<>(original.size());
        original.forEach((nfdKey, nfdValueArray) -> nfcConverted.put(
                nfcConverter(nfdKey),
                nfcConverter(nfdValueArray)));
        return nfcConverted;
    }

    /**
     * Converting a {@link Cookie} to the canonical Unicode normal form (NFC).
     *
     * @param original Input cookie
     * @return Cookie with normalized content
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    @SuppressFBWarnings(
            value = { "HTTPONLY_COOKIE", "INSECURE_COOKIE" },
            justification = "conversion only alters string based types, other attributes are copied from the original cookie",
            matchType = SuppressMatchType.EXACT
    )
    public static Cookie nfcConverter(final Cookie original) {
        final Cookie nfcCookie = new Cookie(nfcConverter(original.getName()), nfcConverter(original.getValue()));
        nfcCookie.setHttpOnly(original.isHttpOnly());
        nfcCookie.setSecure(original.getSecure());
        nfcCookie.setMaxAge(original.getMaxAge());
        nfcCookie.getAttributes().forEach((key, value) -> nfcCookie.setAttribute(nfcConverter(key), nfcConverter(value)));
        if (original.getDomain() != null) {
            nfcCookie.setDomain(nfcConverter(original.getDomain()));
        }
        nfcCookie.setPath(nfcConverter(original.getPath()));
        return nfcCookie;
    }

    /**
     * Converting an array of {@link Cookie}s to canonical Unicode normal form (NFC).
     *
     * @param original Input array of cookies
     * @return Array with normalized cookies
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static Cookie[] nfcConverter(final Cookie... original) {
        if (original == null) {
            return new Cookie[0];
        }
        return Arrays.stream(original)
                .map(NfcHelper::nfcConverter)
                .toArray(Cookie[]::new);
    }

    /**
     * Converting the headers of a {@link HttpServletRequest} from strings to the canonical Unicode
     * normal form (NFC).
     *
     * @param originalRequest The {@link HttpServletRequest} for extracting and converting the headers.
     * @return Map with normalized content.
     * @see #nfcConverter(String)
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     */
    public static Map<String, List<String>> nfcConverterForHeadersFromOriginalRequest(final HttpServletRequest originalRequest) {
        final Map<String, List<String>> converted = new CaseInsensitiveMap<>();
        Collections.list(originalRequest.getHeaderNames()).forEach(nfdHeaderName -> {
            final String nfcHeaderName = nfcConverter(nfdHeaderName);
            final List<String> nfcHeaderEntries = Collections.list(originalRequest.getHeaders(nfdHeaderName)).stream()
                    .map(NfcHelper::nfcConverter)
                    .collect(Collectors.toList());
            converted.put(nfcHeaderName, nfcHeaderEntries);
        });
        return converted;
    }

}
