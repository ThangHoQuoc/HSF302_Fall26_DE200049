package com.hsf302.student.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Majors")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String code;

    @Nationalized
    @Column(nullable = false, length = 100)
    private String name;

    @Nationalized
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    public Major() {
    }

    public Major(String code, String name, String nameEn) {
        this.code = code;
        this.name = name;
        this.nameEn = nameEn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getLocalizedName(String language) {
        return "en".equals(language) ? nameEn : name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof Major major && getId() != null && getId().equals(major.getId());
    }

    @Override
    public int hashCode() {
        return Major.class.hashCode();
    }
}
