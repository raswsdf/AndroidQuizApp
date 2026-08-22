package com.example.quiz_app_submit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QuizDataMaskingTest {

    @Test
    public void masksEveryExposedKoreanAndEnglishAlias() {
        String question = "베가는 기초자산의 변동성을 나타낸다. Vega는 민감도를 계량한다.";

        String masked = QuizData.maskAnswerInQuestion(question, "베가(Vega)");

        assertEquals(
                "________는 기초자산의 변동성을 나타낸다. ________는 민감도를 계량한다.",
                masked
        );
    }

    @Test
    public void masksAliasesButKeepsTheRestOfTheQuestion() {
        String question = "자유무역협정은 국가 간 장벽을 낮춘다. FTA라고도 부른다.";

        String masked = QuizData.maskAnswerInQuestion(
                question,
                "자유무역협정(Free Trade Agreement, FTA)"
        );

        assertEquals(
                "________은 국가 간 장벽을 낮춘다. ________라고도 부른다.",
                masked
        );
    }

    @Test
    public void leavesQuestionUnchangedWhenAnswerIsNotExposed() {
        String question = "기초자산의 변동성 변화에 대한 옵션 가격의 민감도를 뜻한다.";

        assertEquals(
                question,
                QuizData.maskAnswerInQuestion(question, "베가(Vega)")
        );
    }

    @Test
    public void masksAnswerEvenWhenOnlyTheQuestionContainsWhitespace() {
        String question = "엄브렐러 펀드는 하위 펀드 사이의 전환이 자유로운 상품이다.";

        assertEquals(
                "________는 하위 펀드 사이의 전환이 자유로운 상품이다.",
                QuizData.maskAnswerInQuestion(question, "엄브렐러펀드")
        );
    }

    @Test
    public void masksImmediateParentheticalExplanationWithTheAnswer() {
        String question = "COFIX(Cost of Fund Index)는 은행의 자금조달비용을 반영한다.";

        assertEquals(
                "________는 은행의 자금조달비용을 반영한다.",
                QuizData.maskAnswerInQuestion(question, "COFIX(자금조달비용지수)")
        );
    }
}
