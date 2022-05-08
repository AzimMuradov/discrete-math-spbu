package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MessageInfoTest {

    @Test
    fun `calculate message info`() {
        assertEquals(
            expected = MessageInfo(
                countedSymbols = mapOf(
                    'a' to 2,
                    'b' to 3,
                    'c' to 1,
                ),
                messageLength = 6
            ),
            actual = "abbcab".toList().calculateMessageInfo()
        )
    }
}
