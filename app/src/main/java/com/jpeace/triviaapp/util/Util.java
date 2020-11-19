package com.jpeace.triviaapp.util;

import com.jpeace.triviaapp.R;

public class Util {
    public static final int NUMBER_ANS=4;
    public static final int[] ANS_IDs = {R.id.m_ans_1, R.id.m_ans_2, R.id.m_ans_3, R.id.m_ans_4};

    //number of query
    public static final int AMOUNT=50;

    //difficulty of question
    public static final String DIF_EASY="easy";
    public static final String DIF_MEDIUM="medium";
    public static final String DIF_HARD="hard";

    //category of question
    public static final int GEOGRAPHY=22;
    public static final int SCIENCE_NATURE=17;
    public static final int GENERAL_KNOWLEDGE=9;
    public static final int HISTORY=23;

    //type of question
    public static final String TYPE_MULTIPLE="multiple";
    public static final String TYPE_BOOLEAN="boolean";

    //for any type of difficulty and type
    public static final String ANY_TYPE="any_type";

    public static final String MY_KEY="my_key";

    //key json object:
    public static final String CATEGORY="category";
    public static final String TYPE="type";
    public static final String DIFFICULTY="difficulty";
    public static final String QUESTION="question";
    public static final String CORRECT_ANS="correct_answer";
    public static final String INCORRECT_ANS="incorrect_answers";
    public static final String RES="results";

    //my string debug:
    public static final String D="TADA: ";

}
