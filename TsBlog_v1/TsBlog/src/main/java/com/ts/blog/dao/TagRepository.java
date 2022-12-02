package com.ts.blog.dao;

import com.ts.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag save(Tag Tag);

    void deleteById(Long id);

    Optional<Tag> findById(Long id);

    //Tag update(Long id,Tag Tag);

    Page<Tag> findAll(Pageable pageable);

    Tag findByName(String name);
    List<Tag> findAll();


    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
   // List<Tag> findAllById(List list);

  

}
