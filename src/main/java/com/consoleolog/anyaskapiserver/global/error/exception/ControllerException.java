package com.consoleolog.anyaskapiserver.global.error.exception;

/**
 * @FileName		: ControllerException.java
 * @Author			: ACR
 * @Date			: 24. 12. 18.
 * @Description		: Controller 에서의 Exception
 **/
public class ControllerException extends BaseException{
    public ControllerException(String message) {
        super(message);
    }
}
