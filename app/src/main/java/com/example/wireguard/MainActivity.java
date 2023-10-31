package com.example.wireguard;

import static com.wireguard.android.backend.Tunnel.State.UP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View v) {
        WgTunnel tunnel = new WgTunnel();
        Intent intentPrepare = GoBackend.VpnService.prepare(this);
        if(intentPrepare != null) {
            startActivityForResult(intentPrepare, 0);
        }
        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();
        Backend backend = new GoBackend(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    backend.setState(tunnel, UP, new Config.Builder()
                            .setInterface(interfaceBuilder
                                    .addAddress(InetNetwork.parse("10.8.0.8/24"))
                                    .addDnsServer(InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }))
                                    .parsePrivateKey("cI1p4cbGTwxuELeMFlgkZmHYywFtstpoJuvl115zy18=")
                                    .build())
                            .addPeer(peerBuilder.addAllowedIp(InetNetwork
                                    .parse("0.0.0.0/0"))
                                    .setEndpoint(InetEndpoint.parse("85.193.92.118:51820"))
                                    .parsePublicKey("TQ6LoqVmP4HMBPEHsqLZxBTB9zPGrw6/Usx+6R/taFM=")
                                    .parsePreSharedKey("IUPUAJiuPowrF8JRZwIl4bQY7PCzEqFsZok+3aSVEJM=")
                                    .parseAllowedIPs("0.0.0.0/0")
                                    .parsePersistentKeepalive("0")
                                    .build())
                            .build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}