package com.java.app.usermgmt.model;

public enum ToastType {
    SUCCESS("text-success", "fa-solid fa-circle-check" ),
    INFO("text-secondary", "fa-solid fa-circle-info"),
    ERROR("text-danger", "fa-solid fa-circle-xmark");

    public final String colorClass;
    public final String iconClass;

    ToastType(String colorClass, String iconClass) {
        this.colorClass = colorClass;
        this.iconClass = iconClass;
    }

    public String getColor(){
        return colorClass;
    }

    public String getIcon(){
        return iconClass;
    }

}

