package com.ts.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.controller
 * @Author: ts
 * @CreateTime: 2022-11-30  14:37
 * @Description: TODO
 * @Version: 1.0
*/
@Controller
public class MusicShowController {
    @GetMapping("/music")
    public String music() {
        return "music";
    }

}

