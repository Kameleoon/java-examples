package com.kameleoon.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RemoteResponse {
    public String visitorCode = "";
    public Object variable;
    public List<String> customData = new ArrayList<>();
}
