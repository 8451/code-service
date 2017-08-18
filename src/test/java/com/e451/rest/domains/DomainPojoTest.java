package com.e451.rest.domains;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsExceptStaticFinalRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;


@TestPropertySource("application-test.properties")
public class DomainPojoTest {
    private List<PojoClass> pojoClasses;
    private final PojoClassFilter filterTestClasses = new FilterTestClasses();

    @Before
    public void setup(){
        String packageName = "com.e451.rest.domains"; // only scan the DTOs
        pojoClasses = PojoClassFactory.getPojoClassesRecursively(packageName, filterTestClasses);
    }

    @Test
    public void validate() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule())
                .with(new GetterMustExistRule())
                .with(new SetterTester())
                .with(new GetterTester())
                .with(new NoPublicFieldsExceptStaticFinalRule())
                .build();

        validator.validate(pojoClasses);
    }

    // Filter out test classes copied to target/test-classes by Maven
    private static class FilterTestClasses implements PojoClassFilter {
        public boolean include(PojoClass pojoClass) {
            return !pojoClass.getSourcePath().contains("test-classes");
        }
    }
}
