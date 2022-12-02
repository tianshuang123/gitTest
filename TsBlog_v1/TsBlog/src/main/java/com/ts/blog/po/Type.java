package com.ts.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.po
 * @Author: ts
 * @CreateTime: 2022-11-21  21:41
 * @Description: TODO
 * @Version: 1.0
*/


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_type")
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;


    @OneToMany(mappedBy = "type")
    private List<Blog> blogs = new ArrayList<>();
}

