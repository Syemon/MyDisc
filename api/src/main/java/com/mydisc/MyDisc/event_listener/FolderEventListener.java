package com.mydisc.MyDisc.event_listener;

import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.helper.AutowireHelper;
import com.mydisc.MyDisc.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

@Component
public class FolderEventListener {
    @Autowired
    private FileService fileService;

    @PreRemove
    public void onFolderDelete(Folder folder) {
        AutowireHelper.autowire(this, this.fileService);
        fileService.deleteAllInFolder(folder.getId());
    }
}
