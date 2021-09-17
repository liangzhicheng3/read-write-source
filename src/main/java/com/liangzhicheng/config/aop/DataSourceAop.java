package com.liangzhicheng.config.aop;

import com.liangzhicheng.config.bean.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description 设置路由key，默认情况下，所有的查询走从库，插入/修改/删除走主库，通过方法名来区分操作类型
 */
@Aspect
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(com.liangzhicheng.config.annotation.Master) " +
            "&& (execution(* com.liangzhicheng.modules.service..*.list*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.liangzhicheng.config.annotation.Master) " +
            "|| execution(* com.liangzhicheng.modules.service..*.insert*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.save*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.update*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.edit*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.delete*(..)) " +
            "|| execution(* com.liangzhicheng.modules.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

}
