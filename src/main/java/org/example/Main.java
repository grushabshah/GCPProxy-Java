package org.example;
import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws Exception {

        String PROJECT_ID = System.getenv("PROJECT_ID");
        // Authenticate with GCP (replace with your preferred authentication method)
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(Collections.singleton(StorageScopes.DEVSTORAGE_READ_ONLY));
        HttpTransport httpTransport = newProxyTransport();
        Storage storage = new Storage.Builder(httpTransport, GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName("testapp")
                .build();

        // Now you can use the storage client to interact with Google Cloud Storage
        // For example, to list buckets:
        Storage.Buckets.List listRequest = storage.buckets().list(PROJECT_ID);
        System.out.println("Buckets:");
        listRequest.execute().getItems().forEach(bucket -> System.out.println(bucket.getName()));
        System.out.println("Hello world!");
    }


    static HttpTransport newProxyTransport() throws GeneralSecurityException, IOException {
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
        builder.trustCertificates(GoogleUtils.getCertificateTrustStore());
        builder.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)));
        return builder.build();
    }

}