package melon.web.config;

import melon.web.service.MovieService;
import melon.web.service.impl.MovieServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("melon.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Learning Api")
                .description("java-learning")
                .termsOfServiceUrl("https://xiaoheiah.github.io/")
                .version("1.0")
                .contact(new Contact("xiaoheiah","https://xiaoheiah.github.io","hdzee19@gmail.com"))
                .build();
    }

    @Bean(value = "service1")
    public MovieService init1(){
        return new MovieServiceImpl();
    }
    @Bean(value = "service2")
    public MovieService init2(){
        return new MovieServiceImpl();
    }
}
