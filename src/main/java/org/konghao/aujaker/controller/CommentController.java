package org.konghao.aujaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/9 8:53.
 * 点评
 */
@Controller
@RequestMapping(value = "comment")
public class CommentController {

    @GetMapping(value = {"index", "", "/"})
    public String index() {
        return "comment/index";
    }
}
