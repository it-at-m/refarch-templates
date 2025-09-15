package de.muenchen.refarch.archunit.rules;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class TestClassesEndWithTestCondition extends ArchCondition<JavaMethod> {

    TestClassesEndWithTestCondition() {
        super("have top enclosing class name ending with `Test`");
    }

    public static final ArchCondition<JavaMethod> haveTopEnclosingClassEndingWithTest = new TestClassesEndWithTestCondition();

    @Override
    public void check(JavaMethod method, ConditionEvents events) {
        var topEnclosingClass = getTopEnclosingClass(method.getOwner());

        if (!topEnclosingClass.getName().endsWith("Test")) {
            events.add(SimpleConditionEvent.violated(method, "Method %s must be declared in a class whose simple name ends with 'Test' (found: %s)"
                    .formatted(method.getName(), topEnclosingClass.getSimpleName())));
        }
    }

    private JavaClass getTopEnclosingClass(JavaClass item) {
        while (item.getEnclosingClass().isPresent()) {
            item = item.getEnclosingClass().orElseThrow();
        }
        return item;
    }
}
