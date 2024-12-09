package com.robe_ortiz_questions.entity;

public enum TypeOfQuestion {
    TRUE_OR_FALSE("Verdadero o falso"),
    SINGLE_CHOICE("Selección simple"),
	MULTIPLE_CHOICE("Selección múltiple");
	
	private String translate;

	private TypeOfQuestion(String translate) {
		this.translate = translate;
	}

	public String getTranslate() {
		return translate;
	}
	
}
