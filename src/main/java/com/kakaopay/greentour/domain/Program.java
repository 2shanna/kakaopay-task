package com.kakaopay.greentour.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
    @JsonIgnore
    private String editedRegion;

    @Column(name = "OUTLINE")
    private String outline;

    @Column(name = "DETAIL", columnDefinition = "LONGTEXT")
    private String detail;

    @OneToMany(mappedBy = "program")
    @JsonIgnore
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
