package com.home.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.home.app.domain.enumeration.BarcodeReqType;

/**
 * A BarcodeReqLocation.
 */
@Entity
@Table(name = "barcode_req_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BarcodeReqLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_rpc")
    private String sectionRpc;

    @Enumerated(EnumType.STRING)
    @Column(name = "req_type")
    private BarcodeReqType reqType;

    @ManyToOne
    private BarcodeReqReader barcodeReqReader;

    @OneToMany(mappedBy = "loc")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BarcodeReqLocDet> barcodeReqLocDets = new HashSet<>();

    @OneToMany(mappedBy = "loc")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BarcodeReqFile> barcodeReqFiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionRpc() {
        return sectionRpc;
    }

    public BarcodeReqLocation sectionRpc(String sectionRpc) {
        this.sectionRpc = sectionRpc;
        return this;
    }

    public void setSectionRpc(String sectionRpc) {
        this.sectionRpc = sectionRpc;
    }

    public BarcodeReqType getReqType() {
        return reqType;
    }

    public BarcodeReqLocation reqType(BarcodeReqType reqType) {
        this.reqType = reqType;
        return this;
    }

    public void setReqType(BarcodeReqType reqType) {
        this.reqType = reqType;
    }

    public BarcodeReqReader getBarcodeReqReader() {
        return barcodeReqReader;
    }

    public BarcodeReqLocation barcodeReqReader(BarcodeReqReader barcodeReqReader) {
        this.barcodeReqReader = barcodeReqReader;
        return this;
    }

    public void setBarcodeReqReader(BarcodeReqReader barcodeReqReader) {
        this.barcodeReqReader = barcodeReqReader;
    }

    public Set<BarcodeReqLocDet> getBarcodeReqLocDets() {
        return barcodeReqLocDets;
    }

    public BarcodeReqLocation barcodeReqLocDets(Set<BarcodeReqLocDet> barcodeReqLocDets) {
        this.barcodeReqLocDets = barcodeReqLocDets;
        return this;
    }

    public BarcodeReqLocation addBarcodeReqLocDet(BarcodeReqLocDet barcodeReqLocDet) {
        this.barcodeReqLocDets.add(barcodeReqLocDet);
        barcodeReqLocDet.setLoc(this);
        return this;
    }

    public BarcodeReqLocation removeBarcodeReqLocDet(BarcodeReqLocDet barcodeReqLocDet) {
        this.barcodeReqLocDets.remove(barcodeReqLocDet);
        barcodeReqLocDet.setLoc(null);
        return this;
    }

    public void setBarcodeReqLocDets(Set<BarcodeReqLocDet> barcodeReqLocDets) {
        this.barcodeReqLocDets = barcodeReqLocDets;
    }

    public Set<BarcodeReqFile> getBarcodeReqFiles() {
        return barcodeReqFiles;
    }

    public BarcodeReqLocation barcodeReqFiles(Set<BarcodeReqFile> barcodeReqFiles) {
        this.barcodeReqFiles = barcodeReqFiles;
        return this;
    }

    public BarcodeReqLocation addBarcodeReqFile(BarcodeReqFile barcodeReqFile) {
        this.barcodeReqFiles.add(barcodeReqFile);
        barcodeReqFile.setLoc(this);
        return this;
    }

    public BarcodeReqLocation removeBarcodeReqFile(BarcodeReqFile barcodeReqFile) {
        this.barcodeReqFiles.remove(barcodeReqFile);
        barcodeReqFile.setLoc(null);
        return this;
    }

    public void setBarcodeReqFiles(Set<BarcodeReqFile> barcodeReqFiles) {
        this.barcodeReqFiles = barcodeReqFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BarcodeReqLocation barcodeReqLocation = (BarcodeReqLocation) o;
        if (barcodeReqLocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeReqLocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeReqLocation{" +
            "id=" + getId() +
            ", sectionRpc='" + getSectionRpc() + "'" +
            ", reqType='" + getReqType() + "'" +
            "}";
    }
}
