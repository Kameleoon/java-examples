package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AllFlagsResponse {
    public String visitorCode = "";
    public List<FeatureInfo> featuresInfo = new ArrayList<>();
}
