package com.dental.lab.model.enums;

public enum EProductItemStatus {
	
	RECEIVED ("Recibido"),

	ACCEPTED ("En Proceso"),
	
	READY ("Terminado"),
	
	DELIVERED ("Entregado"),
	
	;
	
	private final String nameInSpanish;
	
	private EProductItemStatus(String nameInSpanish) {
		this.nameInSpanish = nameInSpanish;
	}
	
	public String getNameInSpanish() {
		return this.nameInSpanish;
	}
}
