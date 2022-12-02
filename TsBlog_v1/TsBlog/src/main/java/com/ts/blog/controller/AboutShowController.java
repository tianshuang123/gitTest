package com.ts.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.controller
 * @Author: ts
 * @CreateTime: 2022-11-29  21:58
 * @Description: TODO
 * @Version: 1.0
*/

@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

