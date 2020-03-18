package com.kakaopay.greentour.util;

import com.kakaopay.greentour.dto.CsvInfoDto;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtil {

    public List<CsvInfoDto> readAndParseToDto(MultipartFile file) throws Exception {
        String fileName = file.getName();

        List<CsvInfoDto> CsvInfoDtoList = new ArrayList<>();

        log.debug("[start] read file : " + fileName);
        try (InputStream is = file.getInputStream()) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            csvReader.readNext();       // header skip
            log.debug("[success] read file : " + fileName);

            log.debug("[start] parse into dto");
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(CsvInfoDto.class);
            String[] fields = {"programId", "programName", "theme", "region", "outline", "detail"};
            strategy.setColumnMapping(fields);

            CsvToBean csvToBean = new CsvToBeanBuilder(csvReader)
                    .withType(CsvInfoDto.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            CsvInfoDtoList = csvToBean.parse();
            log.debug("[success] parse into dto");
        } catch (IOException e) {
            throw new Exception("[fail] read and parse csv file : " + fileName, e);
        }

        return CsvInfoDtoList;
    }
}
