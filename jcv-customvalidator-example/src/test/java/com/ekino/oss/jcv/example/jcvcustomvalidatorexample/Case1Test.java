package com.ekino.oss.jcv.example.jcvcustomvalidatorexample;

import java.util.LinkedList;
import java.util.List;

import com.ekino.oss.jcv.core.JsonValidator;
import com.ekino.oss.jcv.core.JsonValueComparator;
import com.ekino.oss.jcv.core.validator.Validators;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.ValueMatcherException;

import static com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.*;
import static com.ekino.oss.jcv.core.validator.Validators.*;
import static com.ekino.oss.jcv.example.jcvcustomvalidatorexample.util.ResourceLoader.*;

public class Case1Test {

    @Test
    void should_validate_json_content_with_custom_validator() {

        assertThatJson(loadJson("case1/actual.json"))
            .using(customValidators())
            .isValidAgainst(loadJson("case1/expected.json"));
    }

    private static List<JsonValidator> customValidators() {
        LinkedList<JsonValidator> validators = new LinkedList<>();

        validators.add(myRefValiadtor());
        validators.addAll(Validators.defaultValidators());

        return validators;
    }

    private static JsonValidator myRefValiadtor() {
        return templatedValidator("my_ref", new MyRefComparator());
    }

    private static class MyRefComparator implements JsonValueComparator<String> {

        @Override
        public boolean hasCorrectValue(String actual, String expected) {
            if (actual != null && actual.startsWith("REF_") && actual.length() == 14) {
                return true;
            }
            throw new ValueMatcherException("Invalid reference format", expected, actual);
        }
    }
}
