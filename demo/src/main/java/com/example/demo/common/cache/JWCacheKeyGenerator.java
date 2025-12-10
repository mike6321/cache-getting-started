package com.example.demo.common.cache;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class JWCacheKeyGenerator {

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public String getKey(JoinPoint joinpoint, CacheStrategy cacheStrategy, String cacheName, String keyExpression) {
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        String[] parameterNames = ((MethodSignature) joinpoint.getSignature()).getParameterNames();
        Object[] args = joinpoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }

        return cacheStrategy + ":" + cacheName + ":" + expressionParser.parseExpression(keyExpression).getValue(evaluationContext, String.class);
    }

}
