package uk.satyampi.BlogMs.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import uk.satyampi.BlogMs.exception.SatyamPiLogicalException;

import java.util.Arrays;

@Aspect
@Component
public class LogginAspect {
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     *
     * User Service impl
     */

    // Pointcut for all methods in BlogServiceImpl
    @Pointcut("execution(* uk.satyampi.BlogMs.service.impl.BlogServiceImpl.*(..))")
    public void blogServiceMethods() {
    }

    // Log method entry
    @Before("blogServiceMethods()")
    public void blogServiceMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method invoked: {}", methodName);
        logger.info("Arguments: {}", Arrays.toString(args));
    }

    // Handle exceptions thrown by BlogServiceImpl methods
    @AfterThrowing(pointcut = "blogServiceMethods()", throwing = "exception")
    public void handleBlogServiceException(Exception exception) throws SatyamPiLogicalException {
        logger.error("Exception occurred in blogServiceImpl: {}", exception.getMessage(),exception);
        if(exception instanceof DataIntegrityViolationException)
            throw new SatyamPiLogicalException("DataIntegrityViolationException occurred", exception);

        throw new SatyamPiLogicalException(exception.getMessage(), exception);
    }
}
