package com.ts.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.po
 * @Author: ts
 * @CreateTime: 2022-11-21  21:43
 * @Description: TODO
 * @Version: 1.0*/


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_tag")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs =  new ArrayList<>();
}


