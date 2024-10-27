package com.learnway.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

   @Bean
   public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
       return server -> {
           // 기존 HTTP 커넥터 추가
           server.addAdditionalTomcatConnectors(createStandardConnector());
           // HTTPS 커넥터 추가는 application.properties 설정에 의해 자동으로 처리됨
       };
   }

   // HTTP 커넥터 생성
   private Connector createStandardConnector() {
       Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
       connector.setPort(8095);  // HTTP 포트
       return connector;
   }
}