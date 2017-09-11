package com.home.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BarcodeReq.
 */
@Entity
@Table(name = "barcode_req")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BarcodeReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "govid")
    private String govid;

    @Column(name = "office_id")
    private String officeId;

    @OneToMany(mappedBy = "req")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BarcodeReqReader> barcodeReqReaders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public BarcodeReq requestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getGovid() {
        return govid;
    }

    public BarcodeReq govid(String govid) {
        this.govid = govid;
        return this;
    }

    public void setGovid(String govid) {
        this.govid = govid;
    }

    public String getOfficeId() {
        return officeId;
    }

    public BarcodeReq officeId(String officeId) {
        this.officeId = officeId;
        return this;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Set<BarcodeReqReader> getBarcodeReqReaders() {
        return barcodeReqReaders;
    }

    public BarcodeReq barcodeReqReaders(Set<BarcodeReqReader> barcodeReqReaders) {
        this.barcodeReqReaders = barcodeReqReaders;
        return this;
    }

    public BarcodeReq addBarcodeReqReader(BarcodeReqReader barcodeReqReader) {
        this.barcodeReqReaders.add(barcodeReqReader);
        barcodeReqReader.setReq(this);
        return this;
    }

    public BarcodeReq removeBarcodeReqReader(BarcodeReqReader barcodeReqReader) {
        this.barcodeReqReaders.remove(barcodeReqReader);
        barcodeReqReader.setReq(null);
        return this;
    }

    public void setBarcodeReqReaders(Set<BarcodeReqReader> barcodeReqReaders) {
        this.barcodeReqReaders = barcodeReqReaders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BarcodeReq barcodeReq = (BarcodeReq) o;
        if (barcodeReq.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeReq.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeReq{" +
            "id=" + getId() +
            ", requestDate='" + getRequestDate() + "'" +
            ", govid='" + getGovid() + "'" +
            ", officeId='" + getOfficeId() + "'" +
            "}";
    }
}
