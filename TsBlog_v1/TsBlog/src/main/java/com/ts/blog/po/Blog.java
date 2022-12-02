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
 * @CreateTime: 2022-11-21  21:27
 * @Description: TODO
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_blog")
public class Blog {
    @Id
    @GeneratedValue
    private Long id;
    //标题
    private String title;
    //内容
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    //首图
    private String firstPicture;
    //标记
    private String flag;
    //浏览次数
    private Integer views;
    //赞赏开关
    private Boolean appreciation;
    //版权开启
    private Boolean shareStatement;
    //评论开启
    private Boolean commentabled;
    //发布
    private Boolean published;
    //推荐
    private Boolean recommend;
    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTile;
    //更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }


    @ManyToOne
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @Transient
    private String tagIds;

    //描述
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();




}


