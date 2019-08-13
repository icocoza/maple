package com.ccz.apps.maple.api.common.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ccz.apps.maple.api.controller"))
                .paths(paths())
                .build()
                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET, serverErrorResponseMessages())
//                .globalResponseMessage(RequestMethod.POST, responseMessages())
//                .globalResponseMessage(RequestMethod.PUT, responseMessages())
//                .globalResponseMessage(RequestMethod.DELETE, serverErrorResponseMessages())
                .apiInfo(apiInfo());
    }


    private List<ResponseMessage> serverErrorResponseMessages() {
        List<ResponseMessage> messages = new ArrayList<>();
        createNotValid(messages, 500, "Internal Server Error", "ServerErrorResponseDto");

        return messages;
    }

    private List<ResponseMessage> responseMessages() {
        List<ResponseMessage> messages = new ArrayList<>();
        createNotValid(messages, 500, "Internal Server Error", "ServerErrorResponseDto");

        createNotValid(messages, 400, "ArgumentNotValid Error", "NotValidErrorResponseDto");

        return messages;
    }

    private void createNotValid(List<ResponseMessage> messages, int code, String message, String reference) {
        messages.add(new ResponseMessageBuilder()
                .code(code)
                .message(message)
                .responseModel(new ModelRef(reference))
                .build());
    }

    private Predicate<String> paths() {
        return or(containsPattern("/*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("공통서버 APIs")
                .description("공통 서버 API 입니다.")
                .version("0.1")
                .build();
    }

}
