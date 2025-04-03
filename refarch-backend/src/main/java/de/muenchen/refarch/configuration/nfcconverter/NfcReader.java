package de.muenchen.refarch.configuration.nfcconverter;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * <p>
 * Wrapper for readers that performs an NFC conversion.
 * </p>
 *
 * <strong>Please note:</strong>
 * <ul>
 * <li>With Java readers and writers, an NFC conversion can be carried out safely, as characters are
 * processed there.</li>
 * <li>Before reading the first character, this reader reads the complete text of the wrapped reader
 * into an internal buffer and performs NFC normalization on it.
 * The reason is that NFC conversion cannot be performed on a character-by-character basis.</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
public class NfcReader extends Reader {

    private final Reader original;

    private CharArrayReader converted;

    private void convert() {
        if (converted != null) {
            return;
        }

        log.debug("Converting Reader data to NFC.");
        try {
            final String nfdContent = IOUtils.toString(original);
            final String nfcConvertedContent = NfcHelper.nfcConverter(nfdContent);
            converted = new CharArrayReader(nfcConvertedContent.toCharArray());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read() throws IOException {
        convert();
        return converted.read();
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        convert();
        return converted.read(cbuf, off, len);
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public long skip(final long n) throws IOException {
        convert();
        return converted.skip(n);
    }

    @Override
    public boolean ready() throws IOException {
        convert();
        return converted.ready();
    }

    @Override
    public boolean markSupported() {
        convert();
        return converted.markSupported();
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException {
        convert();
        converted.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        convert();
        converted.reset();
    }

}
