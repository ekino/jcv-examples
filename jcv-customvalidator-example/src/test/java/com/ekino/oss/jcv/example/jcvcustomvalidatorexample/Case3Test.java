package com.ekino.oss.jcv.example.jcvcustomvalidatorexample;

import java.util.LinkedList;
import java.util.List;

import com.ekino.oss.jcv.core.JsonValidator;
import com.ekino.oss.jcv.core.JsonValueComparator;
import com.ekino.oss.jcv.core.initializer.TwoParametersComparatorInitializer;
import com.ekino.oss.jcv.core.validator.Validators;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.ValueMatcherException;

import static com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.*;
import static com.ekino.oss.jcv.core.initializer.Initializers.*;
import static com.ekino.oss.jcv.example.jcvcustomvalidatorexample.util.ResourceLoader.*;
import static java.util.Optional.*;

public class Case3Test {

    @Test
    void should_validate_json_content_with_custom_validator() {

        assertThatJson(loadJson("case3/actual.json"))
            .using(customValidators())
            .isValidAgainst(loadJson("case3/expected.json"));
    }

    private static List<JsonValidator> customValidators() {
        LinkedList<JsonValidator> validators = new LinkedList<>();

        validators.add(myRefValiadtor());
        validators.addAll(Validators.defaultValidators());

        return validators;
    }

    private static JsonValidator myRefValiadtor() {
        return parameterizedValidator(
            "my_ref",
            comparatorWith2Parameters(true, false, initReferenceComparator(14))
        );
    }

    private static TwoParametersComparatorInitializer<String> initReferenceComparator(int defaultValue) {
        return (String param1, String param2) -> {
            Integer size = ofNullable(param2).map(Integer::parseInt).orElse(defaultValue);
            return new MyRefComparator(param1, size);
        };
    }

    private static class MyRefComparator implements JsonValueComparator<String> {

        private final String prefix;
        private final Integer size;

        private MyRefComparator(String prefix, Integer size) {
            this.prefix = prefix;
            this.size = size;
        }

        @Override
        public boolean hasCorrectValue(String actual, String expected) {
            if (actual != null && actual.startsWith(prefix) && actual.length() == size) {
                return true;
            }
            throw new ValueMatcherException("Reference format should be " + size + " chars long and start by " + prefix, expected, actual);
        }
    }
}
