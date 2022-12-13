package portfolio.quizapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import portfolio.quizapp.application.auth.token.JwtProvider;
import portfolio.quizapp.presentation.UserArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

    private final List<HandlerInterceptor> interceptors;

    public WebConfig(JwtProvider jwtProvider, List<HandlerInterceptor> interceptors) {
        this.jwtProvider = jwtProvider;
        this.interceptors = interceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver(jwtProvider));
    }
}
