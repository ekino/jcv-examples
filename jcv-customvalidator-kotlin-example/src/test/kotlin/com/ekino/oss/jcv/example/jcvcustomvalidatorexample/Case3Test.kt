package com.ekino.oss.jcv.example.jcvcustomvalidatorexample

import com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.Companion.assertThatJson
import com.ekino.oss.jcv.core.JsonValueComparator
import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.ValueMatcherException

class Case3Test {

    @Test
    fun `should validate json content with custom validator`() {

        assertThatJson(
            // language=json
            """
            {
              "id": "fda7a233-99b9-4756-8ecc-826a1c5a9bf5",
              "reference_1": "REF_0123456789",
              "reference_2": "TEST-012"
            }
            """.trimIndent()
        )
            .using(validators {
                +defaultValidators()
                +templatedValidator<String>("my_ref") {
                    comparatorWithParameters {
                        MyRefComparator(
                            prefix = getFirstRequiredParam(),
                            size = getSecondParam()?.toInt() ?: 14
                        )
                    }
                }
            })
            .isValidAgainst(
                // language=json
                """
                {
                  "id": "{#uuid#}",
                  "reference_1": "{#my_ref:REF_#}",
                  "reference_2": "{#my_ref:TEST-;8#}"
                }
                """.trimIndent()
            )
    }

    private class MyRefComparator(private val prefix: String, private val size: Int?) : JsonValueComparator<String> {

        override fun hasCorrectValue(actual: String?, expected: String?): Boolean {
            if (actual != null && actual.startsWith(prefix) && actual.length == size) {
                return true
            }
            throw ValueMatcherException(
                "Reference format should be $size chars long and start by $prefix",
                expected,
                actual
            )
        }
    }
}
