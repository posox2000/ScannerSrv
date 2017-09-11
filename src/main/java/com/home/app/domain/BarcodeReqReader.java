package com.home.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BarcodeReqReader.
 */
@Entity
@Table(name = "barcode_req_reader")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BarcodeReqReader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reader_name")
    private String readerName;

    @OneToMany(mappedBy = "barcodeReqReader")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BarcodeReqLocation> locs = new HashSet<>();

    @ManyToOne
    private BarcodeReq req;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaderName() {
        return readerName;
    }

    public BarcodeReqReader readerName(String readerName) {
        this.readerName = readerName;
        return this;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public Set<BarcodeReqLocation> getLocs() {
        return locs;
    }

    public BarcodeReqReader locs(Set<BarcodeReqLocation> barcodeReqLocations) {
        this.locs = barcodeReqLocations;
        return this;
    }

    public BarcodeReqReader addLoc(BarcodeReqLocation barcodeReqLocation) {
        this.locs.add(barcodeReqLocation);
        barcodeReqLocation.setBarcodeReqReader(this);
        return this;
    }

    public BarcodeReqReader removeLoc(BarcodeReqLocation barcodeReqLocation) {
        this.locs.remove(barcodeReqLocation);
        barcodeReqLocation.setBarcodeReqReader(null);
        return this;
    }

    public void setLocs(Set<BarcodeReqLocation> barcodeReqLocations) {
        this.locs = barcodeReqLocations;
    }

    public BarcodeReq getReq() {
        return req;
    }

    public BarcodeReqReader req(BarcodeReq barcodeReq) {
        this.req = barcodeReq;
        return this;
    }

    public void setReq(BarcodeReq barcodeReq) {
        this.req = barcodeReq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BarcodeReqReader barcodeReqReader = (BarcodeReqReader) o;
        if (barcodeReqReader.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeReqReader.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeReqReader{" +
            "id=" + getId() +
            ", readerName='" + getReaderName() + "'" +
            "}";
    }
}
