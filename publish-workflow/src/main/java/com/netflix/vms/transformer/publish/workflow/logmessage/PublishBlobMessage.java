package com.netflix.vms.transformer.publish.workflow.logmessage;

import com.netflix.vms.transformer.common.MessageBuilder;

public class PublishBlobMessage {
    private MessageBuilder msgBuilder = new MessageBuilder("Uploaded VMS Blob: ");

    public PublishBlobMessage(String vip, boolean isNostreams, String blobType, long version, long dataVersion, long size, long duration) {
        msgBuilder.put("keybase", (isNostreams ? vip + "_nostreams" : vip) + "." + blobType);
        msgBuilder.put("region", "ALL");
        msgBuilder.put("version", version);
        msgBuilder.put("dataVersion", dataVersion);
        msgBuilder.put("size", size);
        msgBuilder.put("duration", duration + "ms");
    }

    @Override
    public String toString() {
        return msgBuilder.toString();
    }
}
