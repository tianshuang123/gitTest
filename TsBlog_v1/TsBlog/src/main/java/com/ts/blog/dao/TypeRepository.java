package com.ts.blog.dao;

import com.ts.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TypeRepository extends JpaRepository<Type,Long> {

    Type save(Type type);

    void deleteById(Long id);

    Optional<Type> findById(Long id);

    //Type update(Long id,Type type);

    Page<Type> findAll(Pageable pageable);

    Type findByName(String name);

    List<Type> findAll();

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);


}
