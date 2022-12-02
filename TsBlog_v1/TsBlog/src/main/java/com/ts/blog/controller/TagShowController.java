package com.ts.blog.controller;

import com.ts.blog.po.Tag;
import com.ts.blog.service.BlogService;
import com.ts.blog.service.TagService;
import com.ts.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.controller
 * @Author: ts
 * @CreateTime: 2022-11-29  18:27
 * @Description: TODO
 * @Version: 1.0
*/

@Controller
public class TagShowController {
    @Autowired
    private TagService TagService;

    @Autowired
    private BlogService blogService;


    @GetMapping("/tags/{id}")
    public String Tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Tag> tags = TagService.listTagTop(100000);
        if (id == -1) {
            //导航点进来
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }






}



