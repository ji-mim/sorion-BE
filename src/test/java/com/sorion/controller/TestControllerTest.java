package com.sorion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorion.domain.Category;
import com.sorion.domain.Sound;
import com.sorion.domain.TestResult;
import com.sorion.domain.Users;
import com.sorion.dto.SoundResultRequest;
import com.sorion.repository.CategoryRepository;
import com.sorion.repository.SoundRepository;
import com.sorion.repository.TestResultRepository;
import com.sorion.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   // 시큐리티 필터가 있다면 우회
@AutoConfigureTestDatabase(replace = ANY)   // 테스트용 내장 DB(H2 등) 사용 권장
@Transactional
class TestControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired CategoryRepository categoryRepository;
    @Autowired SoundRepository soundRepository;
    @Autowired UserRepository userRepository;
    @Autowired TestResultRepository testResultRepository;

    private Long categoryId;
    private Long userId;
    private Long soundId1;
    private Long soundId2;

    @BeforeEach
    void setUp() {
        // 카테고리
        Category category = categoryRepository.save(new Category("일상", "example.png"));
        categoryId = category.getId();

        // 유저
        Users user = userRepository.save(new Users("tester"));
        userId = user.getId();

        // 사운드 2건
        Sound s1 = soundRepository.save(new Sound("헤어드라이기", "https://cdn.example.com/dryer.mp3", category, 10));
        Sound s2 = soundRepository.save(new Sound("선풍기", "https://cdn.example.com/fan.mp3", category, 8));
        soundId1 = s1.getId();
        soundId2 = s2.getId();
    }

    @Test
    void GET_tests_categoryId_사운드목록_응답() throws Exception {
        mockMvc.perform(get("/tests/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // SoundResponse 구조에 맞춰 검증하세요.
                // 서비스에서 new SoundResponse(categoryId, soundList.size(), soundList) 이므로
                .andExpect(jsonPath("$.categoryId").value(categoryId))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.soundList").isArray())
                .andExpect(jsonPath("$.soundList.length()").value(2));
    }

    @Test
    void POST_tests_result_저장_후_201과_DB반영() throws Exception {
        int before = (int) testResultRepository.count();

        SoundResultRequest req = new SoundResultRequest(userId, soundId1, 55);

        mockMvc.perform(post("/tests/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        int after = (int) testResultRepository.count();
        assertThat(after).isEqualTo(before + 1);

        // 마지막 저장 데이터 검증
        TestResult saved = testResultRepository.findAll().get(after - 1);
        assertThat(saved.getUsers().getId()).isEqualTo(userId);
        assertThat(saved.getSound().getId()).isEqualTo(soundId1);
        assertThat(saved.getSensitiveRating()).isEqualTo(55);
        assertThat(saved.getStartTime()).isNotNull();
    }
}