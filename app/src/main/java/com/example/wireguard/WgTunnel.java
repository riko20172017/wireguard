package com.example.wireguard;

import com.wireguard.android.backend.Tunnel;

public class WgTunnel implements Tunnel {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void onStateChange(State newState) {
    }
}