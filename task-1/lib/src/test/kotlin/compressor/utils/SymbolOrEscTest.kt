package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class SymbolOrEscTest {

    @Test
    fun `compare two 'symbol or esc' objects`() {
        val a = SymbolOrEsc.Symbol(value = 'a')
        val b = SymbolOrEsc.Symbol(value = 'b')
        val esc = SymbolOrEsc.Esc<Char>()

        assertTrue(a <= a)
        assertTrue(b <= b)
        assertTrue(esc <= esc)

        assertTrue(a <= b)
        assertTrue(b >= a)

        assertTrue(esc <= a)
        assertTrue(a >= esc)

        assertTrue(esc <= b)
        assertTrue(b >= esc)
    }
}
