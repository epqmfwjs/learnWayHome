package com.learnway.global.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="notice not found")
public class DataNotExeption extends Exception {
	public DataNotExeption(String msg) {
		super(msg);
	}

}
