package com.hongwei.android_refactor_examples.example.hugeclass;

public class DownloadResourceServiceB {
    public void downloadResource(ModuleData moduleData, String exeType) {
        if (!checkInput(moduleData, exeType)) {
            return;
        }

        if (!checkNoUpdate()) {
            return;
        }

        List<ResourceInfo> list = generateResourceList();

        normaliseResourceStoragePath();

        download();
    }
}

