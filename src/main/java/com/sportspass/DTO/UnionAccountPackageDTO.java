package com.sportspass.DTO;

import lombok.Data;

import java.util.Date;


@Data
public class UnionAccountPackageDTO {

        private String packageName;
        private int numberOfEntries;
        private double price;
        private Date datePurchase;
        private Date dateExpiry;
        private String message = "";

}
