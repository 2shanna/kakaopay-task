package com.kakaopay.greentour.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "PROGRAM")
public class Program implements Serializable {

    private static final long serialVersionUID = 1622016271583550063L;

    @Id
    @Column(name = "PROGRAM_ID")
    private long programId;

    @Column(name = "PROGRAM_NAME", nullable = false)
    private String programName;

    @Column(name = "THEME")
    private String theme;

    @Column(name = "ORIGINAL_REGION")
    private String originalRegion;

    @Column(name = "EDITED_REGION")
    private String editedRegion;

    @Column(name = "OUTLINE")
    private String outline;

    @Column(name = "DETAIL", columnDefinition = "LONGTEXT")
    private String detail;

    @OneToMany(mappedBy = "program")
    private List<GreenTour> greenTours = new ArrayList<>();

    public Program(long programId, String programName, String theme,
                   String originalRegion, String editedRegion, String outline, String detail) {
        this.programId = programId;
        this.programName = programName;
        this.theme = theme;
        this.originalRegion = originalRegion;
        this.editedRegion = editedRegion;
        this.outline = outline;
        this.detail = detail;
    }
}
