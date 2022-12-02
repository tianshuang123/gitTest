package com.ts.blog.controller;

import com.ts.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.controller
 * @Author: ts
 * @CreateTime: 2022-11-29  21:03
 * @Description: TODO
 * @Version: 1.0
*/

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){

        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.countBlog());
        return "archives";
    }

}

