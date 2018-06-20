package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authorization {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("providerUuid")
    @Expose
    private String providerUuid;
    @SerializedName("authenticated")
    @Expose
    private boolean authenticated;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "accessToken='" + accessToken + '\'' +
                ", providerUuid='" + providerUuid + '\'' +
                ", authenticated=" + authenticated +
                '}';
    }

    public String getProviderUuid() {
        return providerUuid;
    }

    public void setProviderUuid(String providerUuid) {
        this.providerUuid = providerUuid;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

}
