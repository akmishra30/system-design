package com.mahkir.designs.vm.exception;

/**
 * This exception will be thrown when the paid amount is less compare to selected product.
 * 
 * */
public class LessPaidAmountException extends RuntimeException{
	private String message;
	private long remaining;
	
	public LessPaidAmountException(String message, long remaining){
		this.message = message;
		this.remaining = remaining;
	}
	
	public long getRemaining(){
		return remaining;
	}
	
	@Override
	public String getMessage(){
		return message + remaining;
	}
}
