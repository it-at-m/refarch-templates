/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package refarch.configuration.nfcconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class NfcHelperTest {

    private static final String FIRST_NFD = "\u017f\u0307";

    private static final String FIRST_NFC = "\u1e9b";

    private static final String SECOND_NFD = "\u006f\u0302";

    private static final String SECOND_NFC = "\u00f4";

    private static final String THIRD_NFD = "\u0073\u0323\u0307";

    private static final String THIRD_NFC = "\u1e69";

    private static final String[] NFD_INPUT = new String[] { FIRST_NFD, SECOND_NFD, THIRD_NFD };

    private static final String[] NFC_OUTPUT_EXPECTED = new String[] { FIRST_NFC, SECOND_NFC, THIRD_NFC };

    @Test
    void nfcConverterString() {
        assertEquals(FIRST_NFC, NfcHelper.nfcConverter(FIRST_NFD));
        assertEquals(FIRST_NFC.length(), NfcHelper.nfcConverter(FIRST_NFD).length());

        assertEquals(SECOND_NFC, NfcHelper.nfcConverter(SECOND_NFD));
        assertEquals(SECOND_NFC.length(), NfcHelper.nfcConverter(SECOND_NFD).length());

        assertEquals(THIRD_NFC, NfcHelper.nfcConverter(THIRD_NFD));
        assertEquals(THIRD_NFC.length(), NfcHelper.nfcConverter(THIRD_NFD).length());
    }

    @Test
    void nfcConverterStringBuffer() {
        assertEquals(FIRST_NFC, NfcHelper.nfcConverter(new StringBuffer(FIRST_NFD)).toString());
        assertEquals(FIRST_NFC.length(), NfcHelper.nfcConverter(new StringBuffer(FIRST_NFD)).length());

        assertEquals(SECOND_NFC, NfcHelper.nfcConverter(new StringBuffer(SECOND_NFD)).toString());
        assertEquals(SECOND_NFC.length(), NfcHelper.nfcConverter(new StringBuffer(SECOND_NFD)).length());

        assertEquals(THIRD_NFC, NfcHelper.nfcConverter(new StringBuffer(THIRD_NFD)).toString());
        assertEquals(THIRD_NFC.length(), NfcHelper.nfcConverter(new StringBuffer(THIRD_NFD)).length());
    }

    @Test
    void nfcConverterStringArray() {
        assertArrayEquals(NFC_OUTPUT_EXPECTED, NfcHelper.nfcConverter(NFD_INPUT));
        assertEquals(NFC_OUTPUT_EXPECTED.length, NfcHelper.nfcConverter(NFD_INPUT).length);
    }

    @Test
    void nfcConverterMapOfStrings() {
        final Map<String, String[]> nfdInput = new HashMap<>();
        nfdInput.put(FIRST_NFD, NFD_INPUT);
        nfdInput.put(SECOND_NFD, NFD_INPUT);
        nfdInput.put(THIRD_NFD, NFD_INPUT);

        final Map<String, String[]> result = NfcHelper.nfcConverter(nfdInput);
        assertEquals(3, result.size());
        assertArrayEquals(NFC_OUTPUT_EXPECTED, result.get(FIRST_NFC));
        assertArrayEquals(NFC_OUTPUT_EXPECTED, result.get(SECOND_NFC));
        assertArrayEquals(NFC_OUTPUT_EXPECTED, result.get(THIRD_NFC));
    }

    @Test
    void nfcConverterCookie() {
        final Cookie nfcCookie = NfcHelper.nfcConverter(createNfdCookie());

        assertEquals(NfcConverterTest.TOKEN, nfcCookie.getName());
        assertEquals(Arrays.toString(NFC_OUTPUT_EXPECTED), nfcCookie.getValue());
        assertEquals(THIRD_NFC, nfcCookie.getDomain());
        assertEquals(THIRD_NFC, nfcCookie.getPath());
    }

    @Test
    void nfcConverterCookieArray() {
        final Cookie[] nfdCookies = Collections.nCopies(3, createNfdCookie()).toArray(new Cookie[3]);
        final Cookie[] nfcCookies = NfcHelper.nfcConverter(nfdCookies);
        Arrays.asList(nfcCookies).forEach(nfcCookie -> {
            assertEquals(NfcConverterTest.TOKEN, nfcCookie.getName());
            assertEquals(Arrays.toString(NFC_OUTPUT_EXPECTED), nfcCookie.getValue());
            assertEquals(THIRD_NFC, nfcCookie.getDomain());
            assertEquals(THIRD_NFC, nfcCookie.getPath());
        });
    }

    private static Cookie createNfdCookie() {
        final Cookie nfdCookie = new Cookie(NfcConverterTest.TOKEN, Arrays.toString(NFD_INPUT));
        nfdCookie.setDomain(THIRD_NFD);
        nfdCookie.setPath(THIRD_NFD);
        return nfdCookie;
    }

}
