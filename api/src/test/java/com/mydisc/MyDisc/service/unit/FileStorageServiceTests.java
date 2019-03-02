package com.mydisc.MyDisc.service.unit;

import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.exception.FileStorageException;
import com.mydisc.MyDisc.service.FileStorageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
@AutoConfigureMockMvc()
public class FileStorageServiceTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testStoreFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        Map<String, String> storedFileNames = fileStorageService.storeFile(multipartFile);

        Assert.assertEquals(storedFileNames.get("fileName"), "test.txt");
        Assert.assertTrue(storedFileNames.get("storageFileName").matches("[a-zA-Z0-9]{16}"));
    }

    @org.junit.jupiter.api.Test
    public void testStoreFileWithHiddenPath() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "../test.txt",
                "text/plain", "Spring Framework".getBytes());


        assertThrows(FileStorageException.class, () ->
            this.fileStorageService.storeFile(multipartFile),
                "Sorry! Filename contains invalid path sequence ../test.txt");
    }
}
