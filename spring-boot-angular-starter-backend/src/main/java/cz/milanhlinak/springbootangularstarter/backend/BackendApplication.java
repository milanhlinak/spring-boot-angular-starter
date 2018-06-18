package cz.milanhlinak.springbootangularstarter.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Configuration
    @EnableConfigurationProperties({ResourceProperties.class})
    public class WebMvcConfig implements WebMvcConfigurer {

        @Autowired
        private ResourceProperties resourceProperties = new ResourceProperties();

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            long cachePeriod = resourceProperties.getCache().getPeriod().getSeconds();

            final String[] staticLocations = resourceProperties.getStaticLocations();
            final String[] indexLocations = new String[staticLocations.length];
            for (int i = 0; i < staticLocations.length; i++) {
                indexLocations[i] = staticLocations[i] + "index.html";
            }

            registry.addResourceHandler(
                    "/**/*.css",
                    "/**/*.js")
                    .addResourceLocations(staticLocations)
                    .setCacheControl(CacheControl.maxAge(cachePeriod, TimeUnit.SECONDS));

            registry.addResourceHandler(
                    "/**/*.html",
                    "/**/*.json",
                    "/**/*.bmp",
                    "/**/*.jpeg",
                    "/**/*.jpg",
                    "/**/*.png",
                    "/**/*.ttf",
                    "/**/*.svg")
                    .addResourceLocations(staticLocations)
                    .setCacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));

            registry.addResourceHandler("/**")
                    .addResourceLocations(indexLocations)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver() {
                        @Override
                        protected Resource getResource(String resourcePath, Resource location) {
                            return location.exists() && location.isReadable() ? location : null;
                        }
                    });
        }
    }
}
