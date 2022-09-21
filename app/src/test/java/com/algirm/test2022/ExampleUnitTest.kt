package com.algirm.test2022

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var isPalindrome: Boolean? = null
        val words = "a"
        val mutableListChar = words.lowercase().toMutableList()
        for (i in mutableListChar.indices.reversed()) {
            if (mutableListChar[i].isWhitespace()) {
                mutableListChar.removeAt(i)

            }
        }
        val reversedWords = mutableListChar.reversed()
        isPalindrome = mutableListChar == reversedWords
        assertEquals(true, "$isPalindrome $mutableListChar $reversedWords")
    }
}