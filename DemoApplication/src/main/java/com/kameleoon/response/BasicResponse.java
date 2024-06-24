package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponse {
    public String visitorCode = "";
    public String variationKey = "";
}
