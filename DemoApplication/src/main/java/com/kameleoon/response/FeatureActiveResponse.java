package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureActiveResponse {
    public String visitorCode = "";
    public Boolean active;
}
