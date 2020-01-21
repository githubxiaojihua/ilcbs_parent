package com.xiaojihua.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="DEPT_P")
public class Dept implements Serializable {
    @Id
    @Column(name="dept_id")
    //自定义主键生成策略，用hibernate的那六种，默认是四种
    @GeneratedValue(generator="sys-uuid")
    @GenericGenerator(name="sys-uuid",strategy="uuid")
    private String id;     		//部门id

    @Column(name="dept_name")
    private String deptName;  	//部门名称

    /*
        joinColum是用于指定主键和外键的对应关系
        name是外键名称，
        referencedColumnName是外键对应的主表的主键字段名
     */
    @ManyToOne
    @JoinColumn(name="parent_id",referencedColumnName="dept_id")
    private Dept parent;  		//父部门

    @Column(name="state")
    private Integer state;		//状态 0：取消   1：运营

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public Dept getParent() {
        return parent;
    }
    public void setParent(Dept parent) {
        this.parent = parent;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
}
