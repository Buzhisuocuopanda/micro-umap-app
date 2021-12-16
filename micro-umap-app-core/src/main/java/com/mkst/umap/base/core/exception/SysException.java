package com.mkst.umap.base.core.exception;

/**
 * <pre>
 *	业务错误处理
 *  Created by gin.lee on 2019-04-17.
 * </pre>
 */
public class SysException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    protected final String message;
    private int code = 500;

    public SysException(String message)
    {
        this.message = message;
    }

    public SysException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }
    
    public SysException(String message, int code) {
		super(message);
		this.message = message;
		this.code = code;
	}

    @Override
    public String getMessage()
    {
        return message;
    }
    
    public int getCode() {
		return code;
	}
}
