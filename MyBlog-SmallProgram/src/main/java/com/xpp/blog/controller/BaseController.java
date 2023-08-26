package com.xpp.blog.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xpp.blog.controller.ex.FileEmptyException;
import com.xpp.blog.controller.ex.FileIOException;
import com.xpp.blog.controller.ex.FileSizeException;
import com.xpp.blog.controller.ex.FileStateException;
import com.xpp.blog.controller.ex.FileTypeException;
import com.xpp.blog.controller.ex.FileUploadException;
import com.xpp.blog.util.JsonResult;

/*
 * 所有控制器类的父类
 */
public abstract class BaseController {

	// 成功状态码
	protected static final Integer SUCCESS = 20;
	// 插入异常
	protected static final Integer ERROR_INSERT = 40;
	// 修改异常
	protected static final Integer ERROR_UPDATE = 41;
	// 删除异常
	protected static final Integer ERROR_DELETE = 42;
	// 上传文件为空
	protected static final Integer ERROR_FILE_EMPTY = 50;
	// 上传文件大小超过限制
	protected static final Integer ERROR_FILE_SIZE = 51;
	// 文件类型不匹配
	protected static final Integer ERROR_FILE_TYPE = 52;
	// 文件上传中的非正常状态
	protected static final Integer ERROR_FILE_STATE = 53;
	// 文件上传过程中的写读异常
	protected static final Integer ERROR_FILE_IO = 54;

	/**
	 * 对控制器中的异常进行统一处理
	 * 
	 * @param e 异常对象
	 * @return JsonResult 封装响应信息
	 */
	@ExceptionHandler({ FileUploadException.class })
	@ResponseBody // 直接返回json数据
	public JsonResult<Void> handlerException(Throwable e) {

		// 根据不同异常的类型提供不同的处理方式
		// 现在的处理方式是根据不同的类型，返回不同的状态码
		JsonResult<Void> jr = new JsonResult<>(e.getMessage());

		if (e instanceof FileEmptyException) {
			jr.setState(ERROR_FILE_EMPTY);
		} else if (e instanceof FileSizeException) {
			jr.setState(ERROR_FILE_SIZE);
		} else if (e instanceof FileTypeException) {
			jr.setState(ERROR_FILE_TYPE);
		} else if (e instanceof FileStateException) {
			jr.setState(ERROR_FILE_STATE);
		} else if (e instanceof FileIOException) {
			jr.setState(ERROR_FILE_IO);
		}

		return jr;
	}

}