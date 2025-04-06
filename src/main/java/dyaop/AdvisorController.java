//package dyaop;
//
//import org.aopalliance.intercept.MethodInterceptor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.lang.annotation.Annotation;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/advisor")
//public class AdvisorController {
//
//    @Resource
//    private DynamicProxy dynamicProxy;
//
//    private static Map<String, XdxAdvisor> xdxAdvisorMap = new HashMap<>();
//
//    @GetMapping(value = "/add")
//    public String add(String interceptorClass, String expression, String annotationClass) throws Exception {
//        if(StringUtils.isAllBlank(expression, annotationClass) || StringUtils.isBlank(interceptorClass)) {
//            return "the parameter is abnormal";
//        }
//        if (xdxAdvisorMap.containsKey(interceptorClass + annotationClass) || xdxAdvisorMap.containsKey(interceptorClass + expression)) {
//            return "advisor already exists";
//        }
//
//        MethodInterceptor methodInterceptor =  (MethodInterceptor) Class.forName(interceptorClass).getDeclaredConstructor().newInstance();
//        XdxAdvisor xdxAdvisor;
//        // 以注解为主，有注解就用注解
//        if (StringUtils.isNotBlank(annotationClass)) {
//            Class<? extends Annotation> aClass = (Class<? extends Annotation>) Class.forName(annotationClass);
//            xdxAdvisor = new XdxAdvisor(aClass, methodInterceptor);
//            xdxAdvisorMap.put(interceptorClass + annotationClass, xdxAdvisor);
//        } else {
//            xdxAdvisor = new XdxAdvisor(expression, methodInterceptor);
//            xdxAdvisorMap.put(interceptorClass + expression, xdxAdvisor);
//        }
//
//        dynamicProxy.operateAdvisor(xdxAdvisor, OperateEventEnum.ADD);
//
//        return "advisor add success" ;
//    }
//
//
//
//
//    @GetMapping(value = "/delete")
//    public String delete(String interceptorClass, String expression, String annotationClass) {
//        if(StringUtils.isAllBlank(expression, annotationClass) || StringUtils.isBlank(interceptorClass)) {
//            throw new IllegalArgumentException("参数异常");
//        }
//
//        if (!xdxAdvisorMap.containsKey(interceptorClass + annotationClass) && !xdxAdvisorMap.containsKey(interceptorClass + expression)) {
//            return "advisor not exists";
//        }
//
//        // 以注解为主，有注解就用注解
//        StringBuilder advisorKey = new StringBuilder(interceptorClass);
//        if (StringUtils.isNotBlank(annotationClass)) {
//            advisorKey.append(annotationClass);
//        } else {
//            advisorKey.append(expression);
//        }
//
//        XdxAdvisor xdxAdvisor = xdxAdvisorMap.get(advisorKey.toString());
//        dynamicProxy.operateAdvisor(xdxAdvisor, OperateEventEnum.DELETE);
//        xdxAdvisorMap.remove(advisorKey.toString());
//
//        return "advisor delete success";
//    }
//
//
//    /**
//     * 用来测试效果的fun
//     * @return
//     */
//    @GetMapping(value = "/fun")
//    @XdxAnnotation
//    public String fun() {
//        return "my fun";
//    }
//}
