package com.mydisc.MyDisc.controller.integration;

import com.mydisc.MyDisc.MyDiscApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
@AutoConfigureMockMvc()
public class FolderControllerTests {

    private static MediaType MEDIA_TYPE_JSON;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUpJsonMediaType() {
        MEDIA_TYPE_JSON =
                new MediaType(
                        MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),
                        Charset.forName("utf8")
                );
    }

    @Test
    public void testListWithNoCreatedFolders() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/folders");

        this.mockMvc.perform(get("/api/folders"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetWithNoCreatedFolders() throws Exception {
        this.mockMvc.perform(get("/api/folders"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "Lorem", 0 },
                { 123, 1 },
                { "b40f2063-2921-4599-abdd-6469199bc548", 2 }
        });
    }

    @Parameterized.Parameter
    public int input;

    @Parameterized.Parameter(1)
    public int expected;

    @Test
    public void testGetValidation() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}", input))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
