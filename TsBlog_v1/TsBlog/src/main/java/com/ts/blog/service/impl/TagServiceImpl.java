package com.ts.blog.service.impl;

import com.ts.blog.dao.TagRepository;
import com.ts.blog.dao.TagRepository;
import com.ts.blog.exception.MyNotFoundException;
import com.ts.blog.po.Tag;
import com.ts.blog.po.Tag;
import com.ts.blog.service.TagService;
import com.ts.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.service.impl
 * @Author: ts
 * @CreateTime: 2022年11月24日18:31:17
 * @Description: TODO
 * @Version: 1.0
*/
@Service
public class TagServiceImpl implements TagService {


    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag Tag) {
        return tagRepository.save(Tag);
    }

    @Override
    public Optional<Tag> getTag(Long id) {
        return tagRepository.findById(id);
    }


    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) {
        //return tagRepository.findAllTag(converToList(ids));
        return tagRepository.findAllById(converToList(ids));
    }


    private List<Long> converToList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null){
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }


    @Override
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> t = tagRepository.findById(id);
        if (t == null){
            throw  new MyNotFoundException("不存在该类型");
        }

        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t.get());
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }



}

