package com.liuyanzhao.common.exception.file;

import com.liuyanzhao.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author saysky
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
