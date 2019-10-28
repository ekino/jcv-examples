package com.ekino.oss.jcv.example.jcvcustomvalidatorexample;

import java.util.ArrayList;
import java.util.List;

import com.ekino.oss.jcv.core.JsonValidator;
import com.ekino.oss.jcv.core.JsonValueComparator;
import com.ekino.oss.jcv.core.validator.Validators;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.ValueMatcherException;

import static com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.*;
import static com.ekino.oss.jcv.core.initializer.Initializers.*;
import static com.ekino.oss.jcv.example.jcvcustomvalidatorexample.util.ResourceLoader.*;

public class Case2Test {

    @Test
    void should_validate_json_content_with_custom_validator() {

        assertThatJson(loadJson("case2/actual.json"))
            .using(customValidators())
            .isValidAgainst(loadJson("case2/expected.json"));
    }

    private static List<JsonValidator> customValidators() {
        List<JsonValidator> validators = new ArrayList<>();

        validators.add(myRefValiadtor());
        validators.addAll(Validators.defaultValidators());

        return validators;
    }

    private static JsonValidator myRefValiadtor() {
        return parameterizedValidator("my_ref", comparatorWith1Parameter(MyRefComparator::new));
    }

    private static class MyRefComparator implements JsonValueComparator<String> {

        private final String prefix;

        private MyRefComparator(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean hasCorrectValue(String actual, String expected) {
            if (actual != null && actual.startsWith(prefix) && actual.length() == 14) {
                return true;
            }
            throw new ValueMatcherException("Reference format should be 14 chars long and start by " + prefix, expected, actual);
        }
    }
}
