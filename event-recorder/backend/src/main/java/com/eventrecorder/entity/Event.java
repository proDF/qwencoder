package com.eventrecorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@TableName("events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField
    private String title;
    
    @TableField
    private String content;
    
    @TableField
    private String category;
    
    @TableField
    private LocalDateTime eventDate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
