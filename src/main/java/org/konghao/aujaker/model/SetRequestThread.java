package org.konghao.aujaker.model;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class SetRequestThread implements Runnable {
	HttpServletRequest req;
	
	public SetRequestThread(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public void run() {
		try {
			List<String> lists = (List<String>)req.getAttribute("lists");
			if(lists==null) {
				lists = new ArrayList<String>();
				req.setAttribute("lists", lists);
			}
			for(int i=0;i<100;i++) {
				lists.add(i+"--");
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
