package com.kakaopay.greentour.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "Region")
public class Region implements Serializable {

    private static final long serialVersionUID = -3994514172363093659L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_cd")
    private String regionCd;

    @Column(name = "region_name", nullable = false)
    private String regionName;


    public Region (String regionName) {
        this.regionName = regionName;
    }
//    @ManyToMany
//    private List<Program> regions;

}
