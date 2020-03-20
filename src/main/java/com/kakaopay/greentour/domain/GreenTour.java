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
@Table(name = "GREEN_TOUR")
public class GreenTour implements Serializable {

    private static final long serialVersionUID = -6997980982011236287L;

    @Id
    @GeneratedValue
    @Column(name = "GREEN_TOUR_ID")
    private Long greenTourId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "REGION_CD")
    private Region region;


    public GreenTour(Program program, Region region) {
        this.region = region;
        this.program = program;
    }
}
