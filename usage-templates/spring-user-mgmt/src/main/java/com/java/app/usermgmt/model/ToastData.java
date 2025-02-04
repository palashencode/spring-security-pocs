package com.java.app.usermgmt.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToastData {
    private String time;
    private String headMsg;
    private String bodyMsg;
    private ToastType type;

    public static ToastData justLoggedIn(String firstName){
        return success("Just Now", "Login Success"
                    , "Welcome " + firstName + ", You are logged In!");
    }

    public static List<ToastData> toastLists(ToastData... toasts){
        return Arrays.asList(toasts);
    }

    public static ToastData success(String time, String head, String body){
        return success(time, head, body, ToastType.SUCCESS);
    }

    public static ToastData info(String time, String head, String body){
        return success(time, head, body, ToastType.INFO);
    }

    public static ToastData error(String time, String head, String body){
        return success(time, head, body, ToastType.ERROR);
    }

    private static ToastData success(String time, String head, String body, ToastType type){
        return ToastData.builder()
                .time(time)
                .headMsg(head)
                .bodyMsg(body)
                .type(type)
                .build();
    }

}
