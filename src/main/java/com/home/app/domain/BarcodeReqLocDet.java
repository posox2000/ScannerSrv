package com.home.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BarcodeReqLocDet.
 */
@Entity
@Table(name = "barcode_req_loc_det")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BarcodeReqLocDet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loc_attr_one")
    private String locAttrOne;

    @ManyToOne
    private BarcodeReqLocation loc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocAttrOne() {
        return locAttrOne;
    }

    public BarcodeReqLocDet locAttrOne(String locAttrOne) {
        this.locAttrOne = locAttrOne;
        return this;
    }

    public void setLocAttrOne(String locAttrOne) {
        this.locAttrOne = locAttrOne;
    }

    public BarcodeReqLocation getLoc() {
        return loc;
    }

    public BarcodeReqLocDet loc(BarcodeReqLocation barcodeReqLocation) {
        this.loc = barcodeReqLocation;
        return this;
    }

    public void setLoc(BarcodeReqLocation barcodeReqLocation) {
        this.loc = barcodeReqLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BarcodeReqLocDet barcodeReqLocDet = (BarcodeReqLocDet) o;
        if (barcodeReqLocDet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeReqLocDet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeReqLocDet{" +
            "id=" + getId() +
            ", locAttrOne='" + getLocAttrOne() + "'" +
            "}";
    }
}
