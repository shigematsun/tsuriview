package com.example.tsuriview;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * 対応しないURLの場合、固定ページを返す。
 */
@RequiredArgsConstructor
@Configuration
public class Html5HistoryModeResourceConfig implements WebMvcConfigurer {

    private final WebProperties webProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") // 全パスをこのリソースハンドラーの処理対象にする
                .addResourceLocations(webProperties.getResources().getStaticLocations()) // 静的リソース配置先のパスを指定する
                .resourceChain(webProperties.getResources().getChain().isCache()) // 開発時はfalse、本番はtrueが望ましい。trueにしておくとメモリ上にキャッシュされるためI/Oが軽減される
                .addResolver(new SpaPageResourceResolver()); // 拡張したPathResourceResolverを読み込ませる
    }

    public static class SpaPageResourceResolver extends PathResourceResolver {
        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location); // まずはPathResourceResolverで静的リソースを取得する
            return resource != null ? resource : super.getResource("/vue/index.html", location); // 取得できなかった場合は、index.htmlを返す
        }
    }
}
