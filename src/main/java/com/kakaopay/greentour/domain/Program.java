package com.kakaopay.greentour.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "Program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1622016271583550063L;

    @Id
    @Column(name = "program_id")
    private long programId;

    @Column(name = "program_name", nullable = false)
    private String programName;

    @Column(name = "theme")
    private String theme;

//    @ManyToMany
//    private List<Region> regions;
    @Column(name = "whole_region")
    private String wholeRegion;

    @Column(name = "outline")
    private String outline;

    @Column(name = "detail", columnDefinition = "LONGTEXT")
    private String detail;
}
