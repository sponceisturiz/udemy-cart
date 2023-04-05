package mx.com.course.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.com.course.TestConfiguration;
import mx.com.course.actions.CourseAction;
import mx.com.course.broker.message.course.CoursePriceReplyMessage;
import mx.com.course.model.CoursePriceVO;
import mx.com.course.model.request.CartRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {TestConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartControllerTest {

    @MockBean
    CourseAction courseAction;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetTotalPriceFromUserCart() throws Exception {
        long userId = 1001;
        List<CoursePriceVO> testCoursePriceVO = new ArrayList<>();

        testCoursePriceVO.add(new CoursePriceVO(1001, "test course 1", "129.99"));
        testCoursePriceVO.add(new CoursePriceVO(1002, "test course 2", "99.99"));
        testCoursePriceVO.add(new CoursePriceVO(1003, "test course 3", "399.99"));

        CoursePriceReplyMessage coursePriceReplyMessage = new CoursePriceReplyMessage(0, "CART", testCoursePriceVO);

        given(courseAction.publishAndReply(anyLong(), anyList())).willReturn(coursePriceReplyMessage);

        mockMvc.perform(get("/cart/"+userId+"/total"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetUserCart() throws Exception{
        long userId = 1001;

        List<CoursePriceVO> testCoursePriceVO = new ArrayList<>();

        testCoursePriceVO.add(new CoursePriceVO(1001, "test course 1", "129.99"));
        testCoursePriceVO.add(new CoursePriceVO(1002, "test course 2", "99.99"));
        testCoursePriceVO.add(new CoursePriceVO(1003, "test course 3", "399.99"));

        CoursePriceReplyMessage coursePriceReplyMessage = new CoursePriceReplyMessage(0, "CART", testCoursePriceVO);

        given(courseAction.publishAndReply(anyLong(), anyList())).willReturn(coursePriceReplyMessage);

        mockMvc.perform(get("/cart/"+userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testUpdateAddCart() throws Exception{
        long userId = 1002;

        CartRequest testRequest =  new CartRequest(1004);

        mockMvc.perform(put("/cart/"+userId).param("oper", "0").content(asJsonString(testRequest)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isAccepted());
    }

    @Test
    void testUpdateRemoveCart() throws Exception{
        long userId = 1002;

        CartRequest testRequest =  new CartRequest(1003);

        mockMvc.perform(put("/cart/"+userId).param("oper", "1").content(asJsonString(testRequest)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isAccepted());
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } 
}
