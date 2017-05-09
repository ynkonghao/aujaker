package org.konghao.aujaker.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class FileClearSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		System.out.println("session create");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		//在这里删除文件
		HttpSession session = sessionEvent.getSession();
		System.out.println(session.getId()+"----------------delete");
	}

}
