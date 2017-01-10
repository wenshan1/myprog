package me.wenshan.util;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import me.wenshan.MyprogApplication;
public class MyBeanFactory {
	private static AnnotationConfigApplicationContext appCtx = new AnnotationConfigApplicationContext (MyprogApplication.class);
	/*
	public static <T> T getBean (Class<T> cls) {
		return appCtx.getBean(cls);
	}*/
}
