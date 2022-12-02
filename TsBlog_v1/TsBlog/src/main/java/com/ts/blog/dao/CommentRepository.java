package com.ts.blog.dao;


import com.ts.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.dao
 * @Author: ts
 * @CreateTime: 2022-11-28  19:48
 * @Description: TODO
 * @Version: 1.0
*/
public interface CommentRepository extends JpaRepository<Comment,Long> {


    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);



}

