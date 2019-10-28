package com.ekino.oss.jcv.example.jcvcustomvalidatorexample

import com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.Companion.assertThatJson
import com.ekino.oss.jcv.core.JsonValueComparator
import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.ValueMatcherException

class Case1Test {

    @Test
    fun `should validate json content with custom validator`() {

        assertThatJson(
            // language=json
            """
            {
              "id": "fda7a233-99b9-4756-8ecc-826a1c5a9bf5",
              "reference": "REF_0123456789"
            }
            """.trimIndent()
        )
            .using(validators {
                +defaultValidators()
                +templatedValidator("my_ref", MyRefComparator())
            })
            .isValidAgainst(
                // language=json
                """
                {
                  "id": "{#uuid#}",
                  "reference": "{#my_ref#}"
                }
                """.trimIndent()
            )
    }

    private class MyRefComparator : JsonValueComparator<String> {

        override fun hasCorrectValue(actual: String?, expected: String?): Boolean {
            if (actual != null && actual.startsWith("REF_") && actual.length == 14) {
                return true
            }
            throw ValueMatcherException("Invalid reference format", expected, actual)
        }
    }
}
