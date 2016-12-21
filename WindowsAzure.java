package controllers;

import com.microsoft.windowsazure.services.blob.client.*;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class WindowsAzure {
    public void setUploadBlob(String blobName, String blobPath, String blobContainer) throws Exception{
    	Account acc = new Account();

        setUploadBlob(acc.getstorageconnectionstring(), blobName, blobPath, blobContainer);
    }

    public void setUploadBlob(String storageConnectionString,  String blobName, String blobPath, String blobContainer) throws Exception{
        CloudStorageAccount account;
        CloudBlobClient serviceClient;
        CloudBlobContainer container;
        CloudBlockBlob blob;

        account = CloudStorageAccount.parse(storageConnectionString);
        serviceClient = account.createCloudBlobClient();
        // Container name must be lower case.
        container = serviceClient.getContainerReference(blobContainer);
        container.createIfNotExist();

        // Set anonymous access on the container.
        BlobContainerPermissions containerPermissions;
        containerPermissions = new BlobContainerPermissions();
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        container.uploadPermissions(containerPermissions);

        // Upload an image file.
        blob = container.getBlockBlobReference(blobName);
        File fileReference = new File (blobPath);
        if(blobName.endsWith(".svg"))
            blob.getProperties().setContentType("image/svg+xml");
        blob.upload(new FileInputStream(fileReference), fileReference.length());
        System.out.println("Processing complete.");
    }

    public void deleteBlob(String storageConnectionString, String containerName, String blobName) throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        CloudBlockBlob blob = container.getBlockBlobReference(blobName);
        blob.delete();
        System.out.println("Processing complete.");
    }
}
