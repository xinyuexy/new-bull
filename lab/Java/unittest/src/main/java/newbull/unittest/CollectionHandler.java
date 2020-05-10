/**
 * @(#)CollectionHandler.java, Jul 07, 2018.
 * <p>
 * Copyright 2018 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package newbull.unittest;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO 实现一个动态代理，当target是一个Collection，且执行add()操作时，打印参数和操作前后的size()
 * @author fankai
 */
public class CollectionHandler implements InvocationHandler {

    private Object target;

    public CollectionHandler(Object object) {
        target = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (!((target instanceof Collection) && method.getName().equals("add"))) {
            System.out.println(proxy.getClass());   //proxy为生成的代理类对象
            return method.invoke(target, args);
        }

        Collection c = (Collection) target;
        System.out.println("before add size: " + c.size());
        for (Object arg: args) {
            System.out.println("parameters: ");
            System.out.print(arg + " ");
        }
        System.out.println("\ninvoke " + method.getName());
        Object res = method.invoke(target, args);
        System.out.println("after add size: " + c.size());
        return res;
    }

    public static void main(String[] args) {
        Collection proxy = (Collection) Proxy.newProxyInstance(
                Collection.class.getClassLoader(),
                new Class[]{Collection.class},
                new CollectionHandler(new ArrayList<Integer>() {
                })
        );
        System.out.println(proxy.getClass());
        proxy.add(1);
        proxy.add(2);
        System.out.println(proxy.size());
    }
}