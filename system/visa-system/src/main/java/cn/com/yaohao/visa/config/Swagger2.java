/**
 * 
 */
package cn.com.yaohao.visa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author huangjiajun
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

	@Value("${custom.swagger.enable}")
	private boolean enableSwagger;

	// 定义分隔符
	private static final String splitor = ";";

	// swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).enable(enableSwagger).apiInfo(apiInfo()).select()
				// 为当前包路径
				.apis(basePackage("cn.com.yaohao.visa.controller"))
				.paths(PathSelectors.any()).build();
	}

	// 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 页面标题
				.title("签证系统RESTful API")
				// 版本号
				.version("1.0")
				// 描述
				.description("API 描述").build();
	}

	public static Predicate<RequestHandler> basePackage(final String basePackage) {
		return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
	}

	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return input -> {
			// 循环判断匹配
			for (String strPackage : basePackage.split(splitor)) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	@SuppressWarnings("deprecation")
	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.fromNullable(input.declaringClass());
	}

}
