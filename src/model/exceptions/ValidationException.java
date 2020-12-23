package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	//o Map vai guardar os erros de cada campo do formuario
	//o primeiro String indica o nome do campo, e o segundo
	//indica a mensagem de erro
	private Map<String,String> errors = new HashMap<>(); //coleção de todos os erros possiveis para um formulario
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String,String> getErrors()
	{
		return errors;
	}
	
	public void addError(String fieldName, String errorMessage)
	{
		errors.put(fieldName,errorMessage);
	}
}
