package org.konghao.controller;

/**
 * 系统异常
 * @author zslin.com 20160514
 *
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = -4555331337009026323L;

    private String code;

    public SystemException() {
        super();
    }

    public SystemException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SystemException(Throwable throwable) {
        super(throwable);
    }
}
