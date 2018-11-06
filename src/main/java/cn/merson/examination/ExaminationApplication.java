package cn.merson.examination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
//@Configuration
public class ExaminationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExaminationApplication.class, args);
	}

	/**
	 * 文件上传大小配置
	 * @return
	 */
	/*@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();

		//设置单个文件最大50M
		String singleFileSize = "10MB";
		factory.setMaxFileSize(singleFileSize);
		//如果是上传多个文件
		String totalFileSzie = "100MB";
		factory.setMaxRequestSize(totalFileSzie);
		return factory.createMultipartConfig();
	}*/
}
