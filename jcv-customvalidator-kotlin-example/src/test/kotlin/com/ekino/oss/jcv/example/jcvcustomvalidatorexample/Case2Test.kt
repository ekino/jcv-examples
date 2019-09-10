package com.ekino.oss.jcv.example.jcvcustomvalidatorexample

import com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.Companion.assertThatJson
import com.ekino.oss.jcv.core.JsonValueComparator
import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.ValueMatcherException

class Case2Test {

    @Test
    fun `should validate json content with custom validator`() {

        assertThatJson(
            """
            {
              "id": "fda7a233-99b9-4756-8ecc-826a1c5a9bf5",
              "reference_1": "REF_0123456789",
              "reference_2": "TEST-012345678"
            }
            """.trimIndent()
        )
            .using(validators {
                +defaultValidators()
                +templatedValidator<String>("my_ref") {
                    comparatorWith1RequiredParameter(::MyRefComparator)
                }
            })
            .isValidAgainst(
                """
                {
                  "id": "{#uuid#}",
                  "reference_1": "{#my_ref:REF_#}",
                  "reference_2": "{#my_ref:TEST-#}"
                }
                """.trimIndent()
            )
    }

    private class MyRefComparator(private val prefix: String) : JsonValueComparator<String> {

        override fun hasCorrectValue(actual: String?, expected: String?): Boolean {
            if (actual != null && actual.startsWith(prefix) && actual.length == 14) {
                return true
            }
            throw ValueMatcherException(
                "Reference format should be 14 chars long and start by $prefix",
                expected,
                actual
            )
        }
    }
}
