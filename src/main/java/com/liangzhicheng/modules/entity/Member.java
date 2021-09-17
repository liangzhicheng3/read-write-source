package com.liangzhicheng.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member")
public class Member extends Model<Member> {

    private static final long serialVersionUID = 1L;

    /**
     * id(主键)
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    /**
     * 会员名称
     */
    private String name;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
