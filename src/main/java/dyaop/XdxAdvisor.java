package dyaop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class XdxAdvisor extends AbstractPointcutAdvisor {
    private final Advice advice;
    private final Pointcut pointcut;

    // 基于注解的 pointcut
    public XdxAdvisor(Class<? extends Annotation>  annotationClass, MethodInterceptor interceptor) {

        this.advice = interceptor;
        this.pointcut = buildPointcut(annotationClass);
    }
    
    // 基于表达式的 pointcut
    public XdxAdvisor(String expression, MethodInterceptor interceptor) {

        this.advice = interceptor;
        this.pointcut = buildPointcut(expression);
    }


    /**
     * 直接复制的 @Async 构建 pointcut的代码
     * @param annotationType
     * @return
     */
    private Pointcut buildPointcut(Class<? extends Annotation> annotationType) {

        Set<Class<? extends Annotation>> annotationTypes = new LinkedHashSet(2);
        annotationTypes.add(annotationType);
        ComposablePointcut result = null;
        AnnotationMatchingPointcut mpc;
        for(Iterator var3 = annotationTypes.iterator(); var3.hasNext(); result = result.union(mpc)) {
            Class<? extends Annotation> asyncAnnotationType = (Class)var3.next();
            Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
            mpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
            if (result == null) {
                result = new ComposablePointcut(cpc);
            } else {
                result.union(cpc);
            }
        }

        return (Pointcut) (result != null ? result : Pointcut.TRUE);
    }

    private Pointcut buildPointcut(String expression) {
        AspectJExpressionPointcut tmpPointcut = new AspectJExpressionPointcut();
        tmpPointcut.setExpression(expression);
        return  tmpPointcut;
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
    @Override
    public Advice getAdvice() {
        return advice;
    }

//    private Pointcut buildPointcut(Class<? extends Annotation> annotationTypes) {
//
//        AnnotationClassFilter classFilter = new AnnotationClassFilter(annotationTypes, true);
//        return new ComposablePointcut(classFilter);
//    }

}
