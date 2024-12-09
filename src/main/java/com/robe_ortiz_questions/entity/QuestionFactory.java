package com.robe_ortiz_questions.entity;

import java.util.List;

public class QuestionFactory {

    @SuppressWarnings("unchecked")
	public static Question createQuestion(Long id, TypeOfQuestion typeOfQuestion, CategoryOfQuestion category, String question,  Object... extraParams) throws IllegalArgumentException {
        switch (typeOfQuestion) {
            case TRUE_OR_FALSE:
                if (extraParams.length != 1 || !(extraParams[0] instanceof Boolean)) {
                    throw new IllegalArgumentException("Debe proporcionar una respuesta booleana (true / false) para una pregunta de tipo TrueOrFalse.");
                }
                return new TrueOrFalseQuestion(id, question, category, (Boolean) extraParams[0]);

            case SINGLE_CHOICE:
                if (extraParams.length != 2 || !(extraParams[0] instanceof List<?>) || !(extraParams[1] instanceof String)) {
                    throw new IllegalArgumentException("Debe proporcionar una lista de respuestas y una respuesta correcta para una pregunta de tipo SingleChoice.");
                }
                return new SimpleChoiceQuestion(id, question, category, (List<String>) extraParams[0], (String) extraParams[1]);
            default:
                throw new IllegalArgumentException("Tipo de pregunta no soportado.");
        }
    }
}

