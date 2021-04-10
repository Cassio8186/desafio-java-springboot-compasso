package compasso.test.springboot.catalogoprodutos.config;

import com.fasterxml.classmate.TypeResolver;
import compasso.test.springboot.catalogoprodutos.model.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
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

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	private static final String BASE_PACKAGE = "compasso.test.springboot.catalogoprodutos";
	public static final String INTERNAL_ERROR = "Internal Error";
	public static final String ERROR_DTO = "ErrorDTO";
	public static final String NOT_FOUND = "Not Found";
	public static final String BAD_REQUEST = "Bad Request";
	private final String appVersion;

	public SwaggerConfig(@Value("${app.version}") String appVersion) {
		this.appVersion = appVersion;
	}


	@Bean
	public Docket productApi() {
		final TypeResolver typeResolver = new TypeResolver();
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, getResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, putResponseMessages())
				.globalResponseMessage(RequestMethod.POST, postResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, deleteResponseMessages())
				.additionalModels(typeResolver.resolve(ErrorDTO.class))
				.apiInfo(this.metaData());

	}

	private List<ResponseMessage> getResponseMessages() {
		final List<ResponseMessage> responseMessages = new ArrayList<>();
		final ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();

		responseMessages.add(responseMessageBuilder
				.code(500)
				.message(INTERNAL_ERROR)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());
		return responseMessages;
	}

	private List<ResponseMessage> putResponseMessages() {
		final List<ResponseMessage> responseMessages = new ArrayList<>();
		final ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();

		responseMessages.add(responseMessageBuilder
				.code(500)
				.message(INTERNAL_ERROR)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		responseMessages.add(responseMessageBuilder
				.code(404)
				.message(NOT_FOUND)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		responseMessages.add(responseMessageBuilder
				.code(400)
				.message(BAD_REQUEST)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		return responseMessages;
	}

	private List<ResponseMessage> postResponseMessages() {
		final List<ResponseMessage> responseMessages = new ArrayList<>();
		final ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();

		responseMessages.add(responseMessageBuilder
				.code(500)
				.message(INTERNAL_ERROR)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		responseMessages.add(responseMessageBuilder
				.code(400)
				.message(BAD_REQUEST)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		return responseMessages;
	}


	private List<ResponseMessage> deleteResponseMessages() {
		final List<ResponseMessage> responseMessages = new ArrayList<>();
		final ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();

		responseMessages.add(responseMessageBuilder
				.code(500)
				.message(INTERNAL_ERROR)
				.responseModel(new ModelRef(ERROR_DTO))
				.build());

		responseMessages.add(responseMessageBuilder
				.code(404)
				.message(NOT_FOUND)
				.build());

		return responseMessages;
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()
				.title("Catálogo de produtos")
				.description("Neste microserviço deve ser possível criar, alterar, visualizar e excluir um determinado produto, além de visualizar a lista de produtos atuais disponíveis. Também deve ser possível realizar a busca de produtos filtrando por name, description e price.")
				.version(this.appVersion)
				.build();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
