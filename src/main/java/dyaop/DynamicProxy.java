//package dyaop;
//
//import org.springframework.aop.Advisor;
//import org.springframework.aop.framework.Advised;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.aop.framework.ProxyProcessorSupport;
//import org.springframework.aop.support.AopUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.context.annotation.Configuration;
//
//
////@Configuration
////public class DynamicProxy extends ProxyProcessorSupport implements BeanFactoryAware {
////
////    private DefaultListableBeanFactory beanFactory;
////
////    public void operateAdvisor(XdxAdvisor advisor, OperateEventEnum operateEventEnum) {
////
////        // 循环每一个bean
////        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
////            Object bean = beanFactory.getBean(beanDefinitionName);
////
////            // 判断当前bean是否匹配
////            if (!isEligible(bean, advisor)) {
////                continue;
////            }
////
////            // 判断当前bean是不是已经是代理对象了，是就直接进行 Advisor 操作
////            if (bean instanceof Advised) {
////                Advised advised = (Advised) bean;
////                if(operateEventEnum == OperateEventEnum.DELETE) {
////                    advised.removeAdvisor(advisor);
////                }else if(operateEventEnum == OperateEventEnum.ADD){
////                    advised.addAdvisor(advisor);
////                }
////                continue;
////            }
////
////            // 生成 Advisor 的代理对象
////            ProxyFactory proxyFactory = new ProxyFactory();
////            proxyFactory.addAdvisor(advisor);
////            proxyFactory.setTarget(bean);
////            ClassLoader classLoader = this.getProxyClassLoader();
////            Object proxy = proxyFactory.getProxy(classLoader);
////
////            // 销毁之前的bean，把新的bean注入到容器
////            beanFactory.destroySingleton(beanDefinitionName);
////            beanFactory.registerSingleton(beanDefinitionName, proxy);
////        }
////    }
////
////    @Override
////    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
////        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
////    }
////
////    /**
////     * 复制的 @Async 的匹配逻辑
////     */
////    private boolean isEligible(Object bean, Advisor advisor) {
////        return AopUtils.canApply(advisor, bean.getClass());
////    }
////}
