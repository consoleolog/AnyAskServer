package com.consoleolog.anyaskapiserver.global.error.exception;

/**
 * @FileName		: BusinessLogicException.java
 * @Author			: ACR
 * @Date			: 24. 12. 18.
 * @Description		: Service 레이어에서의 Exception
 **/
public class BusinessLogicException extends BaseException{
    public BusinessLogicException(String message) {
        super(message);
    }
}
