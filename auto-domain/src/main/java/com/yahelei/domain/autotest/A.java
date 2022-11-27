package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;

@Data
public class A implements Serializable {
        boolean biaoji;
        int id;
        String description;
        String description1;
        String description2;
        public int getid() {
            return id;
        }
        public void setid(int id) {
            this.id = id;
        }
    public String getdescription1() {
        return description1;
    }
    public void setdescription1(String description1) {
        this.description1 = description1;
    }
    public String getdescription2() {
        return description2;
    }
    public void setdescription2(String description2) {
        this.description2 = description2;
    }
        public String getdescription() {
            return description;
        }
        public void setdescription(String description) {
            this.description = description;
        }
        public boolean getbiaoji() {
            return biaoji;
        }
        public void setbiaoji(Boolean biaoji) {
            this.biaoji = biaoji;
        }
}
