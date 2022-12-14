package com.ojd.tsl.core.RunnableInterceptor;

import com.ojd.tsl.core.RunnableInterceptor.strengthen.BaseStrengthen;
import com.ojd.tsl.exception.SuperScheduledException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RunnableBaseInterceptor implements MethodInterceptor {
    protected final Log logger = LogFactory.getLog(getClass());
    /**
     * 定时任务执行器
     */
    private SuperScheduledRunnable runnable;
    /**
     * 定时任务增强类
     */
    private BaseStrengthen strengthen;

    public RunnableBaseInterceptor(Object object, SuperScheduledRunnable runnable) {
        this.runnable = runnable;
        if (BaseStrengthen.class.isAssignableFrom(object.getClass())) {
            this.strengthen = (BaseStrengthen) object;
        } else {
            throw new SuperScheduledException(object.getClass() + "对象不是BaseStrengthen类型");
        }
    }

    public RunnableBaseInterceptor() {

    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        List<String> methodName = Arrays.asList("invoke", "before", "after", "exception", "afterFinally");
        if (methodName.contains(method.getName())) {
            strengthen.before(obj, method, args, runnable.getContext());
            try {
                result = runnable.invoke();
            } catch (Exception e) {
                strengthen.exception(obj, method, args, runnable.getContext(), e);
                throw new SuperScheduledException(strengthen.getClass() + "中强化执行时发生错误", e);
            } finally {
                strengthen.afterFinally(obj, method, args, runnable.getContext());
            }
            strengthen.after(obj, method, args, runnable.getContext());
        } else {
            result = methodProxy.invokeSuper(obj, args);
        }
        return result;
    }
}
