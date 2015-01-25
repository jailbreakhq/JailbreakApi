package org.jailbreak.service.tests;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

import com.google.common.base.Optional;

public class OptionalAnswer extends ReturnsEmptyValues {

    @Override
    public Object answer(InvocationOnMock invocation) {
        Object answer = super.answer(invocation);
        if (answer != null) {
            return answer;
        }
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnType == Optional.class) {
            return Optional.absent();
        }
        return null;
    }
    
}