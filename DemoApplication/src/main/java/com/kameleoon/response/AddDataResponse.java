package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddDataResponse {
    public String visitorCode = "";
    public String result = "";
}
