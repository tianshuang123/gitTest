package com.ts.blog.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.po
 * @Author: ts
 * @CreateTime: 2022-11-21  21:47
 * @Description: TODO
 * @Version: 1.0
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Integer type;
    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTile;
    //更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @OneToMany(mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();



}
