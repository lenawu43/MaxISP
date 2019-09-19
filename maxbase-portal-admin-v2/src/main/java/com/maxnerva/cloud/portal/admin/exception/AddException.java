package com.maxnerva.cloud.portal.admin.exception;

import com.maxnerva.cloud.common.exception.BaseException;

public class AddException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1770350810334423917L;
	
	public AddException(String format, int status, Object... arguments) {
        super(format, status, arguments);
    }

}
