package com.ts.blog.service;

import com.ts.blog.po.Type;
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
public interface TypeService {

    Type saveType(Type type);

    void deleteType(Long id);

    Optional<Type> getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type);

    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);


}


