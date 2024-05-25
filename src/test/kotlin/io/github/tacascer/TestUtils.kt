package io.github.tacascer

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun beSamePrettyPrinted(expected: String) =
    Matcher<String> { value ->
        MatcherResult(
            value.replace("\r\n", "\n") == expected.replace("\r\n", "\n"),
            { "Expected:\n$expected\n\nActual:\n$value" },
            { "Expected not to be:\n$expected\n\nActual:\n$value" },
        )
    }

infix fun String.shouldBeSamePrettyPrintedAs(expected: String): String {
    this should beSamePrettyPrinted(expected)
    return this
}
