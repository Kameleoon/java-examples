package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureVariationKeyResponse {
    public String visitorCode = "";
    public String variationKey = "";
}
