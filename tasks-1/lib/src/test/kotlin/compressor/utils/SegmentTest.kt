package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SegmentTest {

    @Test
    fun `check if segment contains element`() {
        val segment = Segment(l = 3, r = 7)

        assertFalse(-123 in segment)
        assertFalse(2 in segment)
        assertTrue(3 in segment)
        assertTrue(4 in segment)
        assertTrue(5 in segment)
        assertTrue(6 in segment)
        assertFalse(7 in segment)
        assertFalse(8 in segment)
        assertFalse(168 in segment)
    }
}
