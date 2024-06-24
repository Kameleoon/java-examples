package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EngineTrackingCodeResponse {
    public String visitorCode = "";
    public String variationKey = "";
    public String trackingCode = "";
}
