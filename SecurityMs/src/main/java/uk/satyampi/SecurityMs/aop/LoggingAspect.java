package uk.satyampi.SecurityMs.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     *
     * User Service impl
     */

    // Pointcut for all methods in UserServiceImpl
    @Pointcut("execution(* uk.satyampi.SecurityMs.service.impl.UserServiceImpl.*(..))")
    public void userServiceMethods() {
    }

    // Log method entry
    @Before("userServiceMethods()")
    public void userServiceMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method invoked: {}", methodName);
        logger.info("Arguments: {}", Arrays.toString(args));
    }

    // Handle exceptions thrown by UserServiceImpl methods
    @AfterThrowing(pointcut = "userServiceMethods()", throwing = "exception")
    public void handleUserServiceException(Exception exception) throws SatyamPiLogicalException {
        logger.error("Exception occurred in UserServiceImpl: {}", exception.getMessage(), exception);
        throw new SatyamPiLogicalException(exception.getMessage(), exception);
    }


    /**
     *
     * Jwt Service Impl
     */
    // Pointcut for all methods in JwtServiceImpl
    @Pointcut("execution(* uk.satyampi.SecurityMs.service.impl.JwtServiceImpl.*(..))")
    public void jwtServiceMethods() {
    }

    // Log method entry
    @Before("jwtServiceMethods()")
    public void jwtServiceMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method invoked: {}", methodName);
        logger.info("Arguments: {}", Arrays.toString(args));
    }

    // Handle exceptions thrown by JwtServiceImpl methods
    @AfterThrowing(pointcut = "jwtServiceMethods()", throwing = "exception")
    public void handleJwtServiceException(Exception exception) throws SatyamPiLogicalException {
        logger.error("Exception occurred in JwtServiceImpl: {}", exception.getMessage(), exception);
        throw new SatyamPiLogicalException(exception.getMessage(),exception);
    }

}
