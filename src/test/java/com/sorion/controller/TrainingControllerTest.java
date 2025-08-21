package com.sorion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorion.domain.*;
import com.sorion.dto.TrainingSessionAnswerRequest;
import com.sorion.dto.TrainingSessionRequest;
import com.sorion.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)        // 시큐리티 있다면 필터 우회
@AutoConfigureTestDatabase(replace = ANY)        // 테스트 DB(H2 등) 사용
@Transactional
class TrainingControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired CategoryRepository categoryRepository;
    @Autowired TrainingRepository trainingRepository;
    @Autowired SoundRepository soundRepository;
    @Autowired TrainingStepRepository trainingStepRepository;
    @Autowired UserRepository userRepository;
    @Autowired TrainingSessionRepository trainingSessionRepository;
    @Autowired TrainingSessionAnswerRepository trainingSessionAnswerRepository;

    private Long trainingId;
    private Long userId;
    private Long step1Id;
    private Long step2Id;

    @BeforeEach
    void setUp() {
        // 기본 데이터 시드
        Category cat = categoryRepository.save(new Category("일상", "example.png"));
        Training training = trainingRepository.save(new Training(cat, "소리 적응 트레이닝"));
        trainingId = training.getId();

        Sound s1 = soundRepository.save(new Sound("헤어드라이기", "https://cdn.example.com/dryer.mp3", cat, 10));
        Sound s2 = soundRepository.save(new Sound("선풍기", "https://cdn.example.com/fan.mp3", cat, 8));

        List<Choice> choices = List.of(
                Choice.builder().id("a").label("자동차 경적").build(),
                Choice.builder().id("b").label("휘파람").build(),
                Choice.builder().id("c").label("폭죽").build(),
                Choice.builder().id("d").label("유리").build()
        );

        TrainingStep step1 = trainingStepRepository.save(new TrainingStep(training, s1, 1, choices, "a"));
        TrainingStep step2 = trainingStepRepository.save(new TrainingStep(training, s2, 2, choices, "b"));
        step1Id = step1.getId();
        step2Id = step2.getId();

        Users user = userRepository.save(new Users("tester"));
        userId = user.getId();
    }

    @Test
    void GET_trainings_trainingId_스텝목록_반환() throws Exception {
        mockMvc.perform(get("/trainings/{trainingId}", trainingId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].stepId").isNumber())
                .andExpect(jsonPath("$[0].stepOrder").isNumber())
                .andExpect(jsonPath("$[0].soundName").isString())
                .andExpect(jsonPath("$[0].durationSec").isNumber())
                // 변경된 부분
                .andExpect(jsonPath("$[0].soundUrl").isString())
                .andExpect(jsonPath("$[0].choices").isArray())
                .andExpect(jsonPath("$[0].correctChoiceId").isString());
    }

    @Test
    void POST_trainings_sessions_세션생성_201_및_DB반영() throws Exception {
        int before = (int) trainingSessionRepository.count();

        TrainingSessionRequest req = new TrainingSessionRequest(userId, trainingId);

        mockMvc.perform(post("/trainings/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").isNumber())
                .andExpect(jsonPath("$.trainingId").value(trainingId));

        int after = (int) trainingSessionRepository.count();
        assertThat(after).isEqualTo(before + 1);
    }

    @Test
    void POST_trainings_sessions_sessionId_answer_정답저장_204_및_DB반영() throws Exception {
        // 세션 먼저 생성 (서비스/컨트롤러를 타지 않고 레포로 바로 만들어도 되지만,
        // 여기선 엔드포인트도 한번 검증해 보자)
        var createReq = new TrainingSessionRequest(userId, trainingId);
        var createRes = mockMvc.perform(post("/trainings/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();
        var jsonNode = objectMapper.readTree(createRes.getResponse().getContentAsByteArray());
        long sessionId = jsonNode.get("sessionId").asLong();

        int before = (int) trainingSessionAnswerRepository.count();

        // step1에 대한 사용자 응답 저장
        TrainingSessionAnswerRequest answerReq = new TrainingSessionAnswerRequest(step1Id, "a", true);

        mockMvc.perform(post("/trainings/sessions/{sessionId}/answer", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerReq)))
                .andExpect(status().isNoContent());

        int after = (int) trainingSessionAnswerRepository.count();
        assertThat(after).isEqualTo(before + 1);
    }
}