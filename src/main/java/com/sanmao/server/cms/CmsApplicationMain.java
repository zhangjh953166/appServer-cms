package com.sanmao.server.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

@SpringBootApplication
public class CmsApplicationMain {

	public static void main(String[] args) {
		 SpringApplication.run(CmsApplicationMain.class, args);
	}

}
