package com.eventrecorder.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eventrecorder.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface EventMapper extends BaseMapper<Event> {
    
    @Select("SELECT * FROM events WHERE event_date BETWEEN #{startDate} AND #{endDate}")
    List<Event> findByEventDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Select("SELECT * FROM events WHERE title LIKE CONCAT('%', #{title}, '%') OR content LIKE CONCAT('%', #{content}, '%')")
    List<Event> findByTitleContainingOrContentContaining(@Param("title") String title, @Param("content") String content);
    
    @Select("SELECT * FROM events WHERE YEAR(event_date) = #{year} AND MONTH(event_date) = #{month}")
    List<Event> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    @Select("SELECT * FROM events WHERE YEAR(event_date) = #{year}")
    List<Event> findByYear(@Param("year") int year);
    
    @Select("SELECT * FROM events WHERE category = #{category}")
    List<Event> findByCategory(@Param("category") String category);
}
