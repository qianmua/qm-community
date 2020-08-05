package pres.hjc.community.junit;

import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  21:10
 * @description :
 */
//@Component
//@Aspect
public class AlphaAspect {

    /**
     * () 所有 参数
     *
     */
//    @Pointcut("execution(* pres.hjc.community.service.*.*(..))")
    public void pointCut(){}

//    @Before("pointCut()")
    public void before(){
//        System.out.println("1");
    }

//    @After("pointCut()")
    public void after(){
//        System.out.println("2");
    }

//    @AfterReturning
//    @AfterThrowing



//    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
//        System.out.println("3");
        val proceed = point.proceed();
//        System.out.println("3");

        return proceed;
    }


}
