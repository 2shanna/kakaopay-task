package com.kakaopay.greentour.service;

import com.kakaopay.greentour.dto.CsvInfoDto;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadService {

    public List<CsvInfoDto> store(MultipartFile file) {
        // save file temporally
        File dir = new File("tmp");
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException ex) {
                ex.printStackTrace();     //  TODO
            }
        }
        dir.mkdirs();
        File tmpFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        try (InputStream is = file.getInputStream();
             BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tmpFile))) {
            int i;
            while ((i = is.read()) != -1) {
                stream.write(i);
            }
            stream.flush();
        } catch (IOException e) {
            e.getStackTrace();          //  TODO
        }

        // read saved file
        List<CsvInfoDto> CsvInfoDtoList = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(tmpFile.toPath(),
                StandardCharsets.UTF_8)) {

            // map file to dto
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(CsvInfoDto.class);
            String[] fields = {"programId", "prgmName", "theme", "region", "outline", "detail"};
            strategy.setColumnMapping(fields);

            br.readLine();      //  skip header

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(CsvInfoDto.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            CsvInfoDtoList = csvToBean.parse();

        } catch (Exception e) {
            e.getStackTrace();          // TODO
        }

        return CsvInfoDtoList;
    }
}