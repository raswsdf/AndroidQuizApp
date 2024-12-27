package com.example.quiz_app_submit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizData {
    private String category;
    private List<Question> questions;

    public QuizData(String category) {
        this.category = category;
        questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        // 각 카테고리별 문제를 하드코딩
        if (category.equals("finance")) {
            questions.add(createQuestion("처음 발행된 증권, 채권 등이 거래되는 발행시장과 구분되며, 이미 발행된 증권을 거래하는 시장을 의미합니다.", "2차 시장"));
            questions.add(createQuestion("은행의 자동화된 장비로, 고객이 직접 현금 입출금, 계좌 조회 등을 할 수 있게 합니다.", "ATM"));
            questions.add(createQuestion("개인이 대출금을 갚을 수 없는 상황에서 채권자가 채무자의 담보 자산을 매각하여 대출금을 회수하는 절차입니다.", "압류"));
            questions.add(createQuestion("기업이 자금을 조달하기 위해 발행하는 채무 증서입니다.", "회사채"));
            questions.add(createQuestion("주식이 시장에 상장되어 일반 투자자에게 공개적으로 거래되기 이전에 내부 관계자들 사이에서 미리 거래되는 주식입니다.", "프리 IPO"));
            questions.add(createQuestion("각 국가 간에 서로의 환율을 상호 조정하기 위해 체결하는 협정입니다.", "통화스왑"));
            questions.add(createQuestion("신용이 부족한 사람들이 이용할 수 있는 대출 서비스로, 보통 높은 이자율이 부과됩니다.", "서브프라임"));
            questions.add(createQuestion("고객의 신용도를 평가하여 대출 한도와 금리를 결정하는 기준입니다.", "신용평가"));
            questions.add(createQuestion("기업이 소유한 자산을 매각하고 다시 임차하여 사용하는 금융 거래 방식입니다.", "리스 백"));
            questions.add(createQuestion("주식을 소유한 사람들에게 기업 이익의 일부를 배당 형태로 지급하는 방식입니다.", "배당"));
            questions.add(createQuestion("주식, 채권 등을 여러 상품으로 나누어 투자하여 리스크를 줄이는 투자 전략입니다.", "분산 투자"));
            questions.add(createQuestion("투자자가 손해를 입지 않도록 하기 위해 주가의 하락 가능성에 대비한 매도 주문입니다.", "헤지"));
            questions.add(createQuestion("회사의 자산과 부채를 합산하여 계산한 재무 지표입니다.", "총자산"));
            questions.add(createQuestion("원유, 금, 은 등과 같은 원자재 상품을 의미하며, 경제 상황에 따라 가격 변동성이 큽니다.", "원자재"));
            questions.add(createQuestion("어느 시점에 금융기관이 보유한 모든 채무를 상환할 수 있는 능력을 의미합니다.", "유동성"));
            questions.add(createQuestion("투자 수익률과 위험의 관계를 측정하기 위한 지표입니다.", "베타 계수"));
            questions.add(createQuestion("금융기관이 발행한 채권을 다른 투자자에게 매도하여 유동성을 확보하는 방식입니다.", "채권 매각"));
            questions.add(createQuestion("외국인이 국내 자산에 투자할 때 발생하는 금융 거래입니다.", "외국인 투자"));
            questions.add(createQuestion("기준 금리에 따라 각 금융 기관이 조정하는 금리입니다.", "변동 금리"));
            questions.add(createQuestion("주가의 상승으로 인한 기대 수익을 계산하는 방식입니다.", "자본 이익"));
        } else if (category.equals("economy")) {
            questions.add(createQuestion("국민소득을 총국민 수로 나눈 값. 해당 국가의 소득 수준을 보여주는 가장 대표적인 지표입니다.", "1인당 국민소득"));
            questions.add(createQuestion("상품 및 서비스의 교역에 대한 관세 및 무역장벽을 철폐하는 국가 간 협정으로, 체결국 간 자유로운 무역을 허용합니다.", "FTA"));
            questions.add(createQuestion("수입품에 대해 자국에서 추가로 부과하는 세금으로, 자국 산업을 보호하고자 하는 목적이 있습니다.", "관세"));
            questions.add(createQuestion("경제 주체들이 최적의 자원을 배분하기 위해 합리적으로 선택하고 행동하는 원리입니다.", "합리적 선택"));
            questions.add(createQuestion("경제 전반에서 물가가 지속적으로 상승하는 현상을 의미하며, 구매력이 감소하게 됩니다.", "인플레이션"));
            questions.add(createQuestion("재화와 서비스를 생산하고 분배하고 소비하는 과정에서 일어나는 모든 경제적 활동을 의미합니다.", "경제 활동"));
            questions.add(createQuestion("국가 경제에서 일정한 기간 동안 생산된 최종 생산물의 시장 가치의 합을 의미합니다.", "국내총생산"));
            questions.add(createQuestion("정부의 공공 프로젝트나 서비스 제공을 위해 지출하는 비용을 의미합니다.", "재정 지출"));
            questions.add(createQuestion("특정 기간 동안 경제 주체들이 소비, 투자 등을 통해 지출한 금액을 합산하여 계산한 값입니다.", "국민 지출"));
            questions.add(createQuestion("정부가 정책적으로 물가나 경제 성장률을 목표로 하여 금리를 조정하는 정책입니다.", "통화 정책"));
            questions.add(createQuestion("수요와 공급이 만나는 균형 가격에 따라 자원 분배가 이루어지는 시장 체제입니다.", "자본주의"));
            questions.add(createQuestion("정부가 자원을 계획적으로 배분하여 경제 활동을 조정하는 체제입니다.", "계획 경제"));
            questions.add(createQuestion("세계 경제에서 모든 국가들이 연결되어 경제적 상호작용을 하게 되는 현상입니다.", "글로벌화"));
            questions.add(createQuestion("주식, 채권 등 금융 자산의 가격 변동에 따른 이익을 추구하는 투자를 의미합니다.", "금융 투자"));
            questions.add(createQuestion("경제에서 자원이 무한하지 않고 제한되어 있어 선택과 희생을 요구하는 특성을 의미합니다.", "희소성"));
            questions.add(createQuestion("생산 요소인 노동, 자본, 토지 등의 소유권을 개인이 가지는 경제 체제입니다.", "시장 경제"));
            questions.add(createQuestion("정부가 경기 변동에 따라 세금과 지출을 조정하여 경제를 안정화하는 정책입니다.", "재정 정책"));
            questions.add(createQuestion("상품과 서비스의 가격이 장기간에 걸쳐 하락하는 경제 현상입니다.", "디플레이션"));
            questions.add(createQuestion("한 국가의 경제가 일정 기간 동안 지속적으로 성장하지 못하고 정체되는 현상입니다.", "경기 침체"));
            questions.add(createQuestion("한 국가의 경제가 높은 성장률을 기록하는 가운데, 물가가 오르지 않고 안정적인 상태를 유지하는 현상입니다.", "성장 안정성"));
        } else if (category.equals("public")) {
            questions.add(createQuestion("정부가 직접 소유하거나 관리하는 기관으로, 공공의 이익을 위해 존재하며 자원을 효율적으로 활용합니다.", "공공기관"));
            questions.add(createQuestion("지방정부가 자체적으로 징수하는 세금으로, 지역사회의 공공서비스 재원을 조달합니다.", "지방세"));
            questions.add(createQuestion("공공의 목적을 위해 특정 지역의 주민들이 함께 모여 형성된 단체입니다.", "공동체"));
            questions.add(createQuestion("정부가 제공하는 교육, 의료, 복지 등의 사회 서비스입니다.", "공공 서비스"));
            questions.add(createQuestion("모든 국민에게 균등하게 제공되어야 하는 자원이나 서비스를 의미합니다.", "공공재"));
            questions.add(createQuestion("정부가 직접적으로 국민의 건강을 보호하고 의료 서비스를 제공하는 것을 목표로 하는 정책입니다.", "보건 정책"));
            questions.add(createQuestion("지방자치단체가 주민을 위해 시행하는 지역 사회 관련 행정 활동을 의미합니다.", "지역 행정"));
            questions.add(createQuestion("국가가 국민에게 안정적인 생활을 보장하기 위해 제정한 복지 제도입니다.", "사회 보장"));
            questions.add(createQuestion("공공의 안전과 질서를 유지하기 위해 경찰과 소방 등의 공공 조직이 수행하는 업무입니다.", "공공안전"));
            questions.add(createQuestion("범죄로부터 사회를 보호하고, 정의를 실현하기 위해 법을 집행하는 제도를 의미합니다.", "사법 제도"));
            questions.add(createQuestion("국민들이 정치에 참여하여 권리를 행사하는 제도로, 선거, 참여가 포함됩니다.", "민주주의"));
            questions.add(createQuestion("국민의 투표에 의해 대통령이나 국회의원을 선출하는 정치 제도입니다.", "선거 제도"));
            questions.add(createQuestion("국민이 스스로의 의견을 표현할 수 있는 권리를 보장하는 헌법적 권리입니다.", "표현의 자유"));
            questions.add(createQuestion("사회적 약자를 보호하고, 사회 정의를 실현하기 위한 정부의 법적 지원을 의미합니다.", "사회 정의"));
            questions.add(createQuestion("국가의 법질서와 규범을 통해 구성원들이 협력하여 공동체를 유지하는 사회적 기구입니다.", "법과 질서"));
            questions.add(createQuestion("국민이 다양한 정책과 문제에 대해 스스로 논의하고, 해결책을 제시하는 정치 활동입니다.", "시민 참여"));
            questions.add(createQuestion("국민에게 일정 기준 이상의 생활을 보장하기 위해 시행하는 복지정책을 의미합니다.", "최저 생활 보장"));
            questions.add(createQuestion("전쟁, 테러, 재해 등으로부터 국민의 생명과 재산을 보호하기 위한 국가 차원의 대비 활동입니다.", "안보 정책"));
            questions.add(createQuestion("대중이 신뢰할 수 있도록 정보가 투명하게 공개되며, 국민이 정책을 감시하는 제도를 의미합니다.", "투명성"));
            questions.add(createQuestion("정부의 주요 결정 사항을 국민이 알 수 있도록 정보 공개를 의무화하는 제도입니다.", "정보 공개"));
        }
    }

    // 각 문제에 대한 선택지를 생성하는 메소드
    private Question createQuestion(String questionText, String answer) {
        List<String> options = new ArrayList<>();
        options.add(answer);
        options.add("옵션 1");
        options.add("옵션 2");
        options.add("옵션 3");

        Collections.shuffle(options);

        return new Question(questionText, answer, options);
    }

    public List<Question> getAllQuestions() {
        return questions;
    }

    public List<String> getRandomWrongAnswer(int currentQuestionIndex) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getAnswer();

        List<String> allAnswers = new ArrayList<>();
        for (Question question : questions) {
            allAnswers.add(question.getAnswer());
        }

        allAnswers.remove(correctAnswer);

        List<String> wrongAnswers = new ArrayList<>();
        Random rand = new Random();
        while (wrongAnswers.size() < 3) {
            String wrongAnswer = allAnswers.get(rand.nextInt(allAnswers.size()));
            if (!wrongAnswers.contains(wrongAnswer)) {
                wrongAnswers.add(wrongAnswer);
            }
        }

        wrongAnswers.add(correctAnswer);
        Collections.shuffle(wrongAnswers);

        return wrongAnswers;
    }

}
