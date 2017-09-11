package com.home.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BarcodeReqFile.
 */
@Entity
@Table(name = "barcode_req_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BarcodeReqFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_number")
    private String fileNumber;

    @ManyToOne
    private BarcodeReqLocation loc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public BarcodeReqFile fileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
        return this;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public BarcodeReqLocation getLoc() {
        return loc;
    }

    public BarcodeReqFile loc(BarcodeReqLocation barcodeReqLocation) {
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
        BarcodeReqFile barcodeReqFile = (BarcodeReqFile) o;
        if (barcodeReqFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeReqFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeReqFile{" +
            "id=" + getId() +
            ", fileNumber='" + getFileNumber() + "'" +
            "}";
    }
}
