/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("CharsKt")

package kotlin.text

/**
 * Returns `true` if this character (Unicode code point) is defined in Unicode.
 */
@kotlin.internal.InlineOnly
public inline fun Char.isDefined(): Boolean = Character.isDefined(this)

/**
 * Returns `true` if this character is a letter.
 * @sample samples.text.Chars.isLetter
 */
@kotlin.internal.InlineOnly
public inline fun Char.isLetter(): Boolean = Character.isLetter(this)

/**
 * Returns `true` if this character is a letter or digit.
 * @sample samples.text.Chars.isLetterOrDigit
 */
@kotlin.internal.InlineOnly
public inline fun Char.isLetterOrDigit(): Boolean = Character.isLetterOrDigit(this)

/**
 * Returns `true` if this character (Unicode code point) is a digit.
 * @sample samples.text.Chars.isDigit
 */
@kotlin.internal.InlineOnly
public inline fun Char.isDigit(): Boolean = Character.isDigit(this)


/**
 * Returns `true` if this character (Unicode code point) should be regarded as an ignorable
 * character in a Java identifier or a Unicode identifier.
 */
@kotlin.internal.InlineOnly
public inline fun Char.isIdentifierIgnorable(): Boolean = Character.isIdentifierIgnorable(this)

/**
 * Returns `true` if this character is an ISO control character.
 * @sample samples.text.Chars.isISOControl
 */
@kotlin.internal.InlineOnly
public inline fun Char.isISOControl(): Boolean = Character.isISOControl(this)

/**
 * Returns `true` if this  character (Unicode code point) may be part of a Java identifier as other than the first character.
 * @sample samples.text.Chars.isJavaIdentifierPart
 */
@kotlin.internal.InlineOnly
public inline fun Char.isJavaIdentifierPart(): Boolean = Character.isJavaIdentifierPart(this)

/**
 * Returns `true` if this character is permissible as the first character in a Java identifier.
 * @sample samples.text.Chars.isJavaIdentifierStart
 */
@kotlin.internal.InlineOnly
public inline fun Char.isJavaIdentifierStart(): Boolean = Character.isJavaIdentifierStart(this)

/**
 * Determines whether a character is whitespace according to the Unicode standard.
 * Returns `true` if the character is whitespace.
 * @sample samples.text.Chars.isWhitespace
 */
public actual fun Char.isWhitespace(): Boolean = Character.isWhitespace(this) || Character.isSpaceChar(this)

/**
 * Returns `true` if this character is upper case.
 * @sample samples.text.Chars.isUpperCase
 */
@kotlin.internal.InlineOnly
public inline fun Char.isUpperCase(): Boolean = Character.isUpperCase(this)

/**
 * Returns `true` if this character is lower case.
 * @sample samples.text.Chars.isLowerCase
 */
@kotlin.internal.InlineOnly
public inline fun Char.isLowerCase(): Boolean = Character.isLowerCase(this)

/**
 * Converts this character to upper case using Unicode mapping rules of the invariant locale.
 *
 * This function performs one-to-one character mapping using case mapping information from the UnicodeData file,
 * to support one-to-many character mapping use the [uppercase] function.
 * If this character has no mapping equivalent, the character itself is returned.
 *
 * @sample samples.text.Chars.uppercase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public actual inline fun Char.uppercaseChar(): Char = Character.toUpperCase(this)

/**
 * Converts this character to upper case using Unicode mapping rules of the invariant locale.
 *
 * If this character has no mapping equivalent, a [String] equal to [Char.toString] is returned.
 *
 * @sample samples.text.Chars.uppercase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public actual inline fun Char.uppercase(): String = toString().uppercase()

/**
 * Converts this character to lower case using Unicode mapping rules of the invariant locale.
 *
 * This function performs one-to-one character mapping using case mapping information from the UnicodeData file,
 * to support one-to-many character mapping use the [lowercase] function.
 * If this character has no mapping equivalent, the character itself is returned.
 *
 * @sample samples.text.Chars.lowercase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public actual inline fun Char.lowercaseChar(): Char = Character.toLowerCase(this)

/**
 * Converts this character to lower case using Unicode mapping rules of the invariant locale.
 *
 * If this character has no mapping equivalent, a [String] equal to [Char.toString] is returned.
 *
 * @sample samples.text.Chars.lowercase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public actual inline fun Char.lowercase(): String = toString().lowercase()

/**
 * Returns `true` if this character is a titlecase character.
 * @sample samples.text.Chars.isTitleCase
 */
@kotlin.internal.InlineOnly
public inline fun Char.isTitleCase(): Boolean = Character.isTitleCase(this)

/**
 * Converts this character to title case using Unicode mapping rules of the invariant locale.
 *
 * @see Character.toTitleCase
 */
@OptIn(ExperimentalStdlibApi::class)
@kotlin.internal.InlineOnly
public inline fun Char.toTitleCase(): Char = titlecaseChar()

/**
 * Converts this character to title case using Unicode mapping rules of the invariant locale.
 *
 * This function performs one-to-one character mapping using case mapping information from the UnicodeData file,
 * to support one-to-many character mapping use the [titlecase] function.
 * If this character has no mapping equivalent, the result of calling [uppercaseChar] is returned.
 *
 * @sample samples.text.Chars.titlecase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public inline fun Char.titlecaseChar(): Char = Character.toTitleCase(this)

/**
 * Converts this character to title case using Unicode mapping rules of the invariant locale.
 *
 * If this character has no mapping equivalent, the result of calling [uppercase] is returned.
 *
 * @sample samples.text.Chars.titlecase
 */
@SinceKotlin("1.4")
@ExperimentalStdlibApi
@kotlin.internal.InlineOnly
public fun Char.titlecase(): String {
    return titlecaseChar().let { if (it != uppercaseChar()) it.toString() else uppercase() }
}

/**
 * Returns a value indicating a character's general category.
 */
public val Char.category: CharCategory get() = CharCategory.valueOf(Character.getType(this))

/**
 * Returns the Unicode directionality property for the given character.
 */
public val Char.directionality: CharDirectionality get() = CharDirectionality.valueOf(Character.getDirectionality(this).toInt())

/**
 * Returns `true` if this character is a Unicode high-surrogate code unit (also known as leading-surrogate code unit).
 */
@kotlin.internal.InlineOnly
public actual inline fun Char.isHighSurrogate(): Boolean = Character.isHighSurrogate(this)

/**
 * Returns `true` if this character is a Unicode low-surrogate code unit (also known as trailing-surrogate code unit).
 */
@kotlin.internal.InlineOnly
public actual inline fun Char.isLowSurrogate(): Boolean = Character.isLowSurrogate(this)

// TODO Provide name for JVM7+
///**
// * Returns the Unicode name of this character, or `null` if the code point of this character is unassigned.
// */
//public fun Char.name(): String? = Character.getName(this.toInt())



internal actual fun digitOf(char: Char, radix: Int): Int = Character.digit(char.toInt(), radix)

/**
 * Checks whether the given [radix] is valid radix for string to number and number to string conversion.
 */
@PublishedApi
internal actual fun checkRadix(radix: Int): Int {
    if (radix !in Character.MIN_RADIX..Character.MAX_RADIX) {
        throw IllegalArgumentException("radix $radix was not in valid range ${Character.MIN_RADIX..Character.MAX_RADIX}")
    }
    return radix
}
