package com.ts.blog.service.impl;

import com.ts.blog.dao.TypeRepository;
import com.ts.blog.exception.MyNotFoundException;
import com.ts.blog.po.Type;
import com.ts.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;


/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.service.impl
 * @Author: ts
 * @CreateTime: 2022-11-23  16:04
 * @Description: TODO
 * @Version: 1.0
*/
@Service
public class TypeServiceImpl implements TypeService {


    @Autowired
    private TypeRepository typeRepository;

    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Optional<Type> getType(Long id) {
        return typeRepository.findById(id);
    }


    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }


    @Override
    public Type updateType(Long id, Type type) {
        Optional<Type> t = typeRepository.findById(id);
        if (t == null){
            throw  new MyNotFoundException("不存在该类型");
        }

        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t.get());
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }

    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }



}

