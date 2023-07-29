package ru.aries.hacaton.base.util

import android.util.Patterns
import androidx.core.util.PatternsCompat
import java.util.regex.Pattern


object FieldValidators {

    fun isValidEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidSpace(name: String): Boolean {
        val spaceCasePattern = Pattern.compile("""^\S+$""")
        val spaceCasePatterMatcher = spaceCasePattern.matcher(name)
        return spaceCasePatterMatcher.matches()
    }

    fun isValidLength(name: String): Boolean {
        return name.length >= 3
    }

    fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isStringContainNumber(text: String): Boolean {
        val pattern = Pattern.compile(".*\\d.*")
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun isStringLowerAndUpperCase(text: String): Boolean {
        val lowerCasePattern = Pattern.compile(".*[a-z].*")
        val upperCasePattern = Pattern.compile(".*[A-Z].*")
        val lowerCasePatterMatcher = lowerCasePattern.matcher(text)
        val upperCasePatterMatcher = upperCasePattern.matcher(text)
        return if (!lowerCasePatterMatcher.matches()) false else upperCasePatterMatcher.matches()
    }

    fun isStringContainSpecialCharacter(text: String): Boolean {
        val specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9 ]")
        val specialCharacterMatcher = specialCharacterPattern.matcher(text)
        return specialCharacterMatcher.find()
    }
    // ^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\d)(?=.*[!#$%&? "]).*$
    fun isValidPassword(text: String): Boolean {
        val lowerCasePattern = Pattern.compile("""^.*(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$""")
        val lowerCasePatterMatcher = lowerCasePattern.matcher(text)
        return lowerCasePatterMatcher.matches()
    }

}