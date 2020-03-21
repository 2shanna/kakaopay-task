package com.kakaopay.greentour.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REGION")
public class Region implements Serializable {

    private static final long serialVersionUID = -3994514172363093659L;

    @Id
    @GenericGenerator(name = "REGION_CD", strategy = "com.kakaopay.greentour.domain.generator.RegionCdGenerator")
    @GeneratedValue(generator = "REGION_CD")
    @Column(name = "REGION_CD")
    private String regionCd;

    @NotEmpty
    @Column(name = "REGION_NAME", nullable = false, unique = true)
    private String regionName;

    @Column(name = "REGION_1DEPTH_NAME")
    private String region1DepthName;

    @Column(name = "REGION_2DEPTH_NAME")
    private String region2DepthName;

    @Column(name = "REGION_3DEPTH_NAME")
    private String region3DepthName;

    @OneToMany(mappedBy = "region")
    private List<GreenTour> greenTours = new ArrayList<>();

    public Region(String regionName) {
        this.regionName = regionName;
    }

    public Region(String regionName, String region1DepthName, String region2DepthName, String region3DepthName) {
        this.regionName = regionName;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
    }
}
