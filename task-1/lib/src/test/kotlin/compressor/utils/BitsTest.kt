package compressor.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class BitsTest {

    @Test
    fun `convert bits to string`() {
        assertEquals(expected = "100", actual = Bits(listOf(B1, B0, B0)).toString())
    }

    @Test
    fun `convert list of bits to Bits class`() {
        assertEquals(expected = Bits(listOf(B1, B0, B0)), actual = listOf(B1, B0, B0).asBits())
    }

    @Test
    fun `convert bits to byte array`() {
        assertContentEquals(
            expected = listOf(0b01011101.toByte(), 0b01110000.toByte()).toByteArray(),
            actual = Bits(listOf(B0, B1, B0, B1, B1, B1, B0, B1, B0, B1, B1, B1)).toByteArray()
        )
    }

    @Test
    fun `convert byte array to bits`() {
        assertEquals(
            expected = Bits(listOf(B0, B1, B0, B1, B1, B1, B0, B1, B0, B1, B1, B1)),
            actual = listOf(0b01011101.toByte(), 0b01110000.toByte()).toByteArray().toBits(length = 12)
        )
    }

    @Test
    fun `convert string to bits`() {
        assertEquals(expected = Bits(listOf(B1, B0, B0)), actual = "100".toBits())
    }

    @Test
    fun `convert bit to string`() {
        assertEquals(expected = "0", actual = B0.toString())
    }

    @Test
    fun `convert int to bit`() {
        assertEquals(expected = B0, actual = 0.toBit())
        assertEquals(expected = B1, actual = 1.toBit())
        assertThrows<IllegalArgumentException>(message = "Bit can be either 0 or 1") { 2.toBit() }
    }

    @Test
    fun `bits + bit`() {
        assertEquals(expected = Bits(listOf(B1, B0, B0)), actual = Bits(listOf(B1, B0)) + B0)
    }

    @Test
    fun `bit + bits`() {
        assertEquals(expected = Bits(listOf(B1, B0, B0)), actual = B1 + Bits(listOf(B0, B0)))
    }

    @Test
    fun `bits + bits`() {
        assertEquals(expected = Bits(listOf(B1, B0, B0)), actual = Bits(listOf(B1, B0)) + Bits(listOf(B0)))
    }
}
