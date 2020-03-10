package bo.com.ahosoft.visit.service.util;

import bo.com.ahosoft.visit.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FileUtilService {

    private final Logger log = LoggerFactory.getLogger(FileUtilService.class);

    private final ApplicationProperties applicationProperties;

    /* =============================== ADDITIONAL METHODS =============================== */

    /**
     * Create if does not exist directory
     *
     * @param path Path to create
     * @return true if created
     */
    public boolean createIfNotExistDirectory(String path) {
        File directory = new File(path);
        return directory.isDirectory() || directory.mkdirs();
    }

    public  void createOrUpdateFile(String fileName, byte[] bytes) throws IOException {
        boolean created = createIfNotExistDirectory(applicationProperties.getPathFile());
        if (created)
            Files.write(Paths.get(applicationProperties.getPathFile().concat("/").concat(fileName)), bytes);
        else
            throw new IOException("Error create folder");
    }

    public String readArchive(String fileName) throws IOException {
        String directoryInvoice = applicationProperties.getPathFile();
        String filePath = directoryInvoice.concat("/").concat(fileName);
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();
    }

}
