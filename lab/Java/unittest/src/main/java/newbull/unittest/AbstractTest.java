/**
 * @(#)AbstractTest.java, Jul 07, 2018.
 * <p>
 * Copyright 2018 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package newbull.unittest;

import newbull.unittest.annotations.After;
import newbull.unittest.annotations.Before;
import newbull.unittest.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fankai
 */
public class AbstractTest {

    protected void run() {
        // TODO 通过反射运行测试用例
        Class cl = this.getClass();
        System.out.println("run class: " + cl.getName());

        int tests = 0;  //记录有多少Test方法
        int passed = 0; //记录成功的

        //记录各个注解的方法
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        //搜索各注解的方法
        for (Method m: cl.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class))
                testMethods.add(m);
            if (m.isAnnotationPresent(Before.class))
                beforeMethods.add(m);
            if (m.isAnnotationPresent(After.class))
                afterMethods.add(m);
        }

        try {
            Object obj = cl.newInstance();

            //执行Before
            for (int i=0; i<beforeMethods.size(); i++) {
                Method m = beforeMethods.get(i);
                m.invoke(obj);
            }

            //执行Test方法
            for (int i=0; i<testMethods.size(); i++) {
                Method m = testMethods.get(i);
                m.invoke(obj);
                passed++;
            }

            //执行After
            for (int i=0; i<afterMethods.size(); i++) {
                Method m = afterMethods.get(i);
                m.invoke(obj);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        tests = testMethods.size();
        System.out.printf("passed: %d, failed: %d\n", passed, (tests-passed));
    }
}