package com.ts.blog.service;

import com.ts.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.service
 * @Author: ts
 * @CreateTime: 2022-11-23  16:00
 * @Description: TODO
 * @Version: 1.0
 * */
public interface TagService {

    Tag saveTag(Tag Tag);

    void deleteTag(Long id);

    Optional<Tag> getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();
    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String ids);

    Tag updateTag(Long id, Tag Tag);

    Tag getTagByName(String name);




}


