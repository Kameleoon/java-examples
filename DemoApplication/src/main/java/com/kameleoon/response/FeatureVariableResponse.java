package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureVariableResponse {
    public String visitorCode = "";
    public Object variable = null;
}
