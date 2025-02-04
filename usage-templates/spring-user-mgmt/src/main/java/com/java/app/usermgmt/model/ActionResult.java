package com.java.app.usermgmt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionResult {
    boolean success;
    String message;
}
