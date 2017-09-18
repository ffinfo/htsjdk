package htsjdk.tribble.annotation;

import htsjdk.HtsjdkTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit tests for {@link Strand}.
 * <p>
 *     Makes sure that {@link Strand} will behave according to contract in run-time.
 * </p>
 */
public class StrandTest extends HtsjdkTest {

    @Test
    public void testEncodeReturnNoNulls() {
        for (final Strand subject : Strand.values()) {
            Assert.assertNotNull(subject.encode());
        }
    }

    @Test
    public void testEncodeDecode() {
        for (final Strand subject : Strand.values()) {
            final String encode = subject.encode();
            final Strand back = Strand.decode(encode);
            Assert.assertSame(back, subject);
        }
    }

    @Test
    public void testEncodeDecodeAsChar() {
        for (final Strand subject : Strand.values()) {
            final char encode = subject.encodeAsChar();
            final Strand back = Strand.decode(encode);
            Assert.assertSame(back, subject);
        }
    }

    @Test
    public void testEncodeUniqueness() {
        Assert.assertEquals(Stream.of(Strand.values())
                .map(Strand::encode)
                .collect(Collectors.toSet()).size(), Strand.values().length);
    }

    @Test
    public void testEncodeAsCharUniqueness() {
        Assert.assertEquals(Stream.of(Strand.values())
                .map(Strand::encodeAsChar)
                .collect(Collectors.toSet()).size(), Strand.values().length);
    }

    @Test
    public void testEncodeAsStringAndCharAreEquivalent() {
        for (final Strand subject : Strand.values()) {
            final String str = subject.encode();
            final char ch = subject.encodeAsChar();
            Assert.assertEquals(str, String.valueOf(ch));
        }
    }

    @Test
    public void testAliases() {
        Assert.assertEquals(Strand.FORWARD, Strand.POSITIVE);
        Assert.assertEquals(Strand.REVERSE, Strand.NEGATIVE);
    }

    @Test(dataProvider = "invalidEncodingStringsData", expectedExceptions = IllegalArgumentException.class)
    public void testDecodeOfInvalidEncodingStrings(final String encoding) {
        Strand.decode(encoding);
    }

    @Test(dataProvider = "invalidEncodingCharsData", expectedExceptions = IllegalArgumentException.class)
    public void testDecodeOfInvalidEncodingChars(final char encoding) {
        Strand.decode(encoding);
    }

    @DataProvider(name = "invalidEncodingStringsData")
    public Object[][] invalidEncodingStringsData() {
        return Stream.of("", null, "+++", "---", "POSITIVE",
                "NEGATIVE", "NONE",
                "FORWARD", "REVERSE",
                " +", "- ", "p", "m", "x")
                .map(s -> new Object[] { s}).toArray(Object[][]::new);
    }

    @DataProvider(name = "invalidEncodingCharsData")
    public Object[][] invalidEncodingCharsData() {
        final Character[] testCases = new Character[] {(char) 0, 'c', '\t', '\n', (char) -1, 'p', 'n', 'x' };
        return Stream.of(testCases)
                .map(c -> new Object[] { c })
                .toArray(Object[][]::new);
    }
}
