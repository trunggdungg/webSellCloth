package com.example.webshopmenswear.config;

import com.example.webshopmenswear.config.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;

    //Nếu không có session "CURRENT_USER", thì người dùng sẽ nhận mã trạng thái HTTP 401
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns(
                "/api/reviews",
                "/api/reviews/**",
                "/api/blogs/**",
                "/thong-tin-ca-nhan",
                "/sanpham-yeu-thich"
                // Không có api là trả về giao diện, có api là trả về JSON
            );
    }
}
