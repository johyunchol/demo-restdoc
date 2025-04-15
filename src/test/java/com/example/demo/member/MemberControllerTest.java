package com.example.demo.member;

import static capital.scalable.restdocs.AutoDocumentation.requestFields;
import static capital.scalable.restdocs.jackson.JacksonResultHandlers.prepareJackson;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.SnippetRegistry;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import capital.scalable.restdocs.section.SectionSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.demo.DemoApplication;
import com.example.demo.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureRestDocs
@Order(Integer.MAX_VALUE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class MemberControllerTest {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;
    private RestDocumentationResultHandler document;

    protected ObjectMapper objectMapper = ObjectMapperFactory.newDefault();

    protected static final String SCHEME = "https";
    protected static final String HOST = "api-admin-stage.unban.ai/cjconnect/admin/api";

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        SectionSnippet section = AutoDocumentation.sectionBuilder()
            .snippetNames(
                SnippetRegistry.HTTP_REQUEST,
                SnippetRegistry.AUTO_REQUEST_PARAMETERS,
                SnippetRegistry.AUTO_REQUEST_FIELDS,
                SnippetRegistry.AUTO_RESPONSE_FIELDS,
                SnippetRegistry.HTTP_RESPONSE,
                SnippetRegistry.CURL_REQUEST
            )
            .build();

        document = document(
            "{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            Preprocessors.preprocessResponse(
                ResponseModifyingPreprocessors.replaceBinaryContent(),
                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                Preprocessors.prettyPrint())
        );

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context);
        builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
//        builder.addFilters(new SecureFilter(
//                tokenDomainService,
//                adminUserDomainService,
//                externalLogger,
//                BuildPhase.TEST,
//                redisRepository));

        mockMvc = builder
            .alwaysDo(prepareJackson(objectMapper))
            .alwaysDo(document)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                .uris()
                .withScheme(SCHEME)
                .withHost(HOST)
                .withPort(443)
                .and()
                .snippets()
                .withDefaults(
                    CliDocumentation.curlRequest(),
                    HttpDocumentation.httpRequest(),
                    HttpDocumentation.httpResponse(),
                    requestFields(),
                    AutoDocumentation.responseFields(),
                    AutoDocumentation.pathParameters(),
                    AutoDocumentation.requestParameters(),
                    AutoDocumentation.description(),
                    AutoDocumentation.methodAndPath(),
                    section
                )
            ).build();
    }

    @Test
    @DisplayName("회원 테스트")
    public void findMemberByWordTest() throws Exception {
        var request = new MemberRequestDto(1L);

        var result = mockMvc.perform(post("/api/members/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("get-users",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("User API")
                        .summary("Get all users")
                        .responseSchema(Schema.schema("MemberRequestDto")) // 커스텀 스키마 참조
                        .deprecated(true)
                        .build()
                )
            ))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
//        log.info("## result : {}", responseBody);
        System.out.printf("result : {%s}%n", responseBody);
    }

}
