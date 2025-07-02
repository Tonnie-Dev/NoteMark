package com.tonyxlab.notemark.util

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {
    @Test
    fun `Test user initials formatting` () {

        val oneWordUsername = formatUserInitials("Tonnie")
        assertEquals(expected = "TO", actual = oneWordUsername)

        val twoWordsUsername = formatUserInitials("Tonnie Ein")
        assertEquals(expected = "TE", actual = twoWordsUsername)

        val threeWordsUsername = formatUserInitials("Tonnie Albert Ein")
        assertEquals(expected = "TE", actual = threeWordsUsername)


        val manyWordsUsername = formatUserInitials("Tonnie Albert Ein Newton")
        assertEquals(expected = "TN", actual = manyWordsUsername)
    }

}